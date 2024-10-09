---

copyright:
  years: 2022, 2024
lastupdated: "2024-05-01"

keywords: data, cost, pricing, isv

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

#  Choosing a One Rate plan
{: #onerate}

The One Rate plan offers a predictable cost of ownership with an all-inclusive flat monthly charge ($/GB/month) that includes capacity, and built-in allowances for outbound bandwidth and operational requests. The One Rate plan is best suited for active workloads with large amounts of outbound bandwidth relative to storage capacity.
{: shortdesc}

The built-in allowances for outbound bandwidth and operational requests (Class A, Class B) depend on the monthly stored capacity. There is no data retrieval charge. The One Rate plan has four pricing regions: North America, Europe, South America, and Asia Pacific. Furthermore, the plan aggregates billing metrics (storage capacity, outbound bandwidth and operational requests) across multiple instances within the One Rate pricing region for determining the allowances (the higher the aggregated storage capacity within a region, the higher the allowances for outbound bandwidth and operational requests for that region).

## Why use a One Rate plan?
{: #onerate-why}

- Predictable and lower monthly TCO (total cost of ownership) for workloads with high levels of outbound bandwidth to capacity ratios (>20%).
- One Rate plans provide account-level billing that aggregates storage capacity across service instances by region.
- A flat capacity rate with built-in allowances for data access and egress offers a more predictable cost regardless of fluctuating usage patterns.

## Terminology
{: #onerate-terminology}

**Egress**: The measure of outbound bandwidth (GB) read over the public endpoints.

## Who should use a One Rate plan?
{: #onerate-who}

One Rate plan instances are more expensive when it comes to storage capacity costs, but much less expensive when taking into account egress charges.  You should consider using a One Rate instance if:

1. You are a large enterprise or ISV, and most of the data being stored in Object Storage is constantly being read over the public endpoints.
2. You are reading large files from outside of IBM Cloud - for example in post-production film editing, satellite imaging, or music production.

Most workloads, such as for backups/long-term storage, data analysis using IBM Cloud resources, or for small files (such as PNGs for websites) are better served by a Standard plan. One Rate plans are generally best for workloads where more 20% of the total storage is consistently read over the public endpoints each month.
{:note}

## Getting started with One Rate plans
{: #onerate-gs}

One Rate instances are available in Regional and Single Data Center locations, but are not available in Cross Region locations. There are four pricing tiers based on location:
- North America: `us-south`, `us-east`, `ca-tor`, `mon01`, `sjc04`
- Europe:  `eu-de`, `eu-gb`, `eu-es`, `ams03`, `mil01`, `par01`
- Asia: `au-syd`, `jp-osa`, `jp-tok`, `che01`, `sng01`
- South America: `br-sao`

All buckets in a One Rate plan instance **must** use a new `active` storage class specific to One Rate instances.

One Rate plan instances are aggregated and billed at the IBM Cloud account level based on average end-of-month usage. For detailed information and current pricing, [please review the detailed cost tables](/objectstorage/create#pricing).

Unlike Standard plan instances, One Rate instances provide allowances for [Class A and B request charges](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-request-classes) as well as egress charges.  The thresholds for the allowances are dependant on total storage capacity.

It is **not** possible to convert an instance created under a One Rate plan to a Standard plan, or vice-versa.
{:important}

### How allowances are calculated
{: #onerate-billing}

One Rate plans use an all-inclusive flat monthly rate which includes capacity, operational requests, and outbound bandwidth.  The built-in allowances for outbound bandwidth are determined by the total capacity.

> Total Monthly Cost = Capacity Cost + API Cost (if # of API > allowance) + Bandwidth cost (if Bandwidth > allowance)

- Class A allowance: Number of Class A Requests < 100 x Storage (GB)
- Class B allowance: Number of Class B Requests < 1000 x Storage (GB)
- Bandwidth allowance: Bandwidth (GB) < Storage (GB)

Archive is supported but Restore charges are **not** included in the One Rate allowances
{: note}

### How to provision a One Rate instance
{: #onerate-provision}

A One Rate instance is specified at the point of provisioning, similar to a Lite or Satellite instance.

1. Log in to [the console](https://cloud.ibm.com/){: external}.
2. Navigate to the catalog, by clicking **Catalog** in the navigation bar.
3. Look for the **Object Storage** tile in the storage section and select it.
4. Select **IBM Cloud** from the "Choose an Infrastructure" section.
5. Select **One Rate** from the plans.
6. Choose a name, resource group, and any desired tags.
7. Click **Create** and you're automatically redirected to your new instance.

### Special provisioning codes
{: #onerate-codes}

All buckets created in a One Rate plan must use a [specific provisioning code](/docs/cloud-object-storage?topic=cloud-object-storage-classes) (also known as a storage class or location constraint).

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
{: tab-group="One Rate Codes"}

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
{: tab-group="One Rate Codes"}

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
{: tab-group="One Rate Codes"}

| Location | Location Constraint |
|----------|---------------------|
| `br-sao`  | `br-sao-onerate_active`      |
{: class="simple-tab-table"}
{: caption="Location constraint - South America" caption-side="bottom"}
{: #oneratecodes4}
{: tab-title="South America"}
{: tab-group="One Rate Codes"}

## Billing examples
{: #onerate-billing-examples}

These costs are examples provided to illustrate the mechanics of the billing and are not reflective of actual rates, which can [be found here](/objectstorage/create#pricing).
{: note}

### Predictable TCO pricing example
{: #onerate-tco}

Some workloads see steadily increasing traffic as business grows - which can create some billing surprises as egress charges grow as well.  A One Rate plan can cap those costs until thresholds are crossed.  For example, an account with 10 TB of storage might might see consistent growth until the amount of data being read outside of the IBM Cloud exceeds the amount of data being stored.

| Month | Capacity (GB) | Egress (GB) | Capacity:Egress ratio | Standard cost | One Rate cost |
|-------|---------------|-------------|-----------------------|---------------|---------------|
| 1     | 10 TB         | 500 GB      | 5%                    | $280          | $400          |
| 2     | 10 TB         | 1 TB        | 10%                   | $325          | $400          |
| 3     | 10 TB         | 2 TB        | 20%                   | $416          | $400          |
| 4     | 10 TB         | 5 TB        | 50%                   | $687          | $400          |
| 5     | 10 TB         | 10 TB       | 100%                  | $1,139        | $400          |
| 6     | 10 TB         | 15 TB       | 150%                  | $1,591        | $652          |
| Total |               |             |                       | **$4,438**    | **$2,652**    |
{: caption="Predictable TCO pricing" caption-side="bottom"}

### Aggregation pricing example
{: #onerate-aggregate}

Imagine a large enterprise account called "Rainbow Co.".  It has a number of subsidiary accounts, such as "Blue", and "Green".  Each of these accounts has dozens (or more) Object Storage instances spread out across different regions.  Some have large volumes of storage that is rarely read, while others have smaller volumes but very high rates of egress.

Blue (`us-east`, `us-south`):
| Metric     | Usage  | Standard Cost |
|------------|--------|---------------|
| Storage    | 100 TB | $2,300        |
| Class A    | 100    | $0            |
| Class B    | 100    | $0            |
| Egress     | 100 GB | $9            |
| Total cost |        | **$2,309**    |
{: caption="Pricing example for Blue region." caption-side="bottom"}

Green (`eu-de`, `milO1`):
| Metric     | Usage       | Standard Cost |
|------------|-------------|---------------|
| Storage    | 100 GB      | $2            |
| Class A    | 11,000,000  | $55           |
| Class B    | 110,000,000 | $44           |
| Egress     | 120 TB      | $10,800       |
| Total cost |             | **$10,901**   |
{: caption="Pricing example for Green region." caption-side="bottom"}

Rainbow Co. (Blue and Green):
| Metric     | Total usage | Total Standard Cost | Allowance   | Billable Quantity | One Rate Cost |
|------------|-------------|---------------------|-------------|-------------------|---------------|
| Storage    | 100 TB      | $2,302              | 0 GB        | 100 TB            | $4,004        |
| Class A    | 11,000,100  | $55                 | 10,010,000  | 990,100           | $5            |
| Class B    | 110,000,100 | $44                 | 100,100,000 | 9,900,100         | $4            |
| Egress     | 120 TB      | $10,809             | 100 TB      | 20 TB             | $1,000        |
| Total cost |             | **$13,210**         |             |                   | **$5,013**    |
{: caption="Pricing example for the two regions combined." caption-side="bottom"}

Note that the One Rate cost is significantly lower due to the reduced cost for egress.  Also note that rather than dozens of individual invoices (one for each service instance), there will only be four invoices - one for each location used.

## What next
{: #onerate-next}

Additional information can be found in [the FAQs](/docs/cloud-object-storage?topic=cloud-object-storage-faq-onerate), or in the [provisioning storage](/docs/cloud-object-storage?topic=cloud-object-storage-provision) topics.
