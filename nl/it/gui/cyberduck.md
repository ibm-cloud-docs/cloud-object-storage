---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# Trasferisci i file con Cyberduck
{: #cyberduck}

Cyberduck è un browser di Cloud Object Storage popolare, open source e facile da usare per Mac e Windows. Cyberduck è in grado di calcolare le firme di autorizzazione corrette necessarie per connettersi a IBM COS. Cyberduck è scaricabile da [cyberduck.io/](https://cyberduck.io/){: new_window}.

Per utilizzare Cyberduck per creare una connessione a IBM COS e sincronizzare una cartella di file locali con un bucket, attieniti alla seguente procedura:

 1. Scarica, installa e avvia Cyberduck.
 2. Viene visualizzata la finestra principale dell'applicazione, in cui puoi creare una connessione a IBM COS. Fai clic su **Open Connection** per configurare una connessione a IBM COS.
 3. Viene aperta una finestra a comparsa. Dal menu a discesa in alto, seleziona `S3 (HTTPS)`. Immetti le informazioni nei seguenti campi, quindi fai clic su Connect:

    * `Server`: immetti l'endpoint di IBM COS
        * *Assicurati che la regione dell'endpoint corrisponda al bucket previsto. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Access Key ID`
    * `Secret Access Key`
    * `Add to Keychain`: salva la connessione nel keychain per consentirne l'uso su altre applicazioni *(facoltativo)*

 4. Cyberduck ti porta alla root dell'account dove è possibile creare i bucket.
    * Fai clic con il tasto destro del mouse nel riquadro principale e seleziona **New Folder** (*l'applicazione gestisce molti protocolli di trasferimento in cui la cartella è il costrutto contenitore più comune*).
    * Immetti il nome del bucket e fai clic su Create.
 5. Una volta creato, fai doppio clic sul bucket per visualizzarlo. All'interno del bucket puoi eseguire varie funzioni come:
    * Caricare i file dal bucket
    * Elencare i contenuti del bucket
    * Scaricare gli oggetti dal bucket
    * Sincronizzare i file locali con un bucket
    * Sincronizzare gli oggetti con un altro bucket
    * Creare un archivio di un bucket
 6. Fai clic con il tasto destro del mouse all'interno del bucket e seleziona **Synchronize**. Viene visualizzata una finestra a comparsa in cui puoi esaminare la cartella che vuoi sincronizzare con il bucket. Seleziona la cartella e fai clic su Choose.
 7. Dopo aver selezionato la cartella, si apre una nuova finestra a comparsa. Qui è disponibile un menu a discesa che ti permette di selezionare l'operazione di sincronizzazione con il bucket. Dal menu sono disponibili tre possibili opzioni di sincronizzazione:

    * `Download`: consente di scaricare gli oggetti modificati e mancanti dal bucket.
    * `Upload`: consente di caricare i file modificati e mancanti nel bucket.
    * `Mirror`: consente di eseguire entrambe le operazioni di download e caricamento, garantendo che tutti i file e gli oggetti nuovi e aggiornati siano sincronizzati tra la cartella locale e il bucket.

 8. Si apre un'altra finestra per mostrare le richieste di trasferimento attive e storiche. Una volta completata la richiesta di sincronizzazione, la finestra principale eseguirà un'operazione di elenco sul bucket per riflettere il contenuto aggiornato nel bucket.

## Mountain Duck
{: #mountain-duck}

Mountain Duck si basa su Cyberduck per consentirti di montare Cloud Object Storage come disco in Finder su Mac o Explorer su Windows. Sono disponibili versioni di prova ma è necessaria una chiave di registrazione per l'uso continuato.

La creazione di un segnalibro in Mountain Duck è molto simile alla creazione di connessioni in Cyberduck:

1. Scarica, installa e avvia Mountain Duck
2. Crea un nuovo segnalibro
3. Dal menu a discesa, seleziona `S3 (HTTPS)` e immetti le seguenti informazioni:
    * `Server`: immetti l'endpoint di IBM COS 
        * *Assicurati che la regione dell'endpoint corrisponda al bucket previsto. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Username`: immetti la chiave di accesso
    * Fai clic su **Connect**
    * Ti viene richiesta la tua chiave segreta, che verrà quindi salvata nel keychain.

I tuoi bucket saranno ora disponibili in Finder o Explorer. Puoi interagire con {{site.data.keyword.cos_short}} come qualsiasi altro file system montato.

## CLI
{: #cyberduck-cli}

Cyberduck fornisce anche `duck`, una CLI (command-line interface) che viene eseguita in shell su Linux, Mac OS X e Windows. Le istruzioni di installazione sono disponibili nella [pagina wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window} `duck`.

Per utilizzare `duck` con {{site.data.keyword.cos_full}}, è necessario aggiungere un profilo personalizzato alla [directory di supporto dell'applicazione](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}. Informazioni dettagliate sui profili di connessione `duck`, inclusi i profili di esempio e preconfigurati, sono disponibili in [CLI help/how-to](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}.

Di seguito è riportato un profilo di esempio per un endpoint COS regionale:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

L'aggiunta di questo profilo a `duck` ti consente di accedere a {{site.data.keyword.cos_short}} utilizzando un comando simile al seguente:

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*Valori chiave*
* `<bucket-name>` - nome del bucket COS (*assicurati che le regioni del bucket e dell'endpoint siano coerenti*)
* `<access-key>` - chiave di accesso HMAC
* `<secret-access-key>` - chiave segreta HMAC

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

È disponibile un elenco completo delle opzioni della riga di comando immettendo `duck --help` nella shell o visitando il [sito wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}
