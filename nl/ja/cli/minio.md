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

# Minio Client の使用
{: #minio}

精通している UNIX のようなコマンド (`ls`、`cp`、`cat` など) を {{site.data.keyword.cos_full}} で使用しますか? その場合、オープン・ソースの [Minio Client](https://min.io/download#/linux){:new_window} を使用することができます。各オペレーティング・システムでのインストール手順は、Minio Web サイトの [Quickstart Guide](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} にあります。

## 構成
{: #minio-config}

{{site.data.keyword.cos_short}} を追加するには、以下のコマンドを実行します。

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - コマンドで {{site.data.keyword.cos_short}} を参照するためのショート・ネーム
* `<COS-ENDPOINT` - {{site.data.keyword.cos_short}} インスタンスのエンドポイント。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。
* `<ACCESS-KEY>` - サービス資格情報に割り当てられたアクセス・キー
* `<SECRET-KEY>` - サービス資格情報に割り当てられた秘密鍵

構成情報は、`~/.mc/config.json` にある JSON ファイルに保管されます。

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## サンプル・コマンド
{: #minio-commands}

コマンドおよびオプションのフラグとパラメーターの完全なリストは、「[MinIO Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window}」に記載されています。

### `mb` - バケットの作成
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - バケットのリスト
{: #minio-ls}

すべての使用可能なバケットはリストされていますが、指定されている[エンドポイントの](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)地域によっては一部のオブジェクトにアクセスできません。
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - オブジェクトのリスト
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

### `find` - 名前によるオブジェクトの検索
{: #minio-find}

検索オプションの完全なリストは、[Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window} にあります。
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - オブジェクトの数行の表示
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - オブジェクトのコピー
{: #minio-cp}

このコマンドは、2 つの場所の間でオブジェクトをコピーします。これらの場所は、異なるホスト (異なる[エンドポイント](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)やストレージ・サービスなど) にすることも、ローカル・ファイル・システムの場所 (`~/foo/filename.pdf` など) にすることもできます。
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - オブジェクトの削除
{: #minio-rm}

*その他の削除オプションは、[Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window} にあります。*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - オブジェクトへの STDIN のコピー
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
