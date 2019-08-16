---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# Crittografia e sicurezza dei dati
{: #security}

{{site.data.keyword.cos_full}} utilizza un approccio innovativo per un'archiviazione efficace in termini di costi di grandi volumi di dati non strutturati, garantendo al tempo stesso la sicurezza, la disponibilità e l'affidabilità. Ciò si ottiene utilizzando gli IDA (Information Dispersal Algorithm) per separare i dati in "sezioni" non riconoscibili che vengono distribuite in una rete di data center, rendendo la trasmissione e l'archiviazione di dati intrinsecamente private e sicure. Nessuna copia completa dei dati è presente in un singolo nodo di archiviazione e deve essere disponibile solo un sottoinsieme di nodi per richiamare completamente i dati sulla rete.

Tutti i dati in {{site.data.keyword.cos_full_notm}} sono crittografati quando sono inattivi. Questa tecnologia crittografa singolarmente ogni oggetto utilizzando chiavi generate per ogni oggetto. Queste chiavi sono protette e archiviate in modo affidabile utilizzando gli stessi IDA (Information Dispersal Algorithm) che proteggono i dati oggetto utilizzando un AONT (All-or-Nothing Transform), che impedisce la divulgazione dei dati chiave in caso di compromissione di singoli nodi o unità disco rigido.

Se è necessario che l'utente controlli le chiavi di crittografia, le chiavi root possono essere fornite [in base a ogni singolo oggetto utilizzando SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c), oppure [in base a ogni singolo bucket utilizzando SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp).

È possibile accedere all'archiviazione su HTTPS e, internamente, i dispositivi di archiviazione sono certificati e comunicano tra loro utilizzando TLS.


## Eliminazione dei dati
{: #security-deletion}

Dopo che i dati sono stati eliminati, esistono diversi meccanismi per evitare che gli oggetti eliminati vengano ripristinati o ricreati. L'eliminazione di un oggetto passa per diverse fasi, dal contrassegnare i metadati che indicano l'oggetto come eliminato al rimuovere le regioni del contenuto alla finalizzazione della cancellazione sulle unità stesse fino all'eventuale sovrascrittura dei blocchi che rappresentano quella sezione di dati. A seconda del fatto che si sia compromesso il data center o si possiedano dischi fisici, il momento in cui un oggetto diventa non ripristinabile dipende dalla fase dell'operazione di eliminazione. Quando l'oggetto di metadati viene aggiornato, i client esterni alla rete di data center non possono più leggere l'oggetto. Quando una maggioranza delle sezioni che rappresentano le regioni del contenuto è stata finalizzata dai dispositivi di archiviazione, non è possibile accedere all'oggetto.

## Isolamento dei tenant
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} è una soluzione di archiviazione oggetti multi-tenant a infrastruttura condivisa. Se il tuo carico di lavoro richiede un'archiviazione dedicata o isolata, visita [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) per ulteriori informazioni.
