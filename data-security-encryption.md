---

copyright:
  years: 2017, 2024
lastupdated: "2024-04-17"

keywords: encryption, security, object storage

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Data security
{: #security}

{{site.data.keyword.cos_full}} uses an innovative approach for cost-effectively storing large volumes of unstructured data that ensures security, availability, and reliability.
{: shortdesc}

This level of security is accomplished by using Information Dispersal Algorithms (IDA) to separate data into unrecognizable “slices”. The system distributes these slices across a network of data centers, making transmission and storage of data inherently private and secure. No complete copy of the data resides in any single storage node, and only a subset of nodes needs to be available to fully retrieve the data on the network.

All data in {{site.data.keyword.cos_full_notm}} is encrypted at rest. This technology individually encrypts each object by using per-object generated keys. These keys are secured and reliably stored by using the same Information Dispersal Algorithms that protect object data by using an All-or-Nothing Transform (AONT). Key data is impossible to recover, even if individual nodes or hard disks are compromised.

If it's necessary for a user to control encryption keys, root keys can be provided on a [per-object basis that uses SSE-C](/docs/cloud-object-storage?topic=cloud-object-storage-sse-c), or a [per-bucket basis that uses SSE-KP](/docs/cloud-object-storage?topic=cloud-object-storage-kp).

Storage can be accessed over HTTPS, and internally storage devices communicate with each other using TLS.

## Credential and encryption key rotation
{: #security-rotation}

Credentials, such as HMAC and API keys, do not naturally expire.  Over time, it is possible that employee turnover or an accidental mishandling of information can result in unintended or unwanted access to cloud resources.  Following a rotation schedule can help to prevent this scenario.  Read more about [rotation of encryption keys](/docs/cloud-object-storage?topic=cloud-object-storage-kp#kp-lifecycle) and [access credentials](/docs/secrets-manager?topic=secrets-manager-manual-rotation&interface=ui).

## Access Control Lists
{: #security-acl}

Access control lists (often referred to as ACLs) are an outdated method for controlling access to object storage resources.  Some APIs exist for setting individual objects to a `public-read` status, but this is discouraged in favor of [using IAM to allow unauthenticated access to an entire bucket](/docs/cloud-object-storage?topic=cloud-object-storage-iam-public-access), and using these buckets to serve any open data.

## Data deletion
{: #security-deletion}

{{site.data.keyword.cos_full_notm}} data is erasure coded and distributed to multiple individual storage devices in multiple data centers. When data is deleted, various mechanisms exist which prevent recovery or reconstruction of the deleted objects. Deletion of an object undergoes various stages. First, the metadata is marked to indicate the object is deleted, then, the data is removed. Eventually, deleted metadata is overwritten by a process of compaction and the deleted data blocks are overwritten with new data in the course of normal operations. As soon as the metadata is marked deleted, it is impossible to read an object remotely. IBM's provider-managed encryption and erasure coding prevents data (both before and after deletion) from being accessible from within individual data centers.

Cross regional and regional resiliency buckets distribute information across multiple data centers.  For single site resiliency, data is dispersed to the same number of storage devices but they are all located in the same data center.
{: note}

Data can be made more secure by using one of several available methods to protect the encryption keys including SSE-C, Key Protect or Hyper Protect Crypto Services. Please visit the [manage encryption](/docs/cloud-object-storage/basics?topic=cloud-object-storage-encryption) topic to learn more about the encryption methods.

## Tenant isolation
{: #security-isolation}

{{site.data.keyword.cos_full_notm}} is a multi-tenant Object Storage product. If your workload requires dedicated or isolated storage, see [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) for more information.
