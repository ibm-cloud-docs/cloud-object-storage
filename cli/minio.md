---

copyright:
  years: 2019
lastupdated: "2019-01-10"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Using Minio Client

[Minio Client](https://www.minio.io/downloads.html#download-client){:new_window} is a CLI tool that provides UNIX-like commands (ls, cp, cat, etc.) that supports S3 compatible cloud storage services including {{site.data.keyword.cos_full}}.  It supports Linux, Microsoft Windows&reg;, and Mac OS X.  Advanced users and developers can also install from source.  Official releases for Minio Client is available at https://minio.io/downloads/#minio-client.  Installation instructions for each operating system is available on their [quickstart guide](https://docs.minio.io/docs/minio-client-quickstart-guide.html){:new_window}.

## Configuration

Adding your {{site.data.keyword.cos_short}} is accomplished by simply running the following command:

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - short name for referencing {{site.data.keyword.cos_short}} in commands
* `<COS-ENDPOINT` - endpoint for your {{site.data.keyword.cos_short}} instance 
* `<ACCESS-KEY>` - access key assigned to your Service Credential
* `<SECRET-KEY>` - secret key assigned to your Service Credential

The configuration information is stored in a JSON file located at `~/.mc/config.json`

*Example:*
```
mc config host add cosgeo https://s3-api.us-geo.objectstorage.softlayer.net xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Sample Commands

Below are brief examples for some of the most common commands available in Minio.  A complete list of commands and optional flags/parameters are documented in the [Minio Client Complete Guide](https://docs.minio.io/docs/minio-client-complete-guide){:new_window}

### mb - Make a Bucket

```
mc mb cosgeo/my_test_bucket
```

### ls - List Buckets
*Though all your available bucket will be listed, not all objects may be accessible depending on the specified endpoint's region*
```
mc ls cosgeo
```
*Output:*
```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### ls - List Objects

```
mc ls cosgeo/testbucket1
```
*Output:*
```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
[2018-08-10 09:49:08 HST]  20MiB newbigfile.pdf
[2018-11-29 09:53:15 HST]    31B testsave.txt
```

### find - Search for Objects by Name

*A full list of search options are available in the [complete guide](https://docs.minio.io/docs/minio-client-complete-guide#find){:new_window}*

```
mc find cosgeo/testbucket1 --name my*
```
*Output:*
```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### head - Display few lines of object

```
mc head cosgeo/testbucket1/mynewfile1.txt
```

### cp - Copy objects

```
mc cp cosgeo/testbucket1/mynewfile1.txt cosgeo/my_test_bucket/cp_from_minio.txt
```
*Output:*
```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### rm - Remove objects

*More removal options are available on the [complete guide](https://docs.minio.io/docs/minio-client-complete-guide#rm){:new_window}*

```
mc rm cosgeo/my_test_bucket/cp_from_minio.txt
```

### pipe - Copies STDIN to an object

```
echo -n 'this is a test' | mc pipe cosgeo/my_test_bucket/stdin_pipe_test.txt
```