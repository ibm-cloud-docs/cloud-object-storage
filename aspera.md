---

copyright:
  years: 2017, 2023
lastupdated: "2023-08-09"

keywords: aspera, high speed, big data, packet loss

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

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
{: tip}

It isn't possible to use Aspera high-speed transfer if a targeted bucket has an [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable) policy.
{: important}

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

An Aspera server runs one SSH server on a configurable TCP port (33001 by default). The firewall on the server side must allow this one TCP port to reach the Aspera server. No servers are listening on UDP ports. When a transfer is initiated by an Aspera client, the client opens an SSH session to the SSH server on the designated TCP port and negotiates the UDP port over which the data will travel. By default, Aspera clients and servers are configured to use UDP port 33001. After the session initiation step, both the client and the server will send and receive UDP traffic on the negotiated port. To allow the UDP session to start, the firewall on the Aspera server side must allow port UDP 33001 to reach the Aspera server. For more information, see [Firewall Considerations](https://www.ibm.com/support/pages/firewall-considerations).
{: important}

### Transfer status
{: #aspera-console-transfer-status}

**Active:** Once you initiate a transfer, the transfer status displays as active. While the transfer is active, you can pause, resume, or cancel an active transfer.

**Completed:** Upon completion of your transfer, information about this and all transfers in this session display on the completed tab. You can clear this information. You will only see information about transfers that are completed in the current session.

**Preferences:** You can set the default for uploads and downloads to High speed.

Downloads that use Aspera high-speed transfer incur egress charges. For more information, see the [pricing page](https://cloud.ibm.com/objectstorage/create#pricing).
{: tip}

**Advanced Preferences:** You can set bandwidth for uploads and downloads.

## Using the Aspera Transfer SDK
{: #aspera-transfer-sdk}

1. [Download the Aspera Transfer SDK from the IBM API Hub.](https://developer.ibm.com/apis/catalog/aspera--aspera-transfer-sdk/Introduction) The SDK is a collection of binaries (command line utilities and a daemon to listen for transfer requests), configuration files, and language specific connectors.
2. Install the grpc dependencies from the appropriate package manager (pip, maven, gem, etc).
3. Launch the daemon and import the relevant programming language connector files to your project.
4. Instantiate an Aspera client by passing it the local port used by the daemon.
5. Create a `transfer_spec` containing all the information needed for the transfer:
   1. `icos` information:
      1. API key
      2. Service instance ID
      3. Target endpoint
      4. Bucket name
      5. Transfer direction
      6. Remote host (you find this by sending a GET request to a bucket with a `?faspConnectionInfo` query parameter)
      7. Assets for transfer (basically a set of file paths)
6. Pass the transfer specification and configuration info to a transfer request.

The following is an example using Python:

```py
import random
import string

import grpc
import json
import os.path

from urllib3.connectionpool import xrange

import transfer_pb2 as transfer_manager
import transfer_pb2_grpc as transfer_manager_grpc


def run():
    # create a connection to the transfer manager daemon
    client = transfer_manager_grpc.TransferServiceStub(
        grpc.insecure_channel('localhost:55002'))

    # create file
    file_path = generate_source_file()

    # create transfer spec
    transfer_spec = {
        "session_initiation": {
            "icos": {
                "api_key": os.environ.get('IBMCLOUD_API_KEY'),
                "bucket": os.environ.get('IBMCLOUD_BUCKET'),
                "ibm_service_instance_id": os.environ.get('IBMCLOUD_COS_INSTANCE'),
                "ibm_service_endpoint": os.environ.get('IBMCLOUD_COS_ENDPOINT')
            }
        },
        "direction": "send",
        "remote_host": "https://ats-sl-dal.aspera.io:443",
        "title": "strategic",
        "assets": {
            "destination_root": "/aspera/file",
            "paths": [
                {
                    "source": file_path
                }
            ]
        }
    }
    transfer_spec = json.dumps(transfer_spec)

    # create a transfer request
    transfer_request = transfer_manager.TransferRequest(
        transferType=transfer_manager.FILE_REGULAR,
        config=transfer_manager.TransferConfig(),
        transferSpec=transfer_spec)

    # send start transfer request to transfer manager daemon
    transfer_response = client.StartTransfer(transfer_request)
    transfer_id = transfer_response.transferId
    print("transfer started with id {0}".format(transfer_id))

    # monitor transfer status
    for transfer_info in client.MonitorTransfers(
            transfer_manager.RegistrationRequest(
                filters=[transfer_manager.RegistrationFilter(
                    transferId=[transfer_id]
                )])):
        print("transfer info {0}".format(transfer_info))

        # check transfer status in response, and exit if it's done
        status = transfer_info.status
        if status == transfer_manager.FAILED or status == transfer_manager.COMPLETED:
            print("finished {0}".format(status))
            break


def generate_source_file(name='file'):
    with open(name, 'w') as file:
        # file.write('Hello World!')
        file.write(''.join(random.choice(string.ascii_lowercase) for i in xrange(10 ** 10)))
    return os.path.abspath(name)


if __name__ == '__main__':
    run()
```
