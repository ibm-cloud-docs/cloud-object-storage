---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Object expiry
{: #expiry}

You can set the lifecycle for objects using the web console, REST API, and 3rd party tools that are integrated with {{site.data.keyword.cos_full_notm}}. 

For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Add or manage an expiration policy
{: #expiry-add}

When creating or modifying an expiry lifecycle policy for an object, consider the following:

* An expiry policy can be added to a new or existing bucket at any time. 
* An existing expiry policy can be modified or disabled. 
* A newly added or modified expiry policy applies to new objects uploaded and does not affect existing objects.

## REST API Reference
{: #expiry-api}

### Create a bucket lifecycle configuration
{: #expiry-api-create}

This implementation of the `PUT` operation uses the `lifecycle` query parameter to set lifecycle settings for the bucket. This operation allows for a single lifecycle policy definition for a given bucket. The policy is defined as a rule consisting of the following parameters: `ID`, `Status`, and `Transition`.

The transition action enables future objects written to the bucket to an archived state after a defined period of time. Changes to the lifecycle policy for a bucket are **only applied to new objects** written to that bucket.

Cloud IAM users must have the `Writer` role to add a lifecycle policy to the bucket.


---

### Retrieve a bucket expiry configuration
{: #expiry-api-retrieve}

This implementation of the `GET` operation uses the `lifecycle` query parameter to retrieve the lifecycle settings for the bucket. 

Cloud IAM users must have at minimum the `Reader` role to retrieve a lifecycle for a bucket.


---

### Delete a bucket expiry configuration
{: #expiry-api-delete}

This implementation of the `DELETE` operation uses the `lifecycle` query parameter to remove any lifecycle settings for the bucket. Transitions defined by the rules will no longer take place for new objects. 

**Note:** Existing transition rules will be maintained for objects that were already written to the bucket before the rules were deleted.

Cloud IAM users must have the `Writer` role to remove a lifecycle policy from a bucket.

