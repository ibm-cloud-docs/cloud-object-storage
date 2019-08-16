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


# CrossFTP를 사용하여 파일 전송
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window}는 {{site.data.keyword.cos_full}}를 포함해 S3 호환 가능 클라우드 스토리지 솔루션을 지원하는 전기능 FTP 클라이언트입니다. CrossFTP는 Mac OS X, Microsoft Windows, Linux를 지원하며 다음과 같은 기능이 포함된 무료, 전문가 및 엔터프라이즈 버전이 있습니다.

* 탭 형식 인터페이스
* 비밀번호 암호화
* 검색
* 일괄처리 전송
* 암호화(*전문가/엔터프라이즈 버전*)
* 동기화(*전문가/엔터프라이즈 버전*)
* 스케줄러(*전문가/엔터프라이즈 버전*)
* 명령행 인터페이스(*전문가/엔터프라이즈 버전*)

## IBM Cloud Object Storage에 연결
{: #crossftp-connect}

1. CrossFTP를 다운로드하고 설치하고 시작하십시오.
2. 오른쪽 분할창에서 더하기(+) 아이콘을 클릭하여 사이트 관리자를 열고 사이트를 새로 작성하십시오.
3. *일반* 탭 아래에 다음을 입력하십시오.
    * **프로토콜**을 `S3/HTTPS`로 설정하십시오.
    * **레이블**을 선택한 구체적 이름으로 설정하십시오.
    * **호스트**를 {{site.data.keyword.cos_short}} 엔드포인트로 설정하십시오(예: `s3.us.cloud-object-storage.appdomain.cloud`).
        * *엔드포인트 지역이 의도한 대상 버킷과 일치하는지 확인하십시오. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.*
    * **포트**를 `443`으로 두십시오.
    * **액세스 키** 및 **시크릿**을 대상 버킷에 대한 올바른 액세스 권한이 있는 HMAC 인증 정보로 설정하십시오.
4. *S3* 탭에서
    * `DevPay 사용`을 선택하지 않았는지 확인하십시오.
    * **API 세트 ...**를 클릭하고 `Dev Pay` 및 `CloudFront Distribution`을 선택하지 않았는지 확인하십시오.
5. ***Mac OS X에만 해당***
    * 메뉴 표시줄에서 *보안 > TLS/SSL 프로토콜...*을 클릭하십시오.
    * `사용 가능한 프로토콜 사용자 정의` 옵션을 선택하십시오.
    * `TLSv1.2`를 **사용** 상자에 추가하십시오.
    * **확인**을 클릭하십시오.
6. ***Linux에만 해당***
    * 메뉴 표시줄에서 *보안 > 암호 설정...*을 클릭하십시오.
    * `사용 가능한 암호 스위트 사용자 정의` 옵션을 선택하십시오.
    * `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`를 **사용** 상자에 추가하십시오.
    * **확인**을 클릭하십시오.
7. **적용**을 클릭한 후 **닫기**를 클릭하십시오.
8. *사이트* 아래 새 항목이 3단계에 제공된 *레이블*과 함께 사용 가능해야 합니다.
9. 새 항목을 두 번 클릭하여 엔드포인트에 연결하십시오.

여기서 창에 사용 가능한 모든 버킷 목록이 표시되며 사용 가능한 파일을 찾아보고 로컬 디스크에서 전송할 수 있습니다.
