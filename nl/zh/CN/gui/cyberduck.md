---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# 使用 Cyberduck 传输文件
{: #cyberduck}

Cyberduck 是一种常用的开放式源代码 Cloud Object Storage 浏览器，使用简单，适用于 Mac 和 Windows。Cyberduck 能够计算连接到 IBM COS 所需的正确授权签名。Cyberduck 可以从 [cyberduck.io/](https://cyberduck.io/){: new_window} 进行下载。

要使用 Cyberduck 来创建与 IBM COS 的连接，并将本地文件的文件夹同步到存储区，请执行以下步骤：

 1. 下载、安装并启动 Cyberduck。
 2. 这将打开应用程序的主窗口，在其中可以创建与 IBM COS 的连接。单击**打开连接**以配置与 IBM COS 的连接。
 3. 这将打开一个弹出窗口。从顶部的下拉菜单中，选择 `S3 (HTTPS)`。在以下字段中输入信息，然后单击“连接”：

    * `服务器`：输入 IBM COS 的端点
        * *确保端点的区域与所需存储区相匹配。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。*
    * `访问键标识`
    * `私钥访问密钥`
    * `添加到密钥链`：保存与密钥链的连接，以允许在其他应用程序中使用*（可选）*

 4. Cyberduck 将转至可以在其中创建存储区的帐户的根目录。
    * 在主窗格中右键单击，然后选择**新建文件夹**（*应用程序可处理多种传输协议，其中文件夹是更常用的容器构造*）。
    * 输入存储区名称，然后单击“创建”。
 5. 创建存储区后，双击存储区以进行查看。在存储区中，可以执行各种功能，例如：
    * 将文件上传到存储区
    * 列出存储区内容
    * 从存储区下载对象
    * 将本地文件同步到存储区
    * 将对象同步到其他存储区
    * 创建存储区的归档
 6. 右键单击存储区，然后选择**同步**。这将打开一个弹出窗口，在其中可以浏览至要同步到存储区的文件夹。选择文件夹，然后单击“选择”。
 7. 选择文件夹后，将打开一个新的弹出窗口。在此，有一个下拉菜单可用于选择对存储区执行的同步操作。菜单中提供了三个可能的同步选项：

    * `下载`：这将从存储区下载已更改和缺少的对象。
    * `上传`：这将向存储区上传已更改和缺少的文件。
    * `镜像`：这将同时执行下载和上传操作，以确保所有新的和更新的文件及对象在本地文件夹和存储区之间同步。

 8. 这将打开另一个窗口，其中显示活动和历史传输请求。同步请求完成后，主窗口将对存储区执行列示操作，以反映存储区中的更新内容。

## Mountain Duck
{: #mountain-duck}

Mountain Duck 基于 Cyberduck 而构建，允许您将 Cloud Object Storage 作为磁盘安装在 Mac 上的“访达”或 Windows 上的资源管理器中。有试用版本可用，但要在试用期后继续使用，需要注册密钥。

在 Mountain Duck 中创建书签与在 Cyberduck 中创建连接非常类似：

1. 下载、安装并启动 Mountain Duck。
2. 创建新书签。
3. 从下拉菜单中，选择 `S3 (HTTPS)`，然后输入以下信息：
    * `服务器`：输入 IBM COS 的端点 
        * *确保端点区域与所需存储区相匹配。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。*
    * `用户名`：输入访问键
    * 单击**连接**
    * 系统会提示您输入密钥，该密钥将保存在密钥链中。

现在，存储区将在“访达”或资源管理器中可用。您可以像其他任何已安装的文件系统一样与 {{site.data.keyword.cos_short}} 进行交互。

## CLI
{: #cyberduck-cli}

Cyberduck 还提供了 `duck`，这是在 Linux、Mac OS X 和 Windows 上的 shell 中运行的命令行界面 (CLI)。`duck` [Wiki 页面](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window}上提供了安装指示信息。

要将 `duck` 与 {{site.data.keyword.cos_full}} 配合使用，需要将定制概要文件添加到[应用程序支持目录](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}。在 [CLI help/how-to](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window} 上提供了有关 `duck` 连接概要文件（包括样本和预配置的概要文件）的详细信息。

下面是用于区域 COS 端点的示例概要文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

通过将此概要文件添加到 `duck`，您可以使用类似于下面的命令来访问 {{site.data.keyword.cos_short}}：

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*键值*
* `<bucket-name>` - COS 存储区的名称（*确保存储区和端点区域一致*）
* `<access-key>` - HMAC 访问键
* `<secret-access-key>` - HMAC 密钥

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

通过在 shell 中输入 `duck --help` 或访问 [Wiki 站点](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}，可获取命令行选项的完整列表。
