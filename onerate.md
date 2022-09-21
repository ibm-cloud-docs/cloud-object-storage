---

copyright:
  years: 2022   
lastupdated: "2022-08-31"

keywords: data, cost, pricing, reseller

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

#  Choosing a One Rate plan
{: #onerate}

Workloads that involve high volumes of data egress, such as media servers, can benefit from using a One Rate plan. 
{: shortdesc}

One-Rate plans provide account-level billing that aggregates storage capacity by region and across service instances. A flat capacity rate with built-in allowances for data access and egress offers a more predictable cost regardless of fluctuating usage patterns.

## What is One Rate?
{: #onerate-what}

One Rate plan instances are significantly more expensive when it comes to storage costs, but much less expensive when taking into account egress charges.  You should consider using a One Rate instance if:

1. Your are a storage reseller, and most of the data being stored in Object Storage is being read over the public endpoints.
2. Your storage is being used to read large files outside of IBM Cloud, such as in post-production film editing, satellite imagery, or music production.

Most workloads, such as for backups/long-term storage, data analysis using IBM Cloud resources, or for small files (such as PNGs for websites) are better served by a Standard plan. One Rate plans are generally best for workloads where more 20% of the total storage is read over the public endpoints each month.
{:important}

One Rate instances are available in Regional and Single Data Center locations, but are not available in Cross Region locations. There are four pricing tiers based on location:
- North America: `us-south`, `us-east`, `ca-tor`, `mon01`, `sjc04`
- Europe:  `eu-de`, `eu-gb`, `ams03`, `mil01`, `par01`
- Asia: `au-syd`, `jp-osa`, `jp-tok`, `che01`, `sng01`
- South America: `br-sao`


All buckets in a One Rate instance **must** use a new `active` storage class specific to One Rate instances.

One Rate instances are aggregated and billed at the IBM Cloud account level based on average end-of-month usage.

Unlike Standard instances, One Rate instances provide free tiers for [Class A and B request charges](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-request-classes) as well as egress charges.  The thresholds for the free tiers are dependant on total storage capacity.

It is **not** possible to convert an instance created under a One Rate plan to a Standard plan.

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

## Creating buckets in a One Rate plan

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

