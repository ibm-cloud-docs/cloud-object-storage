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

# Serviceberechtigungsnachweise
{: #service-credentials}

Ein Serviceberechtigungsnachweis liefert die Informationen, die notwendig sind, um eine Anwendung mit in ein JSON-Dokument gepacktem {{site.data.keyword.cos_short}} zu verbinden. Serviceberechtigungsnachweise sind stets an eine Service-ID geknüpft und neue Service-IDs können zusammen mit einem neuen Serviceberechtigungsnachweis erstellt werden. 

Führen Sie die folgenden Schritte aus, um einen Serviceberechtigungsnachweis zu erstellen:
1. Melden Sie sich an der {{site.data.keyword.cloud_notm}}-Konsole an und navigieren Sie zu Ihrer Instanz von {{site.data.keyword.cos_short}}.
2. Klicken Sie im seitlichen Navigationsbereich auf **Serviceberechtigungsnachweise**. 
3. Klicken Sie auf **Neuer Berechtigungsnachweis** und geben Sie die erforderlichen Informationen an. Wenn Sie HMAC-Berechtigungsnachweise generieren möchten, klicken Sie auf das Kontrollkästchen 'HMAC-Berechtigungsnachweis einbeziehen'.
  
4. Klicken Sie zum Generieren des Serviceberechtigungsnachweises auf **Hinzufügen**.

Der Berechtigungsnachweis enthält die folgenden Werte:

Feldname | Wert
--|--
`apikey`  |  Neuer API-Schlüssel, der für die Service-ID erstellt wurde.
`cos_hmac_keys`  |  Paar aus Zugriffsschlüssel/geheimem Schlüssel, das mit S3-kompatiblen Tools und Bibliotheken verwendet werden kann.
`endpoints`  |  Link zur JSON-Darstellung verfügbarer Endpunkte.
`iam_apikey_description`  |  Beschreibung des API-Schlüssels. Diese Wert wird ursprünglich automatisch generiert, kann aber bearbeitet werden.
`iam_apikey_name`  |  Name des API-Schlüssels. Dieser Wert wird ursprünglich automatisch generiert, kann aber bearbeitet werden.
`iam_role_crn`  |  Eindeutige Kennung für die zugewiesene Rolle.
`iam_serviceid_crn`  |  Eindeutige Kennung für die Service-ID.
`resource_instance_id`  |  Eindeutige Kennung für die Instanz von {{site.data.keyword.cos_short}}, auf die mit dem Berechtigungsnachweis zugegriffen wird. Diese wird auch als Serviceberechtigungsnachweis bezeichnet. 

Nachfolgend sehen Sie ein Beispiel eines Serviceberechtigungsnachweises:

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

## `endpoints`-Objekte verstehen
{: #service-credential-endpoints}

Die `endpoints`-URL, die als Teil des Serviceberechtigungsnachweises angegeben ist, enthält eine Liste aller möglichen Endpunkte, die für die Verbindungsherstellung mit einem Client verwendet werden können:

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

Wenn Sie einen Client mit einer Bibliothek erstellen, die einen Wert für einen 'auth'-Endpunkt erfordert, müssen Sie `/oidc/token` an das Ende der oben angegebenen `iam-token`-URL anhängen.
{:tip}

## Berechtigungsnachweise für den Zugriff auf einzelne Buckets verwenden
{: #service-credentials-bucket}

Wenn ein Serviceberechtigungsnachweis erstellt wird, so wird der zugrunde liegenden Service-ID eine Rolle für die gesamte Instanz von {{site.data.keyword.cos_short}} erteilt. Wenn eigentlich beabsichtigt ist, dass mit dem Berechtigungsnachweis Zugriff auf eine Teilmenge von Buckets erteilt wird und nicht auf die gesamte Instanz, so ist es erforderlich, diese Richtlinie zu bearbeiten. Weitere Informationen enthält die Seite [Bucketberechtigungen](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions).

## API-Schlüssel versus HMAC
{: #service-credentials-iam-hmac}

Im Allgemeinen stellen IAM-API-Schlüssel die bevorzugte Methode der Authentifizierung für {{site.data.keyword.cos_full}} dar. HMAC wird in erster Linie zur Abwärtskompatibilität mit Anwendungen unterstützt, die von IaaS {site.data.keyword.cos_short} und traditionellen S3-Anwendungen migriert wurden. IAM wird bei der Entwicklung von Anwendungen mit den COS-SDKs auch nativ unterstützt. Zur Vereinfachung des Vorgangs werden der Ablauf und die Aktualisierung von Token automatisch verarbeitet.


Weitere Informationen zu IAM finden Sie in [Einführung in IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview).

Weitere Informationen zu HMAC finden Sie in [HMAC-Berechtigungsnachweise verwenden](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).
