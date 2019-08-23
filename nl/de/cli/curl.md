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

# `curl` verwenden
{: #curl}

Im Folgenden finden Sie ein Cheat-Sheet der grundlegenden `curl`-Befehle für die {{site.data.keyword.cos_full}}-REST-API. Weiterführende Details finden Sie in der API-Referenz für [Buckets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) oder [Objekte](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations).

Die Verwendung von `curl` erfordert Kenntnisse zur Befehlszeile und zum Objektspeicher. Des Weiteren benötigen Sie die erforderlichen Informationen aus einem [Serviceberechtigungsnachweis](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), aus der [Endpunktreferenz](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) oder aus der [Konsole](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Unbekannte Begriffe und Variablen finden Sie im [Glossar](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

**Hinweis**: Personenbezogene Daten: Bei der Erstellung von Buckets und/oder beim Hinzufügen von Objekten müssen Sie sicherstellen, dass keine Informationen verwendet werden, mit deren Hilfe ein Benutzer (natürliche Person) anhand des Namens, des Standorts oder durch andere Angaben identifiziert werden kann.
{:tip}

## IAM-Token anfordern
{: #curl-iam}

Zum Generieren eines IAM-OAuth-Tokens für die Authentifizierung von Anforderungen gibt es zwei Möglichkeiten: Verwendung eines `curl`-Befehls mit einem API-Schlüssel (siehe unten) oder Verwendung der Befehlszeile über die [IBM Cloud-Befehlszeilenschnittstelle](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli). 

### IAM-Token mit API-Schlüssel anfordern
{: #curl-token}

Stellen Sie zunächst sicher, dass Sie über einen API-Schlüssel verfügen. Rufen Sie diesen von [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys) ab.

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## Ressourceninstanz-ID anfordern
{: #curl-instance-id}

Für einige der folgenden Befehle ist der Parameter `ibm-service-instance-id` erforderlich. Um diesen Wert zu ermitteln, rufen Sie die Registerkarte **Serviceberechtigungsnachweise** Ihrer Object Storage-Instanz in der Cloudkonsole auf. Erstellen Sie bei Bedarf einen neuen Berechtigungsnachweis und verwenden Sie dann die Dropdown-Liste *Berechtigungsnachweise anzeigen*, um das JSON-Format anzuzeigen. Verwenden Sie den Wert von `resource_instance_id`. 

Zur Verwendung mit curl-APIs benötigen Sie nur die UUID, die nach dem letzten einfachen Doppelpunkt beginnt und vor dem letzten doppelten Doppelpunkt endet. Beispiel: Die ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` kann mit `39d8d161-22c4-4b77-a856-f11db5130d7d` abgekürzt werden.
{:tip}

## Buckets auflisten
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Bucket hinzufügen
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Bucket hinzufügen (Speicherklasse)
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

Eine Liste gültiger Bereitstellungscodes für `LocationConstraint` finden Sie im [Handbuch für Speicherklassen](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint).

## Bucket-CORS erstellen
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

Der Header `Content-MD5` muss die binäre Darstellung eines Base64-codierten MD5-Hashwerts sein.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Bucket-CORS abrufen
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Bucket-CORS löschen
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Objekte auflisten
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Bucket-Header abrufen
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Bucket löschen
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Objekt hochladen
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Objektheader abrufen
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Objekt kopieren
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## CORS-Informationen prüfen
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Objekt herunterladen
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## ACL des Objekts prüfen
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Anonymen Zugriff auf Objekt zulassen
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Objekt löschen
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Mehrere Objekte löschen
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

Der Header `Content-MD5` muss die binäre Darstellung eines Base64-codierten MD5-Hashwerts sein.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Mehrteiligen Upload starten
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Teil hochladen
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Mehrteiligen Upload abschließen
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

## Unvollständige mehrteilige Uploads abrufen
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Unvollständige mehrteilige Uploads abbrechen
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
