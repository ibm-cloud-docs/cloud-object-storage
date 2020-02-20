---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-22"

keywords: troubleshooting, support

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}
{:help: data-hd-content-type='help'}

# Support
{: #troubleshooting-cos}

If you have problems or questions when you use {{site.data.keyword.cos_full}}, you can get help starting right here. 
{: shortdesc}

Whether by searching for information or by asking questions through a forum, you can find what you need. If you don't, you can also open a support ticket.

## How do I select an endpoint?
{: #troubleshooting-cos-endpoint}
{: support}

1. Go to the {{site.data.keyword.cos_full_notm}} documentation for [endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started) to research the desired levels of resiliency for your data and appropriate location.
1. Follow the steps to provision your instance in order to create a bucket, choosing a unique name. All buckets in all regions across the globe share a single namespace.
1. Choose your desired level of resiliency, then a location where you would like your data to be physically stored. Resiliency refers to the scope and scale of the geographic area across which your data is distributed. Cross Region resiliency spreads your data across several metropolitan areas, while Regional resiliency spreads data across a single metropolitan area. A Single Data Center distributes data across devices within a single site only.

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


## Other support options
{: #troubleshooting-cos-other-options}

* If you have technical questions about {{site.data.keyword.cos_short}}, post your question on [Stack Overflow](https://stackoverflow.com/search?q=object-storage+ibm) and tag your question with `ibm` and `object-storage`.
* For questions about the service and getting started instructions, use the [IBM Developer dW Answers forum](https://developer.ibm.com/answers/topics/objectstorage/). Include the  `objectstorage` tag.

## Next Steps
{: #troubleshooting-cos-next-steps}

See [Getting help](/docs/get-support?topic=get-support-getting-customer-support) for more details about using the forums.

For more information about opening an IBM support ticket, see [Contacting support](/docs/get-support?topic=get-support-getting-customer-support).
