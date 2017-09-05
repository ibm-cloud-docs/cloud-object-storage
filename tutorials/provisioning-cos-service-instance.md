# Provisioning COS Service Instance

## Obtain IAM Token for Administrator \(Owner\)

You will need to provide values for three request parameters:

**Request parameters:**

`username` : Bluemix user name.

`password` : Bluemix password.

`bss_account` : Bluemix account uuid. You can get his via bluemix CLI. In this use case its b09edf5642ebfad587c594f4d4a354b0

**Request:**

```
curl -X POST \
  'https://iam.stage1.ng.bluemix.net/oidc/token?grant_type=password&username=15900echohills%40gmail.com&response_type=cloud_iam&password=<your_bm_password>&bss_account=b09edf5642ebfad587c594f4d4a354b0' \
  -H 'authorization: Basic Yng6Yng=' \
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
  https://iam.stage1.ng.bluemix.net/oidc/introspect \
  -H 'authorization: Basic Yng6Yng=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d token=<access_token>
```

**Response:**

```
{
    "active": true,
    "iss": "https://iam.stage1.ng.bluemix.net/oidc/token",
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

## Creating Resource Group for your organization

Going forward resources will be created at resource group level. Each cloud foundry organization will be mapped to a resource group. Use the following API to create a resource group for your organization if there is not one. Once you create once, you don't need to create it.

**Request parameters:**

`access_token` : The access\_token.

`account_id` This is your bluemix account uuid.

`resource_id` This is your organization GUID for which you need to create a resource group.

`name` This is the name you want to give to this resource group.

`quota_id` This is the quota id. There is an API to retrieve this for your organization. But for now, use the same.

Request:

```
curl -X POST \
  https://resource-manager-athena.stage1.ng.bluemix.net/v1/resource_groups \
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

From the response, you want to store the `id` . This is your resource group id which will be required when provisioning service instances.  Resource group id is `74eb3cc2f3194e579f6666416663d2f0`.

## Retrieving resource group for your organization

You can retrieve your resource group id if it has already been created for your.

**Request parameters:**

`access_token` : The access\_token.

`resource_id` : This you CF organization GUID for which you would like to retrieve resource group.

**Request:**

```
curl -X GET \
  'https://resource-manager-athena.stage1.ng.bluemix.net/v1/resource_groups?resource_id=69e6e996-bebe-4e24-9aed-8efb448ecff7&resource_origin=CF_ORG' \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 78a0f014-2010-84d5-d9c1-cdf1ee966e82'
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

## Create COS Service instance

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

Resource instance `guid` is our service instance identifier `ibm-service-instance-id`

Resource instance `url` as `resource_instance_url` .

## Check the status for the service \(resource\) instance

**Request Parameters:**

`access_token` : The access\_token.

`resource_instance_url`: This is the response attribute from the create service instance api.

**Request:**

```
curl -X GET \
  https://resource-controller-athena.stage1.ng.bluemix.net<resource_instance_url> \
  -H 'authorization: bearer <access_token>' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: b3aafa2e-1a7a-eaae-1e3f-474a247e753b'
```

**Response:**

```
{
    "id": "crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
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

## List Service Instances

List all service instance with specified plan-id and and the specified resource group.

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
    "rows_count": 1,
    "next_url": "/v1/resource_instances?next_docid=g1AAAAJQeJzLYWBg4MhgTmGQT0lKzi9KdUhJMjTUy0zKNTAw10vOyS9NScwr0ctLLckBKmRKZEiS____fxaY4-ZU-uDkGQMFhiQGBq5PWSBD5OCGWOI0I0kBSCbZoxpztooBZAy3JJoxZriNcQAZE49szEMmj7kbwMawZqF5yRy3OQkgc-pRnPPr5Z8CsK--o5tjgtOcPBYgydAApIBGzUfy2knJBWA3qaObhTuYIWYtgJi1Hzm0PzWAzWJACybc3oMYdQBi1H2EUafdAiARd4T4oIKY9QBiFlJw_T65BBJ7LFlZAN8tubw&limit=100&account_id=b09edf5642ebfad587c594f4d4a354b0&resource_plan_id=744bfc56-d12c-4866-88d5-dac9139e0e5d&resource_group_id=74eb3cc2f3194e579f6666416663d2f0",
    "resources": [
        {
            "id": "crn:v1:staging:public:cloud-object-storage:us-south:a/b09edf5642ebfad587c594f4d4a354b0:d61bf83f-d583-40d0-9877-7b2df60d8b1f::",
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
    ]
}
```
