---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: about, basics

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
{:help: data-hd-content-type='help'}

# About Object Storage
{: #about-cos}

{{site.data.keyword.cos_full}} is a logical progression from block and file storage, and was invented to overcome a number of issues.
{: shortdesc}

## What is Object Storage?
{: #about-cos-defined}
{: help}

{{site.data.keyword.cos_short}} solves the following problems with block and/or file storage:

*  Managing data at extreme scales by using conventional block and file systems was difficult. These technologies lead to data islands because of limitations on various levels of the data management hardware and software stack.

*  Managing namespace at scale that is resulted in maintaining large and complex hierarchies, which are required to access the data. Limitations in nested structures on traditional block and file storage arrays further contributed to data islands.

*  Security required a combination of technologies, complex security schemes, and significant human involvement.

## What approach does {{site.data.keyword.cos_full_notm}} use?
{: #about-cos-defined}
{: help}

{{site.data.keyword.cos_short}} uses a different approach to storing and referencing data:

*  User and application data requires persistent storage. It can be text, binary formats, multimedia, or any other human- or machine-generated content.

*  Metadata is the data about the data. It includes some predefined attributes such as upload time and size. Object Storage allows users to include custom metadata in key and value pairs. Typically this information is pertinent to the user or application that is storing the data and can be amended at any time. A unique aspect to metadata handling in Object Storage systems is that metadata is stored with the object.

*  A unique resource identifier (a key) is assigned to every object in the system. This key allows the Object Storage system to differentiate objects from one another. Additionally, you can find data without needing to know an exact physical drive, array, or site.

This approach allows Object Storage to store data in a simple, flat hierarchy, which reduces the need for large,
performance-inhibiting metadata repositories.

Data access is achieved by using a REST interface over the HTTP protocol, which allows anywhere and anytime access by referencing the object key.
