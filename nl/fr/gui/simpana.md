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


# Utilisation de CommVault Simpana pour archiver des données
{: #commvault}

CommVault Simpana s'intègre au niveau d'archivage d'{{site.data.keyword.cos_full_notm}}. Pour plus d'informations sur Simpana, voir la [documentation CommVault Simpana](https://documentation.commvault.com/commvault/). 

Pour plus d'informations sur IBM COS Infrastructure Archive, voir la [procédure d'archivage de données](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive). 

## Etapes d'intégration
{: #commvault-integration}

1.	A partir de la console Simpana, créez une bibliothèque de stockage en cloud Amazon S3.  

2. Vérifiez que l'hôte de service pointe vers le noeud final. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Simpana met à disposition des compartiments à ce stade ou il peut consommer des compartiments mis à disposition.  

3.	Créez une règle sur le compartiment. Vous pouvez utiliser l'interface CLI AWS, des SDK ou la console Web pour créer la règle. Voici un exemple de règle : 

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

### Association de la règle au compartiment
{: #commvault-assign-policy}

1. Exécutez la commande d'interface CLI suivante :

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Créez une règle de stockage à l'aide de Simpana et associez-la à la bibliothèque Cloud Storage que vous avez créée lors de la première étape. Une règle de stockage régit la façon dont Simpana interagit avec COS pour les transferts de sauvegarde. Vous trouverez une présentation des règles en cliquant [ici](https://documentation.commvault.com/commvault/v11/article?p=13804.htm). 

3.	Créez un groupe de sauvegarde et associez-le à la règle de stockage créée à l'étape précédente. Vous trouverez une présentation du groupe de sauvegarde en cliquant [ici](https://documentation.commvault.com/commvault/v11/article?p=11666.htm). 

## Exécution de sauvegardes
{: #commvault-backup}

Vous pouvez initier votre sauvegarde sur le compartiment avec la règle et effectuer des sauvegardes sur {{site.data.keyword.cos_full_notm}}. Vous trouverez davantage d'informations sur les sauvegardes Simpana en cliquant [ici](https://documentation.commvault.com/commvault/v11/article?p=11677.htm). Le contenu de sauvegarde passe au niveau d'archivage selon la règle configurée sur le compartiment. 

## Exécution de restaurations
{: #commvault-restore}

Vous pouvez restaurer un contenu de sauvegarde à partir d'{{site.data.keyword.cos_full_notm}}. Vous trouverez davantage d'informations sur les restaurations Simpana en cliquant [ici](https://documentation.commvault.com/commvault/v11/article?p=12867.htm). 

### Configuration de Simpana pour restaurer automatiquement des objets à partir du niveau d'archivage
{: #commvault-auto-restore}

1. Créez une tâche qui déclenche une restauration {{site.data.keyword.cos_full_notm}} lorsque vous restaurez une sauvegarde à partir de COS. Pour effectuer une configuration, voir la [documentation de CommVault Simpana](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053). 

2. Restaurez le contenu archivé à partir du niveau d'archivage vers le niveau d'origine via une tâche de rappel de stockage en cloud. Cette tâche est exécutée une fois que Simpana reçoit le code retour émis par {{site.data.keyword.cos_full_notm}}. Vous trouverez davantage d'informations sur le rappel d'archive en cliquant [ici](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053). 

3. Une fois la restauration terminée (à partir du niveau d'archivage vers le niveau d'origine), Simpana lit le contenu et écrit des données sur son emplacement d'origine ou configuré. 
