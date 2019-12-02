---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# Using `Postman`
{: #postman}

Here's a basic `Postman` setup for the {{site.data.keyword.cos_full}} REST API. More detail can be found in the API reference for [buckets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) or [objects](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).

Using `Postman` assumes a certain amount of familiarity with Object Storage and the necessary information from a [service credential](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) or the [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). If any terms or variables are unfamiliar, they can be found in the [glossary](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location, or any other means.
{:tip}

## REST API client overview
{: #postman-rest}

Interacting with a REST API isn't as simple as using a standard internet browser. Simple browsers do not allow any manipulation of the URL request. A REST API client can help quickly put together both simple and complex HTTP requests.  

## Prerequisites
{: #postman-prereqs}
* IBM Cloud account
* [Cloud Storage resource created](https://cloud.ibm.com/catalog/) (lite plan works fine)
* [IBM Cloud CLI installed and configured](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [Service Instance ID for your Cloud Storage](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [IAM (Identity and Access Management) Token](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [Endpoint for your COS bucket](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### Create a bucket
{: #postman-create-bucket}
1.	Start Postman
2.	In the **New** tab, select `PUT`.
3.	Enter the endpoint in the address bar and add the name for your new bucket.
a.	Bucket names must be unique across all buckets, so choose something specific.
4.	In the **Type** menu, select Bearer Token.
5.	Add the IAM Token in the Token box.
6.	Click **Preview Request**.
a.	You should see a confirmation message that the headers were added.
7.	Click the **Header** tab where you should see an existing entry for Authorization.
8.	Add a key.
a.	Key: `ibm-service-instance-id`
b.	Value: Resource Instance ID for your cloud storage service.
9.	Click Send.
10.	You'll receive a status `200 OK` message.

### Create a text file object
{: #postman-create-text-file}

1.	Create a tab by clicking the Plus (+) icon.
2.	Select `PUT` from the list.
3.	In the address bar, enter the endpoint address with the bucket name from previous section and a file name.
4.	In the Type list select Bearer Token.
5.	Add the IAM Token in the token box.
6.	Select the Body tab.
7.	Select raw option and ensure that Text is selected.
8.	Enter text in the provided space.
9.	Click Send.
10.	You receive a status `200 OK` message.

### List the contents of a bucket
{: #postman-list-objects}

1.	Create a new tab by selecting the Plus (+) icon.
2.	Verify `GET` is selected in the list.
3.	In the address bar, enter the endpoint address with the bucket name from the previous section.
4.	In the Type list, select Bearer Token.
5.	Add the IAM Token in the token box.
6.	Click Send.
7.	You receive a status `200 OK` message.
8.	In the Body of the Response section is an XML message with the list of files in your bucket.

## Using the sample collection
{: #postman-collection}

A Postman Collection is available for [download](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: external} with configurable {{site.data.keyword.cos_full}} API request samples.

### Import the collection to Postman
{: #postman-import-collection}

1. In Postman click **Import** in the upper right corner
2. Import the Collection file by using either of these methods:
    * From the Import window drag the Collection file into the window labeled **Drop files here**
    * Click the Choose Files button and browse to the folder and select the Collection file
3. *IBM COS* now appears in the Collections window
4. Expand the Collection and see 20 sample requests
5. Click the three dots to the right of the collection to expand the menu and click **Edit**
6. Edit the variables to match your Cloud Storage environment
    * `bucket` - Enter the name for the new bucket you want to create (bucket names must be unique across Cloud Storage).
    * `serviceid` - Enter the CRN of your Cloud Storage service. Instructions to obtain your CRN are available [here](/docs/overview?topic=overview-crn).
    * `iamtoken` - Enter the OAUTH token for your Cloud Storage service. Instructions to obtain your OAUTH token are available [here](/docs/services/key-protect?topic=key-protect-retrieve-access-token).
    * `endpoint` - Enter the regional endpoint for your Cloud Storage service. Obtain the available endpoints from the [IBM Cloud Dashboard](https://cloud.ibm.com/resources/){: external}
        * *Ensure that your selected endpoint matches your key protect service to ensure that the samples run correctly*
    * `rootkeycrn` - The CRN of the Root Key created in your primary Key Protect service.
        * The CRN resembles `crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Ensure the Key Protect service that is selected matches the region of the Endpoint*
    * `bucketlocationvault` - Enter the location constraint value for the bucket creation for the *Create New Bucket (different storage class)* API request.
        * Acceptable values include:
            * `us-south-vault`
            * `us-standard-flex`
            * `eu-cold`
7. Click Update

### Running the samples
{: #postman-samples}
The API sample requests are fairly straightforward and easy to use. They're designed to run in order and demonstrate how to interact with Cloud Storage. You can also run a functional test against your Cloud Storage service to ensure proper operation.

| Request                                                    | Expected Result                                                                                                                                                                               | Test Results                                                                                       |
|------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| Retrieve list of buckets                                   | In the Body, you set an XML list of the buckets in your cloud storage.                                                                                                                        | Request was successful. Response contains expected content                                         |
| Create new bucket                                          | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Create new text file                                       | Status Code 200 OK                                                                                                                                                                            | Request was successful. Response contains expected header                                          |
| Create new binary file                                     | Click **Body** and click **Choose File** to select an image to upload.                                                                                                                                | Request was successful. Response contains expected header                                          |
| Retrieve list of files from bucket                         | In the Body of the response you see the two files you created in the previous requests.                                                                                                | Request was successful. Response contains expected header                                          |
| Retrieve list of files from bucket (filter by prefix)      | Change the querystring value to `prefix=<some text>`. In the body of the response you see the files with names that start with the prefix specified.                                   | Request was successful. Response contains expected header                                          |
| Retrieve text file                                         | In the Body of the response you see the text you entered in the previous request                                                                                                       | Request was successful. Response contains expected body content. Response contains expected header |
| Retrieve binary file                                       | In the Body of the response you see the image you chose in the previous request.                                                                                                       | Request was successful. Response contains expected header                                          |
| Retrieve list of failed multipart uploads                  | In the Body of the response you see any failed multipart uploads for the bucket.                                                                                                       | Request was successful. Response contains expected content                                         |
| Retrieve list of failed multipart uploads (filter by name) | Change the querystring value to `prefix=<some text>`. In the body of the response you see any failed multipart uploads for the bucket with names that start with the prefix specified. | Request was successful. Response contains expected content                                         |
| Set CORS enabled bucket                                    | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Retrieve bucket CORS config                                | In the body of the response you see the CORS configuration set for the bucket                                                                                                         | Request was successful. Response contains expected content                                         |
| Delete bucket CORS config                                  | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Delete text file                                           | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Delete binary file                                         | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Delete bucket                                              | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Create new bucket (different storage class)                | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Delete bucket (different storage class)                    | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Create new bucket (key protect)                            | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |
| Delete bucket (key protect)                                | Status Code 200 OK                                                                                                                                                                            | Request was successful                                                                             |


## Using the Postman Collection Runner
{: #postman-runner}

The Postman Collection Runner provides a user interface for testing a collection and allows you to run all requests in a Collection at once. 

1. Click the Runner button in the upper right corner on the main Postman window.
2. In the Runner window, select the IBM COS collection and click the big blue **run IBM COS** button at the bottom of the screen.
3. The Collection Runner window will show the iterations as the requests are run. You will see that the test results appear below each of the requests.
    * The **Run Summary** displays a grid view of the requests and allows filtering of the results.
    * You can also click **Export Results** to save the results to a JSON file.
