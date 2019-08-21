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

# Integración de Velero
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window} (antes llamado Heptio Ark) es un conjunto de herramientas para hacer copia de seguridad y restaurar clústeres de Kubernetes mediante la API de S3.

Velero consta de dos partes:

* Componente de servidor que se ejecuta en el clúster
* Interfaz de línea de mandatos que se ejecuta en un cliente local

## Requisitos previos
{: #velero-prereqs}

Antes de empezar, necesita la siguiente configuración:

* La [`CLI de IBM Cloud`](/docs/cli?topic=cloud-cli-getting-started){:new_window} instalada
* La herramientas de línea de mandatos [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} instalada y configurada para que se conecte con el clúster
* Una instancia de {{site.data.keyword.cos_short}}
* Un grupo de {{site.data.keyword.cos_short}}
* Credenciales de HMAC con acceso de Escritor sobre el grupo

## Instalación del cliente de Velero
{: #velero-install}

1. Descargue la [versión](https://github.com/heptio/velero/releases){:new_window} más reciente de Velero para su sistema operativo
2. Extraiga el archivo .tar en una carpeta del sistema local
3. Compruebe que puede ejecutar la biblioteca `velero`:

```
velero --help
```

```
Velero es una herramienta para gestionar la recuperación tras desastre, específicamente para los recursos de clúster de Kubernetes. Proporciona un método sencillo, configurable y potente de hacer una copia de seguridad del estado de la aplicación y de los datos asociados.

Si estás familiarizado con kubectl, Velero da soporte a un modelo similar, que le permite ejecutar mandatos como 'velero get backup' y 'velero create schedule'. Las mismas operaciones también se pueden ejecutar como 'get backup get' y 'velero schedule create'.

Uso:
  velero [mandato]

Mandatos disponibles:
  backup            Trabajar con copias de seguridad
  backup-location   Trabajar con ubicaciones de almacenamiento de copias de seguridad
  bug               Notificar un error de velero
  client            Mandatos relacionados con el cliente Velero
  completion        Código de finalización del shell de salida para el shell especificado (bash o zsh)
  create            Crear recursos de velero
  delete            Suprimir recursos de velero
  describe          Describir recursos de velero
  get               Obtener recursos de velero
  help              Ayuda sobre cualquier mandato
  plugin            Trabajar con plugins
  restic            Trabajar con restic
  restore           Trabajar con restauraciones
  schedule          Trabajar con planificaciones
  server            Ejecutar el servidor de velero
  snapshot-location Trabajar con ubicaciones de instantáneas
  version           Mostrar la versión de velero y la imagen asociada
...
```

*(OPCIONAL)* Pase el binario ark de la carpeta temporal a una ubicación más permanente, como `/usr/local/bin` en Mac OS o Linux.
{: tip}

## Instalación y configuración del servidor de Velero
{: #velero-config}
### Creación del archivo de credenciales
{: #velero-config-credentials}

Cree un archivo de credenciales (`credentials-velero`) con las claves de HMAC de la carpeta Velero local (*carpeta en la que se ha extraído el archivo .tar*)

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Configuración de kubectl
{: #velero-config-kubectl}

Configure [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} de modo que se conecte a su clúster.

1. Inicie la sesión en la plataforma IBM Cloud mediante la CLI.<br/><br/>*Para aumentar la seguridad, también se puede almacenar la clave de API en un archivo o se puede establecer como una variable de entorno.*
    ```bash
    ibmcloud login --apikey <value>
    ```
    {: pre}
2. Recupere la configuración del clúster 
    ```bash
    ibmcloud cs cluster-config <cluster-name>
    ```
    {: pre}
3. Copie y pegue el mandato **export** para establecer la variable de entorno KUBECONFIG

4. Asegúrese de que `kubectl` se configura correctamente ejecutando lo siguiente:
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

### Configuración del servidor de Velero y de Cloud Storage
{: #velero-config-storage}

1. En la carpeta Velero, ejecute lo siguiente para configurar espacios de nombres, RBAC y otros recursos de desarrollo<br/><br/>*El espacio de nombres predeterminado es `velero`. Si desea crear un espacio de nombres personalizado, consulte las instrucciones de ['ejecución en un espacio de nombres personalizado'](https://heptio.github.io/velero/master/namespace.html){:new_window}*
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
2. Creación de un secreto con el archivo de credenciales
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Especifique los siguientes valores en `config/ibm/05-ark-backupstoragelocation.yaml`:
   * `<YOUR_BUCKET>`: nombre del grupo para almacenar los archivos de copia de seguridad
   * `<YOUR_REGION>`: la [restricción de ubicación](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) del grupo (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>`: el URL de punto final regional (`https://s3.us.cloud-object-storage.appdomain.cloud`). Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

    *Para obtener más información, consulte la definición de [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window}. *

### Inicio del servidor de Velero
{: #velero-config-server}

1. En la carpeta Velero, ejecute el mandato siguiente para crear el objeto en el clúster:
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. Ejecute el siguiente mandato para crear el despliegue:
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Asegúrese de que el despliegue se ha planificado correctamente utilizando `kubectl get` en el espacio de nombres `velero`. Cuando el campo `Available` muestre `1`, significa que Ark está preparado:
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Prueba de la copia de seguridad y la restauración
{: #velero-test}

### Copia de seguridad
{: #velero-test-backup}

Ahora puede crear una copia de seguridad simple de su clúster de Kubernetes ejecutando el mandato siguiente:
```bash
velero backup create <backup-name>
```
{: pre}

Este mandato crea una copia de seguridad para cada recurso del clúster, incluidos los volúmenes persistentes.

También puede restringir la copia de seguridad a un espacio de nombres, un tipo de recurso o una etiqueta en particular.

Velero no permite seleccionar por nombre, solo por etiqueta.
{: tip}

Este mandato solo hace copia de seguridad de los componentes que están etiquetados con `app=<app-label>`. 
```bash
velero backup create <backup-name> --selector app=<app-label>
```
{: pre}

Para ver una lista completa de opciones, ejecute:
```bash
velero backup --help
```
{: pre}

### Restauración
{: #velero-test-restore}

Para restaurar una copia de seguridad, ejecute el mandato siguiente:
```bash
velero restore create  --from-backup <backup-name>
```
{: pre}

Para ver una lista completa de opciones, ejecute:
```bash
velero restore --help
```
{: pre}

