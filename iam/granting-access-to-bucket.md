---

copyright:
  years: 2017
lastupdated: "2017-02-23"

---

# Access control for a single bucket

## Granting access to a user

If you only want to grant a user access to a particular bucket,

Invite a user, but don't grant them IAM access. (Ensure that the core IAM docs cover required steps)
Grant them access to the bucket using the COS portal or IAM API.
Give them the endpoints and bucket name, and info on how to get an API key using the IAM API.
Remind them that they can't generate a list of buckets they have access to because they don't have access to service instance operations.  They only need a Service Instance ID if they are creating or listing buckets.
This is possible for Service IDs as well (see Service ID section).
How to view permissions on a bucket
View through IAM UI/API (make sure CRN/other terms are explained)
How to remove permission on a bucket.
Delete the policy through IAM.
How to modify permissions on a bucket.
You need to delete the existing policy and make a new one. (Can be done via API)
