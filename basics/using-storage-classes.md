---

copyright:
  years: 2017
lastupdated: "2017-02-23"

---

# Use storage classes

Not all data that is stored needs to be accessed frequently, and some archival data might be rarely accessed if at all.  For less active workloads, buckets can be created in a different storage class and objects stored in these buckets will incur charges on a different schedule than standard storage.

There are four storage classes:

*  **Standard**: Used for active workloads - there is no charge for data retrieved (besides the cost of the operational request itself).
*  **Vault**: Used for cool workloads where data is accessed less than once a month - an additional retrieval charge is applied each time data is read. The service includes a minimum threshold for object size and storage period consistent with the intended use of this service for cooler, less-active data.
*  **Cold Vault**: Used for cold workloads where data is accessed every 90 days or less - an larger additional retrieval charge is applied each time data is read. The service includes a longer minimum threshold for object size and storage period consistent with the intended use of this service for cold, inactive data.
*  **Flex**: Used for dynamic workloads where access patterns are more difficult to predict. Depending on usage, if the cost of storage combined with retrieval charges exceeds a cap value, then retrieval charges are dropped and a new capacity charge is applied instead. If the data isn't accessed frequently, it is more cost effective than Standard storage, and if access usage patterns unexpectedly become more active it is more cost effective than Vault or Cold Vault storage. There is no minimum object size or storage period.

For pricing details please see [the pricing table at ibm.com](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage#s3api).

For information on how to create buckets with different storage classes, please see the [API reference](docs/services/api-reference/api-reference-buckets.html#create-a-vault-bucket).
