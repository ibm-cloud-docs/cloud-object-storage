---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-19"

keywords: sdks, overview

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
{:go: .ph data-hd-programlang='go'}

# Informazioni sugli SDK IBM COS
{: #sdk-about}

IBM COS fornisce SDK per Java, Python, NodeJS e Go. Questi SDK si basano sugli SDK API AWS S3 ufficiali, ma sono stati modificati per utilizzare le funzioni IBM Cloud come IAM, Key Protect, Immutable Object Storage e altre.

| Funzione                     | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| Supporto chiave API IAM         | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |
| Caricamenti in più parti gestiti | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |
| Scaricamenti in più parti gestiti | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Elenco di bucket esteso     |                                                   |                                                   |                                                   |                                                   |                                                   |
| Elenco di oggetti versione 2    | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Regole di archiviazione               | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Politiche di conservazione| ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Trasferimento ad alta velocità Aspera| ![Icona di casella di spunta](../../icons/checkmark-icon.svg) | ![Icona di casella di spunta](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## Supporto chiave API IAM
{: #sdk-about-iam}
Consente di creare client con una chiave API invece di una coppia di chiavi di accesso/segreto.  La gestione del token è gestita automaticamente e i token vengono aggiornati automaticamente durante le operazioni a lunga esecuzione.
## Caricamenti in più parti gestiti 
Utilizzando la classe `TransferManager`, l'SDK gestirà tutta la logica necessaria al caricamento degli oggetti in più parti.
## Scaricamenti in più parti gestiti 
Utilizzando la classe `TransferManager`, l'SDK gestirà tutta la logica necessaria allo scaricamento degli oggetti in più parti.
## Elenco di bucket esteso
Questa è un'estensione dell'API S3 che restituisce un elenco di bucket con codici di provisioning (una combinazione della classe di ubicazione ed archiviazione del bucket, restituita come `LocationConstraint`) per i bucket quando elencati.  Questo è utile nella ricerca di un bucket poiché i bucket in un'istanza del servizio sono tutti elencati indipendentemente dall'endpoint utilizzato.
## Elenco di oggetti versione 2
L'elenco di versione 2 consente un ambito più potente di elenchi di oggetti.
## Key Protect
Key Protect è un servizio IBM Cloud che gestisce le chiavi di crittografia ed è un parametro facoltativo durante la creazione del bucket.
## SSE-C                      
## Regole di archiviazione              
## Politiche di conservazione         
## Trasferimento ad alta velocità Aspera 
