---

copyright:
  years: 2017
lastupdated: "29-11-2017"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Mount a bucket using `s3fs`

Applications that expect to read and write to a NFS-style filesystem can use `s3fs`, which can mount a bucket as directory while preserving the native object format for files.  