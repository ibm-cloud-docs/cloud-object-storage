---

copyright:
  years: 2019, 2024
lastupdated: "2024-03-20"

keywords: events, serverless, whisk

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using {{site.data.keyword.openwhisk_short}}
{: #functions}

{{site.data.keyword.openwhisk}} is deprecated. Existing Functions entities such as actions, triggers, or sequences will continue to run, but as of 28 December 2023, you can’t create new Functions entities. Existing Functions entities are supported until October 2024. Any Functions entities that still exist on that date will be deleted. For more information, see [Deprecation overview](/docs/openwhisk?topic=openwhisk-dep-overview). {: deprecated}

With [{{site.data.keyword.openwhisk}}](/docs/openwhisk?topic=openwhisk-getting-started), you can use your favorite programming language to write lightweight code that runs app logic in a scalable way. You can run code on-demand with HTTP-based API requests from applications or run code in response to {{site.data.keyword.cloud_notm}} services and third-party events, like updates made to a bucket. The Function-as-a-Service (FaaS) programming platform is based on the open source project Apache OpenWhisk.
{: shortdesc}



## Using {{site.data.keyword.cos_short}} as an event source
{: #functions-events}

{{site.data.keyword.openwhisk_short}} is an event-driven compute platform (also referred to as Serverless computing). Actions (small bits of code) run in response to triggers (some category of event), and rules associate certain actions with certain triggers. Configure {{site.data.keyword.cos_full}} to be an event source, and anytime an object in a particular bucket is written or deleted an action is triggered. You can further tailor the changes feed to only corral events for objects which match a particular prefix or suffix.

1. Set the option to allow Cloud Functions [access](/docs/openwhisk?topic=openwhisk-pkg_obstorage#pkg_obstorage_ev) to listen for changes that are made to your bucket. This involves creating a service-to-service [authorization](/docs/account?topic=account-serviceauth), and uses the new [Notifications Manager](/docs/openwhisk?topic=openwhisk-pkg_obstorage#pkg_obstorage_auth) IAM role.
2. Then, create a [trigger](/docs/openwhisk?topic=openwhisk-pkg_obstorage#pkg_obstorage_ev_trig_ui) to respond to the changes feed.
3. Then use the [{{site.data.keyword.cos_full_notm}} package](/docs/openwhisk?topic=openwhisk-pkg_obstorage#pkg_obstorage_actions) to bind credentials and easily script common tasks.

For more information about using {{site.data.keyword.openwhisk_short}} with {{site.data.keyword.cos_full_notm}}, see the Functions [documentation](/docs/openwhisk?topic=openwhisk-pkg_obstorage).

It is not possible to use a bucket with a firewall [enabled](/docs/cloud-object-storage?topic=cloud-object-storage-setting-a-firewall) as an event source for {{site.data.keyword.openwhisk}} actions.
{: important}

## Next Steps
{: #functions-next-steps}

Be sure to identify the appropriate [region and endpoint](/docs/openwhisk?topic=openwhisk-cloudfunctions_regions) for your service. Then, verify your operations with specific [testing](/docs/openwhisk?topic=openwhisk-test).
