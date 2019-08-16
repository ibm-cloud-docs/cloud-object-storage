---

copyright:
  years: 2017
lastupdated: "2018-05-25"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}

# Endpoint e ubicazioni di archiviazione
{: #endpoints}

L'invio di una richiesta API REST o la configurazione di un client di archiviazione richiede la configurazione di un endpoint di destinazione o di un URL. Ogni ubicazione di archiviazione dispone di un proprio insieme di URL. 

La maggior parte degli utenti dovrebbe utilizzare uno dei seguenti endpoint per una determinata ubicazione di archiviazione. Gli endpoint privati dovrebbero essere utilizzati dall'interno di IBM Cloud e non comportare addebiti di trasferimento dati. Gli endpoint pubblici dovrebbero essere utilizzati dall'esterno di IBM Cloud e comportare addebiti di trasferimento. Se possibile, ti consigliamo di utilizzare un endpoint privato. 

A partire da dicembre 2018, abbiamo aggiornato i nostri endpoint. Gli [endpoint legacy](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) continueranno a funzionare fino a nuova comunicazione. Aggiorna le tue applicazioni per utilizzare i nuovi endpoint elencati qui.
{:note}

## Endpoint regionali
{: #endpoints-region}

I bucket creati in un endpoint regionale distribuiscono i dati in tre data center distribuiti in un'area metropolitana. Ognuno di questi data center può subire un'interruzione, o addirittura una distruzione, senza compromettere la disponibilità. 

<table>
  <thead>
    <tr>
      <th>Regione</th>
      <th>Tipo</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Stati Uniti Sud</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us-south.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us-south.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Stati Uniti Est</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us-east.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us-east.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Regno Unito UE</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-gb.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu-gb.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Germania UE</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-de.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu-de.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Australia Asia Pacifico</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.au-syd.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.au-syd.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Giappone Asia Pacifico</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.jp-tok.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.jp-tok.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

## Endpoint interregionali
{: #endpoints-geo}

I bucket creati in un endpoint interregionale distribuiscono i dati in tre regioni. Ognuna di queste regioni può subire un'interruzione, o addirittura una distruzione, senza compromettere la disponibilità. Le richieste vengono instradate al data center della regione più vicino utilizzando l'instradamento Border Gateway Protocol (BGP). Nel caso di un'interruzione, le richieste vengono reinstradate automaticamente a una regione attiva. Gli utenti avanzati che desiderano scrivere la loro logica di failover possono farlo inviando le richieste a un [punto di accesso specifico](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) ed escludendo l'instradamento BGP.

<table>
  <thead>
    <tr>
      <th>Regione</th>
      <th>Tipo</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Interregionale Stati Uniti</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Interregionale UE</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Interregionale Asia Pacifico</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}



## Endpoint a singoli data center
{: #endpoints-zone}

I singoli data center non sono co-ubicati con i servizi IBM Cloud, ad esempio IAM o Key Protect, e non offrono resilienza in caso interruzione o distruzione di un sito.  

Se un errore di rete comporta una partizione in cui il data center non è in grado di raggiungere una regione IBM Cloud centrale per l'accesso a IAM, le informazioni di autenticazione e autorizzazione vengono lette da una cache che potrebbe diventare obsoleta. Ciò può comportare la mancata applicazione di politiche IAM nuove o modificate fino a 24 ore.
{:important}

<table>
  <thead>
    <tr>
      <th>Regione</th>
      <th>Tipo</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Amsterdam, Paesi Bassi</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ams03.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ams03.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Chennai, India</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.che01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.che01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Hong Kong</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.hkg02.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.hkg02.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Melbourne, Australia</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mel01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mel01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Città del Messico, Messico</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mex01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mex01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Milano, Italia</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mil01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mil01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Montréal, Canada</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mon01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mon01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Oslo, Norvegia</td>
      <td>Pubblico</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.osl01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.osl01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>San Jose, USA</td>
      <td>Pubblico</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>San Paolo, Brasile</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sao01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sao01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Seul, Corea del Sud</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.seo01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.seo01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Toronto, Canada</td>
      <td>
        <p>Pubblico</p>
        <p>Privato</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.tor01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.tor01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

