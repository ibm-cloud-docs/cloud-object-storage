---

copyright:
  years: 2019
lastupdated: "2019-09-10"

keywords: events, serverless, whisk

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 

# Use {{site.data.keyword.openwhisk}}
{: #functions}

With {{site.data.keyword.openwhisk}},](/docs/openwhisk) you can use your favorite programming language to write lightweight code that runs app logic in a scalable way. You can run code on-demand with HTTP-based API requests from applications or run code in response to {{site.data.keyword.cloud_notm}} services and third-party events, like updates made to a bucket. The Function-as-a-Service (FaaS) programming platform is based on the open source project Apache OpenWhisk. {: shortdesc}

## Using object storage as an event source
{: #functions-events }

{{site.data.keyword.openwhisk}} is an event-driven compute platform (also referred to as Serverless computing). Actions (small bits of code) run in response to triggers (some category of event), and rules associate certain actions with certain triggers. Configure {{site.data.keyword.cos_full}} to be an event source, and anytime an object in a particular bucket is written or deleted an action is triggered. You can further tailor the changes feed to only corral events for objects which match a particular prefix or suffix. 

1. You need to [allow Cloud Functions access to listen for changes that are made to your bucket.
2. Then, [create a trigger](/docs/openwhisk?topic=cloud-functions-pkg_obstorage#pkg_obstorage_ev_trig_ui) to respond to the changes feed.
3. You can also use [the {{{{site.data.keyword.cos_full}}}} package](https://test.cloud.ibm.com/docs/openwhisk?topic=cloud-functions-pkg_obstorage#pkg_obstorage_actions) to bind credentials and easily script common tasks.

For more information about using {{site.data.keyword.openwhisk}} with object storage, [see the documentation](/docs/openwhisk?topic=cloud-functions-pkg_obstorage).

It isn't possible to use a [bucket with a firewall enabled](/docs/services/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall) as an event source for {{site.data.keyword.openwhisk}} actions.
{:important}