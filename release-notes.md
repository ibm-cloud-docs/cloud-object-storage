---

copyright:
  years: 2017, 2018
lastupdated: "07-16-2018"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# What's new

## Q1 2019


### February 2019
Buckets can now be created in San Jose single data center. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

### January 2019

Buckets can now be created in AP Australia region. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

## Q4 2018
### December 2018
Users can use [Immutable Object Storage](docs/services/cloud-object-storage/basics/immutable.html) to create retention policies that prevent object deletion or modification.

Buckets can now be created in a Single Data Center configuration in Mexico City, Mexico. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

### November 2018
Buckets can now be created in a Single Data Center configuration in Montréal, Canada. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

### October 2018
The [Java](/docs/services/cloud-object-storage/libraries/java.html) and [Python](/docs/services/cloud-object-storage/libraries/python.html) SDKs now provide support for [transferring data using the Aspera FASP protocol](/docs/services/cloud-object-storage/basics/aspera.html). Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage).

Buckets can now be created in a Single Data Center configuration in Seoul, South Korea. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

## Q3 2018
### September 2018
Users can [archive cold data](/docs/services/cloud-object-storage/basics/archive.html) by setting the proper parameters in a bucket lifecycle configuration policy, either using the console, REST API, or a language-specific SDK.

The [Java](/docs/services/cloud-object-storage/libraries/java.html) and [Python](/docs/services/cloud-object-storage/libraries/python.html) SDKs now provide support for [transferring data using the Aspera FASP protocol](/docs/services/cloud-object-storage/basics/aspera.html). Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage).

### August 2018
Buckets can now be created in a Single Data Center configuration in Sao Paolo, Brazil and Oslo, Norway. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

## Q2 2018
### June 2018
Users who upload or download files or folders using the web-based console have the option to use [Aspera high-speed transfer](https://www.ibm.com/cloud/high-speed-data-transfer) for these operations via a browser plug-in.  This allows for transfers of objects larger than 200MB using the console, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/services/cloud-object-storage/basics/aspera.html) documentation. Downloads using Aspera high-speed incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage).


Buckets can now be created in the EU Germany region. Data stored in these buckets is distributed across three availability zones in the EU Germany region.  More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html) documentation.

Buckets can now be created in a Single Data Center configuration in Chennai, India and Amsterdam, Netherlands. This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

## Q1 2018
### March 2018
Users who upload or download files using the web-based console have the option to use [Aspera high-speed transfer](https://www.ibm.com/cloud/high-speed-data-transfer) for these operations via a browser plug-in.  This allows for transfers of objects larger than 200MB, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/services/cloud-object-storage/basics/aspera.html) documentation.

Buckets can now be created in an AP Cross Region configuration. Data stored in these buckets is distributed across the Seoul, Tokyo, and Hong Kong data centers.  More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html).

Users can run `SELECT` SQL queries directly against structured data objects using IBM Cloud SQL Query.  More information can be found in the [SQL Query documentation](/docs/services/sql-query/sql-query.html).

### February 2018
Buckets can now be created in a Single Data Center configuration in Toronto, Canada and Melbourne, Australia.  This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html) documentation.

### January 2018
The IBM COS SDK for Java has been updated to 2.0. This release primarily fixes an issue for users trying to connect to IBM COS and AWS services within the same application by changing the namespacing for the library from `amazonaws` to `ibm.cloud.objectstorage`. For more information check out the [Github repository](https://github.com/IBM/ibm-cos-sdk-java) and the [API documentation](https://ibm.github.io/ibm-cos-sdk-java).

## Q4 2017

### December 2017
The IBM COS SDK for Python has been updated to 2.0.  This release primarily fixes an issue for users trying to connect to IBM COS and AWS services within the same application by changing the namespacing for the library from `boto3` to `ibm_boto3`.  For more infomation check out the [Github repository](https://github.com/IBM/ibm-cos-sdk-python), the [API documentation](https://ibm.github.io/ibm-cos-sdk-python), or this [blog post](https://www.ibm.com/blogs/bluemix/2017/11/ibm-cloud-object-storage-enhancements-help-companies-better-manage-access-data-app-development-analytics/).
