---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: basics, upload, getting started, ingest

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

# Usar a CLI do AWS
{: #aws-cli}

A interface da linha de comandos oficial para o AWS é compatível com a API S3 do IBM COS. Escrita em Python, ela pode ser instalada por meio do Python Package Index via `pip install awscli`. Por padrão, as chaves de acesso são originadas de `~/.aws/credentials`, mas também podem ser configuradas como variáveis de ambiente.

Estes exemplos foram gerados usando a versão 1.14.2 da CLI. Para verificar a versão instalada, execute `aws --version`.

## Configure a CLI para se conectar ao {{site.data.keyword.cos_short}}
{: #aws-cli-config}

Para configurar a CLI do AWS, digite `aws configure` e forneça suas [credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac) e um nome de região padrão. O "nome da região" usado pelo AWS S3 corresponde ao código de fornecimento (`LocationConstraint`) que o {{site.data.keyword.cos_short}} usa para definir a classe de armazenamento de novos depósitos.

Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes#classes-locationconstraint).

```sh
aws configure
AWS Access Key ID [None]: {Access Key ID}
AWS Secret Access Key [None]: {Secret Access Key}
Default region name [None]: {Provisioning Code}
Default output format [None]: json
```

Isso cria dois arquivos:

 `~/.aws/credentials`:

```
[default]
aws_access_key_id = {Access Key ID}
aws_secret_access_key = {Secret Access Key}
```
{:codeblock}

`~/.aws/config`:

```
[default]
region = {Provisioning Code}
output = json
```
{:codeblock}


Também é possível usar variáveis de ambiente para configurar credenciais HMAC:

```
export AWS_ACCESS_KEY_ID="{Access Key ID}"
export AWS_SECRET_ACCESS_KEY="{Secret Access Key}"
```
{:codeblock}


O terminal do IBM COS deve ser originado usando a opção `--endpoint-url` e não pode ser configurado no arquivo de credenciais.


## Comandos de sintaxe de alto nível
{: #aws-cli-high-level}

Casos de uso simples podem ser realizados usando `aws --endpoint-url {endpoint} s3 <command>`. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints). Os objetos são gerenciados usando os comandos shell familiares, como `ls`, `mv`, `cp` e `rm`. Os depósitos podem ser criados usando `mb` e excluídos usando `rb`.

### Listar todos os depósitos em uma instância de serviço
{: #aws-cli-high-level-list-buckets}

```sh
aws --endpoint-url {endpoint} s3 ls
2016-09-09 12:48  s3://bucket-1
2016-09-16 21:29  s3://bucket-2
```

### Listar objetos em um depósito
{: #aws-cli-high-level-list-objects}

```sh
aws --endpoint-url {endpoint} s3 ls s3://bucket-1
2016-09-28 15:36       837   s3://bucket-1/c1ca2-filename-00001
2016-09-09 12:49       533   s3://bucket-1/c9872-filename-00002
2016-09-28 15:36     14476   s3://bucket-1/98837-filename-00003
2016-09-29 16:24     20950   s3://bucket-1/abfc4-filename-00004
```

### Fazer um novo depósito
{: #aws-cli-high-level-new-bucket}

**Nota**: informações pessoalmente identificáveis (PII): ao criar depósitos e/ou incluir objetos, assegure-se de não usar nenhuma informação que possa identificar qualquer usuário (pessoa natural) por nome, local ou qualquer outro meio.
{:tip}

Se a região padrão no arquivo `~/.aws/config` corresponder ao mesmo local que o terminal escolhido, a criação do depósito será direta.

```sh
aws --endpoint-url {endpoint} s3 mb s3://bucket-1
make_bucket: s3://bucket-1/
```



### Incluir um objeto em um depósito
{: #aws-cli-high-level-upload}

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset.tar.gz
```

Como alternativa, é possível configurar uma nova chave do objeto que seja diferente do nome do arquivo:

```sh
aws --endpoint-url {endpoint} s3 cp large-dataset.tar.gz s3://bucket-1/large-dataset-for-project-x
upload: ./large-dataset.tar.gz to s3://bucket-1/large-dataset-for-project-x
```

### Copiando um objeto de um depósito para outro dentro da mesma região:
{: #aws-cli-high-level-copy}

```bash
$ aws --endpoint-url {endpoint} s3 cp s3://bucket-1/new-file s3://bucket-2/
copy: s3://bucket-1/new-file to s3://bucket-2/new-file
```

### Excluir um objeto de um depósito
{: #aws-cli-high-level-delete-object}
```sh
aws --endpoint-url {endpoint} s3 rm s3://mybucket/argparse-1.2.1.tar.gz
delete: s3://mybucket/argparse-1.2.1.tar.gz
```

### Remover um depósito
{: #aws-cli-high-level-delete-bucket}

```sh
aws --endpoint-url {endpoint} s3 rb s3://bucket-1
remove_bucket: s3://bucket-1/
```

### Criar URLs pré-assinadas
{: #aws-cli-high-level-presign}

A CLI também é capaz de criar URLs pré-assinadas. Isso permite acesso público temporário a objetos sem mudar nenhum controle de acesso existente. A URL que é gerada contém uma assinatura HMAC que ofusca o URI, tornando menos provável que os usuários sem a URL completa possam acessar um arquivo publicamente acessível.

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file
```

Também é possível configurar um prazo de expiração para a URL em segundos (o padrão é 3600):

```bash
$ aws --endpoint-url {endpoint} s3 presign s3://bucket-1/new-file --expires-in 600
```

## Comandos de sintaxe de baixo nível
{: #aws-cli-low-level}

A CLI do AWS também permite chamadas da API diretas que fornecem as mesmas respostas que as solicitações de HTTP diretas, usando o comando `s3api`.

### Listando depósitos:
{: #aws-cli-low-level-list-buckets}

```bash
$ aws --endpoint-url {endpoint} s3api list-buckets
{
    "Owner": {
        "DisplayName": "{storage-account-uuid}",
        "ID": "{storage-account-uuid}"
    },
    "Buckets": [
        {
            "CreationDate": "2016-09-09T12:48:52.442Z",
            "Name": "bucket-1"
        },
        {
            "CreationDate": "2016-09-16T21:29:00.912Z",
            "Name": "bucket-2"
        }
    ]
}
```

### Listando objetos em um depósito
{: #aws-cli-low-level-list-objects}

```sh
$ aws --endpoint-url {endpoint} s3api list-objects --bucket bucket-1
```

```json
{
    "Contents": [
        {
            "LastModified": "2016-09-28T15:36:56.807Z",
            "ETag": "\"13d567d518c650414c50a81805fff7f2\"",
            "StorageClass": "STANDARD",
            "Key": "c1ca2-filename-00001",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 837
        },
        {
            "LastModified": "2016-09-09T12:49:58.018Z",
            "ETag": "\"3ca744fa96cb95e92081708887f63de5\"",
            "StorageClass": "STANDARD",
            "Key": "c9872-filename-00002",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 533
        },
        {
            "LastModified": "2016-09-28T15:36:17.573Z",
            "ETag": "\"a54ed08bcb07c28f89f4b14ff54ce5b7\"",
            "StorageClass": "STANDARD",
            "Key": "98837-filename-00003",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 14476
        },
        {
            "LastModified": "2016-10-06T14:46:26.923Z",
            "ETag": "\"2bcc8ee6bc1e4b8cd2f9a1d61d817ed2\"",
            "StorageClass": "STANDARD",
            "Key": "abfc4-filename-00004",
            "Owner": {
                "DisplayName": "{storage-account-uuid}",
                "ID": "{storage-account-uuid}"
            },
            "Size": 20950
        }
    ]
}
```
