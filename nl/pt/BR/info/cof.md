---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# Usando o Cloud Object Storage com o apps do Cloud Foundry
{: #cloud-foundry}

O {{site.data.keyword.cos_full}} pode ser emparelhado com os aplicativos do {{site.data.keyword.cfee_full}} para fornecer conteúdo altamente disponível usando regiões e terminais.

## Ambiente do Cloud Foundry Enterprise
{: #cloud-foundry-ee}
O {{site.data.keyword.cfee_full}} é uma plataforma para hospedar apps e serviços na nuvem. É possível instanciar plataformas múltiplas, isoladas e de classificação corporativa sob demanda que são executadas dentro de sua própria conta e podem ser implementadas em hardware compartilhado ou dedicado. A plataforma torna mais fácil escalar apps conforme o consumo cresce, simplificando o tempo de execução e a infraestrutura para que seja possível focar o desenvolvimento.

A implementação bem-sucedida de uma plataforma Cloud Foundry requer [planejamento e design adequados](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation) para os recursos e requisitos corporativos necessários. Saiba mais sobre [como começar a usar](/docs/cloud-foundry?topic=cloud-foundry-about#creating) o Cloud Foundry Enterprise Environment e veja um [tutorial](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started) introdutório.

### Regiões
{: #cloud-foundry-regions}
Os [Terminais regionais](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) são uma parte importante do IBM Cloud Environment. É possível criar aplicativos e instâncias de serviço em regiões diferentes com a mesma infraestrutura do IBM Cloud para gerenciamento de aplicativo e a mesma visualização de detalhes de uso para faturamento. Escolhendo uma região do IBM Cloud que está geograficamente próxima a você ou a seus clientes, é possível reduzir a latência de dados em seus aplicativos, assim como minimizar os custos. As regiões também podem ser selecionadas para tratar de quaisquer interesses de segurança ou requisitos regulamentares. 

Com o {{site.data.keyword.cos_full}}, é possível escolher dispersar os dados em um data center único, uma região inteira ou até mesmo uma combinação de regiões [selecionando o terminal](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) em que seu aplicativo envia solicitações de API.

### Conexões de recursos e aliases
{: #cloud-foundry-aliases}

Um alias é uma conexão entre o serviço gerenciado dentro de um grupo de recursos e um aplicativo dentro de uma organização ou um espaço. Os aliases são como links simbólicos que contêm referências a recursos remotos. Isso permite a interoperabilidade e reutilização de uma instância na plataforma. No console do {{site.data.keyword.cloud_notm}}, a conexão (alias) é representada como uma instância de serviço. É possível criar uma instância de um serviço em um grupo de recursos e, em seguida, reutilizá-la de qualquer região disponível, criando um alias em uma organização ou um espaço nessas regiões.

## Armazenando credenciais como variáveis VCAP 
{: #cloud-foundry-vcap}

As credenciais do {{site.data.keyword.cos_short}} podem ser armazenadas na variável de ambiente VCAP_SERVICES, que pode ser analisada para uso ao acessar o serviço {{site.data.keyword.cos_short}}. As credenciais incluem informações conforme apresentadas no exemplo a seguir:

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

A variável de ambiente VCAP_SERVICES pode, então, ser analisada em seu aplicativo para acessar o conteúdo do {{site.data.keyword.cos_short}}. Abaixo está um exemplo de integração da variável de ambiente com o SDK do COS usando Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

Para obter mais informações sobre como usar o SDK para acessar o {{site.data.keyword.cos_short}} com exemplos de código, visite:

* [Usando Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Usando Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Usando Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## Criando ligações de serviços 
{: #cloud-foundry-bindings}

### Painel
{: #cloud-foundry-bindings-console}

A maneira mais simples de criar uma ligação de serviços é usando o [{{site.data.keyword.cloud}} Dashboard](https://cloud.ibm.com/resources). 

1. Efetue login no [Dashboard](https://cloud.ibm.com/resources)
2. Clique em seu aplicativo Cloud Foundry
3. Clique em Conexões no menu à esquerda
4. Clique em **Criar conexão** à direita
5. Na página *Conectar o serviço compatível existente*, passe o mouse sobre seu serviço {{site.data.keyword.cos_short}} e clique em **Conectar**.
6. Na tela pop-up *Conectar o serviço ativado por IAM*, selecione a Função de acesso, deixe Gerar automaticamente para o ID de serviço e clique em **Conectar**
7. O aplicativo Cloud Foundry precisa ser remontado a fim de usar a nova ligação de serviços. Clique em **Remontar** para iniciar o processo.
8. Depois que a remontagem estiver concluída, seu serviço Cloud Object Storage estará disponível para seu aplicativo.

A variável de ambiente VCAP_SERVICES de aplicativos é atualizada automaticamente com as informações de serviço. Para visualizar a nova variável:

1. Clique em *Tempo de execução* no menu à direita
2. Clique em *Variáveis de ambiente*
3. Verifique se o serviço COS está agora listado

### IBM Client Tools (CLI)
{: #cloud-foundry-bindings-cli}

1. Efetue login com a CLI do IBM Cloud
```
 ibmcloud login --apikey <your api key>
```

2. Tenha como destino seu ambiente do Cloud Foundry
```
 ibmcloud target --cf
```

3. Crie um alias do serviço para seu {{site.data.keyword.cos_short}}
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Crie uma ligação de serviços entre seu alias do {{site.data.keyword.cos_short}} e seu aplicativo Cloud Foundry e forneça uma função para sua ligação. As funções válidas são:<br/><ul><li>Gravador</li><li>Reader</li><li>Gerente</li><li>Administrador</li><li>Operator</li><li>Visualizador</li><li>Aplicativos</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### IBM Client Tools (CLI) com credenciais HMAC
{: #cloud-foundry-hmac}

O Hash-based message authentication code (HMAC) é um mecanismo para calcular um código de autenticação de mensagem criado que usa um par de chaves secretas e de acesso. Essa técnica pode ser usada para verificar a integridade e a autenticidade de uma mensagem. Mais informações sobre o uso de [credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) estão disponíveis na documentação do {{site.data.keyword.cos_short}}.

1. Efetue login com a CLI do IBM Cloud
```
 ibmcloud login --apikey <your api key>
```

2. Tenha como destino seu ambiente do Cloud Foundry
```
 ibmcloud target --cf
```

3. Crie um alias do serviço para seu {{site.data.keyword.cos_short}}
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Crie uma ligação de serviços entre seu alias do {{site.data.keyword.cos_short}} e seu aplicativo Cloud Foundry e forneça uma função para sua ligação.<br/><br/>* **Nota:** um parâmetro extra* (`{"HMAC":true}`) *é necessário para criar credenciais de serviço com HMAC ativado.*<br/><br/>As funções válidas são:<br/><ul><li>Gravador</li><li>Reader</li><li>Gerente</li><li>Administrador</li><li>Operator</li><li>Visualizador</li><li>Aplicativos</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### Ligação para o {{site.data.keyword.containershort_notm}}
{: #cloud-foundry-k8s}

A criação de uma ligação de serviços para o {{site.data.keyword.containershort}} requer um procedimento um pouco diferente. 

*Para esta seção, também será necessário instalar o [jq - um processador JSON de linha de comandos leve](https://stedolan.github.io/jq/){:new_window}.*

Você precisa das informações a seguir e substituir os valores de chave nos comandos abaixo:

* `<service alias>` - o novo nome de alias para o serviço COS
* `<cos instance name>` - o nome de sua instância existente do COS
* `<service credential name>` - o novo nome para a chave de serviço/credencial
* `<role>` - a função para anexar à sua chave de serviço (consulte acima para obter as funções válidas, `Writer` é mais frequentemente especificado)
* `<cluster name>` - o nome de seu serviço de cluster Kubernetes existente
* `<secret binding name>` - esse valor é gerado quando o COS está ligado ao serviço de cluster


1. Crie um alias do serviço para sua instância do COS<br/><br/>* **Nota:** a instância do COS pode ter somente um alias do serviço*
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. Crie uma nova chave de serviço com permissões para o alias do serviço COS
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. Ligue o serviço de cluster ao COS
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. Verifique se o alias do serviço COS está ligado ao cluster
```
ibmcloud cs cluster-services --cluster <cluster name>
```
A saída será semelhante a esta:
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. Recupere a lista de Segredos em seu cluster e localize o segredo para seu serviço COS. Geralmente, ele será `binding-` mais o `<service alias>` que você especificou na etapa 1 (ou seja, `binding-sv-cos`). Use esse valor como `<secret binding name>` na etapa 6.
```
kubectl get secrets
```
a saída deve ser semelhante a esta:
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. Verifique se as credenciais HMAC do COS estão disponíveis em seus Segredos de cluster
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
a saída deve ser semelhante a esta:
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
