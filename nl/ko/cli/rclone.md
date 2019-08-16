---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# `rclone` 사용
{: #rclone}

## `rclone` 설치
{: #rclone-install}

`rclone` 도구는 디렉토리를 동기화하고 스토리지 플랫폼 간에 데이터를 마이그레이션하는 데 유용합니다. 이는 Go 프로그램이며 단일 바이너리 파일입니다.

### 빠른 시작 설치
{: #rclone-quick}

*  관련 바이너리를 [다운로드](https://rclone.org/downloads/)하십시오. 
*  아카이브에서 `rclone` 또는 `rclone.exe` 바이너리를 추출하십시오.
*  설정을 위해 `rclone config`를 실행하십시오.

### 스크립트를 사용하여 설치
{: #rclone-script}

Linux/macOS/BSD 시스템에 `rclone`을 설치하십시오.

```
curl https://rclone.org/install.sh | sudo bash
```

베타 버전도 사용 가능합니다.

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

설치 스크립트는 처음에 설치된 `rclone` 버전을 확인하고, 현재 버전이 이미 최신 버전인 경우 다운로드를 건너뜁니다.
{:note}

### 사전 컴파일된 바이너리에서 Linux 설치
{: #rclone-linux-binary}

먼저 바이너리를 페치하고 압축을 푸십시오.

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

그런 다음 바이너리 파일을 적절한 위치에 복사하십시오.

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

문서를 설치하십시오.

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

설정을 위해 `rclone config`를 실행하십시오.

```
rclone config
```

### 사전 컴파일된 바이너리에서 macOS 설치
{: #rclone-osx-binary}

먼저 `rclone` 패키지를 다운로드하십시오.

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

그런 다음, 다운로드된 파일 및 `cd`를 추출된 폴더에 추출하십시오.

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

`rclone`을 `$PATH`로 이동하고 프롬프트가 표시되면 비밀번호를 입력하십시오.

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

디렉토리가 있는 경우에도 `mkdir` 명령은 안전하게 실행할 수 있습니다.
{:tip}

남은 파일을 제거하십시오.

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

설정을 위해 `rclone config`를 실행하십시오.

```
rclone config
```

## IBM COS에 대한 액세스 구성
{: #rclone-config}

1. `rclone config`를 실행하고 새 원격에 대해 `n`을 선택하십시오.

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. 구성의 이름을 입력하십시오.
```
	name> <YOUR NAME>
```

3. “s3” 스토리지를 선택하십시오.

```
	Choose a number from below, or type in your own value
		1 / Alias for a existing remote
		\ "alias"
		2 / Amazon Drive
		\ "amazon cloud drive"
		3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
		\ "s3"
		4 / Backblaze B2
		\ "b2"
	[snip]
		23 / http Connection
	  \ "http"
	Storage> 3
```

  4. IBM COS를 S3 스토리지 제공자로 선택하십시오.

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  1. **False**를 입력하여 인증 정보를 입력하십시오.

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. 액세스 키 및 시크릿을 입력하십시오.

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. IBM COS의 엔드포인트를 지정하십시오. 공용 IBM COS의 경우 제공된 옵션에서 선택하십시오. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.

```
Endpoint for IBM COS S3 API.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3.us.cloud-object-storage.appdomain.cloud"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. IBM COS 위치 제한조건을 지정하십시오. 위치 제한조건이 엔드포인트와 일치해야 합니다. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. ACL을 지정하십시오. `public-read` 및 `private`만 지원됩니다. 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. 표시된 구성을 검토하고 “remote” 저장을 승인한 다음 종료하십시오. 구성 파일은 다음과 같아야 합니다.

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## 명령 참조
{: #rclone-reference}

### 버킷 작성
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### 사용 가능한 버킷 나열
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### 버킷의 컨텐츠 나열
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### 로컬에서 원격으로 파일 복사
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### 원격에서 로컬로 파일 복사
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### 원격의 파일 삭제
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### 명령 나열
{: #rclone-reference-listing}

몇 가지 관련 나열 명령이 있습니다.
* `ls` 오브젝트의 크기 및 경로만 나열
* `lsl` 오브젝트의 수정 시간, 크기 및 경로만 나열
* `lsd` 디렉토리만 나열
* `lsf` 형식을 쉽게 구문 분석하기 위해 오브젝트 및 디렉토리 나열
* `lsjson` 오브젝트 및 디렉토리를 JSON 형식으로 나열

## `rclone sync`
{: #rclone-sync}

`sync` 오퍼레이션은 소스와 대상을 동일하게 하며 대상만 수정합니다. 동기화는 크기 및 수정 시간 또는 MD5SUM으로 테스트하여 변경되지 않은 파일을 전송하지 않습니다. 대상은 필요한 경우 파일 삭제를 포함하여 소스와 일치하도록 업데이트됩니다.

이로 인해 데이터 손실이 발생할 수 있으므로 먼저 `--dry-run` 플래그로 테스트하여 복사하고 삭제할 항목을 정확히 확인하십시오.
{:important}

어떤 시점에서든 오류가 있는 경우 대상의 해당 파일이 삭제되지 않습니다.

디렉토리 자체가 아닌 디렉토리의 _컨텐츠_가 동기화됩니다. `source:path`가 디렉토리인 경우 디렉토리 이름과 컨텐츠가 아닌 복사된 `source:path`의 컨텐츠입니다. 자세한 정보는 `copy` 명령의 자세한 설명을 참조하십시오.

`dest:path`가 없는 경우 작성되고 `source:path` 컨텐츠가 여기로 이동됩니다.

```sh
rclone sync source:path dest:path [flags]
```

### 동시에 여러 위치에서 `rclone` 사용
{: #rclone-sync-multiple}

출력에 대해 여러 서브디렉토리를 선택한 경우 동시에 여러 위치에서 `rclone`을 사용할 수 있습니다.

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

`rclone copy`를 사용하는 동일한 디렉토리에 `sync`를 수행하는 경우 두 프로세스가 상대의 다른 파일을 삭제할 수 있습니다.

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

`sync`, `copy` 또는 `move` 사용 시 겹쳐쓰거나 삭제한 파일이 원래 계층의 이 디렉토리로 이동됩니다.

`--suffix`를 설정하면 이동된 파일에 접미부가 추가됩니다. 디렉토리에 동일한 경로(접미부가 추가된 후)의 파일이 있는 경우 이 파일을 겹쳐씁니다.

사용 중인 원격은 서버 측 이동 또는 복사를 지원해야 하며 사용자는 동기화 대상과 동일한 원격을 사용해야 합니다. 백업 디렉토리는 대상 디렉토리와 겹치지 않아야 합니다.

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

`/path/to/local`을 `remote:current`에 `sync`하지만 업데이트되거나 삭제된 파일의 경우 `remote:old`에 저장됩니다.

스크립트에서 `rclone`을 실행하는 경우 오늘 날짜를 `--backup-dir`에 전달된 디렉토리 이름으로 사용하여 이전 파일을 저장하거나 오늘 날짜와 함께 `--suffix`를 전달할 수 있습니다.

## `rclone` 일일 동기화
{: #rclone-sync-daily}

백업 스케줄링은 백업 자동화에서 중요합니다. 플랫폼에 따라 수행 방법이 다릅니다. Windows에서는 Task Scheduler를 사용할 수 있는 반면 MacOS 및 Linux에서는 crontabs를 사용할 수 있습니다.

### 디렉토리 동기화
{: #rclone-sync-directory}

`Rclone`은 원격 컨테이너와 로컬 디렉토리를 동기화하며, 컨테이너의 로컬 디렉토리에 모든 파일을 저장합니다. `Rclone`은 구문 `rclone sync source destination`을 사용하며, 여기서 `source`는 로컬 폴더이고 `destination`은 IBM COS 내 컨테이너입니다.

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

작성된 대상이 이미 있을 수 있지만, 없는 경우에는 위 단계를 사용하여 버킷을 새로 작성할 수 있습니다.

### 작업 스케줄
{: #rclone-sync-schedule}

작업을 스케줄하기 전에 초기 업로드를 완료했는지 확인하십시오.

#### Windows
{: #rclone-sync-windows}

1. 컴퓨터에 `backup.bat`이라는 텍스트 파일을 작성하고 [디렉토리 동기화](#rclone-sync-directory)에 대한 섹션에서 사용한 명령을 붙여넣으십시오. 전체 경로를 rclone.exe에 지정하고 파일을 반드시 저장하십시오.

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. `schtasks`를 사용하여 작업을 스케줄하십시오. 이 유틸리티는 많은 매개변수를 사용합니다.
	* /RU – 작업을 실행할 사용자. 사용할 사용자가 로그아웃한 경우에 필요합니다.
	* /RP - 사용자의 비밀번호.
	* /SC – DAILY로 설정
	* /TN - 작업의 이름. 백업이라고 함
	* /TR – 작성한 backup.bat 파일에 대한 경로.
	* /ST – 태스크를 시작할 시간. 이는 24시간 형식입니다. 01:05:00은 1:05 AM입니다. 13:05:00은 1:05 PM입니다.

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac 및 Linux
{: #rclone-sync-nix}

1. 컴퓨터에 `backup.sh`라는 텍스트 파일을 작성하고 [디렉토리 동기화](#rclone-sync-directory) 섹션에서 사용한 명령을 붙여넣으십시오. 다음과 같이 표시됩니다. 전체 경로를 rclone 실행 파일에 지정하고 파일을 반드시 저장하십시오.

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. `chmod`를 사용하여 스크립트를 실행 가능하게 설정하십시오.

```sh
chmod +x backup.sh
```

3. crontabs를 편집하십시오.

```sh
sudo crontab -e
```

4. crontabs 파일의 맨 아래에 항목을 추가하십시오. Crontabs는 간단합니다. 처음 5개의 필드는 순서대로 분, 시간, 일, 월 및 주를 나타냅니다. * 사용은 모두를 의미합니다. `backup.sh`가 매일 오전 1시 5분에 실행되도록 하려면 다음과 같이 사용하십시오.

```sh
5 1 * * * /full/path/to/backup.sh
```

5. crontabs를 저장하면 준비가 됩니다.
