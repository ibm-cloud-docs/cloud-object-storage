---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# Per gli amministratori
{: #administrators}

Gli amministratori di sistema e di archiviazione che devono configurare l'archiviazione oggetti e gestire l'accesso ai dati, possono avvalersi di IBM Cloud Identity and Access Management (IAM) per gestire gli utenti, creare e ruotare le chiavi API e per concedere ruoli a utenti e servizi. Se ancora non lo hai fatto, procedi e leggi l'[esercitazione introduttiva](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) per acquisire familiarità con i concetti fondamentali di bucket, oggetti e utenti.

## Configura la tua archiviazione
{: #administrators-setup}

Innanzitutto, devi avere almeno un'istanza della risorsa dell'archiviazione oggetti e alcuni bucket in cui archiviare i dati. Pensa a questi bucket in termini di come vuoi segmentare ulteriormente l'accesso ai tuoi dati, di dove vuoi che i tuoi dati risiedano fisicamente e della frequenza con cui si accederà ai dati. 

### Segmentazione dell'accesso
{: #administrators-access}

Ci sono due livelli in cui puoi segmentare l'accesso: a livello dell'istanza della risorsa e a livello del bucket. 

Forse vuoi assicurarti che un team di sviluppo possa accedere solo alle istanze dell'archiviazione oggetti che stanno utilizzando e non a quelle utilizzate da altri team. Oppure vuoi assicurarti che solamente il software che il tuo team sta creando possa effettivamente modificare i dati archiviati, quindi vuoi che i tuoi sviluppatori con l'accesso alla piattaforma cloud possano solo leggere i dati a scopo di risoluzione dei problemi. Questi sono esempi di politiche a livello di servizio. 

Ora, se il team di sviluppo, o qualsiasi singolo utente, che dispone dell'accesso di visualizzatore a un'istanza di archiviazione deve poter modificare direttamente i dati in uno o più bucket, puoi utilizzare le politiche a livello di bucket per elevare il livello di accesso concesso agli utenti all'interno del tuo account. Ad esempio, un utente potrebbe non essere in grado di creare nuovi bucket, ma può creare ed eliminare gli oggetti all'interno di bucket esistenti. 

## Gestisci l'accesso
{: #administrators-manage-access}

IAM si basa su un concetto fondamentale: a un _soggetto_ viene concesso un _ruolo_ in una _risorsa_.

Ci sono due tipi di base di soggetti: un _utente_ e un _ID servizio_.

C'è anche un altro concetto, una _credenziale del servizio_. Una credenziale del servizio è una raccolta di informazioni importanti necessarie per connettersi a un'istanza di {{site.data.keyword.cos_full}}. Include almeno un identificativo per l'istanza di {{site.data.keyword.cos_full_notm}} (ad esempio, l'ID dell'istanza della risorsa), gli endpoint di servizio/autorizzazione e un modo per consentire l'associazione del soggetto a una chiave API (ad esempio, l'ID servizio). Quando crei la credenziale del servizio, puoi associarla a un ID servizio esistente oppure creare un nuovo ID servizio. 

Pertanto, se vuoi consentire al tuo team di sviluppo di poter utilizzare la console per visualizzare le istanze dell'archiviazione oggetti e i cluster Kubernetes, avrà bisogno del ruolo di visualizzatore (`Viewer`) nelle risorse dell'archiviazione oggetti e del ruolo di amministratore (`Administrator`) in Container Service. Tieni presente che il ruolo di visualizzatore (`Viewer`) consente all'utente solamente di constatare l'esistenza dell'istanza e di visualizzare le credenziali esistenti, **non** di visualizzare i bucket e gli oggetti. Quando le credenziali del servizio sono state create, sono state associate a un ID servizio. Questo ID servizio deve disporre del ruolo di gestore (`Manager`) o di scrittore (`Writer`) nell'istanza per poter creare ed eliminare i bucket e gli oggetti. 

Per ulteriori informazioni sui ruoli IAM e sulle autorizzazioni, vedi la [Panoramica di IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview).
