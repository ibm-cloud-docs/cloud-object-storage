---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: ruby, activestorage, rails, aws

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

# Ruby on Rails/Active Storage
{: #ror-activestorage}

[Rails](https://guides.rubyonrails.org/getting_started.html){:new_window} est une infrastructure de développement d'application Web open source qui combine le langage de programmation Ruby avec HTML, CSS et JavaScript. Elle inclut tout ce qui est nécessaire pour créer des applications Web côté serveur à l'aide du canevas MVC (Model-View-Controller).  

Dans Rails, le modèle (données métier et couche logique) du canevas MVC est géré par un [Active Record](https://guides.rubyonrails.org/active_record_basics.html){:new_window}. Il fournit l'infrastructure de [mappage objet/relationnel (ORM)](https://en.wikipedia.org/wiki/Object-relational_mapping){:new_window} qui connecte les objets métier avec le stockage persistant dans le système de gestion de base de données relationnelle. 

[Active Storage](https://guides.rubyonrails.org/active_storage_overview.html){:new_window} est une infrastructure intégrée permettant de connecter des fichiers à partir de services de stockage en cloud, tels que {{site.data.keyword.cos_full}}, à des objets Active Record. Amazon S3, Google Cloud Storage et Microsoft Azure sont également pris en charge ainsi que les services basés sur disque local. 

Pour démarrer avec Active Storage, exécutez les commandes suivantes à partir du répertoire de base de votre application : 

```
bin/rails active_storage:install
bin/rails db:migrate
```

Cela permet de créer les deux tables requises (`active_storage_blobs` et `active_storage_attachments`) dans la base de données de l'application pour gérer les fichiers joints Active Record.  

## Configuration de stockage
{: #ror-activestorage-config}

Déclarez votre service {{site.data.keyword.cos_short}} dans `config/storage.yml` :

```
ibmcos:
  service: S3
  access_key_id: <%= Rails.application.credentials.dig(:aws, :access_key_id) %>
  secret_access_key: <%= Rails.application.credentials.dig(:aws, :secret_access_key) %>
  region: <bucket-region>
  bucket: <bucket-name>
  endpoint: <regional-endpoint>
```

Ajoutez [aws-sdk-s3](https://github.com/aws/aws-sdk-ruby){:new_window} à votre fichier Gemfile :

```
gem "aws-sdk-s3", require: false
```

Vous pouvez déclarer plusieurs instances de service avec différents noeuds finaux et compartiments pour une utilisation dans différents environnements.
{:tip}

*Valeurs de clé*
* `<bucket-region>` - Région qui correspond à votre compartiment (par exemple, `us-south-standard`). Vous trouverez une liste complète en cliquant [ici](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes#classes-locationconstraint). 
* `<bucket-name>` - Nom de votre compartiment. 
* `<regional-endpoint>` - Noeud final d'accès à votre compartiment (par exemple, `https://s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net`). Vous trouverez une liste complète en cliquant [ici](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints). 

L'instruction `<%= Rails.application.credentials.dig(:aws, :access_key_id|secret_access_key) %>` indique à Rails que la clé d'accès et la clé secrète doivent être extraites des données d'identification stockées dans `~/.aws/credentials` au format suivant :

```
[default]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

## Environnement Rails
{: #ror-activestorage-rails}

Configurez vos environnements pour l'utilisation de votre service {{site.data.keyword.cos_short}} en mettant à jour le paramètre suivant :

```
config.active_storage.service = :ibmcos
```

Mettez à jour le fichier de configuration correspondant pour chacun des environnements :

 * `config/environments/development.rb`
 * `config/environments/test.rb`
 * `config/environments/production.rb`


## Configuration de partage de ressources d'origine croisée
{: #ror-activestorage-cors}

Pour permettre à Rails d'accéder à votre compartiment, vous devez créer une configuration de partage de ressources d'origine croisée semblable à celle qui suit :

```xml
<CORSConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <CORSRule>
        <AllowedMethod>GET</AllowedMethod>
        <AllowedMethod>PUT</AllowedMethod>
        <AllowedMethod>POST</AllowedMethod>
        <AllowedOrigin>www.ibm.com</AllowedOrigin>
        <AllowedHeader>x-amz-*</AllowedHeader>
        <AllowedHeader>content-*</AllowedHeader>
    </CORSRule>
</CORSConfiguration>
```

Cette configuration permet aux demandes émises à partir de `www.ibm.com` d'exécuter des demandes `GET`, `PUT` et `POST` sur votre compartiment. Ajustez l'entrée `<AllowedOrigin>` en fonction des besoins de votre application.  

L'autorisation des en-têtes `x-amz-*` et `content-*` est également requise pour permettre à Rails d'interagir correctement avec votre compartiment. Pour plus d'informations sur le partage de ressources d'origine croisée, voir la rubrique [Référence d'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#create-a-cross-origin-resource-sharing-configuration-for-a-bucket). 
