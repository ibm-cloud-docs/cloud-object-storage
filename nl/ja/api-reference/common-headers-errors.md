---

copyright:
  years: 2017, 2018
lastupdated: "2017-08-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# 共通ヘッダーおよびエラー・コード
{: #compatibility-common}

## 共通する要求ヘッダー
{: #compatibility-request-headers}

以下の表で、サポートされている共通要求ヘッダーについて説明します。この資料で定義されているように一部の要求では他のヘッダーがサポートされることもありますが、{{site.data.keyword.cos_full}} は、以下にリストされていない共通ヘッダーが要求で送信された場合はそれを無視します。

| ヘッダー                  | 注記                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Authorization           | すべての要求で**必須** です (OAuth2 `bearer` トークン)。                                       |
| ibm-service-instance-id | バケットを作成またはリストする要求では**必須**です。                                                            |
| Content-MD5             | base64 でエンコードされた、ペイロードの 128 ビット MD5 ハッシュ。ペイロードが転送中に変更されなかったことを確認するための保全性検査として使用されます。|
| Expect                  | 値 `100-continue` は、ペイロードを送信する前に、ヘッダーが適切であることのシステムからの肯定応答を待ちます。      |
| host                    | エンドポイントまたは `{bucket-name}.{endpoint}` の「仮想ホスト」構文のいずれか。通常、このヘッダーは自動的に追加されます。エンドポイントについて詳しくは、[エンドポイントおよびストレージ・ロケーション](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)を参照してください。| 
| Cache-Control | 要求/応答チェーンに沿ったキャッシング動作を指定するために使用できます。詳しくは、http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 を参照してください。|

### カスタム・メタデータ
{: #compatibility-headers-metadata}

オブジェクト・ストレージを使用する利点の 1 つは、キーと値のペアをヘッダーとして送信することによってカスタム・メタデータを追加できることです。これらのヘッダーの形式は `x-amz-meta-{KEY}` です。AWS S3 とは異なり、IBM COS はメタデータ・キーが同じ複数のヘッダーを結合して、値をコンマで区切った 1 つのリストにすることに注意してください。

## 共通する応答ヘッダー
{: #compatibility-response-headers}

共通する応答ヘッダーの説明を以下の表に示します。

| ヘッダー           | 注記                                                |
|------------------|-----------------------------------------------------|
| Content-Length   | 要求本体の長さ (バイト)。                           |
| Connection       | 接続が開いているのか閉じているのかを示します。      |
| Date             | 要求のタイム・スタンプ                              |
| ETag             | 要求の MD5 ハッシュ値。                             |
| Server           | 応答するサーバーの名前。                            |
| X-Clv-Request-Id | 要求ごとに生成される固有 ID。                       |

### ライフサイクル応答ヘッダー
{: #compatibility-lifecycle-headers}

以下の表に、アーカイブ済みオブジェクトの応答ヘッダーの説明を示します。

| ヘッダー           | 注記                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore|オブジェクトがリストア済みの場合またはリストアが進行中の場合に含まれます。|
|x-amz-storage-class|アーカイブ済みまたは一時的にリストア済みの場合に `GLACIER` を返します。|
|x-ibm-archive-transition-time|アーカイブ層へのオブジェクトの遷移がスケジュールされている日時を返します。|
|x-ibm-transition|オブジェクトに遷移メタデータがある場合に含まれ、遷移の層および元の時刻を返します。|
|x-ibm-restored-copy-storage-class|オブジェクトが `RestoreInProgress` 状態または `Restored` 状態の場合に含まれ、バケットのストレージ・クラスを返します。|

## エラー・コード
{: #compatibility-errors}

| エラー・コード                      | 説明                                                                                                                                                             | HTTP 状況コード                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | アクセスは拒否されました。| 403  Forbidden                       |
| BadDigest                           | 指定された Content-MD5 が、受け取った内容と一致しませんでした。| 400  Bad Request                     |
| BucketAlreadyExists                 | 要求されたバケット名は使用できません。バケット名前空間はシステムのすべてのユーザーで共有されます。別の名前を選択して、やり直してください。| 409 Conflict                        |
| BucketAlreadyOwnedByYou             | 指定されたバケットを作成するという前の要求が成功し、既にそれを所有しています。| 409 Conflict                        |
| BucketNotEmpty                      | 削除しようとしたバケットは空ではありません。| 409 Conflict                        |
| CredentialsNotSupported             | この要求は資格情報をサポートしていません。| 400  Bad Request                     |
| EntityTooSmall                      | 提案したアップロードは許可される最小オブジェクト・サイズよりも小さいです。| 400  Bad Request                     |
| EntityTooLarge                      | 提案したアップロードは許可される最大オブジェクト・サイズを超えています。| 400  Bad Request                     |
| IncompleteBody                      | Content-Length HTTP ヘッダーで指定された数のバイトが提供されませんでした。| 400  Bad Request                     |
| IncorrectNumberOfFilesInPostRequest | POST には、要求ごとに 1 つのファイル・アップロードが必要です。| 400  Bad Request                     |
| InlineDataTooLarge                  | インライン・データは許可される最大サイズを超えています。| 400  Bad Request                     |
| InternalError                       | 内部エラーが発生しました。再試行してください。| 500  Internal Server Error           |
| InvalidAccessKeyId                  | 指定した AWS アクセス・キー ID はレコードに存在しません。| 403  Forbidden                       |
| InvalidArgument                     | 無効な引数です。| 400  Bad Request                     |
| InvalidBucketName                   | 指定されたバケットは無効です。| 400  Bad Request                     |
| InvalidBucketState                  | バケットの現在の状態では無効な要求です。| 409 Conflict                        |
| InvalidDigest                       | 指定した Content-MD5 は無効です。| 400  Bad Request                     |
| InvalidLocationConstraint           | 指定されたロケーション制約は無効です。地域について詳しくは、『バケットの地域の選択方法』を参照してください。| 400  Bad Request                     |
| InvalidObjectState                  | オブジェクトの現在の状態では無効な操作です。| 403  Forbidden                       |
| InvalidPart                         | 指定されたパートのうち 1 つ以上が見つかりませんでした。パートがアップロードされていないか、指定されたエンティティー・タグがパートのエンティティー・タグに一致しなかった可能性があります。| 400  Bad Request                     |
| InvalidPartOrder                    | パートのリストが昇順ではありませんでした。パートのリストはパート番号順に指定される必要があります。| 400  Bad Request                     |
| InvalidRange                        | 要求された範囲を満たすことができません。| 416 Requested Range Not Satisfiable |
| InvalidRequest                      | AWS4-HMAC-SHA256 を使用してください。| 400  Bad Request                     |
| InvalidSecurity                     | 提供されたセキュリティー資格情報は無効です。| 403  Forbidden                       |
| InvalidURI                          | 指定された URI を構文解析できませんでした。| 400  Bad Request                     |
| KeyTooLong                          | キーが長すぎます。| 400  Bad Request                     |
| MalformedPOSTRequest                | POST 要求の本体は正しい形式の multipart/form-data ではありません。| 400  Bad Request                     |
| MalformedXML                        | 提供された XML の形式が正しくないか、公開されたスキーマに照らして検証されませんでした。| 400  Bad Request                     |
| MaxMessageLengthExceeded            | 要求は大きすぎます。| 400  Bad Request                     |
| MaxPostPreDataLengthExceededError   | アップロード・ファイルの前にある POST 要求フィールドが大きすぎます。| 400  Bad Request                     |
| MetadataTooLarge                    | メタデータ・ヘッダーは許可される最大メタデータ・サイズを超えています。| 400  Bad Request                     |
| MethodNotAllowed                    | 指定されたメソッドはこのリソースに対しては許可されません。| 405 Method Not Allowed              |
| MissingContentLength                | Content-Length HTTP ヘッダーを指定する必要があります。| 411 Length Required                 |
| MissingRequestBodyError             | これは、ユーザーが空の XML 文書を要求として送信した場合に発生します。エラー・メッセージは「Request body is empty.」です。| 400  Bad Request                     |
| NoSuchBucket                        | 指定されたバケットは存在しません。| 404 Not Found                       |
| NoSuchKey                           | 指定されたキーは存在しません。| 404 Not Found                       |
| NoSuchUpload                        | 指定されたマルチパート・アップロードは存在しません。アップロード ID が無効であるか、マルチパート・アップロードが打ち切られたか完了した可能性があります。| 404 Not Found                       |
| NotImplemented                      | 提供されたヘッダーは、実装されていない機能を暗黙に示しています。| 501 Not Implemented                 |
| OperationAborted                    | このリソースに対して、競合する条件付き操作が現在進行中です。やり直してください。                                                                         | 409 Conflict                        |
| PreconditionFailed                  | 指定した前提条件の少なくとも 1 つが保持されませんでした。| 412 Precondition Failed             |
| Redirect                            | 一時的リダイレクト。| 307 Moved Temporarily               |
| RequestIsNotMultiPartContent        | バケット POST は enclosure-type multipart/form-data でなければなりません。| 400  Bad Request                     |
| RequestTimeout                      | サーバーへのソケット接続がタイムアウト期間内に読み書きされませんでした。| 400  Bad Request                     |
| RequestTimeTooSkewed                | 要求時刻とサーバーの時間の差が大きすぎます。| 403  Forbidden                       |
| ServiceUnavailable                  | 要求レートを小さくしてください。| 503  Service Unavailable             |
| SlowDown                            | 要求レートを小さくしてください。| 503 Slow Down                       |
| TemporaryRedirect                   | DNS 更新中はバケットにリダイレクトされます。| 307 Moved Temporarily               |
| TooManyBuckets                      | 許可されている数を超えるバケットを作成しようとしました。| 400  Bad Request                     |
| UnexpectedContent                   | この要求はコンテンツをサポートしていません。| 400  Bad Request                     |
| UserKeyMustBeSpecified              | バケット POST には、指定されたフィールド名が含まれている必要があります。指定されている場合は、フィールドの順序を調べてください。| 400  Bad Request                     |
