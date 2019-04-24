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

# Using Cloud Object Storage with Cloud Foundry Apps
{: #cloud-foundry}

{{site.data.keyword.cos_full}} can be paired with {{site.data.keyword.cfee_full}} applications to provide highly-available content through the use of regions and endpoints.

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} is a platform for hosting apps and services in the cloud. You can instantiate multiple, isolated, enterprise-grade platforms on demand that is run within your own account and can be deployed on either shared or dedicated hardware.  The platform makes it easy to scale apps as consumption grows, simplifying the runtime and infrastructure so that you can focus on development.

Successful implementation of a Cloud Foundry platform requires [proper planning and design](/docs/cloud-foundry/design-structure.html#bpimplementation) for necessary resources and enterprise requirements.  Learn more about [getting started](/docs/cloud-foundry/index.html#creating) with the Cloud Foundry Enterprise Environment as well as an introductory [tutorial](/docs/cloud-foundry/getting-started.html#getting-started).

### Regions
{: #cloud-foundry-regions}
[Regional endpoints](/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints) are an important part of the IBM Cloud Environment.  You can create applications and service instances in different regions with the same IBM Cloud infrastructure for application management and the same usage details view for billing.  By choosing an IBM Cloud region that is geographically close to you or your customers, you can reduce data latency in your applications as well as minimize costs. Regions can also be selected address any security concerns or regulatory requirements.  

With {{site.data.keyword.cos_full}} you can choose to disperse data across a single data center, an entire region, or even a combination of regions by [selecting the endpoint](/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints) where your application sends API requests.

### Resource Connections and Aliases
{: #cloud-foundry-aliases}

An alias is a connection between your managed service within a resource group and an application within an org or a space. Aliases are like symbolic links that hold references to remote resources.  It enables interoperability and reuse of an instance across the platform.  In the {{site.data.keyword.cloud_notm}} console, the connection (alias) is represented as a service instance.  You can create an instance of a service in a resource group and then reuse it from any available region by creating an alias in an org or space in those regions.

## Storing Credentials as VCAP Variables 
{: #cloud-foundry-vcap}

{{site.data.keyword.cos_short}} credentials can stored in the VCAP_SERVICES environment variable which can be parsed for use when accessing the {{site.data.keyword.cos_short}} service.  The credentials include information as presented in the following example:

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

The VCAP_SERVICES environment variable can then be parsed within your application in order to access your {{site.data.keyword.cos_short}} content.  Below is an example of integrating the environment variable with the COS SDK using Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
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

For more information on how to use the SDK to access {{site.data.keyword.cos_short}} with code examples visit:

* [Using Java](/docs/services/cloud-object-storage/libraries/java.html#using-java)
* [Using Python](/docs/services/cloud-object-storage/libraries/python.html#using-python)
* [Using Node.js](/docs/services/cloud-object-storage/libraries/node.html#using-node-js)

## Creating Service Bindings 
{: #cloud-foundry-bindings}

### Dashboard
{: #cloud-foundry-bindings-console}

The simplest way to create a service binding is by using the [{{site.data.keyword.cloud}} Dashboard](https://cloud.ibm.com/dashboard/apps).  

1. Login to the [Dashboard](https://cloud.ibm.com/dashboard/apps)
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
{: #cloud-foundry-bindings-cli}

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
{: #cloud-foundry-hmac}

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

4. Create a service binding between your {{site.data.keyword.cos_short}} alias and your Cloud Foundry application and provide a role for your binding.<br/><br/>* **Note:** An additional parameter* (`{"HMAC":true}`) *is necessary to create service credentals with HMAC enabled.*<br/><br/>Valid roles are:<br/><ul><li>Writer</li><li>Reader</li><li>Manager</li><li>Administrator</li><li>Operator</li><li>Viewer</li><li>Editor</li></ul>
```
bx resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### Binding to {{site.data.keyword.containershort_notm}}
{: #cloud-foundry-k8s}

Creating a service binding to {{site.data.keyword.containershort}} requires a slightly different procedure.  

*For this section you will also need to install [jq - a lightweight command-line JSON processor](https://stedolan.github.io/jq/){:new_window}.*

You will need the following information and subtitute the key values in commands below:

* `<service alias>` - new alias name for COS service
* `<cos instance name>` - name of your existing COS instance
* `<service credential name>` - new name for your service key/credential
* `<role>` - role to attach to your service key (see above for valid roles, `Writer` is most often specified)
* `<cluster name>` - name of your existing Kubernetes cluster service
* `<secret binding name>` - this value is generated when COS is bound to the cluster service


1. Create a service alias for your COS Instance<br/><br/>* **Note:** COS Instance can only have one service alias*
```
bx resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. Create a new service key with permissions to the COS service alias
```
bx resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. Bind the cluster service to COS
```
bx cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. Verify COS service alias is bound to the cluster
```
bx cs cluster-services --cluster <cluster name>
```
output should look like this:
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. Retrieve the list of Secrets in your cluster and find the secret for your COS service.  Typically it will be `binding-` plus the `<service alias>` you specified in step 1 (i.e. `binding-sv-cos`).  Use this value as `<secret binding name>` in step 6.
```
kubectl get secrets
```
output should look like this:
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. Verify COS HMAC credentials are available in your cluster Secrets
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
output should look like this:
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```