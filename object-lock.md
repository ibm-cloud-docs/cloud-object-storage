---

copyright:
  years: 2023

lastupdated: "2023-02-01"

keywords: worm, immutable, policy, retention, compliance

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Locking objects
{: #ol-overview}

Object Lock preserves electronic records and maintains data integrity by ensuring that individual object versions are stored in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner. This policy is enforced until a specified date or the removal of any legal holds. 
{: shortdesc}

## Why use Object Lock?
{: #ol-why}

Whether the goal is regulatory compliance or simply adding an extra layer of security and peace of mind, Object Lock prevents unintended alteration or deletion of data. Built on top of object versioning, Object Lock can act as buffer against both simple accidents and complex ransomware attacks.  

Object Lock has been evaluated by Cohasset Associates for use in environments that are subject to SEC 17a-4, CFTC, and FINRA regulations. For more information about how Object Lock relates to these regulations, see the [Cohasset Associates Compliance Assessment](https://www.youtube.com/watch?v=dQw4w9WgXcQ) [[[[TBD]]]].

## Getting started with Object Lock
{: #ol-gs}

In order to get started, there are some some prerequisites: 

- You'll need the `Writer` or `Manager` platform role on a bucket, or a custom role with the appropriate actions (such as `cloud-object-storage.bucket.put_object_lock_configuration`) assigned.
- The bucket must be empty.

1. After navigating to your chosen source bucket, click the **Configuration** tab.
2. Look for **Immutability** and click the **Enable Object Lock** button.
3. Optionally, set a default retention period.
4. Upload an object and navigate to the object details.
5. ??? [[[doesn't seem to work in staging]]]

## Terminology
{: #ol-terminology}

There are two ways use Object Lock to protect data: *retention periods* and *legal holds*.

- A **retention period** defines a time frame during which an object is unable to be modified or deleted. 
- A **legal hold** also prevents an object from being altered, but only remains in place until it is explicitly lifted. 

It is possible to make use of any combination of these parameters - an object version can have one, both, or neither.

### Retention Period
{: #ol-terminology-retention-period}

If you need to protect an object version for a fixed amount of time, you need to specify a *retention period*. The object version can be be deleted after this period expires (assuming there are no legal holds on the object version).

The retention period for new objects can be inherited from the default value set on the bucket, or it can be explicitly defined when writing the objcet by specifying a *Retain Until Date*. 

When you use bucket default settings, you don’t specify a Retain Until Date. Instead, you specify a duration, in either days or years, for which every object version placed in the bucket should be protected. When you place an object in the bucket, a Retain Until Date is calculated for the object version by adding the specified duration to the time of the object write.

If your request to place an object version in a bucket contains an explicit retention mode and period, those settings override any bucket default settings for that object version.
{:note}

Like all other Object Lock settings, retention periods apply to individual object versions. Different versions of a single object can have different retention modes and periods.

Imagine an object that is 60 days into a 90-day retention period, and you overwrite that object with the same name and a two year retention period. The operation will succeed and a new version of the object with a two year retention period is created. Meanwhile, after 30 more days the original version is eligible for deletion.

To extend the retention period of an object, simply send a request to set a new, longer, retention period.  The old value will be overwritten with the new, assuming the requestor has the `cloud-object-storage.object.put_object_lock_retention` action.

### Legal Hold
{: #ol-terminology-legal-hold}

A *legal hold* is ike a retention period in that it prevents an object version from being overwritten or deleted. However, legal holds are more flexible and don't have a defined temporal component. Instead they simply remain in effect until removed. Legal holds can be freely placed and removed by any user who has the `cloud-object-storage.object.put_object_lock_legal_hold` action. 

Legal holds and retention periods operate independently. Legal holds have no impact on retention periods, and vice-versa.

Imagine an object with both a legal hold and a retention period. When the retention period ends, the object version remains protected until the legal hold is removed. If you remove a legal hold while an object version is subject to a retention period it remains protected until the retention period is complete.

## Using Object Lock for business continuity and disaster recovery
{: #ol-bcdr}

Object Lock can be used to provide continuity of service in the event of a ransomware attack, as protected data is unable to be modified or destroyed.

## Consistency and data integrity
{: #ol-consistency}

While IBM Cloud Object Storage provides strong consistency for all data IO operations, bucket configuration is eventually consistent. After enabling a default retention period on a bucket, it may take a few moments for the configuration to propagate across the system and new objects to be assigned the new default. 

## IAM actions
{: #ol-iam}

There are new IAM actions associated with Object Lock. 

| IAM Action                                                       | Role                    |
|------------------------------------------------------------------|-------------------------|
| `cloud-object-storage.bucket.get_object_lock_configuration`      | Manager, Writer, Reader |
| `cloud-object-storage.bucket.put_object_lock_configuration`      | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_retention`          | Manager, Writer, Reader |
| `cloud-object-storage.object.put_object_lock_retention_version`  | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_retention_version`  | Manager, Writer, Reader |
| `cloud-object-storage.object.get_object_lock_legal_hold`         | Manager, Writer, Reader |
| `cloud-object-storage.object.put_object_lock_retention`          | Manager, Writer         |
| `cloud-object-storage.object.put_object_lock_legal_hold`         | Manager, Writer         |
| `cloud-object-storage.object.put_object_lock_legal_hold_version` | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_legal_hold_version` | Manager, Writer, Reader |

## Activity Tracker events 
{: #ol-at}

Object Lock generates additional events.

- `cloud-object-storage.bucket-object-lock.create`
- `cloud-object-storage.bucket-object-lock.read`
- `cloud-object-storage.object-object-lock-legal-hold.create`
- `cloud-object-storage.object-object-lock-legal-hold.read`
- `cloud-object-storage.object-object-lock-retention.create`
- `cloud-object-storage.object-object-lock-retention.read`


For `cloud-object-storage.bucket-object-lock.create` events, the following fields provide extra information:

| Field                                                         | Description                                                                     |
|---------------------------------------------------------------|---------------------------------------------------------------------------------|
| `requestData.object_lock_configuration.enabled`               | Indicates that Object Lock is enabled on the bucket                             |
| `requestData.object_lock_configuration.defaultRetention.mode` | Indicates `COMPLIANCE` mode is active - `GOVERNANCE` mode is not yet supported. |
| `object_lock_configuration.defaultRetention.years`            | The default retention period in years.                                          |
| `object_lock_configuration.defaultRetention.days`             | The default retention period in days.                                           |

Only `object_lock_configuration.defaultRetention.years` or `object_lock_configuration.defaultRetention.days` will be present, but not both at the same time.
{:note}

For operations on protected objects, the following fields may be present:

| Field                                                            | Description                                                                                                                                          |
|------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `requestData.object_lock_protection.legal_hold`                  | Indicates that a legal hold is in force on the object version.                                                                                       |
| `requestData.object_lock_protection.retention.mode`              | Indicates `COMPLIANCE` mode is active on the object version - `GOVERNANCE` mode is not yet supported.                                                |
| `requestData.object_lock_protection.retention.retain_until_date` | Indicates the date that object version is eligible for deletion. After this date the object is no longer delete protected based on a retention date. |


## Usage and accounting
{: #replication-usage}

Locked objects (and their versions) [contribute usage](/docs/cloud-object-storage?topic=cloud-object-storage-billing) just like any other data.

Replication generates additional metrics for use with IBM Cloud Monitoring:

- `ibm_cos_bucket_object_lock?????` [[[ Not sure about this ]]]

## Interactions
{: #ol-interactions}

### Versioning
{: #ol-interactions-versioning}

[Enabling versioning](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) is a prerequisite for enabling Object Lock.

### Key Protect encryption
{: #ol-interactions-kp}

Protected objects will be [encrypted using the root key](/docs/key-protect?topic=key-protect-about) of the bucket. Ensure that the key remains valid in order to prevent unintended crypto-shredding of protected objects.  

### Lifecycle configurations
{: #ol-interactions-lifecycle}

It is possible to enable [lifecycle policies](/docs/cloud-object-storage?topic=cloud-object-storage-expiry#expiry-rules-attributes) that archive locked objects, but naturally not those that expire objects.  
### Immutable Object Storage
{: #ol-interactions-worm}

Object Lock is an alternative to the bucket-level retention policies available when using Immutable Object Storage.  As Object Lock requires versioning to be enabled, and Immutable Object Storage is not compatible with versioning, it is not possible to have both WORM solutions enabled on the same bucket.  

### Other interactions
{: #ol-interactions-worm}

There should be no adverse interactions when using Object Lock with other Object Storage features, such as setting CORS policies, setting IP firewalls or condition based restrictions, bucket quotas, replication, or Code Engine.

## REST API examples
{: #ol-apis-examples}

The following examples are shown using cURL for ease of use. Environment variables are used to represent user specific elements such as `$BUCKET`, `$TOKEN`, and `$REGION`.  Note that `$REGION` would also include any network type specifications, so sending a request to a bucket in `us-south` using the private network would require setting the variable to `private.us-south`.

### Enable object lock on a bucket
{: #ol-apis-enable}

The Object Lock configuration is provided as XML in the body of the request.  New requests will overwrite any existing replication rules that are present on the bucket.

An Object Lock configuration must include one rule.


Header                    | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | String | **Required**: The base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.

The body of the request must contain an XML block with the following schema:

| Element                   | Type      | Children                    | Ancestor                  | Constraint                                                                                                     |
|---------------------------|-----------|-----------------------------|---------------------------|----------------------------------------------------------------------------------------------------------------|
| `ObjectLockConfiguration` | Container | `ObjectLockEnabled`, `Rule` | None                      | Limit 1.                                                                                                       |
| `ObjectLockEnabled`       | String    | None                        | `ObjectLockConfiguration` | The only valid value is `ENABLED`.                                                                             |
| `Rule`                    | Container | `DefaultRetention`          | `ObjectLockConfiguration` | Limit 1                                                                                                        |
| `DefaultRetention`        | Container | `Days`, `Mode`, `Years`     | `Rule`                    | Limit 1.                                                                                                       |
| `Days`                    | Integer   | None                        | `DefaultRetention`        | The number of days that you want to specify for the default retention period. Cannot be combined with `Years`. |
| `Mode`                    | String    | None                        | `DefaultRetention`        | Only `COMPLIANCE` is supported at this time.                                                                   |
| `Years`                   | Integer   | None                        | `DefaultRetention`        | The number of years that you want to specify for the default retention period. Cannot be combined with `Days`. |

This example will retain any new objects for at least 30 days.  

```
curl -X "PUT" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?object-lock" \
     -H 'Authorization: bearer $TOKEN' \
     -H 'Content-MD5: exuBoz2kFBykNwqu64JZuA==' \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'<ObjectLockConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
            <ObjectLockEnabled>string</ObjectLockEnabled>
            <Rule>
                <DefaultRetention>
                  <Days>30</Days>
                  <Mode>COMPLIANCE</Mode>
                </DefaultRetention>
            </Rule>
          </ObjectLockConfiguration>'
```

A successful request returns a `200` response.


### View Object Lock configuration for a bucket
{: #ol-apis-read}

```
curl -X "GET" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?object-lock" \
     -H 'Authorization: bearer $TOKEN' 
```

This returns an XML response body with the appropriate schema:

```xml
<ObjectLockConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <ObjectLockEnabled>string</ObjectLockEnabled>
  <Rule>
      <DefaultRetention>
        <Days>30</Days>
        <Mode>COMPLIANCE</Mode>
      </DefaultRetention>
  </Rule>
</ObjectLockConfiguration>
```

## SDK examples
{: #ol-sdks}

The following examples make use of the IBM COS SDKs for Python and Node.js, although the implementation of object versioning should be fully compatible with any S3-compatible library or tool that allows for the setting of custom endpoints.  Using third-party tools requires HMAC credentials in order to calculate AWS V4 signatures.  For more information on HMAC credentials, [see the documentation](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).  

### Python
{: #ol-sdks-python}

Enabling Object Lock using the IBM COS SDK for Python can be done using the [low-level client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client) syntax.

Using a client:

```python
#!/usr/bin/env python3

import ibm_boto3
from ibm_botocore.config import Config
from ibm_botocore.exceptions import ClientError

# Define constants
API_KEY = os.environ.get('IBMCLOUD_API_KEY')
SERVICE_INSTANCE = os.environ.get('SERVICE_INSTANCE_ID')
ENDPOINT = os.environ.get('ENDPOINT')

BUCKET = "my-locked-bucket" # The bucket that will enable replication.

# Create resource client with configuration info pulled from environment variables.
cosClient = ibm_boto3.client("s3",
                         ibm_api_key_id=API_KEY,
                         ibm_service_instance_id=SERVICE_INSTANCE,
                         config=Config(signature_version="oauth"),
                         endpoint_url=ENDPOINT
                         )

response = cosClient.put_object_lock_configuration(
    Bucket=BUCKET,
    ObjectLockConfiguration={
        'ObjectLockEnabled': 'Enabled',
        'Rule': {
            'DefaultRetention': {
                'Mode': 'COMPLIANCE',
                'Days': 90,
            }
        }
    },
    ChecksumAlgorithm='SHA256'
)
```

### Node.js
{: #ol-sdks-node}

Enabling versioning using the [IBM COS SDK for Node.js](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putBucketVersioning-property):

```js
const AWS = require('aws-sdk');

// Create an S3 client
const cos = new AWS.S3();

// Name of the bucket you want to enable object lock on
const bucketName = 'my-locked-bucket';

// Enable object lock
cos.putObjectLockConfiguration({
  Bucket: bucketName,
  ObjectLockConfiguration: {
    ObjectLockEnabled: 'Enabled',
    Rule: {
      DefaultRetention: {
        Mode: 'COMPLIANCE',
        Days: 30
      }
    }
  }
}, (err, data) => {
  if (err) {
    console.log('Error enabling object lock:', err);
  } else {
    console.log('Object lock enabled successfully');
  }
});
```
