---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# Transferir arquivos usando CrossFTP
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} é um cliente FTP com recursos completos que suporta soluções de armazenamento em nuvem compatíveis com S3, incluindo o {{site.data.keyword.cos_full}}. O CrossFTP suporta Mac OS X, Microsoft Windows, Linux e é fornecido nas versões Free, Pro e Enterprise com recursos como:

* Interface com guias
* Criptografia de senha
* Procurar
* Transferência em lote
* Criptografia (*Versões Pro/Enterprise*)
* Sincronização (*Versões Pro/Enterprise*)
* Planejador (*Versões Pro/Enterprise*)
* Interface da linha de comandos (*Versões Pro/Enterprise*)

## Conectando-se ao IBM Cloud Object Storage
{: #crossftp-connect}

1. Faça download, instale e inicie o CrossFTP.
2. Na área de janela direita, crie um novo Site por clique no ícone de mais (+) para abrir o Site Manager.
3. Sob a guia *Geral*, insira o seguinte:
    * Configure **Protocolo** como `S3/HTTPS`
    * Configure **Rótulo** para um nome descritivo de sua escolha
    * Configure **Host** para o terminal do {{site.data.keyword.cos_short}} (ou seja, `s3.us.cloud-object-storage.appdomain.cloud`)
        * *Assegure-se de que a região do terminal corresponda ao depósito de destino desejado. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * Deixe **Porta** como `443`
    * Configure **Chave de acesso** e **Segredo** para credenciais HMAC com os direitos de acesso adequados para seu depósito de destino
4. Sob a guia *S3*
    * Assegure-se de que `Use DevPay` esteja desmarcado
    * Clique em **Conjunto de APIs...** e assegure-se de que `Dev Pay` e `CloudFront Distribution` estejam desmarcados
5. ***Somente para Mac OS X***
    * Clique em *Segurança > Protocolos TLS/SSL...* na barra de menus
    * Selecione a opção `Customize the enabled protocols`
    * Inclua `TLSv1.2` na caixa **Ativado**
    * Clique em **OK**
6. ***Somente para Linux***
    * Clique em *Segurança > Configurações de cifra...* na barra de menus
    * Selecione a opção `Customize the enabled cipher suites`
    * Inclua `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` na caixa **Ativado**
    * Clique em **OK**
7. Clique em **Aplicar**, em seguida, **Fechar**
8. Uma nova entrada em *Sites* deve estar disponível com o *Rótulo* fornecido na etapa 3
9. Clique duas vezes na nova entrada para se conectar ao terminal

Aqui, a janela exibe uma lista dos depósitos disponíveis e é possível procurar os arquivos disponíveis e transferi-los para e de seus discos locais.
