---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, backup, cloudberry

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


# Cloudberry Labs
{: #cloudberry}

## Cloudberry Backup
{: #cloudberry-backup}

O Cloudberry Backup é um utilitário flexível que permite aos usuários fazer backup de alguns ou de todos de um sistema de arquivos local para um sistema de armazenamento de objeto compatível com a API S3. As versões Free e Professional estão disponíveis para Windows, MacOS e Linux e suportam vários serviços de armazenamento em nuvem populares, incluindo o {{site.data.keyword.cos_full}}. O Backup do Cloudberry pode ser transferido por download por meio de [cloudberrylab.com](https://www.cloudberrylab.com/).

O Backup Cloudberry inclui muitos recursos úteis, incluindo:

* Planejamento
* Backups incrementais e em nível de bloco
* Interface da linha de comandos
* Notificações por e-mail
* Compactação (*somente versão Pro*)

## Cloudberry Explorer
{: #cloudberry-explorer}

Um novo produto do Cloudberry Labs oferece uma interface com o usuário de gerenciamento de arquivo familiar para o {{site.data.keyword.cos_short}}. O [Cloudberry Explorer](https://www.cloudberrylab.com/explorer.aspx){:new_window} também é fornecido nas versões Free e Pro, mas está atualmente disponível somente para Windows. Os recursos-chave incluem:

* Sincronização de pasta/depósito
* Interface da linha de comandos
* Gerenciamento de ACL
* Relatórios de capacidade

A Versão Pro também inclui:
* Procurar 
* Criptografia/compactação
* Upload continuável
* Suporte ao FTP/SFTP

## Usando o Cloudberry com o Object Storage
{: #cloudberry-cos}

Pontos-chave a serem lembrados ao configurar produtos Cloudberry para trabalhar com o {{site.data.keyword.cos_short}}:

* Selecione `S3 Compatible` na lista de opções
* Somente as [Credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) são suportadas atualmente
* Uma conexão separada é necessária para cada depósito
* Assegure-se de que o `Endpoint` especificado na conexão corresponda à região do depósito selecionado (*o backup falhará devido a um destino inacessível*). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
