---

copyright:
  years: 2020
lastupdated: "2020-03-25"

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

Object size can have significant impacts on {{site.data.keyword.cos_full}} performance. Choose the right approach for your workload.  
{: shortdesc}

## Application design
{: #performance-bottlenecks-design}

Avoiding bottlenecks on CPU, Disk IO , memory etc in the application design

## Compute resource power
{: #performance-bottlenecks-compute}

Ensuring that the client (bare metal, VM or container) is adequately powered to deliver optimal performance


## Network
{: #performance-bottlenecks-network}

Nic speed on customer side, although you may have a virtual 10gb nic there are limitations on how many transactions/threads a virtualized nic can effectively perform. As there are countless ways and types of virtualized nic's we can't provide hard and fast rules. We would recommend rational amount of transactions to COS on a single nic perhaps 25-50 and test and see what your vnic can support. Then test again moving up or down in the number to then try and find the proper balance to provide the best performance you can achieve of a consistent basis.

For huge migrations MDMS is our recommendation https://cloud.ibm.com/docs/Db2whc?topic=Db2whc-pda as an example.

Network hops and network errors, an mtr $endpoint_name is useful when opening a ticket to COS to allow them to follow up with networking to see if there are errors on IBM's network hops.