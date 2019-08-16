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

# 使用 Minio Client
{: #minio}

您要使用熟悉的 UNIX 型指令（`ls`、`cp`、`cat` 等）搭配 {{site.data.keyword.cos_full}} 嗎？如果是，開放程式碼 [Minio Client](https://min.io/download#/linux){:new_window} 就是答案。您可以在 Minio 網站的 [quickstart guide](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} 中找到每個作業系統的安裝指示。

## 配置
{: #minio-config}

執行下列指令，即可新增 {{site.data.keyword.cos_short}}：

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - 在指令中參照 {{site.data.keyword.cos_short}} 用的簡稱
* `<COS-ENDPOINT` - {{site.data.keyword.cos_short}} 實例的端點。 如需端點的相關資訊，請參閱[端點及儲存空間位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。
* `<ACCESS-KEY>` - 指派給服務認證的存取金鑰
* `<SECRET-KEY>` - 指派給服務認證的秘密金鑰

配置資訊儲存在位於 `~/.mc/config.json` 的 JSON 檔案中。

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## 範例指令
{: #minio-commands}

[Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window} 中記載了指令及選用性旗標和參數的完整清單。

### `mb` - 建立儲存區
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - 列出儲存區
{: #minio-ls}

雖然會列出所有可用的儲存區，但不一定可以存取所有物件，視指定的[端點](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)地區而定。
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - 列出物件
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

### `find` - 依名稱搜尋物件
{: #minio-find}

搜尋選項的完整清單提供於 [complete guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}。
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - 顯示幾行物件
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - 複製物件
{: #minio-cp}

這個指令會在兩個位置之間複製物件。這些位置可以是不同的主機（例如不同的[端點](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)或儲存空間服務）或本端檔案系統位置（例如 `~/foo/filename.pdf`）。
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - 移除物件
{: #minio-rm}

*更多的移除選項提供於 [complete guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - 將 STDIN 複製到物件
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
