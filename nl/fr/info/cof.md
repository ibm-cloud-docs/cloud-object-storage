---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# Utilisation de Cloud Object Storage avec des applications Cloud Foundry
{: #cloud-foundry}

{{site.data.keyword.cos_full}} peut être associé à des applications {{site.data.keyword.cfee_full}} pour fournir un contenu hautement disponible en utilisant des régions et des noeuds finaux. 

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} est une plateforme d'hébergement d'applications et de services dans le cloud. Vous pouvez instancier à la demande plusieurs plateformes isolées de niveau entreprise qui s'exécutent dans votre propre compte et qui peuvent être déployées sur du matériel partagé ou dédié. La plateforme facilite la mise à l'échelle des applications à mesure que la consommation augmente, ce qui simplifie l'environnement d'exécution et l'infrastructure afin que vous puissiez vous concentrer sur le développement.

La réussite de la mise en oeuvre d'une plateforme Cloud Foundry nécessite [une planification et une conception adéquates](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation) pour les ressources nécessaires et les besoins de l'entreprise. Découvrez comment vous l'[initier](/docs/cloud-foundry?topic=cloud-foundry-about#creating) à Cloud Foundry Enterprise Environment et suivez un [tutoriel](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started) de présentation.

### Regions
{: #cloud-foundry-regions}
Les [noeuds finaux régionaux](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) constituent une partie importante de l'environnement IBM Cloud. Vous pouvez créer des applications et des instances de service dans différentes régions avec la même infrastructure IBM Cloud pour la gestion des applications et la même vue des détails d'utilisation pour la facturation. Le fait de choisir une région IBM Cloud géographiquement proche de vous ou de vos clients vous permet de réduire les temps d'attente de données dans vos applications et d'abaisser les coûts. Les régions peuvent également être choisies pour répondre aux préoccupations en matière de sécurité ou aux exigences réglementaires. 

Avec {{site.data.keyword.cos_full}}, vous pouvez choisir de disperser des données dans un seul centre de données, une région entière, voir même une combinaison de régions en [sélectionnant le noeud final](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) dans lequel votre application envoie des demandes d'API.

### Alias et connexions de ressource
{: #cloud-foundry-aliases}

Un alias est une connexion entre votre service géré au sein d'un groupe de ressources et une application au sein d'une organisation ou d'un espace. Les alias sont des liens symboliques qui contiennent des références à des ressources distantes. Il permet l'interopérabilité et la réutilisation d'une instance dans toute la plateforme. Dans la console {{site.data.keyword.cloud_notm}}, la connexion (alias) est représentée comme une instance de service. Vous pouvez créer une instance d'un service dans un groupe de ressources, puis la réutiliser à partir de n'importe quelle région disponible en créant un alias dans une organisation ou un espace dans ces régions.

## Stockage des données d'identification en tant que variables VCAP 
{: #cloud-foundry-vcap}

Les données d'identification {{site.data.keyword.cos_short}} peuvent être stockées dans la variable d'environnement VCAP_SERVICES, qui peut être analysée pour être utilisée lors de l'accès au service {{site.data.keyword.cos_short}}. Les données d'identification contiennent des informations semblables à celles présentées dans l'exemple suivant :

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

La variable d'environnement VCAP_SERVICES peut ensuite être analysée dans votre application afin de permettre l'accès à votre contenu {{site.data.keyword.cos_short}}. Voici un exemple d'intégration de la variable d'environnement au SDK COS à l'aide de Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

Pour savoir comment utiliser le SDK afin d'accéder à {{site.data.keyword.cos_short}} avec des exemples de code, consultez les rubriques suivantes :

* [Utilisation de Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Utilisation de Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Utilisation de Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## Création de liaisons de service 
{: #cloud-foundry-bindings}

### Tableau de bord
{: #cloud-foundry-bindings-console}

Le moyen le plus simple de créer une liaison de service consiste à utiliser le [tableau de bord {{site.data.keyword.cloud}}](https://cloud.ibm.com/resources). 

1. Connectez-vous au [tableau de bord](https://cloud.ibm.com/resources). 
2. Cliquez sur votre application Cloud Foundry. 
3. Cliquez sur Connexions dans le menu de gauche.
4. Cliquez sur **Créer une connexion** à droite. 
5. Sur la page *Connecter un service compatible existant*, survolez votre service {{site.data.keyword.cos_short}} et cliquez sur **Connecter**.
6. Sur l'écran *Connecter un service activé pour IAM* qui s'affiche, sélectionnez le rôle d'accès, laissez cochée l'option Générer automatiquement pour l'ID de service, puis cliquez sur **Connecter**. 
7. L'application Cloud Foundry doit être reconstituée afin d'utiliser la nouvelle liaison de service. Cliquez sur **Reconstituer** pour lancer le processus. 
8. Une fois la reconstitution terminée, votre service Cloud Object Storage est disponible pour votre application.

La variable d'environnement VCAP_SERVICES des applications est automatiquement mise à jour avec les informations de service. Pour afficher la nouvelle variable :

1. Cliquez sur *Exécution* dans le menu de droite. 
2. Cliquez sur *Variables d'environnement*. 
3. Vérifiez que votre service COS est répertorié. 

### IBM Client Tools (interface CLI)
{: #cloud-foundry-bindings-cli}

1. Connectez-vous à l'interface CLI IBM Cloud. 
```
 ibmcloud login --apikey <your api key>
```

2. Ciblez votre environnement Cloud Foundry. 
```
 ibmcloud target --cf
```

3. Créez un alias de service pour votre service {{site.data.keyword.cos_short}}. 
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Créez une liaison de service entre votre alias {{site.data.keyword.cos_short}} et votre application Cloud Foundry et fournissez un rôle pour votre liaison. Les rôles valides sont les suivants :<br/><ul><li>Auteur</li><li>Lecteur</li><li>Gestionnaire</li><li>Administrateur</li><li>Opérateur</li><li>Afficheur</li><li>Editeur</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### IBM Client Tools (interface CLI) avec des données d'identification HMAC
{: #cloud-foundry-hmac}

Le code HMAC (Hash-based Message Authentication Code) est un mécanisme qui calcule un code d'authentification de message créé et utilise une paire de clés d'accès et secrète. Cette technique peut être utilisée pour vérifier l'intégrité et l'authenticité d'un message. Pour plus d'informations sur l'utilisation des [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials), voir la documentation {{site.data.keyword.cos_short}}. 

1. Connectez-vous à l'interface CLI IBM Cloud. 
```
 ibmcloud login --apikey <your api key>
```

2. Ciblez votre environnement Cloud Foundry. 
```
 ibmcloud target --cf
```

3. Créez un alias de service pour votre service {{site.data.keyword.cos_short}}. 
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Créez une liaison de service entre votre alias {{site.data.keyword.cos_short}} et votre application Cloud Foundry et fournissez un rôle pour votre liaison. <br/><br/>* **Remarque :** un paramètre supplémentaire * (`{"HMAC":true}`) *est nécessaire pour créer des données d'identification de service avec le mécanisme HMAC activé. *<br/><br/>Les rôles valides sont les suivants :<br/><ul><li>Auteur</li><li>Lecteur</li><li>Gestionnaire</li><li>Administrateur</li><li>Opérateur</li><li>Afficheur</li><li>Editeur</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### Liaison à {{site.data.keyword.containershort_notm}}
{: #cloud-foundry-k8s}

La procédure de création d'une liaison de service vers {{site.data.keyword.containershort}} est légèrement différente. 

*Dans le cadre de cette section, vous devrez également installer [jq, un processeur JSON de ligne de commande léger](https://stedolan.github.io/jq/){:new_window}.*

Vous devez fournir les informations suivantes et remplacer les valeurs de clé dans les commandes ci-dessous :

* `<service alias>` - nouveau nom d'alias pour le service COS
* `<cos instance name>` - nom de votre instance COS existante
* `<service credential name>` - nouveau nom de votre clé de service ou de vos données d'identification
* `<role>` - rôle à associer à votre clé de service (voir ci-dessus pour connaître les rôles valides, le rôle le plus souvent spécifié est `Auteur`)
* `<cluster name>` - nom de votre service de cluster Kubernetes existant
* `<secret binding name>` - cette valeur est générée lorsque COS est lié au service de cluster


1. Créez un alias de service pour votre instance COS. <br/><br/>* **Remarque :** une instance COS ne peut avoir qu'un seul alias de service*. 
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. Créez une nouvelle clé de service avec des droits d'accès à l'alias de service COS. 
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. Liez le service de cluster à COS. 
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. Vérifiez que l'alias de service COS est lié au cluster. 
```
ibmcloud cs cluster-services --cluster <cluster name>
```
La sortie ressemble à ceci :
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. Extrayez la liste des secrets de votre cluster et trouvez le secret de votre service COS. En général, il s'agit de `binding-` plus l'élément `<service alias>` que vous avez spécifié lors de l'étape 1 (c'est-à-dire `binding-sv-cos`). Utilisez cette valeur pour  `<secret binding name>` à l'étape 6. 
```
kubectl get secrets
```
La sortie sera semblable à ce qui suit :
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. Vérifiez que les données d'identification COS HMAC sont disponibles dans vos secrets de cluster. 
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
La sortie sera semblable à ce qui suit :
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
