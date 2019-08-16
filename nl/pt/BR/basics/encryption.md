---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# Gerenciar criptografia
{: #encryption}

Todos os objetos armazenados no {{site.data.keyword.cos_full}} são criptografados por padrão usando [chaves geradas aleatoriamente e uma transformação de tudo ou nada](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security). Enquanto esse modelo de criptografia padrão fornece segurança em repouso, algumas cargas de trabalho precisam ser propriedade das chaves de criptografia usadas. É possível gerenciar suas chaves manualmente fornecendo suas próprias chaves de criptografia ao armazenar dados (SSE-C) ou é possível criar depósitos que usam o IBM Key Protect (SSE-KP) para gerenciar chaves de criptografia.

## Server Side Encryption with Customer-Provided Keys (SSE-C)
{: #encryption-sse-c}

O SSE-C é cumprido em objetos. As solicitações para ler ou gravar objetos ou seus metadados usando as chaves gerenciadas pelo cliente enviam as informações de criptografia necessárias como cabeçalhos nas solicitações de HTTP. A sintaxe é idêntica à API S3 e as bibliotecas compatíveis com S3 que suportam o SSE-C devem funcionar conforme o esperado com relação ao {{site.data.keyword.cos_full}}.

Qualquer solicitação usando cabeçalhos do SSE-C deve ser enviada usando SSL. Observe que os valores `ETag` em cabeçalhos de resposta *não* são o hash MD5 do objeto, mas uma sequência hexadecimal de 32 bytes gerada aleatoriamente.

Cabeçalho (Header) | Tipo | Descrição
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | string | Esse cabeçalho é usado para especificar o algoritmo e o tamanho da chave a serem usados com a chave de criptografia armazenada no cabeçalho `x-amz-server-side-encryption-customer-key`. Esse valor deve ser configurado como a sequência `AES256`.
`x-amz-server-side-encryption-customer-key` | string | Esse cabeçalho é usado para transportar a representação de sequência de bytes codificados em base 64 da chave AES 256 usada no processo de criptografia do lado do servidor.
`x-amz-server-side-encryption-customer-key-MD5` | string | Esse cabeçalho é usado para transportar a compilação MD5 de 128 bits codificada em base64 da chave de criptografia de acordo com o RFC 1321. O armazenamento de objeto usará esse valor para validar que a chave que passa no `x-amz-server-side-encryption-customer-key` não foi corrompida durante o processo de transporte e codificação. A compilação deve ser calculada na chave ANTES de a chave ser codificada em base 64.


## Server Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #encryption-kp}

O {{site.data.keyword.keymanagementservicefull}} é um key management system (KMS) centralizado para gerar, gerenciar e destruir chaves de criptografia usadas pelos serviços do {{site.data.keyword.cloud_notm}}. É possível criar uma instância do {{site.data.keyword.keymanagementserviceshort}} por meio do catálogo do {{site.data.keyword.cloud_notm}}.

Depois que você tiver uma instância do {{site.data.keyword.keymanagementserviceshort}} em uma região na qual deseja criar um novo depósito, será necessário criar uma chave raiz e anotar o CRN dessa chave.

É possível escolher usar o {{site.data.keyword.keymanagementserviceshort}} para gerenciar a criptografia para um depósito somente no momento da criação. Não é possível mudar um depósito existente para usar o {{site.data.keyword.keymanagementserviceshort}}.
{:tip}

Ao criar o depósito, é necessário fornecer cabeçalhos adicionais.

Para obter mais informações sobre o {{site.data.keyword.keymanagementservicelong_notm}}, [consulte a documentação](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect).

### Introdução ao SSE-KP
{: #sse-kp-gs}

Todos os objetos armazenados no {{site.data.keyword.cos_full}} são criptografados por padrão usando múltiplas chaves geradas aleatoriamente e uma transformação de tudo ou nada. Enquanto esse modelo de criptografia padrão fornece segurança em repouso, algumas cargas de trabalho precisam ser propriedade das chaves de criptografia usadas. É possível usar o [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) para criar, incluir e gerenciar chaves, que podem então ser associadas à sua instância do {{site.data.keyword.cos_full}} para criptografar depósitos.

### Antes de iniciar
{: #sse-kp-prereqs}

Você precisará de:
  * uma [conta do {{site.data.keyword.cloud}} Platform](http://cloud.ibm.com)
  * uma [instância do {{site.data.keyword.cos_full_notm}}](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * uma [instância do {{site.data.keyword.keymanagementservicelong_notm}}](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * e alguns arquivos em seu computador local para fazer upload.

### Criar ou incluir uma chave no {{site.data.keyword.keymanagementserviceshort}}
{: #sse-kp-add-key}

Navegue para a sua instância do {{site.data.keyword.keymanagementserviceshort}} e [gere ou insira uma chave](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

### Conceder autorização de serviço
{: #sse-kp}
Autorize o {{site.data.keyword.keymanagementserviceshort}} para uso com o IBM COS:

1. Abra o painel do {{site.data.keyword.cloud_notm}}.
2. Na barra de menus, clique em **Gerenciar** &gt; **Conta** &gt; **Usuários**.
3. Na navegação lateral, clique em **Identidade e acesso** &gt; **Autorizações**.
4. Clique em **Criar autorização**.
5. No menu **Serviço de origem**, selecione **Cloud Object Storage**.
6. No menu **Instância de serviço de origem**, selecione a instância de serviço a ser autorizada.
7. No menu **Serviço de destino**, selecione **{{site.data.keyword.keymanagementservicelong_notm}}**.
8. No menu **Instância de serviço de destino**, selecione a instância de serviço a ser autorizada.
9. Ative a função **Leitor**.
10. Clique em **Autorizar**.

### Criar um depósito
{: #encryption-createbucket}

Quando sua chave existir no {{site.data.keyword.keymanagementserviceshort}} e você tiver autorizado o serviço Key Protect para uso com o IBM COS, associe a chave a um novo depósito:

1. Navegue para a sua instância do {{site.data.keyword.cos_short}}.
2. Clique em **Criar depósito**.
3. Insira um nome de depósito, selecione a resiliência **Regional** e escolha um local e uma classe de armazenamento.
4. Em Configuração avançada, ative **Incluir chaves do Key Protect**.
5. Selecione a instância de serviço do Key Protect associada, a chave e o ID da chave.
6. Clique **Criar**.

Na listagem **Depósitos e objetos**, o depósito agora tem um ícone de chave em **Avançado**, indicando que o depósito tem uma chave do Key Protect ativada. Para visualizar os detalhes da chave, clique no menu à direita do depósito e, em seguida, clique em **Visualizar chave do Key Protect**.

Observe que o valor `Etag` retornado para objetos criptografados usando SSE-KP **será** o hash MD5 real do objeto não criptografado original.
{:tip}


## Girando chaves
{: #encryption-rotate}

A rotação de chave é uma parte importante da mitigação do risco de uma violação de dados. Periodicamente, a mudança de chaves reduzirá a perda de dados em potencial se a chave for perdida ou estiver comprometida. A frequência de rotações de chave varia de acordo com a organização e depende de diversas variáveis, incluindo o ambiente, a quantia de dados criptografados, a classificação dos dados e as leis de conformidade. O [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} fornece definições de comprimentos de chave apropriados e fornece diretrizes sobre como as chaves longas devem ser usadas.

### Rotação de chave manual
{: #encryption-rotate-manual}

Para fazer a rotação das chaves para seu {{site.data.keyword.cos_short}}, será necessário criar um novo depósito com o Key Protect ativado usando uma nova Chave raiz e copiar o conteúdo de seu depósito existente para o novo.

**NOTA**: a exclusão de uma chave do sistema fragmentará seu conteúdo e quaisquer dados ainda criptografados com essa chave. Uma vez removida, isso não pode ser desfeito nem revertido e resultará em perda permanente de dados.

1. Crie ou inclua uma nova Chave raiz em seu serviço [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).
2. [Crie um novo depósito](#encryption-createbucket) e inclua a nova Chave raiz
3. Copie todos os objetos de seu depósito original para o novo depósito.
    1. Esta etapa pode ser realizada usando diversos métodos diferentes:
        1. Na linha de comandos usando o [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) ou a [CLI do AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)
        2. Usando a (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]
        3. Usando o SDK com [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) ou [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)
