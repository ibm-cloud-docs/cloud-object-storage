---

copyright:
  years: 2023, 2024
lastupdated: "2024-04-25"

keywords: object, policy, fine grained access control, prefix

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Folder level (prefix) fine grained access control
{: #fgac-overview}

Object Lock preserves electronic records and maintains data integrity by ensuring that individual object versions are stored in a WORM (Write-Once-Read-Many), non-erasable and non-rewritable manner. This policy is enforced until a specified date or the removal of any legal holds.
{: shortdesc}

## Why use fine-grained access control?
{: #ol-why}

Object Lock helps customers govern data preservation and retention requirements by enforcing data immutability for their backup, disaster recovery, and cyber resiliency workloads.

Object Lock ensures that **data cannot be deleted by anyone** and **retention on an object cannot be suspended**. Read the documentation carefully before you lock objects with a retention period.
{: attention}

It's your responsibility to ensure compliance with any regulations that you (your organization) might be subject to when it comes to preservation and storage of data for long-term retention.
{: remember}

You are responsible for ensuring that your {{site.data.keyword.cloud_notm}} Account is kept in good standing per {{site.data.keyword.cloud_notm}} policies and guidelines for while the data is subject to a retention period. For more information, see the {{site.data.keyword.cloud_notm}} Service terms.
{: important}

## Terminology
{: #ol-terminology}

You can use Object Lock to protect data in two ways: *retention periods* and *legal holds*.

- A **retention period** defines a time frame during which an object is unable to be modified or deleted.
- A **legal hold** also prevents an object from being altered, but remains in place only until it is explicitly lifted.

It is possible to use any combination of these parameters - an object version can have one, both, or neither.

### Retain Until Date (Retention Period)
{: #ol-terminology-retention-period}

If you need to protect an object version for a fixed amount of time, you need to specify a *Retain Until Date*, which determines the period in which it cannot be altered. The object version can be deleted after this date passes (assuming that no legal holds apply for the object version).

The retention period for new objects can be inherited from the default value set on the bucket. The retention period can also be explicitly defined when the object is written by specifying a *Retain Until Date*.

When you use bucket default settings, you don’t specify a *Retain Until Date*. Instead, you specify a duration, in either days or years, for which every object version that is placed in the bucket must be protected. When you place an object in the bucket, a *Retain Until Date* is calculated for the object version by adding the specified duration to the time of the object write.

If your request to place an object version in a bucket contains an explicit retention mode and *Retain Until Date*, those settings override any bucket default settings for that object version.
{: note}

Like all other Object Lock settings, the *Retain Until Date* applies to individual object versions. Different versions of a single object can have different retention modes and periods.

Imagine an object that is 60 days into a 90-day retention period, and you overwrite that object with the same name and a two-year retention period. The operation succeeds and a new version of the object with a two-year retention period is created. Meanwhile, after 30 more days the original version is eligible for deletion.

### Extending a retention period
{: #ol-terminology-retention-period-extend}

To extend the retention period of an object, send a request to set a longer retention period. The old value is overwritten with the new value, assuming the requester has the `cloud-object-storage.object.put_object_lock_retention` and `cloud-object-storage.object.put_object_lock_retention_version` actions.

### Legal hold
{: #ol-terminology-legal-hold}

A *legal hold* is like a retention period in that it prevents an object version from being overwritten or deleted. However, legal holds are more flexible and don't have a defined temporal component. Instead, they remain in effect until removed. Legal holds can be freely placed and removed by any user who has the `cloud-object-storage.object.put_object_lock_legal_hold` and `cloud-object-storage.object.put_object_lock_legal_hold_version` actions.

Legal holds have the additional benefit of acting as a method for applying indefinite retention on an object.

Legal holds and retention periods operate independently. Legal holds have no impact on retention periods, and vice-versa.

Imagine an object with both a legal hold and a retention period. When the retention period ends, the object version remains protected until the legal hold is removed. If you remove a legal hold while an object version is subject to a retention period, it remains protected until the retention period is over.

Objects that are locked and stored with a retention period cannot be deleted until the retention period expires and any associated legal hold is removed.
{: important}

Locking objects with a Governance Mode is not supported.
{: note}

## Using Object Lock for business continuity and disaster recovery
{: #ol-bcdr}

Object Lock can be used to provide continuity of service during or after a ransomware attack, as protected data is impossible to be modified or destroyed.

## Consistency and data integrity
{: #ol-consistency}

While {{site.data.keyword.cos_full_notm}} provides strong consistency for all data IO operations, bucket configuration is eventually consistent. It can take a few moments for the configuration to propagate across the system after a default retention period on a bucket is enabled, modified, or deleted. Operations on objects, such as adding a legal hold, are immediately consistent.

## Usage and accounting
{: #ol-usage}

Locked objects (and their versions) contribute usage just like any other data and you are responsible for the [usage costs](/docs/cloud-object-storage?topic=cloud-object-storage-billing) while the object is locked with a retention period.

## Interactions
{: #ol-interactions}

Object Lock can be used in combination with several {{site.data.keyword.cos_short}} features per your use case requirements.

### Versioning
{: #ol-interactions-versioning}

[Enabling versioning](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) is a prerequisite for enabling Object Lock. If a bucket is created by using the `x-amz-bucket-object-lock-enabled` header, versioning is automatically enabled.

Deleting a versioned object creates a _delete marker_. The object might appear to be deleted, but if the object is protected it is impossible to delete the protected version. Delete markers themselves are unprotected.
{: remember}

### Replication
{: #ol-interactions-replication}

Object Lock **cannot be used on the source bucket** for replication, only on the destination. Objects are assigned the default retention period.

### Key Management Systems
{: #ol-interactions-kms}

Protected objects are encrypted by using the root key of the bucket. When Object Lock is enabled on a bucket, the root key that is hosted by Key Protect or Hyper Protect Crypto Services is protected from deletion while an associated bucket has Object Lock enabled. This feature prevents crypto shredding of protected objects.

### Lifecycle configurations
{: #ol-interactions-lifecycle}

It is possible to enable lifecycle policies that [archive locked objects](/docs/cloud-object-storage?topic=cloud-object-storage-archive). However, policies that [expire objects](/docs/cloud-object-storage?topic=cloud-object-storage-expiry) under retention or legal hold (unprotected objects in the bucket can still be expired) cannot be enabled.

### Immutable {{site.data.keyword.cos_short}}
{: #ol-interactions-worm}

Object Lock is an alternative to the retention policies available when you use Immutable {{site.data.keyword.cos_short}}. Object Lock requires the versioning feature to be enabled. However, Immutable {{site.data.keyword.cos_short}} is not compatible with versioning. So it isn't possible to enable both WORM solutions on the same bucket. However, it is possible to have a mix of buckets in a Service Instance, each bucket can use either Immutable {{site.data.keyword.cos_short}} or Object Lock.

### Object Tagging
{: #ol-interactions-tagging}

No restrictions exist for adding or modifying tags on a protected object.

### Other interactions
{: #ol-interactions-worm}

Using Object Lock with other {{site.data.keyword.cos_short}} features, such as setting CORS policies, setting IP firewalls or condition-based restrictions, bucket quotas, or Code Engine, does not result in adverse interactions.

## IAM actions
{: #ol-iam}

The following table shows the Object Lock related actions that a user is authorized to do based on their IAM role.

| IAM Action                                                       | Role                    |
|------------------------------------------------------------------|-------------------------|
| `cloud-object-storage.bucket.get_object_lock_configuration`      | Manager, Writer, Reader |
| `cloud-object-storage.bucket.put_object_lock_configuration`      | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_retention`          | Manager, Writer, Reader |
| `cloud-object-storage.object.put_object_lock_retention`          | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_retention_version`  | Manager, Writer, Reader |
| `cloud-object-storage.object.put_object_lock_retention_version`  | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_legal_hold`         | Manager, Writer, Reader |
| `cloud-object-storage.object.put_object_lock_legal_hold`         | Manager, Writer         |
| `cloud-object-storage.object.get_object_lock_legal_hold_version` | Manager, Writer, Reader |
| `cloud-object-storage.object.put_object_lock_legal_hold_version` | Manager, Writer         |
{: caption="Table 1. IAM roles that are associated with Object Lock actions." caption-side="bottom"}

Users with the **Writer** role can make an object undeletable for many years (possibly thousands of years). Consider crafting custom roles that do not allow most users to set a *Retain Until Date*.
{: attention}

## Activity Tracker events
{: #ol-at}

Object Lock generates the following events.

- `cloud-object-storage.bucket-object-lock.create`
- `cloud-object-storage.bucket-object-lock.read`
- `cloud-object-storage.object-object-lock-legal-hold.create`
- `cloud-object-storage.object-object-lock-legal-hold.read`
- `cloud-object-storage.object-object-lock-retention.create`
- `cloud-object-storage.object-object-lock-retention.read`

For `cloud-object-storage.bucket-object-lock.create` events, the following fields provide extra information:

| Field                                                         | Description                           |
|---------------------------------------------------------------|---------------------------------------|
| `requestData.object_lock_configuration.enabled`               | Indicates that Object Lock is enabled on the bucket. |
| `requestData.object_lock_configuration.defaultRetention.mode` | Indicates `COMPLIANCE` mode is active - `GOVERNANCE` mode is not yet supported. |
| `object_lock_configuration.defaultRetention.years`            | The default retention period in years.|
| `object_lock_configuration.defaultRetention.days`             | The default retention period is in days. |
{: caption="Table 2. Activity Tracker event fields for lock creation events." caption-side="bottom"}

The `object_lock_configuration.defaultRetention.years` or `object_lock_configuration.defaultRetention.days` fields are mutually exclusive, both can't be present at the same time.
{: note}

For operations on protected objects, the following fields can be present:

| Field                                                            | Description            |
|------------------------------------------------------------------|------------------------|
| `requestData.object_lock_protection.legal_hold`                  | Indicates that a legal hold is in force on the object version.|
| `requestData.object_lock_protection.retention.mode`              | Indicates `COMPLIANCE` mode is active on the object version - `GOVERNANCE` mode is not yet supported. |
| `requestData.object_lock_protection.retention.retain_until_date` | Indicates the date when the object version is eligible for deletion. After this date, the object is no longer delete protected based on a retention date. |
{: caption="Table 3. Activity Tracker event fields for operations on protected objects." caption-side="bottom"}

## Before you begin
{: #ol-gs}

Before you can start working with this feature, ensure that the following prerequisites are met.

- You need the `Writer` or `Manager` platform role on a bucket, or a custom role with the appropriate actions (such as `cloud-object-storage.bucket.put_object_lock_configuration`).
- Object Versioning must be enabled.
- You need to use a Standard pricing plan. For more information, see [pricing](/docs/cloud-object-storage?topic=cloud-object-storage-billing) for details.
- You need to pick a region where Object Lock is supported. For more information, see [Integrated Services](/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).

A maximum default retention period of 100 years (or 36500 days) is supported.

The retention period for an object **cannot be decreased**. If you are using default retention for validation testing, use a shorter duration (such as 1 day) as the default retention, then increase it as needed. You can specify the retention period in days or years in the console, with the API, SDKs, or Terraform. In the console, it is also possible to set a *Retain Until Date* in months.
{: tip}

## Working with Object Lock in the console
{: #ol-gs-console}
{: ui}

### Creating and setting up your new bucket for use with Object Lock
{: #ol-gs-new}

1. Go to your {{site.data.keyword.cos_short}} instance and use **Create Bucket** with _Customize your bucket option_.
2. Enter the required bucket configuration details per your use case requirements.
3. Go to the _Object Versioning_ section and set it to **Enabled**.
4. Look for **Immutability**, and under Object Lock, click **Add**.
5. Set Object Lock to **Enabled**.
6. Optionally, set a default retention period.
7. Click **Save**.
8. Proceed with rest of the configuration settings and click **Create bucket**.

### Enabling Object Lock on an existing bucket:
{: #ol-gs-existing}

A bucket can be set for Object Lock use by the following steps.

1. Go to your bucket **Configuration** section.
2. Click **Object Versioning**.
3. At the _Object Versioning_ section click **Edit**, then set the configuration option to **Enabled** and click **Save**.
4. Go to the _Object Lock_ section, click **Add**.
5. Set _Object Lock_ to **Enabled**
6. Optionally, set a default retention period.
7. Click **Save**.

### Adding a Retain Until Date or Legal hold to an object
{: #ol-gs-object}

1. Go to the bucket with the target object.
2. Toggle **Display Versions**.
3. Go to the details of the target version.
4. Add a retention period or toggle a legal hold. You can have both.

## Working with Object Lock with REST API
{: #ol-apis-examples}
{: api}

The following examples are shown for using cURL for ease of use. Environment variables are used to represent user-specific elements such as `$BUCKET`, `$TOKEN`, and `$REGION`. The `$REGION` variable also includes any network type specifications, so sending a request to a bucket in `us-south` by using the private network would require setting the variable to `private.us-south`.

### Enable object lock on a bucket
{: #ol-apis-enable}

The Object Lock configuration is provided as XML in the body of the request. New requests overwrite any existing replication rules that are present on the bucket.

An Object Lock configuration must include one rule.

| Header                    | Type   | Description |
|-------------------------|--------|--------------------------------------------------------------|
|`Content-MD5` | String | **Required**: The Base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.|
{: caption="Table 4. Header information for enabling object lock on a bucket." caption-side="bottom"}

The body of the request must contain an XML block with the following schema:

| Element                   | Type      | Children                    | Ancestor                  | Constraint                  |
|---------------------------|-----------|-----------------------------|---------------------------|-----------------------------|
| `ObjectLockConfiguration` | Container | `ObjectLockEnabled`, `Rule` | None                      | **Required**: Limit 1.      |
| `ObjectLockEnabled`       | String    | None                        | `ObjectLockConfiguration` | **Required**: The only valid value is `Enabled` (case-sensitive). |
| `Rule`                    | Container | `DefaultRetention`          | `ObjectLockConfiguration` | Limit 1.  |
| `DefaultRetention`        | Container | `Days`, `Mode`, `Years`     | `Rule`                    | Limit 1.        |
| `Days`                    | Integer   | None                        | `DefaultRetention`        | The number of days that you want to specify for the default retention period. It cannot be combined with `Years`. |
| `Mode`                    | String    | None                        | `DefaultRetention`        | Only `COMPLIANCE` is supported currently (case-sensitive). |
| `Years`                   | Integer   | None                        | `DefaultRetention`        | The number of years that you want to specify for the default retention period. It cannot be combined with `Days`. |
{: caption="Table 5. Required details for lock creation." caption-side="bottom"}

The following example retains any new objects for at least 30 days.

```sh
curl -X "PUT" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?object-lock" \
     -H 'Authorization: bearer $TOKEN' \
     -H 'Content-MD5: exuBoz2kFBykNwqu64JZuA==' \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'<ObjectLockConfiguration xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
            <ObjectLockEnabled>Enabled</ObjectLockEnabled>
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

```sh
curl -X "GET" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?object-lock" \
     -H 'Authorization: bearer $TOKEN'
```

The example returns an XML response body with the appropriate schema:

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

### Add or extend a retention period for an object
{: #ol-apis-object-add}

The Object Lock configuration is provided as XML in the body of the request. New requests overwrite any existing replication rules that are present on the object, provided the `RetainUntilDate` is farther in the future than the current value.

| Header        | Type   | Description |
|---------------|--------|---------------------------------------------------------------------------------------------|
| `Content-MD5` | String | **Required**: The Base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit. |
{: caption="Table 6. Header details for adding or extending a retention period." caption-side="bottom"}

Optionally, you can specify the version for which to apply the `RetainUntilDate`.

### Optional query parameters
{: #ol-apis-object-add-version}

| Parameter   | Required? | Type   | Description |
|-------------|-----------|--------|-------------|
| `versionID` | Optional  | String | Version ID. |
{: caption="Table 7. Optional parameters for adding or extending a retention period." caption-side="bottom"}

The body of the request must contain an XML block with the following schema:

| Element           | Type      | Children                  | Ancestor    | Constraint             |
|-------------------|-----------|---------------------------|-------------|------------------------|
| `Retention`       | Container | `Mode`, `RetainUntilDate` | None        | **Required**: Limit 1. |
| `Mode`            | String    | None                      | `Retention` | **Required**: Only `COMPLIANCE` is supported currently (case-sensitive). |
| `RetainUntilDate` | String    | None                      | `Retention` | **Required**: The date after which an object is eligible for deletion in ISO8601 Date-Time Format. |
{: caption="Table 8. Required details for adding or extending a retention period." caption-side="bottom"}

The following example retains any new objects for at least until 12 March 2025.

```sh
curl -X "PUT" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?retention" \
     -H 'Authorization: Bearer $TOKEN' \
     -H 'Content-MD5: fT0hYstki6zUvEh7abhcTA==' \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'<Retention>
            <Mode>COMPLIANCE</Mode>
            <RetainUntilDate>2025-03-12T23:01:00.000Z</RetainUntilDate>
          </Retention>'
```

A successful request returns a `200` response.

If the `RetainUntilDate` value is not beyond any existing value, the operation fails with a `403 Access Denied`.

### Add or remove a legal hold for an object
{: #ol-apis-object-add-lh}

The Object Lock configuration is provided as XML in the body of the request. New requests overwrite any existing replication rules that are present on the object, provided the `RetainUntilDate` is farther in the future than the current value.

|Header                    | Type   | Description |
|--------------------------|--------|-------------|
|`Content-MD5` | String | **Required**: The Base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.|
{: caption="Table 9. Header details for adding or extending a legal hold." caption-side="bottom"}

The body of the request must contain an XML block with the following schema:

| Element     | Type      | Children | Ancestor    | Constraint                                          |
|-------------|-----------|----------|-------------|-----------------------------------------------------|
| `LegalHold` | Container | `Status` | None        | Limit 1.                                           |
| `Status`    | String    | None     | `LegalHold` | Supported values are `ON` or `OFF` (case-sensitive) |
{: caption="Table 10. Header details for adding or extending a retention period." caption-side="bottom"}

The following example retains any new objects indefinitely.

```sh
curl -X "PUT" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?legalHold&versionId=$VERSION_ID" \
     -H 'Authorization: Bearer $TOKEN' \
     -H 'Content-MD5: FMh6GxizXUBRaiDuB0vtgQ==' \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'<LegalHold>
            <Status>ON</Status>
          </LegalHold>'

```

A successful request returns a `200` response.

## SDK examples
{: #ol-sdks}

The following examples use the {{site.data.keyword.cos_full_notm}} SDKs for Python, Node.js, Go, and a Terraform script. The implementation of object versioning is compatible with any S3-compatible library or tool that allows for the setting of custom endpoints. Using third-party tools requires HMAC credentials to calculate AWS V4 signatures. For more information about HMAC credentials, see [the documentation](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).

### Python
{: #ol-sdks-python}

Enabling Object Lock by using the {{site.data.keyword.cos_full_notm}} SDK for Python can be done with the [low-level client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){: external} syntax.

By using a client:

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_botocore.exceptions import ClientError
from datetime import datetime, timedelta
import time

# Create a bucket with Object Lock enabled.
def create_bucket_with_objectlock(bucket_name):
        cos_cli.create_bucket(
            Bucket=bucket_name,
            ObjectLockEnabledForBucket=True,
        )
        print("Bucket: {0} created with objectlock enabled".format(bucket_name))

def objectlock_configuration_on_bucket(bucket_name):

    # Putting default retenion on the object storage bucket.
    default_retention_rule = {'DefaultRetention': {'Mode': 'COMPLIANCE', 'Years': 1}}
    object_lock_config = {'ObjectLockEnabled': 'Enabled', 'Rule': default_retention_rule}
    cos_cli.put_object_lock_configuration(Bucket=bucket_name, ObjectLockConfiguration=object_lock_config)
    # Reading the objectlock configuration set on the bucket.
    response = cos_cli.get_object_lock_configuration(Bucket=bucket_name)
    print("Objectlock Configuration for {0} =>".format(bucket_name))
    print(response.ObjectLockConfiguration)


def upload_object(bucket_name,object_name,object_content):
        cos_cli.put_object(
            Bucket=bucket_name,
            Key=object_name,
            Body=object_content
        )
        print("Object: {0} uploaded!".format(object_name))

def objectlock_retention(bucket_name,object_name):
        # Put objectlock retenion on the  object uploaded to the bucket.
        date = datetime.now()+timedelta(seconds=5)
        retention_rule = {'Mode': 'COMPLIANCE', 'RetainUntilDate': date}
        cos_cli.put_object_retention(Bucket=bucket_name, Key=object_name, Retention=retention_rule)

        # Get objectlock retention of the above object.
        response = cos_cli.get_object_retention(Bucket=bucket_name, Key=object_name)
        print("Objectlock Retention for {0}=>".format(object_name))
        print(response.Retention)


def objectlock_legalhold(bucket_name,object_name):
        # Setting the objectlock legalhold status to ON.
        cos_cli.put_object_legal_hold(Bucket=bucket_name, Key=object_name, LegalHold={'Status': 'ON'})
        # Get objectlock retention of the above object.
        response = cos_cli.get_object_legal_hold(Bucket=bucket_name, Key=object_name)
        print("Objectlock Legalhold for {0}=>".format(object_name))
        print(response.LegalHold)
       

COS_ENDPOINT = "" #Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints -> Ex:https://s3.us-south.cloud-object-storage.appdomain.cloud
COS_API_KEY_ID = "" #API Key of the object storage instance created Ex: W00YixxxxxxxxxxMB-odB-2ySfTrFBIQQWanc--P3byk
COS_RESOURCE_INSTANCE_CRN = "" #API key of object storage instance example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4

# Create client connection
cos_cli = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT,
    ibm_service_instance_id=COS_RESOURCE_INSTANCE_CRN, 
    ibm_auth_endpoint="https://iam.test.cloud.ibm.com/identity/token"  
)
new_bucket_name = "create-example-python12345" # bucket name must be unique globally, or it throws an error.
new_text_file_name = "cos_object.txt"
new_text_file_contents = "This is a test file from Python code sample!!!"

# *** Main Program ***
def main():
       create_bucket_with_objectlock(new_bucket_name) # Create a bucket with object lock enabled.
       objectlock_configuration_on_bucket(new_bucket_name) # Put objectlock configuration (the default retention) on the bucket and get the configuration.
       upload_object(new_bucket_name,new_text_file_name,new_text_file_contents) # Upload an object to the bucket.
       objectlock_retention(new_bucket_name,new_text_file_name) # Put objectlock retention (the retain until date) on the object and get the configured retention.
       objectlock_legalhold(new_bucket_name,new_text_file_name)  # Put objectlock legalhold on the object and get the legalhold status.
       
if __name__ == "__main__":
    main()

```

### Node.js
{: #ol-sdks-node}

Enabling versioning with [{{site.data.keyword.cos_full_notm}} SDK for Node.js](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putBucketVersioning-property).

```js
'use strict';

// Required libraries
const ibm = require('ibm-cos-sdk');
const fs = require('fs');
const crypto = require('crypto');

function logError(e) {
    console.log(`ERROR: ${e.code} - ${e.message}\n`);
}

function logDone() {
    console.log('DONE!\n');
}

const COS_ENDPOINT = "";   //Choose endpoint from https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints. For example, https://s3.us-south.cloud-object-storage.appdomain.cloud
const COS_API_KEY_ID = "";  // API key of object storageinstance example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
const COS_AUTH_ENDPOINT = "";
const COS_RESOURCE_INSTANCE_CRN = ""; // example: crn:v1:bluemix:public:cloud-object-storage:global:a <CREDENTIAL_ID_AS_GENERATED>:<SERVICE_ID_AS_GENERATED>::

// Client Creation.
var config = {
    endpoint: COS_ENDPOINT,
    apiKeyId: COS_API_KEY_ID,
    ibmAuthEndpoint: COS_AUTH_ENDPOINT,
    serviceInstanceId: COS_RESOURCE_INSTANCE_CRN,
    signatureVersion: 'iam'
};

var cos = new ibm.S3(config);

// Create a bucket with objectlock enabled.
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        ObjectLockEnabledForBucket: true,
        CreateBucketConfiguration: {
            LocationConstraint: ''
          },     
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}


// Create a text file and upload the object to the bucket.
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
        logDone();
    })
    .catch(logError);
}

function putObjectLockConfigurationonBucket(bucketName) { 
    console.log(`Putting Objectlock Configuration on : ${bucketName}`);
    // Putting objectlock configuration
    var defaultRetention = {Mode: 'COMPLIANCE', Days: 1}
    var objectLockRule = {DefaultRetention : defaultRetention}
    var param = {ObjectLockEnabled: 'Enabled', Rule: objectLockRule}
    return cos.putObjectLockConfiguration({
        Bucket: bucketName,
        ObjectLockConfiguration: param
    }).promise()
    .then(() => {
        console.log(`Object lock Configurtion added!!`);
        logDone();
    })
    .catch(logError);

}

function getObjectLockConfigurationonBucket(bucketName) {
    console.log(`Getting Objectlock Configuration for : ${bucketName}`);
    // Getting objectlock configuration
    return cos.getObjectLockConfiguration({
        Bucket: bucketName,
    }).promise()
    .then((data) => {
        console.log(`objectlock configuration`);
        console.log( JSON.stringify(data.ObjectLockConfiguration, null, "    ") );
        logDone();
    })
    .catch(logError);

}

function putObjectLockRetention(bucketName,keyName) {
    console.log(`Putting Objectlock Retention on : ${keyName}`);
    var inFiveSecond = (new Date(Date.now() + (1000 * 5)))
    var rule = {Mode: 'COMPLIANCE', RetainUntilDate: inFiveSecond}
     // Putting objectlock retention
    return cos.putObjectRetention({
        Bucket: bucketName,
        Key: keyName,
        Retention: rule
    }).promise()
    .then(() => {
        console.log(`Object lock Retention added!!`);
        logDone();
    })
    .catch(logError);

}

function getObjectLockRetention(bucketName,keyName) {
    console.log(`Getting Objectlock Retention for : ${keyName}`);
    // Getting objectlock retention
    return cos.getObjectRetention({
        Bucket: bucketName,
        Key: keyName
    }).promise()
    .then((data) => {
        console.log(`Objectlock retention for : ${keyName} `);
        console.log( JSON.stringify(data.Retention, null, "    ") );
        logDone();
    })
    .catch(logError);

}

function putObjectLockLegalhold(bucketName,keyName) {
    console.log(`Putting Objectlock Legalhold status ON for  : ${keyName}`);
     // Putting objectlock legalhold status
    return cos.putObjectLegalHold({
        Bucket: bucketName,
        Key: keyName,
        LegalHold: {Status: 'ON'}
    }).promise()
    .then(() => {
        console.log(`Object lock legalhold added!!`);
        logDone();
    })
    .catch(logError);

}

function getObjectLockLegalhold(bucketName,keyName) {
    console.log(`Getting Objectlock legalhold for : ${keyName}`);
    // Getting objectlock legalhold
    return cos.getObjectLegalHold({
        Bucket: bucketName,
        Key: keyName
    }).promise()
    .then((data) => {
        console.log(`Objectlock legalhold for : ${keyName} `);
        console.log( JSON.stringify(data.LegalHold, null, "    ") );
        logDone();
    })
    .catch(logError);

}


// Main app
function main() {
    try {
        var newBucketName = "jscosbucket350";
        var newTextFileName = "js_cos_bucket_file.txt";
        var newTextFileContents = "This is a test file from Node.js code sample!!!";

        createBucket(newBucketName) // Create an object storage bucket with object lock enabled.
        .then(() => putObjectLockConfigurationonBucket(newBucketName)) // Put objectlock configuration (the default retention) on object storage bucket.
        .then(() => getObjectLockConfigurationonBucket(newBucketName)) // Read objectlock configuration on object storage bucket.
        .then(() => createTextFile(newBucketName, newTextFileName, newTextFileContents)) // Upload an object to bucket.
        .then(() => putObjectLockRetention(newBucketName, newTextFileName)) // Put objectlock retention (the retain until date) on the object.
        .then(() => getObjectLockRetention(newBucketName, newTextFileName)) // Get the configured retention.
        .then(() => putObjectLockLegalhold(newBucketName,newTextFileName)) // Put objectlock legalhold on the object.
        .then(() => getObjectLockLegalhold(newBucketName,newTextFileName)); // Get the legalhold status.
    }
    catch(ex) {
        logError(ex);
    }
}

main();

```

### Go
{: #ol-sdks-go}

```go
package main

import (
	"bytes"
	"fmt"
	"time"

	"github.com/IBM/ibm-cos-sdk-go/aws"
	"github.com/IBM/ibm-cos-sdk-go/aws/credentials/ibmiam"
	"github.com/IBM/ibm-cos-sdk-go/aws/session"
	"github.com/IBM/ibm-cos-sdk-go/service/s3"
)

const (
	apiKey            = "<apiKey>"
	serviceInstanceID = "<serviceInstanceID>"
	authEndpoint      = "https://iam.cloud.ibm.com/identity/token"
	serviceEndpoint   = "https://<endpoint>.appdomain.cloud"
)

// Create a bucket with objectlock enabled.
func createBucket(bucketName string, client *s3.S3) {
	createBucketInput := new(s3.CreateBucketInput)
	createBucketInput.Bucket = aws.String(bucketName)
	createBucketInput.ObjectLockEnabledForBucket = aws.Bool(true)
	_, e := client.CreateBucket(createBucketInput)
	if e != nil {
		fmt.Println(e)
	} else {
		fmt.Println("Bucket Created !!! ")
	}
}

func uploadObject(bucketName string, client *s3.S3, fileName string, fileContent string) {
	putInput := &s3.PutObjectInput{
		Bucket: aws.String(bucketName),
		Key:    aws.String(fileName),
		Body:   bytes.NewReader([]byte(fileContent)),
	}

	_, e := client.PutObject(putInput)
	if e != nil {
		fmt.Println(e)
	} else {
		fmt.Println("Object Uploaded!!! ")
	}
}

func objectLockConfiguration(bucketName string, client *s3.S3) {
	// Putting default retenion on the object storage bucket.
	putObjectLockConfigurationInput := &s3.PutObjectLockConfigurationInput{
		Bucket: aws.String(bucketName),
		ObjectLockConfiguration: &s3.ObjectLockConfiguration{
			ObjectLockEnabled: aws.String(s3.ObjectLockEnabledEnabled),
			Rule: &s3.ObjectLockRule{
				DefaultRetention: &s3.DefaultRetention{
					Mode: aws.String("COMPLIANCE"),
					Days: aws.Int64(1),
				},
			},
		},
	}
	_, e := client.PutObjectLockConfiguration(putObjectLockConfigurationInput)

	// Reading the objectlock configuration that is set on the bucket.
	getObjectLockConfigurationInput := new(s3.GetObjectLockConfigurationInput)
	getObjectLockConfigurationInput.Bucket = aws.String(bucketName)
	response, e := client.GetObjectLockConfiguration(getObjectLockConfigurationInput)
	if e != nil {
		fmt.Println(e)
	} else {
		fmt.Println("Object Lock Configuration =>", response.ObjectLockConfiguration)
	}
}

func objectLockRetention(bucketName string, client *s3.S3, keyName string) {

	// Put objectlock retenion on the object that is uploaded to the bucket.
	retention_date := time.Now().Local().Add(time.Second * 5)
	putObjectRetentionInput := &s3.PutObjectRetentionInput{
		Bucket: aws.String(bucketName),
		Key:    aws.String(keyName),
		Retention: &s3.ObjectLockRetention{
			Mode:            aws.String("COMPLIANCE"),
			RetainUntilDate: aws.Time(retention_date),
		},
	}
	_, e := client.PutObjectRetention(putObjectRetentionInput)

	// Get objectlock retention of the uploaded object.
	getObjectRetentionInput := new(s3.GetObjectRetentionInput)
	getObjectRetentionInput.Bucket = aws.String(bucketName)
	getObjectRetentionInput.Key = aws.String(keyName)
	response, e := client.GetObjectRetention(getObjectRetentionInput)
	if e != nil {
		fmt.Println(e)
	} else {
		fmt.Println("Object Lock Retention =>", response.Retention)
	}
}

func objectLockLegalHold(bucketName string, client *s3.S3, keyName string) {

	// Setting the objectlock legalhold status to ON.
	putObjectLegalHoldInput := &s3.PutObjectLegalHoldInput{
		Bucket: aws.String(bucketName),
		Key:    aws.String(keyName),
		LegalHold: &s3.ObjectLockLegalHold{
			Status: aws.String("ON"),
		},
	}
	_, e := client.PutObjectLegalHold(putObjectLegalHoldInput)
	// Get objectlock retention of the uploaded object.
	getObjectLegalHoldInput := new(s3.GetObjectLegalHoldInput)
	getObjectLegalHoldInput.Bucket = aws.String(bucketName)
	getObjectLegalHoldInput.Key = aws.String(keyName)
	response, e := client.GetObjectLegalHold(getObjectLegalHoldInput)
	if e != nil {
		fmt.Println(e)
	} else {
		fmt.Println("Object Lock legalhold =>", response.LegalHold)
	}
}

func main() {

	bucketName := "gocosbucket353"
	textFileName := "go_cos_bucket_file.txt"
	textFileContents := "This is a test file from Node.js code sample!!!"
	conf := aws.NewConfig().
		WithEndpoint(serviceEndpoint).
		WithCredentials(ibmiam.NewStaticCredentials(aws.NewConfig(),
			authEndpoint, apiKey, serviceInstanceID)).
		WithS3ForcePathStyle(true)

	sess := session.Must(session.NewSession())
	client := s3.New(sess, conf)
	createBucket(bucketName, client)                                 // Create a object storage bucket with object lock enabled.
	objectLockConfiguration(bucketName, client)                      // Put objectlock configuration (the default retention) on object storage bucket and get the configuration.
	uploadObject(bucketName, client, textFileName, textFileContents) // Upload an object to object storage bucket.
	objectLockRetention(bucketName, client, textFileName)            // Put objectlock retention (the retain until date) on the object and get the configured retention.
	objectLockLegalHold(bucketName, client, textFileName)            // Put objectlock legalhold on the object and get the legalhold status.

}
```

### Terraform
{: #ol-sdks-terraform}
{: terraform}

The following example creates an {{site.data.keyword.cos_full_notm}} instance, and a bucket with object lock and versioning. It specifies the configuration details for the object lock, and uploads a new object with a legal hold.

```terraform
// Create Cloud Object Storage instance.
resource "ibm_resource_instance" "cos_instance" {
  name              = "cos-instance"
  resource_group_id = data.ibm_resource_group.cos_group.id
  service           = "cloud-object-storage"
  plan              = "standard"
  location          = "global"
}

// Create a bucket with objectlock and object versioning enabled.
resource "ibm_cos_bucket" "bucket" {
  bucket_name           = var.bucket_name
  resource_instance_id  = ibm_resource_instance.cos_instance.id
  region_location  = var.regional_loc
  storage_class          = var.standard_storage_class
  object_versioning {
    enable  = true
  }
  object_lock = true
}

// Set object lock configuration on the bucket by providing the crn of the new bucket.
resource ibm_cos_bucket_objectlock_configuration "objectlock" {
 bucket_crn      = ibm_cos_bucket.bucket.crn
 bucket_location = var.regional_loc
 object_lock_configuration{
   objectlockenabled = "Enabled"
   objectlockrule{
     defaultretention{
        mode = "COMPLIANCE"
        days = 6
      }
    }
  }
}

// Upload an object to the object storage bucket with objectlock retention and objectlock legalhold.
resource "ibm_cos_bucket_object" "object_object_lock" {
  bucket_crn      = ibm_cos_bucket.bucket.crn
  bucket_location = ibm_cos_bucket.bucket.region_location
  content         = "Hello World 2"
  key             = "plaintext5.txt"
  object_lock_mode              = "COMPLIANCE"
  object_lock_retain_until_date = "2025-02-15T18:00:00Z"
  object_lock_legal_hold_status = "ON"
  force_delete = true
}
```

For more information about the arguments and attributes, see [ibm_cos_bucket](https://registry.terraform.io/providers/IBM-Cloud/ibm/latest/docs/resources/cos_bucket){: external}.
