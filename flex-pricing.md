---

copyright:
  years: 2020, 2023
lastupdated: "2023-05-26"

keywords: flex, legacy

subcollection: cloud-object-storage


---
{:external: target="_blank" .external}
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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Flex storage class pricing
{: #flex-pricing}


| Storage used  | US South | US East | EU United Kingdom | EU Germany | AP Australia | AP Japan | S&atilde;o Paulo, Brazil | Toronto, Canada |
|---------------|----------|---------|-------------------|------------|--------------|----------|--------------------------|-----------------|
| 0 - 499.99 TB | $0.009   | $0.009  | $0.0096           | $0.0099    | $0.0108      | $0.0102  | $0.0108                  | $0.0093         |
| 500+ TB       | $0.009   | $0.009  | $0.0096           | $0.0099    | $0.0108      | $0.0102  | $0.0108                  | $0.0093         |
{: class="simple-tab-table"}
{: caption="Storage Capacity (GB/month)" caption-side="top"}
{: #storage1}
{: tab-title="Regional"}
{: tab-group="storage-capacity"}

| Storage used  | US Cross Region | EU Cross Region | AP Cross Region |
|---------------|-----------------|-----------------|-----------------|
| 0 - 499.99 TB | $0.014          | $0.0148         | $0.0158         |
| 500+ TB       | $0.014          | $0.0148         | $0.0158         |
{: class="simple-tab-table"}
{: caption="Storage Capacity (GB/month)" caption-side="top"}
{: #storage2}
{: tab-title="Cross Region"}
{: tab-group="storage-capacity"}

| Storage used  | Amsterdam, Netherlands | Chennai, India | Melbourne, Australia | Milan, Italy | Montr&egrave;al, Canada | Paris, France | San Jose, US | Singapore |
|---------------|------------------------|----------------|----------------------|--------------|-------------------------|---------------|--------------|-----------|
| 0 - 499.99 TB | $0.0093                | $0.0108        | $0.0108              | $0.0099      | $0.0093                 | $0.0099       | $0.009       | $0.0102   |
| 500+ TB       | $0.0093                | $0.0108        | $0.0108              | $0.0099      | $0.0093                 | $0.0099       | $0.009       | $0.0102   |
{: class="simple-tab-table"}
{: caption="Storage Capacity (GB/month)" caption-side="top"}
{: #storage3}
{: tab-title="Single Data Center"}
{: tab-group="storage-capacity"}

| Bandwidth used      | US South   | US East    | EU United Kingdom | EU Germany | AP Australia | AP Japan   | S&atilde;o Paulo, Brazil | Toronto, Canada |
|---------------------|------------|------------|-------------------|------------|--------------|------------|--------------------------|-----------------|
| 0 - 50 TB           | $0.09      | $0.09      | $0.09             | $0.09      | $0.14        | $0.14      | $0.18                    | $0.09           |
| Next 100 TB         | $0.07      | $0.07      | $0.07             | $0.07      | $0.11        | $0.11      | $0.14                    | $0.07           |
| Next 350 TB         | $0.05      | $0.05      | $0.05             | $0.05      | $0.08        | $0.08      | $0.10                    | $0.05           |
| Greater than 500 TB | Contact us | Contact us | Contact us        | Contact us | Contact us   | Contact us | Contact us               | Contact us      |
{: class="simple-tab-table"}
{: caption="Public outbound bandwidth (GB/month)" caption-side="top"}
{: #bandwidth1}
{: tab-title="Regional"}
{: tab-group="public-outbound-bandwidth"}

| Bandwidth used      | US Cross Region | EU Cross Region | AP Cross Region |
|---------------------|-----------------|-----------------|-----------------|
| 0 - 50 TB           | $0.09           | $0.09           | $0.14           |
| Next 100 TB         | $0.07           | $0.07           | $0.11           |
| Next 350 TB         | $0.05           | $0.05           | $0.08           |
| Greater than 500 TB | Contact us      | Contact us      | Contact us      |
{: class="simple-tab-table"}
{: caption="Public outbound bandwidth (GB/month)" caption-side="top"}
{: #bandwidth2}
{: tab-title="Cross Region"}
{: tab-group="public-outbound-bandwidth"}

| Bandwidth used      | Amsterdam, Netherlands | Chennai, India | Melbourne, Australia | Milan, Italy | Montr&egrave;al, Canada | Paris, France | San Jose, US | Singapore  |
|---------------------|------------------------|----------------|----------------------|--------------|-------------------------|---------------|--------------|------------|
| 0 - 50 TB           | $0.09                  | $0.18          | $0.14                | $0.12        | $0.09                   | $0.12         | $0.09        | $0.12      |
| Next 100 TB         | $0.07                  | $0.14          | $0.11                | $0.09        | $0.07                   | $0.09         | $0.07        | $0.09      |
| Next 350 TB         | $0.05                  | $0.10          | $0.08                | $0.07        | $0.05                   | $0.07         | $0.05        | $0.07      |
| Greater than 500 TB | Contact us             | Contact us     | Contact us           | Contact us   | Contact us              | Contact us    | Contact us   | Contact us |
{: class="simple-tab-table"}
{: caption="Public outbound bandwidth (GB/month)" caption-side="top"}
{: #bandwidth3}
{: tab-title="Single Data Center"}
{: tab-group="public-outbound-bandwidth"}

| Request type                                  | US South  | US East   | EU United Kingdom | EU Germany | AP Australia | AP Japan  | S&atilde;o Paulo, Brazil | Toronto, Canada |
|-----------------------------------------------|-----------|-----------|-------------------|------------|--------------|-----------|--------------------------|-----------------|
| Class A: PUT, COPY, POST and LIST (per 1,000) | $0.01     | $0.01     | $0.01             | $0.01      | $0.01        | $0.01     | $0.01                    | $0.01           |
| Class B: GET and all others (per 10,000)      | $0.01     | $0.01     | $0.01             | $0.01      | $0.01        | $0.01     | $0.01                    | $0.01           |
| Delete requests                               | No charge | No charge | No charge         | No charge  | No charge    | No charge | No charge                | No charge       |
| Data retrieval (per GB)                       | $0.029    | $0.029    | $0.029            | $0.029     | $0.029       | $0.029    | $0.029                   | $0.029          |
{: class="simple-tab-table"}
{: caption="Operational Requests" caption-side="top"}
{: #requests1}
{: tab-title="Regional"}
{: tab-group="operational-requests"}

| Request type                                  | US Cross Region | EU Cross Region | AP Cross Region |
|-----------------------------------------------|-----------------|-----------------|-----------------|
| Class A: PUT, COPY, POST and LIST (per 1,000) | $0.01           | $0.01           | $0.01           |
| Class B: GET and all others (per 10,000)      | $0.01           | $0.01           | $0.01           |
| Delete requests                               | No charge       | No charge       | No charge       |
| Data retrieval (per GB)                       | $0.029          | $0.029          | $0.029          |
{: class="simple-tab-table"}
{: caption="Operational Requests" caption-side="top"}
{: #requests2}
{: tab-title="Cross Region"}
{: tab-group="operational-requests"}

| Request type                                  | Amsterdam, Netherlands | Chennai, India | Melbourne, Australia | Milan, Italy | Montr&egrave;al, Canada | Paris, France | San Jose, US | Singapore |
|-----------------------------------------------|------------------------|----------------|----------------------|--------------|-------------------------|---------------|--------------|-----------|
| Class A: PUT, COPY, POST and LIST (per 1,000) | $0.01                  | $0.01          | $0.01                | $0.01        | $0.01                   | $0.01         | $0.01        | $0.01     |
| Class B: GET and all others (per 10,000)      | $0.01                  | $0.01          | $0.01                | $0.01        | $0.01                   | $0.01         | $0.01        | $0.01     |
| Delete requests                               | No charge              | No charge      | No charge            | No charge    | No charge               | No charge     | No charge    | No charge |
| Data retrieval (per GB)                       | $0.029                 | $0.029         | $0.029               | $0.029       | $0.029                  | $0.029        | $0.029       | $0.029    |
{: class="simple-tab-table"}
{: caption="Operational Requests" caption-side="top"}
{: #requests3}
{: tab-title="Single Data Center"}
{: tab-group="operational-requests"}

| Flex cap                      | US South | US East | EU United Kingdom | EU Germany | AP Australia | AP Japan | S&atilde;o Paulo, Brazil | Toronto, Canada |
|-------------------------------|----------|---------|-------------------|------------|--------------|----------|--------------------------|-----------------|
| Total GB stored and retrieved | $0.029   | $0.029  | $0.0296           | $0.0299    | $0.0308      | $0.0302  | $0.0308                  | $0.0293         |
{: class="simple-tab-table"}
{: caption="Flex charge model for combined (storage capacity and data retrieval) is calculated using the lowest value of (A) storage capacity charge + data retrieval charge, or (B) capacity x Flex cap charge." caption-side="top"}
{: #cap1}
{: tab-title="Regional"}
{: tab-group="flex-cap"}

| Request type                  | US Cross Region | EU Cross Region | AP Cross Region |
|-------------------------------|-----------------|-----------------|-----------------|
| Total GB stored and retrieved | $0.034         | $0.0348         | $0.0358         |
{: class="simple-tab-table"}
{: caption="Flex charge model for combined (storage capacity and data retrieval) is calculated using the lowest value of (A) storage capacity charge + data retrieval charge, or (B) capacity x Flex cap charge." caption-side="top"}
{: #cap2}
{: tab-title="Cross Region"}
{: tab-group="flex-cap"}

| Request type                  | Amsterdam, Netherlands | Chennai, India | Melbourne, Australia | Milan, Italy | Montr&egrave;al, Canada | Paris, France | San Jose, US | Singapore |
|-------------------------------|------------------------|----------------|----------------------|--------------|-------------------------|---------------|--------------|-----------|
| Total GB stored and retrieved | $0.0293                | $0.0308        | $0.0308              | $0.0102      | $0.0299                 | $0.0299       | $0.0290      | $0.0302   |
{: class="simple-tab-table"}
{: caption="Flex charge model for combined (storage capacity and data retrieval) is calculated using the lowest value of (A) storage capacity charge + data retrieval charge, or (B) capacity x Flex cap charge." caption-side="top"}
{: #cap3}
{: tab-title="Single Data Center"}
{: tab-group="flex-cap"}

| Aspera HST egress   | US South   | US East    | EU United Kingdom | EU Germany | AP Australia | AP Japan   | S&atilde;o Paulo, Brazil | Toronto, Canada |
|---------------------|------------|------------|-------------------|------------|--------------|------------|--------------------------|-----------------|
| 0 - 50 TB           | $0.08      | $0.08      | $0.08             | $0.08      | $0.08        | $0.08      | $0.08                    | $0.08           |
| Next 100 TB         | $0.06      | $0.06      | $0.06             | $0.06      | $0.06        | $0.06      | $0.06                    | $0.06           |
| Next 350 TB         | $0.04      | $0.04      | $0.04             | $0.04      | $0.05        | $0.04      | $0.04                    | $0.04           |
| Greater than 500 TB | Contact us | Contact us | Contact us        | Contact us | Contact us   | Contact us | Contact us               | Contact us      |
{: class="simple-tab-table"}
{: caption="Aspera High-Speed Transfer outbound bandwidth (GB/month)" caption-side="top"}
{: #aspera1}
{: tab-title="Regional"}
{: tab-group="aspera-outbound-bandwidth"}

| Aspera HST egress   | US Cross Region | EU Cross Region | AP Cross Region |
|---------------------|-----------------|-----------------|-----------------|
| 0 - 50 TB           | $0.08           | $0.08           | $0.08           |
| Next 100 TB         | $0.06           | $0.06           | $0.06           |
| Next 350 TB         | $0.04           | $0.04           | $0.04           |
| Greater than 500 TB | Contact us      | Contact us      | Contact us      |
{: class="simple-tab-table"}
{: caption="Aspera High-Speed Transfer outbound bandwidth (GB/month)" caption-side="top"}
{: #aspera2}
{: tab-title="Cross Region"}
{: tab-group="aspera-outbound-bandwidth"}

| Aspera HST egress   | Amsterdam, Netherlands | Chennai, India | Melbourne, Australia | Milan, Italy | Montr&egrave;al, Canada | Paris, France | San Jose, US | Singapore  |
|---------------------|------------------------|----------------|----------------------|--------------|-------------------------|---------------|--------------|------------|
| 0 - 50 TB           | $0.08                  | $0.08          | $0.08                | $0.08        | $0.08                   | $0.08         | $0.08        | $0.08      |
| Next 100 TB         | $0.06                  | $0.06          | $0.06                | $0.06        | $0.06                   | $0.06         | $0.06        | $0.06      |
| Next 350 TB         | $0.04                  | $0.04          | $0.04                | $0.05        | $0.04                   | $0.05         | $0.05        | $0.05      |
| Greater than 500 TB | Contact us             | Contact us     | Contact us           | Contact us   | Contact us              | Contact us    | Contact us   | Contact us |
{: class="simple-tab-table"}
{: caption="Aspera High-Speed Transfer outbound bandwidth (GB/month)" caption-side="top"}
{: #aspera3}
{: tab-title="Single Data Center"}
{: tab-group="aspera-outbound-bandwidth"}
