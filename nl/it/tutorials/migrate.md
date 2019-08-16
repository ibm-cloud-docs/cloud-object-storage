---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# Migrazione dei dati da OpenStack Swift
{: #migrate}

Prima che {{site.data.keyword.cos_full_notm}} diventasse disponibile come un servizio {{site.data.keyword.cloud_notm}} Platform, i progetti che richiedevano un'archiviazione oggetti utilizzavano [OpenStack Swift](https://docs.openstack.org/swift/latest/) o [OpenStack Swift (infrastructure)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift). Consigliamo agli sviluppatori di aggiornare le proprie applicazioni e migrare i loro dati a {{site.data.keyword.cloud_notm}} per avvalersi dei nuovi vantaggi di controllo dell'accesso e crittografia forniti da IAM e Key Protect, nonché delle nuove funzioni non appena diventano disponibili.

Il concetto di un 'contenitore' Swift è identico a quello di un 'bucket' COS. COS limita le istanze del servizio a 100 bucket mentre alcune istanze Swift possono avere un numero maggiore di contenitori. I bucket COS possono ospitare miliardi di oggetti e supportano le barre (`/`) nei nomi degli oggetti per 'prefissi' simili a directory quando si organizzano i dati. COS supporta le politiche IAM ai livelli di istanza bucket e servizio.
{:tip}

Un approccio di migrazione dei dati tra i servizi di archiviazione oggetti è di utilizzare uno strumento 'sync' o 'clone', ad esempio [il programma di utilità della riga di comando open source `rclone`](https://rclone.org/docs/). Questo programma di utilità sincronizzerà una struttura ad albero di file tra due ubicazioni, inclusa l'archiviazione oggetti. Quando `rclone` scrive i dati in COS, utilizzerà l'API COS/S3 per suddividere gli oggetti molto grandi e carica le parti in parallelo in base alle dimensioni e alle soglie impostate come parametri di configurazione.

Esistono alcune differenze tra COS e Swift che devono essere prese in considerazione come parte della migrazione dei dati.

  - COS non supporta ancora le politiche di scadenza o il controllo delle versioni. I flussi di lavoro che dipendono da queste funzioni Swift devono invece gestirli come parte della propria logica dell'applicazione al momento della migrazione in COS.
  - COS supporta i metadati al livello dell'oggetto, ma queste informazioni non vengono conservate quando si utilizza `rclone` per migrare i dati. Possono essere impostati dei metadati personalizzati sugli oggetti in COS utilizzando un'intestazione `x-amz-meta-{key}: {value}`, ma ti consigliamo di eseguire un backup dei metadati al livello dell'oggetto in un database prima di utilizzare `rclone`. I metadati personalizzati possono essere applicati agli oggetti esistenti [copiando l'oggetto su se stesso](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object) - il sistema riconoscerà che i dati dell'oggetto sono identici e aggiorna solo i metadati. Tieni presente che `rclone` **può** conservare le date/ore.
  - COS utilizza le politiche IAM per l'istanza del servizio e il controllo dell'accesso al livello del bucket. [Gli oggetti possono essere resi disponibili pubblicamente](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access) impostando un ACL `public-read`, che elimina il bisogno di un'intestazione di autorizzazione.
  - [I caricamenti in più parti](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) per gli oggetti molto grandi vengono gestiti in modo diverso dall'API COS/S3 rispetto all'API Swift.
  - COS consente intestazioni HTTP facoltative familiari, ad esempio `Cache-Control`, `Content-Encoding`, `Content-MD5` e `Content-Type`.

Questa guida fornisce le istruzioni per migrare i dati da un singolo contenitore Swift a un singolo bucket COS. Questa operazione dovrà essere ripetuta per tutti i contenitori che vuoi migrare ed in seguito la tua logica dell'applicazione dovrà essere aggiornata in modo da utilizzare la nuova API. Dopo che i dati sono stati migrati puoi verificare l'integrità del trasferimento tramite `rclone check`, che confronterà i checksum MD5 e produrrà un elenco di tutti gli oggetti dove non corrispondono.


## Configura {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. Se non ne hai ancora creata una, esegui il provisioning di un'istanza di {{site.data.keyword.cos_full_notm}} dal [catalogo](https://cloud.ibm.com/catalog/services/cloud-object-storage).
  2. Crea tutti i bucket di cui avrai bisogno per archiviare i tuoi dati trasferiti. Leggi attentamente la [guida introduttiva](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) per familiarizzare con i concetti chiave come [endpoint](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) e [classi di archiviazione](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. Poiché la sintassi dell'API Swift è molto diversa da quella dell'API COS/S3, può essere necessario riorganizzare la tua applicazione in modo da poter utilizzare i metodi equivalenti forniti negli SDK COS. Le librerie sono disponibili in ([Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)) o nell'[API REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Configura una risorsa di calcolo per eseguire lo strumento di migrazione.
{: #migrate-compute}
  1. Scegli una macchina Linux/macOS/BSD o un IBM Cloud Infrastructure Bare Metal o Virtual Server
     con la migliore prossimità ai tuoi dati.
     Ti consigliamo la seguente configurazione server:  32 GB di RAM, 2-4 processori core e velocità di rete privata di 1000 Mbps.  
  2. Se stai eseguendo la migrazione su un IBM Cloud Infrastructure Bare Metal o Virtual Server
     utilizza gli endpoint **privati** Swift e COS.
  3. Altrimenti utilizza gli endpoint **pubblici** Swift e COS.
  4. Installa `rclone` da [un gestore pacchetti o un file binario precompilato](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Configura `rclone` per OpenStack Swift
{: #migrate-rclone}

  1. Crea un file di configurazione `rclone` in `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Crea un'origine Swift copiando quanto segue e incollandolo in `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. Ottieni le credenziali OpenStack Swift
    <br>a. Fai clic sulla tua istanza Swift nel [dashboard della console IBM Cloud](https://cloud.ibm.com/).
    <br>b. Fai clic su **Service Credentials** nel pannello di navigazione.
    <br>c. Fai clic su **New credential** per generare le informazioni sulle credenziali. Fai clic su **Add**.
    <br>d. Visualizza le credenziali che hai creato e copia i contenuti JSON.

  4. Compila i seguenti campi:

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. Passa alla sezione Configura `rclone` per COS


## Configura `rclone` per OpenStack Swift (infrastructure)
{: #migrate-config-swift}

  1. Crea un file di configurazione `rclone` in `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Crea un'origine Swift copiando quanto segue e incollandolo in `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. Ottieni le credenziali OpenStack Swift (infrastructure)
    <br>a. Fai clic sul tuo account Swift nel portale clienti dell'infrastruttura IBM Cloud.
    <br>b. Fai clic sul data center del contenitore di origine della migrazione.
    <br>c. Fai clic su **View Credentials**.
    <br>d. Copia quanto segue.
      <br>&nbsp;&nbsp;&nbsp;**Nomeutente**
      <br>&nbsp;&nbsp;&nbsp;**Chiave API**
      <br>&nbsp;&nbsp;&nbsp;**Endpoint di autenticazione** in base a dove stai eseguendo lo strumento di migrazione

  4. Utilizzando le credenziali OpenStack Swift (infrastructure), compila i seguenti campi:

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## Configura `rclone` per COS
{: #migrate-config-cos}

### Ottieni le credenziali COS
{: #migrate-config-cos-credential}

  1. Fai clic sulla tua istanza COS nella console IBM Cloud.
  2. Fai clic su **Service Credentials** nel pannello di navigazione.
  3. Fai clic su **New credential** per generare le informazioni sulle credenziali. 
  4. In **Inline Configuration Parameters** aggiungi `{"HMAC":true}`. Fai clic su **Add**.
  5. Visualizza le credenziali che hai creato e copia i contenuti JSON.

### Ottieni l'endpoint COS
{: #migrate-config-cos-endpoint}

  1. Fai clic su **Buckets** nel pannello di navigazione.
  2. Fai clic sul bucket di destinazione della migrazione.
  3. Fai clic su **Configuration** nel pannello di navigazione.
  4. Scorri verso il basso fino alla sezione **Endpoints** e scegli l'endpoint in base a dove
     stai eseguendo lo strumento di migrazione.

  5. Crea la destinazione COS copiando quanto segue e incollandolo in `rclone.conf`.

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. Utilizzando le credenziali e l'endpoint COS, compila i seguenti campi:

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## Verifica che l'origine e la destinazione della migrazione siano configurate correttamente
{: #migrate-verify}

1. Elenca il contenitore Swift per verificare che `rclone` sia configurato correttamente.

    ```
    rclone lsd SWIFT:
    ```

2. Elenca il bucket COS per verificare che `rclone` sia configurato correttamente.

    ```
    rclone lsd COS:
    ```

## Esegui `rclone`
{: #migrate-run}

1. Effettua un test (nessun dato copiato) di `rclone` per sincronizzare gli oggetti nel tuo contenitore
   Swift di origine (ad es. `swift-test`) nel bucket COS di destinazione (ad es. `cos-test`).

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. Controlla che i file che desideri migrare siano visualizzati nell'output del comando. Se tutto sembra a posto, rimuovi l'indicatore `--dry-run` e aggiungi l'indicatore `-v` per copiare i dati. L'utilizzo dell'indicatore `--checksum` facoltativo eviterà l'aggiornamento di tutti i file con gli stessi hash MD5 e dimensione oggetto in entrambe le ubicazioni.

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   Dovresti tentare di sfruttare al massimo la CPU, la memoria e la rete sulla macchina che esegue rclone per ottenere il tempo di trasferimento più veloce.
   Ci sono pochi altri parametri da prendere in considerazione per l'ottimizzazione di rclone:

   --checkers int  Numero di programmi di controllo da eseguire in parallelo. (valore predefinito 8)
   Questo è il numero di checksum che confrontano le soglie in esecuzione. Ti consigliamo di aumentarlo a 64 o più.

   --transfers int Numero di trasferimenti file da eseguire in parallelo. (valore predefinito 4)
   Questo è il numero di oggetti da trasferire in parallelo. Ti consigliamo di aumentarlo a 64 o 128 o un valore superiore.

   --fast-list Utilizza l'elenco ricorrente se disponibile. Utilizza più memoria ma meno transazioni.
   Utilizza questa opzione per migliorare le prestazioni - riduce il numero di richieste necessarie per copiare un oggetto.

La migrazione dei dati tramite `rclone` copia ma non elimina i dati di origine.
{:tip}


3. Ripeti questa procedura per tutti gli altri contenitori che richiedono la migrazione.
4. Dopo che sono stati copiati tutti i tuoi dati e che hai verificato che la tua applicazione può accedere ai dati in COS, elimina la tua istanza del servizio Swift.
