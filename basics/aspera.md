---

copyright:
  years: 2018
lastupdated: "2018-06-22"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Aspera high-speed transfer

Aspera high-speed transfer is only available for buckets created in [the `us-south` region](/docs/services/cloud-object-storage/basics/endpoints.html) at this time.
{:tip}

Aspera high-speed transfer overcomes the limitations of traditional FTP and HTTP transfers to improve data transfer performance under most conditions, especially on networks experiencing high latency and packet loss. Using Aspera high-speed transfer for browser-based uploads and downloads offers the following benefits:

- Faster transfer speeds
- Transfer large object uploads over 200MB.
- Upload entire folders of any type of data including multi-media files, disk images, or any other unstructured data.
- Customize transfer speeds and default preferences.
- Transfers take place in the background instead of in the active browser window.
- Transfers can be viewed, paused/resumed, or cancelled independently.

The plug-in can be [installed from the Aspera website](http://downloads.asperasoft.com/connect2/) directly. For help troubleshooting issues with the Aspera Connect plug-in, [see the documentation](http://downloads.asperasoft.com/en/documentation/8).

Once the plug-in is installed, you will have the option to set Aspera high-speed transfer as the default for any uploads to the target bucket that use the same browser. Select **Remember my browser preferences**. Options are also available in the bucket configuration page under **Transfer options**. These options allow you to choose between Standard and High-speed as the default transport for uploads and downloads.

## Using the console

Typically, using the web-based console is not the most common way to use {{site.data.keyword.cos_short}}. Objects are limited to 200MB in size and the file name and key will be identical. Multiple objects can be uploaded at the same time, and if the browser allows for multiple threads each object will be uploaded using multiple parts in parallel. Support for larger object sizes and improved performance (depending on network factors) is provided via Aspera high-speed transfer.

When uploading an object using the console, the dialog box will present the option of using Aspera high-speed transfer. Instead of the standard HTTP `PUT`, this option uploads the object using the [FASP protocol](http://asperasoft.com/technology/transport/fasp/) from [IBM Aspera](https://www.ibm.com/cloud/high-speed-data-transfer). Click the **Download plug-in** link and you'll be prompted to install the Aspera Connect plug-in for your web browser.


