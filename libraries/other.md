---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Using compatible SDKs

{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'}
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

## Context Switch Testing

This is is a test for the context switcher.  Let's see if this works...


This is the code for `getBucketContents`
{: javascript}

```javascript
// Retrieve the list of contents for a bucket
function getBucketContents(bucket) {
    console.log(`Retrieving bucket contents from: ${bucket}`);
    return cos.listObjects(
        {Bucket: bucket},
    ).promise()
    .then((data) => {
        if (data != null && data.Contents != null) {
            for (var i = 0; i < data.Contents.length; i++) {
                var itemKey = data.Contents[i].Key;
                var itemSize = data.Contents[i].Size;
                console.log(`Item: ${itemKey} (${itemSize} bytes).`)
            }
            logDone();
        }    
    })
    .catch(logError);
}
```
{: codeblock}
{: javascript}


This is the code for `getBucketContents`
{: python}

```python
# Retrieve the list of contents for a bucket
def get_bucket_contents(bucket):
    print("Retrieving bucket contents from: {0}".format(bucket))
    try:
        files = cos.Bucket(bucket).objects.all()
        for file in files:
            print("Item: {0} ({1} bytes).".format(file.key, file.size))

        log_done()
    except ClientError as be:
        log_client_error(be)
    except Exception as e:
        log_error("Unable to retrieve bucket contents: {0}".format(e))
```
{: codeblock}
{: python}


This is the code for `getBucketContents` (Java)
{: java}

```java
// Retrieve the list of objects in the bucket
public static void getBucketContents(String bucket) {
    System.out.printf("Retrieving bucket contents from: %s\n", bucket);

    ObjectListing objectListing = _cos.listObjects(new ListObjectsRequest().withBucketName(bucket));
    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        System.out.printf("Item: %s (%s bytes)\n", objectSummary.getKey(), objectSummary.getSize());
    }
}
```
{: codeblock}
{: java}
