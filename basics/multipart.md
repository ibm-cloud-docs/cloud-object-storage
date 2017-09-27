---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Store very large objects

IBM COS can support single objects as large as 10TB, although it not recommended to upload objects this large in a single stream.

## Uploading objects in multiple parts

When working with larger objects, multipart upload operations are recommended to write objects into IBM COS. An upload of a single object can be performed as a set of parts and these parts can be uploaded independently in any order and in parallel. Upon upload completion, IBM COS then presents all parts as a single object. This provides many benefits: network interruptions do not cause large uploads to fail, uploads can be paused and restarted over time, and objects can be uploaded as they are being created.

Multipart uploads are only available for objects larger than 5MB. For objects smaller than 50GB, a part size of 20MB to 100MB is recommended for optimum performance. For larger objects, part size can be increased without significant performance impact.  Multipart uploads are limited to no more than 10,000 parts of 5GB each.

Using more than 500 parts leads to inefficiencies in IBM COS and should be avoided when possible.
{:tip}

Due to the additional complexity involved, it is recommended that developers make use of a library that provide multipart upload support.
