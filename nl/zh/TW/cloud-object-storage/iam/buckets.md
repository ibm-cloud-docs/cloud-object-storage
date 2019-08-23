---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# 儲存區許可權
{: #iam-bucket-permissions}

使用使用者介面或 CLI 來建立原則，以針對儲存區指派使用者及「服務 ID」的存取角色。

| 存取角色    | 範例動作                                                    |
|:------------|-------------------------------------------------------------|
| 管理員      | 將物件設為公用、建立及破壞儲存區和物件                      |
| 撰寫者      | 建立及破壞儲存區和物件                                      |
| 讀者        | 列出及下載物件                                              |
| 內容讀者    | 下載物件                                           |

## 授與存取權給使用者
{: #iam-user-access}

如果使用者需要能夠使用主控台，則除了服務存取角色（例如`讀者`）之外，**還**必須將實例本身的最低平台存取角色`檢視者`授與他們。這可讓他們檢視所有儲存區，並列出其內的物件。然後，從左導覽功能表中選取**儲存區許可權**，選取使用者，然後選取他們所需的存取層次（`管理員`或`撰寫者`）。

如果使用者將使用 API 來與資料互動、不需要主控台存取，_而且_ 他們是您帳戶的成員，則您可以授與對單一儲存區的存取權，而不需要對母實例進行任何存取。

## 原則強制執行
{: #iam-policy-enforcement}

IAM 原則會以從最大存取層次到最受限制層次的階層式方式強制執行。衝突會解析為較寬鬆的原則。例如，如果使用者同時具有儲存區的`撰寫者`及`讀者`服務存取角色，將會忽略授與`讀者`角色的原則。

這也適用於服務實例及儲存區層次原則。

- 如果使用者的原則授與服務實例的`撰寫者`角色及單一儲存區的`讀者`角色，將會忽略儲存區層次原則。
- 如果使用者的原則授與服務實例的`讀者`角色及單一儲存區的`撰寫者`角色，將會強制執行兩個原則，而且會優先使用個別儲存區的較寬鬆的`撰寫者`角色。

如果需要限制單一儲存區（或一組儲存區）的存取權，請使用主控台或 CLI 來確定使用者或「服務 ID」沒有任何實例層次原則。

### 使用使用者介面
{: #iam-policy-enforcement-console}

若要建立新的儲存區層次原則，請執行下列動作： 

  1. 從**管理**功能表中，導覽至**存取 IAM** 主控台。
  2. 從左導覽功能表中，選取**使用者**。
  3. 選取使用者。
  4. 選取**存取原則**標籤，以檢視使用者的現有原則、指派新的原則，或編輯現有原則。
  5. 按一下**指派存取權**，以建立新的原則。
  6. 選擇**指派對資源的存取權**。
  7. 請先從服務功能表中，選取 **Cloud Object Storage**。
  8. 然後，選取適當的服務實例。在**資源類型**欄位中輸入`儲存區`，並在**資源 ID** 欄位中輸入儲存區名稱。
  9. 選取所需的服務存取角色。
  10.  按一下**指派**。

請注意，讓**資源類型**或**資源**欄位保留空白，將會建立實例層次原則。
{:tip}

### 使用 CLI
{: #iam-policy-enforcement-cli}

從終端機中，執行下列指令：

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

若要列出現有原則，請執行下列指令：

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

若要編輯現有原則，請執行下列指令：

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## 授與對服務 ID 的存取權
{: #iam-service-id}
如果您需要授與對應用程式或其他非人類實體的儲存區的存取權，請使用「服務 ID」。基於此用途，會特別建立「服務 ID」，「服務 ID」也可以是正在使用的現有「服務 ID」。

### 使用使用者介面
{: #iam-service-id-console}

  1. 從**管理**功能表中，導覽至**存取 (IAM)** 主控台。
  2. 從左導覽功能表中，選取**服務 ID**。
  3. 選取「服務 ID」以檢視所有現有原則，並指派新的原則或編輯現有原則。
  3. 選取服務實例、服務 ID 及所需的角色。
  4. 在**資源類型**欄位中輸入`儲存區`，並在**資源**欄位中輸入儲存區名稱。
  5. 按一下**提交**。

  請注意，讓**資源類型**或**資源**欄位保留空白，將會建立實例層次原則。
{:tip}

### 使用 CLI
{: #iam-service-id-cli}

從終端機中，執行下列指令：

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

若要列出現有原則，請執行下列指令：

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

若要編輯現有原則，請執行下列指令：

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
