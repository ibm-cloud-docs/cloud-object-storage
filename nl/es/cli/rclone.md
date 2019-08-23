---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# Utilización de `rclone`
{: #rclone}

## Instalación de `rclone`
{: #rclone-install}

La herramienta `rclone` sirve para mantener los directorios sincronizados y para migrar datos entre plataformas de almacenamiento. Es un programa Go y viene como un solo archivo binario.

### Instalación rápida
{: #rclone-quick}

*  [Descargue](https://rclone.org/downloads/) el binario adecuado. 
*  Extraiga el binario `rclone` o `rclone.exe` del archivo.
*  Ejecute `rclone config` para configurar la herramienta.

### Instalación mediante un script
{: #rclone-script}

Instale `rclone` en sistemas Linux/macOS/BSD:

```
curl https://rclone.org/install.sh | sudo bash
```

También están disponibles versiones beta:

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

El script de instalación comprueba primero la versión de `rclone` instalada y omite la descarga si la versión actual ya está actualizada.
{:note}

### Instalación en Linux desde un binario precompilado
{: #rclone-linux-binary}

En primer lugar, obtenga y desempaquetar el binario:

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

A continuación, copie el archivo binario en una ubicación razonable:

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

Instale la documentación:

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

Ejecute `rclone config` para configurar la herramienta:

```
rclone config
```

### Instalación en macOS desde un binario precompilado
{: #rclone-osx-binary}

En primer lugar, descargue el paquete `rclone`:

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

A continuación, extraiga el archivo descargado y ejecute `cd` a la carpeta extraída:

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

Mueva `rclone` a `$PATH` y especifique la contraseña cuando se le solicite:

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

El mandato `mkdir` se puede ejecutar de forma segura, incluso si el directorio existe.
{:tip}

Elimine los archivos sobrantes.

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

Ejecute `rclone config` para configurar la herramienta:

```
rclone config
```

## Configuración del acceso a IBM COS
{: #rclone-config}

1. Ejecute `rclone config` y seleccione `n` para un nuevo remoto.

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. Especifique el nombre de la configuración:
```
	name> <YOUR NAME>
```

3. Seleccione el almacenamiento "s3".

```
	Choose a number from below, or type in your own value
		1 / Alias for a existing remote
		\ "alias"
		2 / Amazon Drive
		\ "amazon cloud drive"
		3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
		\ "s3"
		4 / Backblaze B2
		\ "b2"
	[snip]
		23 / http Connection
	  \ "http"
	Storage> 3
```

  4. Seleccione IBM COS como proveedor de almacenamiento S3.

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  1. Escriba **False** para especificar sus credenciales.

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. Especifique la clave de acceso y el secreto.

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. Especifique el punto final para IBM COS. Para IBM COS público, elija entre las opciones proporcionadas. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```
Endpoint for IBM COS S3 API.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3.us.cloud-object-storage.appdomain.cloud"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. Especifique una restricción de ubicación de IBM COS. La restricción de ubicación debe coincidir con el punto final. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. Especifique una ACL. Solo se permiten los valores `public-read` y `private`. 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. Revise la configuración mostrada y acepte guardar el "remote" y luego salga. El archivo de configuración debería parecerse al siguiente:

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## Consulta de mandatos
{: #rclone-reference}

### Creación de un grupo
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### Obtención de una lista de grupos disponibles
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### Obtención del contenido de un grupo
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### Copia de un archivo de local a remoto
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### Copia de un archivo de remoto a local
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### Supresión de un archivo en remoto
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### Obtención de una lista de mandatos
{: #rclone-reference-listing}

Hay varios mandatos de lista relacionados
* `ls`: solo para ver el tamaño y la vía de acceso de los objetos
* `lsl`: solo para ver la hora de modificación, el tamaño y la vía de acceso de los objetos
* `lsd`: solo para ver los directorios
* `lsf`: para ver una lista de objetos y directorios en un formato fácil de analizar
* `lsjson`: para ver una lista de objetos y directorios en formato JSON

## `rclone sync`
{: #rclone-sync}

La operación `sync` hace que el origen y el destino sean idénticos y solo modifica el destino. La sincronización no transfiera archivos no modificados, probando por tamaño y hora de modificación o MD5SUM. El destino se actualiza para que coincida con el origen, incluida la supresión de archivos si es necesario.

Puesto que esto puede provocar una pérdida de datos, pruebe primero con el distintivo `--dry-run` para ver exactamente lo que se va a copiar y a suprimir.
{:important}

Tenga en cuenta que los archivos del destino no se suprimirán si ha habido algún error en algún punto.

Se sincroniza el _contenido_ del directorio, no el directorio propiamente dicho. Cuando `source:path` es un directorio, es el contenido de `source:path` lo que se copia, no el nombre del directorio y el contenido. Para obtener más información, consulte la explicación ampliada en el mandato `copy`.

Si `dest:path` no existe, se crea y el contenido de `source:path` se coloca allí.

```sh
rclone sync source:path dest:path [flags]
```

### Utilización de `rclone` desde varias ubicaciones al mismo tiempo
{: #rclone-sync-multiple}

Puede utilizar `rclone` de varios lugares al mismo tiempo si elige un subdirectorio distinto para la salida:

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

Si ejecuta `sync` en el mismo directorio, debe utilizar `rclone copy`; de lo contrario los dos procesos podrían suprimir los otros archivos del otro:

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

Cuando se utiliza `sync`, `copy` o `move`, los archivos que se sobrescribirían o se suprimirían se mueven en su jerarquía original a este directorio.

Si `--suffix` está establecido, a los archivos que se mueven se les añade el sufijo. Si hay un archivo con la misma vía de acceso (después de que se añada el sufijo) en el directorio, se sobrescribe.

El remoto utilizado debe dar soporte al traslado o a la copia del lado del servidor y debe utilizar el mismo remoto que el destino de la sincronización. El directorio de copia de seguridad no debe solapar el directorio de destino.

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

sincronizará `sync` `/path/to/local` con `remote:current`, pero los archivos que se habrían actualizado o suprimido se guardarán en `remote:old`.

Si ejecuta `rclone` desde un script, es posible que desee utilizar la fecha del día como nombre del directorio que se pasa a `--backup-dir` para almacenar los archivos antiguos, o puede pasar `--suffix` con la fecha del día.

## Sincronización diaria de `rclone`
{: #rclone-sync-daily}

Es importante planificar una copia de seguridad para automatizar las copias de seguridad. El modo de hacerlo depende de la plataforma. En Windows se puede utilizar el Programador de tareas, mientras que en MacOS y Linux se puede utilizar crontabs.

### Sincronización de un directorio
{: #rclone-sync-directory}

`Rclone` sincroniza un directorio local con un contenedor remoto y guarda todos los archivos en el directorio local en el contenedor. `Rclone` utiliza la sintaxis `rclone sync source destination`, donde `source` es la carpeta local y `destination` es el contenedor de IBM COS.

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

Es posible que ya tenga un destino creado, pero, si no es así, puede crear un nuevo grupo siguiendo los pasos anteriores.

### Planificación de un trabajo
{: #rclone-sync-schedule}

Antes de planificar un trabajo, asegúrese de que ha realizado la carga inicial y que se ha completado.

#### Windows
{: #rclone-sync-windows}

1. Cree un archivo de texto llamado `backup.bat` en algún lugar del sistema y pegue el mandato que ha utilizado en la sección sobre [sincronización de un directorio](#rclone-sync-directory).  Especifique la vía de acceso completa a rclone.exe y no olvide guardar el archivo.

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. Utilice `schtasks` para planificar un trabajo. Este programa de utilidad utiliza varios parámetros.
	* /RU: el usuario que va a ejecutar el trabajo. Es necesario si el usuario que desea utilizar está desconectado.
	* /RP: la contraseña del usuario.
	* /SC: establézcalo en DAILY
	* /TN: el nombre del trabajo. Llámelo backup
	* /TR: la vía de acceso al archivo backup.bat que acaba de crear.
	* /ST: la hora en que se iniciará la tarea. Está en formato de hora de 24 horas. 01:05:00 es la 1:05 AM. 13:05:00 sería la 1:05 PM.

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac y Linux
{: #rclone-sync-nix}

1. Cree un archivo llamado `backup.sh` en algún lugar del sistema y pegue el mandato que ha utilizado en la sección sobre [sincronización de un directorio](#rclone-sync-directory). Debería parecerse al siguiente. Especifique la vía de acceso completa al ejecutable rclone y no olvide guardar el archivo.

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. Convierta el script en ejecutable con `chmod`.

```sh
chmod +x backup.sh
```

3. Edite crontabs.

```sh
sudo crontab -e
```

4. Añada una entrada al final del archivo crontabs. Los archivos crontabs son directos: los cinco primeros campos representan, en orden, minutos, horas, días, meses y días de la semana. Para indicar todo, utilice *. Para que `backup.sh` se ejecute cada día a la 1:05 AM, utilice un mandato parecido al siguiente:

```sh
5 1 * * * /full/path/to/backup.sh
```

5. Guarde el archivo crontabs. Ya está listo para continuar.
