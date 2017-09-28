---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Using the command line

This tutorial is a more in-depth walkthrough of the steps performed in the Getting Started material. It uses the command line tools `bx` and `curl` to create an instance of COS and perform data operations.

## Gather required information

### Install Bluemix CLI Client

Ensure you have install Bluemix CLI Client. Please follow direction to install Bluemix CLI [here](https://console.bluemix.net/docs/cli/reference/bluemix_cli/all_versions.html#bluemix-cli-installer-downloads), and you can read more about using the CLI [in the documentation](https://console.bluemix.net/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_cli).

### Login into Bluemix

First we need to actually login to the cloud platform, either by providing an email and password or by using an API key.

```
$ bx login

API endpoint: https://api.bluemix.net

Email> queen-bee@delightfulhoney.com

Password>
Authenticating...
OK

Select an account (or press enter to skip):
1. User A's Account (b09edf5642ebfad587c594f4d4a354b0)

Enter a number> 1
Targeted account User A's Account (b09edf5642ebfad587c594f4d4a354b0)

API endpoint:   https://api.stage1.ng.bluemix.net (API version: 2.75.0)
Region:         us-south
User:           userA@gmail.com
Account:        User A's Account (b09edf5642ebfad587c594f4d4a354b0)
No org or space targeted, use 'bx target --cf or bx target -o ORG -s SPACE'

Tip: use 'bx cf <command>' to run the Cloud Foundry CLI with Bluemix CLI context.


```

Capture the **Bluemix Account ID** from the output **9b09edf5642ebfad587c594f4d4a354b0.**

### Retrieve Access / IAM token

Each API request requires an HTTP header that includes the 'Authorization’ parameter. The value for the header is the access token - The JSON web token \(JWT\) that you receive when logging into the Bluemix platform. It allows you to use the IBM Bluemix REST API to manage your cloud services and resources.

```
$bx iam oauth-tokens
IAM token:  Bearer essdssdsdsdss...sdssds
UAA token:  Bearer ey....hYzsdssdsdsdsds
```

to retrieve your access token information.

More information can be found in the [IAM documentation](https://console.bluemix.net/docs/iam/iamuserinv.html\#iamuserinv).


### Get the GUID for your organization

```
$ bx iam orgs --guid
Getting orgs in region 'us-south' as bluemix_ui_integration_test_1@mailinator.com...
Retrieving current account...
OK

GUID                                   Name                  Region     Account owner        Account ID                         Status
7ade3eaa-656a-4206-92c3-5e45708f8df7   RC_integration_test   us-south   auser@gmail.com   9e16d1fed8aa7e1bd73e7a9d23434a5a   active


```

## Create a resource instance

Cloud Object Storage is a cloud _service_.  When you provision an instance of a service, you create a _resource instance_.  You can have multiple resource instances of COS in the same account.

### Obtain IAM Token for Administrator (Owner)

You will need to provide values for three request parameters:

**Request parameters:**

`username` : Bluemix user name.

`password` : Bluemix password.

`bss_account` : Bluemix account uuid. You can get his via bluemix CLI. In this use case its b09edf5642ebfad587c594f4d4a354b0

**Request:**

```
curl -X POST \
  'https://iam.bluemix.net/oidc/token?grant_type=password&username=15900echohills%40gmail.com&response_type=cloud_iam&password=<your_bm_password>&bss_account=b09edf5642ebfad587c594f4d4a354b0' \
  -H 'cache-control: no-cache'
```

**Response:**

```
{
    "access_token": "eyJpYW1faWQiOiJJQk1pZC0zMTAwMDJLSDNDIiwiaWQiOiJJQk1pZC0zMTAwMDJLSDNDIiwicmVhbG1pZCI6IklCTWlkIiwiaWRlbnRpZmllciI6IjMxMDAwMktIM0MiLCJnaXZlbl9uYW1lIjoiRGhhcm1lc2giLCJmYW1pbHlfbmFtZSI6IkJoYWt0YSIsIm5hbWUiOiJEaGFybWVzaCBCaGFrdGEiLCJlbWFpbCI6IjE1OTAwZWNob2hpbGxzQGdtYWlsLmNvbSIsInN1YiI6IjE1OTAwZWNob2hpbGxzQGdtYWlsLmNvbSIsImFjY291bnQiOnsiYnNzIjoiYjA5ZWRmNTY0MmViZmFkNTg3YzU5NGY0ZDRhMzU0YjAifSwiaWF0IjoxNTAzNTA0MzUyLCJleHAiOjE1MDM1MDc5NTIsImlzcyI6Imh0dHBzOi8vaWFtLnN0YWdlMS5uZy5ibHVlbWl4Lm5ldC9vaWRjL3Rva2VuIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwic2NvcGUiOiJvcGVuaWQiLCJjbGllbnRfaWQiOiJieCJ9.HaMUKbJ0fpPaMN9yVDZ-l52gkc7BFwEfGnP4qofohd6_Tm4Hs3DKycDmc9C38KYAMe_adCFSOBr49nIn9gFIPD4fZNNn77XhcuRTRTBWTQqAjiZjt5JI6MTuVHN0jmFhAw-UFarfl0J1uXqvN6r28dlewmas8tMHulFzTXvVENZ4Z5oUCGNLABLuB-hcxUO8XFWdHZLU4UsCrF2-6LiDo_odK41cg9eDQOTdttdmhVJ7-XhLBgDm1ClYCPVnImTxDLCDrWNaDOLTtJhElfSteFPbYrPFRpmaQhr_YUrf0F4tBcMr4MqqQnwlY64nEuwoc_YTHHMYOEuOrTM2ioGYrw",
    "refresh_token": "aFOfuao19XuRYLPiQXDOakOr4UryoGm0lfy4I5KrNDuXj6SI3qVQQ0G3o5yuY0o3EINVtnkOEKTrtecGCUok2XaJprmPpDrsEA5iDudwrYnDeoHR3rccVF4o9_rmO07Yt8ZH-1tBYEh22DRM_lUYtx0qAB8CDwK763W-XWBMl_sI26CBm-USF5Mbt3iq5JDVnl738tC4WyBLtl21HSE8cRByWYqE1b9NVJ7Qm_rHctW_C_hMW2t1EEYiHiwYGsE5yA-ay7stkCVGWm1V9ZvU1QinltX6gIj0vaOLpbOdEacFTTB3-9WPMxgktK26c-EZ7eUM_KDKTEQI2HBZXQTPC6as8-CgSCe4TZp8WUIoghek4LiS79c2GshPVELDBynER_34QkgXFUUhEeDXjR9l9tOtt6m-b4JYQU6qSxjjvacnSbggynEBUyHuYji0uhwlNQgKWlmeyDyLlrRslIqt24B0RPWvVsL9bL-cCzflfCo_0vLMFozIvsDzJc9U6mdhCodwlJG8ahldQZb-WfTImFl-",
    "token_type": "Bearer",
    "expires_in": 3600,
    "expiration": 1503507952
}
```

You will need to use the `access_token` for subsquent calls as `Authorization` header for the REST calls. You will want to store it as environment variable so it can be levereraged for other calls.

## Introspect `access_token`_ for _`iam_id`

The iam\_id will be required when listing policies for the user.

**Request parameters:**

`token` : The access\_token.

**Request:**

```
curl -X POST \
  https://iam.bluemix.net/oidc/introspect \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d token=<access_token>
```

**Response:**

```
{
    "active": true,
    "iss": "https://iam.bluemix.net/oidc/token",
    "realmId": "IBMid",
    "identifier": "310002KH3C",
    "sub": "15900echohills@gmail.com",
    "account": {
        "bss": "b09edf5642ebfad587c594f4d4a354b0"
    },
    "scope": "openid",
    "client_id": "bx",
    "iat": 1503504352,
    "exp": 1503507952,
    "grant_type": "password",
    "given_name": "Dharmesh",
    "family_name": "Bhakta",
    "name": "Dharmesh Bhakta",
    "email": "15900echohills@gmail.com",
    "iam_id": "IBMid-310002KH3C"
}
```

For the user above,  the `iam_id` is `IBMid-310002KH3C`

### Creating resource group for your organization

_Resource instances_ need to be a part of a _resource group_.  Use the following API to create a resource group for your organization.

**Request parameters:**

`access_token` : The access\_token.

`account_id` This is your bluemix account UUID.

`resource_id` This is your organization GUID for which you need to create a resource group.

`name` This is the name you want to give to this resource group.

<!--
`quota_id` This is the quota ID. There is an API to retrieve this for your organization. But for now, use the same.
-->

Request:

```
curl -X POST \
  https://resource-manager.bluemix.net/v1/resource_groups \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ae69cebb-b1de-5652-335e-6e5d87bd0e7f' \
  -d '{
    "account_id": "b09edf5642ebfad587c594f4d4a354b0",
    "linkages":
      [ { "resource_id": "69e6e996-bebe-4e24-9aed-8efb448ecff7",
          "resource_origin": "CF_ORG" } ],
     "name": "15900echohills-resource-group",
     "quota_id": "7ce89f4a-4381-4600-b814-3cd9a4f4bdf4"
}'
```

Response:

```
{
    "resources": [
        {
            "id": "74eb3cc2f3194e579f6666416663d2f0",
            "account_id": "b09edf5642ebfad587c594f4d4a354b0",
            "name": "15900echohills-resource-group",
            "state": "ACTIVE",
            "default": false,
            "quota_id": "7ce89f4a-4381-4600-b814-3cd9a4f4bdf4",
            "quota_url": "/v1/quota_definitions/7ce89f4a-4381-4600-b814-3cd9a4f4bdf4",
            "payment_methods_url": "/v1/resource_groups/74eb3cc2f3194e579f6666416663d2f0/payment_methods",
            "resource_linkages": [
                {
                    "resource_id": "69e6e996-bebe-4e24-9aed-8efb448ecff7",
                    "resource_origin": "CF_ORG",
                    "created_at": "2017-08-22T05:58:53.758Z",
                    "updated_at": "2017-08-22T05:58:53.758Z"
                }
            ],
            "teams_url": "/v1/resource_groups/74eb3cc2f3194e579f6666416663d2f0/teams",
            "created_at": "2017-08-22T05:58:53.644Z",
            "updated_at": "2017-08-22T05:58:53.644Z"
        }
    ]
}
```

From the response, you want to store the `id` . This is your resource group ID which will be required when provisioning service instances.  Resource group id is `74eb3cc2f3194e579f6666416663d2f0`.

## Retrieving resource group for your organization

**Request parameters:**

`access_token` : The access\_token.

`resource_id` : This you CF organization GUID for which you would like to retrieve resource group.

**Request:**

```
curl -X GET \
  'https://resource-manager.bluemix.net/v1/resource_groups?resource_id=69e6e996-bebe-4e24-9aed-8efb448ecff7&resource_origin=CF_ORG' \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
```

Response:

```
{
    "resources": [
        {
            "id": "74eb3cc2f3194e579f6666416663d2f0",
            "account_id": "b09edf5642ebfad587c594f4d4a354b0",
            "name": "15900echohills-resource-group",
            "state": "ACTIVE",
            "default": false,
            "quota_id": "7ce89f4a-4381-4600-b814-3cd9a4f4bdf4",
            "quota_url": "/v1/quota_definitions/7ce89f4a-4381-4600-b814-3cd9a4f4bdf4",
            "payment_methods_url": "/v1/resource_groups/74eb3cc2f3194e579f6666416663d2f0/payment_methods",
            "resource_linkages": [
                {
                    "resource_id": "69e6e996-bebe-4e24-9aed-8efb448ecff7",
                    "resource_origin": "CF_ORG",
                    "created_at": "2017-08-22T05:58:53.758Z",
                    "updated_at": "2017-08-22T05:58:53.758Z"
                }
            ],
            "teams_url": "/v1/resource_groups/74eb3cc2f3194e579f6666416663d2f0/teams",
            "created_at": "2017-08-22T05:58:53.644Z",
            "updated_at": "2017-08-22T05:58:53.644Z"
        }
    ]
}
```

The `id` attribute in the response is the `resource_group_id`

Now we have everything we need to be able to create a Cloud Object Storage service instance using the Resource controller API.

### Create COS Service instance

**Request parameters:**

`access_token` : The access\_token.

`name` : The name of the service instance you would like to provide.

`region_id` : The value for COS is `globalgeo`.

`resource_plan_id` : The service plan - The id below is for the premium plan.

`resource_group_id` : The resource group id where you like to create thh service instance. You either created a new one or retrieve an existing one using the api above.

**Request:**

```
curl -X POST \
  https://resource-controller-athena.stage1.ng.bluemix.net/v1/resource_instances/ \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 86aa29aa-8f42-9831-e527-e4b6ad93314c' \
  -d '{
  "name": "RC-TEST-INSTANCE-1503509382",
  "region_id": "globalgeo",
  "resource_plan_id": "744bfc56-d12c-4866-88d5-dac9139e0e5d",
  "resource_group_id": "74eb3cc2f3194e579f6666416663d2f0"
}
```

**Response:**

```
{
    "id": "crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
    "guid": "d61bf83f-d583-40d0-9877-7b2df60d8b1f",
    "url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A",
    "created_at": "2017-08-23T17:29:43.643023016Z",
    "updated_at": null,
    "deleted_at": null,
    "name": "RC-TEST-INSTANCE-1503509382",
    "region_id": "globalgeo",
    "account_id": "b09edf5642ebfad587c594f4d4a354b0",
    "resource_plan_id": "744bfc56-d12c-4866-88d5-dac9139e0e5d",
    "resource_group_id": "74eb3cc2f3194e579f6666416663d2f0",
    "create_time": 0,
    "crn": "crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
    "state": "inactive",
    "type": "service_instance",
    "resource_id": "dff97f5c-bc5e-4455-b470-411c3edbe49c",
    "dashboard_url": "https://dev-console.stage1.bluemix.net/objectstorage/crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
    "last_operation": {
        "type": "create",
        "state": "in progress",
        "description": null,
        "updated_at": "2017-08-23T17:29:43.643023016Z"
    },
    "resource_bindings_url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A/resource_bindings",
    "resource_aliases_url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A/resource_aliases"
}
```

You want to capture a few things from this response.

Resource instance `guid` will be used with the data API as the header `ibm-service-instance-id`.

Resource instance `url` as `resource_instance_url` .

### Check the status for the service \(resource\) instance

**Request Parameters:**

`access_token` : The access\_token.

`resource_instance_url`: This is the response attribute from the create service instance api.

**Request:**

```
curl -X GET \
  https://resource-controller.bluemix.net<resource_instance_url> \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: b3aafa2e-1a7a-eaae-1e3f-474a247e753b'
```

**Response:**

```
{
    "id": "crn:v1:bluemix:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
    "guid": "d61bf83f-d583-40d0-9877-7b2df60d8b1f",
    "url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A",
    "created_at": "2017-08-23T17:29:43.643023016Z",
    "updated_at": "2017-08-23T17:30:43.902718703Z",
    "deleted_at": null,
    "name": "RC-TEST-INSTANCE-1503509382",
    "region_id": "globalgeo",
    "account_id": "b09edf5642ebfad587c594f4d4a354b0",
    "resource_plan_id": "744bfc56-d12c-4866-88d5-dac9139e0e5d",
    "resource_group_id": "74eb3cc2f3194e579f6666416663d2f0",
    "create_time": 0,
    "crn": "crn:v1:bluemix:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
    "state": "active",
    "type": "service_instance",
    "resource_id": "dff97f5c-bc5e-4455-b470-411c3edbe49c",
    "dashboard_url": "https://dev-console.stage1.bluemix.net/objectstorage/crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
    "last_operation": {
        "type": "create",
        "state": "succeeded",
        "description": "Cloud Object Storage provision state.",
        "updated_at": "2017-08-23T17:30:43.902718703Z"
    },
    "resource_bindings_url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A/resource_bindings",
    "resource_aliases_url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A/resource_aliases"
}
```

### List resource instances

List all resource instances with specified plan ID within the specified resource group.

Request parameters:

`viewer_access_token` : The access\_token of the viewer user.

`account_id` \_: \_Bluemix account UUID. In this use case its `b09edf5642ebfad587c594f4d4a354b0`

`resource_plan_id` : The service plan - The id below is for the premium plan. `744bfc56-d12c-4866-88d5-dac9139e0e5d`

`resource_group_id` : The resource group id where you like to create the resource instance. You either created a new one or retrieve an existing one using the api above. `74eb3cc2f3194e579f6666416663d2f0`

Request:

```
curl -X GET \
  'https://resource-controller.bluemix.net/v1/resource_instances?account_id=b09edf5642ebfad587c594f4d4a354b0&resource_group_id=74eb3cc2f3194e579f6666416663d2f0&resource_plan_id=744bfc56-d12c-4866-88d5-dac9139e0e5d' \
  -H 'authorization: bearer <viewer_access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 100ba979-b5d0-4e86-05bb-a2297d442fd0'
```

Response:

```
{
    "rows_count": 1,
    "next_url": "/v1/resource_instances?next_docid=g1AAAAJQeJzLYWBg4MhgTmGQT0lKzi9KdUhJMjTUy0zKNTAw10vOyS9NScwr0ctLLckBKmRKZEiS____fxaY4-ZU-uDkGQMFhiQGBq5PWSBD5OCGWOI0I0kBSCbZoxpztooBZAy3JJoxZriNcQAZE49szEMmj7kbwMawZqF5yRy3OQkgc-pRnPPr5Z8CsK--o5tjgtOcPBYgydAApIBGzUfy2knJBWA3qaObhTuYIWYtgJi1Hzm0PzWAzWJACybc3oMYdQBi1H2EUafdAiARd4T4oIKY9QBiFlJw_T65BBJ7LFlZAN8tubw&limit=100&account_id=b09edf5642ebfad587c594f4d4a354b0&resource_plan_id=744bfc56-d12c-4866-88d5-dac9139e0e5d&resource_group_id=74eb3cc2f3194e579f6666416663d2f0",
    "resources": [
        {
            "id": "crn:v1:bluemix:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
            "guid": "d61bf83f-d583-40d0-9877-7b2df60d8b1f",
            "url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A",
            "created_at": "2017-08-23T17:29:43.643023016Z",
            "updated_at": "2017-08-23T17:30:43.902718703Z",
            "deleted_at": null,
            "name": "RC-TEST-INSTANCE-1503509382",
            "region_id": "globalgeo",
            "account_id": "b09edf5642ebfad587c594f4d4a354b0",
            "resource_plan_id": "744bfc56-d12c-4866-88d5-dac9139e0e5d",
            "resource_group_id": "74eb3cc2f3194e579f6666416663d2f0",
            "create_time": 0,
            "crn": "crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
            "state": "active",
            "type": "service_instance",
            "resource_id": "dff97f5c-bc5e-4455-b470-411c3edbe49c",
            "dashboard_url": "https://consolebluemix.net/objectstorage/crn:v1:bluemix:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
            "last_operation": {
                "type": "create",
                "state": "succeeded",
                "description": "Cloud Object Storage provision state.",
                "updated_at": "2017-08-23T17:30:43.902718703Z"
            },
            "resource_bindings_url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A/resource_bindings",
            "resource_aliases_url": "/v1/resource_instances/crn%3Av1%3Astaging%3Apublic%3Acloud-object-storage%3Aus-south%3Aa%2Fb09edf5642ebfad587c594f4d4a354b0%3Ad61bf83f-d583-40d0-9877-7b2df60d8b1f%3A%3A/resource_aliases"
        }
    ]
}
```

## Create buckets and objects

This guide demonstrates how to create bucket and upload objects in a COS resource instance. Note that the term 'service instance' is used interchangably with 'resource instance', and so the

From previous steps our `ibm-service-instance-id`is d61bf83f-d583-40d0-9877-7b2df60d8b1f.

### Creating a bucket for a service instance

**Request/URL parameters:**

`access_token` : The access\_token.

`bucket_name` : Name of the bucket you would like to create. i.e.`bhakta-bucket-viewonly-3`

`ibm-service-instance-id` : The service instance id.

**Request:**

```
curl -X PUT \
  http://169.54.98.10/<bucket_name> \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'ibm-service-instance-id: d61bf83f-d583-40d0-9877-7b2df60d8b1f' \
  -H 'postman-token: b9e4b523-7f6c-b5c8-85c3-2af5ffa17def'
```

**Response:**

```
200 OK
```

## Retrieving the bucket list

**Request parameters:**

`access_token` : The access\_token.

`ibm-service-instance-id` : The resource instance ID.

**Request:**

```
curl -X GET \
  http://169.54.98.10/ \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'ibm-service-instance-id: d61bf83f-d583-40d0-9877-7b2df60d8b1f' \
  -H 'postman-token: d486b4bd-f55f-b7c2-ad77-c1442e40c71e'
```

**Response:**

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListAllMyBucketsResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Owner>
        <ID>d61bf83f-d583-40d0-9877-7b2df60d8b1f</ID>
        <DisplayName>d61bf83f-d583-40d0-9877-7b2df60d8b1f</DisplayName>
    </Owner>
    <Buckets>
        <Bucket>
            <Name>bhakta-bucket-viewonly-3</Name>
            <CreationDate>2017-08-23T18:10:46.122Z</CreationDate>
        </Bucket>
    </Buckets>
</ListAllMyBucketsResult>
```

### Upload Objects to Bucket

**Request/URL parameters:**

`access_token` : The access\_token.

`bucket_name` : Name of the bucket you would like to create. i.e.`bhakta-bucket-viewonly-3`

`object_name` : The name of the object. i.e. `test-object.txt`

**Request:**

```
curl -X PUT \
  http://169.54.98.10/bhakta-bucket-viewonly-3/test-object.txt \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: text/plain' \
  -H 'postman-token: f27430d3-1a26-8fb8-cfbb-9a45c86f98bc'
```

**Response:**

```
200 OK
```

### List objects inside a bucket

**Request parameters:**

`access_token`: The access\_token.

`bucket_name`: Name of the bucket. i.e.`bhakta-bucket-viewonly-3`

**Request:**

```
curl -X GET \
  http://169.54.98.10/bhakta-bucket-viewonly-3 \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: b2e96ee0-71cb-7aea-cbe5-8d17eb605c03'
```

**Response:**

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Name>bhakta-bucket-viewonly-3</Name>
    <Prefix></Prefix>
    <Marker></Marker>
    <MaxKeys>1000</MaxKeys>
    <Delimiter></Delimiter>
    <IsTruncated>false</IsTruncated>
    <Contents>
        <Key>test-object.txt</Key>
        <LastModified>2017-08-23T18:20:58.806Z</LastModified>
        <ETag>"d41d8cd98f00b204e9800998ecf8427e"</ETag>
        <Size>0</Size>
        <Owner>
            <ID>d61bf83f-d583-40d0-9877-7b2df60d8b1f</ID>
            <DisplayName>d61bf83f-d583-40d0-9877-7b2df60d8b1f</DisplayName>
        </Owner>
        <StorageClass>STANDARD</StorageClass>
    </Contents>
</ListBucketResult>
```

## Set a policy on a bucket

A user "bluemix\_ui\_dharmesh2@mailinator.com" has been invited to join a bluemix organization using Bluemix IAM "Invite User" feature. At the time of invitation, the user was not granted access to any "IAM" enabled services. However, the user was invited to join the org as an auditor.

### List all users in Admin's account

We need to obtain user's `iam_id`. The id is the attribute of identity object.

**Request/URL parameters:**

`access_token` : The access\_token.

`bss_account` : Bluemix account uuid. You can get his via bluemix CLI. In this use case its `b09edf5642ebfad587c594f4d4a354b0`

**Request:**

```
curl -X GET \
  https://accountmanagement.stage1.ng.bluemix.net/v1/accounts/b09edf5642ebfad587c594f4d4a354b0/users \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 6cf56b94-8481-6a0f-4c56-1a126036378d'
```

**Response:**

```
{
    "total_results": 1,
    "limit": 100,
    "first_url": "/v1/accounts/b09edf5642ebfad587c594f4d4a354b0/users",
    "next_url": null,
    "resources": [
        {
            "metadata": {
                "guid": "210b2e465a19643c7d511ad2d7851b2b",
                "url": "/v1/accounts/b09edf5642ebfad587c594f4d4a354b0/users/210b2e465a19643c7d511ad2d7851b2b",
                "created_at": "2017-08-16T01:45:07.974Z",
                "updated_at": "2017-08-23T13:28:54.365Z",
                "verified_at": "2017-08-16T01:45:36.451Z",
                "identity": {
                    "id": "IBMid-3100029T14",
                    "realmid": "IBMid",
                    "identifier": "3100029T14",
                    "username": "bluemix_ui_dharmesh2@mailinator.com"
                },
                "linkages": [
                    {
                        "origin": "UAA",
                        "id": "e0b85c57-b8c3-4249-8ee9-33461645cf2d"
                    }
                ]
            },
            "entity": {
                "account_id": "b09edf5642ebfad587c594f4d4a354b0",
                "first_name": "Dharmesh",
                "last_name": "Bhakta",
                "state": "ACTIVE",
                "email": "bluemix_ui_dharmesh2@mailinator.com",
                "phonenumber": "9195551212",
                "role": "MEMBER",
                "photo": ""
            }
        }
    ]
}
```

The `iam_id` for bluemix\_ui\_dharmesh2@mailinator.com is `IBMid-3100029T14`

### Check Existing Policies for the user bluemix\_ui\_dharmesh2@mailinator.com

This is done via IAM PAP Endpoint.  The URL format is as follows:

```
https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2F<bss_account_id>/users/<user_id>>/policies
```

Request/URL Parameters:

`bss_account_id` The bluemix bss account id is in the form of a/b09edf5642ebfad587c594f4d4a354b0.

`user_id` :\_ \_This is the iamid of the user bluemix\_ui\_dharmesh2@mailinator.com. i.e IBMid-3100029T14

`access_token` : The access token.

Request:

```
curl -X GET \
  https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2Fb09edf5642ebfad587c594f4d4a354b0/users/IBMid-3100029T14/policies \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: d080915d-396d-053d-718b-7f979544c8a8'
```

Response:

```
{
    "policies": []
}
```

There is no policy for this user as the user is a brand new user for this account.

### Create a policy for above user granting only access to a bucket

This is done via IAM PAP Endpoint.  The URL format is as follows:

```
https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2F<bss_account_id>/users/<user_id>>/policies
```

**Request/URL Parameters:**

`bss_account_id` The bluemix bss account id is in the form of a/b09edf5642ebfad587c594f4d4a354b0.

`user_id` :\_ \_This is the iamid of the user bluemix\_ui\_dharmesh2@mailinator.com. i.e IBMid-3100029T14

`access_token` : The access token.

The payload describes the policy for the user. The `roles` describes the desired "Viewer" role and the `resources` describes the resource. It has four important attributes `serviceName`, `serviceInstance`, `resourceType` and `resource`.

**Request:**

```
curl -X POST \
  https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2Fb09edf5642ebfad587c594f4d4a354b0/users/IBMid-3100029T14/policies \
  -H 'authorization: bearer eyJraWQiOiIyMDE3MDcyMS0wMDowMDowMCIsImFsZyI6IlJTMjU2In0.eyJpYW1faWQiOiJJQk1pZC0zMTAwMDJLSDNDIiwiaWQiOiJJQk1pZC0zMTAwMDJLSDNDIiwicmVhbG1pZCI6IklCTWlkIiwiaWRlbnRpZmllciI6IjMxMDAwMktIM0MiLCJnaXZlbl9uYW1lIjoiRGhhcm1lc2giLCJmYW1pbHlfbmFtZSI6IkJoYWt0YSIsIm5hbWUiOiJEaGFybWVzaCBCaGFrdGEiLCJlbWFpbCI6IjE1OTAwZWNob2hpbGxzQGdtYWlsLmNvbSIsInN1YiI6IjE1OTAwZWNob2hpbGxzQGdtYWlsLmNvbSIsImFjY291bnQiOnsiYnNzIjoiYjA5ZWRmNTY0MmViZmFkNTg3YzU5NGY0ZDRhMzU0YjAifSwiaWF0IjoxNTAzNTEzODcxLCJleHAiOjE1MDM1MTc0NzEsImlzcyI6Imh0dHBzOi8vaWFtLnN0YWdlMS5uZy5ibHVlbWl4Lm5ldC9vaWRjL3Rva2VuIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwic2NvcGUiOiJvcGVuaWQiLCJjbGllbnRfaWQiOiJieCJ9.h5YXPz5F-R6EIfKfLyjozFeSGeyjvGLDWzE6gSyeMHVZ6Ox-gnROSkjUPM2naYTgxnUr61t55K1-IX5Yq78ZObP4WZFESbUyHTYODT6F8ebwmjYsrt-V-2_2lP6bwKTUJey7qvue3on7WRyxUCoxL09pslH6P6VNLZhvj-V6QAvaceEOaBWrbRUnCbnTTbZ1C5rE3tqtf13r2gbETWfvKCPHYXSyGof9UbApx53ZZZ-D8h9gOq7TIDhLjW8LsJT-AXrl3OI1ZJ-CM6zXn04tnvzVDEzyFKm8fbpHbh6qTwQzFkF3vp50iAlHR-pMtgl3Y8JklM4BR7jE9veR6ZmLjg' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 27793ee9-5b4e-d54e-6341-19aac09a0816' \
  -d '{
  "roles": [
    {
      "id": "crn:v1:bluemix:public:iam::::role:Viewer",
      "displayName": "Viewer"
    }
  ],
  "resources": [
    {
      "serviceName": "cloud-object-storage",
      "serviceInstance": "d61bf83f-d583-40d0-9877-7b2df60d8b1f",
      "resourceType": "bucket",
      "resource": "bhakta-bucket-viewonly-3"
    }
  ]
}'
```

**Response:**

```
{
    "id": "80c98c94-e2f5-480a-a256-d91151d59f23",
    "roles": [
        {
            "id": "crn:v1:bluemix:public:iam::::role:Viewer",
            "displayName": "Viewer",
            "description": "Viewers can take actions that do not change state (i.e. read only)."
        }
    ],
    "resources": [
        {
            "serviceName": "cloud-object-storage",
            "serviceInstance": "d61bf83f-d583-40d0-9877-7b2df60d8b1f",
            "resourceType": "bucket",
            "resource": "bhakta-bucket-viewonly-3"
        }
    ],
    "links": {
        "href": "https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2Fb09edf5642ebfad587c594f4d4a354b0/users/IBMid-3100029T14/policies/80c98c94-e2f5-480a-a256-d91151d59f23",
        "link": "self"
    }
}
```

### Check Policies for the user bluemix\_ui\_dharmesh2@mailinator.com agin

This is done via IAM PAP Endpoint.  The URL format is as follows:

```
https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2F<bss_account_id>/users/<user_id>>/policies
```

**Request/URL Parameters:**

`bss_account_id` The bluemix bss account id is in the form of a/b09edf5642ebfad587c594f4d4a354b0.

`user_id` :\_ \_This is the iamid of the user bluemix\_ui\_dharmesh2@mailinator.com. i.e IBMid-3100029T14

`access_token` : The access token.

**Request:**

```
curl -X GET \
  https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2Fb09edf5642ebfad587c594f4d4a354b0/users/IBMid-3100029T14/policies \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: d080915d-396d-053d-718b-7f979544c8a8'
```

**Response:**

```
{
    "policies": [
        {
            "id": "80c98c94-e2f5-480a-a256-d91151d59f23",
            "roles": [
                {
                    "id": "crn:v1:bluemix:public:iam::::role:Viewer",
                    "displayName": "Viewer",
                    "description": "Viewers can take actions that do not change state (i.e. read only)."
                }
            ],
            "resources": [
                {
                    "serviceName": "cloud-object-storage",
                    "serviceInstance": "d61bf83f-d583-40d0-9877-7b2df60d8b1f",
                    "resourceType": "bucket",
                    "resource": "bhakta-bucket-viewonly-3"
                }
            ],
            "links": {
                "href": "https://iampap.stage1.ng.bluemix.net/acms/v1/scopes/a%2Fb09edf5642ebfad587c594f4d4a354b0/users/IBMid-3100029T14/policies/80c98c94-e2f5-480a-a256-d91151d59f23",
                "link": "self"
            }
        }
    ]
}
```

Now there is one policy for this user.

## Validate the bucket policy

### Obtain IAM Token for User with Viewer role

**Request parameters:**

`username` : Bluemix user name. i.g. bluemix\_ui\_dharmesh@mailinator.com

`password` : Bluemix password.

`bss_account` : Bluemix account uuid. You can get his via bluemix CLI. In this use case its b09edf5642ebfad587c594f4d4a354b0

**Request:**

```
curl -X POST \
  'https://iam.bluemix.net/oidc/token?grant_type=password&username=bluemix_ui_dharmesh2%40mailinator.com&response_type=cloud_iam&password=xxxxxxxx&bss_account=b09edf5642ebfad587c594f4d4a354b0' \
  -H 'authorization: Basic Yng6Yng=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: ab2addae-7830-181a-2834-250de0d9de8d'
```

**Response:**

```
{
    "access_token": "eyJpYW1faWQiOiJJQk1pZC0zMTAwMDJLSDNDIiwiaWQiOiJJQk1pZC0zMTAwMDJLSDNDIiwicmVhbG1pZCI6IklCTWlkIiwiaWRlbnRpZmllciI6IjMxMDAwMktIM0MiLCJnaXZlbl9uYW1lIjoiRGhhcm1lc2giLCJmYW1pbHlfbmFtZSI6IkJoYWt0YSIsIm5hbWUiOiJEaGFybWVzaCBCaGFrdGEiLCJlbWFpbCI6IjE1OTAwZWNob2hpbGxzQGdtYWlsLmNvbSIsInN1YiI6IjE1OTAwZWNob2hpbGxzQGdtYWlsLmNvbSIsImFjY291bnQiOnsiYnNzIjoiYjA5ZWRmNTY0MmViZmFkNTg3YzU5NGY0ZDRhMzU0YjAifSwiaWF0IjoxNTAzNTA0MzUyLCJleHAiOjE1MDM1MDc5NTIsImlzcyI6Imh0dHBzOi8vaWFtLnN0YWdlMS5uZy5ibHVlbWl4Lm5ldC9vaWRjL3Rva2VuIiwiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwic2NvcGUiOiJvcGVuaWQiLCJjbGllbnRfaWQiOiJieCJ9.HaMUKbJ0fpPaMN9yVDZ-l52gkc7BFwEfGnP4qofohd6_Tm4Hs3DKycDmc9C38KYAMe_adCFSOBr49nIn9gFIPD4fZNNn77XhcuRTRTBWTQqAjiZjt5JI6MTuVHN0jmFhAw-UFarfl0J1uXqvN6r28dlewmas8tMHulFzTXvVENZ4Z5oUCGNLABLuB-hcxUO8XFWdHZLU4UsCrF2-6LiDo_odK41cg9eDQOTdttdmhVJ7-XhLBgDm1ClYCPVnImTxDLCDrWNaDOLTtJhElfSteFPbYrPFRpmaQhr_YUrf0F4tBcMr4MqqQnwlY64nEuwoc_YTHHMYOEuOrTM2ioGYrw",
    "refresh_token": "aFOfuao19XuRYLPiQXDOakOr4UryoGm0lfy4I5KrNDuXj6SI3qVQQ0G3o5yuY0o3EINVtnkOEKTrtecGCUok2XaJprmPpDrsEA5iDudwrYnDeoHR3rccVF4o9_rmO07Yt8ZH-1tBYEh22DRM_lUYtx0qAB8CDwK763W-XWBMl_sI26CBm-USF5Mbt3iq5JDVnl738tC4WyBLtl21HSE8cRByWYqE1b9NVJ7Qm_rHctW_C_hMW2t1EEYiHiwYGsE5yA-ay7stkCVGWm1V9ZvU1QinltX6gIj0vaOLpbOdEacFTTB3-9WPMxgktK26c-EZ7eUM_KDKTEQI2HBZXQTPC6as8-CgSCe4TZp8WUIoghek4LiS79c2GshPVELDBynER_34QkgXFUUhEeDXjR9l9tOtt6m-b4JYQU6qSxjjvacnSbggynEBUyHuYji0uhwlNQgKWlmeyDyLlrRslIqt24B0RPWvVsL9bL-cCzflfCo_0vLMFozIvsDzJc9U6mdhCodwlJG8ahldQZb-WfTImFl-",
    "token_type": "Bearer",
    "expires_in": 3600,
    "expiration": 1503521480
}
```

You will need to use the `viewer_access_token` for subsquent calls as `Authorization` header for the REST calls. You will want to store it as environment variable so it can be levereraged for other calls.

### Introspect `access_token`_ for _`iam_id`

The iam\_id will be required when listing policies for the user.

**Request parameters:**

`token` : The access\_token for the viewer user.

**Request:**

```
curl -X POST \
  https://iam.bluemix.net/oidc/introspect \
  -H 'authorization: Basic Yng6Yng=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d token=<viewer_access_token>
```

**Response:**

```
{
    "active": true,
    "iss": "https://iam.bluemix.net/oidc/token",
    "realmId": "IBMid",
    "identifier": "3100029T14",
    "sub": "bluemix_ui_dharmesh2@mailinator.com",
    "account": {
        "bss": "b09edf5642ebfad587c594f4d4a354b0"
    },
    "scope": "openid",
    "client_id": "bx",
    "iat": 1503517880,
    "exp": 1503521480,
    "grant_type": "password",
    "given_name": "Dharmesh",
    "family_name": "Bhakta",
    "name": "Dharmesh Bhakta",
    "email": "bluemix_ui_dharmesh2@mailinator.com",
    "iam_id": "IBMid-3100029T14"
}
```

For the user above,  the `iam_id` is `IBMid-3100029T14`

### Retrieve Service\(Resource\) Instances

Since the policy only granted access to a bucket of service instance. User will not be able to list any service instances with COS plan-id.

Request parameters:

`viewer_access_token` : The access\_token of the viewer user.

`account_id` \_: \_Bluemix account uuid. In this use case its `b09edf5642ebfad587c594f4d4a354b0`

`resource_plan_id` : The service plan - The id below is for the premium plan. `744bfc56-d12c-4866-88d5-dac9139e0e5d`

`resource_group_id` : The resource group id where you like to create thh service instance. You either created a new one or retrieve an existing one using the api above. `74eb3cc2f3194e579f6666416663d2f0`

Request:

```
curl -X GET \
  'https://resource-controller-athena.stage1.ng.bluemix.net/v1/resource_instances?account_id=b09edf5642ebfad587c594f4d4a354b0&resource_group_id=74eb3cc2f3194e579f6666416663d2f0&resource_plan_id=744bfc56-d12c-4866-88d5-dac9139e0e5d' \
  -H 'authorization: bearer <viewer_access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 100ba979-b5d0-4e86-05bb-a2297d442fd0'
```

Response:

```
{
    "message": "Unauthorized for the operation",
    "code": 403
}
```

Return code 403 as expected since the policy did not allow users to have access any instances. Just a bucket.

### Create COS Service instance

**Request parameters:**

`viewer_access_token` : The access\_token of the viewer user.

`name` : The name of the service instance you would like to provide.

`region_id` : The value for COS is `globalgeo`.

`resource_plan_id` : The service plan - The id below is for the premium plan.

`resource_group_id` : The resource group id where you like to create thh service instance. You either created a new one or retrieve an existing one using the api above.

**Request:**

```
curl -X POST \
  https://resource-controller-athena.stage1.ng.bluemix.net/v1/resource_instances/ \
  -H 'authorization: bearer <viewer_access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 86aa29aa-8f42-9831-e527-e4b6ad93314c' \
  -d '{
  "name": "RC-TEST-INSTANCE-1503509382",
  "region_id": "globalgeo",
  "resource_plan_id": "744bfc56-d12c-4866-88d5-dac9139e0e5d",
  "resource_group_id": "74eb3cc2f3194e579f6666416663d2f0"
}
```

**Response:**

```
{
    "message": "Unauthorized for the operation",
    "code": 403
}
```

Return code 403 as expected since the policy only allows him to view a bucket.

## Retrieve the service instance directly

If the user tries to access the instance directly, it should still fail.

**Request Parameters:**

`viewer_access_token` : The access\_token of the viewer user.

`resource_instance_url`: This is the response attribute from the create service instance api.

Request:

```
curl -X GET \
  https://resource-controller-athena.stage1.ng.bluemix.net<resource_instance_url> \
  -H 'authorization: bearer <viewer_access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 234cd462-bb5a-7c7f-a7c2-07a05f4ded00'
```

Response:

```
{
    "message": "Unauthorized for the operation",
    "code": 403
}
```

### Creating a bucket for a service instance

**Request/URL parameters:**

`viewer_access_token` : The access\_token of the viewer user.

`bucket_name` : Name of the bucket you would like to create. i.e.`bhakta-bucket-viewonly-4`

`ibm-service-instance-id` : The service instance id.

**Request:**

```
curl -X PUT \
  http://169.54.98.10/<bucket_name> \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'ibm-service-instance-id: d61bf83f-d583-40d0-9877-7b2df60d8b1f' \
  -H 'postman-token: b9e4b523-7f6c-b5c8-85c3-2af5ffa17def'
```

**Response:**

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Error>
    <Code>AccessDenied</Code>
    <Message>Access Denied</Message>
    <Resource>/bhakta-bucket-viewonly-4/</Resource>
    <RequestId>db620409-7f11-4c76-869c-164cda753747</RequestId>
    <httpStatusCode>403</httpStatusCode>
</Error>
```

If you try creating a bucket with a new name, it does return 403 as expected.

### Retrieving the bucket list

**Request parameters:**

`viewer_access_token` : The access\_tokenof the viewer user.

`ibm-service-instance-id` : The service instance id.

**Request:**

```
curl -X GET \
  http://169.54.98.10/ \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'ibm-service-instance-id: d61bf83f-d583-40d0-9877-7b2df60d8b1f' \
  -H 'postman-token: d486b4bd-f55f-b7c2-ad77-c1442e40c71e'
```

**Response:**

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Error>
    <Code>AccessDenied</Code>
    <Message>Access Denied</Message>
    <Resource></Resource>
    <RequestId>ac84582a-e343-4e54-8936-19b8127220a5</RequestId>
    <httpStatusCode>403</httpStatusCode>
</Error>
```

This is as expected, the user should not be able to list buckets.

### Upload Objects to Bucket

**Request/URL parameters:**

`viewer_access_token` : The access\_tokenof the viewer user.

`bucket_name` : Name of the bucket you would like to create. i.e.`bhakta-bucket-viewonly-3`

`object_name` : The name of the object. i.e. `test-object.txt`

**Request:**

```
curl -X PUT \
  http://169.54.98.10/bhakta-bucket-viewonly-3/test-object.txt \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: text/plain' \
  -H 'postman-token: f27430d3-1a26-8fb8-cfbb-9a45c86f98bc'
```

**Response:**

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Error>
    <Code>AccessDenied</Code>
    <Message>Access Denied</Message>
    <Resource>/bhakta-bucket-viewonly-3/test-object.txt</Resource>
    <RequestId>d229842b-d115-4079-9968-1d465d1562fb</RequestId>
    <httpStatusCode>403</httpStatusCode>
</Error>
```

This is as expected, the user should not be able to upload objects.

### List objects inside a bucket

**Request parameters:**

`viewer_access_token`: The access\_token of the viewer user.

`bucket_name`: Name of the bucket. i.e.`bhakta-bucket-viewonly-3`

**Request:**

```
curl -X GET \
  http://169.54.98.10/bhakta-bucket-viewonly-3 \
  -H 'authorization: Bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: b2e96ee0-71cb-7aea-cbe5-8d17eb605c03'
```

**Response:**

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
    <Name>bhakta-bucket-viewonly-3</Name>
    <Prefix></Prefix>
    <Marker></Marker>
    <MaxKeys>1000</MaxKeys>
    <Delimiter></Delimiter>
    <IsTruncated>false</IsTruncated>
    <Contents>
        <Key>test-object.txt</Key>
        <LastModified>2017-08-23T18:20:58.806Z</LastModified>
        <ETag>"d41d8cd98f00b204e9800998ecf8427e"</ETag>
        <Size>0</Size>
        <Owner>
            <ID>d61bf83f-d583-40d0-9877-7b2df60d8b1f</ID>
            <DisplayName>d61bf83f-d583-40d0-9877-7b2df60d8b1f</DisplayName>
        </Owner>
        <StorageClass>STANDARD</StorageClass>
    </Contents>
</ListBucketResult>
```

This works as expected. User has object listing access on this bucket.
