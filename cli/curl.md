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

# Using `curl`

Here's a 'cheatsheet' of basic `curl` commands for the COS REST API.  Additional detail can be found in the API reference for [buckets](docs/services/cloud-object-storage/api-reference/api-reference-buckets.html) or [objects](docs/services/cloud-object-storage/api-reference/api-reference-objects.html).


List buckets
------------

curl "https://(endpoint)/"
 -H "Authorization: bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"

Add a bucket
------------

curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"

Add a bucket (storage class)
----------------------------

curl -X "PUT" "https://(endpoint)/(bucket-name)"
 -H "Content-Type: text/plain; charset=utf-8"
 -H "Authorization: Bearer (token)"
 -H "ibm-service-instance-id: (resource-instance-id)"
 -d "<CreateBucketConfiguration>
       <LocationConstraint>(provisioning-code)</LocationConstraint>
     </CreateBucketConfiguration>"

Get a bucket's location and storage class
-----------------------------------------

curl "https://(endpoint)(bucket-name)/?location"
 -H "Authorization: Bearer (token)"

Allow public object listing
---------------------------

curl -X "PUT" "https://(endpoint)(bucket-name)/?acl"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"

Check a bucket ACL
------------------

curl "https://(endpoint)(bucket-name)/?acl"
 -H "Authorization: bearer (token)"

Create a bucket CORS
--------------------

curl -X "PUT" "https://(endpoint)(bucket-name)/?cors"
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

Get a bucket CORS
-----------------

curl "https://(endpoint)(bucket-name)/?cors"
 -H "Authorization: bearer (token)"

Delete a bucket CORS
--------------------

curl -X "DELETE" "https://(endpoint)(bucket-name)/?cors"
 -H "Authorization: bearer (token)"

List objects
------------

curl "https://(endpoint)/(bucket-name)"
 -H "Authorization: bearer (token)"

Get bucket headers
------------------

Foo
===

curl --head "https://(endpoint)(bucket-name)/"
 -H "Authorization: bearer (token)"

Delete a bucket
---------------

curl -X "DELETE" "https://(endpoint)(bucket-name)/"
 -H "Authorization: bearer (token)"

Upload an object
----------------

curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"
 -d "(object-contents)"

Get an object's headers
-----------------------

curl --head "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"

Copy an object
--------------

curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"
 -H "x-amz-copy-source: /(bucket-name)/(object-key)"

Check CORS info
---------------

curl -X "OPTIONS" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Access-Control-Request-Method: PUT"
 -H "Origin: http://(url)"

Download an object
------------------

curl "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"

Check object's ACL
------------------

curl "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Authorization: bearer (token)"

Allow anonymous access to an object
-----------------------------------

curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?acl"
 -H "Content-Type: (content-type)"
 -H "Authorization: bearer (token)"
 -H "x-amz-acl: public-read"

Delete an object
----------------

curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)"
 -H "Authorization: bearer (token)"

Delete multiple objects
-----------------------

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

Initiate a multipart upload
---------------------------

curl -X "POST" "https://(endpoint)/(bucket-name)/(object-key)?uploads"
 -H "Authorization: bearer (token)"

Upload a part
-------------

curl -X "PUT" "https://(endpoint)/(bucket-name)/(object-key)?partNumber=(sequential-integer)&uploadId=(upload-id)"
 -H "Authorization: bearer (token)"
 -H "Content-Type: (content-type)"


Complete a multipart upload
---------------------------

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

Get incomplete multipart uploads
--------------------------------

curl "https://(endpoint)/(bucket-name)/?uploads"
 -H "Authorization: bearer (token)"

Abort incomplete multipart uploads
----------------------------------

curl -X "DELETE" "https://(endpoint)/(bucket-name)/(object-key)?uploadId"
 -H "Authorization: bearer (token)"
