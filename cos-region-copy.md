---

copyright:
  years: 2022, 2024
lastupdated: "2024-06-06"

keywords: cos, object storage, copy

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Moving data between buckets
{: #region-copy}

At some point it will become necessary to move or backup your data to a different {{site.data.keyword.cos_full}} region. One approach to moving or replicating data across object storage regions is to use a 'sync' or 'clone' tool, such as [the open-source `rclone` command-line utility](https://rclone.org/docs/){: external}. This utility syncs a file tree between two locations, including cloud object storage. When `rclone` writes data to COS, it uses the COS/S3 API to segment large objects and uploads the parts in parallel according to sizes and thresholds set as configuration parameters.

This guide provides instructions for copying data from one {{site.data.keyword.cos_full_notm}} bucket to another {{site.data.keyword.cos_short}} bucket within the same region or to a second {{site.data.keyword.cos_short}} bucket in a different {{site.data.keyword.cos_short}} region. These steps need to be repeated for all the data that you want to copy from each bucket. After the data is migrated you can verify the integrity of the transfer by using `rclone check`, which will produce a list of any objects that don't match either file size or checksum. Additionally, you can keep buckets in sync by regularly running `rclone sync` from your available sources to your chosen destinations.

## Create a destination {{site.data.keyword.cos_full_notm}} bucket
{: #region-copy-setup-destination}

You have the option of using your existing instance of {{site.data.keyword.cos_full_notm}} or creating a new instance. If you want to reuse your existing instance, skip to step #2.

1. Create an instance of {{site.data.keyword.cos_full_notm}} from the [catalog](https://cloud.ibm.com/catalog/cloud-object-storage).
1. Create any buckets that you need to store your transferred data. Read through the [getting started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to familiarize yourself with key concepts such as [endpoints](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) and [storage classes](/docs/cloud-object-storage?topic=cloud-object-storage-classes).
 1. **The `rclone` utility will not copy any bucket configurations or object metadata**.  Therefore, if you are using any of the {{site.data.keyword.cos_short}} features such as expiration, archive, key protect, and so on. be sure to configure them appropriately before migrating your data. To view which features are supported at your COS destination, please refer to the [feature matrix](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-service-availability).

Feature configuration and access policies documentation can be viewed at the IBM Cloud portal pages listed below:

* [IBM Cloud Identity and Access Management - IAM](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-iam)
* [Activity Tracking Events](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-at)
* [Metrics Monitoring](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration)
* [Object Expiry](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-expiry)
* [Cloud Object Storage Firewall](https://cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall)
* [Content Delivery Network - CDN](https://cloud.ibm.com/docs/cis?topic=cis-resolve-override-cos)
* [Archive](/docs/cloud-object-storage?topic=cloud-object-storage-archive)
* [Key Protect](https://cloud.ibm.com/docs/key-protect?topic=key-protect-integrate-cos)
* [IBM Cloud Functions](https://cloud.ibm.com/docs/openwhisk?topic=openwhisk-pkg_obstorage)

## Set up a compute resource to run the migration tool
{: #region-copy-compute}

1. Choose a Linux&trade;/macOS&trade;/BSD&trade; machine or an IBM Cloud Infrastructure Bare Metal or Virtual Server with the best proximity to your data. Selecting a data center in the same region as the destination bucket is generally the best choice (for example, if moving data from `mel01` to `au-syd`, use a VM or Bare Metal in `au-syd`). The recommended Server configuration is: 32 GB RAM, 2-4 core processor, and private network speed of 1000 Mbps.
1. If you are running the migration on an IBM Cloud Infrastructure Bare Metal or Virtual Server use the **private** COS endpoints to avoid network egress charges.
1. Otherwise, use the **public** or **direct** COS endpoints.
1. Install `rclone` from [either a package manager or a pre-compiled binary](https://rclone.org/install/){: external}.

```sh
curl https://rclone.org/install.sh | sudo bash
```
{: pre}

## Configure `rclone` for COS source data
{: #region-copy-config-cos}

Create 'profiles' for your source and destination of the migration in `rclone`.

### If needed, obtain COS credentials
{: #region-copy-config-cos-credential}

1. Select your COS instance in the IBM Cloud console.
1. Click **Service Credentials** in the navigation pane.
1. Click **New credential** to generate credential information.
1. Select **Advanced** options.
1. Turn HMAC credentials to **On**.
1. Click **Add**.
1. View the credential that you created, and copy the JSON contents.

### Get COS endpoint
{: #region-copy-config-cos-endpoint}

1. Click **Buckets** in the navigation pane.
1. Click the migration destination bucket.
1. Click **Configuration** in the navigation pane.
1. Scroll down to the **Endpoints** section and choose the endpoint based on where you are running the migration tool.
1. Create the COS destination by copying the following and pasting into `rclone.conf`.

```sh
[COS_SOURCE]
type = s3
provider = IBMCOS
env_auth = false
access_key_id =
secret_access_key =
endpoint =
```
{: codeblock}

Use `[COS_DESTINATION]` as the name of the profile you need to create to configure the destination. Repeat the steps above,

Using your credentials and desired endpoint, complete the following fields:

```sh
access_key_id = <access_key_id>
secret_access_key = <secret_access_key>
endpoint = <bucket endpoint>
```
{: codeblock}

## Configure `rclone` for COS destination data
{: #region-copy-config-cos-destination}

Repeat the previous steps for the destination buckets.

## Verify that the source and destination are properly configured
{: #region-copy-verify}

* List the buckets associated with the source to verify `rclone` is properly configured.

```sh
rclone lsd COS_SOURCE:
```
{: pre}

* List the buckets associated with the destination to verify `rclone` is properly configured.

```sh
rclone lsd COS_DESTINATION:
```
{: pre}

If you are using the same COS instance for the source and destination, the bucket listings will match.
{: note}

## Run `rclone`
{: #region-copy-run}

1. Test your configuration with a dry run (where no data is copied) of `rclone` to test the copy of the objects in your source bucket (for example, `source-test`) to target bucket (for example, `destination-test`).

   ```sh
   rclone --dry-run copy COS_SOURCE:source-test COS_DESTINATION:destination-test
   ```

   {: pre}

1. Check that the files you want to migrate appear in the command output. If everything looks good, remove the `--dry-run` flag and, optionally add `-v` and/or `-P` flag to copy the data and track progress. Using the optional `--checksum` flag avoids updating any files that have the same MD5 hash and object size in both locations.

   ```sh
   rclone -v -P copy --checksum COS_SOURCE:source-test COS_DESTINATION:destination-test
   ```

   {: pre}

Try to max out the CPU, memory, and network on the machine running `rclone` to get the fastest transfer time.

There are other parameters to consider when tuning `rclone`. Different combinations of these values will impact CPU, memory, and transfer times for the objects in your bucket.

| Flag | Type | Description |
| --- | --- | --- |
| `--checkers` | `int` | Number of checkers to run in parallel (default 8). This is the number of checksums compare threads running. We recommend increasing this to 64 or more. |
| `--transfers` | `int` | This is the number of objects to transfer in parallel (default 4). We recommend increasing this to 64 or 128 or higher when transferring many small files. |
| `--multi-thread-streams` | `int` | Download large files (> 250M) in multiple parts in parallel. This will improve the download time of large files (default 4). |
| `--s3-upload-concurrency` | `int` | The number of parts of large files (> 200M) to upload in parallel. This will improve the upload time of large files (default 4). |
{: caption="`rclone` options" caption-side="top"}

Migrating data using `rclone copy` only copies but does not delete the source data.
{: tip}

The copy process should be repeated for all other source buckets that require migration/copy/backup.
