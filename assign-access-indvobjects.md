---

copyright:
  years: 2023
lastupdated: "2023-10-18"

keywords: IAM, policy, fine-grained access control

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Assigning access to objects within a bucket using IAM access conditions
{: #fgac-iam-access-conditions}

IAM access policies allow granting permissions in a COS bucket to specific groups of objects. This approach allows for fine-grained access control over data access, making it useful in scenarios where different parts of a bucket need to be accessed by different users or applications.
{: shortdesc}

If access is required to the entire bucket (i.e. when fine-grained access control is not required) then follow the information on [Assigning access to an individual bucket](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions&interface=ui).
{: important}

Each object stored in a COS bucket has a unique key, and these keys often follow a hierarchical structure similar to a file system. For example, an individual object with the key *"folder1/subfolder1/file.txt"* can simulate a folder or directory hierarchy, where the directory  *"folder1"* contains a sub-directory named *"subfolder1"* containing a file *"file.txt"*. Access can be assigned at any folder level.

**Example**: An access policy can be created for all objects and subfolders in the folder named *"folder1"*, or access can be assigned for just objects in the subdirectory named *"subfolder1"*.

A policy administrator can assign access to individual objects and folders by configuring conditions when creating IAM access policies. The next section describes how to construct these types of policies

## Constructing a fine-grained access control policy
{: #fgac-construct-policy}

The first step to granting access to individual objects within a bucket is to construct an IAM policy. You can find more information on constructing an IAM access policy for {{site.data.keyword.cos_short}} in the COS tutorial [Limiting access to a single Object Storage bucket](/docs/cloud-object-storage?topic=cloud-object-storage-limit-access&interface=ui#single-bucket-create-policy). For more general information on building IAM policies, please go to [How IBM Cloud IAM works](/docs/account?topic=account-iamoverview). Let’s define some of the key concepts for an access policy.

### Key Concepts
{: #fgac-key-concepts}

The following items are key components of building an IAM access policy for your {{site.data.keyword.cos_short}} resources.

#### Subject
{: #fgac-key-concepts-subjects}

The subject of an access policy can be an individual user, an access group, a Service ID, or a Trusted Profile. See [What are IAM policies and who can assign them?](docs/account?topic=account-iamusermanpol) for more information on the types of subjects you can apply to a policy.

#### Service
{: #fgac-key-concepts-service}

The service is the IBM Cloud Service that contains the resource you are trying to assign access to. For assigning access to individual objects use the {{site.data.keyword.cos_short}} service.

#### Resource
{: #fgac-key-concepts-resource}

{{site.data.keyword.cos_full}} supports the following resource targets:
- Resource group ID
- Service instance
- Resource type with value of *“bucket”*
- Resource ID (bucket name)

#### Role
{: #fgac-key-concepts-role}

IBM Cloud access roles are groups of actions. Access roles allow the subject to complete specific tasks within the context of the target resources that are defined in the policy. COS supports several pre-defined service roles that makes assigning permissions easier. COS also allows the creation of custom roles. See [Identity and Access Management roles](/docs/cloud-object-storage?topic=cloud-object-storage-iam&interface=ui#iam-roles) for more information on the supported roles for COS.

See the [table below](#fgac-conditions-service-roles) for the list of COS roles and their interaction with conditions.

#### Condition
{: #fgac-key-concepts-condition}

Once a resource is identified, a condition can be used to further scope access for a subject to individual objects in a bucket. This is referred to as fine-grained access control.


A single IAM Policy can have more than one condition by using an `OR` or `AND` statement to combine the conditions. The condition statement (containing one or more conditions) should evaluate to `TRUE` for the user request to be permitted to perform the action. IAM Policy will deny any action that does not get evaluated to be `TRUE`.  The policy statement should contain all condition11 attributes required by the role. If the policy statement does not contain all condition attributes12 required by the role, the actions subject to the omitted condition attributes will be denied.

Use a policy with no condition attributes to give full access, as defined by the role, to the target resource.
{: important}

Use [IAM v2 policy](/apidocs/iam-policy-management#create-v2-policy) to construct IAM policy with attribute-based conditions.
{: tip}

## Using conditions in an IAM policy
{: #fgac-conditions}

### COS supports the following attributes to specify conditions for assigning fine-grained access on COS resources:
{: #fgac-attributes-supported}

#### Prefix and Delimiter
{: #fgac-attributes-prefix-delimeter}

**Prefix** and **Delimeter** are used together to scope all listing permissions to specific objects in a bucket.

The **Prefix** condition attribute defines the prefix for the set of object keys that this condition should allow for listing of objects or folders. For example, in the object named *"folder1/subfolder1/file.txt"*, both *"folder1/"* and *“folder1/subfolder1/”* are possible prefixes.

A **Delimiter** helps the user navigate the bucket as if it was a file hierarchy. Assigning a **Delimiter** condition statement restricts the type of folder structure the user can generate in the listing. In object named *"folder1/subfolder1/file.txt"*, the delimiter *“/”* can be used to simulate a folder hierarchy where each folder is separated by a *“/”*. If a condition statement allows only a delimiter of *“/”*, then a list request with any other delimiter value is not permitted.

Typically the prefix and delimiter are used together in a condition statement with an `AND` operator. It is possible to use a prefix without a delimiter in a condition statement. If the policy is configured with only a prefix and not a delimiter condition statement, the user can use any or no delimiter to list the objects.

**Examples of using Prefix and Delimiter Condition Statements**
(Consider the object named *"folder1/subfolder1/file.txt"*):
**Prefix** of *"folder1/"* `AND` no **Delimiter**
   - User can return a list of every objects that starts with folder1/ by doing a list request on folder1/ and not providing a delimiter
   - If user uses delimiter of *"/"* in the list request, they'd be restricted to only seeing the first level of objects and subfolders in folder1/
   - If user tries to list the subfolder (requests to list prefix = *“folder1/subfolder1/”*), access is denied

Prefix of *"folder1/"* `AND` Delimiter of *"/"*
   - User can only list the objects and subfolders in the first level of folder1
   - User can only do list requests that specify delimiter of *"/"*
   - If user tries to list the contents of subfolder1, access is denied (user would need to have a condition of allowing Prefix = *“folder1/subfolder1/"* to allow listing the contents of subfolder1)

The following APIs are subject to **Prefix/Delimiter** conditions:
- [GET Bucket (List Objects)](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets)
- GET Bucket Object Versions (List Object Versions)
- [List Multipart Uploads](/apidocs/cos/cos-compatibility?code=node#listmultipartuploads)

To see the full list of actions and the supported condition attributes, see Identity and [Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions).
{: tip}

To give a fine-grained user access to navigate to their folder in the UI, the user will need access to list the root folder of the bucket. See here for how to construct the policy to enable this.
{: tip}

#### Path
{: #fgac-attributes-path}

Path is used to scope all read, write and management access on specific objects.

For an object named *"folder1/subfolder1/file.txt"*, the full object key is the path. To restrict `Read/Write/Management` actions to this object, define a condition with Path of *"folder1/subfolder1/file.txt"*.

All COS APIs that act directly on an object are subject to Path conditions. See [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) for the list of COS API actions that support Path.

It is recommended that you define both a Prefix/Delimiter condition and a Path condition when granting `Read/Write` `AND` `List` actions to a user in the same policy. `Manager`, `Writer`, `Reader`, and `Content Reader` are examples of roles where it is recommended to define both a Prefix/Delimiter and Path condition. A condition specifying Prefix/Delimiter and a condition specifying Path should be logically `ORed` in the IAM Policy statement to permit both types of operations (`Read/Write/Management` of objects `OR` `List` objects)
{: note}

Operators used with Condition Attributes: There are several operators that can be used when defining condition attributes. The full list of operators that can be used for **Prefix**, **Delimiter** and **Path** condition attributes can be found in [IAM condition properties](/docs/account?topic=account-iam-condition-properties&interface=ui).

Use of Wildcards: A condition attribute’s values can include a wildcard when the operator is `stringMatch` or `stringMatchAnyOf`.

**Examples of using Wildcards in Condition Statements:**

Consider the object named *"folder1/subfolder1/file.txt"*:
Path of *“folder1/*”*
- User will get Read/Write/Management access, as defined by the role, to all objects that start with *“folder1/”*

Prefix of *"folder1/*"* AND no Delimiter
- For an object list request with prefix set to *“folder1/”* and no Delimiter, the user request will return all objects that start with *“folder1/”*
- For an object list request with prefix set to *“folder1/”* and Delimiter of *“/”*, the request will return a view of the objects and folders just in the first level of folder1
- For an object list request with prefix set to “folder1/subfolder1/” and Delimiter of *“/”*, the request will return a view of the objects and folders just in the first level of folder1/subfolder1

Prefix of *"folder1/*"* AND Delimiter of *"/"*
- For an object list request with prefix set to *“folder1/”* and Delimiter of *“/”*, the request will return a view of the objects and folders just in the first level of folder1
- For an object list request with prefix set to “folder1/subfolder1/” and Delimiter of *“/”*, the request will return the objects (and any subfolders) in folder1/subfolder1
- For an object list request with prefix set to *“folder1/”* and no Delimiter, the request will not be permitted since a delimiter of *“/”* must be used in the list request for this policy to evaluate to true.

### Actions that Don’t Support Conditions
{: #fgac-conditions-actions-not-supported}

There are some COS APIs that do not specify a path or prefix/delimiter in the request. The COS Service roles: `Manager`, `Writer`, `Reader` and `Content Reader` contain some
actions that do not support conditions. This also applies to custom roles. To allow these actions when using a **Prefix/Delimiter** or **Path** condition, the following condition statement is needed in the IAM policy:

```sh
((path stringExists = true) AND (prefix stringExists = true) AND (delimiter stringExists21= true))"
```

See the [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) table for the full list of API actions that do not support **Path**, **Prefix**, or **Delimiter** conditions and require the statement above when using fine-grained access.

Refer to the example for using this clause in an IAM policy.

## Use of Conditions with COS Service Roles
{: #fgac-conditions-service-roles}

| Access role         | Description of actions                                              | Supported Condition Attributes                            |
|:--------------------|--------------------------------------------------------------|------------------------------------------------------|
| Manager             | Make objects public, create, and destroy buckets and objects | Note 1 |
| Writer              | Create and destroy buckets and objects                       | Note 1 |
| Reader              | List buckets, list objects, and download objects.            | Note 1 |
| Content Reader      | List and download objects                                    | Note 1 |
| Object Reader       | Download objects                                             | Path |
| Object Writer       | Upload objects                                               | Path |
| Note 1: These roles support Prefix/Delimiter and Path condition attributes. The roles also include actions that do not support condition attributes. Use the `StringExists` clause in the condition statement to allow these actions. |
{: caption="Table 1. Use of Conditions with COS Service Roles"}

See [Cloud Object Storage](/docs/account?topic=account-iam-service-roles-actions&interface=ui#cloud-object-storage-roles) for the full list of actions for each COS service role and the list of condition attributes supported by each action.

## Create a new policy for a User with Conditions
{: #fgac-new-policy-conditions}

The following examples provide a user with the `“Writer”` COS Service Role with the ability to:

1. List access to the full object hierarchy within folder named *"folder1/subfolder1"*.
2. `Read/Write/Delete` access to all objects in folder named *"subfolder1"*.
3. Perform bucket configuration management such as `HEAD Bucket` and `GET/PUT Bucket Versioning`.


### UI
{: #fgac-new-policy-conditions-ui}

<!--Update this section after IAM has released UI feature for condition building.--> Use *"folder1/subfolder1/file.txt"* as example.
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch *“folder1/subfolder1/*”* AND Delimiter `stringMatchAnyOf`  *“/”*, *“”*}
OR
{Path StringMatch *“folder1/subfolder1/*”*}

### CLI of an IAM policy with a condition
{: #fgac-new-policy-conditions-cli}

Create user policy over CLI general reference [link](/docs/cli?topic=cli-ibmcloud_commands_iam).

Example using *"folder1/subfolder1/file.txt"*.

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
```

Commands to **create** a IAM Policy using ibmcloudcli :
* `ibmcloud iam  user-policy-create user_email_id --file policy.json --api-version v2`

Commands to **update** a IAM Policy using ibmcloudcli :
* `ibmcloud iam user-policy-update user_email_id  policy_id  --file policy.json --api-version v2`

Commands to **list** a IAM Policy using ibmcloudcli :
* `ibmcloud iam user-policy  user_email_id  policy_id  --output json --api-version v2`

Commands to **delete** a IAM Policy using ibmcloudcli :
* `ibmcloud iam user-policy-delete  user_email_id  policy_id  policy_id --api-version v2`

If --api-version v2 is not provided the commands will return with error saying the policy does not exists.
{: note}

### API of an IAM policy with a condition
{: #fgac-new-policy-conditions-api}

Example using *"folder1/subfolder1/file.txt"*.

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

### Terraform of an IAM policy with a condition
{: #fgac-new-policy-conditions-terraform}

**Example**
Creation of FGAC Policy with Conditions: {Prefix StringMatch *“folder1/subfolder1/*”* AND Delimiter StringMatchAnyOf  *“/”*, *“”*} for `Writer` role:

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
 key = "{{resource.attributes.prefix}}"
 operator = "stringMatchAnyOf"
 value = ["folder1/subfolder1/*"]
}
rule_conditions {
 key = "{{resource.attributes.delimiter}}"
 operator = "stringMatchAnyOf"
 value = ["/", “"”]
}
rule_operator = "and"
 pattern = "attribute-based-condition:resource:literal-and-wildcard"
}
```

**Example**
Creation of FGAC Policy with Conditions: {Path StringMatch *“folder1/subfolder1/*”*} for `Writer` role:

```sh
data "ibm_resource_group" "cos_group" {
name = "Default"
}

resource "ibm_iam_user_policy" "example" {
  ibm_id = “ibm_id”
  roles = ["Writer"]
  resources {
   service = "cloud-object-storage"
   resource_type = "bucket"
   resource_instance_id = "cos instance guid"
   resource = "bucket-name"
 }


rule_conditions {
  key = "{{resource.attributes.path}}"
  operator = "stringMatch"
  value = ["folder1/subfolder1/*"]
 }
rule_conditions {
  key = "{{resource.attributes.prefix}}"
  operator = "stringMatch"
  value = “”
 }
rule_operator = "or"
  pattern = "attribute-based-condition:resource:literal-and-wildcard"
}
```


## Additional information
{: #fgac-additional-info}

For additional examples of how to use **Prefix**, **Delimiter**, and **Path** condition attributes, see the [tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-cos-tutorial-fgac) on using fine-grained access control.


