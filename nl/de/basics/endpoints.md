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

# Endpunkte und Speicherpositionen
{: #endpoints}

Wenn Sie eine REST-API-Anforderung senden oder einen Speicherclient konfigurieren, müssen Sie einen Zielendpunkt oder eine Ziel-URL festlegen. Jede Speicherposition verfügt über eine eigene Gruppe von URLs.

Die meisten Benutzer sollten einen der folgenden Endpunkte für eine bestimmte Speicherposition verwenden. In der IBM Cloud sollten private Endpunkte verwendet werden, für die keine Gebühren für die Datenübertragung anfallen. Außerhalb der IBM Cloud sollten öffentliche Endpunkte verwendet werden, für die Gebühren für die Datenübertragung anfallen. Wenn möglich, wird die Verwendung eines privaten Endpunkts empfohlen.

Seit Dezember 2018 sind die Endpunkte aktualisiert. [Traditionelle Endpunkte](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) können bis auf Weiteres weiterverwendet werden. Aktualisieren Sie Ihre Anwendungen, um die hier aufgelisteten neuen Endpunkte verwenden zu können.
{:note}

## Regionale Endpunkte
{: #endpoints-region}

An einem regionalen Endpunkt erstellte Buckets verteilen Daten auf drei Rechenzentren, die auf einen Ballungsraum verteilt sind. In jedem dieser Rechenzentren kann es zu einem Ausfall oder auch zu schweren Beschädigungen kommen, ohne dass hierdurch die Verfügbarkeit der Daten beeinträchtigt wird.

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Typ</th>
      <th>Endpunkt</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Vereinigte Staaten (Süden)</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Vereinigte Staaten (Osten)</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Europa (Großbritannien)</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Europa (Deutschland)</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Asien-Pazifik (Australien)</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Asien-Pazifik (Japan)</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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

## Regionsübergreifende Endpunkte
{: #endpoints-geo}

Buckets, die an einem regionsübergreifenden Endpunkt erstellt wurden, verteilen Daten über drei Regionen. In jeder dieser Regionen kann es zu einem Ausfall oder auch zu schweren Beschädigungen kommen, ohne dass hierdurch die Verfügbarkeit der Daten beeinträchtigt wird. Anforderungen werden mit dem BGP-Routing (BGP = Border Gateway Protocol) an das Rechenzentrum der nächstgelegenen Region weitergeleitet. Bei einem Ausfall werden Anforderungen automatisch an eine aktive Region umgeleitet. Fortgeschrittene Benutzer, die eigene Failoverlogik schreiben möchten, können hierzu Anforderungen an einen [bestimmten Zugriffspunkt](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) senden und so das BGP-Routing umgehen.

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Typ</th>
      <th>Endpunkt</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Vereinigte Staaten - Regionsübergreifend</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Europa - Regionsübergreifend</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Asien-Pazifik - Regionsübergreifend</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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



## Endpunkte mit einzelnen Rechenzentren
{: #endpoints-zone}

Einzelne Rechenzentren verfügen nicht über zugehörige IBM Cloud-Services wie beispielsweise IAM oder Key Protect und bieten bei Auftreten eines Standortausfalls oder bei schweren Beschädigungen eines Standorts keine Ausfallsicherheit. 

Wenn ein Netzbetriebsfehler dazu führt, dass das Rechenzentrum in einem bestimmten Bereich eine zentrale IBM Cloud-Region für den Zugriff auf IAM nicht mehr erreichen kann, dann werden die Authentifizierungs- und Berechtigungsinformationen aus einem Cache gelesen, dessen Daten möglicherweise nicht mehr aktuell sind. Dies kann dazu führen, dass neue oder geänderte IAM-Richtlinien für bis zu 24 Stunden nicht mehr umgesetzt werden können.
{:important}

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Typ</th>
      <th>Endpunkt</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Amsterdam, Niederlande</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Chennai, Indien</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Hongkong</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Melbourne, Australien</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Mexiko Stadt, Mexiko</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Mailand, Italien</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Montréal, Kanada</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat</p>
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
      <td>Oslo, Norwegen</td>
      <td>Öffentlich</td>
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
      <td>Öffentlich</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>São Paulo, Brasilien</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Seoul, Südkorea</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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
      <td>Toronto, Kanada</td>
      <td>
        <p>Öffentlich</p>
        <p>Privat
        </p>
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

