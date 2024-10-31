---

copyright:
  years: 2021, 2023
lastupdated: "2023-05-26"

keywords:  object storage, satellite, local

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Billing for {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}}
{: #billing-cos-satellite}

{{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} has a different pricing model than the typical pay-as-you-go scheme used in the public cloud.
{: shortdesc}

Instead, storage is allocated at a fixed capacity using a "T-shirt size" model.  The available sizes are:

| Object Storage capacity | Raw storage required | Monthly price |
|-------------------------|----------------------|---------------|
| Small (12 TB)           | 18 TB                | $502          |
| Medium (24 TB)          | 36 TB                | $878          |
| Large (48 TB)           | 72 TB                | $1254         |
| Extra Large (96 TB)     | 144 TB               | $2006         |
{: caption="Available sizes" caption-side="top"}

The total cost for using {{site.data.keyword.cos_short}} for {{site.data.keyword.satelliteshort}} is a combination of:

1. Cost of infrastructure
2. Base {{site.data.keyword.satelliteshort}} charge
3. {{site.data.keyword.cos_short}} fixed capacity pricing

## Choosing capacity
{: #billing-satellite-sizing}

The storage instance capacity is set during the provisioning process. You will need to work with your {{site.data.keyword.satelliteshort}} administrator to ensure that enough raw capacity exists in the underlying {{site.data.keyword.satelliteshort}} infrastructure.

As each application has unique storage needs, it is not possible to provide much in the way of generic guidance for choosing a capacity. Take into account the nature of the application (for example, processing high-resolution satellite imagery or 4K video will require a greater capacity than a simple document repository) and the expected scaling of storage needs, and [consult IBM Cloud support](https://cloud.ibm.com/docs/get-support?topic=get-support-using-avatar) as needed.

## Adding capacity
{: #billing-satellite-adding}

At this point, it is not possible to extend capacity once an instance is provisioned.  Instead, you will need to provision an additional instance and create a new bucket for the overflow.
