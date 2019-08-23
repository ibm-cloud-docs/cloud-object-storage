---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: developer, best practices

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

# Orientação do desenvolvedor
{: #dev-guide}

## Ajustando as configurações de cifra
{: #dev-guide-cipher}

O {{site.data.keyword.cos_full}} suporta uma variedade de configurações de cifras para criptografar dados em trânsito. Nem todas as configurações de cifra produzem o desempenho de mesmo nível. A negociação de um `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384`, `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_256_CBC_SHA`, `TLS_RSA_WITH_AES_128_CBC_SHA` demonstrou gerar os mesmos níveis de desempenho que nenhum TLS entre o cliente e o {{site.data.keyword.cos_full_notm}} System.

## Usando uploads de múltiplas partes
{: #dev-guide-multipart}

Ao trabalhar com objetos maiores, as operações de upload de múltiplas partes são recomendadas para gravar objetos no {{site.data.keyword.cos_full_notm}}. Um upload de um único objeto pode ser executado como um conjunto de partes e essas partes podem ser transferidas por upload independentemente em qualquer ordem e em paralelo. Após a conclusão do upload, o {{site.data.keyword.cos_short}} então apresenta todas as partes como um único objeto. Isso fornece muitos benefícios: as interrupções de rede não fazem com que grandes uploads falhem, os uploads podem ser pausados e reiniciados ao longo do tempo e os objetos podem ser transferidos por upload conforme eles estão sendo criados.

Os uploads de múltiplas partes estão disponíveis apenas para objetos maiores que 5 MB. Para objetos menores que 50 GB, um tamanho de parte de 20 MB a 100 MB é recomendado para desempenho ideal. Para objetos maiores, o tamanho da parte pode ser aumentado sem impacto significativo no desempenho.

O uso de mais de 500 partes leva a ineficiências no {{site.data.keyword.cos_short}} e deve ser evitado quando possível.

Devido à complexidade adicional envolvida, é recomendável que os desenvolvedores façam uso de bibliotecas da API S3 que fornecem suporte de upload de múltiplas partes.

Os uploads de múltiplas partes incompletos persistem até que o objeto seja excluído ou o upload de múltiplas partes seja interrompido com `AbortIncompleteMultipartUpload`. Se um upload de múltiplas partes incompleto não for interrompido, o upload parcial continuará a usar recursos. As interfaces devem ser projetadas tendo esse ponto em mente e limpar os uploads de múltiplas partes incompletos.


## Usando kits de desenvolvimento de software
{: #dev-guide-sdks}

Não é obrigatório usar SDKs da API S3 publicados; o software customizado pode alavancar a API para integrar-se diretamente ao {{site.data.keyword.cos_short}}. No entanto, o uso de bibliotecas da API S3 publicadas fornece vantagens como a geração de autenticação/assinatura, a lógica de nova tentativa automática em erros `5xx` e a geração de url pré-assinada. Deve-se tomar cuidado ao gravar o software que usa a API diretamente para manipular erros transitórios, como por exemplo, fornecendo novas tentativas com backoff exponencial ao receber erros `503`.

## Paginação
{: #dev-guide-pagination}

Ao lidar com um grande número de objetos em um depósito, os aplicativos da web podem começar a sofrer degradação de desempenho. Muitos aplicativos empregam uma técnica chamada **paginação** (*o processo de dividir um grande conjunto de registros em páginas discretas*). Quase todas as plataformas de desenvolvimento fornecem objetos ou métodos para realizar a paginação por funcionalidade integrada ou por meio de bibliotecas de terceiros.

Os SDKs do {{site.data.keyword.cos_short}} fornecem suporte para a paginação por meio de um método que lista os objetos em um depósito especificado. Esse método fornece vários parâmetros que o tornam extremamente útil ao tentar quebrar um conjunto de resultados grande.

### Uso básico
{: #dev-guide-pagination-basics}
O conceito básico por trás do método de listagem de objetos envolve configurar o número máximo de chaves (`MaxKeys`) para retornar na resposta. A resposta também inclui um valor `boolean` (`IsTruncated`) que indica se mais resultados estão disponíveis e um valor `string` chamado `NextContinuationToken`. A configuração do token de continuação nas solicitações de acompanhamento retorna o próximo lote de objetos até que mais nenhum resultado esteja disponível.

#### Parâmetros comuns
{: #dev-guide-pagination-params}

|Parâmetro|Descrição|
|---|---|
|`ContinuationToken`|Configura o token para especificar o próximo lote de registros|
|`MaxKeys`|Configura o número máximo de chaves para incluir na resposta|
|`Prefix`|Restringe a resposta a chaves que iniciam com o prefixo especificado|
|`StartAfter`|Configura onde iniciar a listagem de objetos com base na chave|

### Usando Java
{: #dev-guide-pagination-java}

O {{site.data.keyword.cos_full}} SDK for Java fornece o método [`listObjectsV2`](https://ibm.github.io/ibm-cos-sdk-java/com/ibm/cloud/objectstorage/services/s3/AmazonS3.html#listObjectsV2-com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request-){:new_window} que permite retornar a listagem de objetos no tamanho desejado. Há um exemplo de código completo disponível [aqui](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#list-objects-v2).

### Usando Python
{: #dev-guide-pagination-python}

O {{site.data.keyword.cos_full}} SDK for Python fornece o método [`list_objects_v2`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window} que permite retornar a listagem de objetos no tamanho desejado. Há um exemplo de código completo disponível [aqui](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#list-objects-v2).
