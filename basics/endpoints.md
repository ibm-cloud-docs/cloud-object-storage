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

# Select regions and endpoints

A bucket's resiliency is defined by the endpoint used to create it.  _Cross Region_ resiliency will spread your data across several metropolitan areas, while _Regional_ resiliency will spread data across a single metropolitan area.  _Single Data Center_ resiliency spreads data across multiple appliances within a single data center.  Regional and Cross Region buckets can maintain availability during a site outage.

Compute workloads co-located with a Regional {{site.data.keyword.cos_short}} endpoint will see lower latency and better performance. For workloads not concentrated in a single geographic area, a Cross Region `geo` endpoint routes connections to the nearest regional data centers.

When using a Cross Region endpoint, it is possible to direct inbound traffic to a specific access point while still distributing data across all three regions. When sending requests to an individual access point there is no automated failover if that region becomes unavailable.  Applications that direct traffic to an access point instead of the `geo` endpoint **must** implement appropriate failover logic internally to achieve the availabity advantages of the cross-region storage.
{:tip}

Some workloads may benefit from using a Single Data Center endpoint.  Data stored in a single site is still distributed across many physical storage appliances, but is contained to a single data center.  This can improve performance for compute resources within the same site, but will not maintain availability in the case of a site outage.  Single Data Center buckets do not provide automated replication or backup in the case of site destruction, so any applications using a single site should consider disaster recovery in their design.

All requests must use SSL when using IAM, and the service will reject any plaintext requests.

Types of endpoint:

{{site.data.keyword.cloud}} services are connected to a three-tiered network, segmenting public, private, and management traffic.

* **Private endpoints** are available for requests originating from Kubernetes clusters, bare metal servers, virtual servers, and other cloud storage services. Private endpoints provide better performance and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. **Whenever possible, it is best to use a private endpoint.**
* **Public endpoints** can accept requests from anywhere and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Public endpoints should be used for access not originating from an {{site.data.keyword.cloud_notm}} cloud computing resource.  **Note**: Cloud Foundry applications and Cloud Functions are unable to access the private network, so data transfer is metered and charged at standard public network bandwidth rates.

Requests must be sent to the endpoint associated with a given bucket's location. If you aren't sure where a bucket is located, there is an [extension to the bucket listing API](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html#list-buckets-extended) that returns the location and storage class information for all buckets in a service instance.

As of December 2018, we have updated our endpoints. Legacy endpoints will continue to work until further notice. Please update your applications to use the new endpoints listed below.
{:note}


## New Endpoints

### US Cross Region Endpoints

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
      <td>Dallas Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.dal.us.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.dal.us.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>San Jose Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc.us.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc.us.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Washington, DC Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.wdc.us.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.wdc.us.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}


### US Regional Endpoints

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
  </tbody>
</table>
{:.endpointtable}


### EU Cross Region Endpoints

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
      <td>Amsterdam Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ams.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ams.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Frankfurt Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.fra.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.fra.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Milan Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mil.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mil.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

### EU Region Endpoints

<table>
  <colgroup>
    <col/>
    <col/>
    <col/>
  </colgroup>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
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
  </tbody>
</table>
{:.endpointtable}

### AP Cross Region Endpoints

<table>
  <colgroup>
    <col/>
    <col/>
    <col/>
  </colgroup>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
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
    <tr>
      <td>Tokyo Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.tok.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.tok.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Seoul Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.seo.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.seo.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Hong Kong Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.hkg.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.hkg.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

### AP Region Endpoints

<table>
  <colgroup>
    <col/>
    <col/>
    <col/>
  </colgroup>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
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



### Single Data Center Endpoints

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
      <td>Melbourne, Australia</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
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
        <p>Private
        </p>
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

## Legacy Endpoints

### US Cross Region Endpoints (Legacy)

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
          <code class="highlighter-rouge">s3-api.us-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3-api.us-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Dallas Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3-api.dal-us-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3-api.dal-us-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>San Jose Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3-api.sjc-us-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3-api.sjc-us-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Washington, DC Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3-api.wdc-us-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3-api.wdc-us-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}


### US Regional Endpoints (Legacy)

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
          <code class="highlighter-rouge">s3.us-south.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.us-south.objectstorage.service.networklayer.com</span>
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
          <code class="highlighter-rouge">s3.us-east.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.us-east.objectstorage.service.networklayer.com</span>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}


### EU Cross Region Endpoints (Legacy)

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
      <td>EU Cross Region</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.eu-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Amsterdam Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ams-eu-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.ams-eu-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Frankfurt Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.fra-eu-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.fra-eu-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Milan Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mil-eu-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.mil-eu-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

### EU Region Endpoints (Legacy)

<table>
  <colgroup>
    <col/>
    <col/>
    <col/>
  </colgroup>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>EU United Kingdom</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-gb.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.eu-gb.objectstorage.service.networklayer.com</code>
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
          <code class="highlighter-rouge">s3.eu-de.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.eu-de.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

### AP Cross Region Endpoints (Legacy)

<table>
  <colgroup>
    <col/>
    <col/>
    <col/>
  </colgroup>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>AP Cross Region</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ap-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.ap-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Tokyo Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.tok-ap-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.tok-ap-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Seoul Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.seo-ap-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.seo-ap-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Hong Kong Access Point</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.hkg-ap-geo.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.hkg-ap-geo.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

### AP Region Endpoints (Legacy)

<table>
  <colgroup>
    <col/>
    <col/>
    <col/>
  </colgroup>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>AP Japan</td>
      <td>
        <p>Public</p>
        <p>Private</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.jp-tok.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.jp-tok.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}



### Single Data Center Endpoints (Legacy)

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
          <code class="highlighter-rouge">s3.ams03.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.ams03.objectstorage.service.networklayer.com</code>
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
          <code class="highlighter-rouge">s3.che01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.che01.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Melbourne, Australia</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mel01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.mel01.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Mexico City, Mexico</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mex01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.mex01.objectstorage.service.networklayer.com</code>
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
          <code class="highlighter-rouge">s3.mon01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.mon01.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Oslo, Norway</td>
      <td>Public</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.osl01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.osl01.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>São Paulo, Brazil</td>
      <td>
        <p>Public</p>
        <p>Private
        </p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sao01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.sao01.objectstorage.service.networklayer.com</code>
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
          <code class="highlighter-rouge">s3.seo01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.seo01.objectstorage.service.networklayer.com</code>
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
          <code class="highlighter-rouge">s3.tor01.objectstorage.softlayer.net</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.tor01.objectstorage.service.networklayer.com</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}
