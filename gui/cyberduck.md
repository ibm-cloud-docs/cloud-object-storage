---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-23"

keywords: gui, desktop, cyberduck

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}

# Transfer files with Cyberduck
{: #cyberduck}

Cyberduck is a popular, open source, and easy to use cloud Object Storage browser.
{: shortdesc}

Cyberduck can calculated the authorization signatures that are needed to connect to IBM COS. Cyberduck can be downloaded from [cyberduck.io/](https://cyberduck.io/){: new_window}.

To use Cyberduck to create a connection to IBM COS and synchronize a folder of local files to a bucket, follow these steps:

 1. Download, install, and start Cyberduck.
 2. The main window of the application opens, where you can create a connection to IBM COS. Click **Open Connection** to configure a connection to IBM COS.
 3. A pop-up window opens. From the menu, select `S3 (HTTPS)`. Enter information into the following fields, and then click Connect:

    * `Server`: enter endpoint of IBM COS
        * *Ensure the endpoint's region matches the intended bucket. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Access Key ID`
    * `Secret Access Key`
    * `Add to Keychain`: Save the connection to the keychain to allow use across other applications *(optional)*

 4. Cyberduck takes you to the root of the account where buckets can be created.
    * Right-click within the main pane and select **New Folder** (*the application deals with many transfer protocols where Folder is the more common container construct*).
    * Enter the bucket name and then click Create.
 5. After the bucket is created, double-click the bucket to view it. Within the bucket you can perform various functions such as:
    * Upload files to the bucket
    * List bucket contents
    * Download objects from the bucket
    * Synchronize local files to a bucket
    * Synchronize objects to another bucket
    * Create an archive of a bucket
 6. Right-click within the bucket and select **Synchronize**. A pop-up window opens where you can browse to the folder that you want to synchronize to the bucket. Select the folder and click Choose.
 7. After you select the folder, a new pop-up window opens. Here, a drop-down menu is available where you select the synchronization operation with the bucket. Three possible synchronize options are available from the menu:

    * `Download`: This downloads changed and missing objects from the bucket.
    * `Upload`: This uploads changed and missing files to the bucket.
    * `Mirror`: This performs both download and upload operations, ensuring that all new and updated files and objects are synchronized between the local folder and the bucket.

 8. Another window opens to show active and historical transfer requests. After the synchronization request is complete, the main window will perform a list operation on the bucket to show the updated content in the bucket.

## Mountain Duck
{: #mountain-duck}

Mountain Duck builds upon Cyberduck to allow you to mount cloud Object Storage as a disk in Finder on Mac or Explorer on Windows. Trial versions are available but a registration key is required for continued use.

Creating a bookmark in Mountain Duck is similar to creating connections in Cyberduck:

1. Download, install, and start Mountain Duck
2. Create a New Bookmark
3. From the drop-down menu select `S3 (HTTPS)` and enter the following information:
    * `Server`: enter endpoint of IBM COS 
        * *Ensure that the endpoint region matches the intended bucket. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Username`: enter the Access Key
    * Click **Connect**
    * You're prompted for your Secret Key, which will then be saved in the keychain.

Your buckets will now be available in Finder or Explorer. You can interact with {{site.data.keyword.cos_short}} like any other mounted file system.

## CLI
{: #cyberduck-cli}

Cyberduck also provides `duck`, a command-line interface (CLI) that runs in shell on Linux, Mac OS X, and Windows. Installation instructions are available on the `duck` [wiki page](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window}.

In order to use `duck` with {{site.data.keyword.cos_full}}, a custom profile needs to be added to the [Application Support Directory](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}. Detailed information about `duck` connection profiles is available on the [CLI help](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}.

Here is an example profile for a regional COS endpoint:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Adding this profile to `duck` allows you to access {{site.data.keyword.cos_short}} using a command similar to below:

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*Key Values*
* `<bucket-name>` - name of the COS bucket (*ensure that bucket and endpoint regions are consistent*)
* `<access-key>` - HMAC access key
* `<secret-access-key>` - HMAC secret key

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

A full list of command-line options is available by entering `duck --help` in the shell is available in the [wiki site](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}
