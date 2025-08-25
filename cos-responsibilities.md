---

copyright:
  years: 2017, 2019
lastupdated: "2024-04-07"

keywords: cloud object storage, cos, object storage, responsibilities, incident, operations, change, security, regulation, disaster recovery, management

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Your responsibilities when using {{site.data.keyword.cos_full_notm}}
{: #responsibilities}

Learn about the management responsibilities and terms and conditions that you have when you use {{site.data.keyword.cos_full}}. For overall terms of use, see [Cloud Services terms](/docs/overview?topic=overview-terms).
{: shortdesc}



## Operational responsibilities
{: #responsibilities-operational}

**IBM responsibilities**

- Maintaining SLAs as stated in {{site.data.keyword.cos_full_notm}} Service Description.
- Ensure client gets appropriate notifications regarding availability and downtime of its resources.
- Monitor service performance related metrics such as network, storage, and compute capacities.
- Monitor and manage service health and availability.

**Your responsibilities**

- Ensuring data back-ups if necessary.
- Mitigating potential accidental deletion of data. If applicable the client should store archive data in buckets configured with a data retention policy to help address data loss due to accidental deletion. IBM is unable to “un-delete” data.
- Monitor and manage non-IBM network resources to ensure appropriate access to {{site.data.keyword.cloud_notm}} service endpoints including capacity and availability.
- Provisioning {{site.data.keyword.cos_short}} buckets with the appropriate resiliency option, storage class, data locality, and optional configurations necessary for the specific workload and use case. 

## Managing infrastructure and the cloud environment
{: #responsibilities-infrastructure}

**IBM responsibilities**

- Deployment, provisioning, managing {{site.data.keyword.cos_full_notm}} resources offered to clients.
- Ensuring availability of adequate resources to allow for scaling of client resources for client usage requirements.
- All physical and environmental controls.

**Your responsibilities**

- Ensure proper network access and capacity is available and can reach the designated {{site.data.keyword.cos_short}} service endpoints. (private or public).
- Ensure applications can interact with the COS S3 API either via native support or with the addition of a hardware or software gateway solution.

## Security
{: #responsibilities-security}

**IBM responsibilities**

- Security and monitoring of the environment of IBM data center security guidelines.
- Encrypting client data when at-rest and in-transit between different {{site.data.keyword.cloud_notm}} data center locations as part of resiliency options offered to clients.
- Continuous monitoring of resources to check for vulnerabilities and security breaches.

**Your responsibilities**

- Client controls access to {{site.data.keyword.cos_full_notm}} provisioned resources and is responsible for ensuring access to client data is only provided to appropriate resources within the client organization.
- After retrieving data from IBM hosted {{site.data.keyword.cos_short}}, client takes full responsibility to ensure the security and privacy of any local copies maintained.
- If applicable, manage customer provided encryption keys via S3 API or through IBM hosted key management services.
- Scanning of user data for viruses, malware, etc prior to uploading objects to the IBM Storage platform.

## Compliance
{: #responsibilities-compliance}

**IBM responsibilities**

- {{site.data.keyword.cos_full_notm}} to maintain controls commensurate with the compliance certifications/attestations as stated in official data sheets.

**Your responsibilities**

- Client responsibility to ensure and seek appropriate legal guidance in order to validate its compliance with pertinent industry compliance certifications and regulations.
- Client to ensure its use of Object Storage resources are inline with the terms and conditions set forth in the {{site.data.keyword.cloud_notm}} Service Description and any other associated transaction documents.
