---
title: Data security and IBM COS
keywords: 
last_updated: November 18, 2016
tags: 
summary: 
sidebar: crs_sidebar
permalink: security
redirect_from:
  - /crs-security
  - /crs-security.html
folder: help
toc: false
---

### Overview

IBM Cloud Object Storage uses an innovative approach for cost-effectively storing large volumes of unstructured data while ensuring security, availability and reliability. This is accomplished by using Information Dispersal Algorithms (IDAs) to separate data into unrecognizable “slices” that are distributed across a network of data centers, making transmission and storage of data inherently private and secure. No complete copy of the data resides in any single storage node, and only a subset of nodes needs to be available in order to fully retrieve the data on the network.

Objects in IBM COS are encrypted at rest. This technology individually encrypts each object using per-object generated keys. These keys are secured and reliably stored using the same Information Dispersal Algorithms that protect object data using an All-or-Nothing Transform (AONT), which prevents key data from being disclosed if individual nodes or hard drives are compromised.

Storage can be accessed over HTTPS, and internally storage devices are certified and communicate with each other using TLS.


### Data deletion

After data is deleted various mechanisms exist which prevent recovery or reconstruction of the deleted objects. The deletion of an object undergoes various stages, from marking the meta-data indicating the object as deleted, to removing the content regions, to the finalization of the erasure on the drives themselves until the eventual overwriting the blocks representing that slice data. Depending on whether one compromised the data center or has possession of the physical disks, the time an object becomes unrecoverable depends on the phase of the delete operation. When the metadata object is updated, clients external from the data center network can no longer read the object. When a majority of slices representing the content regions have been finalized by the storage devices, it is not possible to access the object.

### Tenant isolation

IBM COS Cross-Region is a shared infrastructure, multi-tenant object storage solution. If your workload requires dedicated or isolated storage, visit [IBM Cloud](https://www.ibm.com/cloud-computing/products/storage/object-storage/flexible-deployment/) for more information.
