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

# Endpoints and storage locations
{: #endpoints}

Sending a REST API request or configuring a storage client requires setting a target endpoint or URL. Most users should use one of the following endpoints for a given storage location. Private endpoints should be used from within the IBM cloud and do not incur data transfer charges. Public endpoints should be used from outside the IBM cloud and do incur transfer charges. If possible, it's recommended to use a private endpoint. 

As of December 2018, we have updated our endpoints. [Legacy endpoints](/docs/services/cloud-object-storage/info/advanced_endpoints.html) will continue to work until further notice. Please update your applications to use the new endpoints listed below.
{:note}

## Regional Endpoints
{: #endpoints-region}

Buckets created at a regional endpoint distribute data across three data centers spread across a metro area. Any one of these data centers can suffer an outage or even destruction without impacting availability.

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
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
          <code class="highlighter-rouge">s3.private.us-south.cloud-object-storage.appdomain.cloud</span>
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
          <code class="highlighter-rouge">s3.private.us-east.cloud-object-storage.appdomain.cloud</span>
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
{:.endpointtable}

## Cross Region Endpoints
{: #endpoints-geo}

Buckets created at a cross region endpoint distribute data across three regions. Any one of these regions can suffer an outage or even destruction without impacting availability. Requests are routed to the nearest region's data center using Border Gateway Protocol (BGP) routing. In the case of an outage, requests are automatically re-routed to an active region. Advanced users who wish to write their own failover logic may do so by sending requests to a [specific access point](/docs/services/cloud-object-storage/info/advanced_endpoints.html) and bypassing the BGP routing.

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
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
{:.endpointtable}



## Single Data Center Endpoints
{: #endpoints-zone}

Single data centers are not co-located with IBM Cloud services, such as IAM or Key Protect, and offer no resiliency in the event of an site outage or destruction. 

If a networking failure results in a partition where the data center is unable to reach a core IBM Cloud region to access IAM, authentication and authorization information is read from a cache that may become stale. This may result in a lack of enforcement of new or altered IAM policies for up to 24 hours.
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
      <td>Hong Kong</td>
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
      <td>San Jose, USA</td>
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
{:.endpointtable}

