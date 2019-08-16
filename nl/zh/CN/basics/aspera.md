---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# 使用 Aspera 高速传输
{: #aspera}

Aspera 高速传输克服了传统 FTP 和 HTTP 传输的限制，能在大多数情况下提高数据传输性能，尤其是在遇到等待时间长和丢包的网络中。Aspera 高速传输使用 [FASP 协议](https://asperasoft.com/technology/transport/fasp/)（而不是标准 HTTP `PUT`）来上传对象。使用 Aspera 高速传输进行上传和下载具有以下优点：

- 传输速度更快
- 在控制台中可上传超过 200 MB 的大对象，使用 SDK 或库可上传 1 GB
- 上传任何类型的数据的整个文件夹，包括多媒体文件、磁盘映像以及其他任何结构化或非结构化数据
- 可定制传输速度和缺省首选项
- 可以查看、暂停/恢复或独立取消传输

Aspera 高速传输可以在 {{site.data.keyword.cloud_notm}} [控制台](#aspera-console)中使用，也可以通过 [SDK](#aspera-sdk) 以编程方式使用。 

Aspera 高速传输仅在某些区域可用。有关更多详细信息，请参阅[集成服务](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)。
{:tip}

## 使用控制台
{: #aspera-console}

在[支持的区域](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)中创建存储区时，可以选择 Aspera 高速传输来上传文件或文件夹。尝试上传对象后，系统会提示您安装 Aspera Connect 客户机。

### 安装 Aspera Connect
{: #aspera-install}

1. 选择**安装 Aspera Connect** 客户机。
2. 根据您的操作系统和浏览器，遵循相应的安装指示信息。
3. 恢复文件或文件夹上传。

Aspera Connect 插件还可以直接从 [Aspera Web 站点](https://downloads.asperasoft.com/connect2/)进行安装。有关对 Aspera Connect 插件问题进行故障诊断的帮助，[请参阅此文档](https://downloads.asperasoft.com/en/documentation/8)。

安装插件后，可以选择将 Aspera 高速传输设置为缺省值，以用于使用同一浏览器执行的到目标存储区的任何上传。选中**记住我的浏览器首选项**。在存储区配置页面的**传输选项**下也提供了相关选项。通过这些选项，可以在“标准”和“高速”之间进行选择，以作为上传和下载的缺省传输。

通常，使用 IBM Cloud Object Storage 基于 Web 的控制台并不是使用 {{site.data.keyword.cos_short}} 的最常用方法。“标准”传输选项将对象大小限制为 200 MB，并且文件名和密钥要完全相同。Aspera 高速传输支持更大的对象大小，并且性能更高（取决于网络因素）。

### 传输状态
{: #aspera-console-transfer-status}

**活动：**启动传输后，传输状态会显示为“活动”。传输处于活动状态时，可以暂停、恢复或取消活动传输。 

**已完成：**完成传输后，有关此传输以及此会话中所有传输的信息都会显示在“已完成”选项卡上。可以清除这些信息。您只能查看有关当前会话中已完成传输的信息。

**首选项：**可以将上传和/或下载的缺省值设置为“高速”。

使用 Aspera 高速传输进行下载将产生输出费用。有关更多信息，请参阅[定价页面](https://www.ibm.com/cloud/object-storage)。
{:tip}

**高级首选项：**可以设置上传和下载的带宽。

----

## 使用库和 SDK
{: #aspera-sdk}

Aspera 高速传输 SDK 提供了在使用 Java 或 Python 的定制应用程序中启动高速传输的能力。

### 何时使用 Aspera 高速传输
{: #aspera-guidance}

Aspera 高速传输使用的 FASP 协议并不适用于与 COS 之间的所有数据传输。具体而言，使用 Aspera 高速传输的任何传输应该满足以下条件：

1. 始终使用多个会话 - 至少有两个并行会话，这样才能最好地利用 Aspera 高速传输功能。请参阅有关 [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) 和 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera) 的特定指导信息。
2. Aspera 高速传输非常适合传输较大的文件，对于包含的数据总量低于 1 GB 的任何文件或目录，应改为使用标准传输管理器类来分块传输对象。Aspera 高速传输需要的首字节响应时间要比标准 HTTP 传输长。对许多 Aspera 传输管理器对象进行实例化以管理单个较小文件的传输，可能会导致性能低于基本 HTTP 请求，因此最好改为对单个客户机进行实例化，以上传较小文件的目录。
3. Aspera 高速传输设计的一部分目的是在有大量丢包的网络环境中提高性能，从而使协议在长距离和公共广域网上具有高性能。对于区域或数据中心内的传输，不应使用 Aspera 高速传输。

Aspera 高速传输 SDK 是封闭式源代码 SDK，因此是 COS SDK（使用 Apache 许可证）的可选依赖项。
{:tip}

#### COS/Aspera 高速传输打包
{: #aspera-packaging}

下图显示了 COS SDK 如何与 Aspera 高速传输库进行交互以提供功能的高级概述。

<img alt="COS/Aspera 高速传输 SDK。" src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="图 1：COS/Aspera 高速传输 SDK。" caption-side="bottom"} 

### 支持的平台
{: #aspera-sdk-platforms}

|操作系统|版本|体系结构|测试的 Java 版本|测试的 Python 版本|
|------------------------|-----------|--------------|--------------|----------------|
|Ubuntu|18.04 LTS|64 位|6 和更高版本|2.7 和 3.6|
|Mac OS X|10.13|64 位|6 和更高版本|2.7 和 3.6|
|Microsoft&reg; Windows|10|64 位|6 和更高版本|2.7 和 3.6|

每个 Aspera 高速传输会话都会衍生一个单独的 `ascp` 进程，此进程在客户机上运行以执行传输。请确保计算环境允许此进程运行。
{:tip}

**其他限制**

* 不支持 32 位二进制文件
* Windows 支持需要 Windows 10
* Linux 支持仅限于 Ubuntu（已针对 18.04 LTS 进行测试）
* 必须使用 IAM API 密钥而不是 HMAC 凭证来创建 Aspera 传输管理器客户机。

### 获取使用 Java 的 SDK
{: #aspera-sdk-java} 
{: java}

使用 {{site.data.keyword.cos_full_notm}} 和 Aspera 高速传输 Java SDK 的最佳方法是利用 Maven 来管理依赖项。如果您不熟悉 Maven，那么可以使用 [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window} 指南来快速入门和熟悉运用。
{: java}

Maven 使用名为 `pom.xml` 的文件来指定 Java 项目所需的库（及其版本）。下面是示例 `pom.xml` 文件，说明如何使用 {{site.data.keyword.cos_full_notm}} 和 Aspera 高速传输 Java SDK
{: java}

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

在[使用 Aspera 高速传输](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera)部分中提供了使用 Java 启动 Aspera 高速传输的示例。
{: java}

### 获取使用 Python 的 SDK
{: #aspera-sdk-python} 
{: python}

{{site.data.keyword.cos_full_notm}} 和 Aspera 高速传输 Python SDK 可从 Python Package Index (PyPI) 软件存储库中获取。
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

在[使用 Aspera 高速传输](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera)部分中提供了使用 Python 启动 Aspera 传输的示例。
{: python}
