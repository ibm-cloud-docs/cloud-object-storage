---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-11"

keywords: data, object storage, unstructured, cleversafe

subcollection: cloud-object-storage

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Esercitazione introduttiva
{: #getting-started}

Questa esercitazione introduttiva illustra i passi necessari per creare i bucket, caricare gli oggetti e configurare le politiche di accesso per consentire ad altri utenti di lavorare con i tuoi dati.
{: shortdesc}

## Prima di iniziare
{: #gs-prereqs}

Ti serve:
  * Un [account della piattaforma {{site.data.keyword.cloud}}](https://cloud.ibm.com)
  * Un'[istanza di {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision)
  * Alcuni file sul tuo computer locale da caricare.
{: #gs-prereqs}

 Questa esercitazione assiste un nuovo utente nei primi passi con la console della piattaforma {{site.data.keyword.cloud_notm}}. Gli sviluppatori che vogliono iniziare con l'API, devono consultare la [Guida per sviluppatori](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev) o la [panoramica dell'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Crea alcuni bucket per archiviare i tuoi dati
{: #gs-create-buckets}

  1. [L'ordinazione di {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) crea un'_istanza del servizio_. {{site.data.keyword.cos_full_notm}} è un sistema a più tenant e tutte le istanze di {{site.data.keyword.cos_short}} condividono l'infrastruttura fisica. Verrai reindirizzato automaticamente all'istanza del servizio da cui puoi iniziare a creare i bucket. Le tue istanze {{site.data.keyword.cos_short}} sono elencate sotto **Archiviazione** nell'[elenco di risorse](https://cloud.ibm.com/resources).

I termini 'istanza della risorsa' e 'istanza del servizio' si riferiscono allo stesso concetto e possono essere utilizzati in modo intercambiabile.
{: tip}

  1. Fai clic su **Create bucket** e scegli un nome univoco. Tutti i bucket in tutte le regioni del mondo condividono un singolo spazio dei nomi. Assicurati di disporre delle [autorizzazioni corrette](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) per creare un bucket.

  **Nota**: quando crei i bucket o aggiungi gli oggetti, assicurati di evitare l'uso di informazioni d'identificazione personale. Le informazioni d'identificazione personale sono informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
  {: tip}

  1. Scegli prima il [livello di _resilienza_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) desiderato e quindi un'_ubicazione_ in cui vuoi archiviare fisicamente i tuoi dati. La resilienza si riferisce all'ambito e alla scala dell'area geografica attraverso la quale vengono distribuiti i tuoi dati. La resilienza _interregionale_ diffonde i tuoi dati a diverse aree metropolitane, mentre la resilienza _regionale_ diffonde i dati a una singola area metropolitana. Un _singolo data center_ distribuisce i dati tra dispositivi solo all'interno di un singolo sito.
  2. Scegli la [_classe di archiviazione_](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes) del bucket, che riflette la frequenza con cui prevedi di leggere i dati archiviati e determina i dettagli di fatturazione. Segui il link **Crea** per creare e accedere al tuo nuovo bucket.

I bucket rappresentano un modo per organizzare i tuoi dati, ma non sono l'unico modo. I nomi oggetto (spesso indicati come _chiavi oggetto_) possono utilizzare una o più barre per un sistema organizzativo simile a una directory. Utilizzi quindi la parte del nome oggetto prima di un delimitatore per formare un _prefisso oggetto_, che viene utilizzato per elencare gli oggetti correlati in un singolo bucket attraverso l'API.
{: tip}


## Aggiungi alcuni oggetti ai tuoi bucket
{: #gs-add-objects}

Ora prosegui e vai a uno dei tuoi bucket selezionandolo dall'elenco. Fai clic su **Add Objects**. I nuovi oggetti sovrascrivono gli oggetti esistenti con gli stessi nomi all'interno dello stesso bucket. Quando utilizzi la console per caricare gli oggetti, il nome oggetto corrisponde sempre al nome file. Non è necessario che vi sia alcuna relazione tra il nome file e la chiave oggetto se utilizzi l'API per scrivere i dati. Vai avanti e aggiungi qualche file a questo bucket.

Gli oggetti sono limitati a 200 MB quando caricati tramite la console a meno che non utilizzi il plugin di [trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-upload). Gli oggetti più grandi (fino a 10 TB) possono anche essere [suddivisi in parti e caricati in parallelo utilizzando l'API](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects). Le chiavi oggetto possono avere una lunghezza massima di 1024 caratteri ed è meglio evitare qualsiasi carattere che possa essere problematico in un indirizzo web. Ad esempio, `?`, `=`, `<` e altri caratteri speciali potrebbero causare comportamenti indesiderati se non codificati in URL.
{:tip}

## Invita un utente al tuo account per amministrare i tuoi bucket e dati
{: #gs-invite-user}

Ora introdurrai un altro utente e gli consentirai di agire come amministratore per l'istanza e tutti i dati memorizzati in essa.

  1. Innanzitutto, per aggiungere il nuovo utente devi lasciare l'interfaccia di {{site.data.keyword.cos_short}} corrente e passare alla console IAM. Vai al menu **Gestisci** e segui il link in **Accesso (IAM)** > **Utenti**. Fai clic su **Invita utenti**.
	<img alt="Invito di utenti a IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_invitebtn.png" max-height="200px" />
	`Figura 1: invito di utenti a IAM`
  2. Immetti l'indirizzo email di un utente che vuoi invitare alla tua organizzazione, quindi espandi la sezione **Servizi** e seleziona "Risorsa" dal menu **Assegna accesso a**. Ora scegli "Cloud Object Storage" dal menu **Servizi**.
	<img alt="Servizi IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_services.png" max-height="200px" />
	`Figura 2: servizi IAM`
  3. A questo punto, vengono visualizzati altri tre campi: _Istanza del servizio_, _Tipo di risorsa_ e _ID risorsa_. Il primo campo definisce a quale istanza di {{site.data.keyword.cos_short}} può accedere l'utente. Può essere impostato anche per concedere lo stesso livello di accesso a tutte le istanze di {{site.data.keyword.cos_short}}. Per ora, possiamo lasciare vuoti gli altri campi.
	<img alt="Invito di utenti a IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_servicesdropdowns.png" max-height="200px" />
	`Figura 3: invito di utenti a IAM`
  4. La casella di spunta **Seleziona i ruoli** determina la serie di azioni disponibili per l'utente. Seleziona il ruolo di accesso alla piattaforma "Amministratore" per consentire all'utente di concedere ad altri [utenti e ID servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) l'accesso all'istanza. Seleziona il ruolo di accesso al servizio "Gestore" per consentire all'utente di gestire l'istanza {{site.data.keyword.cos_short}} nonché di creare ed eliminare bucket e oggetti. Queste combinazioni di _Soggetto_ (utente), _Ruolo_ (gestore) e _Risorsa_ (istanza del servizio {{site.data.keyword.cos_short}}) formano insieme le [politiche IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam). Per una guida più dettagliata su ruoli e politiche, [consulta la documentazione IAM](/docs/iam?topic=iam-userroles).
	<img alt="Ruoli IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_roles.png" max-height="400px" />
	`Figura 4: selezione di ruoli IAM`
  5. {{site.data.keyword.cloud_notm}} utilizza Cloud Foundry come piattaforma di gestione degli account sottostante, quindi è necessario garantire un livello minimo di accesso a Cloud Foundry affinché l'utente possa accedere in primo luogo alla tua organizzazione.  Seleziona un'organizzazione dal menu **Organizzazione**, quindi seleziona "Revisore" dai menu **Ruoli organizzazione** e **Ruoli spazio**.  L'impostazione delle autorizzazioni Cloud Foundry consente all'utente di visualizzare i servizi disponibili per la tua organizzazione, ma non di modificarli.

## Fornisci agli sviluppatori l'accesso a un bucket.
{: #gs-bucket-policy}

  1. Passa al menu **Gestisci** e segui il link in **Accesso (IAM)** > **ID servizio**.  Qui puoi creare un _ID servizio_, che funge da identità astratta associata all'account. Gli ID servizio possono essere assegnati alle chiavi API e vengono utilizzati in situazioni in cui non vuoi correlare l'identità di un determinato sviluppatore a un processo o componente di un'applicazione.
	<img alt="ID servizio IAM" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/console_iam_serviceid.png" max-height="200px" />
	`Figura 5: ID servizio IAM`
  2. Ripeti il passo precedente ma questa volta, nel passo 3, scegli una particolare istanza del servizio e immetti "bucket" come _Tipo di risorsa_ e il CRN completo di un bucket esistente come _ID risorsa_.
  3. Ora l'ID servizio può accedere a quel particolare bucket e a nessun altro.

## Passi successivi
{: #gs-next-steps}

Ora che hai acquisito familiarità con la tua archiviazione oggetti tramite la console basata su web, potresti essere interessato a eseguire un flusso di lavoro simile dalla riga di comando utilizzando il programma di utilità riga comandi `ibmcloud cos` per creare l'istanza del servizio e interagire con IAM e `curl` per accedere direttamente a COS. [Consulta la panoramica dell'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) per iniziare.
