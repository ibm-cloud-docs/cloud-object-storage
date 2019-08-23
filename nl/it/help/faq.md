---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# Domande frequenti (FAQ)
{: #faq}

## Domande sull'API
{: #faq-api}

**I nomi bucket di {{site.data.keyword.cos_full}} sono sensibili al maiuscolo/minuscolo?**

I nomi bucket devono essere indirizzabili al DNS e quindi non sono sensibili al maiuscolo/minuscolo.

**Qual è il numero massimo di caratteri che può essere utilizzato in un nome oggetto?**

1024

**Come posso scoprire la dimensione totale del mio bucket utilizzando l'API?**

Non è possibile recuperare la dimensione di un bucket con una singola richiesta. Dovrai elencare i contenuti di un bucket e sommare la dimensione di ciascun oggetto.

**Posso migrare i dati da AWS S3 a {{site.data.keyword.cos_full_notm}}?**

Sì, puoi utilizzare i tuoi strumenti esistenti per leggere e scrivere i dati in {{site.data.keyword.cos_full_notm}}. Dovrai configurare le credenziali HMAC per consentire l'autenticazione dei tuoi strumenti. Al momento non sono supportati tutti gli strumenti compatibili con S3. Per ulteriori dettagli, vedi [Utilizzo delle credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).


## Domande sull'offerta
{: #faq-offering}

**Esiste un limite di 100 bucket per un account?  Cosa succede se me ne servono altri?**

Sì, 100 è l'attuale limite di bucket. In genere, i prefissi sono il modo migliore per raggruppare gli oggetti in un bucket, a meno che i dati non debbano trovarsi in una regione o una classe di archiviazione diversa. Ad esempio, per raggruppare i record dei pazienti, devi utilizzare un prefisso per ogni paziente. Se questa non è una soluzione praticabile, contatta il supporto clienti.

**Se voglio archiviare i miei dati utilizzando {{site.data.keyword.cos_full_notm}} Vault o Cold Vault, devo creare un altro account?**

No, le classi di archiviazione (come anche le regioni) sono definite a livello di bucket. Ti basta creare un nuovo bucket impostato sulla classe di archiviazione desiderata.

**Quando creo un bucket tramite l'API, come posso impostare la classe di archiviazione?**

La classe di archiviazione (ad esempio `us-flex`) è assegnata alla variabile di configurazione `LocationConstraint` per quel bucket. Ciò è dovuto a una differenza fondamentale nel modo in cui AWS S3 e {{site.data.keyword.cos_full_notm}} gestiscono le classi di archiviazione. {{site.data.keyword.cos_short}} imposta le classi di archiviazione a livello di bucket, mentre AWS S3 assegna una classe di archiviazione a un singolo oggetto. Un elenco di codici di provisioning validi per `LocationConstraint` può essere consultato nella [guida alle classi di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes).

**È possibile modificare la classe di archiviazione di un bucket?  Ad esempio, se ci sono dati di produzione nella classe 'standard', possiamo passare facilmente a 'vault' per scopi di fatturazione se non li utilizziamo frequentemente?**

Attualmente la modifica della classe di archiviazione richiede lo spostamento o la copia manuale dei dati da un bucket a un altro con la classe di archiviazione desiderata.


## Domande sulle prestazioni
{: #faq-performance}

**La coerenza dei dati in {{site.data.keyword.cos_short}} ha un impatto sulle prestazioni?**

La coerenza con qualsiasi sistema distribuito comporta un costo, ma l'efficienza del sistema di archiviazione distribuito di {{site.data.keyword.cos_full_notm}} è maggiore e il sovraccarico è inferiore rispetto ai sistemi con più copie sincrone.

**Non ci sono implicazioni sulle prestazioni se la mia applicazione deve manipolare oggetti di grandi dimensioni?**

Per l'ottimizzazione delle prestazioni, gli oggetti possono essere caricati e scaricati in più parti, in parallelo.


## Domande sulla crittografia
{: #faq-encryption}

**{{site.data.keyword.cos_short}} fornisce la crittografia dei dati inattivi e in transito?**

Sì. I dati inattivi vengono crittografati con la crittografia automatica AES (Advanced Encryption Standard) a 256 bit e l'hash SHA-256 (Secure Hash Algorithm 256) lato provider. I dati in transito sono protetti utilizzando la crittografia TLS/SSL (Transport Layer Security/Secure Sockets Layer) o SNMPv3 con AES carrier-grade integrata.

**Qual è il tipico sovraccarico di crittografia se un cliente vuole crittografare i propri dati?**

La crittografia lato server è sempre attiva per i dati dei clienti. Rispetto all'hashing richiesto nell'autenticazione S3 e alla codifica di cancellazione, la crittografia non è una parte significativa del costo di elaborazione di COS.

**{{site.data.keyword.cos_short}} crittografa tutti i dati?**

Sì, {{site.data.keyword.cos_short}} crittografa tutti i dati.

**{{site.data.keyword.cos_short}} ha la conformità FIPS 140-2 per gli algoritmi di crittografia?**

Sì, l'offerta IBM COS Federal è approvata per i controlli di sicurezza moderata FedRAMP che richiedono una configurazione FIPS convalidata. IBM COS Federal è certificato FIPS 140-2 livello 1. Per ulteriori informazioni sull'offerta COS Federal, [contattaci](https://www.ibm.com/cloud/government) tramite il nostro sito federale.

**Sarà supportata la crittografia della chiave client?**

Sì, la crittografia della chiave client è supportata tramite SSE-C o Key Protect.

## Domande di carattere generale
{: #faq-general}

**Quanti oggetti può contenere un singolo bucket?**

Non esiste un limite pratico al numero di oggetti che possono essere contenuti in un singolo bucket.

**Posso nidificare i bucket uno dentro l'altro?**

No, i bucket non possono essere nidificati. Se all'interno di un bucket è richiesto un maggiore livello di organizzazione, è supportato l'uso dei prefissi: `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. Nota che la chiave oggetto rimane la combinazione `{object-prefix}/{object-name}`.

**Qual è la differenza tra le richieste di 'Classe A' e 'Classe B'?**

Le richieste di 'Classe A' sono operazioni che comportano la modifica o l'elenco. Queste operazioni includono la creazione di bucket, il caricamento o la copia di oggetti, la creazione o la modifica di configurazioni, l'elenco dei bucket e l'elenco dei contenuti dei bucket. Le richieste di 'Classe B' sono quelle correlate al recupero di oggetti o dei relativi metadati/configurazioni associati dal sistema. Non è previsto alcun addebito per l'eliminazione dei bucket o degli oggetti dal sistema.

**Qual è il modo migliore per strutturare i tuoi dati utilizzando l'archiviazione oggetti in modo che tu possa esaminarli e trovare quello che stai cercando?  Senza una struttura di directory, avere migliaia di file su un solo livello rende difficile la visualizzazione.**

Puoi utilizzare i metadati associati a ciascun oggetto per trovare gli oggetti che stai cercando. Il più grande vantaggio dell'archiviazione oggetti è che i metadati sono associati a ciascun oggetto. Ogni oggetto può avere fino a 4 MB di metadati in {{site.data.keyword.cos_short}}. Quando scaricati su un database, i metadati offrono eccellenti capacità di ricerca. È possibile archiviare un numero elevato di coppie (chiave, valore) in 4 MB. Puoi anche utilizzare la ricerca tramite prefisso per trovare quello che stai cercando. Ad esempio, se utilizzi i bucket per separare i dati di ciascun cliente, puoi utilizzare i prefissi all'interno dei bucket per l'organizzazione. Ad esempio:  /bucket1/folder/object dove 'folder/' è  il prefisso.

**Puoi confermare che {{site.data.keyword.cos_short}} sia "immediatamente coerente" rispetto a "eventualmente coerente"?**

{{site.data.keyword.cos_short}} è "immediatamente coerente" per i dati ed "eventualmente coerente" per la contabilità di utilizzo.


**{{site.data.keyword.cos_short}} può partizionare i dati automaticamente come HDFS, in modo che io possa leggere le partizioni in parallelo, ad esempio con Spark?**

{{site.data.keyword.cos_short}} supporta un'operazione GET a intervalli sull'oggetto, per cui un'applicazione può eseguire un'operazione di tipo lettura con striping distribuita. L'esecuzione dello striping avviene sull'applicazione da gestire.
