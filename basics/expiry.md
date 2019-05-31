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
{:http: .ph data-hd-programlang='http'} 

# Delete stale data with expiration rules
{: #expiry}

You can set the lifecycle for objects using the web console, REST API, and 3rd party tools that are integrated with {{site.data.keyword.cos_full_notm}}. 

{{site.data.keyword.cos_notm}} IAM Users must have the appropriate permission in order to add a lifecycle policy to a bucket. Currently, up to 1000 policies may be defined per bucket.

For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

When creating or modifying lifecycle configurations, consider the following:

* A lifecycle configuration can be added to a new or existing bucket at any time.
* An existing lifecycle configuration can be modified or disabled.
* A newly added or modified lifecycle configuration applies to new objects uploaded and does not affect existing objects.

### Add an expiration rule to a bucket’s lifecycle configuration
{: #expiry-api-create}

This implementation of the `PUT` operation uses the `lifecycle` query parameter to set lifecycle settings for the bucket. This operation allows for a single lifecycle policy definition for a given bucket. The policy is defined as a rule consisting of the following parameters: `ID`, `Status`, and `Transition`.
{: http}

An expiration rule deletes objects after a defined period of time. Changes to the lifecycle policy for a bucket are **only applied to new objects** written to that bucket.

Cloud IAM users must have the `Writer` role to add a lifecycle policy to the bucket. Note how Example 2 differentiates between different styles of notating endpoints.

Header                    | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | string | **Required**: The base64 encoded 128-bit MD5 hash of the payload, used as an integrity check to ensure the payload was not altered in transit.
{: http}

The body of the request must contain an XML block with the following schema:
{: http}

| Element                  | Type                 | Children                               | Ancestor                 | Constraint                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | None                     | Limit 1.                                                                                  |
| `Rule`                   | Container            | `ID`, `Status`, `Filter`, `Expiration` | `LifecycleConfiguration` | Limit 1000.                                                                                  |
| `ID`                     | String               | None                                   | `Rule`                   | Must consist of (`a-z,`A-Z0-9`) and the following symbols: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | String               | `Prefix`                               | `Rule`                   | Must contain a `Prefix` element                                                            |
| `Prefix`                 | String               | None                                   | `Filter`                 | The rule applies to any objects with keys containing this prefix.                                                           |
| `Expiration`             | `Container`          | `Days` | `Date`                        | `Rule`                   | Limit 1.                                                                                  |
| `Days`                   | Non-negative integer | None                                   | `Transition`             | Must be a value greater than 0.                                                           |
| `Date`                   | Date                 | None                                   | `Transistion`            | Must be in ISO 8601 Format and the date must be in the future.                            |
{: http}

The body of the request must contain an XML block with the schema addressed in the table (see Example 1).
{: http}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>id1</ID>
		<Filter />
		<Status>Enabled</status>
		<Expiration>
			<Days>60</Days>
		</Expiration>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: caption="Example 1. XML sample from the body of the request." caption-side="bottom"}
{: http}

A successful response returns a `202` if the object is in the archived state and a `200` if the object is already in the restored state.  If the object is already in the restored state and a new request to restore the object is received, the `Days` element will update the expiration time of the restored object.
{: http}

**Syntax**
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: caption="Example 2. Note the use of slashes and dots in this example of syntax." caption-side="bottom"}
{: codeblock}
{: http}

**Example request**
{: http}

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: caption="Example 3. Request header samples for creating an object lifecycle configuration." caption-side="bottom"}
{: http}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Expiration: [
          {
            Days: 123
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Expiration': [
                    {
                        'Days': 123
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Example 4. Example showing creation of lifecycle configuration." caption-side="bottom"}
