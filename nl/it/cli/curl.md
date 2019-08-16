---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, curl, cli

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

# Utilizzo di `curl`
{: #curl}

Di seguito troverai una scheda di riferimento dei comandi `curl` di base per l'API REST di {{site.data.keyword.cos_full}}. Puoi trovare ulteriori dettagli nel riferimento API per i [bucket](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) o gli [oggetti](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations).

L'utilizzo di `curl` presuppone una certa familiarità con la riga di comando e l'archiviazione oggetti e aver ottenuto le informazioni necessarie da una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), dal [riferimento endpoint](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) o dalla [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Se ci sono termini o variabili che non ti sono familiari, puoi trovarli nel [glossario](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

**Nota**: Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}

## Richiedi un token IAM
{: #curl-iam}

Ci sono due modi per generare un token oauth IAM per autenticare le richieste: utilizzando un comando `curl` con una chiave API (descritto di seguito) oppure dalla riga di comando utilizzando la [CLI di IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli). 

### Richiedi un token IAM utilizzando una chiave API
{: #curl-token}

Innanzitutto, assicurati di avere una chiave API. Ottienila da [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys).

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## Ottieni il tuo ID dell'istanza della risorsa
{: #curl-instance-id}

Alcuni dei seguenti comandi richiedono un parametro `ibm-service-instance-id`. Per trovare questo valore, vai alla scheda **Credenziali del servizio** della tua istanza Object Storage nella console cloud. Crea una nuova credenziale, se necessario, quindi utilizza l'elenco a discesa *Visualizza credenziali* per vedere il formato JSON. Utilizza il valore di `resource_instance_id`. 

Per l'utilizzo con le API curl, hai bisogno solo dell'UUID che inizia dopo l'ultimo due punti singolo e che termina prima dei due punti doppi finali. Ad esempio, l'id `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` può essere abbreviato in `39d8d161-22c4-4b77-a856-f11db5130d7d`.
{:tip}

## Elenca i bucket
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Aggiungi un bucket
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Aggiungi un bucket (classe di archiviazione)
{: #curl-add-bucket-class}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}

Puoi fare riferimento a un elenco di codici di provisioning validi per `LocationConstraint` nella [guida delle classi di archiviazione](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint).

## Crea un CORS del bucket
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

L'intestazione `Content-MD5` deve essere la rappresentazione binaria di un hash MD5 con codifica base64. 

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Ottieni un CORS del bucket
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Elimina un CORS del bucket
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Elenca gli oggetti
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Ottieni le intestazioni del bucket
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Elimina un bucket
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Carica un oggetto
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Ottieni le intestazioni di un oggetto
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Copia un oggetto
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## Controlla le informazioni sul CORS
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Scarica un oggetto
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Controlla l'ACL dell'oggetto
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Consenti l'accesso anonimo a un oggetto
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Elimina un oggetto
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Elimina più oggetti
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

L'intestazione `Content-MD5` deve essere la rappresentazione binaria di un hash MD5 con codifica base64. 

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Avvia un caricamento in più parti
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Carica una parte
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Completa un caricamento in più parti
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## Ottieni i caricamenti in più parti incompleti
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Interrompi i caricamenti in più parti incompleti
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
