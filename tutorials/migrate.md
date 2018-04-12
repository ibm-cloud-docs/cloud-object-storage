# Migrating data from Swift (Bluemix PaaS) to COS

Create an instance of Cloud Object Storage

    Create an instance of Cloud Object Storage 

https://console.bluemix.net/catalog/infrastructure/cloud-object-storage
Getting started with COS
https://console.bluemix.net/docs/services/cloud-object-storage/getting-started.html#getting-started-console-

    Create bucket(s)

    choose endpoint https://console.bluemix.net/docs/services/cloud-object-storage/basics/endpoints.html#select-regions-and-endpoints

    choose storage classes https://console.bluemix.net/docs/services/cloud-object-storage/basics/classes.html#use-storage-classes


    Re-write your application to use the COS SDK or API

    Java    https://console.bluemix.net/docs/services/cloud-object-storage/libraries/java.html#java

    Python  https://console.bluemix.net/docs/services/cloud-object-storage/libraries/python.html#python

    Node.js  https://console.bluemix.net/docs/services/cloud-object-storage/libraries/node.html#node-js

    other languages see API reference

    https://console.bluemix.net/docs/services/cloud-object-storage/api-reference/about-compatibility-api.html#about-the-ibm-cloud-object-storage-api



You should now have 2 Service Offering on the dashboard for storage services   https://console.bluemix.net/dashboard/storage

    Cloud Object Storage (COS)

    Object Storage  (Swift)


 

    Get Swift credentials

    Click on your Swift instance     Service Offering = Object Storage 

    Click on Service credentials

    New credential

    Add

    For the credential name just created Click on View credentials

    Click the copy button and paste and save it to a text file

    Get COS credentials

    Click on your COS instance     Service Offering = Cloud Object Storage

    Click on Service credentials

    New credential

    Under Inline Configuration Parameters add {"HMAC":true}

    Add

    For the credential name just created Click on View credentials

    Click the copy button and paste and save it to a text file


Choose a machine to run the migration tool
Choose a machine to run the migration tool that is preferable close to Dallas or London where your data is at or where your moving your data too.

Install rclone
https://rclone.org/install/

Configure rclone

    Run rclone config and ctrl+c which will create the config file

    Edit the rclone config file in ~/.config/rclone.conf


Create the Swift source by copying the following and pasting into rclone.conf

    [SWIFT]

    type = swift

    env_auth = false

    user = 

    key = 

    auth = https://identity.open.softlayer.com/v3

    user_id = 

    domain = 

    tenant = object_storage_28368bdd_f3d9_4505_b2b2_e8c25a1291c2

    tenant_id = b42087a56e5e4bac85425188167e8f62

    tenant_domain = 913312da8ae64d4d8de5dd770d8e6a1e

    region = 

    storage_url = 

    auth_token = 

    endpoint_type = public


    From the Swift credentials text file saved above fill in the following in < >

    user = <username>

    key = <password>

    user_id = <userId>

    domain = <domainName>

    tenant = <project>

    tenant_id = <projectId>

    tenant_domain - <domainId>


Create the COS target by copying the following and pasting into rclone.conf

    [COS-US-SOUTH]

    type = s3

    env_auth = false

    access_key_id = 

    secret_access_key = 

    region = other-v4-signature

    endpoint = s3.us-south.objectstorage.softlayer.net

    location_constraint = 

    acl = 

    server_side_encryption = 

    storage_class = 


    From the COS credentials text file saved above fill in the following in < >

    access_key_id = <access_key_id>

    secret_access_key = <secret_access_key>


List the Swift container(s) to make sure rclone was configured properly

    rclone lsd SWIFT:


List the COS bucket(s) to make sure rclone was configured properly

    rclone lsd COS-US-SOUTH:


Run rclone

Swift container before migration


COS bucket before migration


    Do a dry run (no data copied) of rclone to sync the objects in your specified Swift container to COS bucket

    Example:   copy the objects in Swift container "swift-test" to  COS bucket "cos-test-mll"

    rclone --dry-run copy SWIFT:swift-test COS-US-SOUTH:cos-test-mll

    2018/01/12 17:44:13 NOTICE: hello.js: Not copying as --dry-run

    2018/01/12 17:44:13 NOTICE: donuts with dad.jpg: Not copying as --dry-run

    2018/01/12 17:44:13 NOTICE: Beach train tickets.pdf: Not copying as --dry-run


3.  Check that the files you desire to migrate are listed above
4.   Remove the --dry-run flag and add -v flag to copy the data

    rclone -v copy SWIFT:swift-test COS-US-SOUTH:cos-test-mll

    2018/01/12 17:45:25 INFO  : S3 bucket cos-test-mll: Modify window is 1ns

    2018/01/12 17:45:25 INFO  : S3 bucket cos-test-mll: Waiting for checks to finish

    2018/01/12 17:45:25 INFO  : S3 bucket cos-test-mll: Waiting for transfers to finish

    2018/01/12 17:45:27 INFO  : hello.js: Copied (new)

    2018/01/12 17:45:27 INFO  : donuts with dad.jpg: Copied (new)

    2018/01/12 17:45:27 INFO  : Beach train tickets.pdf: Copied (new)

    2018/01/12 17:45:27 INFO  : 

    Transferred:   760.438 kBytes (205.060 kBytes/s)

    Errors:                 0

    Checks:               0

    Transferred:        3

    Elapsed time:      3.7s


COS bucket after migration


Swift container after copy  (data was copied not removed)


Repeat steps 1-4 for any other containers to migrate in Swift


Once you have copied all your data and verified that your application can access the data in COS then delete your Swift service instance.

