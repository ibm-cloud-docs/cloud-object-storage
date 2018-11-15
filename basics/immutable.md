---

copyright:
  years: 2018
lastupdated: "2018-11-14"

---
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Use Immutable Object Storage

Some industries, such as financial services, have strict oversight and audit requirements that require retention of certain records  without modification for prescribed lengths of time. In 2003 the SEC issued an Interpretive Release that software defined storage could meet the retention requirements if it can meet a set of rules.

| Rule | Requirement |
| :--- | :--- |
| SEC 17a-4(f)(2)(ii)(A) | Protect data from overwrites and deletion |
| SEC 17a-4(f)(2)(ii)(B) | Automatically verify the storage system properly stored the data. |
| SEC 17a-4(f)(2)(ii)(C) | Manage retention policies for the objects. |
| SEC 17a-4(f)(2)(ii)(D) | Ability to download indices and records. |
| SEC 17a-4(f)(3)(iii/v) | Store duplicate copies and provide auditing capabilities. |

Users configuring buckets with Immutable Object Storage can set the length of retention on a per-object basis, or allow objects to inherit a default retention period set on the bucket.  Objects are also subject to optional 'legal holds' that prevent modification or deletion even after the retention period has expired.  

IBM Cloud administrators and operators are unable to override the constraints imposed by Immutable Object Storage.

Immutable Object Storage is not available in all regions, and requires a Standard plan. See [Integrated Services](/docs/services/cloud-object-storage/basics/services.html#service-availability) for more details.
{:tip}

## Who needs Immutable Object Storage?

Users who may benefit from using Immutable Object Storage include:
* Financial Industry
* Healthcare Industry
* Media content archives
* General archives
* Those who have a regulatory requirement to prevent destruction of data
* Those who are looking to prevent privileged modification or deletion of objects or documents

Immutable object storage policies prevent users with `Writer` or `Manager` roles from deleting or modifying an object once it is written. Therefore, it should be used when governance dictates that documents must adhere to a retention policy that prevents privileged users from maliciously or accidentally altering data.

Immutable object storage policies should not be used for objects that are frequently modified or that privileged users must be allowed to delete.

### Event-based retention
User applications can store an object in {{site.data.keyword.cos_full}} with an indefinite retention period and then can change the object retention to a finite value at a later time. For example, a company has a policy of retaining employee records for three years after the employee leaves the company. When an employee joins the company, the records associated with that employee can be indefinitely retained. When the employee leaves the company, the indefinite retention is converted to a finite value of three years from the current time, as defined by company policy. The object is then protected for three years after the retention period change. {{site.data.keyword.cos_full}} provides the interface by which a third party application or a user can convert the retention of those documents from an indefinite to a finite retention. The user or third party application is responsible for triggering the change in the retention period using the SDK or REST API.

### Permanent retention
Objects can be stored in {{site.data.keyword.cos_full}} with a permanent retention period. A permanent retention period never expires, so these objects cannot ever be deleted or modified. The bucket in which the object resides must have permanent retention enabled to store an object permanently. Lifting permanent retention from a bucket or objects is impossible without a legal review process and the engagement of a data fiduciary.

### Audit of access and transactions
Access log data for Immutable Object Storage to review changes to retention parameters, object retention period, and application of legal holds is available on a case-by-case basis by opening a customer service ticket.

## Using the console
{: #console}

TBD

## Using the REST API, Libraries, and SDKs
{: #sdk}
Several new APIs have been introduced to the IBM COS SDKs to provide support for applications working with Immutable Object Storage.

### Add a protection configuration to an existing bucket

This implementation of the `PUT` operation uses the `protection` query parameter to set the retention parameters for an existing bucket. This operation allows you to set or change the minimum, default, and maximum retention period. This operation also allows you to change the protection state of the bucket. 

Objects written to a protected bucket cannot be deleted until the protection period has expired and all legal holds on the object are removed. The bucket's default retention value is given to an object unless an object specific value is provided when the object is created. Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object. 

The minimum supported value for the retention period settings `MinimumRetention`, `DefaultRetention`, and `MaximumRetention` is 0 days and the maximum supported value is 36159 days. 

A `Content-MD5` header is required. This operation does not make use of additional query parameters.
{: http}

**Syntax**
```http
PUT https://{endpoint}/{bucket-name}?protection= # path style
PUT https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Sample request**
```
PUT /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
x-amz-content-sha256: 2938f51643d63c864fdbea618fe71b13579570a86f39da2837c922bae68d72df
Content-MD5: GQmpTNpruOyK6YrxHnpj7g==
Content-Type: text/plain
Host: 67.228.254.193
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

**Sample response**
```
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.14.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 0
```
{: codeblock}
{: http}

```py
client.put_bucket_protection_configuration(
    Bucket=bucket,
    ProtectionConfiguration={
    'Status': 'Retention',
    'MinimumRetention': {'Days': 10},
    'DefaultRetention': {'Days': 100},
    'MaximumRetention': {'Days': 1000}
    })
```
{: python}

```js
function addProtectionConfigurationToBucket(bucketName) {
  console.log(`Adding protection to bucket ${bucketName}`);
  return cos.putBucketProtectionConfiguration({
    Bucket: bucketName,
    ProtectionConfiguration: {
      'Status': 'Retention',
      'MinimumRetention': {'Days': 10},
      'DefaultRetention': {'Days': 100},
      'MaximumRetention': {'Days': 1000}
    }
  }).promise()
  .then(() => {
    console.log(`Protection added to bucket ${bucketName}!`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}
```
{: javascript}

```java
/**
* Set the protection configuration for the specified bucket.
*
* @param setBucketProtectionRequest
*                 The request object containing all options for setting the
*                 bucket protection configuration
*/
public void setBucketProtectionConfiguration(SetBucketProtectionConfigurationRequest setBucketProtectionRequest)
            throws SdkClientException, AmazonServiceException;

/**
*
* @param bucketName
*                 The name of the bucket for which to set protection configuration
* @param bucketProtection
*                 The new protection configuration for this bucket, which
*                 completely replaces any existing configuration.
*/
public void setBucketProtection(String bucketName, BucketProtectionConfiguration protectionConfiguration)
            throws SdkClientException, AmazonServiceException;       
```
{: java}
{: codeblock}

More Java examples are coming soon.
{: java}

### Check protection on a bucket

This implementation of a GET operation fetches the retention parameters for an existing bucket. 
{: http}

**Syntax**
```
GET https://{endpoint}/{bucket-name}?protection= # path style
GET https://{bucket-name}.{endpoint}?protection= # virtual host style
```
{: codeblock}
{: http}

**Sample request**
```xml
GET /example-bucket?protection= HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20181011T190354Z
Content-Type: text/plain
Host: 67.228.254.193
Sample response
HTTP/1.1 200 OK
Date: Wed, 5 Oct 2018 15:39:38 GMT
X-Clv-Request-Id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Accept-Ranges: bytes
Server: Cleversafe/3.13.1 
X-Clv-S3-Version: 2.5
x-amz-request-id: 7afca6d8-e209-4519-8f2c-1af3f1540b42
Content-Length: 299
<ProtectionConfiguration>
  <Status>Retention</Status>
  <MinimumRetention>
    <Days>100</Days>
  </MinimumRetention>
  <MaximumRetention>
    <Days>10000</Days>
  </MaximumRetention>
  <DefaultRetention>
    <Days>2555</Days>
  </DefaultRetention>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

If there is no protection configuration on the bucket, the server responds with disabled status instead.
{: http}

```xml
<ProtectionConfiguration>
  <Status>Disabled</Status>
</ProtectionConfiguration>
```
{: codeblock}
{: http}

```py
client.get_bucket_protection_configuration(
    Bucket=bucket
)
```
{: python}

```js
function getProtectionConfigurationOnBucket(bucketName) {
  console.log(`Retrieve the protection on bucket ${bucketName}`);
  return cos.getBucketProtectionConfiguration({
    Bucket: bucketName
    }).promise()
    .then((data) => {
      console.log(`Configuration on bucket ${bucketName}: ${data}`);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: javascript}

More Java examples are coming soon.
{: java}

### Upload a protected object

This enhancement of the `PUT` operation adds three new request headers: two for specifying the retention period in different ways, and one for adding a single legal hold to the new object. New errors are defined for illegal values for the new headers, and if an object is under retention any overwrites will fail.
{: http}

Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

A `Content-MD5` header is required.
{: http}

These headers apply to POST object and multipart upload requests as well. If uploading an object in multiple parts, each part requires a `Content-MD5` header.
{: http}

|Value	| Type	| Description |
| --- | --- | --- | 
|`Retention-Period` | Non-negative integer (seconds) | Retention period to store on the object in seconds. The object can be neither overwritten nor deleted until the amount of time specified in the retention period has elapsed. If this field and `Retention-Expiration-Date` are specified a `400`  error is returned. If neither is specified the bucket's `DefaultRetention` period will be used. Zero (`0`) is a legal value assuming the bucket's minimum retention period is also `0`. |
| `Retention-expiration-date` | Date (ISO 8601 Format) | Date on which it will be legal to delete or modify the object. You can only specify this or the Retention-Period header. If both are specified a `400`  error will be returned. If neither is specified the bucket's DefaultRetention period will be used. This header should be used to calculate a retention period in seconds and then stored in that manner. |
| `Retention-legal-hold-id` | string | A single legal hold to apply to the object. A legal hold is a Y character long string. The object cannot be overwritten or deleted until all legal holds associated with the object are removed. |

```py
client.put_object(
    Bucket=bucket,
    Body=f,
    Key=key,
    RetentionLegalHoldId="put-object-legal-hold"
)

client.copy_object(
    Bucket=bucket,
    Key='newkey',
    CopySource='%s/%s' % (bucket, key),
    RetentionDirective="Copy"
)

client.complete_multipart_upload(
    Bucket=bucket,
    Key=key,
    MultipartUpload={
        'Parts':[
            {
                'ETag': part['ETag'],
                'PartNumber': 1
            }
        ]
    },
    UploadId=initiate['UploadId'],
    RetentionPeriod=365000
)

client.upload_file(
    obj,
    bucket,
    key,
    ExtraArgs={'RetentionPeriod': 365000}
)
```
{: codeblock}
{: python}

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
  console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
  return cos.putObject({
    Bucket: bucketName,
    Key: objectName,
    Body: 'body',
    RetentionLegalHoldId: legalHoldId
  }).promise()
  .then((data) => {
    console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
  console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
  return cos.copyObject({
    Bucket: destinationBucketName,
    Key: newObjectName,
    CopySource: sourceBucketName + '/' + sourceObjectName,
    RetentionDirective: 'Copy'
  }).promise()
  .then((data) => {
    console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}
```
{: javascript}
{: codeblock}

More Java examples are coming soon.
{: java}

### Add or remove a legal hold to or from a protected object

This implementation of the `POST` operation uses the `legalHold` query parameter and `add` and `remove` query parameters to add or remove a single legal hold from a protected object in a protected bucket.
{: http}

The object can support 100 legal holds:
* A legal hold identifier is a string of maximum length 64 characters and a minimum length of 1 character. Valid characters are letters, numbers, !, _, ., *, ', (, ), and -.
* If the addition of the given legal hold exceeds 100 total legal holds on the object, the new legal hold will not be added, a `400` error will be returned.
* If an identifier is too long it will not be added to the object and a `400` error is returned.
* If an identifier contains invalid characters, it will not be added to the object and a `400` error is returned.
* If an identifier is already in use on an object, the existing legal hold is not modified and the response indicates theidentifier was already in use with a 409 error.
* If an object does not have retention period metadata, a `400` error is returned and adding or removing a legal hold is not allowed.

The legal hold identifiers are stored in the object metadata along with the timestamp of when they are applied to the object. The presence of any legal hold identifiers prevents the modification or deletion of the object data, even if the retention period has expired. 

The presence of a retention period header is required, otherwise a `400` error is returned.
{: http}

The user making adding or removing a legal hold must have `Manager` permissions for this bucket.

A `Content-MD5` header is required. This operation does not make use of operation specific payload elements.
{: http}

**Syntax**
```
POST https://{endpoint}/{bucket-name}?legalHold # path style
POST https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Sample request**
```
POST /BucketName/ObjectName?legalHold&add=legalHoldID HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Wed, 8 Feb 2017 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
```
{: codeblock}
{: http}

**Sample response**
```
HTTP/1.1 200 OK
Date: Wed, 8 Feb 2017 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}

```py
client.add_legal_hold(
    Bucket=bucket,
    Key=key,
    RetentionLegalHoldId=legal_hold_id
)

client.delete_legal_hold(
    Bucket=bucket,
    Key=key,
    RetentionLegalHoldId=legal_hold_id
)
```
{: python}

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
  console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
  return cos.client.addLegalHold({
    Bucket: bucketName,
    Key: objectId,
    RetentionLegalHoldId: legalHoldId
  }).promise()
  .then(() => {
    console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
  console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
  return cos.client.deleteLegalHold({
    Bucket: bucketName,
    Key: objectId,
    RetentionLegalHoldId: legalHoldId
  }).promise()
  .then(() => {
    console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}
```
{: javascript}

More Java examples are coming soon.
{: java}

### Extend the retention period of a protected object

This implementation of the `POST` operation uses the `extendRetention` query parameter to extend the retention period of a protected object in a protected bucket.
{: http}

The retention period of an object can only be extended. It cannot be decreased from the currently configured value.

The retention expansion value can be set in one of three ways: 
* additional time from the current value
* new extension period in seconds
* * new retention expiry date of the object

The current retention period stored in the object metadata is either increased by the given additional time or replaced with the new value, depending on the parameter that is set in the `extendRetention` request. In all cases, the extend retention parameter is checked against the current retention period and the extended parameter is only accepted if the updated retention period is greater than the current retention period.

Objects in protected buckets that are no longer under retention (retention period has expired and the object does not have any legal holds), when overwritten, will again come under retention. The new retention period can be provided as part of the object overwrite request or the default retention time of the bucket will be given to the object.

**Syntax**
```
POST https://{endpoint}/{bucket-name}?extendRetention= # path style
POST https://{bucket-name}.{endpoint}?extendRetention= # virtual host style
```
{: codeblock}
{: http}

**Sample request**
```yaml
POST /BucketName/ObjectName?extendRetention HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Wed, 8Feb 201717:50:00GMT
Authorization: authorization string
Content-Type: text/plain
Additional-Retention-Period: 31470552
```
{: codeblock}
{: http}

**Sample response**
```
HTTP/1.1 200 OK
Date: Wed, 8Feb 201717:51:00GMT
Connection: close
```
{: codeblock}
{: http}

```py
client.extend_object_retention(
    Bucket=bucket,
    Key=key,
    AdditionalRetentionPeriod=10
)
```
{: codeblock}
{: python}

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalDays) {
  console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalDays} days.`);
  return cos.extendObjectRetention({
    Bucket: bucketName,
    Key: objectName,
    AdditionalRetentionPeriod: additionalDays
  }).promise()
  .then((data) => {
    console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}
```
{: javascript}

More Java examples are coming soon.
{: java}

### List legal holds on a protected object

This implementation of the `GET` operation uses the `legalHold` query parameter to return the list of legal holds on an object and related retention state in an XML response body.
{: http}

This operation returns:
* Object creation date
* Object retention period in seconds (our chosen unit of time for S3 API retention periods)
* Calculated retention expiration date based on the period and creation date
* List of legal holds
* Legal hold identifier
* Time stamp when legal hold was applied

If there are no legal holds on the object, an empty `LegalHoldSet` is returned.
If there is no retention period specified on the object, a `404` error is returned.

**Syntax**
```
GET https://{endpoint}/{bucket-name}?legalHold= # path style
GET https://{bucket-name}.{endpoint}?legalHold= # virtual host style
```
{: http}

**Sample request**
```
GET /BucketName/ObjectName?legalHold HTTP/1.1
Host: myBucket.mydsNet.corp.com
Date: Wed, 8 Feb 2017 17:50:00 GMT
Authorization: {authorization-string}
Content-Type: text/plain
```
{: codeblock}
{: http}

**Sample response**
```xml
HTTP/1.1 200 OK
Date: Wed, 8 Feb 2017 17:51:00 GMT
Connection: close
<?xml version="1.0" encoding="UTF-8"?>
<RetentionState>
  <CreateTime>Thu, 2 Sep 2016 21:33:08 GMT</CreateTime>
  <RetentionPeriod>220752000</RetentionPeriod>
  <RetentionPeriodExpirationDate>Fri, 1 Sep 2023 21:33:08
GMT</RetentionPeriodExpirationDate>
  <LegalHoldSet>
    <LegalHold>
      <ID>SomeLegalHoldID</ID>
      <Date>Thu, 15 Sep 2016 23:13:18 GMT</Date>
    </LegalHold>
    <LegalHold>
    ...
    </LegalHold>
  </LegalHoldSet>
</RetentionState>
```
{: codeblock}
{: http}

```py 
client.list_legal_holds(
    Bucket=bucket,
    Key=key
)
```
{: codeblock}
{: python}

```js
function listLegalHoldsOnObject(bucketName, objectName) {
  console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
  return cos.listLegalHolds({
    Bucket: bucketName,
    Key: objectId
  }).promise()
  .then((data) => {
    console.log(`Legal holds on bucket ${bucketName}: ${data}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}
```
{: javascript}

More Java examples are coming soon.
{: java}