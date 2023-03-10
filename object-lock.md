---

copyright:
  years: 2023

lastupdated: "2023-03-10"

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

Object Lock helps customers govern data preservation and retention requirements by enforcing data immutability for their backup, disaster recovery, and cyber resiliency workloads. 

When using Object Lock, it is  your responsibility to ensure compliance with any regulations that you (your organization) may be subject to when it comes to preservation and storage of data for long term retention.
{:important}

Important: When using Object Lock, you are responsible for ensuring that your IBM Cloud Account is kept in good standing per IBM Cloud policies and guidelines for as long as the data is subject to a retention period. Refer to IBM Cloud Service terms for more information. 
{:important}

## Terminology
{: #ol-terminology}

There are two ways use Object Lock to protect data: *retention periods* and *legal holds*.

- A **retention period** defines a time frame during which an object is unable to be modified or deleted. 
- A **legal hold** also prevents an object from being altered, but only remains in place until it is explicitly lifted. 

It is possible to make use of any combination of these parameters - an object version can have one, both, or neither.

### Retain Until Date (Retention Period)
{: #ol-terminology-retention-period}

If you need to protect an object version for a fixed amount of time, you need to specify a *retention period*. The object version can be be deleted after this period expires (assuming there are no legal holds on the object version).

The retention period for new objects can be inherited from the default value set on the bucket, or it can be explicitly defined when writing the object by specifying a *Retain Until Date*. 

When you use bucket default settings, you don’t specify a Retain Until Date. Instead, you specify a duration, in either days or years, for which every object version placed in the bucket should be protected. When you place an object in the bucket, a Retain Until Date is calculated for the object version by adding the specified duration to the time of the object write.

If your request to place an object version in a bucket contains an explicit retention mode and period, those settings override any bucket default settings for that object version.
{:note}

Like all other Object Lock settings, retention periods apply to individual object versions. Different versions of a single object can have different retention modes and periods.

Imagine an object that is 60 days into a 90-day retention period, and you overwrite that object with the same name and a two year retention period. The operation will succeed and a new version of the object with a two year retention period is created. Meanwhile, after 30 more days the original version is eligible for deletion.

### Extending a retention period
{: #ol-terminology-retention-period-extend}

To extend the retention period of an object, simply send a request to set a new, longer, retention period.  The old value will be overwritten with the new, assuming the requestor has the `cloud-object-storage.object.put_object_lock_retention` and `cloud-object-storage.object.put_object_lock_retention_version` actions.

### Legal Hold
{: #ol-terminology-legal-hold}

A *legal hold* is ike a retention period in that it prevents an object version from being overwritten or deleted. However, legal holds are more flexible and don't have a defined temporal component. Instead they simply remain in effect until removed. Legal holds can be freely placed and removed by any user who has the `cloud-object-storage.object.put_object_lock_legal_hold` and `cloud-object-storage.object.put_object_lock_legal_hold_version` actions. 

Legal holds and retention periods operate independently. Legal holds have no impact on retention periods, and vice-versa.

Imagine an object with both a legal hold and a retention period. When the retention period ends, the object version remains protected until the legal hold is removed. If you remove a legal hold while an object version is subject to a retention period it remains protected until the retention period is complete.

Objects locked and stored with a retention period cannot be deleted until retention period expires and any associated legal hold is removed. 
{:important}

## Getting started with Object Lock
{: #ol-gs}

In order to get started, there are some some prerequisites: 

- You'll need the `Writer` or `Manager` platform role on a bucket, or a custom role with the appropriate actions (such as `cloud-object-storage.bucket.put_object_lock_configuration`) assigned.
- The bucket must be empty.
- Object Versioning must be enabled
- You will need to use Standard pricing plan, see pricing for details, 
- You will need to pick a region where Object Lock is supported, refer to Integrated Services for details.

### Creating and setting up your new bucket for use with Object Lock
{: #ol-gs-new}

1. Navigate to your desired Object Storage instance and use **Create Bucket** with _Customize your bucket option_
2. Enter the required bucket configuration details as per your use case requirements
3. Navigate to the _Object Versioning_ section and set it to **Enabled** 
4. Look for **Immutability**,  and under Object Lock click **Add**
5. Set Object Lock to **Enabled**
6. Optionally, set a default retention period.
7. Click on Save 
8. Proceed with rest of the configuration settings and click **Create bucket**

### Enabling Object Lock on an existing (empty) bucket:
{: #ol-gs-existing}

An empty bucket can be set for Object Lock use as follows:

1. Navigate to your bucket **Configuration** section 
2. Click on **Object Versioning**
3. At the _Object Versioning_ section click on **Edit**, set the configuration option to **Enabled** and **Save**
4. Navigate to _Object Lock_ section, click on **Add**
5. Set _Object Lock_ to **Enabled**
6. Optionally, set a default retention period.
7. Click on **Save** 

## Using Object Lock for business continuity and disaster recovery
{: #ol-bcdr}

Object Lock can be used to provide continuity of service in the event of a ransomware attack, as protected data is unable to be modified or destroyed.

## Consistency and data integrity
{: #ol-consistency}

While IBM Cloud Object Storage provides strong consistency for all data IO operations, bucket configuration is eventually consistent. After enabling a default retention period on a bucket, it may take a few moments for the configuration to propagate across the system and new objects to be assigned the new default. 

## Usage and accounting
{: #ol-usage}

Locked objects (and their versions) contribute usage just like any other data and you will be responsible for the [usage costs](/docs/cloud-object-storage?topic=cloud-object-storage-billing) for as long as object remains locked with a retention period. 

## Interactions
{: #ol-interactions}

Object Lock can be used in combination with several object storage features as per your use case requirements. 

### Versioning
{: #ol-interactions-versioning}

[Enabling versioning](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) is a prerequisite for enabling Object Lock. If a bucket is created using the `x-amz-bucket-object-lock-enabled` header, versioning will automatically be enabled.  

### Replication
{: #ol-interactions-replication}

Object Lock can only be used on the source bucket for replication, only as the destination.  Objects will be assigned the default retention period.

### Key Management Systems
{: #ol-interactions-kms}

Protected objects will be [encrypted using the root key](/docs/key-protect?topic=key-protect-about) of the bucket. Ensure that the key remains valid in order to prevent unintended crypto-shredding of protected objects.  

### Lifecycle configurations
{: #ol-interactions-lifecycle}

It is possible to enable [lifecycle policies](/docs/cloud-object-storage?topic=cloud-object-storage-expiry#expiry-rules-attributes) that archive locked objects, but naturally not those that expire objects.  

### Immutable Object Storage
{: #ol-interactions-worm}

Object Lock is an alternative to the retention policies available when using Immutable Object Storage.  As Object Lock requires versioning to be enabled, and Immutable Object Storage is not compatible with versioning, it is not possible to have both WORM solutions enabled on the same bucket.  

### Other interactions
{: #ol-interactions-worm}

There should be no adverse interactions when using Object Lock with other Object Storage features, such as setting CORS policies, setting IP firewalls or condition based restrictions, bucket quotas, replication, or Code Engine.

## IAM actions
{: #ol-iam}

There are new IAM actions associated with Object Lock. 

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
| `ObjectLockEnabled`       | String    | None                        | `ObjectLockConfiguration` | The only valid value is `Enabled` (case-sensitive).                                                                             |
| `Rule`                    | Container | `DefaultRetention`          | `ObjectLockConfiguration` | Limit 1                                                                                                        |
| `DefaultRetention`        | Container | `Days`, `Mode`, `Years`     | `Rule`                    | Limit 1.                                                                                                       |
| `Days`                    | Integer   | None                        | `DefaultRetention`        | The number of days that you want to specify for the default retention period. Cannot be combined with `Years`. |
| `Mode`                    | String    | None                        | `DefaultRetention`        | Only `COMPLIANCE` is supported at this time (case-sensitive).                                                                  |
| `Years`                   | Integer   | None                        | `DefaultRetention`        | The number of years that you want to specify for the default retention period. Cannot be combined with `Days`. |

This example will retain any new objects for at least 30 days.  

```
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

### Add or extend a retention period for an object
{: #ol-apis-object-add}

The Object Lock configuration is provided as XML in the body of the request.  New requests will overwrite any existing replication rules that are present on the object, provided the `RetainUntilDate` is farther in the future than the current value.



Header                    | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | String | **Required**: The base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.

The body of the request must contain an XML block with the following schema:

| | Element           | Type      | Children                  | Ancestor    | Constraint                                                                           |
 |-------------------|-----------|---------------------------|-------------|--------------------------------------------------------------------------------------|
 | `Retention`       | Container | `Mode`, `RetainUntilDate` | None        | Limit 1.                                                                             |
 | `Mode`            | String    | None                      | `Retention` | Only `COMPLIANCE` is supported at this time (case-sensitive).                        |
 | `RetainUntilDate` | String    | None                      | `Retention` | The date after which an object is eligible for deletion in ISO8601 Date-Time Format. |

This example will retain any new objects for at least until March 12, 2023.  

```
curl -X "PUT" "https://$BUCKET.s3.$REGION.cloud-object-storage.appdomain.cloud/?object-lock" \
     -H 'Authorization: Bearer $TOKEN' \
     -H 'Content-MD5: fT0hYstki6zUvEh7abhcTA==' \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'<Retention>
            <Mode>COMPLIANCE</Mode>
            <RetainUntilDate>2023-0312T23:01:00.000Z</RetainUntilDate>
          </Retention>'
```

A successful request returns a `200` response.

### Add or remove a legal hold for an object
{: #ol-apis-object-add}

The Object Lock configuration is provided as XML in the body of the request.  New requests will overwrite any existing replication rules that are present on the object, provided the `RetainUntilDate` is farther in the future than the current value.


Header                    | Type   | Description
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | String | **Required**: The base64 encoded 128-bit MD5 hash of the payload, which is used as an integrity check to ensure that the payload wasn't altered in transit.

The body of the request must contain an XML block with the following schema:

| Element     | Type      | Children | Ancestor    | Constraint                                          |
|-------------|-----------|----------|-------------|-----------------------------------------------------|
| `LegalHold` | Container | `Status` | None        | Limit 1.                                            |
| `Status`    | String    | None     | `LegalHold` | Supported values are `ON` or `OFF` (case-sensitive) |

This example will retain any new objects for at least until March 12, 2023.  

```
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

The following examples make use of the IBM COS SDKs for Python, Node.js, and Go, as well as a Terraform script, although the implementation of object versioning should be fully compatible with any S3-compatible library or tool that allows for the setting of custom endpoints.  Using third-party tools requires HMAC credentials in order to calculate AWS V4 signatures.  For more information on HMAC credentials, [see the documentation](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main).  

### Python
{: #ol-sdks-python}

Enabling Object Lock using the IBM COS SDK for Python can be done using the [low-level client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client) syntax.

Using a client:

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_botocore.exceptions import ClientError
from datetime import datetime, timedelta
import time

# Create new bucket with Object Lock enabled.
def create_bucket_with_objectlock(bucket_name):
        cos_cli.create_bucket(
            Bucket=bucket_name,
            ObjectLockEnabledForBucket=True,
        )
        print("Bucket: {0} created with objectlock enabled".format(bucket_name))

def objectlock_configuration_on_bucket(bucket_name):

    # Putting default retenion on the COS bucket.
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
COS_API_KEY_ID = "" #API Key of the cos instance created Ex: W00YixxxxxxxxxxMB-odB-2ySfTrFBIQQWanc--P3byk
COS_RESOURCE_INSTANCE_CRN = "" #API key of cos instance example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4

# Create client connection
cos_cli = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT,
    ibm_service_instance_id=COS_RESOURCE_INSTANCE_CRN, 
    ibm_auth_endpoint="https://iam.test.cloud.ibm.com/identity/token"  
)
new_bucket_name = "create-example-python12345" # bucket name should be unique gloablly, or else it will throw an error.
new_text_file_name = "cos_object.txt"
new_text_file_contents = "This is a test file from Python code sample!!!"

# *** Main Program ***
def main():
       create_bucket_with_objectlock(new_bucket_name) # Create a new cos bucket with object lock enabled.
       objectlock_configuration_on_bucket(new_bucket_name) # Put objectlock configuration(i.e. default retention) on COS bucket and get the configuration.
       upload_object(new_bucket_name,new_text_file_name,new_text_file_contents) # Upload an object to cos bucket.
       objectlock_retention(new_bucket_name,new_text_file_name) # Put objectlock retention(i.e. retain until date) on the object and get the configured retention.
       objectlock_legalhold(new_bucket_name,new_text_file_name)  # Put objectlock legalhold on the object and get the legalhold status.
       
if __name__ == "__main__":
    main()

```

### Node.js
{: #ol-sdks-node}

Enabling versioning using the [IBM COS SDK for Node.js](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putBucketVersioning-property):

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


const COS_ENDPOINT = "";   //Choose endpoint from https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints. Ex: https://s3.us-south.cloud-object-storage.appdomain.cloud
const COS_API_KEY_ID = "";  // API key of cos instance example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
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

// Create new bucket with objectlock enabled.
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


// Create new text file and upload the object to COS bucket.
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

        createBucket(newBucketName) // Create a new cos bucket with object lock enabled.
        .then(() => putObjectLockConfigurationonBucket(newBucketName)) // Put objectlock configuration(i.e. default retention) on COS bucket.
        .then(() => getObjectLockConfigurationonBucket(newBucketName)) // Read objectlock configuration on COS bucket.
        .then(() => createTextFile(newBucketName, newTextFileName, newTextFileContents)) // Upload an object to cos bucket.
        .then(() => putObjectLockRetention(newBucketName, newTextFileName)) // Put objectlock retention(i.e. retain until date) on the object.
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

// Create new bucket with objectlock enabled.
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
	// Putting default retenion on the COS bucket.
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

	// Reading the objectlock configuration set on the bucket.
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

	// Put objectlock retenion on the  object uploaded to the bucket.
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

	// Get objectlock retention of the above object.
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
	// Get objectlock retention of the above object.
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
	createBucket(bucketName, client)                                 // Create a new cos bucket with object lock enabled.
	objectLockConfiguration(bucketName, client)                      // Put objectlock configuration(i.e. default retention) on COS bucket and get the configuration.
	uploadObject(bucketName, client, textFileName, textFileContents) // Upload an object to cos bucket.
	objectLockRetention(bucketName, client, textFileName)            // Put objectlock retention(i.e. retain until date) on the object and get the configured retention.
	objectLockLegalHold(bucketName, client, textFileName)            // Put objectlock legalhold on the object and get the legalhold status.

}
```

### Terraform
{: #ol-sdks-terraform}

```json


// Create COS instance.
resource "ibm_resource_instance" "cos_instance" {
  name              = "cos-instance"
  resource_group_id = data.ibm_resource_group.cos_group.id
  service           = "cloud-object-storage"
  plan              = "standard"
  location          = "global"
}

// Create a new bucket with objectlock and object versioning enabled.
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


// Set object lock configuration on the bucket by providing the crn of the new COS bucket.
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

// Upload an object to the COS bucket with objectlock retention and objectlock legalhold.
resource "ibm_cos_bucket_object" "object_object_lock" {
  bucket_crn      = ibm_cos_bucket.bucket.crn
  bucket_location = ibm_cos_bucket.bucket.region_location
  content         = "Hello World 2"
  key             = "plaintext5.txt"
  object_lock_mode              = "COMPLIANCE"
  object_lock_retain_until_date = "2023-02-15T18:00:00Z"
  object_lock_legal_hold_status = "ON"
  force_delete = true
}
```
