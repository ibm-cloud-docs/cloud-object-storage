---

copyright:
  years: 2017, 2020
lastupdated: "2020-09-30"

keywords: actions, api

subcollection: cloud-object-storage

---

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:screen: .screen}
{:pre: .pre}
{:table: .aria-labeledby="caption"}
{:codeblock: .codeblock}
{:tip: .tip}
{:download: .download}
{:important: .important}
{:note: .note}


# IAM and Activity Tracker actions by API
{: #at-iam}

List of {{site.data.keyword.cloud_notm}} IAM and {{site.data.keyword.at_full}} actions by API method.
{: shortdesc}

## Resource Configuration API
{: #at-iam-config}

API for configuring {{site.data.keyword.cos_full}} buckets: [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration#introduction)
*  Endpoint URL: `https://config.cloud-object-storage.cloud.ibm.com/v1`

| Action                                    | Method                                | IAM Action                                                                                                                                                        | Activity Tracker action                              |
|-------------------------------------------|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------|
| Returns metadata for the specified bucket | `GET {endpoint}/b/{bucket}`   | `cloud-object-storage.bucket.list_bucket_crn`, </br> `cloud-object-storage.bucket.get_firewall`, </br> `cloud-object-storage.bucket.get_activity_tracking`, </br> `cloud-object-storage.bucket.get_metrics_monitoring` | `cloud-object-storage.resource-configuration.read`   |
| Make changes to a bucket's configuration  | `PATCH {endpoint}/b/{bucket}` | `cloud-object-storage.bucket.put_firewall`, </br> `cloud-object-storage.bucket.put_activity_tracking`, </br> `cloud-object-storage.bucket.put_metrics_monitoring` | `cloud-object-storage.resource-configuration.update` |
{: caption="RC API actions" caption-side="top"}

## S3 API
{: #at-iam-s3}

API for reading and writing objects: [COS Compatibility S3 API](https://cloud.ibm.com/apidocs/cos/cos-compatibility#introduction)
* Endpoint URL is different by region: [Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints)

| Action                                              | Method                                                                      | IAM Action                                                                          | Activity Tracker action                          |
|-----------------------------------------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------------------------|--------------------------------------------------|
| List buckets                                        | `GET {endpoint}/`                                                           | `cloud-object-storage.account.get_account_buckets`                                  | `cloud-object-storage.instance.list`             |
| Create a bucket                                     | `PUT {endpoint}/{bucket}`                                                   | `cloud-object-storage.bucket.put_bucket`                                            | `cloud-object-storage.bucket.create`             |
| List objects                                        | `GET {endpoint}/{bucket}`                                                   | `cloud-object-storage.bucket.get`                                                   | `cloud-object-storage.bucket.list`               |
| Check a bucket's headers                            | `HEAD {endpoint}/{bucket}`                                                  | `cloud-object-storage.bucket.head`, </br> `cloud-object-storage.bucket.list_crk_id` | `cloud-object-storage.bucket-metadata.read`      |
| Delete a bucket                                     | `DELETE {endpoint}/{bucket}`                                                | `cloud-object-storage.bucket.delete_bucket`                                         | `cloud-object-storage.bucket.delete`             |
| Upload an object                                    | `PUT {endpoint}/{bucket}/{key}`                                             | `cloud-object-storage.object.put`                                                   | `cloud-object-storage.object.create`             |
| Download an object                                  | `GET {endpoint}/{bucket}/{key}`                                             | `cloud-object-storage.object.get`                                                   | `cloud-object-storage.object.read`               |
| Check an object's headers                           | `HEAD {endpoint}/{Bucket}/{key}`                                            | `cloud-object-storage.object.head`                                                  | `cloud-object-storage.object-metadata.read`      |
| Delete an object                                    | `DELETE {endpoint}/{bucket}/{key}`                                          | `cloud-object-storage.object.delete`                                                | `cloud-object-storage.object.delete`             |
| Add a CORS configuration                            | `PUT {endpoint}/{bucket}?cors`                                              | `cloud-object-storage.bucket.put_cors`                                              | `cloud-object-storage.bucket-cors.create`        |
| Read a CORS configuration                           | `GET {endpoint}/{bucket}?cors`                                              | `cloud-object-storage.bucket.get_cors`                                              | `cloud-object-storage.bucket-cors.read`          |
| Delete a CORS configuration                         | `DELETE {endpoint}/{bucket}?cors`                                           | `cloud-object-storage.bucket.delete_cors`                                           | `cloud-object-storage.bucket-cors.delete`        |
| Add/edit a bucket's lifecycle configuration         | `PUT {endpoint}/{bucket}?lifecycle`                                         | `cloud-object-storage.bucket.put_lifecycle`                                         | `cloud-object-storage.bucket-lifecycle.create`   |
| Read a bucket's lifecycle configuration             | `GET {endpoint}/{bucket}?lifecycle`                                         | `cloud-object-storage.bucket.get_lifecycle`                                         | `cloud-object-storage.bucket-lifecycle.read`     |
| Delete a bucket's lifecycle configuration           | `DELETE {endpoint}/{bucket}?lifecycle`                                      | `cloud-object-storage.bucket.put_lifecycle`                                         | `cloud-object-storage.bucket-lifecycle.delete`   |
| Add/edit/remove a bucket's Immutable Storage policy | `PUT {endpoint}/{bucket}?protection`                                        | `cloud-object-storage.bucket.put_protection`                                        | `cloud-object-storage.bucket-retention.create`   |
| Read a bucket's Immutable Storage policy            | `GET {endpoint}/{bucket}?protection`                                        | `cloud-object-storage.bucket.get_protection`                                        | `cloud-object-storage.bucket-retention.read`     |
| Initiate a multipart upload                         | `POST {endpoint}/{bucket}/{key}?uploads`                                    | `cloud-object-storage.object.post_initiate_upload`                                  | `cloud-object-storage.object-multipart.start`    |
| Upload a part                                       | `PUT {endpoint}/{bucket}/{key}?uploadId={uploadId}&partNumber={partNumber}` | `cloud-object-storage.object.put_part`                                              | `cloud-object-storage.object-multipart.create`   |
| Complete a multipart upload                         | `POST {endpoint}/{bucket}/{key}?uploadID={uploadId}`                        | `cloud-object-storage.object.post_initiate_upload`                                  | `cloud-object-storage.object-multipart.complete` |
{: caption="S3 API actions" caption-side="top"}
