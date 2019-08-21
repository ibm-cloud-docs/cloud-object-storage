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

# Utilización de la CLI de IBM Cloud
{: #ic-use-the-ibm-cli}

El plugin Cloud Object Storage amplía la interfaz de línea de mandatos (CLI) de IBM Cloud con un derivador de API para trabajar con recursos de Object Storage.

## Requisitos previos
{: #ic-prerequisites}
* Una cuenta de [IBM Cloud](https://cloud.ibm.com/)
* Una instancia de [IBM Cloud Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-gs-dev#gs-dev-provision)
* La [CLI de IBM Cloud](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud_cli)


## Instalación y configuración
{: #ic-installation}

El plugin es compatible con los sistemas operativos Windows, Linux y macOS que se ejecutan en procesadores de 64 bits.

Instale el plugin con el mandato `plugin install`.

```
ibmcloud plugin install cloud-object-storage
```

Una vez instalado el plugin, puede configurarlo con el mandato [`ibmcloud cos config`](#configure-the-program). Esto se puede utilizar para llenar el plugin con sus credenciales, la ubicación de descarga predeterminada, la autenticación elegida, etc.

El programa también ofrece la posibilidad de configurar el directorio local predeterminado para los archivos descargados y de establecer una región predeterminada. Para establecer la ubicación de descarga predeterminada, escriba `ibmcloud cos config ddl` y especifique en el programa una vía de acceso de archivo válida. Para establecer una región predeterminada, escriba `ibmcloud cos config region` y especifique en el programa un código de región, como por ejemplo `us-south`. El valor predeterminado es `us-geo`.


Si utiliza la autenticación de IAM, debe proporcionar un CRN para utilizar algunos de los mandatos. Para establecer el CRN, puede escribir `ibmcloud cos config crn` y especificar el CRN. Para encontrar el CRN, escriba `ibmcloud resource service-instance INSTANCE_NAME`.  De forma alternativa, puede abrir la consola web, seleccionar **Credenciales de servicio** en la barra lateral y crear un nuevo conjunto de credenciales (o visualizar un archivo de credenciales existente que ya haya creado).

Para ver sus credenciales actuales de Cloud Object Storage, escriba `ibmcloud cos config list`. Como el plugin genera el archivo de configuración, es mejor no editar el archivo de forma manual.

### Credenciales de HMAC
{: #ic-hmac-credentials}

Si lo prefiere, puede utilizar [credenciales de HMAC de ID de servicio](/docs/services/cloud-object-storage?topic=cloud-object-storage-hmac) en lugar de la clave de API. Ejecute `ibmcloud cos config hmac` para especificar las credenciales de HMAC y luego cambie el método de autorización mediante `ibmcloud cos config auth`.

Si opta por utilizar la autenticación de señal con su propia clave de API, no es necesario que proporcione ninguna credencial ya que el programa le autentica automáticamente.
{: note}

En cualquier momento, para conmutar entre la autenticación de HMAC e IAM, puede escribir `ibmcloud cos config auth`. Para obtener más información sobre la autenticación y la autorización en IBM Cloud, consulte la [documentación de Identity and Access Management](/docs/iam?topic=iam-iamoverview).

## Índice de mandatos
{: #ic-command-index}

| Mandatos |  |  |
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

Cada operación que se muestra a continuación tiene una explicación de lo que hace, cómo usarla y los parámetros opcionales y obligatorios. A menos que se especifique como opcional, todo parámetro de la lista es obligatorio.

El plugin de CLI no da soporte a la suite completa de características disponibles en Object Storage, como la transferencia de alta velocidad de Aspera, Immutable Object Storage, la creación de grupos de Key Protect o los cortafuegos de grupo.
{: note}

### Terminación anómala de una carga de varias partes
{: #ic-abort-multipart-upload}
* **Acción:** terminar de forma anómala una instancia de carga de varias partes finalizando la carga en el grupo en la cuenta de IBM Cloud Object Storage del usuario.
* **Uso:** `ibmcloud cos abort-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* ID de carga que identifica la carga de varias partes.
		* Distintivo: `--upload-id ID`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Finalización de una carga de varias partes
{: #ic-complete-multipart-upload}
* **Acción:** finalizar una instancia de carga de varias partes ensamblando las partes cargadas y cargando el archivo en el grupo de la cuenta la cuenta de IBM Cloud Object Storage del usuario.
* **Uso:** `ibmcloud cos complete-multipart-upload --bucket BUCKET_NAME --key KEY --upload-id ID --multipart-upload STRUCTURE [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* ID de carga que identifica la carga de varias partes.
		* Distintivo: `--upload-id ID`
	* La estructura (STRUCTURE) de MultipartUpload que se va a definir.
		* Distintivo: `--multipart-upload STRUCTURE`
		* Sintaxis abreviada:  
		`--multipart-upload 'Parts=[{ETag=string,PartNumber=integer},{ETag=string,PartNumber=integer}]'`
		* Sintaxis JSON:  
	`--multipart-upload file://<filename.json>`  
	El mandato `--multipart-upload` toma una estructura JSON que describe las partes de la carga de varias partes que se deben volver a ensamblar en el archivo completo. En este ejemplo, se utiliza el prefijo `file://` para cargar la estructura JSON desde el archivo especificado.
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
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


## Control manual de las cargas de varias partes
{: #ic-manual-multipart-uploads}

La CLI de IBM Cloud Object Storage ofrece la posibilidad de que los usuarios carguen archivos de gran tamaño en varias partes utilizando las funciones de carga de varias partes. Para iniciar una nueva carga de varias partes, ejecute el mandato `create-multipart-upload`, que devuelve el ID de carga de la nueva instancia de carga. Para continuar con el proceso de carga, debe guardar el ID de carga para cada mandato siguiente.

Cuando haya ejecutado el mandato `complete-multipart-upload`, ejecute `upload-part` para cada parte de archivo que desee cargar. **Para las cargas de varias partes, cada parte del archivo (excepto la última parte) debe tener un tamaño de al menos 5 MB.** Para dividir un archivo en varias partes, puede ejecutar `split` en una ventana de terminal. Por ejemplo, si tiene un archivo de 13 MB llamado `TESTFILE` en el escritorio y desea dividirlo en partes de archivo de 5 MB cada una, puede ejecutar `split -b 3m ~/Desktop/TESTFILE part-file-`. Este mandato genera tres partes del archivo en dos partes de archivo de 5 MB cada una, y una parte de archivo de 3 MB, llamadas `part-file-aa`, `part-file-ab` y `part-file-ac`.

A medida que se carga cada parte del archivo, la CLI imprime su ETag. Debe guardar este ETag en un archivo JSON con formato, junto con el número de pieza. Utilice esta plantilla para crear su propio archivo de datos JSON ETag.

```
{
  "Parts": [
    {
      "PartNumber": 1,
      "ETag": "Aquí va el ETag de la primera parte del archivo."
    },
    {
      "PartNumber": 2,
      "ETag": "Aquí va el ETag de la segunda parte del archivo."
    }
  ]
}
```

Añada más entradas a esta plantilla JSON según sea necesario.

Para ver el estado de la instancia de carga de varias partes, siempre puede ejecutar el mandato `upload-part`, especificando el nombre del grupo, la clave y el ID de carga. Esto muestra información sin formato sobre la instancia de la carga de varias partes. Cuando termine de cargar cada parte del archivo, ejecute el mandato `complete-multipart-upload` con los parámetros necesarios. Si todo funciona correctamente, recibirá una confirmación de que el archivo se ha cargado correctamente en el grupo deseado.

### Configuración del programa
{: #ic-config}
* **Acción:** configurar las preferencias del programa.
* **Uso:** `ibmcloud cos config [COMMAND]`
* **Mandatos:**
	* Cambiar entre autenticación de HMAC y de IAM.
		* Mandato: `auth`
	* Guardar CRN en la configuración.
		* Mandato: `crn`
	* Guardar la ubicación de descarga predeterminada en la configuración.
		* Mandato: `ddl`
	* Guardar las credenciales de HMAC en la configuración.
		* Mandato: `hmac`
	* Mostrar la configuración.
		* Mandato: `list`
	* Guardar la región predeterminada en la configuración.
		* Mandato: `region`
	* Cambiar entre el estilo VHost y Path URL.
		* Mandato: `url-style`


### Copia de un objeto del grupo
{: #ic-copy-object}
* **Acción:** copiar un objeto del grupo de origen en el grupo de destino.
* **Uso:** `ibmcloud cos copy-object --bucket BUCKET_NAME --key KEY --copy-source SOURCE [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--metadata MAP] [--metadata-directive DIRECTIVE] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* (SOURCE) El nombre del grupo de origen y el nombre de clave del objeto de origen, separado por una barra inclinada (/). Debe tener codificación URL.
		* Distintivo: `--copy-source SOURCE`
	* _Opcional_: especifica `CACHING_DIRECTIVES` para la cadena de solicitud y respuesta.
		* Distintivo: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica la información de presentación (`DIRECTIVES`).
		* Distintivo: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica las codificaciones de contenido (CONTENT_ENCODING) que se aplican al objeto y, por lo tanto, los mecanismos de decodificación que se deben aplicar para obtener el valor media-type referenciado por el campo de cabecera Content-Type.
		* Distintivo: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: el idioma (LANGUAGE) en el que está el contenido.
		* Distintivo: `--content-language LANGUAGE`
	* _Opcional_: un tipo MIME estándar que describe el formato de los datos del objeto.
		* Distintivo: `--content-type MIME`
	* _Opcional_: copia el objeto si la etiqueta de entidad (Etag) coincide con la etiqueta especificada (ETAG).
		* Distintivo: `--copy-source-if-match ETAG`
	* _Opcional_: copia el objeto si se ha modificado desde la hora especificada (TIMESTAMP).
		* Distintivo: `--copy-source-if-modified-since TIMESTAMP`
	* _Opcional_: copia el objeto si la etiqueta de entidad (ETag) es distinta de la etiqueta especificada (ETAG).
		* Distintivo: `--copy-source-if-none-match ETAG`
	* _Opcional_: copia el objeto si no se ha modificado desde la hora especificada (TIMESTAMP).
		* Distintivo: `--copy-source-if-unmodified-since TIMESTAMP`
	* _Opcional_: un mapa (MAP) de los metadatos que se van a almacenar. Sintaxis: KeyName1=string,KeyName2=string
		* Distintivo: `--metadata MAP`
	* _Opcional_: especifica si los metadatos se copian desde el objeto de origen o si se sustituyen por los metadatos que se proporcionan en la solicitud. Valores de DIRECTIVE: COPY,REPLACE.
		* Distintivo: ` --metadata-directive DIRECTIVE`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Creación de un nuevo grupo
{: #ic-create-bucket}

* **Acción:** crear un grupo en una instancia de IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos create-bucket --bucket BUCKET_NAME [--class CLASS_NAME] [--ibm-service-instance-id ID] [--region REGION] [--json]`
	* Tenga en cuenta que debe proporcionar un CRN si utiliza la autenticación de IAM. Esto se puede establecer con el mandato [`ibmcloud cos config crn`](#configure-the-program).
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: el nombre de la clase.
		* Distintivo: `--class CLASS_NAME`
	* _Opcional_: establece el ID de instancia de servicio de IBM en la solicitud.
		* Distintivo: `--ibm-service-instance-id ID`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`



### Creación de una nueva carga de varias partes
{: #ic-create-multipart-upload}
* **Acción:** comenzar el proceso de carga de un archivo en varias partes creando una nueva instancia de carga de varias partes.
* **Uso:** `ibmcloud cos create-multipart-upload --bucket BUCKET_NAME --key KEY [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* _Opcional_: especifica `CACHING_DIRECTIVES` para la cadena de solicitud y respuesta.
		* Distintivo: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica la información de presentación (`DIRECTIVES`).
		* Distintivo: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica la codificación de contenido (`CONTENT_ENCODING`) del objeto.
		* Distintivo: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: el idioma (LANGUAGE) en el que está el contenido.
		* Distintivo: `--content-language LANGUAGE`
	* _Opcional_: un tipo MIME estándar que describe el formato de los datos del objeto.
		* Distintivo: `--content-type MIME`
	* _Opcional_: un mapa (MAP) de los metadatos que se van a almacenar. Sintaxis: KeyName1=string,KeyName2=string
		* Distintivo: `--metadata MAP`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Supresión de un grupo existente
{: #ic-delete-bucket}

* **Acción:** suprimir un grupo existente en una instancia de IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos delete-bucket --bucket BUCKET_NAME [--region REGION] [--force] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
    * _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
       * Distintivo: `--region REGION`
    * _Opcional_: la operación no solicitará confirmación.
       * Distintivo: `--force`
    * _Opcional_: la salida se devuelve en formato JSON sin formato.
       * Distintivo: `--json`


### Supresión de CORS de grupo
{: #ic-delete-bucket-cors}
* **Acción:** suprimir la configuración de CORS en un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos delete-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Supresión de un objeto
{: #ic-delete-object}
* **Acción:** suprimir un objeto de un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos delete-object --bucket BUCKET_NAME --key KEY [--region REGION] [--force] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
  * _Opcional_: la operación no solicitará confirmación.
  	* Distintivo: `--force`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Supresión de varios objetos
{: #ic-delete-objects}
* **Acción:** suprimir varios objetos de un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos delete-objects --bucket BUCKET_NAME --delete STRUCTURE [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.  
		* Distintivo: `--bucket BUCKET_NAME`  
	* Una estructura (STRUCTURE) que utiliza la sintaxis abreviada o JSON.  
		* Distintivo: `--delete STRUCTURE`  
		* Sintaxis abreviada:  
		`--delete 'Objects=[{Key=string},{Key=string}],Quiet=boolean'`  
		* Sintaxis JSON:  
	`--delete file://<filename.json>`  
	El mandato `--delete` toma una estructura JSON que describe las partes de la carga de varias partes que se deben volver a ensamblar en el archivo completo. En este ejemplo, se utiliza el prefijo `file://` para cargar la estructura JSON desde el archivo especificado.
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
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Descarga de objetos mediante S3Manager
{: #ic-download-s3manager}
* **Acción:** descargar los objetos de S3 de forma simultánea.
* **Uso:** `ibmcloud cos download --bucket BUCKET_NAME --key KEY [--concurrency value] [--part-size SIZE] [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Parámetros que se deben proporcionar:**
	* El nombre (BUCKET_NAME) del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* _Opcional_: el número de goroutines que se van a ejecutar en paralelo por llamada a la operación upload cuando se envíen las partes. El valor
predeterminado es 5.
		* Distintivo: `--concurrency value`
	* _Opcional_: el tamaño (SIZE) de almacenamiento intermedio (en bytes) que se debe utilizar al almacenar los datos en trozos y terminarlos como partes en S3. El tamaño mínimo de parte permitido es 5 MB.
		* Distintivo: `--part-size SIZE`
	* _Opcional_: solo devuelve el objeto si la etiqueta de entidad (ETag) coincide con el valor de ETAG especificado; de lo contrario, devuelve 412 (error de precondición).
		* Distintivo: `--if-match ETAG`
	* _Opcional_: solo devuelve el objeto si se ha modificado desde el valor de TIMESTAMP especificado; de lo contrario, devuelve 304 (no modificado).
		* Distintivo: `--if-modified-since TIMESTAMP`
	* _Opcional_: solo devuelva el objeto si su etiqueta de entidad (ETag) es distinta del valor de ETAG especificado; de lo contrario, devuelva 304 (no modificado).
		* Distintivo: `--if-none-match ETAG`
	* _Opcional_: solo devuelve el objeto si no se ha modificado desde el valor de TIMESTAMP especificado; de lo contrario, devuelve 412 (error de precondición).
		* Distintivo: `--if-unmodified-since TIMESTAMP`
	* _Opcional_: descarga los bytes RANGE especificados de un objeto. Para obtener más información acerca de la cabecera Range de HTTP, [pulse aquí](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35).
		* Distintivo: `--range RANGE`
	* _Opcional_: establece la cabecera (HEADER) Cache-Control de la respuesta.
		* Distintivo: `--response-cache-control HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Disposition de la respuesta.
		* Distintivo: `--response-content-disposition HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Encoding de la respuesta.
		* Distintivo: `--response-content-encoding HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Language de la respuesta.
		* Distintivo: `--response-content-language HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Type de la respuesta.
		* Distintivo: `--response-content-type HEADER`
	* _Opcional_: establece la cabecera (HEADER) Expires de la respuesta.
		* Distintivo: `--response-expires HEADER`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si este distintivo no se proporciona, el programa utilizará la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`
	* _Opcional_: la ubicación en la que se va a guardar el contenido del objeto. Si no se especifica este parámetro, el programa utiliza la ubicación predeterminada.
		* Parámetro: `OUTFILE`


### Obtención de la clase de un grupo
{: #ic-bucket-class}
* **Acción:** determinar la clase de un grupo en una instancia de IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos get-bucket-class --bucket BUCKET_NAME [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Obtención de CORS de grupo
{: #ic-get-bucket-cors}
* **Acción:** devuelve la configuración de CORS de un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos get-bucket-cors --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
  * El nombre del grupo.  
    * Distintivo: `--bucket BUCKET_NAME`
  * _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
    * Distintivo: `--region REGION`
  * _Opcional_: la salida se devuelve en formato JSON sin formato.
    * Distintivo: `--json`


### Búsqueda de un grupo
{: #ic-find-bucket}
* **Acción:** determinar la región y la clase de un grupo en una instancia de IBM Cloud Object Storage. 
* **Uso:** `ibmcloud cos get-bucket-location --bucket BUCKET_NAME [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`
	


### Descarga de un objeto
{: #ic-download-object}
* **Acción:** descargar un objeto de un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos get-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--response-cache-control HEADER] [--response-content-disposition HEADER] [--response-content-encoding HEADER] [--response-content-language HEADER] [--response-content-type HEADER] [--response-expires HEADER] [--region REGION] [--json] [OUTFILE]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* _Opcional_: solo devuelve el objeto si la etiqueta de entidad (ETag) coincide con el valor de ETAG especificado; de lo contrario, devuelve 412 (error de precondición).
		* Distintivo: `--if-match ETAG`
	* _Opcional_: solo devuelve el objeto si se ha modificado desde el valor de TIMESTAMP especificado; de lo contrario, devuelve 304 (no modificado).
		* Distintivo: `--if-modified-since TIMESTAMP`
	* _Opcional_: solo devuelva el objeto si su etiqueta de entidad (ETag) es distinta del valor de ETAG especificado; de lo contrario, devuelva 304 (no modificado).
		* Distintivo: `--if-none-match ETAG`
	* _Opcional_: solo devuelve el objeto si no se ha modificado desde el valor de TIMESTAMP especificado; de lo contrario, devuelve 412 (error de precondición).
		* Distintivo: `--if-unmodified-since TIMESTAMP`
	* _Opcional_: descarga los bytes RANGE especificados de un objeto.
		* Distintivo: `--range RANGE`
	* _Opcional_: establece la cabecera (HEADER) Cache-Control de la respuesta.
		* Distintivo: `--response-cache-control HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Disposition de la respuesta.
		* Distintivo: `--response-content-disposition HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Encoding de la respuesta.
		* Distintivo: `--response-content-encoding HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Language de la respuesta.
		* Distintivo: `--response-content-language HEADER`
	* _Opcional_: establece la cabecera (HEADER) Content-Type de la respuesta.
		* Distintivo: `--response-content-type HEADER`
	* _Opcional_: establece la cabecera (HEADER) Expires de la respuesta.
		* Distintivo: `--response-expires HEADER`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`
	* _Opcional_: la ubicación en la que se va a guardar el contenido del objeto. Si no se especifica este parámetro, el programa utiliza la ubicación predeterminada.
		* Parámetro: `OUTFILE`


### Obtención de las cabeceras de un grupo
{: #ic-bucket-header}
* **Acción:** determinar si un grupo existe en una instancia de IBM Cloud Object Storage.
* **Uso:** `ibmcloud cos head-bucket --bucket BUCKET_NAME [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Obtención de las cabeceras de un objeto
{: #ic-object-header}
* **Acción:** determinar si un archivo existe en un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos head-object --bucket BUCKET_NAME --key KEY [--if-match ETAG] [--if-modified-since TIMESTAMP] [--if-none-match ETAG] [--if-unmodified-since TIMESTAMP] [--range RANGE] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* _Opcional_: solo devuelve el objeto si la etiqueta de entidad (ETag) coincide con el valor de ETAG especificado; de lo contrario, devuelve 412 (error de precondición).
		* Distintivo: `--if-match ETAG`
	* _Opcional_: solo devuelve el objeto si se ha modificado desde el valor de TIMESTAMP especificado; de lo contrario, devuelve 304 (no modificado).
		* Distintivo: `--if-modified-since TIMESTAMP`
	* _Opcional_: solo devuelva el objeto si su etiqueta de entidad (ETag) es distinta del valor de ETAG especificado; de lo contrario, devuelva 304 (no modificado).
		* Distintivo: `--if-none-match ETAG`
	* _Opcional_: solo devuelve el objeto si no se ha modificado desde el valor de TIMESTAMP especificado; de lo contrario, devuelve 412 (error de precondición).
		* Distintivo: `--if-unmodified-since TIMESTAMP`
	* Descarga los bytes RANGE especificados de un objeto.
		* Distintivo: `--range RANGE`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Obtención de una lista de todos los grupos
{: #ic-list-buckets}
* **Acción:** mostrar una lista de todos los grupos de la cuenta de IBM Cloud Object Storage de un usuario. Los grupos se pueden encontrar en diferentes regiones.
* **Uso:** `ibmcloud cos list-buckets [--ibm-service-instance-id ID] [--json]`
	* Tenga en cuenta que debe proporcionar un CRN si utiliza la autenticación de IAM. Esto se puede establecer con el mandato [`ibmcloud cos config crn`](#configure-the-program).
* **Parámetros que se deben proporcionar:**
  * No hay parámetros que especificar.
	* _Opcional_: establece el ID de instancia de servicio de IBM en la solicitud.
		* Distintivo: `--ibm-service-instance-id`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Lista ampliada de grupos
{: #ic-extended-bucket-listing}
* **Acción:** mostrar una lista de todos los grupos de la cuenta de IBM Cloud Object Storage de un usuario. Los grupos se pueden encontrar en diferentes regiones.
* **Uso:** `ibmcloud cos list-buckets-extended [--ibm-service-instance-id ID] [--marker KEY] [--prefix PREFIX] [--page-size SIZE] [--max-items NUMBER] [--json] `
	* Tenga en cuenta que debe proporcionar un CRN si utiliza la autenticación de IAM. Esto se puede establecer con el mandato [`ibmcloud cos config crn`](#configure-the-program).
* **Parámetros que se deben proporcionar:**
  * No hay parámetros que especificar.
	* _Opcional_: establece el ID de instancia de servicio de IBM en la solicitud.
		* Distintivo: `--ibm-service-instance-id`
	* _Opcional_: especifica la clave (KEY) con la que comenzar al mostrar la lista de los objetos de un grupo.
		* Distintivo: `--marker KEY`
	* _Opcional_: limita la respuesta a las claves que empiezan por el prefijo (PREFIX) especificado.
		* Distintivo: `--prefix PREFIX`
	* _Opcional_: el tamaño (SIZE) de cada página que se va a obtener en la llamada de servicio. Esto no afecta al número de elementos que se devuelven en la salida del mandato. Si se establece un tamaño de página menor, se generan más llamadas al servicio y se recuperan menos elementos en cada llamada. Esto puede ayudar a evitar que las llamadas de servicio superen el tiempo de espera.
		* Distintivo: `--page-size SIZE`
	* _Opcional_: el número total de elementos que se devolverán en la salida del mandato.
		* Distintivo: `--max-items NUMBER`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Obtención de una lista de las cargas de varias partes en curso
{: #ic-list-multipart-uploads}
* **Acción:** mostrar las cargas de varias partes en curso.
* **Uso:** `ibmcloud cos list-multipart-uploads --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--key-marker value] [--upload-id-marker value] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: un delimitador (DELIMITER) es un carácter que se utiliza para agrupar claves.
		* Distintivo: `--delimiter DELIMITER`
	* _Opcional_: solicita codificar las claves de objeto en la respuesta y especifica el método de codificación que se va a utilizar.
		* Distintivo: `--encoding-type METHOD`
	* _Opcional_: limita la respuesta a las claves que empiezan por el prefijo (PREFIX) especificado.
		* Distintivo: `--prefix PREFIX`
	* _Opcional_: junto con upload-id-marker, este parámetro especifica la carga de varias partes después de la cual debe comenzar la lista.
		* Distintivo: `--key-marker value`
	* _Opcional_: junto con key-marker, especifica la carga de varias partes después de la cual debe comenzar la lista. Si no se especifica key-marker, el parámetro upload-id-marker se pasa por alto.
		* Distintivo: `--upload-id-marker value`
	* _Opcional_: el tamaño (SIZE) de cada página que se va a obtener en la llamada de servicio. Esto no afecta al número de elementos que se devuelven en la salida del mandato. Si se establece un tamaño de página menor, se generan más llamadas al servicio y se recuperan menos elementos en cada llamada. Esto puede ayudar a evitar que las llamadas de servicio superen el tiempo de espera. (Valor predeterminado: 1000).
		* Distintivo: `--page-size SIZE`
	* _Opcional_: el número total de elementos que se devolverán en la salida del mandato. Si el número total de elementos disponibles es mayor que el valor especificado, se proporciona un valor NextToken en la salida del mandato. Para reanudar la paginación, especifique el valor de NextToken en el argumento starting-token del mandato subsiguiente. (Valor predeterminado: 0).
		* Distintivo: `--max-items NUMBER`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Obtención de una lista de objetos
{: #ic-list-objects}
* **Acción:** obtener una lista de los archivos contenidos en un grupo en la cuenta de IBM Cloud Object Storage de un usuario.  Esta operación se limita actualmente a los 1000 últimos objetos creados y no se puede filtrar.
* **Uso:** `ibmcloud cos list-objects --bucket BUCKET_NAME [--delimiter DELIMITER] [--encoding-type METHOD] [--prefix PREFIX] [--starting-token TOKEN] [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: un delimitador (DELIMITER) es un carácter que se utiliza para agrupar claves.
		* Distintivo: `--delimiter DELIMITER`
	* _Opcional_: solicita codificar las claves de objeto en la respuesta y especifica el método de codificación que se va a utilizar.
		* Distintivo: `--encoding-type METHOD`
	* _Opcional_: limita la respuesta a las claves que empiezan por el prefijo (PREFIX) especificado.
		* Distintivo: `--prefix PREFIX`
	* _Opcional_: una señal (TOKEN) que especifica dónde debe comenzar la paginación. Es el valor NextToken de una respuesta truncada anterior.
		* Distintivo: `--starting-token TOKEN`
	* _Opcional_: el tamaño (SIZE) de cada página que se va a obtener en la llamada de servicio. Esto no afecta al número de elementos que se devuelven en la salida del mandato. Si se establece un tamaño de página menor, se generan más llamadas al servicio y se recuperan menos elementos en cada llamada. Esto puede ayudar a evitar que las llamadas de servicio superen el tiempo de espera. (Valor predeterminado: 1000)
		* Distintivo: `--page-size SIZE`
	* _Opcional_: el número total de elementos que se devolverán en la salida del mandato. Si el número total de elementos disponibles es mayor que el valor especificado, se proporciona un valor NextToken en la salida del mandato. Para reanudar la paginación, especifique el valor de NextToken en el argumento starting-token del mandato subsiguiente. (Valor predeterminado: 0)
		* Distintivo: `--max-items NUMBER`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Obtención de una lista de las partes
{: #ic-list-parts}
* **Acción:** mostrar información sobre una instancia de carga de varias partes en curso.
* **Uso:** `ibmcloud cos list-parts --bucket BUCKET_NAME --key KEY --upload-id ID --part-number-marker VALUE [--page-size SIZE] [--max-items NUMBER] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* ID de carga que identifica la carga de varias partes.
		* Distintivo: `--upload-id ID`
	* El valor de número de parte tras el que comienza la lista (valor predeterminado: 1)
		* Distintivo: `--part-number-marker VALUE`
	* _Opcional_: el tamaño (SIZE) de cada página que se va a obtener en la llamada de servicio. Esto no afecta al número de elementos que se devuelven en la salida del mandato. Si se establece un tamaño de página menor, se generan más llamadas al servicio y se recuperan menos elementos en cada llamada. Esto puede ayudar a evitar que las llamadas de servicio superen el tiempo de espera. (Valor predeterminado: 1000)
		* Distintivo: `--page-size SIZE`
	* _Opcional_: el número total de elementos que se devolverán en la salida del mandato. Si el número total de elementos disponibles es mayor que el valor especificado, se proporciona un valor NextToken en la salida del mandato. Para reanudar la paginación, especifique el valor de NextToken en el argumento starting-token del mandato subsiguiente. (Valor predeterminado: 0)
		* Distintivo: `--max-items NUMBER`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Establecimiento de CORS de grupo
{: #ic-set-bucket-cors}
* **Acción:** establecer la configuración de CORS de un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos put-bucket-cors --bucket BUCKET_NAME [--cors-configuration STRUCTURE] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* _Opcional_: una estructura (STRUCTURE) que utiliza la sintaxis JSON en un archivo.
		* Distintivo: `--cors-configuration STRUCTURE`
		* Sintaxis JSON:  
	`--cors-configuration file://<filename.json>`  
	El mandato `--cors-configuration` toma una estructura JSON que describe las partes de la carga de varias partes que se deben volver a ensamblar en el archivo completo. En este ejemplo, se utiliza el prefijo `file://` para cargar la estructura JSON desde el archivo especificado.
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
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`



### Transferencia de un objeto
{: #ic-upload-object}
* **Acción:** cargar un objeto a un grupo en la cuenta de IBM Cloud Object Storage de un usuario.
* **Uso:** `ibmcloud cos put-object --bucket BUCKET_NAME --key KEY [--body FILE_PATH] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
    * El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* _Opcional_: ubicación de los datos del objeto (`FILE_PATH`).
		* Distintivo: `--body FILE_PATH`
	* _Opcional_: especifica `CACHING_DIRECTIVES` para la cadena de solicitud y respuesta.
		* Distintivo: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica la información de presentación (`DIRECTIVES`).
		* Distintivo: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica la codificación de contenido (`CONTENT_ENCODING`) del objeto.
		* Distintivo: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: el idioma (LANGUAGE) en el que está el contenido.
		* Distintivo: `--content-language LANGUAGE`
	* _Opcional_: el tamaño del cuerpo en bytes. Este parámetro es útil cuando el tamaño del cuerpo no se puede determinar automáticamente. (Valor predeterminado: 0)
		* Distintivo: `--content-length SIZE`
	* _Opcional_: el valor MD5 de 128 bits codificado en base64 de los datos.
		* Distintivo: `--content-md5 MD5`
	* _Opcional_: un tipo MIME estándar que describe el formato de los datos del objeto.
		* Distintivo: `--content-type MIME`
	* _Opcional_: un mapa (MAP) de los metadatos que se van a almacenar. Sintaxis: KeyName1=string,KeyName2=string
		* Distintivo: `--metadata MAP`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Carga de objetos mediante S3Manager
{: #ic-upload-s3manager}
* **Acción:** Upload objects from S3 concurrently.
* **Uso:** `ibmcloud cos upload --bucket BUCKET_NAME --key KEY --file PATH [--concurrency value] [--max-upload-parts PARTS] [--part-size SIZE] [--leave-parts-on-errors] [--cache-control CACHING_DIRECTIVES] [--content-disposition DIRECTIVES] [--content-encoding CONTENT_ENCODING] [--content-language LANGUAGE] [--content-length SIZE] [--content-md5 MD5] [--content-type MIME] [--metadata MAP] [--region REGION] [--json]`
* **Parámetros que se deben proporcionar:**
	* El nombre (BUCKET_NAME) del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* La vía de acceso (PATH) al archivo que se va a cargar.
		* Distintivo: `--file PATH`
	* _Opcional_: el número de goroutines que se van a ejecutar en paralelo por llamada a la operación upload cuando se envíen las partes. El valor
predeterminado es 5.
		* Distintivo: `--concurrency value`
	* _Opcional_: número máximo de partes que se cargarán a S3 que calcula el tamaño de parte del objeto que se va a cargar.  El límite es 10.000 partes.
		* Distintivo: `--max-upload-parts PARTS`
	* _Opcional_: el tamaño (SIZE) de almacenamiento intermedio (en bytes) que se debe utilizar al almacenar los datos en trozos y terminarlos como partes en S3. El tamaño mínimo de parte permitido es 5 MB.
		* Distintivo: `--part-size SIZE`
	* _Opcional_: si se establece este valor en true, el SDK evitará llamar a AbortMultipartUpload tras una anomalía, dejando todas las partes que se hayan cargado correctamente en S3 para la recuperación manual.
		* Distintivo: `--leave-parts-on-errors`
	* _Opcional_: especifica el valor de CACHING_DIRECTIVES para la cadena de solicitud/respuesta.
		* Distintivo: `--cache-control CACHING_DIRECTIVES`
	* _Opcional_: especifica información de presentación (DIRECTIVES).
		* Distintivo: `--content-disposition DIRECTIVES`
	* _Opcional_: especifica las codificaciones de contenido (CONTENT_ENCODING) que se han aplicado al objeto y, por lo tanto, los mecanismos de decodificación que se deben aplicar para obtener el valor media-type referenciado por el campo de cabecera Content-Type.
		* Distintivo: `--content-encoding CONTENT_ENCODING`
	* _Opcional_: el idioma (LANGUAGE) en el que está el contenido.
		* Distintivo: `--content-language LANGUAGE`
	* _Opcional_: el tamaño del cuerpo en bytes. Este parámetro es útil cuando el tamaño del cuerpo no se puede determinar automáticamente.
		* Distintivo: `--content-length SIZE`
	* _Opcional_: el valor MD5 de 128 bits codificado en base64 de los datos.
		* Distintivo: `--content-md5 MD5`
	* _Opcional_: un tipo MIME estándar que describe el formato de los datos del objeto.
		* Distintivo: `--content-type MIME`
	* _Opcional_: un mapa (MAP) de los metadatos que se van a almacenar. Sintaxis: KeyName1=string,KeyName2=string
		* Distintivo: `--metadata MAP`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si este distintivo no se proporciona, el programa utilizará la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Carga de una parte
{: #ic-upload-part}
* **Acción:** cargar una parte de un archivo en una instancia de carga de varias partes existente.
* **Uso:** `ibmcloud cos upload-part --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER [--body FILE_PATH] [--region REGION] [--json]`
	* Tenga en cuenta que debe guardar el número y el ETag de cada parte del archivo cargado (que la CLI le mostrará) para cada parte en un archivo JSON. Consulte la "Guía de carga en varias partes" a continuación para obtener más información.
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo donde tiene lugar la carga de varias partes.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* ID de carga que identifica la carga de varias partes.
		* Distintivo: `--upload-id ID`
	* El número de la parte que se está cargando. Es un entero positivo comprendido entre 1 y 10.000. (Valor predeterminado: 1)
		* Distintivo: `--part-number NUMBER`
	* _Opcional_: ubicación de los datos del objeto (`FILE_PATH`).
		* Distintivo: `--body FILE_PATH`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Carga de una copia de una parte
{: #ic-upload-a-part-copy}
* **Acción:** cargar una parte copiando los datos de un objeto existente.
* **Uso:** `ibmcloud cos upload-part-copy --bucket BUCKET_NAME --key KEY --upload-id ID --part-number NUMBER --copy-source SOURCE [--copy-source-if-match ETAG] [--copy-source-if-modified-since TIMESTAMP] [--copy-source-if-none-match ETAG] [--copy-source-if-unmodified-since TIMESTAMP] [--copy-source-range value] [--region REGION] [--json]`
	* Tenga en cuenta que debe guardar el número y el ETag de cada parte del archivo cargado (que la CLI le mostrará) para cada parte en un archivo JSON. Consulte la "Guía de carga en varias partes" para obtener más información.
* **Parámetros que se deben proporcionar:**
	* El nombre del grupo.
		* Distintivo: `--bucket BUCKET_NAME`
	* La clave (KEY) del objeto.
		* Distintivo: `--key KEY`
	* ID de carga que identifica la carga de varias partes.
		* Distintivo: `--upload-id ID`
	* El número de la parte que se está cargando. Es un entero positivo comprendido entre 1 y 10.000.
		* Distintivo: `--part-number PART_NUMBER`
	* (SOURCE) El nombre del grupo de origen y el nombre de clave del objeto de origen, separado por una barra inclinada (/). Debe tener codificación URL.
		* Distintivo: `--copy-source SOURCE`
	* _Opcional_: copia el objeto si la etiqueta de entidad (Etag) coincide con la etiqueta especificada (ETAG).
		* Distintivo: `--copy-source-if-match ETAG`
	* _Opcional_: copia el objeto si se ha modificado desde la hora especificada (TIMESTAMP).
		* Distintivo: `--copy-source-if-modified-since TIMESTAMP`
	* _Opcional_: copia el objeto si la etiqueta de entidad (ETag) es distinta de la etiqueta especificada (ETAG).
		* Distintivo: `--copy-source-if-none-match ETAG`
	* _Opcional_: copia el objeto si no se ha modificado desde la hora especificada (TIMESTAMP).
		* Distintivo: `--copy-source-if-unmodified-since TIMESTAMP`
	* _Opcional_: el rango de bytes que se van a copiar del objeto de origen. El valor de rango debe utilizar el formato bytes=primero-último, donde el primero y el último son los desplazamientos de byte con base cero que se deben copiar. Por ejemplo, bytes=0-9 indica que desea copiar los diez primeros bytes del origen. Solo puede copiar un rango si el objeto de origen tiene más de 5 MB.
		* Distintivo: `--copy-source-range value`
	* _Opcional_: la región (REGION) en la que se encuentra el grupo. Si no se especifica este distintivo, el programa utiliza la opción predeterminada especificada en la configuración.
		* Distintivo: `--region REGION`
	* _Opcional_: la salida se devuelve en formato JSON sin formato.
		* Distintivo: `--json`


### Espera
{: #ic-wait}
* **Acción:** esperar hasta que se cumpla una determinada condición. Cada submandato sondea una API hasta que se cumple el requisito especificado.
* **Uso:** `ibmcloud cos wait command [arguments...] [command options]`
* **Mandatos:**
    * `bucket-exists`
  		* Esperar hasta que se recibe la respuesta 200 cuando se sondea con head-bucket. Sondea cada 5 segundos hasta que se alcanza un estado satisfactorio. Se sale con el código de retorno 255 después de 20 comprobaciones fallidas.
	* `bucket-not-exists`
		* Esperar hasta que se recibe la respuesta 404 cuando se sondea con head-bucket. Sondea cada 5 segundos hasta que se alcanza un estado satisfactorio. Se sale con el código de retorno 255 después de 20 comprobaciones fallidas.
	* `object-exists`
		* Esperar hasta que se recibe la respuesta 200 cuando se sondea con head-object. Sondea cada 5 segundos hasta que se alcanza un estado satisfactorio. Se sale con el código de retorno 255 después de 20 comprobaciones fallidas.
	* `object-not-exists`
		* Esperar hasta que se recibe la respuesta 404 cuando se sondea con head-object. Sondea cada 5 segundos hasta que se alcanza un estado satisfactorio. Se sale con el código de retorno 255 después de 20 comprobaciones fallidas.

