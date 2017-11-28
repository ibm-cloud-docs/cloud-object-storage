---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Select regions and endpoints

Both regional and cross region endpoints are available for connecting applications to {{site.data.keyword.cos_full}}. Compute workloads co-located with a Regional {{site.data.keyword.cos_short}} endpoint will see lower latency and better performance. For workloads not concentrated in a single geographic area, the `geo` endpoint routes connections to the nearest regional data centers.

When using a cross region endpoint, it is possible to direct inbound traffic to a specific access point while still distributing data across all three regions. While this can improve performance for workloads running in the same region as the targeted access point, there is no automated failover if that region becomes unavailable.  Applications that direct traffic to an access point instead of the `geo` endpoint **must** implement appropriate failover logic internally to achieve the availabity advantages of the cross-region storage.
{:tip}

All requests must use SSL when using IAM, and the service will reject any plaintext requests.

Types of endpoint:

{{site.data.keyword.cloud}} services are connected to a three-tiered network, segmenting public, private, and management traffic.

* **Private endpoints** are available for requests originating from Kubernetes clusters, bare metal servers, virtual servers, and other cloud storage services. Private endpoints provide better performance and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. **Whenever possible, it is best to use a private endpoint.**
* **Public endpoints** can accept requests from anywhere and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Public endpoints should be used for access not originating from an {{site.data.keyword.cloud_notm}} cloud computing resource.  **Note**: Cloud Foundry applications are unable to access the private network, so data transfer is metered and charged at standard public network bandwidth rates.


## US Cross Region Endpoints

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
    <tr>
    <td rowspan="2">US Cross Region</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3-api.us-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3-api.us-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Dallas Access Point</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3-api.dal-us-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3-api.dal-us-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">San Jose Access Point</td>
        <td>public</td>
    <td><code class="highlighter-rouge">s3-api.sjc-us-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3-api.sjc-us-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Washington, DC Access Point</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3-api.wdc-us-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3-api.wdc-us-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
</table>
{:.endpointtable}


## US Regional Endpoints

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
    <tr>
    <td rowspan="2">US South</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3.us-south.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.us-south.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
  <td rowspan="2">US East</td>
  <td>public</td>
  <td><code class="highlighter-rouge">s3.us-east.objectstorage.softlayer.net</code></td>
</tr>
<tr>
  <td>private</td>
  <td><code class="highlighter-rouge">s3.us-east.objectstorage.service.networklayer.com</code></td>
</tr>
</table>
{:.endpointtable}


## EU Cross Region Endpoints

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Type</th>
      <th>Endpoint</th>
    </tr>
  </thead>
    <tr>
    <td rowspan="2">EU Cross Region</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3.eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Amsterdam Access Point</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3.ams-eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.ams-eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Frankfurt Access Point</td>
        <td>public</td>
    <td><code class="highlighter-rouge">s3.fra-eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.fra-eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Milan Access Point</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3.mil-eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.mil-eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
</table>
{:.endpointtable}
