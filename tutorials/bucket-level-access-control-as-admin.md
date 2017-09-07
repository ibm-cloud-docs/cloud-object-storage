# Set a policy on a bucket

A user "bluemix\_ui\_dharmesh2@mailinator.com" has been invited to join a bluemix organization using Bluemix IAM "Invite User" feature. At the time of invitation, the user was not granted access to any "IAM" enabled services. However, the user was invited to join the org as an auditor.

## List all users in Admin's account

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

## Check Existing Policies for the user bluemix\_ui\_dharmesh2@mailinator.com

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

## Create a policy for above user granting only access to a bucket

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

## Check Policies for the user bluemix\_ui\_dharmesh2@mailinator.com agin

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

Now [we can validate that the controls are in place](tutorials/access-control-validation-as-viewer.html).
