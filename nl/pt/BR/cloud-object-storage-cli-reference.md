---

copyright:
  years: 2017, 2018, 2019
lastupdated: "26-06-2019"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:note: .note}

# Usar a CLI do IBM Cloud
{: #ic-use-the-ibm-cli}

O plug-in Cloud Object Storage amplia a interface da linha de comandos (CLI) do IBM Cloud com um wrapper de API para trabalhar com os recursos do Object Storage.

## Pré-requisitos
{: #ic-prerequisites}
* Uma conta do [IBM Cloud](https://cloud.ibm.com/)
* Uma instância do [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision)
* A [CLI do IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## Instalação e configuração
{: #ic-installation}

O plug-in é compatível com os sistemas operacionais Windows, Linux e macOS que são executados em processadores de 64 bits.

Instale o plug-in usando o comando `plugin install`.

```
ibmcloud plugin install cloud-object-storage
```

Quando o plug-in estiver instalado, será possível configurar o plug-in usando o comando [`ibmcloud cos cos config`](#configure-the-program). Isso pode ser usado para preencher o plug-in com suas credenciais, local de download padrão, escolher sua autenticação, etc.

O programa também oferece a capacidade de configurar o diretório local padrão para arquivos transferidos por download e de configurar uma região padrão. Para configurar o local de download padrão, digite `ibmcloud cos config ddl` e insira no programa um caminho de arquivo válido. Para configurar uma região padrão, digite `ibmcloud cos config region` e forneça uma entrada para o programa de um código de região, como `us-south`. Por padrão, esse valor é configurado como `us-geo`.


Se você está usando a autenticação IAM, deve-se fornecer um CRN para usar alguns dos comandos. Para configurar o CRN, é possível digitar `ibmcloud cos config crn` e fornecer seu CRN. É possível localizar o CRN com `ibmcloud resource service-instance INSTANCE_NAME`. Como alternativa, é possível abrir o console baseado na web, selecionar **Credenciais de serviço** na barra lateral e criar um novo conjunto de credenciais (ou visualizar um arquivo de credencial existente que você já criou).

É possível visualizar suas credenciais atuais do Cloud Object Storage, solicitando `ibmcloud cos config list`. Como o arquivo de configuração é gerado pelo plug-in, é melhor não editar o arquivo manualmente.

### Credenciais HMAC
{: #ic-hmac-credentials}

Se preferir, as [credenciais HMAC do ID de serviço](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac) poderão ser usadas em vez de sua chave de API. Execute `ibmcloud cos config hmac` para inserir as credenciais HMAC e, em seguida, alterne o método de autorização usando `ibmcloud cos config auth`.

Se escolher usar a autenticação do token com sua própria chave de API, não será necessário fornecer nenhuma credencial, pois o programa autenticará você automaticamente.
{: note}

A qualquer momento, para alternar entre a autenticação HMAC e a IAM, é possível digitar `ibmcloud cos config auth`. Para obter mais informações sobre autenticação e autorização no IBM Cloud, consulte a [Documentação do Identity and Access Management](/docs/iam?topic=iam-iamoverview).

## Índice de comando
{: #ic-command-index}

| Comandos |  |  |
| --- | --- | --- |
| [`abort-multipart-upload`](#abort-a-multipart-upload) | [`complete-multipart-upload`](#complete-a-multipart-upload) | [`config`](#configure-the-program) |
| [`copy-object`](#copy-object-from-bucket) | [`create-bucket`](#create-a-new-bucket) | [`create-multipart-upload`](#create-a-new-multipart-upload) |
| [`delete-bucket`](#delete-an-existing-bucket) | [`delete-bucket-cors`](#delete-bucket-cors) | [`delete-object`](#delete-an-object) |
| [`delete-objects`](#delete-multiple-objects) | [`download`](#download-objects-using-s3manager) | [`get-bucket-class`](#get-a-buckets-class) | 
| [`get-bucket-cors`](#get-bucket-cors) | [`get-bucket-location`](#find-a-bucket) | [`get-object`](#download-an-object) |
| [`head-bucket`](#get-a-buckets-headers) | [`head-object`](#get-an-objects-headers) | [`list-buckets`](#list-all-buckets) | 
| [`list-buckets-extended`](#extended-bucket-listing) | [`list-multipart-uploads`](#list-in-progress-multipart-uploads) | [`list-objects`](#list-objects) |
| [`list-parts`](#list-parts) | [`put-bucket-cors`](#set-bucket-cors) | [`put-object`](#upload-an-object) |
| [`upload`](#upload-objects-using-s3manager) | [`upload-part`](#upload-a-part) | [`upload-part-copy`](#upload-a-part-copy) |
| [`wait`](#wait) |  |  |

Cada operação listada abaixo tem uma explicação do que ela faz, como usá-la e quaisquer parâmetros opcionais ou necessários. A menos que especificados como opcionais, quaisquer parâmetros listados são necessários.

O plug-in da CLI não suporta o conjunto completo de recursos disponíveis no Object Storage, como o Aspera High-Speed Transfer, o Immutable Object Storage, a criação de depósitos do Key Protect ou os Firewalls de depósito.
{: note}

### Interromper um upload de múltiplas partes
{: #ic-abort-multipart-upload}
* **Ação:** interrompa uma instância de upload de múltiplas partes, terminando o upload para o depósito na conta do IBM Cloud Object Storage do usuário.
* **Uso:** `ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* O ID de upload que identifica o upload de múltiplas partes.
		* Sinalização: `--upload-id ID`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Concluir um upload de múltiplas partes
{: #ic-complete-multipart-upload}
* **Ação:** conclua uma instância de upload de múltiplas partes, montando as partes atualmente transferidas por upload e fazendo upload do arquivo para o depósito na conta do IBM Cloud Object Storage do usuário.
* **Uso:** `ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* O ID de upload que identifica o upload de múltiplas partes.
		* Sinalização: `--upload-id ID`
	* A STRUCTURE de MultipartUpload a ser configurada.
		* Sinalização: `--multipart-upload STRUCTURE`
		* Sintaxe de abreviação:  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* Sintaxe de JSON:  
	`--multipart-upload file://<filename.json>`  
	O comando `--multipart-upload` usa uma estrutura JSON que descreve as partes do upload de múltiplas partes que devem ser remontadas no arquivo completo. Neste exemplo, o prefixo `file://` é usado para carregar a estrutura JSON do arquivo especificado.
		```
			{
  			"Parts": [
    			{
     			 "ETag": "string",
     			 "PartNumber": integer
    			}
    			...
  				]
			}
		```
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


## Controlando manualmente os uploads de múltiplas partes
{: #ic-manual-multipart-uploads}

A CLI do IBM Cloud Object Storage fornece a capacidade de os usuários fazerem upload de arquivos grandes em múltiplas partes usando as funções de upload de múltiplas partes. Para iniciar um novo upload de múltiplas partes, execute o comando `create-multipart-upload`, que retorna o ID de upload da nova instância de upload. Para continuar com o processo de upload, deve-se salvar o ID de upload de cada comando subsequente.

Depois de ter executado o comando `complete-multipart-upload`, execute `upload-part` para cada parte de arquivo que você deseja fazer upload. **Para uploads de múltiplas partes, cada parte de arquivo (exceto a última parte) deve ter pelo menos 5 MB de tamanho.** Para dividir um arquivo em partes separadas, é possível executar `split` em uma janela do terminal. Por exemplo, se você tiver um arquivo de 13 MB denominado `TESTFILE` em sua Área de trabalho e desejar dividi-lo em partes de arquivo de 5 MB cada, será possível executar `split -b 3m ~/Desktop/TESTFILE part-file-`. Esse comando gera três partes de arquivo em duas partes de arquivo de 5 MB cada e uma parte de arquivo de 3 MB, com os nomes `part-file-aa`, `part-file-ab` e `part-file-ac`.

Conforme cada parte de arquivo é transferida por upload, a CLI imprime seu ETag. Deve-se salvar esse ETag em um arquivo JSON formatado, juntamente com o número da parte. Use este modelo para criar seu próprio arquivo de dados JSON de ETag.

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "The ETag of the first file part goes here."
    },
    {
      "PartNumber": 2,
      "ETag": "The ETag of the second file part goes here."
    }
  ]
}
```

Inclua mais entradas para esse modelo JSON conforme necessário.

Para ver o status de sua instância de upload de múltiplas partes, é possível sempre executar o comando `upload-part`, fornecendo o nome do depósito, a chave e o ID de upload. Isso imprime informações brutas sobre a instância de upload de múltiplas partes. Depois de ter concluído o upload de cada parte do arquivo, execute o comando `complete-multipart-upload` com os parâmetros necessários. Se tudo correr bem, você receberá uma confirmação de que o arquivo foi transferido por upload com êxito para o depósito desejado.

### Configurar o programa
{: #ic-config}
* **Ação:** configure as preferências do programa.
* **Uso:** `ibmcloud cos config [COMMAND]`
* **Comandos:**
	* Alterne entre a autenticação HMAC e IAM.
		* Comando: `auth`
	* Armazene o CRN na configuração.
		* Comando: `crn`
	* Armazene o local de download padrão na configuração.
		* Comando: `ddl`
	* Armazene as credenciais HMAC na configuração.
		* Comando: `hmac`
	* Liste a configuração.
		* Comando: `list`
	* Armazene a região padrão na configuração.
		* Comando: `region`
	* Alterne entre o estilo de URL de VHost e de caminho.
		* Comando: `url-style`


### Copiar objeto do depósito
{: #ic-copy-object}
* **Ação:** copie um objeto do depósito de origem para o depósito de destino.
* **Uso:** `ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* (SOURCE) O nome do depósito de origem e o nome da chave do objeto de origem, que é separado por uma barra (/). Deve ser codificado por URL.
		* Sinalização: `--copy-source SOURCE`
	* _Opcional_: especifica `CACHING_DIRECTIVES` para a cadeia de solicitação e resposta.
		* Sinalização: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica as informações de apresentação (`DIRECTIVES`).
		* Sinalização: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica quais codificações de conteúdo (CONTENT_ENCODING) são aplicadas ao objeto e, portanto, quais mecanismos de decodificação devem ser aplicados para obter o tipo de mídia referenciado pelo campo de cabeçalho Content-Type.
		* Sinalização: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: a LANGUAGE em que o conteúdo está.
		* Sinalização: `--content-language LANGUAGE`
	* _Opcional_: um tipo MIME padrão que descreve o formato dos dados do objeto.
		* Sinalização: `--content-type MIME`
	* _Opcional_: copia o objeto se sua tag de entidade (Etag) corresponde à tag especificada (ETAG).
		* Sinalização: `--copy-source-if-match ETAG`
	* _Opcional_: copia o objeto caso ele tenha sido modificado desde o horário especificado (TIMESTAMP).
		* Sinalização: `--copy-source-if-modified-since TIMESTAMP`
	* _Opcional_: copia o objeto caso sua tag de entidade (ETag) seja diferente da tag especificada (ETAG).
		* Sinalização: `--copy-source-if-none-match ETAG`
	* _Opcional_: copia o objeto caso ele não tenha sido modificado desde o horário especificado (TIMESTAMP).
		* Sinalização: `--copy-source-if-unmodified-since TIMESTAMP`
	* _Opcional_: um MAP dos metadados a serem armazenados. Sintaxe: KeyName1=string,KeyName2=string
		* Sinalização: `--metadata MAP`
	* _Opcional_: especifica se os metadados são copiados do objeto de origem ou substituídos pelos metadados fornecidos na solicitação. Valores de DIRECTIVE: COPY,REPLACE.
		* Sinalização: ` --metadata-directive DIRECTIVE`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Criar um novo depósito
{: #ic-create-bucket}

* **Ação:** crie um depósito em uma instância do IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* Observe que se deve fornecer um CRN se você está usando a autenticação IAM. Isso pode ser configurado usando o comando [`ibmcloud cos config crn`](#configure-the-program).
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: o nome da Classe.
		* Sinalização: `--class CLASS_NAME`
	* _Opcional_: configura o ID da instância de serviço IBM na solicitação.
		* Sinalização: `--ibm-service-instance-id ID`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`



### Criar um novo upload de múltiplas partes
{: #ic-create-multipart-upload}
* **Ação:** inicie o processo de upload de arquivo de várias partes criando uma nova instância de upload de múltiplas partes.
* **Uso:** `ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* _Opcional_: especifica `CACHING_DIRECTIVES` para a cadeia de solicitação e resposta.
		* Sinalização: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica as informações de apresentação (`DIRECTIVES`).
		* Sinalização: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica a codificação de conteúdo (`CONTENT_ENCODING`) do objeto.
		* Sinalização: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: a LANGUAGE em que o conteúdo está.
		* Sinalização: `--content-language LANGUAGE`
	* _Opcional_: um tipo MIME padrão que descreve o formato dos dados do objeto.
		* Sinalização: `--content-type MIME`
	* _Opcional_: um MAP dos metadados a serem armazenados. Sintaxe: KeyName1=string,KeyName2=string
		* Sinalização: `--metadata MAP`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Excluir um depósito existente
{: #ic-delete-bucket}

* **Ação:** exclua um depósito existente em uma instância do IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
    * _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
       * Sinalização: `--region REGION`
    * _Opcional_: a operação não solicitará confirmação.
       * Sinalização: `--force`
    * _Opcional_: a saída retornada no formato JSON bruto.
       * Sinalização: `--json`


### Excluir o CORS de depósito
{: #ic-delete-bucket-cors}
* **Ação:** exclua a configuração de CORS em um depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Excluir um objeto
{: #ic-delete-object}
* **Ação:** exclua um objeto de um depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
  * _Opcional_: a operação não solicitará confirmação.
  	* Sinalização: `--force`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Excluir múltiplos objetos
{: #ic-delete-objects}
* **Ação:** exclua múltiplos objetos de um depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.  
		* Sinalização: `--bucket BUCKET_NAME`  
	* Uma STRUCTURE usando a sintaxe de abreviação ou JSON.  
		* Sinalização: `--delete STRUCTURE`  
		* Sintaxe de abreviação:  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* Sintaxe de JSON:  
	`--delete file://<filename.json>`  
	O comando `--delete` toma uma estrutura JSON que descreve as partes do upload de múltiplas partes que devem ser remontadas no arquivo completo. Neste exemplo, o prefixo `file://` é usado para carregar a estrutura JSON do arquivo especificado.
	```
	{
  	"Objects": [
    	{
    	"Key": "string",
    	"VersionId": "string"
    	}
    ...
  	],
  	"Quiet": true|false
	}
	```
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Fazer download de objetos usando o S3Manager
{: #ic-download-s3manager}
* **Ação:** faça download de objetos do S3 simultaneamente.
* **Uso:** `ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Parâmetros a serem fornecidos:**
	* O nome (BUCKET_NAME) do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* _Opcional_: o número de goroutines para girar em paralelo por chamada para Fazer upload ao enviar partes. O valor padrão é 5.
		* Sinalização: `--concurrency value`
	* _Opcional_: o TAMANHO do buffer (em bytes) a ser usado ao armazenar dados em buffer em chunks e terminá-los como partes para S3. O tamanho de parte mínimo permitido é de 5 MB.
		* Sinalização: `--part-size SIZE`
	* _Opcional_: retorne o objeto somente se sua entitytag (ETag) for a mesma que a ETAG especificada, caso contrário, retorne um 412 (condição prévia com falha).
		* Sinalização: `--if-match ETAG`
	* _Opcional_: retorne o objeto somente se ele tiver sido modificado desde o TIMESTAMP especificado, caso contrário, retorne um 304 (não modificado).
		* Sinalização: `--if-modified-since TIMESTAMP`
	* _Opcional_: retorne o objeto somente se sua tag de entidade (ETag) for diferente da ETAG especificada, caso contrário, retorne um 304 (não modificado).
		* Sinalização: `--if-none-match ETAG`
	* _Opcional_: retorne o objeto somente se ele não tiver sido modificado desde o TIMESTAMP especificado, caso contrário, retorne um 412 (condição prévia com falha).
		* Sinalização: `--if-unmodified-since TIMESTAMP`
	* _Opcional_: faz download dos bytes de RANGE especificados de um objeto. Para obter mais informações sobre o cabeçalho do Intervalo de HTTP, [clique aqui](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35).
		* Sinalização: `--range RANGE`
	* _Opcional_: configura o HEADER Cache-Control da resposta.
		* Sinalização: `--response-cache-control HEADER`
	* _Opcional_: configura o HEADER Content-Disposition da resposta.
		* Sinalização: `--response-content-disposition HEADER`
	* _Opcional_: configura o HEADER Content-Encoding da resposta.
		* Sinalização: `--response-content-encoding HEADER`
	* _Opcional_: configura o HEADER Content-Language da resposta.
		* Sinalização: `--response-content-language HEADER`
	* _Opcional_: configura o HEADER Content-Type da resposta.
		* Sinalização: `--response-content-type HEADER`
	* _Opcional_: configura o HEADER Expires da resposta.
		* Sinalização: `--response-expires HEADER`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`
	* _Opcional_: o local no qual salvar o conteúdo do objeto. Se esse parâmetro não for fornecido, o programa usará o local padrão.
		* Parâmetro: `OUTFILE`


### Obter a classe de um depósito
{: #ic-bucket-class}
* **Ação:** Determine a classe de um depósito em uma instância do IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Obter o CORS de depósito
{: #ic-get-bucket-cors}
* **Ação:** retorna a configuração de CORS para o depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
  * O nome do depósito.  
    * Sinalização: `--bucket BUCKET_NAME`
  * _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
    * Sinalização: `--region REGION`
  * _Opcional_: a saída retornada no formato JSON bruto.
    * Sinalização: `--json`


### Localizar um depósito
{: #ic-find-bucket}
* **Ação:** determine a região e a classe de um depósito em uma instância do IBM Cloud Object Storage. 
* **Uso:** `ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`
	


### Fazer download de um objeto
{: #ic-download-object}
* **Ação:** faça download de um objeto de um depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* _Opcional_: retorne o objeto somente se sua tag de entidade (ETag) for a mesma que a ETAG especificada, caso contrário, retorne um 412 (condição prévia com falha).
		* Sinalização: `--if-match ETAG`
	* _Opcional_: retorne o objeto somente se ele tiver sido modificado desde o TIMESTAMP especificado, caso contrário, retorne um 304 (não modificado).
		* Sinalização: `--if-modified-since TIMESTAMP`
	* _Opcional_: retorne o objeto somente se sua tag de entidade (ETag) for diferente da ETAG especificada, caso contrário, retorne um 304 (não modificado).
		* Sinalização: `--if-none-match ETAG`
	* _Opcional_: retorne o objeto somente se ele não tiver sido modificado desde o TIMESTAMP especificado, caso contrário, retorne um 412 (condição prévia com falha).
		* Sinalização: `--if-unmodified-since TIMESTAMP`
	* _Opcional_: faz download dos bytes de RANGE especificados de um objeto. 
		* Sinalização: `--range RANGE`
	* _Opcional_: configura o HEADER Cache-Control da resposta.
		* Sinalização: `--response-cache-control HEADER`
	* _Opcional_: configura o HEADER Content-Disposition da resposta.
		* Sinalização: `--response-content-disposition HEADER`
	* _Opcional_: configura o HEADER Content-Encoding da resposta.
		* Sinalização: `--response-content-encoding HEADER`
	* _Opcional_: configura o HEADER Content-Language da resposta.
		* Sinalização: `--response-content-language HEADER`
	* _Opcional_: configura o HEADER Content-Type da resposta.
		* Sinalização: `--response-content-type HEADER`
	* _Opcional_: configura o HEADER Expires da resposta.
		* Sinalização: `--response-expires HEADER`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`
	* _Opcional_: o local no qual salvar o conteúdo do objeto. Se esse parâmetro não for fornecido, o programa usará o local padrão.
		* Parâmetro: `OUTFILE`


### Obter os cabeçalhos de um depósito
{: #ic-bucket-header}
* **Ação:** determine se um depósito existe em uma instância do IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Obter cabeçalhos de um objeto
{: #ic-object-header}
* **Ação:** determine se um arquivo existe em um depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* _Opcional_: retorne o objeto somente se sua tag de entidade (ETag) for a mesma que a ETAG especificada, caso contrário, retorne um 412 (condição prévia com falha).
		* Sinalização: `--if-match ETAG`
	* _Opcional_: retorne o objeto somente se ele tiver sido modificado desde o TIMESTAMP especificado, caso contrário, retorne um 304 (não modificado).
		* Sinalização: `--if-modified-since TIMESTAMP`
	* _Opcional_: retorne o objeto somente se sua tag de entidade (ETag) for diferente da ETAG especificada, caso contrário, retorne um 304 (não modificado).
		* Sinalização: `--if-none-match ETAG`
	* _Opcional_: retorne o objeto somente se ele não tiver sido modificado desde o TIMESTAMP especificado, caso contrário, retorne um 412 (condição prévia com falha).
		* Sinalização: `--if-unmodified-since TIMESTAMP`
	* Faz download dos bytes de RANGE especificados de um objeto.
		* Sinalização: `--range RANGE`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Listar todos os depósitos
{: #ic-list-buckets}
* **Ação:** imprima uma lista de todos os depósitos na conta do IBM Cloud Object Storage de um usuário. Os depósitos podem estar localizados em regiões diferentes.
* **Uso:** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* Observe que se deve fornecer um CRN se você está usando a autenticação IAM. Isso pode ser configurado usando o comando [`ibmcloud cos config crn`](#configure-the-program).
* **Parâmetros a serem fornecidos:**
  * Nenhum parâmetro a ser fornecido.
	* _Opcional_: configura o ID da instância de serviço IBM na solicitação.
		* Sinalização: `--ibm-service-instance-id`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Listagem ampliada de depósitos
{: #ic-extended-bucket-listing}
* **Ação:** imprima uma lista de todos os depósitos na conta do IBM Cloud Object Storage de um usuário. Os depósitos podem estar localizados em regiões diferentes.
* **Uso:** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* Observe que se deve fornecer um CRN se você está usando a autenticação IAM. Isso pode ser configurado usando o comando [`ibmcloud cos config crn`](#configure-the-program).
* **Parâmetros a serem fornecidos:**
  * Nenhum parâmetro a ser fornecido.
	* _Opcional_: configura o ID da instância de serviço IBM na solicitação.
		* Sinalização: `--ibm-service-instance-id`
	* _Opcional_: especifica a KEY com a qual iniciar ao listar objetos em um depósito.
		* Sinalização: `--marker KEY`
	* _Opcional_: limita a resposta a chaves que iniciam com o PREFIX especificado.
		* Sinalização: `--prefix PREFIX`
	* _Opcional_: o SIZE de cada página para entrar na chamada de serviço. Isso não afeta o número de itens retornados na saída do comando. A configuração de um tamanho de página menor resulta em mais chamadas para o serviço, recuperando menos itens em cada chamada. Isso pode ajudar a evitar que as chamadas de serviço atinjam o tempo limite.
		* Sinalização: `--page-size SIZE`
	* _Opcional_: o NUMBER total de itens a serem retornados na saída do comando.
		* Sinalização: `--max-items NUMBER`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Listar uploads de múltiplas partes em andamento
{: #ic-list-multipart-uploads}
* **Ação:** lista os uploads de múltiplas partes em andamento.
* **Uso:** `ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: um DELIMITER é um caractere que você usa para agrupar chaves.
		* Sinalização: `--delimiter DELIMITER`
	* _Opcional_: solicita para codificar as chaves de objeto na resposta e especifica o METHOD de codificação a ser usado.
		* Sinalização: `--encoding-type METHOD`
	* _Opcional_: limita a resposta a chaves que iniciam com o PREFIX especificado.
		* Sinalização: `--prefix PREFIX`
	* _Opcional_: junto com o upload-id-marker, esse parâmetro especifica o upload de múltiplas partes após o qual a listagem deve ser iniciada.
		* Sinalização: `--key-marker value`
	* _Opcional_: junto com o key-marker, especifica o upload de múltiplas partes após o qual a listagem deve ser iniciada. Se key-marker não for especificado, o parâmetro upload-id-marker será ignorado.
		* Sinalização: `--upload-id-marker value`
	* _Opcional_: o SIZE de cada página para entrar na chamada de serviço. Isso não afeta o número de itens retornados na saída do comando. A configuração de um tamanho de página menor resulta em mais chamadas para o serviço, recuperando menos itens em cada chamada. Isso pode ajudar a evitar que as chamadas de serviço atinjam o tempo limite. (Padrão: 1000).
		* Sinalização: `--page-size SIZE`
	* _Opcional_: o NUMBER total de itens a serem retornados na saída do comando. Se o número total de itens disponíveis for maior do que o valor especificado, um NextToken será fornecido na saída do comando. Para continuar a paginação, forneça o valor de NextToken no argumento starting-token de um comando subsequente. (Padrão: 0).
		* Sinalização: `--max-items NUMBER`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Listar objetos
{: #ic-list-objects}
* **Ação:** liste arquivos presentes em um depósito na conta do IBM Cloud Object Storage de um usuário. Essa operação está atualmente limitada aos 1.000 objetos criados mais recentemente e não pode ser filtrada.
* **Uso:** `ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: um DELIMITER é um caractere que você usa para agrupar chaves.
		* Sinalização: `--delimiter DELIMITER`
	* _Opcional_: solicita para codificar as chaves de objeto na resposta e especifica o METHOD de codificação a ser usado.
		* Sinalização: `--encoding-type METHOD`
	* _Opcional_: limita a resposta a chaves que iniciam com o PREFIX especificado.
		* Sinalização: `--prefix PREFIX`
	* _Opcional_: um TOKEN para especificar onde iniciar a paginação. Esse é o NextToken de uma resposta truncada anteriormente.
		* Sinalização: `--starting-token TOKEN`
	* _Opcional_: o SIZE de cada página para entrar na chamada de serviço. Isso não afeta o número de itens retornados na saída do comando. A configuração de um tamanho de página menor resulta em mais chamadas para o serviço, recuperando menos itens em cada chamada. Isso pode ajudar a evitar que as chamadas de serviço atinjam o tempo limite. (Padrão: 1000)
		* Sinalização: `--page-size SIZE`
	* _Opcional_: o NUMBER total de itens a serem retornados na saída do comando. Se o número total de itens disponíveis for maior do que o valor especificado, um NextToken será fornecido na saída do comando. Para continuar a paginação, forneça o valor de NextToken no argumento starting-token de um comando subsequente. (Padrão: 0)
		* Sinalização: `--max-items NUMBER`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Listar partes
{: #ic-list-parts}
* **Ação:** imprima informações sobre uma instância de upload de múltiplas partes em andamento.
* **Uso:** `ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* O ID de upload que identifica o upload de múltiplas partes.
		* Sinalização: `--upload-id ID`
	* O VALUE de número de parte após o qual a listagem é iniciada (padrão: 1)
		* Sinalização: `--part-number-marker VALUE`
	* _Opcional_: o SIZE de cada página para entrar na chamada de serviço. Isso não afeta o número de itens retornados na saída do comando. A configuração de um tamanho de página menor resulta em mais chamadas para o serviço, recuperando menos itens em cada chamada. Isso pode ajudar a evitar que as chamadas de serviço atinjam o tempo limite. (Padrão: 1000)
		* Sinalização: `--page-size SIZE`
	* _Opcional_: o NUMBER total de itens a serem retornados na saída do comando. Se o número total de itens disponíveis for maior do que o valor especificado, um NextToken será fornecido na saída do comando. Para continuar a paginação, forneça o valor de NextToken no argumento starting-token de um comando subsequente. (Padrão: 0)
		* Sinalização: `--max-items NUMBER`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Configurar o CORS de depósito
{: #ic-set-bucket-cors}
* **Ação:** define a configuração de CORS para um depósito na conta do IBM Cloud Object Storage do usuário.
* **Uso:** `ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* _Opcional_: uma STRUCTURE usando a sintaxe de JSON em um arquivo.
		* Sinalização: `--cors-configuration STRUCTURE`
		* Sintaxe de JSON:  
	`--cors-configuration file://<filename.json>`  
	O comando `--cors-configuration` toma uma estrutura JSON que descreve as partes do upload de múltiplas partes que devem ser remontadas no arquivo completo. Neste exemplo, o prefixo `file://` é usado para carregar a estrutura JSON do arquivo especificado.
	```
	{
  	"CORSRules": [
    	{
      	"AllowedHeaders": ["string", ...],
      	"AllowedMethods": ["string", ...],
      	"AllowedOrigins": ["string", ...],
      	"ExposeHeaders": ["string", ...],
      	"MaxAgeSeconds": integer
    	}
    	...
  	]
	}
	```
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`



### Colocar objeto
{: #ic-upload-object}
* **Ação:** faça upload de um objeto para um depósito na conta do IBM Cloud Object Storage de um usuário.
* **Uso:** `ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
    * O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* _Opcional_: local dos dados do objeto (`FILE_PATH`).
		* Sinalização: `--body FILE_PATH`
	* _Opcional_: especifica `CACHING_DIRECTIVES` para a cadeia de solicitação e resposta.
		* Sinalização: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica as informações de apresentação (`DIRECTIVES`).
		* Sinalização: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica a codificação de conteúdo (`CONTENT_ENCODING`) do objeto.
		* Sinalização: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: a LANGUAGE em que o conteúdo está.
		* Sinalização: `--content-language LANGUAGE`
	* _Opcional_: o SIZE do corpo em bytes. Esse parâmetro é útil quando o tamanho do corpo não pode ser determinado automaticamente. (Padrão: 0)
		* Sinalização: `--content-length SIZE`
	* _Opcional_: a compilação MD5 de 128 bits codificada em base64 dos dados.
		* Sinalização: `--content-md5 MD5`
	* _Opcional_: um tipo MIME padrão que descreve o formato dos dados do objeto.
		* Sinalização: `--content-type MIME`
	* _Opcional_: um MAP dos metadados a serem armazenados. Sintaxe: KeyName1=string,KeyName2=string
		* Sinalização: `--metadata MAP`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Fazer upload de objetos usando o S3Manager
{: #ic-upload-s3manager}
* **Ação:** efetue upload de objetos do S3 simultaneamente.
* **Uso:** `ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parâmetros a serem fornecidos:**
	* O nome (BUCKET_NAME) do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* O PATH para o arquivo para fazer upload.
		* Sinalização: `--file PATH`
	* _Opcional_: o número de goroutines para girar em paralelo por chamada para Fazer upload ao enviar partes. O valor padrão é 5.
		* Sinalização: `--concurrency value`
	* _Opcional_: o número máximo de PARTS que serão transferidas por upload para o S3 que calcula o tamanho da parte do objeto a ser transferido por upload.  O limite é de 10.000 partes.
		* Sinalização: `--max-upload-parts PARTS`
	* _Opcional_: o TAMANHO do buffer (em bytes) a ser usado ao armazenar dados em buffer em chunks e terminá-los como partes para S3. O tamanho de parte mínimo permitido é de 5 MB.
		* Sinalização: `--part-size SIZE`
	* _Opcional_: configurar esse valor como true fará com que o SDK evite chamar o AbortMultipartUpload em uma falha, deixando todas as partes transferidas por upload com êxito no S3 para recuperação manual.
		* Sinalização: `--leave-parts-on-errors`
	* _Opcional_: especifica CACHING_DIRECTIVES para a cadeia de solicitação/resposta.
		* Sinalização: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica informações de apresentação (DIRECTIVES).
		* Sinalização: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica quais codificações de conteúdo (CONTENT_ENCODING) foram aplicadas ao objeto e, portanto, quais mecanismos decodificadores devem ser aplicados para obter o tipo de mídia referenciado pelo campo de cabeçalho Content-Type.
		* Sinalização: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: a LANGUAGE em que o conteúdo está.
		* Sinalização: `--content-language LANGUAGE`
	* _Opcional_: o SIZE do corpo em bytes. Esse parâmetro é útil quando o tamanho do corpo não pode ser determinado automaticamente.
		* Sinalização: `--content-length SIZE`
	* _Opcional_: a compilação MD5 de 128 bits codificada em base64 dos dados.
		* Sinalização: `--content-md5 MD5`
	* _Opcional_: um tipo MIME padrão que descreve o formato dos dados do objeto.
		* Sinalização: `--content-type MIME`
	* _Opcional_: um MAP dos metadados a serem armazenados. Sintaxe: KeyName1=string,KeyName2=string
		* Sinalização: `--metadata MAP`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Fazer upload de uma parte
{: #ic-upload-part}
* **Ação:** faça upload de uma parte de um arquivo em uma instância de upload de múltiplas partes existente.
* **Uso:** `ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* Observe que se deve salvar cada número de parte de arquivo transferido por upload e ETag (que a CLI imprimirá para você) para cada parte em um arquivo JSON. Consulte o "Guia de upload de múltiplas partes" abaixo para obter mais informações.
* **Parâmetros a serem fornecidos:**
	* O nome do depósito no qual o upload de múltiplas partes está ocorrendo.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* O ID de upload que identifica o upload de múltiplas partes.
		* Sinalização: `--upload-id ID`
	* O NUMBER de parte da parte que está sendo transferida por upload. Esse é um número inteiro positivo no intervalo de 1 a 10.000. (Padrão: 1)
		* Sinalização: `--part-number NUMBER`
	* _Opcional_: local dos dados do objeto (`FILE_PATH`).
		* Sinalização: `--body FILE_PATH`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Fazer upload de uma cópia de parte
{: #ic-upload-a-part-copy}
* **Ação:** faça upload de uma parte copiando dados de um objeto existente.
* **Uso:** `ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* Observe que se deve salvar cada número de parte de arquivo transferido por upload e ETag (que a CLI imprimirá para você) para cada parte em um arquivo JSON. Consulte o "Guia de upload de múltiplas partes" para obter mais informações.
* **Parâmetros a serem fornecidos:**
	* O nome do depósito.
		* Sinalização: `--bucket BUCKET_NAME`
	* A KEY do objeto.
		* Sinalização: `--key KEY`
	* O ID de upload que identifica o upload de múltiplas partes.
		* Sinalização: `--upload-id ID`
	* O NUMBER de parte da parte que está sendo transferida por upload. Esse é um número inteiro positivo entre 1 e 10.000.
		* Sinalização: `--part-number PART_NUMBER`
	* (SOURCE) O nome do depósito de origem e o nome da chave do objeto de origem, que é separado por uma barra (/). Deve ser codificado por URL.
		* Sinalização: `--copy-source SOURCE`
	* _Opcional_: copia o objeto se sua tag de entidade (Etag) corresponde à tag especificada (ETAG).
		* Sinalização: `--copy-source-if-match ETAG`
	* _Opcional_: copia o objeto caso ele tenha sido modificado desde o horário especificado (TIMESTAMP).
		* Sinalização: `--copy-source-if-modified-since TIMESTAMP`
	* _Opcional_: copia o objeto caso sua tag de entidade (ETag) seja diferente da tag especificada (ETAG).
		* Sinalização: `--copy-source-if-none-match ETAG`
	* _Opcional_: copia o objeto caso ele não tenha sido modificado desde o horário especificado (TIMESTAMP).
		* Sinalização: `--copy-source-if-unmodified-since TIMESTAMP`
	* _Opcional_: o intervalo de bytes a serem copiados do objeto de origem. O valor do intervalo deve usar o formato bytes=first-last, em que o primeiro e último são os deslocamentos de bytes baseados em zero a serem copiados. Por exemplo, bytes=0-9 indica que você deseja copiar os primeiros dez bytes da origem. Será possível copiar um intervalo somente se o objeto de origem for maior que 5 MB.
		* Sinalização: `--copy-source-range value`
	* _Opcional_: a REGION na qual o depósito está presente. Se essa sinalização não for fornecida, o programa usará a opção padrão que é especificada na configuração.
		* Sinalização: `--region REGION`
	* _Opcional_: a saída retornada no formato JSON bruto.
		* Sinalização: `--json`


### Espera
{: #ic-wait}
* **Ação:** aguarde até que uma condição específica seja satisfeita. Cada subcomando pesquisa uma API até que o requisito listado seja atendido.
* **Uso:** `ibmcloud cos wait command [arguments...] [command options]`
* **Comandos:**
    * `bucket-exists`
  		* Aguarde até que a resposta 200 seja recebida ao pesquisar com o head-bucket. Ele pesquisa a cada 5 segundos até que um estado bem-sucedido tenha sido atingido. Isso sairá com um código de retorno de 255 após 20 verificações com falha.
	* `bucket-not-exists`
		* Aguarde até que a resposta 404 seja recebida ao pesquisar com head-bucket. Ele pesquisa a cada 5 segundos até que um estado bem-sucedido tenha sido atingido. Isso sairá com um código de retorno de 255 após 20 verificações com falha.
	* `object-exists`
		* Aguarde até que a resposta 200 seja recebida ao pesquisar com head-object. Ele pesquisa a cada 5 segundos até que um estado bem-sucedido tenha sido atingido. Isso sairá com um código de retorno de 255 após 20 verificações com falha.
	* `object-not-exists`
		* Aguarde até que a resposta 404 seja recebida ao pesquisar com head-object. Ele pesquisa a cada 5 segundos até que um estado bem-sucedido tenha sido atingido. Isso sairá com um código de retorno de 255 após 20 verificações com falha.

