Validate User's access for various resources

## Obtain IAM Token for User with Viewer role

**Request parameters:**

`username` : Bluemix user name. i.g. bluemix\_ui\_dharmesh@mailinator.com

`password` : Bluemix password.

`bss_account` : Bluemix account uuid. You can get his via bluemix CLI. In this use case its b09edf5642ebfad587c594f4d4a354b0

**Request:**

```
curl -X POST \
  'https://iam.stage1.ng.bluemix.net/oidc/token?grant_type=password&username=bluemix_ui_dharmesh2%40mailinator.com&response_type=cloud_iam&password=xxxxxxxx&bss_account=b09edf5642ebfad587c594f4d4a354b0' \
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

## Introspect `access_token`_ for _`iam_id`

The iam\_id will be required when listing policies for the user.

**Request parameters:**

`token` : The access\_token for the viewer user.

**Request:**

```
curl -X POST \
  https://iam.stage1.ng.bluemix.net/oidc/introspect \
  -H 'authorization: Basic Yng6Yng=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -d token=<viewer_access_token>
```

**Response:**

```
{
    "active": true,
    "iss": "https://iam.stage1.ng.bluemix.net/oidc/token",
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

## Retrieve Service\(Resource\) Instances

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

## Create COS Service instance

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

## Creating a bucket for a service instance

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

## Retrieving the bucket list

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

## Upload Objects to Bucket

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

## List objects inside a bucket

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
