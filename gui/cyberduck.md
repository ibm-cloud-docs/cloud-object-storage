---

copyright:
  years: 2017, 2018
lastupdated: "2018-10-05"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Transfer files using Cyberduck

## Cyberduck

Cyberduck is a popular, open-source, and easy to use cloud object storage browser for Mac and Windows.  Cyberduck is capable of calculating the correct authorization signatures needed to connect to IBM COS.  Cyberduck can be downloaded from [cyberduck.io/](https://cyberduck.io/).

To use Cyberduck to create a connection to IBM COS and synchronize a folder of local files to a bucket, follow these steps:

 1. Download, install, and start Cyberduck.
 2. The main window of the application opens, where you can create a connection to IBM COS. Click **Open Connection** to configure a connection to IBM COS.
 3. A pop-up window opens. From the drop-down menu at the top, select `S3 (HTTPS)`. Enter information into the following fields, and then click Connect:

    * `Server`: enter endpoint of IBM COS
        * *Ensure the endpoint region matches the intended bucket*
    * `Access Key ID`
    * `Secret Access Key`
    * `Add to Keychain`: Save the connection to the keychain to allow use across other applications *(optional)*

 4. Cyberduck takes you to the root of the account where buckets can be created. Right-click within the main panel and select **New Folder** (the application deals with many transfer protocols where Folder is the more common container construct). Enter the bucket name and then click Create.
 5. After the bucket is created, double-click the bucket to view it. Within the bucket you can perform various functions such as upload files to the bucket, list bucket contents, download objects from the bucket, synchronize local files to a bucket, synchronize objects to another bucket, or create an archive of a bucket.
 6. Right-click within the bucket and select **Synchronize**. A pop-up panel opens where you can browse to the folder that you want to synchronize to the bucket. Select the folder and click Choose.
 7. After you select the folder, a new pop-up panel opens. Here, a drop-down menu is available where you select the synchronization operation with the bucket. Three possible synchronize options are available from the menu:

    * `Download`: This will download changed and missing objects from the bucket.
    * `Upload`: This will upload changed and missing files to the bucket.
    * `Mirror`: This will perform both download and upload operations, ensuring that all new and updated files and objects are synchronized between the local folder and the bucket.

 8. Another window opens to show active and historical transfer requests. After the synchronization request is complete, the main window will perform a list operation on the bucket to reflect updated content in the bucket.

## Mountain Duck

Mountain Duck builds upon Cyberduck to allow you to mount cloud object storage as a disk in Finder on Mac or Explorer on Windows.  Trial versions are available but a registration key is required for continued use.

Creating a bookmark in Mountain Duck is very similar to creating connections in Cyberduck:

1. Download, install, and start Mountain Duck
2. Create a New Bookmark
3. From the drop-down menu select `S3 (HTTPS)` and enter the following information:
    * `Server`: enter endpoint of IBM COS 
        * *Ensure the endpoint region matches the intended bucket*
    * `Username`: enter the Access Key
    * Click **Connect**
    * You will be prompted for your `Secret Key` which will then be saved in the keychain