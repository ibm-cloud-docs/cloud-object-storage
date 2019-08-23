---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}

# Migrando dados do OpenStack Swift
{: #migrate}

Antes de o {{site.data.keyword.cos_full_notm}} ter sido disponibilizado como um serviço {{site.data.keyword.cloud_notm}} Platform, os projetos que requeriam um armazenamento de objeto usavam o [OpenStack Swift](https://docs.openstack.org/swift/latest/) ou [OpenStack Swift (infraestrutura)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift). Recomendamos que os desenvolvedores atualizem seus aplicativos e migrem seus dados para o {{site.data.keyword.cloud_notm}} para aproveitar os novos benefícios de controle de acesso e de criptografia fornecidos pelo IAM e pelo Key Protect, bem como os novos recursos que se tornarem disponíveis.

O conceito de um 'contêiner' do Swift é idêntico a um 'depósito' do COS. O COS limita as instâncias de serviço a 100 depósitos e algumas instâncias do Swift podem ter um número maior de contêineres. Os depósitos do COS podem conter bilhões de objetos e suporta barras (`/`) em nomes de objetos para diretórios, como 'prefixos', ao organizar dados. O COS suporta políticas do IAM nos níveis de depósito e de instância de serviço.
{:tip}

Uma abordagem para migrar dados entre os serviços de armazenamento de objeto é usar uma ferramenta 'sync' ou 'clone', como [o utilitário de linha de comandos `rclone` de software livre](https://rclone.org/docs/). Esse utilitário sincronizará uma árvore de arquivos entre dois locais, incluindo o armazenamento em nuvem. Quando o `rclone` gravar dados no COS, ele usará a API S3/do COS para segmentar objetos grandes e fazer upload das partes em paralelo de acordo com os tamanhos e limites configurados como parâmetros de configuração.

Há algumas diferenças entre COS e Swift que devem ser consideradas como parte da migração de dados.

  - O COS ainda não suporta políticas de expiração ou versão. Os fluxos de trabalho que dependem desses recursos do Swift devem, em vez disso, manipulá-los como parte de sua lógica de aplicativo na migração para o COS.
  - O COS suporta metadados de nível de objeto, mas essas informações não são preservadas ao usar `rclone` para migrar dados. Os metadados customizados podem ser configurados em objetos no COS usando um cabeçalho `x-amz-meta-{key}: {value}`, mas é recomendável que os metadados de nível de objeto sejam submetidos a backup para um banco de dados antes de usar `rclone`. Os metadados customizados podem ser aplicados a objetos existentes [copiando o objeto em si mesmo](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object) - o sistema reconhecerá que os dados do objeto são idênticos e atualizará somente os metadados. Observe que `rclone` **pode** preservar registros de data e hora.
  - O COS usa políticas do IAM para controle de acesso de instância de serviço e de nível do depósito. [Os objetos podem ser disponibilizados publicamente](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access), configurando uma ACL `pública-read`, que elimina a necessidade de um cabeçalho de autorização.
  - [Uploads de múltiplas partes](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) para objetos grandes são manipulados de forma diferente na API S3/do COS relativa à API do Swift.
  - O COS permite cabeçalhos de HTTP opcionais familiares, como `Cache-Control`, `Content-Encoding`, `Content-MD5` e `Content-Type`.

Este guia fornece instruções para migrar dados de um único contêiner do Swift para um único depósito do COS. Isso precisará ser repetido para todos os contêineres que você deseja migrar e, em seguida, a lógica do aplicativo precisará ser atualizada para usar a nova API. Depois que os dados são migrados, é possível verificar a integridade da transferência usando `rclone check`, que comparará as somas de verificação MD5 e produzirá uma lista de quaisquer objetos nos quais elas não correspondem.


## Configurar o {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. Caso ainda não tenha criado uma, provisione uma instância do {{site.data.keyword.cos_full_notm}} por meio do [catálogo](https://cloud.ibm.com/catalog/services/cloud-object-storage).
  2. Crie quaisquer depósitos que você precisará para armazenar seus dados transferidos. Leia o [guia de introdução](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para se familiarizar com os conceitos-chave, como [terminais](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) e [classes de armazenamento](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. Como a sintaxe da API do Swift é significativamente diferente da API S3/do COS, pode ser necessário refatorar seu aplicativo para usar métodos equivalentes fornecidos nos SDKs do COS. As bibliotecas estão disponíveis em [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) ou na [API de REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Configurar um recurso de cálculo para executar a ferramenta de migração
{: #migrate-compute}
  1. Escolha uma máquina Linux/macOS/BSD ou um IBM Cloud Infrastructure Bare Metal ou Virtual Server
     com a melhor proximidade de seus dados.
     A configuração do Servidor a seguir é recomendada: 32 GB de RAM, 2 a 4 processadores principais e velocidade de rede privada de 1000 Mbps.  
  2. Se você estiver executando a migração em um IBM Cloud Infrastructure Bare Metal ou Virtual Server,
    use os terminais **privados** do Swift e do COS.
  3. Caso contrário, use os terminais **públicos** do Swift e do COS.
  4. Instale o `rclone` por meio de [um gerenciador de pacote ou binário pré-compilado](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Configurar `rclone` para o OpenStack Swift
{: #migrate-rclone}

  1. Crie um arquivo de configuração `rclone` em `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Crie a origem do Swift copiando o seguinte e colando em `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. Obter credencial do OpenStack Swift
    <br>a. Clique em sua instância do Swift no [painel do console do IBM Cloud](https://cloud.ibm.com/).
    <br>b. Clique em **Credenciais de serviço** no painel de navegação.
    <br>c. Clique em **Nova credencial** para gerar informações de credenciais. Clique em ** Adicionar**.
    <br>d. Visualize a credencial que você criou e copie o conteúdo JSON.

  4. Preencha os campos a seguir:

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. Vá para a seção Configurar `rclone` para COS


## Configurar `rclone` para o OpenStack Swift (infraestrutura)
{: #migrate-config-swift}

  1. Crie um arquivo de configuração `rclone` em `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Crie a origem do Swift copiando o seguinte e colando em `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. Obter credencial do OpenStack Swift (infraestrutura)
    <br>a. Clique em sua conta do Swift no portal do cliente de infraestrutura do IBM Cloud.
    <br>b. Clique no data center do contêiner de origem de migração.
    <br>c. Clique em **Visualizar credenciais**.
    <br>d. Copie o seguinte.
      <br>&nbsp;&nbsp;&nbsp;**Nome do usuário**
      <br>&nbsp;&nbsp;&nbsp;**Chave de API**
      <br>&nbsp;&nbsp;&nbsp;**Terminal de autenticação** com base em onde você está executando a ferramenta de migração

  4. Usando a credencial do OpenStack Swift (infraestrutura), preencha os campos a seguir:

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## Configurar `rclone` para COS
{: #migrate-config-cos}

### Obter credencial do COS
{: #migrate-config-cos-credential}

  1. Clique em sua instância do COS no console do IBM Cloud.
  2. Clique em **Credenciais de serviço** no painel de navegação.
  3. Clique em **Nova credencial** para gerar informações de credenciais.
  4. Em **Parâmetros de configuração sequenciais**, inclua `{"HMAC":true}`. Clique em ** Adicionar**.
  5. Visualize a credencial que você criou e copie o conteúdo JSON.

### Obter terminal do COS
{: #migrate-config-cos-endpoint}

  1. Clique em **Depósitos** no painel de navegação.
  2. Clique no depósito de destino de migração.
  3. Clique em **Configuração** no painel de navegação.
  4. Role para baixo até a seção **Terminais** e escolha o terminal com base em onde
     você está executando a ferramenta de migração.

  5. Crie o destino do COS copiando o seguinte e colando em `rclone.conf`.

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. Usando a credencial e o terminal do COS, preencha os campos a seguir:

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## Verifique se a origem de migração e o destino estão configurados adequadamente
{: #migrate-verify}

1. Liste o contêiner do Swift para verificar se o `rclone` está configurado adequadamente.

    ```
    rclone lsd SWIFT:
    ```

2. Liste o depósito do COS para verificar se o `rclone` está configurado adequadamente.

    ```
    rclone lsd COS:
    ```

## Executar `rclone`
{: #migrate-run}

1. Execute uma simulação (nenhum dado copiado) de `rclone` para sincronizar os objetos em seu contêiner
   do Swift de origem (por exemplo, `swift-test`) para o depósito do COS de destino (por exemplo, `cos-test`).

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. Verifique se os arquivos que você deseja migrar aparecem na saída de comando. Se tudo parecer bem, remova a sinalização `--dry-run` e inclua a sinalização `-v` para copiar os dados. O uso da sinalização `--checksum` opcional evitará a atualização de quaisquer arquivos que tenham o mesmo hash MD5 e o tamanho do objeto em ambos os locais.

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   Será necessário tentar atingir o máximo da CPU, memória e rede na máquina que está executando o rclone para obter o tempo de transferência mais rápido.
   Alguns outros parâmetros a serem considerados para o ajuste de rclone:

   --checkers int Número de verificadores a serem executados em paralelo. (padrão 8)
   Esse é o número de encadeamentos de comparação de checksum em execução. Recomendamos aumentar isso para 64 ou mais.

   --transfers int Número de transferências de arquivos para execução em paralelo. (padrão 4)
   Esse é o número de objetos a serem transferidos em paralelo. Recomendamos aumentar isso para 64 ou 128 ou superior.

   --fast-list Usar lista recursiva, se disponível. Usa mais memória, mas menos transações.
   Use essa opção para melhorar o desempenho - reduz o número de solicitações necessárias para copiar um objeto.

Migrar dados usando `rclone` copia, mas não exclui, os dados de origem.
{:tip}


3. Repita para quaisquer outros contêineres que requerem migração.
4. Quando todos os dados forem copiados e você tiver verificado que seu aplicativo pode acessar os dados no COS, exclua a instância de serviço do Swift.
