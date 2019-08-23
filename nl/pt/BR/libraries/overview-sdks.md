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

# Sobre SDKs do IBM COS
{: #sdk-about}

O IBM COS fornece SDKs para Java, Python, NodeJS e Go. Esses SDKs são baseados nos SDKs oficiais da API S3 da AWS, mas foram modificados para usar recursos do IBM Cloud como IAM, Key Protect, Immutable Object Storage e outros.

| Variável                     | Java                                              | Python                                            | NodeJS                                            | GO                                                | CLI                                               |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| Suporte à chave de API do IAM | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |
| Uploads de múltiplas partes gerenciados | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |
| Downloads de múltiplas partes gerenciados | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Listagem ampliada de depósitos |                                                   |                                                   |                                                   |                                                   |                                                   |
| Listagem de objetos da versão 2 | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Regras de archive           | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Políticas de retenção       | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aspera high-speed transfer  | ![Ícone de visto](../../icons/checkmark-icon.svg) | ![Ícone de visto](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## Suporte à chave de API do IAM
{: #sdk-about-iam}
Permite a criação de clientes com uma chave de API em vez de um par de chaves de Acesso/Secretas. O gerenciamento de tokens é manipulado automaticamente e os tokens são atualizados automaticamente durante as operações de longa execução.
## Uploads de múltiplas partes gerenciados
Usando uma classe `TransferManager`, o SDK manipulará toda a lógica necessária para fazer upload de objetos em múltiplas partes.
## Downloads de múltiplas partes gerenciados
Usando uma classe `TransferManager`, o SDK manipulará toda a lógica necessária para fazer download de objetos em múltiplas partes.
## Listagem ampliada de depósitos 
Esta é uma extensão para a API S3 que retorna uma lista de depósitos com códigos de fornecimento (uma combinação do local e da classe de armazenamento do depósito, retornados como `LocationConstraint`) para depósitos ao listar. Isso é útil para localizar um depósito, uma vez que os depósitos em uma instância de serviço são todos listados, independentemente do terminal usado.
## Listagem de objetos da versão 2
A listagem da versão 2 permite uma definição de escopo mais poderosa de listagens de objetos.
## Key Protect
O Key Protect é um serviço do IBM Cloud que gerencia chaves de criptografia e é um parâmetro opcional durante a criação do depósito.
## SSE-C                      
## Regras de archive              
## Políticas de retenção         
## Aspera high-speed transfer 
