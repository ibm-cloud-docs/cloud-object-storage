---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-23"

keywords: encryption, security, object storage

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
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

# Data security and encryption
{: #security}

{{site.data.keyword.cos_full}} uses an innovative approach for cost-effectively storing large volumes of unstructured data that ensures security, availability, and reliability. 
{: shortdesc}

This level of security is accomplished by using Information Dispersal Algorithms (IDAs) to separate data into unrecognizable “slices”. The system distributes these slices across a network of data centers, making transmission and storage of data inherently private and secure. No complete copy of the data resides in any single storage node, and only a subset of nodes needs to be available in order to fully retrieve the data on the network.

All data in {{site.data.keyword.cos_full_notm}} is encrypted at rest. This technology individually encrypts each object by using per-object generated keys. These keys are secured and reliably stored by using the same Information Dispersal Algorithms that protect object data by using an All-or-Nothing Transform (AONT). Key data is impossible to recover, even if individual nodes or hard disks are compromised.

If it's necessary for a user to control encryption keys, root keys can be provided on a [per-object basis that uses SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c), or a [per-bucket basis that uses SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp).

Storage can be accessed over HTTPS, and internally storage devices communicate with each other using TLS.


## Data deletion
{: #security-deletion}

When data is deleted, various mechanisms exist which prevent recovery or reconstruction of the deleted objects. Deletion of an object undergoes various stages. First, the object is marked with metadata indicating the object as deleted. Then, the content regions are removed, and the erasure on the drives themselves is completed. Eventually, the blocks are overwritten with new data. Depending on whether one compromised the data center or has possession of the physical disks, the time an object takes becomes unrecoverable depends on the phase of the delete operation. When the metadata object is updated, clients external from the data center network can no longer read the object. When most of slices are erased, it is not possible to access the object.

## Tenant isolation
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} is a multi-tenant Object Storage product. If your workload requires dedicated or isolated storage, see [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) for more information.
