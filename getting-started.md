---

copyright:
  years: 2014, 2017
lastupdated: "2017-02-23"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}



# Getting started with IBM Cloud Object Storage {: #getting-started}


IBM Cloud Object Storage provides cloud storage for unstructured data.
Unstructured data refers to files,
audio/visual media,
PDFs,
compressed data archives,
backup images,
application artifacts,
business documents,
or any other binary object.
Like the rest of IBM Cloud,
it's built with scalability,
high availability,
and durability in mind.
IBM Cloud Object Storage does not require replication to provide data resiliency,
but instead uses erasure coding and an Information Dispersal Algorithm to slice and distribute the object across physical storage appliances.
Store and access your data over HTTP(S) using an implementation of the S3 API.
{: shortdesc}

To use the service,
follow these steps:

1. Provision your service instance from the {{site.data.keyword.Bluemix_notm}} catalog. Configure your instance and click **Create**. If you initially choose the **Leave Unbound** option for the **App** field, you can bind the service instance to your {{site.data.keyword.Bluemix_notm}} app later.

2. In your service instance dashboard, create a bucket to start storing objects.

3. Add a file to your bucket, by using the **Actions** menu.

4. To test access to your objects, click **Download** and review the file.

5. When you're ready, [bind the service](/docs/services/reqnsi.html#add_service) to an application.

See how easy building a cloud application can be with a [tutorial showing how to make a simple web-based image gallery](/docs/services/cloud-object-storage/tutorials/web-application.html)) using {{site.data.keyword.Bluemix_notm}} and IBM COS.

# Related Links
{: #rellinks notoc}

## API Reference
{: #api}
* [API Overview](/docs/services/cloud-object-storage/about-compatibility-api.html){: new_window}
* [CLI Overview](/docs/services/cloud-object-storage/CLI.html){: new_window}

## Related Links
{: #general}
* [IBM {{site.data.keyword.Bluemix_notm}} Pricing Sheet](https://www.ng.bluemix.net/#/pricing){: new_window}
* [IBM {{site.data.keyword.Bluemix_notm}} Prerequisites](https://developer.ibm.com/bluemix/support/#prereqs){: new_window}