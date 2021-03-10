---

copyright:
  years: 2019, 2020
lastupdated: "2020-06-19"

keywords: events, activity, logging, api

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Setting a quota on a bucket
{: #quota}

A hard quota sets a maximum amount of storage (in bytes) available for a bucket.  Once reached, the limit prevents adding any additional objects to the bucket until existing objects are moved or deleted to free up space, or the quota is raised. 
{: shortdesc}

## Using the console
{: #quota-console}

You can use the console to add a hard quota to a bucket during creation, or an existing bucket.

### Creating a new bucket with a quota
{: #quota-console-new}

1. After navigating to your object storage instance, click on **Create bucket**.
2. Under _Advanced configurations_, look for **Quota enforcement** and toggle the selector to **Enabled**.
3. Now raise or lower the value and choose the appropriate storage unit. Then, click **Save**.
4. Continue configuring any other rules, setting, or policies on the new bucket.

### Adding a quota to an existing bucket
{: #quota-console-existing}

First, make sure that you have a bucket. If not, follow the [getting started tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to become familiar with the console. 

1. Navigate to a bucket, so that you are looking at a list of objects. Select **Configuration** from the navigational menu.
2. Under _Advanced configurations_, look for **Quota enforcement** and toggle the selector to **Enabled**.
3. Now raise or lower the value and choose the appropriate storage unit. Then, click **Save**.

### Disabling or editing a quota 
{: #quota-console-disable}

1. Navigate to the bucket where you want to change the quota, so that you are looking at a list of objects. Select **Configuration** from the navigational menu.
2. Under _Advanced configurations_, look for **Quota enforcement**.
3. If you want to disable the quota enforcement, toggle the selector to **Disable**.  Alternatively, keep the quota enforcement enabled, but edit the values as needed.
4. Click **Save**.

## Using an API
{: #at-api}

Bucket quotas are managed with the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration).

To add a quota, you send a `PATCH` request to edit the bucket's metadata:

```curl
curl -X PATCH https://config.cloud-object-storage.cloud.ibm.com/v1/b/my-bucket \
     -H 'authorization: bearer $IAM_TOKEN' \
     -d '{"hard_quota": 10000000000}'
```

To disable the quota, set it to zero:

```curl
curl -X PATCH https://config.cloud-object-storage.cloud.ibm.com/v1/b/my-bucket \
     -H 'authorization: bearer $IAM_TOKEN' \
     -d '{"hard_quota": 0}'
```

To temporarily disable writing new data to the bucket, set the quota to a very small integer:

```curl
curl -X PATCH https://config.cloud-object-storage.cloud.ibm.com/v1/b/my-bucket \
     -H 'authorization: bearer $IAM_TOKEN' \
     -d '{"hard_quota": 1}'
```

To check the quota on a bucket, send a GET request to view the `hard_quota` field in the bucket's metadata:

```curl
curl https://config.cloud-object-storage.cloud.ibm.com/v1/b/my-bucket \
     -H 'authorization: bearer $IAM_TOKEN'
```
