---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-30"

keywords: expiry, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:external: target="blank" .external}
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

{{site.data.keyword.cos_notm}} IAM Users must have the appropriate permission in order to add a lifecycle policy to a bucket. Currently, up to 1000 policies may be defined per bucket.

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

This operation does not make use of additional operation specific query parameters.

Header                    | Description | Required   
--------------------------|----------------------------------------------------------------------------------------------------------------------|--------
`Content-MD5` | <ul><li>string</li><li>The base64-encoded 128-bit MD5 digest of the message (without the headers) according to RFC 1864. This header is used as a message integrity check to verify that the data is the same data that was originally sent.   Content MD-5 is required to ensure integrity of the PUT Bucket?lifecycle request.<br /><br /></li><li>Default: None</li><li>Constraints: None</li></ul> | **Yes**

The body of the request must contain an XML block with the schema addressed in the table (see Example 1).


| Element                  | Description                 | Required
|--------------------------|----------------------|----------------------------------------
| `LifecycleConfiguration` |  <ul><li>Container for lifecycle rules.<li>Type: Container<li>Children: Rule<li>Ancestor: None<li>Constraint: Only one Lifecycle Configuration will be supported per bucket</ul> | Yes
| `Rule`                   |  <ul><li>Container for a lifecycle rule.  Up to 1000 rules will be supported.<li>Type: Conatainer<li>Children: Transition<li>Ancestor: LifecycleConfiguration<li>Constraint: Each Rule can contain only one Transition Action.  In addition, the entire policy can contain only one Transition Action.</ul> | Yes
| `ID`                     |  <ul><li>A Unique identifier for the lifecycle rule. The value cannot be longer than 255 characters. The ID can only consist of US Alpha Numeric Characters (a-z A-Z 0-9) and the following symbols:<li>*ID supported symbols* <br /> `! _ . * ' ( ) -`<li>Type: String<li>Children: None<li>Ancestor: Rule</ul> | No
| `Filter`                 |  <ul><li>A filter allows the rule to be only applied to the objects that meet the filter criteria.  If the filter is empty (<Filter></Filter> or <Filter/>, the rule applies to all objects in the bucket.<li>Type: String<li>Children:Prefix<li>Ancestor: Rule<li>Constraint: If the rule contains a Transition Action, only any empty filter specified via one of the following methods will be accepted:</ul><ol><li>&lt;Filter&gt;</Filter&gt; or <li>&lt;Filter/&gt;<li>&lt;Filter&gt;&lt;Prefix&gt;&lt;/Prefix&gt;&lt;/Filter&gt;<li>&lt;Filter&gt;&lt;Prefix/&gt;&lt;/Filter&gt;</ol> | Yes
| `Prefix`                 |  <ul><li>Object Key Prefix to which the rule should be applied.<li>Type: String<li>Children:None<li>Ancestor: Filter<li>Constraint: Only a single Prefix element will be supported per Rule.  If the Rule contains a Transition Action, then, only an empty prefix  <Prefix></Prefix> or <Prefix/>will be accepted.</ul> | No
| `Status`                 |  <ul><li>The lifecycle rule is executed if Status is set to Enabled.<li>Type: String<li>Children: None<li>Ancestor: Rule<li>Valid Values: Enabled, Disabled</ul> | Yes                 
| `Transition`             |  <ul><li>This action specifies a period from the creation of an object after which the object will be transitioned to the defined Storage Class.<li>Type: Container<li>Children: Days, Date, Storage Class<li>Ancestor: Rule<li>Constraint: Only one Transition rule will be supported per bucket</ul> | Yes, If Expiration Action is not included
| `StorageClass`           |  <ul><li>Specifies the Storage Class to which the Object will Transition<li>Type: String<li>Children: None<li>Ancestor: Transition<li>Valid Values: GLACIER</ul> | Yes, if Transition Action is included
| `Expiration`             |  <ul><li>This action specifies a period from the creation of an object after which the object will be deleted.<li>Type: Container<li>Children: Days, Date<li>Ancestor: Rule</ul> | Yes, if Transition Action is not included
| `Days`                   |  <ul><li>Specifies the number of days after the creation of an object when the specified rule takes effect.  A value of 0 for transition means that the rule takes place immediately after the Object is stored.  Expiration Action has to have the Days Value, if included, set to greater than 0.<li>Type: Non-negative Integer<li>Children: None<li>Ancestor: Transition or Expiration<li>Constraint: Value must be greater than or equal to 0</ul> | Yes, if Date is not included.
| `Date`                   |  <ul><li>Specifies the Date at which objects in the bucket will be transitioned to the specified storage class.  If an Object is added after the date has passed, the object is immediately transitioned to the specified storage class.<li>Type: Date<li>Children: None<li>Ancestor: Transition or Expiration<li>Constraint: Date must be in ISO 8601 Format;</ul> | Yes, if Days is not included.

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</status>
		<Transition>
			<Days>20</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Example 1. XML sample from the body of the request." caption-side="bottom"}

A successful response returns a `202` if the object is in the archived state and a `200` if the object is already in the restored state.  If the object is already in the restored state and a new request to restore the object is received, the `Days` element will update the expiration time of the restored object.


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

