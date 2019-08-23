---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: worm, immutable, policy, retention, compliance

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

# Utilisation de la fonction Immutable Object Storage
{: #immutable}

La fonction Immutable Object Storage permet aux clients de conserver des enregistrements électroniques et de maintenir l'intégrité des données de manière non réinscriptible et non effaçable jusqu'à la fin de la durée de conservation et le retrait des éventuelles conservations légales. Cette fonction peut être utilisée par tout client ayant besoin de conserver des données à long terme dans son environnement, y compris, mais pas seulement, les organisations des industries suivantes :

 * Finances
 * Santé
 * Archivage de contenu multimédia
 * Industries spécialisées dans la recherche de moyens visant à empêcher la modification ou la suppression privilégiée d'objets ou de documents

Les fonctions sous-jacentes peuvent également être utilisées par des organisations qui sont spécialisées dans la gestion de documents financiers, tels que les transactions courtier-fournisseur, et qui peuvent avoir besoin de conserver les objets dans un format non réinscriptible et non effaçable. 

La fonction Immutable Object Storage n'est disponible que dans certaines régions. Pour plus d'informations, voir [Services intégrés](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability). De plus, elle requiert un plan de tarification standard. Pour plus d'informations, voir la section sur la [tarification](https://www.ibm.com/cloud/object-storage).
{:note}

L'option Transfert haut débit Aspera ne peut pas être utilisée avec des compartiments auxquels une règle de conservation est associée.
{:important}

## Terminologie et utilisation
{: #immutable-terminology}

### Durée de conservation
{: #immutable-terminology-period}

Durée pendant laquelle un objet doit rester stocké dans le compartiment COS. 

### Règle de conservation
{: #immutable-terminology-policy}

Une règle de conservation est activée au niveau du compartiment COS. Les durées de conservation minimale, maximale et par défaut sont définies par cette règle et s'appliquent à tous les objets contenus dans le compartiment. 

La durée de conservation minimale est la durée minimale pendant laquelle un objet doit être conservé dans le compartiment. 

La durée de conservation maximale est la durée maximale pendant laquelle un objet doit être conservé dans le compartiment. 

Si un objet est stocké dans le compartiment sans qu'aucune durée de conservation personnalisée ne soit spécifiée, la durée de conservation par défaut est utilisée. La durée de conservation minimale doit être inférieure ou égale à la durée de conservation par défaut, laquelle doit être inférieure ou égale à la durée de conservation maximale. 

Remarque : une durée de conservation maximale de 1000 ans peut être spécifiée pour les objets. 

Remarque : pour créer une règle de conservation sur un compartiment, vous aurez besoin du rôle Gestionnaire. Pour plus d'informations, voir [Droits des compartiments](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions#bucket-permissions). 

### Conservation légale 
{: #immutable-terminology-hold}

La suppression de certains enregistrements (objets) peut être empêchée même lorsque la durée de conservation a expiré. Par exemple, une revue légale en attente d'achèvement peut avoir besoin d'accéder à des enregistrements pour une durée prolongée qui dépasse la durée de conservation initialement définie pour l'objet. Dans un tel scénario, un indicateur de conservation légale peut être appliqué au niveau de l'objet. Une ou plusieurs conservations légales peuvent être appliquées à des objets durant les envois par téléchargement initiaux vers un compartiment COS ou après l'ajout d'un objet. Remarque : un maximum de 100 conservations légales peut être appliqué par objet. 

### Conservation indéfinie
{: #immutable-terminology-indefinite}

Permet à l'utilisateur de définir l'objet de telle manière qu'il soit stocké indéfiniment jusqu'à ce qu'une nouvelle durée de conservation soit appliquée. Cette valeur est définie au niveau des objets. 

### Conservation en fonction d'un événement
{: #immutable-terminology-events}

La fonction Immutable Object Storage permet aux utilisateurs de définir une conservation sans limite de durée sur l'objet s'ils ne sont pas certains de la durée finale de la période de conservation pour leur cas d'utilisation ou s'ils souhaitent utiliser la fonction Conservation en fonction d'un événement. Une fois la conservation sans limite de durée définie, les applications utilisateur peuvent modifier la conservation de l'objet en lui affectant une durée de conservation limitée. Par exemple, une société a établi une règle qui prévoit de conserver des dossiers d'employés pendant trois années après le départ de ceux-ci. Lorsqu'un employé est embauché dans l'entreprise, les dossiers qui lui sont associés peuvent être conservés sans limite de durée. Lorsque l'employé quitte l'entreprise, la conservation sans limite de durée est convertie en une conservation avec une durée limitée de trois ans à partir du moment présent, comme stipulé par la règle de la société. L'objet est ensuite protégé pendant trois ans après la modification de la durée de conservation. Un utilisateur ou une application tierce peut changer la conservation sans limite de durée et la remplacer par une conservation avec limite de durée à l'aide d'un SDK ou d'une API REST.

### Conservation permanente
{: #immutable-terminology-permanent}

La conservation permanente ne peut être activée qu'au niveau d'un compartiment COS avec la règle de conservation activée et les utilisateurs peuvent sélectionner l'option de durée de conservation permanente lors des envois par téléchargement d'objet. Ce processus est irréversible, par conséquent, les objets envoyés par téléchargement à l'aide d'une durée de conservation permanente **ne peuvent pas être supprimés**. Il incombe aux utilisateurs de vérifier si le stockage **permanent** d'objets à l'aide de compartiments COS et d'une règle de conservation répond à un besoin légitime. 


Lorsque vous utilisez la fonction Immutable Object Storage, il vous appartient de veiller à ce que votre compte IBM Cloud soit en règle avec chacune des règles et instructions IBM Cloud tant que les données sont soumises à une règle de conservation. Pour plus d'informations, voir les conditions du service IBM Cloud.
{:important}

## Fonction Immutable Object Storage et remarques relatives à diverses réglementations
{: #immutable-regulation}

Lorsque la fonction Immutable Object Storage est utilisée, il incombe au client de s'assurer que la fonction qui est présentée peut être optimisée pour respecter les principales règles relatives au stockage et à la conservation de dossiers électroniques généralement régies par : 

  * [la règle SEC (Securities and Exchange Commission) 17a-4(f)](https://www.ecfr.gov/cgi-bin/text-idx?SID=b6b7a79d18d000a733725e88d333ddb5&mc=true&node=pt17.4.240&rgn=div5#se17.4.240_117a_64),
  * [la règle FINRA (Financial Industry Regulatory Authority) 4511(c)](http://finra.complinet.com/en/display/display_main.html?rbid=2403&element_id=9957), et
  * [la règle CFTC (Commodity Futures Trading Commission) 1.31(c)-(d)](https://www.ecfr.gov/cgi-bin/text-idx?SID=2404f765a6f79e0b7fcf05b6844046cb&mc=true&node=se17.1.1_131&rgn=div8)

Pour aider les clients à prendre des décisions réfléchies, IBM a engagé la société Cohasset Associates Inc. pour effectuer une évaluation indépendante de la fonction Immutable Object Storage d'IBM. Reportez-vous au [rapport](https://www.ibm.com/downloads/cas/JBDNP0KV) rendu par la société Cohasset Associates Inc. pour connaître les détails de l'évaluation de la fonction Immutable Object Storage d'IBM Cloud Object Storage. 

### Audit de l'accès et des transactions
{: #immutable-audit}
Les données de journal d'accès pour la fonction Immutable Object Storage afin d'examiner les modifications apportées aux paramètres de conservation, à la durée de conservation des objets et à l'application des conservations légales sont disponibles au cas par cas en ouvrant un ticket de service client.

## Utilisation de la console
{: #immutable-console}

Des règles de conservation peuvent être ajoutées à des compartiments vides nouveaux ou existants, et ne peuvent pas être retirées. Pour un nouveau compartiment, prenez soin de créer le compartiment dans une [région prise en charge](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#service-availability), puis choisissez l'option **Ajouter une règle de conservation**. Dans le cas d'un compartiment existant, vérifiez que celui-ci ne comporte pas d'objet, puis accédez aux paramètres de configuration et cliquez sur le bouton **Créer une règle** au-dessous de la section Règle de conservation de compartiment. Dans les deux cas, définissez des durées de conservation minimale, maximale et par défaut. 

## Utilisation de l'API REST, de bibliothèques et de SDK
{: #immutable-sdk}

Plusieurs nouvelles API ont été introduites dans les SDK IBM COS pour fournir une prise en charge des applications qui fonctionnent avec les règles de conservation. Sélectionnez un langage (HTTP, Java, Javascript ou Python) en haut de cette page pour visualiser des exemples d'utilisation du SDK COS approprié.  

Notez que tous les exemples de code partent du principe qu'il existe un objet client appelé `cos` pouvant appeler les différentes méthodes. Pour plus de détails sur la création de clients, voir les guides SDK spécifiques. 

Toutes les valeurs de date utilisées pour définir des durées de conservation sont exprimées au format GMT. Un en-tête `Content-MD5` est requis pour garantir l'intégrité des données et est envoyé automatiquement lors de l'utilisation d'un SDK.
{:note}

### Ajout d'une règle de conservation à un compartiment existant
{: #immutable-sdk-add-policy}
Cette implémentation de l'opération `PUT` utilise le paramètre de requête `protection` pour définir les paramètres de conservation d'un compartiment existant. Cette opération vous permet de définir ou modifier la période de conservation minimale, par défaut et maximale. Cette opération vous permet également de modifier l'état de protection du compartiment.  

Les objets écrits dans un compartiment protégé ne peuvent pas être supprimés tant que la période de protection n'est pas arrivée à expiration et que les conservations légales associées à l'objet n'ont pas toutes été retirées. La valeur de conservation par défaut du compartiment est attribuée à un objet, sauf si une valeur spécifique à un objet est fournie lors de la création de l'objet. Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet.  

Les valeurs minimales et maximales prises en charge pour les paramètres de durée de conservation `MinimumRetention`, `DefaultRetention` et `MaximumRetention` sont respectivement de 0 jours et 365243 jours (1000 ans).  

Un en-tête `Content-MD5` est requis. Cette opération n'utilise pas d'autres paramètres de requête.


Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).{:tip}

{: http}

**Syntaxe**
{: http}

```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addProtectionConfigurationToBucket(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    cos.setBucketProtection(bucketName, newConfig);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}

public static void addProtectionConfigurationToBucketWithRequest(String bucketName) {
    System.out.printf("Adding protection to bucket: %s\n", bucketName);

    BucketProtectionConfiguration newConfig = new BucketProtectionConfiguration()
        .withStatus(BucketProtectionStatus.Retention)
        .withMinimumRetentionInDays(10)
        .withDefaultRetentionInDays(100)
        .withMaximumRetentionInDays(1000);

    SetBucketProtectionConfigurationRequest newRequest = new SetBucketProtectionConfigurationRequest()
        .withBucketName(bucketName)
        .withProtectionConfiguration(newConfig);

    cos.setBucketProtectionConfiguration(newRequest);

    System.out.printf("Protection added to bucket %s\n", bucketName);
}
```
{: codeblock}
{: java}

### Vérification de la règle de conservation d'un compartiment
{: #immutable-sdk-get}

Cette implémentation d'une opération GET extrait les paramètres de conservation pour un compartiment existant.
{: http}

**Syntaxe**
{: http}

```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Example response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

Si aucune configuration de protection n'est associée au compartiment, le serveur répond en émettant un statut désactivé.
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void getProtectionConfigurationOnBucket(String bucketName) {
    System.out.printf("Retrieving protection configuration from bucket: %s\n", bucketName;

    BucketProtectionConfiguration config = cos.getBucketProtection(bucketName);

    String status = config.getStatus();

    System.out.printf("Status: %s\n", status);

    if (!status.toUpperCase().equals("DISABLED")) {
        System.out.printf("Minimum Retention (Days): %s\n", config.getMinimumRetentionInDays());
        System.out.printf("Default Retention (Days): %s\n", config.getDefaultRetentionInDays());
        System.out.printf("Maximum Retention (Days): %s\n", config.getMaximumRetentionInDays());
    }
}
```
{: codeblock}
{: java}

### Envoi par téléchargement d'un objet dans un compartiment auquel une règle de conservation est associée
{: #immutable-sdk-upload}

Cette amélioration de l'opération `PUT` permet d'ajouter trois nouveaux en-têtes de demande, deux pour spécifier la durée de conservation de différentes manières, et un pour ajouter une conservation légale au nouvel objet. De nouvelles erreurs sont définies pour signaler les valeurs non admises contenues dans les nouveaux en-têtes, et si un objet est soumis à une durée de conservation, toute tentative d'écrasement échoue.
{: http}

Lorsqu'ils sont écrasés, les objets des compartiments associés à une règle de conservation auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 

Un en-tête `Content-MD5` est requis.
{: http}

Ces en-têtes s'appliquent à un objet POST ainsi qu'aux demandes d'envoi par téléchargement en plusieurs parties. Si vous envoyez par téléchargement un objet en plusieurs parties, chaque partie requiert un en-tête `Content-MD5`.
{: http}

|Valeur	| Type	| Description |
| --- | --- | --- | 
|`Retention-Period` | Entier non négatif (secondes) | Durée de conservation, exprimée en secondes, pendant laquelle stocker l'objet. L'objet ne peut être ni écrasé, ni supprimé tant que la durée de conservation n'est pas écoulée. Si cette zone et la zone `Retention-Expiration-Date` sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la période `DefaultRetention` du compartiment est utilisée. Zéro (`0`) est une valeur légale qui part du principe que la durée de conservation minimale du compartiment est également fixée à `0`. |
| `Retention-expiration-date` | Date (format ISO 8601) | Date à laquelle la suppression ou la modification de l'objet devient légale. Vous pouvez uniquement spécifier cette zone ou bien la zone d'en-tête Retention-Period. Si ces deux zones sont spécifiées, un code d'erreur `400` est renvoyé. Si aucune de ces deux zones n'est spécifiée, la durée de conservation par défaut du compartiment est utilisée. |
| `Retention-legal-hold-id` |Chaîne| Conservation légale unique à appliquer à l'objet. Une conservation légale est une chaîne comportant Y caractères. L'objet ne peut être ni écrasé ni supprimé tant que les conservations légales associées à l'objet n'ont pas toutes été retirées. |

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    
    cos.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text, 
        RetentionLegalHoldId=legal_hold_id)

    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))
  
def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos.copy_object(
        Bucket=destination_bucket_name, 
        Key=new_object_name, 
        CopySource=copy_source, 
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))

    cos.complete_multipart_upload(
        Bucket=bucket_name, 
        Key=object_name,
        MultipartUpload={
            "Parts":[{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))
    
    args = {
        "RetentionPeriod": retention_period
    }

    cos.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void putObjectAddLegalHold(String bucketName, String objectName, String fileText, String legalHoldId) {
    System.out.printf("Add legal hold %s to %s in bucket %s with a putObject operation.\n", legalHoldId, objectName, bucketName);

    InputStream newStream = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(fileText.length());

    PutObjectRequest req = new PutObjectRequest(
        bucketName,
        objectName,
        newStream,
        metadata
    );
    req.setRetentionLegalHoldId(legalHoldId);

    cos.putObject(req);

    System.out.printf("Legal hold %s added to object %s in bucket %s\n", legalHoldId, objectName, bucketName);
}

public static void copyProtectedObject(String sourceBucketName, String sourceObjectName, String destinationBucketName, String newObjectName) {
    System.out.printf("Copy protected object %s from bucket %s to %s/%s.\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);

    CopyObjectRequest req = new CopyObjectRequest(
        sourceBucketName,
        sourceObjectName,
        destinationBucketName,
        newObjectName
    );
    req.setRetentionDirective(RetentionDirective.COPY);


    cos.copyObject(req);

    System.out.printf("Protected object copied from %s/%s to %s/%s\n", sourceObjectName, sourceBucketName, destinationBucketName, newObjectName);
}
```
{: codeblock}
{: java}

### Ajout ou retrait d'une conservation légale dans un objet
{: #immutable-sdk-legal-hold}

Cette implémentation de l'opération `POST` utilise le paramètre de requête `legalHold` ainsi que les paramètres de requête `add` et `remove` pour ajouter ou retirer une conservation légale d'un objet protégé dans un compartiment protégé.
{: http}

L'objet peut prendre en charge 100 conservations légales :

*  Un identificateur de conservation légale est une chaîne comprise entre 1 et 64 caractères. Les caractères valides sont des lettres, des chiffres, `!`, `_`, `.`, `*`, `(`, `)`, `-` et `.
* Si la tentative d'ajout de la conservation légale spécifiée dépasse le seuil de 100 conservations légales affectées à l'objet, l'opération d'ajout échoue et un code d'erreur `400` est renvoyé. 
* Si un identificateur est trop long, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur contient des caractères non valides, il n'est pas ajouté à l'objet et un code d'erreur `400` est renvoyé. 
* Si un identificateur est déjà en cours d'utilisation sur un objet, la conservation légale existante n'est pas modifiée et la réponse indique que l'identificateur était déjà utilisé et renvoie un code d'erreur `409`. 
* Si un objet ne comporte pas de métadonnées de durée de conservation, un code d'erreur `400` est renvoyé et l'ajout ou le retrait d'une conservation légale ne sont pas autorisés. 

La présence d'un en-tête de durée de conservation est obligatoire, sinon, une erreur `400` est renvoyée.
{: http}

L'utilisateur qui effectue l'ajout ou la suppression d'une conservation légale doit disposer des droits d'accès `Manager` pour ce compartiment. 

Un en-tête `Content-MD5` est requis. Cette opération n'utilise pas d'éléments de contenu propres aux opérations.
{: http}

**Syntaxe**
{: http}

```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Exemple de demande**
{: http}

```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void addLegalHoldToObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Adding legal hold %s to object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.addLegalHold(
        bucketName,
        objectName,
        legalHoldId
    );

    System.out.printf("Legal hold %s added to object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}

public static void deleteLegalHoldFromObject(String bucketName, String objectName, String legalHoldId) {
    System.out.printf("Deleting legal hold %s from object %s in bucket %s\n", legalHoldId, objectName, bucketName);

    cos.deleteLegalHold(
        bucketName,
        objectName,
        legalHoldId
    );

    System.out.printf("Legal hold %s deleted from object %s in bucket %s!\n", legalHoldId, objectName, bucketName);
}
```
{: codeblock}
{: java}

### Prolongement de la durée de conservation d'un objet
{: #immutable-sdk-extend}

Cette implémentation de l'opération `POST` utilise le paramètre de requête `extendRetention` pour prolonger la durée de conservation d'un objet protégé dans un compartiment protégé.
{: http}

La durée de conservation d'un objet ne peut être que prolongée. La valeur actuellement configurée ne peut pas être diminuée. 

La valeur de prolongement de la conservation est définie de l'une des trois façons suivantes :

* En ajoutant une durée supplémentaire par rapport à la valeur en cours (`Additional-Retention-Period` ou méthode similaire) 
* En définissant une nouvelle période de prolongement, exprimée en secondes (`Extend-Retention-From-Current-Time` ou méthode similaire) 
* En définissant une nouvelle date d'expiration de la conservation de l'objet (`New-Retention-Expiration-Date` ou méthode similaire) 

La durée de conservation en cours stockée dans les métadonnées de l'objet est soit augmentée de la durée supplémentaire indiquée, soit remplacée par la nouvelle valeur, en fonction du paramètre défini dans la demande `extendRetention`. Dans tous les cas, le paramètre de prolongement de la conservation est vérifié par rapport à la durée de conservation en cours et le paramètre de prolongement n'est accepté que si la durée de conservation mise à jour est supérieure à la durée de conservation en cours. 

Lorsqu'ils sont écrasés, les objets des compartiments protégés auxquels plus aucune durée de conservation ne s'applique (la durée de conservation est arrivée à expiration et aucune conservation légale n'est associée à l'objet) sont de nouveau soumis à une durée de conservation. La nouvelle durée de conservation peut être fournie dans la demande d'écrasement de l'objet ou bien la durée de conservation par défaut du compartiment est attribuée à l'objet. 

**Syntaxe**
{: http}

```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**Exemple de demande**
{: http}

```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:50:00GMT
Connection: close
```
{: codeblock}
{: http}

```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void extendRetentionPeriodOnObject(String bucketName, String objectName, Long additionalSeconds) {
    System.out.printf("Extend the retention period on %s in bucket %s by %s seconds.\n", objectName, bucketName, additionalSeconds);

    ExtendObjectRetentionRequest req = new ExtendObjectRetentionRequest(
        bucketName,
        objectName)
        .withAdditionalRetentionPeriod(additionalSeconds);

    cos.extendObjectRetention(req);

    System.out.printf("New retention period on %s is %s\n", objectName, additionalSeconds);
}
```
{: codeblock}
{: java}

### Création de la liste des conservations légales associées à un objet
{: #immutable-sdk-list-holds}

Cette implémentation de l'opération `GET` utilise le paramètre de requête `legalHold` pour renvoyer la liste de conservations légales associées à un objet et l'état de conservation connexe dans un corps de réponse XML.
{: http}

Cette opération renvoie les éléments suivants :

* La date de création de l'objet
* La durée de conservation, exprimée en secondes
* La date d'expiration de la conservation, calculée sur la base de la durée de conservation et de la date de création
* La liste des conservations légales
* L'identificateur de conservation légale
* L'horodatage correspondant à l'application de la conservation légale

Si aucune conservation légale n'est associée à l'objet, un élément `LegalHoldSet` vide est renvoyé.
Si aucune durée de conservation n'est spécifiée pour l'objet, un code d'erreur `404` est renvoyé. 

**Syntaxe**
{: http}

```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Exemple de demande**
{: http}

```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Fri, 8 Dec 2018 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**Exemple de réponse**
{: http}

```xml
HTTP/1.1 200 OK
Date: Fri, 8 Dec 2018 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Fri, 8 Sep 2018 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Fri, 8 Sep 2018 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

```py 
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

```java
public static void listLegalHoldsOnObject(String bucketName, String objectName) {
    System.out.printf("List all legal holds on object %s in bucket %s\n", objectName, bucketName);

    ListLegalHoldsResult result = cos.listLegalHolds(
        bucketName,
        objectName
    );

    System.out.printf("Legal holds on bucket %s: \n", bucketName);

    List<LegalHold> holds = result.getLegalHolds();
    for (LegalHold hold : holds) {
        System.out.printf("Legal Hold: %s", hold);
    }
}
```
{: codeblock}
{: java}
