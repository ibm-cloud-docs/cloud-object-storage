---

copyright:
  years: 2017, 2018, 2019
lastupdated: "26-06-2019"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:note: .note}

# Utilizza la CLI IBM Cloud
{: #ic-use-the-ibm-cli}

Il plugin Cloud Object Storage estende la CLI (command line interface) di IBM Cloud con un wrapper API per gestire le risorse Object Storage.

## Prerequisiti
{: #ic-prerequisites}
* Un account [IBM Cloud](https://cloud.ibm.com/)
* Un'istanza di [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision)
* La [CLI IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## Installazione e configurazione
{: #ic-installation}

Il plugin è compatibile con i sistemi operativi Windows, Linux e macOS eseguiti su processori a 64 bit.

Installa il plugin utilizzando il comando `plugin install`.

```
ibmcloud plugin install cloud-object-storage
```

Una volta installato il plugin, puoi configurarlo utilizzando il comando [`ibmcloud cos config`](#configure-the-program). Questo può essere utilizzato per popolare il plugin con le tue credenziali, l'ubicazione di download predefinita, la scelta dell'autenticazione e altro.

Il programma ti offre inoltre la possibilità di impostare la directory locale predefinita per i file scaricati e di impostare una regione predefinita. Per impostare l'ubicazione di download predefinita, immetti `ibmcloud cos config ddl` e inserisci nel programma un percorso file valido. Per impostare una regione predefinita, immetti `ibmcloud cos config region` e inserisci nel programma un codice regione, ad esempio `us-south`. Per impostazione predefinita, questo valore è impostato su `us-geo`.


Se utilizzi l'autenticazione IAM, dovrai fornire un CRN per utilizzare alcuni dei comandi. Per impostare il CRN, puoi immettere `ibmcloud cos config crn` e fornire il tuo CRN. Puoi trovare il CRN con `ibmcloud resource service-instance INSTANCE_NAME`.  In alternativa, puoi utilizzare la console basata su web, selezionare **Credenziali del servizio** nella barra laterale e creare una nuova serie di credenziali (o visualizzare un file di credenziali esistente che hai già creato).

Puoi visualizzare le tue credenziali Cloud Object Storage correnti immettendo la richiesta `ibmcloud cos config list`. Poiché il file di configurazione viene generato dal plugin, è meglio non modificarlo manualmente.

### Credenziali HMAC
{: #ic-hmac-credentials}

Se si preferisce, è possibile utilizzare le [credenziali HMAC di un ID servizio](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac) anziché la chiave API. Esegui `ibmcloud cos config hmac` per immettere le credenziali HMAC, quindi cambia il metodo di autenticazione utilizzando `ibmcloud cos config auth`.

Se scegli di utilizzare l'autenticazione tramite token con la tua propria chiave API, non hai bisogno di fornire le credenziali poiché il programma ti autentica automaticamente.
{: note}

In qualsiasi momento, per passare dall'autenticazione HMAC a quella IAM, puoi immettere `ibmcloud cos config auth`. Per ulteriori informazioni sull'autenticazione e l'autorizzazione in IBM Cloud, consulta la [documentazione di Identity and Access Management](/docs/iam?topic=iam-iamoverview).

## Indice dei comandi
{: #ic-command-index}

| Comandi |  |  |
| --- | --- | --- |
| [`abort-multipart-upload`](#abort-a-multipart-upload) | [`complete-multipart-upload`](#complete-a-multipart-upload) | [`config`](#configure-the-program) |
| [`copy-object`](#copy-object-from-bucket) | [`create-bucket`](#create-a-new-bucket) | [`create-multipart-upload`](#create-a-new-multipart-upload) |
| [`delete-bucket`](#delete-an-existing-bucket) | [`delete-bucket-cors`](#delete-bucket-cors) | [`delete-object`](#delete-an-object) |
| [`delete-objects`](#delete-multiple-objects) | [`download`](#download-objects-using-s3manager) | [`get-bucket-class`](#get-a-buckets-class) | 
| [`get-bucket-cors`](#get-bucket-cors) | [`get-bucket-location`](#find-a-bucket) | [`get-object`](#download-an-object) |
| [`head-bucket`](#get-a-buckets-headers) | [`head-object`](#get-an-objects-headers) | [`list-buckets`](#list-all-buckets) | 
| [`list-buckets-extended`](#extended-bucket-listing) | [`list-multipart-uploads`](#list-in-progress-multipart-uploads) | [`list-objects`](#list-objects) |
| [`list-parts`](#list-parts) | [`put-bucket-cors`](#set-bucket-cors) | [`put-object`](#upload-an-object) |
| [`upload`](#upload-objects-using-s3manager) | [`upload-part`](#upload-a-part) | [`upload-part-copy`](#upload-a-part-copy) |
| [`wait`](#wait) |  |  |

Ogni operazione elencata di seguito include una spiegazione della sua azione, del suo utilizzo e di tutti i parametri facoltativi o richiesti. A meno che non siano specificati come facoltativi, tutti i parametri elencati sono obbligatori.

Il plugin della CLI non supporta l'intera suite di funzioni disponibili in Object Storage, come il trasferimento ad alta velocità Aspera, Immutable Object Storage, la creazione di bucket Key Protect o firewall del bucket.
{: note}

### Interrompi un caricamento in più parti
{: #ic-abort-multipart-upload}
* **Azione:** interrompe un'istanza di caricamento in più parti terminando il caricamento nel bucket nell'account IBM Cloud Object Storage dell'utente.
* **Utilizzo:** `ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* ID di caricamento che identifica il caricamento in più parti.
		* Indicatore: `--upload-id ID`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Completa un caricamento in più parti
{: #ic-complete-multipart-upload}
* **Azione:** completa un'istanza di caricamento in più parti assemblando le parti attualmente caricate e caricando il file nel bucket nell'account IBM Cloud Object Storage dell'utente.
* **Utilizzo:** `ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* ID di caricamento che identifica il caricamento in più parti.
		* Indicatore: `--upload-id ID`
	* La STRUTTURA del caricamento in più parti da impostare.
		* Indicatore: `--multipart-upload STRUCTURE`
		* Sintassi abbreviata:  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* Sintassi JSON:  
	`--multipart-upload file://<filename.json>`  
	Il comando `--multipart-upload` utilizza una struttura JSON che descrive le parti del caricamento in più parti che devono essere riassemblate nel file completo. In questo esempio, viene utilizzato il prefisso `file://` per caricare la struttura JSON dal file specificato.
		```
			{
  			"Parts": [
    			{
     			 "ETag": "string",
     			 "PartNumber": integer
    			}
    			...
  				]
			}
		```
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


## Controllo manuale dei caricamenti in più parti
{: #ic-manual-multipart-uploads}

La CLI IBM Cloud Object Storage offre agli utenti la possibilità di caricare file di grandi dimensioni in più parti utilizzando le funzioni di caricamento in più parti. Per avviare un nuovo caricamento in più parti, esegui il comando `create-multipart-upload`, che restituisce l'ID di caricamento della nuova istanza di caricamento. Per continuare con il processo di caricamento, devi salvare l'ID caricamento per ciascun comando successivo.

Dopo aver eseguito il comando `complete-multipart-upload`, esegui `upload-part` per ogni parte del file che vuoi caricare. **Per i caricamenti in più parti, ogni parte del file (tranne l'ultima parte) deve avere una dimensione di almeno 5 MB.** Per suddividere un file in parti separate, puoi eseguire `split` in una finestra di terminale. Ad esempio, se sul tuo desktop hai un file da 13 MB denominato `TESTFILE` e vuoi suddividerlo in parti da 5 MB ciascuna, puoi eseguire `split -b 3m ~/Desktop/TESTFILE part-file-`. Questo comando genera tre parti del file con due parti da 5 MB ciascuna e una parte da 3 MB, denominate `part-file-aa`, `part-file-ab` e `part-file-ac`.

Man mano che viene caricata ogni parte del file, la CLI stampa la relativa ETag. Devi salvare questa ETag in un file JSON formattato, insieme al numero di parte. Utilizza questo template per creare il tuo proprio file di dati JSON ETag.

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "The ETag of the first file part goes here."
    },
    {
      "PartNumber": 2,
      "ETag": "The ETag of the second file part goes here."
    }
  ]
}
```

Aggiungi altre voci a questo template JSON secondo necessità.

Per visualizzare lo stato della tua istanza di caricamento in più parti, puoi sempre eseguire il comando `upload-part`, fornendo il nome del bucket, la chiave e l'ID caricamento. In questo modo vengono stampate informazioni non elaborate relative alla tua istanza di caricamento in più parti. Una volta completato il caricamento di ogni parte del file, esegui il comando `complete-multipart-upload` con i parametri necessari. Se tutto va bene, ricevi una conferma che il file è stato caricato correttamente nel bucket desiderato.

### Configura il programma
{: #ic-config}
* **Azione:** configura le preferenze del programma.
* **Utilizzo:** `ibmcloud cos config [COMMAND]`
* **Comandi:**
	* Alterna l'autenticazione HMAC e IAM.
		* Comando: `auth`
	* Memorizza il CRN nella configurazione.
		* Comando: `crn`
	* Memorizza l'ubicazione di download predefinita nella configurazione.
		* Comando: `ddl`
	* Memorizza le credenziali HMAC nella configurazione.
		* Comando: `hmac`
	* Elenca configurazione.
		* Comando: `list`
	* Memorizza la regione predefinita nella configurazione.
		* Comando: `region`
	* Alterna lo stile URL VHost e percorso.
		* Comando: `url-style`


### Copia un oggetto dal bucket
{: #ic-copy-object}
* **Azione:** copia un oggetto dal bucket di origine al bucket di destinazione.
* **Utilizzo:** `ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* (ORIGINE) Il nome del bucket di origine e il nome chiave dell'oggetto sorgente, separati da una barra (/). Deve essere con codifica URL.
		* Indicatore: `--copy-source SOURCE`
	* _Facoltativo_: specifica `CACHING_DIRECTIVES` per la catena di richieste e risposte.
		* Indicatore: `--cache-control CACHING_DIRECTIVES`
	* _Facoltativo_: specifica le informazioni di presentazione (`DIRECTIVES`).
		* Indicatore: `--content-disposition DIRECTIVES`
	* _Facoltativo_: specifica quali codifiche di contenuto (CONTENT_ENCODING) vengono applicate all'oggetto e quindi quali meccanismi di decodifica devono essere applicati per ottenere il tipo di supporto a cui fa riferimento il campo di intestazione Content-Type.
		* Indicatore: `--content-encoding CONTENT_ENCODING`
	* _Facoltativo_: il LINGUAGGIO del contenuto.
		* Indicatore: `--content-language LANGUAGE`
	* _Facoltativo_: un tipo MIME standard che descrive il formato dei dati oggetto.
		* Indicatore: `--content-type MIME`
	* _Facoltativo_: copia l'oggetto se la sua tag entità (Etag) corrisponde alla tag specificata (ETAG).
		* Indicatore: `--copy-source-if-match ETAG`
	* _Facoltativo_: copia l'oggetto se è stato modificato dall'ora specificata (TIMESTAMP).
		* Indicatore: `--copy-source-if-modified-since TIMESTAMP`
	* _Facoltativo_: copia l'oggetto se la sua tag entità (ETag) è diversa dalla tag specificata (ETAG).
		* Indicatore: `--copy-source-if-none-match ETAG`
	* _Facoltativo_: copia l'oggetto se non è stato modificato dall'ora specificata (TIMESTAMP).
		* Indicatore: `--copy-source-if-unmodified-since TIMESTAMP`
	* _Facoltativo_: un'ASSOCIAZIONE dei metadati da memorizzare. Sintassi: KeyName1=string,KeyName2=string
		* Indicatore: `--metadata MAP`
	* _Facoltativo_: specifica se i metadati vengono copiati dall'oggetto di origine o sostituiti con i metadati forniti nella richiesta. Valori DIRECTIVE: COPY,REPLACE.
		* Indicatore: ` --metadata-directive DIRECTIVE`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Crea un nuovo bucket
{: #ic-create-bucket}

* **Azione:** crea un bucket in un'istanza IBM Cloud Object Storage.
* **Utilizzo:** `ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* Tieni presente che se stai utilizzando l'autenticazione IAM devi fornire un CRN. Questo può essere impostato utilizzando il comando [`ibmcloud cos config crn`](#configure-the-program).
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: il nome della classe.
		* Indicatore: `--class CLASS_NAME`
	* _Facoltativo_: imposta l'ID istanza del servizio IBM nella richiesta.
		* Indicatore: `--ibm-service-instance-id ID`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`



### Crea un nuovo caricamento in più parti
{: #ic-create-multipart-upload}
* **Azione:** inizia il processo di caricamento file in più parti creando una nuova istanza di caricamento in più parti.
* **Utilizzo:** `ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* _Facoltativo_: specifica `CACHING_DIRECTIVES` per la catena di richieste e risposte.
		* Indicatore: `--cache-control CACHING_DIRECTIVES`
	* _Facoltativo_: specifica le informazioni di presentazione (`DIRECTIVES`).
		* Indicatore: `--content-disposition DIRECTIVES`
	* _Facoltativo_: specifica la codifica di contenuto (`CONTENT_ENCODING`) dell'oggetto.
		* Indicatore: `--content-encoding CONTENT_ENCODING`
	* _Facoltativo_: il LINGUAGGIO del contenuto.
		* Indicatore: `--content-language LANGUAGE`
	* _Facoltativo_: un tipo MIME standard che descrive il formato dei dati oggetto.
		* Indicatore: `--content-type MIME`
	* _Facoltativo_: un'ASSOCIAZIONE dei metadati da memorizzare. Sintassi: KeyName1=string,KeyName2=string
		* Indicatore: `--metadata MAP`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elimina un bucket esistente
{: #ic-delete-bucket}

* **Azione:** elimina un bucket esistente in un'istanza IBM Cloud Object Storage.
* **Utilizzo:** `ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
    * _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
       * Indicatore: `--region REGION`
    * _Facoltativo_: l'operazione non richiederà la conferma.
       * Indicatore: `--force`
    * _Facoltativo_: output restituito in formato JSON non elaborato.
       * Indicatore: `--json`


### Elimina il CORS del bucket
{: #ic-delete-bucket-cors}
* **Azione:** elimina la configurazione CORS su un bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elimina un oggetto
{: #ic-delete-object}
* **Azione:** elimina un oggetto da un bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
  * _Facoltativo_: l'operazione non richiederà la conferma.
  	* Indicatore: `--force`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elimina più oggetti
{: #ic-delete-objects}
* **Azione:** elimina più oggetti da un bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.  
		* Indicatore: `--bucket BUCKET_NAME`  
	* Una STRUTTURA che utilizza la sintassi abbreviata o JSON.  
		* Indicatore: `--delete STRUCTURE`  
		* Sintassi abbreviata:  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* Sintassi JSON:  
	`--delete file://<filename.json>`  
	Il comando `--delete` utilizza una struttura JSON che descrive le parti del caricamento in più parti che devono essere riassemblate nel file completo. In questo esempio, viene utilizzato il prefisso `file://` per caricare la struttura JSON dal file specificato.
	```
	{
  	"Objects": [
    	{
    	"Key": "string",
    	"VersionId": "string"
    	}
    ...
  	],
  	"Quiet": true|false
	}
	```
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Scarica oggetti utilizzando S3Manager
{: #ic-download-s3manager}
* **Azione:** scarica gli oggetti da S3 contemporaneamente.
* **Utilizzo:** `ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Parametri da fornire:**
	* Il nome (BUCKET_NAME) del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* _Facoltativo_: il numero di goroutine da ruotare in parallelo per chiamata da caricare durante l'invio di parti. Il valore predefinito è 5.
		* Indicatore: `--concurrency value`
	* _Facoltativo_: la DIMENSIONE buffer (in byte) da utilizzare quando si memorizzano i dati nel buffer e terminano come parti in S3. La dimensione minima consentita per la parte è 5 MB.
		* Indicatore: `--part-size SIZE`
	* _Facoltativo_: restituisce l'oggetto solo se la sua tag entità (ETag) è uguale all'ETAG specificata, altrimenti restituisce un errore 412 (precondizione non riuscita).
		* Indicatore: `--if-match ETAG`
	* _Facoltativo_: restituisce l'oggetto solo se è stato modificato dalla DATA/ORA specificata, altrimenti restituisce un errore 304 (non modificato).
		* Indicatore: `--if-modified-since TIMESTAMP`
	* _Facoltativo_: restituisce l'oggetto solo se la sua tag entità (ETag) è diversa dall'ETAG specificata, altrimenti restituisce un errore 304 (non modificato).
		* Indicatore: `--if-none-match ETAG`
	* _Facoltativo_: restituisce l'oggetto solo se non è stato modificato dalla DATA/ORA specificata, altrimenti restituisce un errore 412 (precondizione non riuscita).
		* Indicatore: `--if-unmodified-since TIMESTAMP`
	* _Facoltativo_: scarica i byte dell'INTERVALLO specificato di un oggetto. Per ulteriori informazioni sull'intestazione HTTP Range, [fai clic qui](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35).
		* Indicatore: `--range RANGE`
	* _Facoltativo_: imposta l'INTESTAZIONE Cache-Control della risposta.
		* Indicatore: `--response-cache-control HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Disposition della risposta.
		* Indicatore: `--response-content-disposition HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Encoding della risposta.
		* Indicatore: `--response-content-encoding HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Language della risposta.
		* Indicatore: `--response-content-language HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Type della risposta.
		* Indicatore: `--response-content-type HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Expires della risposta.
		* Indicatore: `--response-expires HEADER`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizzerà l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`
	* _Facoltativo_: l'ubicazione in cui salvare il contenuto dell'oggetto. Se questo parametro non viene fornito, il programma utilizza l'ubicazione predefinita.
		* Parametro: `OUTFILE`


### Ottieni la classe di un bucket
{: #ic-bucket-class}
* **Azione:** determina la classe di un bucket in un'istanza IBM Cloud Object Storage.
* **Utilizzo:** `ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Ottieni il CORS del bucket
{: #ic-get-bucket-cors}
* **Azione:** restituisce la configurazione CORS per il bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parametri da fornire:**
  * Il nome del bucket.  
    * Indicatore: `--bucket BUCKET_NAME`
  * _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
    * Indicatore: `--region REGION`
  * _Facoltativo_: output restituito in formato JSON non elaborato.
    * Indicatore: `--json`


### Trova un bucket
{: #ic-find-bucket}
* **Azione:** determina la regione e la classe di un bucket in un'istanza IBM Cloud Object Storage. 
* **Utilizzo:** `ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`
	


### Scarica un oggetto
{: #ic-download-object}
* **Azione:** scarica un oggetto da un bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* _Facoltativo_: restituisce l'oggetto solo se la sua tag di entità (ETag) è uguale all'ETAG specificata, altrimenti restituisce un errore 412 (precondizione non riuscita).
		* Indicatore: `--if-match ETAG`
	* _Facoltativo_: restituisce l'oggetto solo se è stato modificato dalla DATA/ORA specificata, altrimenti restituisce un errore 304 (non modificato).
		* Indicatore: `--if-modified-since TIMESTAMP`
	* _Facoltativo_: restituisce l'oggetto solo se la sua tag entità (ETag) è diversa dall'ETAG specificata, altrimenti restituisce un errore 304 (non modificato).
		* Indicatore: `--if-none-match ETAG`
	* _Facoltativo_: restituisce l'oggetto solo se non è stato modificato dalla DATA/ORA specificata, altrimenti restituisce un errore 412 (precondizione non riuscita).
		* Indicatore: `--if-unmodified-since TIMESTAMP`
	* _Facoltativo_: scarica i byte dell'INTERVALLO specificato di un oggetto. 
		* Indicatore: `--range RANGE`
	* _Facoltativo_: imposta l'INTESTAZIONE Cache-Control della risposta.
		* Indicatore: `--response-cache-control HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Disposition della risposta.
		* Indicatore: `--response-content-disposition HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Encoding della risposta.
		* Indicatore: `--response-content-encoding HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Language della risposta.
		* Indicatore: `--response-content-language HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Content-Type della risposta.
		* Indicatore: `--response-content-type HEADER`
	* _Facoltativo_: imposta l'INTESTAZIONE Expires della risposta.
		* Indicatore: `--response-expires HEADER`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`
	* _Facoltativo_: l'ubicazione in cui salvare il contenuto dell'oggetto. Se questo parametro non viene fornito, il programma utilizza l'ubicazione predefinita.
		* Parametro: `OUTFILE`


### Ottieni le intestazioni di un bucket
{: #ic-bucket-header}
* **Azione:** determina se esiste un bucket in un'istanza IBM Cloud Object Storage.
* **Utilizzo:** `ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Ottieni le intestazioni di un oggetto
{: #ic-object-header}
* **Azione:** determina se esiste un file in un bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* _Facoltativo_: restituisce l'oggetto solo se la sua tag di entità (ETag) è uguale all'ETAG specificata, altrimenti restituisce un errore 412 (precondizione non riuscita).
		* Indicatore: `--if-match ETAG`
	* _Facoltativo_: restituisce l'oggetto solo se è stato modificato dalla DATA/ORA specificata, altrimenti restituisce un errore 304 (non modificato).
		* Indicatore: `--if-modified-since TIMESTAMP`
	* _Facoltativo_: restituisce l'oggetto solo se la sua tag entità (ETag) è diversa dall'ETAG specificata, altrimenti restituisce un errore 304 (non modificato).
		* Indicatore: `--if-none-match ETAG`
	* _Facoltativo_: restituisce l'oggetto solo se non è stato modificato dalla DATA/ORA specificata, altrimenti restituisce un errore 412 (precondizione non riuscita).
		* Indicatore: `--if-unmodified-since TIMESTAMP`
	* Scarica i byte dell'INTERVALLO specificato di un oggetto.
		* Indicatore: `--range RANGE`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elenca tutti i bucket
{: #ic-list-buckets}
* **Azione:** stampa un elenco di tutti i bucket nell'account IBM Cloud Object Storage di un utente. I bucket potrebbero trovarsi in regioni diverse.
* **Utilizzo:** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* Tieni presente che se stai utilizzando l'autenticazione IAM devi fornire un CRN. Questo può essere impostato utilizzando il comando [`ibmcloud cos config crn`](#configure-the-program).
* **Parametri da fornire:**
  * Nessun parametro da fornire.
	* _Facoltativo_: imposta l'ID istanza del servizio IBM nella richiesta.
		* Indicatore: `--ibm-service-instance-id`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elenco bucket esteso
{: #ic-extended-bucket-listing}
* **Azione:** stampa un elenco di tutti i bucket nell'account IBM Cloud Object Storage di un utente. I bucket potrebbero trovarsi in regioni diverse.
* **Utilizzo:** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* Tieni presente che se stai utilizzando l'autenticazione IAM devi fornire un CRN. Questo può essere impostato utilizzando il comando [`ibmcloud cos config crn`](#configure-the-program).
* **Parametri da fornire:**
  * Nessun parametro da fornire.
	* _Facoltativo_: imposta l'ID istanza del servizio IBM nella richiesta.
		* Indicatore: `--ibm-service-instance-id`
	* _Facoltativo_: specifica la CHIAVE con cui iniziare quando si elencano gli oggetti in un bucket.
		* Indicatore: `--marker KEY`
	* _Facoltativo_: limita la risposta alle chiavi che iniziano con il PREFISSO specificato.
		* Indicatore: `--prefix PREFIX`
	* _Facoltativo_: la DIMENSIONE di ogni pagina da ottenere nella chiamata al servizio. Questa non influisce sul numero di elementi restituiti nell'output del comando. L'impostazione di una dimensione di pagina inferiore comporta più chiamate al servizio, recuperando meno elementi in ciascuna chiamata. Questo può aiutare a prevenire il timeout delle chiamate al servizio.
		* Indicatore: `--page-size SIZE`
	* _Facoltativo_: il NUMERO totale di elementi da restituire nell'output del comando.
		* Indicatore: `--max-items NUMBER`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elenca i caricamenti in più parti in corso
{: #ic-list-multipart-uploads}
* **Azione:** elenca i caricamenti in più parti in corso.
* **Utilizzo:** `ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: un DELIMITATORE è un carattere che utilizzi per raggruppare le chiavi.
		* Indicatore: `--delimiter DELIMITER`
	* _Facoltativo_: richiede di codificare le chiavi oggetto nella risposta e specifica il METODO di codifica da utilizzare.
		* Indicatore: `--encoding-type METHOD`
	* _Facoltativo_: limita la risposta alle chiavi che iniziano con il PREFISSO specificato.
		* Indicatore: `--prefix PREFIX`
	* _Facoltativo_: insieme a upload-id-marker, questo parametro specifica il caricamento in più parti dopo il quale dovrebbe iniziare l'elenco.
		* Indicatore: `--key-marker value`
	* _Facoltativo_: insieme a key-marker, specifica il caricamento in più parti dopo il quale dovrebbe iniziare l'elenco. Se key-marker non viene specificato, il parametro upload-id-marker viene ignorato.
		* Indicatore: `--upload-id-marker value`
	* _Facoltativo_: la DIMENSIONE di ogni pagina da ottenere nella chiamata al servizio. Questa non influisce sul numero di elementi restituiti nell'output del comando. L'impostazione di una dimensione di pagina inferiore comporta più chiamate al servizio, recuperando meno elementi in ciascuna chiamata. Questo può aiutare a prevenire il timeout delle chiamate al servizio. (Valore predefinito: 1000).
		* Indicatore: `--page-size SIZE`
	* _Facoltativo_: il NUMERO totale di elementi da restituire nell'output del comando. Se il numero totale di elementi disponibili è superiore al valore specificato, nell'output del comando viene fornito un valore NextToken. Per riprendere la paginazione, fornisci il valore NextToken nell'argomento starting-token di un comando successivo. (Valore predefinito: 0).
		* Indicatore: `--max-items NUMBER`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elenca gli oggetti
{: #ic-list-objects}
* **Azione:** elenca i file presenti in un bucket nell'account IBM Cloud Object Storage di un utente.  Questa operazione è attualmente limitata ai 1000 oggetti creati più di recente e non può essere filtrata.
* **Utilizzo:** `ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: un DELIMITATORE è un carattere che utilizzi per raggruppare le chiavi.
		* Indicatore: `--delimiter DELIMITER`
	* _Facoltativo_: richiede di codificare le chiavi oggetto nella risposta e specifica il METODO di codifica da utilizzare.
		* Indicatore: `--encoding-type METHOD`
	* _Facoltativo_: limita la risposta alle chiavi che iniziano con il PREFISSO specificato.
		* Indicatore: `--prefix PREFIX`
	* _Facoltativo_: un TOKEN per specificare da dove iniziare la paginazione. Questo è il NextToken di una risposta precedentemente troncata.
		* Indicatore: `--starting-token TOKEN`
	* _Facoltativo_: la DIMENSIONE di ogni pagina da ottenere nella chiamata al servizio. Questa non influisce sul numero di elementi restituiti nell'output del comando. L'impostazione di una dimensione di pagina inferiore comporta più chiamate al servizio, recuperando meno elementi in ciascuna chiamata. Questo può aiutare a prevenire il timeout delle chiamate al servizio. (Valore predefinito: 1000)
		* Indicatore: `--page-size SIZE`
	* _Facoltativo_: il NUMERO totale di elementi da restituire nell'output del comando. Se il numero totale di elementi disponibili è superiore al valore specificato, nell'output del comando viene fornito un valore NextToken. Per riprendere la paginazione, fornisci il valore NextToken nell'argomento starting-token di un comando successivo. (Valore predefinito: 0)
		* Indicatore: `--max-items NUMBER`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Elenca le parti
{: #ic-list-parts}
* **Azione:** stampa le informazioni relative a un'istanza di caricamento in più parti in corso
* **Utilizzo:** `ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* ID di caricamento che identifica il caricamento in più parti.
		* Indicatore: `--upload-id ID`
	* VALORE del numero di parte dopo il quale inizia l'elenco (valore predefinito: 1)
		* Indicatore: `--part-number-marker VALUE`
	* _Facoltativo_: la DIMENSIONE di ogni pagina da ottenere nella chiamata al servizio. Questa non influisce sul numero di elementi restituiti nell'output del comando. L'impostazione di una dimensione di pagina inferiore comporta più chiamate al servizio, recuperando meno elementi in ciascuna chiamata. Questo può aiutare a prevenire il timeout delle chiamate al servizio. (Valore predefinito: 1000)
		* Indicatore: `--page-size SIZE`
	* _Facoltativo_: il NUMERO totale di elementi da restituire nell'output del comando. Se il numero totale di elementi disponibili è superiore al valore specificato, nell'output del comando viene fornito un valore NextToken. Per riprendere la paginazione, fornisci il valore NextToken nell'argomento starting-token di un comando successivo. (Valore predefinito: 0)
		* Indicatore: `--max-items NUMBER`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Imposta il CORS del bucket
{: #ic-set-bucket-cors}
* **Azione:** imposta la configurazione CORS per un bucket nell'account IBM Cloud Object Storage dell'utente.
* **Utilizzo:** `ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* _Facoltativo_: una STRUTTURA che utilizza la sintassi JSON in un file.
		* Indicatore: `--cors-configuration STRUCTURE`
		* Sintassi JSON:  
	`--cors-configuration file://<filename.json>`  
	Il comando `--cors-configuration` utilizza una struttura JSON che descrive le parti del caricamento in più parti che devono essere riassemblate nel file completo. In questo esempio, viene utilizzato il prefisso `file://` per caricare la struttura JSON dal file specificato.
	```
	{
  	"CORSRules": [
    	{
      	"AllowedHeaders": ["string", ...],
      	"AllowedMethods": ["string", ...],
      	"AllowedOrigins": ["string", ...],
      	"ExposeHeaders": ["string", ...],
      	"MaxAgeSeconds": integer
    	}
    	...
  	]
	}
	```
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`



### Inserisci l'oggetto
{: #ic-upload-object}
* **Azione:** carica un oggetto in un bucket nell'account IBM Cloud Object Storage di un utente.
* **Utilizzo:** `ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parametri da fornire:**
    * Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* _Facoltativo_: ubicazione dei dati oggetto (`FILE_PATH`).
		* Indicatore: `--body FILE_PATH`
	* _Facoltativo_: specifica `CACHING_DIRECTIVES` per la catena di richieste e risposte.
		* Indicatore: `--cache-control CACHING_DIRECTIVES`
	* _Facoltativo_: specifica le informazioni di presentazione (`DIRECTIVES`).
		* Indicatore: `--content-disposition DIRECTIVES`
	* _Facoltativo_: specifica la codifica di contenuto (`CONTENT_ENCODING`) dell'oggetto.
		* Indicatore: `--content-encoding CONTENT_ENCODING`
	* _Facoltativo_: il LINGUAGGIO del contenuto.
		* Indicatore: `--content-language LANGUAGE`
	* _Facoltativo_: la DIMENSIONE del corpo in byte. Questo parametro è utile quando la dimensione del corpo non può essere determinata automaticamente. (Valore predefinito: 0)
		* Indicatore: `--content-length SIZE`
	* _Facoltativo_: il digest MD5 a 128 bit con codifica base64 dei dati.
		* Indicatore: `--content-md5 MD5`
	* _Facoltativo_: un tipo MIME standard che descrive il formato dei dati oggetto.
		* Indicatore: `--content-type MIME`
	* _Facoltativo_: un'ASSOCIAZIONE dei metadati da memorizzare. Sintassi: KeyName1=string,KeyName2=string
		* Indicatore: `--metadata MAP`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Carica oggetti utilizzando S3Manager
{: #ic-upload-s3manager}
* **Azione:** carica gli oggetti da S3 contemporaneamente.
* **Utilizzo:** `ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parametri da fornire:**
	* Il nome (BUCKET_NAME) del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* Il PERCORSO del file da caricare.
		* Indicatore: `--file PATH`
	* _Facoltativo_: il numero di goroutine da ruotare in parallelo per chiamata da caricare durante l'invio di parti. Il valore predefinito è 5.
		* Indicatore: `--concurrency value`
	* _Facoltativo_: il numero massimo di PARTI che verranno caricate in S3 che calcola la dimensione della parte dell'oggetto da caricare.  Il limite è di 10.000 parti.
		* Indicatore: `--max-upload-parts PARTS`
	* _Facoltativo_: la DIMENSIONE buffer (in byte) da utilizzare quando si memorizzano i dati nel buffer e terminano come parti in S3. La dimensione minima consentita per la parte è 5 MB.
		* Indicatore: `--part-size SIZE`
	* _Facoltativo_: impostando questo valore su true, l'SDK eviterà di chiamare AbortMultipartUpload in caso di errore, lasciando tutte le parti caricate correttamente su S3 per il ripristino manuale.
		* Indicatore: `--leave-parts-on-errors`
	* _Facoltativo_: specifica CACHING_DIRECTIVES per la catena di richieste/risposte.
		* Indicatore: `--cache-control CACHING_DIRECTIVES`
	* _Facoltativo_: specifica le informazioni di presentazione (DIRECTIVES).
		* Indicatore: `--content-disposition DIRECTIVES`
	* _Facoltativo_: specifica quali codifiche di contenuto (CONTENT_ENCODING) sono state applicate all'oggetto e quindi quali meccanismi di decodifica devono essere applicati per ottenere il tipo di supporto a cui fa riferimento il campo di intestazione Content-Type.
		* Indicatore: `--content-encoding CONTENT_ENCODING`
	* _Facoltativo_: il LINGUAGGIO del contenuto.
		* Indicatore: `--content-language LANGUAGE`
	* _Facoltativo_: la DIMENSIONE del corpo in byte. Questo parametro è utile quando la dimensione del corpo non può essere determinata automaticamente. 
		* Indicatore: `--content-length SIZE`
	* _Facoltativo_: il digest MD5 a 128 bit con codifica base64 dei dati.
		* Indicatore: `--content-md5 MD5`
	* _Facoltativo_: un tipo MIME standard che descrive il formato dei dati oggetto.
		* Indicatore: `--content-type MIME`
	* _Facoltativo_: un'ASSOCIAZIONE dei metadati da memorizzare. Sintassi: KeyName1=string,KeyName2=string
		* Indicatore: `--metadata MAP`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizzerà l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Carica una parte
{: #ic-upload-part}
* **Azione:** carica un parte di un file in un'istanza di caricamento in più parti esistente.
* **Utilizzo:** `ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* Nota che devi salvare il numero di ciascuna parte del file caricato e l'ETag (che la CLI stamperà per te) per ogni parte in un file JSON. Per ulteriori informazioni, fai riferimento alla "Guida al caricamento in più parti" riportata di seguito.
* **Parametri da fornire:**
	* Il nome del bucket in cui sta avvenendo il caricamento in più parti.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* ID di caricamento che identifica il caricamento in più parti.
		* Indicatore: `--upload-id ID`
	* NUMERO di parte della parte che viene caricata. Questo è un numero intero positivo compreso nell'intervallo 1 - 10.000. (Valore predefinito: 1)
		* Indicatore: `--part-number NUMBER`
	* _Facoltativo_: ubicazione dei dati oggetto (`FILE_PATH`).
		* Indicatore: `--body FILE_PATH`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Carica una copia della parte
{: #ic-upload-a-part-copy}
* **Azione:** carica una parte copiando i dati da un oggetto esistente.
* **Utilizzo:** `ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* Nota che devi salvare il numero di ciascuna parte del file caricato e l'ETag (che la CLI stamperà per te) per ogni parte in un file JSON. Per ulteriori informazioni, fai riferimento alla "Guida al caricamento in più parti".
* **Parametri da fornire:**
	* Il nome del bucket.
		* Indicatore: `--bucket BUCKET_NAME`
	* La CHIAVE dell'oggetto.
		* Indicatore: `--key KEY`
	* ID di caricamento che identifica il caricamento in più parti.
		* Indicatore: `--upload-id ID`
	* NUMERO di parte della parte che viene caricata. Questo è un numero intero positivo compreso tra 1 e 10.000.
		* Indicativo: `--part-number PART_NUMBER`
	* (ORIGINE) Il nome del bucket di origine e il nome chiave dell'oggetto sorgente, separati da una barra (/). Deve essere con codifica URL.
		* Indicatore: `--copy-source SOURCE`
	* _Facoltativo_: copia l'oggetto se la sua tag entità (Etag) corrisponde alla tag specificata (ETAG).
		* Indicatore: `--copy-source-if-match ETAG`
	* _Facoltativo_: copia l'oggetto se è stato modificato dall'ora specificata (TIMESTAMP).
		* Indicatore: `--copy-source-if-modified-since TIMESTAMP`
	* _Facoltativo_: copia l'oggetto se la sua tag entità (ETag) è diversa dalla tag specificata (ETAG).
		* Indicatore: `--copy-source-if-none-match ETAG`
	* _Facoltativo_: copia l'oggetto se non è stato modificato dall'ora specificata (TIMESTAMP).
		* Indicatore: `--copy-source-if-unmodified-since TIMESTAMP`
	* _Facoltativo_: l'intervallo di byte da copiare dall'oggetto di origine. Il valore dell'intervallo deve utilizzare il formato bytes=first-last, dove il primo e l'ultimo sono gli offset di byte in base zero da copiare. Ad esempio, bytes=0-9 indica che vuoi copiare i primi dieci byte dell'origine. Puoi copiare un intervallo solo se l'oggetto di origine è maggiore di 5 MB.
		* Indicatore: `--copy-source-range value`
	* _Facoltativo_: la REGIONE in cui è presente il bucket. Se questo indicatore non viene fornito, il programma utilizza l'opzione predefinita specificata nella configurazione.
		* Indicatore: `--region REGION`
	* _Facoltativo_: output restituito in formato JSON non elaborato.
		* Indicatore: `--json`


### Attendi
{: #ic-wait}
* **Azione:** attendi fino a quando non viene soddisfatta una determinata condizione. Ogni sottocomando esegue il polling di un'API fino a quando non viene soddisfatto il requisito elencato.
* **Utilizzo:** `ibmcloud cos wait command [arguments...] [command options]`
* **Comandi:**
    * `bucket-exists`
  		* Attendi fino a quando non viene ricevuta una risposta 200 durante il polling con head-bucket. Esegue il polling ogni 5 secondi fino al raggiungimento di uno stato di esito positivo. Questa operazione terminerà con un codice di ritorno 255 dopo 20 controlli non riusciti.
	* `bucket-not-exists`
		* Attendi fino a quando non viene ricevuta una risposta 404 durante il polling con head-bucket. Esegue il polling ogni 5 secondi fino al raggiungimento di uno stato di esito positivo. Questa operazione terminerà con un codice di ritorno 255 dopo 20 controlli non riusciti.
	* `object-exists`
		* Attendi fino a quando non viene ricevuta una risposta 200 durante il polling con head-object. Esegue il polling ogni 5 secondi fino al raggiungimento di uno stato di esito positivo. Questa operazione terminerà con un codice di ritorno 255 dopo 20 controlli non riusciti.
	* `object-not-exists`
		* Attendi fino a quando non viene ricevuta una risposta 404 durante il polling con head-object. Esegue il polling ogni 5 secondi fino al raggiungimento di uno stato di esito positivo. Questa operazione terminerà con un codice di ritorno 255 dopo 20 controlli non riusciti.

