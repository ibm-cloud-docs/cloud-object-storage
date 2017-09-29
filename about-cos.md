---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# About IBM COS

Information stored with IBM Cloud Object Storage is encrypted and dispersed across multiple geographic locations, and accessed over HTTP using a REST API. This service makes use of the distributed storage technologies provided by IBM's Cloud Object Storage System (formerly Cleversafe).

IBM COS is available in two configurations: Cross Region and Regional.  Cross Region service provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US and EU. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US South. If a given region or availability zone is unavailable, the object store continues to function without impediment, and any missed changes are applied when the data center comes back online.

Developers use IBM COS API to interact with their storage accounts. This documentation provides support to get started with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions.

Users of the original COS IaaS service provisioned through SoftLayer, [please visit this link for updates on the transition to Bluemix](docs/services/cloud-object-storage/classic/iaas.html).



## Other IBM object storage services

In addition to COS, IBM Cloud currently provides two additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs.

| Offering                        | Interface | Defining advantage                             |
|---------------------------------|-----------|------------------------------------------------|
| IBM Cloud Object Storage        | COS API   | For cloud-native development.                  |
| OpenStack Swift (IaaS)          | Swift API | For workloads requiring specific regions.      |
| OpenStack Swift (Cloud Foundry) | Swift API | Native integration with Cloud Foundry services |


## OpenStack Swift Object Storage (IaaS)

Information stored with OpenStack Swift Object Storage is located in one of 20 global data centers. Developers use the community Swift API to interact with their storage accounts. This offering is managed through the IBM Bluemix Infrastructure Control portal and does not provide encryption at-rest.

For more information on this object storage service, [view the documentation](/docs/infrastructure/objectstorage-swift/index.html).

## Swift Object Storage for Cloud Foundry (PaaS)

Information stored with Object Storage for IBM Bluemix is located in either Dallas or London data centers, and storage accounts are available for binding to Bluemix services. Based on the OpenStack Swift platform, developers use the community Swift API to interact with their storage accounts. This offering is managed through the IBM Bluemix console and does not provide encryption at-rest.

For more information on this object storage service, [view the documentation](/docs/services/ObjectStorage/index.html).

## About this documentation

This documentation is for anyone interested in learning about how to use IBM COS to manage unstructured data IO in their cloud applications, to store large files for machine learning and data analysis, or simply as a tool for backup and other IT services.

### Prerequisites

The code examples throughout assume a basic comfortability with a Linux-like shell interface (such as [bash](https://www.gnu.org/software/bash/), the [OSX Terminal](http://www.imore.com/how-use-terminal-mac-when-you-have-no-idea-where-start), or the [Windows 10 WSL](https://msdn.microsoft.com/en-us/commandline/wsl/install_guide).  For more information on getting started with shell environments, visit [this primer on shell programming fundamentals](https://www.ibm.com/developerworks/library/l-bash/). Any example code is intended merely as an educational demonstration and is in no way intended to serve as a template or boilerplate for production code.
