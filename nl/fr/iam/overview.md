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

# Présentation d'IAM
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management vous permet d'authentifier des utilisateurs de façon sécurisée et de contrôler l'accès aux ressources de cloud de manière cohérente dans la plateforme {{site.data.keyword.cloud_notm}}. Pour plus d'informations, voir le [tutoriel d'initiation](/docs/iam?topic=iam-getstarted#getstarted) for more information.

## Gestion des identités
{: #iam-overview-identity}

La fonction de gestion des identités inclut l'interaction des utilisateurs, des services et des ressources. Les utilisateurs sont identifiés par leur IBMid. Les services sont identifiés par leur ID de service. Et les ressources sont identifiées à l'aide de noms de ressource de cloud (CRN). 

Le service de jeton IAM d'{{site.data.keyword.cloud_notm}} vous permet de créer, mettre à jour, supprimer et utiliser des clés d'API pour les utilisateurs et les services. Ces clés d'API peuvent être créées à l'aide d'appels d'API ou de la section Identity & Access de la console de la plateforme {{site.data.keyword.cloud}}. La même clé peut être utilisée dans plusieurs services. Chaque utilisateur peut avoir plusieurs clés d'API pour prendre en charge des scénarios de rotation de clés, ainsi que des scénarios utilisant différentes clés pour différents objectifs afin de limiter l'exposition d'une clé unique.

Pour plus d'informations, voir la rubrique [qu'est-ce que Cloud IAM ?](/docs/iam?topic=iam-iamoverview#iamoverview). 

### Utilisateurs et clés d'API
{: #iam-overview-user-api-keys}

Des clés d'API peuvent être créées et utilisées par les utilisateurs {{site.data.keyword.cloud_notm}} à des fins d'automatisation et de scriptage, ainsi que pour les journaux fédérés lors de l'utilisation de l'interface de ligne de commande. Des clés d'API peuvent être créées dans l'interface utilisateur Identity and Access Management ou à l'aide de l'interface de ligne de commande `ibmcloud` .

### ID de service et clés d'API
{: #iam-overview-service-id-api-key}

Le service de jeton IAM permet de créer des ID de service et des clés d'API pour ces derniers. Un ID de service est semblable à un ID fonctionnel ou à un ID d'application et est utilisé pour authentifier des services et non pour représenter un utilisateur. 

Les utilisateurs peuvent créer des ID de service et les lier à des portées telles qu'un compte de plateforme {{site.data.keyword.cloud_notm}}, une organisation CloudFoundry ou un espace CloudFoundry, même si pour adopter IAM, il est préférable de lier les ID de service à un compte de plateforme {{site.data.keyword.cloud_notm}}. Cette liaison est effectuée afin de fournir à l'ID de service un conteneur dans lequel résider. Ce conteneur définit également qui peut mettre à jour et supprimer l'ID de service et qui peut créer, mettre à jour, lire et supprimer des clés d'API associées à cet ID de service. Il est important de noter qu'un ID de service n'est PAS lié à un utilisateur.

### Rotation des clés
{: #iam-overview-key-rotation}

Les clés de l'API doivent faire l'objet d'une rotation régulière afin d'éviter toute atteinte à la sécurité causée par des fuites de clés.

## Gestion des accès
{: #iam-overview-access-management}

Le contrôle d'accès IAM est couramment utilisé pour affecter des rôles utilisateur pour les ressources {{site.data.keyword.cloud_notm}} et il contrôle les actions que les utilisateurs peuvent effectuer sur ces ressources. Vous pouvez afficher et gérer des utilisateurs dans l'ensemble du compte ou de l'organisation, en fonction des options d'accès qui vous ont été affectées. Par exemple, les propriétaires de compte se voient automatiquement attribuer le rôle d'administrateur de compte pour Identity and Access Management, ce qui leur permet d'affecter et de gérer des règles de service pour tous les membres de leur compte.

### Utilisateurs, rôles, ressources et règles
{: #iam-overview-access-policies}

Le contrôle d'accès IAM permet d'affecter des règles par service ou par instance de service afin d'autoriser des niveaux d'accès pour la gestion des ressources et des utilisateurs dans le contexte affecté. Une règle accorde à un utilisateur un ou plusieurs rôles sur un ensemble de ressources à l'aide d'une combinaison d'attributs permettant de définir l'ensemble de ressources applicable. Lorsque vous affectez une règle à un utilisateur, vous devez d'abord spécifier le service, puis un ou plusieurs rôles à affecter. Des options de configuration supplémentaires peuvent être disponibles en fonction du service que vous sélectionnez.

Les rôles étant une collection d'actions, les actions qui sont mappées à ces rôles sont propres à un service. Chaque service détermine ce mappage rôle-action durant le processus d'intégration et ce mappage s'applique à tous les utilisateurs du service. Les rôles et les règles d'accès sont configurés via le point d'administration de règles (PAP) et appliqués via le point d'application de règles (PEP) et le point de décision de règles (PDP).

Pour en savoir plus, voir [Meilleures pratiques en matière d'organisation des utilisateurs, des équipes et des applications](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications). 
