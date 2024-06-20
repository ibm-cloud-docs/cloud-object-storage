---

copyright:
  years: 2017, 2024
lastupdated: "2024-05-30"


keywords: faq, frequently asked questions, object storage, Lite Plan, Standard Plan, Free Tier, Smart Tier, storage class, console, cli


subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - Plans
{: #faq-provision}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## Which one of my instances uses a Lite plan?
{: #faq-instance-find-lite}
{: faq}

An account is limited to a single instance of {{site.data.keyword.cos_full_notm}} that uses a Lite plan.  You can find this instance three different ways:

1. Navigate to the [catalog](/objectstorage/create) and attempt to make a new Lite instance.  An error will pop up prompting you to delete the existing instance, and provides a link to the current Lite instance.
2. Navigate to the storage section of the resource list, and click on any area of blank space to select an instance of {{site.data.keyword.cos_short}}. An informational sidebar will appear and provide the plan name: either Lite or Standard.
3. Use the CLI to search for the resource:

```sh
ibmcloud resource search "service_name:cloud-object-storage AND 2fdf0c08-2d32-4f46-84b5-32e0c92fffd8"
```

## How do I upgrade a service instance from a Lite Plan to a Standard Plan?
{: #faq-instance-upgrade}
{: faq}

* Using the Console

   1. Upgrade your account to a [Pay-As-You-Go account](https://cloud.ibm.com/docs/account?topic=account-upgrading-account).
   1. Go to the [IBM Cloud Object Storage console](https://cloud.ibm.com/objectstorage).
   1. Using the left navigation panel, select the name of the Cloud Object Storage instance you want to upgrade.
   1. Click `Plan` in the navigation menu, located after `Instance Usage`. The `Plan` tab for a Lite Plan instance displays a `Change Pricing Plan` section.
   1. Select the "Standard" plan and click `Save`.

* Using the CLI

   1. Use the plan ID for a standard Object Storage instance: 

      744bfc56-d12c-4866-88d5-dac9139e0e5d
      {: codeblock}

      

   1. Using the name of the instance that you are trying to upgrade (for example, to upgrade the instance “"My Object Storage"), issue the command:

      ```sh
      ic resource service-instance-update "My Object Storage" --service-plan-id 744bfc56-d12c-4866-88d5-dac9139e0e5d
      ```


   1. Use the plan ID for a standard Object Storage instance: 

      744bfc56-d12c-4866-88d5-dac9139e0e5d
      {: codeblock}

      

   1. Using the name of the instance that you are trying to upgrade (for example, to upgrade the instance “"My Object Storage"), issue the command:

      ```sh
      ic resource service-instance-update "My Object Storage" --service-plan-id 744bfc56-d12c-4866-88d5-dac9139e0e5d
      ```



## Can I create more than one Object Storage service with a Lite plan?
{: #faq-lite-storage}
{: faq}

If you already have a Lite plan instance created, you may create other Standard plan instances, but only one Lite plan instance is allowed.-->



## What if my Lite Plan instance is locked?
{: #faq-locked-account}
{: faq}

In cases where a Lite Plan instance has exceeded the size limit, and your account is locked or deactivated:

* The [COS support](https://cloud.ibm.com/unifiedsupport/cases/form) team can help to unlock your account.

* Upon enablement, reduce your storage to less than 25GB within a week to prevent it from getting disabled again. Your Lite Plan instance can be reactivated only once. If your usage quota is violated a second time, conversion to a paid plan is required.

## How does frequency of data access impact the pricing of {{site.data.keyword.cos_short}}?
{: #faq-access-price}
{: faq}

Storage cost for {{site.data.keyword.cos_short}} is determined by the total volume of data stored, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system. For details, see [cloud-object-storage-billing](/docs/cloud-object-storage?topic=cloud-object-storage-billing).

## What are the considerations for choosing the correct storage class in {{site.data.keyword.cos_short}}?  
{: #faq-choose-storageclass}
{: faq}

You can choose the correct storage class based on your requirement. For details, see [billing-storage-classes](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-storage-classes).
