---

copyright:
  years: 2020
lastupdated: "2020-04-07"

keywords: migrate, cos, object storage, rclone, copy, sync

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Moving data between buckets
{: #region-copy}
At times it may become necessary to move or backup your data to a different {{site.data.keyword.cos_full}} region. One approach to moving or replicating data across object storage regions is to use a 'sync' or 'clone' tool, such as [the open-source `rclone` command-line utility](https://rclone.org/docs/). This utility syncs a file tree between two locations, including cloud storage. When `rclone` writes data to COS, it uses the COS/S3 API to segment large objects and uploads the parts in parallel according to sizes and thresholds set as configuration parameters.

This guide provides instructions for moving or copying data from one COS bucket to another COS bucket in the same or in a different region. These steps need to be repeated for all buckets that you want to copy. After the data is migrated you can verify the integrity of the transfer by using `rclone check`, which will compare MD5 checksums and produce a list of any objects where they don't match. Additionally, you can keep buckets in sync by running `rclone sync` regularly.

## Create an additional {{site.data.keyword.cos_full_notm}} bucket
{: #migrate-setup}

You have the option of using your existing instance of {{site.data.keyword.cos_full_notm}} or creating a new instance. If you want to reuse your existing instance, skip to step #2.

  1. Create an instance of {{site.data.keyword.cos_full_notm}} from the [catalog](https://cloud.ibm.com/catalog/services/cloud-object-storage).
  2. Create any buckets that you need to store your transferred data. Read through the [getting started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started) to familiarize yourself with key concepts such as [endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) and [storage classes](/docs/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. If you are using any of the COS features such as expiration, archive, key protect, etc. be sure to configure appropriately.


## Set up a compute resource to run the migration tool
{: #migrate-compute}
  1. Choose a Linux/macOS/BSD machine or an IBM Cloud Infrastructure Bare Metal or Virtual Server with the best proximity to your data. The following Server configuration is recommended:  32 GB RAM, 2-4 core processor, and private network speed of 1000 Mbps.  
  2. If you are running the migration on an IBM Cloud Infrastructure Bare Metal or Virtual Server use the **private** COS endpoints to avoid network egress charges.
  3. Otherwise, use the **public** COS endpoints.
  4. Install `rclone` from [either a package manager or precompiled binary](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
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
        2. Click the migration destination bucket.
        3. Click **Configuration** in the navigation pane.
        4. Scroll down to the **Endpoints** section and choose the endpoint based on where you are running the migration tool.

        5. Create the COS destination by copying the following and pasting into `rclone.conf`.

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
## Configure `rclone` for COS
{: #migrate-config-cos}
Repeat the following steps for the source and destination buckets.

### Get COS credential
{: #migrate-config-cos-credential}
If you do not already have a key and secret key, do the following:

  1. Select your COS instance in the IBM Cloud console.
  2. Click **Service Credentials** in the navigation pane.
  3. Click **New credential** to generate credential information.
  4. In **Inline Configuration Parameters** add `{"HMAC":true}`. Click **Add**.
  5. View the credential that you created, and copy the JSON contents.

### Get COS endpoint
{: #migrate-config-cos-endpoint}

  1. Click **Buckets** in the navigation pane.
  2. Click the migration destination bucket.
  3. Click **Configuration** in the navigation pane.
  4. Scroll down to the **Endpoints** section and choose the endpoint based on where you are running the migration tool.

  5. Create the COS destination by copying the following and pasting into `rclone.conf`.
      Replace COS_SoD with COS_SOURCE or COS_DESTINATION as appropriate.
    ```
    [COS_SoD]
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

## Verify that the source and destination are properly configured
    {: #migrate-verify}

    1. List the buckets associated with the source to verify `rclone` is properly configured.

        ```
        rclone lsd COS_SOURCE:
        ```

    2. List the buckets associated with the destination to verify `rclone` is properly configured.

        ```
        rclone lsd COS_DESTINATION:
        ```
Note: If you are using the same COS instance for the source and destination, the bucket listings will match.


## Run `rclone`
{: #migrate-run}

1. Do a dry run (no data copied) of `rclone` to sync the objects in your source
    bucket (for example, `source-test`) to target bucket (for example, `destination-test`).

    ```
    rclone --dry-run copy COS_SOURCE:source-test COS_DESTINATION:destination-test
    ```

2. Check that the files you want to migrate appear in the command output. If everything looks good, remove the `--dry-run` flag and add `-v` flag to copy the data. Using the optional `--checksum` flag avoids updating any files that have the same MD5 hash and object size in both locations.

    ```
    rclone -v copy --checksum COS_SOURCE:source-test COS_DESTINATION:destination-test
    ```

   Try to max out the CPU, memory, and network on the machine running `rclone` to get the fastest transfer time.
   A few other parameters to consider for tuning `rclone`:

Flag | Type | Description
--- | --- | ---
`--checkers` | `int` | Number of checkers to run in parallel (default 8). This is the number of checksums compare threads running. We recommend increasing this to 64 or more.
`--transfers` | `int` | This is the number of objects to transfer in parallel (default 4). We recommend increasing this to 64 or 128 or higher when transferring a large number of small files.
`--multi-thread-streams` | `int` | Download large files (> 250M) in multiple parts in parallel. This will improve the download time of large files (default 4).
`--s3-upload-concurrency` | `int` | The number of parts of large files (> 200M) to upload in parallel. This will improve the upload time of large files (default 4).

Different combinations of these values will impact CPU, memory, and transfer times for the objects in your bucket.


Migrating data using `rclone copy` copies but does not delete the source data.
{:tip}


3. Repeat for any other buckets that require migration/copy/backup.
