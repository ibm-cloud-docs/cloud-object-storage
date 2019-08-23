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

# Utilizzo di Minio Client
{: #minio}

Vuoi utilizzare dei comandi familiari simili a UNIX (`ls`, `cp`, `cat`, ecc.) con {{site.data.keyword.cos_full}}? Se sì, [Minio Client](https://min.io/download#/linux){:new_window} open source è la risposta. Puoi trovare le istruzioni di installazione per ciascun sistema operativo nella [guida introduttiva](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} sul sito web di Minio.

## Configurazione
{: #minio-config}

L'aggiunta del tuo {{site.data.keyword.cos_short}} viene effettuata eseguendo il seguente comando: 

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - nome breve per {{site.data.keyword.cos_short}} di riferimento nei comandi
* `<COS-ENDPOINT` - endpoint per la tua istanza {{site.data.keyword.cos_short}}. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). 
* `<ACCESS-KEY>` - chiave di accesso assegnata alla tua credenziale del servizio
* `<SECRET-KEY>` - chiave del segreto assegnata alla tua credenziale del servizio

Le informazioni di configurazione sono archiviate in un file JSON che si trova in `~/.mc/config.json`

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Comandi di esempio
{: #minio-commands}

Un elenco completo dei comandi e dei parametri e degli indicatori facoltativi è documentato in [Minio Client Complete Guide](https://docs.min.io/docs/minio-client-complete-guide){:new_window}

### `mb` - Crea un bucket
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - Elenca i bucket
{: #minio-ls}

Anche se vengono elencati tutti i tuoi bucket disponibili, è possibile che non tutti gli oggetti siano accessibili, dipende dalla regione dell'[endpoint](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) specificata.
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - Elenca gli oggetti
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

### `find` - Ricerca gli oggetti in base al nome
{: #minio-find}

Un elenco completo delle opzioni di ricerca è disponibile nella [guida completa](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - Visualizza poche righe dell'oggetto
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - Copia gli oggetti
{: #minio-cp}

Questo comando copia un oggetto tra due ubicazioni. Queste ubicazioni possono essere host diversi (ad esempio [endpoint](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) o servizi di archiviazione diversi) oppure ubicazioni file system locali (ad esempio, `~/foo/filename.pdf`).
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - Rimuovi gli oggetti
{: #minio-rm}

*Altre opzioni di rimozione sono disponibili nella [guida completa](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - Copia STDIN in un oggetto
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
