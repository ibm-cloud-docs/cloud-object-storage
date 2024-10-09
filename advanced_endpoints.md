---

copyright:
  years: 2017, 2023
lastupdated: "2023-08-09"

keywords: object storage, endpoints, access points, manual failover

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

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
| AP: Sydney           | Public (Tethered) | `s3.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka            | Public (Tethered) | `s3.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Public Endpoints" caption-side="top"}
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
| AP: Sydney           | Private (Tethered) | `s3.private.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka        | Private (Tethered) | `s3.private.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Private Endpoints" caption-side="top"}
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
| AP: Sydney           | Direct (Tethered) | `s3.direct.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka            | Direct (Tethered) | `s3.direct.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Direct Endpoints" caption-side="top"}
{: #tether3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints"}

## Hosted static website endpoint reference
{: #static-web-endpoint-reference}

| Region               | Hosted Static Website Endpoint                       |
|----------------------|------------------------------------------------------|
| US: Dallas           | `s3-web.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | `s3-web.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | `s3-web.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | `s3-web.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | `s3-web.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | `s3-web.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | `s3-web.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Sydney           | `s3-web.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka        | `s3-web.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Static Web Public Endpoints" caption-side="top"}
{: #swtether1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints-tether"}

| Region               | Hosted Static Website Endpoint                               |
|----------------------|--------------------------------------------------------------|
| US: Dallas           | `s3-web.private.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | `s3-web.private.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | `s3-web.private.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | `s3-web.private.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | `s3-web.private.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | `s3-web.private.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | `s3-web.private.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Sydney            | `s3-web.private.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka        | `s3-web.private.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Static Web Private Endpoints" caption-side="top"}
{: #swtether2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints-tether"}

| Region               | Hosted Static Website Endpoint                              |
|----------------------|-------------------------------------------------------------|
| US: Dallas           | `s3-web.direct.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | `s3-web.direct.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | `s3-web.direct.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | `s3-web.direct.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | `s3-web.direct.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | `s3-web.direct.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | `s3-web.direct.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Sydney            | `s3-web.direct.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka        | `s3-web.direct.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Static Web Direct Endpoints" caption-side="top"}
{: #swtether3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints-tether"}

## Next Steps
{: #advanced-endpoints-next-steps}

Different services and the features they support may vary region by region. Check the documentation for more information regarding [service availability](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).
