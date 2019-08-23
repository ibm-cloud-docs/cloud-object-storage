---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics, credentials

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

# Credenciales de servicio
{: #service-credentials}

Una credencial de servicio proporciona la información necesaria para conectar una aplicación con {{site.data.keyword.cos_short}} empaquetada en un documento JSON. Las credenciales de servicio siempre están asociadas a un ID de servicio, y se pueden crear nuevos ID de servicio junto con una credencial nueva. 

Siga los pasos siguientes para crear una credencial de servicio:
1. Inicie una sesión en la consola de {{site.data.keyword.cloud_notm}} y vaya a la instancia de {{site.data.keyword.cos_short}}.
2. En el panel de navegación lateral, pulse **Credenciales de servicio**. 
3. Pulse **Nueva credencial** y proporcione la información necesaria.
  Si desea generar credenciales de HMAC pulse el recuadro de selección 'Incluir credencial de HMAC'
  
4. Pulse **Añadir** para generar la credencial de servicio.

La credencial contiene los valores siguientes:

Nombre de campo | Valor
--|--
`apikey`  |  Nueva clave de API creada para el ID de servicio
`cos_hmac_keys`  |  Par de clave de acceso /clave secreta para que se utilice con herramientas y bibliotecas compatibles con S3
`endpoints`  |  Enlace con la representación JSON de los puntos finales disponibles
`iam_apikey_description`  |  Descripción de clave de API; inicialmente se genera automáticamente, pero se puede editar
`iam_apikey_name`  |  nombre de clave de API; inicialmente se genera automáticamente, pero se puede editar
`iam_role_crn`  |  Identificador exclusivo del rol asignado
`iam_serviceid_crn`  |  Identificador exclusivo del ID de servicio
`resource_instance_id`  |  Identificador exclusivo de la instancia de {{site.data.keyword.cos_short}} a la que va a acceder la credencial. También recibe el nombre de credencial de servicio. 

A continuación se muestra una credencial de servicio:

```json
{
  "apikey": "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ",
  "cos_hmac_keys": {
      "access_key_id": "347aa3a4b34344f8bc7c7cccdf856e4c",
      "secret_access_key": "gvurfb82712ad14e7a7915h763a6i87155d30a1234364f61"
  },
  "endpoints": "https://control.cloud-object-storage.test.cloud.ibm.com/v2/endpoints",
  "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/3ag0e9402tyfd5d29761c3e97696b71n:d6f74k03-6k4f-4a82-b165-697354o63903::",
  "iam_apikey_name": "auto-generated-apikey-f9274b63-ef0b-4b4e-a00b-b3bf9023f9dd",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
  "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9",
  "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/3ag0e9402tyfd5d29761c3e97696b71n:d6f74k03-6k4f-4a82-b165-697354o63903::"
}
```

## Visión general de los objetos `endpoints`
{: #service-credential-endpoints}

El URL `endpoints` que se proporciona como parte de la credencial de servicio proporciona una lista de todos los puntos finales posibles que se pueden utilizar cuando se establece conexión con un cliente:

```json
{
  "identity-endpoints": {
    "iam-token": "iam.cloud.ibm.com",
    "iam-policy": "iampap.cloud.ibm.com"
  },
  "service-endpoints": {
    "cross-region": {
      "us": {
        "public": {
          "us-geo": "s3.us.cloud-object-storage.appdomain.cloud",
          "Dallas": "s3.dal.us.cloud-object-storage.appdomain.cloud",
          "Washington": "s3.wdc.us.cloud-object-storage.appdomain.cloud",
          "San Jose": "s3.sjc.us.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "us-geo": "s3.private.us.cloud-object-storage.appdomain.cloud",
          "Dallas": "s3.private.dal.us.cloud-object-storage.appdomain.cloud",
          "Washington": "s3.private.wdc.us.cloud-object-storage.appdomain.cloud",
          "San Jose": "s3.private.sjc.us.cloud-object-storage.appdomain.cloud"
        }
      },
      "eu": {
        "public": {
          "eu-geo": "s3.eu.cloud-object-storage.appdomain.cloud",
          "Amsterdam": "s3.ams.eu.cloud-object-storage.appdomain.cloud",
          "Frankfurt": "s3.fra.eu.cloud-object-storage.appdomain.cloud",
          "Milan": "s3.mil.eu.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "eu-geo": "s3.private.eu.cloud-object-storage.appdomain.cloud",
          "Amsterdam": "s3.private.ams.eu.cloud-object-storage.appdomain.cloud",
          "Frankfurt": "s3.private.fra.eu.cloud-object-storage.appdomain.cloud",
          "Milan": "s3.private.mil.eu.cloud-object-storage.appdomain.cloud"
        }
      },
      "ap": {
        "public": {
          "ap-geo": "s3.ap.cloud-object-storage.appdomain.cloud",
          "Tokyo": "s3.tok.ap.cloud-object-storage.appdomain.cloud",
          "Seoul": "s3.seo.ap.cloud-object-storage.appdomain.cloud",
          "Hong Kong": "s3.hkg.ap.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "ap-geo": "s3.private.ap.cloud-object-storage.appdomain.cloud",
          "Tokyo": "s3.private.tok.ap.cloud-object-storage.appdomain.cloud",
          "Seoul": "s3.private.seo.ap.cloud-object-storage.appdomain.cloud",
          "Hong Kong": "s3.private.hkg.ap.cloud-object-storage.appdomain.cloud"
        }
      }
    },
    "regional": {
      "us-south": {
        "public": {
          "us-south": "s3.us-south.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "us-south": "s3.private.us-south.cloud-object-storage.appdomain.cloud"
        }
      },
      "us-east": {
        "public": {
          "us-east": "s3.us-east.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "us-east": "s3.private.us-east.cloud-object-storage.appdomain.cloud"
        }
      },
      "eu-gb": {
        "public": {"eu-gb": "s3.eu-gb.cloud-object-storage.appdomain.cloud"},
        "private": {
          "eu-gb": "s3.private.eu-gb.cloud-object-storage.appdomain.cloud"
        }
      },
      "eu-de": {
        "public": {"eu-de": "s3.eu-de.cloud-object-storage.appdomain.cloud"},
        "private": {
          "eu-de": "s3.private.eu-de.cloud-object-storage.appdomain.cloud"
        }
      },
      "jp-tok": {
        "public": {"jp-tok": "s3.jp-tok.cloud-object-storage.appdomain.cloud"},
        "private": {
          "jp-tok": "s3.private.jp-tok.cloud-object-storage.appdomain.cloud"
        }
      },
      "au-syd": {
        "public": {"au-syd": "s3.au-syd.cloud-object-storage.appdomain.cloud"},
        "private": {
          "au-syd": "s3.private.au-syd.cloud-object-storage.appdomain.cloud"
        }
      }
    },
    "single-site": {
      "ams03": {
        "public": {"ams03": "s3.ams03.cloud-object-storage.appdomain.cloud"},
        "private": {
          "ams03": "s3.private.ams03.cloud-object-storage.appdomain.cloud"
        }
      },
      "che01": {
        "public": {"che01": "s3.che01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "che01": "s3.private.che01.cloud-object-storage.appdomain.cloud"
        }
      },
      "mel01": {
        "public": {"mel01": "s3.mel01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "mel01": "s3.private.mel01.cloud-object-storage.appdomain.cloud"
        }
      },
      "osl01": {
        "public": {"osl01": "s3.osl01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "osl01": "s3.private.osl01.cloud-object-storage.appdomain.cloud"
        }
      },
      "tor01": {
        "public": {"tor01": "s3.tor01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "tor01": "s3.private.tor01.cloud-object-storage.appdomain.cloud"
        }
      },
      "sao01": {
        "public": {"sao01": "s3.sao01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "sao01": "s3.private.sao01.cloud-object-storage.appdomain.cloud"
        }
      },
      "seo01": {
        "public": {"seo01": "s3.seo01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "seo01": "s3.private.seo01.cloud-object-storage.appdomain.cloud"
        }
      },
      "mon01": {
        "public": {"mon01": "s3.mon01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "mon01": "s3.private.mon01.cloud-object-storage.appdomain.cloud"
        }
      },
      "mex01": {
        "public": {"mex01": "s3.mex01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "mex01": "s3.private.mex01.cloud-object-storage.appdomain.cloud"
        }
      }
    }
  }
}
```

Cuando se crea un cliente mediante una biblioteca que requiere un valor de punto final "auth", debe añadir `/oidc/token` al final del URL `iam-token` proporcionado anteriormente.
{:tip}

## Utilización de credenciales de servicio para el acceso a un solo grupo
{: #service-credentials-bucket}

Cuando se crea una credencial de servicio, al ID de servicio subyacente se le otorga un rol sobre toda la instancia de {{site.data.keyword.cos_short}}. Si la intención es que la credencial se utilice para otorgar acceso a un subconjunto de grupos y no a la instancia completa, se tendrá que editar esta política. Consulte la página [Permisos de grupo](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) para obtener más detalles.

## Clave de API frente a HMAC
{: #service-credentials-iam-hmac}

En general, las claves de API de IAM son el método de autenticación recomendado para {{site.data.keyword.cos_full}}. HMAC recibe soporte principalmente por motivos de compatibilidad con versiones anteriores de aplicaciones que se han migrado desde IaaS {site.data.keyword.cos_short}} y aplicaciones S3 antiguas. El IAM también recibe soporte de forma nativa cuando se desarrollan aplicaciones con los SDK de COS. La caducidad y la renovación de señales se gestionan automáticamente para simplificar el proceso.


Para obtener más información sobre IAM, consulte [Iniciación a IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)

Para obtener más información sobre HMAC, consulte [Utilización de credenciales de HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)
