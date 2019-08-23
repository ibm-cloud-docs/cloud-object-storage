---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: node, javascript, sdk

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

# Usando Node.js
{: #node}

## Instalando o SDK
{: #node-install}

A maneira preferencial de instalar o {{site.data.keyword.cos_full}} SDK for Node.js é usar o
gerenciador de pacote [`npm`](https://www.npmjs.com){:new_window} para Node.js. Digite o comando a seguir
em uma linha de comandos:

```sh
npm install ibm-cos-sdk
```

O código-fonte é hospedado no [GitHub](https://github.com/IBM/ibm-cos-sdk-js){:new_window}.

Mais detalhes sobre métodos individuais e classes podem ser localizados na [documentação da API para o SDK](https://ibm.github.io/ibm-cos-sdk-js/){:new_window}.

## Como Começar
{: #node-gs}

### Requisitos mínimos
{: #node-gs-prereqs}

Para executar o SDK, é necessário o **Node 4.x +**.

### Criando um cliente e credenciais de fornecimento
{: #node-gs-credentials}

Para conectar-se ao COS, um cliente é criado e configurado fornecendo informações de credenciais (Chave de API, ID da instância de serviço e Terminal de autenticação IBM). Esses valores também podem ser originados automaticamente de um arquivo de credenciais ou de variáveis de ambiente.

Depois de gerar uma [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), o documento JSON resultante pode ser salvo em `~/.bluemix/cos_credentials`. O SDK originará automaticamente as credenciais desse arquivo, a menos que outras credenciais sejam explicitamente configuradas durante a criação do cliente. Se o arquivo `cos_credentials` contiver chaves HMAC, o cliente será autenticado com uma assinatura, caso contrário, o cliente usará a chave de API fornecida para autenticar com um token de acesso.

O título da seção `default` especifica um perfil padrão e os valores associados para credenciais. É possível criar mais perfis no mesmo arquivo de configuração compartilhada, cada um com suas próprias informações de credenciais. O exemplo a seguir mostra um arquivo de configuração com o perfil padrão:
```
[default]
ibm_api_key_id = <DEFAULT_IBM_API_KEY>
ibm_service_instance_id = <DEFAULT_IBM_SERVICE_INSTANCE_ID>
ibm_auth_endpoint = <DEFAULT_IBM_AUTH_ENDPOINT>
```

Se estiver migrando do AWS S3, também será possível originar os dados de credenciais de `~/.aws/credentials` no formato:

```
aws_access_key_id = <DEFAULT_ACCESS_KEY_ID>
aws_secret_access_key = <DEFAULT_SECRET_ACCESS_KEY>
```

Se ambos, `~/.bluemix/cos_credentials` e `~/.aws/credentials`, existirem, `cos_credentials` terá a preferência.

## Exemplos de código
{: #node-examples}

### Inicializando a configuração
{: #node-examples-init}

```javascript
const AWS = require('ibm-cos-sdk');

var config = {
    endpoint: '<endpoint>',
    apiKeyId: '<api-key>',
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: '<resource-instance-id>',
};

var cos = new AWS.S3(config);
```
*Valores da chave*
* `<endpoint>` - o terminal público para seu armazenamento de objeto de nuvem (disponível por meio do [IBM Cloud Dashboard](https://cloud.ibm.com/resources){:new_window}). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - a chave de API gerada ao criar as credenciais de serviço (o acesso de gravação é necessário para exemplos de criação e exclusão)
* `<resource-instance-id>` - o ID do recurso para seu armazenamento de objeto de nuvem (disponível por meio da [CLI do IBM Cloud](/docs/overview?topic=overview-crn) ou do [IBM Cloud Dashboard](https://cloud.ibm.com/resources){:new_window})

### Criando um depósito
{: #node-examples-new-bucket}

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```javascript
function createBucket(bucketName) {
    console.log(`Creating new bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: 'us-standard'
        },        
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
    }))
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```


*Referências do SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

### Criando um objeto de texto
{: #node-examples-new-file}

```javascript
function createTextFile(bucketName, itemName, fileText) {
    console.log(`Creating new item: ${itemName}`);
    return cos.putObject({
        Bucket: bucketName, 
        Key: itemName, 
        Body: fileText
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} created!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [putObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#putObject-property){:new_window}

### Listar depósitos
{: #node-examples-list-buckets}

```javascript
function getBuckets() {
    console.log('Retrieving list of buckets');
    return cos.listBuckets()
    .promise()
    .then((data) => {
        if (data.Buckets != null) {
            for (var i = 0; i < data.Buckets.length; i++) {
                console.log(`Bucket Name: ${data.Buckets[i].Name}`);
            }
        }
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [listBuckets](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listBuckets-property){:new_window}

### Listar itens em um depósito
{: #node-examples-list-objects}

```javascript
function getBucketContents(bucketName) {
    console.log(`Retrieving bucket contents from: ${bucketName}`);
    return cos.listObjects(
        {Bucket: bucketName},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).`)
            }
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [listObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#listObjects-property){:new_window}

### Obter conteúdo do arquivo de um item específico
{: #node-examples-get-contents}

```javascript
function getItem(bucketName, itemName) {
    console.log(`Retrieving item from bucket: ${bucketName}, key: ${itemName}`);
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log('File Contents: ' + Buffer.from(data.Body).toString());
        }    
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [getObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#getObject-property){:new_window}

### Excluir um item de um depósito
{: #node-examples-delete-object}

```javascript
function deleteItem(bucketName, itemName) {
    console.log(`Deleting item: ${itemName}`);
    return cos.deleteObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then(() => {
        console.log(`Item: ${itemName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
*Referências do SDK*
* [deleteObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObject-property){:new_window}

### Excluir múltiplos itens de um depósito
{: #node-examples-multidelete}

A solicitação de exclusão pode conter um máximo de 1000 chaves que você deseja excluir. Embora a exclusão de objetos em lotes seja muito útil para reduzir a sobrecarga por solicitação, fique atento ao excluir muitas chaves que a solicitação pode levar algum tempo para ser concluída. Além disso, leve em conta os tamanhos dos objetos para assegurar o desempenho adequado.
{:tip}

```javascript
function deleteItems(bucketName) {
    var deleteRequest = {
        "Objects": [
            { "Key": "deletetest/testfile1.txt" },
            { "Key": "deletetest/testfile2.txt" },
            { "Key": "deletetest/testfile3.txt" },
            { "Key": "deletetest/testfile4.txt" },
            { "Key": "deletetest/testfile5.txt" }
        ]        
    }
    return cos.deleteObjects({
        Bucket: bucketName,
        Delete: deleteRequest
    }).promise()
    .then((data) => {
        console.log(`Deleted items for ${bucketName}`);
        console.log(data.Deleted);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });    
}
```

*Referências do SDK*
* [deleteObjects](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteObjects-property){:new_window}

### Excluir um depósito
{: #node-examples-delete-bucket}

```javascript
function deleteBucket(bucketName) {
    console.log(`Deleting bucket: ${bucketName}`);
    return cos.deleteBucket({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Bucket: ${bucketName} deleted!`);
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [deleteBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#deleteBucket-property){:new_window}


### Executar um upload de múltiplas partes
{: #node-examples-multipart}

```javascript
function multiPartUpload(bucketName, itemName, filePath) {
    var uploadID = null;

    if (!fs.existsSync(filePath)) {
        log.error(new Error(`The file \'${filePath}\' does not exist or is not accessible.`));
        return;
    }

    console.log(`Starting multi-part upload for ${itemName} to bucket: ${bucketName}`);
    return cos.createMultipartUpload({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        uploadID = data.UploadId;

        //begin the file upload        
        fs.readFile(filePath, (e, fileData) => {
            //min 5MB part
            var partSize = 1024 * 1024 * 5;
            var partCount = Math.ceil(fileData.length / partSize);
    
            async.timesSeries(partCount, (partNum, next) => {
                var start = partNum * partSize;
                var end = Math.min(start + partSize, fileData.length);
    
                partNum++;

                console.log(`Uploading to ${itemName} (part ${partNum} of ${partCount})`);  

                cos.uploadPart({
                    Body: fileData.slice(start, end),
                    Bucket: bucketName,
                    Key: itemName,
                    PartNumber: partNum,
                    UploadId: uploadID
                }).promise()
                .then((data) => {
                    next(e, {ETag: data.ETag, PartNumber: partNum});
                })
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            }, (e, dataPacks) => {
                cos.completeMultipartUpload({
                    Bucket: bucketName,
                    Key: itemName,
                    MultipartUpload: {
                        Parts: dataPacks
                    },
                    UploadId: uploadID
                }).promise()
                .then(console.log(`Upload of all ${partCount} parts of ${itemName} successful.`))
                .catch((e) => {
                    cancelMultiPartUpload(bucketName, itemName, uploadID);
                    console.error(`ERROR: ${e.code} - ${e.message}\n`);
                });
            });
        });
    })
    .catch((e) => {
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function cancelMultiPartUpload(bucketName, itemName, uploadID) {
    return cos.abortMultipartUpload({
        Bucket: bucketName,
        Key: itemName,
        UploadId: uploadID
    }).promise()
    .then(() => {
        console.log(`Multi-part upload aborted for ${itemName}`);
    })
    .catch((e)=>{
        console.error(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [abortMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#abortMultipartUpload-property){:new_window}
* [completeMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#completeMultipartUpload-property){:new_window}
* [createMultipartUpload](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createMultipartUpload-property){:new_window}
* [uploadPart](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#uploadPart-property){:new_window}

## Usando o Key Protect
{: #node-examples-kp}

O Key Protect pode ser incluído em um depósito de armazenamento para gerenciar chaves de criptografia. Todos os dados são criptografados no IBM COS, mas o Key Protect fornece um serviço para gerar, girar e controlar o acesso às chaves de criptografia usando um serviço centralizado.

### Antes de iniciar
{: #node-examples-kp-prereqs}
Os itens a seguir são necessários para criar um depósito com o Key-Protect ativado:

* Um serviço Key Protect [provisionado](/docs/services/key-protect?topic=key-protect-provision#provision)
* Uma chave Raiz disponível ([gerada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperando o CRN da chave raiz
{: #node-examples-kp-root}

1. Recupere o [ID da instância](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) para seu serviço Key Protect
2. Use a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas as suas [chaves disponíveis](https://cloud.ibm.com/apidocs/key-protect)
    * É possível usar comandos `curl` ou um Cliente REST de API, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acessar a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere o CRN da chave raiz que você usará para ativar o Key Protect no seu depósito. O CRN será semelhante ao abaixo:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Criando um depósito com o Key Protect ativado
{: #node-examples-kp-new-bucket}

```javascript
function createBucketKP(bucketName) {
    console.log(`Creating new encrypted bucket: ${bucketName}`);
    return cos.createBucket({
        Bucket: bucketName,
        CreateBucketConfiguration: {
          LocationConstraint: '<bucket-location>'
        },
        IBMSSEKPEncryptionAlgorithm: '<algorithm>',
        IBMSSEKPCustomerRootKeyCrn: '<root-key-crn>'
    }).promise()
    .then((() => {
        console.log(`Bucket: ${bucketName} created!`);
        logDone();
    }))
    .catch(logError);
}
```
*Valores da chave*
* `<bucket-location>` - a região ou o local para seu depósito (o Key Protect está disponível somente em certas regiões. Assegure-se de que seu local corresponda ao serviço Key Protect) Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes)..
* `<algorithm>` - o algoritmo de criptografia usado para novos objetos incluídos no depósito (o padrão é AES256).
* `<root-key-crn>` - o CRN da Chave raiz que é obtida do serviço Key Protect.

*Referências do SDK*
* [createBucket](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html#createBucket-property){:new_window}

## Usando o recurso Archive
{: #node-examples-archive}

A Camada de Archive permite que os usuários arquivem dados antigos e reduzam seus custos de armazenamento. As políticas de arquivamento (também conhecidas como *Configurações de ciclo de vida*) são criadas para os depósitos e se aplicam a quaisquer objetos incluídos no depósito após a política ser criada.

### Visualizar uma configuração de ciclo de vida de um depósito
{: #node-examples-get-lifecycle}

```javascript
function getLifecycleConfiguration(bucketName) {
    return cos.getBucketLifecycleConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        if (data != null) {
            console.log(`Retrieving bucket lifecycle config from: ${bucketName}`);
            console.log(JSON.stringify(data, null, 4));
        }
        else {
            console.log(`No lifecycle configuration for ${bucketName}`);
        }
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [getBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Criar uma configuração de ciclo de vida 
{: #node-examples-put-lifecycle}

Informações detalhadas sobre a estruturação das regras de configuração de ciclo de vida estão disponíveis na [Referência da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket)

```javascript
function createLifecycleConfiguration(bucketName) {
    //
    var config = {
        Rules: [{
            Status: 'Enabled', 
            ID: '<policy-id>',
            Filter: {
                Prefix: ''
            },
            Transitions: [{
                Days: <number-of-days>, 
                StorageClass: 'GLACIER'
            }]
        }]
    };
    
    return cos.putBucketLifecycleConfiguration({
        Bucket: bucketName,
        LifecycleConfiguration: config
    }).promise()
    .then(() => {
        console.log(`Created bucket lifecycle config for: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Valores da chave*
* `<policy-id>` - o nome da política de ciclo de vida (deve ser exclusivo)
* `<number-of-days>` - o número de dias para manter o arquivo restaurado

*Referências do SDK*
* [putBucketLifecycleConfiguration](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Excluir a configuração de ciclo de vida de um depósito
{: #node-examples-delete-lifecycle}

```javascript
function deleteLifecycleConfiguration(bucketName) {
    return cos.deleteBucketLifecycle({
        Bucket: bucketName
    }).promise()
    .then(() => {
        console.log(`Deleted bucket lifecycle config from: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [deleteBucketLifecycle](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Restaurar temporariamente um objeto
{: #node-examples-restore-object}

Informações detalhadas sobre os parâmetros de solicitação de restauração estão disponíveis na [Referência da API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations#object-operations-archive-restore)

```javascript
function restoreItem(bucketName, itemName) {
    var params = {
        Bucket: bucketName, 
        Key: itemName, 
        RestoreRequest: {
            Days: <number-of-days>, 
            GlacierJobParameters: {
                Tier: 'Bulk' 
            },
        } 
    };
    
    return cos.restoreObject(params).promise()
    .then(() => {
        console.log(`Restoring item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Valores da chave*
* `<number-of-days>` - o número de dias para manter o arquivo restaurado

*Referências do SDK*
* [restoreObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

### Visualizar informações de HEAD para um objeto
{: #node-examples-lifecycle-head-object}
```javascript
function getHEADItem(bucketName, itemName) {
    return cos.headObject({
        Bucket: bucketName,
        Key: itemName
    }).promise()
    .then((data) => {
        console.log(`Retrieving HEAD for item: ${itemName} from bucket: ${bucketName}`);
        console.log(JSON.stringify(data, null, 4));
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

*Referências do SDK*
* [headObject](https://ibm.github.io/ibm-cos-sdk-js/AWS/S3.html){:new_window}

## Atualizando os metadados
{: #node-examples-metadata}

Há duas maneiras de atualizar os metadados em um objeto existente:
* Uma solicitação `PUT` com os novos metadados e o conteúdo do objeto original
* Executando uma solicitação de `COPY` com os novos metadados especificando o objeto original como a origem de cópia

### Usando PUT para atualizar metadados
{: #node-examples-metadata-put}

**Nota:** a solicitação de `PUT` sobrescreve o conteúdo existente do objeto, portanto, ele deve primeiro ser transferido por download e transferido por upload novamente com os novos metadados


```javascript
function updateMetadataPut(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //retrieve the existing item to reload the contents
    return cos.getObject({
        Bucket: bucketName, 
        Key: itemName
    }).promise()
    .then((data) => {
        //set the new metadata
        var newMetadata = {
            newkey: metaValue
        };

        return cos.putObject({
            Bucket: bucketName,
            Key: itemName,
            Body: data.Body,
            Metadata: newMetadata
        }).promise()
        .then(() => {
            console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
        })
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

### Usando COPY para atualizar metadados
{: #node-examples-metadata-copy}

```javascript
function updateMetadataCopy(bucketName, itemName, metaValue) {
    console.log(`Updating metadata for item: ${itemName}`);

    //set the copy source to itself
    var copySource = bucketName + '/' + itemName;

    //set the new metadata
    var newMetadata = {
        newkey: metaValue
    };

    return cos.copyObject({
        Bucket: bucketName, 
        Key: itemName,
        CopySource: copySource,
        Metadata: newMetadata,
        MetadataDirective: 'REPLACE'
    }).promise()
    .then((data) => {
        console.log(`Updated metadata for item: ${itemName} from bucket: ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```

## Usando o Immutable Object Storage
{: #node-examples-immutable}

### Incluir uma configuração de proteção em um depósito existente
{: #node-examples-immutable-add}

Os objetos gravados em um depósito protegido não podem ser excluídos até que o período de proteção tenha expirado e todas as retenções legais no objeto sejam removidas. O valor de retenção padrão do depósito é fornecido para um objeto, a menos que um valor específico do objeto seja fornecido quando o objeto for criado. Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto. 

Os valores mínimo e máximo suportados para as configurações de período de retenção `MinimumRetention`, `DefaultRetention` e `MaximumRetention` são 0 dias e 365243 dias (1000 anos) respectivamente. 


```js
function addProtectionConfigurationToBucket(bucketName) {
    console.log(`Adding protection to bucket ${bucketName}`);
    return cos.putBucketProtectionConfiguration({
        Bucket: bucketName,
        ProtectionConfiguration: {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 100},
            'MaximumRetention': {'Days': 1000}
        }
    }).promise()
    .then(() => {
        console.log(`Protection added to bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Verificar a proteção em um depósito
{: #node-examples-immutable-check}

```js
function getProtectionConfigurationOnBucket(bucketName) {
    console.log(`Retrieve the protection on bucket ${bucketName}`);
    return cos.getBucketProtectionConfiguration({
        Bucket: bucketName
    }).promise()
    .then((data) => {
        console.log(`Configuration on bucket ${bucketName}:`);
        console.log(data);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Fazer upload de um objeto protegido
{: #node-examples-immutable-upload}

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.

|Valor	| Tipo	| Descrição |
| --- | --- | --- | 
|`Retention-Period` | Número inteiro não negativo (segundos) | O período de retenção para armazenar o objeto em segundos. O objeto não pode ser sobrescrito nem excluído até que a quantia de tempo especificada no período de retenção tenha decorrido. Se esse campo e `Retention-Expiration-Date` forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período `DefaultRetention` do depósito será usado. Zero (`0`) é um valor legal que supõe que o período mínimo de retenção do depósito também é `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | A data na qual será legal excluir ou modificar o objeto. É possível especificar somente isso ou o cabeçalho Retention-Period. Se ambos forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período DefaultRetention do depósito será usado. |
| `Retention-legal-hold-id` | sequência| Uma única retenção legal para aplicar ao objeto. Uma retenção legal é uma sequência longa de caracteres Y. O objeto não pode ser sobrescrito nem excluído até que todas as retenções legais associadas ao objeto sejam removidas. |

```js
function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
    console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
    return cos.putObject({
        Bucket: bucketName,
        Key: objectName,
        Body: 'body',
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then((data) => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
    console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
    return cos.copyObject({
        Bucket: destinationBucketName,
        Key: newObjectName,
        CopySource: sourceBucketName + '/' + sourceObjectName,
        RetentionDirective: 'Copy'
    }).promise()
    .then((data) => {
        console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Incluir ou remover uma retenção legal para ou de um objeto protegido
{: #node-examples-immutable-legal-hold}

O objeto pode suportar 100 retenções legais:

*  Um identificador de retenção legal é uma sequência de comprimento máximo de 64 caracteres e um comprimento mínimo de 1 caractere. Os caracteres válidos são letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `.
* Se a adição de uma determinada retenção legal exceder 100 retenções legais totais no objeto, a nova retenção legal não será incluída e um erro `400` será retornado.
* Se um identificador for muito longo, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador contiver caracteres inválidos, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador já estiver em uso em um objeto, a retenção legal existente não será modificada e a resposta indicará que o identificador já estava em uso com um erro `409`.
* Se um objeto não tiver metadados de período de retenção, um erro `400` será retornado e a inclusão ou remoção de uma retenção legal não será permitida.

O usuário que faz a inclusão ou remoção de uma retenção legal deve ter as permissões `Manager` para esse depósito.

```js
function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
    console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
    return cos.client.addLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
    console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
    return cos.client.deleteLegalHold({
        Bucket: bucketName,
        Key: objectId,
        RetentionLegalHoldId: legalHoldId
    }).promise()
    .then(() => {
        console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Ampliar o período de retenção de um objeto protegido
{: #node-examples-immutable-extend}

O período de retenção de um objeto pode somente ser ampliado. Ele não pode ser diminuído do valor configurado atualmente.

O valor de expansão de retenção é configurado de uma de três maneiras:

* tempo adicional do valor atual (`Additional-Retention-Period` ou método semelhante)
* novo período de extensão em segundos (`Extend-Retention-From-Current-Time` ou método semelhante)
* nova data de validade de retenção do objeto (`New-Retention-Expiration-Date` ou método semelhante)

O período de retenção atual armazenado nos metadados do objeto é aumentado pelo tempo adicional fornecido ou substituído pelo novo valor, dependendo do parâmetro que está configurado na solicitação `extendRetention`. Em todos os casos, o parâmetro de ampliação de retenção é verificado com relação ao período de retenção atual e o parâmetro ampliado será aceito somente se o período de retenção atualizado for maior que o período de retenção atual.

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.

```js
function extendRetentionPeriodOnObject(bucketName, objectName, additionalSeconds) {
    console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalSeconds} seconds.`);
    return cos.extendObjectRetention({
        Bucket: bucketName,
        Key: objectName,
        AdditionalRetentionPeriod: additionalSeconds
    }).promise()
    .then((data) => {
        console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}

### Listar retenções legais em um objeto protegido
{: #node-examples-immutable-list-holds}

Essa operação retorna:

* Data de criação do objeto
* Período de retenção do objeto em segundos
* Data de expiração de retenção calculada com base no período e data de criação
* Lista de retenções legais
* Identificador de retenção legal
* Registro de data e hora em que a retenção legal foi aplicada

Se não houver retenções legais no objeto, um `LegalHoldSet` vazio será retornado.
Se não houver nenhum período de retenção especificado no objeto, um erro `404` será retornado.


```js
function listLegalHoldsOnObject(bucketName, objectName) {
    console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
    return cos.listLegalHolds({
        Bucket: bucketName,
        Key: objectId
    }).promise()
    .then((data) => {
        console.log(`Legal holds on bucket ${bucketName}: ${data}`);
    })
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}
```
{: codeblock}
{: javascript}
