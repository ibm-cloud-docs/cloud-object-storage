---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-22"

keywords: cloud object storage, cos, object storage, responsibilities, incident, operations, change, security, regulation, disaster recovery, management

subcollection: cloud-object-storage

---

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:screen: .screen}
{:pre: .pre}
{:table: .aria-labeledby="caption"}
{:codeblock: .codeblock}
{:tip: .tip}
{:note: .note}
{:important: .important}
{:deprecated: .deprecated}
{:download: .download}
{:preview: .preview}

# Your responsibilities when using {{site.data.keyword.cos_full_notm}}
{: #responsibilities}

Learn about the management responsibilities and terms and conditions that you have when you use {{site.data.keyword.cos_full_notm}}. For overall terms of use, see [Cloud Services terms](/docs/overview/terms-of-use?topic=overview-terms#terms).
{: shortdesc}

## Operational responsibilities  
{: #responsibilities-operational}

**IBM responsibilities**
- Maintaining SLA’s as stated in {{site.data.keyword.cos_full_notm}} Service Description.
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
- Ensuring availability of adequate resources to allow for scaling of client resources as per client usage requirements.
- All physical and environmental controls.

**Your responsibilities**
- Ensure proper network access and capacity is available and can reach the designated {{site.data.keyword.cos_short}} service endpoints. (private or public).
- Ensure applications can interact with the COS S3 API either via native support or with the addition of a hardware or software gateway solution.

## Security
{: #responsibilities-security}

**IBM responsibilities**
- Security and monitoring of the environment as per IBM data center security guidelines.
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
- {{site.data.keyword.cos_full_notm}} to maintain controls commensurate with the compliance certifications/attestations as stated in official datasheets.

**Your responsibilities**
- Client responsibility to ensure and seek appropriate legal guidance in order to validate its compliance with pertinent industry compliance certifications and regulations.
- Client to ensure its use of Object Storage resources are inline with the terms and conditions set forth in the {{site.data.keyword.cloud_notm}} Service Description and any other associated transaction documents.







































# Your responsibilities with using {{site.data.keyword.containerlong_notm}}
{: #responsibilities_iks}

Learn about cluster management responsibilities that you have when you use {{site.data.keyword.containerlong}}. For overall terms of use, see [Cloud Services terms](/docs/overview/terms-of-use?topic=overview-terms#terms).
{: shortdesc}



## Overview of shared responsibilities
{: #overview-by-resource}

{{site.data.keyword.containerlong_notm}} is a managed service in the [{{site.data.keyword.cloud_notm}} shared responsibility model](/docs/overview/terms-of-use?topic=overview-shared-responsibilities#self-managed-responsibilities). Review the following table of who is responsible for particular cloud resources when using {{site.data.keyword.containerlong_notm}}. Then, you can view more granular tasks for shared responsibilities in [Tasks for shared responsibilities by area](#task-responsibilities).
{: shortdesc}

If you use other {{site.data.keyword.cloud_notm}} products such as {{site.data.keyword.cos_short}}, responsibilities that are marked as yours in the following table, such as disaster recovery for Data, might be IBM's or shared. Consult those products' documentation for your responsibilities.
{: note}

| Resource | [Incident and operations management](#incident-and-ops) | [Change management](#change-management) | [Identity and access management](#iam-responsibilities) | [Security and regulation compliance](#security-compliance) | [Disaster Recovery](#disaster-recovery) |
| - | - | - | - | - | - |
| Data | You | You | You | You | You |
| Applications | [Shared](#incident-and-ops) | [Shared](#change-management) | [Shared](#iam-responsibilities) | You | You |
| Container platform version | IBM | [Shared](#change-management) | IBM | IBM | IBM |
| Cluster networking | [Shared](#incident-and-ops) | IBM | IBM | IBM | IBM |
| Operating system | IBM | [Shared](#change-management) | IBM | IBM | IBM |
| Worker nodes | [Shared](#incident-and-ops) | IBM | IBM | IBM | IBM |
| Master | IBM | IBM | IBM | IBM | IBM |
| Service | IBM | IBM | IBM | IBM | IBM |
| Virtual storage | IBM | IBM | IBM | IBM | IBM |
| Virtual network | IBM | IBM | IBM | IBM | IBM |
| Hypervisor | IBM | IBM | IBM | IBM | IBM |
| Physical servers and memory | IBM | IBM | IBM | IBM | IBM |
| Physical storage | IBM | IBM | IBM | IBM | IBM |
| Physical network and devices | IBM | IBM | IBM | IBM | IBM |
| Facilities and Data Centers | IBM | IBM | IBM | IBM | IBM |
{: summary="The rows are read from left to right. The resource area of comparing responsibilities is in the first column. The next five columns describe whether you, IBM, or both have shared responsibilities for a particular area."}
{: caption="Table 1. Responsibilities by resource." caption-side="top"}

## Tasks for shared responsibilities by area
{: #task-responsibilities}

After reviewing the [overview](#overview-by-resource), see what tasks you and IBM share responsibility for each area and resource when you use {{site.data.keyword.containerlong_notm}}.
{: shortdesc}

### Incident and operations management
{: #incident-and-ops}

You and IBM share responsibilities for the set up and maintenance of your {{site.data.keyword.containerlong_notm}} cluster environment for your application workloads. You are responsible for incident and operations management of your application data.
{: shortdesc}

| Resource | IBM responsibilities | Your responsibilities |
| -------- | -------------------- | --------------------- |
| Worker nodes | <ul><li>Deploy a fully managed, highly available dedicated master in a secured, IBM-owned infrastructure account for each cluster.</li><li>Provision worker nodes in your IBM Cloud infrastructure account.</li><li>Fulfill requests for more infrastructure, such as adding, reloading, updating, and removing worker nodes.</li><li>Provide tools, such as the [cluster autoscaler](/docs/containers?topic=containers-ca#ca), to extend your cluster infrastructure.</li><li>Integrate ordered infrastructure resources to work automatically with your cluster architecture and become available to your deployed apps and workloads.</li><li>Fulfill automation requests to help recover worker nodes.</li></ul> |<ul><li>Use the provided API, CLI, or console tools to adjust [compute](/docs/containers?topic=containers-add_workers) and [storage](/docs/containers?topic=containers-storage_planning) capacity to meet the needs of your workload.</li><li>As needed, request that worker nodes are rebooted, reloaded, or replaced.</li></ul> |
| Cluster networking | <ul><li>Set up cluster management components, such as public or private service endpoints, VLANs, and load balancers.</li><li>Fulfill requests for more infrastructure, such as attaching worker nodes to existing VLANs or subnets upon resizing a worker pool.</li><li>Set up an OpenVPN connection between the master and worker nodes when the cluster is created.</li><li>Set up a public application load balancer (ALB) that is multizone, if applicable. Provide the ability to set up private ALBs and public or private network load balancers (NLBs).</li><li>Set up default Calico network policies to control basic cluster traffic.</li><li>Provide the ability to isolate network traffic with edge nodes.</li><li>Provide the ability to set up a VPN connection with on-premises resources such as through the strongSwan IPSec VPN service.</li></ul> | <ul><li>Use the provided API, CLI, or console tools to adjust [networking configuration](/docs/containers?topic=containers-cs_network_cluster) to meet the needs of your workload, such as configuring service endpoints or adding VLANs to provide IP addresses for more worker nodes.</li><li>Set up any additional networking capabilities that are needed, such as private ALBs, public or private NLBs, additional Calico network policies, or VPN connections.</li></ul> |
| Applications | <ul><li>Provision clusters with Kubernetes components installed so that you can access the Kubernetes API.</li><li>Provide a number of managed add-ons to extend your app's capabilities, such as [Istio](/docs/containers?topic=containers-istio#istio) and [Knative](/docs/containers?topic=containers-serverless-apps-knative). Maintenance is simplified for you because IBM provides the installation and updates for the managed add-ons.</li><li>Provide cluster integration with select third-party partnership technologies, such as {{site.data.keyword.la_short}}, {{site.data.keyword.mon_short}}, and Portworx.</li><li>Provide automation to enable service binding to other {{site.data.keyword.cloud_notm}} services.</li><li>Create clusters with image pull secrets so that your deployments in the `default` Kubernetes namespace can pull images from {{site.data.keyword.registrylong_notm}}.</li><li>Provide storage classes and plug-ins to support persistent volumes for use with your apps.</li><li>Create clusters with subnet IP addresses reserved to use to expose apps externally.</li><li>Support native Kubernetes public and private load balancers and Ingress routes for exposing services externally.</li></ul> | <ul><li>Use the provided tools and features to [configure and deploy](/docs/containers?topic=containers-app); [set up permissions](/docs/containers?topic=containers-users#users); [integrate with other services](/docs/containers?topic=containers-supported_integrations#supported_integrations); [externally serve](/docs/containers?topic=containers-cs_network_planning#cs_network_planning); [monitor the health](/docs/containers?topic=containers-health); [save, back up, and restore data](/docs/containers?topic=containers-storage_planning#storage_planning); and otherwise manage your [highly available](/docs/containers?topic=containers-ha#ha) and resilient workloads.</li></ul> |
{: summary="The rows are read from left to right. The resource area of comparing responsibilities is in the first column, with the responsibilities of IBM in the second column and your responsibilities in the third column."}
{: caption="Table 2. Responsibilities for incident and operations management" caption-side="top"}

<br />


### Change management
{: #change-management}

You and IBM share responsibilities for keeping your clusters at the latest container platform and operating system versions, along with recovering infrastructure resources that might require changes. You are responsible for change management of your application data.
{: shortdesc}

| Resource | IBM responsibilities | Your responsibilities |
| -------- | -------------------- | --------------------- |
| Operating system | <ul><li>Provide worker node major, minor, and patch OS, version, and security updates.</li><li>Fulfill automation requests to update and recover worker nodes.</li></ul> | <ul><li>Use the API, CLI, or console tools to [apply](/docs/containers?topic=containers-update#update) the provided worker node updates that include operating system patches; or to request that worker nodes are rebooted, reloaded, or replaced.</li></ul> |
| Container platform version | <ul><li>Provide a suite of tools to automate cluster management, such as the {{site.data.keyword.containerlong_notm}} [API ![External link icon](../icons/launch-glyph.svg "External link icon")](https://containers.cloud.ibm.com/global/swagger-global-api/), [CLI plug-in](/docs/containers?topic=containers-cli-plugin-kubernetes-service-cli), and [console ![External link icon](../icons/launch-glyph.svg "External link icon")](https://cloud.ibm.com/kubernetes/clusters).</li><li>Automatically apply Kubernetes master patch OS, version, and security updates.</li><li>Make major and minor updates for master nodes available for you to apply.</li><li>Provide worker node major, minor, and patch OS, version, and security updates.</li><li>Fulfill automation requests to update cluster master and worker nodes.</li></ul> | <ul><li>Use the API, CLI, or console tools to [apply](/docs/containers?topic=containers-update#update) the provided major and minor Kubernetes master updates and major, minor, and patch worker node updates.</li></ul> |
| Applications | <ul><li>Provision clusters with Kubernetes components installed so that you can access the Kubernetes API to perform actions such as rolling updates for your apps.</li><li>Provide a number of managed add-ons to extend your app's capabilities, such as [Istio](/docs/containers?topic=containers-istio#istio), which you can use to do canary or A/B updates for your apps.</li></ul> | <ul><li>Use the provided tools to keep your apps up to date and make sure that you have enough compute resources for your workload.</li></ul> |
{: summary="The rows are read from left to right. The resource area of comparing responsibilities is in the first column, with the responsibilities of IBM in the second column and your responsibilities in the third column."}
{: caption="Table 3. Responsibilities for change management" caption-side="top"}

<br />


### Identity and access management
{: #iam-responsibilities}

You and IBM share responsibilities for controlling access to your {{site.data.keyword.containerlong_notm}} instances. For {{site.data.keyword.iamlong}} responsibilities, consult that product's documentation. You are responsible for identity and access management to your application data.
{: shortdesc}

| Resource | IBM responsibilities | Your responsibilities |
| -------- | -------------------- | --------------------- |
| Applications | <ul><li>Automatically configure security settings to prevent insecure access, such as disabling SSH into the worker node compute hosts.</li><li>Automatically integrate {{site.data.keyword.cloud_notm}} IAM service roles with Kubernetes RBAC roles in the cluster.</li><li>Provide the ability to integrate {{site.data.keyword.at_full_notm}} with your cluster to audit the actions that users take in the cluster.</li><li>Generate an API key that is used to access infrastructure permissions for each resource group and region.</li></ul> | <ul><li>Control identity and access managment to your apps and data. For example, [assign the appropriate access to users and the API key owner](/docs/containers?topic=containers-users).</li><li>Set up {{site.data.keyword.at_full_notm}} or other abilities to track user activity in the cluster.</li></ul> |
{: summary="The rows are read from left to right. The resource area of comparing responsibilities is in the first column, with the responsibilities of IBM in the second column and your responsibilities in the third column."}
{: caption="Table 4. Responsibilities for identity and access management" caption-side="top"}

<br />


### Security and regulation compliance
{: #security-compliance}

IBM is responsible for the security and compliance of {{site.data.keyword.containerlong_notm}}. You are responsible for the security and compliance of any workloads that run in the cluster and your application data.
{: shortdesc}

| Resource | IBM responsibilities | Your responsibilities |
| -------- | -------------------- | --------------------- |
| General | <ul><li>Maintain controls commensurate to [various industry compliance standards](/docs/containers?topic=containers-faqs#standards), such as PCI DSS.</li><li>Monitor, isolate, and recover the cluster master.</li><li>Provide highly available replicas of the Kubernetes master API server, etcd, scheduler, and controller manager components to protect against a master outage.</li><li>Monitor and report the health of the master and worker nodes in the various interfaces.</li><li>Automatically apply master security patch updates, and provide worker node security patch updates.</li><li>Enable certain security settings, such as encrypted disks on worker nodes</li><li>Disable certain insecure actions for worker nodes, such as not permitting users to SSH into the host.</li><li>Encrypt communication between the master and worker nodes with TLS.</li><li>Provide CIS-compliant Linux images for worker node operating systems.</li><li>Continuously monitor master and worker node images to detect vulnerability and security compliance issues.</li><li>Provision worker nodes with two local SSD, AES 256-bit encrypted data partitions.</li><li>Provide options for cluster network connectivity, such as public and private service endpoints.</li><li>Provide options for compute isolation, such as dedicated virtual machines or bare metal.</li><li>Integrate Kubernetes role-based access control (RBAC) with {{site.data.keyword.cloud_notm}} Identity and Access Management (IAM).</li></li> | <ul><li>Set up and maintain security and regulation compliance for your apps and data. For example, choose how to set up your [cluster network](/docs/containers?topic=containers-plan_clusters) and configure further [security settings](/docs/containers?topic=containers-security#security) to meet your workload's security and compliance needs. If applicable, configure your [firewall](/docs/containers?topic=containers-firewall#firewall).</li><li>As part of your incident and operations management responsibilties for the worker nodes, apply the provided security patch updates.</li></ul>  |
{: summary="The rows are read from left to right. The resource area of comparing responsibilities is in the first column, with the responsibilities of IBM in the second column and your responsibilities in the third column."}
{: caption="Table 5. Responsibilities for security and regulation compliance" caption-side="top"}

<br />


### Disaster recovery
{: #disaster-recovery}

IBM is responsible for the recovery of {{site.data.keyword.containerlong_notm}} components in case of disaster. You are responsible for the recovery of the workloads that run the cluster and your application data. If you integrate with other {{site.data.keyword.cloud_notm}} services such as file, block, object, cloud database, logging, or audit event services, consult those services' disaster recovery information.
{: shortdesc}

| Resource | IBM responsibilities | Your responsibilities |
| -------- | -------------------- | --------------------- |
| General | <ul><li>Maintain service availability across [worldwide locations](/docs/containers?topic=containers-regions-and-zones) so that customers can deploy clusters across zones and regions for higher DR tolerance.</li><li>Provision clusters with three replicas of master components for high availability.</li><li>In multizone regions, automatically spread the master replicas across zones.</li><li>Continously monitor to work to ensure the reliability and availability of the service environment by site reliability engineers.</li><li>Update and recover operational {{site.data.keyword.containerlong_notm}} and Kubernetes components within the cluster, such as the Ingress application load balancer and file storage plug-in.</li><li>Back up and recover data in etcd, such as your Kubernetes workload configuration files</li><li>Provide the optional [worker node Autorecovery](/docs/containers?topic=containers-health#autorecovery).</li><li>Provide the ability to integrate with other {{site.data.keyword.cloud_notm}} services such as storage providers so that data can be backed up and restored.</li></ul> | <ul><li>Set up and maintain disaster recovery capabilities for your apps and data. For example, to prepare your cluster for HA/DR scenarios, follow the guidance in [High availability for {{site.data.keyword.containerlong_notm}}](/docs/containers?topic=containers-ha). Note that persistent storage of data such as application logs and cluster metrics are not set up by default.</li></ul>  |
{: summary="The rows are read from left to right. The resource area of comparing responsibilities is in the first column, with the responsibilities of IBM in the second column and your responsibilities in the third column."}
{: caption="Table 6. Responsibilities for disaster recovery" caption-side="top"}







