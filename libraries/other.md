---

copyright:
  years: 2017
lastupdated: "2017-02-23"

---

# Other languages

Python support is provided through the Boto 3 library.  It can be installed from the Python Package Index via `pip install boto3`. The examples shown here were generated using version 1.4.0 of the boto3 package.  

Existing applications that use the original Boto 2.x library should be compatible as well, although it is no longer being actively maintained and users are encouraged to migrate to Boto 3. 

By default, access keys are sourced from `~/.aws/credentials`, but can also be set as environment variables. Minimum required `~/.aws/credentials` file:

```
[default]
aws_access_key_id = {Access Key ID}
aws_secret_access_key = {Secret Access Key}
```

The `boto3` library provides complete access to the S3 API and can source credentials from the `~/.aws/credentials` file referenced above.  The IBM COS endpoint must be specified when creating a service resource or low-level client as shown in the following basic examples. 

Detailed documentation can be found at [boto3.readthedocs.io](https://boto3.readthedocs.io/en/latest/reference/services/s3.html).


## Example service resource script

Creating a service resource provides greater abstraction for higher level tasks.  This is a basic script that fetches the list of buckets owned by an account, and lists the objects in each bucket. 

```python
import boto3

endpoint = 'https://s3-api.us-geo.objectstorage.softlayer.net'

cos = boto3.resource('s3', endpoint_url=endpoint)

for bucket in cos.buckets.all():
    print(bucket.name)
    for obj in bucket.objects.all():
        print("  - %s") % obj.key
```

**Example script response**

```
bucket-1
  - c1ca2-filename-00001
  - c9872-filename-00002
  - 98837-filename-00003
  - abfc4-filename-00004
bucket-2
  - c1ca2-filename-00011
```

## Example low-level client script

Creating a low-level client allows for considerably more detail and access to metadata. This is a basic script that fetches the list of buckets owned by an account, and lists objects in each bucket. As considerably more data is returned than in the previous example, the `pprintpp` package is used to increase the readability of the raw output.

```python
import boto3
import pprint as pp

endpoint = 'https://s3-api.us-geo.objectstorage.softlayer.net'

cos = boto3.client('s3', endpoint_url=endpoint)

print('These are the buckets in this service account:')
buckets = cos.list_buckets()
pp.pprint(buckets, width=180)

for bucket in buckets['Buckets']:
    name = bucket['Name']
    print("Raw output from 'list_buckets()' in %s:" % name)
    objects = cos.list_objects(Bucket=name)
    pp.pprint(objects)
```

**Example script response**

```
These are the buckets in this service account:
{
    u'Buckets': [
        {u'CreationDate': datetime.datetime(1970, 1, 1, 0, 0, tzinfo=tzutc()), u'Name': 'bucket-1'},
        {u'CreationDate': datetime.datetime(2016, 9, 16, 21, 29, 0, 912000, tzinfo=tzutc()), u'Name': 'bucket-2'},
    ],
    u'Owner': {u'DisplayName': '{storage-account-uuid}', u'ID': '{storage-account-uuid}'},
    'ResponseMetadata': {
        'HTTPHeaders': {
            'accept-ranges': 'bytes',
            'content-length': '463',
            'content-type': 'application/xml',
            'date': 'Wed, 12 Oct 2016 13:53:27 GMT',
            'server': 'Cleversafe/3.9.0.129',
            'x-amz-request-id': '0a7a3f3b-d788-45c6-a16d-9025031e43cb',
            'x-clv-request-id': '0a7a3f3b-d788-45c6-a16d-9025031e43cb',
            'x-clv-s3-version': '2.5',
        },
        'HTTPStatusCode': 200,
        'HostId': '',
        'RequestId': '0a7a3f3b-d788-45c6-a16d-9025031e43cb',
        'RetryAttempts': 0,
    },
}
Raw output from 'list_buckets()' in apiary:
{
    u'Contents': [
        {
            u'ETag': '"2bcc8ee6bc1e4b8cd2f9a1d61d817ed2"',
            u'Key': 'c1ca2-filename-00001',
            u'LastModified': datetime.datetime(2016, 10, 6, 14, 44, 37, 211000, tzinfo=tzutc()),
            u'Owner': {
                u'DisplayName': '{storage-account-uuid}',
                u'ID': '{storage-account-uuid}',
            },
            u'Size': 20950,
            u'StorageClass': 'STANDARD',
        },
        {
            u'ETag': '"3ca744fa96cb95e92081708887f63de5"',
            u'Key': 'c9872-filename-00002',
            u'LastModified': datetime.datetime(2016, 10, 11, 13, 12, 32, 234000, tzinfo=tzutc()),
            u'Owner': {
                u'DisplayName': '{storage-account-uuid}',
                u'ID': '{storage-account-uuid}',
            },
            u'Size': 533,
            u'StorageClass': 'STANDARD',
        },
        {
            u'ETag': '"ed5204ca0be62274dcd4dca3a37ae606"',
            u'Key': '98837-filename-00003',
            u'LastModified': datetime.datetime(2016, 10, 6, 15, 11, 49, 20000, tzinfo=tzutc()),
            u'Owner': {
                u'DisplayName': '{storage-account-uuid}',
                u'ID': '{storage-account-uuid}',
            },
            u'Size': 1989,
            u'StorageClass': 'STANDARD',
        },
        {
            u'ETag': '"13d567d518c650414c50a81805fff7f2"',
            u'Key': 'abfc4-filename-00004',
            u'LastModified': datetime.datetime(2016, 9, 28, 15, 36, 56, 807000, tzinfo=tzutc()),
            u'Owner': {
                u'DisplayName': '{storage-account-uuid}',
                u'ID': '{storage-account-uuid}',
            },
            u'Size': 837,
            u'StorageClass': 'STANDARD',
        },
    ],
    u'Delimiter': '',
    u'IsTruncated': False,
    u'Marker': '',
    u'MaxKeys': 1000,
    u'Name': 'bucket-1',
    u'Prefix': '',
    'ResponseMetadata': {
        'HTTPHeaders': {
            'accept-ranges': 'bytes',
            'content-length': '2492',
            'content-type': 'application/xml',
            'date': 'Wed, 12 Oct 2016 13:53:27 GMT',
            'server': 'Cleversafe/3.9.0.129',
            'x-amz-request-id': 'bb6984e9-68bf-4419-be98-1af0dbc782f9',
            'x-clv-request-id': 'bb6984e9-68bf-4419-be98-1af0dbc782f9',
            'x-clv-s3-version': '2.5',
        },
        'HTTPStatusCode': 200,
        'HostId': '',
        'RequestId': 'bb6984e9-68bf-4419-be98-1af0dbc782f9',
        'RetryAttempts': 0,
    },
}
Raw output from 'list_buckets()' in bucket-2:
{
    u'Contents': [
        {
            u'ETag': '"88bfd87922eb17cff5b791c4d3fee1e4"',
            u'Key': 'c1ca2-filename-00011',
            u'LastModified': datetime.datetime(2016, 9, 17, 18, 11, 58, 811000, tzinfo=tzutc()),
            u'Owner': {
                u'DisplayName': '{storage-account-uuid}',
                u'ID': '{storage-account-uuid}',
            },
            u'Size': 112001,
            u'StorageClass': 'STANDARD',
        },
    ],
    u'Delimiter': '',
    u'IsTruncated': False,
    u'Marker': '',
    u'MaxKeys': 1000,
    u'Name': 'bucket-2',
    u'Prefix': '',
    'ResponseMetadata': {
        'HTTPHeaders': {
            'accept-ranges': 'bytes',
            'content-length': '905',
            'content-type': 'application/xml',
            'date': 'Wed, 12 Oct 2016 13:53:27 GMT',
            'server': 'Cleversafe/3.9.0.129',
            'x-amz-request-id': 'ac14085f-2e96-4c5a-b276-de98edb32e30',
            'x-clv-request-id': 'ac14085f-2e96-4c5a-b276-de98edb32e30',
            'x-clv-s3-version': '2.5',
        },
        'HTTPStatusCode': 200,
        'HostId': '',
        'RequestId': 'ac14085f-2e96-4c5a-b276-de98edb32e30',
        'RetryAttempts': 0,
    },
}
```

