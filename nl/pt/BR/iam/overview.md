---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# Visão geral do IAM
{: #iam-overview}

O {{site.data.keyword.cloud}} Identity & Access Management permite que você autentique com segurança os usuários e controle o acesso a todos os recursos em nuvem de forma consistente no {{site.data.keyword.cloud_notm}} Platform. Consulte o [Tutorial de introdução](/docs/iam?topic=iam-getstarted#getstarted) para obter mais informações.

## Gerenciamento de identidade
{: #iam-overview-identity}

O gerenciamento de identidade inclui a interação de usuários, serviços e recursos. Os usuários são identificados por seu IBMid. Os serviços são identificados por seus IDs de serviço. Além disso, os recursos são identificados e endereçados usando CRNs.

O Serviço de Token do {{site.data.keyword.cloud_notm}} IAM permite criar, atualizar, excluir e usar as chaves de API para usuários e serviços. Essas chaves de API podem ser criadas com chamadas da API ou a seção Identidade e acesso do {{site.data.keyword.cloud}} Platform Console. A mesma chave pode ser usada em múltiplos serviços. Cada usuário pode ter múltiplas chaves de API para suportar cenários de rotação de chave, bem como cenários usando chaves diferentes para propósitos diferentes de limitar a exposição de uma única chave.

Consulte [O que é Cloud IAM?](/docs/iam?topic=iam-iamoverview#iamoverview) para obter mais informações.

### Usuários e chaves de API
{: #iam-overview-user-api-keys}

As chaves de API podem ser criadas e usadas pelos usuários do {{site.data.keyword.cloud_notm}} para propósitos de automação e script, bem como login federado ao usar a CLI. As chaves de API podem ser criadas na UI do Identity and Access Management ou usando a CLI `ibmcloud`.

### IDs de serviço e chaves de API
{: #iam-overview-service-id-api-key}

O Serviço de Token do IAM permite criar IDs de serviço e Chaves de API para IDs de serviço. Um ID de serviço é semelhante a um "ID funcional" ou a um "ID do aplicativo" e é usado para autenticar serviços e não para representar um usuário.

Os usuários podem criar IDs de serviço e ligá-los a escopos como uma conta do {{site.data.keyword.cloud_notm}} Platform, uma organização do CloudFoundry ou um espaço do CloudFoundry, embora para adotar o IAM, é melhor ligar os IDs de serviço a uma conta do {{site.data.keyword.cloud_notm}} Platform. Essa ligação é feita para fornecer ao ID de serviço um contêiner no qual residir. Esse contêiner também define quem pode atualizar e excluir o ID de serviço e quem pode criar, atualizar, ler e excluir as chaves de API que estão associadas a esse ID de serviço. É importante observar que um ID de serviço NÃO está relacionado a um usuário.

### Rotação de chave
{: #iam-overview-key-rotation}

As chaves de API devem ser giradas regularmente para evitar quaisquer violações de segurança causadas por chaves vazadas.

## Gerenciamento de acesso
{: #iam-overview-access-management}

O Controle de acesso do IAM fornece uma maneira comum de designar funções de usuário para recursos do {{site.data.keyword.cloud_notm}} e controla as ações que os usuários podem executar nesses recursos. É possível visualizar e gerenciar usuários na conta ou na organização, dependendo das opções de acesso que foram fornecidas a você. Por exemplo, os proprietários da conta são designados automaticamente à função de Administrador da conta para o Identity and Access Managemement, que permite designar e gerenciar políticas de serviço para todos os membros da conta deles.

### Usuários, funções, recursos e políticas
{: #iam-overview-access-policies}

O Controle de acesso do IAM permite a designação de políticas por serviço ou instância de serviço para permitir níveis de acesso para gerenciar recursos e usuários dentro do contexto designado. Uma política concede a um usuário uma função ou funções para um conjunto de recursos usando uma combinação de atributos para definir o conjunto aplicável de recursos. Ao designar uma política a um usuário, primeiro você especifica o serviço, em seguida, uma função ou funções a serem designadas. Opções de configuração adicionais podem estar disponíveis, dependendo do serviço selecionado.

Enquanto as funções são uma coleção de ações, as ações que são mapeadas para essas funções são específicas do serviço. Cada serviço determina essa função para o mapeamento de ação durante o processo de integração e esse mapeamento é aplicável a todos os usuários do serviço. As funções e as políticas de acesso são configuradas por meio do Policy Administration Point (PAP) e cumpridas por meio do Policy Enforcement Point (PEP) e do Policy Decision Point (PDP).

Consulte [Melhores práticas para organizar usuários, equipes, aplicativos](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications) para aprender mais.
