---

copyright:
  years: 2017, 2023
lastupdated: "2023-12-06"

keywords: authorization, iam, basics, credentials

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Service credentials
{: #service-credentials}

A [service credential](/docs/account?topic=account-service_credentials&interface=ui) provides the necessary information to connect an application to {{site.data.keyword.cos_short}} packaged in a JSON document.
{: shortdesc}

Service credentials are always associated with a Service ID, and new Service IDs can be created along with a new credential.

To view a credential you must be granted the Administrator platform role or a custom role that has the `resource-controller.credential.retrieve_all` action. For more information about [this update](/docs/overview?overview-whatsnew#may2022), see [the documentation](/docs/account?topic=account-service_credentials&interface=ui#viewing-credentials-ui).
{: important}

Use the following steps to create a service credential:

1. Log in to the {{site.data.keyword.cloud_notm}} console and navigate to your instance of {{site.data.keyword.cos_short}}.
1. In the side navigation, click **Service Credentials**.
1. Click **New credential** and provide the necessary information. If you want to generate HMAC credentials, switch the `Include HMAC Credential` to `On`. Verify the option is switched to `On` before continuing.
1. Click **Add** to generate service credential.

When creating a service credential, it is possible to provide a value of `None` for the role.  This will prevent the creation of unintended or unnecessary IAM access policies. Any access policies for the associated service ID will need to be managed using the IAM console or APIs.
{: tip}

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
{: caption="Credential values" caption-side="top"}

This is an example of a service credential:

```json
{
  "apikey": "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ",
  "cos_hmac_keys": {
      "access_key_id": "347aa3a4b34344f8bc7c7cccdf856e4c",
      "secret_access_key": "gvurfb82712ad14W7a7915h763a6i87155d30a1234364f61"
  },
  "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
  "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/3ag0e9402tyfd5d29761c3e97696b71n:d6f74k03-6k4f-4a82-b165-697354o63903::",
  "iam_apikey_name": "auto-generated-apikey-f9274b63-ef0b-4b4e-a00b-b3bf9023f9dd",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
  "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9",
  "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/3ag0e9402tyfd5d29761c3e97696b71n:d6f74k03-6k4f-4a82-b165-697354o63903::"
}
```

You can also use the IBM Cloud CLI to create a new service credential (which is a subset of something called a *service key*). This example extracts the credential and writes it to a file where the IBM COS SDKs can automatically source the API key and Service Instance ID. First, create the Service Key (called `config-example` and associated with a COS instance called `cos-dev-enablement` in this example):

```sh
ic resource service-key-create config-example --instance-name cos-dev-enablement
```

Then, extract the credential and create the `cos_credential` file:

```sh
ic resource service-key config-example --output JSON | jq '.[].credentials' > ~/.bluemix/cos_credentials
```

## Understanding the `endpoints` objects
{: #service-credential-endpoints}

The `endpoints` URL (`https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints`) provided as part of the service credential provides a list of all possible endpoints that can be used when connecting a client:

```json
{
   "identity-endpoints":{
      "iam-token":"iam.cloud.ibm.com",
      "iam-policy":"iampap.cloud.ibm.com"
   },
   "service-endpoints":{
      "cross-region":{
         "us":{
            "public":{
               "us-geo":"s3.us.cloud-object-storage.appdomain.cloud",
               "Dallas":"s3.dal.us.cloud-object-storage.appdomain.cloud",
               "Washington":"s3.wdc.us.cloud-object-storage.appdomain.cloud",
               "San Jose":"s3.sjc.us.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "us-geo":"s3.private.us.cloud-object-storage.appdomain.cloud",
               "Dallas":"s3.private.dal.us.cloud-object-storage.appdomain.cloud",
               "Washington":"s3.private.wdc.us.cloud-object-storage.appdomain.cloud",
               "San Jose":"s3.private.sjc.us.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "us-geo":"s3.direct.us.cloud-object-storage.appdomain.cloud",
               "Dallas":"s3.direct.dal.us.cloud-object-storage.appdomain.cloud",
               "Washington":"s3.direct.wdc.us.cloud-object-storage.appdomain.cloud",
               "San Jose":"s3.direct.sjc.us.cloud-object-storage.appdomain.cloud"
            }
         },
         "eu":{
            "public":{
               "eu-geo":"s3.eu.cloud-object-storage.appdomain.cloud",
               "Amsterdam":"s3.ams.eu.cloud-object-storage.appdomain.cloud",
               "Frankfurt":"s3.fra.eu.cloud-object-storage.appdomain.cloud",
               "Milan":"s3.mil.eu.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "eu-geo":"s3.private.eu.cloud-object-storage.appdomain.cloud",
               "Amsterdam":"s3.private.ams.eu.cloud-object-storage.appdomain.cloud",
               "Frankfurt":"s3.private.fra.eu.cloud-object-storage.appdomain.cloud",
               "Milan":"s3.private.mil.eu.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "eu-geo":"s3.direct.eu.cloud-object-storage.appdomain.cloud",
               "Amsterdam":"s3.direct.ams.eu.cloud-object-storage.appdomain.cloud",
               "Frankfurt":"s3.direct.fra.eu.cloud-object-storage.appdomain.cloud",
               "Milan":"s3.direct.mil.eu.cloud-object-storage.appdomain.cloud"
            }
         },
         "ap":{
            "public":{
               "ap-geo":"s3.ap.cloud-object-storage.appdomain.cloud",
               "Tokyo":"s3.tok.ap.cloud-object-storage.appdomain.cloud",
               "Seoul":"s3.seo.ap.cloud-object-storage.appdomain.cloud",
               "Hong Kong":"s3.hkg.ap.cloud-object-storage.appdomain.cloud",
               "Sydney":"s3.syd.ap.cloud-object-storage.appdomain.cloud",
               "Osaka":"s3.osa.ap.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "ap-geo":"s3.private.ap.cloud-object-storage.appdomain.cloud",
               "Tokyo":"s3.private.tok.ap.cloud-object-storage.appdomain.cloud",
               "Seoul":"s3.private.seo.ap.cloud-object-storage.appdomain.cloud",
               "Hong Kong":"s3.private.hkg.ap.cloud-object-storage.appdomain.cloud",
               "Sydney":"s3.private.syd.ap.cloud-object-storage.appdomain.cloud",
               "Osaka":"s3.private.osa.ap.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "ap-geo":"s3.direct.ap.cloud-object-storage.appdomain.cloud",
               "Tokyo":"s3.direct.tok.ap.cloud-object-storage.appdomain.cloud",
               "Seoul":"s3.direct.seo.ap.cloud-object-storage.appdomain.cloud",
               "Hong Kong":"s3.direct.hkg.ap.cloud-object-storage.appdomain.cloud",
               "Sydney":"s3.direct.syd.ap.cloud-object-storage.appdomain.cloud",
               "Osaka":"s3.direct.osa.ap.cloud-object-storage.appdomain.cloud"
            }
         }
      },
      "regional":{
         "us-south":{
            "public":{
               "us-south":"s3.us-south.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "us-south":"s3.private.us-south.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "us-south":"s3.direct.us-south.cloud-object-storage.appdomain.cloud"
            }
         },
         "us-east":{
            "public":{
               "us-east":"s3.us-east.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "us-east":"s3.private.us-east.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "us-east":"s3.direct.us-east.cloud-object-storage.appdomain.cloud"
            }
         },
         "eu-gb":{
            "public":{
               "eu-gb":"s3.eu-gb.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "eu-gb":"s3.private.eu-gb.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "eu-gb":"s3.direct.eu-gb.cloud-object-storage.appdomain.cloud"
            }
         },
         "eu-de":{
            "public":{
               "eu-de":"s3.eu-de.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "eu-de":"s3.private.eu-de.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "eu-de":"s3.direct.eu-de.cloud-object-storage.appdomain.cloud"
            }
         },
         "jp-tok":{
            "public":{
               "jp-tok":"s3.jp-tok.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "jp-tok":"s3.private.jp-tok.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "jp-tok":"s3.direct.jp-tok.cloud-object-storage.appdomain.cloud"
            }
         },
         "jp-osa":{
            "public":{
               "jp-osa":"s3.jp-osa.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "jp-osa":"s3.private.jp-osa.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "jp-osa":"s3.direct.jp-osa.cloud-object-storage.appdomain.cloud"
            }
         },
         "au-syd":{
            "public":{
               "au-syd":"s3.au-syd.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "au-syd":"s3.private.au-syd.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "au-syd":"s3.direct.au-syd.cloud-object-storage.appdomain.cloud"
            }
         },
         "ca-tor":{
            "public":{
               "ca-tor":"s3.ca-tor.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "ca-tor":"s3.private.ca-tor.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "ca-tor":"s3.direct.ca-tor.cloud-object-storage.appdomain.cloud"
            }
         },
         "br-sao":{
            "public":{
               "br-sao":"s3.br-sao.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "br-sao":"s3.private.br-sao.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "br-sao":"s3.direct.br-sao.cloud-object-storage.appdomain.cloud"
            }
         }
      },
      "single-site":{
         "ams03":{
            "public":{
               "ams03":"s3.ams03.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "ams03":"s3.private.ams03.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "ams03":"s3.direct.ams03.cloud-object-storage.appdomain.cloud"
            }
         },
         "che01":{
            "public":{
               "che01":"s3.che01.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "che01":"s3.private.che01.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "che01":"s3.direct.che01.cloud-object-storage.appdomain.cloud"
            }
         },
         "mon01":{
            "public":{
               "mon01":"s3.mon01.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "mon01":"s3.private.mon01.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "mon01":"s3.direct.mon01.cloud-object-storage.appdomain.cloud"
            }
         },
         "mex01":{
            "public":{
               "mex01":"s3.mex01.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "mex01":"s3.private.mex01.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "mex01":"s3.direct.mex01.cloud-object-storage.appdomain.cloud"
            }
         },
         "sjc04":{
            "public":{
               "sjc04":"s3.sjc04.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "sjc04":"s3.private.sjc04.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "sjc04":"s3.direct.sjc04.cloud-object-storage.appdomain.cloud"
            }
         },
         "mil01":{
            "public":{
               "mil01":"s3.mil01.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "mil01":"s3.private.mil01.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "mil01":"s3.direct.mil01.cloud-object-storage.appdomain.cloud"
            }
         },
         "par01":{
            "public":{
               "par01":"s3.par01.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "par01":"s3.private.par01.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "par01":"s3.direct.par01.cloud-object-storage.appdomain.cloud"
            }
         },
         "sng01":{
            "public":{
               "sng01":"s3.sng01.cloud-object-storage.appdomain.cloud"
            },
            "private":{
               "sng01":"s3.private.sng01.cloud-object-storage.appdomain.cloud"
            },
            "direct":{
               "sng01":"s3.direct.sng01.cloud-object-storage.appdomain.cloud"
            }
         }
      }
   },
   "resource-configuration-endpoints":{
      "global":{
         "public":"config.cloud-object-storage.cloud.ibm.com/v1",
         "private":"config.private.cloud-object-storage.cloud.ibm.com/v1",
         "direct":"config.direct.cloud-object-storage.cloud.ibm.com/v1"
      }
   }
}
```

When creating a client by using a library that requires an "auth" endpoint value, you need to add `/oidc/token` to end of the `iam-token` URL provided above.
{: tip}

## Using service credentials for single-bucket access
{: #service-credentials-bucket}

When a service credential is created, the underlying Service ID is granted a role on the entire instance of {{site.data.keyword.cos_short}}. If the intention that the credential be used to grant,  access to a subset of buckets and not the entire instance, this policy needs to be edited. See the [Bucket permissions](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions) page for more details.

## Using service credentials for single-object/folder access
{: #service-credentials-fgac}

When a service credential is created, the underlying Service ID is granted a role on the entire instance of {{site.data.keyword.cos_short}}. If the intention that the credential be used to grant access to a subset of buckets and not the entire instance, this policy needs to be edited. See the [Assigning access to objects within a bucket using IAM access conditions](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions) page for more details.

## Using an API Key for accessing multiple instances
{: #service-credentials-apikey}

If the intention is to use the same API key for more than one {{site.data.keyword.cos_short}} instance, you can modify the access policy of the service ID to grant access. Each API key that is associated with a service ID inherits the policy that is assigned to the service ID.

### Use the Service ID generated from an existing instance
When you create a service credential for a particular instance, that credential has an API Key value. To use that same API Key with another {{site.data.keyword.cos_short}} instance, modify the access policy for the service id associated with that API Key to grant it access to the additional instance.

### Generate a Service ID directly
If you prefer to limit visibility of the API key after its creation, follow the steps in [Creating an API key for a service ID](/docs/account?topic=account-serviceidapikeys&interface=ui#create_service_key). As noted in that topic, make sure you copy or download the key at time of creation. Then, configure the access policies for the service ID to have access to one or more {{site.data.keyword.cos_short}} instances.

## API Key vs HMAC
{: #service-credentials-iam-hmac}

In general IAM API Keys are the preferred method of authentication for {{site.data.keyword.cos_full}}. HMAC is supported primarily for compatibility with an earlier version with applications which migrated from IaaS {{site.data.keyword.cos_short}} and legacy S3 applications. IAM is also natively supported when developing applications with the COS SDKs. Token expiration and refresh are handled automatically to simplify the process.


For more information about IAM visit - [Getting started with IAM](/docs/cloud-object-storage?topic=cloud-object-storage-iam)

For more information about HMAC visit - [Using HMAC Credentials](/docs/cloud-object-storage?topic=cloud-object-storage-uhc-hmac-credentials-main)
