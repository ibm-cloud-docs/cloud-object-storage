---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, error

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

# Sobre a API S3 do {{site.data.keyword.cos_full_notm}}
{: #compatibility-api}

A API do {{site.data.keyword.cos_full}} é uma API baseada em REST para objetos de leitura e composição. Ela usa o {{site.data.keyword.iamlong}} para autenticação e autorização e suporta um subconjunto da API S3 para fácil migração de aplicativos para o {{site.data.keyword.cloud_notm}}.

Esta documentação de referência está sendo continuamente melhorada. Se você tiver perguntas técnicas sobre como usar a API em seu aplicativo, poste-as no [Stack Overflow](https://stackoverflow.com/). Inclua as tags `ibm-cloud-platform` e `object-storage` e ajude a melhorar esta documentação com seu feedback.

Como é relativamente fácil trabalhar com os tokens do {{site.data.keyword.iamshort}}, o `curl` é uma boa opção para teste básico e interação com seu armazenamento. Mais informações podem ser localizadas [na referência de `curl`](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl).

As tabelas a seguir descrevem o conjunto completo de operações da API do {{site.data.keyword.cos_full_notm}}. Para obter mais informações, consulte [a página de referência da API para depósitos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) ou [objetos](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).


## Operações de depósito
{: #compatibility-api-bucket}

Essas operações criam, excluem, obtêm informações e controlam o comportamento de depósitos.

| Operação de depósito      | Nota                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` depósitos         | Usado para recuperar uma lista de todos os depósitos que pertencem a uma conta.|
| `DELETE` depósito       | Exclui um depósito vazio.                                                      |
| `DELETE` CORS de depósito | Exclui qualquer configuração de CORS (compartilhamento de recurso de origem cruzada) definida em um depósito. |
| `GET` depósito          | Lista objetos em um depósito. Limitado à listagem de 1.000 objetos por vez.         |
| `GET` CORS de depósito  | Recupera qualquer configuração de CORS definida em um depósito.                |
| `HEAD` depósito         | Recupera os cabeçalhos de um depósito.                                        |
| `GET` uploads de múltiplas partes | Lista uploads de múltiplas partes que não estão concluídos ou cancelados.         |
| `PUT` depósito          | Os depósitos têm restrições de nomenclatura. As contas são limitadas a 100 depósitos. |
| `PUT` CORS de depósito  | Cria uma configuração de CORS para um depósito.                                |


## Operações de objeto
{: #compatibility-api-object}

Essas operações criam, excluem, obtêm informações e controlam o comportamento de objetos.

| Operação de objeto          | Nota                                                                                |
|:--------------------------|:------------------------------------------------------------------------------------|
| `DELETE` objeto           | Exclui um objeto de um depósito.                                                   |
| `DELETE` lote             | Exclui muitos objetos de um depósito com uma operação.                             |
| `GET` objeto              | Recupera um objeto de um depósito.                                                 |
| `HEAD` objeto             | Recupera os cabeçalhos de um objeto.                                               |
| `OPTIONS` objeto          | Verifica a configuração de CORS para ver se uma solicitação específica pode ser enviada. |
| `PUT` objeto              | Inclui um objeto em um depósito.                                                   |
| `PUT` Objeto (copiar)       | Cria uma cópia de um objeto.                                                       |
| Iniciar upload de múltiplas partes | Cria um ID de upload para um conjunto de partes a serem transferidas por upload.   |
| Fazer upload de parte     | Faz upload de uma parte de um objeto que está associado a um ID de upload.         |
| Fazer upload de parte (copiar) | Faz upload de uma parte de um objeto existente que está associado a um ID de upload. |
| Concluir upload de múltiplas partes | Monta um objeto de partes que estão associadas a um ID de upload.                   |
| Cancelar upload de múltiplas partes | Cancela o upload e exclui as partes pendentes que estão associadas a um ID de upload. |
| Listar partes             | Retorna uma lista de partes que estão associadas a um ID de upload                  |


Algumas operações adicionais, como a identificação e a versão, são atualmente suportadas em implementações de nuvem privada do {{site.data.keyword.cos_short}}, mas não em nuvens públicas ou dedicadas. Mais informações sobre soluções do Object Storage customizadas podem ser localizadas em [ibm.com](https://www.ibm.com/cloud/object-storage).
