---

copyright:
  years: 2017
lastupdated: "2017-02-23"

---

# Select regions and endpoints

Both regional and cross region endpoints are available for connecting applications or clients that use the S3 API to the public cloud. Endpoints should be chosen based on proximity to the application servers accessing the object store to minimize latency.  For workloads not concentrated in a single geographic area, the Cross Region endpoint routes connections to the nearest regional data centers.  When using a cross region endpoint, it is possible to direct traffic to a specific data center, although the data will still be distributed across all three sites.  Endpoints can be accessed over either plaintext or SSL depending on security requirements.

Types of endpoint:

* **Public endpoints** can be accessed from anywhere and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Public endpoints should be used for access not originating from a SoftLayer data center. 
* **Private endpoints** can be accessed by customers running Virtual Machines or Bare Metal Servers on SoftLayer. Private endpoints do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. Customers with workloads running in a SoftLayer data center in Dallas, San Jose, or Washington, DC should use the endpoint for that same city. Customers with existing workloads running in a remote SoftLayer data center should use the US Region endpoint.

{% include important.html content="Bluemix infrastructure offerings are connected to a three-tiered network, segmenting public, private, and management traffic. Infrastructure on a customer's Bluemix account may transfer data between one another across the private network at no cost. Infrastructure offerings (such as bare metal servers, virtual servers, and cloud storage) connect to other applications and services in the Bluemix catalog (such as Watson services, containers, runtimes) across the public network, so data transfer between those two types of offerings is  metered and charged at standard public network bandwidth rates." %}

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
    <td rowspan="2">Dallas</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3-api.dal-us-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3-api.dal-us-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">San Jose</td>
        <td>public</td>
    <td><code class="highlighter-rouge">s3-api.sjc-us-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3-api.sjc-us-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Washington, DC</td>
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
    <td rowspan="2">Amsterdam</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3.ams-eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.ams-eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Frankfurt</td>
        <td>public</td>
    <td><code class="highlighter-rouge">s3.fra-eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3.fra-eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
  <tr>
    <td rowspan="2">Milan</td>
    <td>public</td>
    <td><code class="highlighter-rouge">s3.mil-eu-geo.objectstorage.softlayer.net</code></td>
  </tr>
  <tr>
    <td>private</td>
    <td><code class="highlighter-rouge">s3pi.mil-eu-geo.objectstorage.service.networklayer.com</code></td>
  </tr>
</table>
{:.endpointtable}
