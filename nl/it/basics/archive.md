---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Archivia i dati cold con le regole di transizione
{: #archive}

{{site.data.keyword.cos_full}} Archive è un'opzione [a basso costo](https://www.ibm.com/cloud/object-storage) per i dati a cui accedi raramente. Puoi archiviare i dati eseguendone la transizione da uno qualsiasi dei livelli di archiviazione (Standard, Vault, Cold Vault e Flex) a un archivio offline a lungo termine oppure utilizzare l'opzione Cold Vault online.
{: shortdesc}

Puoi archiviare gli oggetti utilizzando la console web, l'API REST e gli strumenti di terze parti che sono integrati con IBM Cloud Object Storage. 

Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Aggiungi o gestisci una politica di archiviazione in un bucket
{: #archive-add}

Quando crei o modifichi una politica di archiviazione per un bucket, tieni in considerazione quanto segue:

* Una politica di archiviazione può essere aggiunta a un nuovo bucket o a uno esistente in qualsiasi momento.  
* Una politica di archiviazione esistente può essere modificata o disabilitata.  
* Una politica di archiviazione appena aggiunta o modificata si applica ai nuovi oggetti caricati e non influisce su quelli esistenti. 

Per archiviare immediatamente i nuovi oggetti caricati in un bucket, inserisci 0 giorni nella politica di archiviazione.
{:tip}

Archive è disponibile solo in determinate regioni. Per ulteriori dettagli, vedi [Servizi integrati](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability).
{:tip}

## Ripristina un oggetto archiviato
{: #archive-restore}

Per poter accedere a un oggetto archiviato, devi ripristinarlo al livello di archiviazione originale. Quando ripristini un oggetto, puoi specificare il numero di giorni per il quale desideri che sia disponibile. Al termine del periodo specificato, la copia ripristinata viene eliminata.  

Il processo di ripristino può richiedere fino a 12 ore.
{:tip}

I sottostati dell'oggetto archiviato sono:

* Archiviato (Archived): un oggetto nello stato archiviato è stato spostato dal suo livello di archiviazione online (Standard, Vault, Cold Vault e Flex) al livello di archiviazione offline basato sulla politica di archiviazione nel bucket.
* In fase di ripristino (Restoring): un oggetto nello stato in fase di ripristino è nel processo di creazione di una copia dallo stato archiviato al suo livello di archiviazione online originale.
* Ripristinato (Restored): un oggetto nello stato ripristinato è una copia dell'oggetto archiviato che è stato ripristinato al suo livello di archiviazione online originale per un determinato periodo di tempo. Al termine del periodo, la copia dell'oggetto viene eliminata mentre l'oggetto archiviato viene conservato. 

## Limitazioni
{: #archive-limitations}

Le politiche di archiviazione vengono implementate utilizzando un sottoinsieme dell'operazione API S3 `PUT Bucket Lifecycle Configuration`.  

La funzionalità supportata include:
* La specifica di una data o di un numero di giorni nel futuro quando gli oggetti passano a uno stato archiviato. 
* L'impostazione delle [regole di scadenza](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry) per gli oggetti. 

La funzionalità non supportata include:
* Più regole di transizione per bucket.
* Filtraggio degli oggetti da archiviare utilizzando un prefisso o una chiave oggetto. 
* Organizzazione in livelli tra le classi di archiviazione. 

## Utilizzo dell'API REST e degli SDK
{: #archive-api} 

### Crea la configurazione del ciclo di vita di un bucket
{: #archive-api-create} 
{: http}

Questa implementazione dell'operazione `PUT` utilizza il parametro di query `lifecycle` per configurare le impostazioni di ciclo di vita per il bucket. Questa operazione consente una singola definizione della politica del ciclo di vita per un determinato bucket. La politica viene definita come una regola composta dai seguenti parametri: `ID`, `Status` e `Transition`.
{: http}

L'azione di transizione consente oggetti futuri scritti nel bucket in uno stato archiviato dopo un determinato periodo di tempo. Le modifiche alla politica del ciclo di vita per un bucket vengono **applicate solo ai nuovi oggetti** scritti in tale bucket.

Gli utenti Cloud IAM devono disporre almeno del ruolo di scrittore (`Writer`) per aggiungere una politica del ciclo di vita al bucket.

Gli utenti dell'infrastruttura classica devono disporre delle autorizzazioni di proprietario (Owner) ed essere in grado di creare i bucket nell'account di archiviazione per aggiungere una politica del ciclo di vita al bucket.

Questa operazione non utilizza parametri di query aggiuntivi specifici dell'operazione.
{: http}

Intestazione              | Tipo   |  Descrizione
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | stringa | **Obbligatorio**: l'hash MD5 a 128 bit con codifica base64 del payload, utilizzato come un controllo dell'integrità per garantire che il payload non è stato modificato in transito. 
{: http}

Il corpo della richiesta deve contenere un blocco XML con il seguente schema:
{: http}

|Elemento| Tipo   |Elemento secondario|Predecessore|Vincolo|
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | Nessuno                  | Limite 1.                                                                                  |
| `Rule`                   | Container            | `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` | Limite 1.                                                                                  |
| `ID`                     | Stringa              | Nessuno                                | `Rule`                   | Deve essere composto da (`a-z,`A-Z0-9`) e dai seguenti simboli: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | Stringa              | `Prefix`                               | `Rule`                   | Deve contenere un elemento `Prefix`                                                            |
| `Prefix`                 | Stringa              | Nessuno                                | `Filter`                 | **Deve** essere impostato su `<Prefix/>`.                                                           |
| `Transition`             | `Container`          | `Days`, `StorageClass`                 | `Rule`                   | Limite 1.                                                                                  |
| `Days`                   |Numero interno non negativo| Nessuno                                | `Transition`             | Deve essere un valore maggiore di 0.                                                           |
| `Date`                   | Date                 | Nessuno                                | `Transistion`            | Deve essere nel formato ISO 8601 e la data deve essere futura.                             |
| `StorageClass`           | Stringa              | Nessuno                                | `Transition`             | **Deve** essere impostato su `GLACIER`.                                                             |
{: http}

__Sintassi__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Esempio 1. Osserva l'uso delle barre e dei punti in questo esempio di sintassi." caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
		<Filter>
			<Prefix/>
		</Filter>
		<Transition>
			<Days>{integer}</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Esempio 2. Esempio di XML per la creazione della configurazione del ciclo di vita di un oggetto." caption-side="bottom"}

__Esempi__
{: http}

_Richiesta di esempio_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Esempio 3. Esempi di intestazione della richiesta per la creazione della configurazione del ciclo di vita di un oggetto." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Esempio 4. Esempio di XML per il corpo della richiesta PUT." caption-side="bottom"}

_Risposta di esempio_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Esempio 5. Intestazioni della risposta." caption-side="bottom"}

---

### Richiama la configurazione del ciclo di vita di un bucket
{: #archive-api-retrieve} 
{: http}

Questa implementazione dell'operazione `GET` utilizza il parametro di query `lifecycle` per richiamare le impostazioni del ciclo di vita per il bucket. 

Gli utenti Cloud IAM devono disporre almeno del ruolo di lettore (`Reader`) per richiamare un ciclo di vita per un bucket.

Gli utenti dell'infrastruttura classica devono disporre almeno delle autorizzazioni di lettura (`Read`) per il bucket per richiamare una politica del ciclo di vita per un bucket.

Questa operazione non utilizza intestazioni, parametri di query o payload aggiuntivi specifici dell'operazione. 

__Sintassi__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Esempio 6. Variazioni nella sintassi per le richieste GET." caption-side="bottom"}

__Esempi__
{: http}

_Richiesta di esempio_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Esempio 7. Intestazioni della richiesta di esempio per richiamare la configurazione." caption-side="bottom"}

_Risposta di esempio_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Esempio 8. Intestazioni della risposta di esempio dalla richiesta GET." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Esempio 9. Esempio di XML per il corpo della risposta." caption-side="bottom"}

---

### Elimina la configurazione del ciclo di vita di un bucket
{: #archive-api-delete} {: http}

Questa implementazione dell'operazione `DELETE` utilizza il parametro di query `lifecycle` per rimuovere tutte le impostazioni del ciclo di vita per il bucket. Le transizioni definite dalle regole non verranno più eseguite per i nuovi oggetti.  

**Nota:** le regole di transizione esistenti verranno conservate per gli oggetti che sono stati già scritti nel bucket prima dell'eliminazione delle regole. 

Gli utenti Cloud IAM devono disporre almeno del ruolo di scrittore (`Writer`) per rimuovere una politica del ciclo di vita da un bucket.

Gli utenti dell'infrastruttura classica devono disporre almeno delle autorizzazioni di proprietario (`Owner`) per il bucket per rimuovere una politica del ciclo di vita da un bucket.

Questa operazione non utilizza intestazioni, parametri di query o payload aggiuntivi specifici dell'operazione. 

__Sintassi__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Esempio 10. Osserva l'uso delle barre e dei punti nell'esempio di sintassi." caption-side="bottom"}

__Esempi__
{: http}

_Richiesta di esempio_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Esempio 11. Intestazioni della richiesta di esempio per il verbo HTTP DELETE." caption-side="bottom"}

_Risposta di esempio_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Esempio 12. Risposta di esempio dalla richiesta DELETE." caption-side="bottom"}

---

### Ripristina temporaneamente un oggetto archiviato 
{: #archive-api-restore} {: http}

Questa implementazione dell'operazione `POST` utilizza il parametro di query `restore` per richiedere il ripristino temporaneo di un oggetto archiviato. L'utente deve innanzitutto ripristinare un oggetto archiviato prima di scaricare o modificare l'oggetto. Quando ripristina un oggetto, l'utente deve specificare un periodo trascorso il quale la copia temporanea dell'oggetto verrà eliminata. L'oggetto conserva la classe di archiviazione del bucket.

Può verificarsi un ritardo fino a 12 ore prima che la copia di ripristino sia disponibile per l'accesso. Una richiesta `HEAD` può controllare se la copia ripristinata è disponibile.  

Per ripristinare in modo permanente l'oggetto, l'utente deve copiare l'oggetto ripristinato in un bucket che non ha una configurazione attiva del ciclo di vita. 

Gli utenti Cloud IAM devono disporre almeno del ruolo di scrittore (`Writer`) per ripristinare un oggetto. 

Gli utenti dell'infrastruttura classica devono disporre almeno delle autorizzazioni di scrittore (`Write`) per il bucket e dell'autorizzazione di lettura (`Read`) per l'oggetto per ripristinarlo. 

Questa operazione non utilizza parametri di query aggiuntivi specifici dell'operazione.


Intestazione              | Tipo   |  Descrizione
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | stringa | **Obbligatorio**: l'hash MD5 a 128 bit con codifica base64 del payload, utilizzato come un controllo dell'integrità per garantire che il payload non è stato modificato in transito. 

Il corpo della richiesta deve contenere un blocco XML con il seguente schema:

Elemento| Tipo   |Elemento secondario|Predecessore|Vincolo
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | Container | `Days`, `GlacierJobParameters`    | Nessuno    | Nessuno
`Days`                   |Numero intero| Nessuno | `RestoreRequest` |Specificato il ciclo di vita dell'oggetto ripristinato temporaneamente. Il numero minimo di giorni per cui può esistere una copia ripristinata dell'oggetto è 1. Trascorso il periodo di ripristino, la copia temporanea dell'oggetto verrà rimossa.
`GlacierJobParameters` | Stringa | `Tier` | `RestoreRequest` | Nessuno
`Tier` | Stringa | Nessuno | `GlacierJobParameters` | **Deve** essere impostato su `Bulk`.

Una risposta di esito positivo restituisce un codice `202` se l'oggetto è nello stato archiviato e un codice `200` se l'oggetto è già nello stato ripristinato.  Se l'oggetto è già nello stato ripristinato e viene ricevuta una nuova richiesta di ripristino dell'oggetto, l'elemento `Days` aggiornerà la scadenza dell'oggetto ripristinato. 

__Sintassi__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="Esempio 13. Osserva l'uso delle barre e dei punti nell'esempio di sintassi." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Esempio 14. Modello di XML per il corpo della richiesta." caption-side="bottom"}

__Esempi__
{: http}

_Richiesta di esempio_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Esempio 15. Intestazioni della richiesta di esempio per il ripristino dell'oggetto." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Esempio 16. Corpo della richiesta di esempio per il ripristino dell'oggetto." caption-side="bottom"}

_Risposta di esempio_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Esempio 17. Risposta all'oggetto in fase di ripristino (`HTTP 202`)." caption-side="bottom"}

---

### Ottieni le intestazioni di un oggetto
{: http}
{: #archive-api-head}

Una richiesta `HEAD`, fornito un percorso a un oggetto, richiama le intestazioni di tale oggetto. Questa operazione non utilizza parametri di query o elementi payload specifici dell'operazione. 

__Sintassi__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="Esempio 18. Variazioni nella definizione degli endpoint. " caption-side="bottom"}


__Intestazioni della risposta per gli oggetti archiviati__
{: http}

Intestazione | Tipo | Descrizione
--- | ---- | ------------
`x-amz-restore` | stringa |Inclusa se l'oggetto è stato ripristinato o se è in corso un ripristino. Se l'oggetto è stato ripristinato, viene restituita anche la data di scadenza per la copia temporanea. 
`x-amz-storage-class` | stringa |Restituisce `GLACIER` se archiviato o ripristinato temporaneamente.
`x-ibm-archive-transition-time` | data |Restituisce la data e l'ora in cui è pianificata la transizione dell'oggetto al livello di archivio. 
`x-ibm-transition` | stringa |Inclusa se l'oggetto contiene metadati di transizione e restituisce il livello e l'ora originale della transizione. 
`x-ibm-restored-copy-storage-class` | stringa |Inclusa se un oggetto si trova negli stati `RestoreInProgress` o `Restored` e restituisce la classe di archiviazione del bucket.


_Richiesta di esempio_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="Esempio 19. Esempio che mostra le intestazioni della richiesta." caption-side="bottom"}

_Risposta di esempio_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="Esempio 20. Esempio che mostra le intestazioni della risposta." caption-side="bottom"}


### Crea la configurazione del ciclo di vita di un bucket
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
          },
        ]
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
{: caption="Esempio 21. Esempio che mostra la creazione della configurazione del ciclo di vita." caption-side="bottom"}

### Richiama la configurazione del ciclo di vita di un bucket
{: #archive-node-retrieve} {: javascript}

```js
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
{: caption="Esempio 22. Esempio che mostra il richiamo dei metadati del ciclo di vita." caption-side="bottom"}

### Elimina la configurazione del ciclo di vita di un bucket
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Esempio 23. Esempio che mostra come eliminare la configurazione del ciclo di vita di un bucket." caption-side="bottom"}

### Ripristina temporaneamente un oggetto archiviato 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Esempio 24. Codice utilizzato nel ripristino di un oggetto archiviato. " caption-side="bottom"}

### Ottieni le intestazioni di un oggetto
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Esempio 25. Esempio che mostra il richiamo delle intestazioni dell'oggetto. " caption-side="bottom"}


### Crea la configurazione del ciclo di vita di un bucket
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="Esempio 26. Metodo utilizzato nella creazione della configurazione di un oggetto." caption-side="bottom"}

### Richiama la configurazione del ciclo di vita di un bucket
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Esempio 27. Metodo utilizzato nel richiamo della configurazione di un oggetto. " caption-side="bottom"}

### Elimina la configurazione del ciclo di vita di un bucket
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Esempio 28. Metodo utilizzato nell'eliminazione della configurazione di un oggetto. " caption-side="bottom"}

### Ripristina temporaneamente un oggetto archiviato 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="Esempio 29. Ripristino temporaneo di un oggetto archiviato. " caption-side="bottom"}

### Ottieni le intestazioni di un oggetto
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="Esempio 30. Gestione della risposta per le intestazioni dell'oggetto. " caption-side="bottom"}


### Crea la configurazione del ciclo di vita di un bucket
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Esempio 31. Funzione utilizzata nell'impostazione del ciclo di vita di un bucket." caption-side="bottom"}

**Riepilogo del metodo**
{: java}

Metodo |  Descrizione
--- | ---
`getBucketName()` | Richiama il nome del bucket di cui viene impostata la configurazione del ciclo di vita. 
`getLifecycleConfiguration()` | Richiama la nuova configurazione del ciclo di vita per il bucket specificato.
`setBucketName(String bucketName)` | Imposta il nome del bucket di cui viene impostata la configurazione del ciclo di vita. 
`withBucketName(String bucketName)` | Imposta il nome del bucket di cui viene impostata la configurazione del ciclo di vita in modo che le chiamate aggiuntive al metodo possano essere concatenate tra loro.
{: java}

### Richiama la configurazione del ciclo di vita di un bucket
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Esempio 32. Firma della funzione per ottenere la configurazione del ciclo di vita dell'oggetto. " caption-side="bottom"}

### Elimina la configurazione del ciclo di vita di un bucket
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Esempio 33. Funzione utilizzata nell'eliminazione della configurazione di un oggetto. " caption-side="bottom"}

### Ripristina temporaneamente un oggetto archiviato 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="Esempio 34. Firma della funzione per il ripristino di un oggetto archiviato. " caption-side="bottom"}

**Riepilogo del metodo**
{: java}

Metodo |  Descrizione
--- | ---
`clone()` | Crea un clone per indirizzo (shallow) di questo oggetto per tutti i campi ad eccezione del contesto del gestore. 
`getBucketName()` | Restituisce il nome del bucket che contiene il riferimento all'oggetto da ripristinare. 
`getExpirationInDays()` | Restituisce il periodo di tempo in giorni dalla creazione dell'oggetto alla sua scadenza. 
`setExpirationInDays(int expirationInDays)` | Imposta il periodo di tempo, in giorni, tra il caricamento di un oggetto nel bucket e la sua scadenza. 
{: java}

### Ottieni le intestazioni di un oggetto
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="Esempio 35. Funzione utilizzata per ottenere le intestazioni dell'oggetto. " caption-side="bottom"}

**Riepilogo del metodo**
{: java}

Metodo |  Descrizione
--- | ---
`clone()` | Restituisce un clone di questo `ObjectMetadata`.
`getRestoreExpirationTime()` | Restituisce quando un oggetto che è stato ripristinato temporaneamente da ARCHIVE scadrà e dovrà essere ripristinato di nuovo per potervi accedere.
`getStorageClass() ` | Restituisce la classe di archiviazione originale del bucket.
`getIBMTransition()` | Restituisce la classe di archiviazione della transizione e il momento della transizione. 
{: java}

## Passi successivi
{: #archive-next-steps}

Oltre a {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} fornisce al momento ulteriori offerte di archiviazione oggetti per le diverse esigenze dell'utente, tutte accessibili tramite i portali basati su web e le API REST. [Ulteriori informazioni.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
