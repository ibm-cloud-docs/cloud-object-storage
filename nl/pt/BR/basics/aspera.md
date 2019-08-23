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

# Usar o Aspera high-speed transfer
{: #aspera}

O Aspera high-speed transfer supera as limitações de transferências tradicionais de FTP e HTTP para melhorar o desempenho da transferência de dados sob a maioria das condições, especialmente em redes com alta latência e perda de pacote. Em vez do HTTP padrão `PUT`, o Aspera high-speed transfer faz upload do objeto usando o [protocolo FASP](https://asperasoft.com/technology/transport/fasp/). O uso do Aspera high-speed transfer para uploads e downloads oferece os benefícios a seguir:

- Velocidades de transferência mais rápida
- Transferir uploads de objeto grande acima de 200 MB no console e 1 GB usando um SDK ou uma biblioteca
- Fazer upload de pastas inteiras de qualquer tipo de dados, incluindo arquivos de multimídia, imagens de disco e quaisquer outros dados estruturados ou não estruturados
- Customizar velocidades de transferência e preferências padrão
- As transferências podem ser visualizadas, pausadas/continuadas ou canceladas independentemente

O Aspera high-speed transfer está disponível no [console](#aspera-console) do {{site.data.keyword.cloud_notm}} e também pode ser usado programaticamente usando um [SDK](#aspera-sdk). 

O Aspera high-speed transfer está disponível somente em certas regiões. Consulte [Serviços integrados](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability) para obter mais detalhes.
{:tip}

## Utilizando o Console
{: #aspera-console}

Ao criar um depósito em uma [região suportada](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability), você tem a opção de selecionar o Aspera high-speed transfer fazer upload de arquivos ou pastas. Depois de tentar fazer upload de um objeto, você é solicitado a instalar o cliente Aspera Connect.

### Instalar o Aspera Connect
{: #aspera-install}

1. Selecione **Instalar o cliente Aspera Connect**.
2. Siga as instruções de instalação, dependendo de seu sistema operacional e navegador.
3. Continue o upload de arquivo ou pasta.

O plug-in Aspera Connect também pode ser instalado no [website do Aspera](https://downloads.asperasoft.com/connect2/) diretamente. Para obter problemas de resolução de problemas com o plug-in Aspera Connect, [consulte a documentação](https://downloads.asperasoft.com/en/documentation/8).

Quando o plug-in estiver instalado, você terá a opção de configurar o Aspera high-speed transfer como o padrão para quaisquer uploads para o depósito de destino que usam o mesmo navegador. Selecione **Lembrar minhas preferências do navegador**. As opções também estão disponíveis na página de configuração do depósito em **Opções de transferência**. Essas opções permitem que você escolha entre Standard e High-speed como o transporte padrão para uploads e downloads.

Geralmente, usar o console baseado na web do IBM Cloud Object Storage não é a maneira mais comum de usar o {{site.data.keyword.cos_short}}. A opção de transferência Standard limita o tamanho dos objetos para 200 MB e o nome do arquivo e a chave serão idênticos. O suporte para tamanhos de objetos maiores e desempenho melhorado (dependendo de fatores de rede) é fornecido pelo Aspera high-speed transfer.

### Status da transferência
{: #aspera-console-transfer-status}

**Ativo:** depois de iniciar uma transferência, o status da transferência é exibido como ativo. Embora a transferência esteja ativa, é possível pausar, continuar ou cancelar uma transferência ativa. 

**Concluído:** após a conclusão de sua transferência, informações sobre isso e todas as transferências nesta sessão são exibidas na guia Concluído. É possível limpar essas informações. Você verá somente informações sobre as transferências concluídas na sessão atual.

**Preferências:** é possível configurar o padrão para uploads e/ou downloads para a Alta velocidade.

Os downloads usando o Aspera high-speed transfer incorrem em encargos de egresso. Para obter mais informações, consulte a [página de precificação](https://www.ibm.com/cloud/object-storage).
{:tip}

**Preferências avançadas:** é possível configurar a largura da banda para uploads e downloads.

----

## Usando bibliotecas e SDKs
{: #aspera-sdk}

O SDK do Aspera high-speed transfer fornece a capacidade de iniciar a transferência em alta velocidade em seus aplicativos customizados ao usar o Java ou o Python.

### Quando usar o Aspera high-speed transfer
{: #aspera-guidance}

O protocolo FASP que o Aspera high-speed transfer usa não é adequado para todas as transferências de dados do COS e para ele. Especificamente, quaisquer transferências que fazem uso do Aspera high-speed transfer devem:

1. Sempre fazer uso de múltiplas sessões - pelo menos duas sessões paralelas utilizarão melhor os recursos dos Aspera high-speed transfers. Consulte a orientação específica para [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) e [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
2. O Aspera high-speed transfer é ideal para arquivos maiores e quaisquer arquivos ou diretórios que contêm uma quantia total de dados menor que 1 GB devem, em vez disso, transferir o objeto em múltiplas partes usando as classes padrão do Transfer Manager. Os Aspera high-speed transfers requerem um tempo maior para o primeiro byte do que as transferências HTTP normais. A instanciação de muitos objetos do Aspera Transfer Manager para gerenciar as transferências de arquivos menores individuais pode resultar no desempenho do subpar com relação a solicitações de HTTP básicas, portanto, é melhor instanciar um único cliente para fazer upload de um diretório de arquivos menores.
3. O Aspera high-speed transfer foi projetado em parte para melhorar o desempenho em ambientes de rede com grandes quantias de perda de pacote, tornando o protocolo eficaz em grandes distâncias e redes públicas de longa distância. O Aspera high-speed transfer não deve ser usado para as transferências em uma região ou um data center.

O SDK do Aspera high-speed transfer é de origem fechada e, portanto, uma dependência opcional para o SDK do COS (que usa uma licença Apache).
{:tip}

#### Pacote do COS/Aspera High-Speed Transfer
{: #aspera-packaging}

A imagem abaixo exibe uma visão geral resumida de como o SDK do COS interage com a biblioteca do Aspera high-speed transfer para fornecer funcionalidade.

<img alt="SDK do Aspera High-Speed Transfer." src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="Figura 1: SDK do COS/Aspera High-Speed Transfer." caption-side="bottom"} 

### Plataformas suportadas
{: #aspera-sdk-platforms}

| Sistema Operacional                     | Versão   | Arquitetura | Versão Java testada | Versão Python testada |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64 bits      | 6 e superior | 2.7, 3.6       |
| Mac OS X               | 10.13     | 64 bits      | 6 e superior | 2.7, 3.6       |
| Microsoft&reg; Windows | 10        | 64 bits      | 6 e superior | 2.7, 3.6       |

Cada sessão do Aspera high-speed transfer gera um processo `ascp` individual que é executado na máquina do cliente para executar a transferência. Assegure-se de que seu ambiente de computação possa permitir que esse processo seja executado.
{:tip}

**Limitações adicionais**

* Binários de 32 bits não são suportados
* O suporte ao Windows requer o Windows 10
* O suporte ao Linux é limitado ao Ubuntu (testado com relação ao LTS 18.04)
* Os clientes do Aspera Transfer Manager devem ser criados usando as chaves de API do IAM e não as credenciais HMAC.

### Obtendo o SDK usando Java
{: #aspera-sdk-java} 
{: java}

A melhor maneira de usar o SDK Java do {{site.data.keyword.cos_full_notm}} e do Aspera high-speed transfer é usar o Maven para gerenciar dependências. Se você não estiver familiarizado com o Maven, poderá colocá-lo em funcionamento usando o guia [Maven em 5 minutos](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window}.
{: java}

O Maven usa um arquivo chamado `pom.xml` para especificar as bibliotecas (e suas versões) necessárias para um projeto Java. Abaixo está um arquivo `pom.xml` de exemplo para usar o SDK Java do {{site.data.keyword.cos_full_notm}} e do Aspera high-speed transfer
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

Exemplos de iniciação dos Aspera high-speed transfers com Java estão disponíveis na seção [Usando o Aspera High-Speed Transfer](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera).
{: java}

### Obtendo o SDK usando Python
{: #aspera-sdk-python} 
{: python}

Os SDKs Python do {{site.data.keyword.cos_full_notm}} e do Aspera high-speed transfer estão disponíveis no repositório de software Python Package Index (PyPI).
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Os exemplos de iniciação de transferências do Aspera com Python estão disponíveis na seção [Usando o Aspera High-Speed Transfer](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
{: python}
