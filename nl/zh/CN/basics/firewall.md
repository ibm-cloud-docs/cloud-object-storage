---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 

# 设置防火墙
{: #setting-a-firewall}

IAM 策略为管理员提供了限制对各个存储区的访问权的方法。如果某些数据只能通过可信网络进行访问该怎么办？存储区防火墙限制对数据的所有访问，除非请求源自允许的 IP 地址的列表。
{: shortdesc}

有一些与设置防火墙相关的规则：

* 设置或查看防火墙的用户必须具有对存储区的`管理者`角色。 
* 具有对存储区的`管理者`角色的用户可以通过任何 IP 地址来查看和编辑允许的 IP 地址的列表，以防止意外锁定。
* {{site.data.keyword.cos_short}} 控制台仍可以访问存储区，前提是用户的 IP 地址经过授权。
* 其他 {{site.data.keyword.cloud_notm}} 服务**无权**绕过防火墙。此限制意味着依赖于 IAM 策略进行存储区访问的其他服务（例如，Aspera、SQL Query、Security Advisor、Watson Studio、Cloud Functions 等）将无法访问存储区。

设置防火墙后，存储区将与 {{site.data.keyword.cloud_notm}} 的其余部分相隔离。在启用防火墙之前，请考虑这可能对依赖于其他服务直接访问存储区的应用程序和工作流程有怎样的影响。
{: important}

## 使用控制台设置防火墙
{: #firewall-console}

首先，确保您有存储区。如果没有，请遵循[入门教程](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)以熟悉控制台。

### 设置授权 IP 地址的列表
{: #firewall-console-enable}

1. 在 {{site.data.keyword.cloud_notm}} [控制台仪表板](https://cloud.ibm.com/)中，选择**存储**以查看资源列表。
2. 接下来，从**存储**菜单中选择具有您的存储区的服务实例。这将转至 {{site.data.keyword.cos_short}} 控制台。
3. 选择要将访问权限制为授权 IP 地址的存储区。 
4. 从导航菜单中，选择**访问策略**。
5. 选择**授权 IP** 选项卡。
6. 单击**添加 IP 地址**，然后选择**添加**。
7. 以 [CIDR 表示法](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing)添加 IP 地址列表，例如 `192.168.0.0/16, fe80:021b::0/64`。地址可以遵循 IPv4 或 IPv6 标准。单击**添加**。
8. 在控制台中保存地址之前，不会强制实施防火墙。单击**全部保存**以强制实施防火墙。
9. 现在，此存储区中的所有对象都只能通过这些 IP 地址进行访问！

### 除去任何 IP 地址限制
{: #firewalls-console-disable}

1. 在**授权 IP** 选项卡中，选中要从授权列表中除去的任何 IP 地址或范围旁边的框。
2. 选择**删除**，然后通过再次单击**删除**来确认对话框。
3. 在控制台中保存更改之前，不会强制实施更新后的列表。单击**全部保存**以强制实施新规则。
4. 现在，此存储区中的所有对象都只能通过这些 IP 地址进行访问！

如果未列出授权的 IP 地址，这意味着正常 IAM 策略将应用于存储区，并且对用户的 IP 地址没有任何限制。
{: note}


## 通过 API 设置防火墙
{: #firewall-api}

防火墙通过 [COS 资源配置 API](https://cloud.ibm.com/apidocs/cos/cos-configuration) 进行管理。此新的 REST API 用于配置存储区。 

具有`管理者`角色的用户可以通过任何网络来查看和编辑允许的 IP 地址的列表，以防止意外锁定。
{: tip}
