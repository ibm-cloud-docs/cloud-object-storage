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

# Velero 통합
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window}(이전 이름은 Heptio Ark)는 S3 API를 사용하여 Kubernetes 클러스터를 백업하고 복원하는 도구 세트입니다. 

Velero는 두 부분으로 구성되어 있습니다. 

* 클러스터에서 실행되는 서버 컴포넌트
* 로컬 클라이언트에서 실행되는 명령행 인터페이스

## 전제조건
{: #velero-prereqs}

시작하기 전에 다음 항목을 갖춰야 합니다. 

* [`IBM Cloud CLI`](/docs/cli?topic=cloud-cli-getting-started){:new_window}가 설치됨
* [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} 명령행 도구가 설치되어 클러스터에 연결하도록 구성됨
* {{site.data.keyword.cos_short}} 인스턴스
* {{site.data.keyword.cos_short}} 버킷
* 버킷에 대한 Writer 액세스 권한이 있는 HMAC 인증 정보

## Velero 클라이언트 설치
{: #velero-install}

1. 자신의 OS에 맞는 최신 Velero [버전](https://github.com/heptio/velero/releases){:new_window}을 다운로드하십시오. 
2. .tar 파일의 압축을 로컬 시스템에 있는 폴더에 푸십시오. 
3. `velero` 바이너리를 실행할 수 있는지 확인하십시오. 

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

Usage:
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

*(선택사항)* Mac OS 또는 Linux의 경우 ark 바이너리를 임시 폴더에서 `/usr/local/bin`과 같은 영구 위치로 이동하십시오.
{: tip}

## Velero 서버 설치 및 구성
{: #velero-config}
### 인증 정보 파일 작성
{: #velero-config-credentials}

로컬 Velero 폴더(*.tar 파일의 압축을 푼 폴더*)에 HMAC 키를 포함하는 인증 정보 파일(`credentials-velero`)을 작성하십시오. 

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### kubectl 구성
{: #velero-config-kubectl}

클러스터에 연결하도록 [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window}을 구성하십시오. 

1. CLI를 사용하여 IBM Cloud 플랫폼에 로그인하십시오. <br/><br/>*보안을 강화하기 위해 API 키를 파일에 저장하거나 환경 변수로 설정할 수도 있습니다. *
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. 클러스터 구성을 검색하십시오.
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. **export** 명령을 복사하고 붙여넣어 KUBECONFIG 환경 변수를 설정하십시오. 

4. 다음 명령을 실행하여 `kubectl`이 올바르게 구성되었는지 확인하십시오.
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

### Velero 서버 및 클라우드 스토리지 구성
{: #velero-config-storage}

1. Velero 폴더에서 다음 명령을 실행하여 네임스페이스, RBAC 및 기타 스캐폴딩을 설정하십시오. <br/><br/>*기본 네임스페이스는 `velero`입니다. 사용자 정의 네임스페이스를 작성하려는 경우에는 ['Run in custom namespace'](https://heptio.github.io/velero/master/namespace.html){:new_window}*의 지시사항을 참조하십시오.
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
2. 인증 정보 파일을 사용하여 시크릿을 작성하십시오.
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. `config/ibm/05-ark-backupstoragelocation.yaml`에 다음 값을 지정하십시오. 
   * `<YOUR_BUCKET>` - 백업 파일 저장을 위한 버킷의 이름입니다. 
   * `<YOUR_REGION>` - 버킷의 [위치 제한조건](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint)(`us-standard`)입니다. 
   * `<YOUR_URL_ACCESS_POINT>` - 지역 엔드포인트 URL입니다(`https://s3.us.cloud-object-storage.appdomain.cloud`). 엔드포인트에 대한 자세한 정보는 [엔드포인트 및 스토리지 위치](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)를 참조하십시오. 

    *자세한 정보는 [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window} 정의를 참조하십시오. *

### Velero 서버 시작
{: #velero-config-server}

1. Velero 폴더에서 다음 명령을 실행하여 클러스터에 오브젝트를 작성하십시오.
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. 다음 명령을 실행하여 배치를 작성하십시오.
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. `velero` 네임스페이스에 대해 `kubectl get`을 사용하여 배치가 성공적으로 스케줄되었는지 확인하십시오. `Available`이 `1`이면 Ark가 준비된 것입니다.
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## 백업 및 복원 테스트
{: #velero-test}

### 백업
{: #velero-test-backup}

이제 다음 명령을 실행하여 Kubernetes 클러스터의 간단한 백업을 작성할 수 있습니다. 
```bash
velero backup create <backup-name>
```
{: pre}

이 명령은 지속적 볼륨을 비롯하여 클러스터에 있는 모든 리소스의 백업을 작성합니다. 

백업을 특정 네임스페이스, 리소스 유형 또는 레이블로 제한할 수도 있습니다. 

Velero는 이름을 통한 선택을 허용하지 않으며, 레이블 사용만을 허용합니다.
{: tip}

이 명령은 `app=<app-label>`로 레이블 지정된 컴포넌트만 백업합니다.  
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

다음 명령을 실행하면 옵션의 전체 목록을 볼 수 있습니다. 
```bash
velero backup --help
```
{: pre}

### 복원
{: #velero-test-restore}

백업을 복원하려면 다음 명령을 실행하십시오. 
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

다음 명령을 실행하면 옵션의 전체 목록을 볼 수 있습니다. 
```bash
velero restore --help
```
{: pre}

