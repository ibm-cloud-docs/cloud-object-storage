---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: activity tracker, event logging, observability

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
{:table: .aria-labeledby="caption"}


# {{site.data.keyword.cloudaccesstrailshort}}  eventos
{: #at-events}

Use o serviço {{site.data.keyword.cloudaccesstrailfull}} para controlar como os usuários e os aplicativos interagem com o {{site.data.keyword.cos_full}}.
{: shortdesc}

O serviço {{site.data.keyword.cloudaccesstrailfull_notm}} registra atividades iniciadas pelo usuário que mudam o estado de um serviço no {{site.data.keyword.Bluemix_notm}}. Para obter mais informações, consulte
[Introdução ao {{site.data.keyword.cloudaccesstrailshort}}](/docs/services/cloud-activity-tracker?topic=cloud-activity-tracker-getting-started).



## Lista de eventos
{: #at-events-list}

A tabela a seguir lista as ações que geram um evento:

<table>
  <caption>Ações que geram eventos</caption>
  <tr>
    <th>Ações</th>
	  <th>Descrição</th>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.info`</td>
	  <td>Um evento é gerado quando um usuário solicita metadados do depósito e se o IBM Key Protect está ativado no depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.create`</td>
	  <td>Um evento é gerado quando um usuário cria um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.read`</td>
	  <td>Um evento é gerado quando um usuário solicita a lista de objetos em um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.update`</td>
	  <td>Um evento é gerado quando um usuário atualiza um depósito, por exemplo, quando um usuário renomeia um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket.delete`</td>
	  <td>Um evento é gerado quando um usuário exclui um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.create`</td>
	  <td>Um evento é gerado quando um usuário configura a lista de controle de acesso em um depósito para `public-read` ou `private`.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-acl.read`</td>
	  <td>Um evento é gerado quando um usuário lê a lista de controle de acesso em um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.create`</td>
	  <td>Um evento é gerado quando um usuário cria uma configuração de compartilhamento de recurso de origem cruzada para um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.read`</td>
	  <td>Um evento é gerado quando um usuário solicita se a configuração de compartilhamento de recurso de origem cruzada está ativada em um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.update`</td>
	  <td>Um evento é gerado quando um usuário modifica uma configuração de compartilhamento de recurso de origem cruzada para um depósito.</td>
  </tr>
  <tr>
    <td>`cloud-object-storage.bucket-cors.delete`</td>
	  <td>Um evento é gerado quando um usuário exclui uma configuração de compartilhamento de recurso de origem cruzada para um depósito.</td>
  </tr>
</table>



## Onde visualizar os eventos
{: #at-ui}

Os eventos do {{site.data.keyword.cloudaccesstrailshort}} estão disponíveis no **domínio de contas** do {{site.data.keyword.cloudaccesstrailshort}}.

Os eventos são enviados para a região do {{site.data.keyword.cloudaccesstrailshort}} mais próxima ao local do depósito do {{site.data.keyword.cos_full_notm}} que é mostrado na [página de serviços suportados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability#integrated-service-availability).
