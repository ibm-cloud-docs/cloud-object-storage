---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-10-16"

keywords: tutorial, static objects, performance, caching

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}

# Integrating Caching Services for Performance 
{: #integrating-caching}

The resiliency and availability of {{site.data.keyword.cos_full}} is well documented, so wouldn't it be great if you could increase the performance of your website's static assets by moving them closer to your customers, clients, or end-users? 
{: shortdesc}

In this quickstart, you will find step-by-step general instructions on how to integrate a {{site.data.keyword.cos_short}} instance with your a static web site. Of course, throughout the process, there will be numerous options beyond that which is covered here. At each step of your own process of integrating Cloud Internet Services, such as caching, you will want to investigate how all of the options work with your specific situation. And as with every learning process, mistakes are opportunities to learn. However, a mistake in a configuration can also render a resource, asset, or even whole collections inaccessible and offline.

**Note**: When you configure services, any incorrect setting may materially affect the ultimate availability of your resources. In other words, you may lose data. The information provided here is a generalization, and therefore unsuitable to any specific purpose. Please use this for educational and experimental purposes, and not directly upon any mission-critical or production work.
{: tip}

## Before you begin
{: #icsp-prereqs}

You will need:

* An [{{site.data.keyword.cloud}} Platform account](https://cloud.ibm.com/login)
* A provisioned [instance of {{site.data.keyword.cos_full_notm}}](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-provision) and publicly accessible bucket, as shown in [Getting Started](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started)
* A static website addressable by a domain name on the Internet
* The duplicate of all the static content from the website listed above in a bucket with suitable public read access.
* The endpoint particular to your bucket's regional and resiliency settings
* Access to your Domain Name Service settings, including the technical and other requirements, as well as the ability to change the settings.

It is actually very rare to find a web server directly connected to the internet for public access. It's possible that you or your web hosting provider already have placed hardware in between the physical server containing your web site's content and the routers that handle the traffic. The basic idea for boosting performance is getting the static assets of your web content as close to your end-users as possible, using proxies containing the content.

The first two prerequisites listed here are straightforward but having an extra website available with DNS management settings that you can experiment with, complete with all the static content for the website simply sitting in a publicly accessible bucket, may not be an option. 
{: tip}

## Getting Started with Internet Services
{: #icsp-getting-started} 

1. Log in to your {{site.data.keyword.cloud_notm}} account and select "Catalog" from the menu.

2. To select "Internet Services," select the category, "Security and Identity," and if you don't see the choice, filter the results by selecting the "Infrastructure" type of offering. You should then see the option to choose as shown in Figure 1.

3. For the purposes of this tutorial, please select the "Free Trial" plan that includes a 30-day trial of the Cloud Internet Services, including caching, as shown in Figure 2.

4. To connect your existing domain for your experimental website, click the "Let's get started button" to setup the DNS records, delegate the management of the domain name settings, and validation of your new name server configuration. Basic information about DNS is available at [the Wikipedia page](https://en.wikipedia.org/wiki/Domain_Name_System){: external}. The overview of the process of connecting your domain is shown in Figure 3.

5. As shown in Figure 4, adding your domain name to Cloud Internet Services is simple and straightforward. Please note that the DNS provider may not be the same as your website host.

6. If you aren't familiar with your static website's DNS record information, exporting it in the [BIND](https://help.dyn.com/how-to-format-a-zone-file/){: external} format may not only keep the information available, but also provide a visual record of the types of records you will be working with. As Shown in Figure 5, you can also import that file directly into your new DNS configuration.

7. Figure 6 shows how you can manually enter each record of your new DNS configuration. Or, if you click the "Import" button shown, as suggested previously, your old DNS zone records will show up here for use in the next steps.

8. 