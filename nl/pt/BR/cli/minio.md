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

# Usando o Minio Client
{: #minio}

Deseja usar comandos familiares semelhantes ao UNIX (`ls`, `cp`, `cat`, etc.) com o {{site.data.keyword.cos_full}}? Se sim, o software livre [Minio Client](https://min.io/download#/linux){:new_window} é a resposta. É possível localizar instruções de instalação para cada sistema operacional que estão disponíveis no [guia de iniciação rápida](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} no website do Minio.

## Configuração
{: #minio-config}

A inclusão de seu {{site.data.keyword.cos_short}} é realizada executando o comando a seguir:

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>` - nome abreviado para referenciar o {{site.data.keyword.cos_short}} em comandos
* `<COS-ENDPOINT` - terminal para sua instância do {{site.data.keyword.cos_short}}. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<ACCESS-KEY>` - chave de acesso que está designada à sua Credencial de serviço
* `<SECRET-KEY>` - chave secreta que é designada à sua Credencial de serviço

As informações de configuração são armazenadas em um arquivo JSON que está em `~/.mc/config.json`

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Comandos de amostra
{: #minio-commands}

Uma lista completa de comandos e sinalizações opcionais e parâmetros está documentada no [Guia completo do Minio Client](https://docs.min.io/docs/minio-client-complete-guide){:new_window}

### `mb` - Fazer um depósito
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls` - Listar depósitos
{: #minio-ls}

Embora todos os seus depósitos disponíveis estejam listados, nem todos os objetos podem ser acessíveis, dependendo da região do [terminal](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) especificada.
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls` - Listar objetos
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

### `find` - Procurar objetos por nome
{: #minio-find}

Uma lista integral de opções de procura está disponível no [guia completo](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head` - Exibir algumas linhas de objeto
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp` - Copiar objetos
{: #minio-cp}

Esse comando copia um objeto entre dois locais. Esses locais podem ser hosts diferentes (como [terminais](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) ou serviços de armazenamento diferentes) ou locais do sistema de arquivos local (como `~/foo/filename.pdf`).
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm` - Remover objetos
{: #minio-rm}

*Mais opções de remoção estão disponíveis no [guia completo](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe` - Copia o STDIN para um objeto
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
