---

copyright:
  years: 2023
lastupdated: "2023-10-04"

keywords: access control, iam, basics, objects

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Assigning access to objects within a bucket using IAM access conditions
{: #fgac-iam-access-conditions}

IAM access policies allow granting permissions in a COS bucket to specific groups of objects. This approach allows for fine-grained access control over data access, making it useful in scenarios where different parts of a bucket need to be accessed by different users or applications.
{: shortdesc}

If access is required to the entire bucket (i.e. when fine grained access control is not required) then follow the information on [Assigning access to an individual bucket](/docs/cloud-object-storage?topic=cloud-object-storage-iam-bucket-permissions&interface=ui).
{: important}

Each object stored in a COS bucket has a unique key, and these keys often follow a hierarchical structure similar to a file system. For example, an individual object with the key "folder1/subfolder1/file.txt" can simulate a folder or directory hierarchy, where the directory  "folder1" contains a sub-directory named "subfolder1" containing a file "file.txt". Access can be assigned at any folder level.

**Example**: An access policy can be created for all objects and subfolders in the folder named "folder1", or access can be assigned for just objects in the subdirectory named "subfolder1".

A policy administrator can assign access to individual objects and folders by configuring conditions when creating IAM access policies. The next section describes how to construct these types of policies

## Constructing a Fine-Grained Access Control Policy
{: #fgac-construct-policy}

The first step to granting access to individual objects within a bucket is to construct an IAM policy. You can find more information on constructing an IAM access policy for Cloud Object Storage in the COS tutorial [Limiting access to a single Object Storage bucket](/docs/cloud-object-storage?topic=cloud-object-storage-limit-access&interface=ui#single-bucket-create-policy). For more general information on building IAM policies, please go to [How IBM Cloud IAM works](/docs/account?topic=account-iamoverview). Let’s define some of the key concepts for an access policy.

## Terminology
{: #fgac-terminology}

Key Concepts: The following items are key components of building an IAM access policy for your Cloud Object Storage resources.

**Subject**: The subject of an access policy can be an individual user, an access group, a Service ID, or a Trusted Profile. See [What are IAM policies and who can assign them?](docs/account?topic=account-iamusermanpol) for more information on the types of subjects you can apply to a policy.

**Service**: The service is the IBM Cloud Service that contains the resource you are trying to assign access to. For assigning access to individual objects use the Cloud Object Storage service.

**Resource**: IBM COS supports the following resource targets: a resource group ID, a service instance, a resource type with value of “bucket”, and a resource ID (bucket name).

**Role**: IBM Cloud access roles are groups of actions. Access roles allow the subject to complete specific tasks within the context of the target resources that are defined in the policy. COS supports several pre-defined service roles that makes assigning permissions easier. COS also allows the creation of custom roles. See [Identity and Access Management roles](/docs/cloud-object-storage?topic=cloud-object-storage-iam&interface=ui#iam-roles) for more information on the supported roles for COS. Only the following roles are recommended for assigning individual object access:
- `Object Writer`
- `Object Deleter`
- `Object Lister`
- `Object Reader`
- `WriterNoConditions`

See table 1. for the list of COS roles and their interaction with conditions.

**Condition**: Once a resource is identified, a condition can be used to further scope access for a subject to individual objects in a bucket. This is referred to as fine-grained access control. Use a policy with no condition attributes to give full access to the target resource. A single IAM Policy can have more than one condition by using an OR or AND statement to combine the conditions. The condition statement (containing one or more conditions) should evaluate to TRUE for the user request to be permitted to perform the action. IAM Policy will deny any action that does not get evaluated to be TRUE/allowed by condition.

Use [IAM v2 policy](/apidocs/iam-policy-management#create-v2-policy) to construct IAM policy containing resource attribute-based conditions using API.
{: tip}

### COS supports the following attributes to specify conditions for assigning fine-grained access on COS resources:
{: #fgac-attributes-supported}

Prefix/Delimiter: Prefix and Delimiter are used together to scope all listing permissions for specific objects.
    If you want to provide listing access to all objects in the bucket, then do not use a Prefix and Delimiter condition.
    {: tip}

    The Prefix condition attribute defines the prefix for the set of object keys that this condition should allow for listing of objects or folders. For example, in the object named "folder1/subfolder1/file.txt", both "folder1/" and “folder1/subfolder1/” are possible prefixes. Using the prefix “folder1/” will grant list access to see the objects directly in “folder1” as well as the names of any possible subfolders directly in “folder1”.

    A Delimiter helps the user navigate the bucket as if it was a file hierarchy. Assigning a Delimiter condition statement restricts the type of folder structure the user can generate in the listing. In object named "folder1/subfolder1/file.txt", the delimiter “/” can be used to simulate a folder hierarchy where each folder is separated by a “/”. If a condition statement allows only a delimiter of “/”, then a list request with any other delimiter value is not permitted.

    Typically the prefix and delimiter are used together in a condition statement with an AND operator. It is possible to use a prefix without a delimiter in a condition statement. If the policy is configured with only a prefix and not a delimiter condition statement, the user can use any or no delimiter to list the objects.

    Examples of using Prefix and Delimiter Condition Statements:
    Consider the object named "folder1/subfolder1/file.txt":
    Prefix of "folder1/" AND no Delimiter
      - user can return a list of every objects that starts with folder1/ by doing a list request on folder1/ and not providing a delimiter
      - if user uses delimiter of "/" in the list request, they'd be restricted to only seeing the first level of objects and subfolders in folder1/
      - if user tries to list the subfolder (requests to list prefix = “folder1/subfolder1/”), access is denied

    Prefix of "folder1/" AND Delimiter of "/"
      - user can only list the objects and subfolders in the 1st level of folder1
      - user can only do list requests that specify delimiter of "/"
      - if user tries to list the contents of subfolder1, access is denied (user would need to have a condition allowing Prefix = “folder1/subfolder1/ for this) 

    The following APIs are subject to Prefix/Delimiter conditions:
   [GET Bucket (List Objects)](/docs/cloud-object-storage?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets)
   [GET Bucket Object Versions (List Object Versions)]()
   [List Multipart Uploads](/apidocs/cos/cos-compatibility?code=node#listmultipartuploads)

    To give a fine-grained user access to navigate to their folder in the UI, the user will need access to list the root folder of the bucket. See here for how to construct the policy to enable this.
    {: tip}

Path: Path is used to scope all read, write and management access on specific objects.
    If you want to provide such access to ALL objects in the bucket, do NOT specify a Path condition.
    {: tip}

    For an object named "folder1/subfolder1/file.txt", the full object key is the path. To restrict Read/Write/Management actions to this object, define a condition with Path of "folder1/subfolder1/file.txt".

All COS APIs that act directly on an object are subject to Path conditions. See [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) for the list of COS API actions that support Path.

Operators used with Condition Attributes: The full list of operators that can be used for prefix, delimiter, and path condition attributes can be found here. IAM policy supports the configuration of multiple values for an attribute by using `stringMatchAnyOf` and `stringEqualsAnyOf`.

Use of Wildcards: A condition attribute’s values can include a wildcard when the operator is `stringMatch` or `stringMatchAnyOf`. For information on the use of wildcards in a policy see [Assigning access by using wildcard policies](/docs/account?topic=account-wildcard).

Consider the object named "folder1/subfolder1/file.txt":

Path of “folder1/*”

    User will get Read/Write/Management access, as defined by the role, to all objects that start with “folder1/”



Prefix of "folder1/*" AND no Delimiter

- For a user list request with prefix set to “folder1/” and no Delimiter, the user request will return all objects that start with “folder1/”

- For a user list request with prefix set to “folder1/” and Delimiter of “/”, the request will return a view of the objects and folders just in the first level of folder1

- For a user list request with prefix set to “folder1/subfolder1/” and Delimiter of “/”, the request will return the objects (and any subfolders) in folder1/subfolder1



Prefix of "folder1/*" AND Delimiter of "/"

- For a user list request with prefix set to “folder1/” and Delimiter of “/”, the request will return a view of the objects and folders just in the first level of folder1

- For a user list request with prefix set to “folder1/subfolder1/” and Delimiter of “/”, the request will return the objects (and any subfolders) in folder1/subfolder1

- For a user list request with prefix set to “folder1/” and no Delimiter, the user request will not be permitted

| Access role         | Example actions                                              | Supported with Conditions                            |
|:--------------------|--------------------------------------------------------------|------------------------------------------------------|
| Manager             | Make objects public, create, and destroy buckets and objects | Not Recommended |
| Writer              | Create and destroy buckets and objects                       | Not Recommended |
| Reader              | List buckets, list objects, and download objects.            | Not Recommended |
| Content Reader      | List and download objects                                    | Not Recommended |
| Object Reader       | Download objects                                             | Yes |
| Object Writer       | Upload objects                                               | Yes |
| Object Deleter      | Upload objects                                               | Yes |
| WriterNoConditions  | Upload objects                                               | Yes |
{: caption="Table 1. Use of Conditions with COS Service Roles"}

See [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions) for the full list of Actions that each role supports.

**Actions that Don’t Support Conditions**: There are some COS APIs that do not support condition attributes. These include bucket level actions and other actions that don’t apply to one specific object. To see a list of these actions, go to [Identity and Access Management actions](/docs/cloud-object-storage?topic=cloud-object-storage-iam#iam-actions). A condition statement in a policy will apply to all the actions defined by the role. When a condition is configured in a policy with a role that contains actions that do not support conditions, the sub-set of actions that do not support conditions will be denied. Manager, Writer, Reader and Content Reader are examples of roles that contain a combination of actions that support conditions and actions that do not support conditions, these roles are not recommended for use with fine-grained access control for both object listing and object management (`read/write/configuration`).

`WriterNoConditions` is a new IAM role created to be used for certain use cases when assigning access to individual objects. This service role contains actions that do not support condition attributes. If you want to give a user permission to perform actions that do not support conditions while providing fine-grained access control, then you will need to create two separate IAM policies:
- one that grants the subject “WriterNoConditions” role
- and another policy that contains the role and condition for fine-grained access

**Example**: If a user should be allowed to use HEAD bucket to determine that the bucket exists as well as write objects to folder1/subfolder1/in the bucket,then two policies are required: one with `WriterNoConditions` role with no conditions specified, and a second policy with `ObjectWriter` role where the condition contains the path set to folder1/subfolder1.

It is recommended that you define both a Prefix/Delimiter condition and a Path condition when granting Read/Write AND LIST actions to a user in the same policy. A condition specifying Prefix/Delimiter and a condition specifying Path should be logically ORed in the IAM Policy statement to permit both types of operations (Read/Write/Management of objects OR LIST objects). Failure to include either condition statement will result in the subject having Read/Write/Management OR LIST actions on all objects in the resource, not just on the target path or prefix.
{: note}

If a COS custom role supports actions that both support and do not support conditions, and a condition is assigned in the policy, all those actions will not be permitted.
{: note}

All IAM policies with conditions are subject to the IAM policy limits. See [IBM Cloud IAM limits](https://cloud.ibm.com/docs/account?topic=account-known-issues#iam_limits) for more info on the IAM policy limits .

COS does not support CBR rules that only apply to a specific prefix/delimiter or path.

## Create a new policy for a user with Conditions<!--needs updating with conditions-->
{: #fgac-new-policy-conditions}

These examples provide list access to the full object hierarchy within folder named "folder1/subfolder1" and provide object `read/write/delete` access to all objects in folder named "subfolder1".

### UI
{: #fgac-new-policy-conditions-ui}

<!--Update this section after IAM has released UI feature for condition building.--> Use "folder1/subfolder1/file.txt" as example.
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

### CLI<!--needs updating with conditions-->
{: #fgac-new-policy-conditions-cli}

Create an access policy for the specified user in the current account [ibmcloud iam user-policy-create](/docs/cli?topic=cli-ibmcloud_commands_iam#ibmcloud_iam_user_policy_create).

Include an example construction using the CLI of an IAM policy with a condition. Use "folder1/subfolder1/file.txt" as example
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

### API
{: #fgac-new-policy-conditions-api}

Include an example construction using the API of an IAM policy with a condition. Use "folder1/subfolder1/file.txt" as example.
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}


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
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectWriter"
        },
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectReader"
        },
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectLister"
        },
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectDeleter"
        }

      ]
    }
  },
  "rule": {
    "operator": "or",
    "conditions": [
      {
        "key": "{{resource.attributes.path}}",
        "operator": "stringMatch",
        "value": "folder1/subfolder1/*"
      },
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
      }
    ]
  },
  "pattern": "attribute-based-condition:resource:literal-and-wildcard"
}'
```

Response
```sh
{
    "id": "25f6fd1b-44d0-4922-b185-14fee771d3ee",
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
                "key": "{{resource.attributes.path}}",
                "operator": "stringMatch",
                "value": "folder1/subfolder1/*"
            },
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
            }
        ]
    },
    "control": {
        "grant": {
            "roles": [
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectWriter"
                },
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectReader"
                },
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectLister"
                },
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectDeleter"
                }
            ]
        }
    },
    "href": "https://iam.test.cloud.ibm.com/v2/policies/25f6fd1b-44d0-4922-b185-14fee771d3ee",
    "created_at": "2023-09-01T18:52:09.209Z",
    "created_by_id": "IBMid-1234user",
    "last_modified_at": "2023-09-01T18:52:09.209Z",
    "last_modified_by_id": "IBMid-1234user",
    "counts": {
        "account": {
            "current": 776,
            "limit": 4020
        },
        "subject": {
            "current": 6,
            "limit": 1000
        }
    },
    "state": "active",
    "version": "v1.0"
}
```
### Terraform<!--needs updating with conditions-->
{: #fgac-new-policy-conditions-terraform}

Include an example construction using the Terraform of an IAM policy with a condition. Use "folder1/subfolder1/file.txt" as example.
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

## Create a new policy for a ServiceID with Conditions<!--needs updating with conditions-->
{: #fgac-new-policy-serviceid-conditions}

Create an access policy and assign it to a [service ID](/docs/cli?topic=cli-ibmcloud_commands_iam#ibmcloud_iam_service_policy_create).

### UI
{: #fgac-new-policy-serviceid-conditions-ui}

<!--Update this section after IAM has released UI feature for condition building.--> Use "folder1/subfolder1/file.txt" as example.
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

### CLI<!--needs updating with conditions-->
{: #fgac-new-policy-serviceid-conditions-cli}

Include an example construction using the CLI of an IAM policy with a condition. Use "folder1/subfolder1/file.txt" as example
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

### API<!--needs updating with conditions-->
{: #fgac-new-policy-serviceid-conditions-api}

Include an example construction using the API of an IAM policy with a condition. Use "folder1/subfolder1/file.txt" as example
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

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
        "value": "iam-ServiceId-123453service"
      }
    ]
  },
  "control": {
    "grant": {
      "roles": [
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectWriter"
        },
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectReader"
        },
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectLister"
        },
        {
          "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectDeleter"
        }

      ]
    }
  },
  "rule": {
    "operator": "or",
    "conditions": [
      {
        "key": "{{resource.attributes.path}}",
        "operator": "stringMatch",
        "value": "folder1/subfolder1/*"
      },
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
      }
    ]
  },
  "pattern": "attribute-based-condition:resource:literal-and-wildcard"
}'
```

Response
```sh
{
    "id": "25f6fd1b-44d0-4922-b185-14fee771d3ee",
    "description": "access control for RESOURCE_NAME",
    "type": "access",
    "subject": {
        "attributes": [
            {
                "key": "iam_id",
                "operator": "stringEquals",
                "value": "iam-ServiceId-123453service"
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
                "key": "{{resource.attributes.path}}",
                "operator": "stringMatch",
                "value": "folder1/subfolder1/*"
            },
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
            }
        ]
    },
    "control": {
        "grant": {
            "roles": [
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectWriter"
                },
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectReader"
                },
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectLister"
                },
                {
                    "role_id": "crn:v1:bluemix:public:cloud-object-storage::::serviceRole:ObjectDeleter"
                }
            ]
        }
    },
    "href": "https://iam.test.cloud.ibm.com/v2/policies/25f6fd1b-44d0-4922-b185-14fee771d3ee",
    "created_at": "2023-09-01T18:52:09.209Z",
    "created_by_id": "IBMid-1234user",
    "last_modified_at": "2023-09-01T18:52:09.209Z",
    "last_modified_by_id": "IBMid-1234user",
    "counts": {
        "account": {
            "current": 776,
            "limit": 4020
        },
        "subject": {
            "current": 6,
            "limit": 1000
        }
    },
    "state": "active",
    "version": "v1.0"
}
```
### Terraform<!--needs updating with conditions-->
{: #fgac-new-policy-serviceid-conditions-terraform}

Include an example construction using the Terraform of an IAM policy with a condition. Use "folder1/subfolder1/file.txt" as example.
Roles: `Object Lister`, `Object Writer`, `Object Deleter`, `Object Reader`
Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”}
OR
{Path StringMatch “folder1/subfolder1/*”}

**Example of creation of FGAC Policy with Conditions: {Prefix StringMatch “folder1/subfolder1/*” AND Delimiter StringMatchAnyOf  “/”, “”} for Reader role:**

```sh
data "ibm_resource_group" "cos_group" {
name = "Default"
}

resource "ibm_iam_user_policy" "example" {
ibm_id = “user_email_id”
roles = ["Reader"]
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

**Example of creation of FGAC Policy with Conditions: {Path StringMatch “folder1/subfolder1/*”} for Reader role:**

```sh
data "ibm_resource_group" "cos_group" {
name = "Default"
}

resource "ibm_iam_user_policy" "example" {
ibm_id = “ibm_id”
roles = ["Reader"]
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

Commands to create IAM policy using TF:
* terraform init
* terraform apply

Commands to update IAM policy using TF:
* terraform apply

Commands to read IAM policy using TF:
* terraform import ibm_iam_user_policy.example user_email_id/policy_id

Commands to delete IAM policy using TF:
* terraform destroy



## Additional information
{: #fgac-additional-info}

For additional examples of how to use prefix, delimiter, and path condition attributes, see the [tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-cos-tutorial-fgac) on using Fine Grain Access Control.


