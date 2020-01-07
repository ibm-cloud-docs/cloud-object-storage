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

When using a Cross Region endpoint, it is possible to direct inbound traffic to a specific access point while still distributing data across all three regions. When sending requests to an individual access point there is no automated failover if that region becomes unavailable. Applications that direct traffic to an access point instead of the `geo` endpoint **must** implement appropriate failover logic internally to achieve the availability advantages of the cross-region storage.
{:tip}

Some workloads may benefit from using a Single Data Center endpoint. Data stored in a single site is still distributed across many physical storage appliances, but is contained to a single data center. This can improve performance for compute resources within the same site, but will not maintain availability in the case of a site outage. Single Data Center buckets do not provide automated replication or backup in the case of site destruction, so any applications using a single site should consider disaster recovery in their design.

All requests must use SSL when using IAM, and the service will reject any plaintext requests.

## Endpoint Types
{ #advanced-endpoint-types}

{{site.data.keyword.cloud}} services are connected to a three-tiered network, segmenting public, private, and management traffic.

* **Private endpoints** are available for requests originating from Kubernetes clusters, bare metal servers, virtual servers, and other cloud storage services. Private endpoints provide better performance and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. **Whenever possible, it is best to use a private endpoint.**
* **Public endpoints** can accept requests from anywhere and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Public endpoints should be used for access not originating from an {{site.data.keyword.cloud_notm}} cloud computing resource. 
* **Direct endpoints** can accept requests from within the VPC and charges are assessed on outgoing bandwidth. Incoming bandwidth is free. Use direct endpoints to connect from a VPC to Cloud Object Storage. Directions for connecting to {{site.data.keyword.cos_full_notm}} from VPC are available [here](https://cloud.ibm.com/docs/vpc-on-classic?topic=vpc-on-classic-connecting-to-ibm-cloud-object-storage-from-a-vpc).

Requests must be sent to the endpoint associated with a given bucket's location. If you aren't sure where a bucket is located, there is an [extension to the bucket listing API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) that returns the location and storage class information for all buckets in a service instance.

As of December 2018, we have updated our endpoints. Legacy endpoints will continue to work until further notice. Please update your applications to use the [new endpoints &lpar;JSON&rpar;](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints){: external}.
{:note}
