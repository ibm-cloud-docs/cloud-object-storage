---

copyright:
  years: 2026
lastupdated: "2026-08-27"

keywords: cyber vault, COS cyber vault, onboarding, beta program, support case, account registration, cyber resilience

subcollection: cloud-object-storage

content-type: howto

---

{{site.data.keyword.attribute-definition-list}}

# Onboarding your account to the IBM Cloud Cyber Vault Beta program
{: #cyber-vault-onboarding}

IBM Cloud Cyber Vault is in **Beta**. This service is provided solely for evaluation and testing purposes for a limited time. No warranties, SLAs, or support commitments apply during Beta. Beta features are not intended for production use. Review the [IBM terms of service](https://www.ibm.com/support/customer/csol/terms/?id=Z126-6567&cc=us&lc=en){: external} before using this solution.
{: beta}

Access to the IBM Cloud Cyber Vault Beta program is provided on request. To join the program, you must submit a support case from the {{site.data.keyword.cloud_notm}} console. After your account is reviewed and approved, the {{site.data.keyword.IBM_notm}} team configures Cyber Vault access for your account and contacts you with next steps.
{: shortdesc}

## Before you begin
{: #cyber-vault-onboarding-prereqs}

Before you submit your onboarding request, gather the following information:

- Your **{{site.data.keyword.cloud_notm}} account ID**. You can find this value by clicking your account name in the {{site.data.keyword.cloud_notm}} console navigation bar, or by running `ibmcloud account list` with the {{site.data.keyword.cloud_notm}} CLI.
- Your **customer environment type**: Cloud Native or On-Premises.
- Your **primary use case**: for example, backup protection, multimedia, archive, or disaster recovery.
- If you are using Cyber Vault for a backup use case, the name of any **partner data protection software** you are using alongside Cyber Vault, such as Veeam, Commvault, Dell, Cohesity, or IBM Storage Defender.
- Your **estimated data volume** in terabytes (TB).
- Any **regulatory compliance requirements** that apply to your environment, such as FSCloud, SOC2, or HIPAA.
- Your contact information: name, company name, email address, and phone number.

## Submitting an onboarding request
{: #cyber-vault-onboarding-submit}

Submit your request by creating a support case in the {{site.data.keyword.cloud_notm}} console.

1. Log in to the [{{site.data.keyword.cloud_notm}} console](https://cloud.ibm.com){: external}.
2. In the console navigation bar, click the **Help** icon, then select **Support center**.
3. On the Support center page, click **Create a case**.
4. On the Category page, select **{{site.data.keyword.cos_full_notm}}**.
5. On the Topic page:
   - Set **Topic** to **{{site.data.keyword.cos_full_notm}} – Access/Security**.
   - Set **Subtopic** to **COS Cyber Vault**.
6. Click **Next** to proceed to the Details page.
7. In the **Subject** field, enter:

   ```
   Onboarding Request for IBM Cloud Cyber Vault Beta
   ```
   {: codeblock}

8. In the **Description** field, paste the following template and replace each placeholder with your specific information:

   ```
   Description:
   We are requesting assistance with onboarding our account for IBM Cloud Cyber Vault. Please provide the necessary steps and configuration to enable IBM Cloud Cyber Vault for our account.

   Required Information:
   Customer experience with IBM Cloud (New/Existing): [New or Existing]
   Account ID: [Insert Account ID]
   Customer Environment: [Cloud Native/On-premise]
   Customer Use Case: [Backup Protection, Multimedia, etc.]
   Additional Partner Data Protection Software used with Cyber Vault (if using for Backup use case, example: Veeam, Commvault, Dell, Cohesity, IBM Storage Defender etc.): [Insert software names or N/A]
   Expected Data Volume: [Insert estimated data volume in TB]
   Regulatory Compliance Use Case Requirements: [FSCloud, SOC2, HIPAA, or N/A]

   Contact Information:
   Name: [Insert Name]
   Company Name: [Insert Company Name]
   Email: [Insert Email]
   Phone: [Insert Phone Number]
   ```
   {: codeblock}

9. Click **Next** to review the case details, then click **Submit case**.

After the {{site.data.keyword.IBM_notm}} team processes your request, you receive a confirmation email with next steps and access instructions.

You can also submit your onboarding request directly at [https://cloud.ibm.com/unifiedsupport/cases/form](https://cloud.ibm.com/unifiedsupport/cases/form){: external}.
{: tip}

## What happens after you submit
{: #cyber-vault-onboarding-next}

After you submit your support case, the {{site.data.keyword.IBM_notm}} team:

1. Reviews your account and use case to confirm eligibility for the Cyber Vault Beta program.
2. Enables Cyber Vault access for your {{site.data.keyword.cloud_notm}} account.
3. Contacts you by email with confirmation and any additional configuration steps that are required for your environment.

Review the onboarding details carefully. Vault configuration is permanent and cannot be changed after a vault is provisioned. No fields can be edited after creation.
{: important}

After your account is enabled, you can proceed to create your first Cyber Vault. See [Provisioning an IBM Cloud Cyber Vault](/docs/cloud-object-storage?topic=cloud-object-storage-cyber-vault-provisioning) for detailed instructions.

## Next steps
{: #cyber-vault-onboarding-followup}

- [Provisioning an IBM Cloud Cyber Vault](/docs/cloud-object-storage?topic=cloud-object-storage-cyber-vault-provisioning) — step-by-step instructions for creating a vault by using the {{site.data.keyword.cloud_notm}} console or the COS Solutions API.
- [IBM Cloud Cyber Vault overview](/docs/cloud-object-storage?topic=cloud-object-storage-cyber-vault-overview) — learn about the key capabilities, security controls, and architecture of the Cyber Vault solution.
- Review the [Object Lock documentation](/docs/cloud-object-storage?topic=cloud-object-storage-object-lock) to understand compliance-mode retention behavior before you configure your vault.
