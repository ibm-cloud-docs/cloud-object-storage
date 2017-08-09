---

copyright:
  years: 2014, 2017
lastupdated: "2017-02-23"

---

# About IBM COS

Information stored with IBM Cloud Object Storage is encrypted and dispersed across multiple geographic locations, and accessed through an implementation of the S3 API. This service makes use of the distributed storage technologies provided by IBM's Cloud Object Storage System (formerly Cleversafe). 

IBM COS is available in two configurations: Cross Region and Regional.  Cross Region service provides higher durability and availability than using a single region at the cost of slightly higher latency, and is available today in the US and EU. Regional service reverses those tradeoffs, and distributes objects across multiple availability zones within a single region, and is available in the US South. If a given region or availability zone is unavailable, the object store continues to function without impediment, and any missed changes are applied when the data center comes back online.

Developers use IBM COS's implementation of the S3 API to interact with their storage accounts. This documentation provides support to get started with provisioning accounts, to create buckets, to upload objects, and to use a reference of common API interactions. This offering is managed through the IBM Bluemix Infrastructure (formerly SoftLayer) Control portal.

## About this documentation

This documentation is for anyone interested in learning about how to use IBM COS to manage unstructured data IO in their cloud applications, to store large files for machine learning and data analysis, or simply as a tool for backup and other IT services. 

### Prerequisites

The code examples throughout assume a basic comfortability with a Linux-like shell interface (such as [bash](https://www.gnu.org/software/bash/), the [OSX Terminal](http://www.imore.com/how-use-terminal-mac-when-you-have-no-idea-where-start), or the [Windows 10 WSL](https://msdn.microsoft.com/en-us/commandline/wsl/install_guide).  For more information on getting started with shell environments, visit [this primer on shell programming fundamentals](https://www.ibm.com/developerworks/library/l-bash/). Any example code is intended merely as an educational demonstration and is in no way intended to serve as a template or boilerplate for production code. 

Please contact `nicholas.lange@ibm.com` with questions or suggestions about the documentation itself.

