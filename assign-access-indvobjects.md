---

copyright:
  years: 2023, 2024
lastupdated: "2024-08-13"

keywords: IAM, policy, fine-grained access control, controls, conditions, prefix, delimiter, path, folder1/subfolder1/file.txt, folder1, subfolder1, wildcard, operator, stringMatchAnyOf, stringexists

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Assigning access to objects within a bucket using IAM access conditions
{: #fgac-iam-access-conditions}

IAM access policies allow granting permissions in a Cloud Object Storage bucket to specific groups of objects. This approach allows for fine-grained access control over data access, making it useful in scenarios where different parts of a bucket need to be accessed by different users or applications.
{: shortdesc}

If access is required to the entire bucket (that is, when fine-grained access control is not required) then follow the information on [Assigning access to an individual bucket](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions&interface=ui).
{: important}

Each object that is stored in a Cloud Object Storage bucket has a unique key, and these keys often follow a hierarchical structure similar to a file system.

Related links to IAM access policies
- [Resource attribute-based conditions](/docs/account?topic=account-iam-condition-properties&interface=ui#resource-based-conditions)
- [String comparisons](/docs/account?topic=account-wildcard#string-comparisons)
- [Checking a policy version in the console](/docs/account?topic=account-known-issues#check-policy-version)

**Example**

- An individual object with the key *"folder1/subfolder1/file.txt"* can simulate a folder or directory hierarchy, where the directory *“folder1”* contains a subdirectory that is named *“sub-folder1”* containing a file *“file.txt”*. Access can be assigned at any folder level.
- An access policy can be created for all objects and - in the folder named *“folder1”*, or access can be assigned for just objects in the subdirectory named *“subfolder1”*.

A policy administrator can assign access to individual objects and folders by configuring conditions when creating IAM access policies. The next section describes how to construct these types of policies.

## Constructing a fine-grained access control policy
{: #fgac-construct-policy}

The first step to granting access to individual objects within a bucket is to construct an IAM policy. You can find more information on constructing an IAM access policy for {{site.data.keyword.cos_short}} in the Cloud Object Storage tutorial [Limiting access to a single Object Storage bucket](/docs/cloud-object-storage?topic=cloud-object-storage-limit-access&interface=ui). For more general information on building IAM policies, go to [How IBM Cloud IAM works](/docs/account?topic=account-iamoverview). The following items are key components of building an IAM access policy for your {{site.data.keyword.cos_short}} resources.

### Subject
{: #fgac-key-concepts-subjects}

The subject of an access policy can be an individual user, an access group, a Service ID, or a Trusted Profile. See [What are IAM policies and who can assign them?](/docs/account?topic=account-iamusermanpol) for more information on the types of subjects you can apply to a policy.

### Service
{: #fgac-key-concepts-service}

The service is the IBM Cloud Service that contains the resource you are trying to assign access to. For assigning access to individual objects, use the Cloud Object Storage service.

### Resource
{: #fgac-key-concepts-resource}

{{site.data.keyword.cos_full}} supports the following resource targets:

- Resource group ID
- Service instance
- Resource type with value of *“bucket”*
- Resource ID (bucket name)

### Role
{: #fgac-key-concepts-role}

IBM Cloud access roles are groups of actions. Access roles allow the subject to complete specific tasks within the context of the target resources that are defined in the policy. Cloud Object Storage supports several pre-defined service roles that make assigning permissions easier. Cloud Object Storage also allows the creation of custom roles. For more information on the supported roles for Cloud Object Storage, see [Identity and Access Management roles](/docs/cloud-object-storage?topic=cloud-object-storage-iam&interface=ui#iam-roles).

For the list of Cloud Object Storage roles and their interaction with conditions, go to this [table](#fgac-conditions-service-roles).

### Condition
{: #fgac-key-concepts-condition}

When a resource is identified, a condition can be used to further scope access for a subject to individual objects in a bucket, which is referred to as fine-grained access control.


A single IAM Policy can have more than one condition by using an `OR` or `AND` statement to combine the conditions. The condition statement (containing one or more conditions) should evaluate to `TRUE` for the user request to be permitted to perform the action. IAM Policy will deny any action that does not get to be evaluated `TRUE`. The policy statement should contain all condition attributes required by the role. If the policy statement does not contain all condition attributes required by the role, the actions subject to the omitted condition attributes will be denied.

Use a policy with no condition attributes to give full access, as defined by the role, to the target resource.
{: important}

Use [IAM v2 policy](/apidocs/iam-policy-management#create-v2-policy) to construct IAM policy with attribute-based conditions.
{: tip}

## Using conditions in an IAM policy
{: #fgac-conditions}

Cloud Object Storage supports the following attributes to specify conditions for assigning fine-grained access on Cloud Object Storage resources:

### Prefix and Delimiter
{: #fgac-attributes-prefix-delimiter}

**Prefix** and **Delimiter** are used together to scope all listing permissions to specific objects in a bucket.

- The **Prefix** condition attribute defines the prefix for the set of object keys that this condition should allow for listing of objects or folders. For example, in the object named *"folder1/subfolder1/file.txt"*, both *"folder1/"* and *“folder1/subfolder1/”* are possible prefixes.

- A **Delimiter** helps the user navigate the bucket as if it was a file hierarchy. Assigning a delimiter condition statement restricts the type of folder structure that the user can generate in the listing. In an object named *"folder1/subfolder1/file.txt"*, the delimiter *“/”* can be used to simulate a folder hierarchy where each folder is separated by a *“/”*. If a condition statement allows only a delimiter of *“/”*, then a list request with any other delimiter value is not permitted.

Typically the prefix and delimiter are used together in a condition statement with an `AND` operator. It is possible to use a prefix without a delimiter in a condition statement. If the policy is configured with only a prefix and not a delimiter condition statement, the user can use any or no delimiter to list the objects.

**Examples of using Prefix and Delimiter condition statements**
{: #fgac-examples-prefix-delimiter-condition-statements}

Consider the object named *"folder1/subfolder1/file.txt"*:

Prefix of *"folder1/"* `AND` no Delimiter

- You can return a list of every object that starts with *folder1/* by doing a list request on *folder1/* and not providing a delimiter.
- If you use a delimiter of *"/"* in the list request, they'd be restricted to only seeing the first level of objects and sub-folders in *folder1/*.
- If user tries to list the subfolder (requests to list prefix = *“folder1/subfolder1/”*), access is denied.

Prefix of *"folder1/"* `AND` Delimiter of *"/"*

- You can only list the objects and sub-folders in the first level of *folder1*.
- You can only do list requests that specify a delimiter of *"/"*.
- If you try to list the contents of *subfolder1*, access is denied (user would need to have a condition of allowing prefix = *“folder1/subfolder1/"* to allow listing the contents of *subfolder1*).

The following APIs are subject to Prefix/Delimiter conditions:
- [GET Bucket (List Objects)](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets)
- GET Bucket Object Versions (List Object Versions)
- [List Multipart Uploads](/apidocs/cos/cos-compatibility?code=node#listmultipartuploads)


To see the full list of actions and the supported condition attributes, see [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions).
{: tip}

To give a fine-grained user access to navigate to their folder in the UI, the user needs access to list the root folder of the bucket. See [Scenario 4](/docs/cloud-object-storage?topic=cloud-object-storage-object-access-tutorial#object-access-scenario-4) for how to construct the policy to enable this.
{: tip}

### Path
{: #fgac-attributes-path}

Path is used to scope all read, write, or management access on specific objects.

For an object named *"folder1/subfolder1/file.txt"*, the full object key is the path. To restrict read, write or management actions to this object, define a condition with Path of *"folder1/subfolder1/file.txt"*.

All Cloud Object Storage APIs that act directly on an object are subject to Path conditions. See [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) for the list of Cloud Object Storage API actions that support Path.

It is recommended that you define both a Prefix/Delimiter condition and a Path condition when granting read, write or list actions to a user in the same policy. `Manager`, `Writer`, `Reader`, and `Content Reader` are examples of roles where it is recommended to define both a Prefix/Delimiter and Path condition. See [Scenario 4](/docs/cloud-object-storage?topic=cloud-object-storage-object-access-tutorial#object-access-scenario-4) for how to construct the policy to enable this. A condition specifying Prefix/Delimiter and a condition specifying Path should be logically `ORed` in the IAM Policy statement to permit both types of operations (read, write or management of objects or list objects).
{: note}

### Operators used with condition attributes
{: #fgac-attributes-operators}

There are several operators that can be used when defining condition attributes. The full list of operators that can be used for prefix, delimiter, and path condition attributes can be found in [Resource attribute-based conditions](/docs/account?topic=account-iam-condition-properties&interface=ui#resource-based-conditions).

### Use of wildcards
{: #fgac-attributes-wildcards}

A condition attribute’s values can include a [wildcard](/docs/account?topic=account-wildcard#) when the operator is `stringMatch` or `stringMatchAnyOf`.

**Examples of using wildcards in condition statements:**

Consider the object named *"folder1/subfolder1/file.txt"*:

Path of _“folder1/*”_

- You will get read, write, or management access, as defined by the role, to all objects that start with *“folder1/”*.

Prefix of _"folder1/*"_ `AND` no Delimiter

- For an object list request with prefix set to *“folder1/”* and no delimiter, the user request returns all objects that start with *“folder1/”*.
- For an object list request with prefix set to *“folder1/”* and delimiter of *“/”*, the request returns a view of the objects and folders just in the first level of *folder1*.
- For an object list request with prefix set to “folder1/subfolder1/” and delimiter of *“/”*, the request returns a view of the objects and folders just in the first level of *folder1/subfolder1*.

Prefix of _"folder1/*"_ `AND` Delimiter of *"/"*

- For an object list request with prefix set to *“folder1/”* and delimiter of *“/”*, the request returns a view of the objects and folders just in the first level of *folder1*.
- For an object list request with prefix set to “folder1/subfolder1/” and delimiter of *“/”*, the request returns the objects (and any sub-folders) in *folder1/subfolder1*.
- For an object list request with prefix set to *“folder1/”* and no delimiter, the request will not be permitted since a delimiter of *“/”* must be used in the list request for this policy to evaluate to true.

### Actions that do not use a Prefix/Delimiter or Path
{: #fgac-conditions-actions-not-supported}

There are some Cloud Object Storage APIs that do not specify a path or prefix and delimiter. The Cloud Object Storage Service roles: `Manager`, `Writer`, `Reader`, and `Content Reader` are examples of roles that contain these actions. This also applies to custom roles. To allow these actions when using a **Prefix/Delimiter** or **Path** condition, the following condition statement is needed in the IAM policy:

```sh
((path stringExists = false) AND (prefix stringExists = false) AND (delimiter stringExists= false))
```

See the [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) table for the full list of API actions that do not support **Prefix/Delimiter** or **Path** conditions and require the statement above when using fine-grained access.

Refer to the section on how to [Create a new policy for a user with conditions](#fgac-new-policy-conditions) for using this clause in an IAM policy.

## Use of conditions with Cloud Object Storage service roles
{: #fgac-conditions-service-roles}

| Access role         | Description of actions                                        | Supported Condition Attributes                            |
|:--------------------|-------------------------------------------------------------- |------------------------------------------------------|
| Manager             | Make objects public, create, and destroy buckets and objects. | See Note  |
| Writer              | Create and destroy buckets and objects.                       | See Note  |
| Reader              | List buckets, list objects, and download objects.             | See Note  |
| Content Reader      | List and download objects.                                    | See Note  |
| Object Reader       | Download objects.                                             | Path |
| Object Writer       | Upload objects.                                               | Path |
{: caption="Use of Conditions with COS Service Roles"}

**Note:** These roles support Prefix/Delimiter and Path condition attributes. The roles also include actions that do not specify a path or prefix and delimiter. Use the `stringExists` clause in the condition statement to allow these actions.

See [link](/docs/account?topic=account-iam-service-roles-actions&interface=ui#cloud-object-storage-roles) for the full list of actions for each Cloud Object Storage service role and the list of condition attributes that are supported by each action.

## Create a new policy for a user with conditions
{: #fgac-new-policy-conditions}

The following example provides a user with the `“Writer”` Cloud Object Storage Service Role with the ability to:

1. List access to the full object hierarchy within a folder named *"folder1/subfolder1"*.
2. Read, write, or delete access to all objects in a folder named *"subfolder1"*.
3. Perform bucket configuration management such as `HEAD Bucket` and `GET/PUT Bucket Versioning`.

### CLI of an IAM policy with a condition
{: #fgac-new-policy-conditions-cli}
{: cli}

For general information on how to use the CLI, see the section on [using the IBM Cloud® Command Line Interface](/docs/cli?topic=cli-ibmcloud_commands_iam).
{: tip}

Example

```sh
policy.json:

{
"type": "access",
  "subject": {
    "attributes": [
      {
        "value": "IBMid-664001QJNU",
        "operator": "stringEquals",
        "key": "iam_id"
      }
    ]
  },
  "resource": {
    "attributes": [
      {
        "value": "cloud-object-storage",
        "operator": "stringEquals",
        "key": "serviceName"
      },
      {
        "value": "e6156134-5ed7-4f73-80d3-d6d1ef56f1f9",
        "operator": "stringEquals",
        "key": "serviceInstance"
      },
      {
        "value": "bucket",
        "operator": "stringEquals",
        "key": "resourceType"
      },
      {
        "value": "fgac-tf-test",
        "operator": "stringEquals",
        "key": "resource"
      }
    ]
  },
  "control": {
    "grant": {
      "roles": [
        {
          "role_id": "crn:v1:bluemix:public:iam::::serviceRole:Writer"
        }      ]
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
            "value": "folder1/subfolder1/*"
          },
          {
            "key": "{{resource.attributes.delimiter}}",
            "operator": "stringEqualsAnyOf",
            "value": [
              "/",
              ""
            ]
          }
        ]
      },
      {
        "key": "{{resource.attributes.path}}",
        "operator": "stringMatch",
        "value": "folder1/subfolder1/*"
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
}
ibmcloud iam  user-policy-create hello@ibm.com --file policy.json --api-version v2
```
{: codeblock}


Use --api-version v2 with resource-based attribute conditions.
{: note}

### API of an IAM policy with a condition
{: #fgac-new-policy-conditions-api}
{: api}

Example

Request

```sh
curl -X POST 'https://iam.cloud.ibm.com/v2/policies' \
-H 'Authorization: Bearer $TOKEN' \
-H 'Content-Type: application/json' \
-d '{
  "type": "access",
  "description": "access control for RESOURCE_NAME",
  "resource": {
    "attributes": [
      {
        "key": "serviceName",
        "operator": "stringEquals",
        "value": "cloud-object-storage"
      },
      {
        "key": "serviceInstance",
        "operator": "stringEquals",
        "value": "$SERVICE_INSTANCE"
      },
      {
        "key": "accountId",
        "operator": "stringEquals",
        "value": "$ACCOUNT_ID"
      },
      {
        "key": "resourceType",
        "operator": "stringEquals",
        "value": "bucket"
      },
      {
        "key": "resource",
        "operator": "stringEquals",
        "value": "$RESOURCE_NAME"
      }
    ]
  },
  "subject": {
    "attributes": [
      {
        "key": "iam_id",
        "operator": "stringEquals",
        "value": "IBMid-123453user"
      }
    ]
  },
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
            "operator": "stringMatch",
            "value": "folder1/subfolder1/*"
          },
          {
            "key": "{{resource.attributes.delimiter}}",
            "operator": "stringEqualsAnyOf",
            "value": [
              "/",
              ""
            ]
          }
        ]
      },
      {
        "key": "{{resource.attributes.path}}",
        "operator": "stringMatch",
        "value": "folder1/subfolder1/*"
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
}'
```
{: codeblock}

Response

```sh
{
    "id": "d4078e99-d78a-4a50-95d6-b528e3c87dff",
    "description": "access control for RESOURCE_NAME",
    "type": "access",
    "subject": {
        "attributes": [
            {
                "key": "iam_id",
                "operator": "stringEquals",
                "value": "IBMid-123453user"
            }
        ]
    },
    "resource": {
        "attributes": [
            {
                "key": "serviceName",
                "operator": "stringEquals",
                "value": "cloud-object-storage"
            },
            {
                "key": "serviceInstance",
                "operator": "stringEquals",
                "value": "$SERVICE_INSTANCE"
            },
            {
                "key": "accountId",
                "operator": "stringEquals",
                "value": "$ACCOUNT_ID"
            },
            {
                "key": "resourceType",
                "operator": "stringEquals",
                "value": "bucket"
            },
            {
                "key": "resource",
                "operator": "stringEquals",
                "value": "$RESOURCE_NAME"
            }
        ]
    },
    "pattern": "attribute-based-condition:resource:literal-and-wildcard",
    "rule": {
        "operator": "or",
        "conditions": [
            {
                "operator": "and",
                "conditions": [
                    {
                        "key": "{{resource.attributes.prefix}}",
                        "operator": "stringMatch",
                        "value": "folder1/subfolder1/*"
                    },
                    {
                        "key": "{{resource.attributes.delimiter}}",
                        "operator": "stringEqualsAnyOf",
                        "value": [
                            "/",
                            ""
                        ]
                    }
                ]
            },
            {
                "key": "{{resource.attributes.path}}",
                "operator": "stringMatch",
                "value": "folder1/subfolder1/*"
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
    "control": {
        "grant": {
            "roles": [
                {
                    "role_id": "crn:v1:bluemix:public:iam::::serviceRole:Writer"
                }
            ]
        }
    },
    "href": "https://iam.test.cloud.ibm.com/v2/policies/d4078e99-d78a-4a50-95d6-b528e3c87dff",
    "created_at": "2023-10-09T16:24:52.391Z",
    "created_by_id": " IBMid-12345user",
    "last_modified_at": "2023-10-09T16:24:52.391Z",
    "last_modified_by_id": " IBMid-12345user",
    "counts": {
        "account": {
            "current": 785,
            "limit": 4020
        },
        "subject": {
            "current": 1,
            "limit": 1000
        }
    },
    "state": "active",
    "version": "v1.0"
}
```
{: codeblock}

### Terraform of an IAM policy with a condition
{: #fgac-new-policy-conditions-terraform}
{: terraform}

Example
{: http}

```sh
data "ibm_resource_group" "cos_group" {
      name = "Default"
}

resource "ibm_iam_user_policy" "example" {
 ibm_id = “user_email_id”
 roles = ["Writer"]
 resources {
  service = "cloud-object-storage"
  resource_type = "bucket"
  resource_instance_id = "cos instance guid"
  resource = "bucket-name"
}


rule_conditions {
 operator = "and"
 conditions {
  key = "{{resource.attributes.prefix}}"
  operator = "stringMatch"
  value = ["folder1/subfolder1/*"]
 }
 conditions {
  key = "{{resource.attributes.delimiter}}"
  operator = "stringEqualsAnyOf"
  value = ["/",""]
 }
 }
rule_conditions {
  key = "{{resource.attributes.path}}"
  operator = "stringMatch"
  value = ["folder1/subfolder1/*"]
 }
rule_conditions {
 operator = "and"
  conditions {
   key = "{{resource.attributes.delimiter}}"
   operator = "stringExists"
   value = ["false"]
  }
  conditions {
   key = "{{resource.attributes.prefix}}"
   operator = "stringExists"
   value = ["false"]
  }
  conditions {
   key = "{{resource.attributes.path}}"
   operator = "stringExists"
   value = ["false"]
  }
 }
rule_operator = "or"
 pattern = "attribute-based-condition:resource:literal-and-wildcard"
}
```
{: codeblock}

## Additional information
{: #fgac-additional-info}

For examples on how to use **Prefix/Delimiter**, or **Path** condition attributes, see the [tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-object-access-tutorial) on controlling access to individual objects in a bucket.
