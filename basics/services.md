---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-07-25"

keywords: cloud services, integration, aspera, key protect, archive, worm

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
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

# Integrated service availability
{: #service-availability}

The table below describes the regions where the following services are supported
* [Aspera high-speed transfer](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera)
* [Key Protect](/docs/services/cloud-object-storage/basics/cloud-object-storage/basics?topic=cloud-object-storage-encryption#sse-kp)
* [Archive Data](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-archive)
* [Immutable Object Storage](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-immutable)
* [Activity Tracker](docs/services/Activity-Tracker-with-LogDNA?topic=logdnaat-getting-started#getting-started)


Downloads using Aspera high-speed will incur additional egress charges. For more information, see the [pricing page](https://www.ibm.com/cloud/object-storage).
{:tip}

## Cross Region
{: #service-availability-geo}

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Aspera</th>
      <th>Key Protect</th>
      <th>Archive Data</th>
      <th>Immutable Object Storage</th>
      <th>Activity Tracker</th>
    </tr>
  </thead>
  <tr>
    <td>AP Cross Region</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>jp-tok</td>
  </tr>
  <tr>
    <td>EU Cross Region</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>eu-de</td>
  </tr>
  <tr>
    <td>US Cross Region</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>us-south</td>
  </tr>
 </table>





## Regional
{: #service-availability-region}

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Aspera</th>
      <th>Key Protect</th>
      <th>Archive Data</th>
      <th>Immutable Object Storage</th>
      <th>Activity Tracker</th>
    </tr>
  </thead>
   <tr>
    <td>AP Australia</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>No</td>
   </tr>
   <tr>
    <td>AP Japan</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>jp-tok</td>
   </tr>
   <tr>
    <td>EU Great Britain</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>eu-gb</td>
   </tr>
   <tr>
    <td>EU Germany</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>eu-de</td>
   </tr>
   <tr>
    <td>US South</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>us-south</td>
   </tr>
   <tr>
    <td>US East</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>us-south</td>
   </tr>
</table>



## Single Data Centers
{: #service-availability-zone}

<table>
  <thead>
    <tr>
      <th>Region</th>
      <th>Aspera</th>
      <th>Key Protect</th>
      <th>Archive Data</th>
      <th>Immutable Object Storage</th>
      <th>Activity Tracker</th>
    </tr>
  </thead>
  <tr>
    <td>Amsterdam, Netherlands</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>eu-de</td>
  </tr>
  <tr>
    <td>Chennai, India</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>jp-tok</td>
  </tr>
  <tr>
    <td>Hong Kong</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>jp-tok</td>
  </tr>
  <tr>
    <td>Melbourne, Australia</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
  </tr>
  <tr>
    <td>Mexico City, Mexico</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>us-south</td>
  </tr>
  <tr>
    <td>Milan, Italy</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>eu-de</td>
  </tr>
  <tr>
    <td>Montréal, Canada</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>us-south</td>
  </tr>
  <tr>
    <td>Oslo, Norway</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>eu-de</td>
  </tr>
  <tr>
    <td>San Jose, USA</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>us-south</td>
  </tr>
  <tr>
    <td>São Paulo, Brazil</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>us-south</td>
  </tr>
  <tr>
    <td>Seoul, South Korea</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>jp-tok</td>
  </tr>
  <tr>
    <td>Toronto, Canada</td>
    <td>Yes</td>
    <td>No</td>
    <td>No</td>
    <td>No</td>
    <td>us-south</td>
  </tr>
</table>

