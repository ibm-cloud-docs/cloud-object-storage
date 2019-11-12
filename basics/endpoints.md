---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-11-11"

keywords: endpoint, location, object storage

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:table: .aria-labeledby="caption"}

# Endpoints and storage locations
{: #endpoints}

Sending a REST API request or configuring a storage client requires setting a target endpoint or URL. Each storage location has its own set of URLs.
{: .shortdesc}

Private endpoints can be used from within the IBM Cloud and don't incur data transfer charges. Public endpoints can be used from outside the IBM Cloud and do incur transfer charges. If possible, it's best to use a private endpoint.

In December 2018, we updated our endpoints. [Legacy endpoints](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) continue to work until further notice. Update your applications to use the new endpoints listed here.
{:note}

## Regional Endpoints
{: #endpoints-region}

Buckets that are created at a regional endpoint distribute data across three data centers that are spread across a metro area. Any one of these data centers can suffer an outage or even destruction without impacting availability.

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <caption>Regional Endpoints</caption>
    <tr>
      <td>US South</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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
      <td>US East</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>EU United Kingdom</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>EU Germany</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>AP Australia</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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
      <td>AP Japan</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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


## Cross Region Endpoints
{: #endpoints-geo}

Buckets that are created at a cross region endpoint distribute data across three regions. Any one of these regions can suffer an outage or even destruction without impacting availability. Requests are routed to the nearest region's data center by using Border Gateway Protocol (BGP) routing. In an outage, requests are automatically rerouted to an active region. Advanced users who want to write their own failover logic can do so by sending requests to a [specific access point](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) and bypassing the BGP routing.

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <caption>Cross Region Endpoints</caption>
    <tr>
      <td>US Cross Region</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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
      <td>EU Cross Region</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>AP Cross Region</td>
      <td>
        <p>Public</p>
        <p>Private
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




## Single Data Center Endpoints
{: #endpoints-zone}

Single data centers are not colocated with IBM Cloud services, such as IAM or Key Protect, and offer no resiliency in a site outage or destruction. 

If a networking failure results in a partition where the data center is unable to access IAM, authentication and authorization information is read from a cache that might become stale. This cached data might result in a lack of enforcement of new or altered IAM policies for up to 24 hours.
{:important}

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <caption>Table 3: Single Data Center Endpoints</caption>
    <tr>
      <td>Amsterdam, Netherlands</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>Chennai, India</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>Hong Kong S.A.R. of the PRC</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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
        <p>Public</p>
        <p>Private</p>
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
      <td>Mexico City, Mexico</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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
      <td>Milan, Italy</td>
      <td>
        <p>Public</p>
        <p>Private</p>
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
        <p>Public</p>
        <p>Private</p>
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
      <td>Oslo, Norway</td>
      <td>Public</td>
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
      <td>Paris, France</td>
      <td>Public</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.par01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.par01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>San Jose, US</td>
      <td>Public</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>São Paulo, Brazil</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>Seoul, South Korea</td>
      <td>
        <p>Public</p>
        <p>Private
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
      <td>Toronto, Canada</td>
      <td>
        <p>Public</p>
        <p>Private
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

## EU-Managed Endpoints
{: #endpoints-eu-managed}

The Activity Tracker and Logging Service with LogDNA can archive to a bucket at specific {{site.data.keyword.cos_full}} instances. This table shows the EU-Managed locations of {{site.data.keyword.cos_full}} instances for archiving events.

| COS bucket location | Resiliency | City |
|----|----|---
| `ams03` | Single Site | Amsterdam |
| `eu-de` | Regional | Frankfurt |
| `eu-gb` | Regional | London |
| `mil01` | Single Site | Milan |
| `osl01` | Single Site | Oslo |
| `par01` | Single Site | Paris |
| `eu-geo` | Cross Region | Amsterdam, Frankfurt, Milan |
{: caption="EU-managed Endpoints"}
 