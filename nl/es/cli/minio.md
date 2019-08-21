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

# Utilización de Minio Client
{: #minio}

¿Desea utilizar mandatos de tipo UNIX con los que está familiarizado (`ls`, `cp`, `cat`, etc.) con {{site.data.keyword.cos_full}}? Si es así, [Minio Client](https://min.io/download#/linux){:new_window} de código abierto es la respuesta. Encontrará instrucciones de instalación para cada sistema operativo disponible en la [guía de inicio rápido](https://docs.min.io/docs/minio-client-quickstart-guide.html){:new_window} en el sitio web de Minio.

## Configuración
{: #minio-config}

Para añadir su {{site.data.keyword.cos_short}}, ejecute el siguiente mandato:

```
mc config host add <ALIAS> <COS-ENDPOINT> <ACCESS-KEY> <SECRET-KEY>
```

* `<ALIAS>`: nombre abreviado para hacer referencia a {{site.data.keyword.cos_short}} en mandatos
* `<COS-ENDPOINT`: punto final para la instancia de {{site.data.keyword.cos_short}}. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<ACCESS-KEY>`: clave de acceso que se asigna a la credencial de servicio
* `<SECRET-KEY>`: clave secreta que se asigna a la credencial de servicio

La información de configuración se guarda en un archivo JSON que se encuentra en `~/.mc/config.json`

```
mc config host add cos https://s3.us-south.cloud-object-storage.appdomain.cloud xx1111cfbe094710x4819759x57e9999 9f99fc08347d1a6xxxxx0b7e0a9ee7b0c9999c2c08ed0000
```

## Mandatos de ejemplo
{: #minio-commands}

Encontrará una lista completa de mandatos y parámetros y distintivos opcionales en la [Guía completa de Minio Client](https://docs.min.io/docs/minio-client-complete-guide){:new_window}

### `mb`: crear un grupo
{: #minio-mb}

```
mc mb cos/my_test_bucket
```

### `ls`: obtener una lista de grupos
{: #minio-ls}

Aunque se muestran todos los grupos disponibles, es posible que no se pueda acceder a todos los objetos en función de la región de [punto final](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) especificada.
{: tip}

```
mc ls cos
```

```
[2018-06-05 09:55:08 HST]     0B testbucket1/
[2018-05-24 04:17:34 HST]     0B testbucket_south/
[2018-10-15 16:14:28 HST]     0B my_test_bucket/
```


### `ls`: obtener una lista de objetos
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

### `find`: buscar objetos por nombre
{: #minio-find}

Encontrará una lista completa de opciones de búsqueda en la [guía completa](https://docs.min.io/docs/minio-client-complete-guide#find){:new_window}
{: tip}

```
mc find cos/testbucket1 --name my*
```

```
[2018-11-12 08:09:53 HST]    34B mynewfile1.txt
[2018-05-31 01:49:26 HST]    34B mynewfile12.txt
```

### `head`: mostrar algunas líneas de un objeto
{: #minio-head}

```
mc head cos/testbucket1/mynewfile1.txt
```

### `cp`: copiar objetos
{: #minio-cp}

Este mandato copia un objeto entre dos ubicaciones. Estas ubicaciones pueden ser hosts distintos (por ejemplo, distintos [puntos finales](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints) o servicios de almacenamiento) o ubicaciones del sistema de archivos local (por ejemplo, `~/foo/filename.pdf`).
```
mc cp cos/testbucket1/mynewfile1.txt cos/my_test_bucket/cp_from_minio.txt
```

```
...1/mynewfile1.txt:  34 B / 34 B  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  100.00% 27 B/s 1s
```

### `rm`: eliminar objetos
{: #minio-rm}

*Encontrará más opciones de eliminación disponibles en la [guía completa](https://docs.min.io/docs/minio-client-complete-guide#rm){:new_window}*

```
mc rm cos/my_test_bucket/cp_from_minio.txt
```

### `pipe`: copiar STDIN en un objeto
{: #minio-pipe}

```
echo -n 'this is a test' | mc pipe cos/my_test_bucket/stdin_pipe_test.txt
```
