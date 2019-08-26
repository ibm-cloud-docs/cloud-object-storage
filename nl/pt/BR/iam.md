---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics

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

# Introdução ao IAM
{: #iam}

## Funções e ações do Identity and Access Management
{: #iam-roles}

O acesso às instâncias de serviço do {{site.data.keyword.cos_full}} para usuários em sua conta é controlado pelo {{site.data.keyword.Bluemix_notm}} Identity and Access Management (IAM). A todo usuário que acessa o serviço {{site.data.keyword.cos_full}} em sua conta deve ser designada uma política de acesso com uma função de usuário do IAM definida. Essa política determina quais ações o usuário pode executar dentro do contexto do serviço ou instância que você seleciona. As ações permitidas são customizadas e definidas pelo serviço {{site.data.keyword.Bluemix_notm}} como operações que têm permissão para serem executadas no serviço. As ações são, então, mapeadas para funções de usuário do IAM.

As políticas permitem que o acesso seja concedido em diferentes níveis. Algumas das opções incluem o seguinte: 

* Acesso em todas as instâncias do serviço em sua conta
* Acesso a uma instância de serviço individual em sua conta
* Acesso a um recurso específico dentro de uma instância
* Acesso a todos os serviços ativados para o IAM em sua conta

Depois de definir o escopo da política de acesso, você designa uma função. Revise as tabelas a seguir que descrevem quais ações cada função permite dentro do serviço {{site.data.keyword.cos_short}}.

A tabela a seguir detalha as ações que são mapeadas para funções de gerenciamento de plataforma. As funções de gerenciamento de plataforma permitem que os usuários executem tarefas em recursos de serviço no nível de plataforma, por exemplo, designar acesso de usuário para o serviço, criar ou excluir IDs de serviço, criar instâncias e ligar instâncias aos aplicativos.

| Função de gerenciamento de plataforma | Descrição das ações | Exemplo de ações|
|:-----------------|:-----------------|:-----------------|
| Visualizador | Visualizar instâncias de serviço, mas não as modificar | <ul><li>Listar instâncias de serviço do COS disponíveis</li><li>Visualizar detalhes do plano de serviço do COS</li><li>Visualizar detalhes de uso</li></ul>|
| Aplicativos | Execute todas as ações da plataforma, exceto gerenciar as contas e designar políticas de acesso |<ul><li>Criar e excluir instâncias de serviço do COS</li></ul> |
| Operador | Não usado pelo COS | Nenhum |
| Administrador | Executar todas as ações da plataforma com base no recurso para o qual essa função está sendo designada, incluindo a designação de políticas de acesso a outros usuários |<ul><li>Atualizar políticas de usuário</li>Atualizar planos de precificação</ul>|
{: caption="Tabela 1. Funções e ações do usuário do IAM"}


A tabela a seguir detalha as ações que são mapeadas para as funções de acesso de serviço. As funções de acesso de serviço permitem que os usuários acessem o {{site.data.keyword.cos_short}}, bem como a capacidade de chamar a API do {{site.data.keyword.cos_short}}.

| Função de acesso de serviço | Descrição das ações                                                                                                                                       | Ações de exemplo                                                                     |
|:--------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------|
| Leitor de conteúdo  | Fazer download de objetos, mas não listar objetos ou depósitos | <ul><li>Fazer download de objetos</li></ul> |
| Reader              | Além das ações do Leitor de conteúdo, os Leitores podem listar depósitos e/ou objetos, mas não os modificar. | <ul><li>Listar depósitos</li><li>Listar e fazer download de objetos</li></ul>                    |
| Gravador              | Além das ações do Leitor, os Gravadores podem criar depósitos e fazer upload de objetos. | <ul><li>Criar novos depósitos e objetos</li><li>Remover depósitos e objetos</li></ul> |
| Gerente             | Além das ações do Gravador, os Gerenciadores podem concluir as ações privilegiadas que afetam o controle de acesso. | <ul><li>Incluir uma política de retenção</li><li>Incluir um firewall do depósito</li></ul>              |
{: caption="Tabela 3. Funções e ações de acesso do serviço IAM"}


Para obter informações sobre como designar funções de usuário na IU, consulte [Gerenciando o acesso ao IAM](/docs/iam?topic=iam-iammanidaccser).
 
