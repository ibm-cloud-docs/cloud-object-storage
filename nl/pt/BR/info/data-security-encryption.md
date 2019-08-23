---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security

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

# Segurança e criptografia de dados
{: #security}

O {{site.data.keyword.cos_full}} usa uma abordagem inovadora para armazenar com custo reduzido grandes volumes de dados não estruturados enquanto garante segurança, disponibilidade e confiabilidade. Isso é realizado usando o Information Dispersal Algorithms (IDAs) para separar dados em “fatias” não reconhecíveis que são distribuídas em uma rede de data centers, tornando a transmissão e o armazenamento de dados inerentemente privados e seguros. Nenhuma cópia completa dos dados reside em qualquer nó de armazenamento único e somente um subconjunto de nós precisa estar disponível para recuperar totalmente os dados na rede.

Todos os dados no {{site.data.keyword.cos_full_notm}} são criptografados em repouso. Essa tecnologia criptografa individualmente cada objeto usando chaves geradas por objeto. Essas chaves são protegidas e armazenadas de forma confiável usando os mesmos Information Dispersal Algorithms que protegem os dados do objeto usando um All-or-Nothing Transform (AONT), que evita que os dados-chave sejam divulgados se os nós individuais ou unidades de disco rígido estiverem comprometidos.

Se for necessário que o usuário controle chaves de criptografia, as chaves raiz poderão ser fornecidas em uma [base por objeto usando SSE-C](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-sse-c) ou uma [base por depósito usando SSE-KP](/docs/services/cloud-object-storage?topic=cloud-object-storage-encryption#encryption-kp).

O armazenamento pode ser acessado por meio de HTTPS e os dispositivos de armazenamento interno são certificados e se comunicam entre si usando TLS.


## Exclusão de dados
{: #security-deletion}

Após os dados serem excluídos, existem vários mecanismos que impedem a recuperação ou a reconstrução dos objetos excluídos. A exclusão de um objeto passa por vários estágios, da marcação dos metadados indicando o objeto como excluído à remoção das regiões de conteúdo à finalização do apagamento nas próprias unidades até a eventual sobrescrição dos blocos que representam os dados de fatia. Dependendo de alguém ter comprometido o data center ou ter a posse dos discos físicos, o tempo em que um objeto se torna irrecuperável depende da fase da operação de exclusão. Quando o objeto de metadados é atualizado, os clientes externos da rede do data center não podem mais ler o objeto. Quando uma maioria de fatias representando as regiões de conteúdo tiver sido finalizada pelos dispositivos de armazenamento, não será possível acessar o objeto.

## Isolamento do locatário
{: #security-isolation}

O {{site.data.keyword.cos_full_notm}} é uma solução de armazenamento de objeto de diversos locatários com infraestrutura compartilhada. Se sua carga de trabalho requerer armazenamento dedicado ou isolado, visite [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage) para obter mais informações.
