---

copyright:
  years: 2024
lastupdated: "2024-04-19"

keywords: events, serverless, whisk, code engine

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using {{site.data.keyword.codeenginefull_notm}} with {{site.data.keyword.cos_full}}
{: #code-engine}

Use {{site.data.keyword.codeenginefull_notm}} to run containerized workloads, such as web apps, functions, and batch jobs, that respond to {{site.data.keyword.cos_full}} events.
{: shortdesc}

## Using {{site.data.keyword.cos_short}} as an event source
{: #code-engine-event-source}

Using {{site.data.keyword.cos_short}} as an event source {{site.data.keyword.codeengineshort}} is an event-driven compute platform (also referred to as Serverless computing). Actions (small bits of code) run in response to triggers (some category of event), and rules associate certain actions with certain triggers. Configure {{site.data.keyword.cos_full}} to be an event source, and anytime an object in a particular bucket is written or deleted an action is triggered. You can further tailor the changes feed to only corral events for objects which match a particular prefix or suffix.
See [Working with the IBM Cloud Object Storage event producer](/docs/codeengine?topic=codeengine-eventing-cosevent-producer) for more information.
