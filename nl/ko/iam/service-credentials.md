---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, iam, basics, credentials

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

# 서비스 인증 정보
{: #service-credentials}

서비스 인증 정보는 애플리케이션을 {{site.data.keyword.cos_short}}에 연결하는 데 필요한, JSON 문서에 패키지된 정보를 제공합니다. 서비스 인증 정보는 항상 서비스 ID와 연관되며, 새 서비스 ID는 새 인증 정보와 함께 작성할 수 있습니다.  

서비스 인증 정보를 작성하려면 다음 단계를 사용하십시오. 
1. {{site.data.keyword.cloud_notm}} 콘솔에 로그인하여 {{site.data.keyword.cos_short}}의 인스턴스로 이동하십시오. 
2. 측면 탐색에서 **서비스 인증 정보**를 클릭하십시오.  
3. **새 인증 정보**를 클릭하고 필요한 정보를 제공하십시오.
  HMAC 인증 정보를 생성하려는 경우에는 'HMAC 인증 정보 포함' 선택란을 클릭하십시오. 
  
4. **추가**를 클릭하여 서비스 인증 정보를 생성하십시오. 

이 인증 정보는 다음 값을 포함합니다. 

필드 이름|값
--|--
`apikey`  |  서비스 ID에 대해 작성된 새 API 키
`cos_hmac_keys`  |  S3 호환 가능 도구 및 라이브러리와 함께 사용할 액세스 키/비밀 키 쌍
`endpoints`  |  사용 가능한 엔드포인트의 JSON 표현에 대한 링크
`iam_apikey_description`  |  API 키 설명 - 처음에는 자동 생성되지만 편집 가능함
`iam_apikey_name`  |  API 키 이름 - 처음에는 자동 생성되지만 편집 가능함
`iam_role_crn`  |  지정된 역할의 고유 ID
`iam_serviceid_crn`  |  서비스 ID의 고유 ID
`resource_instance_id`  |  인증 정보가 액세스하는 {{site.data.keyword.cos_short}} 인스턴스의 고유 ID입니다. 이는 서비스 인증 정보라고도 합니다. 

서비스 인증 정보의 예는 다음과 같습니다. 

```json
{
  "apikey": "0viPHOY7LbLNa9eLftrtHPpTjoGv6hbLD1QalRXikliJ",
  "cos_hmac_keys": {
      "access_key_id": "347aa3a4b34344f8bc7c7cccdf856e4c",
      "secret_access_key": "gvurfb82712ad14e7a7915h763a6i87155d30a1234364f61"
  },
  "endpoints": "https://control.cloud-object-storage.test.cloud.ibm.com/v2/endpoints",
  "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/3ag0e9402tyfd5d29761c3e97696b71n:d6f74k03-6k4f-4a82-b165-697354o63903::",
  "iam_apikey_name": "auto-generated-apikey-f9274b63-ef0b-4b4e-a00b-b3bf9023f9dd",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
  "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/3ag0e9402tyfd5d29761c3e97696b71n::serviceid:ServiceId-540a4a41-7322-4fdd-a9e7-e0cb7ab760f9",
  "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/3ag0e9402tyfd5d29761c3e97696b71n:d6f74k03-6k4f-4a82-b165-697354o63903::"
}
```

## `endpoints` 오브젝트 이해
{: #service-credential-endpoints}

서비스 인증 정보의 일부로서 제공되는 `endpoints` URL은 클라이언트에 연결할 때 사용 가능한 모든 엔드포인트의 목록을 제공합니다. 

```json
{
  "identity-endpoints": {
    "iam-token": "iam.cloud.ibm.com",
    "iam-policy": "iampap.cloud.ibm.com"
  },
  "service-endpoints": {
    "cross-region": {
      "us": {
        "public": {
          "us-geo": "s3.us.cloud-object-storage.appdomain.cloud",
          "Dallas": "s3.dal.us.cloud-object-storage.appdomain.cloud",
          "Washington": "s3.wdc.us.cloud-object-storage.appdomain.cloud",
          "San Jose": "s3.sjc.us.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "us-geo": "s3.private.us.cloud-object-storage.appdomain.cloud",
          "Dallas": "s3.private.dal.us.cloud-object-storage.appdomain.cloud",
          "Washington": "s3.private.wdc.us.cloud-object-storage.appdomain.cloud",
          "San Jose": "s3.private.sjc.us.cloud-object-storage.appdomain.cloud"
        }
      },
      "eu": {
        "public": {
          "eu-geo": "s3.eu.cloud-object-storage.appdomain.cloud",
          "Amsterdam": "s3.ams.eu.cloud-object-storage.appdomain.cloud",
          "Frankfurt": "s3.fra.eu.cloud-object-storage.appdomain.cloud",
          "Milan": "s3.mil.eu.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "eu-geo": "s3.private.eu.cloud-object-storage.appdomain.cloud",
          "Amsterdam": "s3.private.ams.eu.cloud-object-storage.appdomain.cloud",
          "Frankfurt": "s3.private.fra.eu.cloud-object-storage.appdomain.cloud",
          "Milan": "s3.private.mil.eu.cloud-object-storage.appdomain.cloud"
        }
      },
      "ap": {
        "public": {
          "ap-geo": "s3.ap.cloud-object-storage.appdomain.cloud",
          "Tokyo": "s3.tok.ap.cloud-object-storage.appdomain.cloud",
          "Seoul": "s3.seo.ap.cloud-object-storage.appdomain.cloud",
          "Hong Kong": "s3.hkg.ap.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "ap-geo": "s3.private.ap.cloud-object-storage.appdomain.cloud",
          "Tokyo": "s3.private.tok.ap.cloud-object-storage.appdomain.cloud",
          "Seoul": "s3.private.seo.ap.cloud-object-storage.appdomain.cloud",
          "Hong Kong": "s3.private.hkg.ap.cloud-object-storage.appdomain.cloud"
        }
      }
    },
    "regional": {
      "us-south": {
        "public": {
          "us-south": "s3.us-south.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "us-south": "s3.private.us-south.cloud-object-storage.appdomain.cloud"
        }
      },
      "us-east": {
        "public": {
          "us-east": "s3.us-east.cloud-object-storage.appdomain.cloud"
        },
        "private": {
          "us-east": "s3.private.us-east.cloud-object-storage.appdomain.cloud"
        }
      },
      "eu-gb": {
        "public": {"eu-gb": "s3.eu-gb.cloud-object-storage.appdomain.cloud"},
        "private": {
          "eu-gb": "s3.private.eu-gb.cloud-object-storage.appdomain.cloud"
        }
      },
      "eu-de": {
        "public": {"eu-de": "s3.eu-de.cloud-object-storage.appdomain.cloud"},
        "private": {
          "eu-de": "s3.private.eu-de.cloud-object-storage.appdomain.cloud"
        }
      },
      "jp-tok": {
        "public": {"jp-tok": "s3.jp-tok.cloud-object-storage.appdomain.cloud"},
        "private": {
          "jp-tok": "s3.private.jp-tok.cloud-object-storage.appdomain.cloud"
        }
      },
      "au-syd": {
        "public": {"au-syd": "s3.au-syd.cloud-object-storage.appdomain.cloud"},
        "private": {
          "au-syd": "s3.private.au-syd.cloud-object-storage.appdomain.cloud"
        }
      }
    },
    "single-site": {
      "ams03": {
        "public": {"ams03": "s3.ams03.cloud-object-storage.appdomain.cloud"},
        "private": {
          "ams03": "s3.private.ams03.cloud-object-storage.appdomain.cloud"
        }
      },
      "che01": {
        "public": {"che01": "s3.che01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "che01": "s3.private.che01.cloud-object-storage.appdomain.cloud"
        }
      },
      "mel01": {
        "public": {"mel01": "s3.mel01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "mel01": "s3.private.mel01.cloud-object-storage.appdomain.cloud"
        }
      },
      "osl01": {
        "public": {"osl01": "s3.osl01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "osl01": "s3.private.osl01.cloud-object-storage.appdomain.cloud"
        }
      },
      "tor01": {
        "public": {"tor01": "s3.tor01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "tor01": "s3.private.tor01.cloud-object-storage.appdomain.cloud"
        }
      },
      "sao01": {
        "public": {"sao01": "s3.sao01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "sao01": "s3.private.sao01.cloud-object-storage.appdomain.cloud"
        }
      },
      "seo01": {
        "public": {"seo01": "s3.seo01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "seo01": "s3.private.seo01.cloud-object-storage.appdomain.cloud"
        }
      },
      "mon01": {
        "public": {"mon01": "s3.mon01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "mon01": "s3.private.mon01.cloud-object-storage.appdomain.cloud"
        }
      },
      "mex01": {
        "public": {"mex01": "s3.mex01.cloud-object-storage.appdomain.cloud"},
        "private": {
          "mex01": "s3.private.mex01.cloud-object-storage.appdomain.cloud"
        }
      }
    }
  }
}
```

"auth" 엔드포인트 값을 필요로 하는 라이브러리를 사용하여 클라이언트를 작성할 때는 위에 제공된 `iam-token` URL의 끝에 `/oidc/token`을 추가해야 합니다.
{:tip}

## 단일 버킷 액세스를 위해 서비스 인증 정보 사용
{: #service-credentials-bucket}

서비스 인증 정보가 작성될 때는 기반 서비스 ID에 전체 {{site.data.keyword.cos_short}} 인스턴스에 대한 역할이 부여됩니다. 전체 인스턴스가 아니라 버킷의 서브세트에 대한 액세스를 부여하는 것이 해당 인증 정보의 사용 의도인 경우에는 이 정책을 편집해야 합니다. 세부사항은 [버킷 권한](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions) 페이지를 참조하십시오. 

## API 키 대 HMAC
{: #service-credentials-iam-hmac}

일반적으로 {{site.data.keyword.cos_full}}에 대해 선호되는 인증 방법은 IAM API 키입니다. HMAC는 주로 IaaS {site.data.keyword.cos_short}} 및 레거시 S3 애플리케이션에서 마이그레이션된 애플리케이션과의 하위 호환성을 위해 지원됩니다. IAM은 또한 COS SDK를 사용하여 애플리케이션을 개발할 때 기본적으로 지원됩니다. 프로세스를 간소화하기 위해 토큰 만기 및 새로 고치기가 자동으로 처리됩니다. 


IAM에 대한 자세한 정보를 얻으려면 [IAM 시작하기](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)를 참조하십시오. 

HMAC에 대한 자세한 정보를 얻으려면 [HMAC 인증 정보 사용](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)을 참조하십시오. 
