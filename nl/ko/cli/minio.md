---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cli, open source, minio

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

# Minio Client 사용
{: #minio}

{{site.data.keyword.cos_full}}에서 익숙한 UNIX 유사 명령(`ls`, `cp`, `cat` 등)을 사용하시겠습니까? 그렇다면 오픈 소스 [Minio Client](https://min.io/download#/linux){:new_window}가 그 해답입니다. Minio 웹 사이트의 [빠른 시작 안내서](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window}에서 사용 가능한 각 운영 체제에 대한 설치 지시사항을 찾을 수 있습니다.

## 구성
{: #minio-config}

다음 명령을 실행하여 {{site.data.keyword.cos_short}}가 추가됩니다.

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - 명령에서 {{site.data.keyword.cos_short}}를 참조하기 위한 단축 이름
* `<COS-ENDPOINT` - {{site.data.keyword.cos_short}} 인스턴스의 엔드포인트. 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오.
* `<ACCESS-KEY>` - 서비스 인증 정보에 지정된 액세스 키
* `<SECRET-KEY>` - 서비스 인증 정보에 지정된 비밀 키

구성 정보는 `~/.mc/config.json`에 있는 JSON 파일에 저장됩니다.

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## 샘플 명령
{: #minio-commands}

명령 및 선택적 플래그와 매개변수의 전체 목록이 [Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window}에 있습니다.

### `mb` - 버킷 작성
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - 버킷 나열
{: #minio-ls}

사용 가능한 모든 버킷이 나열되지만 지정된 [엔드포인트](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) 지역에 따라 일부 오브젝트에 액세스할 수 없습니다.
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - 오브젝트 나열
{: #minio-ls-objects}

```
mc ls cos/testbucket1
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
[2018-08-10 09:49:08 HST]  20MiB newbigfile.pdf
[2018-11-29 09:53:15 HST]    31B testsave.txt
```

### `find` - 이름별 오브젝트 검색
{: #minio-find}

전체 검색 옵션 목록은 [complete guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}에서 사용 가능합니다.
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - 몇 개의 오브젝트 행 표시
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - 오브젝트 복사
{: #minio-cp}

이 명령은 두 위치 간에 오브젝트를 복사합니다. 이러한 위치는 서로 다른 호스트(예: 여러 [엔드포인트](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) 또는 스토리지 서비스) 또는 로컬 파일 시스템 위치(예: `~/foo/filename.pdf`)입니다.
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - 오브젝트 제거
{: #minio-rm}

*추가 제거 옵션은 [complete guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}에서 사용 가능합니다*.

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - 오브젝트에 STDIN 복사
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
