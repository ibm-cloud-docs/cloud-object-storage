---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cli, open source, minio

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

# 使用 Minio 客户机
{: #minio}

您是否希望对 {{site.data.keyword.cos_full}} 使用熟悉的类似 UNIX 的命令（`ls`、`cp`、`cat` 等）？如果是，那么开放式源代码 [Minio 客户机](https://min.io/download#/linux){:new_window}非常适合。您可以在 Minio Web 站点上的[快速入门指南](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window}中找到适用于每种操作系统的安装指示信息。

## 配置
{: #minio-config}

通过运行以下命令来完成添加 {{site.data.keyword.cos_short}} 的操作：

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - 用于在命令中引用 {{site.data.keyword.cos_short}} 的短名称
* `<COS-ENDPOINT` - {{site.data.keyword.cos_short}} 实例的端点。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<ACCESS-KEY>` - 分配给服务凭证的访问密钥
* `<SECRET-KEY>` - 分配给服务凭证的私钥

配置信息存储在位于 `~/.mc/config.json` 的 JSON 文件中

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## 样本命令
{: #minio-commands}

[MinIO Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window} 中记录了命令以及可选标志和参数的完整列表

### `mb` - 创建存储区
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - 列出存储区
{: #minio-ls}

虽然列出了所有可用的存储区，但并非所有对象都可访问，具体取决于指定[端点](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)的区域。
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - 列出对象
{: #minio-ls-objects}

```
mc ls cos/testbucket1
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
[2018-08-10 09:49:08 HST]  20MiB newbigfile.pdf
[2018-11-29 09:53:15 HST]    31B testsave.txt
```

### `find` - 通过名称搜索对象
{: #minio-find}

[Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window} 中提供了搜索选项的完整列表
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - 显示对象的若干行
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - 复制对象
{: #minio-cp}

此命令会在两个位置之间复制对象。这些位置可以是不同的主机（例如，不同的[端点](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)或存储服务）或本地文件系统位置（例如，`~/foo/filename.pdf`）。
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - 除去对象
{: #minio-rm}

*[Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window} 上提供了更多除去选项*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - 将 STDIN 复制到对象
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
