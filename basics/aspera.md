---

copyright:
  years: 2017, 2022
lastupdated: "2022-01-19"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Using Aspera high-speed transfer
{: #aspera}

Aspera high-speed transfer overcomes the limitations of traditional FTP and HTTP transfers to improve data transfer performance under most conditions, especially in networks with high latency and packet loss.
{: shortdesc}
 
This feature is not currently supported in {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}. [Learn more.](/docs/cloud-object-storage?topic=cloud-object-storage-about-cos-satellite)
{: note}

Instead of the standard HTTP `PUT` operation, Aspera high-speed transfer uploads the object by using the [FASP protocol](https://www.ibm.com/products/aspera/technology){: external}. Using Aspera high-speed transfer for uploads and downloads offers the following benefits:

- Faster transfer speeds
- Transfer large object uploads over 200 MB in the console and 1 GB by using an SDK or library
- Upload entire folders of any type of data, such as multi-media files, disk images, and any other structured or unstructured data
- Customize transfer speeds and default preferences
- Transfers can be viewed, paused, resumed, or cancelled independently

Aspera high-speed transfer is available in the {{site.data.keyword.cloud_notm}} [console](#aspera-console) and can also be used programmatically by using an [SDK](#aspera-sdk). 

Aspera high-speed transfer is available in certain regions only. See [Integrated Services](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) for more details.
{:tip}

It isn't possible to use Aspera high-speed transfer if a targeted bucket has an [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) policy.
{:important}

## Using the console
{: #aspera-console}

If you add objects by using the console in a [supported region](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability), you are prompted with an option to install the Aspera Connect client. This browser plug-in provides Aspera high-speed transfer to upload files or folders.

### Install Aspera Connect
{: #aspera-install}

1. Select **Install Aspera Connect** client.
2. Follow the installation instructions for your operating system and browser.
3. Resume file or folder upload.

The Aspera Connect plug-in can also be installed from the [Aspera website](https://downloads.asperasoft.com/connect2/) directly. For help troubleshooting issues with the Aspera Connect plug-in, [see the documentation](https://downloads.asperasoft.com/en/documentation/8).

After the plug-in is installed, you have the option to set Aspera high-speed transfer as the default for any uploads to the target bucket that use the same browser. Select **Remember my browser preferences**. Options are also available in the bucket configuration page under **Transfer options**. These options allow you to choose between Standard and High speed as the default transport for uploads and downloads.

Typically, using the IBM Cloud Object Storage web-based console isn't the most common way to use {{site.data.keyword.cos_short}}. The Standard transfer option limits objects size to 200 MB and the file name and key will be the same. Support for larger object sizes and improved performance (depending on network factors) is provided by Aspera high-speed transfer.

An Aspera server runs one SSH server on a configurable TCP port (22 by default, but often customers use port 33001). The firewall on the server side must allow this one TCP port to reach the Aspera server. No servers are listening on UDP ports. When a transfer is initiated by an Aspera client, the client opens an SSH session to the SSH server on the designated TCP port and negotiates the UDP port over which the data will travel. By default, Aspera clients and servers are configured to use UDP port 33001. After the session initiation step, both the client and the server will send and receive UDP traffic on the negotiated port. To allow the UDP session to start, the firewall on the Aspera server side must allow port UDP 33001 to reach the Aspera server.WFor For more information, see [Firewall Considerations](https://www.ibm.com/support/pages/firewall-considerations).
{:important}

### Transfer status
{: #aspera-console-transfer-status}

**Active:** Once you initiate a transfer, the transfer status displays as active. While the transfer is active, you can pause, resume, or cancel an active transfer. 

**Completed:** Upon completion of your transfer, information about this and all transfers in this session display on the completed tab. You can clear this information. You will only see information about transfers that are completed in the current session.

**Preferences:** You can set the default for uploads and downloads to High speed.

Downloads that use Aspera high-speed transfer incur egress charges. For more information, see the [pricing page](https://cloud.ibm.com/objectstorage/create#pricing).
{:tip}

**Advanced Preferences:** You can set bandwidth for uploads and downloads.
