---

copyright:
  years: 2026
lastupdated: "2026-08-27"

keywords: cyber vault, COS cyber vault, cyber resilience, immutable storage, object lock, data protection, ransomware, backup, air gap, CBR, Key Protect, onboarding, beta, HMAC

subcollection: cloud-object-storage

content-type: overview

---

{{site.data.keyword.attribute-definition-list}}

# IBM Cloud Cyber Vault overview
{: #cyber-vault-overview}

IBM Cloud Cyber Vault is in **Beta**. This new solution is provided as a Beta trial offering and made solely available for evaluation and testing purposes for a limited time. There are no warranties, SLAs or support provided, and beta products are not intended for production use. Review the [IBM terms of service](https://www.ibm.com/support/customer/csol/terms/?id=Z126-6567&cc=us&lc=en) before using this solution. Do not use IBM Cloud Cyber Vault for production workloads during the Beta period.
{: beta}

IBM Cloud Cyber Vault is a managed, air-gapped, S3-compatible storage solution that provides immutable, isolated object storage for enterprise data protection. Cyber Vault is designed to defend critical data from ransomware, accidental deletion, and insider threats by combining compliance-mode Object Lock retention, encryption, identity-based access restrictions, network isolation, time-windowed write access, and automatically provisioned HMAC credential pairs into a single, provisioned vault resource.
{: shortdesc}

## What is IBM Cloud Cyber Vault?
{: #cyber-vault-what-is}

IBM Cloud Cyber Vault is a managed S3 compatible storage solution that is built on IBM COS and IBM Cloud primitives that provides virtual air gap, immutability with Object Lock, encryption with FIPS 140-2 Level 3 managed keys and built-in geo-redundancy.

When you create a Cyber Vault, the service automatically configures a hardened COS bucket with the following protections applied:

- **Compliance-mode Object Lock retention** enforces that every object that is written to the vault is locked and cannot be deleted or overwritten until its retention period expires. This protection cannot be overridden by any user or administrator, including root users.
- **Encryption** protects all data at rest. IBM manages and rotates the keys that are used for encrypting vault data.
- **IAM restrictions**: The Cyber Vault is provisioned with dedicated HMAC credentials scoped separately for read and write workflows. Each credential set grants the minimum set of permissions required for its workflow following a least-privilege access model.
- **Time-windowed write access** optionally restricts vault writes to a defined daily time window, ensuring that data can only be written during approved hours.
- **Context-Based Restrictions (CBR)** limit vault access to specific network zones, VPC CRNs, or private IP CIDR ranges, enforcing network-level isolation.
- **Read access controls** restrict read operations to explicitly authorized identities, supporting the principle of least privilege across the full data lifecycle.

Together, these controls implement a verified, software-defined air-gap pattern that protects backup and archive data from ransomware, exfiltration, and unauthorized modification.

## Key features and capabilities
{: #cyber-vault-features}

Compliance-mode Object Lock retention
:   Every object that is stored in a Cyber Vault is automatically assigned a compliance-mode WORM (Write Once, Read Many) retention lock. Objects cannot be deleted or modified until the configured retention period expires. This applies to all new objects regardless of the requester's IAM role. The default retention period is configurable at creation time from 0 to 3,650 days. Setting it to `0` applies no automatic retention lock, allowing backup software to manage per-object retention directly via the S3 Object Lock API.

Encryption
:   All vault data is encrypted at rest using AES-256. {{site.data.keyword.IBM_notm}} automatically creates and rotates encryption keys protected by FIPS 140-2 Level 3 certified HSMs.

Scoped HMAC credential pairs
:   When a vault is created, two scoped HMAC credential pairs are provisioned automatically: a write key for use by backup agents, and a read key for recovery operations and audit verification. Credentials are displayed securely for a limited time when viewed.

Time-windowed write access
:   When `enableTimeWindows` is set to `true`, vault write access is restricted to a defined daily time window specified by `accessStartTime` and `accessEndTime` (in `HH:MM` format, 24-hour clock). Writes are rejected outside this window, enabling organizations to implement strict ingestion schedules.

Context-Based Restrictions (CBR)
:   The `cbrRestrictions` configuration restricts vault access to specified network zones. You can authorize access from specific account VPCs (by VPC CRN) or from private IP CIDR ranges (RFC 1918). Public endpoints can be disabled by enforcing private-endpoint-only access.

Immutable configuration
:   Vault configuration is permanent and cannot be changed after creation. Review all settings carefully before provisioning. No update API is available in the Beta release.

Asynchronous provisioning
:   Vault creation is an asynchronous operation. The API returns HTTP `202 Accepted` with an initial status of `provisioning`. You can poll the vault status endpoint to determine when the vault transitions to `active`.

## Regional availability
{: #cyber-vault-regions}

IBM Cloud Cyber Vault is currently available in the following region:

| Region | Location | API endpoint |
| ------ | -------- | ------------ |
| `us-south` | Dallas, United States | `https://solutions.private.cloud-object-storage.cloud.ibm.com/v1` |
{: caption="IBM Cloud Cyber Vault regional availability" caption-side="bottom"}

Support for additional {{site.data.keyword.cloud_notm}} regions will be enabled in a future release. All vault API requests must target `us-south` at this time. Attempting to specify any other value for the `region` field in a create request returns a `400 Bad Request` error.

All vault data is accessible only through **private** and **direct** S3 endpoints. No public endpoint is exposed for Cyber Vault buckets.

## Prerequisites
{: #cyber-vault-prereqs}

Before you create an IBM Cloud Cyber Vault, ensure that you have the following:

- An active **{{site.data.keyword.cloud_notm}} account** with the ability to create {{site.data.keyword.cloud_notm}} resources.
- An existing **{{site.data.keyword.cos_full_notm}} instance** (Standard plan) in the `us-south` region, or the IAM permissions that are required for the Cyber Vault service to create one on your behalf if no `cosInstanceId` is supplied.
- **IAM permissions**:
   - To list all Cyber Vaults in the UI, you must have the **Cyber Vault Reader** role at the account level.
   - To create a Cyber Vault with automatic HMAC credential provisioning, you must have the **Cyber Vault Manager** role at the account level and the **Editor** or **Administrator** role on the target {{site.data.keyword.cos_full_notm}} instance.
- **VPC or network zone information**: The CRNs of the VPCs or the private IP CIDR ranges from which the vault will be accessed. Network isolation is required for all vault creation requests.

## Vault statuses
{: #cyber-vault-statuses}

The following table describes the possible vault states during and after provisioning.

| Status | Meaning |
| ------ | ------- |
| `provisioning` | The vault is being set up. Credentials and endpoints are not yet available. |
| `active` | The vault is ready. S3 endpoints, HMAC credentials, and all security policies are active. |
| `failed` | Provisioning encountered an error. An error message is included in the vault details. |
| `deleting` | Vault deletion has been initiated. |
{: caption="Cyber Vault statuses" caption-side="bottom"}

## How Cyber Vault works
{: #cyber-vault-how-it-works}

The following end-to-end flow describes how IBM Cloud Cyber Vault protects your data:

1. **Provisioning**: You create a Cyber Vault by specifying your {{site.data.keyword.cos_full_notm}} instance, region, retention configuration, encryption settings, and access controls. The {{site.data.keyword.cos_full_notm}} Solutions service provisions a dedicated {{site.data.keyword.cos_full_notm}} bucket with all protective controls applied automatically. Two scoped HMAC credential pairs (write and read) are generated at this time.

2. **Data ingestion (write window)**: Authorized write identities — backup agents that use the provisioned write HMAC key — write data to the vault during the permitted time window (if `enableTimeWindows` is enabled). Each object receives a compliance-mode Object Lock retention tag immediately on write.

3. **Immutability enforcement**: Once written, no identity — including account administrators or the vault owner — can delete or modify the locked object until its retention period expires. This is enforced by the underlying {{site.data.keyword.cos_full_notm}} Object Lock compliance mode and cannot be bypassed.

4. **Network isolation**: CBR rules help ensure that vault traffic flows only through authorized VPCs or private networks. Only private and direct endpoints are available; no public endpoint is exposed.

5. **Recovery**: Authorized read identities use the provisioned read HMAC key to access vault data through the vault's private or direct endpoint. Read access is not time-windowed; it is controlled solely by the `readIdentities` configuration.

6. **Lifecycle**: Objects are automatically unlocked after their retention period expires, at which point they can be deleted as part of standard lifecycle management. You cannot delete a vault that still contains locked objects.

## Next steps
{: #cyber-vault-next-steps}

- [Onboarding your account to the IBM Cloud Cyber Vault Beta program](/docs/cloud-object-storage?topic=cloud-object-storage-cyber-vault-onboarding) to request access before provisioning your first vault.
- [Provisioning an IBM Cloud Cyber Vault](/docs/cloud-object-storage?topic=cloud-object-storage-cyber-vault-provisioning) to create your first vault by using the {{site.data.keyword.cloud_notm}} console or the COS Solutions API.
- Review the [Object Lock documentation](/docs/cloud-object-storage?topic=cloud-object-storage-object-lock) to understand compliance-mode retention behavior.
- Review the [Context-Based Restrictions tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-cbr-tutorial) to understand how CBR network zones are configured.
- Review [{{site.data.keyword.keymanagementserviceshort}} for COS encryption](/docs/cloud-object-storage?topic=cloud-object-storage-kp-encrypt-bucket) if you plan to use customer-managed encryption keys.
