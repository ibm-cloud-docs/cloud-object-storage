---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, simpana

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


# CommVault Simpana를 사용하여 데이터 아카이브
{: #commvault}

CommVault Simpana는 {{site.data.keyword.cos_full_notm}}의 아카이브 티어와 통합됩니다. Simpana에 대한 자세한 정보는 [CommVault Simpana 문서](https://documentation.commvault.com/commvault/)를 참조하십시오.

IBM COS 인프라 아카이브에 대한 자세한 정보는 [수행 방법: 데이터 아카이브](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive)를 참조하십시오.

## 통합 단계
{: #commvault-integration}

1.	Simpana 콘솔에서 Amazon S3 클라우드 스토리지 라이브러리를 작성하십시오. 

2. 서비스 호스트가 엔드포인트를 가리키는지 확인하십시오. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. Simpana는 이 단계에서 버킷을 프로비저닝하거나 프로비저닝된 버킷을 이용할 수 있습니다. 

3.	버킷에 대한 정책을 작성하십시오. AWS CLI, SDK 또는 웹 콘솔을 사용하여 정책을 작성할 수 있습니다. 정책의 예는 다음과 같습니다.

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### 정책을 버킷과 연관시키기
{: #commvault-assign-policy}

1. 다음 CLI 명령을 실행하십시오.

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Simpana에서 스토리지 정책을 작성하고 이 스토리지 정책을 1단계에서 작성한 클라우드 스토리지 라이브러리에 연관시키십시오. 스토리지 정책은 Simpana가 백업 전송을 위해 COS와 상호작용하는 방식을 통제합니다. 정책 개요는 [여기](https://documentation.commvault.com/commvault/v11/article?p=13804.htm)에 있습니다.

3.	백업 세트를 작성하고 이전 단계에서 작성된 스토리지 정책에 백업 세트를 연관시키십시오. 백업 세트 개요는 [여기](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)에 있습니다.

## 백업 수행
{: #commvault-backup}

정책을 사용하여 버킷에 대한 백업을 시작하고 {{site.data.keyword.cos_full_notm}}에 대한 백업을 수행할 수 있습니다. Simpana 백업에 대한 자세한 정보는 [여기](https://documentation.commvault.com/commvault/v11/article?p=11677.htm)에서 볼 수 있습니다. 백업 컨텐츠가 버킷에 구성된 정책을 기반으로 한 아카이브 티어로 전이됩니다.

## 복원 수행
{: #commvault-restore}

{{site.data.keyword.cos_full_notm}}에서 백업 컨텐츠를 복원할 수 있습니다. Simpana 복원에 대한 자세한 정보는 [여기](https://documentation.commvault.com/commvault/v11/article?p=12867.htm)에 있습니다.

### 아카이브 티어에서 오브젝트를 자동으로 복원하도록 Simpana 구성
{: #commvault-auto-restore}

1. COS에서 백업을 복원하는 경우 {{site.data.keyword.cos_full_notm}} 복원을 트리거하는 태스크를 작성하십시오. 구성하려면 [CommVault Simpana 문서](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)를 참조하십시오.

2. 클라우드 스토리지 재호출 태스크를 통해 아카이브 티어에서 원래 티어로 백업된 컨텐츠를 복원하십시오. Simpana가 {{site.data.keyword.cos_full_notm}}에서 리턴 코드를 수신하면 이 태스크가 실행됩니다. 아카이브 재호출에 대한 자세한 정보는 [여기](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053)에 있습니다.

3. 복원(아카이브 티어에서 원래 티어로)이 완료되면 Simpana가 컨텐츠를 읽고 원래 위치 또는 구성된 위치에 기록합니다.
