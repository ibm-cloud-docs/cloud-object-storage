---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Utilizza il trasferimento ad alta velocità Aspera
{: #aspera}

Il trasferimento ad alta velocità Aspera supera le limitazioni dei trasferimenti FTP e HTTP tradizionali per migliorare le prestazioni di trasferimento dati nella maggior parte delle condizioni, specialmente nelle reti in cui si verificano una latenza elevata e la perdita di pacchetti. Invece della richiesta `PUT` HTTP standard, il trasferimento ad alta velocità Aspera carica l'oggetto utilizzando il [protocollo FASP](https://asperasoft.com/technology/transport/fasp/). L'utilizzo del trasferimento ad alta velocità Aspera per i caricamenti e gli scaricamenti offre i seguenti vantaggi: 

- Velocità di trasferimento più veloci
- Trasferire i caricamenti di oggetti di grandi dimensioni superiori a 200MB nella console e a 1GB utilizzando un SDK o una libreria
- Caricare intere cartelle di qualsiasi tipo di dati inclusi i file multimediali, le immagini disco e qualsiasi altro dato strutturato o non strutturato
- Personalizzare le velocità di trasferimento e le preferenze predefinite
- I trasferimenti possono essere esaminati, sospesi/ripresi o annullati indipendentemente

Il trasferimento ad alta velocità Aspera è disponibile nella [console](#aspera-console) di {{site.data.keyword.cloud_notm}} e può essere anche utilizzato in modo programmatico utilizzando un [SDK](#aspera-sdk). 

Il trasferimento ad alta velocità Aspera è disponibile solo in determinate regioni. Per ulteriori dettagli, vedi [Servizi integrati](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability).
{:tip}

## Utilizzo della console
{: #aspera-console}

Quando crei un bucket in una [regione supportata](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability), hai la possibilità di selezionare il trasferimento ad alta velocità Aspera per caricare i file o le cartelle. Quando provi a caricare un oggetto, ti viene richiesto di installare il client Aspera Connect.

### Installa Aspera Connect
{: #aspera-install}

1. Seleziona **Install Aspera Connect** client.
2. Segui le istruzioni di installazione in base al tuo sistema operativo e al tuo browser.
3. Riprendi il caricamento del file o della cartella.

Il plug-in di Aspera Connect può essere installato direttamente anche dal [sito web di Aspera](https://downloads.asperasoft.com/connect2/). Per assistenza nella risoluzione dei problemi con il plug-in Aspera Connect, [vedi la documentazione](https://downloads.asperasoft.com/en/documentation/8).

Una volta installato il plug-in, puoi configurare il trasferimento ad alta velocità Aspera come predefinito per i caricamenti nel bucket di destinazione che utilizzano lo stesso browser. Seleziona **Remember my browser preferences**. Le opzioni sono disponibili anche nella pagina di configurazione del bucket in **Transfer options**. Queste opzioni ti consentono di scegliere tra standard e alta velocità come trasporto predefinito per i caricamenti e gli scaricamenti. 

Di norma, l'utilizzo della console basata su web di IBM Cloud Object Storage non è il modo più comune per utilizzare {{site.data.keyword.cos_short}}. L'opzione di trasferimento standard limita la dimensione degli oggetti a 200MB e il nome file e la chiave saranno identici. Il supporto per le dimensioni degli oggetti più grandi e per le prestazioni migliorate (a seconda dei fattori di rete) viene fornito dal trasferimento ad alta velocità Aspera.

### Stato di trasferimento
{: #aspera-console-transfer-status}

**Active:** una volta avviato un trasferimento, lo stato del trasferimento viene visualizzato come attivo. Mentre il trasferimento è attivo, puoi sospendere, riprendere o annullare un trasferimento attivo.  

**Completed:** al completamento del tuo trasferimento, le informazioni relative ad esso e a tutti i trasferimenti di questa sessione vengono visualizzate nella scheda Completed. Puoi eliminare queste informazioni. Vedrai solo le informazioni sui trasferimenti completati nella sessione corrente. 

**Preferences:** puoi configurare l'impostazione predefinita per i caricamenti e/o gli scaricamenti su alta velocità. 

Gli scaricamenti eseguiti utilizzando il trasferimento ad alta velocità Aspera comporteranno addebiti in uscita. Per ulteriori informazioni, vedi la [pagina dei prezzi](https://www.ibm.com/cloud/object-storage).
{:tip}

**Advanced Preferences:** puoi configurare la larghezza di banda per i caricamenti e gli scaricamenti. 

----

## Utilizzo delle librerie e degli SDK
{: #aspera-sdk}

L'SDK del trasferimento ad alta velocità Aspera consente di avviare il trasferimento ad alta velocità all'interno delle tue applicazioni personalizzate quando utilizzi Java o Python.

### Quando utilizzare il trasferimento ad alta velocità Aspera
{: #aspera-guidance}

Il protocollo FASP utilizzato dal trasferimento ad alta velocità Aspera non è adatto per tutti i trasferimenti di dati verso e da COS. Nello specifico, i trasferimenti che utilizzano il trasferimento ad alta velocità Aspera dovrebbero:

1. Utilizzare sempre più sessioni - almeno due sessioni in parallelo utilizzeranno al meglio le capacità dei trasferimenti ad alta velocità Aspera. Vedi le indicazioni specifiche per [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) e [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
2. Il trasferimento ad alta velocità Aspera è ideale per i file più grandi e i file o le directory che contengono una quantità totale dati inferiore a 1 GB devono invece trasferire l'oggetto in più parti utilizzando le classi standard di Transfer Manager. I trasferimenti ad alta velocità Aspera richiedono un TTFB (time to first byte) più lungo dei normali trasferimenti HTTP. L'istanziazione di molti oggetti di Aspera Transfer Manager per gestire i trasferimenti di singoli file più piccoli può causare prestazioni scadenti relative alle richieste HTTP di base, pertanto è meglio istanziare invece un singolo client per caricare una directory di file più piccoli. 
3. Il trasferimento ad alta velocità Aspera è stato progettato in parte per migliorare le prestazioni negli ambienti di rete con grandi quantità di perdita di pacchetti, rendendo il protocollo performante sulle lunghe distanze e nelle reti di ampie aree pubbliche. Il trasferimento ad alta velocità Aspera non deve essere utilizzato per i trasferimenti all'interno di una regione o di un data center. 

L'SDK del trasferimento ad alta velocità Aspera è closed-source e, pertanto, una dipendenza facoltativa per l'SDK COS (che utilizza una licenza Apache).
{:tip}

#### Impacchettamento del trasferimento ad alta velocità COS/Aspera
{: #aspera-packaging}

L'immagine riportata di seguito mostra una panoramica di alto livello su come l'SDK COS interagisce con la libreria del trasferimento ad alta velocità Aspera per fornire funzionalità. 

<img alt="SDK del trasferimento ad alta velocità COS/Aspera." src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="Figura 1: SDK del trasferimento ad alta velocità COS/Aspera." caption-side="bottom"} 

### Piattaforme supportate
{: #aspera-sdk-platforms}

| Sistema operativo      | Versione  | Architettura | Versione Java testata | Versione Python testata |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64-Bit       | 6 e successive | 2.7, 3.6       |
| Mac OS X               | 10.13     | 64-Bit       | 6 e successive | 2.7, 3.6       |
| Microsoft&reg; Windows | 10        | 64-Bit       | 6 e successive | 2.7, 3.6       |

Ogni sessione di trasferimento ad alta velocità Aspera genera un singolo processo `ascp` che viene eseguito sulla macchina client per eseguire il trasferimento. Assicurati che il tuo ambiente di calcolo possa consentire l'esecuzione di questo processo.
{:tip}

**Limitazioni aggiuntive**

* I file binari a 32-bit non sono supportati
* Il supporto Windows richiede Windows 10
* Il supporto Linux è limitato a Ubuntu (testato su 18.04 LTS)
* I client Aspera Transfer Manager devono essere creati utilizzando le chiavi API IAM e non le credenziali HMAC. 

### Come ottenere l'SDK utilizzando Java
{: #aspera-sdk-java} 
{: java}

Il modo migliore per utilizzare {{site.data.keyword.cos_full_notm}} e l'SDK Java del trasferimento ad alta velocità Aspera è quello di utilizzare Maven per gestire le dipendenze. Se non hai dimestichezza con Maven, puoi diventare operativo utilizzando la guida [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window}.
{: java}

Maven utilizza un file denominato `pom.xml` per specificare le librerie (e le loro versioni) necessarie per un progetto Java. Di seguito è riportato un file `pom.xml` di esempio per utilizzare {{site.data.keyword.cos_full_notm}} e l'SDK Java del trasferimento ad alta velocità Aspera
{: java}

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

Esempi di avvio dei trasferimenti ad alta velocità Aspera con Java sono disponibili nella sezione [Utilizzo del trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera).
{: java}

### Come ottenere l'SDK utilizzando Python
{: #aspera-sdk-python} 
{: python}

Gli SDK {{site.data.keyword.cos_full_notm}} e Python del trasferimento ad alta velocità Aspera sono disponibili nel repository del software PyPI (Python Package Index).
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Esempi di avvio dei trasferimenti Aspera con Python sono disponibili nella sezione [Utilizzo del trasferimento ad alta velocità Aspera](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
{: python}
