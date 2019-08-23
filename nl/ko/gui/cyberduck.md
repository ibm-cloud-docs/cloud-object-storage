---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# Cyberduck을 사용하여 파일 전송
{: #cyberduck}

Cyberduck은 사용하기 쉬운 인기 있는 오픈 소스 Cloud Object Storage 브라우저입니다(Mac 및 Windows용). Cyberduck은 IBM COS에 연결하는 데 필요한 올바른 권한 서명을 추정할 수 있습니다. Cyberduck은 [cyberduck.io/](https://cyberduck.io/){: new_window}에서 다운로드할 수 있습니다.

Cyberduck을 사용하여 IBM COS에 대한 연결을 작성하고 로컬 파일의 폴더를 버킷에 동기화하려면 다음 단계를 수행하십시오.

 1. Cyberduck를 다운로드하고 설치하고 시작하십시오.
 2. 애플리케이션의 기본 창이 열리고 이 창에서 IBM COS에 대한 연결을 작성할 수 있습니다. **연결 열기**를 클릭하여 IBM COS에 대한 연결을 구성하십시오.
 3. 팝업 창이 열립니다. 맨 위 드롭 다운 메뉴에서 `S3(HTTPS)`를 선택하십시오. 다음 필드에 정보를 입력한 후 연결을 클릭하십시오.

    * `서버`: IBM COS의 엔드포인트 입력
        * *엔드포인트의 지역이 의도한 버킷과 일치하는지 확인하십시오. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.*
    * `액세스 키 ID`
    * `시크릿 액세스 키`
    * `키 체인에 추가`: 키 체인에 대한 연결을 저장하여 다른 애플리케이션에서 사용을 허용*(선택사항)*

 4. Cyberduck을 통해 버킷을 작성할 수 있는 계정의 루트로 이동합니다.
    * 기본 분할창 내에서 마우스 오른쪽 단추를 클릭하고 **새 폴더**를 선택하십시오(*폴더가 일반적인 컨테이너 구성인 경우 애플리케이션이 많은 전송 프로토콜 처리*).
    * 버킷 이름을 입력한 다음 작성을 클릭하십시오.
 5. 버킷을 작성한 후 버킷을 두 번 클릭하여 보십시오. 버킷 내에서 다음과 같은 다양한 기능을 수행할 수 있습니다.
    * 버킷에 파일 업로드
    * 버켓 컨텐츠 나열
    * 버킷에서 오브젝트 다운로드
    * 버킷에 로컬 파일 동기화
    * 오브젝트를 다른 버킷에 동기화
    * 버킷의 아카이브 작성
 6. 버킷 내에서 마우스 오른쪽 단추를 클릭하고 **동기화**를 선택하십시오. 팝업 창이 열리고 이 창에서 버킷에 동기화할 폴더를 찾아볼 수 있습니다. 폴더를 선택하고 선택을 클릭하십시오.
 7. 폴더를 선택하면 새 팝업 창이 열립니다. 여기서 버킷에 대한 동기화 오퍼레이션을 선택하는 드롭 다운 메뉴를 사용할 수 있습니다. 메뉴에서 세 가지 가능한 동기화 옵션을 사용할 수 있습니다.

    * `다운로드`: 버킷에서 변경된 오브젝트와 누락된 오브젝트를 다운로드합니다.
    * `업로드`: 버킷에 변경된 파일과 누락된 파일을 업로드합니다.
    * `미러`: 다운로드 및 업로드 오퍼레이션을 수행하며, 모든 새 파일과 오브젝트 및 업데이트된 파일과 오브젝트가 로컬 폴더와 버킷 간에 동기화되도록 합니다.

 8. 다른 창이 열리며 활성 및 히스토리 전송 요청이 표시됩니다. 동기화 요청이 완료되면 기본 창이 버킷에 업데이트된 컨텐츠를 반영하도록 버킷에서 나열 오퍼레이션을 수행합니다.

## Mountain Duck
{: #mountain-duck}

Mountain Duck은 Cyberduck을 기반으로 빌드되어 사용자가 Mac의 Finder 또는 Windows의 Explorer에서 Cloud Object Storage를 디스크로서 마운트할 수 있습니다. 평가판을 사용할 수 있지만 계속 사용하려면 등록 키가 필요합니다.

Mountain Duck에서 책갈피 작성은 Cyberduck에서 연결 작성과 유사합니다.

1. Mountain Duck을 다운로드하고 설치하고 시작하십시오.
2. 책갈피를 새로 작성하십시오.
3. 드롭 다운 메뉴에서 `S3(HTTPS)`를 선택하고 다음 정보를 입력하십시오.
    * `서버`: IBM COS의 엔드포인트 입력 
        * *엔드포인트 지역이 의도한 버킷과 일치하는지 확인하십시오. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.*
    * `사용자 이름`: 액세스 키 입력
    * **연결**을 클릭하십시오.
    * 키 체인에 저장되는 비밀 키에 대한 프롬프트가 표시됩니다.

이제 Finder 또는 Explorer에서 버킷을 사용할 수 있습니다. 마운트된 다른 파일 시스템과 같이 {{site.data.keyword.cos_short}}와 상호작용할 수 있습니다.

## CLI
{: #cyberduck-cli}

Cyberduck은 Linux, Mac OS X 및 Windows의 쉘에서 실행되는 명령행 인터페이스(CLI) `duck`을 제공합니다. 설치 지시사항은 `duck` [위키 페이지](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window}에서 사용 가능합니다.

{{site.data.keyword.cos_full}}에서 `duck`을 사용하려면 사용자 정의 프로파일을 [애플리케이션 지원 디렉토리](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}에 추가해야 합니다. 샘플 및 사전 구성된 프로파일을 포함한 `duck` 연결 프로파일에 대한 자세한 정보는 [CLI 도움말/사용 방법](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}에서 볼 수 있습니다.

다음은 지역 COS 엔드포인트에 대한 예제 프로파일입니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.

이 프로파일을 `duck`에 추가하면 다음과 유사한 명령을 사용하여 {{site.data.keyword.cos_short}}에 액세스할 수 있습니다.

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*키 값*
* `<bucket-name>` - COS 버킷의 이름(*버킷 및 엔드포인트 지역이 일치하는지 확인*)
* `<access-key>` - HMAC 액세스 키
* `<secret-access-key>` - HMAC 비밀 키

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

명령행 옵션의 전체 목록은 쉘에 `duck --help`를 입력하거나 [위키 사이트](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}를 방문하여 볼 수 있습니다.
