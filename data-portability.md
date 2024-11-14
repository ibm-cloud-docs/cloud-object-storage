---

copyright:
  years: 2024
lastupdated: "2024-11-14"

keywords: data,portability

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Understanding data portability
{: #data-portability}

Data portability involves a set of tools and procedures that enable you to export the digital artifacts that are needed to implement similar workload and data processing on different service providers, or on-premises software. It includes procedures for copying and storing your service content, including the related configuration that is used by the service to store and process the data on your own location.
{: shortdesc}

## Responsibilities
{: #data-portability-responsibilities}

IBM Cloud services provide interfaces and instructions to guide you to copy and store your service content, including the related configuration, on your own selected location.

You are responsible for the use of the exported data and configuration for data portability to other infrastructures, which includes:

- The planning and execution for setting up alternative infrastructure on different cloud providers, or on-premises software that provide similar capabilities to the {{site.data.keyword.IBM_notm}} services.
- The planning and execution for the porting of the required application code on the alternative infrastructure, including the adaptation of your application code, deployment automation, and more.
- The conversion of the exported data and configuration to the format that's required by the alternative infrastructure and adapted applications.

For more information about your responsibilities when using {{site.data.keyword.cos_full}}, see [Shared responsibilities for {{site.data.keyword.cos_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-responsibilities).

## Data export procedures
{: #data-portability-procedures}

{{site.data.keyword.cos_full_notm}} provides mechanisms to export your content that is uploaded, stored, and processed using this service.

[About the {{site.data.keyword.cos_full_notm}} S3 API](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api) documents the commands to interact with your data held in {{site.data.keyword.cos_short}} buckets.

Further, more detailed information and examples for commands to extract your information for detailed topics are below:

- [Bucket operations](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations) provides detailed examples about bucket operations.
- [Object operations](/docs/cloud-object-storage?topic=cloud-object-storage-object-operations) discusses object operations.
- [{{site.data.keyword.cos_full_notm}} S3 API](/apidocs/cos/cos-compatibility) discusses s3 API detail and how to use the methods.

Moreover {{site.data.keyword.cos_full_notm}} provides mechanisms to export settings and configuration used to process the customer's content.

- [COS Resource Configuration API](/apidocs/cos/cos-configuration) details bucket configuration.

## Exported data formats
{: #data-portability-data-formats}

As {{site.data.keyword.cos_full_notm}} is a data architecture for storing unstructured data securely, without a schema.  You must manage how you store data in your buckets.

The format of the data output exported using the methods outlined in the Data export procedures are also covered in the {{site.data.keyword.cos_full_notm}} S3 API [documentation](/apidocs/cos/cos-compatibility).

## Data ownership
{: #data-portability-ownership}

All exported data is classified as your content. Apply your full ownership and licensing rights, as stated in the [IBM Cloud Service Agreement](https://www.ibm.com/terms/?id=Z126-6304_WS).

