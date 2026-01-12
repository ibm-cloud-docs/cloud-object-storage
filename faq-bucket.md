---

copyright:
  years: 2024
lastupdated: "2024-06-17"


keywords: faq, frequently asked questions, object storage, S3, HMAC, bucket management, IPv6. endpoints

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - Bucket management
{: #faq-bucket}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## How can I find out the total size of my bucket by using the API?
{: #faq-bucket-size}
{: faq}

You can use the [Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration#returns-metadata-for-the-specified-bucket) to get the bytes used for a given bucket.

## How can I view my buckets?
{: #faq-bucket-view}
{: faq}

You can view and navigate your buckets using the console, CLI or the API.

For example, the CLI command `ibmcloud cos buckets` will list all buckets associated with the targeted service instance.

## Is there a 100-bucket limit to an account? What happens if I need more?
{: #faq-bucket-limit}
{: faq}

Yes, 100 is the current bucket limit. Generally, prefixes are a better way to group objects in a bucket, unless the data needs to be in a different region or storage class. For example, to group patient records, you would use one prefix per patient. If this is not a workable solution and you require additional buckets, contact IBM customer support.

## When I create a bucket by using the API, how do I set the storage class?
{: #faq-bucket-class}
{: faq}

The storage class (for example, `us-smart`) is assigned to the `LocationConstraint` configuration variable for that bucket. This is because of a key difference between the way AWS S3 and {{site.data.keyword.cos_full_notm}} handle storage classes. {{site.data.keyword.cos_short}} sets storage classes at the bucket level, while AWS S3 assigns a storage class to an individual object. For a list of valid provisioning codes for `LocationConstraint`, see [the Storage Classes guide](/docs/cloud-object-storage?topic=cloud-object-storage-classes).

## Can the storage class of a bucket be changed? For example, if you have production data in 'standard', can we easily switch it to 'vault' for billing purposes if we are not using it frequently?
{: #faq-change-class}
{: faq}

You can change the storage class by manually moving or copying the data from one bucket to another bucket with the wanted storage class.

## Can the location of a bucket be changed?
{: #faq-change-loc}
{: faq}

To change a location, create a new bucket in the desired location and move existing data to the new bucket.

## How many objects can fit in a single bucket?
{: #faq-single}
{: faq}

There is no practical limit to the number of objects in a single bucket.

## Can I nest buckets inside one another?
{: #faq-nest}
{: faq}

No, buckets cannot be nested. If a greater level of organization is required within a bucket, the use of prefixes is supported: `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. The object's key remains the combination `{object-prefix}/{object-name}`.

## Can I restore a bucket from a specific back-up file?
{: #faq-cos-backup}
{: faq}

It is possible to overwrite an existing bucket. Restore options depend on the capabilities provided by the back-up tool you use; check with your back-up provider. As described in [Your responsibilities when using IBM Cloud Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-responsibilities), you are responsible for ensuring data back-ups if necessary. {{site.data.keyword.cos_full}} does not provide a back-up service.

## If I set an archive policy on an existing bucket, does the policy apply to existing files?
{: #faq-archive}
{: faq}

The policy applies to the new objects uploaded but does not affect existing objects on a bucket. For details, see [Add or manage an archive policy on a bucket](/docs/cloud-object-storage?topic=cloud-object-storage-archive#archive-add).

## Can I create a bucket, in the same or different region, with a deleted bucket name?
{: #faq-reuse-name}
{: faq}

A bucket name can be reused as soon as 5 minutes after the contents of the bucket have been deleted and the bucket has been deleted.  Then, the objects and bucket are irrevocably deleted and **can not** be restored.

If you do not first empty and then delete the bucket, and instead [delete or schedule the {{site.data.keyword.cos_short}} service instance for deletion](/docs/cloud-object-storage?topic=cloud-object-storage-provision#deleting-a-service-instance), the bucket names will be held in reserve for a [default period of seven (7) days until the account reclamation process](/docs/cli?topic=cli-ibmcloud_commands_resource#ibmcloud_resource_reclamations) is completed. Until the reclamation process is complete, it is possible to restore the instance, along with the buckets and objects. After reclamation is complete, all buckets and objects will be irrevocably deleted and **can not** be restored, although the bucket names will be made available for new buckets to reuse.

## How do I select an endpoint?
{: #troubleshooting-cos-endpoint}
{: faq}

1. Go to the {{site.data.keyword.cos_full_notm}} documentation for [endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to research the desired levels of resiliency for your data and appropriate location.
1. Follow the steps to provision your instance in order to create a bucket, choosing a unique name. All buckets in all regions across the globe share a single namespace.
1. Choose your desired level of resiliency, then a location where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which your data is distributed. Cross Region resiliency spreads your data across several metropolitan areas, while Regional resiliency spreads data across a single metropolitan area. A Single Data Center distributes data across devices within a single site only.

## How do I find a bucket’s name?
{: #troubleshooting-cos-name}
{: faq}

To find a bucket’s name, go to the IBM Cloud console, select **Storage**, and then select the name of your Object Storage instance from within the **Storage** category. The Object Storage Console opens with a list of buckets, their names, locations, and other details. This name is the one you can use when prompted for a bucket name value by another service.

## How do I find the details for a bucket?
{: #troubleshooting-cos-details}
{: faq}

To find the details for a bucket, go to the IBM Cloud console, select **Storage**, and then select the name of your Object Storage instance from within the **Storage** category. The Object Storage Console opens with a list of buckets.  Find the bucket you want to see the details, and go to the end of the row and select the options list represented by the three-dot colon.  Click the three-dot colon and select **Configuration** to see the details for the bucket.

## How do I find a bucket’s location and endpoint?
{: #troubleshooting-cos-find}
{: faq}

You can view the bucket location in the IBM Cloud console with these steps:
1. From the IBM Cloud console, select **Storage** to view your resource list.
1. Next, select the service instance with your bucket from within the **Storage** category. This takes you to the Object Storage Console.
1. Choose the bucket for which you want to see location and endpoints.
1. Select **Configuration** from the navigation menu to view the page with Location and Endpoints data.

Or you can list bucket information with a GET request that includes the “extended” parameter as shown in [Getting an extended listing](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended).

## Do {{site.data.keyword.cos_short}} endpoints support IPv6 connections?
{: #faq-ipv6-not-supported}
{: faq}

No.

## How do I restrict access to a single bucket using IAM?
{: #troubleshooting-cos-access-iam}
{: faq}

1. Go to the {{site.data.keyword.cos_full_notm}} page for [using service credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials-bucket) to research the authentication topic.
1. Create a bucket, but do not add any public or other permissions to it.
1. To add the new user you first need to leave the current {{site.data.keyword.cos_short}} interface and head for the IAM console. Go to the **Manage** menu and follow the link at **Access (IAM)** > **Users**. Click **Service Credentials**.
1. Click **New credential** and provide the necessary information. If you want to generate HMAC credentials, click the 'Include HMAC Credential' check box. Select the "Manager" service access role to allow the user to manage the bucket that you will select next.
1. Click **Add** to generate service credential.

## How do I resolve a 404 error when using the command line?
{: #troubleshooting-cos-error404}
{: faq}

You can view a bucket or object in the IBM Cloud console but the following error occurs when you use a command line interface to access that same bucket:

* Cloud CLI error: “The specified bucket was not found in your IBM Cloud account. This may be because you provided the wrong region. Provide the bucket's correct region and try again.”
* AWS CLI error: “An error occurred (NoSuchBucket) when calling the ListObjectsV2 operation: The specified bucket does not exist.”

The bucket’s location must correspond to the endpoint used by the CLI. This error occurs when the bucket or object cannot be found at the default endpoint for the CLI.

To avoid the error, make sure the bucket location matches the endpoint used by the CLI.  For the parameters to set a region or endpoint, refer to the documentation for [Cloud Object Storage CLI](/docs/cloud-object-storage?topic=cloud-object-storage-ic-cos-cli#ic-installation) or [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli).

## How do I copy or move files to another bucket in a different location?
{: #troubleshooting-cos-move}
{: faq}

Refer to [Move data between buckets](/docs/cloud-object-storage?topic=cloud-object-storage-region-copy) for an example of how to use the `rclone` command line utility for copying data. If you use other 'sync' or 'clone' tools, be aware that you might need to implement a script to move files to a bucket in a different location because multiple endpoints are not allowed in a command.

## Can I migrate a bucket from one COS instance to another?
{: #faq-create-bucket}
{: faq}

Yes, You can achieve the same by creating a bucket in the target Object Storage instance and perform a sync. For details see [cloud-object-storage-region-copy](
 /docs/cloud-object-storage?topic=cloud-object-storage-region-copy).

## After deleting a Object Storage instance, is it possible to reuse the same bucket names that were part of the deleted COS instance?
{: #faq-resue-bucketname}
{: faq}

When an empty bucket is deleted, the name of the bucket is held in reserve by the system for 5 minutes after the delete operation.  After 5 minutes the name is released for re-use.

## Can I enable Object Storage replication between two different regions for DR purposes?
{: #faq-cos-replication}
{: faq}

Yes, it is possible to configure buckets for automated [replication of objects to a destination bucket](/docs/cloud-object-storage?topic=cloud-object-storage-replication-overview).

## How can I setup notifications when objects are updated or written to a bucket?
{: #faq-notification-setup}
{: faq}

You can use [{{site.data.keyword.codeengineshort}}](/docs/codeengine?topic=codeengine-getting-started) to receive events about actions taken on your bucket.

## Does Object Storage have rate limits when writing to or reading from buckets?
{: #faq-rate-limit}
{: faq}

Yes, Object Storage has rate limiting. For details, see [COS support](https://cloud.ibm.com/unifiedsupport/cases/form).

## How can I compare various attributes of an object in two different buckets?
{: #faq-comp-attributes}
{: faq}

Use [`rclone`](https://rclone.org/commands/rclone_check){: external}. It enables you to compare various attributes.

## What is the default retention period for buckets?
{: #faq-default-retention}
{: faq}

There is no default retention period applied. You can set it while creating the bucket.

## Can we add a retention policy to an existing bucket?
{: #faq-add-retention}
{: faq}

Yes, [Retention policies](/docs/cloud-object-storage?topic=cloud-object-storage-immutable#immutable-sdk-add-policy) can be added to an existing bucket; however, the retention period can only be extended. It cannot be decreased from the currently configured value.

## Why is there a "legal hold" concept on top of the "retention period"?
{: #faq-legal-hold}
{: faq}

A legal hold prevents an object from being overwritten or deleted. However, a legal hold does not have to be associated with a retention period and remains in effect until the legal hold is removed. For details, see [Legal hold and retention period](/docs/cloud-object-storage?topic=cloud-object-storage-immutable#immutable-terminology-hold).

## How to invoke IBM Cloud Object Storage bucket operations using cURL?
{: #faq-using-curl}
{: faq}

You have the most power by using the command line in most environments with IBM Cloud Object Storage and cURL. However using cURL assumes a certain amount of familiarity with the command line and Object Storage. For details, see [Using cURL](/docs/cloud-object-storage?topic=cloud-object-storage-curl).

## How can I list all permissions of a bucket? 
{: #faq-bucket-permison}
{: faq}

The IAM feature creates a report at the instance level which may extend to their buckets. It does not specifically report at the bucket level. For details, see [Account Access Report](/docs/account?topic=account-access-report).

## How do I get bucket information without using the web console? 
{: #faq-bucketinfo-webconsole}
{: faq}

Use the {{site.data.keyword.cos_short}} Resource Configuration API to get bucket information. For details, see [COS configuration](https://cloud.ibm.com/apidocs/cos/cos-configuration#returns-metadata-for-the-specified-bucket) and [COS Integration](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration).

## How can I manage service credentials for {{site.data.keyword.cos_short}} instances?
{: #faq-mnge-cosinst}
{: faq}

When a service credential is created, the underlying Service ID is granted a role on the entire instance of Object Storage. For details, see [Managing Service credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials).

## Why are parts of my credentials hidden or not viewable?
{: #faq-unviewable-credentials}
{: faq}

There may be an issue where the viewer does not have sufficient roles to view the credential information.  For more information, see [the account credentials documentation](/account?topic=account-service_credentials&interface=ui#viewing-credentials-ui).

## Is there a way to enable Key Protect on a {{site.data.keyword.cos_short}} bucket after the bucket is created?
{: #faq-bucket-keypro}
{: faq}

No, it is impossible to add Key Protect after creating a bucket. Key Protect can only be added while creating the bucket.

## Can I host a website using a {{site.data.keyword.cos_short}} bucket?
{: #faq-static-website}
{: faq}

You can use {{site.data.keyword.cos_short}} bucket to host a static website. For details, see [Hosting Website using COS](/docs/cloud-object-storage?topic=cloud-object-storage-static-website-tutorial).
 
## Are REST and cURL commands supported for {{site.data.keyword.cos_short}} bucket creation using HMAC credentials?
{: #faq-using-hmac}
{: faq}

Yes, you should setup an authorization header. For details, see [Using HMAC Signature](/docs/cloud-object-storage?topic=cloud-object-storage-hmac-signature).

## What kind of IAM authorization is required to edit a bucket's authorized IPs list? 
{: #faq-author-iplist}
{: faq}

You must have 'Manager' privilege on the bucket to manage the firewall and to set the authorizations.

## Can I convert a single region {{site.data.keyword.cos_short}} bucket to cross region without having to copy objects?
{: #faq-singleregion-to-multiregion}
{: faq}

No, you must copy objects to the target bucket. For details, see [COS Region Copy](/docs/cloud-object-storage?topic=cloud-object-storage-region-copy).

## How can I set a notification when usage in a {{site.data.keyword.cos_short}} instance is near a certain billing amount?
{: #faq-quota-billing}
{: faq}

You can use a "soft" bucket quota feature by integrating with Metrics Monitoring and configuring for notifications. For details on establishing a hard quota that prevents usage beyond a set bucket size, see [Using Bucket Quota](/docs/cloud-object-storage?topic=cloud-object-storage-quota).

## How do I delete a non-empty bucket when I do not see any objects in it?
{: #faq-delete-nonempty-bucket}
{: faq}

There may be versioned objects or incomplete multipart uploads that are still within the bucket but aren't being displayed. Both of these can be cleaned up by setting an [expiry policy](/docs/cloud-object-storage?topic=cloud-object-storage-expiry) to delete stale data.

Also, you can delete multipart uploads directly using the [Minio client](/docs/cloud-object-storage?topic=cloud-object-storage-minio) command: `mc rm s3/ -I -r --force`

## Why do I receive an error when I try to create a bucket?
{: #faq-create-bucket-error}
{: faq}

Check [IAM permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam) because a user must have "Writer" permissions to create buckets.

[Content-based restrictions](/docs/cloud-object-storage?topic=cloud-object-storage-cos-tutorial-cbr) may be preventing the user from acting on the service.

## How do cross-origin resource sharing (CORS) and a bucket firewall differ in limiting access to data?
{: #faq-cors}
{: faq}

CORS allows interactions between resources from different origins that are normally prohibited. A bucket firewall allows access only to requests from a list of allowed IP addresses. For more information on CORS, see [What is CORS?](/docs/CDN?topic=CDN-cors-and-cors-requests-through-your-cdn#what-is-cors).

## How do I allow Aspera High-Speed Transfer through a bucket with context-based restrictions or a firewall?
{: #faq-aspera-ip}
{: faq}

The full list (in JSON) of Aspera High-Speed Transfer IP addresses that are used with {{site.data.keyword.cos_full_notm}} can be found [using this API endpoint](https://ats.aspera.io/pub/v1/servers/softlayer).
