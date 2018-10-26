---

copyright:
  years: 2017, 2018
lastupdated: "19-10-2018"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Use the IBM Cloud CLI

The Cloud Object Storage plugin for command-line interface (CLI) provides extra capabilities for working with IBM® Cloud Object Storage. You can use the Cloud Object Storage plugin for CLI to manage and maintain buckets and objects withing IBM® Cloud Object Storage.

**Note:** The Cloud Object Storage plugin for command-line interface (CLI) is compatible with Windows, Linux, MacOS V10 and must be a 64-bit machine.

The beta version of a {{site.data.keyword.cos_short}} plugin [{{site.data.keyword.cloud}} Platform CLI](https://clis.ng.bluemix.net/ui/home.html) is available for anyone interested in trying it out.  Note that this plug-in is beta software and subject to change, and is not suitable for production workloads. If you have any comments or suggestions on the CLI plugin, click on the "Feedback" button on this page and select the relevant section of the documentation.
{:tip}

## Installation and configuration

Install the plugin using the `plugin install` command.

```
ibmcloud plugin install cloud-object-storage
```

Configure the plugin with `ibmcloud cos config`.  This will populate your credentials or repopulate them if your _~/.bluemix/config.json_ file has reset. 


The program also offers the ability for you to set the default local directory for downloaded files (by default, this is set to `~/Downloads`), and to set a default region. To set the default download location, type `ibmcloud cos config --dl` and input into the program a valid file path. To set a default region, type `ibmcloud cos config --region` and provide a input into the program a region code, such as `us-south`. By default, this value is set to `us-geo`.


  After a period of inactivity, the credentials under ~/.bluemix/config.json will reset.
  Re-login to IBM Cloud using `ibmcloud login` to repopulate the necessary credentials to use with the plugin.
  {:tip}

### HMAC Credentials
If preferred, HMAC credentials associated with a Service ID can be used to connect to object storage in lieu of the user API key used to authenticate to the CLI itself. To generate HMAC credentials, you can add the `{"HMAC":true}` parameter when generating a [service credential](/docs/services/cloud-object-storage/hmac/credentials.html). Then you can run `ibmcloud cos config --hmac` to input your HMAC credentials into the program (please note that you must switch priority to HMAC credentials using `ibmcloud cos config --switch` after inputing HMAC credentials). If you choose to use IAM authentication (the standard IBM way to authenticate), you do not need to provide any credentials – the program will authenticate you automatically.

At any time, to switch between HMAC and IAM authentication, you can type `ibmcloud cos config --switch [iam | hmac]`. For more information on IAM-based authentication, click [here](https://console.bluemix.net/docs/iam/quickstart.html#getstarted).

## Supported commands

Each operation listed below has an explanation of what it does, how to use it, and any optional or required parameters. Unless specified as optional, any listed parameters are mandatory.

The IBM Cloud CLI mandates that commands start with `ibmcloud`. However, until the "Bluemix" brand is phased out, you can also start commands by `bx` and `bluemix`. So, you can do `ibmcloud cos`, `bluemix cos`, or `bx cos` to start a command.

### Create a new bucket
* **Action:** Create a new bucket in an IBM Cloud Object Storage instance.
* **Usage:** `ibmcloud cos create-bucket --bucket [BUCKET_NAME] [--region REGION_NAME] [--class CLASS_TYPE]`
* **Parameters to provide:**
    * The name of the new bucket.
    * _Optional_: The region where the bucket will be created. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
       * Flag: `--region REGION_NAME`
    * _Optional_: The class of the bucket. If this parameter is not provided, the program will create a bucket of class `standard`. Valid values for this string are `standard`, `vault`, `cold`, or `flex`.  
	   * Flag: `--class CLASS_TYPE`


### Delete an existing bucket
* **Action:** Delete an existing bucket in an IBM Cloud Object Storage instance.
* **Usage:** `ibmcloud cos delete-bucket --bucket [BUCKET_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
    * The name of the bucket to be deleted.
    * _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
       * Flag: `--region REGION_NAME`


### Find a bucket
* **Action:** Determine the location and class of a bucket in an IBM Cloud Object Storage instance. 
* **Usage:** `ibmcloud cos get-bucket-location --bucket [BUCKET_NAME]`
* **Parameters to provide:**
	* The name of the bucket to get the location.


### Get a bucket's class
* **Action:** Determine the class of a bucket in an IBM Cloud Object Storage instance.
* **Usage:** `ibmcloud get-bucket-class --bucket [BUCKET_NAME]`
* **Parameters to provide:**
	* The name of the bucket to get the class.
	

### Get a bucket's headers
* **Action:** Determine if a bucket exists in an IBM Cloud Object Storage instance.
* **Usage:** `ibmcloud cos head-bucket --bucket [BUCKET_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
	* The name of the bucket to get the details.
	* _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder. 
		* Flag: `--region REGION_NAME`

### List all buckets
* **Action:** Print a list of all the buckets in a user's IBM Cloud Object Storage account. Note that buckets may be located in different regions.
* **Usage:** `ibmcloud cos list-buckets`
* **Parameters to provide:**
    * No parameters to provide.


### Download an object
* **Action:** Download an object from a bucket in a user's IBM Cloud Object Storage account.
* **Usage:** `ibmcloud cos get-object --bucket [BUCKET_NAME] --key [KEY_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
    * Bucket name
    * Object name to download
	* _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
		* Flag: `--region REGION_NAME`

### Get an object's headers
* **Action:** Determine if a file exists in a bucket in a user's IBM Cloud Object Storage account.
* **Usage:** `ibmcloud cos head-object --bucket [BUCKET_NAME] --key [KEY_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
    * Bucket name to upload to
    * Name of the file to get the head of
	* _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
		* Flag: `--region REGION_NAME`


### Upload an object
* **Action:** Upload an object to a bucket in a user's IBM Cloud Object Storage account.
* **Usage:** `ibmcloud cos put-object --bucket [BUCKET_NAME] --key [KEY_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
    * Bucket name to upload to
    * Path of the file the user is uploading
	* _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
		* Flag: `--region REGION_NAME`


### Delete an object
* **Action:** Delete an object from a bucket in a user's IBM Cloud Object Storage account.
* **Usage:** `ibmcloud cos delete-object --bucket [BUCKET_NAME] --key [KEY_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
    * Bucket name to delete from
    * Object name to delete
	* _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
		* Flag: `--region REGION_NAME`

### Copy objects between buckets
* **Action:** Copy an object from source bucket to destination bucket.
* **Usage:** `ibmcloud cos copy-object --bucket [DESTINATION_BUCKET] --copysource [COPY_SOURCE] --key [KEY_NAME] [--region REGION_NAME] `
* **Parameters to provide:**
    * Destination bucket name
	* Copy Source name of the source bucket and key name of the source object, separated by a slash (/). Must be URL-encoded.
	*  The name of the key to copy to
	* _Optional_: Region which both buckets exists in, if not provided it will use the default region in config file.  It is not possible to copy between regions.
		* Flag: `--region REGION_NAME`

### List objects
* **Action:** List files present in a bucket in a user's IBM Cloud Object Storage Account.  This operation is currently limited to the 1000 most recently created objects and can't be filtered.
* **Usage:** `ibmcloud cos list-objects --bucket [BUCKET_NAME] [--region REGION_NAME]`
* **Parameters to provide:**
	* Bucket name to print the list of files
	* _Optional_: The region where the bucket is located. If this parameter is not provided, the program will use the `DefaultRegion` value in the `credentials.json` file located in the user's `.bluemix` folder.
		* Flag: `--region REGION_NAME`


### Configure the Program
* **Action:** Configure the program's preferences.
* **Usage:** `ibmcloud cos config [flag...]`. If no flag is provided, then the program will automatically run the Service Instance ID setup.
* **Parameters to provide:**
	* _Optional_: Save your HMAC credentials into the program (i.e., the access key and the secret key).
		* Flag: `--hmac`
	* _Optional_: Save your Service Instance ID (CRN) into the program.
		* Flag: `--crn`
	* _Optional_: Set the default download location for file downloads. Set to the user's Downloads folder by default.
		* Flag: `--dl`
	* _Optional_: Switch between HMAC and IAM-based authentication. Set to IAM-based authentication by default.
		* Flag: `--switch [iam | hmac]`
	* _Optional:_ Set the default region where the program will look for and create buckets. By default, this is set to `us-geo`.
		* Flag: `--region [region...]`


## Manually controlling multipart uploads 
The IBM Cloud Object Storage CLI provides the ability for users to upload large files in multiple parts using the AWS multipart upload functionality. To initiate a new multipart upload, run the `multipart-upload-create` command, which returns the new upload instance's upload ID. In order to continue with the upload process, you must save the upload ID for each subsequent command. This command requires you to generate an MD5 hash. Refer to the [Amazon documentation](https://aws.amazon.com/premiumsupport/knowledge-center/s3-multipart-upload-cli/) for more information on how to generate an MD5 hash for a multipart upload instance.

Once you have run the `multipart-upload-complete` command, run `part-upload` for each file part you wish to upload. **Note that for multipart uploads, every file part (except for the last part) must be at least 5MB in size.** To split a file into separate parts, you can run `split` in a terminal window. For example, if you have a 13MB file named `TESTFILE` on your Desktop, and you would like to split it into file parts of 5MB each, you can run `split -b 3m ~/Desktop/TESTFILE part-file-`. The Terminal will generate 3 file parts – two file parts of 5MB each, and one file part of 3MB, with the names `part-file-aa`, `part-file-ab`, and `part-file-ac`. For more information on this process, refer to the attached video in the [Amazon documentation.](https://aws.amazon.com/premiumsupport/knowledge-center/s3-multipart-upload-cli/)

As each file part is uploaded, the CLI will print out its ETag. You must save this ETag into an appropriately formatted JSON file, along with the part number. Below is an example of how the JSON file should be formatted. Use this template to create your own ETag JSON data file.

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "The ETag of the first file part goes here."
    },
    {
      "PartNumber": 2,
      "ETag": "The ETag of the second file part goes here."
    }
  ]
}
```

Add more entries to this JSON template as necessary.

To see the status of your multipart upload instance, you can always run the `part-list` command, providing the bucket name, key, and the upload ID. This will print out raw information about your multipart upload instance. Once you have completed uploading each part of the file, run the `multipart-upload-complete` command with the necessary parameters. If all goes well, you will receive a confirmation that the file uploaded successfully to the desired bucket.


### Create a new multipart upload
* **Action:** Begin the multipart file upload process by creating a new multipart upload instance.
* **Usage:** `ibmcloud cos create-multipart-upload --bucket [BUCKET_NAME] --key [KEY_NAME] --metadata [MD5_CODE]`
* **Parameters to provide:**
	* The name of the bucket where the file will be uploaded to.
		* Flag: `--bucket BUCKET_NAME`
	* The name of the file that is being uploaded (the key).
		* Flag: `--key FILE_NAME`
	* An MD5 hash of the file name.
		* Flag: `--metadata MD5_CODE`

### Upload a part
* **Action:** Upload a part of a file in an existing multipart upload instance.
* **Usage:** `ibmcloud cos upload-part --bucket [BUCKET_NAME] --key [FILE_NAME] --part-number [PART-NUMBER] --body [FILE_PATH] --upload-id [UPLOAD_ID]`
	* Note that you must save each uploaded file part's number and ETag (which the CLI will print out for you) for each part into a JSON file. Refer to the "Multipart Upload Guide" below for more information.
* **Parameters to provide:**
	* The name of the bucket where the file will be uploaded to.
		* Flag: `--bucket BUCKET_NAME`
	* The name of the file that is being uploaded via multipart.
		* Flag: `--key FILE_NAME`
	* The file part's number (i.e. whether it is the first, second, etc. part uploaded)
		* Flag: `--part-number PART_NUMBER`
	* The filepath of the file part.
		* Flag: `--body FILE_PATH`
	* The upload ID generated in the `multipart-upload-create` command.
		* Flag: `--upload-id UPLOAD_ID`


### List parts
* **Action:** Print out information about an in progress multipart upload instance.
* **Usage:** `ibmcloud cos list-parts --bucket [BUCKET_NAME] --key [KEY_NAME] --upload-id [UPLOAD_ID]`
* **Parameters to provide:**
	* The name of the bucket where the file will be uploaded to.
		* Flag: `--bucket BUCKET_NAME`
	* The name of the file that is being uploaded via multipart.
		* Flag: `--key FILE_NAME`
	* The upload ID generated in the `create-multipart-upload` command.
		* Flag: `--upload-id UPLOAD_ID`


### Abort a multipart upload
* **Action:** Abort a multipart upload instance by ending the upload to the bucket in the user's IBM Cloud Object Storage account.
* **Usage:** `ibmcloud cos abort-multipart-upload --bucket [BUCKET_NAME] --key [KEY] --upload-id [UPLOAD-ID]`
* **Parameters to provide:**
	* The bucket name where the abort of the multipart upload is taking place.
		* Flag: `--bucket BUCKET_NAME`
	* The name of the file that is being uploaded via multipart.
		* Flag: `--key FILE_NAME`
	* The upload ID generated in the `create-multipart-upload` command.
		* Flag: `--upload-id UPLOAD_ID`

### Complete a multipart upload
* **Action:** Complete a multipart upload instance by assembling the currently uploaded parts and uploading the file to the bucket in the user's IBM Cloud Object Storage account.
* **Usage:** `ibmcloud cos complete-multipart-upload --etag-data [ETAG_DATA_FILEPATH] --bucket [BUCKET_NAME] --key [KEY_NAME] --upload-id [UPLOAD_ID]`
* **Parameters to provide:**
	* The filepath of the JSON file that contains the ETag and part number data for each part uploaded. This is a file the user is required to create on their own. For a guide on how to create this file, refer to the "Multipart Upload Guide" below.
		* Flag: `--etag-data ETAG_DATA_FILEPATH`
	* The name of the bucket where the file will be uploaded to.
		* Flag: `--bucket BUCKET_NAME`
	* The name of the file that is being uploaded via multipart.
		* Flag: `--key FILE_NAME`
	* The upload ID generated in the `multipart-upload-create` command.
		* Flag: `--upload-id UPLOAD_ID`

### `credentials.json` Guide
The `credentials.json` file stores user preferences. The IBM Cloud Object Storage CLI reads and writes to this file periodically. Below is an example of a `credentials.json` file, and an explanation of each field:

```
{
    "CRN": "EXAMPLE_CRN",
    "Last Updated": "Wednesday, August 29 2018 at 11:11:38",
    "HMACProvided": "true",
    "DownloadLocation": "/Users/myusername/Downloads",
    "DefaultRegion": "eu-gb"
}
```

* The user's service instance ID (`CRN`)
* The last time the service instance ID was updated (`Last Updated`)
* The user's choice between HMAC and IAM authentication (`HMACProvided`)
* The user's default download location (`DownloadLocation`)
* The user's default region to look for and create buckets in (`DefaultRegion`)

This file is generated automatically by the `config` command and can be manually edited.
