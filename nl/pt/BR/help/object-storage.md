---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, basics

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

# Sobre o Object Storage
{: #about-cos}

Object Storage é um conceito de tecnologia de armazenamento moderno e uma progressão lógica do armazenamento de bloco e arquivo. O Object Storage existe desde o final de 1990, mas ganhou aceitação e sucesso de mercado nos últimos 10 anos.

O Object Storage foi inventado para superar uma série de problemas:

*  O gerenciamento de dados em escalas muito grandes usando sistemas de bloqueio e de arquivo convencionais era difícil porque essas tecnologias levavam a ilhas de dados devido a limitações em vários níveis da pilha de hardware e software de gerenciamento de dados.

*  O gerenciamento de namespace em escala resultava na manutenção de hierarquias grandes e complexas, que são necessárias para acessar os dados. As limitações em estruturas aninhadas nas matrizes de armazenamento de bloco e de arquivo tradicionais contribuíram ainda mais para a formação de ilhas de dados.

*  O fornecimento da segurança de acesso requeria uma combinação de tecnologias, esquemas de segurança complexa e um envolvimento humano significativo no gerenciamento dessas áreas.

O Object Storage, também conhecido como object-based storage (OBS), usa uma abordagem diferente para armazenar e referenciar dados. Os conceitos de armazenamento de dados do objeto incluem as três construções a seguir:

*  Dados: esses são os dados do usuário e do aplicativo que requerem armazenamento persistente. Eles podem ser texto, formatos binários, multimídia ou qualquer outro conteúdo gerado por pessoa ou por máquina.

*  Metadados: esses são os dados sobre os dados. Eles incluem alguns atributos predefinidos, como tempo e tamanho de upload. O Object Storage permite que os usuários incluam metadados customizados contendo quaisquer informações em pares de chave e valor. Essas informações geralmente contêm informações que são pertinentes ao usuário ou aplicativo que está armazenando os dados e podem ser corrigidas a qualquer momento. Um aspecto exclusivo para a manipulação de metadados nos sistemas de armazenamento de objeto é que os metadados são armazenados com o objeto.

*  Chave: um identificador de recurso exclusivo é designado a cada objeto em um sistema OBS. Essa chave permite que o sistema de armazenamento de objeto diferencie objetos uns dos outros e é usado para localizar os dados sem precisar saber a unidade física exata, a matriz ou o site em que os dados estão.

Essa abordagem permite que o armazenamento de objeto armazene dados em uma hierarquia horizontal simples, que alivia a necessidade de
repositórios de metadados grandes com inibição de desempenho.

O acesso a dados é obtido usando uma interface REST sobre o protocolo HTTP, que permite o acesso em qualquer lugar e a qualquer hora simplesmente referenciando a chave do objeto.
