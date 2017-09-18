---

copyright:
  years: 2014, 2017
lastupdated: "2017-02-23"

---

# About IBM COS

Information stored with IBM Cloud Object Storage is encrypted and dispersed across multiple geographic locations, and accessed over HTTP using a REST API. This service makes use of the distributed storage technologies provided by IBM's Cloud Object Storage System (formerly Cleversafe).

IBM COS is available in two configurations: Cross Region and Regional.  Cross Region service provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US and EU. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US South. If a given region or availability zone is unavailable, the object store continues to function without impediment, and any missed changes are applied when the data center comes back online.

Developers use IBM COS API (based on the S3 API) to interact with their storage accounts. This documentation provides support to get started with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions.

## Understanding IBM Cloud

Until recently, IBM offered Platform-as-a-Service and Infrastructure-as-a-Service in two distinct environments: [Bluemix][1]{: new_window} and [SoftLayer][2]{: new_window}.  Now these distinctions are fading as all cloud services are united in a single robust IBM Cloud platform with direct access to compute resources (containers, 12 factor apps, VMs, VSIs, serverless, bare metal), storage (block, file, and object), data management (databases and analytics engines), networking (load balancers, firewalls, DNS), DevOps tools (logging, monitoring), blockchain, an IoT platform, Watson services, and more.

## Other IBM object storage services

In addition to COS, IBM Cloud currently provides two additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs.

| Offering | Interface | Defining advantage |
|-- |-- |-- |
| IBM Cloud Object Storage | S3 API | -- Highest availability, durability and resiliency<br> -- Encryption at rest and in flight|
| OpenStack Swift Object Storage | Swift API | -- Native integration with OpenStack clouds <br> -- Choice of 20 geographic regions|
| Object Storage for IBM Bluemix Developer Services | Swift API | -- Native integration with Bluemix services |


## OpenStack Swift Object Storage

Information stored with OpenStack Swift Object Storage is located in one of 20 global data centers. Developers use the community Swift API to interact with their storage accounts. This offering is managed through the IBM Bluemix Infrastructure Control portal and does not provide encryption at-rest.

## Object Storage for IBM Bluemix Developer Services

Information stored with Object Storage for IBM Bluemix is located in either Dallas or London data centers, and storage accounts are available for binding to Bluemix services. Based on the OpenStack Swift platform, developers use the community Swift API to interact with their storage accounts. This offering is managed through the IBM Bluemix console and does not provide encryption at-rest.

## About this documentation

This documentation is for anyone interested in learning about how to use IBM COS to manage unstructured data IO in their cloud applications, to store large files for machine learning and data analysis, or simply as a tool for backup and other IT services.

### Prerequisites

The code examples throughout assume a basic comfortability with a Linux-like shell interface (such as [bash](https://www.gnu.org/software/bash/), the [OSX Terminal](http://www.imore.com/how-use-terminal-mac-when-you-have-no-idea-where-start), or the [Windows 10 WSL](https://msdn.microsoft.com/en-us/commandline/wsl/install_guide).  For more information on getting started with shell environments, visit [this primer on shell programming fundamentals](https://www.ibm.com/developerworks/library/l-bash/). Any example code is intended merely as an educational demonstration and is in no way intended to serve as a template or boilerplate for production code.
