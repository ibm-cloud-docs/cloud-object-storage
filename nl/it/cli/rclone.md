---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# Utilizzo di `rclone`
{: #rclone}

## Installa `rclone`
{: #rclone-install}

Lo strumento `rclone` è utile per mantenere sincronizzate le directory e per migrare i dati tra le piattaforme di archiviazione. È un programma Go e viene fornito come un singolo file binario. 

### Installazione rapida
{: #rclone-quick}

*  [Scarica](https://rclone.org/downloads/) il file binario pertinente. 
*  Estrai il file binario `rclone` o `rclone.exe` dall'archivio. 
*  Esegui `rclone config` per la configurazione.

### Installazione utilizzando uno script
{: #rclone-script}

Installa `rclone` sui sistemi Linux/macOS/BSD:

```
curl https://rclone.org/install.sh | sudo bash
```

Sono disponibili anche le versioni beta:

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

Lo script di installazione controlla innanzitutto la versione installata di `rclone` e non scarica alcun file se la versione corrente è già aggiornata.
{:note}

### Installazione su Linux dal file binario precompilato
{: #rclone-linux-binary}

Innanzitutto, recupera e spacchetta il file binario: 

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

Poi, copia il file binario in un'ubicazione adatta:

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

Installa la documentazione:

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

Esegui `rclone config` per la configurazione:

```
rclone config
```

### Installazione su macOS dal file binario precompilato
{: #rclone-osx-binary}

Innanzitutto, scarica il pacchetto `rclone`:

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

Poi, estrai il file scaricato ed esegui `cd` alla cartella estratta:

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

Sposta `rclone` nel tuo `$PATH` e immetti la tua password quando richiesto:

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

Il comando `mkdir` è sicuro da eseguire, anche se la directory esiste.
{:tip}

Rimuovi i file rimanenti.

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

Esegui `rclone config` per la configurazione:

```
rclone config
```

## Configura l'accesso a IBM COS
{: #rclone-config}

1. Esegui `rclone config` e seleziona `n` per un nuovo remoto.

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. Immetti il nome per la configurazione:
```
	name> <YOUR NAME>
```

3. Seleziona l'archivio “s3”.

```
	Choose a number from below, or type in your own value
		1 / Alias for a existing remote
		\ "alias"
		2 / Amazon Drive
		\ "amazon cloud drive"
		3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
		\ "s3"
		4 / Backblaze B2
		\ "b2"
	[snip]
		23 / http Connection
	  \ "http"
	Storage> 3
```

  4. Seleziona IBM COS come il provider di archiviazione S3. 

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  1. Immetti **False** per immettere le tue credenziali.

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. Immetti la chiave di accesso e il segreto. 

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. Specifica l'endpoint per IBM COS. Per IBM COS pubblico, scegli tra le opzioni fornite. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). 

```
Endpoint for IBM COS S3 API.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3.us.cloud-object-storage.appdomain.cloud"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. Specifica un vincolo di ubicazione IBM COS. Il vincolo di ubicazione deve corrispondere all'endpoint. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). 

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. Specifica un ACL. Sono supportati solo `public-read` e `private`.  

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. Esamina la configurazione visualizzata e accetta di salvare il “remoto”, quindi esci. Il file di configurazione dovrebbe essere simile a questo

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## Riferimento ai comandi
{: #rclone-reference}

### Crea un bucket
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### Elenca i bucket disponibili
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### Elenca il contenuto di un bucket
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### Copia un file dal locale al remoto
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### Copia un file dal remoto al locale
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### Elimina un file sul remoto
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### Comandi di elenco
{: #rclone-reference-listing}

Ci sono diversi comandi di elenco correlati
* `ls` per elencare solo la dimensione e il percorso degli oggetti
* `lsl` per elencare solo l'ora di modifica, la dimensione e il percorso degli oggetti
* `lsd` per elencare solo le directory
* `lsf` per elencare gli oggetti e le directory in un formato facile da analizzare
* `lsjson` per elencare gli oggetti e le directory in formato JSON

## `rclone sync`
{: #rclone-sync}

L'operazione `sync` rende identici l'origine e la destinazione e modifica solo la destinazione. La sincronizzazione non trasferisce i file invariati, viene controllata la dimensione e l'ora di modifica oppure MD5SUM. La destinazione viene aggiornata in modo da corrispondere all'origine, inclusa l'eliminazione di file, se necessario. 

Poiché ciò può causare la perdita di dati, verifica innanzitutto l'indicatore `--dry-run` per vedere esattamente cosa dovrebbe essere copiato ed eliminato.
{:important}

Tieni presente che i file nella destinazione non verranno eliminati se si sono verificati degli errori in qualsiasi momento. 

Viene sincronizzato il _contenuto_ della directory, non la directory stessa. Quando `source:path` è una directory, è il contenuto di `source:path` ad essere copiato, non il nome della directory e il contenuto. Per ulteriori informazioni, vedi la spiegazione estesa nel comando `copy`.

Se `dest:path` non esiste, viene creato e il contenuto di `source:path` viene copiato in esso. 

```sh
rclone sync source:path dest:path [flags]
```

### Utilizzo di `rclone` da più ubicazioni contemporaneamente
{: #rclone-sync-multiple}

Puoi utilizzare `rclone` da più ubicazioni contemporaneamente se scegli una sottodirectory diversa per l'output:

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

Se esegui la sincronizzazione (`sync`) nella stessa directory, devi utilizzare `rclone copy` altrimenti i due processi potrebbero eliminare l'uno i file dell'altro:

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

Quando utilizzi `sync`, `copy` o `move`, i file che sono stati sovrascritti o eliminati vengono spostati nella loro gerarchia originale in questa directory.

Se `--suffix` è impostato, ai file spostati viene aggiunto il suffisso. Se è presente un file con lo stesso percorso (dopo l'aggiunta del suffisso) nella directory, viene sovrascritto. 

Il remoto in uso deve supportare lo spostamento o la copia del lato server e devi utilizzare lo stesso remoto della destinazione della sincronizzazione. La directory di backup non deve sovrapporsi a quella di destinazione. 

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

sincronizzerà (`sync`) `/path/to/local` in `remote:current`, ma per i file che sono stati aggiornati o eliminati, verranno archiviati in `remote:old`.

Se esegui `rclone` da uno script, potresti voler utilizzare la data di oggi come il nome directory passato a `--backup-dir` per archiviare i file più vecchi oppure potresti voler passare `--suffix` con la data di oggi.

## `rclone` daily sync
{: #rclone-sync-daily}

La pianificazione di un backup è importante per automatizzare i backup. Tale procedura varia a seconda della tua piattaforma. Windows può utilizzare l'Utilità di pianificazione (Task Scheduler) mentre MacOS e Linux possono utilizzare i crontab.

### Sincronizzazione di una directory
{: #rclone-sync-directory}

`Rclone` sincronizza una directory locale con il contenitore remoto, archiviando tutti i file nella directory locale nel contenitore. `Rclone` utilizza la sintassi, `rclone sync source destination`, dove `source` è la cartella locale e `destination` è il contenitore all'interno del tuo IBM COS.

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

È possibile che tu abbia già una destinazione che è stata creata, ma se questo non fosse il caso, puoi creare un nuovo bucket utilizzando i passi sopra riportati.

### Pianificazione di un lavoro
{: #rclone-sync-schedule}

Prima di pianificare un lavoro, assicurati di aver eseguito il tuo caricamento iniziale e di averlo completato. 

#### Windows
{: #rclone-sync-windows}

1. Crea un file di testo denominato `backup.bat` sul tuo computer e incollalo nel comando che hai utilizzato nella sezione relativa alla [sincronizzazione di una directory](#rclone-sync-directory).  Specifica il percorso completo al file rclone.exe e non dimenticare di salvare il file. 

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. Utilizza `schtasks` per pianificare un lavoro. Questo programma di utilità utilizza diversi parametri. 
	* /RU – l'utente con il quale eseguire il lavoro. È necessario se l'utente che vuoi utilizzare si è scollegato. 
	* /RP – la password per l'utente.
	* /SC – impostato su DAILY
	* /TN – il nome del lavoro. Chiamalo backup
	* /TR – il percorso al file backup.bat che hai appena creato. 
	* /ST – l'ora di inizio dell'attività. Viene espressa nel formato di 24 ore. 01:05:00 indica 1:05 AM. 13:05:00 indicherebbe 1:05 PM.

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac e Linux
{: #rclone-sync-nix}

1. Crea un file di testo denominato `backup.sh` sul tuo computer e incolla il comando che hai utilizzato nella sezione relativa alla [sincronizzazione di una directory](#rclone-sync-directory). È qualcosa di simile a quanto riportato di seguito. Specifica il percorso completo all'eseguibile rclone e non dimenticare di salvare il file. 

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. Rendi eseguibile lo script con `chmod`.

```sh
chmod +x backup.sh
```

3. Modifica i crontab.

```sh
sudo crontab -e
```

4. Aggiungi una voce alla fine del file dei crontab. I crontab sono semplici: i primi cinque campi rappresentano, nell'ordine, minuti, ore, giorni, mesi e giorni feriali. L'utilizzo di * indica tutti. Per fare in modo che `backup.sh` venga eseguito ogni giorno alle 1:05 AM, utilizza qualcosa di simile a questo: 

```sh
5 1 * * * /full/path/to/backup.sh
```

5. Salva i crontab e sei pronto a iniziare. 
