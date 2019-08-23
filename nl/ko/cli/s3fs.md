---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# `s3fs`를 사용하여 버킷 마운트
{: #s3fs}

NFS 스타일 파일 시스템에 읽고 쓸 애플리케이션은 `s3fs`를 사용하며, 이는 파일에 대한 기본 오브젝트 형식을 유지하면서 버킷을 디렉토리로서 마운트할 수 있습니다. 이렇게 되면 로컬 파일에서 읽기 및 쓰기에 의존하는 레거시 애플리케이션에 대한 액세스를 제공할 뿐만 아니라 익숙한 쉘 명령(예: 나열하는 경우 `ls` 또는 파일을 복사하는 경우 `cp`)을 사용하여 클라우드 스토리지와 상호작용할 수 있습니다. 자세한 개요는 [프로젝트의 공식 README를 방문](https://github.com/s3fs-fuse/s3fs-fuse)하십시오.

## 전제조건
{: #s3fs-prereqs}

* IBM Cloud 계정 및 {{site.data.keyword.cos_full}}의 인스턴스
* Linux 또는 OSX 환경
* 인증 정보([IAM API 키](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) 또는 [HMAC 인증 정보](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## 설치
{: #s3fs-install}

OSX에서 [Homebrew](https://brew.sh/)를 사용하십시오.

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

Debian 또는 Ubuntu의 경우: 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

공식 `s3fs` 문서에서는 `libcurl4-openssl-dev` 대신 `libcurl4-gnutls-dev` 사용을 제안합니다. 어느 쪽이든 작동되지만 OpenSSL 버전에서 성능이 향상될 수 있습니다.
{:tip}

또한 소스에서 `s3fs`를 빌드할 수 있습니다. 먼저 Github 저장소를 복제하십시오.

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

그런 다음 `s3fs`를 빌드하십시오.

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

바이너리를 설치하십시오.

```sh
sudo make install
```
{:codeblock}

## 구성
{: #s3fs-config}

`<access_key>:<secret_key>` 또는 `:<api_key>`가 포함된 파일에 인증 정보를 저장하십시오. 이 파일은 제한된 액세스를 가져야 하므로 다음을 실행하십시오.

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

이제 다음을 사용하여 버킷을 마운트할 수 있습니다.

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

인증 정보 파일에 API 키만 있는 경우(HMAC 인증 정보가 없음) `ibm_iam_auth` 플래그도 추가해야 합니다.

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

`<bucket>`은 기존 버킷이고 `<mountpoint>`는 버킷을 마운트할 로컬 디렉토리입니다. `<endpoint>`는 [버킷의 위치](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)와 일치해야 합니다. `credentials_file`은 API 키 또는 HMAC 인증 정보를 사용하여 작성된 파일입니다.

이제 `ls <mountpoint>`는 로컬 파일인 것처럼(또는 오브젝트 접두부의 경우 중첩된 디렉토리인 것처럼) 해당 버킷의 오브젝트를 나열합니다.

## 성능 최적화
{: #s3fs-performance}

성능이 실제 로컬 파일 시스템과 같지 않지만 몇 가지 고급 옵션을 사용하여 처리량을 늘릴 수 있습니다. 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM`은 HTTPS 엔드포인트를 사용할 때만 관련됩니다. 기본적으로 IBM COS에 대한 보안 연결은 `AES256-SHA` 암호 스위트를 사용합니다. 대신 `AESGCM` 스위트를 사용하면 동일한 레벨의 암호화 보안이 제공되면서 TLS 암호화 기능으로 인해 발생한 클라이언트 시스템의 CPU 오버헤드가 크게 줄어듭니다.
2. `kernel_cache`는 `s3fs` 마운트 포인트에서 커널 버퍼 캐시를 사용합니다. 즉, 동일한 파일에 대한 각 읽기가 커널의 버퍼 캐시에서 제공될 수 있으므로 `s3fs`에서 오브젝트를 한 번만 읽습니다. 커널 버퍼 캐시는 다른 프로세스에서 사용하지 않는 사용 가능한 메모리만 사용합니다. 이 옵션은 버킷이 마운트될 때 다른 프로세스/시스템에서 버킷 오브젝트를 겹쳐쓰게 하려고 하고 유스 케이스에 최신 컨텐츠에 대한 라이브 액세스가 필요한 경우에 권장되지 않습니다. 
3. `max_background=1000`은 `s3fs` 동시 파일 읽기 성능을 개선합니다. 기본적으로 FUSE는 최대 128KB의 파일 읽기 요청을 지원합니다. 더 많이 읽어야 하는 경우 커널이 대형 요청을 작은 하위 요청으로 분할하여 s3fs 프로세스가 비동기식으로 이를 처리할 수 있도록 합니다. `max_background` 옵션은 이와 같은 동시 비동기 요청의 글로벌 최대 수를 설정합니다. 기본적으로 12로 설정되지만, 임의의 높은 값(1000)으로 설정하면 많은 수의 파일을 동시에 읽는 경우에도 읽기 요청이 차단되지 않습니다.
4. `max_stat_cache_size=100000`은 `s3fs`에서 전송된 중복 HTTP `HEAD` 요청의 수를 줄이고 디렉토리를 나열하거나 파일 속성을 검색하는 데 걸리는 시간을 줄입니다. 일반적인 파일 시스템 사용에서는 오브젝트 스토리지 시스템의 `HEAD` 요청에 맵핑된 `stat()` 호출을 통해 파일의 메타데이터에 빈번하게 액세스할 수 있습니다. 기본적으로 `s3fs`는 최대 1000개의 오브젝트의 속성(메타데이터)을 캐시합니다. 각 캐시된 항목은 최대 0.5KB의 메모리를 사용합니다. 이상적으로는 캐시가 버킷에 있는 모든 오브젝트에 대한 메타데이터를 보유할 수 있는 것이 좋습니다. 하지만 이 캐싱의 메모리 사용 결과를 고려해야 합니다. `100000`으로 설정하면 0.5KB * 100000 = 50MB가 사용됩니다.
5. `multipart_size=52`는 COS 서버(MB 단위)에서 송수신되는 요청과 응답의 최대 크기를 설정합니다. `s3fs`는 기본적으로 이를 10MB로 설정합니다. 이 값을 늘리면 HTTP 연결당 처리량(MB/s)도 늘어납니다. 반면에 파일에서 제공되는 첫 번째 바이트에 대한 대기 시간이 각각 늘어납니다. 따라서 유스 케이스가 각 파일에서 적은 양의 데이터만 읽는 경우 이 값을 늘리지 않을 수 있습니다. 또한 대형 오브젝트의 경우(예를 들어, 50MB를 넘는) 이 값이 다중 요청을 사용하여 동시에 파일을 페치할 수 있을 만큼 작은 경우 처리량이 늘어납니다. 이 옵션의 최적 값은 약 50MB입니다. COS 우수 사례는 4MB의 배수인 요청 사용을 제안하므로 이 옵션을 52MB로 설정하는 것이 좋습니다.
6. `parallel_count=30`은 동시에 COS로 전송된 단일 파일 읽기/쓰기 오퍼레이션당 최대 요청 수를 설정합니다. 기본적으로 이는 5로 설정됩니다. 매우 큰 오브젝트의 경우 이 값을 늘려 처리량을 늘릴 수 있습니다. 이전 옵션과 마찬가지로 각 파일에서 적은 양의 데이터만 읽는 경우 이 값을 작게 유지하십시오.
7. `multireq_max=30` 디렉토리를 나열할 때 오브젝트 메타데이터 요청(`HEAD`)은 목록의 각 오브젝트에 대해 전송됩니다(메타데이터가 캐시에 있는 경우 제외). 이 옵션은 단일 디렉토리 나열 오퍼레이션에 대해 COS로 전송된 이와 같은 동시 요청 수를 제한합니다. 기본적으로 이는 20으로 설정됩니다. 이 값은 위의 `parallel_count` 옵션 이상이어야 합니다.
8. `dbglevel=warn`은 /var/log/syslog에 메시지를 로그하기 위해 디버그 레벨을 기본값(`crit`) 대신 `warn`으로 설정합니다.

## 제한사항
{: #s3fs-limitations}

오브젝트 스토리지 서비스에서 TTFB(Time to The First Byte)에 대한 대기 시간이 길고 랜덤 쓰기 액세스가 부족하므로 s3fs가 모든 애플리케이션에는 적합하지 않을 수 있습니다. 딥러닝 워크로드와 같이 대형 파일만 읽는 워크로드는 `s3fs`를 사용하여 양호한 처리량을 유지할 수 있습니다. 
