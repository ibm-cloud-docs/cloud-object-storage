---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: endpoints, legacy, access points, manual failover

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

# Informações adicionais sobre terminais
{: #advanced-endpoints}

A resiliência de um depósito é definida pelo terminal usado para criá-la. A resiliência _Região cruzada_ difundirá seus dados em várias áreas metropolitanas, enquanto a resiliência _Regional_ difundirá dados em uma única área metropolitana. A resiliência de _Data center único_ difunde dados em múltiplos dispositivos dentro de um único data center. Os depósitos Regionais e de Região cruzada podem manter a disponibilidade durante uma indisponibilidade do site.

As cargas de trabalho de cálculo localizadas concomitantemente com um terminal Regional do {{site.data.keyword.cos_short}} verão uma latência inferior e um desempenho melhor. Para cargas de trabalho não concentradas em uma única área geográfica, um terminal de Região cruzada `geo` roteia conexões para os data centers regionais mais próximos.

Ao usar um terminal de Região cruzada, é possível direcionar o tráfego de entrada para um ponto de acesso específico enquanto ainda distribui dados entre as três regiões. Ao enviar solicitações para um ponto de acesso individual, não haverá failover automatizado se essa região se tornar indisponível. Os aplicativos que direcionam o tráfego para um ponto de acesso em vez do terminal `geo` **devem** implementar a lógica de failover apropriada internamente para alcançar as vantagens de disponibilidade do armazenamento de região cruzada.
{:tip}

Algumas cargas de trabalho podem se beneficiar do uso de um terminal de Data center único. Os dados armazenados em um único site ainda são distribuídos em muitos dispositivos de armazenamento físico, mas estão contidos em um data center único. Isso pode melhorar o desempenho dos recursos de cálculo dentro do mesmo site, mas não manterá a disponibilidade no caso de uma indisponibilidade do site. Os depósitos do Data center único não fornecem replicação automatizada ou backup no caso de destruição do site, portanto, quaisquer aplicativos que usam um único site devem considerar a recuperação de desastre em seu design.

Todas as solicitações devem usar SSL ao usar o IAM e o serviço rejeitará quaisquer solicitações de texto sem formatação.

Tipos de terminal:

Os serviços do {{site.data.keyword.cloud}} são conectados a uma rede de três camadas, segmentando o tráfego público, privado e de gerenciamento.

* Os **Terminais privados** estão disponíveis para solicitações originadas de clusters Kubernetes, servidores bare metal, servidores virtuais e outros serviços de armazenamento em nuvem. Os terminais privados fornecem melhor desempenho e não incorrem em encargos para nenhuma largura da banda de saída ou recebida, mesmo se o tráfego é de regiões cruzadas ou em data centers. **Sempre que possível, é melhor usar um terminal privado.**
* Os **Terminais públicos** podem aceitar solicitações de qualquer lugar e os encargos são avaliados na largura da banda de saída. A largura da banda recebida é grátis. Os terminais públicos devem ser usados para acesso não originado de um recurso de computação em nuvem do {{site.data.keyword.cloud_notm}}. 

As solicitações devem ser enviadas para o terminal associado ao local de um determinado depósito. Se você não tem certeza de onde um depósito está localizado, há uma [extensão para a API de listagem de depósito](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) que retorna as informações de local e de classe de armazenamento para todos os depósitos em uma instância de serviço.

A partir de dezembro de 2018, nós atualizamos nossos terminais. Os terminais anteriores continuarão a funcionar até novo aviso. Atualize seus aplicativos para usar os [novos terminais](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints).
{:note}
