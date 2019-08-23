---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# Usando `Postman`
{: #postman}

Aqui está uma configuração básica de `Postman` para a API de REST do {{site.data.keyword.cos_full}}. Detalhes adicionais podem ser localizados na referência da API para [depósitos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) ou [objetos](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).

O uso de `Postman` supõe uma certa quantia de familiaridade com armazenamento de objeto e as informações necessárias de uma [credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) ou o [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Se quaisquer termos ou variáveis não forem familiares, eles poderão ser localizados no [glossário](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

Informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}

## Visão geral do cliente da API de REST
{: #postman-rest}

REST (Representational State Transfer) é um estilo arquitetural que fornece um padrão para sistemas de computador para
interagir entre si por meio da web, geralmente usando URLs HTTP padrão e verbos (GET, PUT, POST, etc.) que são suportados por todas as principais linguagens e plataformas de desenvolvimento. No entanto, interagir com uma API de REST não é tão simples quanto usar um navegador de Internet padrão. Os navegadores simples não permitem qualquer manipulação da solicitação de URL. É neste ponto que um cliente da API de REST entra.

Um cliente da API de REST fornece um aplicativo baseado em GUI simples para fazer interface com uma biblioteca da API de REST existente. Um bom cliente torna mais fácil testar, desenvolver e documentar APIs, permitindo que os usuários coloquem rapidamente as solicitações de HTTP simples e complexas. O Postman é um excelente cliente da API de REST que fornece um ambiente de desenvolvimento de API completo que inclui ferramentas integradas para APIs de design e mock, depuração, teste, documentação, monitor e publicação. Ele também fornece recursos úteis, como Coleções e Áreas de trabalho, que tornam a colaboração fácil. 

## Pré-requisitos
{: #postman-prereqs}
* Conta do IBM Cloud
* [Recurso do Cloud Storage criado](https://cloud.ibm.com/catalog/) (o plano Lite/grátis funciona bem)
* [CLI do IBM Cloud instalada e configurada](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [ID da instância de serviço para seu Cloud Storage](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [Token do IAM (Identity and Access Management)](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [Terminal para o seu depósito do COS](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### Criar um depósito
{: #postman-create-bucket}
1.	Ative o Postman
2.	Na guia Novo, selecione `PUT` na lista suspensa
3.	Insira o terminal na barra de endereço e inclua o nome para seu novo depósito.
a.	Os nomes dos depósitos devem ser exclusivos entre todos os depósitos, portanto, escolha algo específico.
4.	Na lista suspensa Tipo, selecione Token de acesso.
5.	Inclua o Token do IAM na caixa de Token.
6.	Clique em Visualizar solicitação.
a.	Você deverá ver uma mensagem de confirmação de que os cabeçalhos foram incluídos.
7.	Clique na guia Cabeçalho na qual você deverá ver uma entrada existente para Autorização.
8.	Inclua uma nova chave.
a.	Chave: `ibm-service-instance-id`
b.	Valor: ID da instância de recurso para seu serviço de armazenamento em nuvem.
9.	Clique em Enviar.
10.	Você receberá uma mensagem de status `200 OK`.

### Criar um novo arquivo de texto
{: #postman-create-text-file}

1.	Crie uma nova guia clicando no ícone Mais (+).
2.	Selecione `PUT` na lista.
3.	Na barra de endereço, insira o endereço de terminal com o nome do depósito da seção anterior e um nome de arquivo.
4.	Na lista Tipo, selecione Token de acesso.
5.	Inclua o Token do IAM na caixa de token.
6.	Selecione a guia Corpo.
7.	Selecione a opção Bruto e assegure-se de que o Texto esteja selecionado.
8.	Insira o texto no espaço fornecido.
9.	Clique em Enviar.
10.	Você receberá uma mensagem de status `200 OK`.

### Listar o conteúdo de um depósito
{: #postman-list-objects}

1.	Crie uma nova guia selecionando o ícone Mais (+).
2.	Verifique se `GET` está selecionado na lista.
3.	Na barra de endereço, insira o endereço de terminal com o nome do depósito da seção anterior.
4.	Na lista Tipo, selecione Token de acesso.
5.	Inclua o Token do IAM na caixa de token.
6.	Clique em Enviar.
7.	Você receberá uma mensagem de status `200 OK`.
8.	No Corpo da seção Resposta, há uma mensagem XML com a lista de arquivos em seu depósito.

## Usando a coleção de amostra
{: #postman-collection}

Uma Coleção de Postman está disponível para [download ![Ícone de link externo](../icons/launch-glyph.svg "Ícone de link externo")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window} com amostras de solicitação da API do {{site.data.keyword.cos_full}} configuráveis.

### Importar a coleção para o Postman
{: #postman-import-collection}

1. No Postman, clique no botão Importar no canto superior direito
2. Importe o arquivo de Coleção usando um destes métodos:
    * Na janela Importar, arraste e solte o arquivo de Coleção na janela rotulada **Descartar arquivos aqui**
    * Clique no botão Escolher arquivos e navegue para a pasta e selecione o arquivo de Coleção
3. O *IBM COS* agora deve aparecer na janela Coleções
4. Expanda a Coleção e você deverá ver vinte (20) solicitações de amostra
5. A Coleção contém seis (6) variáveis que precisarão ser configuradas para executar com êxito as solicitações de API
    * Clique nos três pontos para a direita da coleção para expandir o menu e clique em Editar
6. Edite as variáveis para corresponder ao ambiente do Cloud Storage
    * **bucket** - Insira o nome para o novo depósito que você deseja criar (os nomes dos depósitos devem ser exclusivos no Cloud Storage).
    * **serviceid** - Insira o CRN de seu serviço Cloud Storage. As instruções para obter seu CRN estão disponíveis [aqui](/docs/overview?topic=overview-crn).
    * **iamtoken** - Insira o token OAUTH para seu serviço Cloud Storage. As instruções para obter o token OAUTH estão disponíveis [aqui](/docs/services/key-protect?topic=key-protect-retrieve-access-token).
    * **endpoint** - Insira o terminal regional para seu serviço Cloud Storage. Obtenha os terminais disponíveis no [IBM Cloud Dashboard](https://cloud.ibm.com/resources/){:new_window}
        * *Assegure-se de que seu terminal selecionado corresponda ao seu serviço de proteção de chave para assegurar que as amostras sejam executadas corretamente*
    * **rootkeycrn** - O CRN da Chave raiz criada em seu serviço Key Protect primário.
        * O CRN deve assemelhar-se ao seguinte:<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Assegure-se de que o serviço Key Protect selecionado corresponda à região do Terminal*
    * **bucketlocationvault** - Insira o valor de restrição de local para a criação do depósito para a solicitação de API *Criar novo depósito (classe de armazenamento diferente)*.
        * Os valores aceitos incluem:
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. Clique em Atualizar

### Executando as amostras
{: #postman-samples}
As solicitações de amostra de API são bastante simples e fáceis de usar. Elas são projetadas para serem executadas em ordem e demonstram como interagir com o Cloud Storage. Elas também podem ser usadas para executar um teste funcional com relação ao seu serviço Cloud Storage para assegurar a operação adequada.

<table>
    <tr>
        <th>Solicitar</th>
        <th>Resultado esperado</th>
        <th>Resultados do teste</th>
    </tr>
    <tr>
        <td>Recuperar lista de depósitos</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo, é necessário configurar uma lista XML dos depósitos em seu armazenamento em nuvem.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o conteúdo esperado</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Criar novo depósito</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Criar novo arquivo de texto</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o cabeçalho esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Criar novo arquivo binário</td>
        <td>
            <ul>
                <li>
                    Clique em Corpo e clique em Escolher arquivo para selecionar uma imagem para fazer upload
                </li>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o cabeçalho esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar lista de arquivos do depósito</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver os dois arquivos que foram criados nas solicitações anteriores
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o cabeçalho esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar lista de arquivos do depósito (filtrar por prefixo)</td>
        <td>
            <ul>
                <li>Mudar o valor de querystring para prefix=&lt;some text&gt;</li>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver os arquivos com nomes que iniciam com o prefixo especificado
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o cabeçalho esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar arquivo de texto</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver o texto inserido na solicitação anterior
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o conteúdo do corpo esperado</li>
                <li>A resposta contém o cabeçalho esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar arquivo binário</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver a imagem escolhida na solicitação anterior
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o cabeçalho esperado</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Recuperar lista de uploads de múltiplas partes com falha</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver quaisquer uploads de múltiplas partes com falha para o depósito
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o conteúdo esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar lista de uploads de múltiplas partes com falha (filtrar por nome)</td>
        <td>
            <ul>
                <li>Mudar o valor de querystring para prefix=&lt;some text&gt;</li>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver quaisquer uploads de múltiplas partes com falha para o depósito com nomes que iniciam com o prefixo especificado
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o conteúdo esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Configurar depósito ativado para CORS</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Recuperar a configuração de CORS de depósito</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
                <li>
                    No Corpo da resposta, você deverá ver o conjunto de configuração de CORS para o depósito
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
                <li>A resposta contém o conteúdo esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Excluir a configuração de CORS de depósito</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Excluir arquivo de texto</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Excluir arquivo binário</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Excluir depósito</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Criar novo depósito (classe de armazenamento diferente)</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Excluir depósito (classe de armazenamento diferente)</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Criar novo depósito (proteção de chave)</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Excluir depósito (proteção de chave)</td>
        <td>
            <ul>
                <li>Código de status 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>A solicitação foi bem-sucedida</li>
            </ul>
        </td>                
    </tr>
</table>

## Usando o Postman Collection Runner
{: #postman-runner}

O Postman Collection Runner fornece uma interface com o usuário para testar uma coleção e permite executar todas as solicitações em uma Coleção de uma só vez. 

1. Clique no botão Executor no canto superior direito na janela principal do Postman.
2. Na janela Executor, selecione a coleção do IBM COS e clique no botão grande azul **Executar o IBM COS** na parte inferior da tela.
3. A janela Collection Runner mostrará as iterações conforme as solicitações forem executadas. Você verá que os resultados do teste aparecem abaixo de cada uma das solicitações.
    * O **Resumo da execução** exibe uma visualização em grade das solicitações e permite a filtragem dos resultados.
    * Também é possível clicar em **Exportar resultados**, que salvará os resultados em um arquivo JSON.
