---

copyright:
  years: 2020
lastupdated: "2020-08-25"

keywords: developer, best practices, object storage

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

# Network topology 
{: #performance-topology}

There are many ways to connect to {{site.data.keyword.cos_full}} and the choice of endpoint can have an impact on performance.
{: shortdesc}

## Physical distance
{: #performance-topology-distance}

When an application makes a request to COS, it needs to cross some amount of physical distance.  As this distance increases, the latency of the request will also increase. In order to lessen the latency imposed by physical distance, it is optimal to co-locate compute resources and object storage where possible. If your application is running in the IBM Cloud in the `us-south` region, then in order to optimize performance it would be best to read and write data to a bucket also located in the `us-south` region. 

Workloads that require accessing data in far reaching places might benefit from using IBM Aspera, especially if there is significant packet loss.  More information about using IBM Aspera High-Speed Transfer and COS can be found in the Aspera guide.

Applications with global reach will benefit from using a Content Delivery Network to cache assets stored in COS in locations closer to their end users.  The original files continue to be hosted in their bucket, but copies can be cached in various locations around the world where users are originating requests. 

## Resilience requirements
{: #performance-topology-resilience}

Some workloads might require the additional levels of resiliency that comes with writing data to Cross Region buckets, while others might rely on the increased marginal performance found in a Single Data Center bucket.  Each application needs to strike a balance between higher availability and faster performance.  

When using a Cross Region endpoint, it is possible to direct inbound traffic to a specific access point while still dispersing data across all three regions. When sending requests to an individual access point **there is no automated failover if that region becomes unavailable**. Applications that direct traffic to an access point instead of the `geo` endpoint **must** implement appropriate failover logic internally to achieve the availability advantages of the cross-region storage. 

One reason for using an access point is to control where data ingress and egress occurs while still dispersing the data across the widest possible area. Imagine an application running in the `us-south` region that wants to store data in a US cross-region bucket but wants to ensure that all read and write requests remain in the Dallas area:

1. The application creates a client using the `https://s3.private.dal.us.cloud-object-storage.appdomain.cloud` endpoint.
2. The COS service in Dallas suffers an outage.
3. The application detects a persistent failure trying to use the access point.
4. The application recognizes the need to fail over to a different access point, such as San Jose.
5. The application creates a new client using the `https://s3.private.sjc.us.cloud-object-storage.appdomain.cloud` endpoint.
6. Connectivity is resumed, and access can be re-routed to Dallas when service is restored.

For contrast, imagine another application using the normal US cross-region endpoint:

1. The application creates a client using the `https://s3.us.cloud-object-storage.appdomain.cloud` endpoint.
1. The COS service in Dallas suffers an outage.
2. All COS requests are automatically rerouted to San Jose or Washington until service is restored.

## Network type
{: #performance-topology-network}

Traffic directed to COS can come from one of three networks: Public, Private, or Direct. The network that is targeted is defined by the COS service endpoint used to access a bucket. While a bucket is created in a single location (be that Cross Region, Regional, or Single Site) it is still possible to access that same bucket via any of the three network types described.

Public traffic traverses the public Internet until it reaches the IBM Cloud and is routed to a load balancer that  directs traffic into the COS distributed storage network. Private traffic originates within the IBM Cloud and never touches the public Internet.  Direct traffic originates in a Virtual Private Cloud that could contain both local data centers and IBM Cloud resources. This architecture requires IBM Direct Link, and allows users to connect directly to the Private IBM Cloud network from a user's data center (using a reverse proxy) without ever touching the public Internet. 

Because the Private network eliminates any variances, congestion, or vulnerabilities found in the Public Internet, it is recommended that all workloads use the Private network whenever possible.