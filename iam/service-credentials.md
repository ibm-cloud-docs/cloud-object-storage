---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-08-26"

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

# Service credentials
{: #service-credentials}

A service credential provides the necessary information to connect an application to {{site.data.keyword.cos_short}} packaged in a JSON document. 
{: shortdesc}

Service credentials are always associated with a Service ID, and new Service IDs can be created along with a new credential. 

Use the following steps to create a service credential:
1. Log in to the {{site.data.keyword.cloud_notm}} console and navigate to your instance of {{site.data.keyword.cos_short}}.
2. In the side navigation, click **Service Credentials**. 
3. Click **New credential** and provide the necessary information.
  If you want to generate HMAC credentials, click 'Include HMAC Credential' check box
  
4. Click **Add** to generate service credential.

The credential has the following values:

| Field name               | Value                                                                                                                                               |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `apikey`                 | New API key that is created for the Service ID                                                                                                      |
| `cos_hmac_keys`          | Access Key and Secret Key pair for use with S3-compatible tools and libraries                                                                       |
| `endpoints`              | Link to JSON representation of available endpoints                                                                                                  |
| `iam_apikey_description` | API key description - initially generated but editable                                                                                          |
| `iam_apikey_name`        | API key name - initially generated but editable                                                                                                 |
| `iam_role_crn`           | Unique identifier for the assigned role                                                                                                             |
| `iam_serviceid_crn`      | Unique identifier for the Service ID                                                                                                                |
| `resource_instance_id`   | Unique identifier for the instance of {{site.data.keyword.cos_short}} the credential accesses. This is also referred to as a service credential. |
This is an example of a service credential:

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

## Understanding the `endpoints` objects
{: #service-credential-endpoints}

The `endpoints` URL provided as part of the service credential provides a list of all possible endpoints that can be used when connecting a client:

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

When creating a client by using a library that requires an "auth" endpoint value, you need to add `/oidc/token` to end of the `iam-token` URL provided above.
{:tip}

## Using service credentials for single-bucket access
{: #service-credentials-bucket}

When a service credential is created, the underlying Service ID is granted a role on the entire instance of {{site.data.keyword.cos_short}}. If the intention that the credential be used to grant,  access to a subset of buckets and not the entire instance, this policy needs to be edited. See the [Bucket permissions](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) page for more details.

## API Key vs HMAC
{: #service-credentials-iam-hmac}

In general IAM API Keys are the preferred method of authentication for {{site.data.keyword.cos_full}}. HMAC is supported primarily for compatibility with an earlier version with applications which migrated from IaaS {site.data.keyword.cos_short}} and legacy S3 applications. IAM is also natively supported when developing applications with the COS SDKs. Token expiration and refresh are handled automatically to simplify the process.


For more information about IAM visit - [Getting started with IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)

For more information about HMAC visit - [Using HMAC Credentials](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)