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


# Utilización de CommVault Simpana para archivar datos
{: #commvault}

CommVault Simpana se integra con el nivel de archivador de {{site.data.keyword.cos_full_notm}}. Para obtener más información acerca de Simpana, consulte la [documentación de CommVault Simpana](https://documentation.commvault.com/commvault/)

Para obtener más información sobre el archivador de infraestructura de IBM COS, consulte [Cómo archivar datos](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive).

## Pasos de la integración
{: #commvault-integration}

1.	En la consola de Simpana, cree una biblioteca de almacenamiento en la nube de Amazon S3. 

2. Asegúrese de que el host de servicio apunte al punto final. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). En este paso, Simpana suministra grupos o puede consumir grupos suministrados. 

3.	Cree una política en el grupo. Puede utilizar la CLI de AWS, los SDK o la consola web para crear la política. A continuación se muestra un ejemplo de política:

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

### Asociación de la política con el grupo
{: #commvault-assign-policy}

1. Ejecute el siguiente mandato de CLI:

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Cree una política de almacenamiento con Simpana y asocie la política de almacenamiento con la biblioteca de Cloud Storage que ha creado en el primer paso. Una política de almacenamiento controla la forma en que Simpana interactúa con COS para las transferencias de copia de seguridad. [Aquí](https://documentation.commvault.com/commvault/v11/article?p=13804.htm) encontrará una visión general de la política.

3.	Cree un conjunto de copias de seguridad y asocie el conjunto de copias de seguridad a la política de almacenamiento creada en el paso anterior. [Aquí](https://documentation.commvault.com/commvault/v11/article?p=11666.htm) encontrará una visión general del conjunto de copias de seguridad

## Realización de copias de seguridad
{: #commvault-backup}

Puede iniciar la copia de seguridad en el grupo con la política y realizar copias de seguridad en {{site.data.keyword.cos_full_notm}}. [Aquí](https://documentation.commvault.com/commvault/v11/article?p=11677.htm) encontrará más información sobre las copias de seguridad de Simpana. El contenido de la copia de seguridad se transfiere al nivel de archivador en función de la política configurada en el grupo.

## Realización de restauraciones
{: #commvault-restore}

Puede restaurar el contenido de una copia de seguridad de {{site.data.keyword.cos_full_notm}}. [Aquí](https://documentation.commvault.com/commvault/v11/article?p=12867.htm) encontrará más información sobre la restauración de Simpana.

### Configuración de Simpana para que restaure automáticamente los objetos desde el nivel de archivador
{: #commvault-auto-restore}

1. Cree una tarea que active la restauración de {{site.data.keyword.cos_full_notm}} cuando restaure una copia de seguridad de COS. Consulte la [documentación de CommVault Simpana](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053) para configurarla.

2. La restauración copia el contenido de la copia de seguridad del nivel de archivador en su nivel original mediante una llamada a Cloud Storage. Esta tarea se ejecuta cuando Simpana recibe el código de retorno de {{site.data.keyword.cos_full_notm}}. [Aquí](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053) encontrará más información sobre la llamada al archivador.

3. Una vez finalizada la restauración (del nivel de archivador en su nivel original), Simpana lee el contenido y escribe en su ubicación original o configurada.
