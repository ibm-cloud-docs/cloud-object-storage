---
copyright:
  years: 2017
lastupdated: '2017-09-27'
---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Manage encryption

All objects stored in {{site.data.keyword.cos_full}} are encrypted by default using multiple randomly generated keys and an all-or-nothing-transform. While this default encryption model is highly secure, some workloads need to be in possession of the encryption keys used.  Keys can either be managed manually by a client, or can be managed using IBM Key Protect.

For more information on Key Protect, [see the documentation](/docs/services/keymgmt/index.html#getting-started-with-key-protect).
