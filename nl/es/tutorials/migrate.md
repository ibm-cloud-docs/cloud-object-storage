---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-22"

keywords: tutorial, migrate, openstack swift

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

# Migración de datos de OpenStack Swift
{: #migrate}

Antes de que {{site.data.keyword.cos_full_notm}} estuviera disponible como servicio de {{site.data.keyword.cloud_notm}} Platform, los proyectos que requería un almacén de objetos utilizaban [OpenStack Swift](https://docs.openstack.org/swift/latest/) u [OpenStack Swift (infraestructura)](/docs/infrastructure/objectstorage-swift?topic=objectstorage-swift-GettingStarted#getting-started-with-object-storage-openstack-swift). Se recomienda a los desarrolladores que actualicen sus aplicaciones y migren sus datos a {{site.data.keyword.cloud_notm}} para aprovechar las nuevas ventanas en cuanto a control de accesos y cifrado que proporcionan IAM y Key Protect, así como las nuevas características a medida que estén disponibles.

El concepto de 'contenedor' en Swift es idéntico al de 'grupo' en COS. COS limita las instancias de servicio a 100 grupos y algunas instancias de Swift pueden tener un número mayor de contenedores. Los grupos de COS pueden albergar miles de millones de objetos y admiten barras inclinadas (`/`) en nombres de objeto para 'prefijos' de tipo directorio cuando se organizan los datos. COS da soporte a políticas de IAM a nivel de grupo y de instancia de servicio.
{:tip}

Un enfoque a la migración de datos entre servicios de almacenamiento de objetos es utilizar una herramienta 'sync' o 'clone', como [el programa de utilidad de línea de mandatos de código abierto `rclone`](https://rclone.org/docs/). Este programa de utilidad sincronizará un árbol de archivos entre dos ubicaciones, incluido el almacenamiento en la nube. Cuando `rclone` escribe datos en COS, utilizará la API COS/S3 para segmentar los objetos grandes y cargar las partes en paralelo de acuerdo con los tamaños y los umbrales establecidos como parámetros de configuración.

Existen algunas diferencias entre COS y Swift que se deben tener en cuenta como parte de la migración de datos.

  - COS todavía no admite las políticas de caducidad o el mantenimiento de versiones. Los flujos de trabajo que dependen de estas características de Swift deben gestionarlas como parte de su lógica de aplicación después de migrar a COS.
  - COS da soporte a los metadatos a nivel de objeto, pero esta información no se conserva cuando se utiliza `rclone` para migrar datos. Los metadatos personalizados se pueden establecer en objetos en COS utilizando una cabecera `x-amz-meta-{key}: {value}`, pero se recomienda que los metadatos a nivel de objeto se copien en una base de datos antes de utilizar `rclone`. Los metadatos personalizados se pueden aplicar a objetos existentes [copiando el objeto en sí mismo](https://cloud.ibm.com/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object): el sistema reconocerá que los datos del objeto son idénticos y solo actualizará los metadatos. Tenga en cuenta que `rclone` **puede** conservar las indicaciones de fecha y hora.
  - COS utiliza políticas de IAM para el control de acceso a nivel de instancia de servicio y de grupo. [Los objetos se pueden poner a disponibilidad pública](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-public-access) estableciendo una ACL `public-read`, que elimina la necesidad de una cabecera de autorización.
  - Las [cargas de varias partes](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-large-objects) para objetos grandes se gestionan de forma diferente en la API de COS/S3 y en la API de Swift.
  - COS permite cabeceras HTTP opcionales, como por ejemplo `Cache-Control`, `Content-Encoding`, `Content-MD5` y `Content-Type`.

Esta guía contiene instrucciones para migrar datos desde un solo contenedor Swift a un solo grupo de COS. Esto se deberá repetir para todos los contenedores que se desee migrar y, a continuación, se deberá actualizar la lógica de la aplicación para que utilice la nueva API. Una vez migrados los datos, puede verificar la integridad de la transferencia mediante `rclone check`, que comparará las sumas de comprobación de MD5 y generará una lista de los objetos en los que no coincidan.


## Configuración de {{site.data.keyword.cos_full_notm}}
{: #migrate-setup}

  1. Si aún no lo ha hecho, suministre una instancia de {{site.data.keyword.cos_full_notm}} desde el [catálogo](https://cloud.ibm.com/catalog/services/cloud-object-storage).
  2. Cree los grupos que necesite para almacenar los datos transferidos. Consulte la [guía de iniciación](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para familiarizarse con conceptos clave, como [puntos finales](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints) y [clases de almacenamiento](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-classes).
  3. Como la sintaxis de la API Swift es significativamente diferente de la de la API de COS/S3, es posible que tenga que refactorizar la aplicación para poder utilizar métodos equivalentes que se proporcionan en los SDK de COS. Dispone de bibliotecas en ([Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node)) o la [API REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api).

## Configuración de un recurso de cálculo para que ejecute la herramienta de migración
{: #migrate-compute}
  1. Elija la máquina Linux/macOS/BSD o el servidor nativo o virtual de la infraestructura de IBM Cloud que esté más próximo a sus datos.
     Se recomienda la siguiente configuración de servidor: RAM de 32 GB, procesador de entre 2 y 4 núcleos y velocidad de red privada de 1000 Mbps.  
  2. Si ejecuta la migración en un servidor nativo o virtual de la infraestructura de IBM Cloud, utilice los puntos finales de Swift y de COS **privados**.
  3. De lo contrario, utilice puntos finales **públicos** de Swift y de COS.
  4. Instale `rclone` desde [un gestor de paquetes o el binario precompilado](https://rclone.org/install/).

      ```
      curl https://rclone.org/install.sh | sudo bash
      ```

## Configuración de `rclone` para OpenStack Swift
{: #migrate-rclone}

  1. Cree un archivo de configuración `rclone` en `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Cree el código fuente de Swift copiando lo siguiente y pegándolo en `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        auth = https://identity.open.softlayer.com/v3
        user_id =
        key =
        region =
        endpoint_type =
        ```

  3. Obtenga la credencial de OpenStack Swift
    <br>a. Pulse la instancia de Swift en el [panel de control de la consola de IBM Cloud](https://cloud.ibm.com/).
    <br>b. Pulse **Credenciales de servicio** en el panel de navegación.
    <br>c. Pulse **Nueva credencial** para general la información de credenciales. Pulse
**Añadir**.
    <br>d. Visualice la credencial que ha creado y copie el contenido JSON.

  4. Rellene los siguientes campos:

        ```
        user_id = <IDusuario>
        key = <contraseña>
        region = dallas O london            depende de la ubicación del contenedor
        endpoint_type = public O internal   internal es el punto final privado
        ```

  5. Salte a la sección para configurar `rclone` para COS


## Configuración de `rclone` para OpenStack Swift (infraestructura)
{: #migrate-config-swift}

  1. Cree un archivo de configuración `rclone` en `~/.rclone.conf`.

        ```
        touch ~/.rclone.conf
        ```

  2. Cree el código fuente de Swift copiando lo siguiente y pegándolo en `rclone.conf`.

        ```
        [SWIFT]
        type = swift
        user =
        key =
        auth =
        ```

  3. Obtenga la credencial de OpenStack Swift (infraestructura)
    <br>a. Pulse la cuenta Swift en el portal de clientes de la infraestructura IBM Cloud.
    <br>b. Pulse el centro de datos del contenedor del origen de la migración.
    <br>c. Pulse **Ver credenciales**.
    <br>d. Copie lo siguiente.
      <br>&nbsp;&nbsp;&nbsp;**Nombre usuario**
      <br>&nbsp;&nbsp;&nbsp;**Clave de API**
      <br>&nbsp;&nbsp;&nbsp;**Punto final de autenticación** en función de dónde ejecute la herramienta de migración.

  4. Utilizando la credencial de OpenStack Swift (infraestructura), rellene los siguientes campos:

        ```
        user = <Nombre de usuario>
        key = <Clave de API (Contraseña)>
        auth = <dirección punto final público o privado>
        ```

## Configuración de `rclone` para COS
{: #migrate-config-cos}

### Obtención de la credencial de COS
{: #migrate-config-cos-credential}

  1. Pulse la instancia de COS en la consola de IBM Cloud.
  2. Pulse **Credenciales de servicio** en el panel de navegación.
  3. Pulse **Nueva credencial** para general la información de credenciales.
  4. En **Parámetros de configuración en línea**, añada `{"HMAC":true}`. Pulse
**Añadir**.
  5. Visualice la credencial que ha creado y copie el contenido JSON.

### Obtención del punto final de COS
{: #migrate-config-cos-endpoint}

  1. Pulse **Grupos** en el panel de navegación.
  2. Pulse el grupo de destino de la migración.
  3. Pulse **Configuración** en el panel de navegación.
  4. Desplácese hacia abajo hasta la sección **Puntos finales** y seleccione el punto final en función de dónde se ejecute la herramienta de migración.

  5. Cree el destino de COS copiando lo siguiente y pegándolo en `rclone.conf`.

    ```
    [COS]
    type = s3
    access_key_id =
    secret_access_key =
    endpoint =
    ```

  6. Utilizando la credencial de COS y el punto final, rellene los campos siguientes:

    ```
    access_key_id = <id_clave_acceso>
    secret_access_key = <clave_acceso_secreta>
    endpoint = <punto final grupo>       
    ```

## Comprobación de que el origen de la migración y el destino se han configurados correctamente
{: #migrate-verify}

1. Obtenga una lista del contenedor de Swift para verificar que `rclone` se ha configurado correctamente.

    ```
    rclone lsd SWIFT:
    ```

2. Obtenga una lista del grupo de COS para verificar que `rclone` se ha configurado correctamente.

    ```
    rclone lsd COS:
    ```

## ejecución de `rclone`
{: #migrate-run}

1. Realice una ejecución en seco (no se copian datos) de `rclone` para sincronizar los objetos del contenedor de Swift de origen (es decir, `swift-test`) con el grupo de COS de destino (es decir, `cos-test`).

    ```
    rclone --dry-run copy SWIFT:swift-test COS:cos-test
    ```

1. Compruebe que los archivos que desea migrar aparecen en la salida del mandato. Si todo tiene buen aspecto, elimine el distintivo `--dry-run` y añada el distintivo `-v` para copiar los datos. Si utiliza el distintivo opcional `--checksum`, evita que se actualicen los archivos que tienen el mismo hash MD5 y el mismo tamaño de objeto en ambas ubicaciones.

    ```
    rclone -v copy --checksum SWIFT:swift-test COS:cos-test
    ```

   Debería intentar maximizar la CPU, la memoria y la red en la máquina que ejecuta rclone para obtener el tiempo de transferencia más rápido.
   Otros parámetros que debe tener en cuenta son los siguientes:

   --checkers int  Número de comprobadores que se ejecutarán en paralelo. (El valor predeterminado es 8)
   Es el número de hebras de comparación de suma de comprobación que se ejecutan. Se recomienda aumentar este valor a 64 o más.

   --transfers int Número de transferencias de archivos que se ejecutarán en paralelo. (El valor predeterminado es 4)
   Es el número de objetos que se van a transferir en paralelo. Se recomienda aumentar este valor a 64 o 128 o más.

   --fast-list Utilizar lista recursiva si está disponible. Utiliza más memoria, pero menos transacciones.
   Utilice esta opción para mejorar el rendimiento: reduce el número de solicitudes necesarias para copiar un objeto.

La migración de datos mediante `rclone` copia los datos de origen, pero no los suprime.
{:tip}


3. Repita el proceso para cualquier otro contenedor que se tenga que migrar.
4. Una vez que se hayan copiado todos los datos y se ha verificado que la aplicación puede acceder a los datos en COS, suprima la instancia de servicio de Swift.
