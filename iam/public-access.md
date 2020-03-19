---

copyright:
  years: 2017, 2019
lastupdated: "2019-12-05"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Allowing public access
{: #iam-public-access}

Sometimes data is meant to be shared. Buckets might hold open data sets for academic and private research or image repositories that are used by web applications and content delivery networks. Make these buckets accessible using the **Public Access** group.
{: shortdesc}

## Using the console to set public access
{: #iam-public-access-console}

First, make sure that you have a bucket. If not, follow the [getting started tutorial](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) to become familiar with the console.

### Enable public access
{: #public-access-console-enable}

1. From the {{site.data.keyword.cloud_notm}} [console dashboard](https://cloud.ibm.com/), select **Storage** to view your resource list.
2. Next, select the service instance with your bucket from within the **Storage** menu. This takes you to the {{site.data.keyword.cos_short}} Console.
3. Choose the bucket that you want to be publicly accessible. Keep in mind this policy makes _all objects in a bucket_ available to download for anyone with the appropriate URL.
4. Select **Access policies** from the navigation menu.
5. Select the **Public access** tab.
6. Click **Create access policy**. After you read the warning, choose **Enable**.
7. Now all objects in this bucket are publicly accessible!

### Disable public access
{: #public-access-console-disable}

1. From anywhere in the {{site.data.keyword.cloud_notm}} [console](https://cloud.ibm.com/), select the **Manage** menu, and the **Access (IAM)**.
2. Select **Access groups** from the navigation menu.
3. Select **Public Access** to see a list of all public access policies currently in use.
4. Find the policy that corresponds to the bucket you want to return to enforced access control.
5. From the list of actions on the far right of the policy entry, choose **Remove**.
6. Confirm the dialog box, and the policy is now removed from the bucket.

## Allowing public access on individual objects
{: #public-access-object}

To make an object publicly accessible through the REST API, an `x-amz-acl: public-read` header can be included in the request. Setting this header bypasses any [IAM policy](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) checks and allow for unauthenticated `HEAD` and `GET` requests. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Additionally, [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main-signature) make it possible to allow [temporary public access that uses pre-signed URLs](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url).

### Upload a public object
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### Allow public access to an existing object
{: #public-access-object-existing}

Using the query parameter `?acl` without a payload and the `x-amz-acl: public-read` header allows public access to the object without needing to overwrite the data.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### Make a public object private again
{: #public-access-object-private}

Using the query parameter `?acl` without a payload and an empty `x-amz-acl:` header revokes public access to the object without needing to overwrite the data.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## Static websites
{: #public-access-static-website}

While {{site.data.keyword.cos_full_notm}} doesn't support automatic static website hosting, it's possible to manually configure a web server and use it to serve publically accessible content hosted in a bucket. For more information, see [this tutorial](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).
