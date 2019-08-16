---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# Monta un bucket utilizzando `s3fs`
{: #s3fs}

Le applicazioni che prevedono di leggere e di scrivere in un filesystem in stile NFS possono utilizzare `s3fs` che può montare un bucket come una directory mentre preserva il formato oggetto nativo per i file. Ciò ti consente di interagire con la tua archiviazione cloud utilizzando comandi shell familiari, come `ls` per elencare o `cp` per copiare i file, come pure fornendo l'accesso alle applicazioni legacy che si basano sulla lettura e sulla scrittura dai file locali. Per una panoramica più dettagliata, [consulta il README ufficiale del progetto](https://github.com/s3fs-fuse/s3fs-fuse).

## Prerequisiti
{: #s3fs-prereqs}

* Un account IBM Cloud e un'istanza di {{site.data.keyword.cos_full}}
* Un ambiente Linux o OSX
* Le credenziali (una [chiave API IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) o le [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## Installazione
{: #s3fs-install}

Su OSX, utilizza [Homebrew](https://brew.sh/):

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

Su Debian o Ubuntu: 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

La documentazione `s3fs` ufficiale suggerisce di utilizzare `libcurl4-gnutls-dev` invece di `libcurl4-openssl-dev`. Funzionano entrambi, ma la versione OpenSSL può offrire prestazioni migliori.
{:tip}

Puoi anche creare `s3fs` dall'origine. Innanzitutto, clona il repository Github:

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

Quindi crea `s3fs`:

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

E installa il file binario:

```sh
sudo make install
```
{:codeblock}

## Configurazione
{: #s3fs-config}

Archivia le tue credenziali in un file che contiene `<access_key>:<secret_key>` o `:<api_key>`. Questo file deve avere accesso limitato, quindi esegui: 

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

Ora puoi montare un bucket utilizzando:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

Se il file delle credenziali ha solo una chiave API (nessuna credenziale HMAC), dovrai aggiungere anche l'indicatore `ibm_iam_auth`:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

`<bucket>` è un bucket esistente e `<mountpoint>` è la directory locale in cui vuoi montare il bucket. `<endpoint>` deve corrispondere all'[ubicazione del bucket](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints). `credentials_file` è il file creato con la chiave API o le credenziali HMAC.

Ora, `ls <mountpoint>` elencherà gli oggetti presenti in tale bucket come se fossero file locali (o, nel caso dei prefissi oggetto, come se fossero directory nidificate).

## Ottimizzazione delle prestazioni
{: #s3fs-performance}

Poiché le prestazioni non saranno mai uguali a quelle di un vero filesystem locale, è possibile utilizzare alcune delle opzioni avanzate per aumentare la velocità effettiva. 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM` è rilevante solo quando utilizzi un endpoint HTTPS. Per impostazione predefinita, le connessioni sicure a IBM COS utilizzano la suite di cifratura `AES256-SHA`. Se invece utilizzi la suite `AESGCM`, riduci notevolmente il sovraccarico della CPU sulla tua macchina client, causato dalle funzioni di crittografia TLS, offrendo lo stesso livello di sicurezza crittografica. 
2. `kernel_cache` abilita la cache del buffer del kernel sul tuo punto di montaggio `s3fs`. Ciò significa che gli oggetti verranno letti solo una volta da `s3fs`, poiché la lettura ripetitiva dello stesso file può essere fornita dalla cache del buffer del kernel. La cache del buffer del kernel utilizzerà solo la memoria libera non utilizzata da altri processi. Questa opzione è sconsigliata se prevedi che gli oggetti bucket vengano sovrascritti da un altro processo o da un'altra macchina mentre il bucket viene montato e il tuo caso di utilizzo richiede un accesso attivo al contenuto più recente.  
3. `max_background=1000` migliora le prestazioni della lettura simultanea del file `s3fs`. Per impostazione predefinita, FUSE supporta le richieste di lettura fino a 128 KB. Quando viene richiesta la lettura di dimensioni maggiori, il kernel suddivide la richiesta di grandi dimensioni in sottorichieste più piccole e consente a s3fs di elaborarle in modo asincrono. L'opzione `max_background` imposta il numero massimo globale di tali richieste asincrone simultanee. Per impostazione predefinita, lo imposta su 12, ma se viene impostato su un valore elevato arbitrario (1000), le richieste di lettura non verranno bloccate anche quando viene letto simultaneamente un numero elevato di file. 
4. `max_stat_cache_size=100000` riduce il numero di richieste `HEAD` HTTP ridondanti inviate da `s3fs` e riduce il tempo impiegato per elencare una directory o richiamare gli attributi del file. L'utilizzo normale del file system rende frequente l'accesso ai metadati di un file tramite una chiamata `stat()` che viene associata alla richiesta `HEAD` sul sistema dell'archiviazione oggetti. Per impostazione predefinita, `s3fs` memorizza nella cache gli attributi (metadati) di massimo 1000 oggetti. Ogni voce memorizzata nella cache occupa fino a 0,5 KB di memoria. Idealmente, vorresti che la cache fosse in grado di contenere i metadati di tutti gli oggetti nel tuo bucket. Tuttavia, puoi voler considerare le implicazioni sull'utilizzo della memoria di questa memorizzazione in cache. Se lo imposti su `100000`, non occuperà più di 0,5 KB * 100000 = 50 MB.
5. `multipart_size=52` imposterà la dimensione massima delle richieste e delle risposte inviate e ricevute dal server COS, in scala di MB. `s3fs` lo imposta su 10 MB per impostazione predefinita. L'aumento di questo valore aumenta anche la velocità effettiva (MB/s) per connessione HTTP. D'altro canto, aumenterà, rispettivamente, la latenza per il primo byte fornito dal file. Pertanto, se il tuo caso di utilizzo legge solo una piccola quantità di dati da ciascun file, probabilmente non vuoi aumentare questo valore. Inoltre, per gli oggetti di grandi dimensioni (diciamo più di 50 MB), la velocità effettiva aumenta se questo valore è sufficientemente piccolo da consentire simultaneamente il recupero del file utilizzando più richieste. Ritengo che il valore ottimale per questa opzione sia di circa 50 MB. Le procedure consigliate di COS suggeriscono di utilizzare richieste che siano multipli di 4 MB, quindi il consiglio è quello di impostare questa opzione su 52 (MB).
6. `parallel_count=30` imposta il numero massimo di richieste inviate simultaneamente a COS, per singola operazione di lettura/scrittura file. Per impostazione predefinita, è impostato su 5. Per oggetti molto grandi, puoi ottenere maggiore velocità effettiva aumentando questo valore. Analogamente alla precedente opzione, mantieni basso questo valore se leggi solo una piccola quantità di dati di ciascun file. 
7. `multireq_max=30` quando elenchi una directory, viene inviata una richiesta di metadati dell'oggetto (`HEAD`) per ciascun oggetto nell'elenco (a meno che i metadati non si trovino nella cache). Questa opzione limita il numero di richieste simultanee di questo tipo inviate a COS, per una singola operazione di elenco della directory. Per impostazione predefinita, è impostato su 20. Tieni presente che questo valore deve essere maggiore o uguale all'opzione `parallel_count` sopra citata. 
8. `dbglevel=warn` imposta il livello di debug su `warn` invece del valore predefinito (`crit`) per registrare i messaggi in /var/log/syslog.

## Limitazioni
{: #s3fs-limitations}

È importante ricordare che s3fs potrebbe non essere adatto a tutte le applicazioni poiché i servizi Object Storage hanno una latenza elevata per TTFB (time to first byte) e per la mancanza di accesso casuale in scrittura. I carichi di lavoro che leggono solo file di grandi dimensioni, ad esempio i carichi di lavoro di apprendimento approfondito, possono raggiungere una buona velocità effettiva utilizzando `s3fs`. 
