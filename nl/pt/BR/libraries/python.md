---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: python, sdk

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

# Usando Python
{: #python}

O suporte ao Python é fornecido por meio de uma bifurcagem da biblioteca `boto3`. Ele pode ser instalado por meio do Python Package Index via `pip install ibm-cos-sdk`.

O código-fonte pode ser localizado em [GitHub](https://github.com/ibm/ibm-cos-sdk-python/).

A biblioteca `ibm_boto3` fornece acesso completo à API do {{site.data.keyword.cos_full}}. Os terminais, uma chave de API e o ID da instância devem ser especificados durante a criação de um recurso de serviço ou cliente de baixo nível, conforme mostrado nos exemplos básicos a seguir.

O ID da instância de serviço também é referido como um _ID da instância de recurso_. O valor pode ser localizado criando uma [credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) ou por meio da CLI.
{:tip}

A documentação detalhada pode ser localizada [aqui](https://ibm.github.io/ibm-cos-sdk-python/).

## Fazendo upgrade de 1.x.x
{: #python-migrate}

A versão 2.0 do SDK introduz uma mudança de namespace que permite que um aplicativo use a biblioteca `boto3` original para se conectar aos recursos do AWS no mesmo aplicativo ou ambiente. Para migrar de 1.x para 2.0, algumas mudanças são necessárias.

    1. Atualize o `requirements.txt` ou por meio de PyPI via `pip install -U ibm-cos-sdk`. Verifique se não existem versões mais antigas com `pip list | grep ibm-cos`.
    2. Atualize quaisquer declarações de importação de `boto3` para `ibm_boto3`.
    3. Se necessário, reinstale o `boto3` original atualizando o `requirements.txt` ou por meio de PyPI via `pip install boto3`.

## Criando um cliente e credenciais de fornecimento
{: #python-credentials}

Para se conectar ao COS, um cliente é criado e configurado fornecendo informações de credenciais (chave de API e ID da instância de serviço). Esses valores também podem ser originados automaticamente de um arquivo de credenciais ou de variáveis de ambiente.

Depois de gerar uma [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), o documento JSON resultante pode ser salvo em `~/.bluemix/cos_credentials`. O SDK originará automaticamente as credenciais desse arquivo, a menos que outras credenciais sejam explicitamente configuradas durante a criação do cliente. Se o arquivo `cos_credentials` contiver chaves HMAC, o cliente será autenticado com uma assinatura, caso contrário, o cliente usará a chave de API fornecida para autenticar usando um token de acesso.

Se estiver migrando do AWS S3, também será possível originar os dados de credenciais de `~/.aws/credentials` no formato:

```
[default]
aws_access_key_id = {API_KEY}
aws_secret_access_key = {SERVICE_INSTANCE_ID}
```

Se ambos, `~/.bluemix/cos_credentials` e `~/.aws/credentials`, existirem, `cos_credentials` terá a preferência.

### Reunir informações necessárias
{: #python-prereqs}

As variáveis a seguir aparecem nos exemplos:

* `bucket_name` deve ser uma sequência [exclusiva e protegida por DNS](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-new-bucket). Como os nomes dos depósitos são exclusivos em todo o sistema, esses valores precisarão ser mudados se este exemplo for executado múltiplas vezes. Observe que os nomes são reservados por 10 a 15 minutos após a exclusão.
* `ibm_api_key_id` é o valor localizado na [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `apikey`.
* `ibm_service_instance_id` é o valor localizado na [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) como `resource_instance_id`. 
* `endpoint_url` é uma URL de terminal em serviço, inclusive do protocolo `https://`. Esse valor **não** é o valor `endpoints` que está localizado na [Credencial de serviço](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `LocationConstraint` é um [código de fornecimento válido](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint) que corresponde ao valor `endpoint`. 


## Exemplos de código
{: #python-examples}

Exemplos de código foram gravados usando o **Python 2.7.15**

### Inicializando a configuração
{: #python-examples-init}

  
```python
import ibm_boto3
from ibm_botocore.client import Config, ClientError

# Constants for IBM COS values
COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>" # eg "W00YiRnLW4a3fTjMB-oiB-2ySfTrFBIQQWanc--P3byk"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>" # eg "crn:v1:bluemix:public:cloud-object-storage:global:a/3bf0d9003abfb5d29761c3e97696b71c:d6f04d83-6c4f-4a62-a165-696756d63903::"

# Create resource
cos = ibm_boto3.resource("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)
```
*Valores da chave*
* `<endpoint>` - o terminal público para seu armazenamento de objeto de nuvem com esquema prefixado ('https://') (disponível por meio do [IBM Cloud Dashboard](https://cloud.ibm.com/resources){:new_window}). Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).
* `<api-key>` - a chave de api gerada ao criar as credenciais de serviço (o acesso de gravação é necessário para exemplos de criação e exclusão)
* `<resource-instance-id>` - o ID do recurso para seu armazenamento de objeto de nuvem (disponível por meio da [CLI do IBM Cloud](/docs/cli?topic=cloud-cli-idt-cli) ou do [IBM Cloud Dashboard](https://cloud.ibm.com/resources){:new_window})
* `<location>` - o local padrão para seu armazenamento de objeto de nuvem (deve corresponder à região que é usada para `<endpoint>`)

*Referências do SDK*
* [`ServiceResource`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}


### Criando um novo depósito
{: #python-examples-new-bucket}

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes).

```python
def create_bucket(bucket_name):
    print("Creating new bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            }
        )
        print("Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create bucket: {0}".format(e))
```

*Referências do SDK*
* Classes
  * [` Depósito `](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Métodos
    * [`criação`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

### Criando um novo arquivo de texto
{: #python-examples-new-file}

```python
def create_text_file(bucket_name, item_name, file_text):
    print("Creating new item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).put(
            Body=file_text
        )
        print("Item: {0} created!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create text file: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [`Objeto programa`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Métodos
    * [`put`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.put){:new_window}

### Listar depósitos disponíveis
{: #python-examples-list-buckets}

```python
def get_buckets():
    print("Retrieving list of buckets")
    try:
        buckets = cos.buckets.all()
        for bucket in buckets:
            print("Bucket Name: {0}".format(bucket.name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve list buckets: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [ Depósito ](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ServiceResource](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#service-resource){:new_window}
* Collections
    * [depósitos](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.ServiceResource.buckets){:new_window}

### Listar itens em um depósito
{: #python-examples-list-objects}

```python
def get_bucket_contents(bucket_name):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        files = cos.Bucket(bucket_name).objects.all()
        for file in files:
            print("Item: {0} ({1} bytes).".format(file.key, file.size))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [ Depósito ](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
    * [ObjectSummary](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#objectsummary){:new_window}
* Collections
    * [objetos](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.objects){:new_window}

### Obter conteúdo do arquivo de um item específico
{: #python-examples-get-file-contents}

```python
def get_item(bucket_name, item_name):
    print("Retrieving item from bucket: {0}, key: {1}".format(bucket_name, item_name))
    try:
        file = cos.Object(bucket_name, item_name).get()
        print("File Contents: {0}".format(file["Body"].read()))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve file contents: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [`Objeto programa`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Métodos
    * [`get`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.get){:new_window}

### Excluir um item de um depósito
{: #python-examples-delete-object}

```python
def delete_item(bucket_name, item_name):
    print("Deleting item: {0}".format(item_name))
    try:
        cos.Object(bucket_name, item_name).delete()
        print("Item: {0} deleted!".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete item: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [Objeto programa](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
* Métodos
    * [ excluir ](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.delete){:new_window}

### Excluir múltiplos itens de um depósito
{: #python-examples-delete-multiple-objects}

A solicitação de exclusão pode conter um máximo de 1000 chaves que você deseja excluir. Embora isso seja útil na redução da sobrecarga por solicitação, fique atento ao excluir muitas chaves. Além disso, leve em conta os tamanhos dos objetos para assegurar o desempenho adequado.
{:tip}

```python
def delete_items(bucket_name):
    try:
        delete_request = {
            "Objects": [
                { "Key": "deletetest/testfile1.txt" },
                { "Key": "deletetest/testfile2.txt" },
                { "Key": "deletetest/testfile3.txt" },
                { "Key": "deletetest/testfile4.txt" },
                { "Key": "deletetest/testfile5.txt" }
            ]
        }

        response = cos_cli.delete_objects(
            Bucket=bucket_name,
            Delete=delete_request
        )

        print("Deleted items for {0}\n".format(bucket_name))
        print(json.dumps(response.get("Deleted"), indent=4))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to copy item: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Métodos
    * [delete_objects](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.delete_objects){:new_window}

### Excluir um depósito
{: #python-examples-delete-bucket}

```python
def delete_bucket(bucket_name):
    print("Deleting bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).delete()
        print("Bucket: {0} deleted!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to delete bucket: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [ Depósito ](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Métodos
    * [ excluir ](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.delete){:new_window}

### Execute um upload de múltiplas partes
{: #python-examples-multipart}

#### Fazer upload do arquivo binário (método preferencial)
{: #python-examples-multipart-binary}

O método [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window} da classe [S3.Object](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window} executa automaticamente um upload de múltiplas partes quando necessário. A classe [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window} é usada para determinar o limite para usar o upload de múltiplas partes.

```python
def multi_part_upload(bucket_name, item_name, file_path):
    try:
        print("Starting file transfer for {0} to bucket: {1}\n".format(item_name, bucket_name))
        # set 5 MB chunks
        part_size = 1024 * 1024 * 5

        # set threadhold to 15 MB
        file_threshold = 1024 * 1024 * 15

        # set the transfer threshold and chunk size
        transfer_config = ibm_boto3.s3.transfer.TransferConfig(
            multipart_threshold=file_threshold,
            multipart_chunksize=part_size
        )

        # the upload_fileobj method will automatically execute a multi-part upload
        # in 5 MB chunks for all files over 15 MB
        with open(file_path, "rb") as file_data:
            cos.Object(bucket_name, item_name).upload_fileobj(
                Fileobj=file_data,
                Config=transfer_config
            )

        print("Transfer for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [Objeto programa](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#object){:new_window}
    * [TransferConfig](https://ibm.github.io/ibm-cos-sdk-python/reference/customizations/s3.html#s3-transfers){:new_window}
* Métodos
    * [upload_fileobj](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Object.upload_fileobj){:new_window}

#### Executar manualmente um upload de múltiplas partes
{: #python-examples-multipart-manual}

Se desejado, a classe [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} pode ser usada para executar um upload de múltiplas partes. Isso poderá ser útil se mais controle sobre o processo de upload for necessário.

```python
def multi_part_upload_manual(bucket_name, item_name, file_path):
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT
        )

        print("Starting multi-part upload for {0} to bucket: {1}\n".format(item_name, bucket_name))

        # initiate the multi-part upload
        mp = cos_cli.create_multipart_upload(
            Bucket=bucket_name,
            Key=item_name
        )

        upload_id = mp["UploadId"]

        # min 5MB part size
        part_size = 1024 * 1024 * 5
        file_size = os.stat(file_path).st_size
        part_count = int(math.ceil(file_size / float(part_size)))
        data_packs = []
        position = 0
        part_num = 0

        # begin uploading the parts
        with open(file_path, "rb") as file:
            for i in range(part_count):
                part_num = i + 1
                part_size = min(part_size, (file_size - position))

                print("Uploading to {0} (part {1} of {2})".format(item_name, part_num, part_count))

                file_data = file.read(part_size)

                mp_part = cos_cli.upload_part(
                    Bucket=bucket_name,
                    Key=item_name,
                    PartNumber=part_num,
                    Body=file_data,
                    ContentLength=part_size,
                    UploadId=upload_id
                )

                data_packs.append({
                    "ETag":mp_part["ETag"],
                    "PartNumber":part_num
                })

                position += part_size

        # complete upload
        cos_cli.complete_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id,
            MultipartUpload={
                "Parts": data_packs
            }
        )
        print("Upload for {0} Complete!\n".format(item_name))
    except ClientError as be:
        # abort the upload
        cos_cli.abort_multipart_upload(
            Bucket=bucket_name,
            Key=item_name,
            UploadId=upload_id
        )
        print("Multi-part upload aborted for {0}\n".format(item_name))
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to complete multi-part upload: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Métodos
    * [abort_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.abort_multipart_upload){:new_window}
    * [complete_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.complete_multipart_upload){:new_window}
    * [create_multipart_upload](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.create_multipart_upload){:new_window}
    * [upload_part](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.upload_part){:new_window}

### Upload de objeto grande usando o TransferManager
{: #python-examples-multipart-transfer}

O `TransferManager` fornece uma outra maneira de executar grandes transferências de arquivos, incorporando automaticamente uploads de múltiplas partes sempre que necessário configurando parâmetros de configuração.

```python
def upload_large_file(bucket_name, item_name, file_path):
    print("Starting large file upload for {0} to bucket: {1}".format(item_name, bucket_name))

    # set the chunk size to 5 MB
    part_size = 1024 * 1024 * 5

    # set threadhold to 5 MB
    file_threshold = 1024 * 1024 * 5

    # Create client connection
    cos_cli = ibm_boto3.client("s3",
        ibm_api_key_id=COS_API_KEY_ID,
        ibm_service_instance_id=COS_SERVICE_CRN,
        ibm_auth_endpoint=COS_AUTH_ENDPOINT,
        config=Config(signature_version="oauth"),
        endpoint_url=COS_ENDPOINT
    )

    # set the transfer threshold and chunk size in config settings
    transfer_config = ibm_boto3.s3.transfer.TransferConfig(
        multipart_threshold=file_threshold,
        multipart_chunksize=part_size
    )

    # create transfer manager
    transfer_mgr = ibm_boto3.s3.transfer.TransferManager(cos_cli, config=transfer_config)

    try:
        # initiate file upload
        future = transfer_mgr.upload(file_path, bucket_name, item_name)

        # wait for upload to complete
        future.result()

        print ("Large file upload complete!")
    except Exception as e:
        print("Unable to complete large file upload: {0}".format(e))
    finally:
        transfer_mgr.shutdown()
```

### Listar itens em um depósito (v2)
{: #python-examples-list-objects-v2}

O objeto [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} contém um método atualizado para listar o conteúdo ([list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}). Esse método permite limitar o número de registros que são retornados e recuperar os registros em lotes. Isso pode ser útil para paginar seus resultados em um aplicativo e melhorar o desempenho.

```python
def get_bucket_contents_v2(bucket_name, max_keys):
    print("Retrieving bucket contents from: {0}".format(bucket_name))
    try:
        # create client object
        cos_cli = ibm_boto3.client("s3",
            ibm_api_key_id=COS_API_KEY_ID,
            ibm_service_instance_id=COS_SERVICE_CRN,
            ibm_auth_endpoint=COS_AUTH_ENDPOINT,
            config=Config(signature_version="oauth"),
            endpoint_url=COS_ENDPOINT)

        more_results = True
        next_token = ""

        while (more_results):
            response = cos_cli.list_objects_v2(Bucket=bucket_name, MaxKeys=max_keys, ContinuationToken=next_token)
            files = response["Contents"]
            for file in files:
                print("Item: {0} ({1} bytes).".format(file["Key"], file["Size"]))

            if (response["IsTruncated"]):
                next_token = response["NextContinuationToken"]
                print("...More results in next batch!\n")
            else:
                more_results = False
                next_token = ""

        log_done()
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to retrieve bucket contents: {0}".format(e))
```

*Referências do SDK*
* Classes
    * [S3.Client](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window}
* Métodos
    * [list_objects_v2](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Client.list_objects_v2){:new_window}

## Usando o Key Protect
{: #python-examples-kp}
O Key Protect pode ser incluído em um depósito de armazenamento para criptografar dados sensíveis em repouso na nuvem.

### Antes de iniciar
{: #python-examples-kp-prereqs}

Os itens a seguir são necessários para criar um depósito com o Key-Protect ativado:

* Um serviço Key Protect [provisionado](/docs/services/key-protect?topic=key-protect-provision)
* Uma chave Raiz disponível ([gerada](/docs/services/key-protect?topic=key-protect-create-root-keys#create_root_keys) ou [importada](/docs/services/key-protect?topic=key-protect-import-root-keys#import_root_keys))

### Recuperando o CRN da chave raiz
{: #python-examples-kp-root}

1. Recupere o [ID da instância](/docs/services/key-protect?topic=key-protect-retrieve-instance-ID#retrieve-instance-ID) para seu serviço Key Protect
2. Use a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api) para recuperar todas as suas [chaves disponíveis](https://cloud.ibm.com/apidocs/key-protect)
    * É possível usar comandos `curl` ou um Cliente REST de API, como [Postman](/docs/services/cloud-object-storage?topic=cloud-object-storage-postman), para acessar a [API do Key Protect](/docs/services/key-protect?topic=key-protect-set-up-api#set-up-api).
3. Recupere o CRN da chave raiz que você usa para ativar o Key Protect em seu depósito. O CRN é semelhante ao abaixo:

`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`

### Criando um depósito com o Key-Protect ativado
{: #python-examples-kp-new-bucket}
```python
COS_KP_ALGORITHM = "<algorithm>"
COS_KP_ROOTKEY_CRN = "<root-key-crn>"

# Create a new bucket with key protect (encryption)
def create_bucket_kp(bucket_name):
    print("Creating new encrypted bucket: {0}".format(bucket_name))
    try:
        cos.Bucket(bucket_name).create(
            CreateBucketConfiguration={
                "LocationConstraint":COS_BUCKET_LOCATION
            },
            IBMSSEKPEncryptionAlgorithm=COS_KP_ALGORITHM,
            IBMSSEKPCustomerRootKeyCrn=COS_KP_ROOTKEY_CRN
        )
        print("Encrypted Bucket: {0} created!".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to create encrypted bucket: {0}".format(e))
```

*Valores da chave*
* `<algorithm>` - o algoritmo de criptografia que é usado para novos objetos incluídos no depósito (o padrão é AES256).
* `<root-key-crn>` - o CRN da Chave raiz que é obtida do serviço Key Protect.

*Referências do SDK*
* Classes
    * [ Depósito ](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#bucket){:new_window}
* Métodos
    * [criação](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#S3.Bucket.create){:new_window}

## Usando o Aspera High-Speed Transfer
{: #python-examples-aspera}

Instalando a [biblioteca do Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera#aspera-packaging), é possível alavancar as transferências de arquivos em alta velocidade em seu aplicativo. A biblioteca Aspera é de origem fechada e, portanto, uma dependência opcional para o SDK do COS (que usa uma licença do Apache).

Cada sessão do Aspera cria um processo `ascp` individual que é executado na máquina do cliente para executar a transferência. Assegure-se de que seu ambiente de computação possa permitir que esse processo seja executado.
{:tip}


### Inicializando o AsperaTransferManager
{: #python-examples-aspera-init}

Antes de inicializar o `AsperaTransferManager`, certifique-se de que você tenha um objeto [`client`](https://ibm.github.io/ibm-cos-sdk-python/reference/services/s3.html#client){:new_window} em funcionamento (não um objeto `resource` ou `session`).

```python
import ibm_boto3
from ibm_botocore.client import Config
from ibm_s3transfer.aspera.manager import AsperaTransferManager

COS_ENDPOINT = "<endpoint>" # Current list avaiable at https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints
COS_API_KEY_ID = "<api-key>"
COS_AUTH_ENDPOINT = "https://iam.cloud.ibm.com/identity/token"
COS_RESOURCE_CRN = "<resource-instance-id>"
COS_BUCKET_LOCATION = "<location>"

# Create resource
cos = ibm_boto3.client("s3",
    ibm_api_key_id=COS_API_KEY_ID,
    ibm_service_instance_id=COS_RESOURCE_CRN,
    ibm_auth_endpoint=COS_AUTH_ENDPOINT,
    config=Config(signature_version="oauth"),
    endpoint_url=COS_ENDPOINT
)

transfer_manager = AsperaTransferManager(cos)
```

É necessário fornecer uma Chave de API do IAM para os Aspera high-speed transfers. As [Credenciais HMAC](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials#service-credentials-iam-hmac){:new_window} **NÃO** são suportadas atualmente. Para obter mais informações sobre o IAM, [clique aqui](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview#getting-started-with-iam).
{:tip}

Para obter o rendimento mais alto, divida a transferência em um número especificado de **sessões** paralelas que enviam chunks de dados cujo tamanho é definido por um valor do **limite**.

A configuração típica para usar múltiplas sessões deve ser:
* Taxa de destino de 2500 MBps
* Limite de 100 MB (*esse é o valor recomendado para a maioria dos aplicativos*)

```python
ms_transfer_config = AsperaConfig(multi_session="all",
                                  target_rate_mbps=2500,
                                  multi_session_threshold_mb=100)
```
No exemplo acima, o sdk gerará sessões suficientes para tentar atingir a taxa de destino de 2500 MBps.

Como alternativa, o gerenciamento de sessões pode ser configurado explicitamente no sdk. Isso é útil em casos em que o controle mais preciso sobre a utilização de rede é desejado.

A configuração típica para usar múltiplas sessões explícitas deve ser:
* 2 ou 10 sessões
* Limite de 100 MB (*esse é o valor recomendado para a maioria dos aplicativos*)

```python
from ibm_s3transfer.aspera.manager import AsperaConfig
# Configure 2 sessions for transfer
ms_transfer_config = AsperaConfig(multi_session=2,
                                  multi_session_threshold_mb=100)

# Create the Aspera Transfer Manager
transfer_manager = AsperaTransferManager(client=client,
                                         transfer_config=ms_transfer_config)
```
Para obter melhor desempenho na maioria dos cenários, sempre use múltiplas sessões para minimizar qualquer sobrecarga associada à instanciação de um Aspera high-speed transfer. **Se sua capacidade de rede for pelo menos 1 Gbps, será necessário usar 10 sessões.** As redes de largura da banda inferior devem usar duas sessões.
{:tip}

### Upload de arquivo
{: #python-examples-aspera-upload}
```python
bucket_name = "<bucket-name>"
upload_filename = "<absolute-path-to-file>"
object_name = "<item-name>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload(upload_filename, bucket_name, object_name)

    # Wait for upload to complete
    future.result()
```

*Valores da chave*
* `<bucket-name>` - o nome do depósito de destino
* `<absolute-path-to-file>` - o caminho do diretório e o nome do arquivo para o arquivo a ser transferido por upload
* `<item-name>` - o nome do novo arquivo incluído no depósito

### Download de arquivo
{: #python-examples-aspera-download}

```python
bucket_name = "<bucket-name>"
download_filename = "<absolute-path-to-file>"
object_name = "<object-to-download>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download(bucket_name, object_name, download_filename)

    # Wait for download to complete
    future.result()
```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado.
* `<absolute-path-to-file>` - o diretório e o nome do arquivo no qual salvar o arquivo no sistema local.
* `<object-to-download>` - o nome do arquivo no depósito a ser transferido por download.

### Upload de diretório
{: #python-examples-aspera-directory-upload}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY, and have objects in it.
local_upload_directory = "<absolute-path-to-directory>"
# THIS SHOULD NOT HAVE A LEADING "/"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Perform upload
    future = transfer_manager.upload_directory(local_upload_directory, bucket_name, remote_directory)

    # Wait for upload to complete
    future.result()
```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado
* `<absolute-path-to-directory>` - o diretório local que contém os arquivos a serem transferidos por upload. Deve-se ter uma `/` inicial e final (ou seja, `/Users/testuser/Documents/Upload/`)
* `<object prefix>` - o nome do diretório no depósito para armazenar os arquivos. Ele não deve ter uma barra inicial `/` (ou seja, `newuploads/`)

### Download de diretório
{: #python-examples-aspera-directory-download}

```python
bucket_name = "<bucket-name>"
# THIS DIRECTORY MUST EXIST LOCALLY
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Create Transfer manager
with AsperaTransferManager(client) as transfer_manager:

    # Get object with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory)

    # Wait for download to complete
    future.result()
```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado
* `<absolute-path-to-directory>` - o diretório local para salvar os arquivos transferidos por download. Deve-se ter uma barra inicial e final `/` (ou seja, `/Users/testuser/Downloads/`)
* `<object prefix>` - o nome do diretório no depósito para armazenar os arquivos. Ele não deve ter uma barra inicial `/` (ou seja, `todownload/`)

### Usando assinantes
{: #python-examples-aspera-subscribers}

Os assinantes fornecem a observabilidade em transferências, anexando métodos de retorno de chamada customizados. Todas as transferências executam a transição entre as fases a seguir:

`Queued - In Progress - Done`

Há três assinantes disponíveis para cada fase:

* `CallbackOnQueued()` - chamado quando uma nova transferência tiver sido incluída no `AsperaTransferManager`
* `CallbackOnProgress()` - chamado quando uma transferência tiver começado a transmitir dados (disparados repetidamente enquanto a transferência está em andamento).
* `CallbackOnDone()` - chamado quando a transferência é concluída

```python
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

# Subscriber callbacks
class CallbackOnQueued(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_queued(self, future, **kwargs):
        print("Directory download queued.")

class CallbackOnProgress(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_progress(self, future, bytes_transferred, **kwargs):
        print("Directory download in progress: %s bytes transferred" % bytes_transferred)

class CallbackOnDone(AsperaBaseSubscriber):
    def __init__(self):
        pass

    def on_done(self, future, **kwargs):
        print("Downloads complete!")

# Create Transfer manager
transfer_manager = AsperaTransferManager(client)

# Attach subscribers
subscribers = [CallbackOnQueued(), CallbackOnProgress(), CallbackOnDone()]

# Get object with Aspera
future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, subscribers)

# Wait for download to complete
future.result()
```

*Valores da chave*
* `<bucket-name>` - o nome do depósito em sua instância de serviço do Object Storage que tem o Aspera ativado
* `<absolute-path-to-directory>` - o diretório local para salvar os arquivos transferidos por download. Deve-se ter uma barra inicial e final `/` (ou seja, `/Users/testuser/Downloads/`)
* `<object prefix>` - o nome do diretório no depósito para armazenar os arquivos. Ele não deve ter uma barra inicial `/` (ou seja, `todownload/`)

O código de amostra acima produz a saída a seguir:

```
Directory download queued.
Directory download in progress: 5632 bytes transferred
Directory download in progress: 1047552 bytes transferred
...
Directory download in progress: 53295130 bytes transferred
Directory download in progress: 62106855 bytes transferred
Download complete!
```

### Pausar/Continuar/Cancelar
{: #python-examples-aspera-pause}

O SDK fornece a capacidade de gerenciar o progresso de transferências de arquivo/diretório por meio dos métodos a seguir do objeto `AsperaTransferFuture`:

* `pause()`
* `resume()`
* `cancel()`

Não há efeitos colaterais de chamar qualquer um dos métodos descritos acima. A limpeza e a manutenção adequadas são manipuladas pelo SDK.
{:tip}

```python
# Create Transfer manager
bucket_name = "<bucket-name>"
local_download_directory = "<absolute-path-to-directory>"
remote_directory = "<object prefix>"

with AsperaTransferManager(client) as transfer_manager:

    # download a directory with Aspera
    future = transfer_manager.download_directory(bucket_name, remote_directory, local_download_directory, None, None)

    # pause the transfer
    future.pause()

    # resume the transfer
    future.resume()

    # cancel the transfer
    future.cancel()
```

### Resolução de problemas do Aspera
{: #python-examples-aspera-ts}
**Problema:** os desenvolvedores que usam Python 2.7.15 no Windows 10 podem ter falhas ao instalar o Aspera SDK.

**Causa:** se houver versões diferentes do Python instaladas em seu ambiente, você poderá encontrar falhas de instalação ao tentar instalar o Aspera SDK. Isso pode ser causado por um arquivo DLL ausente ou DLL errado no caminho.

**Solução:** a primeira etapa para resolver esse problema seria reinstalar as bibliotecas do Aspera. Pode ter havido uma falha durante a instalação. Como resultado, isso pode ter afetado os arquivos DLL. Se isso não resolver os problemas, será necessário atualizar sua versão do Python. Se não for possível fazer isso, será possível usar a instalação do [Intel® Distribution for Python*](https://software.intel.com/en-us/distribution-for-python){:new_window}. Isso permitirá que você instale o Aspera SDK sem nenhum problema.

## Atualizando os metadados
{: #python-examples-metadata}
Há duas maneiras de atualizar os metadados em um objeto existente:
* Uma solicitação `PUT` com os novos metadados e o conteúdo do objeto original
* Executando uma solicitação de `COPY` com os novos metadados especificando o objeto original como a origem de cópia

### Usando PUT para atualizar metadados
{: #python-examples-metadata-put}
**Nota:** a solicitação de `PUT` sobrescreve o conteúdo existente do objeto, portanto, ele deve primeiro ser transferido por download e transferido por upload novamente com os novos metadados

```python
def update_metadata_put(bucket_name, item_name, key, value):
    try:
        # retrieve the existing item to reload the contents
        response = cos_cli.get_object(Bucket=bucket_name, Key=item_name)
        existing_body = response.get("Body").read()

        # set the new metadata
        new_metadata = {
            key: value
        }

        cos_cli.put_object(Bucket=bucket_name, Key=item_name, Body=existing_body, Metadata=new_metadata)

        print("Metadata update (PUT) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```

### Usando COPY para atualizar metadados
{: #python-examples-metadata-copy}

```python
def update_metadata_copy(bucket_name, item_name, key, value):
    try:
        # set the new metadata
        new_metadata = {
            key: value
        }

        # set the copy source to itself
        copy_source = {
            "Bucket": bucket_name,
            "Key": item_name
        }

        cos_cli.copy_object(Bucket=bucket_name, Key=item_name, CopySource=copy_source, Metadata=new_metadata, MetadataDirective="REPLACE")

        print("Metadata update (COPY) for {0} Complete!\n".format(item_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        log_error("Unable to update metadata: {0}".format(e))
```

## Usando o Immutable Object Storage
{: #python-examples-immutable}

### Incluir uma configuração de proteção em um depósito existente
{: #python-examples-immutable-add}

Os objetos gravados em um depósito protegido não podem ser excluídos até que o período de proteção tenha expirado e todas as retenções legais no objeto sejam removidas. O valor de retenção padrão do depósito é fornecido para um objeto, a menos que um valor específico do objeto seja fornecido quando o objeto for criado. Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto. 

Os valores mínimo e máximo suportados para as configurações de período de retenção `MinimumRetention`, `DefaultRetention` e `MaximumRetention` são 0 dias e 365243 dias (1000 anos) respectivamente. 

```py
def add_protection_configuration_to_bucket(bucket_name):
    try:
        new_protection_config = {
            "Status": "Retention",
            "MinimumRetention": {"Days": 10},
            "DefaultRetention": {"Days": 100},
            "MaximumRetention": {"Days": 1000}
        }

        cos.put_bucket_protection_configuration(Bucket=bucket_name, ProtectionConfiguration=new_protection_config)

        print("Protection added to bucket {0}\n".format(bucket_name))
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to set bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}

### Verificar a proteção em um depósito
{: #python-examples-immutable-check}
```py
def get_protection_configuration_on_bucket(bucket_name):
    try:
        response = cos.get_bucket_protection_configuration(Bucket=bucket_name)
        protection_config = response.get("ProtectionConfiguration")

        print("Bucket protection config for {0}\n".format(bucket_name))
        print(protection_config)
        print("\n")
    except ClientError as be:
        print("CLIENT ERROR: {0}\n".format(be))
    except Exception as e:
        print("Unable to get bucket protection config: {0}".format(e))
```
{: codeblock}
{: python}


### Fazer upload de um objeto protegido
{: #python-examples-immutable-upload}

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.


|Valor	| Tipo	| Descrição |
| --- | --- | --- | 
|`Retention-Period` | Número inteiro não negativo (segundos) | O período de retenção para armazenar o objeto em segundos. O objeto não pode ser sobrescrito nem excluído até que a quantia de tempo especificada no período de retenção tenha decorrido. Se esse campo e `Retention-Expiration-Date` forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período `DefaultRetention` do depósito será usado. Zero (`0`) é um valor legal que supõe que o período mínimo de retenção do depósito também é `0`. |
| `Retention-expiration-date` | Data (formato ISO 8601) | A data na qual será legal excluir ou modificar o objeto. É possível especificar somente isso ou o cabeçalho Retention-Period. Se ambos forem especificados, um erro `400` será retornado. Se nenhum for especificado, o período DefaultRetention do depósito será usado. |
| `Retention-legal-hold-id` | string | Uma única retenção legal para aplicar ao objeto. Uma retenção legal é uma sequência longa de caracteres Y. O objeto não pode ser sobrescrito nem excluído até que todas as retenções legais associadas ao objeto sejam removidas. |

```py
def put_object_add_legal_hold(bucket_name, object_name, file_text, legal_hold_id):
    print("Add legal hold {0} to {1} in bucket {2} with a putObject operation.\n".format(legal_hold_id, object_name, bucket_name))
    
    cos.put_object(
        Bucket=bucket_name,
        Key=object_name,
        Body=file_text, 
        RetentionLegalHoldId=legal_hold_id)

    print("Legal hold {0} added to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))
  
def copy_protected_object(source_bucket_name, source_object_name, destination_bucket_name, new_object_name):
    print("Copy protected object {0} from bucket {1} to {2}/{3}.\n".format(source_object_name, source_bucket_name, destination_bucket_name, new_object_name))

    copy_source = {
        "Bucket": source_bucket_name,
        "Key": source_object_name
    }

    cos.copy_object(
        Bucket=destination_bucket_name, 
        Key=new_object_name, 
        CopySource=copy_source, 
        RetentionDirective="Copy"
    )

    print("Protected object copied from {0}/{1} to {2}/{3}\n".format(source_bucket_name, source_object_name, destination_bucket_name, new_object_name));

def complete_multipart_upload_with_retention(bucket_name, object_name, upload_id, retention_period):
    print("Completing multi-part upload for object {0} in bucket {1}\n".format(object_name, bucket_name))

    cos.complete_multipart_upload(
        Bucket=bucket_name, 
        Key=object_name,
        MultipartUpload={
            "Parts":[{
                "ETag": part["ETag"],
                "PartNumber": 1
            }]
        },
        UploadId=upload_id,
        RetentionPeriod=retention_period
    )

    print("Multi-part upload completed for object {0} in bucket {1}\n".format(object_name, bucket_name))

def upload_file_with_retention(bucket_name, object_name, path_to_file, retention_period):
    print("Uploading file {0} to object {1} in bucket {2}\n".format(path_to_file, object_name, bucket_name))
    
    args = {
        "RetentionPeriod": retention_period
    }

    cos.upload_file(
        Filename=path_to_file,
        Bucket=bucket_name,
        Key=object_name,
        ExtraArgs=args
    )

    print("File upload complete to object {0} in bucket {1}\n".format(object_name, bucket_name))
```
{: codeblock}
{: python}

### Incluir ou remover uma retenção legal para ou de um objeto protegido
{: #python-examples-immutable-legal-hold}

O objeto pode suportar 100 retenções legais:

*  Um identificador de retenção legal é uma sequência de comprimento máximo de 64 caracteres e um comprimento mínimo de 1 caractere. Os caracteres válidos são letras, números, `!`, `_`, `.`, `*`, `(`, `)`, `-` e `.
* Se a adição de uma determinada retenção legal exceder 100 retenções legais totais no objeto, a nova retenção legal não será incluída e um erro `400` será retornado.
* Se um identificador for muito longo, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador contiver caracteres inválidos, ele não será incluído no objeto e um erro `400` será retornado.
* Se um identificador já estiver em uso em um objeto, a retenção legal existente não será modificada e a resposta indicará que o identificador já estava em uso com um erro `409`.
* Se um objeto não tiver metadados de período de retenção, um erro `400` será retornado e a inclusão ou remoção de uma retenção legal não será permitida.


O usuário que faz a inclusão ou remoção de uma retenção legal deve ter as permissões `Manager` para esse depósito.


```py
def add_legal_hold_to_object(bucket_name, object_name, legal_hold_id):
    print("Adding legal hold {0} to object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.add_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} added to object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))

def delete_legal_hold_from_object(bucket_name, object_name, legal_hold_id):
    print("Deleting legal hold {0} from object {1} in bucket {2}\n".format(legal_hold_id, object_name, bucket_name))

    cos.delete_legal_hold(
        Bucket=bucket_name,
        Key=object_name,
        RetentionLegalHoldId=legal_hold_id
    )

    print("Legal hold {0} deleted from object {1} in bucket {2}!\n".format(legal_hold_id, object_name, bucket_name))
```
{: codeblock}
{: python}

### Ampliar o período de retenção de um objeto protegido
{: #python-examples-immutable-extend}

O período de retenção de um objeto pode somente ser ampliado. Ele não pode ser diminuído do valor configurado atualmente.

O valor de expansão de retenção é configurado de uma de três maneiras:

* tempo adicional do valor atual (`Additional-Retention-Period` ou método semelhante)
* novo período de extensão em segundos (`Extend-Retention-From-Current-Time` ou método semelhante)
* nova data de validade de retenção do objeto (`New-Retention-Expiration-Date` ou método semelhante)

O período de retenção atual armazenado nos metadados do objeto é aumentado pelo tempo adicional fornecido ou substituído pelo novo valor, dependendo do parâmetro que está configurado na solicitação `extendRetention`. Em todos os casos, o parâmetro de ampliação de retenção é verificado com relação ao período de retenção atual e o parâmetro ampliado será aceito somente se o período de retenção atualizado for maior que o período de retenção atual.

Os objetos em depósitos protegidos que não estão mais sob retenção (o período de retenção expirou e o objeto não tem nenhuma retenção legal), quando sobrescritos, ficarão novamente sob retenção. O novo período de retenção pode ser fornecido como parte da solicitação de sobrescrição do objeto ou o tempo de retenção padrão do depósito será fornecido para o objeto.



```py
def extend_retention_period_on_object(bucket_name, object_name, additional_seconds):
    print("Extend the retention period on {0} in bucket {1} by {2} seconds.\n".format(object_name, bucket_name, additional_seconds))

    cos.extend_object_retention(
        Bucket=bucket_ame,
        Key=object_name,
        AdditionalRetentionPeriod=additional_seconds
    )

    print("New retention period on {0} is {1}\n".format(object_name, additional_seconds))
```
{: codeblock}
{: python}

### Listar retenções legais em um objeto protegido
{: #python-examples-immutable-list-holds}

Essa operação retorna:

* Data de criação do objeto
* Período de retenção do objeto em segundos
* Data de expiração de retenção calculada com base no período e data de criação
* Lista de retenções legais
* Identificador de retenção legal
* Registro de data e hora em que a retenção legal foi aplicada

Se não houver retenções legais no objeto, um `LegalHoldSet` vazio será retornado.
Se não houver nenhum período de retenção especificado no objeto, um erro `404` será retornado.


```py 
def list_legal_holds_on_object(bucket_name, object_name):
    print("List all legal holds on object {0} in bucket {1}\n".format(object_name, bucket_name));

    response = cos.list_legal_holds(
        Bucket=bucket_name,
        Key=object_name
    )

    print("Legal holds on bucket {0}: {1}\n".format(bucket_name, response))
```
{: codeblock}
{: python}

