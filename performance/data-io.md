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

# Object size and data IO
{: #performance-io}

Object size can have significant impacts on {{site.data.keyword.cos_full}} performance. Choose the right approach for your workload.  
{: shortdesc}

## Concurrency and latency
{: #performance-io-concurrency}

For large objects greater than 200mb in size, Aspera is a good option but not for smaller than 200mb objects. 

When uploading data to COS multipart uploads are a great way to break up transfers into many parallel transactions. 

## Throttling batch deletes
{: #performance-io-batch}

## Consistency impacts
{: #performance-io-consistency}

when writing the same object over and over in a short amount of time perhaps you might consider a different storage type as this will cause contention.

## Existence checks
{: #performance-io-head}

## Retry strategy
{: #performance-io-retry}