---

copyright:
  years: 2018
lastupdated: "2018-08-20"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Aspera high-speed transfer

Aspera high-speed transfer overcomes the limitations of traditional FTP and HTTP transfers to improve data transfer performance under most conditions, especially on networks experiencing high latency and packet loss. Using Aspera high-speed transfer for browser-based uploads and downloads offers the following benefits:

- Faster transfer speeds
- Transfer large object uploads over 200MB.
- Upload entire folders of any type of data including multi-media files, disk images, or any other unstructured data.
- Customize transfer speeds and default preferences.
- Transfers take place in the background instead of in the active browser window.
- Transfers can be viewed, paused/resumed, or cancelled independently.

Aspera high-speed is available only in regions listed in [Integrated services](/docs/services/cloud-object-storage/basics/services.html#service-availability).
{:tip}

## Installing the Aspera high-speed plug-in

When you create a bucket in a [supported region](/docs/services/cloud-object-storage/basics/services.html#service-availability), you have the option to select Aspera high-speed transfer to upload files or folders. Once you attempt to upload an object, you are prompted to install the Aspera Connect client.

### Install Aspera Connect

1. Select **Install Aspera Connect** client.
2. Follow the install instructions depending on your operating system and browser.
3. Resume file or folder upload.

The Aspera Connect plug-in can also be installed from the [Aspera website](http://downloads.asperasoft.com/connect2/) directly. For help troubleshooting issues with the Aspera Connect plug-in, [see the documentation](http://downloads.asperasoft.com/en/documentation/8).

Once the plug-in is installed, you have the option to set Aspera high-speed transfer as the default for any uploads to the target bucket that use the same browser. Select **Remember my browser preferences**. Options are also available in the bucket configuration page under **Transfer options**. These options allow you to choose between Standard and High-speed as the default transport for uploads and downloads.

## Using the console

Typically, using the web-based console is not the most common way to use {{site.data.keyword.cos_short}}. The Standard transfer option limits objects size to 200MB and the file name and key will be identical.  Support for larger object sizes and improved performance (depending on network factors) is provided by Aspera high-speed transfer.

Instead of the standard HTTP `PUT`, Aspera high-speed transfer uploads the object using the [FASP protocol](http://asperasoft.com/technology/transport/fasp/) from [Aspera high-speed transfer](https://www.ibm.com/cloud/high-speed-data-transfer). 
### Transfer status

**Active:** Once you initiate a transfer, the transfer status displays as active. While the transfer is active, you can pause, resume or cancel an active transer. 

**Completed:** Upon completion of your transfer, information about this and all transfers in this session display on the Completed tab. You can clear this information. You will only see information about transfers completed in the current session.

**Preferences:** You can set the default for uploads and/or downloads to High-speed.

Downloads using Aspera high-speed will incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud-computing/bluemix/pricing-object-storage).
{:tip}

**Advanced Preferences:** You can set bandwidth for uploads and downloads.

## Using Libraries and SDKs

The {{site.data.keyword.cos_short}} and Aspera SDK works together to provide the ability to initiate high-speed transfer within your custom applications when using either Java or Python.

The following operations are **supported**:
* File upload/download
* Directory upload/download
* Pause/Resume/Cancel operations

The following item is **not supported**:
* HMAC credentials

### Supported Platforms

|OS|Version|Architecture|Java Version|Python Version|
|---|---|---|---|---|
|Ubuntu|18.04 LTS|64-Bit|6, 8|2.7, 3.6|
|Mac OS X|10.13|64-Bit|6|2.7, 3.6|
|Microsoft&reg; Windows|10|64-Bit|6|2.7, 3.6|

*Limitations for initial release*
* No 32-Bit support for any OS
* No Windows support other than Windows 10
* No Linux support for any distribution other than Ubuntu (tested against the latest LTS)

### Getting the SDK using Java
{: #aspera-sdk-java}

The best way to use {{site.data.keyword.cos_full_notm}} and Aspera Connect Java SDK is to use Maven to manage dependencies. If you aren't familiar with Maven, you get can get up and running using the [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window} guide.

Maven uses a file named `pom.xml` to specify the libraries (and their versions) needed for a Java project.  Below is an example `pom.xml` file for using the {{site.data.keyword.cos_full_notm}} and Aspera Java SDK

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.cos</groupId>
    <artifactId>docs</artifactId>
    <packaging>jar</packaging>
    <version>2.0-SNAPSHOT</version>
    <name>docs</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <dependency>
            <groupId>com.ibm.cos</groupId>
            <artifactId>ibm-cos-java-sdk</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.ibm.cos-aspera</groupId>
            <artifactId>cos-aspera-mac-10.7-64</artifactId>
            <version>0.1.162112</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.8.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.3</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

Additional code is necessary to use the Aspera library within your application.  The shared library will need to be explicitly loaded in your application initialization.

```java
System.load("<path-to-local-folder>/libfaspmanager2.jnilib");
```

#### Aspera/Java code examples

Examples of initiating Aspera transfers with Java are available in [Using Java](/docs/services/cloud-object-storage/libraries/java.html#using-aspera-connect-high-speed-transfer) section.

### Getting the SDK using Python
{: #aspera-sdk-python}

The {{site.data.keyword.cos_full_notm}} and Aspera Connect Python SDK is available from the Python Package Index (PyPI) software repository.  The Aspera SDK is an optional dependency that can be included in the requirements.txt or setup.py.

```json
extras_requires = {
    "aspera": ["ibm-aspera-sdk==1.0.0"]
}
```

Both can be installed using the following commands:

```
pip install ibm-cos-sdk["aspera"]
pip install cos-aspera
```

An additional dependency is also required for **Python 2.7**
```
pip install backports.functools_lru_cache
```

Final step is to add COS Aspera install path to the `PYTHONPATH` environment variable (typically located in your `site-packages` folder, i.e. `~/Library/Python/2.7/lib/python/site-packages`)

```
export PYTHONPATH=$PYTHONPATH:~/Library/Python/2.7/lib/python/site-packages/cos-aspera-mac-10-7-64-py-27
```

To test your installation run the following command and ensure you do not receive any errors:

```
python -c  "import faspmanager2"
```

#### Aspera/Python code examples

Examples of initiating Aspera transfers with Python are available in [Using Python](/docs/services/cloud-object-storage/libraries/python.html#using-aspera-connect-high-speed-transfer) section.