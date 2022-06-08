---

copyright:
  years: 2022
lastupdated: "2022-06-15"

keywords: data, replication, loss prevention

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Replicating objects
{: #replication-overview}

Bucket replication allows  users to define rules for automatic, asynchronous copying of objects from source bucket to one or many destination buckets in the same or different locations. 
{: shortdesc}

## What is replication?
{: #replication-what}

Replication copies newly created objects and object updates from a source bucket to one or more destination buckets.

- Only new objects or new versions of the existing objects (created after the replication rule) are copied to the destination bucket.
- The metadata of the source object is applied to the replicated object.
- Bi-directional replication rules between two buckets requires rules to be active on both buckets.
- Filters (prefix, tags) can be used to scope the replication rule to only apply to a subset of objects in a bucket.

## Why use replication?
{: #replication-why}

- Keep copies of data across buckets in multiple geographic locations, or even multiple Cross Region buckets.

- Meet compliance regulations for data sovereignty by defining replication rules that store replicas only within the allowable locations.

- Keep production and test data in sync, as replication retains object metadata such as last modified time, version ID, etc.

- Manage the storage class and lifecycle policies for the replicated objects independent of the source, by defining different a different storage class and/or lifecycle rules for the destination bucket. Similarly, you can store replicas in a bucket under different ownership, and also control access to the replicas.

- Maintain a Hybrid Cloud footprint by allowing the transfer of you locally managed data (on-premises) that is part of IBM Satellite to public Cloud.

## Requirements for replication
{: #replication-reqs}

- Both the source and destination buckets must have versioning enabled.
- {{site.data.keyword.cos_short}} must be granted write access to both source and destination buckets by IAM.
- Creating or altering replication rules requires the `Writer` or `Manager` role, or a custom role with the appropriate actions assigned.
- The target bucket must not have a legacy bucket firewall enabled. 
- Objects encrypted using SSE-C can not be replicated.
- Objects in an archived state can not be replicated.

As versioning is a requirement for replication, it is not possible to replicate objects in buckets configured with an Immutable Object Storage policy.
{: note}

## Getting started with replication
{: #replication-gs}

First, you'll need to have access to two buckets, each with object versioning enabled.

1. After navigating to your chosen source bucket, click the **Configuration** tab.
2. Look for **Bucket replication** and click the **Setup replication** button.
3. Select **Replication source** and click **Next**.
4. Assuming the destination bucket is in the same IBM Cloud account, select the instance and bucket from the drop-down menus.  Alternatively, toggle the radio button to **No** and paste in the CRN of the destination bucket.
5. Click on the **Check permissions** button.

Now, you'll need to grant {{site.data.keyword.cos_short}} `Writer` permissions on the destination bucket. There are several ways to do this, but the easiest is to use the IBM Cloud Shell and the IBM Cloud CLI.

1. Open an IBM Cloud Shell in a new window or tab.
2. Copy the IBM Cloud CLI command and paste it into the new shell.
3. Return to the bucket configuration window or tab, and click on the **Check permissions** button again.

Now you'll create a replication rule.

1. Ensure the rule status radio button is set to **Enabled**.
2. Give the rule a name and a priority, as well as any prefix or tag filters that will limit the objects subject to the replication rule.
3. Click **Done**.

## Terminology
{: #replication-terminology}

**Source bucket**: The bucket for which a replication policy is configured. It is the source of replicated objects.

**Target bucket**: The bucket that is defined as the destination in the source bucket replication policy. It is the target of replicated objects. Also referred to as a 'destination' bucket.

**Replica**: The new object created in a target bucket as a result of a request made to a source bucket. Assuming that the target bucket does not have any replication rules in place, this object can be altered or deleted without impacting the original.

## Consistency and data integrity
{: #replication-consistency}

While IBM COS provides strong consistency for all data IO operations, bucket configuration is eventually consistent. After enabling replication rules for the first time on a bucket, it may take a few moments for the configuration to propagate across the system. 

## IAM actions
{: #replication-iam}

There are new IAM actions associated with replication. 

| IAM Action                                     | Role                    |
|------------------------------------------------|-------------------------|
| `cloud-object-storage.bucket.get_replication`    | Manager, Writer, Reader |
| `cloud-object-storage.bucket.put_replication`    | Manager, Writer         |
| `cloud-object-storage.bucket.delete_replication` | Manager, Writer         |

## Activity Tracker events 
{: #replication-at}

Replication generates additional events.

- `cloud-object-storage.bucket-replication.create`
- `cloud-object-storage.bucket-replication.read`
- `cloud-object-storage.bucket-replication.delete`

For `cloud-object-storage.bucket-replication.create` events, the following fields include extra information:

| Field                                             | Description                                                                  |
|---------------------------------------------------|------------------------------------------------------------------------------|
| `requestData.replication.num_sync_remote_buckets` | The number of destination buckets specified in the bucket replication rules. |
| `requestData.replication.failed_remote_sync`      | The CRNs of the buckets that failed the replication check.                   |

When replication is active, operations on objects may generate the following extra information:

| Field                                           | Description                                                                                                                                                                                                   |
|-------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `requestData.replication.replication_throttled` | Indicates if the replication of the object was delayed on the source due to a throttling mechanism.                                                                                                           |
| `requestData.replication.destination_bucket_id` | The CRN of the destination bucket.                                                                                                                                                                            |
| `requestData.replication.sync_type`             | The type of sync operation. A `content` sync indicates that the object data _and_ any metadata was written to the destination; a `metadata` sync indicates that only metadata was written to the destination. |

| Field                                       | Description                                                                                     |
|---------------------------------------------|-------------------------------------------------------------------------------------------------|
| `responseData.replication.source_bucket_id` | The CRN of the source bucket.                                                                   |
| `responseData.replication.result`           | Values can be `success`, `failure` (indicates a server error), `user` (indicates a user error). |
| `responseData.replication.message`          | The HTTP response message (such as `OK`).                                                       |



## Usage and accounting
{: #replication-usage}

All replicas are objects themselves, and contribute usage just like any other data. Successful replication results in billable `PUT`, `GET`, and `HEAD` requests, although any bandwidth consumed in the replication process is not billed.  

Replication generates additional metrics for use with IBM Cloud Monitoring:

- `ibm_cos_bucket_replication_sync_requests_issued`
- `ibm_cos_bucket_replication_sync_requests_received`

## Interactions
{: #replication-interactions}

### Versioning
{: #replication-interactions-versioning}

As stated previously, versioning is mandatory in order to enable replication. After you enable versioning on both the source and destination buckets and configure replication on the source bucket, you may encounter the following issues:

- If you attempt to disable versioning on the source bucket, {{site.data.keyword.cos_short}} returns an error. You must remove the replication configuration before you can disable versioning on the source bucket.
- If you disable versioning on the destination bucket, replication fails. 

### Lifecycle configurations
{: #replication-interactions-lifecycle}

The time it takes for objects to replicate is determined by the size of the objects. Larger objects may take several hours to finish replication. If a lifecycle policy is enabled on a destination bucket, the lifecycle rules will honor the original creation time of the object, not the time that the replica became available in the destination bucket. 

### Immutable Object Storage
{: #replication-interactions-worm}

Using retention policies is not possible on a bucket with versioning enabled, and as versioning is a requirement for replication, it is not possible to replicate objects in a bucket with Immutable Object Storage enabled.

### Legacy bucket firewalls
{: #replication-interactions-firewall}

Buckets using legacy firewalls to restrict access based on IP addresses are not able to use replication, as the background services that replicate the objects do not have fixed IP addresses and can not pass the firewall.  

It is recommended to instead use context-based restrictions for controlling access based on network information.  

## S3 API compatibility
{: #replication-s3api}

The IBM COS implementation of the S3 APIs for replication is a subset of the AWS S3 APIs for replication.

The following fields are supported: 

- `DeleteMarkerReplication`
- `Destination`
  - `Bucket`
- `Filter`
  - `Prefix`
  - `Tag`
- `ID`
- `Prefix`
- `Priority`
- `Status`

## Replicating existing objects
{: #replication-existing}

A replication rule can only act on objects that are written _after_ the rule is configured and applied to a bucket.  If there are existing objects in a bucket that should be replicated, the replication processes needs to be made aware of the existence of the objects. This can be easily accomplished by using the `PUT copy` operation to copy objects onto themselves. The server can see that the source and destination objects are identical, so there is no actual writing of data. This makes the copy-in-place approach efficient and quick. 

This process will reset object metadata, including creation timestamps.  This will impact lifecycle policies and any other services that use creation or modification timestamps (such as content delivery networks).  Ensure that any disruptions that may arise from resetting object metadata are dealt with appropriately.
{: important}

The process involves:

1. Creating a list of all the objects in a bucket that should be subject to replication rules,
2. Iterating over that list, performing a `PUT copy` operation on each object with the source being identical to the target of the request.

The following example is written in Python, but the algorithm could be applied in any programming language or context.

```py
import os
import ibm_boto3
from ibm_botocore.config import Config

# Create client connection
cos = ibm_boto3.client("s3",
                       ibm_api_key_id=os.environ.get('IBMCLOUD_API_KEY'),
                       ibm_service_instance_id=os.environ['SERVICE_INSTANCE_ID'],
                       config=Config(signature_version="oauth"),
                       endpoint_url=os.environ['US_GEO']
                       )

# Define the bucket with existing objects for replication
bucket = os.environ['BUCKET']

def copy_in_place(bucket):
    print("Priming existing objects in " + bucket + " for replication...")

    paginator = cos.get_paginator('list_objects_v2')
    pages = paginator.paginate(Bucket=bucket)

    for page in pages:
        for obj in page['Contents']:
            key = obj['Key']
            print("  * Copying " + key + " in place...")
            try:
                cos.copy_object(
                    CopySource={
                        'Bucket': bucket,
                        'Key': key
                        },
                    Bucket=bucket,
                    Key=key,
                    MetadataDirective='REPLACE'
                    )
                print("    Success!")
            except Exception as e:
                print("    Unable to copy object: {0}".format(e))
    print("Existing objects in " + bucket + " are now subject to replication rules.")

copy_in_place(bucket)

```
