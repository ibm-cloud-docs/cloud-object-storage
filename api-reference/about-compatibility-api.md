---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# About the COS API

# API Reference

The Cloud Object Storage API is a REST-based API for reading and writing objects. It uses IBM Cloud Identity & Access Management for authentication/authorization, and supports a subset of the S3 API for easy migration of applications to IBM Cloud.

This reference documentation is being continously improved. If you have technical questions about using the API in your application, please post them on StackOverflow using both `ibm-bluemix` and `object-storage` tags and we will do our best to answer, and then improve this documentation thanks to your feedback.

The following tables describe the complete set of operations when using the COS API to access IBM Cloud Object Storage.  For details on using the operations, including examples, see [the API reference page for buckets](docs/services/cloud-object-storage/api-reference-buckets.html) or [objects](docs/services/cloud-object-storage/api-reference-objects.html).


## Bucket operations

These operations create, destroy, get information about, and control behavior of buckets.

| Bucket operation        | Note                                                                            |
|:------------------------|:--------------------------------------------------------------------------------|
| `GET` buckets           | Used to retrieve a list of all buckets belonging to an account.                 |
| `DELETE` bucket         | Deletes an empty bucket.                                                        |
| `DELETE` bucket CORS    | Deletes any cross-origin resource sharing configuration set on a bucket.        |
| `GET` bucket            | Lists objects contained in a bucket.  Limited to listing 1,000 objects at once. |
| `GET` bucket CORS       | Retrieves any cross-origin resource sharing configuration set on a bucket.      |
| `HEAD` bucket           | Retrieves a bucket's headers.                                                   |
| `GET` multipart uploads | Lists multipart uploads that have not completed or been canceled.               |
| `PUT` bucket            | Buckets have naming restrictions. Accounts are limited to 100 buckets.          |
| `PUT` bucket CORS       | Creates a cross-origin resource sharing configuration for a bucket.             |

Note that the S3 API 'version 2' method of listing objects within a bucket is not supported, and the 'version 1' syntax is needed.

## Object operations

These operations create, destroy, get information about, and control behavior of objects.

| Object operation          | Note                                                                      |
|:--------------------------|:--------------------------------------------------------------------------|
| `DELETE` Object           | Deletes an object from a bucket.                                          |
| `DELETE` Multiple Objects | Deletes multiple objects from a bucket.                                   |
| `GET` Object              | Retrieves an object from a bucket.                                        |
| `HEAD` Object             | Retrieves an object's headers.                                            |
| `OPTIONS` Object          | Checks CORS configuration to see if a specific request can be sent.       |
| `POST` Object             | Adds an object to a bucket using HTML forms.                              |
| `PUT` Object              | Adds an object to a bucket.                                               |
| `PUT` Object (Copy)       | Creates a copy of an object.                                              |
| Initiate Multipart Upload | Creates an upload ID for a given set of parts to be uploaded.             |
| Upload Part               | Uploads a part of an object associated with an upload ID.                 |
| Upload Part (Copy)        | Uploads a part of an existing object associated with an upload ID.        |
| Complete Multipart Upload | Assembles an object from parts associated with an upload ID.              |
| Abort Multipart Upload    | Aborts upload and deletes outstanding parts associated with an upload ID. |
| List Parts                | Returns a list of parts associated with an upload ID                      |


Some additional operations, such as tagging and versioning, are supported in private cloud implementations of IBM COS, but not in public or dedicated clouds at this time. More information custom object storage solutions can be found at [ibm.com](https://www.ibm.com/cloud-computing/products/storage/object-storage/cloud/).
