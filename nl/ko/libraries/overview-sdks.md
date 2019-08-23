---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-06-19"

keywords: sdks, overview

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
{:go: .ph data-hd-programlang='go'}

# IBM COS SDK 정보
{: #sdk-about}

IBM COS는 Java, Python, NodeJS 및 Go용 SDK를 제공합니다. 이러한 SDK는 공식 AWS S3 API SDK를 기반으로 하면서 IAM, Key Protect, 불변 오브젝트 스토리지 등의 IBM Cloud 기능을 사용하도록 수정되었습니다. 

| 기능                        | Java                                              | Python                                            | NodeJS                                            | Go                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| IAM API 키 지원             | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |
| 관리되는 다중 파트 업로드       | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |
| 관리되는 다중 파트 다운로드     | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| 확장된 버킷 나열            |                                                   |                                                   |                                                   |                                                   |                                                   |
| 버전 2 오브젝트 나열        | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| 아카이브 규칙               | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| 보존 정책                   | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aspera 고속 전송            | ![체크표시 아이콘](../../icons/checkmark-icon.svg) | ![체크표시 아이콘](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## IAM API 키 지원
{: #sdk-about-iam}
액세스/비밀 키 쌍 대신 API 키를 사용하여 클라이언트를 작성할 수 있도록 합니다. 토큰 관리는 자동으로 처리되며 토큰은 장기 실행 오퍼레이션 중에 자동으로 새로 고쳐집니다. 
## 관리되는 다중 파트 업로드
SDK가 `TransferManager` 클래스를 사용하여 다중 파트로 오브젝트를 업로드하는 데 필요한 모든 로직을 처리합니다. 
## 관리되는 다중 파트 다운로드
SDK가 `TransferManager` 클래스를 사용하여 다중 파트로 오브젝트를 다운로드하는 데 필요한 모든 로직을 처리합니다. 
## 확장된 버킷 나열
이는 버킷을 나열할 때 버킷의 목록을 버킷의 프로비저닝 코드(`LocationConstraint`로 리턴되는, 버킷의 위치와 스토리지 클래스의 조합)와 함께 리턴하는 S3 API에 대한 확장입니다. 이는 사용된 엔드포인트에 관계없이 서비스 인스턴스에 있는 모든 버킷을 나열하므로 버킷을 찾는 데 유용합니다. 
## 버전 2 오브젝트 나열
버전 2 나열은 오브젝트 나열에 대해 더 강력한 범위 지정을 사용할 수 있게 해 줍니다. 
## Key Protect
Key Protect는 암호화 키를 관리하는 IBM Cloud 서비스이며, 버킷 작성 중에 사용할 수 있는 선택적 매개변수입니다. 
## SSE-C                      
## 아카이브 규칙              
## 보존 정책         
## Aspera 고속 전송 
