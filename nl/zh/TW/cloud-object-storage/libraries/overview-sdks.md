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

# 關於 IBM COS SDK
{: #sdk-about}

IBM COS 提供 Java、Python、NodeJS 及 Go 的 SDK。這些 SDK 是根據正式的 AWS S3 API SDK，但已修改為使用 IBM Cloud 特性，例如 IAM、Key Protect、Immutable Object Storage 及其他特性。

| 特性 | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
|IAM API 金鑰支援| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |
|受管理多部分上傳| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |
|受管理多部分下載| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |                                                   |                                                   |
|延伸儲存區清單|                                                   |                                                   |                                                   |                                                   |                                                   |
|第 2 版物件清單| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |                                                   |                                                   |
|Key Protect| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |
|SSE-C| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |                                                   |                                                   |
|保存規則| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |                                                   |                                                   |
|保留原則| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aspera 高速傳輸| ![勾號](../../icons/checkmark-icon.svg) | ![勾號](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## IAM API 金鑰支援
{: #sdk-about-iam}
允許使用 API 金鑰建立用戶端，而非存取金鑰/密碼金鑰的配對。記號管理為自動處理，且記號會在長時間執行的作業期間自動重新整理。
## 受管理多部分上傳
SDK 會使用 `TransferManager` 類別，處理以多個部分上傳物件的所有必要邏輯。
## 受管理多部分下載
SDK 會使用 `TransferManager` 類別，處理以多個部分下載物件的所有必要邏輯。
## 延伸儲存區清單
這是 S3 API 的延伸，它會在列出時傳回具有儲存區佈建碼的儲存區清單（儲存區位置和儲存空間類別的結合，以 `LocationConstraint` 的形式傳回）。這適用於尋找儲存區，因為服務實例中的儲存區會全部列出，而不管使用的端點為何。
## 第 2 版物件清單
第 2 版清單允許更強大的物件清單範圍設定。
## Key Protect
Key Protect 是 IBM Cloud 服務，它會管理加密金鑰，且是儲存區建立期間的選用性參數。
## SSE-C                      
## 保存規則              
## 保留原則         
##  Aspera 高速傳輸 
