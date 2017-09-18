# Gather required information

## Install Bluemix CLI Client

Ensure you have install Bluemix CLI Client. Please follow direction to install Bluemix CLI [here](https://console.bluemix.net/docs/cli/reference/bluemix_cli/all_versions.html#bluemix-cli-installer-downloads), and you can read more about using the CLI [in the documentation](https://console.bluemix.net/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_cli).

## Login into Bluemix

First we need to actually login to the cloud platform.  This can be done either by providing an email and password, or by using an API key.

```
$ bx login

API endpoint: https://api.bluemix.net

Email> queen-bee@delightfulhoney.com

Password>
Authenticating...
OK

Select an account (or press enter to skip):
1. User A's Account (b09edf5642ebfad587c594f4d4a354b0)

Enter a number> 1
Targeted account User A's Account (b09edf5642ebfad587c594f4d4a354b0)

API endpoint:   https://api.stage1.ng.bluemix.net (API version: 2.75.0)
Region:         us-south
User:           userA@gmail.com
Account:        User A's Account (b09edf5642ebfad587c594f4d4a354b0)
No org or space targeted, use 'bx target --cf or bx target -o ORG -s SPACE'

Tip: use 'bx cf <command>' to run the Cloud Foundry CLI with Bluemix CLI context.


```

Capture the **Bluemix Account ID** from the output **9b09edf5642ebfad587c594f4d4a354b0.**

## Retrieve Access / IAM token

Each API request requires an HTTP header that includes the 'Authorization’ parameter. The value for the header is the access token - The JSON web token \(JWT\) that you receive when logging into the Bluemix platform. It allows you to use the IBM Bluemix REST API, access services, and resources. Run

```
$bx iam oauth-tokens
IAM token:  Bearer essdssdsdsdss...sdssds
UAA token:  Bearer ey....hYzsdssdsdsdsds
```

to retrieve your access token information.

More information can be found in the [IAM documentation](https://console.bluemix.net/docs/iam/iamuserinv.html\#iamuserinv).


## Get the GUID for your organization

```
$ bx iam orgs --guid
Getting orgs in region 'us-south' as bluemix_ui_integration_test_1@mailinator.com...
Retrieving current account...
OK

GUID                                   Name                  Region     Account owner        Account ID                         Status
7ade3eaa-656a-4206-92c3-5e45708f8df7   RC_integration_test   us-south   auser@gmail.com   9e16d1fed8aa7e1bd73e7a9d23434a5a   active


```
## Next steps
Next, [lets provision an instance](provisioning-cos-service-instance.html) of Cloud Object Storage.
