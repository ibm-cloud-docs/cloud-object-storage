---

copyright:
  years: 2022   
lastupdated: "2022-08-31"

keywords: data, cost, pricing, isv

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

#  Choosing a One Rate plan
{: #onerate}

Workloads that involve consistently high volumes of data egress, such as media servers, can benefit from using a One Rate plan. 
{: shortdesc}

## Why use a One Rate plan?
{: #onerate-why}

- One-Rate plans provide account-level billing that aggregates storage capacity across service instances by region. 
- A flat capacity rate with built-in allowances for data access and egress offers a more predictable cost regardless of fluctuating usage patterns.

## What is One Rate?
{: #onerate-what}

One Rate plan instances are significantly more expensive when it comes to storage costs, but much less expensive when taking into account egress charges.  You should consider using a One Rate instance if:

1. You are a large enterprise or ISV, and most of the data being stored in Object Storage is constantly being read over the public endpoints.
2. Your storage is being used to read large files outside of IBM Cloud, such as in post-production film editing, satellite imagery, or music production.

Most workloads, such as for backups/long-term storage, data analysis using IBM Cloud resources, or for small files (such as PNGs for websites) are better served by a Standard plan. One Rate plans are generally best for workloads where more 20% of the total storage is consistently read over the public endpoints each month.
{:note}

One Rate instances are available in Regional and Single Data Center locations, but are not available in Cross Region locations. There are four pricing tiers based on location:
- North America: `us-south`, `us-east`, `ca-tor`, `mon01`, `sjc04`
- Europe:  `eu-de`, `eu-gb`, `ams03`, `mil01`, `par01`
- Asia: `au-syd`, `jp-osa`, `jp-tok`, `che01`, `sng01`
- South America: `br-sao`

All buckets in a One Rate instance **must** use a new `active` storage class specific to One Rate instances.

One Rate instances are aggregated and billed at the IBM Cloud account level based on average end-of-month usage. For detailed information and current pricing, [please review the detailed cost tables](https://cloud.ibm.com/objectstorage/create#pricing).

Unlike Standard instances, One Rate instances provide free tiers for [Class A and B request charges](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-request-classes) as well as egress charges.  The thresholds for the free tiers are dependant on total storage capacity.

It is **not** possible to convert an instance created under a One Rate plan to a Standard plan.
{:important}

## How to provision a One Rate instance
{: #onerate-provision}

A One Rate instance is specified at the point of provisioning, similar to a Lite or Satellite instance.   

1. Log in to [the console](https://cloud.ibm.com/){: external}.
2. Navigate to the catalog, by clicking **Catalog** in the navigation bar.
3. Look for the **Object Storage** tile in the storage section and select it.
4. Select **IBM Cloud** from the "Choose an Infrastructure" section.
5. Select **One Rate** from the plans.
6. Choose a name, resource group, and any desired tags.
7. Click **Create** and you're automatically redirected to your new instance.

## How free tiers are calculated
{: #onerate-billing}

Total Monthly Cost = Capacity Cost + API Cost (if # of API > free tier) + Bandwidth cost (if Bandwidth > free tier)

- Class A Free Tier: Number of Class A Requests < 100 x Storage (GB)
- Class B Free Tier: Number of Class B Requests < 1000 x Storage (GB)
- Bandwidth Free Tier: Bandwidth (GB) < Storage (GB)

Archive is supported but Restore charges are **not** included in the One Rate free tiers
{: note}

### Pricing example

Imagine a large enterprise account called "Rainbow Co.".  It has a number of subsidiary accounts, such as "Blue", and "Green".  Each of these accounts has dozens (or more) Object Storage instances spread out across different regions.  Some have large volumes of storage that is rarely read, while others have smaller volumes but very high rates of egress.  Note that these costs are examples provided to illustrate the mechanics of the billing and are not reflective of actual rates, which can [be found here](https://cloud.ibm.com/objectstorage/create#pricing).

Blue (`us-east`, `us-south`):
| Metric     | Usage  | Standard Cost |
|------------|--------|---------------|
| Storage    | 100 TB | $2,300        |
| Class A    | 100    | $0            |
| Class B    | 100    | $0            |
| Egress     | 100 GB | $9            |
| Total cost |        | **$2,309**    |

Green (`eu-de`, `milO1`):
| Metric     | Usage       | Standard Cost |
|------------|-------------|---------------|
| Storage    | 100 GB      | $2            |
| Class A    | 11,000,000  | $55           |
| Class B    | 110,000,000 | $44           |
| Egress     | 120 TB      | $10,800       |
| Total cost |             | **$10,901**   |


Rainbow Co. (Blue and Green):
| Metric     | Total usage | Total Standard Cost | Allowance   | Billable Quantity | One Rate Cost |
|------------|-------------|---------------------|-------------|-------------------|---------------|
| Storage    | 100 TB      | $2,302              | 0 GB        | 100 TB            | $4,004        |
| Class A    | 11,000,100  | $55                 | 10,010,000  | 990,100           | $5            |
| Class B    | 110,000,100 | $44                 | 100,100,000 | 9,900,100         | $4            |
| Egress     | 120 TB      | $10,809             | 100 TB      | 20 TB             | $1,000        |
| Total cost |             | **$13,210**         |             |                   | **$5,013**    |

Note that the One Rate cost is significantly lower due to the reduced cost for egress.  Also note that rather than dozens of individual invoices (one for each service instance), there will only be four invoices - one for each location used.

## Creating buckets in a One Rate plan

All buckets created in a One Rate plan must use a specific provisioning code (also known as a storage class or location constraint).

North America:

| Location   | Location Constraint |
|------------|---------------------|
| `us-south` | `us-south-active`   |
| `us-east`  | `us-east-active`    |
| `ca-tor`   | `ca-tor-active`     |
| `mon01`    | `mon01-active`      |
| `sjc04`    | `sjc04-active`      |

Europe:

| Location   | Location Constraint |
|------------|---------------------|
| `eu-de` | `eu-de-active`   |
| `eu-gb`  | `eu-de-active`    |

North America:

