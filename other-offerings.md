---

copyright:
  years: 2014, 2017
lastupdated: "2017-02-23"

---

# Other IBM object storage services

In addition to COS, IBM Cloud currently provides two additional object storage offerings for different user needs, all of which are accessible through web-based portals and REST APIs.

| Offering | Interface | Defining advantage |
|-- |-- |-- |
| IBM Cloud Object Storage | S3 API | -- Highest availability, durability and resiliency<br> -- Encryption at rest and in flight|
| OpenStack Swift Object Storage | Swift API | -- Native integration with OpenStack clouds <br> -- Choice of 20 geographic regions|
| Object Storage for IBM Bluemix Developer Services | Swift API | -- Native integration with Bluemix services |
{:.overviewtable}

## OpenStack Swift Object Storage 

Information stored with OpenStack Swift Object Storage is located in one of 20 global data centers. Developers use the community Swift API to interact with their storage accounts. This offering is managed through the IBM Bluemix Infrastructure Control portal and does not provide encryption at-rest.

## Object Storage for IBM Bluemix Developer Services

Information stored with Object Storage for IBM Bluemix is located in either Dallas or London data centers, and storage accounts are available for binding to Bluemix services. Based on the OpenStack Swift platform, developers use the community Swift API to interact with their storage accounts. This offering is managed through the IBM Bluemix console and does not provide encryption at-rest.


