---

copyright:
  years: 2018
lastupdated: "2018-08-03"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Using Duplicati

This guide walks you through using Duplicati to perform backups to Cloud Object Storage (S3) and restore files from Cloud Object Storage (S3).

## Prerequisites

* [Duplicati downloaded and installed ](https://www.duplicati.com/download) on the drive you want backed up.
* Cloud Object Storage (S3) Account credentials
* Port 80 (http) and/or 443 (https) open for inbound and outbound communication with the Cloud Object Storage (S3) endpoints. Port 443 is required if you choose the SSL option during the backup configuration process.
* Firefox or Google Chrome installed if you would like to use SSL. 

## Running Duplicati for the first time

This guide focuses on the Web GUI interaction with Duplicati but there is also a command line interface for those customers interested in being able to script it during deployment. 

The first time you launch Duplicati, the browser opens to the URL http://127.0.0.1:8200/ngax/index.html. 

## Creating a backup Job

To create a backup job.

1. Click Add backup.
2. Provide the backup job a name.
3. Set an encryption passphrase. 

**Note:** While it is not necessary to encrypt the backups it is highly recommended. 
[:tip}

4. Once set, click Next.

On the next page choose 'S3 Compatible' from the drop-down next to Storage Type.

Click  'Use SSL' checkbox and in the Server drop-down choose Custom URL. 
This will provide you with an additional box to specify the US Geo Cloud Object Storage (S3) endpoint. 

In this example I am using the Private network endpoint (s3-api.us-geo.objectstorage.service.networklayer.com). 

Provide a name for the bucket you would like the backups to go to. Duplicati will create the bucket if it does not already exist. 

You can leave both 'Bucket create region' and 'Storage class' set to Default and then specify a sub-folder for your backups to reside in if you so desire. In the bottom 2 boxes provide your 'Access Key ID' and 'Secret Access Key'. Due to incompatibility with SSLv3 and TLS1 we will need to make one more adjustment. To do this click on 'Advanced options', select 'allowed-ssl-versions' from the drop-down, and set the version to either TLS1.1 or TLS1.2.

Click 'Test Connection' and you will likely be greeted with the message 'The bucket name should start with your username, prepend automatically?'. 

Click Yes and have Duplicati create the Bucket. If everything went smoothly you should now see a box that says 'Connection worked!', 

click Ok and scroll to the bottom of the page and click Next.

On this page you will select the files and/or the directories you would like to backup to Cloud Object Storage (S3). The configuration supports filtering and the ability to exlude certain files based on specific attributes. When you have selected the files/directories to backup click Next to set your backup schedule.

With your schedule set click Next and set the 'Upload' (chunk) size for the backups as well as the retention scheme and click Save. Your backup job is not set up and ready to go. You can click 'Run now' to trigger a manual run of the backup job.

## Restoring Files

The first time you start a restore process in Duplicati you will be prompted for your Cloud Object Storage (S3) credentials again. Follow the same steps as you did when first creating the backup including changing the Allowed SSL Versions under the Advanced Options settings. Test the connection and if everything worked, click Next.

On the subsequent page enter the encryption password used when you created the backups or leave the box blank and click Next. On the following page you will see a box labeled 'Restore from' where you can select the specific backup you would like to restore from as as well as which files and directories you would like restored. After you have made your selections click Continue.

When restoring files you have the option of restoring them to their original location or a new location if you would like to compare them before making a change. If you choose to restore them to their original location you can also have Duplicato prepend the filenames with a timestamp to distinguish them from the files currently on your server. You can also choose to restore the original file permissions. Once you have everything set click Restore to start the process.

This will start the restore process and give you a progress indicator along with an ETA on when the restore will be completed.