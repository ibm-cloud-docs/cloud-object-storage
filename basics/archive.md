---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Archive cold data with transition rules
{: #archive}

{{site.data.keyword.cos_full}} Archive is a [low cost](https://www.ibm.com/cloud/object-storage) option for data that is rarely accessed. You can store data by transitioning from any of the storage tiers (Standard, Vault, Cold Vault and Flex) to long-term offline archive or use the online Cold Vault option.
{: shortdesc}

You can archive objects using the web console, REST API, and 3rd party tools that are integrated with IBM Cloud Object Storage. 

For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Add or manage an archive policy on a bucket
{: #archive-add}

When creating or modifying an archive policy for a bucket, consider the following:

* An archive policy can be added to a new or existing bucket at any time. 
* An existing archive policy can be modified or disabled. 
* A newly added or modified archive policy applies to new objects uploaded and does not affect existing objects.

To immediately archive new objects uploaded to a bucket, enter 0 days on the archive policy.
{:tip}

Archive is available in certain regions only. See [Integrated Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) for more details.
{:tip}

## Restore an archived object
{: #archive-restore}

In order to access an archived object, you must restore it to the original storage tier. When restoring an object, you can specify the number of days you want the object to be available. At the end of the specified period, the restored copy is deleted. 

The restoration process can take up to 12 hours.
{:tip}

The archived object sub-states are:

* Archived: An object in the archived state has been moved from its online storage tier (Standard, Vault, Cold Vault and Flex) to the offline archive tier based on the archive policy on the bucket.
* Restoring: An object in the restoring state is in the process of generating a copy from the archived state to its original online storage tier.
* Restored: An object in the restored state is a copy of the archived object that was restored to its original online storage tier for a specified amount of time. At the end of the period, the copy of the object is deleted, while maintaining the archived object.

## Limitations
{: #archive-limitations}

Archive policies are implemented using subset of the `PUT Bucket Lifecycle Configuration` S3 API operation.

Supported functionality includes:
* Specifying either a date or the number of days in the future when objects transition to an archived state.
* Setting [expiration rules](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry) for objects.

Unsupported functionality includes:
* Multiple transition rules per bucket.
* Filtering objects to archive using a prefix or object key.
* Tiering between storage classes.

## Using the REST API and SDKs
{: #archive-api} 

### Create a bucket lifecycle configuration
{: #archive-api-create} 

This implementation of the `PUT` operation uses the `lifecycle` query parameter to set lifecycle settings for the bucket. This operation allows for a single lifecycle policy definition for a given bucket. The policy is defined as a rule consisting of the following parameters: `ID`, `Status`, and `Transition`.
{: http}

The transition action enables future objects written to the bucket to an archived state after a defined period of time. Changes to the lifecycle policy for a bucket are **only applied to new objects** written to that bucket.

Cloud IAM users must have at a minimum the `Writer` role to add a lifecycle policy to the bucket.

Classic Infrastructure Users must have Owner Permissions and be able to create buckets in the storage account to add a lifecycle policy to the bucket.

This operation does not make use of additional operation specific query parameters.
{: http}

Header                    | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | string | **Required**: The base64 encoded 128-bit MD5 hash of the payload, used as an integrity check to ensure the payload was not altered in transit.
{: http}

The body of the request must contain an XML block with the following schema:
{: http}

| Element                  | Type                 | Children                               | Ancestor                 | Constraint                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Container            | `Rule`                                 | None                     | Limit 1.                                                                                  |
| `Rule`                   | Container            | `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` | Limit 1.                                                                                  |
| `ID`                     | String               | None                                   | `Rule`                   | Must consist of (`a-z,`A-Z0-9`) and the following symbols: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | String               | `Prefix`                               | `Rule`                   | Must contain a `Prefix` element                                                            |
| `Prefix`                 | String               | None                                   | `Filter`                 | **Must** be set to `<Prefix/>`.                                                           |
| `Transition`             | `Container`          | `Days`, `StorageClass`                 | `Rule`                   | Limit 1.                                                                                  |
| `Days`                   | Non-negative integer | None                                   | `Transition`             | Must be a value greater than 0.                                                           |
| `Date`                   | Date                 | None                                   | `Transistion`            | Must be in ISO 8601 Format and the date must be in the future.                            |
| `StorageClass`           | String               | None                                   | `Transition`             | **Must** be set to `GLACIER`.                                                             |
{: http}

__Syntax__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Example 1. Note the use of slashes and dots in this example of syntax." caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
		<Filter>
			<Prefix/>
		</Filter>
		<Transition>
			<Days>{integer}</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Example 2. XML sample for creating an object lifecycle configuration." caption-side="bottom"}

__Examples__
{: http}

_Sample Request_

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
{: http}
{: caption="Example 3. Request header samples for creating an object lifecycle configuration." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Example 4. XML sample for PUT request body." caption-side="bottom"}

_Sample Response_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Example 5. Response headers." caption-side="bottom"}

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
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
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
{: caption="Example 21. Example showing creation of lifecycle configuration." caption-side="bottom"}

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
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="Example 26. Method used in creating an object configuration." caption-side="bottom"}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Example 31. Function used in setting a bucket lifecycle." caption-side="bottom"}

**Method Summary**
{: java}

Method |  Description
--- | ---
`getBucketName()` | Gets the name of the bucket whose lifecycle configuration is being set.
`getLifecycleConfiguration()` | Gets the new lifecycle configuration for the specified bucket.
`setBucketName(String bucketName)` | Sets the name of the bucket whose lifecycle configuration is being set.
`withBucketName(String bucketName)` | Sets the name of the bucket whose lifecycle configuration is being set, and returns this object so that additional method calls may be chained together.
{: java}

---

### Retrieve a bucket lifecycle configuration
{: #archive-api-retrieve} 

This implementation of the `GET` operation uses the `lifecycle` query parameter to retrieve the lifecycle settings for the bucket. 

Cloud IAM users must have at a minimum the `Reader` role to retrieve a lifecycle for a bucket.

Classic Infrastructure Users must have at minimum `Read` permissions on the bucket to retrieve a lifecycle policy for a bucket.

This operation does not make use of additional operation specific headers, query parameters, or payload.

__Syntax__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Example 6. Variations in syntax for GET requests." caption-side="bottom"}

__Examples__ 
{: http}

_Sample Request_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Example 7. Sample request headers for retrieving configuration." caption-side="bottom"}

_Sample Response_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Example 8. Sample response headers from GET request." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Example 9. XML example for response body." caption-side="bottom"}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Example 22. Example showing retrieval of lifecycle metadata." caption-side="bottom"}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Example 27. Method used in retrieving an object configuration." caption-side="bottom"}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Example 32. Function signature for obtaining object lifecycle configuration." caption-side="bottom"}

---

### Delete a bucket lifecycle configuration
{: #archive-api-delete} 

This implementation of the `DELETE` operation uses the `lifecycle` query parameter to remove any lifecycle settings for the bucket. Transitions defined by the rules will no longer take place for new objects. 

**Note:** Existing transition rules will be maintained for objects that were already written to the bucket before the rules were deleted.

Cloud IAM users must have at a minimum the `Writer` role to remove a lifecycle policy from a bucket.

Classic Infrastructure Users must have `Owner` permissions on the bucket to remove a lifecycle policy from a bucket.

This operation does not make use of additional operation specific headers, query parameters, or payload.

__Syntax__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Example 10. Note the use of slashes and dots in the example of syntax." caption-side="bottom"}

__Examples__
{: http}

_Sample Request_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Example 11. Sample request headers for the DELETE HTTP verb." caption-side="bottom"}

_Sample Response_
{: http}

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Example 12. Sample response from DELETE request." caption-side="bottom"}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Example 23. Example showing how to delete a bucket's lifecycle configuration." caption-side="bottom"}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Example 28. Method used in deleting object configuration." caption-side="bottom"}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Example 33. Function used in deleting object configuration." caption-side="bottom"}

---

### Temporarily restore an archived object 
{: #archive-api-restore}

This implementation of the `POST` operation uses the `restore` query parameter to request temporary restoration of an archived object. The user must first restore an archived object before downloading or modifying the object. When restoring an object, the user must specify a period after which the temporary copy of the object will be deleted. The object maintains the storage class of the bucket.

There can be a delay of up to 12 hours before the restored copy is available for access. A `HEAD` request can check if the restored copy is available. 

To permanently restore the object, the user must copy the restored object to a bucket that does not have an active lifecycle configuration.

Cloud IAM users must have at a minimum the `Writer` role to restore an object.

Classic Infrastructure users must have at a minimum `Write` permissions on the bucket and `Read` permission on the object to restore it.

This operation does not make use of additional operation specific query parameters.

Header                    | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | string | **Required**: The base64 encoded 128-bit MD5 hash of the payload, used as an integrity check to ensure the payload was not altered in transit.
{: http}

The body of the request must contain an XML block with the following schema:

Element                  | Type      | Children                               | Ancestor                 | Constraint
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | Container | `Days`, `GlacierJobParameters`    | None       | None
`Days`                   | Integer | None | `RestoreRequest` | Specified the lifetime of the temporarily restored object. The minimum number of days that a restored copy of the object can exist is 1. After the restore period has elapsed, temporary copy of the object will be removed.
`GlacierJobParameters` | String | `Tier` | `RestoreRequest` | None
`Tier` | String | None | `GlacierJobParameters` | **Must** be set to `Bulk`.

A successful response returns a `202` if the object is in the archived state and a `200` if the object is already in the restored state.  If the object is already in the restored state and a new request to restore the object is received, the `Days` element will update the expiration time of the restored object.

__Syntax__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="Example 13. Note the use of slashes and dots in the example of syntax." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Example 14. Model of XML for request body." caption-side="bottom"}

__Examples__
{: http}

_Sample Request_
{: http}

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Example 15. Sample request headers for object restoration." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Example 16. Sample request body for object restoration." caption-side="bottom"}

_Sample Response_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Example 17. Response to restoring object (`HTTP 202`)." caption-side="bottom"}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Example 24. Code used in restoring an archived object." caption-side="bottom"}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="Example 29. Temporarily restoring an archived object." caption-side="bottom"}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="Example 34. Function signature for restoring an archived object." caption-side="bottom"}

**Method Summary**
{: java}

Method |  Description
--- | ---
`clone()` | Creates a shallow clone of this object for all fields except the handler context.
`getBucketName()` | Returns the name of the bucket containing the reference to the object to restore.
`getExpirationInDays()` | Returns the time in days from an object's creation to its expiration.
`setExpirationInDays(int expirationInDays)` | Sets the time, in days, between when an object is uploaded to the bucket and when it expires.
{: java}

---

### Get an object's headers
{: #archive-api-head}

A `HEAD` given a path to an object retrieves that object's headers. This operation does not make use of operation specific query parameters or payload elements.
{: http}

__Syntax__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="Example 18. Variations in defining endpoints." caption-side="bottom"}


__Response headers for archived objects__
{: http}

Header | Type | Description
--- | ---- | ------------
`x-amz-restore` | string | Included if the object has been restored or if a restoration is in progress. If the object has been restored, the expiry date for the temporary copy is also returned.
`x-amz-storage-class` | string | Returns `GLACIER` if archived or temporarily restored.
`x-ibm-archive-transition-time` | date | Returns the date and time when the object is scheduled to transition to the archive tier.
`x-ibm-transition` | string | Included if the object has transition metadata and returns the tier and original time of transition.
`x-ibm-restored-copy-storage-class` | string | Included if an object is in the `RestoreInProgress` or `Restored` states and returns the storage class of the bucket.
{: http}

_Sample request_
{: http}

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="Example 19. Example showing request headers." caption-side="bottom"}

_Sample response_
{: http}

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="Example 20. Example showing response headers." caption-side="bottom"}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="Example 30. Handling the response for object headers." caption-side="bottom"}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
  else   
    console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Example 25. Example showing retrieval of object headers." caption-side="bottom"}


```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="Example 35. Function used in obtaining object headers." caption-side="bottom"}

**Method Summary**
{: java}

Method |  Description
--- | ---
`clone()` | Returns a clone of this `ObjectMetadata`.
`getRestoreExpirationTime()` | Returns the time at which an object that has been temporarily restored from ARCHIVE will expire, and will need to be restored again in order to be accessed.
`getStorageClass() ` | Returns the original storage class of the bucket.
`getIBMTransition()` | Return the transition storage class and time of transition.
{: java}









## Next Steps
{: #archive-next-steps}

In addition to {{site.data.keyword.cos_full_notm}}, {{site.data.keyword.cloud_notm}} currently provides several additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs. [Learn more.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)