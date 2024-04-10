---

copyright:
  years: 2020, 2024
lastupdated: "2024-04-10"

keywords: migrate, aws

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Migrating from AWS
{: #migrate}

There are many tools to assist you to successfully migrate your information from AWS to {{site.data.keyword.cos_full}}, with more secure and globally accessible results.
{: shortdesc}

## Before you begin
{: #migrate-preparation}

Determine your goals and process for your migration before starting your migration. You may also consider training and partnerships to be beneficial. Your planning and assessment stage will consider many possibilities, including security and technical capabilities.

Documentation for any project will help keep you keep track of your resources as well as your goals. After assessing your existing projects, you may benefit by updating them to use {{site.data.keyword.cos_full_notm}} libraries like those for ([Java](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/cloud-object-storage/libraries?topic=cloud-object-storage-node)). If you're interested in programmer interfaces, the [REST API](/docs/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) will provide an in-depth look at operations and configurations.

Refer to the [getting started guide](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to familiarize yourself with key concepts such as [endpoints](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) and [storage classes](/docs/cloud-object-storage/basics?topic=cloud-object-storage-classes).

## Provision and configure {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

1. If you haven't already, create an instance of {{site.data.keyword.cos_full_notm}} from the [Console](https://cloud.ibm.com/catalog/services/cloud-object-storage){: external}.
1. Create any buckets that you anticipate will be needed to store your transferred data.
1. While {{site.data.keyword.cos_short}} is compatible with the S3 API, it may be necessary to create new [Service credentials](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials), or bring your own keys for your projects. In this guide, we will use [HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) similar to the format of AWS credentials.
1. Managing [encryption](/docs/cloud-object-storage?topic=cloud-object-storage-encryption) provides insights into security. Refer to product documentation on [{{site.data.keyword.keymanagementservicefull}}](/docs/key-protect?topic=key-protect-about) and [{{site.data.keyword.hscrypto}}](/docs/hs-crypto?topic=hs-crypto-overview) for more information.

## Determine your solution
{: #migrate-options}

It is true that a massively complex [migration](https://www.ibm.com/cloud/mass-data-migration){: external} requires a complete service to plan and implement migrating your data to {{site.data.keyword.cos_full_notm}}. But whatever the size of your data, your goals and timetable take precedence. Once you have provisioned and set your target, it is time to choose a process to achieve your goals on your time.

There are many ways to achieve the goal of migrating your AWS data. Integrated solutions provide comprehensive guides to migration, as shown in the [{{site.data.keyword.icp4i_full_notm}}](https://www.ibm.com/cloud/cloud-pak-for-integration/high-speed-data-transfer){: external}. In addition to full-featured migration services, you may also want to investigate third party migration tools as part of your investigation. But don't forget that there are many CLI and GUI tools readily available for use as part of your migration.

* [COS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-ic-cos-cli) can be used for many operations. For example, you may wish to use the CLI to configure your {{site.data.keyword.cos_full_notm}} instances, and to create and configure buckets.
* [AWS CLI](/docs/cloud-object-storage?topic=cloud-object-storage-aws-cli) can be used to list your current bucket's contents to prepare for migrating from AWS, among other operations:

```bash
aws s3 ls --recursive s3://<BUCKET_NAME> --summarize > bucket-contents-source.txt
```

* [`rclone`](/docs/cloud-object-storage?topic=cloud-object-storage-rclone) has many uses, and we'll look at it specifically, next.

### Migrate your data
{: #migrate-data-strategy}

Based on the process and tools you've chosen, choose a strategy for migrating your data. Here is a simplified process using the command line and the Go-based `rclone` executable as an example.

1. Install `rclone` from [either a package manager or precompiled binary](https://rclone.org/install/){: external}. There are more configuration options available with explanations at the {{site.data.keyword.cos_full_notm}} [documentation](/docs/cloud-object-storage?topic=cloud-object-storage-rclone).

   ```bash
   curl https://rclone.org/install.sh | sudo bash
   ```

   {: codeblock}

#### Configure `rclone` with your AWS credentials
{: #migrate-aws-cred-config}

Start by creating 'profiles' for your source and destination of the migration in `rclone`. A profile contains the configuration and credentials needed for working with your date. To migrate from AWS, those credentials are needed to continue. Also, create a profile for your destination credentials specifically for {{site.data.keyword.cos_full_notm}}.

1. There are many options to configuring `rclone` and following the `rclone config` wizard is one way you can create profiles. You can create an `rclone` config file in `~/.rclone.conf` by using the command as shown. Please use the root path of your home directory if the path shown isn't available.

   ```bash
   touch ~/.config/rclone/rclone.conf
   ```

   {: codeblock}

1. Create the AWS configuration settings by copying the following and pasting into `rclone.conf` using an appropriate editor.

   ```bash
   [AWS]
   type = s3
   provider = AWS
   env_auth = false
   access_key_id =
   secret_access_key =
   region =
   ```

   {: codeblock}

1. Paste your AWS `access_key_id` and `secret_access_key` as obtained per instructions [here](https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html){: external} into the appropriate fields of your configuration as shown.

#### Configure `rclone` with your COS credentials
{: #migrate-cos-credential-config}

To complement the credentials of the source, we look at configuring the destination profile next.

1. Create the COS configuration settings by copying the following and pasting into `rclone.conf` using an appropriate editor.

   ```bash
   [COS]
   type = s3
   provider = IBMCOS
   env_auth = false
   region =
   access_key_id =
   secret_access_key =
   endpoint =

   ```

   {: codeblock}

1. Paste your [HMAC](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main) `access_key_id` and `secret_access_key` into the appropriate fields of your configuration as shown in the first step. As noted in the beginning of the guide, you will want to enter the appropriate values for your instance regarding your [region and endpoint](/docs/cloud-object-storage/basics?topic=cloud-object-storage-endpoints).

#### Verify your configurations
{: #migrate-verify-config}

1. List the buckets from your source to verify `rclone` is properly configured for retrieval.

    ```bash
    rclone lsd AWS:
    ```

   {: codeblock}

2. List the COS bucket for your destination you created to verify `rclone` is properly configured for storage.

    ```bash
    rclone lsd COS:
    ```

   {: codeblock}

### Use `rclone` to migrate from AWS
{: #migrate-aws-run}

1. Do a dry run (no data copied) of `rclone` to sync the objects in your source
   bucket (for example, `content-to-be-migrated`) to the target COS bucket (for example, `new-bucket`).

   ```bash
   rclone --dry-run copy AWS:content-to-be-migrated COS:new-bucket
   ```

   {: codeblock}

1. Check that the files you want to migrate appear after running the command. If everything looks as you expect, remove the `--dry-run` flag and add a `-v` flag to show a verbose output while the data is being copied. Using the optional `--checksum` flag avoids updating any files that have the same MD5 hash and object size in both locations.

   ```bash
   rclone -v copy --checksum AWS:content-to-be-migrated COS:new-bucket
   ```

   {: codeblock}

As you perform the migration of your data using the process you've outlined, you will want to validate and verify the results.

### Validating your migration from AWS
{: #migrate-testing}

Integrated query-in-place dashboards allows you to see analytics based directly on your data. Using [{{site.data.keyword.mon_full_notm}}](/docs/cloud-object-storage?topic=cloud-object-storage-mm-cos-integration), you can follow up your migration using pre-built charts.

## Next Steps
{: #migrate-next-steps}

Get started by visiting the [catalog](https://cloud.ibm.com){: external}, and creating the resources to begin your journey from AWS to {{site.data.keyword.cos_full_notm}} with confidence and efficiency.