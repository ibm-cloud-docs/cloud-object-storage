---

copyright:
  years: 2021, 2025
lastupdated: "2025-05-22"

keywords: object storage, terrafrom, bucket backup

subcollection: cloud-object-storage


---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'}
{:javascript: .ph data-hd-programlang='javascript'}
{:java: .ph data-hd-programlang='java'}
{:python: .ph data-hd-programlang='python'}
{:go: .ph data-hd-programlang='go'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# About Terraform

{: #about-terraform}

Terraform is an open source project that lets you specify your cloud infrastructure resources and services by using the high-level scripting HashiCorp Configuration Language (HCL). With HCL, you have one common language to declare the cloud resources that you want and the state that you want your resources to be in

Terraform on IBM Cloud enables predictable and consistent provisioning of IBM Cloud platform, classic infrastructure, and VPC infrastructure resources so that you can rapidly build complex, multi-tier cloud environments, and enable Infrastructure as Code (IaC).

## How does Terraform on IBM Cloud work

{: #Working-with-terraform}

Let's say you want to spin up multiple copies of your cloud environment that uses a cluster of virtual servers, a load balancer, and a database server on IBM Cloud. You could learn how to create each resource, review the API or the commands that you need, and write a bash script to spin up these components. But it's easier, faster, and more orderly to use one language to declare all your requirements, document them in a configuration file, and let Terraform on IBM Cloud do it all for you.

## How to provision Terraform on IBM Cloud and manage cloud services?

{: #how-does-terraform-work}

To use Terraform on IBM Cloud, you must create a Terraform configuration file that describes the IBM Cloud resources that you need and how you want to configure them. Based on your configuration, Terraform creates an execution plan and describes the actions that need to be executed to get to the required state. You can review the execution plan, change it, or simply execute the plan. When you change your configuration, Terraform on IBM Cloud can determine what changed and create incremental execution plans that you can apply to your existing IBM Cloud resources.

The following steps show how Terraform on IBM Cloud provisions your services in IBM Cloud.

1. You declare the IBM Cloud resources that you want in a Terraform configuration file by using HashiCorp Configuration Language (HCL). Store this configuration file in a source code repository that is version-controlled and that allows teams to collaborate, such as GitHub or GitLab.
2. Configure the IBM Cloud Provider plug-in.
3. Create a Terraform execution plan that summarizes all the actions that Terraform needs to run to create, update, or delete the IBM Cloud resources in your Terraform template.
4. Apply the Terraform configuration file in IBM Cloud.
