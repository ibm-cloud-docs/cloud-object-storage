---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-19"

keywords: sdks, overview

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
{:go: .ph data-hd-programlang='go'}

# 关于 IBM COS SDK
{: #sdk-about}

IBM COS 提供了 SDK for Java、SDK for Python、SDK for NodeJS 和 SDK for Go。这些 SDK 基于官方 AWS S3 API SDK，但已修改为使用 IBM Cloud 功能（如 IAM、Key Protect、不可变对象存储器等）。

|功能|Java|Python|NodeJS|GO|CLI|
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
|IAM API 密钥支持| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|
|受管分块上传| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|
|受管分块下载| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|扩展存储区列示|                                                   |                                                   |                                                   |                                                   |                                                   |
|V2 对象列示| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|Key Protect| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|
|SSE-C| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|归档规则| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|保留时间策略| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|Aspera 高速传输| ![“复选标记”图标](../../icons/checkmark-icon.svg)| ![“复选标记”图标](../../icons/checkmark-icon.svg)|                                                   |                                                   |                                                   |

## IAM API 密钥支持
{: #sdk-about-iam}
允许使用 API 密钥（而不是访问密钥/私钥对）来创建客户机。令牌管理是自动处理的，在长时间运行的操作期间，令牌会自动刷新。
## 受管分块上传
通过使用 `TransferManager` 类，SDK 将处理用于分块上传对象的所有必需逻辑。
## 受管分块下载
通过使用 `TransferManager` 类，SDK 将处理用于分块下载对象的所有必需逻辑。
## 扩展存储区列示
这是 S3 API 的扩展，用于在列示时返回具有供应代码的存储区列表（存储区的位置和存储类的组合，作为 `LocationConstraint` 返回）。这对于查找存储区非常有用，因为服务实例中的存储区会全部列出，而与使用的端点无关。
## V2 对象列示
V2 列示可实现对象列表作用域限定的更强大功能。
## Key Protect
Key Protect 是一项 IBM Cloud 服务，用于管理加密密钥，在创建存储区期间是可选参数。
## SSE-C                      
## 归档规则              
## 保留时间策略         
## Aspera 高速传输 
