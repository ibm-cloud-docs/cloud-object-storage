---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-05"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 
{:http: .ph data-hd-programlang='http'} 

# Suppression des données périmées à l'aide de règles d'expiration
{: #expiry}

Une règle d'expiration supprime les objets au terme d'une période définie (à partir de la date de création de l'objet). 

Vous pouvez définir le cycle de vie des objets à l'aide de la console Web, de l'API REST et des outils tiers qui sont intégrés à {{site.data.keyword.cos_full_notm}}. 

* Une règle d'expiration peut être ajoutée à un compartiment nouveau ou existant. 
* Une règle d'expiration existante peut être modifiée ou désactivée. 
* Une règle d'expiration récemment ajoutée ou modifiée s'applique à tous les objets nouveaux et existants dans le compartiment.
* Les opérations d'ajout ou de modification de règles de cycle de vie requièrent le rôle `Writer`.  
* Jusqu'à 1 000 règles de cycle de vie (archivage + expiration) peuvent être définies par compartiment. 
* Prévoyez jusqu'à 24 heures avant que les modifications apportées aux règles d'expiration prennent effet. 
* La portée de chaque règle d'expiration peut être limitée en définissant un filtre de préfixe facultatif à appliquer uniquement à un sous-ensemble d'objets dont les noms correspondent au préfixe. 
* Une règle d'expiration sans filtre de préfixe s'applique à tous les objets du compartiment. 
* Le délai d'expiration d'un objet, spécifié en nombre de jours, est calculé à partir du moment où l'objet a été créé et est arrondi au jour suivant à minuit (UTC). Par exemple, si la règle d'expiration d'un compartiment prévoit qu'un ensemble d'objets arrive à échéance dix jours après la date de création, un objet qui a été créé le 15 avril 2019 à 5 heures 10 (UTC) expirera le 26 avril 2019 à minuit (UTC).  
* Les règles d'expiration de chaque compartiment sont évaluées une fois toutes les 24 heures. Tout objet admissible à l'expiration (d'après la date d'expiration qui lui est associée) sera mis en file d'attente pour suppression. La suppression des objets arrivés à expiration commence le jour suivant et prend généralement moins de 24 heures. Une fois que des objets sont supprimés, vous cessez d'être facturé pour le stockage qui leur est associé. 

Les actions d'expiration des objets soumis à une règle de conservation Immutable Object Storage pour un compartiment sont retardées jusqu'à ce que la règle de conservation ne soit plus appliquée.
{: important}

## Attributs des règles d'expiration
{: #expiry-rules-attributes}

Chaque règle d'expiration comporte les attributs suivants :

### ID
L'ID d'une règle doit être unique dans la configuration de cycle de vie du compartiment. 

### Expiration
Le bloc d'expiration contient les détails qui régissent la suppression automatique des objets. Il peut s'agir d'une date spécifique située dans le futur ou d'une période au terme de laquelle de nouveaux objets sont écrits. 

### Prefix
Chaîne facultative qui sera mise en correspondance avec le préfixe du nom de l'objet dans le compartiment. Une règle avec un préfixe ne s'applique qu'aux objets concordants. Vous pouvez utiliser plusieurs règles pour différentes actions d'expiration et pour différents préfixes dans le même compartiment. Par exemple, dans une même configuration de cycle de vie, une règle peut supprimer tous les objets commençant par `logs/` au terme d'une période de 30 jours et une seconde règle peut supprimer les objets commençant par `vidéo/` au terme d'une période de 365 jours.  

### Status
Une règle peut être activée ou désactivée. Une règle est active uniquement lorsqu'elle est activée. 

## Exemples de configuration de cycle de vie

Avec la configuration suivante, tous les nouveaux objets arrivent à expiration au bout de 30 jours :

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-after-30-days</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>30</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Avec la configuration suivante, tous les objets dotés du préfixe `foo/` arrivent à expiration le 1er juin 1020 :

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>delete-on-a-date</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Date>2020-06-01T00:00:00.000Z</Date>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

Vous pouvez également combiner des règles de transition et d'expiration. Avec la configuration suivante, tous les objets sont archivés 90 jours après leur création et tous les objets comportant le préfixe `foo/` sont supprimés au bout de 180 jours : 

```xml
<LifecycleConfiguration>
  <Rule>
		<ID>archive-first</ID>
		<Filter />
		<Status>Enabled</Status>
    <Transition>
      <Days>90</Days>
      <StorageClass>GLACIER</StorageClass>
    </Transition>
	</Rule>
	<Rule>
		<ID>then-delete</ID>
    <Filter>
      <Prefix>foo/</Prefix>
    </Filter>
		<Status>Enabled</Status>
		<Expiration>
			<Days>180</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```

## Utilisation de la console
{: #expiry-using-console}

Lorsque vous créez un nouveau compartiment, cochez la case **Ajouter une règle d'expiration**. Cliquez ensuite sur **Ajouter une règle** pour créer la nouvelle règle d'expiration. Vous pouvez ajouter jusqu'à cinq règles lors de la création d'un compartiment, et des règles supplémentaires peuvent être ajoutées ultérieurement. 

Pour un compartiment existant, sélectionnez **Configuration** dans le menu de navigation et cliquez sur **Ajouter une règle** sous la section _Règle d'expiration_. 

## Utilisation de l'API et des SDK
{: #expiry-using-api-sdks}

Vous pouvez gérer des règles d'expiration à l'aide d'un programme à l'aide de l'API REST ou des SDK IBM COS. Sélectionnez le format des exemples en sélectionnant une catégorie dans le sélecteur de contexte.

### Ajout d'une règle d'expiration à la configuration de cycle de vie d'un compartiment
{: #expiry-api-create}

**Référence d'API REST**
{: http}

Cette implémentation de l'opération `PUT` utilise le paramètre de requête `lifecycle` pour définir les paramètres de cycle de vie du compartiment. Cette opération permet de créer une définition de règle de cycle de vie unique pour un compartiment. La règle est définie comme un ensemble de règles composé des paramètres suivants : `ID`, `Status`, `Filter` et `Expiration`.
{: http}
 
Les utilisateurs Cloud IAM doivent disposer du rôle `Writer` afin de pouvoir retirer une règle de cycle de vie d'un compartiment. 

Les utilisateurs d'infrastructure classique doivent disposer des droits d'accès `Owner` sur le compartiment afin de pouvoir retirer une règle de cycle de vie d'un compartiment.

En-tête | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Chaîne | **Obligatoire** : Hachage MD5 128 bits codé en base 64 du contenu, utilisé comme contrôle d'intégrité pour s'assurer que le contenu n'a pas été modifié alors qu'il était en transit.  
{: http}

Le corps de la demande doit contenir un bloc XML avec le schéma suivant :
{: http}

|Elément| Type   |Enfants|Ancêtre|Contrainte|
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` |Conteneur| `Rule` |Néant |Limite 1.|
| `Rule` |Conteneur| `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | Limite 1000.  |
| `ID` | Chaîne |Néant | `Rule` | Doit être composé de (`a-z,`A-Z0-9`) et des symboles suivants : `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter` | Chaîne | `Prefix` | `Rule` | Doit contenir un élément `Prefix`. |
| `Prefix` | Chaîne |Néant | `Filter` | La règle s'applique à tous les objets dont les clés correspondent à ce préfixe. |
| `Expiration` | `Conteneur` | `Days` ou `Date` | `Rule` |Limite 1.|
| `Days` |Entier non négatif|Néant | `Expiration` | Doit être une valeur supérieure à 0. |
| `Date` | Date |Néant | `Expiration` |Doit être au format ISO 8601. |
{: http}

Le corps de la demande doit contenir un bloc XML avec le schéma présenté dans le tableau (voir l'exemple 1).
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Exemple 1. Exemple XML extrait du corps de la demande. " caption-side="bottom"}
{: http}

**Syntaxe**
{: http}

```yaml
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Exemple 2. Notez l'emploi de barres obliques et de points dans cet exemple de syntaxe. " caption-side="bottom"}
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```yaml
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</Status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Exemple 3. Exemples d'en-tête de demande illustrant la création d'une configuration de cycle de vie d'objet." caption-side="bottom"}
{: http}

**Exemple de code à utiliser avec le SDK COS NodeJS**
{: javascript}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);
var date = new Date('June 16, 2019 00:00:00');

var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'OPTIONAL_STRING_VALUE',
        Filter: {}, /* required */
        Expiration:
        {
          Date: date
        }
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

**Exemple de code à utiliser avec le SDK COS Python**
{: python}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.
{: python}

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'Status': 'Enabled',
                'Filter': {},
                'Expiration':
                {
                    'Days': 123
                },
            },
        ]
    }
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

**Exemple de code à utiliser avec le SDK COS Java**
{: java}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";
            
            String storageClass = "us-south";
            String location = "us"; 
 
            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);
            
            // Define a rule for exiring items in a bucket
            int days_to_delete = 10;
            BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule()
                    .withId("Delete rule")
                    .withExpirationInDays(days_to_delete)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);
            
            // Add the rule to a new BucketLifecycleConfiguration.
            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(Arrays.asList(rule));
            
            // Use the client to set the LifecycleConfiguration on the bucket.
            _cosClient.setBucketLifecycleConfiguration(bucketName, configuration);   
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
    }
```
{: codeblock}
{: java}
{: caption="Exemple 1. Exemples de code illustrant la création d'une configuration de cycle de vie. " caption-side="bottom"}

### Examen d'une configuration de cycle de vie d'un compartiment, y compris l'expiration
{: #expiry-api-view}

Cette implémentation de l'opération `GET` utilise le paramètre de requête `lifecycle` pour examiner les paramètres de cycle de vie du compartiment. Une réponse HTTP `404` sera renvoyée si aucune configuration de cycle de vie n'est présente.
{: http}

Les utilisateurs Cloud IAM doivent disposer du rôle `Reader` afin de pouvoir retirer une règle de cycle de vie d'un compartiment. 

Les utilisateurs d'infrastructure classique doivent disposer des droits d'accès `Read` sur le compartiment afin de pouvoir retirer une règle de cycle de vie d'un compartiment.

En-tête | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | Chaîne | **Obligatoire** : Hachage MD5 128 bits codé en base 64 du contenu, utilisé comme contrôle d'intégrité pour s'assurer que le contenu n'a pas été modifié alors qu'il était en transit.  
{: http}

**Syntaxe**
{: http}

```yaml
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Exemple 5. Notez l'emploi de barres obliques et de points dans cet exemple de syntaxe. " caption-side="bottom"}
{: codeblock}
{: http}

**Exemple d'en-tête de demande**
{: http}

```yaml
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="Exemple 6. Exemples d'en-tête de demande illustrant la création d'une configuration de cycle de vie d'objet. " caption-side="bottom"}
{: http}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate. 

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').get_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.  

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Use the client to read the configuration
            BucketLifecycleConfiguration config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            
            System.out.println(config.toString());
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }
        
    }
```
{: codeblock}
{: java}
{: caption="Exemple 2. Exemples de code illustrant l'inspection d'une configuration de cycle de vie. " caption-side="bottom"}

### Suppression d'une configuration de cycle de vie d'un compartiment, y compris l'expiration
{: #expiry-api-delete}

Cette implémentation de l'opération `DELETE` utilise le paramètre de requête `lifecycle` pour examiner les paramètres de cycle de vie du compartiment. Toutes les règles de cycle de vie associées au compartiment seront supprimées. Les transitions définies par les règles ne seront plus effectuées pour les nouveaux objets. Toutefois, les règles de transition existantes seront conservées pour les objets qui avaient déjà été écrits dans le compartiment avant la suppression des règles. Les règles d'expiration n'existeront plus. Une réponse HTTP `404` sera renvoyée si aucune configuration de cycle de vie n'est présente.
{: http}

Les utilisateurs Cloud IAM doivent disposer du rôle `Writer` afin de pouvoir retirer une règle de cycle de vie d'un compartiment. 

Les utilisateurs d'infrastructure classique doivent disposer des droits d'accès `Owner` sur le compartiment afin de pouvoir retirer une règle de cycle de vie d'un compartiment.

**Syntaxe**
{: http}

```yaml
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Exemple 7. Notez l'emploi de barres obliques et de points dans cet exemple de syntaxe. " caption-side="bottom"}
{: codeblock}
{: http}

**Exemple d'en-tête de demande**
{: http}

```yaml
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-Length: 305
```
{: codeblock}
{: caption="Exemple 8. Exemples d'en-tête de demande illustrant la création d'une configuration de cycle de vie d'objet. " caption-side="bottom"}
{: http}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.
{: javascript}

```js
var aws = require('ibm-cos-sdk');
var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
var config = {
    endpoint: ep,
    apiKeyId: 'ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: 'crn:v1:bluemix:public:cloud-object-storage:global:a/<CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::',
};
var s3 = new aws.S3(config);

var params = {
  Bucket: 'STRING_VALUE' /* required */
};

s3.deleteBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.  

```python
import sys
import ibm_boto3
from ibm_botocore.client import Config

api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE"
service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
auth_endpoint = "https://iam.cloud.ibm.com/identity/token"
service_endpoint = "https://s3.us-south.cloud-object-storage.appdomain.cloud"

cos = ibm_boto3.resource('s3',
                         ibm_api_key_id=api_key,
                         ibm_service_instance_id=service_instance_id,
                         ibm_auth_endpoint=auth_endpoint,
                         config=Config(signature_version='oauth'),
                         endpoint_url=service_endpoint)

response = cos.Bucket('<name-of-bucket>').delete_bucket_lifecycle_configuration(
    Bucket='string'
)

print("Bucket lifecyle: {0}".format(response))
```
{: codeblock}
{: python}

L'utilisation des SDK {{site.data.keyword.cos_full}} nécessite uniquement d'appeler les fonctions appropriées avec les paramètres corrects et la configuration adéquate.
{: java}

```java
package com.ibm.cloud;

    import java.sql.Timestamp;
    import java.util.List;
    import java.util.Arrays;

    import com.ibm.cloud.objectstorage.ClientConfiguration;
    import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
    import com.ibm.cloud.objectstorage.auth.AWSCredentials;
    import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
    import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
    import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
    import com.ibm.cloud.objectstorage.services.s3.model.Bucket;
    import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
    import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsRequest;
    import com.ibm.cloud.objectstorage.services.s3.model.ObjectListing;
    import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
    import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;

    public class App
    {
        private static AmazonS3 _cosClient;

        /**
         * @param args
         */
        public static void main(String[] args)
        {
            SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/identity/token";
            String bucketName = "<sample-bucket-name>";
            String api_key = "ZRZDoNoUseOLL7bRO8SAMPLEHPUzUL_-fsampleyYE";
            String service_instance_id = "85SAMPLE-eDOb-4NOT-bUSE-86nnnb31eaxx"
            String endpoint_url = "https://s3.us-south.cloud-object-storage.appdomain.cloud";

            String storageClass = "us-south";
            String location = "us";

            _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

            // Delete the configuration.
            _cosClient.deleteBucketLifecycleConfiguration(bucketName);
            
            // Verify that the configuration has been deleted by attempting to retrieve it.
            config = _cosClient.getBucketLifecycleConfiguration(bucketName);
            String s = (config == null) ? "Configuration has been deleted." : "Configuration still exists.";
            System.out.println(s);
        }
        
        /**
         * @param bucketName
         * @param clientNum
         * @param api_key
         * @param service_instance_id
         * @param endpoint_url
         * @param location
         * @return AmazonS3
         */
        public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
        {
            AWSCredentials credentials;
            credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

            ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
            clientConfig.setUseTcpKeepAlive(true);

            AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                    .withClientConfiguration(clientConfig).build();
            return cosClient;
        }

    }
```
{: codeblock}
{: java}
{: caption="Exemple 3. Exemples de code illustrant la suppression d'une configuration de cycle de vie. " caption-side="bottom"}

## Etapes suivantes
{: #expiry-next-steps}

L'expiration n'est qu'un des nombreux concepts de cycle de vie disponibles pour {{site.data.keyword.cos_full_notm}}. Vous pouvez explorer davantage chacun des concepts abordés dans cette introduction en vous rendant sur la page de la plateforme [{{site.data.keyword.cloud_notm}}](https://cloud.ibm.com/). 

