---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup 是一个灵活的实用程序，允许用户将本地文件系统的部分或全部内容备份到与 S3 API 兼容的对象存储系统。Free 和 Professional 版本可用于 Windows、MacOS 和 Linux，并且支持若干常用云存储服务，包括 {{site.data.keyword.cos_full}}。Cloudberry Backup 可以从 [cloudberrylab.com](https://www.cloudberrylab.com/) 进行下载。

Cloudberry Backup 包含许多有用的功能，包括：

* 安排
* 增量备份和块级别备份
* 命令行界面
* 电子邮件通知
* 压缩（仅限 *Pro 版本*）

## Cloudberry Explorer
{: #cloudberry-explorer}

Cloudberry Labs 推出的新产品，用于为 {{site.data.keyword.cos_short}} 提供熟悉的文件管理用户界面。[Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} 也提供有 Free 和 Pro 版本，但目前仅可用于 Windows。主要功能包括：

* 文件夹/存储区同步
* 命令行界面
* ACL 管理
* 容量报告

Pro 版本还包括以下功能：
* 搜索 
* 加密/压缩
* 可恢复上传
* FTP/SFTP 支持

## 将 Cloudberry 与对象存储器配合使用
{: #cloudberry-cos}

配置 Cloudberry 产品以便与 {{site.data.keyword.cos_short}} 配合使用时，要记住的要点如下：

* 从选项列表中选择`与 S3 兼容`
* 目前仅支持 [HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)
* 每个存储区需要单独的连接
* 确保连接中指定的`端点`与所选存储区的区域相匹配（*如果目标不可访问，备份会失败*）。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
