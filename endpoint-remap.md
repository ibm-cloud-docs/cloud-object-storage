---

copyright:
  years: 2017, 2024
lastupdated: "2024-04-17"

keywords: endpoint, location, object storage, IAM, cross region, border gateway protocol

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}

# Legacy endpoints
{: #remap-endpoints}

Over time the endpoint URLs used to access {{site.data.keyword.cos_short}} have changed, and older applications and workflows should be updated to use the newer addresses.
{: shortdesc}

## Regional Endpoints
{: #remap-endpoints-region}

Buckets that are created at a regional endpoint distribute data across three data centers that are spread across a metro area. Any one of these data centers can suffer an outage or even destruction without impacting availability.

| Region            | Type   | Legacy Endpoint                           | New Endpoint                                       |
|-------------------|--------|-------------------------------------------|----------------------------------------------------|
| US South          | Public | `s3.us-south.objectstorage.softlayer.net` | `s3.us-south.cloud-object-storage.appdomain.cloud` |
| US East           | Public | `s3.us-east.objectstorage.softlayer.net`  | `s3.us-east.cloud-object-storage.appdomain.cloud`  |
| EU United Kingdom | Public | `s3.eu-gb.objectstorage.softlayer.net`    | `s3.eu-gb.cloud-object-storage.appdomain.cloud`    |
| EU Germany        | Public | `s3.eu-de.objectstorage.softlayer.net`    | `s3.eu-de.cloud-object-storage.appdomain.cloud`    |
| AP Australia      | Public | `s3.au-syd.objectstorage.softlayer.net`   | `s3.au-syd.cloud-object-storage.appdomain.cloud`   |
| AP Japan          | Public | `s3.jp-tok.objectstorage.softlayer.net`   | `s3.jp-tok.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Regional Endpoints" caption-side="top"}
{: #regionalendpointtable1}
{: tab-title="Public"}
{: tab-group="Regional-endpoints"}

| Region            | Type    | Legacy Endpoint                                      | New Endpoint                                               |
|-------------------|---------|------------------------------------------------------|------------------------------------------------------------|
| US South          | Private | `s3.us-south.objectstorage.service.networklayer.com` | `s3.private.us-south.cloud-object-storage.appdomain.cloud` |
| US East           | Private | `s3.us-east.objectstorage.service.networklayer.com`  | `s3.private.us-east.cloud-object-storage.appdomain.cloud`  |
| EU United Kingdom | Private | `s3.eu-gb.objectstorage.service.networklayer.com`    | `s3.private.eu-gb.cloud-object-storage.appdomain.cloud`    |
| EU Germany        | Private | `s3.eu-de.objectstorage.service.networklayer.com`    | `s3.private.eu-de.cloud-object-storage.appdomain.cloud`    |
| AP Australia      | Private | `s3.au-syd.objectstorage.service.networklayer.com`   | `s3.private.au-syd.cloud-object-storage.appdomain.cloud`   |
| AP Japan          | Private | `s3.jp-tok.objectstorage.service.networklayer.com`   | `s3.private.jp-tok.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Regional Endpoints" caption-side="top"}
{: #regionalendpointtable2}
{: tab-title="Private"}
{: tab-group="Regional-endpoints"}

| Region            | Type   | Legacy Endpoint                                  | New Endpoint                                              |
|-------------------|--------|--------------------------------------------------|-----------------------------------------------------------|
| US South          | Direct | `s3.us-south.objectstorage.adn.networklayer.com` | `s3.direct.us-south.cloud-object-storage.appdomain.cloud` |
| US East           | Direct | `s3.us-east.objectstorage.adn.networklayer.com`  | `s3.direct.us-east.cloud-object-storage.appdomain.cloud`  |
| EU United Kingdom | Direct | `s3.eu-gb.objectstorage.adn.networklayer.com`    | `s3.direct.eu-gb.cloud-object-storage.appdomain.cloud`    |
| EU Germany        | Direct | `s3.eu-de.objectstorage.adn.networklayer.com`    | `s3.direct.eu-de.cloud-object-storage.appdomain.cloud`    |
| AP Australia      | Direct | `s3.au-syd.objectstorage.adn.networklayer.com`   | `s3.direct.au-syd.cloud-object-storage.appdomain.cloud`   |
| AP Japan          | Direct | `s3.jp-tok.objectstorage.adn.networklayer.com`   | `s3.direct.jp-tok.cloud-object-storage.appdomain.cloud`   |
{: class="simple-tab-table"}
{: caption="Regional Endpoints" caption-side="top"}
{: #regionalendpointtable3}
{: tab-title="Direct"}
{: tab-group="Regional-endpoints"}


## Cross Region Endpoints
{: #remap-endpoints-geo}

Buckets that are created at a cross region endpoint distribute data across three regions. Any one of these regions can suffer an outage or even destruction without impacting availability. Requests are routed to the nearest Cross Region metropolitan area by using Border Gateway Protocol (BGP) routing. In an outage, requests are automatically rerouted to an active region. Advanced users who want to write their own failover logic can do so by sending requests to a [tethered endpoint](https://test.cloud.ibm.com/docs/cloud-object-storage?topic=cloud-object-storage-advanced-endpoints) and bypassing the BGP routing.

| Region          | Type   | Legacy Endpoint                             | New Endpoint                                 |
|-----------------|--------|---------------------------------------------|----------------------------------------------|
| US Cross Region | Public | `s3-api.us-geo.objectstorage.softlayer.net` | `s3.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | Public | `s3.eu-geo.objectstorage.softlayer.net`     | `s3.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | Public | `s3.ap-geo.objectstorage.softlayer.net`     | `s3.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Endpoints" caption-side="top"}
{: #crossregionalendpointtable1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints"}

| Region          | Type    | Legacy Endpoint                                        | New Endpoint                                         |
|-----------------|---------|--------------------------------------------------------|------------------------------------------------------|
| US Cross Region | Private | `s3-api.us-geo.objectstorage.service.networklayer.com` | `s3.private.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | Private | `s3.eu-geo.objectstorage.service.networklayer.com`     | `s3.private.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | Private | `s3.ap-geo.objectstorage.service.networklayer.com`     | `s3.private.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Endpoints" caption-side="top"}
{: #crossregionalendpointtable2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints"}

| Region          | Type   | Legacy Endpoint                                    | New Endpoint                                        |
|-----------------|--------|----------------------------------------------------|-----------------------------------------------------|
| US Cross Region | Direct | `s3-api.us-geo.objectstorage.adn.networklayer.com` | `s3.direct.us.cloud-object-storage.appdomain.cloud` |
| EU Cross Region | Direct | `s3.eu-geo.objectstorage.adn.networklayer.com`     | `s3.direct.eu.cloud-object-storage.appdomain.cloud` |
| AP Cross Region | Direct | `s3.ap-geo.objectstorage.adn.networklayer.com`     | `s3.direct.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Cross Region Endpoints" caption-side="top"}
{: #crossregionalendpointtable3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints"}

### Tethered endpoints
{: #remap-endpoint-reference}

| Region               | Type              | Legacy Endpoint                                 | New Endpoint                                     |
|----------------------|-------------------|-------------------------------------------------|--------------------------------------------------|
| US: Dallas           | Public (Tethered) | `s3-api.dal-us-geo.objectstorage.softlayer.net` | `s3.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Public (Tethered) | `s3-api.sjc-us-geo.objectstorage.softlayer.net` | `s3.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Public (Tethered) | `s3-api.wdc-us-geo.objectstorage.softlayer.net` | `s3.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Public (Tethered) | `s3.ams-eu-geo.objectstorage.softlayer.net`     | `s3.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Public (Tethered) | `s3.fra-eu-geo.objectstorage.softlayer.net`     | `s3.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Public (Tethered) | `s3.mil-eu-geo.objectstorage.softlayer.net`     | `s3.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Public (Tethered) | `s3.tok-ap-geo.objectstorage.softlayer.net`     | `s3.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Sydney           | Public (Tethered) | `s3.syd-ap-geo.objectstorage.softlayer.net`     | `s3.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka            | Public (Tethered) | `s3.osa-ap-geo.objectstorage.softlayer.net`     | `s3.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2a. Cross Region Endpoints (Tethered)" caption-side="top"}
{: #tether1}
{: tab-title="Public"}
{: tab-group="Cross-regional-endpoints-tether"}

| Region               | Type               | Legacy Endpoint                                            | New Endpoint                                             |
|----------------------|--------------------|------------------------------------------------------------|----------------------------------------------------------|
| US: Dallas           | Private (Tethered) | `s3-api.dal-us-geo.objectstorage.service.networklayer.com` | `s3.private.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Private (Tethered) | `s3-api.sjc-us-geo.objectstorage.service.networklayer.com` | `s3.private.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Private (Tethered) | `s3-api.wdc-us-geo.objectstorage.service.networklayer.com` | `s3.private.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Private (Tethered) | `s3.ams-eu-geo.objectstorage.service.networklayer.com`     | `s3.private.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Private (Tethered) | `s3.fra-eu-geo.objectstorage.service.networklayer.com`     | `s3.private.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Private (Tethered) | `s3.mil-eu-geo.objectstorage.service.networklayer.com`     | `s3.private.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Private (Tethered) | `s3.tok-ap-geo.objectstorage.service.networklayer.com`     | `s3.private.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Sydney            | Private (Tethered) | `s3.syd-ap-geo.objectstorage.service.networklayer.com`     | `s3.private.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka        | Private (Tethered) | `s3.osa-ap-geo.objectstorage.service.networklayer.com`     | `s3.private.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2a. Cross Region Endpoints (Tethered)" caption-side="top"}
{: #tether2}
{: tab-title="Private"}
{: tab-group="Cross-regional-endpoints-tether"}

| Region               | Type              | Legacy Endpoint                                        | New Endpoint                                            |
|----------------------|-------------------|--------------------------------------------------------|---------------------------------------------------------|
| US: Dallas           | Direct (Tethered) | `s3-api.dal-us-geo.objectstorage.adn.networklayer.com` | `s3.direct.dal.us.cloud-object-storage.appdomain.cloud` |
| US: San Jose         | Direct (Tethered) | `s3-api.sjc-us-geo.objectstorage.adn.networklayer.com` | `s3.direct.sjc.us.cloud-object-storage.appdomain.cloud` |
| US: Washington, D.C. | Direct (Tethered) | `s3-api.wdc-us-geo.objectstorage.adn.networklayer.com` | `s3.direct.wdc.us.cloud-object-storage.appdomain.cloud` |
| EU: Amsterdam        | Direct (Tethered) | `s3.ams-eu-geo.objectstorage.adn.networklayer.com`     | `s3.direct.ams.eu.cloud-object-storage.appdomain.cloud` |
| EU: Frankfurt        | Direct (Tethered) | `s3.fra-eu-geo.objectstorage.adn.networklayer.com`     | `s3.direct.fra.eu.cloud-object-storage.appdomain.cloud` |
| EU: Milan            | Direct (Tethered) | `s3.mil-eu-geo.objectstorage.adn.networklayer.com`     | `s3.direct.mil.eu.cloud-object-storage.appdomain.cloud` |
| AP: Tokyo            | Direct (Tethered) | `s3.tok-ap-geo.objectstorage.adn.networklayer.com`     | `s3.direct.tok.ap.cloud-object-storage.appdomain.cloud` |
| AP: Sydney           | Direct (Tethered) | `s3.syd-ap-geo.objectstorage.adn.networklayer.com`     | `s3.direct.syd.ap.cloud-object-storage.appdomain.cloud` |
| AP: Osaka            | Direct (Tethered) | `s3.osa-ap-geo.objectstorage.adn.networklayer.com`     | `s3.direct.osa.ap.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Table 2a. Cross Region Endpoints (Tethered)" caption-side="top"}
{: #tether3}
{: tab-title="Direct"}
{: tab-group="Cross-regional-endpoints-tether"}

## Single Data Center Endpoints
{: #remap-endpoints-zone}

Single data centers are not co-located with IBM Cloud services, such as IAM or Key Protect, and offer no resiliency in a site outage or destruction.

If a networking failure results in a partition where the data center is unable to access IAM, authentication and authorization information is read from a cache that might become stale. This cached data might result in a lack of enforcement of new or altered IAM policies for up to 24 hours.
{: important}

| Region                      | Type   | Legacy Endpoint                        | New Endpoint                                    |
|-----------------------------|--------|----------------------------------------|-------------------------------------------------|
| Amsterdam, Netherlands      | Public | `s3.ams03.objectstorage.softlayer.net` | `s3.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India              | Public | `s3.che01.objectstorage.softlayer.net` | `s3.che01.cloud-object-storage.appdomain.cloud` |
| Milan, Italy                | Public | none                                   | `s3.mil01.cloud-object-storage.appdomain.cloud` |
| Montr&egrave;al, Canada     | Public | `s3.mon01.objectstorage.softlayer.net` | `s3.mon01.cloud-object-storage.appdomain.cloud` |
| Paris, France               | Public | `s3.par01.objectstorage.softlayer.net` | `s3.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US                | Public | none                                   | `s3.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil    | Public | `s3.sao01.objectstorage.softlayer.net` | `s3.sao01.cloud-object-storage.appdomain.cloud` |
| Singapore                   | Public | none                                   | `s3.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Single Data Center Endpoints" caption-side="top"}
{: #sdcendpointtable1}
{: tab-title="Public"}
{: tab-group="single-datacenter-endpoints"}

| Region                      | Type    | Legacy Endpoint                                   | New Endpoint                                            |
|-----------------------------|---------|---------------------------------------------------|---------------------------------------------------------|
| Amsterdam, Netherlands      | Private | `s3.ams03.objectstorage.service.networklayer.com` | `s3.private.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India              | Private | `s3.che01.objectstorage.service.networklayer.com` | `s3.private.che01.cloud-object-storage.appdomain.cloud` |
| Milan, Italy                | Private | none                                              | `s3.private.mil01.cloud-object-storage.appdomain.cloud` |
| Montr&egrave;al, Canada     | Private | `s3.mon01.objectstorage.service.networklayer.com` | `s3.private.mon01.cloud-object-storage.appdomain.cloud` |
| Paris, France               | Private | `s3.par01.objectstorage.service.networklayer.com` | `s3.private.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US                | Private | none                                              | `s3.private.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil    | Private | `s3.sao01.objectstorage.service.networklayer.com` | `s3.private.sao01.cloud-object-storage.appdomain.cloud` |
| Singapore                   | Private | none                                              | `s3.private.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Single Data Center Endpoints" caption-side="top"}
{: #sdcendpointtable2}
{: tab-title="Private"}
{: tab-group="single-datacenter-endpoints"}

| Region                      | Type   | Legacy Endpoint                               | New Endpoint                                           |
|-----------------------------|--------|-----------------------------------------------|--------------------------------------------------------|
| Amsterdam, Netherlands      | Direct | `s3.ams03.objectstorage.adn.networklayer.com` | `s3.direct.ams03.cloud-object-storage.appdomain.cloud` |
| Chennai, India              | Direct | `s3.che01.objectstorage.adn.networklayer.com` | `s3.direct.che01.cloud-object-storage.appdomain.cloud` |
| Milan, Italy                | Direct | none                                          | `s3.direct.mil01.cloud-object-storage.appdomain.cloud` |
| Montr&egrave;al, Canada     | Direct | `s3.mon01.objectstorage.adn.networklayer.com` | `s3.direct.mon01.cloud-object-storage.appdomain.cloud` |
| Paris, France               | Direct | `s3.par01.objectstorage.adn.networklayer.com` | `s3.direct.par01.cloud-object-storage.appdomain.cloud` |
| San Jose, US                | Direct | none                                          | `s3.direct.sjc04.cloud-object-storage.appdomain.cloud` |
| S&atilde;o Paulo, Brazil    | Direct | `s3.sao01.objectstorage.adn.networklayer.com` | `s3.direct.sao01.cloud-object-storage.appdomain.cloud` |
| Singapore                   | Direct | none                                          | `s3.direct.sng01.cloud-object-storage.appdomain.cloud` |
{: class="simple-tab-table"}
{: caption="Single Data Center Endpoints" caption-side="top"}
{: #sdcendpointtable3}
{: tab-title="Direct"}
{: tab-group="single-datacenter-endpoints"}
