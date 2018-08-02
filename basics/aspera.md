---

copyright:
  years: 2018
lastupdated: "07-16-2018"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Aspera high-speed transfer

Aspera high-speed transfer overcomes the limitations of traditional FTP and HTTP transfers to improve data transfer performance under most conditions, especially on networks experiencing high latency and packet loss. Using Aspera high-speed transfer for browser-based uploads and downloads offers the following benefits:

- Faster transfer speeds
- Transfer large object uploads over 200MB.
- Upload entire folders of any type of data including multi-media files, disk images, or any other unstructured data.
- Customize transfer speeds and default preferences.
- Transfers take place in the background instead of in the active browser window.
- Transfers can be viewed, paused/resumed, or cancelled independently.

Aspera high-speed is available only in regions listed in [Integrated services](/docs/services/cloud-object-storage/basics/services.html#service-availability).
{:tip}

## Installing the Aspera high-speed plug-in

When you create a bucket in a [supported region](/docs/services/cloud-object-storage/basics/services.html#service-availability), you have the option to select Aspera high-speed transfer to upload files or folders. Once you attempt to upload an object, you are prompted to install the Aspera Connect client.

### Install Aspera Connect

1. Select **Install Aspera Connect** client.
2. Follow the install instructions depending on your operating system and browser.
3. Resume file or folder upload.

The Aspera Connect plug-in can also be installed from the [Aspera website](http://downloads.asperasoft.com/connect2/) directly. For help troubleshooting issues with the Aspera Connect plug-in, [see the documentation](http://downloads.asperasoft.com/en/documentation/8).

Once the plug-in is installed, you have the option to set Aspera high-speed transfer as the default for any uploads to the target bucket that use the same browser. Select **Remember my browser preferences**. Options are also available in the bucket configuration page under **Transfer options**. These options allow you to choose between Standard and High-speed as the default transport for uploads and downloads.

## Using the console

Typically, using the web-based console is not the most common way to use {{site.data.keyword.cos_short}}. The Standard transfer option limits objects size to 200MB and the file name and key will be identical.  Support for larger object sizes and improved performance (depending on network factors) is provided by Aspera high-speed transfer.

Instead of the standard HTTP `PUT`, Aspera high-speed transfer uploads the object using the [FASP protocol](http://asperasoft.com/technology/transport/fasp/) from [Aspera high-speed transfer](https://www.ibm.com/cloud/high-speed-data-transfer). 
### Transfer status

**Active:** Once you initiate a transfer, the transfer status displays as active. You can pause, resume or cancel an active transfer. 

**Completed:** Upon completion of your transfer, information about this and all transfers in this session display on the Completed tab. You can clear this information. You will only see information about transfers completed in the current session.

**Preferences:** You can set the default for uploads and/or downloads to High-speed.

Downloads using Aspera high-speed will incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage).
{:tip}

**Advanced Preferences:** You can set bandwidth for uploads and downloads.
