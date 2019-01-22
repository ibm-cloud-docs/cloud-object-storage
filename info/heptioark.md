---

copyright:
  years: 2019
lastupdated: "2019-01-22"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Heptio Ark Integration

[Ark](https://github.com/heptio/ark){:new_window} is a toolset provided by [Heptio](https://heptio.com/){:new_window} to backup and restore your Kubernetes cluster resources.  Ark supports the use of S3-compatible storage providers including {{site.data.keyword.cos_full}} for different backup/snapshot operations.

Ark consists of two parts:

* Server component that runs on the cluster
* Command-line tool that runs on a local client

## Prerequisites

Before you begin you'll need to ensure you have the following:

* The `kubectl` command-line tool installed and configured to connect to your cluster
* An {{site.data.keyword.cos_short}} instance 
* A {{site.data.keyword.cos_short}} bucket
* HMAC credentials with Writer access to bucket

## Install Ark Client

1. Download the latest [release](https://github.com/heptio/ark/releases){:new_window} of Ark for your OS
2. Extract the tarball to a folder on your local system
3. Verify you can run the `ark` binary such as the following:

```
./ark --help
```

You should see the following output:

```
Heptio Ark is a tool for managing disaster recovery, specifically for Kubernetes
cluster resources. It provides a simple, configurable, and operationally robust
way to back up your application state and associated data.

If you're familiar with kubectl, Ark supports a similar model, allowing you to
execute commands such as 'ark get backup' and 'ark create schedule'. The same
operations can also be performed as 'ark backup get' and 'ark schedule create'.

Usage:
  ark [command]

Available Commands:
  backup            Work with backups
  backup-location   Work with backup storage locations
  bug               Report an Ark bug
  client            Ark client related commands
  completion        Output shell completion code for the specified shell (bash or zsh)
  create            Create ark resources
  delete            Delete ark resources
  describe          Describe ark resources
  get               Get ark resources
  help              Help about any command
  plugin            Work with plugins
  restic            Work with restic
  restore           Work with restores
  schedule          Work with schedules
  server            Run the ark server
  snapshot-location Work with snapshot locations
  version           Print the ark version and associated image
...
```

4. *(OPTIONAL)* Move the ark binary out of the temporary folder to somewhere more permanent such as `/usr/local/bin` on Mac OS or Linux.

## Install and Configure Ark Server



## Testing Backup/Restore