---

copyright:
  years: 2017, 2024
lastupdated: "2024-07-01"


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

In cases where a Lite Plan instance has exceeded the size limit, your account is locked or deactivated.

* To continue using the service instance, follow the [steps to upgrade it to a Standard plan](/docs/cloud-object-storage?topic=cloud-object-storage-faq-provision#faq-instance-upgrade). Effective December 15th, 2024, support will end for all Lite Plan instances. To avoid loss of data, Lite Plan users need to convert their Lite Plan instance to the Standard (paid) plan before that date.
* If necessary, you can [create a support case](https://cloud.ibm.com/unifiedsupport/cases/form) to request that the Cloud Support team unlock the account and provide a one-time reactivation of the instance to allow time for you to convert the plan.

## How does frequency of data access impact the pricing of {{site.data.keyword.cos_short}}?
{: #faq-access-price}
{: faq}

Storage cost for {{site.data.keyword.cos_short}} is determined by the total volume of data stored, the amount of public outbound bandwidth used, and the total number of operational requests processed by the system. For details, see [cloud-object-storage-billing](/docs/cloud-object-storage?topic=cloud-object-storage-billing).

## What are the considerations for choosing the correct storage class in {{site.data.keyword.cos_short}}?  
{: #faq-choose-storageclass}
{: faq}

You can choose the correct storage class based on your requirement. For details, see [billing-storage-classes](/docs/cloud-object-storage?topic=cloud-object-storage-billing#billing-storage-classes).

## What is Free Tier?
{: #faq-free-tier}
{: faq}

Free Tier is a no-cost option that allows you to use {{site.data.keyword.cos_short}} for free, within certain allowances, for 12 months. It enables you to easily evaluate and explore all the features of {{site.data.keyword.cos_short}} without any upfront costs. To get Free Tier, you must create a Smart Tier bucket in any location, in an instance provisioned under the Standard plan.

## What are the specific allowances included in the Free Tier?
{: #faq-free-tier-allowances}
{: faq}

Free Tier includes free monthly usage in the Smart Tier storage class under the Standard plan. Free Tier allowances include up to 5 GB of Smart Tier storage capacity, 2,000 Class A (PUT, COPY, POST, and LIST) requests, 20,000 Class B (GET and all others) requests, 10 GB of data retrieval, and 5GB of egress (public outbound bandwidth) each month.

## When does Free Tier expire?
{: #faq-free-tier-expire}
{: faq}

The Free Tier provides free usage for the specified allowances for 12 months from the date when the {{site.data.keyword.cos_short}} instance was initially created.

## What happens if I exceed the Free Tier usage limits or after the 12-month period ends?
{: #faq-free-tier-exceed-limits}
{: faq}

If you exceed the Free Tier monthly allowances within the 12-month period, you are only charged for the portion above the allowance and only in the months when they are exceeded.

## What happens after the 12-month Free Tier period ends?
{: #faq-free-tier-period-end}
{: faq}

Once the 12-month Free Tier period ends, you are charged at the standard pay-as-you-go rates ([see pricing](https://cloud.ibm.com/objectstorage/create#pricing){: external}).

## How can I transition from Free Tier to production use?
{: #faq-free-tier-to-production}
{: faq}

Free Tier enables you to seamlessly transition to production use when you are ready to scale up. No further action is needed. You are billed for any usage over the Free Tier usage allowances.

## How are the Free Tier allowances applied across multiple Smart Tier buckets in my account?
{: #faq-free-tier-allowances-applied}
{: faq}

The Free Tier limits apply to the total usage across all Smart Tier buckets in the Standard Plan.

## How can I transition from my current Lite Plan instance to Free Tier?
{: #faq-lite-plan-to-free-tier}
{: faq}

There is no direct path to transition from the old Lite Plan to the Free Tier. First, upgrade your Lite Plan to a Standard plan. Then you can enable the Free Tier by either creating a Smart Tier bucket in the Standard plan or, if you already had a Smart Tier bucket in the Lite Plan, the Free Tier will apply to it once the Lite Plan is upgraded to the Standard plan.
