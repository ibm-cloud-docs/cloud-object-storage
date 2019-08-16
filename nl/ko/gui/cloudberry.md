---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

Cloudberry Backup은 사용자가 로컬 파일 시스템의 일부 또는 모두를 S3 API 호환 가능 Object Storage 시스템에 백업하는 데 사용할 수 있는 유연한 유틸리티입니다. 무료 버전과 전문가 버전은 Windows, MacOS 및 Linux에서 사용 가능하며 {{site.data.keyword.cos_full}}를 포함해 다수의 인기 있는 클라우드 스토리지 서비스를 지원합니다. Cloudberry Backup은 [cloudberrylab.com](https://www.cloudberrylab.com/)에서 다운로드할 수 있습니다.

Cloudberry Backup에는 다음을 포함한 많은 유용한 기능이 포함되어 있습니다.

* 스케줄링
* 증분 및 블록 레벨 백업
* 명령행 인터페이스
* 이메일 알림
* 압축(*전문가 버전만 해당*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Cloudberry Labs의 새 제품은 {{site.data.keyword.cos_short}}에 대한 익숙한 파일 관리 사용자 인터페이스를 제공합니다. [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window}는 무료 버전과 전문가 버전이 있지만 현재는 Windows에서만 사용 가능합니다. 주요 기능은 다음과 같습니다.

* 폴더/버킷 동기화
* 명령행 인터페이스
* ACL 관리
* 용량 보고서

전문가 버전은 다음과 같습니다.
* 검색 
* 암호화/압축
* 재개할 수 있는 업로드
* FTP/SFTP 지원

## Object Storage에서 Cloudberry 사용
{: #cloudberry-cos}

{{site.data.keyword.cos_short}}에서 작동하도록 Cloudberry 제품을 구성하는 경우 기억할 요점:

* 옵션 목록에서 `S3 호환` 선택
* [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials)만 현재 지원됨
* 각 버킷에 개별 연결이 필요함
* 연결에 지정된 `엔드포인트`가 선택한 버킷의 지역과 일치하는지 확인하십시오(*액세스 불가능한 대상으로 인해 백업 실패*). 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.
