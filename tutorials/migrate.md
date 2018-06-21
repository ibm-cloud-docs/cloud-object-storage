---

copyright:
  years: 2018
lastupdated: "2018-06-13"

---

# Migrating data from OpenStack Swift

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

Before {{site.data.keyword.cos_full_notm}} became available as an {{site.data.keyword.cloud_notm}} Platform service, projects that required an object store used [OpenStack Swift](/docs/services/ObjectStorage/index.html) or [OpenStack Swift (infrastructure)](/docs/infrastructure/objectstorage-swift/index.html#getting-started-with-object-storage-openstack-swift). We recommend developers update their applications and migrate their data to {{site.data.keyword.cloud_notm}} to take advantage of the new access control and encryption benefits provided by IAM and Key Protect, as well as new features as they become available.

The concept of a Swift 'container' is identical to a COS 'bucket'.  COS limits service instances to 100 buckets and some Swift instances may have a larger number of containers. COS buckets can hold billions of objects and supports forward slashes (`/`) in object names for directory-like 'prefixes' when organizing data.  COS supports IAM policies at the bucket and service instance levels.
{:tip}

One approach to migrating data across object storage services is to use a 'sync' or 'clone' tool, such as [the open source `rclone` command line utility](https://rclone.org/docs).  This utility will sync a filetree between two locations, including cloud storage.  When `rclone` writes data to COS it will use the COS/S3 API to segment large objects and upload the parts in parallel according to sizes and thresholds set as configuration parameters.  

There are some differences between COS and Swift that must be considered as part of data migration.

  - COS does not yet support expiration policies or versioning.  Workflows that depend on these Swift features must instead handle them as part of their application logic upon migration into COS.
  - COS supports object-level metadata, but this information is not preserved when using `rclone` to migrate data.  Custom metadata can be set on objects in COS using a `x-amz-meta-{key}: {value}` header, but it is recommended that object-level metadata is backed up to a database prior to using `rclone`.  Custom metadata can be applied to existing objects by [copying the object onto itself](https://console.bluemix.net/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object).  Note that `rclone` **can** preserve timestamps.
  - COS uses IAM policies for service instance and bucket-level access control.  [Objects can be made publicly available](/docs/services/cloud-object-storage/iam/public-access.html)) by setting a `public-read` ACL, which eliminates the need for an authorization header.
  - [Multipart uploads](/docs/services/cloud-object-storage/basics/multipart.html) for large objects are handled differently in the COS/S3 API relative to the Swift API. 
  - COS allows for familiar optional HTTP headers such as `Cache-Control`, `Content-Encoding`, `Content-MD5`, and `Content-Type`.  

This guide provides instructions for migrating data from a single Swift container to a single COS bucket. This will need to be repeated for all containers that you want to migrate, and then your application logic will need to be updated to use the new API.  After the data is migrated you can verify the integrity of the transfer using `rclone check`, which will compare MD5 checksums and produce a list of any objects where they don't match.


## Set up {{site.data.keyword.cos_full_notm}}

  1. If you haven't created one yet, provision an instance of {{site.data.keyword.cos_full_notm}} from the [catalog](https://console.bluemix.net/catalog/services/cloud-object-storage).  
  2. Read through the [getting started guide](/docs/services/cloud-object-storage/getting-started.html) to familiarize yourself with key concepts such as [endpoints](/docs/services/cloud-object-storage/basics/endpoints.html) and [storage classes](/docs/services/cloud-object-storage/basics/classes.html).  Create any buckets that you will need to store your transferred data.
  3. Re-write your application to use the COS SDKs ([Java](/docs/services/cloud-object-storage/libraries/java.html), [Python](/docs/services/cloud-object-storage/libraries/python.html), [Node.js](/docs/services/cloud-object-storage/libraries/node.html)) or the [REST API](/docs/services/cloud-object-storage/api-reference/about-api.html).

## Set up a compute resource to run the migration tool
  1. Choose a Linux/macOS/BSD machine or an IBM Cloud Infrastructure Bare Metal or Virtual Server
     with the best proximity to your data.
  2. If you are running the migration on an IBM Cloud Infrastructure Bare Metal or Virtual Server
     use the **private** Swift and COS endpoints.
  3. Otherwise use the **public** Swift and COS endpoints.  
  4. Install `rclone` from [either a package manager or precompiled binary](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Configure `rclone` for OpenStack Swift
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
    <br>a. Click on your Swift instance in the [IBM Cloud console dashboard](https://console.bluemix.net/).
    <br>b. Click on **Service Credentials** in the navigation panel.
    <br>c. Click on **New credential** to generate credential information.  Click **Add**.
    <br>d. View the credential you created, and copy the JSON contents.

  4. Fill in the following fields:

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. Skip to section Configure `rclone` for COS


## Configure `rclone` for OpenStack Swift (infrastructure)
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
    <br>a. Click on your Swift account in the <a href="https://control.softlayer.com/storage/objectstorage">IBM Cloud   infrastructure customer portal</a>.
    <br>b. Click on the data center of the migration source container.
    <br>c. Click on **View Credentials**.
    <br>d. Copy the following.
      <br>&nbsp;&nbsp;&nbsp;**Username**
      <br>&nbsp;&nbsp;&nbsp;**API Key**
      <br>&nbsp;&nbsp;&nbsp;**Authentication Endpoint** based on where you are running the migration tool

  4. Using the OpenStack Swift (infrastructure) credential, fill in the following fields:

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## Configure `rclone` for COS
### Get COS credential
  1. Click on your COS instance in the IBM Cloud console.
  2. Click on **Service Credentials** in the navigation panel.
  3. Click on **New credential** to generate credential information.
  4. Under **Inline Configuration Parameters** add `{"HMAC":true}`. Click **Add**.
  5. View the credential you created, and copy the JSON contents.

### Get COS endpoint
  1. Click on **Buckets** in the navigation panel.
  2. Click on the migration target bucket.
  3. Click on **Configuration** in the navigation panel.
  4. Scroll down to the **Endpoints** section and choose the endpoint based on where
     where you are running the migration tool.

  5. Create the COS target by copying the following and pasting into `rclone.conf`.  

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. Using the COS credential and endpoint, fill in the following fields:

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## Verify the migration source and target are properly configured
1. List the Swift container to verify `rclone` is properly configured.

    ```
    rclone lsd SWIFT:
    ```

2. List the COS bucket to verify `rclone` is properly configured.

    ```
    rclone lsd COS:
    ```

## Run `rclone`

1. Do a dry run (no data copied) of `rclone` to sync the objects in your source
   Swift container (e.g. `swift-test`) to target COS bucket (e.g. `cos-test`).

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

2. Check that the files you desire to migrate appear in the command output. If everything looks good, remove the `--dry-run` flag and add `-v` flag to copy the data

    ```
    rclone -v copy SWIFT:swift-test COS:cos-test
    ```

Migrating data using `rclone` copies but does not delete the source data.
{:tip}


3. Repeat for any other containers that require migration.
4. Once all your data is copied, and you have verified that your application can access the data in COS, then delete your Swift service instance.
