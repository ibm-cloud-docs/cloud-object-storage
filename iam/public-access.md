---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Allowing public access

To make an object publicly accessible when using IAM for authentication, an `x-amz-acl: public-read` header can be included when uploading the object.  This will bypass any [IAM policy](/docs/services/cloud-object-storage/iam/overview.html) checks and allow for unauthenticated `HEAD` and `GET` requests.

If using [HMAC authentication](/docs/services/cloud-object-storage/hmac/hmac-signature.html), it is possible to allow [temporary public access using pre-signed URLs](/docs/services/cloud-object-storage/hmac/presigned-urls.html).

## Upload a public object

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{:codeblock}

## Allow public access to an existing object
Using the query parameter `?acl` without a payload and the `x-amz-acl: public-read` header will allow public access to the object without needing to overwrite the data.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
```
{:codeblock}

## Make a public object private again
Using the query parameter `?acl` without a payload and an empty `x-amz-acl:` header will revoke public access to the object without needing to overwrite the data.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:" \
     -H "Content-Type: text/plain; charset=utf-8" \
```
{:codeblock}
