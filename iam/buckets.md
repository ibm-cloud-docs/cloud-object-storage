---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Bucket permissions

Assign access roles for users and service IDs against buckets, using either the UI or the CLI to create policies.

| Access role         | Example actions                                                            |
|:--------------------| ---------------------------------------------------------------------------|
| AccessAdministrator | Grant anonymous access to objects, create and destroy buckets and objects  |
| AccessEditor        | Create and destroy buckets and objects                                     |
| AccessViewer        | List and download objects                                                  |

## Granting access to a user

If the user needs to be able to use the console, it is necessary to also grant them a minimum role of `viewer` on the instance itself.  This will allow them to view all buckets and list the objects within them. Then select **Bucket permissions** from the left navigation menu, select the user, and select the level of access (`AccessAdministrator` or `AccessEditor`) that they require.

If the user will interact with data using the API and doesn't require console access, _and_ they are a member of your account, you can grant access to a single bucket without any access to the parent instance.

### Using the UI

  1. Navigate to the **Identity and Access** console from the **Manage** menu.
  2. Select **Users** from the left navigation menu.
  3. Select the service instance, user, and desired role.
  4. Enter `bucket` in the **Resource Type** field and the bucket name in the **Resource** field.
  5. Click **Submit**

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

## Granting access to a service ID

If you need to grant access to a bucket for an application or other non-human entity, use a service instance.  The service ID can be created specifically for this purpose, or can be an existing service ID already in use.

### Using the UI

  1. Navigate to the **Identity and Access** console from the **Manage** menu.
  2. Select **Service IDs** from the left navigation menu.
  3. Select the service instance, service ID, and desired role.
  4. Enter `bucket` in the **Resource Type** field and the bucket name in the **Resource** field.
  5. Click **Submit**

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
