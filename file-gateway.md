---

copyright:
  years: 2021
lastupdated: "2021-04-20"

keywords: file system, gateway, file access

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Using IBM Cloud Object Storage File Access
{: #fa-gateway}

IBM Cloud Object Storage File Access is a software-defined solution providing an SMB and NFS protocol interface for applications to store and retrieve infrequently used files on IBM Cloud Object Storage. This solution is an excellent choice for archiving, retention, and active archiving use cases as it also provides a built-in capability to discover and migrate files form existing file systems to IBM Cloud Object Storage.
{: shortdesc}

IT administrators can use Cloud Object Storage File Access to discover and migrate cold and infrequently used files from multiple enterprise NAS, Windows, or Linux file servers to Cloud Object Storage. This enables administrators to free up storage space on their filer systems or completely eliminate file infrastructure that is used solely for infrequently used file data, such as backup and active archiving workloads and file repositories.

Multiple enterprise applications can access one or more unified file systems on Cloud Object Storage through SMB and NFS protocol interfaces provided by Cloud Object Storage File Access without any rewrite. It is a highly secure and available solution that is easy to install, use, and manage. It can be integrated with Active Directory to provide authentication and access control to file data stored on Cloud Object Storage.

IBM Cloud Object Storage File Access runs as a pair of active-passive gateway virtual appliances (VMware, Hyper-V, or KVM). Configuration and maintenance operations are performed using a browser pointing to a portal that also runs in another pair of active-active virtual appliances (VMware, Hyper-V, or KVM).

## Requirements
{: #fa-gateway-requirements}

### Hardware requirements
{: #fa-gateway-requirements-hardware}

Recommended minimum server hardware configuration for the VMware ESXi, Microsoft Hyper-V, or Red Hat® Enterprise Linux hypervisor that will run the IBM Cloud Object Storage File Access Gateway VM:

- CPU	x64 (4+ cores)
- RAM	8 GB
- Cache disk	Up to 8 TB, SSD or NVMe preferred (2 TB minimum)
- Network	10 Gigabit
- Network switch (At least 2 for redundancy)

Recommended minimum server hardware configuration for the VMware ESXi or Red Hat Enterprise Linux hypervisor that will run the IBM Cloud Object Storage File Access Portal VM:

- CPU	x64 (12+ cores)
- RAM	40+ GB
- Cache disk	3+ TB, SSD or NVMe preferred (1 TB minimum)
- Network	10 Gigabit
- Network switch (At least 2 for redundancy)

IBM Cloud Object Storage File Access solution performance depends upon the underlying hardware and network it runs on. Note that it is recommended to use SAS SSD or NVMe for cache disk for both the Gateway and the Portal. SSD must be high drive writes per day, at least seven drive writes per day is recommended. Cache disk must also be highly redundant or available, for example RAID5, RAID6, or RAID 10.
{: note}

### Software requirements
{: #fa-gateway-requirements-hardware}

Hypervisor requirements
- ESXi: VMware ESXi 6.5, or later. Cloud Object Storage File Access Gateway Portal can be managed in VMware vCenter and in VMware vCloud Director.
- KVM and OpenStack: Linux machine with KVM virtualization and Red Hat Virtual Machine Manager (virt-manager) installed and running. Make sure that memory overcommitting is disabled.
- Hyper-V: Hyper-V for Microsoft Windows Servers 2012 R2, and higher.

All resources allocated to an IBM Cloud Object Storage File Access Portal VM should be dedicated to that VM and not shared with other VMs. You must not run any other applications on the IBM Cloud Object Storage File Access Portal VMs.
{: note}

## Installation and administration

For details on installation and administration, [see the IBM Cloud Object Storage File Access documentation](https://www.ibm.com/docs/en/cosfa/7.0?topic=gateway-cos-fa-administrator-guidepdf).

