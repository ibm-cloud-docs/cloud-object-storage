---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Bucket permissions

Assign access roles for users and Service IDs against buckets, using either the UI or the CLI to create policies.

| Access role | Example actions                            |
|:------------|---------------------------------------------------|
| Manager | Make objects public, create and destroy buckets and objects |
| Writer | Create and destroy buckets and objects |
| Reader | List and download objects |



## Granting access to a user

If the user needs to be able to use the console, it is necessary to also grant them a minimum role of `Viewer` on the instance itself.  This will allow them to view all buckets and list the objects within them. Then select **Bucket permissions** from the left navigation menu, select the user, and select the level of access (`Manager` or `Writer`) that they require.

If the user will interact with data using the API and doesn't require console access, _and_ they are a member of your account, you can grant access to a single bucket without any access to the parent instance.

## Policy enforcement
IAM policies are enforced hierarchically from greatest level of access to most restricted. Conflicts are resolved to the more permissive policy.  For example, if a user has both the `Writer` and `Reader` role on a bucket, the policy granting the `Reader` role will be ignored.

This is also applicable to service instance and bucket level policies.
  - If a user has a policy granting the `Writer` role on a service instance and the `Reader` role on a single bucket, the bucket-level policy will be ignored.
  - If a user has a policy granting the `Reader` role on a service instance and the `Writer` role on a single bucket, both policies will be enforced and the more permissive `Writer` role will take precedence for the individual bucket.

If it is necessary to restrict access to a single bucket (or set of buckets) ensure the user or Service ID doesn't have any instance level policies using either the console or CLI.

### Using the UI

  1. Navigate to the **Identity and Access** console from the **Manage** menu.
  2. Select **Users** from the left navigation menu.
  3. Select a user to view their existing policies, and assign a new policy or edit an existing policy.
  3. Select the service instance, user, and desired role.
  4. Enter `bucket` in the **Resource Type** field and the bucket name in the **Resource** field.
  5. Click **Submit**

Note that leaving the **Resource Type** or **Resource** fields blank will create an instance-level policy.
{:tip}

### Using the CLI

From a terminal run the following command:

```bash
bx iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

To list existing policies:

```bash
bx iam user-policies <user-name>
```
{:codeblock}

To edit an existing policy:

```bash
bx iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## Granting access to a service ID

If you need to grant access to a bucket for an application or other non-human entity, use a Service ID.  The Service ID can be created specifically for this purpose, or can be an existing Service ID already in use.

### Using the UI

  1. Navigate to the **Identity and Access** console from the **Manage** menu.
  2. Select **Service IDs** from the left navigation menu.
  3. Select a Service ID to view any existing policies, and assign a new policy or edit an existing policy.
  3. Select the service instance, service ID, and desired role.
  4. Enter `bucket` in the **Resource Type** field and the bucket name in the **Resource** field.
  5. Click **Submit**

  Note that leaving the **Resource Type** or **Resource** fields blank will create an instance-level policy.
  {:tip}
  
### Using the CLI

From a terminal run the following command:

```bash
bx iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

To list existing policies:

```bash
bx iam service-policies <service-id-name>
```
{:codeblock}

To edit an existing policy:

```bash
bx iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
