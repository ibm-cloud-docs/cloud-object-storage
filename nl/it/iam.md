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

# Introduzione a IAM
{: #iam}

## Ruoli e azioni di Identity and Access Management
{: #iam-roles}

L'accesso alle istanze del servizio {{site.data.keyword.cos_full}} per gli utenti nel tuo account è controllato da{{site.data.keyword.Bluemix_notm}} IAM (Identity and Access Management). Ad ogni utente che accede al servizio {{site.data.keyword.cos_full}} nel tuo account deve essere assegnato una politica di accesso con un ruolo utente IAM definito. Tale politica determina quali azioni l'utente può eseguire nel contesto del servizio o dell'istanza che selezioni. Le azioni consentite sono personalizzate e definite dal servizio {{site.data.keyword.Bluemix_notm}} come operazioni che è consentito eseguire sul servizio. Le azioni vengono quindi associate ai ruoli utente IAM.

Le politiche abilitano l'accesso da concedere a diversi livelli. Alcune delle opzioni includono quanto segue: 

* Accesso a tutte le istanze del servizio nel tuo account
* Accesso a una singola istanza del servizio nel tuo account
* Accesso a una specifica risorsa all'interno di un'istanza
* Accesso a tutti i servizi abilitati IAM nel tuo account

Dopo aver definito l'ambito della politica di accesso, assegna un ruolo. Esamina le seguenti tabelle che descrivono quali azioni sono consentite da ciascun ruolo all'interno del servizio {{site.data.keyword.cos_short}}.

La seguente tabella illustra le azioni che sono associate ai ruoli di gestione della piattaforma. I ruoli di gestione della piattaforma consentono agli utenti di eseguire le attività sulle risorse del servizio a livello di piattaforma, ad esempio assegnare l'accesso utente per il servizio, creare o eliminare l'ID servizio, creare le istanze e associare le istanze alle applicazioni.

| Ruolo di gestione della piattaforma | Descrizione delle azioni | Azioni di esempio|
|:-----------------|:-----------------|:-----------------|
|Viewer (Visualizzatore)| Visualizza le istanze del servizio ma non modificarle. | <ul><li>Elenca le istanze del servizio COS disponibili</li><li>Visualizza i dettagli del piano di servizio COS</li><li>Visualizza dettagli di utilizzo</li></ul>|
| Editor | Esegui tutte le azioni della piattaforma tranne la gestione degli account e l'assegnazione delle politiche di accesso |<ul><li>Crea ed elimina le istanze del servizio COS</li></ul> |
|Operator (Operatore)| Non utilizzato da COS | Nessuna |
|Administrator (Amministratore)| Esegui tutte le azioni di piattaforma in base alla risorsa assegnata a questo ruolo, inclusa l'assegnazione di politiche di accesso ad altri utenti |<ul><li>Aggiorna le politiche utente</li>Aggiorna i piani di prezzo</ul>|
{: caption="Tabella 1. Azioni e ruoli utente IAM"}


La seguente tabella illustra le azioni che sono associate ai ruoli di accesso al servizio. I ruoli di accesso al servizio consentono agli utenti di accedere a {{site.data.keyword.cos_short}} così come di richiamare l'API {{site.data.keyword.cos_short}}.

| Ruolo di accesso al servizio | Descrizione delle azioni                                                                                                                                       | Azioni di esempio                                                                     |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Content Reader (Lettore di contenuto)     | Scarica gli oggetti ma non elencare gli oggetti o i bucket. | <ul><li>Scarica oggetti</li></ul> |
| Reader (Lettore)    | Oltre alle azioni consentite dal ruolo di Content Reader, il ruolo di Reader consente di elencare i bucket e/o gli oggetti ma non di modificarli. | <ul><li>Elenca i bucket</li><li> Elenca e scarica oggetti                                   </li></ul>                    |
| Writer (Scrittore)      | Oltre alle azioni consentite dal ruolo Reader, il ruolo Writer consente di creare bucket e caricare oggetti. | <ul><li>Crea nuovi bucket e oggetti</li><li>Rimuovi bucket e oggetti</li></ul> |
| Manager (Gestore)     |Oltre alle azioni consentite dal ruolo Writer, il ruolo Manager consente di completare azioni privilegiate che influenzano il controllo dell'accesso. | <ul><li>Aggiungi una politica di conservazione</li><li>Aggiungi un firewall del bucket</li></ul>              |
{: caption="Tabella 3. Azioni e ruoli di accesso al servizio IAM"}


Per informazioni sull'assegnazione dei ruoli utente nell'IU, consulta [Gestione dell'accesso IAM](/docs/iam?topic=iam-iammanidaccser).
 
