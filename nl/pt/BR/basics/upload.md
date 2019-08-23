---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Fazer upload de dados
{: #upload}

Depois de deixar seus depósitos organizados, é hora de incluir alguns objetos. Dependendo de como você deseja usar seu armazenamento, há maneiras diferentes de obter dados no sistema. Um cientista de dados tem alguns arquivos grandes que são usados para análise de dados, um administrador de sistemas precisa manter os backups de banco de dados sincronizados com os arquivos locais e um desenvolvedor está gravando software que precisa ler e gravar milhões de arquivos. Cada um desses cenários é mais bem atendido por métodos diferentes de ingestão de dados.

## Utilizando o Console
{: #upload-console}

Geralmente, usar o console baseado na web não é a maneira mais comum de usar o {{site.data.keyword.cos_short}}. Os objetos são limitados a 200 MB e o nome do arquivo e a chave são idênticos. Múltiplos objetos podem ser transferidos por upload ao mesmo tempo e, se o navegador permitir múltiplos encadeamentos, cada objeto será transferido por upload usando múltiplas partes em paralelo. O suporte para tamanhos de objetos maiores e desempenho melhorado (dependendo de fatores de rede) é fornecido pelo [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera).

## Usando uma ferramenta compatível
{: #upload-tool}

Alguns usuários desejam usar um utilitário independente para interagir com seu armazenamento. Como a API do Cloud Object Storage suporta o conjunto mais comum de operações da API S3, muitas ferramentas compatíveis com S3 também podem se conectar ao {{site.data.keyword.cos_short}} usando as [credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).

Alguns exemplos incluem exploradores de arquivos como [Cyberduck](https://cyberduck.io/) ou [Transmit](https://panic.com/transmit/), utilitários de backup como [Cloudberry](https://www.cloudberrylab.com/) e [Duplicati](https://www.duplicati.com/), utilitários de linha de comandos como [s3cmd](https://github.com/s3tools/s3cmd) ou [Minio Client](https://github.com/minio/mc) e muitos outros.

## Usando a API
{: #upload-api}

A maioria dos aplicativos programáticos do Object Storage usa um SDK (como [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) ou [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python)) ou a [API do Cloud Object Storage](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api). Geralmente, os objetos são transferidos por upload em [múltiplas partes](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects), com o tamanho da parte e o número de partes configurados por uma classe do Transfer Manager.
