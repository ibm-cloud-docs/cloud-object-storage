---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# Gestisci la crittografia
{: #encryption}

Tutti gli oggetti archiviati in {{site.data.keyword.cos_full}} vengono crittografati per impostazione predefinita utilizzando [chiavi generate casualmente e una modalità AONT (all-or-nothing-transform)](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security). Nonostante questo modello di crittografia fornisca la sicurezza sui dati inattivi, alcuni carichi di lavoro devono disporre delle chiavi di crittografia utilizzate. Puoi gestire le tue chiavi manualmente fornendo le tue chiavi di crittografia quando archivi i dati (SSE-C) oppure puoi creare bucket che utilizzano IBM Key Protect (SSE-KP) per gestire le chiavi di crittografia. 

## Server Side Encryption with Customer-Provided Keys (SSE-C)
{: #encryption-sse-c}

SSE-C viene applicato agli oggetti. Le richieste di lettura o scrittura degli oggetti o dei loro metadati utilizzando le chiavi gestite del cliente inviano le informazioni di crittografia richieste come intestazioni nelle richieste HTTP. La sintassi è identica all'API S3 e le librerie compatibili con S3 che supportano SSE-C dovrebbero funzionare come previsto in {{site.data.keyword.cos_full}}.

Qualsiasi richiesta che utilizza le intestazioni SSE-C deve essere inviata utilizzando SSL. Tieni presente che i valori `ETag` nelle intestazioni della risposta *non* sono l'hash MD5 dell'oggetto, ma una stringa esadecimale a 32 bit generata casualmente. 

Intestazione | Tipo | Descrizione
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | stringa | Questa intestazione viene utilizzata per specificare l'algoritmo e la dimensione chiave da utilizzare con la chiave di crittografia archiviata nell'intestazione `x-amz-server-side-encryption-customer-key`. Questo valore deve essere impostato sulla stringa `AES256`. 
`x-amz-server-side-encryption-customer-key` | stringa | Questa intestazione viene utilizzata per trasportare la rappresentazione della stringa di byte con codifica base64 della chiave AES 256 utilizzata nel processo SSE (server side encryption).
`x-amz-server-side-encryption-customer-key-MD5` | stringa | Questa intestazione viene utilizzata per trasportare il digest MD5 a 128 bit con codifica base64 della chiave di crittografia in base all'RFC 1321. L'archivio oggetti utilizzerà questo valore per convalidare che la chiave che viene passata in `x-amz-server-side-encryption-customer-key` non è stata danneggiata durante il trasporto e il processo di codifica. Il digest deve essere calcolato sulla chiave PRIMA che la chiave diventi con codifica base64. 


## Server Side Encryption with {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} è un sistema di gestione delle chiavi (key management system - KMS) centralizzato per creare, gestire ed eliminare le chiavi di crittografia utilizzate dai servizi {{site.data.keyword.cloud_notm}}. Puoi creare un'istanza di {{site.data.keyword.keymanagementserviceshort}} dal catalogo {{site.data.keyword.cloud_notm}}.

Una volta che disponi di un'istanza di {{site.data.keyword.keymanagementserviceshort}} in una regione in cui vuoi creare un nuovo bucket, devi creare una chiave root e prendere nota del CRN di tale chiave.

Puoi scegliere di utilizzare {{site.data.keyword.keymanagementserviceshort}} per gestire la crittografia di un bucket solo al momento della creazione. Non puoi modificare un bucket esistente in modo che utilizzi {{site.data.keyword.keymanagementserviceshort}}.
{:tip}

Quando crei il bucket, devi fornire intestazioni aggiuntive. 

Per ulteriori informazioni su {{site.data.keyword.keymanagementservicelong_notm}}, [vedi la documentazione](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect).

### Introduzione a SSE-KP
{: #sse-kp-gs}

Tutti gli oggetti archiviati in {{site.data.keyword.cos_full}} vengono crittografati per impostazione predefinita utilizzando più chiavi generate casualmente e una modalità AONT (all-or-nothing-transform). Nonostante questo modello di crittografia fornisca la sicurezza sui dati inattivi, alcuni carichi di lavoro devono disporre delle chiavi di crittografia utilizzate. Puoi utilizzare [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) per creare, aggiungere e gestire le chiavi che poi puoi associare alla tua istanza di {{site.data.keyword.cos_full}} per crittografare i bucket.

### Prima di iniziare
{: #sse-kp-prereqs}

Avrai bisogno di:
  * un [account della piattaforma {{site.data.keyword.cloud}}](http://cloud.ibm.com)
  * un'[istanza di {{site.data.keyword.cos_full_notm}}](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * un'[istanza di {{site.data.keyword.keymanagementservicelong_notm}}](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * e alcuni file sul tuo computer locale da caricare.

### Crea o aggiungi una chiave in {{site.data.keyword.keymanagementserviceshort}}
{: #sse-kp-add-key}

Passa alla tua istanza di {{site.data.keyword.keymanagementserviceshort}} e [genera o immetti una chiave](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

### Concedi l'autorizzazione di servizio
{: #sse-kp}
Autorizza {{site.data.keyword.keymanagementserviceshort}} per essere utilizzato con IBM COS:

1. Apri il tuo dashboard {{site.data.keyword.cloud_notm}}.
2. Dalla barra dei menu, fai clic su **Gestisci ** &gt; **Account** &gt; **Utenti**. 
3. Nella navigazione laterale, fai clic su **Identità e accesso** &gt; **Autorizzazioni**.
4. Fai clic su **Crea autorizzazione**.
5. Nel menu **Servizio di origine**, seleziona **Cloud Object Storage**.
6. Nel menu **Istanza servizio di origine**, seleziona l'istanza del servizio da autorizzare. 
7. Nel menu **Servizio di destinazione**, seleziona **{{site.data.keyword.keymanagementservicelong_notm}}**.
8. Nel menu **Istanza servizio di destinazione**, seleziona l'istanza del servizio da autorizzare. 
9. Abilita il ruolo **Lettore**.
10. Fai clic su **Autorizza**.

### Crea un bucket
{: #encryption-createbucket}

Quando la tua chiave è presente in {{site.data.keyword.keymanagementserviceshort}} e hai autorizzato il servizio Key Protect per essere utilizzato con IBM COS, associa la chiave a un nuovo bucket:

1. Passa alla tua istanza di {{site.data.keyword.cos_short}}.
2. Fai clic su **Create bucket**.
3. Immetti un nome bucket, seleziona la resilienza **regionale** e scegli un'ubicazione e una classe di archiviazione. 
4. In Advanced Configuration, abilita **Add Key Protect Keys**.
5. Seleziona l'istanza, la chiave e l'ID chiave del servizio Key Protect associato. 
6. Fai clic su **Create**.

Nell'elenco **Buckets and objects**, il bucket mostra ora un'icona a forma di chiave in **Advanced**, ciò indica che il bucket ha una chiave Key Protect abilitata. Per visualizzare i dettagli della chiave, fai clic sul menu alla destra del bucket e poi fai clic su **View Key Protect key**.

Tieni presente che il valore `Etag` restituito per gli oggetti crittografati utilizzando SSE-KP **sarà** l'hash MD5 effettivo dell'oggetto non crittografato originale.
{:tip}


## Rotazione delle chiavi
{: #encryption-rotate}

La rotazione delle chiavi è una parte importante dell'attenuazione del rischio di una violazione dei dati. Modificando periodicamente le chiavi, riduci la potenziale perdita di dati nel caso in cui la chiave venga persa o compromessa. La frequenza delle rotazioni delle chiavi varia in base all'organizzazione e dipende da un numero di variabili inclusi l'ambiente, la quantità di dati crittografati, la classificazione dei dati e le leggi di conformità. Il [National Institute of Standards and Technology (NIST)](https://www.nist.gov/topics/cryptography){:new_window} fornisce le definizioni delle lunghezze delle chiavi appropriate e le linee guida relative a quanto tempo devono essere utilizzate le chiavi. 

### Rotazione manuale delle chiavi
{: #encryption-rotate-manual}

Per ruotare le chiavi per il tuo {{site.data.keyword.cos_short}}, dovrai creare un nuovo bucket con Key Protect abilitato utilizzando una nuova chiave root e copiare il contenuto dal tuo bucket esistente a quello nuovo. 

**NOTA**: l'eliminazione di una chiave dal sistema eliminerà il suo contenuto e i dati ancora crittografati con tale chiave. Una volta rimossa, l'operazione non può essere annullata e il risultato sarà una perdita permanente dei dati. 

1. Crea o aggiungi una nuova chiave root nel tuo servizio [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).
2. [Crea un nuovo bucket](#encryption-createbucket) e aggiungi la chiave root
3. Copia tutti gli oggetti dal tuo bucket originale al nuovo. 
    1. Questo passo può essere eseguito utilizzando diversi metodi: 
        1. Dalla riga di comando utilizzando [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) o [AWS CLI](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)
        2. Utilizzando (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]
        3. Utilizzando l'SDK con [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) o [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)
