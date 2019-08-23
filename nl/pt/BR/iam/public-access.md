---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Permitindo acesso público
{: #iam-public-access}

Às vezes, os dados são destinados a serem compartilhados. Os depósitos podem reter conjuntos de dados abertos para pesquisa acadêmica e privada ou repositórios de imagem que são usados por aplicativos da web e redes de entrega de conteúdo. Torne esses depósitos acessíveis usando o grupo **Acesso público**.
{: shortdesc}

## Usando o console para configurar o acesso público
{: #iam-public-access-console}

Primeiro, certifique-se de que você tenha um depósito. Caso contrário, siga o [tutorial de introdução](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para se familiarizar com o console.

### Ativar o acesso público
{: #public-access-console-enable}

1. No [painel do console](https://cloud.ibm.com/) do {{site.data.keyword.cloud_notm}}, selecione **Armazenamento** para visualizar sua lista de recursos.
2. Em seguida, selecione a instância de serviço com seu depósito no menu **Armazenamento**. Isso leva você para o {site.data.keyword.cos_short}}.
3. Escolha o depósito que você deseja que seja publicamente acessível. Tenha em mente que essa política torna _todos os objetos em um depósito_ disponíveis para download de qualquer pessoa com a URL apropriada.
4. Selecione **Políticas de acesso** no menu de navegação.
5. Selecione a guia **Acesso público**.
6. Clique em **Criar política de acesso**. Depois de ler o aviso, escolha **Ativar**.
7. Agora todos os objetos nesse depósito são publicamente acessíveis!

### Desativar o acesso público
{: #public-access-console-disable}

1. De qualquer lugar no [console](https://cloud.ibm.com/) do {{site.data.keyword.cloud_notm}}, selecione o menu **Gerenciar** e o **Acesso (IAM)**.
2. Selecione **Grupos de acesso** no menu de navegação.
3. Selecione **Acesso público** para ver uma lista de todas as políticas de acesso público atualmente em uso.
4. Localize a política que corresponde ao depósito que você deseja retornar ao controle de acesso cumprido.
5. Na lista de ações na extrema direita da entrada de política, escolha **Remover**.
6. Confirme a caixa de diálogo e a política é agora removida do depósito.

## Permitindo acesso público em objetos individuais
{: #public-access-object}

Para tornar um objeto publicamente acessível por meio da API de REST, um cabeçalho `x-amz-acl: public-read` pode ser incluído na solicitação. A configuração desse cabeçalho efetua bypass de qualquer verificação de [política do IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) e permite as solicitações `HEAD` e `GET` não autenticadas. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Além disso, as [credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) tornam possível permitir o [acesso público temporário que usa URLs pré-assinadas](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url).

### Fazer upload de um objeto público
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### Permitir acesso público a um objeto existente
{: #public-access-object-existing}

Usando o parâmetro de consulta `?acl` sem uma carga útil e o cabeçalho `x-amz-acl: public-read` permite acesso público ao objeto sem a necessidade de sobrescrever os dados.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### Tornar um objeto público privado novamente
{: #public-access-object-private}

Usar o parâmetro de consulta `?acl` sem uma carga útil e um cabeçalho vazio `x-amz-acl:` revoga o acesso público ao objeto sem precisar sobrescrever os dados.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## Websites estáticos
{: #public-access-static-website}

Embora o {{site.data.keyword.cos_full_notm}} não suporte hospedagem automática de website estático, é possível configurar manualmente um servidor da web e usá-lo para entregar conteúdo publicamente acessível hospedado em um depósito. Para obter mais informações, consulte [este tutorial](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).
