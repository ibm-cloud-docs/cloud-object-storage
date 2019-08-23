---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: big data, multipart, multiple parts, transfer

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
{:S3cmd: .ph data-hd-programlang='S3cmd'}

# Armazenar objetos grandes
{: #large-objects}

O {{site.data.keyword.cos_full}} pode suportar objetos únicos tão grandes quanto 10 TB ao usar uploads de múltiplas partes. Os objetos grandes também podem ser transferidos por upload [usando o console com o Aspera high-speed transfer ativado](/docs/services/cloud-object-storage?topic=cloud-object-storage-aspera). Na maioria dos cenários, o Aspera high-speed transfer resulta em um desempenho significativamente maior para transferir dados, especialmente em longas distâncias ou sob condições de rede instável.

## Fazendo upload de objetos em múltiplas partes
{: #large-objects-multipart}

As operações de upload de múltiplas partes são recomendadas para gravar objetos maiores no {{site.data.keyword.cos_short}}. Um upload de um único objeto é executado como um conjunto de partes e essas partes podem ser transferidas por upload independentemente em qualquer ordem e em paralelo. Após a conclusão do upload, o {{site.data.keyword.cos_short}} então apresenta todas as partes como um único objeto. Isso fornece muitos benefícios: as interrupções de rede não fazem com que grandes uploads falhem, os uploads podem ser pausados e reiniciados ao longo do tempo e os objetos podem ser transferidos por upload conforme eles estão sendo criados.

Os uploads de múltiplas partes estão disponíveis somente para objetos maiores que 5 MB. Para objetos menores que 50 GB, um tamanho de parte de 20 MB a 100 MB é recomendado para desempenho ideal. Para objetos maiores, o tamanho da parte pode ser aumentado sem impacto significativo no desempenho. Os uploads de múltiplas partes são limitados a não mais que 10.000 partes de 5 GB cada até um tamanho máximo de objeto de 10 TB.


Devido à complexidade envolvida no gerenciamento e otimização de uploads feitos em paralelo, muitos desenvolvedores usam bibliotecas que fornecem suporte de upload de múltiplas partes.

A maioria das ferramentas, como as CLIs ou o IBM Cloud Console, assim como as bibliotecas e os SDKs mais compatíveis, transferirá automaticamente os objetos em uploads de múltiplas partes.

## Usando a API de REST ou os SDKs
{: #large-objects-multipart-api} 

Os uploads de múltiplas partes incompletos persistem até que o objeto seja excluído ou que o upload de múltiplas partes seja interrompido. Se um upload de múltiplas partes incompleto não for interrompido, o upload parcial continuará a usar recursos. As interfaces devem ser projetadas tendo esse ponto em mente e limpar os uploads de múltiplas partes incompletos.
{:tip}

Há três fases para fazer upload de um objeto em múltiplas partes:

1. O upload é iniciado e um `UploadId` é criado.
2. As partes individuais são transferidas por upload especificando seus números de partes sequenciais e o `UploadId` para o objeto.
3. Quando todas as partes concluem o upload, o upload é concluído enviando uma solicitação com o `UploadId` e um bloco XML que lista cada número de parte e seu respectivo valor `Etag`.

Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

### Iniciar um upload de múltiplas partes
{: #large-objects-multipart-api-initiate} 
{: http}

Um `POST` emitido para um objeto com o parâmetro de consulta `upload` cria um novo valor `UploadId`, que é, então, referenciado por cada parte do objeto que está sendo transferido por upload.
{: http}

**Sintaxe**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploads= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploads= # virtual host style
```
{: codeblock}
{: http}

**Solicitação de exemplo**
{: http}

```http
POST /some-bucket/multipart-object-123?uploads= HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Resposta de exemplo**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 20:34:12 GMT
X-Clv-Request-Id: 258fdd5a-f9be-40f0-990f-5f4225e0c8e5
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
Content-Type: application/xml
Content-Length: 276
```
{: codeblock}
{: http}

```xml
<InitiateMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <UploadId>0000015a-95e1-4326-654e-a1b57887784f</UploadId>
</InitiateMultipartUploadResult>
```
{: codeblock}
{: http}

----

### Fazer upload de uma parte
{: #large-objects-multipart-api-upload-part} 
{: http}

Uma solicitação `PUT` que é emitida para um objeto com parâmetros de consulta `partNumber` e `uploadId` fará upload de uma parte de um objeto. As partes podem ser transferidas por upload em série ou em paralelo, mas devem ser numeradas em ordem.
{: http}

**Sintaxe**
{: http}

```bash
PUT https://{endpoint}/{bucket-name}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # path style
PUT https://{bucket-name}.{endpoint}/{object-name}?partNumber={sequential-integer}&uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Solicitação de exemplo**
{: http}

```http
PUT /some-bucket/multipart-object-123?partNumber=1&uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/pdf
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 13374550
```
{: codeblock}
{: http}

**Resposta de exemplo**
{: http}

```http
HTTP/1.1 200 OK
Date: Sat, 18 Mar 2017 03:56:41 GMT
X-Clv-Request-Id: 17ba921d-1c27-4f31-8396-2e6588be5c6d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "7417ca8d45a71b692168f0419c17fe2f"
Content-Length: 0
```
{: codeblock}
{: http}

### Concluir um upload de múltiplas partes
{: #large-objects-multipart-api-complete} 
{: http}

Uma solicitação `POST` que é emitida para um objeto com o parâmetro de consulta `uploadId` e o bloco XML apropriado no corpo concluirá um upload de múltiplas partes.
{: http}

**Sintaxe**
{: http}

```bash
POST https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
POST https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>{sequential part number}</PartNumber>
    <ETag>{ETag value from part upload response header}</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Solicitação de exemplo**
{: http}

```http
POST /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Content-Type: text/plain; charset=utf-8
Host: s3.us.cloud-object-storage.appdomain.cloud
Content-Length: 257
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUpload>
  <Part>
    <PartNumber>1</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
  <Part>
    <PartNumber>2</PartNumber>
    <ETag>"7417ca8d45a71b692168f0419c17fe2f"</ETag>
  </Part>
</CompleteMultipartUpload>
```
{: codeblock}
{: http}

**Resposta de exemplo**
{: http}

```http
HTTP/1.1 200 OK
Date: Fri, 03 Mar 2017 19:18:44 GMT
X-Clv-Request-Id: c8be10e7-94c4-4c03-9960-6f242b42424d
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
ETag: "765ba3df36cf24e49f67fc6f689dfc6e-2"
Content-Type: application/xml
Content-Length: 364
```
{: codeblock}
{: http}

```xml
<CompleteMultipartUploadResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
  <Location>http://s3.us.cloud-object-storage.appdomain.cloud/zopse/multipart-object-123</Location>
  <Bucket>some-bucket</Bucket>
  <Key>multipart-object-123</Key>
  <ETag>"765ba3df36cf24e49f67fc6f689dfc6e-2"</ETag>
</CompleteMultipartUploadResult>
```
{: codeblock}
{: http}


### Interromper uploads de múltiplas partes incompletos
{: #large-objects-multipart-api-abort} 
{: http}

Uma solicitação `DELETE` emitida para um objeto com o parâmetro de consulta `uploadId` exclui todas as partes não concluídas de um upload de múltiplas partes.
{: http}
**Sintaxe**
{: http}

```bash
DELETE https://{endpoint}/{bucket-name}/{object-name}?uploadId={uploadId}= # path style
DELETE https://{bucket-name}.{endpoint}/{object-name}?uploadId={uploadId}= # virtual host style
```
{: codeblock}
{: http}

**Solicitação de exemplo**
{: http}

```http
DELETE /some-bucket/multipart-object-123?uploadId=0000015a-df89-51d0-2790-dee1ac994053 HTTP/1.1
Authorization: Bearer {token}
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}

**Resposta de exemplo**
{: http}

```http
HTTP/1.1 204 No Content
Date: Thu, 16 Mar 2017 22:07:48 GMT
X-Clv-Request-Id: 06d67542-6a3f-4616-be25-fc4dbdf242ad
Accept-Ranges: bytes
Server: Cleversafe/3.9.1.114
X-Clv-S3-Version: 2.5
```
{: codeblock}
{: http}

### Usando o S3cmd (CLI)
{: #large-objects-s3cmd} 
{: S3cmd}

O [S3cmd](https://s3tools.org/s3cmd){:new_window} é uma ferramenta de linha de comandos e cliente Linux e Mac grátis para fazer upload, recuperar e gerenciar dados em provedores de serviços de armazenamento em nuvem que usam o protocolo S3. Ele é projetado para usuários avançados que estão familiarizados com os programas de linha de comandos e são ideais para scripts em lote e backup automatizado. O S3cmd é gravado em Python. É um projeto de software livre disponível sob o GNU Public License v2 (GPLv2) e é grátis para uso comercial e privado.
{: S3cmd}

O S3cmd requer o Python 2.6 ou mais recente e é compatível com o Python 3. A maneira mais fácil de instalar o S3cmd é com o Python Package Index (PyPi).
{: S3cmd}

```
pip install s3cmd
```
{: codeblock}
{: S3cmd}

Depois que o pacote tiver sido instalado, pegue o arquivo de configuração de exemplo do {{site.data.keyword.cos_full}} [aqui](https://gist.githubusercontent.com/greyhoundforty/a4a9d80a942d22a8a7bf838f7abbcab2/raw/05ad584edee4370f4c252e4f747abb118d0075cb/example.s3cfg){:new_window} e atualize-o com suas credenciais do Cloud Object Storage (S3):
{: S3cmd}

```
$ wget -O $HOME/.s3cfg https://gist.githubusercontent.com/greyhoundforty/676814921b8f4367fba7604e622d10f3/raw/422abaeb70f1c17cd5308745c0e446b047c123e0/s3cfg
```
{: codeblock}
{: S3cmd}

As quatro linhas que precisam ser atualizadas são
{: S3cmd}

* `access_key`
* `secret_key`
* `host_base`
* `host_bucket`
{: S3cmd}
Isso é o mesmo se você usar o arquivo de exemplo ou aquele gerado executando: `s3cmd --configure`.
{: S3cmd}

Assim que essas linhas tiverem sido atualizadas com os detalhes do COS do portal do Cliente, será possível testar a conexão emitindo o comando `s3cmd ls`, que listará todos os depósitos na conta.
{: S3cmd}

```
$ s3cmd ls 
2017-02-03 14:52  s3://backuptest
2017-02-06 15:04  s3://coldbackups
2017-02-03 21:23  s3://largebackup
2017-02-07 17:44  s3://winbackup
```
{: codeblock}
{: S3cmd}

A lista integral de opções e comandos junto com as informações básicas de uso está disponível no site [s3tools](https://s3tools.org/usage){:new_window}.
{: S3cmd}

### Uploads de múltiplas partes com S3cmd
{: #large-objects-s3cmd-upload} 
{: S3cmd}

Um comando `put` executará automaticamente um upload de múltiplas partes ao tentar fazer upload de um arquivo maior que o limite especificado.
{: S3cmd}

```
s3cmd put FILE [FILE...] s3://BUCKET[/PREFIX]
```
{: codeblock}
{: S3cmd}

O limite é determinado pela opção `--multipart-chunk-size-mb`:
{: S3cmd}

```
--multipart-chunk-size-mb=SIZE
    Size of each chunk of a multipart upload. Files bigger
    than SIZE are automatically uploaded as multithreaded-
    multipart, smaller files are uploaded using the
    traditional method. SIZE is in megabytes, default
    chunk size is 15MB, minimum allowed chunk size is 5MB,
    maximum is 5GB.
```
{: codeblock}
{: S3cmd}

Exemplo:
{: S3cmd}

```
s3cmd put bigfile.pdf s3://backuptest/bigfile.pdf --multipart-chunk-size-mb=5
```
{: codeblock}
{: S3cmd}

Saída:
{: S3cmd}

```
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 1 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  1731.92 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 2 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2001.14 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 3 of 4, 5MB] [1 of 1]
 5242880 of 5242880   100% in    2s  2000.28 kB/s  done
upload: 'bigfile.pdf' -> 's3://backuptest/bigfile.pdf'  [part 4 of 4, 4MB] [1 of 1]
 4973645 of 4973645   100% in    2s  1823.51 kB/s  done
 ```
{: codeblock}
{: S3cmd}

### Usando o SDK Java
{: #large-objects-java} 
{: java}

O SDK Java fornece duas maneiras de executar uploads de objetos grandes:
{: java}

* [Uploads de múltiplas partes](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-multipart-object)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-transfer-manager)
{: codeblock}
{: java}

### Usando o SDK Python
{: #large-objects-python} 
{: python}

O SDK Python fornece duas maneiras de executar uploads de objetos grandes:
{: python}

* [Uploads de múltiplas partes](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart)
* [TransferManager](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-multipart-transfer)
{: codeblock}
{: python}

### Usando o SDK Node.js
{: #large-objects-node} 
{: javascript}

O SDK Node.js fornece uma única maneira de executar uploads de objetos grandes:
{: javascript}

* [Uploads de múltiplas partes](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#node-multipart-upload)
{: codeblock}
{: javascript}
