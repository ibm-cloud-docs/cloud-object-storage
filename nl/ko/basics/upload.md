---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# 데이터 업로드
{: #upload}

버킷을 구성한 후 몇 가지 오브젝트를 추가해야 합니다. 스토리지 사용 방법에 따라 시스템에 데이터를 가져오는 방법은 여러 가지가 있습니다. 데이터 과학자는 분석에 사용되는 몇 개의 대형 파일을 가지며, 시스템 관리자는 데이터베이스 백업을 로컬 파일과 동기화된 상태로 유지해야 하고, 개발자는 수백만 개의 파일을 읽고 써야 하는 소프트웨어를 작성합니다. 이러한 각 시나리오는 여러 데이터 수집 방법으로 가장 잘 제공됩니다.

## 콘솔 사용
{: #upload-console}

대개 웹 기반 콘솔 사용은 {{site.data.keyword.cos_short}}를 사용하는 가장 일반적인 방법은 아닙니다. 오브젝트는 200MB로 제한되며 파일 이름과 키는 동일합니다. 다중 오브젝트를 동시에 업로드할 수 있으며, 브라우저가 다중 스레드를 허용하는 경우 다중 파트를 동시에 사용하여 각 오브젝트를 업로드합니다. 대형 오브젝트 크기 및 성능 개선에 대한 지원은(네트워크 요인에 따라 다름) [Aspera 고속 전송](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)을 통해 제공됩니다.

## 호환 가능 도구 사용
{: #upload-tool}

일부 사용자는 독립형 유틸리티를 사용하여 스토리지와 상호작용하려고 합니다. Cloud Object Storage API는 S3 API 오퍼레이션의 가장 일반적인 세트를 지원하므로 많은 S3 호환 가능 도구는 [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)를 사용하여 {{site.data.keyword.cos_short}}에 연결될 수 있습니다.

일부 예에는 [Cyberduck](https://cyberduck.io/) 또는 [Transmit](https://panic.com/transmit/)과 같은 파일 탐색기, [Cloudberry](https://www.cloudberrylab.com/) 및 [Duplicati](https://www.duplicati.com/)와 같은 백업 유틸리티, [s3cmd](https://github.com/s3tools/s3cmd) 또는 [Minio Client](https://github.com/minio/mc)와 같은 명령행 유틸리티, 그리고 기타 많은 내용이 포함됩니다.

## API 사용
{: #upload-api}

Object Storage의 프로그래밍 방식의 애플리케이션 대부분은 SDK(예: [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 또는 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) 또는 [Cloud Object Storage API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)를 사용합니다. 일반적으로 오브젝트는 전송 관리자 클래스에서 파트의 수와 파트 크기가 구성된 [다중 파트](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)로 업로드됩니다.
