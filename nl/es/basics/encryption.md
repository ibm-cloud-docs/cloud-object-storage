---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: encryption, security, sse-c, key protect

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

# Gestión del cifrado
{: #encryption}

Todos los objetos almacenados en {{site.data.keyword.cos_full}} se cifran de forma predeterminada utilizando [claves generadas aleatoriamente y una transformación de tipo todo o nada](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-security). Aunque este modelo de cifrado predeterminado proporciona seguridad en reposo, algunas cargas de trabajo tienen que estar en posesión de las claves de cifrado utilizadas. Puede gestionar las claves manualmente proporcionando sus propias claves de cifrado al almacenar datos (SSE-C), o bien puede crear grupos que utilicen IBM Key Protect (SSE-KP) para gestionar claves de cifrado.

## Cifrado del lado del servidor con claves proporcionadas por el cliente (SSE-C)
{: #encryption-sse-c}

Se impone SSE-C sobre los objetos. Las solicitudes de leer o escribir objetos o sus metadatos utilizando claves gestionadas por el cliente envían la información de cifrado necesaria como cabeceras en las solicitudes HTTP. La sintaxis es idéntica a la de la API S3, y las bibliotecas compatibles con S3 que dan soporte a SSE-C deberían funcionar según lo esperado en {{site.data.keyword.cos_full}}.

Cualquier solicitud que utilice cabeceras SSE-C se debe enviar utilizando SSL. Tenga en cuenta que los valores de `ETag` de las cabeceras de respuesta *no* son el hash MD5 del objeto, sino una serie hexadecimal de 32 bytes generada aleatoriamente.

Cabecera | Tipo | Descripción
--- | ---- | ------------
`x-amz-server-side-encryption-customer-algorithm` | serie | Esta cabecera se utiliza para especificar el algoritmo y el tamaño de clave que se deben utilizar con la clave de cifrado almacenada en la cabecera `x-amz-server-side-encryption-customer-key`. Este valor se debe establecer en la serie `AES256`.
`x-amz-server-side-encryption-customer-key` | serie | Esta cabecera se utiliza para transportar la representación de serie de bytes codificada en base 64 de la clave AES 256 utilizada en el proceso de cifrado del lado del servidor.
`x-amz-server-side-encryption-customer-key-MD5` | serie | Esta cabecera se utiliza para transportar el valor MD5 de 128 bits codificado en base 64 de la clave de cifrado de acuerdo con RFC 1321. El almacén de objetos utilizará este valor para validar que se pasa la clave en `x-amz-server-side-encryption-customer-key` y que no ha resultado dañada durante el proceso de transporte y codificación. El valor se debe calcular en la clave ANTES de que la clave se codifique en base 64.


## Cifrado del lado del servidor con {{site.data.keyword.keymanagementservicelong_notm}} (SSE-KP)
{: #encryption-kp}

{{site.data.keyword.keymanagementservicefull}} es un sistema de gestión de claves centralizado (KMS) para generar, gestionar y destruir las claves de cifrado que utilizan los servicios de {{site.data.keyword.cloud_notm}}. Puede crear una instancia de {{site.data.keyword.keymanagementserviceshort}} desde el catálogo de {{site.data.keyword.cloud_notm}}.

Una vez que tenga una instancia de {{site.data.keyword.keymanagementserviceshort}} en la región en la que desee crear un nuevo grupo, deberá crear una clave raíz y anotar el CRN de dicha clave.

Puede optar por utilizar {{site.data.keyword.keymanagementserviceshort}} para gestionar el cifrado para un grupo solo en el momento de la creación. No se puede cambiar un grupo existente para que utilice {{site.data.keyword.keymanagementserviceshort}}.
{:tip}

Cuando cree el grupo, debe proporcionar cabeceras adicionales.

Para obtener más información sobre {{site.data.keyword.keymanagementservicelong_notm}}, [consulte la documentación](/docs/services/key-protect?topic=key-protect-getting-started-tutorial#getting-started-with-key-protect).

### Iniciación a SSE-KP
{: #sse-kp-gs}

Todos los objetos almacenados en {{site.data.keyword.cos_full}} se cifran de forma predeterminada utilizando claves generadas aleatoriamente y una transformación de tipo todo o nada. Aunque este modelo de cifrado predeterminado proporciona seguridad en reposo, algunas cargas de trabajo tienen que estar en posesión de las claves de cifrado utilizadas. Puede utilizar [{{site.data.keyword.keymanagementservicelong_notm}}](/docs/services/key-protect?topic=key-protect-about) para crear, añadir y gestionar claves, que luego puede asociar con la instancia de {{site.data.keyword.cos_full}} para cifrar grupos.

### Antes de empezar
{: #sse-kp-prereqs}

Necesitará:
  * una cuenta de [{{site.data.keyword.cloud}} Platform](http://cloud.ibm.com)
  * una [instancia de {{site.data.keyword.cos_full_notm}}](http://cloud.ibm.com/catalog/services/cloud-object-storage){: new_window}
  * una [instancia de {{site.data.keyword.keymanagementservicelong_notm}}](http://cloud.ibm.com/catalog/services/key-protect){: new_window}
  * y algunos archivos en el sistema local que cargar.

### Creación o adición de una clave en {{site.data.keyword.keymanagementserviceshort}}
{: #sse-kp-add-key}

Vaya a la instancia de {{site.data.keyword.keymanagementserviceshort}} y [genere o especifique una clave](/docs/services/key-protect?topic=key-protect-getting-started-tutorial).

### Concesión de autorización del servicio
{: #sse-kp}
Autorice a {{site.data.keyword.keymanagementserviceshort}} para que se utilice con IBM COS:

1. Abra el panel de control de {{site.data.keyword.cloud_notm}}.
2. En la barra de menús, pulse **Gestionar** &gt; **Cuenta** &gt; **Usuarios**.
3. En la parte de navegación, pulse **Identidad y acceso**&gt;**Autorizaciones**.
4. Pulse **Crear autorización**.
5. En el menú **Servicio de origen**, seleccione **Almacenamiento de objetos de nube**.
6. En el menú **Instancia de servicio de origen**, seleccione la instancia de servicio que se debe autorizar.
7. En el menú **Servicio de destino**, seleccione **{{site.data.keyword.keymanagementservicelong_notm}}**.
8. En el menú **Instancia de servicio de destino**, seleccione la instancia de servicio que se debe autorizar.
9. Habilite el rol de **Lector**.
10. Pulse **Autorizar**.

### Creación de un grupo
{: #encryption-createbucket}

Cuando la clave exista en {{site.data.keyword.keymanagementserviceshort}} y haya autorizado que el servicio Key Protect se utilice con IBM COS, asocie la clave con un nuevo grupo:

1. Vaya a la instancia de {{site.data.keyword.cos_short}}.
2. Pulse **Crear grupo**.
3. Escriba un nombre de grupo, seleccione la resiliencia **Regional** y elija una ubicación y una clase de almacenamiento.
4. En Configuración avanzada, habilite **Añadir claves de Key Protect**.
5. Seleccione la instancia de servicio de protección claves asociada, la clave y el ID de clave.
6. Pulse **Crear**.

En la lista **Grupos y objetos**, ahora el grupo tiene un icono de clave bajo **Avanzado**, lo que indica que el grupo tiene una clave de protección clave habilitada. Para ver los detalles de la clave, pulse el menú situado a la derecha del grupo y luego pulse **Ver clave de Key Protect**.

Observe que el valor `Etag` que se devuelve para los objetos cifrados mediante SSE-KP **será** el hash MD5 real del objeto descifrado original.
{:tip}


## Rotación de claves
{: #encryption-rotate}

La rotación de clave es una parte importante de la mitigación del riesgo de una violación de datos.  El hecho de cambiar las claves periódicamente reduce el riesgo potencial de pérdida de datos si la clave se pierde o se ve comprometida. La frecuencia de las rotaciones de claves varía según la organización y depende de diversas variables que incluyen el entorno, la cantidad de datos cifrados, la clasificación de los datos y las leyes de conformidad. El [Instituto Nacional de Estándares y Tecnología (NIST)](https://www.nist.gov/topics/cryptography){:new_window} proporciona definiciones de las longitudes adecuadas de las claves y ofrece directrices sobre cómo se deben utilizar las claves largas.

### Rotación manual de claves
{: #encryption-rotate-manual}

Para rotar las claves de {{site.data.keyword.cos_short}}, deberá crear un nuevo grupo con el servicio Key Protect habilitado utilizando una nueva clave raíz y deberá copiar el contenido del grupo existente en el nuevo.

**NOTA**: si se suprime una clave del sistema, se destruye su contenido y los datos que aún estén cifrados con esa clave. Una vez eliminada, la acción no se puede deshacer ni invertir y los datos se perderán de forma permanente.

1. Cree o añada una nueva clave raíz en el servicio [Key Protect](/docs/services/key-protect?topic=key-protect-getting-started-tutorial) .
2. [Cree un nuevo grupo](#encryption-createbucket) y añada la nueva clave raíz
3. Copie todos los objetos del grupo original en el nuevo grupo.
    1. Este paso se puede realizar de diversas formas:
        1. Desde la línea de mandatos mediante [CURL](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-curl) o la [API de AWS](/docs/services/cloud-object-storage/cli?topic=cloud-object-storage-aws-cli)
        2. Mediante la (API)[/docs/services/cloud-object-storage/api-reference/api-reference-objects.html#copy-object]
        3. Mediante el SDK con [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java), [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python), [Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node) o [Go](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-go)
