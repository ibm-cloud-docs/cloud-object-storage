---

copyright:
  years: 2023
lastupdated: "2023-10-30"

keywords: object, bucket, iam, policy, role, tutorial

subcollection: cloud-object-storage

content-type: tutorial

services: cloud-object-storage

account-plan: lite

completion-time: 15m

---

{{site.data.keyword.attribute-definition-list}}

# Controling access to individual objects in a bucket
{: #object-access-tutorial}
{: toc-content-type="tutorial"}
{: toc-services="cloud-object-storage"}
{: toc-completion-time="15m"}

This tutorial provides examples for how to use IAM access policies with {{site.data.keyword.cos_full}} buckets to grant users access to [individual objects within a bucket](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions&interface=ui).
{: shortdesc}

## Before you begin
{: #object-access-before-you-begin}

{{site.data.keyword.cos_full_notm}} stores data in a flat structure where one bucket can contain billions of distinctly-named objects. A folder hierarchy can be simulated by using identical prefixes in related object names. Also, an object name can be referred to as an object key. Here is an example:

```text
Bucket Name: MyBucket
Objects in MyBucket:

User1/userDoc1.txt
User1/userDoc2.zip
Engineering/project1.git
Engineering/project2.git
Product/2023/roadmap1.ppt
Product/2024/roadmap2.ppt
Orgchart.pdf
```

In this example, the prefix ```User1```, ```Engineering```, and ```Product``` can resemble root
level folders. In addition, ```2023``` and ```2024``` can represent subdirectories. Use the delimiter “/” to represent the file hierarchy. A delimiter can be any supported character. ```Orgchart.pdf``` is considered a root-level object.

When running a list request on your bucket, you can specify a prefix for listing objects or list the content of the entire bucket. In addition, you can optionally pass a delimiter value in the listing request to simulate a folder structure in the response. For more information, see the examples of using [prefix and delimiter](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions&interface=cli#fgac-attributes-prefix-delimeter) condition statements.

Read or write operations typically target a specific object name which is also referred to as the object [path](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions&interface=cli#fgac-attributes-path).

## Scenarios
{: #object-access-scenarios}

The following examples show how to use IAM policies and conditions to grant access to individual objects in a bucket. We will continue to use the example bucket shown above. These examples show excerpts of the full access policy with respect to configuring the condition statements. For more information, see [Assigning access to objects within a bucket using IAM access conditions](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions&interface=cli).

### Scenario 1: Grant Adam read access to all objects in the ```User1``` folder only.
{: #object-access-scenario-1}

This will give Adam the ability to read all objects that start with the key name of ```User1/```. This will not give Adam the ability to list objects and he therefore cannot navigate the UI to access these objects. Adam can only retrieve objects in the ```User1``` folder through non-UI methods. Use a wildcard in the policy to give access to all possible objects that begin with ```User1/```. Failure to include a wildcard would give Adam only access to the object named ```User1/```.

```sh
"control": {
    "grant": {
      "roles": [
         { "role_id":
             "crn:v1:bluemix:public:iam::::serviceRole:ObjectReader"
         }
       ]
     }
   },
   "rule": {
     "conditions": [
       {
         "key": "{{resource.attributes.path}}",
         "operator": "stringMatch",
         "value": "User1/*"
       }
     ]
   },
   "pattern": "attribute-based-condition:resource:literal-and-wildcard"
```
{: codeblock}

### Scenario 2: Grant Adam list and read access to all objects in the ```User1``` folder.
{: #object-access-scenario-2}

This will give Adam the ability to read and list all objects that start with the key name of ```User1/```. Also, use a wildcard in the ```prefix``` condition attribute. Failure to include the wildcard results in Adam only having List access to the first level of objects or folders in the ```User1``` folder. This policy will not permit Adam to list the bucket at the root level. If Adam uses the UI, he must search the bucket with the specific prefix of ```User1/``` to see the objects for which he has access.

```sh
"control": {
    "grant": {
      "roles": [
        {
          "role_id":
          "crn:v1:bluemix:public:iam::::serviceRole:ContentReader"
        }
      ]
    }
  },
  "rule": {
    "operator": "or",
    "conditions": [
      {
        "operator": "and",
        "conditions": [
          {
            "key": "{{resource.attributes.prefix}}",
            "operator": "stringMatch",
            "value": "User1/*"
          },
          {
            "key": "{{resource.attributes.delimiter}}",
            "operator": "stringEquals",
            "value": "/"
          }
        ]
      },
      {
        "key": "{{resource.attributes.path}}",
        "operator": "stringMatch",
        "value": "User1/*"
      }
    ]
  },
  "pattern": "attribute-based-condition:resource:literal-and-wildcard
```
{: codeblock}

### Scenario 3: Grant Samantha accesss to list, read, and replicate files in only the ```2023``` and ```2024``` subdirectories under the ```Product``` folder.
{: #object-access-scenario-3}

These sets of actions will require Samantha to have at least the Writer role. The Writer role also contains some actions that do not specify a ```Path``` or a ```Prefix``` or ```Delimiter``` such as ```cloud-object-storage.bucket.put_replication```. To allow these actions, use the [StringExists](/docs/cloud-object-storage?topic=cloud-object-storage-fgac-iam-access-conditions&interface=ui#fgac-conditions-actions-not-supported) operator with the resource attributes based conditions.

Samantha will not have access to navigate the UI from the root folder. This situation is shown in [Scenario 4](#object-access-scenario-4).

```sh
"control": {
    "grant": {
      "roles": [
        {
          "role_id": "crn:v1:bluemix:public:iam::::serviceRole:Writer"
        }
      ]
    }
  },
  "rule": {
    "operator": "or",
    "conditions": [
      {
        "operator": "and",
        "conditions": [
          {
            "key": "{{resource.attributes.prefix}}",
            "operator": "stringMatchAnyOf",
            "value": [
              "Product/2023/*",
              "Product/2024/*"
            ]
          },
          {
            "key": "{{resource.attributes.delimiter}}",
            "operator": "stringEquals",
            "value": "/"
          }
        ]
      },
      {
      "key": "{{resource.attributes.path}}",
      "operator": "stringMatchAnyOf",
      "value": [
        "Product/2023/*",
        "Product/2024/*"
      ]
    },
    {
      "operator": "and",
      "conditions": [
        {
          "key": "{{resource.attributes.delimiter}}",
          "operator": "stringExists",
          "value": false1
        },
        {
          "key": "{{resource.attributes.prefix}}",
          "operator": "stringExists",
          "value": false
        },
        {
          "key": "{{resource.attributes.path}}",
          "operator": "stringExists",
          "value": false
        }
      ]
    }
  ]
},
"pattern": "attribute-based-condition:resource:literal-and-wildcard"
```
{: codeblock}

### Scenario 4: Grant Samantha access to navigate the UI to the files in the ```2023``` and ```2024``` folders in addition to list, read and replicate files in ```2023``` and ```2024```
{: #object-access-scenario-4}

To navigate the UI to ```MyBucket```, Samantha needs the Platform role. In addition, Samantha is given access to any directories above the target folder. In this case, Samantha needs access to list the root level (defined by the prefix of the empty string) and the ```Product/``` folder. This allows Samantha to see all root level folders and objects.

```sh
"control": {
    "grant": {
      "roles": [
        {
          "role_id": "crn:v1:bluemix:public:iam::::serviceRole:Writer"
        },
        {
          "role_id": "crn:v1:bluemix:public:iam::::role:Viewer"
        }
      ]
    }
  },
  "rule": {
    "operator": "or",
    "conditions": [
      {
        "operator": "and",
        "conditions": [
          {
            "key": "{{resource.attributes.prefix}}",
            "operator": "stringMatchAnyOf",
            "value": [
              "Product/2023/*",
              "Product/2024/*"
          ]
        },
        {
          "key": "{{resource.attributes.delimiter}}",
          "operator": "stringEquals",
          "value": "/"
          }
        ]
      },
      {
        "operator": "and",
        "conditions": [
          {
            "key": "{{resource.attributes.prefix}}",
            "operator": "stringEqualsAnyOf",
            "value": [
              "",
              "Product/"
            ]
          },
          {
            "key": "{{resource.attributes.delimiter}}",
            "operator": "stringEquals",
            "value": "/"
          }
        ]
      },
      {
        "key": "{{resource.attributes.path}}",
        "operator": "stringMatchAnyOf",
        "value": [
          "Product/2023/*",
          "Product/2024/*"
        ]
      },
      {
        "operator": "and",
        "conditions": [
          {
            "key": "{{resource.attributes.delimiter}}",
            "operator": "stringExists",
            "value": false
          },
          {
            "key": "{{resource.attributes.prefix}}",
            "operator": "stringExists",
            "value": false
          },
          {
            "key": "{{resource.attributes.path}}",
            "operator": "stringExists",
            "value": false
          }
        ]
      }
    ]
  },
  "pattern": "attribute-based-condition:resource:literal-and-wildcard"
```
{: codeblock}















