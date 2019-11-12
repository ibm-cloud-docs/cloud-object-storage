---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-11-11"

keywords: migrate, openstack swift, object storage

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
{:http: .ph data-hd-programlang='http'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}

# Migrating data from OpenStack Swift
{: #migrate}

We recommend developers update their applications and migrate their data to {{site.data.keyword.cos_full}} to take advantage of the new access control and encryption benefits that are provided by IAM and Key Protect, as well as new features as they become available.
{: .shortdesc}

Before {{site.data.keyword.cloud_notm}} became available as an {{site.data.keyword.cloud_notm}} Platform service, projects that required an object store used [OpenStack Swift](https://docs.openstack.org/swift/latest/) or [OpenStack Swift (infrastructure)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift). 

The concept of a Swift 'container' is the same as a COS 'bucket'. COS limits service instances to 100 buckets and some Swift instances might have a larger number of containers. COS buckets can hold billions of objects and supports forward slashes (`/`) in object names for directory-like 'prefixes' to organize data. COS supports IAM policies at the bucket and service instance levels.
{:tip}

One approach to migrating data across object storage services is to use a 'sync' or 'clone' tool, such as [the open source `rclone` command-line utility](https://rclone.org/docs/). This utility syncs a file tree between two locations, including cloud storage. When `rclone` writes data to COS, it uses the COS/S3 API to segment large objects and upload the parts in parallel according to sizes and thresholds set as configuration parameters.

Differences exist between COS and Swift that must be considered as part of data migration.

  - COS doesn't yet support versioning. Workflows that depend on versioning must instead handle them as part of their application logic upon migration into COS.
  - COS supports object-level metadata, but this information isn't preserved by `rclone` during migration. Custom metadata can be set on objects in COS by setting an `x-amz-meta-{key}: {value}` header, but we suggest that object-level metadata is backed up to a database before using `rclone`. Custom metadata can be applied to existing objects by [copying the object onto itself](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object) - the system recognizes overwrites of the same object data and updates metadata only. You **can** set `rclone` to preserve time stamps.
  - COS uses IAM policies for service instance and bucket-level access control. [Objects can be made publicly available](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access) by setting a `public-read` ACL, which eliminates the need for an authorization header.
  - [Multipart uploads](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) for large objects are handled differently in the COS/S3 API relative to the Swift API.
  - COS allows for familiar optional HTTP headers such as `Cache-Control`, `Content-Encoding`, `Content-MD5`, and `Content-Type`.

This guide provides instructions for migrating data from a single Swift container to a single COS bucket. These steps need to be repeated for all containers that you want to migrate, and then your application logic will need to be updated to use the new API. After the data is migrated you can verify the integrity of the transfer by using `rclone check`, which will compare MD5 checksums and produce a list of any objects where they don't match.


## Set up {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. Create an instance of {{site.data.keyword.cos_full_notm}} from the [catalog](https://cloud.ibm.com/catalog/services/cloud-object-storage).
  2. Create any buckets that you need to store your transferred data. Read through the [getting started guide](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) to familiarize yourself with key concepts such as [endpoints](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) and [storage classes](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. Because the syntax of the Swift API is significantly different from the COS/S3 API, it might be necessary to refactor your application. Libraries are available in ([Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)) or the [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Set up a compute resource to run the migration tool
{: #migrate-compute}
  1. Choose a Linux/macOS/BSD machine or an IBM Cloud Infrastructure Bare Metal or Virtual Server
     with the best proximity to your data.
     The following Server configuration is recommended:  32 GB RAM, 2-4 core processor, and private network speed of 1000 Mbps.  
  2. If you are running the migration on an IBM Cloud Infrastructure Bare Metal or Virtual Server
     use the **private** Swift and COS endpoints.
  3. Otherwise, use the **public** Swift and COS endpoints.
  4. Install `rclone` from [either a package manager or precompiled binary](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Configure `rclone` for OpenStack Swift
{: #migrate-rclone}

  1. Create an `rclone` config file in `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Create the Swift source by copying the following and pasting into `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. Get OpenStack Swift credential
    <br>a. Select your Swift instance in the [IBM Cloud console dashboard](https://cloud.ibm.com/).
    <br>b. Click **Service Credentials** in the navigation pane.
    <br>c. Click **New credential** to generate credential information. Click **Add**.
    <br>d. View the credential that you created, and copy the JSON contents.

  4. Complete the following fields:

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. Skip to section Configure `rclone` for COS


## Configure `rclone` for OpenStack Swift (infrastructure)
{: #migrate-config-swift}

  1. Create an `rclone` config file in `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Create the Swift source by copying the following and pasting into `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. Get OpenStack Swift (infrastructure) credential
    <br>a. Select your Swift account in the IBM Cloud infrastructure customer portal.
    <br>b. Click the data center of the migration source container.
    <br>c. Click **View Credentials**.
    <br>d. Copy the necessary information.
      <br>&nbsp;&nbsp;&nbsp;**User name**
      <br>&nbsp;&nbsp;&nbsp;**API Key**
      <br>&nbsp;&nbsp;&nbsp;**Authentication Endpoint** based on where you are running the migration tool

  4. Using the OpenStack Swift (infrastructure) credential, complete the following fields:

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## Configure `rclone` for COS
{: #migrate-config-cos}

### Get COS credential
{: #migrate-config-cos-credential}

  1. Select your COS instance in the IBM Cloud console.
  2. Click **Service Credentials** in the navigation pane.
  3. Click **New credential** to generate credential information.
  4. In **Inline Configuration Parameters** add `{"HMAC":true}`. Click **Add**.
  5. View the credential that you created, and copy the JSON contents.

### Get COS endpoint
{: #migrate-config-cos-endpoint}

  1. Click **Buckets** in the navigation pane.
  2. Click the migration target bucket.
  3. Click **Configuration** in the navigation pane.
  4. Scroll down to the **Endpoints** section and choose the endpoint based on where you are running the migration tool.

  5. Create the COS target by copying the following and pasting into `rclone.conf`.

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. Using the COS credential and endpoint, complete the following fields:

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## Verify that the migration source and target are properly configured
{: #migrate-verify}

1. List the Swift container to verify `rclone` is properly configured.

    ```
    rclone lsd SWIFT:
    ```

2. List the COS bucket to verify `rclone` is properly configured.

    ```
    rclone lsd COS:
    ```

## Run `rclone`
{: #migrate-run}

1. Do a dry run (no data copied) of `rclone` to sync the objects in your source
   Swift container (for example, `swift-test`) to target COS bucket (for example, `cos-test`).

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. Check that the files you want to migrate appear in the command output. If everything looks good, remove the `--dry-run` flag and add `-v` flag to copy the data. Using the optional `--checksum` flag avoids updating any files that have the same MD5 hash and object size in both locations.

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   Try to max out the CPU, memory, and network on the machine running `rclone` to get the fastest transfer time.
   A few other parameters to consider for tuning `rclone`:

Flag | Type | Description
--- | --- | ---
`--checkers` | `int` | Number of checkers to run in parallel (default 8). This is the number of checksums compare threads running. We recommend increasing this to 64 or more.
`--transfers` | `int` | Number of file transfers to run in parallel (default 4). This is the number of objects to transfer in parallel. We recommend increasing this to 64 or 128 or higher.
`--fast-list` | None | Use recursive list if available. Uses more memory but fewer transactions. Use this option to improve performance, as it reduces the number of requests that are needed to copy an object.

Migrating data using `rclone` copies but does not delete the source data.
{:tip}


3. Repeat for any other containers that require migration.
4. Once all your data is copied, and you have verified that your application can access the data in COS, then delete your Swift service instance.
