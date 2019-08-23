---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# Utilización de Cloud Object Storage con apps de Cloud Foundry
{: #cloud-foundry}

{{site.data.keyword.cos_full}} se puede emparejar con aplicaciones de {{site.data.keyword.cfee_full}} para proporcionar contenido de alta disponibilidad mediante el uso de regiones y puntos finales.

## Entorno Cloud Foundry Enterprise
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} es una plataforma para alojar apps y servicios en la nube. Puede crear instancias de varias plataformas aisladas de nivel empresarial bajo demanda que se ejecuten dentro de su propia cuenta y que se puedan desplegar en hardware compartido o dedicado. La plataforma facilita el escalado de apps a medida que crece el consumo, simplificando el tiempo de ejecución y la infraestructura para que el usuario se pueda centrar en el desarrollo.

Para implantar correctamente una plataforma Cloud Foundry, se necesita una [planificación y un diseño adecuados](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation) de los recursos necesarios y los requisitos de la empresa. Encontrará más información sobre [cómo empezar](/docs/cloud-foundry?topic=cloud-foundry-about#creating) a trabajar con el entorno Cloud Foundry Enterprise y una [guía de aprendizaje](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started) de introducción.

### Regiones
{: #cloud-foundry-regions}
Los [puntos finales regionales](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) son una parte importante del entorno de IBM Cloud. Puede crear aplicaciones e instancias de servicio en diferentes regiones con la misma infraestructura de IBM Cloud para la gestión de aplicaciones y la misma vista de detalles de uso para la facturación. Si elige una región de IBM Cloud geográficamente cercana a sus clientes, puede reducir la latencia de los datos en sus aplicaciones, así como minimizar los costes. Las regiones también se pueden seleccionar según requisitos legales o cumplimiento con normas de seguridad. 

Con {{site.data.keyword.cos_full}}, puede optar por distribuir los datos en un solo centro de datos, una región entera o incluso una combinación de regiones [seleccionando el punto final](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) al que la aplicación envía las solicitudes de API.

### Conexiones y alias de recursos
{: #cloud-foundry-aliases}

Un alias es una conexión entre el servicio gestionado dentro de un grupo de recursos y una aplicación dentro de una organización o un espacio. Los alias son como enlaces simbólicos que contienen referencias a recursos remotos. Permite la interoperatividad y la reutilización de una instancia en toda la plataforma. En la consola de {{site.data.keyword.cloud_notm}}, la conexión (alias) está representada como una instancia de servicio. Puede crear una instancia de un servicio en un grupo de recursos y luego reutilizarla de cualquier región disponible mediante la creación de un alias en una organización o espacio en esas regiones.

## Almacenamiento de credenciales como variables de VCAP 
{: #cloud-foundry-vcap}

Las credenciales de {{site.data.keyword.cos_short}} se pueden almacenar en la variable de entorno VCAP_SERVICES, que se puede analizar utilizarla al acceder al servicio {{site.data.keyword.cos_short}}. Las credenciales incluyen la información que se muestra en el ejemplo siguiente:

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

Luego la variable de entorno VCAP_SERVICES se puede analizar dentro de la aplicación para poder acceder al contenido de {{site.data.keyword.cos_short}}. A continuación se muestra un ejemplo de integración de la variable de entorno con el SDK de COS mediante Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// iniciar el sdk de cos
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

Para obtener más información sobre cómo utilizar el SDK para acceder a {{site.data.keyword.cos_short}} con ejemplos de código consulte:

* [Utilización de Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Utilización de Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Utilización de Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## Creación de enlaces de servicio 
{: #cloud-foundry-bindings}

### Panel de control
{: #cloud-foundry-bindings-console}

La forma más sencilla de crear un enlace de servicio es mediante el [panel de control de {{site.data.keyword.cloud}}](https://cloud.ibm.com/resources). 

1. Inicie una sesión en el [panel de control](https://cloud.ibm.com/resources)
2. Pulse la aplicación Cloud Foundry
3. Pulse Conexiones en el menú de la izquierda
4. Pulse **Crear conexión** a la derecha
5. En la página *Conectar servicio compatible existente*, pase el puntero del ratón sobre el servicio {{site.data.keyword.cos_short}} y pulse **Conectar**.
6. En la pantalla emergente *Conectar servicio habilitado para IAM*, seleccione el rol de acceso, deje Generar automáticamente para el ID de servicio y pulse **Conectar**
7. La aplicación Cloud Foundry se debe volver a transferir para que utilice el nuevo enlace de servicio. Pulse **Volver a transferir** para iniciar el proceso.
8. Una vez finalizada la retransferencia, el servicio Cloud Object Storage está disponible para la aplicación.

La variable de entorno VCAP_SERVICES de aplicaciones se actualiza automáticamente con la información de servicio. Para ver la nueva variable:

1. Pulse *Tiempo de ejecución* en el menú de la derecha
2. Pulse *Variables de entorno*
3. Verifique que el servicio COS ya está en la lista

### IBM Client Tools (CLI)
{: #cloud-foundry-bindings-cli}

1. Inicie sesión con la CLI de IBM Cloud
```
 ibmcloud login --apikey <your api key>
```

2. Elija como destino su entorno de Cloud Foundry
```
 ibmcloud target --cf
```

3. Cree un alias de servicio para {{site.data.keyword.cos_short}}
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Cree un enlace de servicio entre el alias de {{site.data.keyword.cos_short}} y la aplicación de Cloud Foundry y proporcione un rol para el enlace. Los roles válidos son:<br/><ul><li>Escritor</li><li>Lector</li><li>Gestor</li><li>Administrador</li><li>Operador</li><li>Visor</li><li>Editor</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### IBM Client Tools (CLI) con credenciales de HMAC
{: #cloud-foundry-hmac}

El código de autenticación de mensajes basado en hash (HMAC) es un mecanismo para calcular un código de autenticación de mensajes creado que utiliza un par de clave de acceso y clave secreta. Esta técnica se puede utilizar para verificar la integridad y la autenticidad de un mensaje. Encontrará más información sobre cómo utilizar las [credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) en la documentación de {{site.data.keyword.cos_short}}.

1. Inicie sesión con la CLI de IBM Cloud
```
 ibmcloud login --apikey <your api key>
```

2. Elija como destino su entorno de Cloud Foundry
```
 ibmcloud target --cf
```

3. Cree un alias de servicio para {{site.data.keyword.cos_short}}
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Cree un enlace de servicio entre el alias de {{site.data.keyword.cos_short}} y la aplicación de Cloud Foundry y proporcione un rol para el enlace.<br/><br/>* **Nota:** se necesita el parámetro adicional* (`{"HMAC":true}`) *para crear credenciales de servicio con HMAC habilitado.*<br/><br/>Los roles válidos son:<br/><ul><li>Escritor</li><li>Lector</li><li>Gestor</li><li>Administrador</li><li>Operador</li><li>Visor</li><li>Editor</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### Enlace con {{site.data.keyword.containershort_notm}}
{: #cloud-foundry-k8s}

El proceso para crear un enlace de servicio con {{site.data.keyword.containershort}} es un poco diferente. 

*En esta sección también tendrá que instalar un [procesador JSON de línea de mandatos ligero jq - a](https://stedolan.github.io/jq/){:new_window}.*

Necesita la siguiente información y sustituir los valores de clave en los mandatos siguientes:

* `<service alias>`: nuevo nombre de alias para el servicio COS
* `<cos instance name>`: nombre de la instancia de COS existente
* `<service credential name>`: nuevo nombre de la clave/credencial de servicio
* `<role>`: rol que se debe adjuntar a la clave de servicio (consulte la sección anterior para ver los roles válidos, `Escritor` es el que se especifica con más frecuencia)
* `<cluster name>`: nombre del servicio de clúster de Kubernetes existente
* `<secret binding name>`: este valor se genera cuando COS se enlaza al servicio de clúster


1. Cree un alias de servicio para la instancia de COS<br/><br/>* **Nota:** la instancia de COS solo puede tener un alias de servicio*
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. Crear una clave de servicio nueva con permisos para el alias de servicio de COS
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. Enlace el servicio de clúster a COS
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. Verifique que el alias de servicio de COS está enlazado al clúster
```
ibmcloud cs cluster-services --cluster <cluster name>
```
La
salida tendrá un aspecto similar al siguiente:
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. Recupere la lista de secretos del clúster y busque el secreto correspondiente al servicio COS. Normalmente será `binding-` más el `<service alias>` que ha especificado en el paso 1 (es decir, `binding-sv-cos`). Utilice este valor como `<secret binding name>` en el paso 6.
```
kubectl get secrets
```
La salida debe tener un aspecto similar al siguiente:
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. Verifique que las credenciales de HMAC de COS están disponibles en los secretos del clúster
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
La salida debe tener un aspecto similar al siguiente:
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
