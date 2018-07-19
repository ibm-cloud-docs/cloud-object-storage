---

copyright:
  years: 2017, 2018
lastupdated: "2018-07-19"

---

# Using Cloud Object Storage with Cloud Foundry Apps

{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

{{site.data.keyword.cos_full}} can be paired with {{site.data.keyword.cfee_full}} applications to provide highly-available content through the use of regions and endpoints.

## Cloud Foundry Enterprise Environment

{{site.data.keyword.cfee_full}} is a platform for hosting apps and services in the cloud. You can instantiate multiple, isolated, enterprise-grade platforms on demand that is run within your own account and can be deployed on either shared or dedicated hardware.  The platform makes it easy to scale apps as consumption grows, simplifying the runtime and infrastructure so that you can focus on development.

Successful implementation of a Cloud Foundry platform requires [proper planning and design](/docs/cloud-foundry/design-structure.html#bpimplementation) for necessary resources and enterprise requirements.  Learn more about [getting started](/docs/cloud-foundry/index.html#creating) with the Cloud Foundry Enterprise Environment as well as an introductory [tutorial](/docs/cloud-foundry/getting-started.html#getting-started).

### Regions

[Regional endpoints](/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints) are an important part of the IBM Cloud Environment.  You can create applications and service instances in different regions with the same IBM Cloud infrastructure for application management and the same usage details view for billing.  By choosing an IBM Cloud region that is geographically close to you or your customers, you can reduce data latency in your applications as well as minimize costs. Regions can also be selected address any security concerns or regulatory requirements.  

With {{site.data.keyword.cos_full}} you can choose to disperse data across a single data center, an entire region, or even a combination of regions by [selecting the endpoint](/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints) where your application sends API requests.

### Resource Connections and Aliases

An alias is a connection between your managed service within a resource group and an application within an org or a space. Aliases are like symbolic links that hold references to remote resources.  It enables interoperability and reuse of an instance across the platform.  In the {{site.data.keyword.cloud_notm}} console, the connection (alias) is represented as a service instance.  You can create an instance of a service in a resource group and then reuse it from any available region by creating an alias in an org or space in those regions.

## Storing Credentials as VCAP Variables 

{{site.data.keyword.cos_short}} credentials can stored in the VCAP_SERVICES environment variable which can be parsed for use when accessing the {{site.data.keyword.cos_short}} service.  The credentials include information as presented in the following example:

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://cos-service.bluemix.net/endpoints",
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

The VCAP_SERVICES environment variable can then be parsed within your application in order to access your {{site.data.keyword.cos_short}} content.  Below is an example of integrating the environment variable with the COS SDK using Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.softlayer.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.ng.bluemix.net/oidc/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

For more information on how to use the SDK to access {{site.data.keyword.cos_short}} with code examples visit:

* [Using Java](/docs/services/cloud-object-storage/libraries/java.html#using-java)
* [Using Python](/docs/services/cloud-object-storage/libraries/python.html#using-python)
* [Using Node.js](/docs/services/cloud-object-storage/libraries/node.html#using-node-js)

## Creating Service Bindings 

### Dashboard

The simplest way to create a service binding is by using the [{{site.data.keyword.cloud}} Dashboard](https://console.bluemix.net/dashboard/apps).  

1. Login to the [Dashboard](https://console.bluemix.net/dashboard/apps)
2. Click on your Cloud Foundry application
3. Click on Connections in the menu on the left
4. Click on the **Create Connection** button on the right
5. From the *Connect Existing Compatible Service* page hover over your {{site.data.keyword.cos_short}} service and click on the **Connect** button.
6. From the *Connect IAM-Enabled Service* popup screen select the Access Role, leave Auto Generate for the Service ID, and click **Connect**
7. The Cloud Foundry application will need to be restaged in order to use the new service binding.  Click the **Restage** button to start the process.
8. Once restaging is complete your Cloud Object Storage service will be available to your application.

The applications VCAP_SERVICES environment variable will be automatically updated with the service information.  To view the new variable:

1. Click on *Runtime* in the menu on the right
2. Click on *Environment variables*
3. Verify your COS service is now listed

### IBM Client Tools (CLI)

1. Login to with IBM Cloud CLI
```
 bx login --apikey <your api key>
```

2. Target your Cloud Foundry environment
```
 bx target --cf
```

3. Create a service alias for your {{site.data.keyword.cos_short}}
```
bx resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Create a service binding between your {{site.data.keyword.cos_short}} alias and your Cloud Foundry application and provide a role for your binding.  Valid roles are:<br/><ul><li>Writer</li><li>Reader</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Viewer</li><li>Editor</li></ul>
```
bx resource service-binding-create <service alias> <cf app name> <role>
```

### IBM Client Tools (CLI) with HMAC Credentials

Hash-based message authentication code (HMAC) is a mechanism for calculating a message authentication code created using a pair of access and secret keys. This can be used to verify the integrity and authenticity of a message.  More information about using [HMAC credentials](/docs/services/cloud-object-storage/hmac/credentials.html#using-hmac-credentials) is available in the {{site.data.keyword.cos_short}} documentation.

1. Login to with IBM Cloud CLI
```
 bx login --apikey <your api key>
```

2. Target your Cloud Foundry environment
```
 bx target --cf
```

3. Create a service alias for your {{site.data.keyword.cos_short}}
```
bx resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Create a service binding between your {{site.data.keyword.cos_short}} alias and your Cloud Foundry application and provide a role for your binding.<br/><br/>***Note:** An additional parameter* (`{"HMAC":true}`) *is necessary to create service credentals with HMAC enabled.*<br/><br/>Valid roles are:<br/><ul><li>Writer</li><li>Reader</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Viewer</li><li>Editor</li></ul>
```
bx resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### Binding to {{site.data.keyword.containershort_notm}}

Creating a service binding to {{site.data.keyword.containershort}} requires a slightly different procedure.  For this section you will also need to install [jq - a lightweight command-line JSON processor](https://stedolan.github.io/jq/){:new_window}.

1. Generate COS HMAC credentials and put in K8s<br/><br/>***Note:** cluster-service-bind requires an existing alias to a COS global instance*
```
bx resource service-alias-create cos-alias --instance-name
```
 
2. Create a new service key with write permissions to the COS 
```
bx resource service-key-create "cos-hmac" Writer --alias-name cos-alias --parameters '{"HMAC":true}’
```

3. Bind the cluster service to COS
```
bx cs cluster-service-bind default cos-alias
```

4. Verify cos-alias is bound to the cluster
```
bx cs cluster-services cos-alias 0fd0adbe-4811-4c85-b781-2d0d68f2207e cos-hmac default
```

5. Verify COS HMAC credentials are available in your cluster Secrets<br/><br/>***Note:** This should be changed to verify all COS credentials are in Secrets*
```
kubectl get secret binding-cos-alias -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
output should look like this:
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```