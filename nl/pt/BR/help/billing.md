---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# Faturamento
{: #billing}

As informações sobre precificação podem ser localizadas em [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}.

## faturas
{: #billing-invoices}

Localize suas faturas de conta em **Gerenciar** > **Faturamento e uso** no menu de navegação.

Cada conta recebe uma única conta. Se você precisar de faturamento separado para conjuntos diferentes de contêineres, a criação de múltiplas contas será necessária.

## Precificação do {{site.data.keyword.cos_full_notm}}
{: #billing-pricing}

Os custos de armazenamento para o {{site.data.keyword.cos_full}} são determinados pelo volume total de dados que são armazenados, a quantia de largura da banda de saída pública usada e o número total de solicitações operacionais processadas pelo sistema.

As ofertas de infraestrutura são conectadas a uma rede de três camadas, segmentando o tráfego público, privado e de gerenciamento. Os serviços de infraestrutura podem transferir dados entre um e outro na rede privada sem nenhum custo. As ofertas de infraestrutura (como servidores bare metal, servidores virtuais e armazenamento em nuvem) se conectam a outros aplicativos e serviços no catálogo do {{site.data.keyword.cloud_notm}} Platform (como serviços do Watson e tempos de execução do Cloud Foundry) em toda a rede pública, portanto, a transferência de dados entre esses dois tipos de ofertas é medida e cobrada em taxas de largura da banda de rede pública padrão.
{: tip}

## Classes de solicitação
{: #billing-request-classes}

As solicitações de 'Classe A' envolvem modificação ou listagem. Essa categoria inclui criar depósitos, fazer upload ou copiar objetos, criar ou mudar configurações, listar depósitos e listar o conteúdo de depósitos.

As solicitações de 'Classe B' estão relacionadas à recuperação de objetos ou a seus metadados associados ou configurações do sistema.

A exclusão de depósitos ou objetos do sistema não incorre em um encargo.

| Classe | Solicitações | Exemplos |
|--- |--- |--- |
| Classe A | Solicitações de PUT, COPY e POST, assim como solicitações de GET usadas para listar depósitos e objetos | Criando depósitos, fazendo upload ou copiando objetos, listando depósitos, listando conteúdo de depósitos, configurando ACLs e definindo configurações de CORS |
| Classe B | Solicitações de GET (excluindo listagem), HEAD e OPTIONS | Recuperando objetos e metadados |

## Transferências do Aspera
{: #billing-aspera}

O [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) incorre em encargos extras de egresso. Para obter mais informações, consulte a [página de precificação](https://www.ibm.com/cloud/object-storage#s3api).

## Classes de armazenamento
{: #billing-storage-classes}

Nem todos os dados que estão armazenados precisam ser acessados frequentemente e alguns dados de arquivamento podem ser raramente acessados, se é que isso acontece. Para cargas de trabalho menos ativas, os depósitos podem ser criados em uma classe de armazenamento diferente e os objetos que são armazenados nesses depósitos incorrem em encargos em um planejamento diferente do armazenamento padrão.

Há quatro classes:

*  **Standard** é usado para cargas de trabalho ativas, sem encargos para dados recuperados (exceto o custo da solicitação operacional em si).
*  **Vault** é usado para cargas de trabalho frias em que os dados são acessados menos de uma vez por mês - um encargo extra de recuperação (US$/GB) é aplicado cada vez que os dados são lidos. O serviço inclui um limite mínimo para o tamanho do objeto e o período de armazenamento consistentes com o uso desejado desse serviço para dados mais frios, menos ativos.
*  **Cold Vault** é usado para cargas de trabalho frias nos quais os dados são acessados a cada 90 dias ou menos - um encargo extra de recuperação maior (US$/GB) é aplicado cada vez que os dados são lidos. O serviço inclui um limite mínimo maior para o tamanho do objeto e o período de armazenamento consistentes com o uso desejado desse serviço para dados frios e inativos.
*  **Flex** é usado para cargas de trabalho dinâmicas em que os padrões de acesso são mais difíceis de prever. Dependendo do uso, se os custos de encargos de recuperação excederem um valor máximo, os encargos de recuperação serão descartados e um novo encargo de capacidade será aplicado. Se os dados não forem acessados frequentemente, ele será mais eficiente em termos de custo do que o armazenamento Standard e, se os padrões de uso de acesso se tornarem inesperadamente mais ativos, ele será mais eficiente em custo do que o armazenamento Vault ou Cold Vault. O Flex não requer um tamanho de objeto ou período de armazenamento mínimo.

Para obter mais informações sobre precificação, consulte [a tabela de precificação em ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Para obter mais informações sobre a criação de depósitos com diferentes classes de armazenamento, consulte a [Referência da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).
