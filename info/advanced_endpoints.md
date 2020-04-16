---

copyright:
  years: 2017, 2019
lastupdated: "2019-12-04"

keywords: object storage, endpoints, access points, manual failover

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
{:download: .download} 
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Additional endpoint information
{: #advanced-endpoints}

When deciding how to configure your {{site.data.keyword.cos_full}} instance, consider how the endpoints reflect your needs for resiliency and access.
{: shortdesc}

A bucket's resiliency is defined by the endpoint used to create it. _Cross Region_ resiliency will spread your data across several metropolitan areas, while _Regional_ resiliency will spread data across a single metropolitan area. _Single Data Center_ resiliency spreads data across multiple appliances within a single data center. Regional and Cross Region buckets can maintain availability during a site outage.

Compute workloads co-located with a Regional {{site.data.keyword.cos_short}} endpoint will see lower latency and better performance. For workloads not concentrated in a single geographic area, a Cross Region `geo` endpoint routes connections to the nearest regional data centers.

Some workloads may benefit from using a Single Data Center endpoint. Data stored in a single site is still distributed across many physical storage appliances, but is contained to a single data center. This can improve performance for compute resources within the same site, but will not maintain availability in the case of a site outage. Single Data Center buckets do not provide automated replication or backup in the case of site destruction, so any applications using a single site should consider disaster recovery in their design.

All requests must use SSL when using IAM, and the service will reject any plaintext requests.

## Endpoint Types
{: #advanced-endpoint-types}

{{site.data.keyword.cloud}} services are connected to a three-tiered network, segmenting public, private, and management traffic.

* **Private endpoints** are available for requests originating from Kubernetes clusters, bare metal servers, virtual servers, and other cloud storage services. Private endpoints provide better performance and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. **Whenever possible, it is best to use a private endpoint.**
* **Public endpoints** can accept requests from anywhere and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Public endpoints should be used for access not originating from an {{site.data.keyword.cloud_notm}} cloud computing resource. 
* **Direct endpoints** can accept requests from within the VPC and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Use direct endpoints to connect from a VPC to Cloud Object Storage. Directions for connecting to {{site.data.keyword.cos_full_notm}} from VPC are available [here](https://cloud.ibm.com/docs/vpc-on-classic?topic=vpc-on-classic-connecting-to-ibm-cloud-object-storage-from-a-vpc).

Requests must be sent to the endpoint associated with a given bucket's location. If you aren't sure where a bucket is located, there is an [extension to the bucket listing API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) that returns the location and storage class information for all buckets in a service instance.

As of December 2018, we have updated our endpoints. Legacy endpoints will continue to work until further notice. Please update your applications to use the [new endpoints &lpar;JSON&rpar;](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints){: external}.
{:note}

## Using cross-region access points
{: #advanced-endpoint-using-cre}

When using a Cross Region endpoint, it is possible to direct inbound traffic to a specific access point while still distributing data across all three regions. When sending requests to an individual access point **there is no automated failover if that region becomes unavailable**. Applications that direct traffic to an access point instead of the `geo` endpoint **must** implement appropriate failover logic internally to achieve the availabity advantages of the cross-region storage. 

One reason for using an access point is to control where data ingress and egress occurs while still distributing the data across the widest possible area. Imagine an application running in the `us-south` region that wants to store data in a US cross-region bucket but wants to ensure that all read and write requests remain in the Dallas area:

1. The application creates a client using the `https://s3.private.dal.us.cloud-object-storage.appdomain.cloud` endpoint.
2. The {{site.data.keyword.cos_short}} service in Dallas suffers an outage.
3. The application detects a persistent failure trying to use the access point.
4. The application recognizes the need to fail over to a different access point, such as San Jose.
5. The application creates a new client using the `https://s3.private.sjc.us.cloud-object-storage.appdomain.cloud` endpoint.
6. Connectivity is resumed, and access can be re-routed to Dallas when service is restored.

When sending requests to an individual access point there is no automated failover if that region becomes unavailable.
{: note}

For contrast, imagine another application using the normal US cross-region endpoint:

1. The application creates a client using the `https://s3.us.cloud-object-storage.appdomain.cloud` endpoint.
1. The {{site.data.keyword.cos_short}} service in Dallas suffers an outage.
2. All {{site.data.keyword.cos_short}} requests are automatically rerouted to San Jose or Washington until service is restored.

## Endpoint reference
{: #advanced-endpoint-reference}

Direct endpoints can be accessed using the domains referenced in Table 1. Choose the type of resiliency and seek the endpoint location suitable for your needs. 

| Region | Type | Endpoint |
|-----|-----|-----|
| US South | Direct | `s3.direct.us-south.cloud-object-storage.appdomain.cloud` |
| US East | Direct | `s3.direct.us-east.cloud-object-storage.appdomain.cloud` |
| EU United Kingdom | Direct | `s3.direct.eu-gb.cloud-object-storage.appdomain.cloud` |
| EU Germany | Direct | `s3.direct.eu-de.cloud-object-storage.appdomain.cloud` |
| AP Australia | Direct | `s3.direct.au-syd.cloud-object-storage.appdomain.cloud` |
| AP Japan | Direct | `s3.direct.jp-tok.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 1. Direct Endpoints" caption-side="top"}
{: #directendpointtable1}
{: tab-title="Region"}
{: tab-group="direct-endpoints"}

| Location | Type | Endpoint |
|-----|-----|-----|
| US Cross Region | Direct | `s3.direct.us.cloud-object-storage.appdomain.cloud` |
| Dallas | Direct | `s3.direct.dal.us.cloud-object-storage.appdomain.cloud` |
| Washington | Direct | `s3.direct.wdc.us.cloud-object-storage.appdomain.cloud` |
| San Jose | Direct | `s3.direct.sjc.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | Direct | `s3.direct.eu.cloud-object-storage.appdomain.cloud` |
| Amsterdam | Direct | `s3.direct.ams.eu.cloud-object-storage.appdomain.cloud` |
| Frankfurt | Direct | `s3.direct.fra.eu.cloud-object-storage.appdomain.cloud` |
| Milan | Direct | `s3.direct.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | Direct | `s3.direct.ap.cloud-object-storage.appdomain.cloud` |
| Tokyo | Direct | `s3.direct.tok.ap.cloud-object-storage.appdomain.cloud` |
| Seoul | Direct | `s3.direct.seo.ap.cloud-object-storage.appdomain.cloud` |
| Hong Kong S.A.R. of the PRC | Direct | `s3.direct.hkg.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 1. Direct Endpoints" caption-side="top"}
{: #directendpointtable2}
{: tab-title="Cross Region"}
{: tab-group="direct-endpoints"}

| Location | Type | Endpoint |
|-----|-----|-----|
| Amsterdam, Netherlands | Direct | `s3.direct.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India | Direct | `s3.direct.che01.cloud-object-storage.appdomain.cloud` |
| Hong Kong S.A.R. of the PRC | Direct | `s3.direct.hkg02.cloud-object-storage.appdomain.cloud` |
| Melbourne, Australia | Direct | `s3.direct.mel01.cloud-object-storage.appdomain.cloud` |
| Mexico City, Mexico | Direct | `s3.direct.mex01.cloud-object-storage.appdomain.cloud` |
| Milan, Italy | Direct | `s3.direct.mil01.cloud-object-storage.appdomain.cloud` |
| Montr&egrave;al, Canada | Direct | `s3.direct.mon01.cloud-object-storage.appdomain.cloud` |
| Oslo, Norway | Direct | `s3.direct.osl01.cloud-object-storage.appdomain.cloud` |
| Paris, France | Direct | `s3.direct.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US | Direct | `s3.direct.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil | Direct | `s3.direct.sao01.cloud-object-storage.appdomain.cloud` |
| Seoul, South Korea | Direct | `s3.direct.seo01.cloud-object-storage.appdomain.cloud` |
| Singapore | Direct | `s3.direct.sng01.cloud-object-storage.appdomain.cloud` |
| Toronto, Canada | Direct | `s3.direct.tor01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 1. Direct Endpoints" caption-side="top"}
{: #directendpointtable3}
{: tab-title="Single Data Center"}
{: tab-group="direct-endpoints"}

## Next Steps
{: #advanced-endpoints-next-steps}

Different services and the features they support may vary region by region. Check the documentation for more information regarding [service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).
