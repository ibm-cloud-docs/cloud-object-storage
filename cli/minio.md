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

# Using Minio Client
{: #minio}

Do you want to use familiar UNIX-like commands (`ls`, `cp`, `cat`, etc.) with {{site.data.keyword.cos_full}}? If so, the open source [Minio Client](https://min.io/download#/linux){:new_window} is the answer. You can find installation instructions for each operating system is available in the [quickstart guide](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} on the Minio website.

## Configuration
{: #minio-config}

Adding your {{site.data.keyword.cos_short}} is accomplished by running the following command:

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - short name for referencing {{site.data.keyword.cos_short}} in commands
* `<COS-ENDPOINT` - endpoint for your {{site.data.keyword.cos_short}} instance. For more information about endpoints, see [Endpoints and storage locations](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<ACCESS-KEY>` - access key that is assigned to your Service Credential
* `<SECRET-KEY>` - secret key that is assigned to your Service Credential

The configuration information is stored in a JSON file that is at `~/.mc/config.json`

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Sample Commands
{: #minio-commands}

A complete list of commands and optional flags and parameters are documented in the [Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window}

### `mb` - Make a Bucket
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - List Buckets
{: #minio-ls}

Though all your available buckets are listed, not all objects might be accessible depending on the specified [endpoint's](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) region.
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - List Objects
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

### `find` - Search for Objects by Name
{: #minio-find}

A full list of search options is available in the [complete guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - Display few lines of object
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - Copy objects
{: #minio-cp}

This command copies an object between two locations. These locations can be different hosts (such as different [endpoints](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) or storage services) or local file system locations (such as `~/foo/filename.pdf`).
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - Remove objects
{: #minio-rm}

*More removal options are available on the [complete guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - Copies STDIN to an object
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```