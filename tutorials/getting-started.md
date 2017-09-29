---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Using the command line

This tutorial is a more in-depth walkthrough of the steps performed in the Getting Started material. It uses the command line tools `bx` and `curl` to create an instance of COS and perform data operations.

## Gather required information

### Install Bluemix CLI Client

Ensure you have install Bluemix CLI Client. Please follow direction to install Bluemix CLI [here](https://console.bluemix.net/docs/cli/reference/bluemix_cli/all_versions.html#bluemix-cli-installer-downloads), and you can read more about using the CLI [in the documentation](https://console.bluemix.net/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_cli).

### Login into Bluemix

First we need to actually login to the cloud platform, either by providing an email and password or by using an API key.

```
$ bx login --apikey <api-key>
```
