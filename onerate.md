---

copyright:
  years: 2022, 2024, {CURRENT_YEAR}]
lastupdated: "2025-07-08"

keywords: data, cost, pricing, isv

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

#  Choosing a One-Rate plan
{: #onerate}

The One-Rate plan offers a predictable Total Cost of Ownership (TCO) through a flat monthly rate per GB of storage ($/GB). This all-inclusive rate covers storage capacity, outbound bandwidth, and API requests (Class A and B). One-Rate is ideal for hot, active workloads with high API activity, or outbound data transfer relative to stored capacity.
{: shortdesc}

The built-in monthly allowances for API operations and egress eliminate cost variability due to data access patterns. These allowances depend on the monthly stored capacity. There is no data retrieval charge. The One-Rate plan has four pricing regions: North America, Europe, South America, and Asia Pacific. Furthermore, the plan aggregates billing metrics (storage capacity, outbound bandwidth, and operational requests) across multiple instances within the One-Rate pricing region for determining the allowances (the higher the aggregated storage capacity within a region, the higher the allowances for outbound bandwidth and operational requests for that region).

## Why use a One-Rate plan?
{: #onerate-why}

- One flat rate per GB per month – Includes storage, API, egress, and retrieval with no separate charges
- Automatic volume discount– Rate drops as stored capacity grows, applied to entire capacity
- Predictable and lower monthly TCO for workloads with high activity and outbound bandwidth.
- Regional aggregation – Combine costs across departments, subaccounts, and instances spread across different locations within a region for lower rates
- Competitive customer pricing – Known, fixed costs enable attractive flat-rate offerings to users
- Built-in allowances that scale — API and egress allowances grow with total storage capacity
- Global availability — Available across four pricing regions: North America, Europe, Latin America, and Asia Pacific

## Terminology
{: #onerate-terminology}

- **Egress**: Outbound data read via public endpoints (charged as GB/month)
- **Allowances**: Bundled thresholds for Class A/B requests and egress based on stored capacity

## Use cases
{: #onerate-use-case}

One-Rate is a strong fit for production-scale workloads across various industries. It enables teams to simplify billing and optimize value by bundling API and egress charges with capacity.

**Enterprise Data Lakehouse**:
For analytics and AI workloads with frequent API traffic. Supports scalable, durable storage that integrates with watsonx and other IBM AI services.
→ *Best fit: Hot, active workloads needing predictable performance and pricing.*

**Cloud Native App Storage**:
For mobile, web, or streaming applications with fluctuating access and moderate egress.
→ *Best fit: Cloud-native apps with hot, active data pipelines.*

**Media Content Storage**:
For content-heavy ISVs and media providers needing flat pricing for high API and egress usage.
→ *Best fit: Large media workloads benefiting from bundled transfer and API savings.*

One-Rate plans are generally best for active workloads. For cold storage, long-term backup, or workloads with minimal egress, the Standard plan can be more cost-effective.
{: note}




## Getting started with One-Rate plans
{: #onerate-gs}

One-Rate instances are available in Regional and Single Data Center locations but are not available in Cross Region locations. There are four pricing tiers based on location:
- North America: `us-south`, `us-east`, `ca-tor`, `mon01`, `sjc04`
- Europe:  `eu-de`, `eu-gb`, `eu-es`, `ams03`, `mil01`, `par01`
- Asia: `au-syd`, `jp-osa`, `jp-tok`, `che01`, `sng01`
- South America: `br-sao`

All buckets in a One-Rate plan instance **must** use a new `active` storage class specific to One-Rate instances.

One-Rate plan instances are aggregated and billed at the IBM Cloud account level based on average end-of-month usage. For detailed information and current pricing, [please review the detailed cost tables](/objectstorage/create#pricing).

Unlike Standard plan instances, One-Rate instances provide allowances for [Class A and B request charges](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-request-classes) as well as egress charges. The thresholds for the allowances are dependent on total storage capacity.

It is **not** possible to convert an instance created under a One-Rate plan to a Standard plan, or vice-versa.
{: important}

### How allowances are calculated
{: #onerate-billing}

One-Rate plans use an all-inclusive flat monthly rate, which includes capacity, operational requests, and outbound bandwidth. The built-in allowances for outbound bandwidth are determined by the total capacity.

> Total Monthly Cost = Capacity Cost + API Cost (if # of API > allowance) + Bandwidth cost (if Bandwidth > allowance)

- Class A allowance: Number of Class A Requests < 1000 x Storage (GB)
- Class B allowance: Number of Class B Requests < 5000 x Storage (GB)
- Bandwidth allowance: Bandwidth (GB) < Storage (GB)

Archive is supported but Restore charges are **not** included in the One-Rate allowances.
{: note}

### How to provision a One-Rate instance
{: #onerate-provision}

A One-Rate instance is specified at the point of provisioning, similar to Standard plan instance.

1. Log in to [the console](https://cloud.ibm.com/){: external}.
2. Navigate to the catalog, by clicking **Catalog** in the navigation bar.
3. Look for the **Object Storage** tile in the storage section and select it.
4. Select **IBM Cloud** from the "Choose an Infrastructure" section.
5. Select **One-Rate** from the plans.
6. Choose a name, resource group, and any desired tags.
7. Click **Create** and you're automatically redirected to your new instance.

### Special provisioning codes
{: #onerate-codes}

All buckets created in a One-Rate plan must use a [specific provisioning code](/docs/cloud-object-storage?topic=cloud-object-storage-classes) (also known as a storage class or location constraint).

| Location   | Location Constraint |
|------------|---------------------|
| `us-south` | `us-south-onerate_active`   |
| `us-east`  | `us-east-onerate_active`    |
| `ca-tor`   | `ca-tor-onerate_active`     |
| `mon01`    | `mon01-onerate_active`      |
| `sjc04`    | `sjc04-onerate_active`      |
{: class="simple-tab-table"}
{: caption="Location constraint - North America" caption-side="bottom"}
{: #oneratecodes1}
{: tab-title="North America"}
{: tab-group="One-Rate Codes"}

| Location | Location Constraint |
|----------|---------------------|
| `eu-de`  | `eu-de-onerate_active`      |
| `eu-gb`  | `eu-de-onerate_active`      |
| `eu-es`  | `eu-es-onerate_active`      |
| `ams03`  | `ams03-onerate_active`      |
| `mil01`  | `mil01-onerate_active`      |
| `par01`  | `par01-onerate_active`      |
{: class="simple-tab-table"}
{: caption="Location constraint - Europe" caption-side="bottom"}
{: #oneratecodes2}
{: tab-title="Europe"}
{: tab-group="One-Rate Codes"}

| Location | Location Constraint |
|----------|---------------------|
| `au-syd`  | `au-syd-onerate_active`      |
| `jp-tok`  | `jp-tok-onerate_active`      |
| `jp-osa`  | `jp-osa-onerate_active`      |
| `sng01`  | `sng01-onerate_active`      |
| `che01`  | `che01-onerate_active`      |
{: class="simple-tab-table"}
{: caption="Location constraint - Asia" caption-side="bottom"}
{: #oneratecodes3}
{: tab-title="Asia"}
{: tab-group="One-Rate Codes"}

| Location | Location Constraint |
|----------|---------------------|
| `br-sao`  | `br-sao-onerate_active`      |
{: class="simple-tab-table"}
{: caption="Location constraint - South America" caption-side="bottom"}
{: #oneratecodes4}
{: tab-title="South America"}
{: tab-group="One-Rate Codes"}



## What next
{: #onerate-next}

Additional information can be found in [the FAQs](/docs/cloud-object-storage?topic=cloud-object-storage-faq-onerate), or in the [provisioning storage](/docs/cloud-object-storage?topic=cloud-object-storage-provision) topics.
