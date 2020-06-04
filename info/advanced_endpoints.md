---

copyright:
  years: 2017, 2020
lastupdated: "2020-06-04"

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
* **Direct endpoints** are used in Bring-Your-Own-IP scenarios, generally for requests originating from resources within VPCs. Like Private endpoints, Direct endpoints provide better performance over Public endpoints and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. Directions for connecting to {{site.data.keyword.cos_full_notm}} from VPC are available [here](/docs/vpc-on-classic?topic=vpc-on-classic-connecting-to-ibm-cloud-object-storage-from-a-vpc).

Requests must be sent to the endpoint associated with a given bucket's location. If you aren't sure where a bucket is located, there is an [extension to the bucket listing API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) that returns the location and storage class information for all buckets in a service instance.

As of December 2018, we have updated our endpoints. Legacy endpoints will continue to work until further notice. Please update your applications to use the [new endpoints &lpar;JSON&rpar;](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints){: external}.
{:note}

## Using tethered endpoints
{: #advanced-endpoint-using-cre}

When accessing a Cross Region bucket, it is possible to direct inbound traffic to a tethered endpoint while still distributing data across all three regions. When sending requests to a tethered endpoint **there is no automated failover if that region becomes unavailable**. Applications that direct traffic to a tethered endpoint instead of the `geo` endpoint **must** implement appropriate failover logic internally to achieve the availability advantages of the cross region storage. 

One reason for using a tethered endpoint is to control where data ingress and egress occurs while still distributing the data across the widest possible area. Imagine an application running in the `us-south` region that wants to store data in a US cross-region bucket but wants to ensure that all read and write requests remain in the Dallas area:

1. The application creates a client using the `https://s3.private.dal.us.cloud-object-storage.appdomain.cloud` endpoint.
2. The {{site.data.keyword.cos_short}} service in Dallas suffers an outage.
3. The application detects a persistent failure trying to use the tethered endpoint.
4. The application recognizes the need to fail over to a different tethered endpoint, such as San Jose.
5. The application creates a new client using the `https://s3.private.sjc.us.cloud-object-storage.appdomain.cloud` endpoint.
6. Connectivity is resumed, and access can be re-routed to Dallas when service is restored.

When sending requests to a tethered endpoint there is no automated failover if that region becomes unavailable.
{: note}

For contrast, imagine another application using the normal US cross-region endpoint:

1. The application creates a client using the `https://s3.us.cloud-object-storage.appdomain.cloud` endpoint.
1. The {{site.data.keyword.cos_short}} service in Dallas suffers an outage.
2. All {{site.data.keyword.cos_short}} requests are automatically rerouted to San Jose or Washington until service is restored.

### Tethered endpoint reference
{: #advanced-endpoint-reference}

| Region               | Type              | Endpoint                                         |
|----------------------|-------------------|--------------------------------------------------|
| US: Dallas           | Public (Tethered) | `s3.us.dal.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Public (Tethered) | `s3.us.sjc.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Public (Tethered) | `s3.us.wdc.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Public (Tethered) | `s3.eu.ams.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Public (Tethered) | `s3.eu.fra.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Public (Tethered) | `s3.eu.mil.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Public (Tethered) | `s3.ap.tok.cloud-object-storage.appdomain.cloud` |
| AP: Seoul            | Public (Tethered) | `s3.ap.seo.cloud-object-storage.appdomain.cloud` |
| AP: Hong Kong        | Public (Tethered) | `s3.ap.hkg.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #tether1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints"}

| Region               | Type               | Endpoint                                                 |
|----------------------|--------------------|----------------------------------------------------------|
| US: Dallas           | Private (Tethered) | `s3.private.us.dal.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Private (Tethered) | `s3.private.us.sjc.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Private (Tethered) | `s3.private.us.wdc.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Private (Tethered) | `s3.private.eu.ams.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Private (Tethered) | `s3.private.eu.fra.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Private (Tethered) | `s3.private.eu.mil.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Private (Tethered) | `s3.private.ap.tok.cloud-object-storage.appdomain.cloud` |
| AP: Seoul            | Private (Tethered) | `s3.private.ap.seo.cloud-object-storage.appdomain.cloud` |
| AP: Hong Kong        | Private (Tethered) | `s3.private.ap.hkg.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #tether2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints"}

| Region               | Type              | Endpoint                                                 |
|----------------------|-------------------|----------------------------------------------------------|
| US: Dallas           | Direct (Tethered) | `s3.private.us.dal.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Direct (Tethered) | `s3.private.us.sjc.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Direct (Tethered) | `s3.private.us.wdc.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Direct (Tethered) | `s3.private.eu.ams.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Direct (Tethered) | `s3.private.eu.fra.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Direct (Tethered) | `s3.private.eu.mil.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Direct (Tethered) | `s3.private.ap.tok.cloud-object-storage.appdomain.cloud` |
| AP: Seoul            | Direct (Tethered) | `s3.private.ap.seo.cloud-object-storage.appdomain.cloud` |
| AP: Hong Kong        | Direct (Tethered) | `s3.private.ap.hkg.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #tether3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints"}

## Next Steps
{: #advanced-endpoints-next-steps}

Different services and the features they support may vary region by region. Check the documentation for more information regarding [service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).
