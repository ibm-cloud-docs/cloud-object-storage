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

# Integration von Velero
{: #velero}
[Velero](https://github.com/heptio/velero){:new_window} (ehemals Heptio Ark) ist ein Toolset zur Sicherung (Backup) und Wiederherstellung (Restore) von Kubernetes-Clustern unter Verwendung der S3-API.

Der Velero besteht aus zwei Teilen:

* Einer Serverkomponente, die auf dem Cluster ausgeführt wird.
* Einer Befehlszeilenschnittstelle, die auf einem lokalen Client ausgeführt wird.

## Voraussetzungen
{: #velero-prereqs}

Bevor Sie beginnen, müssen Sie sicherstellen, dass die folgende Konfiguration vorhanden ist:

* Die [`IBM Cloud-Befehlszeilenschnittstelle (CLI)`](/docs/cli?topic=cloud-cli-getting-started){:new_window} ist installiert.
* Das Befehlszeilentool [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} ist installiert und zur Verbindungsherstellung mit Ihrem Cluster konfiguriert.
* Es ist eine {{site.data.keyword.cos_short}}-Instanz vorhanden.
* Ein {{site.data.keyword.cos_short}}-Bucket ist vorhanden.
* Es sind HMAC-Berechtigungsnachweise mit Zugriff als Schreibberechtigter auf das Bucket vorhanden.

## Velero-Client installieren
{: #velero-install}

1. Laden Sie die aktuellste [Version](https://github.com/heptio/velero/releases){:new_window} von Velero für Ihr Betriebssystem herunter.
2. Extrahieren Sie die TAR-Datei in einen Ordner auf Ihrem lokalen System.
3. Überprüfen Sie, ob Sie die `velero`-Binärdatei ausführen können: 

```
velero --help
```

```
Velero ist ein Tool zur Verwaltung von Disaster-Recovery, speziell für Kubernetes-Clusterressourcen. Es bietet eine einfache, konfigurierbare und leistungsfähige Möglichkeit, den Anwendungsstatus und die damit verbundenen Daten per Backup zu sichern.

Velero unterstützt ein ähnliches Modell wie kubectl. Wenn Sie mit kubectl vertraut sind, können Sie daher Befehle wie'velero get backup' und 'velero create schedule' ausführen. Dieselben Operationen können auch als 'velero backup get' und 'velero schedule create' ausgeführt werden.

Syntax:
  velero [Befehl]

Verfügbare Befehle:
  backup            Mit Backups arbeiten
  backup-location   Mit Backup-Speicherpositionen arbeiten
  bug               Velero-Bug melden
  client            Für Velero-Clients relevante Befehle
  completion        Shell-Beendigungscode für angegebene Shell (bash oder zsh) ausgeben
  create            Velero-Ressourcen erstellen
  delete            Velero-Ressourcen löschen
  describe          Velero-Ressourcen beschreiben
  get               Velero-Ressourcen abrufen
  help              Hilfe zu beliebigem Befehl anfordern
  plugin            Mit Plug-ins arbeiten
  restic            Mit Restic arbeiten
  restore           Mit Restores arbeiten
  schedule          Mit Zeitplänen arbeiten
  server            Velero-Server ausführen
  snapshot-location Mit Snapshotpositionen arbeiten
  version           Velero-Version und zugehörige Grafik ausgeben
...
```

*(OPTIONAL)* Verschieben Sie die ark-Binärdatei aus dem temporären Ordner an einen anderen permanenten Standort, wie zum Beispiel `/usr/local/bin` unter Mac OS oder Linux.
{: tip}

## Velero-Server installieren und konfigurieren
{: #velero-config}
### Berechtigungsnachweisdatei erstellen
{: #velero-config-credentials}

Erstellen Sie eine Berechtigungsnachweisdatei (`credentials-velero`) mit den HMAC-Schlüsseln in Ihrem lokalen Velero-Ordner (*d. h. dem Ordner, in den die TAR-Datei extrahiert wurde*).

```
 [default]
 aws_access_key_id=<ACCESS_KEY_ID>
 aws_secret_access_key=<SECRET_ACCESS_KEY>
```

### Kubectl konfigurieren
{: #velero-config-kubectl}

Konfigurieren Sie [`kubectl`](https://kubernetes.io/docs/reference/kubectl/overview/){:new_window} für die Verbindungsherstellung zu Ihrem Cluster.

1. Melden Sie sich über die Befehlszeilenschnittstelle (CLI) an der IBM Cloud-Plattform an. <br/><br/>*Für mehr Sicherheit ist es auch möglich, den API-Schlüssel in einer Datei zu speichern oder als Umgebungsvariable festzulegen.*
    ```bash
    ibmcloud login --apikey <wert>
    ```
    {: pre}
2. Rufen Sie die Clusterkonfiguration ab.
```bash
    ibmcloud cs cluster-config <clustername>
    ```
    {: pre}
3. Kopieren Sie den Befehl **export** und fügen Sie ihn ein, um die Umgebungsvariable KUBECONFIG festzulegen.

4. Stellen Sie sicher, dass `kubectl` ordnungsgemäß konfiguriert ist. Führen Sie dazu folgenden Befehl aus:
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

### Velero-Server und Cloud Storage konfigurieren
{: #velero-config-storage}

1. Führen Sie im Velero-Ordner die folgenden Schritte aus, um Namensbereiche, RBAC und weiteres Scaffolding einzurichten.<br/><br/>*Der Standardnamensbereich lautet `velero`. Wenn Sie einen angepassten Namensbereich erstellen möchten, lesen Sie die Anweisungen unter ['In angepasstem Namensbereich ausführen'](https://heptio.github.io/velero/master/namespace.html){:new_window}*.
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
2. Erstellen Sie einen geheimen Schlüssel mit Ihrer Berechtigungsnachweisdatei.
    ```bash
    kubectl create secret generic cloud-credentials --namespace velero--from-file cloud=credentials-ark
    ```
    {: pre}

    ```bash
    secret/cloud-credentials created
    ```
    {: pre}

3. Geben Sie in der Datei `config/ibm/05-ark-backupstoragelocation.yaml` die folgenden Werte an:
   * `<YOUR_BUCKET>` - Name des Buckets zum Speichern von Backupdateien
   * `<YOUR_REGION>` - Die [Standortbeschränkung](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) Ihres Buckets (`us-standard`)
   * `<YOUR_URL_ACCESS_POINT>` - Die URL des regionalen Endpunkts (`https://s3.us.cloud-object-storage.appdomain.cloud`). Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

    *Weitere Informationen finden Sie zur Definition von [BackupStorageLocation](https://heptio.github.io/velero/master/api-types/backupstoragelocation.html#aws){:new_window}.*

### Velero-Server starten
{: #velero-config-server}

1. Führen Sie im Velero-Ordner den folgenden Befehl aus, um das Objekt in Ihrem Cluster zu erstellen:
    ```bash
    kubectl apply -f config/ibm/05-ark-backupstoragelocation.yaml
    ```
    {: pre}
 
    ```bash
    backupstoragelocation.velero.io/default created
    ```
    {: pre}

2. Führen Sie den folgenden Befehl aus, um die Bereitstellung zu erstellen:
    ```bash
    kubectl apply -f config/ibm/10-deployment.yaml
    ```
    {: pre}

    ```bash
    deployment.apps/ark created
    ```
    {: pre}
3. Stellen Sie sicher, dass die Bereitstellung erfolgreich geplant ist, indem Sie `kubectl get` für den Namensbereich `velero` verwenden. Wenn für `Available` der Wert `1` angegeben wird, ist Ark einsatzbereit:
    ```bash
    kubectl get deployments --namespace=velero
    ```
    {: pre}

    ```bash
    NAME   DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
    ark    1         1         1            0           48s
    ```
    {: pre}

## Backup und Restore testen
{: #velero-test}

### Backup
{: #velero-test-backup}

Sie können nun ein einfaches Backup Ihres Kubernetes-Clusters erstellen, indem Sie den folgenden Befehl ausführen:
```bash
velero backup create <backupname>
```
{: pre}

Dieser Befehl erstellt ein Backup für jede Ressource auf dem Cluster, einschließlich persistenter Datenträger.

Sie können das Backup auch auf einen bestimmten Namensbereich, Ressourcentyp oder eine bestimmte Bezeichnung (Label)beschränken.

Velero lässt keine Auswahl nach Namen zu, sondern nur nach Bezeichnungen (Labels).
{: tip}

Dieser Befehl sichert nur die Komponenten, die mit `app=<app-label>` gekennzeichnet sind. 
```bash
velero backup create <backupname> --selector app=<app-bezeichnung>
```
{: pre}

Eine vollständige Liste der Optionen kann durch Ausführen des folgenden Befehls abgerufen werden:
```bash
velero backup --help
```
{: pre}

### Restore
{: #velero-test-restore}

Führen Sie zum Wiederherstellen eines Backups (Restore) den folgenden Befehl aus:
```bash
velero restore create  --from-backup <backupname>
```
{: pre}

Eine vollständige Liste der Optionen kann durch Ausführen des folgenden Befehls abgerufen werden:
```bash
velero restore --help
```
{: pre}

