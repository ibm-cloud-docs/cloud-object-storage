---

copyright:
  years: 2017, 2025
lastupdated: "2025-07-01"

keywords: administration, billing, platform, aspera, invoices, pricing, bucket backup

subcollection: cloud-object-storage


---
{:external: target="_blank" .external}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Billing
{: #billing}

Information on pricing can be found at [{{site.data.keyword.cloud}}](https://www.ibm.com/products/cloud-object-storage/pricing/){: external}.
{: shortdesc}

## Invoices
{: #billing-invoices}

Find your account invoices at **Manage** > **Billing and Usage** in the navigation menu.

Under a Standard plan, service instance receives a single bill. If you need separate billing for different sets of buckets, then creating multiple instances is necessary.

For each storage class, billing is based on aggregated usage across all buckets at the instance level. For example, for Smart Tier, the billing is based on usage across all Smart Tier buckets in a given instance - not on the individual buckets.
{: important}

## {{site.data.keyword.cos_full_notm}} pricing
{: #billing-pricing}

Storage costs for {{site.data.keyword.cos_full}} are determined by the average monthly stored volume of data, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system.

Infrastructure offerings are connected to a three-tiered network, segmenting public, private, and management traffic. Infrastructure services can transfer data between one another across the private network at no cost. Infrastructure offerings (such as bare metal servers, virtual servers, and cloud storage) connect to other applications and services in the {{site.data.keyword.cloud_notm}} Platform catalog (such as Watson services) across the public network, so data transfer between those two types of offerings is metered and charged at standard public network bandwidth rates.
{: tip}

## Request classes
{: #billing-request-classes}

'Class A' requests involve modification or listing. This category includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.

'Class B' requests are related to retrieving objects or their associated metadata or configurations from the system.

Deleting buckets or objects from the system does not incur a charge. For charges related to Multiple Deletes, see [Delete multiple objects](/docs/cloud-object-storage?topic=cloud-object-storage-object-operations#object-operations-multidelete).

| Class | Requests | Examples |
|--- |--- |--- |
| Class A | PUT, COPY, and POST requests, as well as GET requests used to list buckets and objects | Creating buckets, uploading or copying objects, listing buckets, listing contents of buckets, setting ACLs, and setting CORS configurations |
| Class B | GET (excluding listing), HEAD, and OPTIONS requests | Retrieving objects and metadata |
{: caption="Request classes" caption-side="top"}

Requests made using the Resource Configuration API are not charged for requests and do not accrue usage for billing purposes.
{: note}

## Aspera transfers
{: #billing-aspera}

[Aspera high-speed transfer](/docs/cloud-object-storage?topic=cloud-object-storage-aspera) incurs extra egress charges. For more information, see the [pricing page](/objectstorage/create#pricing){: external}.

## Backup billing
{: #billing-backup}

There are two charges for use of the {{site.data.keyword.cos_short}} Backup service. The first charge is for the amount of data stored within the Backup Vault. This is measured in Gigabytes. Usage is averaged over the billing period. There is an additional second charge for any restores initiated from the Backup Vault during the billing period. Restore jobs incur a fee for each gigabyte of data that is restored.

Use of the {{site.data.keyword.cos_short}} Backup feature does not incur any operational, retrieval, or egress charges on the source bucket. There are also no operational charges for writing backups to the Backup Vault.

The target bucket of a restore job will incur operational charges for the data written to the bucket on restore.

## Storage classes
{: #billing-storage-classes}

Not all data that is stored needs to be accessed frequently, and some archival data might be rarely accessed if at all. For less active workloads, buckets can be created in a different storage class and objects that are stored in these buckets incur charges on a different schedule than standard storage.

There are six classes:

*  **Smart Tier** can be used for any workload, especially dynamic workloads where access patterns are unknown or difficult to predict.  Smart Tier provides a simplified pricing structure and automatic cost optimization by classifying the data into "hot", "cool", and "cold" tiers based on monthly usage patterns. All data in the bucket is then billed at the lowest applicable rate.  There are no threshold object sizes or storage periods, and there are no retrieval fees.
*  **Standard** is used for active workloads, with no charge for data retrieved (other than the cost of the operational request itself).
*  **Vault** is used for cool workloads where data is accessed less than once a month - an extra retrieval charge ($/GB) is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault** is used for cold workloads where data is accessed every 90 days or less - a larger extra retrieval charge ($/GB) is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.

**Flex** has been replaced by Smart Tier for dynamic workloads. Flex users can continue to manage their data in existing Flex buckets, although no new Flex buckets may be created.  Existing users can reference pricing information [here](/docs/cloud-object-storage?topic=cloud-object-storage-flex-pricing).
{: note}

For more information about pricing, see [the pricing table at ibm.com](/objectstorage/create#pricing){: external}.

The **Active** storage class is only used with [One-Rate plans](/docs/cloud-object-storage?topic=cloud-object-storage-onerate), and cannot be used in Standard or Lite plan instances.
{: important}

For more information about creating buckets with different storage classes, see the [API reference](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

### Smart Tier pricing details
{: #smart-tier-pricing}

Based on monthly averages, data in a Smart Tier bucket is classified into one of three tiers based on the following variables:

| Variable | Description |
| --- | --- |
| `storage` | Total volume of data stored in GB |
| `retrievals` | Total volume of data retrieved in GB |
| `requests` | Sum of the number of Class A (write) requests plus 1/10 of the number of Class B (read) requests |
{: caption="Smart Tier bucket classification" caption-side="top"}

- Data is classified **hot** if the total `requests > 1000 x (storage - retrievals)`.
- Data is classified **cold** if the total `requests < (storage - retrievals)`.
- Data is classified **cool** if neither of the above equations are true.

For example, let's imagine a bucket in the `us-south` region with an access pattern that changes from month to month. The bucket stores 1 TB of data, but some objects are very large and others are very small.

1. In the first month, there is a lot of activity but mostly with smaller objects. In total, there are 4 million requests and 100 GB is retrieved. This month the bucket is classified as **hot**.
2. In the second month activity slows down, but the focus is on larger objects. This month there are only 4 thousand requests but 200 GB is retrieved. Now the bucket is classified as **cool**.
3. In the third month activity slows to a near stop.  There are only 400 requests and 10 GB is retrieved. This month the bucket is classified as **cold**.

Let's see how the costs might compare to the other storage classes.

| Month | `storage` | `requests` | `retrieval` | Classification | Standard | Vault | Cold Vault | Smart Tier |
|-------|-----------|------------|-------------|----------------|----------|-------|------------|------------|
| 1     | 1,000 GB  | 4,000,000  | 100 GB      | Hot            | $41      | $53   | $111       | **$41**    |
| 2     | 1,000 GB  | 4,000      | 200 GB      | Cool           | $21      | $14   | $16        | **$12**    |
| 3     | 1,000 GB  | 400        | 10 GB       | Cold           | $21      | $12   | $7         | **$8**     |
| Total | -         | -          | -           | -              | $83      | $79   | $134       | **$61**    |
{: caption="Cost comparison" caption-side="top"}

Note that in situations where data is very cold, it is possible to get a lower rate with a Cold Vault bucket, although unexpected spikes in access could accrue significant costs.  In this scenario, if the data doesn't require on-demand access, it might be better to archive the objects instead.

### Free Tier monthly allowances
{: #free-tier-allowances}

The following Free Tier allowances apply to each month for up to 12 months and apply to the total usage across all Smart Tier buckets in the Standard Plan:

* Up to 5 GB of Smart Tier storage capacity
* 2,000 Class A (PUT, COPY, POST, and LIST) requests
* 20,000 Class B (GET and all others) requests
* 10 GB of data retrieval
* 5GB of egress (public outbound bandwidth) each month

## Get bucket metadata
{: #billing-get-bucket-metadata}

In order to determine your current usage, you may wish to query a bucket to see `bytes_used` and `object_count`. Use of this command returns metadata containing that information for the specified bucket.

```sh
curl https://config.cloud-object-storage.cloud.ibm.com/v1/b/{my-bucket} \
                        -H 'authorization: bearer <IAM_token>'
```
{: codeblock}

The appropriate response to the request should contain `bytes_used` and `object_count`.

```sh
{
  "name": "{my-bucket}",
  "crn": "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903:bucket:my-new-bucket",
  "service_instance_id": "d6f04d83-6c4f-4a62-a165-696756d63903",
  "service_instance_crn": "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::",
  "time_created": "2018-03-26T16:23:36.980Z",
  "time_updated": "2018-10-17T19:29:10.117Z",
  "object_count": 764265234,
  "bytes_used": 28198745752445144
}
```
{: codeblock}

## Get resource information from an API
{: #resource-api-metadata}

The resource controller is the next-generation {{site.data.keyword.cloud_notm}} Platform provisioning layer that manages the lifecycle of {{site.data.keyword.cos_short}} resources in a customer account. The API can provide actual billable metrics, such as types of requests and charges for storage, to get you started. More information can be found at the [documentation](https://cloud.ibm.com/apidocs/resource-controller){: external}

```sh
curl -X GET https://resource-controller.cloud.ibm.com/v2/resource_instances -H 'Authorization: Bearer <IAM_TOKEN>'
```
{: codeblock}

An appropriate response should list metadata for your resources as shown in the example.

```sh
{
  "rows_count": 1,
  "next_url": "/v2/resource_instances?next_docid=g1AAAACkeJzLYWBgYMpgTmFQSklKzi9KdUhJMtTLTMrVTSouNjAw1EvOyS9NScwr0ctLLckBqc1jAZIMC4DU____92eBxdycyiQ6O2sOMCQxMLHnZKEaZ0qEcQ8gxv2HG-fo9M_-Asg4-TVZWQCZcDI1&limit=2&account_id=d86af7367f70fba4f306d3c19c7344b2",
  "resources": [
    {
      "id": "crn:v1:bluemix:public:cloud-object-storage:global:a/4329073d16d2f3663f74bfa955259139:8d7af921-b136-4078-9666-081bd8470d94::",
      "guid": "8d7af921-b136-4078-9666-081bd8470d94",
      "url": "/v2/resource_instances/8d7af921-b136-4078-9666-081bd8470d94",
      "created_at": "2018-04-19T00:18:53.302077457Z",
      "updated_at": "2018-04-19T00:18:53.302077457Z",
      "deleted_at": null,
      "name": "my-instance",
      "region_id": "global",
      "account_id": "4329073d16d2f3663f74bfa955259139",
      "resource_plan_id": "2fdf0c08-2d32-4f46-84b5-32e0c92fffd8",
      "resource_group_id": "0be5ad401ae913d8ff665d92680664ed",
      "resource_group_crn": "crn:v1:bluemix:public:resource-controller::a/4329073d16d2f3663f74bfa955259139::resource-group:0be5ad401ae913d8ff665d92680664ed",
      "target_crn": "crn:v1:bluemix:public:resource-catalog::a/9e16d1fed8aa7e1bd73e7a9d23434a5a::deployment:2fdf0c08-2d32-4f46-84b5-32e0c92fffd8%3Aglobal",
      "crn": "crn:v1:bluemix:public:cloud-object-storage:global:a/4329073d16d2f3663f74bfa955259139:8d7af921-b136-4078-9666-081bd8470d94::",
      "state": "active",
      "type": "service_instance",
      "resource_id": "dff97f5c-bc5e-4455-b470-411c3edbe49c",
      "dashboard_url": "/objectstorage/crn%3Av1%3Abluemix%3Apublic%3Acloud-object-storage%3Aglobal%3Aa%2F4329073d16d2f3663f74bfa955259139%3A8d7af921-b136-4078-9666-081bd8470d94%3A%3A",
      "last_operation": null,
      "resource_aliases_url": "/v2/resource_instances/8d7af921-b136-4078-9666-081bd8470d94/resource_aliases",
      "resource_bindings_url": "/v2/resource_instances/8d7af921-b136-4078-9666-081bd8470d94/resource_bindings",
      "resource_keys_url": "/v2/resource_instances/8d7af921-b136-4078-9666-081bd8470d94/resource_keys",
      "plan_history": [
        {
          "resource_plan_id": "2fdf0c08-2d32-4f46-84b5-32e0c92fffd8",
          "start_date": "2018-04-19T00:18:53.302077457Z"
        }
      ],
      "migrated": false,
      "controlled_by": ""
    }
  ]
}
```
{: codeblock}
