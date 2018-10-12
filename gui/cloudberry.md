---

copyright:
  years: 2017
lastupdated: "2018-02-15"

---
{:shortdesc: .shortdesc}
{:new_window: target="_blank"}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}


# Backup files with Cloudberry

Cloudberry is a flexible backup utility that allows users to back up some or all of a local filesystem to an S3 API compatible object storage system. It can be configured to be run either manually or scheduled based on need, and can backup different directories to different buckets if needed.  Cloudberry can be downloaded from [cloudberrylab.com](http://www.cloudberrylab.com/).

When configuring Cloudberry, select 'S3 Compatible' from the list of options, not 'Amazon S3' or 'SoftLayer'.
{:tip}

The current release of the Cloudberry Client for Windows uses TLSv1.0 for establishing secure data transmission over the public Internet.  IBM Cloud requires the more modern TLSv1.1 or TLSv1.2 to establish a secure connection. Connections from the Cloudberry Client for Windows will fail unless the 'Use SSL' box is left **unchecked** during configuration.
{:tip}
