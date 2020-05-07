---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: encryption, security, object storage

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

# Data security
{: #security}

{{site.data.keyword.cos_full}} uses an innovative approach for cost-effectively storing large volumes of unstructured data that ensures security, availability, and reliability. 
{: shortdesc}

This level of security is accomplished by using Information Dispersal Algorithms (IDAs) to separate data into unrecognizable “slices”. The system distributes these slices across a network of data centers, making transmission and storage of data inherently private and secure. No complete copy of the data resides in any single storage node, and only a subset of nodes needs to be available in order to fully retrieve the data on the network.

All data in {{site.data.keyword.cos_full_notm}} is encrypted at rest. This technology individually encrypts each object by using per-object generated keys. These keys are secured and reliably stored by using the same Information Dispersal Algorithms that protect object data by using an All-or-Nothing Transform (AONT). Key data is impossible to recover, even if individual nodes or hard disks are compromised.

If it's necessary for a user to control encryption keys, root keys can be provided on a [per-object basis that uses SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c), or a [per-bucket basis that uses SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp).

Storage can be accessed over HTTPS, and internally storage devices communicate with each other using TLS.


## Data deletion
{: #security-deletion}

{{site.data.keyword.cos_full_notm}} data is erasure coded and distributed to multiple individual storage devices in multiple data centers. When data is deleted, various mechanisms exist which prevent recovery or reconstruction of the deleted objects. Deletion of an object undergoes various stages. First, the metadata is marked to indicate the object is deleted, then, the data is removed. Eventually, deleted metadata is overwritten by a process of compaction and the deleted data blocks are overwritten with new data in the course of normal operations. As soon as the metadata is marked deleted, it is not possible to read an object remotely. IBM's provider-managed encryption and erasure coding prevents data (both before and after deletion) from being accessible from within individual data centers.

Cross regional and regional resiliency buckets distribute information across multiple data centers.  For single site resiliency, data is dispersed to the same number of storage devices but they are all located in the same data center.
{: note}

Data can be made more secure by using one of several available methods to protect the encryption keys including SSE-C, Key Protect or Hyper Protect Crypto Services. Please visit the [manage encryption](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-encryption) topic to learn more about the encryption methods. 



## Tenant isolation
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} is a multi-tenant Object Storage product. If your workload requires dedicated or isolated storage, see [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) for more information.
