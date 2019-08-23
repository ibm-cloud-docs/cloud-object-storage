---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Intestazioni comuni & Codici di errore
{: #compatibility-common}

## Intestazioni della richiesta comuni
{: #compatibility-request-headers}

La seguente tabella descrive le intestazioni della richiesta comuni. {{site.data.keyword.cos_full}} ignora le intestazioni comuni non elencate di seguito se inviate in una richiesta, sebbene alcune richieste possono supportare altre intestazioni come definito in questa documentazione. 

| Intestazione            | Nota                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | **Obbligatoria** per tutte le richieste (token `bearer` OAuth2).                                                                            |
| ibm-service-instance-id | **Obbligatoria** per le richieste per creare o elencare i bucket.                                                                              |
| Content-MD5             | L'hash MD5 a 128 bit con codifica base64 del payload, utilizzato come un controllo dell'integrità per garantire che il payload non è stato modificato in transito. |
| Expect                  | Il valore `100-continue` attenderà che il sistema confermi che le intestazioni sono appropriate prima di inviare il payload. |
| host                    | L'endpoint o la sintassi 'virtual host' di `{bucket-name}.{endpoint}`. Di norma, questa intestazione viene aggiunta automaticamente. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)| 
| Cache-Control | Può essere utilizzata per specificare il comportamento della memorizzazione nella cache insieme alla catena richiesta/risposta. Per ulteriori informazioni, vai all'indirizzo http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 |

### Metadati personalizzati
{: #compatibility-headers-metadata}

Un vantaggio derivante dall'utilizzo dell'archiviazione oggetti è la capacità di aggiungere metadati personalizzati inviando le coppie chiave-valore come intestazioni. Queste intestazioni prendono la forma di `x-amz-meta-{KEY}`. Tieni presente che, a differenza di AWS S3, IBM COS combinerà più intestazioni con la stessa chiave di metadati in un elenco separato da virgole di valori. 

## Intestazioni della risposta comuni
{: #compatibility-response-headers}

La seguente tabella descrive le intestazioni della risposta comuni. 

| Intestazione     | Nota                                                |
|------------------|-----------------------------------------------------|
| Content-Length   | La lunghezza del corpo della richiesta in byte.     |
| Connection       | Indica se la connessione è aperta o chiusa.         |
| Date             | Data/ora della richiesta.                          |
| ETag             | Valore hash MD5 della richiesta.                   |
| Server           | Nome del server di risposta.                       |
| X-Clv-Request-Id | Identificativo univoco generato per richiesta.     |

### Intestazioni della risposta del ciclo di vita
{: #compatibility-lifecycle-headers}

La seguente tabella descrive le intestazioni della risposta per gli oggetti archiviati

| Intestazione     | Nota                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore|Inclusa se l'oggetto è stato ripristinato o se è in corso un ripristino. |
|x-amz-storage-class|Restituisce `GLACIER` se archiviato o ripristinato temporaneamente.|
|x-ibm-archive-transition-time|Restituisce la data e l'ora in cui è pianificata la transizione dell'oggetto al livello di archivio. |
|x-ibm-transition|Inclusa se l'oggetto contiene metadati di transizione e restituisce il livello e l'ora originale della transizione. |
|x-ibm-restored-copy-storage-class|Inclusa se un oggetto si trova negli stati `RestoreInProgress` o `Restored` e restituisce la classe di archiviazione del bucket.|

## Codici di errore
{: #compatibility-errors}

| Codice di errore                    | Descrizione                                                                                                                                                             | Codice stato HTTP                   |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | Accesso negato                                                                                                                                                          | 403 Forbidden                       |
| BadDigest                           | Il Content-MD5 che hai specificato non corrispondeva a quello che abbiamo ricevuto.                                                                                     | 400 Bad Request                     |
| BucketAlreadyExists                 | Il nome bucket richiesto non è disponibile. Lo spazio dei nomi del bucket è condiviso da tutti gli utenti del sistema. Seleziona un nome diverso e riprova.             | 409 Conflict                        |
| BucketAlreadyOwnedByYou             | La tua precedente richiesta di creazione del bucket denominato è riuscita e ne sei già il proprietario.                                                                 | 409 Conflict                        |
| BucketNotEmpty                      | Il bucket che hai tentato di eliminare non è vuoto.                                                                                                                     | 409 Conflict                        |
| CredentialsNotSupported             | Questa richiesta non supporta le credenziali.                                                                                                                           | 400 Bad Request                     |
| EntityTooSmall                      | Il caricamento che hai proposto è inferiore alla dimensione minima dell'oggetto consentita.                                                                             | 400 Bad Request                     |
| EntityTooLarge                      | Il caricamento che hai proposto supera la dimensione massima dell'oggetto consentita.                                                                                   | 400 Bad Request                     |
| IncompleteBody                      | Non hai fornito il numero di byte specificati dall'intestazione HTTP Content-Length.                                                                                    | 400 Bad Request                     |
| IncorrectNumberOfFilesInPostRequest | POST richiede esattamente un caricamento file per richiesta.                                                                                                            | 400 Bad Request                     |
| InlineDataTooLarge                  | I dati inline superano la dimensione massima consentita.                                                                                                                | 400 Bad Request                     |
| InternalError                       | Abbiamo riscontrato un errore interno. Riprova.                                                                                                                         | 500 Internal Server Error           |
| InvalidAccessKeyId                  | L'ID chiave di accesso AWS che hai fornito non esiste nei nostri record.                                                                                                | 403 Forbidden                       |
| InvalidArgument                     | Argomento non valido                                                                                                                                                    | 400 Bad Request                     |
| InvalidBucketName                   | Il bucket specificato non è valido.                                                                                                                                     | 400 Bad Request                     |
| InvalidBucketState                  | La richiesta non è valida con lo stato corrente del bucket.                                                                                                             | 409 Conflict                        |
| InvalidDigest                       | Il Content-MD5 che hai specificato non è valido.                                                                                                                        | 400 Bad Request                     |
| InvalidLocationConstraint           | Il vincolo di ubicazione specificato non è valido. Per ulteriori informazioni, vedi la sezione su come selezionare una regione per i tuoi bucket.                       | 400 Bad Request                     |
| InvalidObjectState                  | L'operazione non è valida per lo stato corrente dell'oggetto.                                                                                                           | 403 Forbidden                       |
| InvalidPart                         | Impossibile trovare una o più delle parti specificate. La parte potrebbe non essere stata caricata o la tag entità specificata potrebbe non corrispondere a quella della parte. | 400 Bad Request                     |
| InvalidPartOrder                    | L'elenco delle parti non era in ordine crescente. L'elenco delle parti deve essere specificato nell'ordine basato sul numero parte.                                     | 400 Bad Request                     |
| InvalidRange                        | L'intervallo richiesto non può essere soddisfatto.                                                                                                                      | 416 Requested Range Not Satisfiable |
| InvalidRequest                      | Utilizza AWS4-HMAC-SHA256.                                                                                                                                              | 400 Bad Request                     |
| InvalidSecurity                     | Le credenziali di sicurezza fornite non sono valide.                                                                                                                    | 403 Forbidden                       |
| InvalidURI                          | Impossibile analizzare l'URI specificato.                                                                                                                               | 400 Bad Request                     |
| KeyTooLong                          | La tua chiave è troppo lunga.                                                                                                                                           | 400 Bad Request                     |
| MalformedPOSTRequest                | Il corpo della tua richiesta POST non è un multipart/form-data formato correttamente.                                                                                   | 400 Bad Request                     |
| MalformedXML                        | L'XML che hai fornito non è formato correttamente oppure non è stato convalidato con il nostro schema pubblicato.                                                       | 400 Bad Request                     |
| MaxMessageLengthExceeded            | La tua richiesta era troppo grande.                                                                                                                                     | 400 Bad Request                     |
| MaxPostPreDataLengthExceededError   | I campi della tua richiesta POST che precedono il file di caricamento erano troppo grandi.                                                                              | 400 Bad Request                     |
| MetadataTooLarge                    | Le tue intestazioni di metadati superano la dimensione massima di metadati consentita.                                                                                  | 400 Bad Request                     |
| MethodNotAllowed                    | Il metodo specificato non è consentito su questa risorsa.                                                                                                               | 405 Method Not Allowed              |
| MissingContentLength                | Devi fornire l'intestazione HTTP Content-Length.                                                                                                                        | 411 Length Required                 |
| MissingRequestBodyError             | Ciò si verifica quando l'utente invia un documento xml vuoto come una richiesta. Il messaggio di errore è "Request body is empty."                                      | 400 Bad Request                     |
| NoSuchBucket                        | Il bucket specificato non esiste.                                                                                                                                       | 404 Not Found                       |
| NoSuchKey                           | La chiave specificata non esiste.                                                                                                                                       | 404 Not Found                       |
| NoSuchUpload                        | Il caricamento in più parti specificato non esiste. L'ID di caricamento potrebbe non essere valido o il caricamento in più parti potrebbe essere stato interrotto o completato. | 404 Not Found                       |
| NotImplemented                      | Un'intestazione che hai fornito implica una funzionalità che non è implementata.                                                                                        | 501 Not Implemented                 |
| OperationAborted                    | Al momento, su questa risorsa è in corso un'operazione condizionale in conflitto. Riprova.                                                                              | 409 Conflict                        |
| PreconditionFailed                  | Almeno una delle precondizioni che hai specificato non è presente.                                                                                                      | 412 Precondition Failed             |
| Redirect                            | Reindirizzamento temporaneo.                                                                                                                                            | 307 Moved Temporarily               |
| RequestIsNotMultiPartContent        | POST del bucket deve essere enclosure-type multipart/form-data.                                                                                                         | 400 Bad Request                     |
| RequestTimeout                      | La tua connessione socket al server non è stata letta o scritta entro il periodo di timeout.                                                                            | 400 Bad Request                     |
| RequestTimeTooSkewed                | La differenza tra l'ora della richiesta e quella del server è troppo grande.                                                                                            | 403 Forbidden                       |
| ServiceUnavailable                  | Riduci la tua velocità di richiesta.                                                                                                                                    | 503 Service Unavailable             |
| SlowDown                            | Riduci la tua velocità di richiesta.                                                                                                                                    | 503 Slow Down                       |
| TemporaryRedirect                   | Vieni reindirizzato al bucket durante l'aggiornamento di DNS.                                                                                                              | 307 Moved Temporarily               |
| TooManyBuckets                      | Hai tentato di creare più bucket di quelli consentiti.                                                                                                                  | 400 Bad Request                     |
| UnexpectedContent                   | Questa richiesta non supporta il contenuto.                                                                                                                             | 400 Bad Request                     |
| UserKeyMustBeSpecified              | Il POST del bucket deve contenere il nome campo specificato. Se è specificato, controlla l'ordine dei campi.                                                            | 400 Bad Request                     |
