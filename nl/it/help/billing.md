---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# Fatturazione
{: #billing}

Le informazioni sui prezzi sono disponibili su [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}.

## Fatture
{: #billing-invoices}

Trova le fatture del tuo account su **Gestisci** > **Fatturazione e utilizzo** nel menu di navigazione.

Ogni account riceve una singola fattura. Se hai bisogno di una fatturazione separata per diversi insiemi di contenitori, è necessario creare più account.

## Prezzi di {{site.data.keyword.cos_full_notm}}
{: #billing-pricing}

I costi di archiviazione per {{site.data.keyword.cos_full}} sono determinati dal volume totale di dati archiviati, dalla quantità di larghezza di banda in uscita pubblica utilizzata e dal numero totale di richieste operative elaborate dal sistema.

Le offerte di infrastruttura sono connesse a una rete a tre livelli, segmentando il traffico pubblico, privato e di gestione. I servizi di infrastruttura possono trasferire i dati tra loro attraverso la rete privata senza alcun costo. Le offerte di infrastruttura (come i server bare metal, i server virtuali e l'archiviazione cloud) si connettono ad altre applicazioni e servizi nel catalogo della piattaforma {{site.data.keyword.cloud_notm}} (come i servizi Watson e i runtime Cloud Foundry) attraverso la rete pubblica, pertanto il trasferimento dei dati tra questi due tipi di offerte viene misurato e addebitato alle tariffe standard di larghezza di banda della rete pubblica.
{: tip}

## Classi di richiesta
{: #billing-request-classes}

Le richieste di 'Classe A' implicano la modifica o l'elenco. Questa categoria include la creazione di bucket, il caricamento o la copia di oggetti, la creazione o la modifica di configurazioni, l'elenco dei bucket e l'elenco dei contenuti dei bucket.

Le richieste di 'Classe B' sono correlate al recupero di oggetti o dei relativi metadati o configurazioni dal sistema.

L'eliminazione dei bucket o degli oggetti dal sistema non comporta alcun addebito.

| Classe | Richieste | Esempi |
|--- |--- |--- |
| Classe A | Richieste PUT, COPY e POST, nonché richieste GET utilizzate per elencare bucket e oggetti | Creazione di bucket, caricamento o copia di oggetti, elenco dei bucket, elenco dei contenuti dei bucket, impostazione degli ACL e impostazione delle configurazioni CORS |
| Classe B | Richieste GET (escluso l'elenco), HEAD e OPTIONS | Recupero di oggetti e metadati |

## Trasferimenti Aspera
{: #billing-aspera}

Il [trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) comporta costi in uscita aggiuntivi. Per ulteriori informazioni, vedi la [pagina dei prezzi](https://www.ibm.com/cloud/object-storage#s3api).

## Classi di archiviazione
{: #billing-storage-classes}

Non è necessario accedere di frequente a tutti i dati archiviati ed è possibile che ad alcuni dati di archivio si acceda raramente, se non mai. Per i carichi di lavoro meno attivi, i bucket possono essere creati in una diversa classe di archiviazione e gli oggetti archiviati in questi bucket vengono addebitati in base a una pianificazione diversa rispetto all'archiviazione standard.

Esistono quattro classi:

*  **Standard** viene utilizzata per i carichi di lavoro attivi, senza alcun addebito per i dati recuperati (diverso dal costo della richiesta operativa stessa).
*  **Vault** viene utilizzata per i carichi di lavoro cool in cui si accede ai dati meno di una volta al mese; viene applicato un addebito ($/GB) aggiuntivo per il recupero ogni volta che vengono letti i dati. Il servizio include una soglia minima per la dimensione oggetto e il periodo di archiviazione coerenti con l'utilizzo previsto di questo servizio per i dati meno attivi.
*  **Cold Vault** viene utilizzata per i carichi di lavoro cold in cui si accede ai dati ogni 90 giorni o meno; viene applicato un addebito ($/GB) aggiuntivo maggiore per il recupero ogni volta che vengono letti i dati. Il servizio include una soglia minima maggiore per la dimensione oggetto e il periodo di archiviazione coerenti con l'utilizzo previsto di questo servizio per i dati statici e inattivi.
*  **Flex** viene utilizzata per i carichi di lavoro dinamici in cui i modelli di accesso sono più difficili da prevedere. A seconda dell'utilizzo, se i costi e gli addebiti per il recupero superano un valore limite, gli addebiti per il recupero vengono eliminati e viene applicato invece un nuovo addebito per la capacità. Se non si accede di frequente ai dati, è più conveniente rispetto all'archiviazione Standard e se i modelli di utilizzo dell'accesso diventano improvvisamente più attivi, è più conveniente dell'archiviazione Vault o Cold Vault. Flex non richiede una soglia minima per la dimensione oggetto o il periodo di conservazione.

Per ulteriori informazioni sui prezzi, vedi [la tabella dei prezzi sul sito ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Per ulteriori informazioni sulla creazione di bucket con diverse classi di archiviazione, vedi la [Guida di riferimento API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).
