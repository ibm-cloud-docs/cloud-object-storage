---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# Transferencia de archivos con Cyberduck
{: #cyberduck}

Cyberduck es un navegador de Cloud Object Storage muy utilizado, fácil de utilizar y de código abierto para Mac y Windows. Cyberduck es capaz de calcular las firmas de autorizaciones correctas necesarias para conectarse a IBM COS. Cyberduck se puede descargar desde [ciberduck.io/](https://cyberduck.io/){: new_window}.

Para utilizar Cyberduck para crear una conexión con IBM COS y sincronizar una carpeta de archivos locales con un grupo, siga estos pasos:

 1. Descargue, instale e inicie Cyberduck.
 2. Se abre la ventana principal de la aplicación, en la que puede crear una conexión con IBM COS. Pulse **Abrir conexión** para configurar una conexión con IBM COS.
 3. Se abre una ventana emergente. En el menú desplegable de la parte superior, seleccione `S3 (HTTPS)`. Especifique información
en los campos siguientes y, a continuación, pulse Conectar:

    * `Servidor`: escriba el punto final de IBM COS
        * *Asegúrese de que la región del punto final coincida con el grupo previsto. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `ID de clave de acceso`
    * `Clave de acceso secreta`
    * `Añadir a la cadena de claves`: guarde la conexión con la cadena de claves para permitir su uso en otras aplicaciones *(opcional)*

 4. Cyberduck le lleva a la raíz de la cuenta donde se pueden crear los grupos.
    * Pulse con el botón derecho en el panel principal y seleccione **Nueva carpeta** (*la aplicación funciona con varios protocolos de transferencia, entre los que Carpeta es la construcción de contenedor más utilizada*).
    * Especifique el nombre del grupo y luego pulse Crear.
 5. Una vez que se ha creado el grupo, efectúe una doble pulsación en el grupo para visualizarlo. Dentro del grupo puede realizar varias funciones, como por ejemplo:
    * Cargar archivos en el grupo
    * Obtener una lista del contenido del grupo
    * Descargar objetos del grupo
    * Sincronizar archivos locales en un grupo
    * Sincronizar objetos con otro grupo
    * Crear un archivador de un grupo
 6. Pulse con el botón derecho en el grupo y seleccione **Sincronizar**. Se abre una ventana emergente en la que puede ver la carpeta que desea sincronizar con el grupo. Seleccione la carpeta y pulse Elegir.
 7. Después de seleccionar la carpeta, se abre una ventana emergente nueva. Aquí hay un menú desplegable en el que se selecciona la operación de sincronización con el grupo. Hay tres opciones de sincronización posibles disponibles en el menú:

    * `Descargar`: descarga los objetos modificados y los que faltan del grupo.
    * `Cargar`: carga los archivos modificados y los que faltan en el grupo.
    * `Duplicar`: realiza las operaciones de descarga y de carga, asegurando que todos los archivos y objetos nuevos y actualizados se sincronizan entre la carpeta local y el grupo.

 8. Se abre otra ventana que muestra las solicitudes de transferencia activas e históricas. Una vez finalizada la solicitud de sincronización, la ventana principal lleva a cabo una operación de lista en el grupo para reflejar el contenido actualizado del grupo.

## Mountain Duck
{: #mountain-duck}

Mountain Duck se basa en Cyberduck para permitirle montar Cloud Object Storage como un disco en Finder en Mac o en el Explorador en Windows. Dispone de versiones de prueba, pero se requiere una clave de registro para su uso continuado.

El proceso de creación de un marcador en Mountain Duck es muy parecido al de creación de conexiones en Cyberduck:

1. Descargue, instale e inicie Mountain Duck
2. Cree un nuevo marcador
3. En el menú desplegable, seleccione `S3 (HTTPS)` y especifique la información siguiente:
    * `Servidor`: escriba el punto final de IBM COS 
        * *Asegúrese de que la región de punto final coincida con el grupo previsto. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Nombre de usuario`: especifique la clave de acceso
    * Pulse **Conectar**
    * Se le solicita la clave secreta, que luego se guardará en la cadena de claves.

Ahora sus grupos estarán disponibles en Finder o en el Explorador. Puede interactuar con {{site.data.keyword.cos_short}} como con cualquier otro sistema de archivos montado.

## CLI
{: #cyberduck-cli}

Cyberduck también proporciona `duck`, una interfaz de línea de mandatos (CLI) que se ejecuta en el shell en Linux, Mac OS X y Windows. Encontrará las instrucciones de instalación en la [página de la wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window} de `duck`.

Para poder utilizar `pak` con {{site.data.keyword.cos_full}}, se debe añadir un perfil personalizado al [directorio de soporte de aplicaciones](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}. Encontrará información detallada sobre los perfiles de conexión de `pak`, incluidos perfiles de ejemplo y preconfigurados, en la [ayuda de la CLI](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}.

A continuación se muestra un perfil de ejemplo para un punto final de COS regional:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

La adición de este perfil a `pak` le permite acceder a {{site.data.keyword.cos_short}} con un mandato parecido al siguiente:

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*Valores de clave*
* `<bucket-name>`: nombre del grupo de COS (*asegúrese de que las regiones del grupo y del punto final sean coherentes*)
* `<access-key>`: clave de acceso de HMAC
* `<secret-access-key>`: clave secreta de HMAC

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

Para ver una lista completa de opciones de línea de mandatos, escriba `duck --help` en el shell o visite el [sitio de la wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}.
