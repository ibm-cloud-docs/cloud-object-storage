---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

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

# Setting a firewall
{: #setting-a-firewall}

IAM policies provide a way for administrators to limit access to individual buckets. What if certain data must be accessed from trusted networks only? A bucket firewall restricts all access to data unless the request originates from a list of allowed IP addresses.
{: shortdesc}

There are some rules around setting a firewall:

* A user that sets or views a firewall must have the `Manager` role on the bucket. 
* A user with the `Manager` role on the bucket can view and edit the list of allowed IP addresses from any IP address to prevent accidental lockouts.
* The {{site.data.keyword.cos_short}} Console can still access the bucket, provided the user's IP address is authorized.
* Other {{site.data.keyword.cloud_notm}} services **are not authorized** to bypass the firewall. This limitation means that other services that rely on IAM policies for bucket access (such as Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions, and others) will be unable to do so.

When a firewall is set, the bucket is isolated from the rest of {{site.data.keyword.cloud_notm}}. Consider how this may impact applications and workflows that depend on other services directly accessing a bucket before enabling the firewall.
{: important}

## Using the console to set a firewall
{: #firewall-console}

First, make sure that you have a bucket. If not, follow the [getting started tutorial](/docs/services/cloud-object-storage/getting-started.html) to become familiar with the console.

### Set a list of authorized IP addresses
{: #firewall-console-enable}

1. From the {{site.data.keyword.cloud_notm}} [console dashboard](https://cloud.ibm.com/), select **Storage** to view your resource list.
2. Next, select the service instance with your bucket from within the **Storage** menu. This takes you to the {{site.data.keyword.cos_short}} Console.
3. Choose the bucket that you want to limit access to authorized IP addresses. 
4. Select **Access policies** from the navigation menu.
5. Select the **Authorized IPs** tab.
6. Click **Add IP addresses**, then choose **Add**.
7. Add a list of IP addresses in [CIDR notation](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing), for example `192.168.0.0/16, fe80:021b::0/64`. Addresses can follow either IPv4 or IPv6 standards.  Click **Add**.
8. The firewall will not be enforced until the address is saved in the console.  Click **Save all** to enforce the firewall.
9. Now all objects in this bucket are only accessible from these IP addesses!

### Remove any IP address restrictions
{: #firewalls-console-disable}

1. From the **Authorized IPs** tab, check the boxes next to any IP addresses or ranges to remove from the authorized list.
2. Select **Delete**, and then confirm the dialog box by clicking **Delete** again.
3. The updated list won't be enforced until the changes are saved in the console.  Click **Save all** to enforce the new rules.
4. Now all objects in this bucket are only accessible from these IP addesses!

If there are no authorized IP addresses listed this means that normal IAM policies will apply to the bucket, with no restrictions on the user's IP address.  
{: note}


## Set a firewall through an API
{: #firewall-api}

Firewalls are managed with the [COS Resource Configuration API](/apidocs/cos/cos-configuration). This new REST API is used for configuring buckets. 

Users with the `manager` role can view and edit the list of allowed IP addresses from any network in order to prevent accidental lockouts.
{: tip}
