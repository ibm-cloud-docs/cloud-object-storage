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

# Credenciais de serviço
{: #service-credentials}

Uma credencial de serviço fornece as informações necessárias para conectar um aplicativo ao {{site.data.keyword.cos_short}} empacotado em um documento JSON. As credenciais de serviço são sempre associadas a um ID de serviço e os novos IDs de serviço podem ser criados juntamente com uma nova credencial. 

Use as etapas a seguir para criar uma credencial de serviço:
1. Efetue login no console do {{site.data.keyword.cloud_notm}} e navegue para sua instância do {{site.data.keyword.cos_short}}.
2. Na navegação lateral, clique em **Credenciais de serviço**. 
3. Clique em **Nova credencial** e forneça as informações necessárias.
  Se você deseja gerar credenciais HMAC, clique na caixa de seleção 'Incluir credencial HMAC'
  
4. Clique em **Incluir** para gerar a credencial de serviço.

A credencial contém os valores a seguir:

Nome do campo | Valor
--|--
`apikey`  |  Nova chave de API criada para o ID de serviço
`cos_hmac_keys`  |  Par de Chave de acesso/Chave secreta para uso com ferramentas e bibliotecas compatíveis com S3
`endpoints`  |  Link para a representação JSON de terminais disponíveis
`iam_apikey_description`  |  Descrição da chave de API - inicialmente gerada automaticamente, mas editável
`iam_apikey_name`  |  Nome da chave de API - inicialmente gerada automaticamente, mas editável
`iam_role_crn`  |  Identificador exclusivo para a função designada
`iam_serviceid_crn`  |  Identificador exclusivo para o ID de serviço
`resource_instance_id`  |  Identificador exclusivo para a instância do {{site.data.keyword.cos_short}} que a credencial acessará. Isso também é referido como uma credencial de serviço. 

Este é um exemplo de uma credencial de serviço:

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

## Entendendo os objetos `endpoints`
{: #service-credential-endpoints}

A URL `endpoints` fornecida como parte da credencial de serviço fornece uma lista de todos os terminais possíveis que podem ser usados ao conectar um cliente:

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

Ao criar um cliente usando uma biblioteca que requer um valor de terminal "auth", é necessário incluir `/oidc/token` no final da URL `iam-token` fornecida acima.
{:tip}

## Usando credenciais de serviço para acesso a um único depósito
{: #service-credentials-bucket}

Quando uma credencial de serviço é criada, o ID de serviço subjacente é concedido a uma função na instância inteira do {{site.data.keyword.cos_short}}. Se a intenção é que a credencial seja usada para conceder acesso a um subconjunto de depósitos e não à instância inteira, essa política precisa ser editada. Consulte a página [Permissões de depósito](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) para obter mais detalhes.

## Chave de API vs. HMAC
{: #service-credentials-iam-hmac}

Em geral, as Chaves de API do IAM são o método preferencial de autenticação para o {{site.data.keyword.cos_full}}. O HMAC é suportado principalmente para compatibilidade com versões anteriores com aplicativos que migraram do IaaS {site.data.keyword.cos_short}} e aplicativos S3 anteriores. O IAM também é suportado nativamente ao desenvolver aplicativos com os SDKs do COS. A expiração e a atualização do token são manipuladas automaticamente para simplificar o processo.


Para obter mais informações sobre o IAM, visite [Introdução ao IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)

Para obter mais informações sobre o HMAC, visite [Usando credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)
