---

copyright:
  years: 2018
lastupdated: "2018-05-17"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Using `Postman`

Here's a basic `Postman` setup for the {{site.data.keyword.cos_full}} REST API. Additional detail can be found in the API reference for [buckets](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html) or [objects](/docs/services/cloud-object-storage/api-reference/api-reference-objects.html).

Using `Postman` assumes a certain amount of familiarity with object storage and the necessary information from a [service credential](/docs/services/cloud-object-storage/iam/service-credentials.html) or the [console](/docs/services/cloud-object-storage/getting-started.html).  If any terms or variables are unfamiliar they can be found in the [glossary](/docs/services/cloud-object-storage/basics/glossary.html).

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}
## REST API client overview

REST (REpresentational State Transfer) is an architectural style that provides a standard for computer systems to
interact with each other over the web, typically using standard HTTP URLs and verbs (GET, PUT, POST, etc.) which are supported by all major development languages and platforms. However, interacting with a REST API is not as simple as using a standard internet browser. Simple browsers do not allow any manipulation of the URL request.  This is where a REST API client comes in.

A REST API client provides a simple GUI-based application to interface with an existing REST API library. A good client makes it easy to test, develop, and document APIs by allowing users to quickly put together both simple and complex HTTP requests. Postman is an excellent REST API client that provides a complete API development environment that include built-in tools for design and mock, debug, test, documentation, monitor, and publish APIs. It also provides helpful features such as Collections and Workspaces that make collaboration a cinch. 

## Prerequisites
* IBM Cloud account
* Cloud Storage resource created (lite/free plan works fine)
  - https://console.bluemix.net/docs/tutorials/static-files-cdn.html#create_cos
* IBM Cloud CLI installed configured
  - https://console.bluemix.net/docs/services/cloud-object-storage/getting-started-cli.html
* Resource Instance ID for your Cloud Storage
  - https://console.bluemix.net/docs/services/cloud-object-storage/getting-started-cli.html#gather-key-information
* IAM (Identity and Access Management) Token 
  - https://console.bluemix.net/docs/services/cloud-object-storage/getting-started-cli.html#gather-key-information
* Endpoint for your COS resource
  - IBM Cloud Dashboard – Cloud Storage Instance: Use the entry under Public/us-geo
-------------------------------------
### Create a bucket
----------------------------
1.	Launch Postman
2.	In the New Tab select PUT the drop-down list
3.	Enter the endpoint in the address bar and add the name for your new bucket.
a.	Bucket names must be unique across all buckets so choose something specific.
4.	In the Type drop-down list select Bearer Token.
5.	Add the IAM Token in the Token box.
6.	Click on Preview Request.
a.	You should see a confirmation message that the headers were added.
7.	Click on the Header tab where you should see an existing entry for Authorization.
8.	Add a new key.
a.	Key: ibm-service-instance-id
b.	Value: Resource Instance ID for you cloud storage service.
9.	Click Send.
10.	You will receive a status 200 OK message.
----------------------------
### Create a new text file
----------------------------
1.	Create a new tab by clicking the Plus (+) icon.
2.	Select PUT from the list.
3.	In the address bar, enter the endpoint address with the bucket name from previous section and a file name.
4.	In the Type list select Bearer Token.
5.	Add the IAM Token in the token box.
6.	Select the Body tab.
7.	Select raw option and ensure Text is selected.
8.	Enter text in the provided space.
9.	Click Send.
10.	You will receive a status 200 OK message.
----------------------------
### List the contents of a bucket
---------------------------
1.	Create a new tab by selecting the Plus (+) icon.
2.	Verify GET is selected in the list.
3.	In the address bar, enter the endpoint address with the bucket name from the previous section.
4.	In the Type list, select Bearer Token.
5.	Add the IAM Token in the token box.
6.	Click Send.
7.	You will receive a status 200 OK message.
8.	In the Body of the Response section there is an XML message with the list of files in your bucket.
