---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# 使用 CrossFTP 传输文件
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} 是一个功能齐全的 FTP 客户机，支持与 S3 兼容的云存储器解决方案，包括 {{site.data.keyword.cos_full}}。CrossFTP 支持 Mac OS X、Microsoft Windows 和 Linux，并提供 Free、Pro 和 Enterprise 版本，具有多种功能，例如：

* 选项卡式界面
* 密码加密
* 搜索
* 批量传输
* 加密（*Pro/Enterprise 版本*）
* 同步（*Pro/Enterprise 版本*）
* 调度程序（*Pro/Enterprise 版本*）
* 命令行界面（*Pro/Enterprise 版本*）

## 连接到 IBM Cloud Object Storage
{: #crossftp-connect}

1. 下载、安装并启动 CrossFTP。
2. 在右窗格中，通过单击加号 (+) 图标以打开站点管理器来创建新站点。
3. 在*常规*选项卡下，输入以下内容：
    * 将**协议**设置为 `S3/HTTPS`
    * 将**标签**设置为您选择的描述性名称
    * 将**主机**设置为 {{site.data.keyword.cos_short}} 端点（即，`s3.us.cloud-object-storage.appdomain.cloud`）
        * *确保端点区域与所需目标存储区相匹配。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。*
    * 将**端口**保留为 `443`
    * 将**访问键**和**私钥**设置为 HMAC 凭证（具有对目标存储区的相应访问权）
4. 在 *S3* 选项卡下
    * 确保未选中`使用 DevPay`
    * 单击 **API 集...**，并确保未选中 `Dev Pay` 和 `CloudFront 分布`
5. ***仅限 Mac OS X***
    * 单击菜单栏中的*安全性 > TLS/SSL 协议...*
    * 选择`定制启用的协议`选项
    * 将 `TLSv1.2` 添加到**已启用**框
    * 单击**确定**
6. ***仅限 Linux***
    * 单击菜单栏中的*安全性 > 密码设置...*
    * 选择`定制启用的密码套件`选项
    * 将 `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` 添加到**已启用**框
    * 单击**确定**
7. 单击**应用**，然后单击**关闭**
8. *站点*下的新条目应该可用，并且具有步骤 3 中提供的*标签*
9. 双击新条目以连接到端点

此处的窗口中将显示可用存储区的列表，您可以浏览可用文件并与本地磁盘之间传输这些文件。
