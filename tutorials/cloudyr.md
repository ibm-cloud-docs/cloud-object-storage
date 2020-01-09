---

copyright:
  years: 2019
lastupdated: "2019-11-14"

keywords: r, tutorial, cloudyr, data science

subcollection: cloud-object-storage

---

{:external: target="_blank" .external}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download} 
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Using cloudyr for data science
{: #cloudyr-data-science}

When you use the [`R` programming language](https://www.r-project.org/about.html){: external} for your projects, get the most out of the features for supporting data science from {{site.data.keyword.cos_full}} by using [cloudyr](https://cloudyr.github.io){: external}.
{: shortdesc}

This tutorial shows you how to integrate data from the {{site.data.keyword.cloud}} Platform within your `R` project. Your project will use {{site.data.keyword.cos_full_notm}} for storage with S3-compatible connectivity in your project.

## Before you begin
{: #cloudyr-prereqs}

We need to make sure that we have the prerequisites before continuing:

  - {{site.data.keyword.cloud_notm}} Platform account
  - An instance of {{site.data.keyword.cos_full_notm}}
  - `R` installed and configured 
  - S3-compatible authentication configuration

### Create HMAC credentials
{: #cloudyr-hmac}

Before we begin, we might need to create a set of [HMAC credentials](/docs/hmac?topic=cloud-object-storage-hmac) as part of a [Service Credential](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) by using the configuration parameter `{"HMAC":true}` when we create credentials. For example, use the {{site.data.keyword.cos_full_notm}} CLI as shown here. 
  
```bash
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{: pre}
  
To store the results of the generated key, append the text, ` > cos_credentials` to the end of the command in the example. For the purposes of this tutorial you need to find the `cos_hmac_keys` heading with child keys, `access_key_id`, and `secret_access_key`.
  
```
      cos_hmac_keys:
          access_key_id:      7xxxxxxxxxxxxxxa6440da12685eee02
          secret_access_key:  8xxxx8ed850cddbece407xxxxxxxxxxxxxx43r2d2586
```
{: screen}

While it is best practices to set credentials in environment variables, you can also set your credentials inside your local copy of your `R` script itself. Environment variables can alternatively be set before you start `R` using an `Renviron.site` or `.Renviron` file, used to set environment variables in `R` during startup.

Ensure you set the actual values for the `access_key_id` and `secret_access_key` in your code along with the {{site.data.keyword.cos_full_notm}} [endpoint](/docs/basics?topic=cloud-object-storage-endpoints) for your instance.
{: note}

## Add credentials to your `R` project
{: #cloudyr-credentials}

As it is beyond the scope of this tutorial, it is assumed you already installed the `R` language and suite of applications. Before you add any libraries or code to your project, ensure that you have credentials available to connect to {{site.data.keyword.cos_full_notm}}. You will need the appropriate [region](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints-region) for your bucket and endpoint.

```R
Sys.setenv("AWS_ACCESS_KEY_ID" = "access_key_id",
           "AWS_SECRET_ACCESS_KEY" = "secret_access_key",
           "AWS_S3_ENDPOINT" = "myendpoint",
           "AWS_DEFAULT_REGION" = "us-geo")
```
{: codeblock}

## Add libraries to your `R` project
{: #cloudyr-s3-library}

We used a `cloudyr` [S3-compatible client](https://github.com/cloudyr/aws.s3){: external} to test our credentials resulting in listing your buckets. To get additional packages, we use the source code collective known as [CRAN](https://cran.r-project.org/){: external} that operates through a series of [mirrors](https://cran.r-project.org/mirmon_report.html){: external}.

For this example, we use [aws.s3](https://cran.r-project.org/package=aws.s3){: external} as shown in the example and added to the code to set or access your credentials.

```R
library("aws.s3")
bucketlist()
```
{: codeblock}

### Use library methods in your `R` project
{: #cloudyr-s3-library-methods}

You can learn a lot from working with sample packages. For example, the package for [Cosmic Microwave Background Data Analysis](https://github.com/frycast/rcosmo){: external} presents a conundrum. The executable of the project for local compiling are small enough to work on one's personal machine, but working with the source data would be constrained due to the size of the data.

In addition to PUT, HEAD, and other compatible API commands, we can GET objects as shown with the S3-compatible client we included earlier. 
 
```R
# return object using 'S3 URI' syntax, with progress bar
get_object("s3://mybucket.myendpoint/example.csv", show_progress = TRUE)
```
{: codeblock}

## Add data to your `R` project
{: #cloudyr-add-data}

As you can guess, the library discussed earlier has a `save_object()` method that can write directly to your bucket. While there are many ways to [load data](https://cran.r-project.org/doc/manuals/r-release/R-intro.html#Loading-data-from-other-R-packages){: external}, we can use [cloudSimplifieR](https://cran.r-project.org/package=cloudSimplifieR){: external} to work with an [open data set](https://developer.ibm.com/clouddataservices/category/open-data/){: external}.

```R
library(cloudSimplifieR)
d <- as.data.frame(csvToDataframe("s3://myendpoint/mybucket/example.csv"))
plot(d)
```
{: codeblock}

## Next steps
{: #cloudyr-next-steps}

In addition to creating your own projects, you can also use [R Studio to analyze data](https://developer.ibm.com/clouddataservices/docs/ibm-data-science-experience/integrate/analyze-data-using-rstudio/){: external}.