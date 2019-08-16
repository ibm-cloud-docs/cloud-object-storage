---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-29"

keywords: archive, glacier, tier, s3, compatibility, api

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tsSymptoms: .tsSymptoms}
{:tsCauses: .tsCauses}
{:tsResolve: .tsResolve}
{:tip: .tip}
{:important: .important}
{:note: .note}
{:download: .download}
{:http: .ph data-hd-programlang='http'} 
{:javascript: .ph data-hd-programlang='javascript'} 
{:java: .ph data-hd-programlang='java'} 
{:python: .ph data-hd-programlang='python'} 

# Arquivar dados frios com regras de transição
{: #archive}

O {{site.data.keyword.cos_full}} Archive é uma opção de [baixo custo](https://www.ibm.com/cloud/object-storage) para dados que são raramente acessados. É possível armazenar dados executando a transição de qualquer uma das camadas de armazenamento (Standard, Vault, Cold Vault e Flex) para archive off-line de longo prazo ou usar a opção on-line Cold Vault.
{: shortdesc}

É possível arquivar objetos usando o console da web, a API de REST e as ferramentas de terceiros que são integradas ao IBM Cloud Object Storage. 

Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)
{:tip}

## Incluir ou gerenciar uma política de archive em um depósito
{: #archive-add}

Ao criar ou modificar uma política de archive para um depósito, considere o seguinte:

* Uma política de archive pode ser incluída em um depósito novo ou existente a qualquer momento. 
* Uma política de archive existente pode ser modificada ou desativada. 
* Uma política de archive recém-incluída ou modificada aplica-se a novos objetos transferidos por upload e não afeta objetos existentes.

Para arquivar imediatamente novos objetos transferidos por upload para um depósito, insira 0 dias na política de archive.
{:tip}

O archive está disponível somente em determinadas regiões. Consulte [Serviços integrados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) para obter mais detalhes.
{:tip}

## Restaurar um objeto arquivado
{: #archive-restore}

Para acessar um objeto arquivado, deve-se restaurá-lo para a camada de armazenamento original. Ao restaurar um objeto, é possível especificar o número de dias que você deseja que o objeto fique disponível. No término do período especificado, a cópia restaurada é excluída. 

O processo de restauração pode levar até 12 horas.
{:tip}

Os subestados do objeto arquivado são:

* Arquivado: um objeto no estado arquivado foi movido de sua camada de armazenamento on-line (Standard, Vault, Cold Vault e Flex) para a camada de archive off-line, com base na política de archive no depósito.
* Restaurando: um objeto no estado restaurando está no processo de geração de uma cópia do estado arquivado para sua camada de armazenamento on-line original.
* Restaurado: um objeto no estado restaurado é uma cópia do objeto arquivado que foi restaurada para sua camada de armazenamento on-line original por um período de tempo especificado. No término do período, a cópia do objeto é excluída, enquanto mantém o objeto arquivado.

## Limitações
{: #archive-limitations}

As políticas de archive são implementadas usando o subconjunto da operação de API S3 `PUT Bucket Lifecycle Configuration`. 

A funcionalidade suportada inclui:
* Especificar uma data ou o número de dias no futuro quando os objetos executam a transição para um estado arquivado.
* Configurar [regras de expiração](/docs/services/cloud-object-storage?topic=cloud-object-storage-expiry) para objetos.

A funcionalidade não suportada inclui:
* Múltiplas regras de transição por depósito.
* Filtrar objetos para archive usando um prefixo ou chave do objeto.
* Definição de camada entre as classes de armazenamento.

## Usando a API de REST e SDKs
{: #archive-api} 

### Criar uma configuração de ciclo de vida do depósito
{: #archive-api-create} 
{: http}

Essa implementação da operação `PUT` usa o parâmetro de consulta `lifecycle` para definir as configurações de ciclo de vida para o depósito. Essa operação permite uma definição de política de ciclo de vida única para um determinado depósito. A política é definida como uma regra que consiste nos parâmetros a seguir: `ID`, `Status` e `Transition`.
{: http}

A ação de transição permite que objetos futuros sejam gravados no depósito em um estado arquivado após um período de tempo definido. As mudanças na política de ciclo de vida de um depósito são **aplicadas somente a novos objetos** gravados nesse depósito.

Os usuários do Cloud IAM devem ter no mínimo a função `Writer` para incluir uma política de ciclo de vida no depósito.

Os usuários da infraestrutura clássica devem ter Permissões de proprietário e ser capazes de criar depósitos na conta de armazenamento para incluir uma política de ciclo de vida no depósito.

Essa operação não faz uso de parâmetros adicionais de consulta específicos da operação.
{: http}

Cabeçalho (Header)                    | Tipo   | Descrição
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | string | **Necessário**: o hash MD5 de 128 bits codificado em base64 da carga útil, usado como uma verificação de integridade para assegurar que a carga útil não tenha sido alterada em trânsito.
{: http}

O corpo da solicitação deve conter um bloco XML com o esquema a seguir:
{: http}

| Elemento                  | Tipo                 | Filhos                               | Antecessor               | Restrição                                                                                 |
|--------------------------|----------------------|----------------------------------------|--------------------------|--------------------------------------------------------------------------------------------|
| `LifecycleConfiguration` | Contêiner            | `Rule`                                 | Nenhum                   | Limite 1.                                                                                  |
| `Rule`                   | Contêiner            | `ID`, `Status`, `Filter`, `Transition` | `LifecycleConfiguration` | Limite 1.                                                                                  |
| `ID`                     | String               | Nenhum                                 | `Rule`                   | Deve consistir em (`a-z, `A-Z0-9`) e nos símbolos a seguir: `!` `_` `.` `*` `'` `(` `)` `-` |
| `Filter`                 | String               | `Prefix`                               | `Rule`                   | Deve conter um elemento `Prefix`                                                            |
| `Prefix`                 | String               | Nenhum                                 | `Filter`                 | **Deve** ser configurado como `<Prefix/>`.                                                           |
| `Transition`             | `Container`          | `Days`, `StorageClass`                 | `Rule`                   | Limite 1.                                                                                  |
| `Days`                   | Número inteiro não negativo | Nenhum                          | `Transition`             | Deve ser um valor maior que 0.                                                           |
| `Date`                   | data                 | Nenhum                                 | `Transistion`            | Deve estar no formato ISO 8601 e a data deve estar no futuro.                            |
| `StorageClass`           | String               | Nenhum                                 | `Transition`             | **Deve** ser configurado como `GLACIER`.                                                             |
{: http}

__Sintaxe__
{: http}

```
PUT https://{endpoint}/{bucket}?lifecycle # path style
PUT https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemplo 1. Observe o uso de barras e pontos neste exemplo de sintaxe." caption-side="bottom"}

```xml
<LifecycleConfiguration>
	<Rule>
		<ID>{string}</ID>
		<Status>Enabled</status>
		<Filter>
			<Prefix/>
		</Filter>
		<Transition>
			<Days>{integer}</Days>
			<StorageClass>GLACIER</StorageClass>
		</Transition>
	</Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Exemplo 2. Amostra XML para criar uma configuração de ciclo de vida do objeto." caption-side="bottom"}

__Exemplos__
{: http}

_Solicitação de amostra_

```
PUT /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
Content-Type: text/plain
Content-MD5: M625BaNwd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Exemplo 3. Amostras de cabeçalho da solicitação para criar uma configuração de ciclo de vida do objeto." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter>
			<Prefix/>
		</Filter>
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Exemplo 4. Amostra de XML para o corpo da solicitação de PUT." caption-side="bottom"}

_Resposta de amostra_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemplo 5. Cabeçalhos de resposta." caption-side="bottom"}

---

### Recuperar uma configuração de ciclo de vida do depósito
{: #archive-api-retrieve} 
{: http}

Essa implementação da operação `GET` usa o parâmetro de consulta `lifecycle` para recuperar as configurações de ciclo de vida para o depósito. 

Os usuários do Cloud IAM devem ter no mínimo a função `Reader` para recuperar um ciclo de vida para um depósito.

Os usuários da infraestrutura clássica devem ter no mínimo permissões `Read` no depósito para recuperar uma política de ciclo de vida para um depósito.

Essa operação não faz uso de cabeçalhos, parâmetros de consulta ou carga útil específicos da operação adicionais.

__Sintaxe__
{: http}

```
GET https://{endpoint}/{bucket}?lifecycle # path style
GET https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemplo 6. Variações em sintaxe para solicitações de GET." caption-side="bottom"}

__Exemplos__
{: http}

_Solicitação de amostra_

```
GET /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 17:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Exemplo 7. Cabeçalhos da solicitação de amostra para recuperar a configuração." caption-side="bottom"}

_Resposta de amostra_

```
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 17:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemplo 8. Cabeçalhos de resposta de amostra da solicitação de GET." caption-side="bottom"}

```xml
<LifecycleConfiguration>
    <Rule>
        <ID>my-archive-policy</ID>
        <Filter />
        <Status>Enabled</status>
        <Transition>
            <Days>20</Days>
            <StorageClass>GLACIER</StorageClass>
        </Transition>
    </Rule>
</LifecycleConfiguration>
```
{: codeblock}
{: http}
{: caption="Exemplo 9. Exemplo de XML para corpo de resposta." caption-side="bottom"}

---

### Excluir uma configuração de ciclo de vida do depósito
{: #archive-api-delete} {: http}

Essa implementação da operação `DELETE` usa o parâmetro de consulta `lifecycle` para remover quaisquer configurações de ciclo de vida para o depósito. As transições definidas pelas regras não ocorrerão mais para novos objetos. 

**Nota:** as regras de transição existentes serão mantidas para objetos que já foram gravados no depósito antes que as regras fossem excluídas.

Os usuários do Cloud IAM devem ter no mínimo a função `Writer` para remover uma política de ciclo de vida de um depósito.

Os usuários da infraestrutura clássica devem ter permissões `Owner` no depósito para remover uma política de ciclo de vida de um depósito.

Essa operação não faz uso de cabeçalhos, parâmetros de consulta ou carga útil específicos da operação adicionais.

__Sintaxe__
{: http}

```
DELETE https://{endpoint}/{bucket}?lifecycle # path style
DELETE https://{bucket}.{endpoint}?lifecycle # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemplo 10. Observe o uso de barras e pontos no exemplo de sintaxe." caption-side="bottom"}

__Exemplos__
{: http}

_Solicitação de amostra_

```
DELETE /images?lifecycle HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 18:50:00 GMT
Authorization: authorization string
```
{: codeblock}
{: http}
{: caption="Exemplo 11. Cabeçalhos da solicitação de amostra para o verbo DELETE HTTP." caption-side="bottom"}

_Resposta de amostra_

```
HTTP/1.1 204 No Content
Date: Wed, 7 Feb 2018 18:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemplo 12. Resposta de amostra da solicitação de DELETE." caption-side="bottom"}

---

### Restaurar temporariamente um objeto arquivado 
{: #archive-api-restore} {: http}

Essa implementação da operação `POST` usa o parâmetro de consulta `restore` para solicitar a restauração temporária de um objeto arquivado. O usuário deve primeiro restaurar um objeto arquivado antes de fazer download ou modificar o objeto. Ao restaurar um objeto, o usuário deve especificar um período após o qual a cópia temporária do objeto será excluída. O objeto mantém a classe de armazenamento do depósito.

Pode haver um atraso de até 12 horas antes que a cópia restaurada esteja disponível para acesso. Uma solicitação de `HEAD` poderá verificar se a cópia restaurada está disponível. 

Para restaurar permanentemente o objeto, o usuário deve copiar o objeto restaurado para um depósito que não tenha uma configuração de ciclo de vida ativa.

Os usuários do Cloud IAM devem ter no mínimo a função `Writer` para restaurar um objeto.

Os usuários da infraestrutura clássica devem ter no mínimo as permissões `Write` no depósito e a permissão `Read` no objeto para restaurá-lo.

Essa operação não faz uso de parâmetros adicionais de consulta específicos da operação.

Cabeçalho (Header)                    | Tipo   | Descrição
--------------------------|--------|----------------------------------------------------------------------------------------------------------------------
`Content-MD5` | string | **Necessário**: o hash MD5 de 128 bits codificado em base64 da carga útil, usado como uma verificação de integridade para assegurar que a carga útil não tenha sido alterada em trânsito.

O corpo da solicitação deve conter um bloco XML com o esquema a seguir:

Elemento                  | Tipo      | Filhos                               | Antecessor               | Restrição
-------------------------|-----------|----------------------------------------|--------------------------|--------------------
`RestoreRequest` | Contêiner | `Days`, `GlacierJobParameters`    | Nenhum     | Nenhuma
`Days`                   | Integer |Nenhum | `RestoreRequest` | Especificado o tempo de vida do objeto temporariamente restaurado. O número mínimo de dias em que uma cópia restaurada do objeto pode existir é 1. Depois que o período de restauração tiver decorrido, a cópia temporária do objeto será removida.
`GlacierJobParameters` | String | `Tier` | `RestoreRequest` | Nenhum
`Tier` | String | Nenhum | `GlacierJobParameters` | **Deve** ser configurado como `Bulk`.

Uma resposta bem-sucedida retornará um `202` se o objeto estiver no estado arquivado e em um `200` se o objeto já estiver no estado restaurado. Se o objeto já estiver no estado restaurado e uma nova solicitação para restaurar o objeto for recebida, o elemento `Days` atualizará o prazo de expiração do objeto restaurado.

__Sintaxe__
{: http}

```
POST https://{endpoint}/{bucket}/{object}?restore # path style
POST https://{bucket}.{endpoint}/{object}?restore # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemplo 13. Observe o uso de barras e pontos no exemplo de sintaxe." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>{integer}</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Exemplo 14. Modelo de XML para corpo da solicitação." caption-side="bottom"}

__Exemplos__
{: http}

_Solicitação de amostra_

```
POST /images/backup?restore HTTP/1.1
Host: s3.us.cloud-object-storage.appdomain.cloud
Date: Wed, 7 Feb 2018 19:50:00 GMT
Authorization: {authorization string}
Content-Type: text/plain
Content-MD5: rgRRGfd/OytcM7O5gIaQ==
Content-Length: 305
```
{: codeblock}
{: http}
{: caption="Exemplo 15. Cabeçalhos da solicitação de amostra para restauração de objeto." caption-side="bottom"}

```xml
<RestoreRequest>
	<Days>3</Days> 
	<GlacierJobParameters>
		<Tier>Bulk</Tier>
	</GlacierJobParameters>
</RestoreRequest>
```
{: codeblock}
{: http}
{: caption="Exemplo 16. Corpo da solicitação de amostra para restauração de objeto." caption-side="bottom"}

_Resposta de amostra_

```
HTTP/1.1 202 Accepted
Date: Wed, 7 Feb 2018 19:51:00 GMT
Connection: close
```
{: codeblock}
{: http}
{: caption="Exemplo 17. Resposta à restauração de objeto (`HTTP 202`)." caption-side="bottom"}

---

### Obter cabeçalhos de um objeto
{: http}
{: #archive-api-head}

Um `HEAD` fornecido em um caminho para um objeto recupera os cabeçalhos desse objeto. Essa operação não faz uso de parâmetros de consulta ou elementos de carga útil específicos da operação.

__Sintaxe__
{: http}

```bash
HEAD https://{endpoint}/{bucket-name}/{object-name} # path style
HEAD https://{bucket-name}.{endpoint}/{object-name} # virtual host style
```
{: codeblock}
{: http}
{: caption="Exemplo 18. Variações na definição de terminais." caption-side="bottom"}


__Cabeçalhos de resposta para objetos arquivados__
{: http}

Cabeçalho (Header) | Tipo | Descrição
--- | ---- | ------------
`x-amz-restore` | string | Incluído se o objeto tiver sido restaurado ou se uma restauração estiver em andamento. Se o objeto tiver sido restaurado, a data de validade para a cópia temporária também será retornada.
`x-amz-storage-class` | string | Retorna `GLACIER` se arquivado ou temporariamente restaurado.
`x-ibm-archive-transition-time` | date | Retorna a data e hora em que o objeto está planejado para fazer a transição para a camada de archive.
`x-ibm-transition` | string | Incluído se o objeto tiver metadados de transição e retornar a camada e o horário original de transição.
`x-ibm-restored-copy-storage-class` | string | Incluído se um objeto estiver nos estados `RestoreInProgress` ou `Restored` e retornará a classe de armazenamento do depósito.


_Solicitação de amostra_

```http
HEAD /images/backup HTTP/1.1
Authorization: {authorization-string}
x-amz-date: 20160825T183244Z
Host: s3.us.cloud-object-storage.appdomain.cloud
```
{: codeblock}
{: http}
{: caption="Exemplo 19. Exemplo mostrando cabeçalhos da solicitação." caption-side="bottom"}

_Resposta de amostra_

```http
HTTP/1.1 200 OK
Date: Wed, 7 Feb 2018 19:51:00 GMT
X-Clv-Request-Id: da214d69-1999-4461-a130-81ba33c484a6
Accept-Ranges: bytes
Server: 3.x
X-Clv-S3-Version: 2.5
ETag: "37d4c94839ee181a2224d6242176c4b5"
Content-Type: text/plain; charset=UTF-8
Last-Modified: Thu, 25 Aug 2017 17:49:06 GMT
Content-Length: 11
x-ibm-transition: transition="ARCHIVE", date="Mon, 03 Dec 2018 22:28:38 GMT"
x-amz-restore: ongoing-request="false", expiry-date="Thu, 06 Dec 2018 18:28:38 GMT"
x-amz-storage-class: "GLACIER"
x-ibm-restored-copy-storage-class: "Standard"
```
{: codeblock}
{: http}
{: caption="Exemplo 20. Exemplo mostrando cabeçalhos de resposta." caption-side="bottom"}


### Criar uma configuração de ciclo de vida do depósito
{: #archive-node-create} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  LifecycleConfiguration: {
    Rules: [ /* required */
      {
        Status: 'Enabled', /* required */
        ID: 'STRING_VALUE',
        Filter: '', /* required */
        Prefix: '',
        Transitions: [
          {
            Date: DATE, /* required if Days not specified */
            Days: 0, /* required if Date not specified */
            StorageClass: 'GLACIER' /* required */
          },
        ]
      },
    ]
  }
};

s3.putBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemplo 21. Exemplo mostrando a criação de configuração de ciclo de vida." caption-side="bottom"}

### Recuperar uma configuração de ciclo de vida do depósito
{: #archive-node-retrieve} {: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.getBucketLifecycleConfiguration(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemplo 22. Exemplo mostrando a recuperação de metadados de ciclo de vida." caption-side="bottom"}

### Excluir uma configuração de ciclo de vida do depósito
{: #archive-node-delete} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE' /* required */
};
s3.deleteBucketLifecycle(params, function(err, data) {
  if (err) console.log(err, err.stack); // an error occurred
  else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemplo 23. Exemplo mostrando como excluir a configuração de ciclo de vida de um depósito." caption-side="bottom"}

### Restaurar temporariamente um objeto arquivado 
{: #archive-node-restore} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
  ContentMD5: 'STRING_VALUE', /* required */
  RestoreRequest: {
   Days: 1, /* days until copy expires */
   GlacierJobParameters: {
     Tier: Bulk /* required */
   },
  }
 };
 s3.restoreObject(params, function(err, data) {
   if (err) console.log(err, err.stack); // an error occurred
   else     console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemplo 24. Código usado na restauração de um objeto arquivado." caption-side="bottom"}

### Obter cabeçalhos de um objeto
{: #archive-node-head} 
{: javascript}

```js
var params = {
  Bucket: 'STRING_VALUE', /* required */
  Key: 'STRING_VALUE', /* required */
};
s3.headObject(params, function(err,data) {
  if (err) console.log(err, err.stack); // an error occurred
  else   
    console.log(data);           // successful response
});
```
{: codeblock}
{: javascript}
{: caption="Exemplo 25. Exemplo mostrando a recuperação de cabeçalhos de objeto." caption-side="bottom"}


### Criar uma configuração de ciclo de vida do depósito
{: #archive-python-create} 
{: python}

```py
response = client.put_bucket_lifecycle_configuration(
    Bucket='string',
    LifecycleConfiguration={
        'Rules': [
            {
                'ID': 'string',
                'Status': 'Enabled',
                'Filter': '',
                'Prefix': '',
                'Transitions': [
                    {
                        'Date': datetime(2015, 1, 1),
                        'Days': 123,
                        'StorageClass': 'GLACIER'
                    },
                ]
            },
        ]
    }
)
```
{: codeblock}
{: python}
{: caption="Exemplo 26. Método usado na criação de uma configuração de objeto." caption-side="bottom"}

### Recuperar uma configuração de ciclo de vida do depósito
{: #archive-python-retrieve} 
{: python}

```py
response = client.get_bucket_lifecycle_configuration(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Exemplo 27. Método usado na recuperação de uma configuração de objeto." caption-side="bottom"}

### Excluir uma configuração de ciclo de vida do depósito
{: #archive-python-delete} 
{: python}

```py
response = client.delete_bucket_lifecycle(Bucket='string')
```
{: codeblock}
{: python}
{: caption="Exemplo 28. Método usado na exclusão de configuração de objeto." caption-side="bottom"}

### Restaurar temporariamente um objeto arquivado 
{: #archive-python-restore} 
{: python}

```py
response = client.restore_object(
    Bucket='string',
    Key='string',
    RestoreRequest={
        'Days': 123,
        'GlacierJobParameters': {
            'Tier': 'Bulk'
        },
    }
)
```
{: codeblock}
{: python}
{: caption="Exemplo 29. Restaurando temporariamente um objeto arquivado." caption-side="bottom"}

### Obter cabeçalhos de um objeto
{: #archive-python-head} 
{: python}

```py
response = client.head_object(
    Bucket='string',
    Key='string'
)
```
{: codeblock}
{: python}
{: caption="Exemplo 30. Manipulando a resposta para cabeçalhos de objeto." caption-side="bottom"}


### Criar uma configuração de ciclo de vida do depósito
{: #archive-java-create} 
{: java}

```java
public SetBucketLifecycleConfigurationRequest(String bucketName,
                                              BucketLifecycleConfiguration lifecycleConfiguration)
```
{: codeblock}
{: java}
{: caption="Exemplo 31. Função usada na configuração de um ciclo de vida do depósito." caption-side="bottom"}

**Resumo do método**
{: java}

Método |  Descrição
--- | ---
`getBucketName()` | Obtém o nome do depósito cuja configuração de ciclo de vida está sendo configurada.
`getLifecycleConfiguration()` | Obtém a nova configuração de ciclo de vida para o depósito especificado.
`setBucketName(String bucketName)` | Configura o nome do depósito cuja configuração de ciclo de vida está sendo configurada.
`withBucketName(String bucketName)` | Configura o nome do depósito cuja configuração de ciclo de vida está sendo configurada e retorna esse objeto para que chamadas de método adicionais possam ser encadeadas juntas.
{: java}

### Recuperar uma configuração de ciclo de vida do depósito
{: #archive-java-get} 
{: java}

```java
public GetBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Exemplo 32. Assinatura de função para obter a configuração de ciclo de vida do objeto." caption-side="bottom"}

### Excluir uma configuração de ciclo de vida do depósito
{: #archive-java-put} 
{: java}

```java
public DeleteBucketLifecycleConfigurationRequest(String bucketName)
```
{: codeblock}
{: java}
{: caption="Exemplo 33. Função usada na exclusão de configuração do objeto." caption-side="bottom"}

### Restaurar temporariamente um objeto arquivado 
{: #archive-java-restore} 
{: java}

```java
public RestoreObjectRequest(String bucketName,
                            String key,
                            int expirationInDays)
```
{: codeblock}
{: java}
{: caption="Exemplo 34. Assinatura de função para restaurar um objeto arquivado." caption-side="bottom"}

**Resumo do método**
{: java}

Método |  Descrição
--- | ---
`clone()` | Cria um clone superficial desse objeto para todos os campos, exceto o contexto do manipulador.
`getBucketName()` | Retorna o nome do depósito contendo a referência ao objeto a ser restaurado.
`getExpirationInDays()` | Retorna o tempo em dias da criação de um objeto à sua expiração.
`setExpirationInDays(int expirationInDays)` | Configura o tempo, em dias, entre quando um objeto é transferido por upload para o depósito e quando ele expira.
{: java}

### Obter cabeçalhos de um objeto
{: #archive-java-head} 
{: java}

```java
public ObjectMetadata()
```
{: codeblock}
{: java}
{: caption="Exemplo 35. Função usada na obtenção de cabeçalhos de objeto." caption-side="bottom"}

**Resumo do método**
{: java}

Método |  Descrição
--- | ---
`clone()` | Retorna um clone desse `ObjectMetadata`.
`getRestoreExpirationTime()` | Retorna o tempo no qual um objeto que foi restaurado temporariamente do ARCHIVE expirará e precisará ser restaurado novamente para ser acessado.
`getStorageClass() ` | Retorna a classe de armazenamento original do depósito.
`getIBMTransition()` | Retorne a classe de armazenamento de transição e o tempo de transição.
{: java}

## Próximas Etapas
{: #archive-next-steps}

Além do {{site.data.keyword.cos_full_notm}}, o {{site.data.keyword.cloud_notm}} fornece atualmente várias ofertas de armazenamento de objeto adicionais para diferentes necessidades do usuário. Todas elas podem ser acessadas por meio de portais baseados na web e APIs de REST. [Saiba mais.](https://cloud.ibm.com/docs/services/ibm-cos?topic=ibm-cos-object-storage-in-the-ibm-cloud)
