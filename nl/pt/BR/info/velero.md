---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: heptio, kubernetes, backup

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

# Integração do Velero
{: #velero}
O [Velero](https://github.com/heptio/velero){:new_window} (anteriormente Heptio Ark) é um conjunto de ferramentas para fazer backup e restaurar clusters Kubernetes usando a API S3.

A Velero consiste em duas partes:

* Componente do servidor que é executado no cluster
* Interface da linha de comandos que é executada em um cliente local

## Pré-requisitos
{: #velero-prereqs}

Antes de iniciar, é necessária a configuração a seguir:

* A [`CLI do IBM Cloud`](/docs/cli?topic=cloud-cli-getting-started){:new_window} instalada
* A ferramenta de linha de comandos [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} que está instalada e configurada para se conectar ao seu cluster
* Uma instância do {{site.data.keyword.cos_short}}
* Um depósito do {{site.data.keyword.cos_short}}
* Credenciais HMAC com acesso de Gravador ao depósito

## Instalar o cliente Velero
{: #velero-install}

1. Faça download da [versão](https://github.com/heptio/velero/releases){:new_window} mais recente do Velero para seu S.O.
2. Extraia o arquivo .tar em uma pasta em seu sistema local
3. Verifique se é possível executar o binário `velero`:

```
velero --help
```

```
Velero is a tool for managing disaster recovery, specifically for Kubernetes
cluster resources. It provides a simple, configurable, and operationally robust
way to back up your application state and associated data.

If you're familiar with kubectl, Velero supports a similar model, allowing you to
execute commands such as 'velero get backup' and 'velero create schedule'. The same
operations can also be performed as 'velero backup get' and 'velero schedule create'.

Uso:
  velero [command]

Available Commands:
  backup            Work with backups
  backup-location   Work with backup storage locations
  bug               Report an velero bug
  client            Velero client related commands
  completion        Output shell completion code for the specified shell (bash or zsh)
  create            Create velero resources
  delete            Delete velero resources
  describe          Describe velero resources
  get               Get velero resources
  help              Help about any command
  plugin            Work with plugins
  restic            Work with restic
  restore           Work with restores
  schedule          Work with schedules
  server            Run the velero server
  snapshot-location Work with snapshot locations
  version           Print the velero version and associated image
...
```

*(OPCIONAL)* Mova o binário ark para fora da pasta temporária para um lugar mais permanente, como `/usr/local/bin`, no Mac OS ou Linux.
{: tip}

## Instalar e configurar o servidor Velero
{: #velero-config}
### Criar arquivo de credenciais
{: #velero-config-credentials}

Crie um arquivo de credenciais (`credentials-velero`) com as chaves HMAC em sua pasta do Velero local (*a pasta em que o arquivo .tar foi extraído*)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configurar kubectl
{: #velero-config-kubectl}

Configure [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} para se conectar ao seu cluster.

1. Efetue login no IBM Cloud Platform usando a CLI.<br/><br/>*Para aumentar a segurança, também é possível armazenar a chave de API em um arquivo ou configurá-la como uma variável de ambiente.*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. Recupere a configuração de cluster
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. Copie e cole o comando **export** para configurar a variável de ambiente KUBECONFIG

4. Assegure-se de que o `kubectl` esteja configurado corretamente executando:
    ```bash
    kubectl cluster-config
    ```
    {: pre}
  
    ```bash
    Kubernetes master is running at https://c6.hou02.containers.cloud.ibm.com:29244
    Heapster is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/heapster/proxy
    KubeDNS is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
    kubernetes-dashboard is running at https://c6.hou02.containers.cloud.ibm.com:29244/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy
    ```
    {: pre}

### Configurar o servidor Velero e o Cloud Storage
{: #velero-config-storage}

1. Na pasta do Velero, execute o seguinte para configurar namespaces, RBAC e outros andaimes<br/><br/>*O namespace padrão é `velero`. Se você deseja criar um namespace customizado, consulte as instruções em ['executar no namespace customizado'](https://heptio.github.io/velero/master/namespace.html){:new_window}*
    ```bash
    kubectl apply -f config/common/00-prereqs.yaml
    ```
    {: pre}

    ```bash
    customresourcedefinition.apiextensions.k8s.io/backups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/schedules.velero.io created
    customresourcedefinition.apiextensions.k8s.io/restores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/downloadrequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/deletebackuprequests.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumebackups.velero.io created
    customresourcedefinition.apiextensions.k8s.io/podvolumerestores.velero.io created
    customresourcedefinition.apiextensions.k8s.io/resticrepositories.velero.io created
    customresourcedefinition.apiextensions.k8s.io/backupstoragelocations.velero.io created
    customresourcedefinition.apiextensions.k8s.io/volumesnapshotlocations.velero.io created
    namespace/velero created
    serviceaccount/velero created
    clusterrolebinding.rbac.authorization.k8s.io/velero created
    ```
    {: pre}
2. Crie um Segredo com seu arquivo de credenciais
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Especifique os valores a seguir em `config/ibm/05-ark-backupstoragelocation.yaml`:
   * `<YOUR_BUCKET>` - o nome do depósito para armazenar arquivos de backup
   * `<YOUR_REGION>` - a [restrição de local](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) de seu depósito (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - a URL de terminal regional (`https://s3.us.cloud-object-storage.appdomain.cloud`). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

    *Para obter mais informações, consulte a definição [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window} para obter informações adicionais.*

### Iniciar o servidor Velero
{: #velero-config-server}

1. Na pasta do Velero, execute o comando a seguir para criar o objeto em seu cluster:
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. Execute o comando a seguir para criar a implementação:
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Assegure-se de que a implementação seja planejada com êxito usando `kubectl get` no namespace `velero`. Quando `Available` lê `1`, o Ark está pronto para continuar:
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Testando backup e restauração
{: #velero-test}

### Backup
{: #velero-test-backup}

Agora é possível criar um backup simples de seu cluster Kubernetes executando o comando a seguir:
```bash
velero backup create <backup-name>
```
{: pre}

Esse comando cria um backup para cada recurso no cluster, incluindo volumes persistentes.

Também é possível restringir o backup para um namespace, tipo de recurso ou rótulo específico.

O Velero não permite seleção por nome, apenas por rótulos.
{: tip}

Esse comando faz backup somente dos componentes que são rotulados com `app=<app-label>`. 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

Uma lista integral de opções está disponível executando:
```bash
velero backup --help
```
{: pre}

### Restaurar
{: #velero-test-restore}

Para restaurar um backup, execute o comando a seguir:
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

Uma lista integral de opções está disponível executando:
```bash
velero restore --help
```
{: pre}

