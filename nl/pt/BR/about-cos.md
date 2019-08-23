---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, overview, cos

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


# Sobre {{site.data.keyword.cos_full_notm}}
{: #about-ibm-cloud-object-storage}

As informações armazenadas com o {{site.data.keyword.cos_full}} são criptografadas e dispersas em múltiplas localizações geográficas e acessadas por meio de HTTP usando uma API de REST. Esse serviço usa as tecnologias de armazenamento distribuído fornecidas pelo {{site.data.keyword.cos_full_notm}} System (anteriormente Cleversafe).

O {{site.data.keyword.cos_full_notm}} está disponível com três tipos de resiliência: Região cruzada, Regional e Data center único. A Região cruzada fornece durabilidade e disponibilidade mais altas do que usar uma única região a custo de uma latência um pouco mais alta e está disponível hoje nos EUA, na UE e na AP. O serviço Regional reverte essas trocas e distribui objetos em múltiplas zonas de disponibilidade em uma única região e está disponível nas regiões dos EUA, da UE e da AP. Se uma determinada região ou zona de disponibilidade estiver indisponível, o armazenamento de objeto continuará a funcionar sem impedimento. O Data center único distribui objetos em múltiplas máquinas dentro do mesmo local físico. Confira [aqui](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) as regiões disponíveis.

Os desenvolvedores usam uma API do {{site.data.keyword.cos_full_notm}} para interagir com seu armazenamento de objeto. Esta documentação fornece suporte para a [introdução](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) às contas de fornecimento para criar depósitos, fazer upload de objetos e usar uma referência de interações comuns de API.

## Outros serviços de armazenamento de objeto da IBM
{: #about-other-cos}
Além do {{site.data.keyword.cos_full_notm}}, o {{site.data.keyword.cloud_notm}} fornece atualmente várias ofertas de armazenamento de objeto adicionais para diferentes necessidades do usuário. Todas elas podem ser acessadas por meio de portais baseados na web e APIs de REST. [Saiba mais.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
