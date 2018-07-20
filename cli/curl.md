---

copyright:
  years: 2017, 2018
lastupdated: "2018-07-12"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Using `curl`

Here's a 'cheatsheet' of basic `curl` commands for the {{site.data.keyword.cos_full}} REST API.  Additional detail can be found in the API reference for [buckets](/docs/services/cloud-object-storage/api-reference/api-reference-buckets.html) or [objects](/docs/services/cloud-object-storage/api-reference/api-reference-objects.html).

Using `curl` assumes a certain amount of familiarity with the command line and object storage, and have gotten the necessary information from a [service credential](/docs/services/cloud-object-storage/iam/service-credentials.html), the [endpoints reference](/docs/services/cloud-object-storage/basics/endpoints.html), or the [console](/docs/services/cloud-object-storage/getting-started.html).  If any terms or variables are unfamiliar they can be found in the [glossary](/docs/services/cloud-object-storage/basics/glossary.html).

**Note**: Personally Identifiable Information (PII): When creating buckets and/or adding objects, please ensure to not use any information that can identify any user (natural person) by name, location or any other means.
{:tip}

## Request an IAM Token
There are two ways to generate an IAM oauth token for authenticating requests: using a `curl` command with an API key (described below) or from the command line using [IBM Cloud CLI](/docs/services/cloud-object-storage/getting-started-cli.html#gather-key-information).  
### Request an IAM token using an API key
First ensure that you have an API key.  Get this from [{{site.data.keyword.iamlong}}](https://www.bluemix.net/iam/#/apikeys).

```
curl -X "POST" "https://iam.bluemix.net/oidc/token" \
     -H 'Accept: application/json' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode "apikey={api-key}" \
     --data-urlencode "response_type=cloud_iam" \
     --data-urlencode "grant_type=urn:ibm:params:oauth:grant-type:apikey"
```
{:codeblock}

## List buckets

```
curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Add a bucket

```
curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
```
{:codeblock}

## Add a bucket (storage class)


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

## Allow public object listing

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?acl"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Check a bucket ACL

```
curl "https://(endpoint)/(bucket-name)/?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Create a bucket CORS

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/?cors"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d \$'<CORSConfiguration>
         <CORSRule>
           <AllowedOrigin>(url)</AllowedOrigin>
           <AllowedMethod>(request-type)</AllowedMethod>
           <AllowedHeader>(url)</AllowedHeader>
         </CORSRule>
       </CORSConfiguration>
```
{:codeblock}

The `Content-MD5` header needs to be the binary representation of a base64-encoded MD5 hash.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Get a bucket CORS

```
curl "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Delete a bucket CORS

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/?cors"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## List objects

```
curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Get bucket headers

```
curl --head "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Delete a bucket

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Upload an object

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)" \
 -H "Authorization: bearer (token)" \
 -H "Content-Type: (content-type)" \
 -d "(object-contents)"
```
{:codeblock}

## Get an object's headers

```
curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Copy an object

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"
```
{:codeblock}

## Check CORS info

```
curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"
```
{:codeblock}

## Download an object

```
curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Check object's ACL

```
curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Allow anonymous access to an object

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"
```
{:codeblock}

## Delete an object

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Delete multiple objects

```
curl -X "POST" "https://(endpoint)/(bucket-name)?delete"
 -H "Content-MD5: (md5-hash)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d \$'<?xml version="1.0" encoding="UTF-8"?>
         <Delete>
           <Object>
             <Key>(first-object)</Key>
           </Object>
           <Object>
             <Key>(second-object)</Key>
           </Object>
         </Delete> '
```
{:codeblock}

The `Content-MD5` header needs to be the binary representation of a base64-encoded MD5 hash.

```
echo -n (XML block) | openssl dgst -md5 -binary | openssl enc -base64
```
{:codeblock}

## Initiate a multipart upload

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Upload a part

```
curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
```
{:codeblock}

## Complete a multipart upload

```
curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: text/plain; charset=utf-8"
 -d \$'<CompleteMultipartUpload>
         <Part>
           <PartNumber>1</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
         <Part>
           <PartNumber>2</PartNumber>
           <ETag>(etag)</ETag>
         </Part>
       </CompleteMultipartUpload>
```
{:codeblock}

## Get incomplete multipart uploads

```
curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"
```
{:codeblock}

## Abort incomplete multipart uploads

```
curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
```
{:codeblock}
