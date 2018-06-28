---

copyright:
  years: 2017, 2018
lastupdated: "2018-06-27"

---

# Using Cloud Object Storage with Cloud Foundry Apps

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

{{site.data.keyword.cos_full}} can be paired with IBM&reg; Cloud Foundry Enterprise Environment applications to provide highly-available content through the use of regions and endpoints.

## Cloud Foundry Enterprise Environment

IBM&reg; Cloud Foundry Enterprise Environment is a platform for hosting apps and services in the cloud. You can instantiate multiple, isolated, enterprise-grade platforms on demand that is run within your own account and can be deployed on either shared or dedicated hardware.  The platform makes it easy to scale apps as consumption grows, simplifying the runtime and infrastructure so that you can focus on development.

Successful implementation of a Cloud Foundry platform requires [proper planning and design](/docs/cloud-foundry/design-structure.html#bpimplementation) for necessary resources and enterprise requirements.  Learn more about [getting started](/docs/cloud-foundry/index.html#creating) with the Cloud Foundry Enterprise Environment as well as an introductory [tutorial](/docs/cloud-foundry/getting-started.html#getting-started).

## Regions and Aliasing
Explain the nature of IBM Cloud regions and why aliasing is necessary for COS

## Storing Credentials as VCAP Variables 
Provide an example of storing credentials as VCAP variables

{{site.data.keyword.cos_short}} credentials can stored in the VCAP_SERVICES environment variable which can be parsed for use when accessing the Cloud Storage service.  The credentials include information as presented in the following example:

```json
{
  "apikey": "abcDEFg_loQtE13laVRPAbnnBUqKIPayN4EyJnBnYU9S-",
  "endpoints": "https://cos-service.bluemix.net/endpoints",
  "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:8899d05c-b172-2416-4d7e-0e5c326b2605::",
  "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
  "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
  "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
}
```

The VCAP_SERVICES environment variable can then be parsed within your application in order to access your cloud storage content.  Consider the following Python example:

```Python
import os
import json

vcap_env = os.environ['VCAP_SERVICES']
vcap = json.loads(vcap_env)

# Constants for IBM COS values
COS_API_KEY_ID = vcap['apikey']
COS_SERVICE_CRN = vcap['iam_serviceid_crn']
```

For more information on how to use the SDK to access {{site.data.keyword.cos_short}} with code examples visit:

* [Using Java](/docs/services/cloud-object-storage/libraries/java.html#using-java)
* [Using Python](/docs/services/cloud-object-storage/libraries/python.html#using-python)
* [Using Node.js](/docs/services/cloud-object-storage/libraries/node.html#using-node-js)

## Creating Bindings 

### Dashboard
Provide examples of using the UI to create bindings

### IBM Client Tools (CLI)
Provide example of using the CLI to create bindings

### IBM Client Tools (CLI) with HMAC
Provide example of using the CLI to create a binding with HMAC credentials