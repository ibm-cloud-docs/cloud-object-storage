---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# 使用 `s3fs` 安装存储区
{: #s3fs}

需要对 NFS 样式的文件系统执行读写操作的应用程序可以使用 `s3fs`，s3fs 可以将存储区安装为目录，同时保留文件的本机对象格式。这允许您使用熟悉的 shell 命令（如用于列示的 `ls` 或用于复制文件的 `cp`）与 Cloud Storage 进行交互，以及提供对依赖于读写本地文件的旧应用程序的访问权。有关更详细的概述，请[访问项目的官方自述文件](https://github.com/s3fs-fuse/s3fs-fuse)。

## 先决条件
{: #s3fs-prereqs}

* IBM Cloud 帐户和 {{site.data.keyword.cos_full}} 的实例
* Linux 或 OSX 环境
* 凭证（[IAM API 密钥](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)或 [HMAC 凭证](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)）

## 安装
{: #s3fs-install}

在 OSX 上，使用 [Homebrew](https://brew.sh/)：

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

在 Debian 或 Ubuntu 上： 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

官方 `s3fs` 文档建议使用的是 `libcurl4-gnutls-dev`，而不是 `libcurl4-openssl-dev`。这两者均可正常工作，但 OpenSSL 版本的性能可能更佳。
{:tip}

您还可以通过源代码来构建 `s3fs`。请首先克隆 Github 存储库：

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

然后构建 `s3fs`：

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

安装二进制文件：

```sh
sudo make install
```
{:codeblock}

## 配置
{: #s3fs-config}

将凭证存储在包含 `<access_key>:<secret_key>` 或 `:<api_key>` 的文件中。此文件需要具有有限访问权，因此请运行：

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

现在，您可以使用以下命令来安装存储区：

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

如果凭证文件仅有 API 密钥（无 HMAC 凭证），那么还需要添加 `ibm_iam_auth` 标志：

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

`<bucket>` 是现有存储区，`<mountpoint>` 是要在其中安装存储区的本地目录。`<endpoint>` 必须对应于[存储区的位置](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)。`credentials_file` 是创建的包含 API 密钥或 HMAC 凭证的文件。

现在，`ls <mountpoint>` 会像列出本地文件一样，列出该存储区中的对象（或者对于对象前缀，就像嵌套目录一样列出）。

## 性能优化
{: #s3fs-performance}

虽然性能绝不会与真正的本地文件系统完全一样，但是可以使用一些高级选项来提高吞吐量。 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM` 仅在使用 HTTPS 端点时适用。缺省情况下，与 IBM COS 的安全连接使用的是 `AES256-SHA` 密码套件。改为使用 `AESGCM` 套件后，可大大降低客户端机器上由 TLS 加密功能产生的 CPU 开销，同时提供相同级别的加密安全性。
2. `kernel_cache` 支持 `s3fs` 安装点上的内核缓冲区高速缓存。这意味着对象将仅由 `s3fs` 读取一次，因为同一文件的重复读取可以通过内核的缓冲区高速缓存进行处理。内核缓冲区高速缓存将仅使用未被其他进程使用的可用内存。如果在存储区安装期间预期存储区对象会被其他进程/机器覆盖，并且您的用例需要实时访问最新的内容，那么建议不要使用此选项。 
3. `max_background=1000` 可提高 `s3fs` 并行文件读取性能。缺省情况下，FUSE 支持最多 128 KB 的文件读请求。当要求读取的对象超过此大小时，内核会将大请求拆分为较小的子请求，并允许 s3fs 以异步方式处理这些子请求。`max_background` 选项用于设置此类并行异步请求的全局最大数目。缺省情况下，此值设置为 12，但将其设置为任意高值 (1000) 可避免读请求被阻止，即使同时读取大量文件。
4. `max_stat_cache_size=100000` 可减少 `s3fs` 发送的冗余 HTTP `HEAD` 请求数，并缩短列出目录或检索文件属性所用的时间。典型的文件系统用法会通过 `stat()` 调用频繁访问文件的元数据，该调用将映射到对象存储系统上的 `HEAD` 请求。缺省情况下，`s3fs` 会高速缓存最多 1000 个对象的属性（元数据）。每个高速缓存的条目最多需要 0.5 KB 内存。理想情况下，您会希望高速缓存能够保存存储区中所有对象的元数据。但是，您可能需要考虑这种高速缓存对内存使用量的影响。将其设置为 `100000` 需要的内存不超过 0.5 KB * 100000 = 50 MB。
5. `multipart_size=52` 将设置从 COS 服务器发送和接收的请求和响应的最大大小（以 MB 为单位）。缺省情况下，`s3fs` 将此值设置为 10 MB。增大此值还会增加每个 HTTP 连接的吞吐量（MB/秒）。另一方面，还会相应增加提供文件中第一个字节的等待时间。因此，如果您的用例仅读取每个文件中的少量数据，那么您可能不需要增大此值。此外，对于大型对象（例如，超过 50 MB），如果此值足够小，允许使用多个请求来并行访存文件，那么吞吐量会增加。我发现此选项的最佳值大约为 50 MB。COS 最佳实践建议使用的请求数为 4 MB 的倍数，因此建议将此选项设置为 52 (MB)。
6. `parallel_count=30` 设置每个文件读/写操作中并行发送给 COS 的最大请求数。缺省情况下，此值设置为 5。对于超大对象，可以通过增大此值来获取更多吞吐量。与先前选项一样，如果只读取每个文件的少量数据，请使此值保持较小。
7. `multireq_max=30` - 列出目录时，会针对列表中的每个对象发送对象元数据请求 (`HEAD`)，除非在高速缓存中找到元数据。此选项用于限制针对单个目录列示操作发送到 COS 的此类并发请求数。缺省情况下，此值设置为 20。请注意，此值必须大于或等于上面的 `parallel_count` 选项。
8. `dbglevel=warn` 将调试级别设置为 `warn`，而不设置为缺省值 (`crit`)，以将消息记录到 /var/log/syslog。

## 限制
{: #s3fs-limitations}

请务必记住，s3fs 可能并不适用于所有应用程序，因为 Object Storage 服务的首字节响应时间的等待时间较长，并且缺少随机写访问权。仅读取大文件的工作负载（如深度学习工作负载）可以使用 `s3fs` 实现良好的吞吐量。 
