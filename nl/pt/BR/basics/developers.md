---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, getting started, cli

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

# Para desenvolvedores
{: #gs-dev}
Primeiro, assegure-se de que você tenha a [CLI do {{site.data.keyword.cloud}} Platform](https://cloud.ibm.com/docs/cli/index.html) e o [IBM Developer Tools](https://cloud.ibm.com/docs/cloudnative/idt/index.html) instalados.

## Provisionar uma instância do {{site.data.keyword.cos_full_notm}}
{: #gs-dev-provision}

  1. Primeiro, certifique-se de que você tenha uma chave de API. Obtenha isso no [IBM Cloud Identity and Access Management](https://cloud.ibm.com/iam/apikeys).
  2. Efetue login no {{site.data.keyword.cloud_notm}} Platform usando a CLI. Também é possível armazenar a chave de API em um arquivo ou configurá-la como uma variável de ambiente.

```
ibmcloud login --apikey <value>
```
{:codeblock}

  3. Em seguida, provisione uma instância do {{site.data.keyword.cos_full_notm}} especificando o nome para a instância, o ID e o plano desejado (Lite ou Standard). Isso nos dará o CRN. Se você tiver uma conta com upgrade, especifique o plano `Standard`. Caso contrário, especifique `Lite`.

```
ibmcloud resource service-instance-create <instance-name> cloud-object-storage <plan> global
```
{:codeblock}

O [Guia de introdução](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) apresenta as etapas básicas para criar depósitos e objetos, bem como convidar usuários e criar políticas. Uma lista de comandos básicos 'curl' pode ser localizada [aqui](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

Saiba mais sobre como usar a CLI do {{site.data.keyword.cloud_notm}} para criar aplicativos, gerenciar clusters Kubernetes e mais [na documentação](/docs/cli/reference/ibmcloud?topic=cloud-cli-ibmcloud_cli).


## Usando a API
{: #gs-dev-api}

Para gerenciar dados armazenados no {{site.data.keyword.cos_short}}, é possível usar as ferramentas compatíveis com a API S3 como a [CLI do AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli) com [credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) para compatibilidade. Como é relativamente fácil trabalhar com os tokens do IAM, o `curl` é uma boa opção para teste básico e interação com seu armazenamento. Mais informações podem ser localizadas na [referência `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl), assim como [na documentação de referência da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Usando bibliotecas e SDKs
{: #gs-dev-sdk}
Há SDKs do IBM COS disponíveis para [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go) e [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node). Essas são versões bifurcadas dos SDKs do AWS S3 que foram modificados para suportar a [autenticação baseada em token do IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview), bem como suportar o [Key Protect](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption). 

## Construindo aplicativos no IBM Cloud
{: #gs-dev-apps}
O {{site.data.keyword.cloud}} fornece flexibilidade para os desenvolvedores na escolha das opções de arquitetura e implementação corretas para um determinado aplicativo. Execute seu código em [bare metal](https://cloud.ibm.com/catalog/infrastructure/bare-metal), em [máquinas virtuais](https://cloud.ibm.com/catalog/infrastructure/virtual-server-group), usando uma [estrutura serverless](https://cloud.ibm.com/openwhisk) em [contêineres](https://cloud.ibm.com/kubernetes/catalog/cluster) ou usando o [Cloud Foundry](https://cloud.ibm.com/catalog/starters/sdk-for-nodejs). 

A [Cloud Native Computing Foundation](https://www.cncf.io) incubou e recentemente "graduou" a estrutura de orquestração do contêiner do [Kubernetes](https://kubernetes.io), fundando a base para o {{site.data.keyword.cloud}} Kubernetes Service. Os desenvolvedores que desejam usar o armazenamento de objeto para armazenamento persistente em seus aplicativos do Kubernetes podem aprender mais nos links a seguir:

 * [Escolhendo uma solução de armazenamento](/docs/containers?topic=containers-storage_planning#choose_storage_solution)
 * [Tabela de comparação para opções de armazenamento persistente](/docs/containers?topic=containers-storage_planning#persistent_storage_overview)
 * [Página principal do COS](/docs/containers?topic=containers-object_storage)
 * [Instalando o COS](/docs/containers?topic=containers-object_storage#install_cos)
 * [Criando a instância de serviço do COS](/docs/containers?topic=containers-object_storage#create_cos_service)
 * [Criando o segredo do COS](/docs/containers?topic=containers-object_storage#create_cos_secret)
 * [Decidir sobre a configuração](/docs/containers?topic=containers-object_storage#configure_cos)
 * [Provisionar o COS](/docs/containers?topic=containers-object_storage#add_cos)
 * [Informações de backup e restauração](/docs/containers?topic=containers-object_storage#backup_restore)
 * [Referência de classe de armazenamento](/docs/containers?topic=containers-object_storage#storageclass_reference)


