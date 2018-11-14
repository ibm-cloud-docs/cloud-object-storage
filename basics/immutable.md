---

copyright:
  years: 2018
lastupdated: "2018-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Use Immutable Object Storage

Some industries, such as financial services, have strict oversight and audit requirements that require retention of certain records for prescribed lengths of time without modification. In 2003 the SEC issued an Interpretive Release that software defined storage could meet the retention requirements if it can meet a set of rules.

| Rule | Requirement |
| :--- | :--- |
| SEC 17a-4(f)(2)(ii)(A) | Protect data from overwrites and deletion |
| SEC 17a-4(f)(2)(ii)(B) | Automatically verify the storage system properly stored the data. |
| SEC 17a-4(f)(2)(ii)(C) | Manage retention policies for the objects. |
| SEC 17a-4(f)(2)(ii)(D) | Ability to download indices and records. |
| SEC 17a-4(f)(3)(iii/v) | Store duplicate copies and provide auditing capabilities. |

Users configuring buckets with Immutable Object Storage can set the length of retention on a per-object basis, or allow objects to inherit a default retention period set on the bucket.  Objects are also subject to optional 'legal holds' that prevent modification or deletion even after the retention period has expired.  

IBM Cloud administrators and operators are unable to override the constraints imposed by Immutable Object Storage.

Immutable Object Storage is not available in all regions. See [Integrated Services](/docs/services/cloud-object-storage/basics/services.html#service-availability) for more details.
{:tip}

## Using the console
{: #console}

TBD

## Using Libraries and SDKs
{: #sdk}
Several new APIs have been introduced to the IBM COS SDKs to provide support for applications working with Immutable Object Storage.

{: python}
```py
import string
import os
import pytz
import random
import ibm_boto3 as boto3
import ibm_botocore as botocore
import logging
from ibm_botocore.exceptions import ClientError
from ibm_botocore.config import Config
from datetime import datetime as datetimex
from datetime import timedelta

endpoint_url = 'https://10.137.15.35:8443'
ibm_api_key_id = 'bfe4bc68ec914c23bb5ce43a8fefaf29'
ibm_service_instance_id = 'admin'
ibm_auth_endpoint = 'https://192.168.11.7:4567/oidc/token'


def set_debug_level(level):
    logs = ['ibm_boto3',
            'ibm_botocore',
            'requests',
            'urllib3',
            'endpoint',
            'client',
            'auth']
    for name in logs:
        add_debug_logging_handler(name, level)


def add_debug_logging_handler(name, level=logging.DEBUG):
    logger = logging.getLogger(name)
    if logger:
        handler = logging.StreamHandler()
        handler.setFormatter(
            logging.Formatter('%(asctime)s %(levelname)s %(message)s'))
        logger.addHandler(handler)
        logger.setLevel(level)


set_debug_level(logging.DEBUG)

default_bucket_protection_config = {
            'Status': 'Retention',
            'MinimumRetention': {'Days': 10},
            'DefaultRetention': {'Days': 10},
            'MaximumRetention': {'Days': 1000},
            'EnablePermanentRetention': True}


def create_resource():
    session = boto3.session.Session()
    res = session.resource(
        's3',
        endpoint_url=endpoint_url,
        ibm_api_key_id=ibm_api_key_id,
        ibm_service_instance_id=ibm_service_instance_id,
        ibm_auth_endpoint=ibm_auth_endpoint,
        verify=False,
        config=Config(signature_version='oauth')
    )

    return res


def get_random_name(num_chars=10):
    base = string.ascii_lowercase + string.digits
    random_bytes = bytearray(os.urandom(num_chars))
    bucket_name = "demo-object-retention" + ''.join([base[b % len(base)]
                                                     for b in random_bytes]
                                                    )

    return bucket_name


class DemoWORM(object):

    # Create resource
    cos_res = create_resource()

    # Get client from resource
    s3client = cos_res.meta.client

    def __init__(self):
        # Create bucket
        _bucket = self.cos_res.create_bucket(
            Bucket=get_random_name(),
            IBMServiceInstanceId=ibm_service_instance_id)

        # Confirm that the bucket was created
        try:
            self.s3client.head_bucket(Bucket=_bucket.name)
        except ClientError:
            raise Exception('Bucket was not created correctly.')
        print _bucket
        # Test WORM operations
        self.test_worm_operations(_bucket.name)

    def test_worm_operations(self, bucket_name):

        # Put bucket protection
        protection = self.s3client.put_bucket_protection_configuration(
            Bucket=bucket_name,
            ProtectionConfiguration=default_bucket_protection_config)
        print "Bucket Protection values="
        print protection

        # Get bucket protection and confirm it matches expected
        protection_conf = self.check_bucket_protection(bucket_name)

        print "GET Bucket Protection values Response conf "
        print protection_conf

        # Put object with retention period of 12 days
        key = get_random_name()
        period = 12*24*60*60
        put_object_w_Retention = self.put_object_with_retention(
            bucket_name,
            key,
            retention_period=period)

        print "PUT Bucket Protection values Response = "
        print put_object_w_Retention

        # Head object and confirm retention detail matches expected
        self.check_head_object_retention_headers(bucket_name,
                                                 key,
                                                 str(period),
                                                 self.get_expiration_date(12))

        # GET object and confirm retention detail matches expected
        self.check_get_object_retention_headers(bucket_name,
                                                key,
                                                str(period),
                                                self.get_expiration_date(12))

        # Add 5 random object legal holds
        _legal_holds = []
        for pos in range(0, 5):
            _legal_holds.append("test-legal-hold-%d" % pos)

        for legal_hold_id in _legal_holds:
            self.s3client.add_legal_hold(Bucket=bucket_name,
                                         Key=key,
                                         RetentionLegalHoldId=legal_hold_id)

        # HEAD object and confirm legal hold matches expected
        self.check_legal_hold_count(bucket_name, key, 5)

        # List legal holds
        legal_holds = self.s3client.list_legal_holds(Bucket=bucket_name,
                                                     Key=key)

        # Delete random legal hold
        legal_hold_to_delete = legal_holds['LegalHolds'][2]
        legal_hold_id = legal_hold_to_delete['ID']
        self.s3client.delete_legal_hold(
                                        Bucket=bucket_name,
                                        Key=key,
                                        RetentionLegalHoldId=legal_hold_id)

        # HEAD object and confirm legal hold count matches expected
        self.check_legal_hold_count(bucket_name, key, 4)

        # Extend object retention with additional retention period of 10 days
        add_retention_period = 10 * 24 * 60 * 60
        self.s3client.extend_object_retention(
            Bucket=bucket_name,
            Key=key,
            AdditionalRetentionPeriod=add_retention_period)

        # HEAD object and confirm retention detail matches expected
        self.check_head_object_retention_headers(
            bucket_name,
            key,
            str(period + add_retention_period),
            self.get_expiration_date(22))

        # Copy object with retention detail retained
        new_key = get_random_name()
        self.s3client.copy_object(
            Bucket=bucket_name,
            Key=new_key,
            CopySource='%s/%s' % (bucket_name, key),
            RetentionDirective="Copy")

        # HEAD object and confirm retention detail for new object
        # matches expected
        self.check_head_object_retention_headers(
            bucket_name,
            new_key,
            str(period + add_retention_period),
            self.get_expiration_date(22))

        # Copy object replacing retention detail
        new_key = get_random_name()
        self.s3client.copy_object(
            Bucket=bucket_name,
            Key=new_key,
            CopySource='%s/%s' % (bucket_name, key),
            RetentionDirective="Replace")

        # HEAD object and confirm retention detail for new object
        # matches expected. Since we aren't retaining retention info
        # the new object's retention info should assume the bucket's default
        # retention.
        self.check_head_object_retention_headers(
            bucket_name,
            new_key,
            str(default_bucket_protection_config['DefaultRetention']['Days']
                * 24 * 60 * 60),
            self.get_expiration_date(
                default_bucket_protection_config['DefaultRetention']['Days']))

        # Do a multipart upload with object retention period 20 days
        key = get_random_name()
        period = 20 * 24 * 60 * 60
        retention_args = self.get_retention_args(retention_period=period)
        self.create_multipart_upload(bucket_name, key, retention_args)

        # HEAD object and confirm retention detail matches expected
        self.check_head_object_retention_headers(bucket_name,
                                                 key,
                                                 str(period),
                                                 self.get_expiration_date(20))

        # Put object with indefinite retention
        key = get_random_name()
        period = -1
        put_object_w_Retention = self.put_object_with_retention(
            bucket_name,
            key,
            retention_period=period)

        # Head object and confirm retention detail matches expected
        self.check_head_object_retention_headers(bucket_name,
                                                 key,
                                                 str(period))

        # Put object with permanent retention
        key = get_random_name()
        period = -2
        put_object_w_Retention = self.put_object_with_retention(
            bucket_name,
            key,
            retention_period=period)

        # Head object and confirm retention detail matches expected
        self.check_head_object_retention_headers(bucket_name,
                                                 key,
                                                 str(period))

    def check_bucket_protection(self, bucket_name):

        returned = self.s3client.get_bucket_protection_configuration(
            Bucket=bucket_name)
        returned_protection_config = returned['ProtectionConfiguration']
        returned_minimum_days = \
            returned_protection_config['MinimumRetention']['Days']
        returned_maximum_days = \
            returned_protection_config['MaximumRetention']['Days']
        returned_default_days = \
            returned_protection_config['DefaultRetention']['Days']
        expected_minimum_days = \
            default_bucket_protection_config['MinimumRetention']['Days']
        expected_maximum_days = \
            default_bucket_protection_config['MaximumRetention']['Days']
        expected_default_days = \
            default_bucket_protection_config['DefaultRetention']['Days']
        assert(returned_minimum_days == expected_minimum_days)
        assert(returned_maximum_days == expected_maximum_days)
        assert(returned_default_days == expected_default_days)

    ''' Puts an object with required retention information '''
    def put_object_with_retention(self,
                                  bucket_name,
                                  key,
                                  retention_expiration_date=None,
                                  retention_legal_hold_id=None,
                                  retention_period=None,
                                  retention_directive=None):

        # Create common put object arguments
        _args = {}
        _args['Bucket'] = bucket_name
        _args['Body'] = 'some test content'
        _args['Key'] = key

        # Add object retention arguments
        retention_args = self.get_retention_args(
            retention_expiration_date,
            retention_legal_hold_id,
            retention_period,
            retention_directive)
        _args.update(retention_args)

        self.s3client.put_object(**_args)

    ''' Returns object retention arguments '''
    def get_retention_args(
            self,
            retention_expiration_date=None,
            retention_legal_hold_id=None,
            retention_period=None,
            retention_directive=None):
        _args = {}
        if retention_expiration_date:
            _args['RetentionExpirationDate'] = retention_expiration_date
        if retention_legal_hold_id is not None:
            _args['RetentionLegalHoldId'] = retention_legal_hold_id
        if retention_period:
            _args['RetentionPeriod'] = retention_period
        if retention_directive:
            _args['RetentionDirective'] = retention_directive
        return _args

    ''' HEAD object and check that retention headers match expected '''
    def check_head_object_retention_headers(
            self,
            bucket_name,
            key,
            expected_retention_period=None,
            expected_retention_expiration=None):

        # HEAD object
        returned = self.s3client.head_object(Bucket=bucket_name, Key=key)

        # Confirm retention headers match expected
        returned_headers = returned['ResponseMetadata']['HTTPHeaders']
        if 'retention-period' in returned_headers:
            returned_retention_period = returned_headers['retention-period']
            assert(returned_retention_period == expected_retention_period)
        if 'retention-expiration-date' in returned_headers:
            returned_retention_expiration = \
                self.convert_date_to_string(
                    returned_headers['retention-expiration-date'])
            assert(
                returned_retention_expiration == expected_retention_expiration)

    ''' GET object and check that retention headers match expected '''
    def check_get_object_retention_headers(self,
                                           bucket_name,
                                           key,
                                           expected_retention_period,
                                           expected_retention_expiration):

        # GET object
        returned = self.s3client.get_object(Bucket=bucket_name, Key=key)

        # Confirm retention headers match expected
        returned_headers = returned['ResponseMetadata']['HTTPHeaders']
        returned_retention_period = returned_headers['retention-period']
        returned_retention_expiration = \
            self.convert_date_to_string(
                returned_headers['retention-expiration-date'])
        assert(returned_retention_period == expected_retention_period)
        assert(returned_retention_expiration == expected_retention_expiration)

    ''' HEAD object and confirm legal hold count matches expected '''
    def check_legal_hold_count(self,
                               bucket_name,
                               key,
                               expected_count):

        # HEAD object
        returned = self.s3client.head_object(Bucket=bucket_name, Key=key)
        returned_headers = returned['ResponseMetadata']['HTTPHeaders']
        legal_hold_count = returned_headers['retention-legal-hold-count']
        assert(legal_hold_count == str(expected_count))

    def get_expiration_date(self, add_days=0):
        _dt = datetimex.now(pytz.timezone('UTC'))
        if add_days != 0:
            _dt += timedelta(days=add_days)
        return _dt.strftime("%Y-%m-%d")

    def get_expiration_date_with_time(self, add_days=0):
        _dt = datetimex.now(pytz.timezone('UTC'))
        if add_days != 0:
            _dt += timedelta(days=add_days)
        return _dt.strftime("%Y-%m-%d %H:%M:%S+00:00")

    def convert_date_to_string(self, date):
        wday, day, month, year, time, _ = date.split(' ')
        hour, minute, second = time.split(':')
        date_object = datetimex.strptime(
            day + ' ' + month + ' ' + year
            + ' ' + hour + ' ' + minute + ' '
            + second, "%d %b %Y %H %M %S")
        return date_object.strftime("%Y-%m-%d")

    def create_multipart_upload(self, bucket_name, key, extra_args={}):

        _size = 6 * 1024 * 1024
        _part_size = 5 * 1024 * 1024
        upload = self.s3client.create_multipart_upload(Bucket=bucket_name,
                                                       Key=key,
                                                       ContentType='',
                                                       Metadata={})

        _upload_id = upload.get('UploadId')
        _parts = []
        for i, part in enumerate(self.generate_random(_size, _part_size)):
            _part_number = i + 1
            uploaded_part = self.s3client.upload_part(Bucket=bucket_name,
                                                      Key=key,
                                                      PartNumber=_part_number,
                                                      UploadId=_upload_id,
                                                      Body=part)

            _parts.append(
                {'ETag': uploaded_part.get('ETag').replace('"', '').strip(),
                 'PartNumber': _part_number})

        _args = {}
        _args['Bucket'] = bucket_name
        _args['Key'] = key
        _args['MultipartUpload'] = {'Parts': _parts}
        _args['UploadId'] = _upload_id

        _args.update(extra_args)

        _ret = self.s3client.complete_multipart_upload(**_args)
        assert(_ret['ResponseMetadata']['HTTPStatusCode'] == 200)

    def generate_random(self, size, part_size=5*1024*1024):
        """
        Generate the specified number random data.
        (actually each MB is a repetition of the first KB)
        """
        chunk = 1024
        # allowed = string.ascii_letters
        allowed = '1234567890'
        for x in range(0, size, part_size):
            strpart = ''.join([allowed[random.randint(0, len(allowed) - 1)] for _ in range(chunk)])
            s = ''
            left = size - x
            this_part_size = min(left, part_size)
            for y in range(int(this_part_size / chunk)):
                s = s + strpart
            s = s + strpart[:(this_part_size % chunk)]
            yield s
            if (x == size):
                return


def main():
    set_debug_level(logging.DEBUG)
    demo = DemoWORM()


if __name__ == "__main__":
    main()
```
{: python}

{: javascript}
```js
function addProtectionConfigurationToBucket(bucketName) {
  console.log(`Adding protection to bucket ${bucketName}`);
  return cos.putBucketProtectionConfiguration({
    Bucket: bucketName,
    ProtectionConfiguration: {
      'Status': 'Retention',
      'MinimumRetention': {'Days': 10},
      'DefaultRetention': {'Days': 100},
      'MaximumRetention': {'Days': 1000}
    }
  }).promise()
  .then(() => {
    console.log(`Protection added to bucket ${bucketName}!`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function getProtectionConfigurationOnBucket(bucketName) {
  console.log(`Retrieve the protection on bucket ${bucketName}`);
  return cos.getBucketProtectionConfiguration({
    Bucket: bucketName
    }).promise()
    .then((data) => {
      console.log(`Configuration on bucket ${bucketName}: ${data}`);
    }
    .catch((e) => {
        console.log(`ERROR: ${e.code} - ${e.message}\n`);
    });
}

function addLegalHoldToObject(bucketName, objectName, legalHoldId) {
  console.log(`Adding legal hold ${legalHoldId} to object ${objectName} in bucket ${bucketName}`);
  return cos.client.addLegalHold({
    Bucket: bucketName,
    Key: objectId,
    RetentionLegalHoldId: legalHoldId
  }).promise()
  .then(() => {
    console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}!`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function deleteLegalHoldFromObject(bucketName, objectName, legalHoldId) {
  console.log(`Deleting legal hold ${legalHoldId} from object ${objectName} in bucket ${bucketName}`);
  return cos.client.deleteLegalHold({
    Bucket: bucketName,
    Key: objectId,
    RetentionLegalHoldId: legalHoldId
  }).promise()
  .then(() => {
    console.log(`Legal hold ${legalHoldId} deleted from object ${objectName} in bucket ${bucketName}!`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function listLegalHoldsOnObject(bucketName, objectName) {
  console.log(`List all legal holds on object ${objectName} in bucket ${bucketName}`);
  return cos.listLegalHolds({
    Bucket: bucketName,
    Key: objectId
  }).promise()
  .then((data) => {
    console.log(`Legal holds on bucket ${bucketName}: ${data}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function extendRetentionPeriodOnObject(bucketName, objectName, additionalDays) {
  console.log(`Extend the retention period on ${objectName} in bucket ${bucketName} by ${additionalDays} days.`);
  return cos.extendObjectRetention({
    Bucket: bucketName,
    Key: objectName,
    AdditionalRetentionPeriod: additionalDays
  }).promise()
  .then((data) => {
    console.log(`New retention period on ${objectName} is ${data.RetentionPeriod}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function putObjectAddLegalHold(bucketName, objectName, legalHoldId) {
  console.log(`Add legal hold ${legalHoldId} to ${objectName} in bucket ${bucketName} with a putObject operation.`);
  return cos.putObject({
    Bucket: bucketName,
    Key: objectName,
    Body: 'body',
    RetentionLegalHoldId: legalHoldId
  }).promise()
  .then((data) => {
    console.log(`Legal hold ${legalHoldId} added to object ${objectName} in bucket ${bucketName}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}

function copyProtectedObject(sourceBucketName, sourceObjectName, destinationBucketName, newObjectName, ) {
  console.log(`Copy protected object ${sourceObjectName} from bucket ${sourceBucketName} to ${destinationBucketName}/${newObjectName}.`);
  return cos.copyObject({
    Bucket: destinationBucketName,
    Key: newObjectName,
    CopySource: sourceBucketName + '/' + sourceObjectName,
    RetentionDirective: 'Copy'
  }).promise()
  .then((data) => {
    console.log(`Protected object copied from ${sourceBucketName}/${sourceObjectName} to ${destinationBucketName}/${newObjectName}`);
  })
  .catch((e) => {
      console.log(`ERROR: ${e.code} - ${e.message}\n`);
  });
}
```
{: javascript}