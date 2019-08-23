---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

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

# Configurando um firewall
{: #setting-a-firewall}

As políticas do IAM fornecem uma maneira de os administradores limitarem o acesso a depósitos individuais. E se determinados dados devem ser acessados somente por meio de redes confiáveis? Um firewall de depósito restringe todo o acesso aos dados, a menos que a solicitação seja originada de uma lista de endereços IP permitidos.
{: shortdesc}

Há algumas regras ao redor da configuração de um firewall:

* Um usuário que configura ou visualiza um firewall deve ter a função `Manager` no depósito. 
* Um usuário com a função `Manager` no depósito pode visualizar e editar a lista de endereços IP permitidos de qualquer endereço IP para evitar bloqueios de acesso acidentais.
* O {{site.data.keyword.cos_short}} Console ainda pode acessar o depósito, desde que o endereço IP do usuário esteja autorizado.
* Outros serviços do {{site.data.keyword.cloud_notm}} **não são autorizados** a efetuar bypass do firewall. Essa limitação significa que outros serviços que dependem de políticas do IAM para acesso ao depósito (como o Aspera, o SQL Query, o Security Advisor, o Watson Studio, o Cloud Functions e outros) não serão capazes de fazer isso.

Quando um firewall é configurado, o depósito é isolado do restante do {{site.data.keyword.cloud_notm}}. Considere como isso pode afetar os aplicativos e os fluxos de trabalho que dependem de outros serviços acessando diretamente um depósito antes de ativar o firewall.
{: important}

## Usando o console para configurar um firewall
{: #firewall-console}

Primeiro, certifique-se de que você tenha um depósito. Caso contrário, siga o [tutorial de introdução](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para se familiarizar com o console.

### Configurar uma lista de endereços IP autorizados
{: #firewall-console-enable}

1. No [painel do console](https://cloud.ibm.com/) do {{site.data.keyword.cloud_notm}}, selecione **Armazenamento** para visualizar sua lista de recursos.
2. Em seguida, selecione a instância de serviço com seu depósito no menu **Armazenamento**. Isso leva você para o {{site.data.keyword.cos_short}} Console.
3. Escolha o depósito para o qual você deseja limitar o acesso a endereços IP autorizados. 
4. Selecione **Políticas de acesso** no menu de navegação.
5. Selecione a guia **IPs autorizados**.
6. Clique em **Incluir endereços IP**, em seguida, escolha **Incluir**.
7. Inclua uma lista de endereços IP em [Notação CIDR](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing), por exemplo, `192.168.0.0/16, fe80:021b::0/64`. Os endereços podem seguir os padrões IPv4 ou IPv6. Clique em ** Adicionar**.
8. O firewall não será cumprido até que o endereço seja salvo no console. Clique em **Salvar todos** para cumprir o firewall.
9. Agora, todos os objetos nesse depósito são acessíveis somente por meio desses endereços IP!

### Remover quaisquer restrições de endereço IP
{: #firewalls-console-disable}

1. Na guia **IPs autorizados**, marque as caixas ao lado de quaisquer endereços IP ou intervalos para remover da lista autorizada.
2. Selecione **Excluir** e, em seguida, confirme a caixa de diálogo clicando em **Excluir** novamente.
3. A lista atualizada não será cumprida até que as mudanças sejam salvas no console. Clique em **Salvar todos** para cumprir as novas regras.
4. Agora, todos os objetos nesse depósito são acessíveis somente por meio desses endereços IP!

Se não houver endereços IP autorizados listados, isso significa que as políticas normais do IAM se aplicarão ao depósito, sem restrições no endereço IP do usuário.
{: note}


## Configurar um firewall por meio de uma API
{: #firewall-api}

Os firewalls são gerenciados com a [API de Configuração de recursos do COS](https://cloud.ibm.com/apidocs/cos/cos-configuration). Essa nova API de REST é usada para configurar depósitos. 

Os usuários com a função `manager` podem visualizar e editar a lista de endereços IP permitidos de qualquer rede a fim de evitar bloqueios de acesso acidentais.
{: tip}
