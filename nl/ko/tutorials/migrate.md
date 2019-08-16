---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# OpenStack Swift에서의 데이터 마이그레이션
{: #migrate}

{{site.data.keyword.cos_full_notm}}를 {{site.data.keyword.cloud_notm}} 플랫폼 서비스로 사용할 수 있게 되기 전에는 오브젝트 저장소를 필요로 하는 프로젝트가 [OpenStack Swift](https://docs.openstack.org/swift/latest/) 또는 [OpenStack Swift(인프라)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift)를 사용했었습니다. IBM에서는 IAM 및 Key Protect에서 제공하는 새 액세스 제어 및 암호화 이점을 활용하고 출시되는 새 기능을 이용할 수 있도록 개발자들에게 애플리케이션을 업데이트하고 데이터를 {{site.data.keyword.cloud_notm}}로 마이그레이션하도록 권장하고 있습니다. 

Swift '컨테이너'는 COS '버킷'과 동일한 개념입니다. COS는 서비스 인스턴스를 100개의 버킷으로 제한하며 일부 Swift 인스턴스에는 이보다 더 많은 수의 컨테이너가 있을 수 있습니다. COS 버킷은 수십억 개의 오브젝트를 저장할 수 있으며 데이터를 정리할 때 디렉토리와 유사한 '접두부'를 사용할 수 있도록 오브젝트 이름에 슬래시((`/`)를 지원합니다. COS는 버킷 및 서비스 인스턴스 레벨에서 IAM 정책을 지원합니다.
{:tip}

오브젝트 스토리지 서비스 간에 데이터를 마이그레이션하는 데 대한 한 가지 접근법은 [오픈 소스 `rclone` 명령행 유틸리티](https://rclone.org/docs/)와 같은 '동기화' 또는 '복제' 도구를 사용하는 것입니다. 이 유틸리티는 클라우드 스토리지를 비롯한 두 위치 간의 파일 트리를 동기화합니다. `rclone`은 COS에 데이터를 기록할 때 COS/S3 API를 사용하여 대형 오브젝트를 분할하고 구성 매개변수로 설정된 크기 및 임계값에 따라 파트를 병렬로 업로드합니다. 

COS와 Swift 사이에는 데이터 마이그레이션의 일부로서 고려해야 하는 몇 가지 차이점이 있습니다. 

  - COS는 아직 만기 정책 또는 버전화를 지원하지 않습니다. 이러한 Swift 기능에 의존하는 워크플로우는 COS로의 마이그레이션 시 이를 자신의 애플리케이션 로직의 일부로서 처리해야 합니다. 
  - COS는 오브젝트 레벨 메타데이터를 지원하지만, `rclone`을 사용하여 데이터를 마이그레이션하는 경우에는 이 정보가 보존되지 않습니다. `x-amz-meta-{key}: {value}` 헤더를 사용하여 COS의 오브젝트에 사용자 정의 메타데이터를 설정할 수 있지만, `rclone`을 사용하기 전에 해당 오브젝트 레벨 메타데이터를 데이터베이스에 백업하는 것이 좋습니다. 사용자 정의 메타데이터는 [오브젝트를 자체 복사](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object)하여 기존 오브젝트에 적용할 수 있습니다. 시스템은 오브젝트 데이터가 동일한 것을 인식하며 메타데이터만 업데이트합니다. `rclone`은 시간소인을 보존 **가능**하다는 점을 참고하십시오. 
  - COS는 서비스 인스턴스 및 버킷 레벨 액세스 제어에 IAM 정책을 사용합니다. `public-read` ACL을 설정하여 [오브젝트를 공용으로 사용 가능하도록 설정](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access)할 수 있으며, 이렇게 하면 권한 부여 헤더가 필요하지 않습니다. 
  - 대형 오브젝트의 [다중 파트 업로드](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects)는 Swift API에 대해 COS/S3 API에서 서로 다르게 처리됩니다. 
  - COS는 `Cache-Control`, `Content-Encoding`, `Content-MD5` 및 `Content-Type`과 같은 친숙한 선택적 HTTP 헤더를 사용할 수 있도록 허용합니다. 

이 안내서는 하나의 Swift 컨테이너에서 하나의 COS 버킷으로 데이터를 마이그레이션하는 데 대한 지시사항을 제공합니다. 이는 마이그레이션할 모든 컨테이너에 대해 반복해야 하며, 그 후에는 새 API를 사용하도록 애플리케이션 로직을 업데이트해야 합니다. 데이터가 마이그레이션되고 나면 `rclone check`를 사용하여 전송의 무결성을 확인할 수 있으며, 이는 MD5 체크섬을 비교하고 일치하지 않는 오브젝트의 목록을 생성합니다. 


## {{site.data.keyword.cos_full_notm}} 설정
{: #migrate-setup}

  1. 아직 작성하지 않은 경우에는 [카탈로그](https://cloud.ibm.com/catalog/services/cloud-object-storage)에서 {{site.data.keyword.cos_full_notm}}의 인스턴스를 프로비저닝하십시오. 
  2. 전송된 데이터를 저장하는 데 필요한 버킷을 작성하십시오. [시작하기 안내서](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)를 읽고 [엔드포인트](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) 및 [스토리지 클래스](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes)와 같은 주요 개념을 숙지하십시오. 
  3. Swift API의 구문은 COS/S3 API와 크게 다르므로, COS SDK에서 제공하는 동등한 메소드를 사용하려면 애플리케이션을 리팩터해야 합니다. 사용 가능한 라이브러리에는 [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) 또는 [REST API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api)용이 있습니다. 

## 마이그레이션 도구를 실행할 컴퓨팅 리소스 설정
{: #migrate-compute}
  1. Linux/macOS/BSD 시스템, 또는 자신의 데이터에 가장 가까운 IBM Cloud Infrastructure Bare Metal 또는
     Virtual Server를 선택하십시오.
     권장 서버 구성은 32GB RAM, 2 - 4개 코어 프로세서, 1000Mbps의 사설 네트워크 속도입니다.   
  2. IBM Cloud Infrastructure Bare Metal 또는 Virtual Server에서 마이그레이션을 실행하는 경우에는
     **개인용** Swift 및 COS 엔드포인트를 사용하십시오. 
  3. 그 외의 경우에는 **공용** Swift 및 COS 엔드포인트를 사용하십시오. 
  4. [패키지 관리자 또는 사전 컴파일된 바이너리](https://rclone.org/install/)로부터 `rclone`을 설치하십시오. 

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## OpenStack Swift를 위한 `rclone` 구성
{: #migrate-rclone}

  1. `~/.rclone.conf`에 `rclone` 구성 파일을 작성하십시오. 

        ```
        touch ~/.rclone.conf
        ```

  2. 다음 내용을 복사해 `rclone.conf`에 붙여넣어 Swift 소스를 작성하십시오. 

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. OpenStack Swift 인증 정보를 가져오십시오.
    <br>a. [IBM Cloud 콘솔 대시보드](https://cloud.ibm.com/)에서 Swift 인스턴스를 클릭하십시오.
    <br>b. 탐색 패널에서 **서비스 인증 정보**를 클릭하십시오.
    <br>c. **새 인증 정보**를 클릭하여 인증 정보를 생성하십시오. **추가**를 클릭하십시오.
    <br>d. 작성한 인증 정보를 보고 JSON 컨텐츠를 복사하십시오. 

  4. 다음 필드를 채우십시오. 

        ```
        user_id = <userId>
        key = <password>
        region = dallas OR london            depending on container location
        endpoint_type = public OR internal   internal is the private endpoint
        ```

  5. COS를 위한 `rclone` 구성 섹션으로 건너뛰십시오. 


## OpenStack Swift(인프라)를 위한 `rclone` 구성
{: #migrate-config-swift}

  1. `~/.rclone.conf`에 `rclone` 구성 파일을 작성하십시오. 

        ```
        touch ~/.rclone.conf
        ```

  2. 다음 내용을 복사해 `rclone.conf`에 붙여넣어 Swift 소스를 작성하십시오. 

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. OpenStack Swift(인프라) 인증 정보를 가져오십시오.
    <br>a. IBM Cloud 인프라 고객 포털에서 Swift 계정을 클릭하십시오.
    <br>b. 마이그레이션 소스 컨테이너의 데이터 센터를 클릭하십시오.
    <br>c. **인증 정보 보기**를 클릭하십시오.
    <br>d. 다음 항목을 복사하십시오.
      <br>&nbsp;&nbsp;&nbsp;**사용자 이름**
      <br>&nbsp;&nbsp;&nbsp;**API 키**
      <br>&nbsp;&nbsp;&nbsp;**인증 엔드포인트**(마이그레이션 도구를 실행하고 있는 위치에 따라)

  4. OpenStack Swift(인프라) 인증 정보를 사용하여 다음 필드를 채우십시오. 

        ```
        user = <Username>
        key = <API Key (Password)>
        auth = <public or private endpoint address>
        ```

## COS를 위한 `rclone` 구성
{: #migrate-config-cos}

### COS 인증 정보 가져오기
{: #migrate-config-cos-credential}

  1. IBM Cloud 콘솔에서 COS 인스턴스를 클릭하십시오. 
  2. 탐색 패널에서 **서비스 인증 정보**를 클릭하십시오. 
  3. **새 인증 정보**를 클릭하여 인증 정보를 생성하십시오. 
  4. **인라인 구성 매개변수**에서 `{"HMAC":true}`를 추가하십시오. **추가**를 클릭하십시오. 
  5. 작성한 인증 정보를 보고 JSON 컨텐츠를 복사하십시오. 

### COS 엔드포인트 가져오기
{: #migrate-config-cos-endpoint}

  1. 탐색 패널에서 **버킷**을 클릭하십시오. 
  2. 마이그레이션 대상 버킷을 클릭하십시오. 
  3. 탐색 패널에서 **구성**을 클릭하십시오. 
  4. **엔드포인트** 섹션으로 스크롤하여 마이그레이션 도구를 실행하고 있는 위치에 따라
     엔드포인트를 선택하십시오. 

  5. 다음 내용을 복사해 `rclone.conf`에 붙여넣어 COS 대상을 작성하십시오. 

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. COS 인증 정보 및 엔드포인트를 사용하여 다음 필드를 채우십시오. 

    ```
    access_key_id = <access_key_id>
    secret_access_key = <secret_access_key>
    endpoint = <bucket endpoint>       
    ```

## 마이그레이션 소스 및 대상이 올바르게 구성되었는지 확인
{: #migrate-verify}

1. Swift 컨테이너를 나열하여 `rclone`이 적절히 구성되었는지 확인하십시오. 

    ```
    rclone lsd SWIFT:
    ```

2. COS 버킷을 나열하여 `rclone`이 적절히 구성되었는지 확인하십시오. 

    ```
    rclone lsd COS:
    ```

## `rclone` 실행
{: #migrate-run}

1. `rclone`의 드라이 런(데이터가 복사되지 않음)을 수행하여 소스 Swift
   컨테이너(예: `swift-test`)에서 대상 COS 버킷(예: `cos-test`)으로 오브젝트를 동기화하십시오. 

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. 마이그레이션하려는 데이터가 명령 출력에 표시되는지 확인하십시오. 문제가 없는 경우에는 `--dry-run` 플래그를 제거하고 `-v` 플래그를 추가하여 데이터를 복사하십시오. 선택적 `--checksum` 플래그를 사용하면 두 위치에서 MD5 해시와 오브젝트 크기가 동일한 파일이 업데이트되지 않습니다. 

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   가장 빠른 전송 시간을 얻으려면 rclone을 실행하는 시스템의 CPU, 메모리 및 네트워크를 최대로 사용해야 합니다.
   rclone 조정에 대해 고려해야 하는 다른 몇 가지 매개변수는 다음과 같습니다. 

   --checkers 정수 병렬로 실행할 검사기의 수입니다. (기본값 8)
   이는 실행되는 체크섬 비교 스레드의 수입니다. 이는 64 이상으로 늘리는 것이 좋습니다. 

   --transfers 정수 병렬로 실행할 파일 전송의 수입니다. (기본값 4)
   이는 병렬로 전송할 오브젝트의 수입니다. 이는 64 또는 128 이상으로 늘리는 것이 좋습니다. 

   --fast-list 사용 가능한 경우에는 재귀 목록을 사용하십시오. 더 많은 메모리를 사용하지만 트랜잭션 수가 적어집니다.
   성능을 향상시키려면 이 옵션을 사용하십시오. 오브젝트를 복사하는 데 필요한 요청의 수가 줄어듭니다. 

`rclone`을 사용한 데이터 마이그레이션은 소스 데이터를 복사하지만 삭제하지는 않습니다.
{:tip}


3. 마이그레이션이 필요한 다른 컨테이너에 대해 작업을 반복하십시오. 
4. 모든 데이터가 복사되고 애플리케이션이 COS의 데이터에 액세스할 수 있음을 확인한 후에는 Swift 서비스 인스턴스를 삭제하십시오. 
