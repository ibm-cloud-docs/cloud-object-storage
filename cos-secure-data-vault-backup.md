---

copyright:
  years: 2026
lastupdated: "2026-04-01"

keywords: object storage, backup, immutable, data vault, veeam, commvault, ibm storage defender, context-based restrictions, worm

subcollection: cloud-object-storage

content-type: tutorial

services: cloud-object-storage

account-plan: standard

completion-time: 30m

---

# Create a Secure Data Vault for Backup Products
{: #secure-data-vault-backup}
{: toc-content-type="tutorial"}
{: toc-services="cloud-object-storage"}
{: toc-completion-time="30m"}

This tutorial guides you through creating a secure, immutable data vault in {{site.data.keyword.cos_full}} for use with enterprise backup solutions such as IBM Storage Defender Data Protect, Veeam Backup & Replication, and Commvault.
{: shortdesc}

## Overview
{: #secure-vault-overview}

Enterprise backup solutions require a secure, reliable object storage target that protects backup data from accidental deletion, ransomware attacks, and unauthorized access. By configuring {{site.data.keyword.cos_short}} with immutability policies and context-based restrictions, you create a hardened data vault that meets compliance requirements and provides robust data protection.

### Objectives
{: #secure-vault-objectives}

- Create a {{site.data.keyword.cos_short}} bucket configured for backup workloads
- Enable immutability (WORM) to protect backup data from modification or deletion
- Configure context-based restrictions to limit access to authorized networks and services
- Retrieve the credentials and connection information that are required by your backup product

### Architecture
{: #secure-vault-architecture}

The secure data vault architecture consists of:

- **{{site.data.keyword.cos_short}} bucket** - The storage target for backup data
- **Immutability policy** - WORM (Write Once Read Many) protection preventing data modification
- **Context-based restrictions** - Network and service-based access controls
- **Service credentials** - HMAC keys (Access Key and Secret Key) for S3-compatible access

## Before you begin
{: #secure-vault-prereqs}

Ensure that you have the following prerequisites in place:

1. An [{{site.data.keyword.cloud_notm}} account](https://cloud.ibm.com/registration){: external} with a valid payment method.
2. An instance of [{{site.data.keyword.cos_full}}](https://cloud.ibm.com/objectstorage/create){: external}.
3. Appropriate IAM permissions.
   1. **Manager** role on the {{site.data.keyword.cos_short}} instance to create buckets and set policies.
   2. **Administrator** role on the account to configure context-based restrictions.
4. Network connectivity information from your backup infrastructure (IP ranges, subnets).
5. Your backup product that is installed and ready to configure (IBM Storage Defender, Veeam, or Commvault).

## Create a bucket for backup data
{: #create-backup-bucket}
{: step}

Create a dedicated bucket for your backup workloads with appropriate storage class and settings.

### Using the console
{: #create-bucket-console}

1. Log in to the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com/){: external}.
2. Navigate to your {{site.data.keyword.cos_short}} instance from the **Resource list**.
3. Click **Create bucket**.
4. Select **Customize your bucket**.
5. Configure the bucket settings.
   1. **Bucket name**: Enter a unique, descriptive name (for example, `company-backup-vault-prod`).
   2. **Resiliency**: Select **Cross Region** or **Regional** based on your requirements.
   3. **Location**: Choose a location close to your backup infrastructure.
   4. **Storage class**: Select **Standard** for frequently accessed backups or **Vault** for long-term retention.
6. Click **Create bucket**.

### Using the CLI
{: #create-bucket-cli}

```bash
ibmcloud cos bucket-create \
  --bucket company-backup-vault-prod \
  --ibm-service-instance-id <your-cos-instance-crn> \
  --region us-south \
  --class standard
```
{: codeblock}

You can find your {{site.data.keyword.cos_full_notm}} instance CRN in the {{site.data.keyword.cloud_notm}} console under the **Service credentials** section of your {{site.data.keyword.cos_short}} instance.
{: tip}

## Configure immutability (WORM protection)
{: #configure-immutability}
{: step}

Immutability helps to ensure that backup data cannot be modified or deleted until the retention period expires. This protects against ransomware, accidental deletion, and malicious insiders.

Immutability cannot be disabled once enabled on a bucket. Plan your retention periods carefully before enabling this feature.
{: attention}

### Understanding retention periods
{: #retention-periods}

{{site.data.keyword.cos_short}} immutability uses three retention period settings:

| Setting | Description |
|---------|-------------|
| **Minimum retention** | The shortest time an object must be retained. Objects cannot be deleted before this period expires. |
| **Default retention** | Applied to objects when no retention period is specified during upload. |
| **Maximum retention** | The longest retention period that can be set on any object in the bucket. |
{: caption="Retention period settings" caption-side="top"}

### Enable immutability by using the console
{: #enable-immutability-console}

1. Navigate to your backup bucket in the {{site.data.keyword.cos_short}} console.
2. Click the **Configuration** tab.
3. Scroll to **Immutable Object Storage** and click **Add retention policy**.
4. Configure the retention periods.
   1. **Minimum retention period**: Set based on your shortest backup retention requirement (for example, 7 days).
   2. **Default retention period**: Set to your standard backup retention (for example, 30 days).
   3. **Maximum retention period**: Set to your longest retention requirement (for example, 365 days or 7 years for compliance).
5. Click **Save**.

### Enable immutability by using the CLI
{: #enable-immutability-cli}

```bash
ibmcloud cos bucket-protection-configuration-put \
  --bucket company-backup-vault-prod \
  --protection-configuration '{
    "status": "Retention",
    "minimum_retention": {"days": 7},
    "default_retention": {"days": 30},
    "maximum_retention": {"days": 365}
  }'
```
{: codeblock}

## Configure context-based restrictions
{: #configure-cbr}
{: step}

Context-based restrictions (CBR) add an additional layer of security by limiting which networks, services, and endpoints can access your backup bucket. Even if credentials are compromised, access is denied unless the request originates from an approved context.

### Plan your access rules
{: #plan-cbr-rules}

Before configuring CBR, identify the following:

- **Backup server IP addresses or subnets**: The network ranges where your backup software runs
- **Private or public endpoint access**: Prefer private endpoints for production workloads
- **Service-to-service access**: Whether other {{site.data.keyword.cloud_notm}} services need access

### Create a network zone
{: #create-network-zone}

A network zone defines the allowed source networks for access.

1. Navigate to [Context-based restrictions](https://cloud.ibm.com/context-based-restrictions/zones){: external} in the {{site.data.keyword.cloud_notm}} console.
2. Click **Create zone**.
3. Enter a descriptive name (for example, `backup-infrastructure-zone`).
4. Add allowed network sources.
   1. **IP addresses**: Add your backup server IPs (for example, `10.240.0.0/24`).
   2. **VPC**: Select your VPC if backup servers run in {{site.data.keyword.cloud_notm}}.
   3. **Service references**: Add any {{site.data.keyword.cloud_notm}} services that need access.
5. Click **Create**.

### Create a CBR rule
{: #create-cbr-rule}

1. Navigate to [Context-based restrictions rules](https://cloud.ibm.com/context-based-restrictions/rules){: external}.
2. Click **Create rule**.
3. Configure the rule.
   1. **Description**: `Restrict backup bucket to authorized infrastructure`.
   2. **Service**: Select **Cloud Object Storage**.
   3. **Scope**: Select your {{site.data.keyword.cos_short}} instance and the specific backup bucket.
   4. **Contexts**: Add the network zone that you created.
   5. **Enforcement**: Start with **Report-only** to test, then switch to **Enabled**.
4. Click **Create**.

Test your CBR rules thoroughly in report-only mode before enabling enforcement. Check the Activity Tracker logs to verify that legitimate backup traffic is matching the rules.
{: tip}

### CBR rule by using the CLI
{: #create-cbr-cli}

```bash
# Create a network zone
ibmcloud cbr zone-create \
  --name backup-infrastructure-zone \
  --addresses 10.240.0.0/24,10.241.0.0/24

# Create a CBR rule for the backup bucket
ibmcloud cbr rule-create \
  --description "Restrict backup bucket access" \
  --service-name cloud-object-storage \
  --resource-attributes "accountId=<account-id>,serviceName=cloud-object-storage,resource=company-backup-vault-prod" \
  --zone-id <zone-id> \
  --enforcement-mode enabled
```
{: codeblock}

## Create service credentials with HMAC keys
{: #create-credentials}
{: step}

Backup products use S3-compatible APIs to connect to {{site.data.keyword.cos_short}}. This requires HMAC credentials (Access Key and Secret Key).

### Using the console
{: #create-credentials-console}

1. Navigate to your {{site.data.keyword.cos_short}} instance in the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com/){: external}.
2. Click **Service credentials** in the left navigation.
3. Click **New credential**.
4. Configure the credential.
   1. **Name**: Enter a descriptive name (for example, `veeam-backup-credentials`).
   2. **Role**: Select **Writer** for backup operations (or **Manager** if the backup product needs to manage bucket settings).
   3. **HMAC Credential**: Toggle to **On** (this is critical for S3 compatibility).
5. Click **Add**.
6. Expand the new credential to view the details.

### Using the CLI
{: #create-credentials-cli}

```bash
ibmcloud resource service-key-create veeam-backup-credentials Writer \
  --instance-name my-cos-instance \
  --parameters '{"HMAC": true}'
```
{: codeblock}

### Retrieve HMAC credentials
{: #retrieve-hmac}

After creating the service credential, expand it to find the HMAC keys:

```json
{
  "apikey": "...",
  "cos_hmac_keys": {
    "access_key_id": "1a2b3c4d5e6f7g8h9i0j",
    "secret_access_key": "AbCdEfGhIjKlMnOpQrStUvWxYz1234567890abcd"
  },
  "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
  "iam_apikey_description": "...",
  "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:..."
}
```
{: codeblock}

Record the following values for your backup product configuration:

| Field | Description | Example |
|-------|-------------|---------|
| `access_key_id` | The Access Key for S3 authentication | `1a2b3c4d5e6f7g8h9i0j` |
| `secret_access_key` | The Secret Key for S3 authentication | `AbCdEfGhIjKlMnOpQrStUvWxYz...` |
{: caption="HMAC credential values" caption-side="top"}

Store your Secret Key securely. It cannot be retrieved again after the credential is created.
{: attention}

## Retrieve endpoint URLs
{: #retrieve-endpoints}
{: step}

{{site.data.keyword.cos_short}} provides multiple endpoints based on resiliency and access type.

### Find your endpoint
{: #find-endpoint}

1. Navigate to your bucket in the {{site.data.keyword.cos_short}} console.
2. Click the **Configuration** tab.
3. Scroll to **Endpoints** to see the available endpoints for your bucket's location.

### Endpoint types
{: #endpoint-types}

| Type | Description | Use case |
|------|-------------|----------|
| **Public** | Accessible over the internet | Remote backup sites, cloud-native backup |
| **Private** | Accessible only within {{site.data.keyword.cloud_notm}} | Backup servers in {{site.data.keyword.cloud_notm}} VPC |
| **Direct** | High-bandwidth connection for {{site.data.keyword.cloud_notm}} services | {{site.data.keyword.cloud_notm}} service integrations |
{: caption="Endpoint types" caption-side="top"}

### Common endpoint URLs
{: #common-endpoints}

**Regional endpoints (US South example):**

| Type | Endpoint URL |
|------|-------------|
| Public | `s3.us-south.cloud-object-storage.appdomain.cloud` |
| Private | `s3.private.us-south.cloud-object-storage.appdomain.cloud` |
| Direct | `s3.direct.us-south.cloud-object-storage.appdomain.cloud` |
{: caption="US South regional endpoints" caption-side="top"}

**Cross Region endpoints (US example):**

| Type | Endpoint URL |
|------|-------------|
| Public | `s3.us.cloud-object-storage.appdomain.cloud` |
| Private | `s3.private.us.cloud-object-storage.appdomain.cloud` |
| Direct | `s3.direct.us.cloud-object-storage.appdomain.cloud` |
{: caption="US Cross Region endpoints" caption-side="top"}

For a complete list of endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints).
{: tip}

## Configure your backup product
{: #configure-backup-product}
{: step}

Use the information gathered to configure your backup product's object storage repository.

### Required configuration values
{: #required-values}

Gather the following information before configuring your backup product:

| Parameter | Value | Where to find |
|-----------|-------|---------------|
| **Access Key** | Your HMAC access key ID | Service credentials |
| **Secret Key** | Your HMAC secret access key | Service credentials |
| **Bucket name** | Your backup bucket name | Bucket list in {{site.data.keyword.cos_short}} console |
| **Endpoint URL** | Regional or cross-region endpoint | Bucket configuration tab |
| **Region** | The region code (for example, `us-south`) | Bucket configuration tab |
{: caption="Configuration parameters" caption-side="top"}

### IBM Storage Defender Data Protect
{: #configure-storage-defender}

IBM Storage Defender Data Protect supports cloud tiering and archiving to S3-compatible storage including {{site.data.keyword.cos_short}}. The configuration process involves registering an external S3 target and creating protection policies.

For detailed configuration steps, refer to the [IBM Storage Defender Redbook](https://www.redbooks.ibm.com/redpieces/pdfs/sg248554.pdf){: external}, which covers Data Protect versions 7.1.1 and later.

#### Register an S3 external target
{: #register-s3-target}

1. Log in to the IBM Storage Defender Data Protect management interface.
2. Navigate to **System Configuration** > **External Storage**.
3. Click **Add External Target**.
4. Select **Cloud** as the target type, then select **S3 Compatible Storage**.
5. Configure the S3 connection.
   1. **Name**: Enter a descriptive name (for example, `ibm-cos-backup-vault`).
   2. **Endpoint URL**: Enter your {{site.data.keyword.cos_short}} endpoint (for example, `https://s3.us-south.cloud-object-storage.appdomain.cloud`).
   3. **Access Key**: Enter your HMAC `access_key_id`.
   4. **Secret Key**: Enter your HMAC `secret_access_key`.
   5. **Bucket Name**: Enter your backup bucket name.
6. Click **Test Connection** to validate the configuration
7. Click **Save** to register the external target

#### Create a protection policy for S3 archiving
{: #create-protection-policy}

1. Navigate to **Protection** > **Policies**.
2. Create or edit a protection policy.
3. In the policy configuration, enable **Cloud Tiering** or **Cloud Archive**.
4. Select the S3 external target that you registered.
5. Configure retention and tiering rules as needed.
6. Save the policy and assign it to your backup workloads.

Data Protect supports using IBM Storage Protect server as an S3 interface. If you have an existing Storage Protect infrastructure, you can leverage it as an S3 target for Data Protect clusters.
{: tip}

### Veeam Backup & Replication
{: #configure-veeam}

Veeam Backup & Replication supports {{site.data.keyword.cos_short}} as an S3-compatible object storage target. Use the New Object Storage Repository wizard to add your {{site.data.keyword.cos_short}} bucket.

For complete documentation, see [Adding S3 Compatible Object Storage](https://helpcenter.veeam.com/docs/vbr/userguide/adding_s3c_object_storage.html){: external} in the Veeam Help Center.

#### Step 1: Launch the wizard
{: #veeam-step1}

1. Open the Veeam Backup & Replication console.
2. Navigate to **Backup Infrastructure** view.
3. In the inventory pane, right-click **Backup Repositories**.
4. Select **Add Backup Repository**.
5. In the Add Backup Repository dialog, select **Object Storage** > **S3 Compatible** > **S3 Compatible**.

#### Step 2: Specify name
{: #veeam-step2}

1. In the **Name** field, enter a descriptive name for the repository (for example, `IBM-COS-Backup-Vault`).
2. Optionally enter a **Description**.
3. Select **Limit concurrent tasks to N** if you want to limit parallel operations.
4. Click **Next**.

#### Step 3: Specify account
{: #veeam-step3}

1. In the **Service point** field, enter your {{site.data.keyword.cos_short}} endpoint URL (for example, `s3.us-south.cloud-object-storage.appdomain.cloud`).
2. In the **Region** field, enter the region code (for example, `us-south`).
3. From the **Credentials** drop-down, click **Add** to create new credentials.
   1. In the **Access key** field, enter your HMAC `access_key_id`.
   2. In the **Secret key** field, enter your HMAC `secret_access_key`.
   3. Click **OK**.
4. Select the **Connection mode**.
   1. **Direct**: Data transfers directly to object storage (recommended for most deployments).
   2. **Through gateway server**: Use a gateway server if you have network restrictions.
5. Click **Next**.

#### Step 4: Specify bucket
{: #veeam-step4}

1. From the **Bucket** drop-down, select your backup bucket.
2. Click **Browse** to select or create a folder within the bucket for Veeam data.
3. Optionally configure storage limits:
   1. **Limit object storage consumption to**: Set a maximum capacity.
   2. **Make recent backups immutable for X days**: Enable immutability protection.
4. Click **Next**.

#### Step 5: Specify mount server
{: #veeam-step5}

1. Select the **Mount server** to use for restore operations.
2. Configure the **Instant recovery write cache folder**.
3. Click **Next**.

#### Step 6: Review and finish
{: #veeam-step6}

1. Review the configuration summary.
2. Click **Finish** to create the object storage repository.

For Veeam immutability to work, your {{site.data.keyword.cos_short}} bucket must have S3 Object Lock enabled. Note that S3 Object Lock is a different feature from the {{site.data.keyword.cos_short}} Immutable Object Storage retention policy. Check [S3 Object Lock compatibility](/docs/cloud-object-storage?topic=cloud-object-storage-object-lock) for details.
{: attention}

### Commvault
{: #configure-commvault}

Commvault supports {{site.data.keyword.cos_short}} as an S3-compatible cloud storage target. You can configure it through the Command Center for backup and archive operations.

For complete documentation, see [Getting Started with IBM Cloud Object Storage](https://documentation.commvault.com/2024/essential/getting_started_with_ibm_cloud_object_storage.html){: external} and [Configuration for IBM Cloud Object Storage](https://documentation.commvault.com/2024/essential/configuration_for_ibm_cloud_object_storage.html){: external} in the Commvault documentation.

#### Add cloud storage credentials
{: #commvault-credentials}

1. Log in to the Commvault Command Center.
2. Navigate to **Manage** > **Security**.
3. Click **Credential vault** (or **Manage credentials** in older versions).
4. Click **Add** to create a new credential.
5. Configure the credential:
   - **Credential name**: Enter a descriptive name (for example, `ibm-cos-credentials`).
   - **User account**: Enter the `access_key_id` from your {{site.data.keyword.cos_short}} service credentials.
   - **Password/API key**: Enter the `secret_access_key` from your {{site.data.keyword.cos_short}} service credentials.
6. Click **Save**

When creating service credentials in the {{site.data.keyword.cloud_notm}} console, ensure that the **Include HMAC Credential** option is enabled. The `access_key_id` and `secret_access_key` are found in the `cos_hmac_keys` section of the credentials JSON.
{: tip}

#### Configure cloud storage
{: #commvault-storage}

1. In the Command Center, navigate to **Storage** > **Cloud**.
2. Click **Add cloud storage**.
3. Select **IBM Cloud Object Storage** (or **S3 Compatible** if {{site.data.keyword.cos_full}} is not listed).
4. Configure the storage settings:
   - **Name**: Enter a descriptive name for the cloud storage.
   - **Service host/Endpoint**: Enter your {{site.data.keyword.cos_short}} endpoint (for example, `s3.us-south.cloud-object-storage.appdomain.cloud`).
   - **Credentials**: Select the credential that you created earlier.
5. From the **MediaAgent** list, select the MediaAgent that accesses the cloud storage.
6. Configure the **DDB MediaAgent** for deduplication (can be the same or different MediaAgent).
7. Click **Save**

#### Add a storage pool for backups
{: #commvault-pool}

1. Navigate to **Storage** > **Cloud**.
2. Select your {{site.data.keyword.cos_full_notm}}.
3. Click **Add storage pool** or configure bucket settings.
4. Select or enter your bucket name.
5. Configure retention and deduplication settings as needed.
6. Associate the storage pool with your backup plans.

For WORM/immutability with Commvault, configure Object Lock on your {{site.data.keyword.cos_short}} bucket and enable the corresponding compliance settings in Commvault's storage configuration.
{: tip}

## Verify the configuration
{: #verify-configuration}
{: step}

After configuring your backup product, verify the setup:

### Test backup operations
{: #test-backup}

1. Create a small test backup job in your backup product.
2. Run the backup and verify that it completes successfully.
3. Verify that the backup data appears in your {{site.data.keyword.cos_short}} bucket.

### Verify immutability
{: #verify-immutability}

1. Attempt to delete a recently backed-up object directly in the {{site.data.keyword.cos_short}} console.
2. The deletion should be denied with a retention policy error.
3. Verify in the Activity Tracker that the deletion attempt was logged.

### Verify that CBR is working
{: #verify-cbr}

1. Check Activity Tracker for any denied requests from outside your approved network zones.
2. Attempt access from a nonapproved network to verify it's blocked (if safe to do so).
3. Review CBR rule evaluations in the {{site.data.keyword.cloud_notm}} console.

## Best practices
{: #best-practices}

### Security recommendations
{: #security-recommendations}

- **Use private endpoints**: Route backup traffic over the {{site.data.keyword.cloud_notm}} private network when possible.
- **Rotate credentials regularly**: Create new HMAC credentials periodically and update your backup product.
- **Monitor with Activity Tracker**: Enable Activity Tracker to log all bucket operations.
- **Apply least privilege**: Grant only the minimum required IAM roles to backup credentials.
- **Enable versioning**: Consider enabling versioning for more protection against overwrites.

### Immutability recommendations
{: #immutability-recommendations}

- **Align retention with backup policies**: Set a minimum retention to match your shortest backup retention requirement.
- **Plan for compliance**: If subject to regulations (HIPAA, SEC, GDPR), set retention periods accordingly.
- **Test recovery procedures**: Regularly test that you can restore from immutable backups.

### Performance recommendations
{: #performance-recommendations}

- **Choose the appropriate storage class**: Use Standard for active backups, Vault or Cold Vault for archives.
- **Enable transfer acceleration**: For large backups over long distances, consider Aspera high-speed transfer.
- **Right-size concurrent connections**: Most backup products allow tuning concurrent upload streams.

## Related links
{: #related-links}

### {{site.data.keyword.cos_short}} documentation
{: #cos-docs}

- [{{site.data.keyword.cos_short}} documentation](/docs/cloud-object-storage)
- [Immutable Object Storage](/docs/cloud-object-storage?topic=cloud-object-storage-immutable)
- [Context-based restrictions](/docs/cloud-object-storage?topic=cloud-object-storage-cos-cbr)
- [Service credentials and HMAC](/docs/cloud-object-storage?topic=cloud-object-storage-service-credentials)
- [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints)
- [Activity Tracker integration](/docs/cloud-object-storage?topic=cloud-object-storage-at-events)
- [S3 Object Lock](/docs/cloud-object-storage?topic=cloud-object-storage-object-lock)

### Backup product documentation
{: #backup-docs}

- [IBM Storage Defender Redbook: Data Management Service and Data Protect](https://www.redbooks.ibm.com/redpieces/pdfs/sg248554.pdf){: external}
- [IBM Storage Defender Data Protect documentation](/docs/en/storage-defender/base?topic=storage-defender-data-protect)
- [Veeam: Adding S3 Compatible Object Storage](https://helpcenter.veeam.com/docs/vbr/userguide/adding_s3c_object_storage.html){: external}
- [Veeam: S3 Compatible Storage Considerations and Limitations](https://helpcenter.veeam.com/docs/vbr/userguide/s3_compatible_limitations.html){: external}
- [Commvault: Getting Started with IBM Cloud Object Storage](https://documentation.commvault.com/2024/essential/getting_started_with_ibm_cloud_object_storage.html){: external}
- [Commvault: Configuration for IBM Cloud Object Storage](https://documentation.commvault.com/2024/essential/configuration_for_ibm_cloud_object_storage.html){: external}
- [Commvault: Adding IBM Cloud Object Storage Repository](https://documentation.commvault.com/2024/essential/adding_ibm_cloud_object_storage_repository.html){: external}

## Next steps
{: #next-steps}

Now, that your secure data vault is configured:

1. **Set up backup schedules**: Configure your backup product with appropriate backup schedules.
2. **Configure alerting**: Set up notifications for backup failures or policy violations.
3. **Document recovery procedures**: Create runbooks for restore operations.
4. **Plan for disaster recovery**: Consider replicating critical backups to a secondary region.
5. **Review access periodically**: Audit IAM policies and CBR rules regularly.
