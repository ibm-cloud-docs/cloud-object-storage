---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: administrator, storage, iam, access

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

# Para administradores
{: #administrators}

Os administradores de armazenamento e de sistema que precisam configurar o armazenamento de objeto e gerenciar o acesso aos dados podem tirar vantagem do IBM Cloud Identity and Access Management (IAM) para gerenciar usuários, criar e fazer a rotação de chaves de API e conceder funções a usuários e serviços. Se você ainda não tiver feito isso, prossiga e leia o [tutorial de introdução](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para familiarizar-se com os conceitos principais de depósitos, objetos e usuários.

## Configurar seu armazenamento
{: #administrators-setup}

Primeiro que tudo, é necessário ter pelo menos uma instância de recurso de armazenamento de objeto e alguns depósitos nos quais armazenar dados. Pense nesses depósitos em termos de como você deseja segmentar mais o acesso a seus dados, onde você deseja que seus dados residam fisicamente e com que frequência os dados serão acessados.

### Segmentando o acesso
{: #administrators-access}

Há dois níveis nos quais é possível segmentar o acesso: no nível da instância de recurso e no nível do depósito. 

Talvez você deseje se certificar de que uma equipe de desenvolvimento possa acessar somente as instâncias do armazenamento de objeto com as quais ela está trabalhando e não aquelas usadas por outras equipes. Ou você deseja assegurar que somente o software que sua equipe está fazendo possa realmente editar os dados que estão sendo armazenados, portanto, deseja que seus desenvolvedores com acesso à plataforma de nuvem apenas possam ler dados por razões de resolução de problemas. Estes são exemplos de políticas de nível de serviço.

Agora, se a equipe de desenvolvimento ou qualquer usuário individual tem acesso de visualizador a uma instância de armazenamento, mas deve ser capaz de editar dados diretamente em um ou mais depósitos, é possível usar políticas de nível do depósito para elevar o nível de acesso concedido a usuários dentro de sua conta. Por exemplo, um usuário pode não ser capaz de criar novos depósitos, mas pode criar e excluir objetos dentro de depósitos existentes.

## Gerenciar acesso
{: #administrators-manage-access}

O IAM é baseado em um conceito fundamental: um _assunto_ é concedido a uma _função_ em um _recurso_.

Há dois tipos básicos de assuntos: um _usuário_ e um _ID de serviço_.

Há outro conceito, uma _credencial de serviço_. Uma credencial de serviço é uma coleção de informações importantes necessárias para se conectar a uma instância do {{site.data.keyword.cos_full}}. Isso inclui, no mínimo, um identificador para a instância do {{site.data.keyword.cos_full_notm}} (ou seja, o ID da instância de recurso), os terminais de serviço/aut. e um meio de associar o assunto a uma chave de API (ou seja, ID de serviço). Ao criar a credencial de serviço, você tem a opção de associá-la a um ID de serviço existente ou de criar um novo ID de serviço.

Portanto, se você desejar permitir que sua equipe de desenvolvimento possa usar o console para visualizar instâncias de armazenamento de objeto e clusters Kubernetes, ela precisará das funções `Viewer` nos recursos de armazenamento de objeto e nas funções `Administrator` no Container Service. Observe que a função `Viewer` permite ao usuário somente ver que a instância existe e visualizar as credenciais existentes, **não** visualizar depósitos e objetos. Quando as credenciais de serviço foram criadas, elas foram associadas a um ID de serviço. Esse ID de serviço precisaria ter a função `Manager` ou `Writer` na instância para poder criar e destruir depósitos e objetos.

Para obter mais informações sobre funções e permissões do IAM, consulte [a visão geral do IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview).
