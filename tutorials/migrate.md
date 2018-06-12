---

copyright:
  years: 2018
lastupdated: "2018-06-12"

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
  3. Re-write your application to use the COS SDKs ([Java](/docs/services/cloud-object-storage/libraries/java.html), [Python](/docs/services/cloud-object-storage/libraries/python.html), [Node.js](/docs/services/cloud-object-storage/libraries/node.html)) or the [REST API](/docs/services/cloud-object-storage/api-reference/about-api.html).
  4. You should now see two different object storage instances on the [dashboard](/dashboard/storage) for storage services.  Ensure that you have resource groups and all regions displayed.

 ## Set up a compute resource to run the migration tool
  1. Choose a Linux/macOS/BSD machine or an IBM Cloud Infrastructure Bare Metal or Virtual Server
     with the best proximity to your data.
  2. If you are running the migration on an IBM Cloud Infrastructure Bare Metal or Virtual Server
     use the **private** Swift and COS endpoints.
  3. Otherwise use the **public** Swift and COS endpoints.  
  2. Install `rclone` from [either a package manager or precompiled binary](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Get Credentials and Endpoints

# Get Swift Credential
  1. Click on your Swift instance in the console.
  2. Click on **Service Credentials** in the navigation panel.
  3. Click on **New credential** to generate credential information.  Click **Add**.
  4. View the credential you created, and copy the JSON contents and save to a file for reference.  

# Get COS Credential
  1. Click on your COS instance in the console.
  2. Click on **Service Credentials** in the navigation panel.
  3. Click on **New credential** to generate credential information.
  4. Under **Inline Configuration Parameters** add `{"HMAC":true}`. Click **Add**.
  5. View the credential you created, and copy the JSON contents and save to a file for reference.

# Get COS Endpoint for bucket
  1. Click on **Buckets** in the navigation panel.
  2. Click on the target bucket for the migration.
  3. Click on **Configuration** in the navigation panel.
  4. Scroll down to the **Endpoints** section and choose the endpoint based on where
     you are running the migration tool then save to a file for reference.

## Configure `rclone`
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

3. Using the Swift Service Credential, fill in the following fields:

    ```
    user_id = <userId>
    key = <password>
    region = **dallas** OR **london**  depending on the location of the container
    endpoint_type =  **public** OR **internal**  internal is the private endpoint
    ```

4. Create the COS target by copying the following and pasting into `rclone.conf`.  .  

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

5. Using the COS Service Credential and Endpoint, fill in the following fields:

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint from above>       
    ```

6. List the Swift container to verify `rclone` is properly configured.

    ```
    rclone lsd SWIFT:
    ```

7. List the COS bucket to verify `rclone` is properly configured.

    ```
    rclone lsd COS:
    ```

## Run `rclone`

1. Do a dry run (no data copied) of `rclone` to sync the objects in your specified Swift container (e.g. `swift-test`) to COS bucket (e.g. `cos-test`).

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
