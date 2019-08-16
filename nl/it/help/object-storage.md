---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, basics

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

# Informazioni su Object Storage
{: #about-cos}

L'archiviazione oggetti è un moderno concetto di tecnologia di archiviazione e una progressione logica dall'archiviazione di blocchi e file. L'archiviazione oggetti esiste dalla fine degli anni '90, ma ha ottenuto l'accettazione e il successo nel mercato negli ultimi 10 anni.

L'archiviazione oggetti è stata inventata per superare una serie di problemi:

*  La gestione dei dati su larghissima scala utilizzando i sistemi di blocco e file convenzionali risultava difficile perché queste tecnologie producono isole di dati a causa delle limitazioni sui vari livelli dello stack di hardware e software di gestione dei dati.

*  La gestione dello spazio dei nomi su larga scala comportava il mantenimento di gerarchie ampie e complesse, necessarie per accedere ai dati. Le limitazioni nelle strutture nidificate sugli array di archiviazione blocchi e file tradizionali hanno ulteriormente contribuito alla formazione di isole di dati.

*  La garanzia della sicurezza dell'accesso richiedeva una combinazione di tecnologie, schemi di sicurezza complessi e un significativo coinvolgimento umano nella gestione di queste aree.

L'archiviazione oggetti, nota anche come archiviazione basata sugli oggetti (OBS), utilizza un approccio diverso per l'archiviazione e il riferimento ai dati. I concetti di archiviazione dei dati oggetto includono i seguenti tre costrutti:

*  Dati: sono i dati dell'utente e dell'applicazione che richiedono l'archiviazione persistente. Possono essere di testo, formati binari, multimediali o qualsiasi altro contenuto generato dall'uomo o dalla macchina.

*  Metadati: sono i dati relativi ai dati. Includono alcuni attributi predefiniti come la dimensione e il tempo di caricamento. L'archiviazione oggetti consente agli utenti di includere metadati personalizzati contenenti qualsiasi informazione nelle coppie di chiave e valore. Queste informazioni in genere contengono informazioni pertinenti all'utente o all'applicazione che archivia i dati e che possono essere modificate in qualsiasi momento. Un aspetto unico della gestione dei metadati nei sistemi di archiviazione oggetti è che i metadati vengono archiviati con l'oggetto.

*  Chiave: un identificativo di risorsa univoco viene assegnato a ogni oggetto in un sistema OBS. Questa chiave consente al sistema di archiviazione oggetti di differenziare gli oggetti gli uni dagli altri e viene utilizzata per trovare i dati senza la necessità di conoscere l'unità fisica, l'array o il sito esatto in cui si trovano i dati.

Questo approccio consente all'archiviazione oggetti di archiviare i dati in una gerarchia semplice e uniforme, che riduce la necessità di
grandi repository di metadati che inibiscono le prestazioni.

L'accesso ai dati si ottiene utilizzando un'interfaccia REST sul protocollo HTTP, che consente l'accesso ovunque e in qualsiasi momento semplicemente facendo riferimento alla chiave oggetto.
