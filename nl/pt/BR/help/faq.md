---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# Perguntas mais frequentes
{: #faq}

## Perguntas sobre API
{: #faq-api}

**Os nomes dos depósitos do {{site.data.keyword.cos_full}} fazem distinção entre maiúsculas e minúsculas?**

É necessário que os nomes dos depósitos sejam endereçáveis por DNS e, portanto, não façam distinção entre maiúsculas e minúsculas.

**Qual é o número máximo de caracteres que podem ser usados em um nome de Objeto?**

1024

**Como posso descobrir o tamanho total do meu depósito usando a API?**

Não é possível buscar o tamanho de um depósito com uma única solicitação. Será necessário listar o conteúdo de um depósito e somar o tamanho de cada objeto.

**Posso migrar dados do AWS S3 para o {{site.data.keyword.cos_full_notm}}?**

Sim, é possível usar as ferramentas existentes para ler e gravar dados no {{site.data.keyword.cos_full_notm}}. Será necessário configurar as credenciais HMAC para que suas ferramentas sejam autenticadas. Nem todas as ferramentas compatíveis com S3 são atualmente não suportadas. Para obter mais detalhes, consulte [Usando credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).


## Perguntas sobre oferta
{: #faq-offering}

**Há um limite de 100 depósitos para uma conta? O que acontecerá se precisarmos de mais?**

Sim, 100 é o limite atual de depósitos. Geralmente, os prefixos são uma maneira melhor de agrupar objetos em um depósito, a menos que os dados precisem estar em uma região ou classe de armazenamento diferente. Por exemplo, para agrupar registros de pacientes, você usaria um prefixo por paciente. Se essa não for uma solução viável, entre em contato com o suporte ao cliente.

**Se eu desejar armazenar meus dados usando o {{site.data.keyword.cos_full_notm}} Vault ou Cold Vault, eu precisarei criar outra conta?**

Não, as classes de armazenamento (e as regiões também) são definidas no nível do depósito. Simplesmente crie um novo depósito que esteja configurado para a classe de armazenamento desejada.

**Quando eu crio um depósito usando a API, como configuro a classe de armazenamento?**

A classe de armazenamento (por exemplo, `us-flex`) é designada à variável de configuração `LocationConstraint` para esse depósito. Isso é devido a uma diferença chave entre a maneira como o AWS S3 e o {{site.data.keyword.cos_full_notm}} manipulam as classes de armazenamento. O {{site.data.keyword.cos_short}} configura as classes de armazenamento no nível do depósito, enquanto o AWS S3 designa uma classe de armazenamento a um objeto individual. Uma lista de códigos de fornecimento válidos para `LocationConstraint` pode ser referenciada no [Guia de Classes de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes).

**A classe de armazenamento de um depósito pode ser mudada? Por exemplo, se tivermos dados de produção em 'standard', poderemos facilmente alterná-los para 'vault' para propósitos de faturamento se não estivermos usando-os frequentemente?**

Hoje, a mudança da classe de armazenamento requer a movimentação ou cópia manual dos dados de um depósito para outro com a classe de armazenamento desejada.


## Perguntas sobre desempenho
{: #faq-performance}

**A consistência de dados no {{site.data.keyword.cos_short}} é fornecida com um impacto no desempenho?**

A consistência com qualquer sistema distribuído é fornecida com um custo, mas a eficiência do sistema de armazenamento disperso do {{site.data.keyword.cos_full_notm}} é maior e a sobrecarga é inferior em comparação a sistemas com múltiplas cópias síncronas.

**Não haverá implicações de desempenho se o meu aplicativo precisar manipular objetos grandes?**

Para otimização de desempenho, os objetos podem ser transferidos por upload e por download em múltiplas partes, em paralelo.


## Perguntas sobre criptografia
{: #faq-encryption}

**O {{site.data.keyword.cos_short}} fornece criptografia em repouso e em movimento?**

Sim. Os dados em repouso são criptografados com criptografia Advanced Encryption Standard (AES) de 256 bits automática do lado do provedor e hash do Secure Hash Algorithm (SHA)-256. Os dados em movimento são protegidos usando Segurança da Camada de Transporte/Secure Sockets Layer (TLS/SSL) de classificação de transportadora integrada ou SNMPv3 com criptografia AES.

**Qual é a sobrecarga de criptografia típica se um cliente deseja criptografar seus dados?**

A criptografia do lado do servidor está sempre ativada para dados do cliente. Em comparação com o hashing necessário na autenticação S3 e a codificação de apagamento, a criptografia não é uma grande parte do custo de processamento do COS.

**O {{site.data.keyword.cos_short}} criptografa todos os dados?**

Sim, o {{site.data.keyword.cos_short}} criptografa todos os dados.

**O {{site.data.keyword.cos_short}} tem conformidade com FIPS 140-2 para os algoritmos de criptografia?**

Sim, a oferta IBM COS Federal é aprovada para controles de Segurança Moderada do FedRAMP que requerem uma configuração FIPS validada. O IBM COS Federal é certificado no nível 1 do FIPS 140-2. Para obter mais informações sobre a oferta COS Federal, [entre em contato conosco](https://www.ibm.com/cloud/government) por meio de nosso site Federal.

**A criptografia de chave de cliente do cliente será suportada?**

Sim, a criptografia de chave do cliente é suportada usando SSE-C ou Key Protect.

## Perguntas gerais
{: #faq-general}

**Quantos objetos podem caber em um único depósito?**

Não há limite prático para o número de objetos em um único depósito.

**Posso aninhar depósitos um dentro do outro?**

Não, os depósitos não podem ser aninhados. Se um nível superior de organização for necessário em um depósito, o uso de prefixos será suportado: `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. Observe que a chave do objeto permanece a combinação `{object-prefix}/{object-name}`.

**Qual é a diferença entre as solicitações de 'Classe A' e 'Classe B'?**

As solicitações de 'Classe A' são operações que envolvem modificação ou listagem. Isso inclui criar depósitos, fazer upload ou copiar objetos, criar ou mudar configurações, listar depósitos e listar o conteúdo de depósitos. As solicitações de 'Classe B' são aquelas relacionadas à recuperação de objetos ou seus metadados/configurações associados do sistema. Não há encargos para excluir depósitos ou objetos do sistema.

**Qual é a melhor maneira de estruturar seus dados usando o Object Storage de forma que seja possível 'olhar' para eles e localizar o que você está procurando? Sem uma estrutura de diretório, ter milhares de arquivos em um nível parece difícil de visualizar.**

É possível usar metadados associados a cada objeto para localizar os objetos que você está procurando. A maior vantagem do armazenamento de objeto são os metadados associados a cada objeto. Cada objeto pode ter até 4 MB de metadados no {{site.data.keyword.cos_short}}. Quando transferido para um banco de dados, os metadados fornecem excelentes recursos de procura. Um grande número de pares (chave, valor) pode ser armazenado em 4 MB. Também é possível usar a procura de Prefixo para localizar o que você está procurando. Por exemplo, se você usar depósitos para separar cada um dos dados do cliente, será possível usar prefixos dentro de depósitos para organização. Por exemplo: /bucket1/folder/object em que 'folder/' é o prefixo.

**É possível confirmar se o {{site.data.keyword.cos_short}} é ‘imediatamente consistente’, em vez de ‘finalmente consistente’?**

O {{site.data.keyword.cos_short}} é ‘imediatamente consistente’ para os dados e ‘finalmente consistente’ para a contabilidade de uso.


**O {{site.data.keyword.cos_short}} pode particionar os dados automaticamente para mim como HDFS, para que seja possível ler as partições em paralelo, por exemplo, com o Spark?**

O {{site.data.keyword.cos_short}} suporta um GET classificado no objeto, portanto, um aplicativo pode executar uma operação distribuída de tipo de leitura dividida. A execução de divisão seria no aplicativo a ser gerenciado.
