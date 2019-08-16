---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: endpoints, legacy, access points, manual failover

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

# Ulteriori informazioni sull'endpoint
{: #advanced-endpoints}

La resilienza di un bucket è definita dall'endpoint utilizzato per crearlo. La resilienza _interregionale_ diffonderà i tuoi dati a diverse aree metropolitane, mentre la resilienza _regionale_ diffonderà i dati a una singola area metropolitana. La resilienza _a singolo data center_ diffonde i dati a più appliance all'interno di un singolo data center. I bucket regionali e interregionali possono mantenere la disponibilità durante l'interruzione di un sito.

I carichi di lavoro di calcolo co-ubicati con un endpoint {{site.data.keyword.cos_short}} regionale vedranno una latenza inferiore e migliori prestazioni. Per i carichi di lavoro non concentrati in un'unica area geografica, un endpoint `geo` interregionale instrada le connessioni ai data center regionali più vicini.

Quando utilizzi un endpoint interregionale, è possibile indirizzare il traffico in entrata a uno specifico punto di accesso continuando al tempo stesso a distribuire i dati a tutte e tre le regioni. Quando si inviano richieste a un singolo punto di accesso, non c'è alcun failover automatizzato se tale regione diventa non disponibile. Le applicazioni che indirizzano il traffico a un punto di accesso invece che all'endpoint `geo` **devono** implementare una logica di failover appropriata internamente per ottenere i vantaggi in termini di disponibilità dell'archiviazione interregionale.
{:tip}

Alcuni carichi di lavoro possono trarre vantaggio dall'utilizzo di un endpoint a singolo data center. I dati archiviati in un unico sito continuano a essere distribuiti a molte appliance di archiviazione fisica ma sono contenuti in un singolo data center. Ciò può migliorare le prestazioni per le risorse di calcolo all'interno dello stesso sito ma non manterrà la disponibilità in caso di interruzione di un sito. I bucket a data center singolo non forniscono una replica o un backup automatizzati in caso di distruzione del sito; pertanto, la progettazione delle applicazioni che utilizzano un singolo sito deve prendere in considerazione un ripristino di emergenza.

Tutte le richieste devono utilizzare SSL quando si utilizza IAM e il servizio rifiuterà le richieste di testo semplice.

Tipi di endpoint:

I servizi {{site.data.keyword.cloud}} sono connessi a una rete a tre livelli, che segmenta il traffico pubblico, privato e di gestione.

* Gli **endpoint privati** sono disponibili per le richieste provenienti da cluster Kubernetes, server bare metal, server virtuali e altri servizi di archiviazione cloud. Gli endpoint privati forniscono prestazioni migliori e non comportano addebiti per la larghezza di banda in uscita o in entrata anche se il traffico è interregionale o tra diversi data center. **Ogni qualvolta è possibile, è meglio utilizzare un endpoint privato.**
* Gli **endpoint pubblici** possono accettare le richieste da qualsiasi luogo e i costi vengono valutati sulla larghezza di banda in uscita. La larghezza di banda in entrata è gratuita. Gli endpoint pubblici dovrebbero essere utilizzati per l'accesso non proveniente da una risorsa di calcolo cloud {{site.data.keyword.cloud_notm}}. 

Le richieste devono essere inviate all'endpoint associato all'ubicazione di uno specifico bucket. Se non sei sicuro dell'ubicazione del bucket, è disponibile una [estensione all'API di elenco bucket](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) che restituisce le informazioni su ubicazione e classe di archiviazione per tutti i bucket in un'istanza del servizio.

A partire dal dicembre 2018 abbiamo aggiornato i nostri endpoint. Gli endpoint legacy continueranno a funzionare fino a nuovo avviso. Aggiorna le tue applicazioni per utilizzare i [nuovi endpoint](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints).
{:note}
