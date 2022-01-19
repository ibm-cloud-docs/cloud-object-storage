---

copyright:
  years: 2021
lastupdated: "2021-12-01"

keywords:  object storage, satellite, local

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:table: .aria-labeledby="caption"}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Provisioning {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}
{: #provision-cos-satellite}

You can provision {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} using the IBM Cloud console.
{: shortdesc}

## Before you begin
{: #pre-provision-satellite}

Before deploying {{site.data.keyword.cos_short}} in a Satellite location, you must first deploy [a {{site.data.keyword.satelliteshort}} location](/docs/satellite?topic=satellite-locations) with sufficient raw block storage allocated for provisioning {{site.data.keyword.cos_short}}. 

{{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} requires OpenShift 4.7.
{:note}

| Object Storage capacity | Raw storage required | Minimum host requirements            |
|-------------------------|----------------------|--------------------------------------|
| Small (12 TB)           | 18 TB                | 9 nodes of 4 vCPU and 16 GiB memory  |
| Medium (24 TB)          | 36 TB                | 9 nodes of 4 vCPU and 16 GiB memory  |
| Large (48 TB)           | 72 TB                | 9 nodes of 4 vCPU and 16 GiB memory  |
| Extra Large (96 TB)     | 144 TB               | 18 nodes of 4 vCPU and 16 GiB memory |


For more information on configuring hosts for storage, [see the {{site.data.keyword.satelliteshort}} documentation](/docs/satellite?topic=satellite-host-reqs#reqs-host-storage).

Workloads that demand higher performance may benefit from the additional computing power provided by the Extra Large plan, regardless of total storage required.

When provisioning block storage, is recommended to use a ["Silver" storage class at a minimum](/docs/satellite?topic=satellite-config-storage-ebs#sat-ebs-sc-reference) to ensure adequate performance.
{:tip}

## Configure a satellite location
{: #provision-satellite-location}

1. Log in to [the console](https://cloud.ibm.com/){: external}.

## Provision an object storage service instance
{: #provision-satellite-cos}

1. Log in to [the console](https://cloud.ibm.com/){: external}.
2. Navigate to the catalog, by clicking **Catalog** in the navigation bar.
3. Look for the **Object Storage** tile in the storage section and select it.
4. Select **Satellite** from the "Choose an Infrastructure" section.
5. Choose an existing [Satellite location](/docs/satellite?topic=satellite-locations).
6. [Choose a capacity](/docs/cloud-object-storage?topic=cloud-object-storage-billing-cos-satellite) for your new {{site.data.keyword.cos_short}} instance.
7. Click **Create** and you're automatically redirected to your new instance.

## Assign hosts and storage to object storage cluster
{: #provision-satellite-assign}

1. Log in to [the console](https://cloud.ibm.com/){: external}.