---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# Panoramica di IAM
{: #iam-overview}

{{site.data.keyword.cloud}} Identity & Access Management ti consente di autenticare gli utenti in modo sicuro e di controllare l'accesso a tutte le risorse cloud in modo congruente in {{site.data.keyword.cloud_notm}} Platform. Per ulteriori informazioni, vedi l'[Esercitazione introduttiva](/docs/iam?topic=iam-getstarted#getstarted).

## Gestione delle identità
{: #iam-overview-identity}

La gestione delle identità include l'interazione di utenti, servizi e risorse. Tutti gli utenti sono identificati dal loro ID IBM. I servizi sono identificati dai loro ID servizio. Le risorse, inoltre, sono identificate e indirizzate utilizzando i CRN.

Il servizio token {{site.data.keyword.cloud_notm}} IAM ti consente di creare, aggiornare, eliminare e utilizzare chiavi API per utenti e servizi. Queste chiavi API possono essere create con chiamate API o la sezione Identity & Access della console {{site.data.keyword.cloud}} Platform. La stessa chiave può essere utilizzata su più servizi. Ogni utente può disporre di più chiavi API per supportare scenari di rotazione delle chiavi, così come scenari che utilizzano chiavi diverse per scopi diversi per limitare l'esposizione di una singola chiave.

Per ulteriori informazioni, vedi [Cos'è Cloud IAM?](/docs/iam?topic=iam-iamoverview#iamoverview).

### Utenti e chiavi API
{: #iam-overview-user-api-keys}

Le chiavi API possono essere create e utilizzate dagli utenti {{site.data.keyword.cloud_notm}} per scopi di automazione e creazione di script come pure per l'accesso federato quando si utilizza la CLI. Le chiavi API possono essere create nell'IU Identity and Access Management o utilizzando la CLI di `ibmcloud` .

### ID servizio e chiavi API
{: #iam-overview-service-id-api-key}

Il servizio token IAM consente di creare ID servizio e chiavi API per gli ID servizio. Un ID servizio è simile a un "id funzionale" o un "id applicazione" e viene utilizzato per autenticare i servizi e non per rappresentare un utente.

Gli utenti possono creare degli ID servizio e associarli mediante bind ad ambiti quali un account {{site.data.keyword.cloud_notm}} Platform, un'organizzazione CloudFoundry o uno spazio CloudFoundry anche se, per adottare IAM, è meglio associare mediante bind gli ID servizio a un account {{site.data.keyword.cloud_notm}} Platform. Questo bind viene eseguito per dare all'ID servizio un contenitore in cui essere presente. Questo contenitore definisce anche chi può aggiornare ed eliminare l'ID servizio e chi può creare, aggiornare, leggere ed eliminare le chiavi API associate a tale ID servizio. È importante notare che un ID servizio NON è correlato ad un utente.

### Rotazione delle chiavi
{: #iam-overview-key-rotation}

Le chiavi API devono essere regolarmente ruotate per evitare eventuali violazioni della sicurezza causate da chiavi fuoriuscite.

## Gestione dell'accesso
{: #iam-overview-access-management}

Il controllo dell'accesso IAM fornisce un modo comune per assegnare i ruoli utente per le risorse {{site.data.keyword.cloud_notm}} e controlla le azioni che gli utenti possono eseguire su tali risorse. Puoi visualizzare e gestire gli utenti nell'ambito dell'account o dell'organizzazione, a seconda delle opzioni di accesso che ti sono state concesse. Ad esempio, ai proprietari di account viene automaticamente assegnato il ruolo di amministratore dell'account per Identity and Access Managemement, che consente loro di assegnare e gestire le politiche di servizio per tutti i membri del loro account.

### Utenti, ruoli, risorse e politiche
{: #iam-overview-access-policies}

Il controllo dell'accesso IAM abilita l'assegnazione di politiche per ogni servizio o istanza del servizio per consentire livelli di accesso per la gestione di risorse e utenti all'interno del contesto assegnato. Una politica concede a un utente uno o più ruoli a un insieme di risorse utilizzando una combinazione di attributi per definire l'insieme di risorse applicabile. Quando assegni una politica a un utente, specifichi prima il servizio e quindi uno o più ruoli da assegnare. Potrebbero essere disponibili delle opzioni di configurazione aggiuntive, a seconda del servizio da te selezionato.

Mentre i ruoli sono una raccolta di azioni, le azioni associate a questi ruoli sono specifiche per il servizio. Ogni servizio determina questa associazione tra ruolo e azione durante il processo di onboarding e questa associazione è applicabile a tutti gli utenti del servizio. I ruoli e le politiche di accesso sono configurati mediante il PAP (Policy Administration Point) e implementati tramite il PEP (Policy Enforcement Point) e il PDP (Policy Decision Point).

Per ulteriori informazioni, vedi [Procedure ottimali per organizzare gli utenti, i team e le applicazioni](/docs/tutorials?topic=solution-tutorials-users-teams-applications#best-practices-for-organizing-users-teams-applications).
