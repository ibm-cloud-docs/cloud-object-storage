---

copyright:
   years: 2022, 2024
lastupdated: "2024-01-09"

keywords: tutorials, cbr, firewall, allowlist, rules
subcollection: cloud-object-storage
content-type: tutorial
account-plan: lite
completion-time: 10m

---

{{site.data.keyword.attribute-definition-list}}

# Securing data using context-based restrictions
{: #cos-tutorial-cbr}
{: toc-content-type="tutorial"}
{: toc-completion-time="10m"}

In this tutorial, you will establish [context-based restrictions](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall) that prevent any access to object storage data unless the request originates from a trusted network zone.
{: shortdesc}

## Before you begin
{: #cos-tutorial-cbr-prereqs}

Before you plan on using [context-based restrictions](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall) with Cloud Object Storage buckets, you need:

- An [IBM Cloud™ Platform account](http://cloud.ibm.com/)
- An [instance of IBM Cloud Object Storage](/objectstorage/create)
- A role of `Administrator` for context-based restrictions
- A bucket

## Navigate to the context-based restrictions console
{: #cos-tutorial-cbr-console}
{: step}

From the **Manage** menu, select **Context-based restrictions**.

![Navigate to CBR](/images/cbr_1.png){: caption="Navigate to CBR"}

## Create a new rule
{: #cos-tutorial-cbr-new-rule}
{: step}

1. Click on **Rules**.
1. Choose a name for the rule. This will help keep things organized if you end up with a lot of different rules across all of your cloud services.
1. Click **Continue**.

![Name the rule](/images/cbr_3.png){: caption="Name the rule"}

## Scope the rule
{: #cos-tutorial-cbr-scope}
{: step}

Now you can choose the specific object storage resources to which you would like to apply the context-based restrictions. This can become as specific or generic as you wish - you could apply the rule to all object storage instances and buckets, a specific service instance, or even a specific bucket.  Additionally, you can choose which networks (public, private, or direct) you wish to be included.

In this example, we will choose a service instance.

1. Select **IAM services**.
2. Choose **Cloud Object Storage** from the drop down menu.
3. Select the **Resources based on specific attributes** radio button.
4. Check the **Service instance** box.
5. Select the service instance you want the rule to affect.

![Scope the rule](/images/cbr_4.png){: caption="Scope the rule"}


If you want to instead only limit access to a specific bucket, you can select the **Resource ID** checkbox instead.  Provide the name of the bucket in the field - nothing else is necessary.
{: tip}

## Create a network zone
{: #cos-tutorial-cbr-network}
{: step}

Now that we know what the rule will affect, we need to decide what the rule will allow. To do this, we'll create a new _network zone_ and apply it to the new rule.

1. Click on **Create +**.

![Scope the rule](/images/cbr_5.png){: caption="Scope the rule"}

2. Give the network zone a helpful name and description.
3. Add some IP ranges to the **Allowed IP addresses** text box.
4. Click **Next**.

![Scope the rule](/images/cbr_6.png){: caption="Scope the rule"}

## Finish the rule and verify that it works
{: #cos-tutorial-cbr-rule}
{: step}

Finally, all you need to do is click **Create** and your new rule will be active.

An easy way to check that it works is to [send a simple CLI command] from outside of the allowed network zone, such as a bucket listing (`ic cos buckets`).  It will fail with a `403` error code.

## Next steps
{: #cos-tutorial-cbr-next}

Learn more about [context-based restrictions and how they relate to legacy bucket firewalls](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall).
