---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 

# Using `Postman`
{: #postman}

Here's a basic `Postman` setup for the {{site.data.keyword.cos_full}} REST API. Additional detail can be found in the API reference for [buckets](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html) or [objects](/docs/services/cloud-object-storage/api-reference/api-reference-objects.html).

Using `Postman` assumes a certain amount of familiarity with object storage and the necessary information from a [service credential](/docs/services/cloud-object-storage/iam/service-credentials.html) or the [console](/docs/services/cloud-object-storage/getting-started.html).  If any terms or variables are unfamiliar they can be found in the [glossary](/docs/services/cloud-object-storage/basics/glossary.html).

Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}

## REST API client overview
{: #postman-rest}

REST (REpresentational State Transfer) is an architectural style that provides a standard for computer systems to
interact with each other over the web, typically using standard HTTP URLs and verbs (GET, PUT, POST, etc.) which are supported by all major development languages and platforms. However, interacting with a REST API is not as simple as using a standard internet browser. Simple browsers do not allow any manipulation of the URL request.  This is where a REST API client comes in.

A REST API client provides a simple GUI-based application to interface with an existing REST API library. A good client makes it easy to test, develop, and document APIs by allowing users to quickly put together both simple and complex HTTP requests. Postman is an excellent REST API client that provides a complete API development environment that include built-in tools for design and mock, debug, test, documentation, monitor, and publish APIs. It also provides helpful features such as Collections and Workspaces that make collaboration a cinch. 

## Prerequisites
{: #postman-prereqs}
* IBM Cloud account
* [Cloud Storage resource created](https://cloud.ibm.com/catalog/) (lite/free plan works fine)
* [IBM Cloud CLI installed and configured](https://cloud.ibm.com/docs/services/cloud-object-storage/getting-started-cli.html)
* [Service Instance ID for your Cloud Storage](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [IAM (Identity and Access Management) Token](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [Endpoint for your COS bucket](/docs/services/cloud-object-storage/basics/endpoints.html)

### Create a bucket
{: #postman-create-bucket}
1.	Launch Postman
2.	In the New Tab select `PUT` the drop-down list
3.	Enter the endpoint in the address bar and add the name for your new bucket.
a.	Bucket names must be unique across all buckets so choose something specific.
4.	In the Type drop-down list select Bearer Token.
5.	Add the IAM Token in the Token box.
6.	Click on Preview Request.
a.	You should see a confirmation message that the headers were added.
7.	Click on the Header tab where you should see an existing entry for Authorization.
8.	Add a new key.
a.	Key: `ibm-service-instance-id`
b.	Value: Resource Instance ID for your cloud storage service.
9.	Click Send.
10.	You will receive a status `200 OK` message.

### Create a new text file
{: #postman-create-text-file}

1.	Create a new tab by clicking the Plus (+) icon.
2.	Select `PUT` from the list.
3.	In the address bar, enter the endpoint address with the bucket name from previous section and a file name.
4.	In the Type list select Bearer Token.
5.	Add the IAM Token in the token box.
6.	Select the Body tab.
7.	Select raw option and ensure Text is selected.
8.	Enter text in the provided space.
9.	Click Send.
10.	You will receive a status `200 OK` message.

### List the contents of a bucket
{: #postman-list-objects}

1.	Create a new tab by selecting the Plus (+) icon.
2.	Verify `GET` is selected in the list.
3.	In the address bar, enter the endpoint address with the bucket name from the previous section.
4.	In the Type list, select Bearer Token.
5.	Add the IAM Token in the token box.
6.	Click Send.
7.	You will receive a status `200 OK` message.
8.	In the Body of the Response section there is an XML message with the list of files in your bucket.

## Using the sample collection
{: #postman-collection}

A Postman Collection is available for [download ![External link icon](../icons/launch-glyph.svg "External link icon")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window} with configurable {{site.data.keyword.cos_full}} API request samples.

### Import the collection to Postman
{: #postman-import-collection}

1. In Postman click on the Import button in the upper right corner
2. Import the Collection file using either of these methods:
    * From the Import window drag and drop the Collection file into the window labeled **Drop files here**
    * Click on the Choose Files button and browse to the folder and select the Collection file
3. *IBM COS* should now appear in the Collections window
4. Expand the Collection and you should see twenty (20) sample requests
5. The Collection contains six (6) variables that will need to be set in order to successfully execute the API requests
    * Click on the three dots to the right of the collection to expand the menu and click Edit
6. Edit the variables to match your Cloud Storage environment
    * **bucket** - Enter the name for the new bucket you wish to create (bucket names must be unique across Cloud Storage).
    * **serviceid** - Enter the CRN of your Cloud Storage service.  Instructions to obtain your CRN are available [here](/docs/cli?topic=cloud-cli-ibmcloud-cli#ibmcloud-cli).
    * **iamtoken** - Enter the OAUTH token for your Cloud Storage service.  Instructions to obtain your OAUTH token are available [here](/docs/cli?topic=cloud-cli-ibmcloud-cli#ibmcloud-cli).
    * **endpoint** - Enter the regional endpoint for your Cloud Storage service.  Obtain the available endpoints from the [IBM Cloud Dashboard](https://cloud.ibm.com/resources/){:new_window}
        * *Ensure that your selected endpoint matches your key protect service to ensure the samples run correctly*
    * **rootkeycrn** - The CRN of the Root Key created in your primary Key Protect service.
        * The CRN should resemble the following:<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Ensure the Key Protect service selected matches the region of the Endpoint*
    * **bucketlocationvault** - Enter the location constraint value for the bucket creation for the *Create New Bucket (different storage class)* API request.
        * Acceptable values include:
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. Click on Update

### Running the samples
{: #postman-samples}
The API sample requests are fairly straightforward and easy to use.  They are designed to run in order and demonstrate how to interact with Cloud Storage.  They can also be used to run a functional test against your Cloud Storage service to ensure proper operation.

<table>
    <tr>
        <th>Request</th>
        <th>Expected Result</th>
        <th>Test Results</th>
    </tr>
    <tr>
        <td>Retrieve list of buckets</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body you should set an XML list of the buckets in your cloud storage.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected content</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Create new bucket</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Create new text file</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Create new binary file</td>
        <td>
            <ul>
                <li>
                    Click on Body and click on Choose File to select an image to upload
                </li>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Retrieve list of files from bucket</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see the two files you created in the previous requests
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Retrieve list of files from bucket (filter by prefix)</td>
        <td>
            <ul>
                <li>Change the querystring value to prefix=&lt;some text&gt;</li>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see the files with names that start with the prefix specified
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Retrieve text file</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see the text you entered in the previous request
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected body content</li>
                <li>Response contains expected header</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Retrieve binary file</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see the image you chose in the previous request
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected header</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Retrieve list of failed multipart uploads</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see any failed multipart uploads for the bucket
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected content</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Retrieve list of failed multipart uploads (filter by name)</td>
        <td>
            <ul>
                <li>Change the querystring value to prefix=&lt;some text&gt;</li>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see any failed multipart uploads for the bucket with names that start with the prefix specified
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected content</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Set CORS enabled bucket</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Retrieve bucket CORS config</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
                <li>
                    In the Body of the response you should see the CORS configuration set for the bucket
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
                <li>Response contains expected content</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Delete bucket CORS config</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Delete text file</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Delete binary file</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Delete bucket</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Create new bucket (different storage class)</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Delete bucket (different storage class)</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Create new bucket (key protect)</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Delete bucket (key protect)</td>
        <td>
            <ul>
                <li>Status Code 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Request was successful</li>
            </ul>
        </td>                
    </tr>
</table>

## Using the Postman Collection Runner
{: #postman-runner}

The Postman Collection Runner provides a user interface for testing a collection and allows you to run all requests in a Collection at once. 

1. Click on the Runner button in the upper right corner on the main Postman window.
2. In the Runner window, select the IBM COS collection and click on the big blue **Run IBM COS** button at the bottom of the screen.
3. The Collection Runner window will show the iterations as the requests are run.  You will see the test results appear below each of the requests.
    * The **Run Summary** displays a grid view of the requests and allows filtering of the results.
    * You can also click on **Export Results** which will save the results to a JSON file.
