---

copyright:
  years: 2017, 2022

lastupdated: "2022-01-28"

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:help: data-hd-content-type='help'}

# Endpoints and storage locations
{: #endpoints}

Sending a REST API request or configuring a storage client requires setting a target endpoint or URL. Each storage location has its own set of URLs.
{: shortdesc}

A bucket's resiliency is defined by the endpoint used to create it. _Cross Region_ resiliency will spread your data across several metropolitan areas, while _Regional_ resiliency will spread data across a single metropolitan area. _Single Data Center_ resiliency spreads data across multiple appliances within a single data center. Regional and Cross Region buckets can maintain availability during a site outage.

Compute workloads co-located with a Regional {{site.data.keyword.cos_short}} endpoint will see lower latency and better performance. For workloads requiring Cross Region resiliency, performance impacts are mitigated via `geo` endpoint routes connecting to the nearest Cross Region metropolitan area.

Some workloads may benefit from using a Single Data Center endpoint. Data stored in a single site is still distributed across many physical storage appliances, but is contained within a single data center. This can improve performance for compute resources within the same site, but will not maintain availability in the case of a site outage. Single Data Center buckets do not provide automated replication or backup in the case of site destruction, so any applications using a single site should consider disaster recovery in their design.

All requests must use SSL when using IAM, and the service will reject any plaintext requests.

All {{site.data.keyword.cos_full}} endpoints support TLS 1.2 encryption.
{: note}

## Endpoint Types
{: #advanced-endpoint-types}

{{site.data.keyword.cloud}} services are connected to a three-tiered network, segmenting public, private, and management traffic.

* **Private endpoints** are available for most requests originating from within IBM Cloud. Private endpoints provide better performance and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. **Whenever possible, it is best to use a private endpoint.**
* **Public endpoints** can accept requests from anywhere and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Public endpoints should be used for access not originating from an {{site.data.keyword.cloud_notm}} cloud computing resource. 
* **Direct endpoints** are used in Bring-Your-Own-IP scenarios, generally for requests originating from [resources within VPCs](/docs/vpc?topic=vpc-about-vpc). Like Private endpoints, Direct endpoints provide better performance over Public endpoints and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. Directions for connecting to {{site.data.keyword.cos_full_notm}} from VPC are available [here](/docs/vpc-on-classic?topic=vpc-on-classic-connecting-to-ibm-cloud-object-storage-from-a-vpc).

Requests must be sent to the endpoint associated with a given bucket's location. If you aren't sure where a bucket is located, there is an [extension to the bucket listing API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) that returns the location and storage class information for all buckets in a service instance.

When using Virtual Private Endpoints in an application that makes requests to IBM COS, it may be necessary to add some additional configuration for authentication. The IBM COS SDKs will automatically attempt to fetch an IAM token from `https://iam.cloud.ibm.com/identity/token`.  If you are using a virtualized endpoint for token acquisition you will need alter the IAM endpoint appropriately.
{: note}

## Regional Endpoints
{: #endpoints-region}

Buckets that are created at a regional endpoint distribute data across three data centers that are spread across a metro area. Any one of these data centers can suffer an outage or even destruction without impacting availability.

| Region     | Type   | Endpoint                                           |
| ---------- | ------ | -------------------------------------------------- |
| `us-south` | Public | `s3.us-south.cloud-object-storage.appdomain.cloud` |
| `us-east`  | Public | `s3.us-east.cloud-object-storage.appdomain.cloud`  |
| `eu-gb`    | Public | `s3.eu-gb.cloud-object-storage.appdomain.cloud`    |
| `eu-de`    | Public | `s3.eu-de.cloud-object-storage.appdomain.cloud`    |
| `au-syd`   | Public | `s3.au-syd.cloud-object-storage.appdomain.cloud`   |
| `jp-tok`   | Public | `s3.jp-tok.cloud-object-storage.appdomain.cloud`   |
| `jp-osa`   | Public | `s3.jp-osa.cloud-object-storage.appdomain.cloud`   |
| `ca-tor`   | Public | `s3.ca-tor.cloud-object-storage.appdomain.cloud`   |
| `br-sao`   | Public | `s3.br-sao.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Table 1. Regional Endpoints" caption-side="top"}
{: #regionalendpointtable1}
{: tab-title="Public"}
{: tab-group="Regional-endpoints"}

| Region     | Type    | Endpoint                                                   |
| ---------- | ------- | ---------------------------------------------------------- |
| `us-south` | Private | `s3.private.us-south.cloud-object-storage.appdomain.cloud` |
| `us-east`  | Private | `s3.private.us-east.cloud-object-storage.appdomain.cloud`  |
| `eu-gb`    | Private | `s3.private.eu-gb.cloud-object-storage.appdomain.cloud`    |
| `eu-de`    | Private | `s3.private.eu-de.cloud-object-storage.appdomain.cloud`    |
| `au-syd`   | Private | `s3.private.au-syd.cloud-object-storage.appdomain.cloud`   |
| `jp-tok`   | Private | `s3.private.jp-tok.cloud-object-storage.appdomain.cloud`   |
| `jp-osa`   | Private | `s3.private.jp-osa.cloud-object-storage.appdomain.cloud`   |
| `ca-tor`   | Private | `s3.private.ca-tor.cloud-object-storage.appdomain.cloud`   |
| `br-sao`   | Private | `s3.private.br-sao.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Table 1. Regional Endpoints" caption-side="top"}
{: #regionalendpointtable2}
{: tab-title="Private"}
{: tab-group="Regional-endpoints"}

| Region     | Type   | Endpoint                                                  |
| ---------- | ------ | --------------------------------------------------------- |
| `us-south` | Direct | `s3.direct.us-south.cloud-object-storage.appdomain.cloud` |
| `us-east`  | Direct | `s3.direct.us-east.cloud-object-storage.appdomain.cloud`  |
| `eu-gb`    | Direct | `s3.direct.eu-gb.cloud-object-storage.appdomain.cloud`    |
| `eu-de`    | Direct | `s3.direct.eu-de.cloud-object-storage.appdomain.cloud`    |
| `au-syd`   | Direct | `s3.direct.au-syd.cloud-object-storage.appdomain.cloud`   |
| `jp-tok`   | Direct | `s3.direct.jp-tok.cloud-object-storage.appdomain.cloud`   |
| `jp-osa`   | Direct | `s3.direct.jp-osa.cloud-object-storage.appdomain.cloud`   |
| `ca-tor`   | Direct | `s3.direct.ca-tor.cloud-object-storage.appdomain.cloud`   |
| `br-sao`   | Direct | `s3.direct.br-sao.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Table 1. Regional Endpoints" caption-side="top"}
{: #regionalendpointtable3}
{: tab-title="Direct"}
{: tab-group="Regional-endpoints"}


## Cross Region Endpoints
{: #endpoints-geo}

Buckets that are created at a cross region endpoint distribute data across three regions. Any one of these regions can suffer an outage or even destruction without impacting availability. Requests are routed to the nearest Cross Region metropolitan area by using Border Gateway Protocol (BGP) routing. In an outage, requests are automatically rerouted to an active region. Advanced users who want to write their own failover logic can do so by sending requests to a [tethered endpoint](/docs/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) and bypassing the BGP routing.

| Region | Type   | Endpoint                                     |
| ------ | ------ | -------------------------------------------- |
| `us`   | Public | `s3.us.cloud-object-storage.appdomain.cloud` |
| `eu`   | Public | `s3.eu.cloud-object-storage.appdomain.cloud` |
| `ap`   | Public | `s3.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #crossregionalendpointtable1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints"}

| Region | Type    | Endpoint                                             |
| ------ | ------- | ---------------------------------------------------- |
| `us`   | Private | `s3.private.us.cloud-object-storage.appdomain.cloud` |
| `eu`   | Private | `s3.private.eu.cloud-object-storage.appdomain.cloud` |
| `ap`   | Private | `s3.private.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #crossregionalendpointtable2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints"}

| Region | Type   | Endpoint                                            |
| ------ | ------ | --------------------------------------------------- |
| `us`   | Direct | `s3.direct.us.cloud-object-storage.appdomain.cloud` |
| `eu`   | Direct | `s3.direct.eu.cloud-object-storage.appdomain.cloud` |
| `ap`   | Direct | `s3.direct.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #crossregionalendpointtable3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints"}

## Single Data Center Endpoints
{: #endpoints-zone}

Single data centers are not co-located with IBM Cloud services, such as IAM or Key Protect, and offer no resiliency in a site outage or destruction. 

If a networking failure results in a partition where the data center is unable to access IAM, authentication and authorization information is read from a cache that might become stale. This cached data might result in a lack of enforcement of new or altered IAM policies for up to 24 hours.
{: important}

| Region  | Type   | Endpoint                                        |
| ------- | ------ | ----------------------------------------------- |
| `ams03` | Public | `s3.ams03.cloud-object-storage.appdomain.cloud` |
| `che01` | Public | `s3.che01.cloud-object-storage.appdomain.cloud` |
| `hkg02` | Public | `s3.hkg02.cloud-object-storage.appdomain.cloud` |
| `mex01` | Public | `s3.mex01.cloud-object-storage.appdomain.cloud` |
| `mil01` | Public | `s3.mil01.cloud-object-storage.appdomain.cloud` |
| `mon01` | Public | `s3.mon01.cloud-object-storage.appdomain.cloud` |
| `par01` | Public | `s3.par01.cloud-object-storage.appdomain.cloud` |
| `sjc04` | Public | `s3.sjc04.cloud-object-storage.appdomain.cloud` |
| `seo01` | Public | `s3.seo01.cloud-object-storage.appdomain.cloud` |
| `sng01` | Public | `s3.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 3. Single Data Center Endpoints" caption-side="top"}
{: #sdcendpointtable1}
{: tab-title="Public"}
{: tab-group="single-datacenter-endpoints"}

| Region  | Type    | Endpoint                                                |
| ------- | ------- | ------------------------------------------------------- |
| `ams03` | Private | `s3.private.ams03.cloud-object-storage.appdomain.cloud` |
| `che01` | Private | `s3.private.che01.cloud-object-storage.appdomain.cloud` |
| `hkg02` | Private | `s3.private.hkg02.cloud-object-storage.appdomain.cloud` |
| `mex01` | Private | `s3.private.mex01.cloud-object-storage.appdomain.cloud` |
| `mil01` | Private | `s3.private.mil01.cloud-object-storage.appdomain.cloud` |
| `mon01` | Private | `s3.private.mon01.cloud-object-storage.appdomain.cloud` |
| `par01` | Private | `s3.private.par01.cloud-object-storage.appdomain.cloud` |
| `sjc04` | Private | `s3.private.sjc04.cloud-object-storage.appdomain.cloud` |
| `sjc01` | Private | `s3.private.sjc04.cloud-object-storage.appdomain.cloud` |
| `seo01` | Private | `s3.private.seo01.cloud-object-storage.appdomain.cloud` |
| `sng01` | Private | `s3.private.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 3. Single Data Center Endpoints" caption-side="top"}
{: #sdcendpointtable2}
{: tab-title="Private"}
{: tab-group="single-datacenter-endpoints"}

| Region  | Type   | Endpoint                                               |
| ------- | ------ | ------------------------------------------------------ |
| `ams03` | Direct | `s3.direct.ams03.cloud-object-storage.appdomain.cloud` |
| `che01` | Direct | `s3.direct.che01.cloud-object-storage.appdomain.cloud` |
| `hkg02` | Direct | `s3.direct.hkg02.cloud-object-storage.appdomain.cloud` |
| `mex01` | Direct | `s3.direct.mex01.cloud-object-storage.appdomain.cloud` |
| `mil01` | Direct | `s3.direct.mil01.cloud-object-storage.appdomain.cloud` |
| `mon01` | Direct | `s3.direct.mon01.cloud-object-storage.appdomain.cloud` |
| `par01` | Direct | `s3.direct.par01.cloud-object-storage.appdomain.cloud` |
| `sjc04` | Direct | `s3.direct.sjc04.cloud-object-storage.appdomain.cloud` |
| `seo01` | Direct | `s3.direct.seo01.cloud-object-storage.appdomain.cloud` |
| `sng01` | Direct | `s3.direct.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 3. Single Data Center Endpoints" caption-side="top"}
{: #sdcendpointtable3}
{: tab-title="Direct"}
{: tab-group="single-datacenter-endpoints"}

## EU-Managed Endpoints
{: #endpoints-eu-managed}

The {{site.data.keyword.at_full_notm}} can archive to a bucket at specific {{site.data.keyword.cos_full_notm}} instances. This table shows the EU-Managed locations of {{site.data.keyword.cos_short}} instances for archiving events.

| {{site.data.keyword.cos_short}} bucket location | Resiliency   | City                        |
| ----------------------------------------------- | ------------ | --------------------------- |
| `ams03`                                         | Single Site  | Amsterdam                   |
| `eu-de`                                         | Regional     | Frankfurt                   |
| `eu-gb`                                         | Regional     | London                      |
| `mil01`                                         | Single Site  | Milan                       |
| `par01`                                         | Single Site  | Paris                       |
| `eu-geo`                                        | Cross Region | Amsterdam, Frankfurt, Milan |
{: caption="Table 4. EU-managed Endpoints" caption-side="top"}
 

## Resource Configuration Endpoints
{: #endpoints-rc}

Requests made using the Resource Configuration API are sent to a global endpoint, regardless of the bucket's location.  

| Type    | Endpoint                                               |
| ------- | ------------------------------------------------------ |
| Public  | `config.cloud-object-storage.cloud.ibm.com/v1`         |
| Private | `config.private.cloud-object-storage.cloud.ibm.com/v1` |
| Direct  | `config.direct.cloud-object-storage.cloud.ibm.com/v1`  |

## Decommissioned locations
{: #endpoints-decom}

Over time, it may be necessary for locations to transform from a Single Data Center to a Regional configuration, or for a location to be decommissioned entirely. These situations will require users to migrate data from one bucket to another. Please consult this [guide for migrating a bucket using Rclone](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-region-copy).

| Region  | Type    | Endpoint                                                |
| ------- | ------- | ------------------------------------------------------- |
| `mel01` | Public  | `s3.mel01.cloud-object-storage.appdomain.cloud`         |
| `mel01` | Private | `s3.private.mel01.cloud-object-storage.appdomain.cloud` |
| `mel01` | Direct  | `s3.direct.mel01.cloud-object-storage.appdomain.cloud`  |
| `tor01` | Public  | `s3.tor01.cloud-object-storage.appdomain.cloud`         |
| `tor01` | Private | `s3.private.tor01.cloud-object-storage.appdomain.cloud` |
| `tor01` | Direct  | `s3.direct.tor01.cloud-object-storage.appdomain.cloud`  |
| `osl01` | Public  | `s3.direct.osl01.cloud-object-storage.appdomain.cloud`  |
| `osl01` | Private | `s3.direct.osl01.cloud-object-storage.appdomain.cloud`  |
| `osl01` | Direct  | `s3.direct.osl01.cloud-object-storage.appdomain.cloud`  |
{: caption="Table 5. Decommissioned Endpoints" caption-side="top"}
