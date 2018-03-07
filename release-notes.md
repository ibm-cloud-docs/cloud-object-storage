---

copyright:
  years: 2017
lastupdated: "28-11-2017"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# What's new

## Q1 2018

### March 2018
Users who upload or download files using the web-based console have the option to use [Aspera high-speed transfer](https://www.ibm.com/cloud/high-speed-data-transfer) for these operations via a browser plug-in.  This allows for transfers of objects larger than 200MB, and also allows for greater control and visibility of uploads and downloads. Additional information can be found in the [Uploading Data](/docs/services/cloud-object-storage/basics/upload.html#high-speed-transfer) documentation.

Buckets can now be created in an AP Cross Region configuration.
 Data stored in these buckets is distributed across the Seoul, Tokyo, and Hong Kong data centers.  More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html) documentation.

### February 2018
Buckets can now be created in a Single Data Center configuration in Toronto and Melbourne.  This allows for lower latency when accessing storage from compute resources co-located within the same data center, or for data requiring a specific geographic location. More information can be found in the [Select Regions and Endpoints](/docs/services/cloud-object-storage/basics/endpoints.html) documentation.  

### January 2018
The IBM COS SDK for Java has been updated to 2.0. This release primarily fixes an issue for users trying to connect to IBM COS and AWS services within the same application by changing the namespacing for the library from `amazonaws` to `ibm.cloud.objectstorage`. For more information check out the [Github repository](https://github.com/IBM/ibm-cos-sdk-java) and the [API documentation](https://ibm.github.io/ibm-cos-sdk-java).

## Q4 2017

### December 2017
The IBM COS SDK for Python has been updated to 2.0.  This release primarily fixes an issue for users trying to connect to IBM COS and AWS services within the same application by changing the namespacing for the library from `boto3` to `ibm_boto3`.  For more infomation check out the [Github repository](https://github.com/IBM/ibm-cos-sdk-python), the [API documentation](https://ibm.github.io/ibm-cos-sdk-python), or this [blog post](https://www.ibm.com/blogs/bluemix/2017/11/ibm-cloud-object-storage-enhancements-help-companies-better-manage-access-data-app-development-analytics/).
