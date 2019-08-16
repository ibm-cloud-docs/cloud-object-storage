---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# 搭配使用 Cloud Object Storage 與 Cloud Foundry 應用程式
{: #cloud-foundry}

{{site.data.keyword.cos_full}} 可以與 {{site.data.keyword.cfee_full}} 應用程式配對，藉由使用地區及端點來提供高可用性內容。

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} 是在雲端中用來管理應用程式及服務的平台。您可以依需求實例化在自己的帳戶內執行且可部署於共用或專用硬體的多個隔離企業級平台。此平台可讓您隨著使用成長而輕鬆地調整應用程式，並簡化運行環境及基礎架構，讓您可以專注於開發。

成功的 Cloud Foundry 平台實作需要[適當的規劃及設計](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation)，才能取得必要的資源及企業需求。進一步瞭解[開始使用](/docs/cloud-foundry?topic=cloud-foundry-about#creating) Cloud Foundry Enterprise Environment 以及介紹性[指導教學](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started)。

### 地區
{: #cloud-foundry-regions}
[地區端點](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)是 IBM Cloud Environment 的重要部分。您可以在具有相同 IBM Cloud 基礎架構的不同地區中建立應用程式及服務實例來管理應用程式，以及使用相同的用量詳細資料視圖來計費。藉由選擇地理上接近您或您客戶的 IBM Cloud 地區，即可降低應用程式中的資料延遲，並且最小化成本。您也可以選取地區，來解決所有安全考量或法規需求。 

使用 {{site.data.keyword.cos_full}}，您可以藉由[選取端點](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)（應用程式傳送 API 要求的位置），來選擇要將資料分散到單一資料中心、整個地區，或甚至是地區組合。

### 資源連線及別名
{: #cloud-foundry-aliases}

別名是資源群組內的受管理服務與組織或空間內的應用程式之間的連線。別名就像符號鏈結，可保留遠端資源的參照。它支援跨平台的實例交互作業及重複使用。在 {{site.data.keyword.cloud_notm}} 主控台中，此連線（別名）會以服務實例來代表。您可以在資源群組中建立服務的實例，然後在那些地區的組織或空間中建立別名，以從任何可用的地域重複使用它。

## 將認證儲存為 VCAP 變數 
{: #cloud-foundry-vcap}

{{site.data.keyword.cos_short}} 認證可以儲存在 VCAP_SERVICES 環境變數中，在存取 {{site.data.keyword.cos_short}} 服務時，可以剖析該變數以供使用。認證包括下列範例所呈現的資訊：

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
"apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

然後，您可以在應用程式內剖析 VCAP_SERVICES 環境變數，以存取 {{site.data.keyword.cos_short}} 內容。下面範例說明如何使用 Node.js 來整合此環境變數與 COS SDK。

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

如需如何使用 SDK 來存取具有程式碼範例之 {{site.data.keyword.cos_short}} 的相關資訊，請造訪：

* [使用 Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [使用 Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [使用 Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## 建立服務連結 
{: #cloud-foundry-bindings}

### 儀表版
{: #cloud-foundry-bindings-console}

建立服務連結的最簡單方式是使用 [{{site.data.keyword.cloud}} 儀表板](https://cloud.ibm.com/resources)。 

1. 登入[儀表板](https://cloud.ibm.com/resources)。
2. 按一下 Cloud Foundry 應用程式。
3. 按一下左側功能表中的「連線」。
4. 按一下右側的**建立連線**。
5. 從*連接現有相容服務* 頁面中，將游標移至 {{site.data.keyword.cos_short}} 服務上方，然後按一下**連接**。
6. 從*連接已啟用 IAM 的服務* 蹦現畫面中，選取「存取角色」，將「服務 ID」保留為「自動產生」，然後按一下**連接**。
7. 需要重新編譯打包 Cloud Foundry 應用程式，才能使用新的服務連結。按一下**重新編譯打包**，以啟動處理程序。
8. 重新編譯打包完成之後，應用程式就可以使用 Cloud Object Storage 服務。

使用服務資訊自動更新應用程式 VCAP_SERVICES 環境變數。若要檢視新變數，請執行下列動作：

1. 按一下右側功能表中的*運行環境*。
2. 按一下*環境變數*。
3. 驗證現在已列出 COS 服務。

### IBM 用戶端工具 (CLI)
{: #cloud-foundry-bindings-cli}

1. 使用 IBM Cloud CLI 登入。
```
 ibmcloud login --apikey <your api key>
```

2. 將目標設為 Cloud Foundry 環境。
```
 ibmcloud target --cf
```

3. 建立 {{site.data.keyword.cos_short}} 的服務別名。
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. 在 {{site.data.keyword.cos_short}} 別名與 Cloud Foundry 應用程式之間建立服務連結，並為連結提供一個角色。有效的角色如下：<br/><ul><li>撰寫者</li><li>讀者</li><li>管理員</li><li>管理者</li><li>操作員</li><li>檢視者</li><li>編輯者</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### 具有 HMAC 認證的 IBM 用戶端工具 (CLI)
{: #cloud-foundry-hmac}

雜湊型訊息鑑別碼 (HMAC) 是一種機制，用於計算使用一對存取及密碼金鑰所建立的訊息鑑別碼。此技術可以用來驗證訊息的完整性及確實性。{{site.data.keyword.cos_short}} 文件中提供使用 [HMAC 認證](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)的相關資訊。

1. 使用 IBM Cloud CLI 登入。
```
 ibmcloud login --apikey <your api key>
```

2. 將目標設為 Cloud Foundry 環境。
```
 ibmcloud target --cf
```

3. 建立 {{site.data.keyword.cos_short}} 的服務別名。
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. 在 {{site.data.keyword.cos_short}} 別名與 Cloud Foundry 應用程式之間建立服務連結，並為連結提供一個角色。<br/><br/>***附註：*需要有*額外的參數* (`{"HMAC":true}`)*，才能建立已啟用 HMAC 的服務認證。*<br/><br/>有效的角色如下：<br/><ul><li>撰寫者</li><li>讀者</li><li>管理員</li><li>管理者</li><li>操作員</li><li>檢視者</li><li>編輯者</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### 與 {{site.data.keyword.containershort_notm}} 的連結
{: #cloud-foundry-k8s}

建立與 {{site.data.keyword.containershort}} 的服務連結時，需要稍微不同的程序。 

*在本節中，您同時需要安裝 [jq（輕量型指令行 JSON 處理器）](https://stedolan.github.io/jq/){:new_window}。*

您需要下列資訊，並替換下面指令中的索引鍵值：

* `<service alias>` - COS 服務的新別名
* `<cos instance name>` - 現有 COS 實例的名稱
* `<service credential name>` - 服務金鑰/認證的新名稱
* `<role>` - 要附加至服務金鑰的角色（如需有效的角色，請參閱上面，最常指定的是 `Writer` 角色）
* `<cluster name>` - 現有 Kubernetes 叢集服務的名稱
* `<secret binding name>` - 當 COS 連結至叢集服務時，即會產生此值


1. 建立 COS 實例的服務別名。<br/><br/>***附註：**「COS 實例」只能有一個服務別名*
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. 建立具有 COS 服務別名許可權的新服務金鑰
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. 將叢集服務連結至 COS。
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. 驗證 COS 服務別名已連結至叢集。
```
ibmcloud cs cluster-services --cluster <cluster name>
```
輸出將看起來如下：
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. 擷取叢集中的「密碼」清單，並尋找 COS 服務的密碼。通常，它會是 `binding-` 加上您在步驟 1 指定的 `<service alias>`（即 `binding-sv-cos`）。請使用此值作為步驟 6 中的 `<secret binding name>`。
```
  kubectl get secrets
  ```
輸出應看起來如下：
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. 驗證叢集「密碼」中具有 COS HMAC 認證。
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
輸出應看起來如下：
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
