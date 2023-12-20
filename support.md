---

copyright:
  years: 2017, 2023
lastupdated: "2023-12-08"

keywords: troubleshooting, support, questions

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}
{:help: data-hd-content-type='help'}

# Support
{: #troubleshooting-cos}

If you have problems or questions when you use {{site.data.keyword.cos_full}}, you can get help starting right here.
{: shortdesc}

Whether by searching for information or by asking questions through a forum, you can find what you need. If you don't, you can also open a support ticket.

## How do I select an endpoint?
{: #troubleshooting-cos-endpoint}
{: support}

1. Go to the {{site.data.keyword.cos_full_notm}} documentation for [endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to research the desired levels of resiliency for your data and appropriate location.
1. Follow the steps to provision your instance in order to create a bucket, choosing a unique name. All buckets in all regions across the globe share a single namespace.
1. Choose your desired level of resiliency, then a location where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which your data is distributed. Cross Region resiliency spreads your data across several metropolitan areas, while Regional resiliency spreads data across a single metropolitan area. A Single Data Center distributes data across devices within a single site only.

## How do I find a bucket’s name?
{: #troubleshooting-cos-name}

To find a bucket’s name, go to the IBM Cloud console, select **Storage**, and then select the name of your Object Storage instance from within the **Storage** category. The Object Storage Console opens with a list of buckets, their names, locations, and other details. This name is the one you can use when prompted for a bucket name value by another service.

## How do I find the details for a bucket?
{: #troubleshooting-cos-details}

To find the details for a bucket, go to the IBM Cloud console, select **Storage**, and then select the name of your Object Storage instance from within the **Storage** category. The Object Storage Console opens with a list of buckets.  Find the bucket you want to see the details, and go to the end of the row and select the options list represented by the three-dot colon.  Click the three-dot colon and select **Configuration** to see the details for the bucket.

## How do I find a bucket’s location and endpoint?
{: #troubleshooting-cos-find}

You can view the bucket location in the IBM Cloud console with these steps:
1. From the IBM Cloud console, select **Storage** to view your resource list.
1. Next, select the service instance with your bucket from within the **Storage** category. This takes you to the Object Storage Console.
1. Choose the bucket for which you want to see location and endpoints.
1. Select **Configuration** from the navigation menu to view the page with Location and Endpoints data.

Or you can list bucket information with a GET request that includes the “extended” parameter as shown in [Getting an extended listing](/docs/cloud-object-storage/basics?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended).

## How do I encrypt my data?
{: #troubleshooting-cos-encryption}
{: support}

1. Go to the {{site.data.keyword.cos_full_notm}} documentation for [managing encryption](/docs/cloud-object-storage?topic=cloud-object-storage-encryption) to research the encryption topic.
1. Choose between [{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) for your encryption needs.
1. Remember that customer-provided keys are enforced on objects.
1. Use [IBM Key Protect](/docs/key-protect?topic=key-protect-about) or [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) to create, add, and manage keys, which you can then associate with your instance of {{site.data.keyword.cos_full_notm}}.
1. Grant service authorization
     1. Open your IBM Cloud dashboard.
     1. From the menu bar, click **Manage > Access**.
     1. In the side navigation, click **Authorizations**.
     1. Click **Create authorization**.
     1. In the **Source service** menu, select **Cloud Object Storage**.
     1. In the **Source service instance** menu, select the service instance to authorize.
     1. In the **Target service** menu, select **IBM Key Protect** or **{{site.data.keyword.hscrypto}}**.
     1. In the **Target service instance** menu, select the service instance to authorize.
     1. Enable the **Reader** role.
     1. Click **Authorize**.


## How do I restrict access to a single bucket using IAM?
{: #troubleshooting-cos-access-iam}
{: support}

1. Go to the {{site.data.keyword.cos_full_notm}} page for [using service credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials-bucket) to research the authentication topic.
1. Create a bucket, but do not add any public or other permissions to it.
1. To add the new user you first need to leave the current {{site.data.keyword.cos_short}} interface and head for the IAM console. Go to the **Manage** menu and follow the link at **Access (IAM)** > **Users**. Click **Service Credentials**.
1. Click **New credential** and provide the necessary information. If you want to generate HMAC credentials, click the 'Include HMAC Credential' check box. Select the "Manager" service access role to allow the user to manage the bucket that you will select next.
1. Click **Add** to generate service credential.

## How do I resolve a 404 error when using the command line?
{: #troubleshooting-cos-error404}

You can view a bucket or object in the IBM Cloud console but the following error occurs when you use a command line interface to access that same bucket:
* Cloud CLI error: “The specified bucket was not found in your IBM Cloud account. This may be because you provided the wrong region. Provide the bucket's correct region and try again.”
* AWS CLI error: “An error occurred (NoSuchBucket) when calling the ListObjectsV2 operation: The specified bucket does not exist.”

The bucket’s location must correspond to the endpoint used by the CLI. This error occurs when the bucket or object cannot be found at the default endpoint for the CLI. 

To avoid the error, make sure the bucket location matches the endpoint used by the CLI.  For the parameters to set a region or endpoint, refer to the documentation for [Cloud Object Storage CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-plugin-ic-cos-cli) or [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli).

## How do I copy or move files to another bucket in a different location?
{: #troubleshooting-cos-move}

Refer to [Move data between buckets](/docs/cloud-object-storage?topic=cloud-object-storage-region-copy) for an example of how to use the rclone command line utility for copying data. If you use other 'sync' or 'clone' tools, be aware that you might need to implement a script to move files to a bucket in a different location because multiple endpoints are not allowed in a command.


## Other support options
{: #troubleshooting-cos-other-options}

* If you have technical questions about {{site.data.keyword.cos_short}}, post your question on [Stack Overflow](https://stackoverflow.com/search?q=object-storage+ibm) and tag your question with `ibm` and `object-storage`.
* For questions about the service and getting started instructions, use the [IBM Developer dW Answers forum](https://developer.ibm.com/answers/topics/objectstorage/){: external}. Include the  `objectstorage` tag.

## Next Steps
{: #troubleshooting-cos-next-steps}

For more information about asking questions, see [Contacting support](https://cloud.ibm.com/docs/get-support?topic=get-support-using-avatar#asking-a-question).

See [Getting help](/docs/get-support?topic=get-support-using-avatar) for more details about using the forums.

For more information about opening an IBM support ticket, see how to [create a request](/docs/get-support?topic=get-support-open-case).
