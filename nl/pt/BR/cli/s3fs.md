---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: s3fs, open source, file system, gateway

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

# Montar um depósito usando `s3fs`
{: #s3fs}

Os aplicativos que esperam ler e gravar em um sistema de arquivos no estilo NFS podem usar `s3fs`, que pode montar um depósito como diretório enquanto preserva o formato de objeto nativo para arquivos. Isso permite que você interaja com seu armazenamento em nuvem usando comandos shell familiares, como `ls` para listar ou `cp` para copiar arquivos, bem como fornece acesso a aplicativos anteriores que dependem de leitura e gravação de arquivos locais. Para obter uma visão geral mais detalhada, [visite o LEIA-ME oficial do projeto](https://github.com/s3fs-fuse/s3fs-fuse).

## Pré-requisitos
{: #s3fs-prereqs}

* Uma conta do IBM Cloud e uma instância do {{site.data.keyword.cos_full}}
* Um ambiente Linux ou OSX
* Credenciais (uma [chave de API do IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) ou [credenciais HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## Instalação
{: #s3fs-install}

No OSX, use [Homebrew](https://brew.sh/):

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

No Debian ou Ubuntu: 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

A documentação oficial do `s3fs` sugere o uso de `libcurl4-gnutls-dev` em vez de `libcurl4-openssl-dev`. Ambos funcionam, mas a versão do OpenSSL pode resultar em melhor desempenho.
{:tip}

Também é possível construir `s3fs` por meio da origem. Primeiro clone o repositório Github:

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

Em seguida, construa `s3fs`:

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

E instale o binário:

```sh
sudo make install
```
{:codeblock}

## Configuração
{: #s3fs-config}

Armazene suas credenciais em um arquivo que contenha `<access_key>:<secret_key>` ou `:<api_key>`. Esse arquivo precisa ter acesso limitado, então, execute:

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

Agora é possível montar um depósito usando:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

Se o arquivo de credenciais tiver somente uma chave de API (sem credenciais HMAC), será necessário incluir a sinalização `ibm_iam_auth` também:

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

O `<bucket>` é um depósito existente e o `<mountpoint>` é o diretório local no qual você deseja montar o depósito. O `<endpoint>` deve corresponder ao [local do depósito](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints). O `credentials_file` é o arquivo criado com a chave de API ou credenciais HMAC.

Agora, `ls <mountpoint>` listará os objetos nesse depósito como se eles fossem arquivos locais (ou, no caso de prefixos de objeto, como se eles fossem diretórios aninhados).

## Otimização de desempenho
{: #s3fs-performance}

Embora o desempenho nunca seja igual a um sistema de arquivos local verdadeiro, é possível usar algumas opções avançadas para aumentar o rendimento. 

```sh
s3fs <bucket_name> <mountpoint> -o url=http{s}://<COS_endpoint> –o passwd_file=<credentials_file> \
-o cipher_suites=AESGCM \
-o kernel_cache \
-o max_background=1000 \
-o max_stat_cache_size=100000 \
-o multipart_size=52 \
-o parallel_count=30 \
-o multireq_max=30 \
-o dbglevel=warn
```
{:codeblock}

1. `cipher_suites=AESGCM` é relevante somente ao usar um terminal HTTPS. Por padrão, as conexões seguras com o IBM COS usam o conjunto de cifras `AES256-SHA`. O uso de um conjunto `AESGCM` em seu lugar reduz significativamente a sobrecarga da CPU em sua máquina do cliente, causada pelas funções de criptografia TLS, enquanto oferece o mesmo nível de segurança criptográfica.
2. `kernel_cache` ativa o cache de buffer do kernel em seu ponto de montagem do `s3fs`. Isso significa que os objetos serão lidos somente uma vez por `s3fs`, uma vez que a leitura repetitiva do mesmo arquivo pode ser entregue por meio do cache de buffer do kernel. O cache de buffer do kernel usará somente a memória livre que não estiver em uso por outros processos. Essa opção não é recomendada se você espera que os objetos do depósito sejam sobrescritos de outro processo/máquina enquanto o depósito é montado e seu caso de uso requer acesso em tempo real ao conteúdo mais atualizado. 
3. `max_background=1000` melhora o desempenho de leitura de arquivo simultâneo do `s3fs`. Por padrão, FUSE suporta solicitações de leitura de arquivo de até 128 KB. Ao pedir para ler mais do que isso, o kernel divide a solicitação grande para subsolicitações menores e permite que o s3fs as processe de forma assíncrona. A opção `max_background` configura o número máximo global de tais solicitações assíncronas simultâneas. Por padrão, ela é configurada como 12, mas configurá-la para um valor alto arbitrário (1000) evita que solicitações de leitura sejam bloqueadas, mesmo ao ler um grande número de arquivos simultaneamente.
4. `max_stat_cache_size=100000` reduz o número de solicitações de HTTP `HEAD` redundantes enviadas por `s3fs` e reduz o tempo que leva para listar um diretório ou recuperar atributos de arquivo. O uso típico do sistema de arquivos torna frequente o acesso aos metadados de um arquivo por meio de uma chamada `stat()` que é mapeada para a solicitação `HEAD` no sistema de armazenamento de objeto. Por padrão, o `s3fs` armazena em cache os atributos (metadados) de até 1000 objetos. Cada entrada em cache leva até 0,5 KB de memória. Idealmente, você desejaria que o cache fosse capaz de reter os metadados de todos os objetos em seu depósito. No entanto, você pode desejar considerar as implicações de uso de memória desse armazenamento em cache. Configurá-lo como `100000` não tomará mais que 0,5 KB * 100000 = 50 MB.
5. `multipart_size=52` configurará o tamanho máximo de solicitações e respostas enviadas e recebidas do servidor COS, em escala de MB. `s3fs` configura isso como 10 MB por padrão. Aumentar esse valor também aumenta o rendimento (MB/s) por conexão HTTP. Por outro lado, a latência para o primeiro byte entregue por meio do arquivo aumentará, respectivamente. Portanto, se o seu caso de uso lê somente uma pequena quantia de dados de cada arquivo, provavelmente você não deseja aumentar esse valor. Além disso, para objetos grandes (digamos, mais de 50 MB), o rendimento aumentará se esse valor for pequeno o suficiente para permitir que o arquivo seja buscado simultaneamente usando múltiplas solicitações. Eu acho que o valor ideal para essa opção é de cerca de 50 MB. As melhores práticas do COS sugerem o uso de solicitações que são múltiplos de 4 MB e, portanto, a recomendação é configurar essa opção para 52 (MB).
6. `parallel_count=30` configura o número máximo de solicitações enviadas simultaneamente para o COS, por operação de leitura/gravação de arquivo único. Por padrão, isso é configurado como 5. Para objetos muito grandes, é possível obter mais rendimento aumentando esse valor. Como com a opção anterior, mantenha esse valor baixo se você lê somente uma pequena quantia de dados de cada arquivo.
7. `multireq_max=30` Ao listar um diretório, uma solicitação de metadados de objeto (`HEAD`) é enviada por cada objeto na listagem (a menos que os metadados estejam localizados em cache). Essa opção limita o número de solicitações simultâneas enviadas ao COS para uma única operação de listagem de diretórios. Por padrão, ela é configurada como 20. Observe que esse valor deve ser maior ou igual à opção `parallel_count` acima.
8. `dbglevel=warn` configura o nível de depuração como `warn` em vez do padrão (`crit`) para registrar mensagens em /var/log/syslog.

## Limitações
{: #s3fs-limitations}

É importante lembrar que o s3fs pode não ser adequado para todos os aplicativos, pois os serviços de armazenamento de objeto têm alta latência de tempo para o primeiro byte e não possuem acesso de gravação aleatória. As cargas de trabalho que leem somente arquivos grandes, como cargas de trabalho de deep learning, podem alcançar um bom rendimento usando `s3fs`. 
