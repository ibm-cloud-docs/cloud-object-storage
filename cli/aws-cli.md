---

copyright:
  years: 2017, 2018
lastupdated: "15-03-2018"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Use the AWS CLI

The official command line interface for AWS is compatible with the IBM COS S3 API. Written in Python, it can be installed from the Python Package Index via `pip install awscli`. By default, access keys are sourced from `~/.aws/credentials`, but can also be set as environment variables.

These examples have been generated using version 1.14.2 of the CLI.  To check the version installed, run `aws --version`.

## 2. Configure the CLI to connect to {{site.data.keyword.cos_short}}
To configure AWS CLI, type `aws configure` and provide your [HMAC credentials](/docs/services/cloud-object-storage/hmac/credentials.html) and a default region name.  The "region name" used by AWS S3 corresponds to the provisioning code (`LocationConstraint`) that {{site.data.keyword.cos_short}} uses to define the storage class of new buckets.

Valid provisioning codes for `LocationConstraint` are: <br>
&emsp;&emsp;  `us-standard` / `us-vault` / `us-cold` / `us-flex` <br>
&emsp;&emsp;  `us-east-standard` / `us-east-vault`  / `us-east-cold` / `us-east-flex` <br>
&emsp;&emsp;  `us-south-standard` / `us-south-vault`  / `us-south-cold` / `us-south-flex` <br>
&emsp;&emsp;  `eu-standard` / `eu-vault` / `eu-cold` / `eu-flex` <br>
&emsp;&emsp;  `eu-gb-standard` / `eu-gb-vault` / `eu-gb-cold` / `eu-gb-flex` <br>
&emsp;&emsp;  `ap-standard` / `ap-vault` / `ap-cold` / `ap-flex` <br>
&emsp;&emsp;  `che01-standard` / `che01-vault` / `che01-cold` / `che01-flex` <br>
&emsp;&emsp;  `mel01-standard` / `mel01-vault` / `mel01-cold` / `mel01-flex` <br>
&emsp;&emsp;  `tor01-standard` / `tor01-vault` / `tor01-cold` / `tor01-flex` <br>

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

This creates two files:

 `~/.aws/credentials`:

```
[default]
aws_access_key_id = {Access Key ID}
aws_secret_access_key = {Secret Access Key}
```
{:codeblock}

`~/.aws/config`:

```
[default]
region = {Provisioning Code}
output = json
```
{:codeblock}


You can also use environment variables to set HMAC credentials:

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


The IBM COS endpoint must be sourced using the `--endpoint-url` option, and can not be set in the credentials file.


## High-level syntax commands
Simple use cases can be accomplished using `aws --endpoint-url {endpoint} s3 <command>`. Objects are managed using familiar shell commands, such as `ls`, `mv`, `cp`, and `rm`.  Buckets can be created using `mb` and deleted using `rb`.

### List all buckets within a service instance

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### List objects within a bucket

Note that it is not possible to create buckets for use with Key Protect when using HMAC credentials for authorization.

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### Make a new bucket

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}

If the default region in the `~/.aws/config` file corresponds the same location as the chosen endpoint, then bucket creation is straightforward.

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### Add an object to a bucket

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

Alternatively, you can set a new object key that is different from the file name:

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### Copying an object from one bucket to another within the same region:

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### Delete an object from a bucket

```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### Remove a bucket

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### Create presigned URLs
The CLI is also capable of creating pre-signed URLs.  These allow for temporary public access to objects without changing any existing access controls.  The URL that is generated contains an HMAC signature that obfuscates the URI, making it less likely that users without the full URL would be able to access an otherwise publicly accessible file.

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

It is also possible to set an expiration time for the URL in seconds (default is 3600):

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## Low-level syntax commands

The AWS CLI also allows direct API calls that provide the same responses as direct HTTP requests by using the `s3api` command.

### Listing buckets:

```bash
$ aws --endpoint-url {endpoint} s3api list-buckets
{
    "Owner": {
        "DisplayName": "{storage-account-uuid}",
        "ID": "{storage-account-uuid}"
    },
    "Buckets": [
        {
            "CreationDate": "2016-09-09T12:48:52.442Z",
            "Name": "bucket-1"
        },
        {
            "CreationDate": "2016-09-16T21:29:00.912Z",
            "Name": "bucket-2"
        }
    ]
}
```

### Listing objects within a bucket

```sh
$ aws --endpoint-url {endpoint} s3api list-objects --bucket bucket-1
```

```json
{
    "Contents": [
        {
            "LastModified": "2016-09-28T15:36:56.807Z",
            "ETag": "\"13d567d518c650414c50a81805fff7f2\"",
            "StorageClass": "STANDARD",
            "Key": "c1ca2-filename-00001",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 837
        },
        {
            "LastModified": "2016-09-09T12:49:58.018Z",
            "ETag": "\"3ca744fa96cb95e92081708887f63de5\"",
            "StorageClass": "STANDARD",
            "Key": "c9872-filename-00002",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 533
        },
        {
            "LastModified": "2016-09-28T15:36:17.573Z",
            "ETag": "\"a54ed08bcb07c28f89f4b14ff54ce5b7\"",
            "StorageClass": "STANDARD",
            "Key": "98837-filename-00003",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 14476
        },
        {
            "LastModified": "2016-10-06T14:46:26.923Z",
            "ETag": "\"2bcc8ee6bc1e4b8cd2f9a1d61d817ed2\"",
            "StorageClass": "STANDARD",
            "Key": "abfc4-filename-00004",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 20950
        }
    ]
}
```
