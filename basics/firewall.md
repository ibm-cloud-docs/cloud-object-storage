---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: ip address, firewall, configuration, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Setting a firewall
{: #firewall}

IAM policies provide a way for administrators to limit access to individual buckets. What if certain data must be accessed from trusted networks only? A bucket firewall restricts all access data unless the request originates from a list of allowed IP addresses.
{: shortdesc}

A user that sets or views a firewall must have the `Manager` role on the bucket. 

## Using the API to set a firewall
{: #firewall-api}

Setting firewalls uses the [COS Resource Configuration API](/apidocs/cos/cos-configuration). This new REST API is used for configuring buckets. 

Users with the `manager` role can view and edit the list of allowed IP addresses from any network in order to prevent accidental lockouts.
{: tip}