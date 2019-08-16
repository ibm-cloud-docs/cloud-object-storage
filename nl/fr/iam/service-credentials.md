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

# Données d'identification au service
{: #service-credentials}

Des données d'identification de service fournissent les informations nécessaires pour connecter une application à {{site.data.keyword.cos_short}} conditionnée dans un document JSON. Les données d'identification de service sont toujours associées à un ID de service et de nouveaux ID de service peuvent être créés avec de nouvelles données d'identification de service.  

Pour créer des données d'identification de service, procédez comme suit :
1. Connectez-vous à la console {{site.data.keyword.cloud_notm}} et accédez à votre instance d'{{site.data.keyword.cos_short}}. 
2. Dans la barre de navigation, cliquez sur **Données d'identification de service**.  
3. Cliquez sur **Nouvelles données d'identification** et fournissez les informations nécessaires.
  Si vous souhaitez générer des données d'identification HMAC, cochez la case 'Inclure les données d'identification HMAC'. 
  
4. Cliquez sur **Ajouter** pour générer des données d'identification de service. 

Les données d'identification contiennent les valeurs suivantes :

Nom de zone | Valeur  
--|--
`apikey`  |  Nouvelle clé d'API créée pour l'ID de service. 
`cos_hmac_keys`  |  Paire clé d'accès/clé secrète à utiliser avec des outils et des bibliothèques compatibles S3. 
`endpoints`  |  Lien vers la représentation JSON des noeuds finaux disponibles. 
`iam_apikey_description`  |  Description de clé d'API - Générée automatiquement au départ, mais modifiable. 
`iam_apikey_name`  |  Nom de clé d'API - Générée automatiquement au départ, mais modifiable. 
`iam_role_crn`  |  Identificateur unique pour le rôle affecté. 
`iam_serviceid_crn`  |  Identificateur unique pour l'ID de service. 
`resource_instance_id`  |  Identificateur unique pour l'instance d'{{site.data.keyword.cos_short}} à laquelle les donnée d'identification permettront d'accéder. Celles-ci sont également appelées données d'identification de service. 

Voici un exemple de données d'identification de service :

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

## Présentation des objets `endpoints`
{: #service-credential-endpoints}

L'URL `endpoints` fournie dans le cadre des données d'identification de service fournit une liste des noeuds finaux qui peuvent être utilisés lors de la connexion d'un client : 

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

Lorsque vous créez un client à l'aide d'une bibliothèque nécessitant une valeur de noeud final "auth", vous devez ajouter `/oidc/token` à la fin de l'URL `iam-token` fournie ci-dessus. {:tip}

## Utilisation des données d'identification de service pour un accès à un seul compartiment
{: #service-credentials-bucket}

Lorsque des données d'identification de service sont créées, un rôle est affecté à l'ID de service sous-jacent sur toute l'instance d'{{site.data.keyword.cos_short}}. Si vous avez l'intention d'utiliser les données d'identification de service pour octroyer des droits d'accès à un sous-ensemble de compartiments et non à toute l'instance, cette règle doit être éditée. Pour plus d'informations, voir la rubrique [Droits des compartiments](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions). 

## Clé d'API ou HMAC
{: #service-credentials-iam-hmac}

En général, l'utilisation des clés d'API IAM est privilégiée par rapport à d'autres méthodes pour l'authentification auprès d'{{site.data.keyword.cos_full}}. HMAC est principalement pris en charge à des fins de compatibilité avec les versions antérieures pour les applications qui ont été migrées à partir d'IaaS {site.data.keyword.cos_short}} et les applications S3 existantes. IAM est également pris en charge en mode natif lors du développement d'applications avec les SDK COS. L'expiration et l'actualisation des jetons sont gérées automatiquement pour simplifier le processus. 


Pour plus d'informations sur IAM, voir [Initiation à IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview). 

Pour plus d'informations sur HMAC, voir [Utilisation des données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac). 
