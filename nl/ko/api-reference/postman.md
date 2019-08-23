---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# `Postman` 사용
{: #postman}

다음은 {{site.data.keyword.cos_full}} REST API에 대한 기본 `Postman` 설정입니다. 추가 세부사항은 [버킷](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) 또는 [오브젝트](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)에 대한 API 참조에 있습니다.

`Postman` 사용 시 [서비스 인증 정보](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) 또는 [콘솔](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)의 필수 정보 및 오브젝트 저장소가 어느 정도 익숙하다고 가정합니다. 용어나 변수가 익숙하지 않은 경우 이를 [용어집](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)에서 찾을 수 있습니다.

개인 식별 정보(PII): 버킷 작성 및/또는 오브젝트 추가 시 이름, 위치 또는 다른 수단으로 사용자(자연인)를 식별할 수 있는 정보를 사용하지 마십시오.
{:tip}

## REST API 클라이언트 개요
{: #postman-rest}

REST(Representational State Transfer)는 일반적으로 표준 HTTP URL과 verb(GET, PUT, POST 등)를 사용하여
웹을 통해 상호작용하도록 컴퓨터 시스템에 대한 표준을 제공하는 아키텍처 스타일이며, 모든 주요 개발 언어와 플랫폼에서 지원됩니다. 하지만 REST API와 상호작용은 표준 인터넷 브라우저를 사용하는 것만큼 간단하지 않습니다. 단순 브라우저는 URL 요청 조작을 허용하지 않습니다. 이는 REST API 클라이언트가 사용되는 위치입니다.

REST API 클라이언트는 기존 REST API 라이브러리와 인터페이스로 접속할 수 있는 단순 GUI 기반 애플리케이션을 제공합니다. 좋은 클라이언트를 사용하면 사용자가 단순한 HTTP 요청과 복잡한 HTTP 요청을 쉽게 조합할 수 있어 API를 쉽게 테스트하고 개발하고 문서화할 수 있습니다. Postman은 디자인과 대역, 디버그, 테스트, 문서, 모니터 및 공개 API를 위한 기본 제공 도구가 포함된 완벽한 API 개발 환경을 제공하는 뛰어난 REST API 클라이언트입니다. 또한 협업을 용이하게 하는 콜렉션 및 작업공간 등의 유용한 기능도 제공합니다. 

## 전제조건
{: #postman-prereqs}
* IBM Cloud 계정
* [클라우드 스토리지 리소스가 작성됨](https://cloud.ibm.com/catalog/)(Lite/무료 플랜이 제대로 작동함)
* [IBM Cloud CLI가 설치되고 구성됨](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [클라우드 스토리지의 서비스 인스턴스 ID](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [Identity and Access Management(IAM) 토큰](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [COS 버킷의 엔드포인트](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### 버킷 작성
{: #postman-create-bucket}
1.	Postman을 실행하십시오.
2.	새 탭의 드롭 다운 목록에서 `PUT`을 선택하십시오.
3.	주소 표시줄에 엔드포인트를 입력하고 새 버킷의 이름을 추가하십시오.
a.	버킷 이름은 모든 버킷에서 고유해야 하므로 특정한 것을 선택하십시오.
4.	유형 드롭 다운 목록에서 Bearer 토큰을 선택하십시오.
5.	토큰 상자에 IAM 토큰을 추가하십시오.
6.	요청 미리보기를 클릭하십시오.
a.	헤더가 추가되었다는 확인 메시지가 표시됩니다.
7.	권한에 대한 기존 항목이 표시되는 헤더 탭을 클릭하십시오.
8.	새 키를 추가하십시오.
a.	키: `ibm-service-instance-id`
b.	값: 클라우드 스토리지 서비스의 리소스 인스턴스 ID.
9.	전송을 클릭하십시오.
10.	상태 `200 OK` 메시지를 수신합니다.

### 새 텍스트 파일 작성
{: #postman-create-text-file}

1.	더하기(+) 아이콘을 클릭하여 탭을 새로 작성하십시오.
2.	목록에서 `PUT`을 선택하십시오.
3.	주소 표시줄에 파일 이름 및 이전 섹션의 버킷 이름을 사용하여 엔드포인트 주소를 입력하십시오.
4.	유형 목록에서 Bearer 토큰을 선택하십시오.
5.	토큰 상자에 IAM 토큰을 추가하십시오.
6.	본문 탭을 선택하십시오.
7.	원시 옵션을 선택하고 텍스트가 선택되었는지 확인하십시오.
8.	제공된 공백에 텍스트를 입력하십시오.
9.	전송을 클릭하십시오.
10.	상태 `200 OK` 메시지를 수신합니다.

### 버킷의 컨텐츠 나열
{: #postman-list-objects}

1.	더하기(+) 아이콘을 선택하여 탭을 새로 작성하십시오.
2.	목록에서 `GET`을 선택했는지 확인하십시오.
3.	주소 표시줄에 이전 섹션의 버킷 이름을 사용하여 엔드포인트 주소를 입력하십시오.
4.	유형 목록에서 Bearer 토큰을 선택하십시오.
5.	토큰 상자에 IAM 토큰을 추가하십시오.
6.	전송을 클릭하십시오.
7.	상태 `200 OK` 메시지를 수신합니다.
8.	응답 섹션의 본문에 버킷의 파일 목록과 함께 XML 메시지가 표시됩니다.

## 샘플 콜렉션 사용
{: #postman-collection}

Postman 콜렉션은 구성 가능한 {{site.data.keyword.cos_full}} API 요청 샘플과 함께 [다운로드 ![외부 링크 아이콘](../icons/launch-glyph.svg "외부 링크 아이콘")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window}에 사용 가능합니다.

### Postman으로 콜렉션 가져오기
{: #postman-import-collection}

1. Postman에서 오른쪽 상단 모서리에 있는 가져오기 단추를 클릭하십시오.
2. 다음 방법 중 하나를 사용하여 콜렉션 파일을 가져오십시오.
    * 가져오기 창에서 콜렉션 파일을 **Drop files here**로 레이블 지정된 창에 끌어서 놓으십시오.
    * 파일 선택 단추를 클릭하고 폴더로 이동하여 콜렉션 파일을 선택하십시오.
3. 이제 *IBM COS*가 콜렉션 창에 표시됩니다.
4. 콜렉션을 펼치면 20개의 샘플 요청이 표시됩니다.
5. 콜렉션에는 API 요청을 실행하기 위해 설정해야 하는 6개의 변수가 포함됩니다.
    * 콜렉션 오른쪽에 있는 세 개의 점을 클릭하여 메뉴를 펼치고 편집을 클릭하십시오.
6. 클라우드 스토리지 환경과 일치하도록 변수를 편집하십시오.
    * **bucket** - 작성할 새 버킷의 이름을 입력하십시오(버킷 이름이 클라우드 스토리지에서 고유해야 함).
    * **serviceid** - 클라우드 스토리지 서비스의 CRN을 입력하십시오. CRN을 얻기 위한 지시사항은 [여기](/docs/overview?topic=overview-crn)에 있습니다.
    * **iamtoken** - 클라우드 스토리지 서비스의 OAUTH 토큰을 입력하십시오. OAUTH 토큰을 얻기 위한 지시사항은 [여기](/docs/services/key-protect?topic=key-protect-retrieve-access-token)에 있습니다.
    * **endpoint** - 클라우드 스토리지 서비스의 지역 엔드포인트를 입력하십시오. [IBM Cloud 대시보드](https://cloud.ibm.com/resources/){:new_window}에서 사용 가능한 엔드포인트를 확보하십시오.
        * *샘플이 올바르게 실행되도록 하기 위해 선택한 엔드포인트가 Key Protect 서비스와 일치하는지 확인*
    * **rootkeycrn** - 기본 Key Protect 서비스에서 작성된 루트 키의 CRN입니다.
        * CRN은 다음과 유사해야 합니다.<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *선택한 Key Protect 서비스가 엔드포인트의 지역과 일치하는지 확인하십시오.*
    * **bucketlocationvault** - *버킷 새로 작성(다른 스토리지 클래스)* API 요청을 위한 버킷 작성에 대해 위치 제한조건 값을 입력하십시오.
        * 허용 가능한 값에 다음이 포함됩니다.
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. 업데이트를 클릭하십시오.

### 샘플 실행
{: #postman-samples}
API 샘플 요청은 매우 간단하고 사용하기 쉽습니다. 이러한 요청은 순서대로 실행되도록 설계되었으며 클라우드 스토리지와 상호작용하는 방법을 설명합니다. 또한 적절한 오퍼레이션을 위해 클라우드 스토리지 서비스에 대해 기능 테스트를 실행하는 데 사용할 수 있습니다.

<table>
    <tr>
        <th>요청</th>
        <th>예상 결과</th>
        <th>테스트 결과</th>
    </tr>
    <tr>
        <td>버킷 목록 검색</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
                <li>
                    본문에서는 클라우드 스토리지에 있는 버킷의 XML 목록을 설정해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 컨텐츠 포함</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>버킷 새로 작성</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>텍스트 파일 새로 작성</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 헤더 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>바이너리 파일 새로 작성</td>
        <td>
            <ul>
                <li>
                    본문을 클릭하고 파일 선택을 클릭하여 업로드할 이미지 선택
                </li>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 헤더 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>버킷에서 파일 목록 검색</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 이전 요청에서 작성된 두 개의 파일을 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 헤더 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>버킷에서 파일 목록 검색(접두부로 필터링)</td>
        <td>
            <ul>
                <li>querystring 값을 prefix=&lt;some text&gt;로 변경</li>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 이름이 지정된 접두부로 시작하는 파일을 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 헤더 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>텍스트 파일 검색</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 이전 요청에서 입력한 텍스트를 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 본문 컨텐츠 포함</li>
                <li>응답에 예상 헤더 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>바이너리 파일 검색</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 이전 요청에서 입력한 이미지를 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 헤더 포함</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>실패한 다중 파트 업로드 목록 검색</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 버킷에 대한 실패한 다중 파트 업로드를 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 컨텐츠 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>실패한 다중 파트 업로드 목록 검색(이름으로 필터링)</td>
        <td>
            <ul>
                <li>querystring 값을 prefix=&lt;some text&gt;로 변경</li>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 이름이 지정된 접두부로 시작하는 버킷에 대한 실패한 다중 파트 업로드를 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 컨텐츠 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>CORS 사용 버킷 설정</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>버킷 CORS 구성 검색</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
                <li>
                    응답 본문에서 버킷에 대한 CORS 구성 세트를 확인해야 합니다.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
                <li>응답에 예상 컨텐츠 포함</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>버킷 CORS 구성 삭제</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>텍스트 파일 삭제</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>바이너리 파일 삭제</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>버킷 삭제</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>버킷 새로 작성(다른 스토리지 클래스)</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>버킷 삭제(다른 스토리지 클래스)</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>버킷 새로 작성(Key Protect)</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>버킷 삭제(Key Protect)</td>
        <td>
            <ul>
                <li>상태 코드 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>요청 성공</li>
            </ul>
        </td>                
    </tr>
</table>

## Postman Collection Runner 사용
{: #postman-runner}

Postman Collection Runner는 콜렉션 테스트를 위한 사용자 인터페이스를 제공하고 콜렉션에서 한 번에 모든 요청을 실행하게 할 수 있습니다. 

1. 기본 Postman 창의 오른쪽 상단 모서리에 있는 Runner 단추를 클릭하십시오.
2. Runner 창에서 IBM COS 콜렉션을 선택하고 화면의 맨 아래에 있는 큰 파란색 **IBM COS 실행** 단추를 클릭하십시오.
3. Collection Runner 창에는 요청이 실행될 때 반복이 표시됩니다. 각 요청 아래에 테스트 결과가 표시됩니다.
    * **요약 실행**은 결과의 격자 보기를 표시하고 결과 필터링을 허용합니다.
    * 또한 결과를 JSON 파일에 저장할 **결과 내보내기**를 클릭할 수 있습니다.
