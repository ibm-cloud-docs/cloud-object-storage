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

# Client-side bottlenecks
{: #performance-bottlenecks}

Often, poor performance is investigated and there is no indication of any lag or bottlenecks on the server side. These issues are resolved by making improvements to other aspects of the application architecture.  
{: shortdesc}

## Application design
{: #performance-bottlenecks-design}

Reading and writing data to an object store can use significant resources in order to facilitate integrity checks, especially for applications that are transferring large objects.  It is important to ensure that the application design takes into account any potential bottlenecks on CPU, disk IO, memory, and network use in order to minimize any performance impact.

## Compute resource power
{: #performance-bottlenecks-compute}

Containers, virtual machines, or bare metal must have sufficient power for the application to run efficiently.  No amount of careful software design and engineering can overcome an underpowered computing environment, and optimal performance is dependent on adequate resources.  

## Network
{: #performance-bottlenecks-network}

NIC speeds can determine the limits of data throughput. It is possible to configure a virtual 10GB NIC, but there are limitations on how many transactions and threads a virtualized NIC can effectively perform and manage. Due to the countless potential configurations and types of virtualized NICs, it isn't possible to provide precise guidance. It is recommended to experiment with a rational amount of transactions to COS on a single virtualized NIC, perhaps 25-50 at first. Then test again, moving up or down in the number to try and find the proper balance and to provide the best performance on a consistent basis.

If encountering network errors, it is useful to provide the specific endpoint where requests are being sent when opening a support ticket. This allows the support team to efficiently investigate the networking to see if there are errors on IBM's network hops.

For very large migrations from an on-premise system to the IBM Cloud, the Mass Data Migration Service (MDMS) is recommended. For more information, see the [MDMS documentation](/docs/mass-data-migration?topic=mass-data-migration-getting-started-tutorial).