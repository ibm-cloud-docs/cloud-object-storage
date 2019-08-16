---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# Autorizzazioni bucket
{: #iam-bucket-permissions}

Assegnazione dei ruoli di accesso per gli utenti e gli ID servizio rispetto ai bucket, utilizzando l'IU o la CLI per creare le politiche.

| Ruolo di accesso | Azioni di esempio                                             |
|:------------|-------------------------------------------------------------|
| Manager (Gestore)     | Rendi gli oggetti pubblici, crea ed elimina bucket e oggetti |
| Writer (Scrittore)      | Crea ed elimina bucket e oggetti                      |
| Reader (Lettore)              | Elenca e scarica oggetti                                   |
| Content Reader (Lettore di contenuto)      | Scarica oggetti                                   |

## Concessione dell'accesso a un utente
{: #iam-user-access}

Se l'utente deve poter utilizzare la console, è necessario **anche** concedergli un ruolo di accesso alla piattaforma minimo di `Viewer` (Visualizzatore) sull'istanza stessa in aggiunta al ruolo di accesso al servizio (come ad esempio `Reader` (Lettore)). Ciò gli consentirà di visualizzare tutti i bucket ed elencare gli oggetti al loro interno. Seleziona quindi **Autorizzazioni bucket** dal menu di navigazione a sinistra, seleziona l'utente e seleziona il livello di accesso (`Manager` o `Writer`) di cui necessita.

Se l'utente interagirà con i dati utilizzando l'API e non richiede l'accesso alla console _ed_ è un membro del tuo account, puoi concedere l'accesso ad un singolo bucket senza alcun accesso all'istanza principale.

## Implementazione delle politiche
{: #iam-policy-enforcement}

Le politiche IAM sono implementate in modo gerarchico dal livello più alto di accesso a quello più limitato. I conflitti vengono risolti optando per la politica più permissiva. Ad esempio, se un utente ha sia il ruolo di accesso al servizio `Writer` che quello `Reader` su un bucket, la politica che concede il ruolo `Reader` verrà ignorata.

Ciò è applicabile anche alle politiche a livello di istanza del servizio e bucket.

- Se un utente ha una politica che concede il ruolo `Writer` su un'istanza del servizio e il ruolo `Reader` su un singolo bucket, la politica a livello di bucket verrà ignorata.
- Se un utente ha una politica che concede il ruolo `Reader` su un'istanza del servizio e il ruolo `Writer` su un singolo bucket, entrambe le politiche verranno implementate e il più permissivo ruolo `Writer` avrà la precedenza per il singolo bucket.

Se è necessario limitare l'accesso ad un singolo bucket (o insieme di bucket), assicurati che l'ID utente o l'ID servizio non abbiano politiche a livello di istanza utilizzando la console o la CLI.

### Utilizzo dell'IU
{: #iam-policy-enforcement-console}

Per creare una nuova politica a livello di bucket: 

  1. Vai alla console **Accesso (IAM)** dal menu **Gestisci**.
  2. Seleziona **Utenti** dal menu di navigazione di sinistra.
  3. Seleziona un utente.
  4. Seleziona la scheda **Politiche di accesso** per visualizzare le politiche esistenti dell'utente, assegna una nuova politica oppure modifica una politica esistente.
  5. Fai clic su **Assegna accesso** per creare una nuova politica.
  6. Scegli **Assegna l'accesso alle risorse**.
  7. Seleziona prima **Cloud Object Storage** dal menu dei servizi.
  8. Seleziona quindi l'istanza del servizio appropriata. Immetti `bucket` nel campo **Tipo di risorsa** e il nome bucket nel campo **ID risorsa**.
  9. Seleziona il ruolo di accesso al servizio desiderato.
  10.  Fai clic su **Assegna**

Nota: lasciare vuoti i campi **Tipo di risorsa** o **Risorsa** creerà una politica a livello di istanza.
{:tip}

### Utilizzo della CLI
{: #iam-policy-enforcement-cli}

Da un terminale, esegui questo comando:

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Per elencare le politiche esistenti:

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

Per modificare una politica esistente:

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## Concessione dell'accesso ad un ID servizio
{: #iam-service-id}
Se devi concedere l'accesso a un bucket per un'applicazione o un'altra entità non umana, utilizza un ID servizio. L'ID servizio può essere creato specificamente per questo scopo oppure può essere un ID servizio esistente già in uso.

### Utilizzo dell'IU
{: #iam-service-id-console}

  1. Vai alla console **Accesso (IAM)** dal menu **Gestisci**.
  2. Seleziona **ID servizio** dal menu di navigazione di sinistra.
  3. Seleziona un ID servizio per visualizzare le politiche esistenti e assegna una nuova politica o modifica una politica esistente.
  3. Seleziona l'istanza del servizio, l'ID servizio e il ruolo desiderato.
  4. Immetti `bucket` nel campo **Tipo di risorsa** e il nome bucket nel campo **Risorsa**.
  5. Fai clic su **Invia**

  Nota: lasciare vuoti i campi **Tipo di risorsa** o **Risorsa** creerà una politica a livello di istanza.
{:tip}

### Utilizzo della CLI
{: #iam-service-id-cli}

Da un terminale, esegui questo comando:

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Per elencare le politiche esistenti:

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

Per modificare una politica esistente:

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
