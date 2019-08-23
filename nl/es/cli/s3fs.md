---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# Montaje de un grupo mediante `s3fs`
{: #s3fs}

Las aplicaciones que esperan leer y escribir en un sistema de archivos de estilo NFS pueden utilizar `s3fs`, que puede montar un grupo como directorio, conservando el formato de objeto nativo para los archivos. Esto le permite interactuar con el almacenamiento de la nube utilizando mandatos de shell conocidos, como `ls` para obtener una lista o `cp` para copiar archivos, así como proporcionar acceso a aplicaciones antiguas que se basan en la lectura y escritura de archivos locales. Para obtener una visión general más detallada, [consulte el archivo README oficial del proyecto](https://github.com/s3fs-fuse/s3fs-fuse).

## Requisitos previos
{: #s3fs-prereqs}

* Cuenta de IBM Cloud y una instancia de {{site.data.keyword.cos_full}}
* Un entorno Linux u OSX
* Credenciales (una [clave de API de IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) o [credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## Instalación
{: #s3fs-install}

En OSX, utilice [Homebrew](https://brew.sh/):

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

En Debian o en Ubuntu: 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

En la documentación oficial de `s3fs` se recomienda utilizar `libcurl4-gnutls-dev` en lugar de `libcurl4-openssl-dev`. Cualquiera de los dos funciona, pero la versión OpenSSL puede ofrecer un mejor resultado. 
{:tip}

También puede crear `s3fs` desde el origen. Primero clone el repositorio Github:

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

Luego cree `s3fs`:

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

E instale el binario:

```sh
sudo make install
```
{:codeblock}

## Configuración
{: #s3fs-config}

Guarde sus credenciales en un archivo que contenga `<access_key>:<secret_key>` o `:<api_key>`. Este archivo debe tener acceso limitado, de modo que ejecute:

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

Ahora puede montar un grupo con:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

Si el archivo de credenciales solo tiene una clave de API (no tiene credenciales HMAC), también tendrá que añadir el distintivo `ibm_iam_auth`:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

`<bucket>` es un grupo existente y `<mountpoint>` es el directorio local en el que desea montar el grupo. `<endpoint>` debe corresponder a la [ubicación del grupo](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints). `credentials_file` es el archivo que se ha creado con la clave de API o con las credenciales HMAC.

Ahora, `ls <mountpoint>` mostrará los objetos de ese grupo como si fueran archivos locales (o, en el caso de los prefijos de objeto, como si fueran directorios anidados).

## Optimización del rendimiento
{: #s3fs-performance}

Aunque el rendimiento nunca será el mismo que el de un verdadero sistema de archivos local, se pueden utilizar algunas opciones avanzadas para aumentar el rendimiento. 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM` solo resulta relevante cuando se utiliza un punto final HTTPS. De forma predeterminada, las conexiones seguras con IBM COS utilizan la suite de cifrado `AES256-SHA`. Si se utiliza una suite `AESGCM` en su lugar se reduce en gran medida la sobrecarga de la CPU en la máquina cliente, provocada por las funciones criptográficas de TLS, al tiempo que ofrece el mismo nivel de seguridad criptográfica.
2. `kernel_cache` habilita la memoria caché del almacenamiento intermedio del kernel en el punto de montaje `s3fs`. Esto significa que `s3fs` solo leerá los objetos una vez, ya que la lectura repetitiva del mismo archivo se puede obtener de la memoria caché de almacenamiento intermedio del kernel. La memoria caché de almacenamiento intermedio del kernel solo utilizará memoria libre que no utilicen otros procesos. No se recomienda esta opción si espera que los objetos del grupo se sobrescriban desde otro proceso u otra máquina mientras se monta el grupo y si su caso de uso requiere acceso directo al contenido más actualizado. 
3. `max_background=1000` mejora el rendimiento de lectura de archivos simultáneos de `s3fs`. De forma predeterminada, FUSE da soporte a solicitudes de lectura de archivos de hasta 128 KB. Cuando se solicita leer más que eso, el kernel divide la solicitud grande en subsolicitudes menores y deja que s3fs las procesen de forma asíncrona. La opción `max_background` establece el número máximo global de estas solicitudes asíncronas simultáneas. De forma predeterminada, se establece en 12, pero, si se establece un valor alto arbitrario (1000), se evita que las solicitudes de lectura se bloqueen, incluso cuando se lee un gran número de archivos simultáneamente.
4. `max_stat_cache_size=100000` reduce el número de solicitudes HTTP `HEAD` redundantes que envía `s3fs` y
reduce el tiempo que se tarda en obtener una lista de un directorio o en recuperar atributos de un archivo. En un sistema de archivos se suele acceder con frecuenta a los metadatos de un archivo mediante una llamada `stat()`, que se correlaciona con la solicitud `HEAD` en el sistema de almacenamiento de objetos. De forma predeterminada, `s3fs` almacena en memoria caché los atributos (metadatos) de un máximo de 1000 objetos. Cada entrada almacenada en memoria caché ocupa un máximo de 0,5 KB de memoria. Lo ideal es que la memoria caché sea capaz de mantener los metadatos correspondientes a todos los objetos de su grupo. Sin embargo, tenga en cuenta las implicaciones en el uso de memoria de esta colocación en memoria caché. Si se establece en `100000`, no ocupará más de 0,5 KB * 100000 = 50 MB.
5. `multipart_size=52` establecerá el tamaño máximo de las solicitudes y respuestas enviadas y recibidas desde el servidor COS, en escala de MB. `s3fs` establece este valor en 10 MB de forma predeterminada. Si se aumenta este valor también aumenta el rendimiento (MB/s) por conexión HTTP. Por otro lado, la latencia del primer byte que se sirve desde el archivo aumentará consecuentemente. Por lo tanto, si en su caso de uso solo se lee una pequeña cantidad de datos de cada archivo, probablemente no deseará aumentar este valor. Además, para los objetos grandes (digamos de más de 50 MB) el rendimiento aumenta si este valor es lo suficientemente pequeño como para permitir que el archivo se capte simultáneamente mediante varias solicitudes. Probablemente el valor óptimo para esta opción sea uno cercano a 50 MB. En las prácticas recomendadas de COS se recomienda utilizar solicitudes que sean de múltiplos de 4 MB, y por lo tanto la recomendación es establecer esta opción en 52 (MB).
6. `parallel_count=30` establece el número máximo de solicitudes enviadas de forma simultánea a COS, por operación de lectura/escritura de archivo individual. De forma predeterminada, este valor se establece en 5. Para objetos de gran tamaño, puede obtener un mayor rendimiento si aumenta este valor. Al igual que sucede con la opción anterior, mantenga este valor bajo si solo lee una pequeña cantidad de datos de cada archivo.
7. `multireq_max=30` Cuando se obtiene una lista de un directorio, se envía una solicitud de metadatos de objeto (`HEAD`) por cada objeto de la lista (a menos que los metadatos se encuentren en la memoria caché). Esta opción limita el número de solicitudes simultáneas que se envían a COS, para una única operación de listado de directorios. De forma predeterminada, se establece en 20. Tenga en cuenta que este valor debe ser mayor o igual que la opción `parallel_count` anterior.
8. `dbglevel=warn` establece el nivel de depuración en `warn` en lugar del valor predeterminado (`crit`) para el registro de mensajes en /var/log/syslog.

## Limitaciones
{: #s3fs-limitations}

Es importante recordar que es posible que s3fs no resulte adecuado para todas las aplicaciones, ya que los servicios de almacenamiento de objetos tienen una latencia alta de tiempo hasta el primer byte y no tienen acceso de escritura aleatorio. Las cargas de trabajo que solo leen archivos grandes, como las cargas de trabajo de deep learning, pueden conseguir un buen rendimiento con `s3fs`. 
