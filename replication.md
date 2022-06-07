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

First, create a new bucket with object versioning enabled.

1. After navigating to your chosen bucket, click the **Configuration** tab.
2. Look for **Object replication** and toggle the selector to **Enabled**.
3. ...
4. ...
5. ...
6. ...

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
| cloud-object-storage.bucket.get_replication    | Manager, Writer, Reader |
| cloud-object-storage.bucket.put_replication    | Manager, Writer         |
| cloud-object-storage.bucket.delete_replication | Manager, Writer         |

## Activity Tracker events 
{: #replication-at}

Replication will generate new events.

- `cloud-object-storage.bucket-replication.create`
- `cloud-object-storage.bucket-replication.read`
- `cloud-object-storage.bucket-replication.list`

- Event for completing replica write ????
- Event details for failed replications ????

## Usage and accounting
{: #replication-usage}

All replicas are objects themselves, and contribute usage just like any other data. Replication results in billable `GET` and `HEAD` requests, although any bandwidth consumed in the replication process is not billed.  

- New metrics ????

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

The following fields are not supported: 

- `AccessControlTranslation`
- `Account`
- `EncryptionConfiguration`
- `Metrics`
- `StorageClass`
- `ReplicationTime`
- `ExistingObjectReplication`
- `SourceSelectionCriteria`

