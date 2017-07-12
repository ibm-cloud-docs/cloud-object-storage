---
title: Introduction to object storage 
keywords: 
last_updated: November 18, 2016
tags: 
summary: 
sidebar: crs_sidebar
permalink: object-storage
folder: help
toc: false
---

Object storage is a modern storage technology concept and a logical progression from block and file storage. Object storage has been around since the late 1990s, but has gained market acceptance and success over the last 10 years. 

Object storage was invented to overcome a number of issues:

*  Managing data at very large scales using conventional block and file systems was difficult because these technologies lead to data islands due to limitations on various levels of the data management hardware and software stack.

*  Managing namespace at scale resulted in maintaining large and complex hierarchies, which are required to access the data. Limitations in nested structures on traditional block and file storage arrays further contributed to data islands being formed. 

*  Providing access security required a combination of technologies, complex security schemes, and significant human involvement in managing these areas.

Object storage, also known as object-based storage (OBS) uses a different approach to storing and referencing data. Object data storage concepts include the following three constructs:
 
*  Data: This is the user and application data that requires persistent storage. It can be text, binary formats, multimedia, or any other human- or machine-generated content.

*  Metadata: This is the data about the data. It includes some predefined attributes such as upload time and size. Object storage allows users to include custom metadata containing any information in key and value pairs. This information typically contains information that is pertinent to the user or application that is storing the data and can be amended at any time. A unique aspect to metadata handling in object storage systems is that metadata is stored with the object. 

*  Key: A unique resource identifier is assigned to every object in an OBS system. This key allows the object storage system to differentiate objects from one another and is used to find the data without needing to know the exact physical drive, array, or site where the data is.

This approach allows object storage to store data in a simple, flat hierarchy, which alleviates the need for large,
performance-inhibiting metadata repositories. 

Data access is achieved by using a REST interface over the HTTP protocol, which allows anywhere and anytime access simply by referencing the object key.