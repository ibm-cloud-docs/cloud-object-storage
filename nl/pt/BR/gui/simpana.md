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


# Usar o CommVault Simpana para arquivar dados
{: #commvault}

O CommVault Simpana integra-se à camada de Archive do {{site.data.keyword.cos_full_notm}}. Para obter mais informações sobre o Simpana, consulte: [Documentação do CommVault Simpana](https://documentation.commvault.com/commvault/)

Para obter mais informações sobre o IBM COS Infrastructure Archive, consulte [Como: Archive Data](/docs/services/cloud-object-storage?topic=cloud-object-storage-archive).

## Etapas de integração
{: #commvault-integration}

1.	No console do Simpana, crie uma biblioteca de armazenamento em nuvem do Amazon S3. 

2. Assegure-se de que o Host de serviço aponte para o terminal. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). O Simpana provisiona depósitos nesta etapa ou pode consumir depósitos provisionados. 

3.	Crie uma política no depósito. É possível usar a CLI do AWS, os SDKs ou o console da web para criar a política. Segue um exemplo de uma política:

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

### Associar a política ao depósito
{: #commvault-assign-policy}

1. Execute o comando da CLI a seguir:

```shell
aws s3api put-bucket-lifecycle-configuration --bucket <bucket name> --lifecycle-configuration file://<saved policy file> --endpoint <endpoint>
```

2.	Crie uma política de armazenamento com o Simpana e associe a política de armazenamento à biblioteca do Cloud Storage que você criou na primeira etapa. Uma política de armazenamento governa a maneira como o Simpana interage com o COS para transferências de backup. Uma visão geral de política pode ser localizada [aqui](https://documentation.commvault.com/commvault/v11/article?p=13804.htm).

3.	Crie um conjunto de backup e associe o conjunto de backup à política de armazenamento criada na etapa anterior. A visão geral do conjunto de backup pode ser localizada [aqui](https://documentation.commvault.com/commvault/v11/article?p=11666.htm)

## Executando backups
{: #commvault-backup}

É possível iniciar seu backup para o depósito com a política e executar backups para o {{site.data.keyword.cos_full_notm}}. Mais informações sobre os backups do Simpana estão disponíveis [aqui](https://documentation.commvault.com/commvault/v11/article?p=11677.htm). O conteúdo do backup executa a transição para a camada de Archive com base na política configurada no depósito.

## Executando restaurações
{: #commvault-restore}

É possível restaurar o conteúdo de backup do {{site.data.keyword.cos_full_notm}}. Mais informações sobre a restauração do Simpana podem ser localizadas [aqui](https://documentation.commvault.com/commvault/v11/article?p=12867.htm).

### Configurar o Simpana para restaurar objetos automaticamente na camada de Archive
{: #commvault-auto-restore}

1. Crie uma tarefa que acione a restauração do {{site.data.keyword.cos_full_notm}} quando você restaurar um backup do COS. Consulte a [documentação do CommVault Simpana](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053) para configurar.

2. Restaure o conteúdo submetido a backup da camada de Archive para sua camada original por meio de uma tarefa de rechamada de armazenamento em nuvem. Essa tarefa é executada quando o Simpana recebe o código de retorno do {{site.data.keyword.cos_full_notm}}. Mais informações sobre a rechamada de archive podem ser localizadas [aqui](https://medium.com/codait/analyzing-data-with-ibm-cloud-sql-query-bc53566a59f5?linkId=49971053).

3. Quando a restauração (da camada de Archive para sua camada original) for concluída, o Simpana lerá o conteúdo e gravará em seu local original ou configurado.
