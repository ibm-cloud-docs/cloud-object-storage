---

copyright:
  years: 2017, 2020
lastupdated: "2019-02-10"

keywords: updates, releases, news, object storage

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# What's new
{: #updates}

News on the latest releases from {{site.data.keyword.cos_full}} provide the updates you need on all things related to {{site.data.keyword.cos_full_notm}}.
{: shortdesc}

## Q1 2020
{: #updates-2020-q1}

Buckets can now be created in a new [Smart Tier storage class](/docs/cloud-object-storage?topic=cloud-object-storage-billing#smart-tier-pricing-details) that optimizes costs based on usage patterns. 

## Q4 2019
{: #updates-2019-q4}

### December 2019
{: #updates-2019-q4-dec}

Buckets can now be created in a Single Data Center configuration in Singapore. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

### November 2019
{: #updates-2019-q4-nov}

Data can now be [archived](/docs/cloud-object-storage?topic=cloud-object-storage-archive) in buckets located in São Paulo, Brazil (`sao1`).

### October 2019
{: #updates-2019-q4-oct}

[Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) is now available for buckets in US Cross Region (`us`).

Buckets can now be created in a Single Data Center configuration in Paris, France. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).



## Q3 2019
{: #updates-2019-q3}

### September 2019
{: #updates-2019-q3-sep}
Changes made to object storage data can be used as an [event source for Cloud Functions](/docs/cloud-object-storage?topic=cloud-object-storage-functions).

### August 2019
{: #updates-2019-q3-aug}
Data can be encrypted [using HPCS](/docs/cloud-object-storage?topic=cloud-object-storage-encryption).

Object-level events can be tracked [using Activity Tracker with LogDNA](/docs/cloud-object-storage?topic=cloud-object-storage-at).

## Q2 2019
{: #updates-2019-q2}

### June 2019
{: #updates-2019-q2-jun}
Objects can be automatically deleted by [adding expiration rules to a bucket's lifecycle configuration](/docs/cloud-object-storage?topic=cloud-object-storage-expiry).

### May 2019
{: #updates-2019-q2-may}
Users can access and interact with object storage [using the IBM Cloud CLI](/docs/cloud-object-storage?topic=cloud-object-storage-cli-ic-cos-cli).

### April 2019
{: #updates-2019-q2-apr}
Buckets can now be created in a Single Data Center configuration in Hong Kong. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).

## Q1 2019
{: #updates-2019-q1}

### March 2019
{: #updates-2019-q1-mar}

User can use [COS Firewall](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall#setting-a-firewall) to restrict access to the data in COS only if request originates from a list of allowed IP addresses.

IAM policies can now grant [public access](/docs/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access) to entire buckets.
Buckets can now be created in a Single Data Center configuration in Milan, Italy. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

### February 2019
{: #updates-2019-q1-feb}
Buckets can now be created in a Single Data Center configuration in San Jose, USA. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

### January 2019
{: #updates-2019-q1-jan}
Buckets can now be created in AP Australia region. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## Q4 2018
{: #updates-2018-q4}

### December 2018
{: #updates-2018-q4-dec}

Users can use [Immutable Object Storage](/docs/cloud-object-storage/basics?topic=cloud-object-storage-immutable) to create retention policies that prevent object deletion or modification.

Buckets can now be created in a Single Data Center configuration in Mexico City, Mexico. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

### November 2018
{: #updates-2018-q4-nov}

Buckets can now be created in a Single Data Center configuration in Montréal, Canada. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

### October 2018
{: #updates-2018-q4-oct}

The [Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java) and [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python) SDKs now provide support for [transferring data using the Aspera FASP protocol](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera). Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).

Buckets can now be created in a Single Data Center configuration in Seoul, South Korea. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## Q3 2018
{: #updates-2018-q3}

### September 2018
{: #updates-2018-q3-sep}

Users can [archive cold data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-archive) by setting the proper parameters in a bucket lifecycle configuration policy, either using the console, REST API, or a language-specific SDK.

The [Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java) and [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python) SDKs now provide support for [transferring data using the Aspera FASP protocol](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera). Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).

### August 2018
{: #updates-2018-q3-aug}
Buckets can now be created in a Single Data Center configuration in Sao Paolo, Brazil and Oslo, Norway. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## Q2 2018
{: #updates-2018-q2}

### June 2018
{: #updates-2018-q2-jun}

Users who upload or download files or folders using the web-based console have the option to use Aspera high-speed transfer for these operations via a browser plug-in. This allows for transfers of objects larger than 200MB using the console, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera) documentation. Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).


Buckets can now be created in the EU Germany region. Data stored in these buckets is distributed across three availability zones in the EU Germany region. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) documentation.

Buckets can now be created in a Single Data Center configuration in Chennai, India and Amsterdam, Netherlands. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

## Q1 2018
{: #updates-2017-q1}

### March 2018
{: #updates-2018-q1-mar}

Users who upload or download files using the web-based console have the option to use Aspera high-speed transfer for these operations via a browser plug-in. This allows for transfers of objects larger than 200MB, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/cloud-object-storage/basics?topic=cloud-object-storage-aspera) documentation.

Buckets can now be created in an AP Cross Region configuration. Data stored in these buckets is distributed across the Seoul, Tokyo, and Hong Kong data centers. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

Users can run `SELECT` SQL queries directly against structured data objects using IBM Cloud SQL Query. More information can be found in the [SQL Query documentation](/docs/sql-query?topic=sql-query-overview).

### February 2018
{: #updates-2018-q1-feb}

Buckets can now be created in a Single Data Center configuration in Toronto, Canada and Melbourne, Australia. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in the [Select Regions and Endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) documentation.

### January 2018
{: #updates-2018-q1-jan}

The IBM COS SDK for Java has been updated to 2.0. This release primarily fixes an issue for users trying to connect to IBM COS and AWS services within the same application by changing the namespacing for the library from `amazonaws` to `ibm.cloud.objectstorage`. For more information check out the [Github repository](https://github.com/IBM/ibm-cos-sdk-java) and the [API documentation](https://ibm.github.io/ibm-cos-sdk-java/).

## Q4 2017
{: #updates-2017-q4}

### December 2017
{: #updates-2017-q4-dec}

The IBM COS SDK for Python has been updated to 2.0. This release primarily fixes an issue for users trying to connect to IBM COS and AWS services within the same application by changing the namespacing for the library from `boto3` to `ibm_boto3`. For more infomation check out the [Github repository](https://github.com/IBM/ibm-cos-sdk-python) or the [API documentation](https://ibm.github.io/ibm-cos-sdk-python/).
