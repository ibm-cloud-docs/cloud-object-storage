---

copyright:
  years: 2017, 2019
lastupdated: "2019-11-11"

keywords: cli, command line interface, object storage, curl

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Using `cURL`
{: #curl}

You can get the most out working with the command line in most environments with {{site.data.keyword.cos_full}} and `cURL`.  
{: shortdesc}

Here's a 'cheat sheet' of basic `curl` commands for the {{site.data.keyword.cos_full}} REST API. More detail can be found in the API reference for [buckets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) or [objects](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-object-operations).

Using `curl` assumes a certain amount of familiarity with the command line and Object Storage, and have the necessary information from a [service credential](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials), the [endpoints reference](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints), or the [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). If any terms or variables are unfamiliar, they can be found in the [glossary](/docs/overview?topic=overview-glossary).

**Note**: Personally Identifiable Information (PII): When you create buckets or adding objects, ensure to not use any information that can identify any user (natural person) by name, location, or any other means.
{:tip}

## Request an IAM Token
{: #curl-iam}

Two ways you can generate an IAM `oauth` token for authenticating requests are using a `curl` command with an API key (described later), or from the command line by using [{{site.data.keyword.cloud}} CLI](/docs/cli?topic=cloud-cli-ibmcloud_commands_iam#ibmcloud_iam_oauth_tokens). 

### Request an IAM token by using an API key
{: #curl-token}

Ensure that you have an API key. You can get one from [{{site.data.keyword.iamlong}}](https://cloud.ibm.com/iam/apikeys).

```
curl -X "POST" "https://iam.cloud.ibm.com/identity/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## Get your resource instance ID
{: #curl-instance-id}

Some of the following commands require an `ibm-service-instance-id` parameter. To find this value, go to the **Service credentials** tab of your Object Storage instance in the cloud console. Create a credential if needed, then use the *View credentials* menu to see the JSON format. Use the value of `resource_instance_id`. 

For use with curl APIs, you need only the UUID that starts after the last single colon and ends before the final double colon. For example, the ID `crn:v1:bluemix:public:cloud-object-storage:global:a/81caa0254631ce5f9330ae427618f209:39d8d161-22c4-4b77-a856-f11db5130d7d::` can be abbreviated to `39d8d161-22c4-4b77-a856-f11db5130d7d`.
{:tip}

## List buckets
{: #curl-list-buckets}

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Add a bucket
{: #curl-add-bucket}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Add a bucket (storage class)
{: #curl-add-bucket-class}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"
```
{:codeblock}

A list of valid codes for `LocationConstraint` can be referenced in [the Storage Classes guide](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-classes#classes-locationconstraint).

## Create a bucket CORS
{: #curl-new-cors}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CORSConfiguration>
      <CORSRule>
        <AllowedOrigin>(url)</AllowedOrigin>
        <AllowedMethod>(request-type)</AllowedMethod>
        <AllowedHeader>(url)</AllowedHeader>
      </CORSRule>
     </CORSConfiguration>"
```
{:codeblock}

The `Content-MD5` header needs to be the binary representation of a base64-encoded MD5 hash.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Get a bucket CORS
{: #curl-get-cors}
```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Delete a bucket CORS
{: #curl-delete-cors}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## List objects
{: #curl-list-objects}
```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Get bucket headers
{: #curl-head-bucket}
```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Get bucket metadata
{: #curl-bucket-metadata}

Note the use of the config API endpoint isn't the same as the endpoint for your bucket itself. Use of this command returns metadata for the specified bucket.
 
```
curl https://config.cloud-object-storage.cloud.ibm.com/v1/b/{my-bucket} \
                        -H 'authorization: bearer <IAM_token>' 
```
{:codeblock}

## Delete a bucket
{: #curl-delete-bucket}

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Upload an object
{: #curl-put-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Get an object's headers
{: #curl-head-object}

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Copy an object
{: #curl-copy-object}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## Check CORS information
{: #curl-options-object}

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Download an object
{: #curl-get-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Check object's ACL
{: #curl-acl-object}

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Enable a firewall
{: #curl-enable-firewall}

Note the use of the config API endpoint isn't the same as the endpoint for your bucket itself. Use of this command enables a firewall for the specified bucket. No other {{site.data.keyword.cloud}} services can access the bucket when the firewall is active.
 
```
curl -X PATCH https://config.cloud-object-storage.cloud.ibm.com/v1/b/{my-bucket} \
                        -H 'authorization: bearer $IAM_TOKEN' \
                        -d '{"firewall": {"allowed_ip": ["10.142.175.0/22", "10.198.243.79"]}}'
```
{:codeblock}

## Enable activity tracking
{: #curl-enable-activity-tracking}

Note the use of the config API endpoint isn't the same as the endpoint for your bucket itself. Use of this command enables activity tracking for the specified bucket.
 
```
curl -X PATCH https://config.cloud-object-storage.cloud.ibm.com/v1/b/{my-bucket} \
                        -H 'authorization: bearer <IAM_token>' \
                        -d '{"activity_tracking": { \
                                "activity_tracker_crn": at_crn, \
                                "read_data_events": True, \
                                "write_data_events": True}'
```
{:codeblock}

## Allow anonymous access to an object
{: #curl-public-object}
```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Delete an object
{: #curl-delete-object}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Delete many objects
{: #curl-delete-objects}
```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete>"
```
{:codeblock}

The `Content-MD5` header needs to be the binary representation of a base64-encoded MD5 hash.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Start a multipart upload
{: #curl-multipart-initiate}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Upload a part
{: #curl-multipart-part}

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Complete a multipart upload
{: #curl-multipart-complete}

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d "<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>"
```
{:codeblock}

## Get incomplete multipart uploads
{: #curl-multipart-get}

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Stop incomplete multipart uploads
{: #curl-multipart-abort}
```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
