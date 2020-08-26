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

# Using tethered endpoints
{: #advanced-endpoints}

When deciding how to configure your {{site.data.keyword.cos_full}} instance, consider how the endpoints reflect your needs for resiliency and access.
{: shortdesc}

When you use a Cross Region bucket, it is possible to direct your accesses to a tethered endpoint associated with a specific Cross Region metropolitan area, rather than connecting to the nearest available Cross Region metropolitan area.  In contrast to the `geo` endpoint, when you send requests to a tethered end point **there is no automated failover if that region becomes unavailable**. Applications that direct traffic to a tethered endpoint **must** implement appropriate failover logic internally to achieve the availability advantages of the Cross Region storage. 

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

## Tethered endpoint reference
{: #advanced-endpoint-reference}

| Region               | Type              | Endpoint                                         |
|----------------------|-------------------|--------------------------------------------------|
| US: Dallas           | Public (Tethered) | `s3.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Public (Tethered) | `s3.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Public (Tethered) | `s3.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Public (Tethered) | `s3.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Public (Tethered) | `s3.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Public (Tethered) | `s3.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Public (Tethered) | `s3.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Seoul            | Public (Tethered) | `s3.seo.ap.cloud-object-storage.appdomain.cloud` |
| AP: Hong Kong        | Public (Tethered) | `s3.hkg.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #tether1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints"}

| Region               | Type               | Endpoint                                                 |
|----------------------|--------------------|----------------------------------------------------------|
| US: Dallas           | Private (Tethered) | `s3.private.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Private (Tethered) | `s3.private.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Private (Tethered) | `s3.private.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Private (Tethered) | `s3.private.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Private (Tethered) | `s3.private.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Private (Tethered) | `s3.private.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Private (Tethered) | `s3.private.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Seoul            | Private (Tethered) | `s3.private.seo.ap.cloud-object-storage.appdomain.cloud` |
| AP: Hong Kong        | Private (Tethered) | `s3.private.hkg.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #tether2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints"}

| Region               | Type              | Endpoint                                                 |
|----------------------|-------------------|----------------------------------------------------------|
| US: Dallas           | Direct (Tethered) | `s3.direct.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Direct (Tethered) | `s3.direct.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Direct (Tethered) | `s3.direct.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Direct (Tethered) | `s3.direct.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Direct (Tethered) | `s3.direct.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Direct (Tethered) | `s3.direct.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Direct (Tethered) | `s3.direct.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Seoul            | Direct (Tethered) | `s3.direct.seo.ap.cloud-object-storage.appdomain.cloud` |
| AP: Hong Kong        | Direct (Tethered) | `s3.direct.hkg.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2. Cross Region Endpoints" caption-side="top"}
{: #tether3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints"}

## Next Steps
{: #advanced-endpoints-next-steps}

Different services and the features they support may vary region by region. Check the documentation for more information regarding [service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).
