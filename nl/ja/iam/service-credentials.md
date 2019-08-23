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

# サービス資格情報
{: #service-credentials}

サービス資格情報は、アプリケーションを {{site.data.keyword.cos_short}} に接続するために必要な情報を提供するもので、JSON 文書としてパッケージされます。サービス資格情報は常にサービス ID に関連付けられ、新しい資格情報と一緒に新規サービス ID を作成できます。 

サービス資格情報を作成するには、以下のステップを使用します。
1. {{site.data.keyword.cloud_notm}} コンソールにログインし、ご使用の {{site.data.keyword.cos_short}} のインスタンスにナビゲートします。
2. サイドにあるナビゲーションで、**「サービス資格情報」**をクリックします。 
3. **「新規資格情報」**をクリックし、必要な情報を指定します。
  HMAC 資格情報を生成する場合は、「HMAC 資格情報を含める (Include HMAC Credential)」チェック・ボックスをクリックします。
  
4. **「追加」**をクリックして、サービス資格情報を生成します。

資格情報には以下の値が含まれます。

フィールド名 | 値
--|--
`apikey`  |  サービス ID 用に作成された新規 API キー
`cos_hmac_keys`  |  S3 対応のツールおよびライブラリーで使用するためのアクセス・キー/秘密鍵のペア
`endpoints`  |  使用可能なエンドポイントの JSON 表現へのリンク
`iam_apikey_description`  |  API キーの説明 - 最初は自動生成されるが、編集可能
`iam_apikey_name`  |  API キー名 - 最初は自動生成されるが、編集可能
`iam_role_crn`  |  割り当てられる役割の固有 ID
`iam_serviceid_crn`  |  サービス ID の固有 ID
`resource_instance_id`  |  資格情報がアクセスする {{site.data.keyword.cos_short}} のインスタンスの固有 ID。これは、サービス資格情報とも呼ばれます。

サービス資格情報の例を以下に示します。

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

## `endpoints` オブジェクトの理解
{: #service-credential-endpoints}

サービス資格情報の一部として提供される `endpoints` URL によって、クライアント接続時に使用できるすべてのエンドポイントのリストが提供されます。

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

「auth」エンドポイント値を必要とするライブラリーを使用してクライアントを作成する場合は、上記の `iam-token` URL の終わりに `/oidc/token` を追加する必要があります。
{:tip}

## 単一バケットへのアクセス用のサービス資格情報の使用
{: #service-credentials-bucket}

サービス資格情報が作成されると、基礎となるサービス ID には、{{site.data.keyword.cos_short}} のインスタンス全体に対する役割が付与されます。資格情報を使用する目的が、インスタンス全体ではなくバケットのサブセットに対するアクセス権限を付与することである場合、このポリシーを編集する必要があります。詳しくは、[バケット許可](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-bucket-permissions)のページを参照してください。

## API キー vs HMAC
{: #service-credentials-iam-hmac}

一般的には、{{site.data.keyword.cos_full}} の認証メソッドとして望ましいのは IAM API キーです。HMAC は、基本的に IaaS {{site.data.keyword.cos_short}} および既存の S3 アプリケーションからマイグレーションされたアプリケーションとの後方互換性のためにサポートされています。また、COS SDK を使用してアプリケーションを開発する際も、ネイティブにサポートされるのは IAM です。プロセスを単純化するために、トークンの満了とリフレッシュは自動的に処理されます。


IAM について詳しくは、[IAM の概要](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)にアクセスしてください。

HMAC について詳しくは、[HMAC 資格情報の使用](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac)にアクセスしてください。
