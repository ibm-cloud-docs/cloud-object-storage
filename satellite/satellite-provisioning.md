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

Before deploying {{site.data.keyword.cos_short}} in a Satellite location, you must first deploy [a {{site.data.keyword.satelliteshort}} location](/docs/satellite?topic=satellite-locations) with sufficient raw block storage allocated for provisioning {{site.data.keyword.cos_short}}. Note that this location **must be [on-prem](#x6969434){: term}** in a user-owned [VM](##x2043253){: term}. It cannot be contained in {{site.data.keyword.cloud_notm}}.

{{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} is currently known to be compatible with **NetApp ONTAP-SAN** and **AWS Elastic Block Storage**.  
{:important} 

{{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} requires OpenShift 4.7.
{:note}

| Object Storage capacity | Raw storage required | Minimum host requirements |
| Small (12 TB) | 18 TB | 8 nodes of 3 vCPU and 32 GiB memory |
| Medium (24 TB) | 36 TB | 8 nodes of 3 vCPU and 32 GiB memory |
| Large (48 TB) | 72 TB | 8 nodes of 3 vCPU and 32 GiB memory | 
| Extra Large (96 TB) | 144 TB | 16 nodes of 3 vCPU and 32 GiB memory |

For more information on configuring hosts for storage, [see the {{site.data.keyword.satelliteshort}} documentation](/docs/satellite?topic=satellite-host-reqs#reqs-host-storage).

Workloads that demand higher performance may benefit from using an Extra Large plan.

It is recommended to use a Silver plan at a minimum to ensure adequate performance.
{:tip}

## Creating a service instance
{: #provision-satellite-create}

1. Log in to [the console](https://cloud.ibm.com/){: external}.
2. Navigate to the catalog, by clicking **Catalog** in the navigation bar.
3. Look for the **Object Storage** tile in the storage section and select it.
4. Select **Satellite** from the "Choose an Infrastructure" section.
5. Choose an existing [Satellite location](/docs/satellite?topic=satellite-locations).
6. [Choose a capacity](/docs/cloud-object-storage?topic=cloud-object-storage-billing-cos-satellite) for your new {{site.data.keyword.cos_short}} instance.
7. Click **Create** and you're automatically redirected to your new instance.
