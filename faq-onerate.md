---

copyright:
  years: 2017, 2024, {CURRENT_YEAR}]
lastupdated: "2025-07-01"

keywords: faq, frequently asked questions, object storage

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - One-Rate plans
{: #faq-onerate}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## What is the difference between a Standard and One-Rate plan?
{: #faq-onerate-diff}
{: faq}

* Standard plan is our most popular public Cloud pricing plan, that meets the requirements of majority of the enterprise workloads. The Standard plan is best suited for workloads that have large amount of storage and relatively small Outbound bandwidth (Outbound bandwidth < 20% of Storage capacity). The plan offers flexible choices for storage class based on data access patterns (lower the cost, the less frequently data is accessed). The Standard plan bills for every stored capacity ($/GB/month), Outbound bandwidth ($/GB), class A ($/1,000), class B ($/10,000) and retrieval ($/GB) metrics, where applicable.

* One-Rate plan is suited for active workloads with high API requests or large amounts of Outbound bandwidth. Typical workloads belong to large enterprises and ISV's which may have sub-accounts with multiple divisions/departments or end-users. The plan offers a predictable TCO with an all-inclusive flat monthly charge ($/GB/month) that includes capacity, and built-in allowances for Outbound bandwidth and Operational requests. The built-in allowances for Outbound bandwidth and Operational requests (Class A, Class B) depend on the monthly stored capacity. There is no data retrieval charge.

## How are the allowance thresholds (for Outbound bandwidth, class A and class B) calculated for the One-Rate plan?
{: #faq-onerate-what}
{: faq}

For each of the One-Rate plan pricing regions (North America, Europe, South America, and Asia Pacific), the total aggregated Storage capacity across all instances (within a region) is used to determine the allowance thresholds.

- Outbound bandwidth: No charge if Outbound bandwidth ≤ 100% of Storage capacity in GB, then list prices apply ($0.05/GB for North America and Europe, $0.08/GB for South America and Asia Pacific). For example, for an account with aggregated monthly Storage capacity of 100 GB in North America, there are no Outbound bandwidth charges up to 100 GB of transferred data within that month.

- Class A: No charge if class A requests ≤ 1000 x Storage capacity in GB, then list prices apply ($0.0052/1000). For example, for an account with aggregated monthly Storage capacity of 100 GB in North America, there are no Outbound bandwidth charges up to 100,000 class A requests that month in North America.

- Class B: No charge for class B ≤ 5000 x Storage (GB), then list prices apply ($0.0042/10,000). For example, for an account with aggregated monthly Storage capacity of 100 GB in North America, there are no Outbound bandwidth charges up to 500,000 class A requests that month in North America.

## Which storage classes are supported in the One-Rate plan?
{: #faq-onerate-class}
{: faq}

There is only one storage class available in the One-Rate plan: One-Rate Active.

## What are the One-Rate pricing regions?
{: #faq-onerate-regions}
{: faq}

There are four One-Rate pricing regions: North America, Europe, South America and Asia Pacific. The following Regional and Single Sites are included in the four One-Rate pricing regions:

North America:

* Regional: `us-south`, `us-east`, `ca-tor`
* Single Sites: `mon01`, `sjc04`

Europe:

* Regional: `eu-gb`, `eu-de`
* Single Sites: `ams03`, `mil01`, `par01`

South America:

* Regional: `br-sao`

Asia Pacific:

* Regional: `au-syd`, `jp-osa`, `jp-tok`
* Single Sites: `che01`, `sng01`

## Is the pricing different for the four One-Rate pricing regions?
{: #faq-onerate-price-diff}
{: faq}

The pricing rates are same for North America and Europe, similarly for South America and Asia Pacific. See [One-Rate pricing plan details](/objectstorage/create#pricing).

## Are all Cloud Object Storage features available in the One-Rate Plan?
{: #faq-onerate-features}
{: faq}

All Cloud Object Storage features (Versioning, Archive, Replication, WORM, Expiration, and so on) are available in the One-Rate Plan.

## Is the One-Rate plan available in all Cloud Object Storage regions?
{: #faq-onerate-avail-reg}
{: faq}

The One-Rate plan is available in all Cloud Object Storage Regional and Single sites.

## Can I configure a lifecycle policy to archive or expire my objects in the One-Rate Active buckets?
{: #faq-onerate-lifecycle}
{: faq}

Yes, you can set a lifecycle policy to archive objects from the One-Rate Active buckets to either Archive (restore ≤ 12 hours) or Accelerated Archive (restore ≤ 2 hours). Similarly, you can set expiration rules to expire objects based on the date of creation of the object, or a specific date.

## What pricing rates will apply to objects archived from the One-Rate Active buckets?
{: #faq-onerate-archive-rates}
{: faq}

For Archive and Accelerated Archive, standard pricing applies based on the bucket location. For example, a bucket created in `us-south` will incur archive pricing for `us-south`. Similarly, a bucket in `ca-tor` will incur archive pricing for `ca-tor`.

## Can I move my existing buckets from Standard plan to One-Rate plan?
{: #faq-onerate-move}
{: faq}

Existing buckets in the Standard plan cannot be moved to the One-rate plan. Clients must first enroll in the One-Rate plan, create a new service instance and new buckets before data can be populated.

## Can I upgrade my Cloud Object Storage Lite plan instance to One-Rate plan?
{: #faq-onerate-upgrade}
{: faq}

No, Lite Plan instances can only be upgraded to the Cloud Object Storage Standard plan.

## Are there any minimum object size or minimum duration requirements for objects stored with the One-Rate plan?
{: #faq-onerate-minmax}
{: faq}

There are no minimum object size or minimum duration requirements for the One-Rate plan.

## What is the cost of data retrieval from One-Rate Active buckets?
{: #faq-onerate-retrieve-cost}
{: faq}

There is no data retrieval charge for the One-Rate Active buckets.

## What happens if I exceed my monthly allowance for Outbound bandwidth and Operational requests?
{: #faq-onerate-exceed-allowance}
{: faq}

For any usage (Outbound bandwidth or Operational requests) that exceeds the allowance determined by aggregated monthly capacity, a small overage fee applies based on the One-Rate pricing regions. See [One-Rate pricing plan details](/objectstorage/create#pricing).

## Is the overage pricing tiered for Outbound bandwidth and Operational requests?
{: #faq-onerate-tiers}
{: faq}

No, the overage pricing for the One-Rate plan has flat rates regardless of excess usage. See [One-Rate pricing plan details](/objectstorage/create#pricing).

## I already have a Cloud Object Storage Standard plan in my IBM Cloud account. Can I add a One-Rate plan for my new workloads?
{: #faq-onerate-new-workload}
{: faq}

Yes, you can add a One-Rate plan to your existing account in addition to the Standard plan. If you are a new to Cloud Object Storage, you can add either Standard or One-Rate plan (or both) based on your workload requirements.
