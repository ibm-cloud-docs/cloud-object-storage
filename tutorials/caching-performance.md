---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-10-16"

keywords: tutorial, performance, caching, static objects

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

In this overview, you will find step-by-step general instructions on how to integrate a {{site.data.keyword.cos_short}} instance with a static web site, serving up the static contents via [Cloudflare](https://www.cloudflare.com){: external} as a Content Distribution Netork (CDN). Of course, throughout the process, there will be numerous options beyond that which can be covered, but can be explored at your convenience right [here](https://www.ibm.com/cloud/cloudflare){: external}. 

At each step of your own process of integrating Cloud Internet Services, such as DNS or caching, you will want to investigate how all of the options work with your specific situation. And as with every learning process, mistakes are opportunities to learn. However, a mistake in a configuration can also render a resource, asset, or even whole collections inaccessible and offline.

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
* Access to your Domain Name Service (DNS) settings, including the technical and other requirements, as well as the ability to change the settings.

The first two prerequisites listed here are straightforward. However, having an extra website available with DNS management settings that you can experiment with, complete with all the static content for the website also available in a publicly accessible bucket, may not be an option. If you wish to get started building your own static website with {{site.data.keyword.cos_full_notm}}, you can follow this [tutorial](https://developer.ibm.com/tutorials/cl-deploy-a-hello-world-webpage-to-bluemix-app/){: external}.
{: tip}

## Getting Started with IBM Cloud Internet Services
{: #icsp-getting-started} 

It is actually very rare to find a web server directly connected to the internet for public access. It's possible that you or your web hosting provider already have placed hardware in between the physical server containing your web site's content and the routers that handle the traffic. The basic idea for boosting performance is getting the static assets of your web content as close to your end-users as possible, using proxies containing the content.

### Setting up Domain Name Services
{: #icsp-domain-name} 

1. Log in to your {{site.data.keyword.cloud_notm}} account and select "Catalog" from the menu.

2. To select "Internet Services," select the category, "Security and Identity." If you don't see the option available right away, filter the results by selecting the "Infrastructure" type of offering in the menu. You should then see the option for Cloud Internet Services to choose as shown in Figure 1.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-1-cis-catalog.png){: caption="Figure 1. Catalog showing Internet Services option"}

3. For the purposes of this tutorial, please select the "Free Trial" plan that includes a 30-day trial of the Cloud Internet Services, including caching, as shown in Figure 2.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-2-cis-offering.png){: caption="Figure 2. Offering showing Free Trial plan"}

4. To connect your existing domain for your experimental website, click the "Let's get started button" to setup the DNS records, delegate the management of the domain name settings, and validation of your new name server configuration. Basic information about DNS is available at [the Wikipedia page](https://en.wikipedia.org/wiki/Domain_Name_System){: external}. The overview of the process of connecting your domain is shown in Figure 3.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-3-cis-first-step.png){: caption="Figure 3. Welcome with overview graphic"}

5. As shown in Figure 4, adding your domain name to Cloud Internet Services is simple and straightforward. Please note that your DNS provider may or may not be the same as your website host. You will need to be familiar with both the general definitions of DNS settings and the specific manner in which your host or provider allows you to modify those settings.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-4-cis-add-domain.png){: caption="Figure 4. Connect your domain form entry"}

6. If you aren't familiar with your static website's DNS record information, exporting it in the [BIND](https://help.dyn.com/how-to-format-a-zone-file/){: external} format may not only keep the information available, but also provide a visual record of the types of records you will be working with. As Shown in Figure 5, you can also import that file directly into your new DNS configuration.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-5-cis-import-dns.png){: caption="Figure 5. Import DNS records"}

7. Figure 6 shows how you can manually enter each record of your new DNS configuration. Or, if you click the "Import" button shown, as suggested previously, your old DNS zone records will show up here for use in the next steps.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-6-cis-a-name-record.png){: caption="Figure 6. Manually enter DNS records"}

8. As the general aim of caching involves pointing end users of your content to the cached versions of your files stored in locations other than your original server. DNS is used as the addressing system order to point your end users in the right direction. Figure 7 exemplifies how that information is shared. When adding your domain to Internet Services, you will receive new name server addresses. Those addresses will need to be used to update your current DNS settings.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-7-cis-ns-management.png){: caption="Figure 7. Compare current and targeted name servers"}

9. Just as a reminder, Figure 8 repeats the instructions for updating your website's DNS information. The information provide as part of your process will be different, and the instructions for changing DNS information is beyond the scope of this document.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-8-cis-nameservers.png){: caption="Figure 8. New name servers for sample static  website"}

10. Once completed across all platforms, the Internet Services DNS will confirm the changes!

### Setting up a global proxy
{: #icsp-global-proxy}

To get started using Networks-as-a-Service, you can start by watching a demonstration at the [documentation](/docs/infrastructure/loadbalancer-service?topic=loadbalancer-service-naas-video). Also, there is a complete tutorial for building a scalable web application using [virtual servers](/docs/infrastructure/loadbalancer-service?topic=solution-tutorials-highly-available-and-scalable-web-application), to provide more context.

Every situation is different, and proxies should be configured for specific uses. In order to use caching, we need at least one proxy. Specifically, you can find specific information for your uses at the [documentation](https://cloud.ibm.com/docs/infrastructure/loadbalancer-service?topic=loadbalancer-service-about-ibm-cloud-load-balancer).
{: tip} 

1. The setup for your first proxy is simple. Figure 9 shows the minimum of requiring just one hostname of defined network support to [get started](/docs/infrastructure/loadbalancer-service?topic=loadbalancer-service-ibm-cloud-load-balancer-basics#load-balancing-methods).
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-9-cis-load-balancer.png){: caption="Figure 9. New Global Load Balancer for a sample static website"}

2. In addition to the proxy, you can now group VSI or Bare Metal server instances in server pools. The Load Balancer requires at least two free IP addresses from its private subnet. In addition, if the load balancer is a public load balancer and the IBM system pool option isn't used, then at least two free IP addresses are needed from your public subnet as well, according to the [documentation](/docs/infrastructure/loadbalancer-service?topic=loadbalancer-service-load-balancer-provisioning-troubleshooting).

### Setting up caching rules
{: #icsp-caching-rules}

With the free trial of Cloud Internet Services, you can create up to 50 page rules to determine how your caching proxies support your website. 

1.  Rules are used to classify the traffic and are based on the HTTP header fields. Figure 10 shows your domain and the list of rules. To create your first page rule, click on "Create rule."
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-10-page-rules-start.png){: caption="Figure 10. Define rules for caching a sample static website"}

2.  Figure 11 shows a typical pattern used to match the URL request to be handled by your new rule. When the traffic matches all the rules, an action specified by the policy is taken.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-11-cis-create-page-rule.png){: caption="Figure 11. Add a patternt to a  rule"}

3. The behavior defined by that policy can be configured as shown in Figure 12. The option "Resolve override with COS" should be selected from the Setting. Your {{site.data.keyword.cos_short}} instance should appear in the option list. When your instance has been selected, publicly accessible buckets from your instance should appear as options in the "Bucket" list. You can add the name of the bucket as the new "Record Name" that will be concatenated to your domain name, as shown. The endpoint for your instance (depending upon Bucket resiliency and storage options), will appear in the "Host Header Override" section. Once your DNS information has propagated to the servers, you can use the new "host" in your code and scripts.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-12-cis-add-page-rule-behavior.png){: caption="Figure 12. Link your COS instance to your website via caching"}

4. With the creation of the policy, you can now choose your caching level, as appropriate to your needs. Figure 13 shows the policy behavior for the override to be created. Click "Create" and your first caching rule has been set.
    ![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/cdn-13-cis-cache-level.png){: caption="Figure 13. Set the cache level"}

Congratulations! You've just created your first page rule caching everything in your static website, as part of a Cloudflare CDN. When you confirm the new DNS settings are working, just add the new domain to your scripts when you want to use the cached content automatically. 

## Next Steps
{: #icsp-next-steps}

Testing is strongly suggested and encouraged. Writing a script that uses a proxy to make requests and record the responses can also give you future suggestions for geo-routing, for example. Good luck!  