---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# For developers

First, ensure you have the [Bluemix CLI](https://clis.ng.bluemix.net/ui/home.html) and [IBM Developer Tools](https://console.bluemix.net/docs/cloudnative/dev_cli.html) installed.

## Provision an instance of COS
  1. First, make sure you have an API key.  Get this from [IBM Cloud Identity and Access Management](https://www.bluemix.net/iam/#/apikeys).
  2. Login to Bluemix using the CLI.  It's also possible to store the API key in a file or set it as an environment variable.

```
bx login --apikey <value>
```
  3. Next, provision an instance of COS specifying the name for the instance, the ID and the desired plan (lite or standard).  This will get us the CRN.  If you have an upgraded account, specify the `Premium` plan.  Otherwise specify `Lite`.

```
bx resource instance-create <instance-name> cloud-object-storage <plan> -r global
```

  4. Now we need the ID for the new instance.

```
bx resource instance <instance-name> -r global
```

  5. Next, get a token from IAM.

```
bx iam oauth-tokens
```

## Create a bucket and upload an object

  1. Take your new token, and the ID of the instance, and create a new bucket in the `us-south` region.

```
curl -X "PUT" "https://s3.us-south.objectstorage.softlayer.net/<bucket-name>" \
     -H "Authorization: Bearer <token>" \
     -H "ibm-service-instance-id: <resource-instance-id>"
```

  2. Upload an object.

```
  curl -X "PUT" "https://s3.us-south.objectstorage.softlayer.net/<bucket-name>/<object-key>" \
       -H "Authorization: Bearer <token>" \
       -H "Content-Type: text/plain; charset=utf-8" \
       -d "This is a tiny object made of plain text."
```

## Manage access

  1. Invite someone to your account with minimal permissions.

```
bx account user-invite <email-address> <org-name> auditor <space-name> auditor
```

  2. Then grant them read-only access to your COS instances.

```
bx iam user-policy-create <email-address> --roles AccessViewer --service-name cloud-object-storage
```

  3. Grant them write access to the bucket you created.

```
bx iam user-policy-create nglange@gmail.com --roles AccessEditor --service-name cloud-object-storage --resource-type bucket --resource <bucket-name>
```

Learn more about [the Bluemix CLI in the documentation](docs/cli/reference/bluemix_cli/bx_cli.html).

## Using the API

There isn't a focused command line utility for managing data stored in COS.  As IAM tokens are relatively easy to work with, `curl` is a good choice for basic testing and interaction with your storage.  More information can be found in [the `curl` reference](docs/services/cloud-object-storage/cli/curl.html), as well as [the API reference documentation](docs/services/cloud-object-storage/api-reference/about-compatibility-api.html).
