---

copyright:
  years: 2017, 2022
lastupdated: "2022-04-11"

keywords: juicefs, open source, file system, gateway

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Using `JuiceFS`
{: #juicefs}

`JuiceFS` helps you use {{site.data.keyword.cos_full}} locally as massive local disks and to be mounted and read on different cross-platform and cross-region hosts at the same time.
{: shortdesc}

For a more detailed overview, [visit the project's documentation](https://juicefs.com/docs/community/introduction/).

## Prerequisites
{: #juicefs-prereqs}

* IBM Cloud account and an instance of {{site.data.keyword.cos_full}}
* A Linux or macOS environment
* Credentials ([HMAC credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main))

## Installation
{: #juicefs-install}

For Linux systems with x86 architecture, download the file with the file name linux-amd64 and execute the following command in the terminal.

1. Get the latest version number

  ```sh
  JFS_LATEST_TAG=$(curl -s https://api.github.com/repos/juicedata/juicefs/releases/latest | grep 'tag_name' | cut -d '"' -f 4 | tr -d 'v')
  ```
  {:codeblock}

2. Download the client to the current directory

  ```sh
  wget "https://github.com/juicedata/juicefs/releases/download/v${JFS_LATEST_TAG}/juicefs-${JFS_LATEST_TAG}-linux-amd64.tar.gz"
  ```
  {:codeblock}

3. Unzip the installation package

  ```sh
  tar -zxf "juicefs-${JFS_LATEST_TAG}-linux-amd64.tar.gz"
  ```
  {:codeblock}

4. Install the client

  ```sh
  sudo install juicefs /usr/local/bin
  ```
  {:codeblock}

For macOS, you need to install [macFUSE](https://osxfuse.github.io) first and then install `JuiceFS` with [Homebrew](https://brew.sh):

1. Add the Tap
  
  ```sh
  brew tap juicedata/homebrew-tap
  ```

2. Install the client

  ```sh
  brew install juicefs
  ```

Be aware that macFUSE is closed-source software containing a kernel extension, and may require a license for commercial use.
{:important}

## Creating A File System
{: #juicefs-create-filesystem}

`JuiceFS` implements a distributed file system by adopting the architecture that seperates "data" and "metadata" storage. The data itself is persisted in object storage (e.g., {{site.data.keyword.cos_full}}), and the corresponding metadata can be persisted in various databases such as Redis, MySQL, TiKV, SQLite, etc., based on the scenarios and requirements.

If the file system needs to be used by multiple hosts on a shared basis, use a cloud database that can be accessed over the network. For single-host use only, you can use a SQLite database.

Here is an example of creating a standalone file system. The command format is as follows.

```sh
juicefs format --storage ibmcos \
--bucket http{s}://<bucket_name>.<endpoint> \
--access-key <access_key> \
--secret-key <secret_key> \
<meta-url> <fs-name>
```
{:codeblock}

For example, create a file system named `myjfs`, use {{site.data.keyword.cos_full}} as the data store, and use a SQLite database named `myjfs.db` to store the metadata.

```sh
juicefs format --storage ibmcos \
--bucket http{s}://<bucket_name>.<endpoint> \
--access-key <access_key> \
--secret-key <secret_key> \
sqlite3://$HOME/myjfs.db myjfs
```
{:codeblock}

If you using JuiceFS on {{site.data.keyword.BluVirtServers}}, please choose **Private endpoints**，it provides better performance and do not incur charges for any outgoing or incoming bandwidth even if the traffic is cross regions or across data centers. Whenever possible, it is best to use a private endpoint.
{: tip}

{{site.data.keyword.databases-for-redis}} is a better choice for JuiceFS to store metadata.

## Mounting the File System
{: #juicefs-mount}

The information for <endpoint>, <access_key>, <secret_key> and <fs-name> will be stored in `myjfs.db` when formating a file system. So you don't need to enter this information repeatedly when mounting the filesystem.

Mount a file system using the following format.

```sh
juicefs mount [command options] <meta-url> <mountpoint>
```
{:codeblock}

In short, just specify the database path and mount point. For example, mounting the filesystem to the `/mnt/jfs` directory.

```sh
sudo juicefs mount sqlite3://$HOME/myjfs.db /mnt/jfs
```
{:codeblock}

It can be mounted in the background using the `-d` option.

```sh
sudo juicefs mount -d sqlite3://$HOME/myjfs.db /mnt/jfs
```
{:codeblock}

After successful mounting, any files written into `/mnt/jfs` will be stored in {{site.data.keyword.cos_full}}.

## Unmount A File System
{: #juicefs-unmount}

```sh
sudo juicefs umount <mountpoint>
```
{:codeblock}

## Note

When using JuiceFS, files will eventually be split into Chunks, Slices and Blocks and stored in object storage. Therefore, you may notice that the source files stored in JuiceFS cannot be found in the file browser of the object storage platform; instead, there are only a directory of chunks and a bunch of numbered directories and files in the bucket. Don't panic! That's exactly what makes JuiceFS a high-performance file system. [Read about the technical architecture of `JuiceFS`](https://juicefs.com/docs/community/architecture).
{: tip}