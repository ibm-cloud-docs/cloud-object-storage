---

copyright:
  years: 2026
lastupdated: "2026-01-08"

keywords: HA for cloud object storage, DR for cloud object storage, cloud object storage recovery time objective, cloud object storage recovery point objective

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}



# Understanding high availability and disaster recovery for {{site.data.keyword.cos_full}}
{: #cos-ha-dr}





[High availability](#x2284708){: term} (HA) is the ability for a service to remain operational and accessible in the presence of unexpected failures. <br>[Disaster recovery](#x2113280){: term} is the process of recovering the service instance to a working state.
{: shortdesc}



{{site.data.keyword.cos_full_notm}} is a global service that allows you to configure storage data resiliency while maintaining high availability. For more information, see [Service Level Agreement (SLA)](https://www.ibm.com/support/customer/csol/terms/?id=i126-9268&lc=en). You can also find the available region and data center locations in the [Service and infrastructure availability by local](/docs/overview?topic=overview-services_region) documentation.



## High availability architecture
{: #cos-ha-architecture}

{{site.data.keyword.cos_short}} is a global service and you have a choice to configure the storage resiliency. A bucket's resiliency is defined by the endpoint that is used to create it, i.e., Cross Region, Regional, and Single Site.

- Cross-Region resiliency will spread your data across several metropolitan areas

- Regional resiliency will spread data across a single metropolitan area

- Single Data Center resiliency spreads data across multiple appliances within a single data center

Regional and Cross Region buckets can maintain availability during a site or zone outage without any configuration changes required so it is recommended to use these storage bucket resiliency settings when configuring your workloads for high availability. Data that is stored in a single site is still distributed across many physical storage appliances, but is contained within a single data center without any zonal support.



### High availability features
{: #cos-ha-features}

{{site.data.keyword.cos_short}} provides the following capabilities to help you plan for high availability in the event of an outage:



| Feature | Description | Consideration |
| -------------- | -------------- | -------------- |
| Storage Bucket Resiliency | Ability to configure specific resiliency choice for customer data. | {{site.data.keyword.cos_short}} buckets that are created at a regional endpoint distribute data across three or more zones that are contained in a metro area. Any one of these zones can suffer an outage or even destruction without impacting availability.<br></br><br>Buckets that are created at a cross-region endpoint distribute data across three regions in a geographical location. Any one of these regions can suffer an outage or even destruction without impacting availability.<br><br>Requests are routed to the nearest cross-region metropolitan area by using Global Server Load Balancing (GSLB).</br> <br>Refer to [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints) for more information.</br>|
| Replication | Replication copies newly created objects and object updates from a source bucket to a target bucket and allows you to define rules for automatic, asynchronous copying of objects. | To ensure that you have a backup copy available in the event of a disaster, it is recommended that replication be configured and setup. Learn more about [Tracking replication events](/docs/cloud-object-storage?topic=cloud-object-storage-replication-overview). |
{: caption="HA features for {{site.data.keyword.cos_short}}" caption-side="bottom"}

## Disaster recovery architecture
{: #disaster-recovery-intro}


Cross-region, regional, and single site buckets offer varying levels of tolerance against specific disaster scenarios. Choose the right resiliency model for your bucket that aligns with your business’s disaster recovery requirements. For many disaster scenarios at the data center or regional level, IBM plans to target a recovery time of the service and associated content in less than 24 hours with an RPO of 1 hour. 

Additional optional architectures can be implemented by the customer to improve recovery times.

For example, to recover from the unlikely event of a complete COS regional outage where restoration of the original data is not possible, a duplicated bucket could be created in an alternate region. Waiting for IBM Cloud to recover an affected region or service is also a valid path, but remember it can take many hours or longer and there may be data loss depending on the disaster scenario.

The duplicated bucket can be configured to mirror the production bucket but with updated references to regional services. For instance, if using Key Protect, a duplicated bucket in Madrid should reference root keys that are stored in a Madrid Key Protect Instance. In the case of a disaster scenario, where waiting for IBM to fully recover the region is not possible, customers can repopulate this duplicated bucket with a backup copy of the original data in the source bucket.  Alternatively, customers can set up replication rules prior to any outage to keep data in-sync between the source bucket and the duplicated bucket. For the highest level of resiliency, such a duplicated bucket should be created before the fact (before any potential disaster) and kept in-sync with the source bucket that uses replication. Use of the object replication feature should be considered along with the resiliency model of your source bucket and your overall business disaster recovery objectives.

Plan for the recovery into a recovery region. The replicated bucket should align with the workload [disaster recovery approaches](/docs/resiliency?topic=resiliency-dr-approaches) within IBM Cloud. If the disaster does not impact the production source bucket’s configuration or availability (for example just data loss), it may be possible for a customer to repair the data in the source bucket in place. In the case that failover to a replicated bucket is required, the client application will need to be reconfigured to call the endpoint of the target-replicated bucket.



### Disaster recovery features
{: #dr-features}

IBM COS provides the following disaster recovery features that can be configured by customers:

| Feature | Description | Consideration |
| -------------- | -------------- | -------------- |
| Object Replication | Replication copies newly created objects and object updates from a source bucket to a target bucket and allows you to define rules for automatic, asynchronous copying of objects. | To ensure that you have a second copy available in the event of a disaster, you can configure replication between a production bucket and a target recovery bucket. Depending on your business’s resiliency requirements, replication may not be required if using Cross-Region or Regional buckets. Learn more about [Tracking replication events](/docs/cloud-object-storage?topic=cloud-object-storage-replication-overview).  |
| Object Versioning | Enable object versioning to maintain previous versions of objects that can be restored in the case of data corruption or deletion. | Customers can enable object versioning on buckets and restore old versions in the event of data corruption. The bucket must be available to perform version recovery. [Learn More](/docs/cloud-object-storage?topic=cloud-object-storage-versioning) |
| Object Lock | Object Lock prevents deletion of object versions during a specified retention period. | Enable object lock to protect against accidental or unauthorized object deletions or overwrites. Ensure that safe object versions are available for recovery. [Learn More](/docs/cloud-object-storage?topic=cloud-object-storage-versioning)  |
{: caption="DR features for {{site.data.keyword.cos_short}}" caption-side="bottom"}

Other disaster recovery options are created and supported by the customer.

| Feature | Description | Consideration |
| -------------- | -------------- | -------------- |
| Backup and restore | Use scripts or 3rd-party backup applications to back up data in source buckets to a recovery region. | Customer must host and manage any script or 3rd-party backup solution to back up data stored in COS buckets. |
{: caption="Customer DR features for {{site.data.keyword.cos_short}}" caption-side="bottom"}

### Planning for DR
{: #features-for-disaster-recovery}

The DR steps must be practiced regularly. As you build your plan, consider the following failure scenarios and resolutions.

| Failure | Resolution |
| -------------- | -------------- |
| Hardware failure (single point) | {{site.data.keyword.cos_short}} buckets are resilient from single point hardware failures within a zone. No configuration required. |
| Data center failure | Cross-region and regional COS buckets are resilient to individual data center failures. No configuration or failover is required by the customer.<br><br>Customers with buckets in single data center zones can configure replication or use 3rd party backup solutions to ensure that a safe copy of data is available outside the zone. Waiting for IBM Cloud to recover an affected region or service is also a valid path, but remember it can take many hours or longer depending on the nature of the data center outage. |
| Data corruption | Use object versioning, object replication, or 3rd party back up solutions to ensure that uncorrupted versions of objects exist to recover from in the case of data corruption or accidental deletion. |
| Regional failure | Cross-region COS buckets are resilient to regional failures. Some integrated regional services like Key Protect may require additional failover steps for cross-region buckets. <br><br>Customers with buckets in regional or single data center COS buckets should follow the disaster recovery steps above in the case of total regional failures. Waiting for IBM Cloud to recover an affected region or service is also a valid path, but remember it can take many hours or longer depending on the nature of the regional outage. |
{: caption="DR scenarios for {{site.data.keyword.cos_short}}" caption-side="bottom"}

### Use of IBM Cloud Key Management Service for adding envelop encryption:
{: #use-kms}

If you are using any other IBM Cloud service integration, for example IBM Cloud Key Management Service like Key Protect or Hyper Protect, to add envelop encryption, you will need to ensure that the appropriate configuration plan for key replica is used. This is essential when using a Cross Region configuration which ensures a replica key is available in the event of an outage. Refer to Key Protect documentation for [high availability and disaster recovery](/docs/key-protect?topic=key-protect-ha-dr).

### Your responsibilities for HA and DR
{: #cos-feature-responsibilities}



| Responsibility | Description |
| -------------- | ----------------------------------------- |
| Resiliency | Provision Object Storage buckets with the appropriate resiliency option, storage class, data locality, and optional configurations necessary for the specific workload and use case. |
| Data Backup | Ensure customer data backups if required as per your organization requirements. |
| Network | Monitor and manage non-IBM network resources to ensure appropriate access to IBM Cloud service endpoints including capacity and availability. |
| Using IBM Cloud KMS for adding envelop encryption | If you are using IBM Cloud Key Protect or Hyper Protect Crypto Services to add envelop encryption, ensure to review the respective High Availability and Disaster Recovery documentation to fully understand the implications. You may be required to use a key instance location that has a key replica which can be used in the event of a failover. Please also ensure to review the appropriate licensing and plan information. |
{: caption="Your responsibilities for HA and DR" caption-side="bottom"}

To find out more about responsibility ownership between the customer and {{site.data.keyword.cos_short}}, refer to [Your responsibilities when using {{site.data.keyword.cos_short}}(/docs/cloud-object-storage?topic=cloud-object-storage-responsibilities)].

## Recovery time objective (RTO) and recovery point objective (RPO)
{: #rto-rpo-features}

IBM Cloud Object Storage offering has plans in place to provide for the recovery of both the Cloud Service, and the associated Content, which happens within hours in the event of a corresponding disaster.

| Feature | RTO and RPO |
| -------------- | -------------- |
| Recover from hardware failure (single point) | RTO = 0, RPO = 0 for all resiliency models |
| Recover from data center outage | RTO = 0, RPO = 0 for Cross-Region and Regional resiliency models |
| Restore previous object version | RTO = seconds, RPO = near 0 |
| Recover to bucket in separate region with active replication | RTO = minutes, scripting may improve time further and also consider time to adjust workloads to target the recovery bucket, , <br><br>RPO = near 1 hour |
| Recover to new bucket in new region without active replication | RTO = minutes to days, consider the amount of time to reconfigure a new bucket and to adjust workloads to target the new bucket endpoint. Also consider time to populate the bucket with a copy of the original data. RPO is subject to the customer’s backup and recovery plan |
{: caption="RTO/RPO features for {{site.data.keyword.cos_short}}" caption-side="bottom"}



### Change management
{: #change-management}

Change management includes tasks such as upgrades, configuration changes, and deletion. In order to ensure that users are given access as per role requirements, please review [Getting Started with IAM](/docs/cloud-object-storage?topic=cloud-object-storage-iam).

It is recommended that you grant users and processes the IAM roles and actions with the least privilege that is required for their work. See [How can I prevent accidental deletion of services?](/docs/cloud-object-storage?topic=cloud-object-storage-versioning)



## How {{site.data.keyword.IBM}} helps ensure disaster recovery
{: #ibm-disaster-recovery}



{{site.data.keyword.IBM}} takes specific recovery actions in the case of a disaster.


- Recovery from zone or regional failures<br>In the event of a zone failure IBM Cloud will resolve the zone outage and when the zone comes back on-line, the global load balancer will resume sending API requests to the restored instance node without need for customer action.</br>
- {{site.data.keyword.IBM}} conducts annual tests of various disaster scenarios and continuously refines our recovery documentation based on findings that are found during these tests.
- 24 × 7 global support is available to customers with {{site.data.keyword.IBM}} Subject Matter Experts who are on call to help in the case of a disaster.  <br>All {{site.data.keyword.IBM}} Subject Matter Experts are trained annually on business continuity and disaster recovery policies and procedures to ensure preparedness in the event of a disaster.</br>

## How {{site.data.keyword.IBM_notm}} maintains services
{: #ibm-service-maintenance}


All upgrades follow the {{site.data.keyword.IBM_notm}} service best practices and have a recovery plan and rollback process in-place. Regular upgrades for new features and maintenance occur as part of normal operations. Such maintenance can occasionally cause short interruption intervals that are handled by [client availability retry logic](/docs/resiliency?topic=resiliency-high-availability-design#client-retry-logic-for-ha). Changes are rolled out sequentially, region by region and zone by zone within a region. Updates are backed out at the first sign of a defect.


Complex changes are enabled and disabled with feature flags to control exposure.


Changes that impact customer workloads are detailed in notifications. For more information, see [monitoring notifications and status](/docs/account?topic=account-viewing-cloud-status) for planned maintenance, announcements, and release notes that impact this service.
