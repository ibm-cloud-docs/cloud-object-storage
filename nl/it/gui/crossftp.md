---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# Trasferisci i file utilizzando CrossFTP
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} è un client FTP completo che supporta soluzioni di archiviazione cloud compatibili con S3 tra cui {{site.data.keyword.cos_full}}. CrossFTP supporta Mac OS X, Microsoft Windows, Linux ed è disponibile nelle versioni Free, Pro ed Enterprise con funzioni come:

* Interfaccia a schede
* Crittografia password
* Ricerca
* Trasferimento in batch
* Crittografia (*versioni Pro/Enterprise*)
* Sincronizzazione (*versioni Pro/Enterprise*)
* Utilità di pianificazione (*versioni Pro/Enterprise*)
* Interfaccia riga di comando (*versioni Pro/Enterprise*)

## Connessione a IBM Cloud Object Storage
{: #crossftp-connect}

1. Scarica, installa e avvia CrossFTP.
2. Nel riquadro di destra, crea un nuovo sito facendo clic sull'icona più (+) per aprire Site Manager.
3. Nella scheda *General* immetti quanto segue:
    * Imposta **Protocol** su `S3/HTTPS`
    * Imposta **Label** su un nome descrittivo di tua scelta
    * Imposta **Host** su un endpoint {{site.data.keyword.cos_short}} (ad esempio, `s3.us.cloud-object-storage.appdomain.cloud`)
        * *Assicurati che la regione dell'endpoint corrisponda al bucket di destinazione previsto. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * Lascia il valore **Port** su `443`
    * Imposta **Access Key** e **Secret** sulle credenziali HMAC con i diritti di accesso adeguati al tuo bucket di destinazione
4. Nella scheda *S3*
    * Assicurati che l'opzione `Use DevPay` sia deselezionata
    * Fai clic su **API Set ...** e assicurati che le opzioni `Dev Pay` e `CloudFront Distribution` siano deselezionate
5. ***Solo per Mac OS X***
    * Fai clic su *Security > TLS/SSL Protocols...* nella barra dei menu
    * Seleziona l'opzione `Customize the enabled protocols`
    * Aggiungi `TLSv1.2` alla casella **Enabled**
    * Fai clic su **OK**
6. ***Solo per Linux***
    * Fai clic su *Security > Cipher Settings...* nella barra dei menu
    * Seleziona l'opzione `Customize the enabled cipher suites`
    * Aggiungi `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` alla casella **Enabled**
    * Fai clic su **OK**
7. Fai clic su **Apply** e quindi su **Close**
8. Dovrebbe essere disponibile una nuova voce sotto *Sites* con l'etichetta (*Label*) fornita nel passo 3
9. Fai doppio clic sulla nuova voce per connetterti all'endpoint

Da qui, la finestra visualizza un elenco dei bucket disponibili e puoi esaminare i file disponibili e trasferirli da e verso i tuoi dischi locali.
