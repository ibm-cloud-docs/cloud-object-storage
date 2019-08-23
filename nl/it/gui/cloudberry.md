---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup è un programma di utilità flessibile che consente agli utenti di eseguire il backup di alcuni o tutti i file system locali su un sistema di archiviazione oggetti compatibile con l'API S3. Sono disponibili versioni Free e Professional per Windows, MacOS e Linux che supportano numerosi servizi di archiviazione cloud popolari tra cui {{site.data.keyword.cos_full}}. Cloudberry Backup è scaricabile da [cloudberrylab.com](https://www.cloudberrylab.com/).

Cloudberry Backup include molte funzioni utili tra cui:

* Pianificazione
* Backup incrementali e a livello di blocco
* Interfaccia riga di comando
* Notifiche email
* Compressione (*solo versione Pro*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Un nuovo prodotto di Cloudberry Labs offre un'interfaccia utente di gestione file familiare per {{site.data.keyword.cos_short}}. [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} viene fornito anche nelle versioni Free e Pro, ma al momento è disponibile solo per Windows. Le funzioni principali includono:

* Sincronizzazione di cartelle/bucket
* Interfaccia riga di comando
* Gestione ACL
* Report sulla capacità

La versione Pro include anche:
* Ricerca 
* Crittografia/Compressione
* Caricamento ripristinabile
* Supporto FTP/SFTP

## Utilizzo di Cloudberry con Object Storage
{: #cloudberry-cos}

Punti chiave da ricordare quando si configurano i prodotti Cloudberry per funzionare con {{site.data.keyword.cos_short}}:

* Seleziona `S3 Compatible` dall'elenco di opzioni
* Al momento sono supportate solo le [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)
* È richiesta una connessione separata per ogni bucket
* Assicurati che l'`Endpoint` specificato nella connessione corrisponda alla regione del bucket selezionato (*il backup non riuscirà a causa di una destinazione inaccessibile*). Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
