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

# Billing for {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}
{: #billing-cos-satellite}

{{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} has a different pricing model than the typical pay-as-you-go scheme used in the public cloud.
{: shortdesc}

Instead, storage is allocated at a fixed capacity using a "T-shirt size" model.  The available sizes are:

* Small (12 TB)
* Medium (24 TB)
* Large (48 TB)
* Extra Large (96 TB)
  
These represent usable capacity - keep in mind that the {{site.data.keyword.cos_short}} system makes use of erasure coding and requires 1.5x underlying raw storage for provisioning.
{:important}

## Choosing capacity
{: #billing-satellite-sizing}

The storage instance capacity is set during the provisioning process. You will need to work with your {{site.data.keyword.satelliteshort}} administrator to ensure that enough raw capacity exists in the underlying {{site.data.keyword.satelliteshort}} infrastructure.

As each application has unique storage needs, it is not possible to provide much in the way of generic guidance for choosing a capacity. Take into account the nature of the application (for example, processing high-resolution satellite imagery or 4K video will require a greater capacity than a simple document repository) and the expected scaling of storage needs, and [consult IBM Cloud support](https://test.cloud.ibm.com/docs/get-support?topic=get-support-using-avatar) as needed.

## Adding capacity
{: #billing-satellite-adding}

At this point, it is not possible to extend capacity once an instance is provisioned.  Instead, you will need to provision an additional instance and create a new bucket for the overflow.  
