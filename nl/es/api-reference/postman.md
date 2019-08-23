---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# Utilización de `Postman`
{: #postman}

Esta es una configuración básica de `Postman` para la API REST de {{site.data.keyword.cos_full}}. Encontrará más información en la consulta de API correspondiente a [grupos](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) o a [objetos](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).

Para utilizar `Postman` se presupone que el usuario está familiarizado con el almacenamiento de objetos y con la información necesaria de [credencial de servicio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) o la [consola](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Si no está familiarizado con alguno de los términos o de las variables, los puede consultar en el [glosario](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

Información de identificación personal (PII): cuando cree grupos y/o añada objetos, asegúrese de no utilizar ninguna información que pueda identificar a cualquier usuario (persona física) por nombre, ubicación o cualquier otro medio.
{:tip}

## Visión general del cliente de API REST
{: #postman-rest}

REST (REpresentational State Transfer) es un estilo de arquitectura que ofrece un estándar para que los sistemas interactúen entre sí sobre la web, generalmente mediante URL HTTP y verbos estándares
(GET, PUT, POST, etc.) que reciben soporte de los principales lenguajes de desarrollo y las principales plataformas. Sin embargo, interactuar con una API REST no es tan sencillo como usar un navegador de Internet estándar. Los navegadores simples no permiten manipular la solicitud de URL. Aquí es donde entra un cliente de API REST.

Un cliente de API REST proporciona una sencilla aplicación basada en GUI que actúa como interfaz con una biblioteca de API REST existente. Un buen cliente facilita la prueba, el desarrollo y la documentación de las API al permitir que los usuarios combinen solicitudes HTTP tanto simples como complejas. Postman constituye un excelente cliente de API REST que ofrece un completo entorno de desarrollo de API que incluye herramientas integradas para diseñar, depurar, probar, documentar, supervisar y publicar API. También proporciona características útiles, tales como Colecciones y Espacios de trabajo, que hacen que la colaboración sea pan comido. 

## Requisitos previos
{: #postman-prereqs}
* Cuenta de IBM Cloud
* [Recurso de Cloud Storage creado](https://cloud.ibm.com/catalog/) (el plan lite/gratuito es suficiente)
* [CLI de IBM Cloud instalada y configurada](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [ID de instancia de servicio para Cloud Storage](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [Señal de IAM (Identity and Access Management)](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [Punto final para el grupo COS](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### Creación de un grupo
{: #postman-create-bucket}
1.	Inicie Postman
2.	En el nuevo separador, seleccione `PUT` en la lista desplegable
3.	Especifique el punto final en la barra de direcciones y añada el nombre del nuevo grupo.
a.	Los nombres de grupos deben ser exclusivos entre todos los grupos, así que elija algo específico.
4.	En la lista desplegable Tipo, seleccione la señal de transporte.
5.	Añada la señal de IAM en el recuadro Señal.
6.	Pulse Vista previa de la solicitud.
a.	Debería ver un mensaje en el que se confirma que se han añadido las cabeceras.
7.	Pulse el separador Cabecera, donde debería ver una entrada existente para Autorización.
8.	Añada una clave nueva.
a.	Clave: `ibm-service-instance-id`
b.	Valor: ID de instancia del recurso correspondiente al servicio Cloud Storage.
9.	Pulse Enviar.
10.	Recibirá el mensaje de estado `200 OK`.

### Creación de un nuevo archivo de texto
{: #postman-create-text-file}

1.	Cree un nuevo separador pulsando el icono Más (+).
2.	Seleccione `PUT` en la lista.
3.	En la barra de direcciones, especifique la dirección del punto final con el nombre de grupo de la sección anterior y un nombre de archivo.
4.	En la lista Tipo, seleccione la señal de transporte.
5.	Añada la señal de IAM en el recuadro Señal.
6.	Seleccione el separador Cuerpo.
7.	Seleccione la opción raw y asegúrese de que Texto está seleccionado.
8.	Escriba el texto en el espacio proporcionado.
9.	Pulse Enviar.
10.	Recibirá el mensaje de estado `200 OK`.

### Obtención de una lista del contenido de un grupo
{: #postman-list-objects}

1.	Cree un nuevo separador seleccionando el icono Más (+).
2.	Compruebe que `GET` está seleccionado en la lista.
3.	En la barra de direcciones, especifique la dirección del punto final con el nombre de grupo de la sección anterior.
4.	En la lista Tipo, seleccione la señal de transporte.
5.	Añada la señal de IAM en el recuadro Señal.
6.	Pulse Enviar.
7.	Recibirá el mensaje de estado `200 OK`.
8.	En el cuerpo de la sección Respuesta hay un mensaje XML con la lista de archivos del grupo.

## Utilización de la colección de ejemplo
{: #postman-collection}

Dispone de una colección de Postman que puede [descargar![Icono de enlace externo](../icons/launch-glyph.svg "Icono de enlace externo")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window} con ejemplos de solicitud de API de {{site.data.keyword.cos_full}} que se pueden configurar.

### Importación de la colección en Postman
{: #postman-import-collection}

1. En Postman, pulse el botón Importar en la esquina superior derecha
2. Importe el archivo de colección con uno de estos métodos:
    * Desde la ventana Importar, arrastre y suelte el archivo de colección en la ventana **Soltar archivos aquí**
    * Pulse el botón Elegir archivos y vaya a la carpeta y seleccione el archivo de colección
3. *IBM COS* debería aparecer en la ventana de colecciones
4. Expanda la colección y debería ver veinte (20) solicitudes de ejemplo
5. La colección contiene seis (6) variables que tendrán que establecerse para poder ejecutar correctamente las solicitudes de API
    * Pulse en los tres puntos que hay a la derecha de la colección para ampliar el menú y pulse Editar.
6. Editar las variables para que coincidan con el entorno de Cloud Storage
    * **bucket**: especifique el nombre del nuevo grupo que desea crear (los nombres de los grupos deben ser exclusivos en Cloud Storage).
    * **serviceid**: especifique el CRN del servicio Cloud Storage. [Aquí](/docs/overview?topic=overview-crn) encontrará instrucciones para obtener su CRN.
    * **iamtoken**: especifique la señal OAUTH del servicio Cloud Storage. [Aquí](/docs/services/key-protect?topic=key-protect-retrieve-access-token) encontrará instrucciones para obtener su señal OAUTH.
    * **endpoint**: especifique el punto final regional del servicio Cloud Storage. Obtenga los puntos finales disponibles en el [panel de control de IBM Cloud](https://cloud.ibm.com/resources/){:new_window}
        * *Asegúrese de que ha seleccionado un punto final que coincide con el servicio Key Protect para asegurarse de que los ejemplos se ejecutan correctamente*
    * **rootkeycrn**: el CRN de la clave raíz creada en el servicio Key Protect principal.
        * El CRN debe parecerse al siguiente:<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Asegúrese de que el servicio Key Protect seleccionado coincide con la región del punto final*
    * **bucketlocationvault**: especifique el valor de la restricción de ubicación para la creación del grupo para la solicitud de API para *Crear grupo nuevo (clase de almacenamiento diferente)*.
        * Los valores admitidos son:
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. Pulse Actualizar.

### Ejecución de los ejemplos
{: #postman-samples}
Las solicitudes de ejemplo de API son bastante sencillas y fáciles de utilizar. Están diseñadas para que se ejecuten en orden y muestren cómo interactuar con Cloud Storage. También se pueden utilizar para ejecutar una prueba funcional sobre el servicio Cloud Storage para garantizar una operación adecuada.

<table>
    <tr>
        <th>Solicitar</th>
        <th>Resultado esperado</th>
        <th>Resultados de la prueba</th>
    </tr>
    <tr>
        <td>Recuperar lista de grupos</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo, debe establecer una lista XML de los grupos de Cloud Storage.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene el contenido esperado</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Crear nuevo grupo</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Crear nuevo archivo de texto</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene la cabecera esperada</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Crear nuevo archivo binario</td>
        <td>
            <ul>
                <li>
                    Pulse en el cuerpo y pulse Elegir archivo para seleccionar la imagen que desea cargar
                </li>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene la cabecera esperada</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar la lista de archivos del grupo</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver los dos archivos que ha creado en las solicitudes anteriores
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene la cabecera esperada</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar la lista de archivos del grupo (filtrar por prefijo)</td>
        <td>
            <ul>
                <li>Cambiar el valor de querystring por prefix=&lt;some text&gt;</li>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver archivos con nombres que empiezan por el prefijo especificado
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene la cabecera esperada</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar archivo de texto</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver el texto que ha especificado en la solicitud anterior
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene el contenido del cuerpo esperado</li>
                <li>La respuesta contiene la cabecera esperada</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar archivo binario</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver la imagen que ha elegido en la solicitud anterior
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene la cabecera esperada</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Recuperar la lista de cargas fallidas de varias partes</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver las cargas de varias fallidas de varias partes correspondientes al grupo
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene el contenido esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Recuperar la lista de cargas fallidas de varias partes (filtrar por nombre)</td>
        <td>
            <ul>
                <li>Cambiar el valor de querystring por prefix=&lt;some text&gt;</li>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver las cargas de varias partes fallidas correspondientes al grupo con nombres que empiezan por el prefijo especificado
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene el contenido esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Establecer grupo habilitado para CORS</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Recuperar configuración de CORS de grupo</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
                <li>
                    En el cuerpo de la respuesta, debería ver las cargas la configuración de CORS definida para el grupo
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
                <li>La respuesta contiene el contenido esperado</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Suprimir configuración de CORS de grupo</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suprimir archivo de texto</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suprimir archivo binario</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suprimir grupo</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Crear grupo nuevo (clase de almacenamiento diferente)</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suprimir grupo (clase de almacenamiento diferente)</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Crear grupo nuevo (Key Protect)</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suprimir grupo (Key Protect)</td>
        <td>
            <ul>
                <li>Código de estado 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La solicitud se ha ejecutado correctamente</li>
            </ul>
        </td>                
    </tr>
</table>

## Utilización de Postman Collection Runner
{: #postman-runner}

Postman Collection Runner proporciona una interfaz de usuario para probar una colección y le permite ejecutar todas las solicitudes en una colección a la vez. 

1. Pulse el botón Runner que se encuentra en la esquina superior derecha de la ventana principal de Postman.
2. En la ventana Runner, seleccione la colección de IBM COS y pulse el botón azul **Ejecutar IBM COS** en la parte inferior de la pantalla.
3. La ventana Collection Runner mostrará las iteraciones a medida que se ejecuten las solicitudes. Verá aparecer los resultados de la prueba bajo cada una de las solicitudes.
    * La ventana **Ejecutar resumen** muestra una vista de cuadrícula de las solicitudes y permite filtrar los resultados.
    * También puede pulsar **Exportar resultados**, lo que guardará los resultados en un archivo JSON.
