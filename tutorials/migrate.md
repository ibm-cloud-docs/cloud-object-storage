---

copyright:
  years: 2018
lastupdated: "2018-04-17"

---

# Migrating data from OpenStack Swift

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

Before {{site.data.keyword.cos_full_notm}} became available as an {{site.data.keyword.cloud_notm}} Platform service, projects that required an object store used [OpenStack Swift](/docs/services/ObjectStorage/index.html). We recommend developers update their applications and migrate their data to {{site.data.keyword.cloud_notm}} to take advantage of the new access control and encryption benefits provided by IAM and Key Protect, as well as new features as they become available.

The concept of a Swift 'container' is identical to a COS 'bucket'.  COS limits service instances to 100 buckets and some Swift instances may have a larger number of containers. COS buckets can hold billions of objects and supports forward slashes (`/`) in object names for directory-like 'prefixes' when organizing data.  COS supports IAM policies at the bucket and service instance levels.
{:tip}

## Set up {{site.data.keyword.cos_full_notm}}

  1. If you haven't created one yet, provision an instance of {{site.data.keyword.cos_full_notm}} from the [catalog](/catalog/services/cloud-object-storage).  
  2. Read through the [getting started guide](/docs/services/cloud-object-storage/getting-started.html) to familiarize yourself with key concepts such as [endpoints](/docs/services/cloud-object-storage/basics/endpoints.html) and [storage classes](/docs/services/cloud-object-storage/basics/classes.html).  Create any buckets that you will need to store your transferred data. The example assumes the target bucket is in the `us-south` region and uses a standard storage class.
  3. Re-write your application to use the COS SDKs ([Java](/docs/services/cloud-object-storage/libraries/java.html), [Python](/docs/services/cloud-object-storage/libraries/python.html), [Node.js](/docs/services/cloud-object-storage/libraries/node.html)) or the [REST API](/docs/services/cloud-object-storage/api-reference/about-compatibility-api.html).
  4. You should now see two different object storage instances on the [dashboard](/dashboard/storage) for storage services.  Ensure that you have resource groups and all regions displayed.

## Get Swift credentials

  1. Click on your Swift instance in the console.
  2. Click on **Service Credentials** in the navigation panel.
  3. Click on **New credential** to generate credential information.  Click **Add**.
  4. View the credential you created, and copy the JSON contents and save to a file for reference.

## Get COS credentials

  1. Click on your COS instance in the console.
  2. Click on **Service Credentials** in the navigation panel.
  3. Click on **New credential** to generate credential information.
  4. Under **Inline Configuration Parameters** add `{"HMAC":true}`. Click **Add**.
  5. View the credential you created, and copy the JSON contents and save to a file for reference.

## Set up a compute resource
  1. Choose a Linux/macOS/BSD machine with the best proximity to your data.
  2. Install `rclone` from [either a package manager or precompiled binary](https://rclone.org/install/).

```bash
curl https://rclone.org/install.sh | sudo \bash
```
{: pre}

## Configure `rclone`
  1. Create an `rclone` config file in `~/.rclone.conf`.

```bash
touch ~/.rclone.conf
```
{: pre}

  2. Create the Swift source by copying the following and pasting into `rclone.conf`.

```
[SWIFT]
type = swift
env_auth = false
user =
key =
auth = https://identity.open.softlayer.com/v3
user_id =
domain =
tenant =
tenant_id =
tenant_domain =
region =
storage_url =
auth_token =
endpoint_type = public
```
{: pre}

  3. Using the Swift Service Credential, fill in the following fields:

```
user = <username>
key = <password>
user_id = <userId>
domain = <domainName>
tenant = <project>
tenant_id = <projectId>
tenant_domain = <domainId>
```

{: pre}

  4. Create the COS target by copying the following and pasting into `rclone.conf`. Adjust the `endpoint` and `location_constraint` fields if not using a standard [storage class](/docs/services/cloud-object-storage/basics/classes.html) located in the `us-south` [region](/docs/services/cloud-object-storage/basics/endpoints.html).  

```
[COS]
type = s3
env_auth = false
access_key_id =
secret_access_key =
region = other-v4-signature
endpoint = s3.us-south.objectstorage.softlayer.net
location_constraint =
acl =
server_side_encryption =
storage_class =
```
{: pre}

  5. Using the COS Service Credential, fill in the following fields:

```
access_key_id = <access_key_id>
secret_access_key = <secret_access_key>
```

{: pre}

  6. List the Swift container to verify `rclone` is properly configured.

```bash
rclone lsd SWIFT:
```

{: pre}

  7. List the COS bucket to verify `rclone` is properly configured.

```bash
rclone lsd COS:
```

{: pre}

## Run `rclone`

  1. Do a dry run (no data copied) of `rclone` to sync the objects in your specified Swift container (e.g. `swift-test`) to COS bucket (e.g. `cos-test`).

```bash
rclone --dry-run copy SWIFT:swift-test COS:cos-test
```

{: pre}

  2. Check that the files you desire to migrate appear in the command output. If everything looks good, remove the `--dry-run` flag and add `-v` flag to copy the data

```bash
rclone -v copy SWIFT:swift-test COS:cos-test
```

Migrating data using `rclone` copies but does not delete the source data.
{:tip}

{: pre}

  3. Repeat for any other containers that require migration.
  4. Once all your data is copied, and you have verified that your application can access the data in COS, then delete your Swift service instance.
