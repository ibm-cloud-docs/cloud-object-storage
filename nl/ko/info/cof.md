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

# Cloud Object Storage를 Cloud Foundry 앱과 함께 사용
{: #cloud-foundry}

{{site.data.keyword.cos_full}}는 {{site.data.keyword.cfee_full}} 애플리케이션과 쌍을 이루어 지역 및 엔드포인트를 사용함으로써 고가용성 컨텐츠를 제공할 수 있습니다. 

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}}는 클라우드에서 앱과 서비스를 호스팅하기 위한 플랫폼입니다. 사용자는 자신의 계정 내에서 실행되며 공유 또는 전용 하드웨어에 배치될 수 있는 여러 격리된 엔터프라이즈용 플랫폼을 요청 시 인스턴스화할 수 있습니다. 이 플랫폼은 이용량 증가에 맞춰 앱을 쉽게 스케일링할 수 있게 해 주며, 사용자가 개발에만 집중할 수 있도록 런타임 및 인프라를 간소화합니다. 

Cloud Foundry 플랫폼을 성공적으로 구현하려면 필요한 리소스 및 엔터프라이즈 요구사항에 대한 [적절한 계획 및 디자인](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation)이 필요합니다. Cloud Foundry Enterprise Environment [시작하기](/docs/cloud-foundry?topic=cloud-foundry-about#creating)와 소개 [튜토리얼](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started)에서 더 자세히 알아보십시오. 

### 지역
{: #cloud-foundry-regions}
[지역 엔드포인트](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)는 IBM Cloud 환경의 중요한 부분입니다. 애플리케이션 관리를 위한 IBM 클라우드 인프라와 청구를 위한 사용량 세부사항 보기가 동일한 애플리케이션 및 서비스 인스턴스를 여러 지역에 작성할 수 있습니다. 고객과 가까운 IBM Cloud 지역을 선택하면 애플리케이션의 데이터 대기 시간을 줄이고 비용을 최소화할 수 있습니다. 보안 문제 또는 규제 요구사항을 해결하기 위해 지역을 선택할 수도 있습니다.  

{{site.data.keyword.cos_full}}를 사용하면 애플리케이션이 API 요청을 전송하는 [엔드포인트를 선택](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints)함으로써 단일 데이터 센터, 한 지역 전체, 또는 여러 지역의 조합 내에서 데이터를 분산시킬 수 있습니다. 

### 리소스 연결 및 별명
{: #cloud-foundry-aliases}

별명은 리소스 그룹 내 관리 서비스와 조직 또는 영역 내 애플리케이션 간의 연결입니다. 별명은 원격 리소스에 대한 참조가 있는 기호 링크와 같습니다. 이는 플랫폼 간의 상호 운용성 및 인스턴스 재사용을 가능하게 합니다. {{site.data.keyword.cloud_notm}} 콘솔에서 연결(별명)은 서비스 인스턴스로 표시됩니다. 사용자는 리소스에서 서비스 인스턴스를 작성한 후 사용 가능한 지역의 조직 또는 영역에 별명을 작성하여 해당 지역에서 이를 재사용할 수 있습니다. 

## 인증 정보를 VCAP 변수로서 저장 
{: #cloud-foundry-vcap}

{{site.data.keyword.cos_short}} 인증 정보는 VCAP_SERVICES 환경 변수에 저장할 수 있으며, 이는 {{site.data.keyword.cos_short}} 서비스에 액세스할 때 사용하기 위해 구문 분석될 수 있습니다. 이 인증 정보는 다음 예에 표시되어 있는 것과 같은 정보를 포함합니다. 

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

그 후에는 {{site.data.keyword.cos_short}} 컨텐츠에 액세스하기 위해 애플리케이션 내에서 VCAP_SERVICES 환경 변수를 구문 분석할 수 있습니다. 아래 내용은 Node.js를 사용하여 환경 변수를 COS SDK와 통합하는 예입니다. 

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

SDK를 사용하여 {{site.data.keyword.cos_short}}에 액세스하는 방법에 대한, 코드 예를 비롯한 자세한 정보는 다음 위치에 방문하면 볼 수 있습니다. 

* [Java 사용](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Python 사용](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Node.js 사용](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## 서비스 바인딩 작성 
{: #cloud-foundry-bindings}

### 대시보드
{: #cloud-foundry-bindings-console}

서비스 바인딩을 작성하는 가장 간단한 방법은 [{{site.data.keyword.cloud}} 대시보드](https://cloud.ibm.com/resources)를 사용하는 것입니다.  

1. [대시보드](https://cloud.ibm.com/resources)에 로그인하십시오. 
2. Cloud Foundry 애플리케이션을 클릭하십시오. 
3. 왼쪽에 있는 메뉴에서 연결을 클릭하십시오. 
4. 오른쪽에 있는 **연결 작성**을 클릭하십시오. 
5. *기존 호환 가능 서비스 연결* 페이지에서 {{site.data.keyword.cos_short}} 서비스 위로 마우스 커서를 이동한 후 **연결**을 클릭하십시오. 
6. *IAM 사용 서비스 연결* 팝업 화면에서 액세스 역할을 선택하고, 서비스 ID를 자동 생성으로 둔 후 **연결**을 클릭하십시오. 
7. 새 서비스 바인딩을 사용하려면 Cloud Foundry 애플리케이션을 다시 스테이징해야 합니다. **다시 스테이징**을 클릭하여 프로세스를 시작하십시오. 
8. 다시 스테이징이 완료되면 애플리케이션에서 Cloud Object Storage 서비스를 사용할 수 있습니다. 

애플리케이션 VCAP_SERVICES 환경 변수는 서비스 정보로 자동으로 업데이트됩니다. 새 변수를 보려면 다음 작업을 수행하십시오. 

1. 오른쪽에 있는 메뉴에서 *런타임*을 클릭하십시오. 
2. *환경 변수*를 클릭하십시오. 
3. COS 서비스가 나열되어 있는지 확인하십시오. 

### IBM 클라이언트 도구(CLI)
{: #cloud-foundry-bindings-cli}

1. IBM Cloud CLI를 사용하여 로그인하십시오. 
```
 ibmcloud login --apikey <your api key>
```

2. Cloud Foundry 환경을 대상으로 지정하십시오. 
```
 ibmcloud target --cf
```

3. {{site.data.keyword.cos_short}}에 대한 서비스 별명을 작성하십시오. 
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4.  {{site.data.keyword.cos_short}} 별명과 Cloud Foundry 애플리케이션 간의 서비스 바인딩을 작성하고 바인딩의 역할을 제공하십시오. 유효한 역할은 다음과 같습니다. <br/><ul><li>Writer</li><li>Reader</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Viewer</li><li>Editor</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### HMAC 인증 정보를 사용한 IBM 클라이언트 도구(CLI)
{: #cloud-foundry-hmac}

해시 기반 메시지 인증 코드(HMAC)는 액세스 키와 비밀 키의 쌍을 사용하여 작성되는 메시지 인증 코드를 계산하는 메커니즘입니다. 이 기술은 메시지의 무결성 및 신뢰성을 확인하는 데 사용할 수 있습니다. [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) 사용에 대한 자세한 정보는 {{site.data.keyword.cos_short}} 문서에 있습니다. 

1. IBM Cloud CLI를 사용하여 로그인하십시오. 
```
 ibmcloud login --apikey <your api key>
```

2. Cloud Foundry 환경을 대상으로 지정하십시오. 
```
 ibmcloud target --cf
```

3. {{site.data.keyword.cos_short}}에 대한 서비스 별명을 작성하십시오. 
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4.  {{site.data.keyword.cos_short}} 별명과 Cloud Foundry 애플리케이션 간의 서비스 바인딩을 작성하고 바인딩의 역할을 제공하십시오. <br/><br/>* **참고:* *HMAC를 사용으로 설정하여 서비스 인증 정보를 작성하려면 추가 매개변수*(`{"HMAC":true}`)*가 필요합니다. *<br/><br/>유효한 역할은 다음과 같습니다. <br/><ul><li>Writer</li><li>Reader</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Viewer</li><li>Editor</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### {{site.data.keyword.containershort_notm}}에 바인딩
{: #cloud-foundry-k8s}

{{site.data.keyword.containershort}}에 대한 서비스 바인딩을 작성하려면 약간 다른 프로시저가 필요합니다.  

*이 절의 작업을 수행하려면 [jq(경량 명령행 JSON 프로세서)](https://stedolan.github.io/jq/){:new_window}도 설치해야 합니다. *

사용자는 다음 정보를 확보하여 아래 명령행에서 키 값을 대체해야 합니다. 

* `<service alias>` - COS 서비스의 새 별명 이름
* `<cos instance name>` - 기존 COS 인스턴스의 이름
* `<service credential name>` - 서비스 키/인증 정보의 새 이름
* `<role>` - 서비스 키에 연결할 역할(유효한 역할은 위 내용 참조, 가장 일반적으로 지정되는 것은 `Writer`)
* `<cluster name>` - 기존 Kubernetes 클러스터 서비스의 이름
* `<secret binding name>` - 이 값은 COS가 클러스터 서비스에 바인드될 때 생성됨


1. COS 인스턴스의 서비스 별명을 작성하십시오. <br/><br/>* **참고:** COS 인스턴스는 하나의 서비스 별명만 가질 수 있습니다. *
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. COS 서비스 별명에 대한 권한이 있는 새 서비스 키를 작성하십시오. 
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. 클러스터 서비스를 COS에 바인드하십시오. 
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. 해당 COS 서비스 별명이 클러스터에 바인드되었는지 확인하십시오. 
```
ibmcloud cs cluster-services --cluster <cluster name>
```
출력은 다음과 같습니다.
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. 클러스터의 시크릿 목록을 검색하여 COS 서비스의 시크릿을 찾으십시오. 이는 일반적으로 `binding-`에 1단계에서 지정한 `<service alias>`(예: `binding-sv-cos`)이 결합된 항목입니다. 이 값을 6단계에서 `<secret binding name>`으로 사용하십시오. 
```
kubectl get secrets
```
출력은 다음과 같습니다.
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. 클러스터 시크릿에 해당 COS HMAC 인증 정보가 있는지 확인하십시오. 
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
출력은 다음과 같습니다.
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
