---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# Informazioni sull'API S3 {{site.data.keyword.cos_full_notm}}
{: #compatibility-api}

L'API {{site.data.keyword.cos_full}} è un'API basata su REST per la lettura e la scrittura di oggetti. Utilizza {{site.data.keyword.iamlong}} per l'autenticazione e l'autorizzazione e supporta un sottoinsieme dell'API S3 per una facile migrazione delle applicazioni a {{site.data.keyword.cloud_notm}}.

Questa documentazione di riferimento viene costantemente aggiornata. Se hai domande tecniche relative all'utilizzo dell'API nella tua applicazione, pubblicale in [StackOverflow](https://stackoverflow.com/). Aggiungi le tag `ibm-cloud-platform` e `object-storage` e aiutaci a migliorare questa documentazione grazie al tuo feedback.

Poiché i token {{site.data.keyword.iamshort}} sono relativamente semplici da utilizzare, `curl` è una buona scelta per la verifica di base e l'interazione con la tua archiviazione. Puoi trovare ulteriori informazioni nel [riferimento `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

La tabella riportata di seguito descrive l'insieme completo di operazioni dell'API {{site.data.keyword.cos_full_notm}}. Per ulteriori informazioni, vedi [la pagina di riferimento API per i bucket](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) o [gli oggetti](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).


## Operazioni per il bucket
{: #compatibility-api-bucket}

Queste operazioni creano, eliminano, richiamano le informazioni sui bucket e ne controllano il comportamento. 

| Operazione bucket       | Nota                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` Buckets           | Utilizzato per richiamare un elenco di tutti i bucket che appartengono a un account.              |
| `DELETE` Bucket         | Elimina un bucket vuoto.                                                       |
| `DELETE` Bucket CORS    | Elimina qualsiasi configurazione CORS (cross-origin resource sharing) impostata in un bucket. |
| `GET` Bucket            | Elenca gli oggetti in un bucket. Vengono elencati 1.000 oggetti alla volta.         |
| `GET` Bucket CORS       | Richiama qualsiasi configurazione CORS impostata in un bucket.                              |
| `HEAD` Bucket           | Richiama le intestazioni di un bucket.                                                  |
| `GET` Multipart Uploads | Elenca i caricamenti in più parti incompleti o annullati.                     |
| `PUT` Bucket            | I bucket hanno limitazioni di denominazione. Gli account sono limitati a 100 bucket.         |
| `PUT` Bucket CORS       | Crea una configurazione CORS per un bucket.                                     |


## Operazioni per l'oggetto
{: #compatibility-api-object}

Queste operazioni creano, eliminano, richiamano le informazioni sugli oggetti e ne controllano il comportamento. 

| Operazione oggetto        | Nota                                                                            |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` Object           | Elimina un oggetto da un bucket.                                                   |
| `DELETE` Batch            | Elimina molti oggetti da un bucket con un'operazione.                             |
| `GET` Object              | Richiama un oggetto da un bucket.                                                 |
| `HEAD` Object             | Richiama le intestazioni di un oggetto.                                                     |
| `OPTIONS` Object          | Controlla la configurazione CORS per vedere se può essere inviata una richiesta specifica.           |
| `PUT` Object              | Aggiunge un oggetto a un bucket.                                                        |
| `PUT` Object (Copy)       | Crea una copia di un oggetto.                                                       |
| Begin Multipart Upload    | Crea un ID di caricamento per un insieme di parti da caricare.                            |
| Upload Part               | Carica una parte di un oggetto associato a un ID di caricamento.                  |
| Upload Part (Copy)        | Carica una parte di un oggetto esistente associato a un ID di caricamento.         |
| Complete Multipart Upload | Assembla un oggetto dalle parti associate a un ID di caricamento.              |
| Cancel Multipart Upload   | Annulla il caricamento ed elimina le parti in sospeso associate a un ID di caricamento. |
| List Parts                | Restituisce un elenco delle parti associate a un ID di caricamento.                       |


Al momento, alcune operazioni aggiuntive, come il contrassegno tramite tag e il controllo della versione, sono supportate nelle implementazioni del cloud privato di {{site.data.keyword.cos_short}}, ma non nei cloud pubblici o dedicati. Puoi trovare ulteriori soluzioni Object Storage personalizzate sul sito [ibm.com](https://www.ibm.com/cloud/object-storage).
