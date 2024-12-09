---

copyright:
  years: 2024
lastupdated: "2024-12-09"

keywords: HA for cloud object storage, DR for cloud object storage, cloud object storage recovery time objective, cloud object storage recovery point objective

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}



# Understanding high availability and disaster recovery for {{site.data.keyword.cos_full}}
{: #cos-ha-dr}





[High availability](#x2284708){: term} (HA) is the ability for a service to remain operational and accessible in the presence of unexpected failures. [Disaster recovery](#x2113280){: term} is the process of recovering the service instance to a working state.
{: shortdesc}



{{site.data.keyword.cos_full_notm}} is a global service that allows you to configure storage data resiliency while maintaining high availability. For more information, see [Service Level Agreement (SLA)](https://www.ibm.com/support/customer/csol/terms/?id=i126-9268&lc=en). You can also find the available region and data center locations in the [Service and infrastructure availability by local](/docs/overview?topic=overview-services_region) documentation.



## High availability architecture
{: #cos-ha-architecture}

{{site.data.keyword.cos_short}} is a global service and you have a choice to configure the storage resiliency. A bucket's resiliency is defined by the endpoint used to create it i.e. Cross Region, Regional and Single Site.

- Cross Region resiliency will spread your data across several metropolitan areas,

- Regional resiliency will spread data across a single metropolitan area

- Single Data Center resiliency spreads data across multiple appliances within a single data center.

Regional and Cross Region buckets can maintain availability during a site outage without any configuration changes required so it is recommended to use these storage bucket resiliency settings when configuring your workloads for high availability. Data stored in a single site is still distributed across many physical storage appliances, but is contained within a single data center.



### High availability features
{: #cos-ha-features}

{{site.data.keyword.cos_short}} provides the following capabilities to help you plan for high availability in the event of an outage:



| Feature | Description | Consideration |
| -------------- | -------------- | -------------- |
| Storage Bucket Resiliency | Ability to configure specific resiliency choice for customer data. | IBM Cloud Object Storage buckets that are created at a regional endpoint distribute data across three data centers that are spread across a metro area. Any one of these data centers can suffer an outage or even destruction without impacting availability.<br></br><br>Buckets that are created at a cross-region endpoint distribute data across three regions in a geographical location. Any one of these regions can suffer an outage or even destruction without impacting availability.Requests are routed to the nearest cross-region metropolitan area by using Border Gateway Protocol (BGP) routing.</br> <br>Refer to [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) for more information.</br>|
| Replication | Replication copies newly created objects and object updates from a source bucket to a target bucket and allows you to define rules for automatic, asynchronous copying of objects. | To ensure you have a backup copy available in the event of a disaster, it is recommended that replication be configured and setup. Learn more about [Tracking replication events](/docs/cloud-object-storage?topic=cloud-object-storage-replication-overview). |
{: caption="HA features for {{site.data.keyword.cos_short}}" caption-side="bottom"}

## Disaster recovery architecture
{: #disaster-recovery-intro}






### Disaster recovery features
{: #dr-features}

### Your responsibilities for HA and DR
{: #cos-feature-responsibilities}

The following information can help you create and continuously practice your plan for HA and DR.



| Responsibility | Description |
| -------------- | ----------------------------------------- |
| Resiliency | Provision Object Storage buckets with the appropriate resiliency option, storage class, data locality, and optional configurations necessary for the specific workload and use case. |
| Data Backup | Ensure customer data back-ups if required as per your organization requirements. |
| Network | Monitor and manage non-IBM network resources to ensure appropriate access to IBM Cloud service endpoints including capacity and availability. |
| Using IBM Cloud KMS for adding envelop encryption | If you are using IBM Cloud Key Protect or Hyper Protect Crypto Services to add envelop encryption, ensure to review the respective High Availability and Disaster Recovery documentation to fully understand the implications. You may be required to use a key instance location that has a key replica which can be used in the event of a failover. Please also ensure to review the appropriate licensing and plan information. |
{: caption="Your responsibilities for HA and DR" caption-side="bottom"}

To find out more about responsibility ownership between the customer and {{site.data.keyword.cos_short}}, refer to [Your responsibilities when using {{site.data.keyword.cos_short}}(/docs/cloud-object-storage?topic=cloud-object-storage-responsibilities)].

## Recovery time objective (RTO) and recovery point objective (RPO)
{: #rto-rpo-features}

IBM Cloud Object Storage offering has plans in place to provide for the recovery of both the Cloud Service, and the associated Content, within hours in the event of a corresponding disaster.



### Change management
{: #change-management}

Change management includes tasks such as upgrades, configuration changes, and deletion. In order to ensure users are given access as per role requirements, please review [Getting Started with IAM](/docs/cloud-object-storage?topic=cloud-object-storage-iam).

It is recommended that you grant users and processes the IAM roles and actions with the least privilege required for their work. See [How can I prevent accidental deletion of services?](https://github.ibm.com/cloud-docs/cloud-object-storage/blob/source/docs/resiliency?topic=resiliency-dr-faq#prevent-accidental-deletion).



## How {{site.data.keyword.IBM}} helps ensure disaster recovery
{: #ibm-disaster-recovery}



{{site.data.keyword.IBM}} takes specific recovery actions in the case of a disaster.

### How {{site.data.keyword.IBM_notm}} recovers from zone or regional failures
{: #ibm-zone-regional-failure}


In the event of a zone failure IBM Cloud will resolve the zone outage and when the zone comes back on-line, the global load balancer will resume sending API requests to the restored instance node without need for customer action.

Single data centers offer no resiliency in a site outage or destruction.
[: note]

## How {{site.data.keyword.IBM_notm}} maintains services
{: #ibm-service-maintenance}


All upgrades follow the {{site.data.keyword.IBM_notm}} service best practices and have a recovery plan and rollback process in-place. Regular upgrades for new features and maintenance occur as part of normal operations. Such maintenance can occasionally cause short interruption intervals that are handled by [client availability retry logic](/docs/resiliency?topic=resiliency-high-availability-design#client-retry-logic-for-ha). Changes are rolled out sequentially, region by region and zone by zone within a region. Updates are backed out at the first sign of a defect.


Complex changes are enabled and disabled with feature flags to control exposure.


Changes that impact customer workloads are detailed in notifications. For more information, see [monitoring notifications and status](/docs/account?topic=account-viewing-cloud-status) for planned maintenance, announcements, and release notes that impact this service.
