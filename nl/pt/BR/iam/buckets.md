---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# Permissões de depósito
{: #iam-bucket-permissions}

Designe funções de acesso para usuários e IDs de serviço com relação a depósitos, usando a UI ou a CLI para criar políticas.

| Função de acesso | Ações de exemplo                                             |
|:------------|-------------------------------------------------------------|
| Gerente     | Tornar objetos públicos, criar e destruir depósitos e objetos |
| Gravador      | Criar e destruir depósitos e objetos                      |
| Reader      | Listar e fazer download de objetos                                   |
| ContentReader      | Fazer download de objetos                          |

## Concedendo acesso a um usuário
{: #iam-user-access}

Se o usuário precisa ser capaz de usar o console, é necessário **também** conceder a ele uma função de acesso de plataforma mínima de `Viewer` na instância em si, além da função de acesso ao serviço (como `Reader`). Isso permitirá que ele visualize todos os depósitos e liste os objetos dentro deles. Em seguida, selecione **Permissões de depósito** no menu de navegação esquerdo, selecione o usuário e selecione o nível de acesso (`Manager` ou `Writer`) que ele requer.

Se o usuário interagir com dados usando a API e não requerer acesso ao console _e_ ele for um membro de sua conta, será possível conceder acesso a um único depósito sem nenhum acesso à instância pai.

## Execução de política
{: #iam-policy-enforcement}

As políticas do IAM são cumpridas hierarquicamente do maior nível de acesso ao mais restrito. Os conflitos são resolvidos para a política mais permissiva. Por exemplo, se um usuário tiver ambas as funções de acesso ao serviço, `Writer` e `Reader`, em um depósito, a política que conceder a função `Reader` será ignorada.

Isso também é aplicável às políticas de instância de serviço e de nível do depósito.

- Se um usuário tiver uma política que conceda a função `Writer` em uma instância de serviço e a função `Reader` em um único depósito, a política de nível de depósito será ignorada.
- Se um usuário tiver uma política que conceda a função `Reader` em uma instância de serviço e a função `Writer` em um único depósito, ambas as políticas serão cumpridas e a função `Writer` mais permissiva terá precedência para o depósito individual.

Se for necessário restringir o acesso a um único depósito (ou conjunto de depósitos), assegure-se de que o ID do usuário ou do serviço não tenha políticas de nível de instância usando o console ou a CLI.

### Usando a UI
{: #iam-policy-enforcement-console}

Para criar uma nova política de nível do depósito: 

  1. Navegue para o console de **Acesso IAM** no menu **Gerenciar**.
  2. Selecione **Usuários** no menu de navegação esquerdo.
  3. Selecione um usuário.
  4. Selecione a guia **Acessar políticas** para visualizar as políticas existentes do usuário, designar uma nova política ou editar uma política existente.
  5. Clique em **Designar acesso** para criar uma nova política.
  6. Escolha **Designar acesso a recursos**.
  7. Primeiro selecione **Cloud Object Storage** no menu de serviços.
  8. Em seguida, selecione a instância de serviço apropriada. Insira `bucket` no campo **Tipo de recurso** e o nome do depósito no campo **ID do recurso**.
  9. Selecione a função de acesso ao serviço desejada.
  10.  Clique em **Designar**

Observe que deixar os campos **Tipo de recurso** ou **Recurso** em branco criará uma política de nível de instância.
{:tip}

### Utilizando o CLI
{: #iam-policy-enforcement-cli}

Em um terminal, execute o comando a seguir:

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Para listar políticas existentes:

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

Para editar uma política existente:

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## Concedendo acesso a um ID de serviço
{: #iam-service-id}
Se for necessário conceder acesso a um depósito para um aplicativo ou outra entidade não humana, use um ID de serviço. O ID de serviço pode ser criado especificamente para esse propósito ou pode ser um ID de serviço existente já em uso.

### Usando a UI
{: #iam-service-id-console}

  1. Navegue para o console de **Acesso (IAM)** no menu **Gerenciar**.
  2. Selecione **IDs de serviço** no menu de navegação esquerdo.
  3. Selecione um ID de serviço para visualizar quaisquer políticas existentes e designe uma nova política ou edite uma política existente.
  3. Selecione a instância de serviço, o ID de serviço e a função desejada.
  4. Insira `bucket` no campo **Tipo de recurso** e o nome do depósito no campo **Recurso**.
  5. Clique em **Enviar**

  Observe que deixar os campos **Tipo de recurso** ou **Recurso** em branco criará uma política de nível de instância.
  {:tip}

### Utilizando o CLI
{: #iam-service-id-cli}

Em um terminal, execute o comando a seguir:

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Para listar políticas existentes:

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

Para editar uma política existente:

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
