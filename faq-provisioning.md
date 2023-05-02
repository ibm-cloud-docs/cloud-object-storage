---

copyright:
  years: 2017, 2021, 2023
lastupdated: "2023-04-17"

keywords: faq, frequently asked questions, object storage

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - Provisioning, Plans, and Naming
{: #faq-provision}

Frequently asked questions can produce helpful answers and insight into best practices for working with {{site.data.keyword.cos_full}}.
{: shortdesc}

## Why can I not create or delete a service instance?
{: #faq-instance-create-delete}
{: faq}

A user is required to have have at a minimum the platform role of `editor` for all IAM enabled services, or at least for Cloud Object Service. For more information, see the [IAM documentation on roles](/docs/account?topic=account-iam-service-roles-actions).


## Which one of my instances uses a Lite plan?
{: #faq-instance-find-lite}
{: faq}

An account is limited to a single instance of {{site.data.keyword.cos_full_notm}} that uses a Lite plan.  You can find this instance three different ways:

1. Navigate to the [catalog](https://cloud.ibm.com/objectstorage/create) and attempt to make a new Lite instance.  An error will pop up prompting you to delete the existing instance, and provides a link to the current Lite instance.
2. Navigate to the storage section of the resource list, and click on any area of blank space to select an instance of {{site.data.keyword.cos_short}}. An informational sidebar will appear and provide the plan name: either Lite or Standard.
3. Use the CLI to search for the resource:

```sh
ibmcloud resource search "service_name:cloud-object-storage AND 2fdf0c08-2d32-4f46-84b5-32e0c92fffd8"
```

## How do I upgrade a service instance from Lite to Standard?
{: #faq-instance-upgrade}
{: faq}

Typically, this is easily done from the {{site.data.keyword.cos_full_notm}} console by clicking on **Plan** in the navigation menu, located after **Connections**.

In cases where the instance has been locked due to exceeding the maximum allowed size of a Lite instance it may be necessary to use the CLI. The plan ID for a standard {{site.data.keyword.cos_short}} instance is `744bfc56-d12c-4866-88d5-dac9139e0e5d` (if curious, this can be found by issuing the CLI command `ic catalog service cloud-object-storage`).  You'll need to know the name of the instance you are trying to upgrade.  For example, to upgrade the instance "My Object Storage", you can issue the command:

```sh
ic resource service-instance-update "My Object Storage" --service-plan-id 744bfc56-d12c-4866-88d5- dac9139e0e5d
```

## Are bucket names case-sensitive?
{: #faq-name-case}
{: faq}

Bucket names are required to be DNS addressable and are not case-sensitive.

## What is the maximum number of characters that can be used in a key, or Object name?
{: #faq-max-key}
{: faq}

Keys have a 1024-character limit.

## What are some tools unable to render object names?
{: #faq-xml-error}
{: faq}

Object names that contain unicode characters that are not allowed by the XML standard will result in "Malformed XML" messages. For more information, see [the XML reference documentation](https://www.w3.org/TR/xml/#charsets).

## Can I create more than one Object Storage service with a Lite account?
{: #faq-lite-storage}
{: faq}
 
If you already have a Lite plan instance created, you may create other Standard plan instances, but only one Lite plan instance is allowed.

## What happens if I exceed the maximum usage allowed for a Lite plan?
{: #faq-lite-exceed}
{: faq}
 
Once you exceed the allowed usage, the service instance associated with the Lite plan becomes inaccessible.  You will receive a warning notification email with corrective steps. If you do not take action, the instance is removed.
