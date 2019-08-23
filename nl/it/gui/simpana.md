---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, simpana

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


# Utilizza CommVault Simpana per archiviare i dati
{: #commvault}

CommVault Simpana si integra con il livello di archivio di {{site.data.keyword.cos_full_notm}}. Per ulteriori informazioni su Simpana, consulta la [documentazione di CommVault Simpana](https://documentation.commvault.com/commvault/)

Per ulteriori informazioni sull'archivio dell'infrastruttura IBM COS, vedi [Procedure: archiviare i dati](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive).

## Passi dell'integrazione
{: #commvault-integration}

1.	Dalla console Simpana, crea una libreria di archiviazione cloud Amazon S3. 

2. Assicurati che l'host del servizio punti all'endpoint. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). In questo passo, Simpana esegue il provisioning dei bucket o può utilizzare i bucket di cui è stato eseguito il provisioning. 

3.	Crea una politica sul bucket. Per creare la politica, puoi utilizzare la CLI AWS, gli SDK o la console web. Di seguito è riportato un esempio di politica:

```shell
{
  "Rules": [
    {
      "ID": "CommVault",
      "Status": "Enabled",
      "Filter": {
        "Prefix": ""
      },
      "Transitions": [
        {
        "Days": 0,
        "StorageClass": "GLACIER"
        }
      ]
    }
  ]
}
```

### Associa la politica al bucket
{: #commvault-assign-policy}

1. Esegui il seguente comando della CLI:

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Crea una politica di archiviazione con Simpana e associa tale politica alla libreria di archiviazione cloud che hai creato nel primo passo. Una politica di archiviazione regola il modo in cui Simpana interagisce con COS per i trasferimenti di backup. Una panoramica delle politiche è disponibile [qui](https://documentation.commvault.com/commvault/v11/article?p=13804.htm).

3.	Crea un insieme di backup e associalo alla politica di archiviazione creata nel passo precedente. La panoramica dell'insieme di backup è disponibile [qui](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)

## Esecuzione di backup
{: #commvault-backup}

Puoi avviare il tuo backup nel bucket con la politica ed eseguire i backup su {{site.data.keyword.cos_full_notm}}. Ulteriori informazioni sui backup Simpana sono disponibili [qui](https://documentation.commvault.com/commvault/v11/article?p=11677.htm). I contenuti di backup passano al livello di archivio in base alla politica configurata sul bucket.

## Esecuzione di ripristini
{: #commvault-restore}

Puoi ripristinare i contenuti di backup da {{site.data.keyword.cos_full_notm}}. Ulteriori informazioni sul ripristino di Simpana sono disponibili [qui](https://documentation.commvault.com/commvault/v11/article?p=12867.htm).

### Configura Simpana per ripristinare automaticamente gli oggetti dal livello di archivio
{: #commvault-auto-restore}

1. Crea un'attività che attivi il ripristino di {{site.data.keyword.cos_full_notm}} quando ripristini un backup da COS. Per la configurazione, consulta la [documentazione di CommVault Simpana](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).

2. Ripristina i contenuti di cui è stato eseguito il backup dal livello di archivio al livello originale tramite un'attività di richiamo dell'archiviazione cloud. Questa attività viene eseguita dopo che Simpana riceve il codice di ritorno da {{site.data.keyword.cos_full_notm}}. Ulteriori informazioni sul richiamo dell'archivio sono disponibili [qui](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).

3. Una volta completato il ripristino (dal livello di archivio al livello originale), Simpana legge i contenuti e scrive nella sua ubicazione originale o configurata.
