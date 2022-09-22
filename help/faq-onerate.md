---

copyright:
  years: 2017, 2022
lastupdated: "2021-11-29"

keywords: faq, frequently asked questions, object storage

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - One Rate plans
{: #faq-onerate}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## How are the allowance thresholds (for Outbound bandwidth, class A and class B) calculated for the One-Rate plan??
{: #faq-onerate-what}
{: faq}

For each of the One-Rate plan pricing regions(North America, Europe, South America,and Asia Pacific), the total aggregated Storage capacity across all instances (within a region) is used to determine the allowance thresholds.

- Outbound bandwidth: No charge if Outbound bandwidth ≤ 100% of Storage capacity in GB, then list prices apply ($0.05/GBfor North America and Europe, $0.08/GB for South America and Asia Pacific). For example, for an account with aggregated monthly Storage capacity of 100 GB in North America, there are no Outbound bandwidth charges up to 100 GBof transferred data within that month.

- Class A: No charge if class A requests ≤ 100 x Storage capacity in GB, then list prices apply ($0.005/1000). For example, for an account with aggregated monthly Storage capacity of 100 GB in North America, there are no Outbound bandwidth charges up to 10,000 class A requests that month in North America.

- Class B: No charge for class B ≤ 1000 x Storage(GB), then list prices apply ($0.004/1000)For example, for an account with aggregated monthly Storage capacity of 100 GB in North America, there are no Outbound bandwidth charges up to 100,000 class A requests that month in North America.

