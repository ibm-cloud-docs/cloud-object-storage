---

copyright:
  years: 2020
lastupdated: "2020-03-25"

keywords: developer, best practices, object storage

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Encryption and data integrity 
{: #performance-encryption}

Workloads with high performance requirements that rely on {{site.data.keyword.cos_full}} can benefit from fine tuning encryption settings and avoiding unnecessary requests.  
{: shortdesc}

## Cypher tuning
{: #performance-encryption-cypher}

## Using conditional reads
{: #performance-encryption-conditional}