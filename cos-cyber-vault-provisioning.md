---

copyright:
  years: 2026
lastupdated: "2026-08-27"

keywords: cyber vault, COS cyber vault, provisioning, create vault, object lock, CBR, Key Protect, IAM, time window, retention, HMAC, credentials, auto_hmac, service_id

subcollection: cloud-object-storage

content-type: howto

---

{{site.data.keyword.attribute-definition-list}}

# Provisioning an IBM Cloud Cyber Vault
{: #cyber-vault-provisioning}

IBM Cloud Cyber Vault is in **Beta**. This service may change before it is generally available. Features, endpoints, and configuration options described in this documentation are subject to change without notice. Review the [IBM Beta terms of service](/docs/overview?topic=overview-terms) before using this service.
{: beta}

Provision an IBM Cloud Cyber Vault to create an immutable, isolated storage vault on {{site.data.keyword.cos_full_notm}}. You can provision a vault by using the {{site.data.keyword.cloud_notm}} console or the COS Solutions API.
{: shortdesc}

## Before you begin
{: #cyber-vault-provisioning-prereqs}

Before provisioning an IBM Cloud Cyber Vault, ensure that you have the following:

- An active **{{site.data.keyword.cloud_notm}} account** with billing enabled and Cyber Vault Beta access enabled for your account. See [Onboarding your account to the IBM Cloud Cyber Vault Beta program](/docs/cloud-object-storage?topic=cloud-object-storage-cyber-vault-onboarding) if you have not yet requested access.
- An existing **{{site.data.keyword.cos_full_notm}} instance** (Standard plan) in the `us-south` (Dallas) region. The vault HMAC credentials are automatically created against this instance.
- **IAM permissions**: To create a Cyber Vault with automatic HMAC credential provisioning, you must have the **Cyber Vault Manager** role at the account level and the **Editor** or **Administrator** role on the {{site.data.keyword.cos_full_notm}} instance you intend to use.
- **At least one network restriction**: Either a VPC CRN or a private IP CIDR range (RFC 1918). Network isolation configuration is mandatory for all vault creation requests.

## Credential modes
{: #cyber-vault-credential-modes}

The {{site.data.keyword.cloud_notm}} console creates vaults exclusively in `auto_hmac` mode. Two scoped HMAC service credential pairs — a **write key** for backup agents and a **read key** for recovery and monitoring — are created automatically when the vault is provisioned. No pre-created IAM service IDs are required.

To use pre-existing IAM service IDs instead of auto-provisioned HMAC credentials, use the [{{site.data.keyword.cos_full_notm}} Solutions API](#cyber-vault-api-service-id) and supply an `access_policies` block in the create request.

## Provisioning a Cyber Vault by using the console
{: #cyber-vault-provisioning-ui}
{: ui}

Use the {{site.data.keyword.cloud_notm}} console to create a Cyber Vault by completing a guided form.

### Step 1: Open the Cyber Vaults page
{: #cyber-vault-ui-step-1}

1. Log in to the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com){: external}.
2. From the navigation menu, select **{{site.data.keyword.cos_full_notm}}**.
3. In the left navigation panel, click **Cyber Vault**.
4. On the Cyber Vaults page, click **Create Cyber Vault**.

The Create Cyber Vault form opens. A warning banner at the top of the form reminds you that vault configuration cannot be edited after creation.

### Step 2: Enter vault details
{: #cyber-vault-ui-step-2}

Complete the **Details** section of the creation form.

**Vault name prefix**
:   Enter a name prefix for your vault. The final vault name includes an auto-generated unique suffix appended by the service. The prefix must be between 3 and 63 characters, consist of lowercase alphanumeric characters and hyphens only, and begin and end with a lowercase alphanumeric character. You can use the refresh icon to generate a random prefix.

**Description (Optional)**
:   Enter a free-text description of the vault's purpose. For example: `Immutable backup vault for production database snapshots`.

**COS instance**
:   Select an existing {{site.data.keyword.cos_short}} instance from the dropdown. The vault HMAC credentials are created against the instance you select here.

### Step 3: Select a location
{: #cyber-vault-ui-step-3}

In the **Location** section, select the geographic region where your vault data is physically stored.

**Select a location**
:   At this time, only **Dallas (us-south)** is available.

### Step 4: Configure the object lock retention period
{: #cyber-vault-ui-step-4}

In the **Object lock retention period** section, set the default number of days that objects written to the vault are locked.

**Default retention (days)**
:   Use the stepper to enter the number of days. The default is `30`. Object lock prevents objects from being deleted for the configured amount of time. Enter `0` to apply locks manually per-object using your backup software's S3 Object Lock API support.

Setting **Default retention** to `0` does not apply an automatic retention lock. Objects can still have retention locks applied individually at write time.
{: note}

### Step 5: Review access controls
{: #cyber-vault-ui-step-5}

The **Access controls** section is informational. HMAC credentials are created automatically for read and write access. No identity configuration is required in the console.

**Time-based write restrictions (optional)**
:   Toggle **Time-based write restrictions** on to restrict when backup agents can write to the vault. When enabled, two time fields appear:

   - **Start time (UTC)**: The start of the daily write window in `HH:MM` format. For example, `07:00`.
   - **End time (UTC)**: The end of the daily write window in `HH:MM` format. For example, `18:00`. The end time must be later than the start time.

When the toggle is off, backup agents have always-on write access within any network restrictions you configure.

### Step 6: Configure context-based restrictions
{: #cyber-vault-ui-step-6}

In the **Context-based restrictions** section, restrict vault access by network zone. Click **Add Context-based restrictions** to open the restrictions panel. Select the restriction type and provide the required information.

| Restriction type | Description |
| ---------------- | ----------- |
| **VPC** | Select a VPC from your account by name or region. The vault accepts connections only from within the selected VPC. |
| **Cross-account VPC** | Enter a VPC CRN directly to add a VPC from another account, such as in a hub-and-spoke topology. |
| **IP address** | Enter a private IP CIDR range (RFC 1918 compliant: `10.0.0.0/8`, `172.16.0.0/12`, or `192.168.0.0/16`). |
{: caption="Context-based restriction types" caption-side="bottom"}

Click **Save** in the panel to add the restriction to the list. Repeat to add multiple restrictions of any type.

{{site.data.keyword.IBM_notm}} recommends configuring at least one context-based restriction for all production vaults. A vault with no restrictions is accessible from any network that holds valid HMAC credentials.
{: note}

### Step 7: Review encryption
{: #cyber-vault-ui-step-7}

The **Encryption** section is informational. In the Beta release, encryption is provider-managed only. {{site.data.keyword.IBM_notm}} automatically creates and rotates AES-256 keys for the vault. No key management is required.

To use a customer-managed root key (Bring Your Own Key), use the [{{site.data.keyword.cos_full_notm}} Solutions API](#cyber-vault-provisioning-api) and set `encryption_config.type` to `customer_managed`.

### Step 8: Accept the license and create the vault
{: #cyber-vault-ui-step-8}

1. Select the **I have read and agree to the following license agreement** checkbox and review the linked terms.
2. Click **Create**.

Vault configuration is permanent and cannot be changed after creation. Review all settings carefully before clicking **Create**.
{: important}

The vault status displays as **Provisioning** while the service configures the underlying {{site.data.keyword.cos_full_notm}} bucket, creates the HMAC credentials, and applies all access controls. After provisioning completes, the status changes to **Active** and the vault is ready to use.

Vault creation is an asynchronous operation. The `access_restrictions` block — which contains your write and read HMAC credential details — is absent while the vault is in `provisioning` status. It becomes available once the vault reaches `active` status.
{: note}

## Provisioning a Cyber Vault by using the API
{: #cyber-vault-provisioning-api}
{: api}

You can create a Cyber Vault programmatically by sending a `POST` request to the {{site.data.keyword.cos_full_notm}} Solutions API. All requests must be authenticated with an {{site.data.keyword.cloud_notm}} IAM Bearer token.

The API endpoint for all Cyber Vault management operations is:

```
https://solutions.private.cloud-object-storage.cloud.ibm.com/v1
```
{: codeblock}

### Mode 1: Auto-HMAC (default)
{: #cyber-vault-api-auto-hmac}

In the default `auto_hmac` mode, omit the `access_policies` field. The service automatically creates two scoped HMAC service credential pairs — a write key and a read key — against your `existing_cos_instance_id`.

#### Creating a vault without time restrictions
{: #cyber-vault-api-create-no-time}

The following example creates a vault with always-on write access within the specified network zone.

```bash
curl -X POST \
  https://solutions.private.cloud-object-storage.cloud.ibm.com/v1/cyber_vaults \
  -H "Authorization: Bearer $CUSTOMER_IAM_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-cyber-vault",
    "region": "us-south",
    "existing_cos_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/abc123def456abc123def456abc123de:12345678-1234-1234-1234-123456789012::",
    "retention_config": {
      "minimum_days": 7,
      "default_days": 30,
      "maximum_days": 365
    },
    "encryption_config": {
      "type": "provider_managed"
    },
    "network_isolation_config": {
      "allowed_vpc_crns": [
        "crn:v1:bluemix:public:is:us-south:a/abc123def456abc123def456abc123de::vpc:r006-11112222-3333-4444-5555-666677778888"
      ],
      "allowed_ip_ranges": [
        "10.0.0.0/8"
      ]
    }
  }'
```
{: codeblock}

#### Creating a vault with write time restrictions
{: #cyber-vault-api-create-time-window}

The optional `write_time_restrictions` block constrains when the auto-provisioned write and read IAM policies are active. Outside the specified window, the auto-provisioned service IDs have no effective access to the vault bucket.

```bash
curl -X POST \
  https://solutions.private.cloud-object-storage.cloud.ibm.com/v1/cyber_vaults \
  -H "Authorization: Bearer $CUSTOMER_IAM_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-cyber-vault-scheduled",
    "region": "us-south",
    "existing_cos_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/abc123def456abc123def456abc123de:12345678-1234-1234-1234-123456789012::",
    "retention_config": {
      "minimum_days": 7,
      "default_days": 30,
      "maximum_days": 365
    },
    "encryption_config": {
      "type": "provider_managed"
    },
    "network_isolation_config": {
      "allowed_vpc_crns": [
        "crn:v1:bluemix:public:is:us-south:a/abc123def456abc123def456abc123de::vpc:r006-11112222-3333-4444-5555-666677778888"
      ],
      "allowed_ip_ranges": [
        "10.0.0.0/8"
      ]
    },
    "write_time_restrictions": {
      "start": "02:00:00+00:00",
      "end": "06:00:00+00:00",
      "days_of_week": [1, 2, 3, 4, 5],
      "timezone": "UTC",
      "description": "Backup window: Mon-Fri 02:00-06:00 UTC"
    }
  }'
```
{: codeblock}

### Mode 2: Supply existing service IDs
{: #cyber-vault-api-service-id}

To use your own pre-existing IAM service IDs instead of auto-provisioned HMAC credentials, supply an `access_policies` block in the create request. The service skips HMAC creation and uses the supplied identities directly.

```bash
curl -X POST \
  https://solutions.private.cloud-object-storage.cloud.ibm.com/v1/cyber_vaults \
  -H "Authorization: Bearer $CUSTOMER_IAM_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-cyber-vault-byoid",
    "region": "us-south",
    "existing_cos_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/abc123def456abc123def456abc123de:12345678-1234-1234-1234-123456789012::",
    "retention_config": {
      "minimum_days": 7,
      "default_days": 30,
      "maximum_days": 365
    },
    "encryption_config": {
      "type": "provider_managed"
    },
    "network_isolation_config": {
      "allowed_vpc_crns": [
        "crn:v1:bluemix:public:is:us-south:a/abc123def456abc123def456abc123de::vpc:r006-11112222-3333-4444-5555-666677778888"
      ]
    },
    "access_policies": {
      "write": {
        "identities": [
          { "type": "service_id", "id": "ServiceId-aaaabbbb-1111-2222-3333-444455556666" }
        ]
      },
      "read": {
        "identities": [
          { "type": "service_id", "id": "ServiceId-ccccdddd-7777-8888-9999-aaaabbbbcccc" }
        ]
      }
    }
  }'
```
{: codeblock}

### Request body fields
{: #cyber-vault-api-request-fields}

The following table describes all fields in the create Cyber Vault request body.

| Field | Type | Required | Description |
| ----- | ---- | -------- | ----------- |
| `name` | string | Required | The vault name prefix. Must be 3–63 characters of lowercase alphanumeric characters and hyphens. Must begin and end with a lowercase alphanumeric character. An auto-generated unique suffix is appended. |
| `description` | string | Optional | A human-readable description of the vault. Maximum 256 characters. |
| `region` | string | Required | {{site.data.keyword.cloud_notm}} region for the vault. Only `us-south` is supported at this time. |
| `existing_cos_instance_id` | string | Required for `auto_hmac` | The CRN of an existing COS Standard plan instance. HMAC credentials are created against this instance. Required when `access_policies` is omitted. Returns `400` if absent in `auto_hmac` mode. |
| `retention_config` | object | Required | Object Lock retention configuration. |
| `retention_config.minimum_days` | integer | Required | Minimum retention period in days that can be applied to individual objects. |
| `retention_config.default_days` | integer | Required | Default retention period in days applied to objects that do not specify a per-object retention period at write time. Set to `0` for backup software that manages its own per-object retention via the S3 Object Lock API. |
| `retention_config.maximum_days` | integer | Required | Maximum retention period in days. Range: 0–3,650. |
| `encryption_config` | object | Required | Encryption configuration for the vault. |
| `encryption_config.type` | string | Required | Encryption key management type. Accepted values: `provider_managed` ({{site.data.keyword.IBM_notm}} manages keys, FIPS 140-2 Level 3 HSMs), `customer_managed` (you supply a {{site.data.keyword.keymanagementserviceshort}} or {{site.data.keyword.hscrypto}} root key CRN). |
| `encryption_config.key_crn` | string | Conditional | CRN of the {{site.data.keyword.keymanagementserviceshort}} or {{site.data.keyword.hscrypto}} root key. Required when `encryption_config.type` is `customer_managed`. |
| `network_isolation_config` | object | Required | Network isolation configuration. At least one of `allowed_vpc_crns` or `allowed_ip_ranges` must be non-empty. Returns `400` if both are absent. |
| `network_isolation_config.allowed_vpc_crns` | array of strings | Conditional | List of VPC CRNs whose traffic is permitted to reach the vault. |
| `network_isolation_config.allowed_ip_ranges` | array of strings | Conditional | List of private IP CIDR ranges (RFC 1918 only) whose traffic is permitted to reach the vault. |
| `write_time_restrictions` | object | Optional | Time window during which write (and read) IAM policies are active. Absent means always-on access. |
| `write_time_restrictions.start` | string | Conditional | Start of the daily write window. Format: `HH:MM:SS+00:00` (UTC). Required when `write_time_restrictions` is present. |
| `write_time_restrictions.end` | string | Conditional | End of the daily write window. Format: `HH:MM:SS+00:00` (UTC). Must be later than `start`. Required when `write_time_restrictions` is present. |
| `write_time_restrictions.days_of_week` | array of integers | Optional | ISO weekday integers on which the window is active (`1`=Monday through `7`=Sunday). Omit to apply the window every day. |
| `write_time_restrictions.timezone` | string | Optional | IANA timezone string. Defaults to `"UTC"`. |
| `write_time_restrictions.description` | string | Optional | Free-text label stored in the IAM policy description. |
| `access_policies` | object | Optional | Explicit access policy configuration. When present, the vault uses `service_id` mode and HMAC credential auto-creation is skipped. |
| `access_policies.write.identities` | array | Conditional | List of IAM service IDs authorized for write access. Required when `access_policies` is present. Each entry must include `type` (`"service_id"`) and `id`. |
| `access_policies.read.identities` | array | Conditional | List of IAM service IDs authorized for read-only access. Required when `access_policies` is present. |
{: caption="Create Cyber Vault request body fields" caption-side="bottom"}

### Create response
{: #cyber-vault-api-create-response}

A successful request returns HTTP `202 Accepted`. The vault `status` field is initially set to `provisioning`. The `access_restrictions` block is absent at this stage; it is populated once the vault reaches `active` status.

**`auto_hmac` mode — `202 Accepted` (with write time restrictions):**

```json
{
  "id": "vault-aabbccdd-1234-5678-abcd-eeff00112233",
  "name": "cv-my-cyber-vault-scheduled-aabbccdd",
  "status": "provisioning",
  "credential_mode": "auto_hmac",
  "region": "us-south",
  "account_id": "abc123def456abc123def456abc123de",
  "cos_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/abc123def456abc123def456abc123de:12345678-1234-1234-1234-123456789012::",
  "network_isolation_config": {
    "allowed_vpc_crns": [
      "crn:v1:bluemix:public:is:us-south:a/abc123def456abc123def456abc123de::vpc:r006-11112222-3333-4444-5555-666677778888"
    ],
    "allowed_ip_ranges": ["10.0.0.0/8"]
  },
  "write_time_restrictions": {
    "start": "02:00:00+00:00",
    "end": "06:00:00+00:00",
    "days_of_week": [1, 2, 3, 4, 5],
    "timezone": "UTC",
    "description": "Backup window: Mon-Fri 02:00-06:00 UTC"
  },
  "retention_config": { "minimum_days": 7, "default_days": 30, "maximum_days": 365 },
  "encryption_config": { "type": "provider_managed" },
  "created_at": "2026-08-15T10:00:00Z",
  "updated_at": "2026-08-15T10:00:00Z"
}
```
{: codeblock}

**`service_id` mode — `202 Accepted`:**

```json
{
  "id": "vault-11111111-aaaa-bbbb-cccc-222222222222",
  "name": "cv-my-cyber-vault-byoid-11111111",
  "status": "provisioning",
  "credential_mode": "service_id",
  "region": "us-south",
  "cos_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/abc123def456abc123def456abc123de:12345678-1234-1234-1234-123456789012::",
  "network_isolation_config": {
    "allowed_vpc_crns": [
      "crn:v1:bluemix:public:is:us-south:a/abc123def456abc123def456abc123de::vpc:r006-11112222-3333-4444-5555-666677778888"
    ]
  },
  "retention_config": { "minimum_days": 7, "default_days": 30, "maximum_days": 365 },
  "encryption_config": { "type": "provider_managed" },
  "created_at": "2026-08-15T11:00:00Z",
  "updated_at": "2026-08-15T11:00:00Z"
}
```
{: codeblock}

The following table describes the fields in the create response.

| Field | Type | Description |
| ----- | ---- | ----------- |
| `id` | string (UUID) | Unique identifier for the vault. Use this value as `{vault_id}` in subsequent API calls. |
| `name` | string | The full vault name including the auto-generated suffix. |
| `status` | string | Current vault status. One of: `provisioning`, `active`, `failed`, `deleting`. |
| `credential_mode` | string | Credential mode used. One of: `auto_hmac`, `service_id`. |
| `region` | string | Region in which the vault was created. |
| `account_id` | string | {{site.data.keyword.cloud_notm}} account ID. |
| `cos_instance_id` | string | CRN of the {{site.data.keyword.cos_full_notm}} instance associated with the vault. |
| `network_isolation_config` | object | Network isolation settings as specified in the create request. |
| `write_time_restrictions` | object | Write time window as specified in the create request. Absent if not configured. |
| `retention_config` | object | Object Lock retention configuration as specified in the create request. |
| `encryption_config` | object | Encryption configuration as specified in the create request. |
| `created_at` | string (date-time) | ISO 8601 timestamp of vault creation. |
| `updated_at` | string (date-time) | ISO 8601 timestamp of the last update. |
| `vault_private_endpoint` | string | Private S3 endpoint URL. Populated after provisioning completes. |
| `vault_direct_endpoint` | string | Direct S3 endpoint URL. Populated after provisioning completes. |
| `access_restrictions` | object | HMAC credential or service ID access configuration. Populated after provisioning completes. |
{: caption="Create Cyber Vault response fields" caption-side="bottom"}

### Retrieving vault details and HMAC credentials
{: #cyber-vault-api-get}

After a vault reaches `active` status, retrieve its details — including endpoint URLs and HMAC credential information — by using the `GET /v1/cyber_vaults/{vault_id}` endpoint.

#### Getting vault details (credential URLs, no secrets)
{: #cyber-vault-api-get-default}

By default, the `GET` response includes the Resource Controller URL for each HMAC credential but does not return the `access_key_id` or `secret_access_key` values. Use the `credential_url` in each credential entry to retrieve the secrets directly from Resource Controller with your own IAM token.

```bash
curl -X GET \
  https://solutions.private.cloud-object-storage.cloud.ibm.com/v1/cyber_vaults/vault-87654321-abcd-1234-efgh-210987654321 \
  -H "Authorization: Bearer $CUSTOMER_IAM_TOKEN"
```
{: codeblock}

**Response — `200 OK`:**

```json
{
  "id": "vault-87654321-abcd-1234-efgh-210987654321",
  "name": "cv-my-cyber-vault-87654321",
  "status": "active",
  "credential_mode": "auto_hmac",
  "region": "us-south",
  "account_id": "abc123def456abc123def456abc123de",
  "cos_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/abc123def456abc123def456abc123de:12345678-1234-1234-1234-123456789012::",
  "vault_private_endpoint": "https://cv-my-cyber-vault-87654321.s3.private.us-south.cloud-object-storage.appdomain.cloud",
  "vault_direct_endpoint": "https://cv-my-cyber-vault-87654321.s3.direct.us-south.cloud-object-storage.appdomain.cloud",
  "network_isolation_config": {
    "allowed_vpc_crns": [
      "crn:v1:bluemix:public:is:us-south:a/abc123def456abc123def456abc123de::vpc:r006-11112222-3333-4444-5555-666677778888"
    ],
    "allowed_ip_ranges": ["10.0.0.0/8"]
  },
  "access_restrictions": {
    "write": {
      "credentials": [
        {
          "type": "hmac",
          "name": "cv-my-cyber-vault-87654321-write-key",
          "id": "wr-cred-uuid-1111-2222-3333",
          "credential_url": "https://resource-controller.cloud.ibm.com/v2/resource_keys/wr-cred-uuid-1111-2222-3333"
        }
      ],
      "time_windows": []
    },
    "read": {
      "credentials": [
        {
          "type": "hmac",
          "name": "cv-my-cyber-vault-87654321-read-key",
          "id": "rd-cred-uuid-4444-5555-6666",
          "credential_url": "https://resource-controller.cloud.ibm.com/v2/resource_keys/rd-cred-uuid-4444-5555-6666"
        }
      ]
    }
  },
  "retention_config": { "minimum_days": 7, "default_days": 30, "maximum_days": 365 },
  "encryption_config": { "type": "provider_managed" },
  "created_at": "2026-08-15T10:00:00Z",
  "updated_at": "2026-08-15T10:01:30Z"
}
```
{: codeblock}

When `write_time_restrictions` were configured at creation time, the same window is echoed in `access_restrictions.write.time_windows[]` so you can inspect the active schedule without re-reading the original request.

#### Getting vault details with inline HMAC secrets
{: #cyber-vault-api-get-include-credentials}

To retrieve the `access_key_id` and `secret_access_key` values inline in a single API call, add the `include_credentials=true` query parameter. The orchestrator fetches the secrets from Resource Controller on your behalf using your forwarded IAM token and returns them inside each credential entry's `hmac_keys` block. **Secrets are never stored or cached by the orchestrator.**

```bash
curl -X GET \
  "https://solutions.private.cloud-object-storage.cloud.ibm.com/v1/cyber_vaults/vault-87654321-abcd-1234-efgh-210987654321?include_credentials=true" \
  -H "Authorization: Bearer $CUSTOMER_IAM_TOKEN"
```
{: codeblock}

**Response — `200 OK`:**

```json
{
  "id": "vault-87654321-abcd-1234-efgh-210987654321",
  "name": "cv-my-cyber-vault-87654321",
  "status": "active",
  "credential_mode": "auto_hmac",
  "access_restrictions": {
    "write": {
      "credentials": [
        {
          "type": "hmac",
          "name": "cv-my-cyber-vault-87654321-write-key",
          "id": "wr-cred-uuid-1111-2222-3333",
          "credential_url": "https://resource-controller.cloud.ibm.com/v2/resource_keys/wr-cred-uuid-1111-2222-3333",
          "hmac_keys": {
            "access_key_id": "347aa3a4b34344f8bc7c7cccdf856e4c",
            "secret_access_key": "gvurfb82712ad14W7a7915h763a6i87155d30a1234364f61"
          }
        }
      ],
      "time_windows": []
    },
    "read": {
      "credentials": [
        {
          "type": "hmac",
          "name": "cv-my-cyber-vault-87654321-read-key",
          "id": "rd-cred-uuid-4444-5555-6666",
          "credential_url": "https://resource-controller.cloud.ibm.com/v2/resource_keys/rd-cred-uuid-4444-5555-6666",
          "hmac_keys": {
            "access_key_id": "a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4",
            "secret_access_key": "z9y8x7w6v5u4z9y8x7w6v5u4z9y8x7w6v5u4z9y8x7"
          }
        }
      ]
    }
  },
  "retention_config": { "minimum_days": 7, "default_days": 30, "maximum_days": 365 },
  "encryption_config": { "type": "provider_managed" },
  "created_at": "2026-08-15T10:00:00Z",
  "updated_at": "2026-08-15T10:01:30Z"
}
```
{: codeblock}

The `hmac_keys` block is present only when `include_credentials=true` is set. The `access_key_id` and `secret_access_key` values are fetched live from Resource Controller using your own bearer token, are never stored by the orchestrator, and are never written to structured logs. Treat the response body as a secret.
{: important}

`include_credentials=true` is silently ignored for vaults that use `service_id` mode. No `hmac_keys` block is returned for those vaults.
{: note}

If your IAM token does not have the `resource-controller.credential.retrieve_all` permission on the credential resource, the API returns `403`:

```json
{
  "status_code": 403,
  "error": "failed to retrieve HMAC credentials from Resource Controller: caller does not have resource-controller.credential.retrieve_all on the credential resource"
}
```
{: codeblock}

#### Listing all vaults
{: #cyber-vault-api-list}

To retrieve a list of all Cyber Vaults in your account, send a `GET` request to the `/v1/cyber_vaults` endpoint.

```bash
curl -X GET \
  "https://solutions.private.cloud-object-storage.cloud.ibm.com/v1/cyber_vaults?account_id=abc123def456abc123def456abc123de" \
  -H "Authorization: Bearer $CUSTOMER_IAM_TOKEN"
```
{: codeblock}

**Query parameters:**

| Parameter | Type | Required | Description |
| --------- | ---- | -------- | ----------- |
| `account_id` | string | Required | {{site.data.keyword.cloud_notm}} account ID (32-character hexadecimal string). |
| `cos_instance_id` | string | Optional | Filter vaults by {{site.data.keyword.cos_full_notm}} instance CRN. |
| `limit` | integer | Optional | Maximum number of vaults to return. Range: 1–100. Default: `50`. |
| `offset` | integer | Optional | Number of vaults to skip for pagination. Range: 0–10000. Default: `0`. |
{: caption="List Cyber Vaults query parameters" caption-side="bottom"}

### Error responses
{: #cyber-vault-api-errors}

The following table describes the error codes returned by the create vault operation.

| HTTP status | Description |
| ----------- | ----------- |
| `400 Bad Request` | A required field is missing or invalid. Common causes: `existing_cos_instance_id` absent in `auto_hmac` mode, `network_isolation_config` empty, an IP range is not in RFC 1918 private space, or a field value is outside the allowed range. |
| `401 Unauthorized` | The Bearer token is missing, expired, or lacks the required IAM permissions. |
| `403 Forbidden` | The caller is not authorized to create service credentials on the specified {{site.data.keyword.cos_full_notm}} instance, or lacks `resource-controller.credential.retrieve_all` when `include_credentials=true` is used. |
| `409 Conflict` | A vault with the same name already exists in this account and region. |
| `500 Internal Server Error` | An unexpected server error occurred. Retry the request. If the problem persists, contact {{site.data.keyword.cloud_notm}} support and provide the `trace` value from the error response. |
{: caption="Cyber Vault API error codes" caption-side="bottom"}

**`400` — Missing `existing_cos_instance_id` in `auto_hmac` mode:**

```json
{
  "status_code": 400,
  "error": "existing_cos_instance_id is required when credential_mode is auto_hmac (the default)"
}
```
{: codeblock}

**`400` — Missing `network_isolation_config`:**

```json
{
  "status_code": 400,
  "error": "network_isolation_config is required: at least one of allowed_vpc_crns or allowed_ip_ranges must be provided"
}
```
{: codeblock}

**`400` — IP range not in RFC 1918 private space:**

```json
{
  "status_code": 400,
  "error": "allowed_ip_ranges must contain only private IP CIDR ranges (RFC 1918: 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16), got: 203.0.113.0/24"
}
```
{: codeblock}

## Viewing vault details by using the console
{: #cyber-vault-retrieve-ui}
{: ui}

The Cyber Vaults list page shows all vaults in your account with the following columns: **Vault Name**, **COS Instance**, **Status**, **Location**, **Encryption**, **Retention**, and **Created**.

To view the full details of a vault:

1. Log in to the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com){: external}.
2. From the navigation menu, select **{{site.data.keyword.cos_full_notm}}**.
3. In the left navigation panel, click **Cyber Vault**.
4. Click the vault name in the list to open its details page, or click the actions menu (⋯) on the vault entry and select **View details**.

The vault details page displays:

- Vault CRN and vault ID
- Status (`provisioning`, `active`, `failed`, or `deleting`)
- Private endpoint URL and direct endpoint URL
- HMAC write key and read key details, with a **View credentials** action for each
- Context-based restrictions (VPC CRNs and IP ranges)
- Write time restrictions (if configured)
- Default retention period
- Encryption type
- Creation timestamp

## Next steps
{: #cyber-vault-provisioning-next-steps}

After your vault is provisioned and its status is `active`, you can:

- **Retrieve your HMAC credentials**: Use `GET /v1/cyber_vaults/{vault_id}?include_credentials=true` to retrieve the write and read `access_key_id` and `secret_access_key` values. Configure your backup software with the write HMAC key and the vault's `vault_private_endpoint` as the S3 target.
- **Upload objects**: Use the vault's `vault_private_endpoint` or `vault_direct_endpoint` with standard S3-compatible tools or the {{site.data.keyword.cos_short}} SDKs to write backup data to the vault.
- **Verify retention locks**: After writing an object, use the COS Object Lock API to confirm that the compliance-mode retention lock was applied. See the [Object Lock documentation](/docs/cloud-object-storage?topic=cloud-object-storage-object-lock) for details.
- **Configure monitoring**: Enable {{site.data.keyword.logs_full_notm}} on the underlying COS bucket to audit all object access and configuration change events. See [Tracking events on your {{site.data.keyword.cos_full_notm}} buckets](/docs/cloud-object-storage?topic=cloud-object-storage-at) for instructions.
- **Check vault status**: Poll the vault status endpoint (`GET /v1/cyber_vaults/{vault_id}/status`) to monitor the vault lifecycle.
