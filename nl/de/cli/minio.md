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

# MinIO Client verwenden
{: #minio}

Möchten Sie gängige UNIX-Befehle (`ls`, `cp`, `cat` usw.) für {{site.data.keyword.cos_full}} verwenden? In diesem Fall stellt das Open-Source-Tool [MinIO Client](https://min.io/download#/linux){:new_window} eine ideale Lösung dar. Installationsanweisungen für jedes Betriebssystem finden Sie im [Quickstart Guide](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} auf der MinIO-Website.

## Konfiguration
{: #minio-config}

Mit dem folgenden Befehl können Sie {{site.data.keyword.cos_short}} hinzufügen:

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - Kurzname für Verweise auf {{site.data.keyword.cos_short}} in Befehlen.
* `<COS-ENDPOINT` - Endpunkt Ihrer {{site.data.keyword.cos_short}}-Instanz. Weitere Informationen zu Endpunkten finden Sie in [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<ACCESS-KEY>` - Zugriffsschlüssel, der Ihrem Serviceberechtigungsnachweis zugeordnet ist.
* `<SECRET-KEY>` - Geheimer Schlüssel, der Ihrem Serviceberechtigungsnachweis zugeordnet ist.

Die Konfigurationsinformationen werden in einer JSON-Datei gespeichert, die sich unter `~/.mc/config.json` befindet.

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Beispielbefehle
{: #minio-commands}

Eine vollständige Liste der Befehle und optionalen Flags und Parameter sind im [MinIO Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window} dokumentiert.

### `mb` - Bucket erstellen
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - Buckets auflisten
{: #minio-ls}

Obwohl alle verfügbaren Buckets aufgelistet werden, kann möglicherweise nicht auf alle Objekte zugegriffen werden. Dies richtet sich nach der Region des angegebenen [Endpunkts](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - Objekte auflisten
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

### `find` - Objekte nach Namen suchen
{: #minio-find}

Eine vollständige Liste der Suchoptionen finden Sie im [Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}.
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - Wenige Objektzeilen anzeigen
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - Objekte kopieren
{: #minio-cp}

Mit diesem Befehl wird ein Objekt zwischen zwei Standorten kopiert. Diese Standorte können unterschiedliche Hosts (z. B. verschiedene [Endpunkte](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) oder Speicherservices) oder Positionen im lokalen Dateisystem sein (z. B. `~/foo/filename.pdf`).
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - Objekte entfernen
{: #minio-rm}

*Weitere Entfernungsoptionen sind im [Complete Guide](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window} verfügbar.*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - STDIN in ein Objekt kopieren
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
