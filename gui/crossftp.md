---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# Transfer files using CrossFTP
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} is a full-featured FTP client that supports S3-compatible cloud storage solutions including {{site.data.keyword.cos_full}}.  CrossFTP supports Mac OS X, Microsoft Windows, Linux and comes in Free, Pro, and Enterprise versions with features such as:

* Tabbed Interface
* Password Encryption
* Search
* Batch Transfer
* Encryption (*Pro/Enterprise versions*)
* Synchronization (*Pro/Enterprise versions*)
* Scheduler (*Pro/Enterprise versions*)
* Command-line interface (*Pro/Enterprise versions*)

## Connecting to IBM Cloud Object Storage
{: #crossftp-connect}

1. Download, install, and start CrossFTP.
2. In the right-pane, create a new Site by click on the plus (+) icon to open the Site Manager.
3. Under the *General* tab enter the following:
    * Set **Protocol** to `S3/HTTPS`
    * Set **Label** to a descriptive name of your choosing
    * Set **Host** to the an {{site.data.keyword.cos_short}} endpoint (i.e. `s3.us.cloud-object-storage.appdomain.cloud`)
        * *Ensure the endpoint region matches the intended target bucket*
    * Leave **Port** as `443`
    * Set **Access Key** and **Secret** to HMAC credentials with the proper access rights to your target bucket
4. Under the *S3* tab
    * Ensure `Use DevPay` is unchecked
    * Click **API Set ...** and ensure `Dev Pay` and `CloudFront Distribution` is unchecked
5. ***For Mac OS X only***
    * Click on *Security > TLS/SSL Protocols...* in the menu bar
    * Select the option `Customize the enabled protocols`
    * Add `TLSv1.2` to the **Enabled** box
    * Click **OK**
6. ***For Linux only***
    * Click on *Security > Cipher Settings...* in the menu bar
    * Select the option `Customize the enabled cipher suites`
    * Add `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` to the **Enabled** box
    * Click **OK**
7. Click **Apply** then **Close**
8. A new entry under *Sites* should be available with the *Label* provided in step 3
9. Double-click on the new entry to connect to the endpoint

From here the window displays a list of the available buckets and you can browse the available files and transfer them to and from your local disks.