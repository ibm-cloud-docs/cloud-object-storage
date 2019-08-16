---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-04-12"

keywords: aspera, high speed, big data, packet loss

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
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Aspera 고속 전송 사용
{: #aspera}

Aspera 고속 전송은 기존 FTP 및 HTTP 전송의 한계를 넘어 대부분의 조건, 특히 높은 대기 시간과 패킷 손실이 발생하는 네트워크에서 데이터 전송 성능을 향상시킵니다. 표준 HTTP `PUT` 대신 Aspera 고속 전송은 [FASP 프로토콜](https://asperasoft.com/technology/transport/fasp/)을 사용하여 오브젝트를 업로드합니다. 업로드 및 다운로드에 Aspera 고속 전송을 사용하는 경우 다음과 같은 이점이 있습니다.

- 빠른 전송 속도
- 콘솔에서 200MB 넘게 그리고 SDK 또는 라이브러리 사용 시 1GB 넘게 대형 오브젝트 업로드 전송
- 멀티미디어 파일, 디스크 이미지 및 기타 구조화되거나 구조화되지 않은 데이터를 포함하여 모든 데이터 유형의 전체 폴더 업로드
- 전송 속도 및 기본 환경 설정 사용자 정의
- 전송을 개별적으로 보거나 일시정지/재개하거나 취소할 수 있음

Aspera 고속 전송은 {{site.data.keyword.cloud_notm}} [콘솔](#aspera-console)에서 사용 가능하며 [SDK](#aspera-sdk)를 사용하여 프로그래밍 방식으로 사용될 수도 있습니다. 

Aspera 고속 전송은 특정 지역에서만 사용 가능합니다. 세부사항은 [통합 서비스](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)를 참조하십시오.
{:tip}

## 콘솔 사용
{: #aspera-console}

[지원되는 지역](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability)에서 버킷을 작성할 때 파일 또는 폴더를 업로드할 Aspera 고속 전송을 선택할 수 있는 옵션이 있습니다. 오브젝트를 업로드하려고 시도하면 Aspera Connect 클라이언트를 설치하도록 프롬프트가 표시됩니다.

### Aspera Connect 설치
{: #aspera-install}

1. **Aspera Connect 설치** 클라이언트를 선택하십시오.
2. 운영 체제 및 브라우저에 따라 설치 지시사항을 따르십시오.
3. 파일 또는 폴더 업로드를 재개하십시오.

[Aspera 웹 사이트](https://downloads.asperasoft.com/connect2/)에서 직접 Aspera Connect 플러그인을 설치할 수도 있습니다. Aspera Connect 플러그인에 대한 문제점 해결에 도움을 받으려면 [문서를 참조](https://downloads.asperasoft.com/en/documentation/8)하십시오.

플러그인이 설치되면 동일한 브라우저를 사용하는 대상 버킷에 대한 업로드의 기본값으로 Aspera 고속 전송을 설정하는 옵션이 제공됩니다. **내 브라우저 환경 설정 기억**을 선택하십시오. 옵션은 **전송 옵션** 아래 버킷 구성 페이지에서도 사용 가능합니다. 이러한 옵션을 사용하면 업로드 및 다운로드를 위한 기본 전송으로 표준과 고속 중에서 선택할 수 있습니다.

대개 IBM Cloud Object Storage 웹 기반 콘솔 사용은 {{site.data.keyword.cos_short}}를 사용하는 가장 일반적인 방법은 아닙니다. 표준 전송 옵션은 오브젝트 크기를 200MB로 제한하고 파일 이름과 키는 동일합니다. 대규모 오브젝트 크기 및 성능 향상에 대한 지원(네트워크 요인에 따라 다름)은 Aspera 고속 전송을 통해 제공됩니다.

### 전송 상태
{: #aspera-console-transfer-status}

**활성:** 전송을 시작하면 전송 상태가 활성으로 표시됩니다. 전송이 활성이면 활성 전송을 일시정지하거나 재개하거나 취소할 수 있습니다. 

**완료됨:** 전송 완료 시 이에 대한 정보와 이 세션의 모든 전송이 완료됨 탭에 표시됩니다. 이 정보를 지울 수 있습니다. 현재 세션에서 완료된 전송에 대한 정보만 볼 수 있습니다.

**환경 설정:** 업로드 및/또는 다운로드의 기본값을 고속으로 설정할 수 있습니다.

Aspera 고속 전송을 사용한 다운로드에는 Egress 비용이 발생합니다. 자세한 정보는 [가격 페이지](https://www.ibm.com/cloud/object-storage)를 참조하십시오.
{:tip}

**고급 환경 설정:** 업로드 및 다운로드를 위한 대역폭을 설정할 수 있습니다.

----

## 라이브러리 및 SDK 사용
{: #aspera-sdk}

Aspera 고속 전송 SDK는 Java 또는 Python을 사용할 때 사용자 정의 애플리케이션 내에서 고속 전송을 시작할 수 있는 기능을 제공합니다.

### Aspera 고속 전송 사용 시기
{: #aspera-guidance}

Aspera 고속 전송을 사용하는 FASP 프로토콜은 COS의 모든 데이터 전송에는 적합하지 않습니다. 특히 Aspera 고속 전송을 사용하는 전송은 다음을 수행해야 합니다.

1. 항상 다중 세션을 사용하십시오. 둘 이상의 병렬 세션이 Aspera 고속 전송 기능을 가장 잘 활용합니다. [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) 및 [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera)에 대한 구체적 지침을 참조하십시오.
2. Aspera 고속 전송은 대형 파일에 이상적이며, 총 1GB 미만의 데이터가 포함된 모든 파일 또는 디렉토리는 대신 표준 전송 관리자 클래스를 사용하여 다중 파트로 오브젝트를 전송해야 합니다. Aspera 고속 전송에는 일반 HTTP 전송보다 긴 TTFB(Time-To-First-Byte)가 필요합니다. 개별 소형 파일의 전송을 관리하기 위해 많은 Aspera 전송 관리자 오브젝트를 인스턴스화하면 기본 HTTP 요청에 비해 성능이 떨어질 수 있으므로 대신 단일 클라이언트를 인스턴스화하여 소형 파일의 디렉토리를 업로드하는 것이 좋습니다.
3. Aspera 고속 전송은 많은 양의 패킷이 손실되는 네트워크 환경에서 성능을 향상시키기 위해 설계되었으며, 이를 위해 프로토콜의 성능이 장거리 공용 광역 네트워크에 맞도록 설정합니다. Aspera 고속 전송은 지역 또는 데이터 센터 내 전송에 사용하면 안됩니다.

Aspera 고속 전송 SDK는 닫힌 소스이므로 COS SDK(Apache 라이센스 사용)에 대한 선택적 종속 항목입니다.
{:tip}

#### COS/Aspera 고속 전송 패키징
{: #aspera-packaging}

다음 이미지에는 COS SDK가 Aspera 고속 전송 라이브러리와 상호작용하여 기능을 제공하는 방법에 대한 상위 레벨 개요가 표시됩니다.

<img alt="COS/Aspera 고속 전송 SDK." src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="그림 1: COS/Aspera 고속 전송 SDK." caption-side="bottom"} 

### 지원되는 플랫폼
{: #aspera-sdk-platforms}

| OS                     | 버전      | 아키텍처     | 테스트된 Java 버전 | 테스트된 Python 버전 |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64비트       | 6 이상       | 2.7, 3.6       |
| Mac OS X               | 10.13     | 64비트       | 6 이상       | 2.7, 3.6       |
| Microsoft&reg; Windows |10         | 64비트       | 6 이상       | 2.7, 3.6       |

각 Aspera 고속 전송 세션은 전송을 수행하기 위해 클라이언트 시스템에서 실행되는 개별 `ascp` 프로세스를 생성합니다. 자신의 컴퓨팅 환경이 이 프로세스의 실행을 허용하는지 확인하십시오.
{:tip}

**추가 제한사항**

* 32비트 바이너리가 지원되지 않음
* Windows 지원에 Windows 10이 필요함
* Linux 지원이 Ubuntu로 제한됨(18.04 LTS에 대해 테스트됨)
* HMAC 인증 정보가 아닌 IAM API 키를 사용하여 Aspera 전송 관리자 클라이언트를 작성해야 합니다.

### Java를 사용하여 SDK 가져오기
{: #aspera-sdk-java} 
{: java}

{{site.data.keyword.cos_full_notm}} 및 Aspera 고속 전송 Java SDK를 사용하는 가장 좋은 방법은 Maven을 사용하여 종속성을 관리하는 것입니다. Maven에 대해 잘 모르는 경우 [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window} 안내서를 사용하여 시작하고 실행할 수 있습니다.
{: java}

Maven은 `pom.xml` 파일을 사용하여 Java 프로젝트에 필요한 라이브러리(및 해당 버전)를 지정합니다. 다음은 {{site.data.keyword.cos_full_notm}} 및 Aspera 고속 전송 Java SDK 사용에 대한 예제 `pom.xml` 파일입니다.
{: java}

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
            <artifactId>cos-aspera</artifactId>
            <version>0.1.163682</version>
        </dependency>
    </dependencies>
</project>
```
{: codeblock}
{: java}

Java에서 Aspera 고속 전송을 시작하는 예는 [Aspera 고속 전송 사용](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) 절에 있습니다.
{: java}

### Python을 사용하여 SDK 가져오기
{: #aspera-sdk-python} 
{: python}

{{site.data.keyword.cos_full_notm}} 및 Aspera 고속 전송 Python SDK는 PyPI(Python Package Index) 소프트웨어 저장소에서 사용 가능합니다.
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Python에서 Aspera 전송을 시작하는 예는 [Aspera 고속 전송 사용](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera) 절에서 사용 가능합니다.
{: python}
