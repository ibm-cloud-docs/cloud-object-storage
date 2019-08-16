---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Tutorial Introdução
{: #getting-started}

Este tutorial de introdução passa pelas etapas que são necessárias para criar depósitos, fazer upload de objetos e configurar políticas de acesso para permitir que outros usuários trabalhem com seus dados.
{: shortdesc}

## Antes de iniciar
{: #gs-prereqs}

Você precisa de:
  * Uma [conta do {{site.data.keyword.cloud}} Platform](https://cloud.ibm.com)
  * Uma [instância do {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * E alguns arquivos em seu computador local para fazer upload.
{: #gs-prereqs}

 Este tutorial usa um novo usuário nas primeiras etapas com o console do {{site.data.keyword.cloud_notm}} Platform. Para os desenvolvedores que desejam obter uma introdução à API, consulte o [Guia do Desenvolvedor](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) ou a [Visão geral da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Crie alguns depósitos para armazenar seus dados
{: #gs-create-buckets}

  1. [Pedir o {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) cria uma _instância de serviço_. O {{site.data.keyword.cos_full_notm}} é um sistema com vários locatários e todas as instâncias do {{site.data.keyword.cos_short}} compartilham a infraestrutura física. Você é redirecionado automaticamente para a instância de serviço na qual é possível iniciar a criação de depósitos. Suas instâncias do {{site.data.keyword.cos_short}} são listadas em **Armazenamento** na [lista de recursos](https://cloud.ibm.com/resources).

Os termos 'instância de recurso' e 'instância de serviço ' referem-se ao mesmo conceito e podem ser usados de forma intercambiável.
{: tip}

  1. Siga **Criar depósito** e escolha um nome exclusivo. Todos os depósitos em todas as regiões do globo compartilham um único namespace. Assegure-se de que você tenha as [permissões corretas](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) para criar um depósito.

  **Nota**: ao criar depósitos ou incluir objetos, certifique-se de evitar o uso de Informações Pessoalmente Identificáveis (PII). PII são as informações que podem identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
  {: tip}

  1. Escolha um [nível de _resiliência_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) desejado primeiro e, em seguida, um _local_ em que você gostaria que seus dados fossem armazenados fisicamente. Resiliência refere-se ao escopo e escala da área geográfica na qual seus dados são distribuídos. A resiliência _Região cruzada_ difunde seus dados em várias áreas metropolitanas, enquanto a resiliência _Regional_ difunde dados em uma única área metropolitana. Um _Data center único_ distribui dados entre os dispositivos somente em um único site.
  2. Escolha a [_classe de armazenamento_ do depósito](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes), que é um reflexo da frequência com a qual você espera ler os dados armazenados e determina os detalhes de faturamento. Siga o link **Criar** para criar e acessar seu novo depósito.

Os depósitos são uma maneira de organizar seus dados, mas eles não são a única maneira. Os nomes de objetos (geralmente referidos como _chaves de objetos_) podem usar uma ou mais barras para um sistema organizacional semelhante a um diretório. Em seguida, você usa a parte do nome do objeto antes de um delimitador para formar um _prefixo de objeto_, que é usado para listar objetos relacionados em um único depósito por meio da API.
{: tip}


## Incluir alguns objetos em seus depósitos
{: #gs-add-objects}

Agora vá em frente e acesse um de seus depósitos selecionando-o na lista. Clique em **Incluir objetos**. Os novos objetos sobrescrevem objetos existentes com os mesmos nomes dentro do mesmo depósito. Quando você usa o console para fazer upload de objetos, o nome do objeto sempre corresponde ao nome do arquivo. Não haverá necessidade de haver nenhum relacionamento entre o nome do arquivo e a chave do objeto se você estiver usando a API para gravar dados. Vá em frente e inclua diversos arquivos nesse depósito.

Os objetos são limitados a 200 MB quando transferidos por upload por meio do console, a menos que você use o plug-in [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload). Os objetos maiores (até 10 TB) também podem ser [divididos em partes e transferidos por upload em paralelo usando a API](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects). As chaves de objetos podem ter até 1024 caracteres de comprimento e é melhor evitar quaisquer caracteres que possam ser problemáticos em um endereço da web. Por exemplo, `?`, `=`, `<` e outros caracteres especiais poderão causar um comportamento indesejado, se não codificado por URL.
{:tip}

## Convidar um usuário para sua conta para administrar seus depósitos e dados
{: #gs-invite-user}

Agora, você vai trazer outro usuário e permitir que ele aja como um administrador para a instância e quaisquer dados armazenados nela.

  1. Primeiro, para incluir o novo usuário, é necessário deixar a interface do {{site.data.keyword.cos_short}} atual e dirigir-se ao console do IAM. Acesse o menu **Gerenciar** e siga o link em **Acesso (IAM)** > **Usuários**. Clique em **Convidar usuários**.
	<img alt="Convidar usuários do IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`Figura 1: Convidar usuários do IAM`
  2. Insira o endereço de e-mail de um usuário que você deseja convidar para sua organização, em seguida, expanda a seção **Serviços** e selecione "Recurso" no menu **Designar acesso a**. Agora, escolha "Cloud Object Storage" no menu **Serviços**.
	<img alt="Serviços do IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`Figura 2: Serviços do IAM`
  3. Agora, três campos adicionais aparecem: _Instância de serviço_, _Tipo de recurso_ e _ID do recurso_. O primeiro campo define qual instância do {{site.data.keyword.cos_short}} o usuário pode acessar. Ele também pode ser configurado para conceder o mesmo nível de acesso a todas as instâncias do {{site.data.keyword.cos_short}}. Podemos deixar os outros campos em branco por enquanto.
	<img alt="Convidar usuários do IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`Figura 3: Convidar usuários do IAM`
  4. A caixa de seleção sob **Selecionar funções** determina o conjunto de ações disponíveis para o usuário. Selecione a função de acesso de plataforma "Administrador" para permitir que o usuário conceda a outros [usuários e IDs de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) acesso à instância. Selecione a função de acesso ao serviço "Gerenciador" para permitir que o usuário gerencie a instância do {{site.data.keyword.cos_short}}, bem como criar e excluir depósitos e objetos. Essas combinações de um _Assunto_ (usuário), _Função_ (Gerenciador) e _Recurso_ (instância de serviço do {{site.data.keyword.cos_short}}) juntas formam [Políticas do IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam). Para obter orientação mais detalhada sobre funções e políticas, [consulte a documentação do IAM](/docs/iam?topic=iam-userroles).
	<img alt="Funções do IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`Figura 4: Funções de seleção do IAM`
  5. O {{site.data.keyword.cloud_notm}} usa o Cloud Foundry como a plataforma de gerenciamento de conta subjacente, portanto, é necessário conceder um nível mínimo de acesso ao Cloud Foundry para que o usuário acesse sua organização em primeiro lugar. Selecione uma organização no menu **Organização** e, em seguida, selecione "Auditor" em ambos os menus **Funções organizacionais** e **Funções de espaço**. A configuração de permissões do Cloud Foundry permite ao usuário visualizar os serviços disponíveis para sua organização, mas não os mudar.

## Forneça aos desenvolvedores acesso a um depósito.
{: #gs-bucket-policy}

  1. Navegue para o menu **Gerenciar** e siga o link em **Acesso (IAM)** > **IDs de serviço**. Aqui é possível criar um _ID de serviço_, que serve como uma identidade extraída ligada à conta. Os IDs de serviço podem ser designados a chaves de API e são usados em situações nas quais você não deseja vincular a identidade de um Desenvolvedor específico a um processo ou componente de um aplicativo.
	<img alt="IDs de serviço do IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`Figura 5: IDs de serviço do IAM`
  2. Repita o processo acima, mas, na etapa 3, escolha uma instância de serviço específica e insira "depósito" como o _Tipo de recurso_ e o CRN integral de um depósito existente como o _ID do recurso_.
  3. Agora, o ID de serviço pode acessar esse depósito específico e nenhum outro.

## Próximas Etapas
{: #gs-next-steps}

Agora que você está familiarizado com o armazenamento de objeto por meio do console baseado na web, talvez esteja interessado em fazer um fluxo de trabalho semelhante por meio da linha de comandos usando o utilitário de linha de comandos `ibmcloud cos` para criar a instância de serviço e interagir com o IAM e o `curl` para acessar o COS diretamente. [Consulte a visão geral da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) para obter uma introdução.
