# Create buckets and objects

This guide demonstrates how to create bucket and upload objects for Bluemix COS Service instance. It demonstrates use of the `service instance giud` and `access_token`.

From previous steps our `ibm-service-instance-id`is d61bf83f-d583-40d0-9877-7b2df60d8b1f.

## Creating a bucket for a service instance

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

## Upload Objects to Bucket

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

## List objects inside a bucket

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

Next, [create a bucket level access policy](tutorials/bucket-level-access-control-as-admin.html).
