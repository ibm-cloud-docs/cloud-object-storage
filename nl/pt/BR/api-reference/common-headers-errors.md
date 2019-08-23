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

# Cabeçalhos comuns e códigos de erro
{: #compatibility-common}

## Cabeçalhos comuns da solicitação
{: #compatibility-request-headers}

A tabela a seguir descreve os cabeçalhos comuns da solicitação suportados. O {{site.data.keyword.cos_full}} ignora quaisquer cabeçalhos comuns não listados abaixo se enviados em uma solicitação, embora algumas solicitações possam suportar outros cabeçalhos, conforme definido nesta documentação.

| Cabeçalho (Header)                  | Nota                                                                                                                               |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Autorização           | **Necessário** para todas as solicitações (token OAuth2 `bearer`).                                                                            |
| ibm-service-instance-id | **Necessário** para solicitações para criar ou listar depósitos.                                                                              |
| Content-MD5             | O hash MD5 de 128 bits codificado em base64 da carga útil, usado como uma verificação de integridade para assegurar que a carga útil não tenha sido alterada em trânsito.  |
| Expect                  | O valor `100-continue` aguardará a confirmação do sistema de que os cabeçalhos são apropriados antes de enviar a carga útil. |
| host                    |O terminal ou a sintaxe de 'host virtual' de `{bucket-name}.{endpoint}`. Geralmente, esse cabeçalho é incluído automaticamente. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)    | 
| Cache-Control | Pode ser usado para especificar o comportamento de armazenamento em cache ao longo da cadeia de solicitação/resposta. Para obter mais informações, acesse http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9 |

### Metadados customizados
{: #compatibility-headers-metadata}

Um benefício de uso do armazenamento de objeto é a capacidade de incluir metadados customizados, enviando pares chave-valor como cabeçalhos. Esses cabeçalhos tomam a forma de `x-amz-meta-{KEY}`. Observe que, diferentemente do AWS S3, o IBM COS combinará múltiplos cabeçalhos com a mesma chave de metadados em uma lista separada por vírgula de valores.

## Cabeçalhos de resposta comuns
{: #compatibility-response-headers}

A tabela a seguir descreve os cabeçalhos de resposta comuns.

| Cabeçalho (Header)           | Nota                                                |
|------------------|-----------------------------------------------------|
| Content-Length   | O comprimento do corpo da solicitação em bytes.           |
| Conexão       | Indica se a conexão está aberta ou encerrada. |
| data             | O registro de data e hora da solicitação.          |
| ETag             | O valor do hash MD5 da solicitação.                |
| Server           | Nome do servidor de resposta.                      |
| X-Clv-Request-Id | Identificador exclusivo gerado por solicitação.    |

### Cabeçalhos de resposta do ciclo de vida
{: #compatibility-lifecycle-headers}

A tabela a seguir descreve os cabeçalhos de resposta para objetos arquivados

| Cabeçalho (Header)           | Nota                                                |
|------------------|-----------------------------------------------------|
|x-amz-restore|Incluído se o objeto tiver sido restaurado ou se uma restauração estiver em andamento.|
|x-amz-storage-class|Retorna `GLACIER` se arquivado ou temporariamente restaurado.|
|x-ibm-archive-transition-time|Retorna a data e hora em que o objeto está planejado para fazer a transição para a camada de archive.|
|x-ibm-transition|Incluído se o objeto tiver metadados de transição e retornar a camada e o horário original de transição.|
|x-ibm-restored-copy-storage-class|Incluído se um objeto estiver nos estados `RestoreInProgress` ou `Restored` e retornará a classe de armazenamento do depósito.|

## Códigos de erro
{: #compatibility-errors}

| Código de erro                          | Descrição                                                                                                                                                             | Código de Status HTTP                    |
|-------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------|
| AccessDenied                        | Acesso negado                                                                                                                                                          | 403 Proibido                       |
| BadDigest                           | O Content-MD5 que você especificou não correspondeu ao que nós recebemos.                                                                                              | 400 Solicitação inválida            |
| BucketAlreadyExists                 | O nome do depósito solicitado não está disponível. O namespace do depósito é compartilhado por todos os usuários do sistema. Selecione um nome diferente e tente novamente.                   | 409 Conflito                        |
| BucketAlreadyOwnedByYou             | Sua solicitação anterior para criar o depósito nomeado foi bem-sucedida e você já possui isso.                                                                         | 409 Conflito                        |
| BucketNotEmpty                      | O depósito que você tentou excluir não está vazio.                                                                                                                     | 409 Conflito                        |
| CredentialsNotSupported             | Essa solicitação não suporta credenciais.                                                                                                                              | 400 Solicitação inválida            |
| EntityTooSmall                      | Seu upload proposto é menor do que o tamanho mínimo permitido do objeto.                                                                                               | 400 Solicitação inválida            |
| EntityTooLarge                      | Seu upload proposto excede o tamanho máximo permitido do objeto.                                                                                                       | 400 Solicitação inválida            |
| IncompleteBody                      | Você não forneceu o número de bytes especificado pelo cabeçalho de HTTP Content-Length.                                                                                | 400 Solicitação inválida            |
| IncorrectNumberOfFilesInPostRequest | O POST requer exatamente um upload de arquivo por solicitação.                                                                                                         | 400 Solicitação inválida            |
| InlineDataTooLarge                  | Os dados sequenciais excedem o tamanho máximo permitido.                                                                                                               | 400 Solicitação inválida            |
| InternalError                       | Nós encontramos um erro interno. Tente novamente.                                                                                                                      | 500 Erro interno do servidor        |
| InvalidAccessKeyId                  | O ID da chave de acesso do AWS que você forneceu não existe em nossos registros.                                                                                       | 403 Proibido                        |
| InvalidArgument                     | Argumento inválido                                                                                                                                                     | 400 Solicitação inválida            |
| InvalidBucketName                   | O depósito especificado não é válido.                                                                                                                                  | 400 Solicitação inválida            |
| InvalidBucketState                  | A solicitação não é válida com o estado atual do depósito.                                                                                                             | 409 Conflito                        |
| InvalidDigest                       | O Content-MD5 que você especificou não é válido.                                                                                                                       | 400 Solicitação inválida            |
| InvalidLocationConstraint           | A restrição de local especificada não é válida. Para obter mais informações sobre regiões, consulte Como selecionar uma região para seus depósitos.                    | 400 Solicitação inválida            |
| InvalidObjectState                  | A operação não é válida para o estado atual do objeto.                                                                                                                 | 403 Proibido                        |
| InvalidPart                         | Uma ou mais das partes especificadas não puderam ser localizadas. A parte pode não ter sido transferida por upload ou a tag de entidade especificada pode não ter correspondido à tag de entidade da parte.  | 400 Solicitação inválida            |
| InvalidPartOrder                    | A lista de partes não estava em ordem crescente. A lista de partes deve ser especificada em ordem por número de peça.                                                  | 400 Solicitação inválida            |
| InvalidRange                        | O intervalo solicitado não pode ser satisfeito.                                                                                                                        | 416 O intervalo solicitado não pode ser satisfeito |
| InvalidRequest                      | Use AWS4-HMAC-SHA256.                                                                                                                                                  | 400 Solicitação inválida            |
| InvalidSecurity                     | As credenciais de segurança fornecidas não são válidas.                                                                                                                | 403 Proibido                        |
| InvalidURI                          | Não foi possível analisar o URI especificado.                                                                                                                          | 400 Solicitação inválida            |
| KeyTooLong                          | Sua chave é muito longa.                                                                                                                                               | 400 Solicitação inválida            |
| MalformedPOSTRequest                | O corpo de sua solicitação de POST não é de dados de formulário/partes múltiplas bem formados.                                                                              | 400 Solicitação inválida            |
| MalformedXML                        | O XML que você forneceu não foi bem formado ou não foi validado com relação ao nosso esquema publicado.                                                                | 400 Solicitação inválida            |
| MaxMessageLengthExceeded            | Seu pedido era muito grande.                                                                                                                                           | 400 Solicitação inválida            |
| MaxPostPreDataLengthExceededError   | Seus campos de solicitação de POST precedendo o arquivo de upload eram muito grandes.                                                                                  | 400 Solicitação inválida            |
| MetadataTooLarge                    | Seus cabeçalhos de metadados excedem o tamanho máximo permitido de metadados.                                                                                          | 400 Solicitação inválida            |
| MethodNotAllowed                    | O método especificado não é permitido com relação a esse recurso.                                                                                                      | 405 Método não permitido            |
| MissingContentLength                | Deve-se fornecer o cabeçalho de HTTP Content-Length.                                                                                                                   | 411 Comprimento necessário          |
| MissingRequestBodyError             | Isso acontece quando o usuário envia um documento xml vazio como uma solicitação. A mensagem de erro é "O corpo da solicitação está vazio."                            | 400 Solicitação inválida            |
| NoSuchBucket                        | O depósito especificado não existe.                                                                                                                                    | 404 Não Localizado                       |
| NoSuchKey                           | A chave especificada não existe.                                                                                                                                       | 404 Não Localizado                       |
| NoSuchUpload                        | O upload de múltiplas partes especificado não existe. O ID de upload pode ser inválido ou o upload de múltiplas partes pode ter sido interrompido ou concluído.                  | 404 Não Localizado                       |
| NotImplemented                      | Um cabeçalho que você forneceu implica uma funcionalidade que não está implementada.                                                                                   | 501 Não implementado                |
| OperationAborted                    | Uma operação condicional conflitante está atualmente em andamento com relação a esse recurso. Tente novamente.                                                         | 409 Conflito                        |
| PreconditionFailed                  | Pelo menos uma das condições prévias que você especificou não foi mantida.                                                                                             | 412 Condição prévia com falha       |
| Redirecionar                            | Redirecionamento temporário.                                                                                                                                       | 307 Movido temporariamente          |
| RequestIsNotMultiPartContent        | O POST de depósito deve ser dos dados de formulário/partes múltiplas do tipo de gabinete.                                                                                   | 400 Solicitação inválida            |
| RequestTimeout                      | Sua conexão do soquete com o servidor não foi lida ou gravada dentro do período de tempo limite.                                                                       | 400 Solicitação inválida            |
| RequestTimeTooSkewed                | A diferença entre o horário da solicitação e o horário do servidor é muito grande.                                                                                     | 403 Proibido                        |
| ServiceUnavailable                  | Reduza a taxa de solicitações.                                                                                                                                         | 503 Serviço indisponível            |
| SlowDown                            | Reduza a taxa de solicitações.                                                                                                                                         | 503 Reduzir                         |
| TemporaryRedirect                   | Você está sendo redirecionado para o depósito enquanto o DNS é atualizado.                                                                                             | 307 Movido temporariamente          |
| TooManyBuckets                      | Você tentou criar mais depósitos do que o permitido.                                                                                                                   | 400 Solicitação inválida            |
| UnexpectedContent                   | Essa solicitação não suporta conteúdo.                                                                                                                                 | 400 Solicitação inválida            |
| UserKeyMustBeSpecified              | O POST de depósito deve conter o nome do campo especificado. Se ele for especificado, verifique a ordem dos campos.                                                    | 400 Solicitação inválida            |
