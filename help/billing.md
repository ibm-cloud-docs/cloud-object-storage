---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-10-09"

keywords: administration, billing, platform

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

# Billing
{: #billing}

Information on pricing can be found at [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage/pricing/){: external}.
{: .shortdesc}

## Invoices
{: #billing-invoices}

Find your account invoices at **Manage** > **Billing and Usage** in the navigation menu.

Each account receives a single bill. If you need separate billing for different sets of containers, then creating multiple accounts is necessary.

## {{site.data.keyword.cos_full_notm}} pricing
{: #billing-pricing}

Storage costs for {{site.data.keyword.cos_full}} are determined by total volume of data that is stored, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system.

Infrastructure offerings are connected to a three-tiered network, segmenting public, private, and management traffic. Infrastructure services can transfer data between one another across the private network at no cost. Infrastructure offerings (such as bare metal servers, virtual servers, and cloud storage) connect to other applications and services in the {{site.data.keyword.cloud_notm}} Platform catalog (such as Watson services and Cloud Foundry runtimes) across the public network, so data transfer between those two types of offerings is metered and charged at standard public network bandwidth rates.
{: tip}

## Request classes
{: #billing-request-classes}

'Class A' requests involve modification or listing. This category includes creating buckets, uploading or copying objects, creating or changing configurations, listing buckets, and listing the contents of buckets.

'Class B' requests are related to retrieving objects or their associated metadata or configurations from the system.

Deleting buckets or objects from the system does not incur a charge.

| Class | Requests | Examples |
|--- |--- |--- |
| Class A | PUT, COPY, and POST requests, as well as GET requests used to list buckets and objects | Creating buckets, uploading or copying objects, listing buckets, listing contents of buckets, setting ACLs, and setting CORS configurations |
| Class B | GET (excluding listing), HEAD, and OPTIONS requests | Retrieving objects and metadata |

## Aspera transfers
{: #billing-aspera}

[Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) incurs extra egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage#s3api).

## Storage classes
{: #billing-storage-classes}

Not all data that is stored needs to be accessed frequently, and some archival data might be rarely accessed if at all. For less active workloads, buckets can be created in a different storage class and objects that are stored in these buckets incur charges on a different schedule than standard storage.

There are four classes:

*  **Standard** is used for active workloads, with no charge for data retrieved (other than the cost of the operational request itself).
*  **Vault** is used for cool workloads where data is accessed less than once a month - an extra retrieval charge ($/GB) is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault** is used for cold workloads where data is accessed every 90 days or less - a larger extra retrieval charge ($/GB) is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.
*  **Flex** is used for dynamic workloads where access patterns are more difficult to predict. Depending on usage, if the costs of and retrieval charges exceeds a cap value, then retrieval charges are dropped and a new capacity charge is applied instead. If the data isn't accessed frequently, it is more cost effective than Standard storage, and if access usage patterns unexpectedly become more active it is more cost effective than Vault or Cold Vault storage. Flex doesn't require a minimum object size or storage period.

For more information about pricing, see [the pricing table at ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

For more information about creating buckets with different storage classes, see the [API reference](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).

## Get bucket metadata
{: #curl-bucket-metadata}

In order to determine your current usage, you may wish to query a bucket to see `bytes_used` and `object_count`. Use of this command returns metadata containing that information for the specified bucket.
 
```
curl https://config.cloud-object-storage.cloud.ibm.com/v1/b/{my-bucket} \
                        -H 'authorization: bearer <IAM_token>' 
```
{: codeblock}

The appropriate response to the request should contain `bytes_used` and `object_count`. 
```
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

## Get resource information from an API
{: #resource-api-metadata}

The resource controller is the next-generation {{site.data.keyword.cloud_notm}} Platform provisioning layer that manages the lifecycle of {{site.data.keyword.cos_short}} resources in a customer account. The API can provide actual billable metrics, such as types of requests and charges for storage, to get you started. More information can be found at the [documentation](https://cloud.ibm.com/apidocs/resource-controller){: external}

```bash
curl -X GET https://resource-controller.cloud.ibm.com/v2/resource_instances -H 'Authorization: Bearer <IAM_TOKEN>' 
```
{: codeblock}

An appropriate response should list metadata for your resources as shown in the example. 

```
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