---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, cyberduck

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

# Transferir arquivos com o Cyberduck
{: #cyberduck}

O Cyberduck é um navegador do Object Storage em nuvem popular, de software livre e fácil de usar para Mac e Windows. O Cyberduck é capaz de calcular as assinaturas de autorização corretas que são necessárias para se conectar ao IBM COS. O Cyberduck pode ser transferido por download por meio de [cyberduck.io/](https://cyberduck.io/){: new_window}.

Para usar o Cyberduck para criar uma conexão com o IBM COS e sincronizar uma pasta de arquivos locais em um depósito, siga estas etapas:

 1. Faça download, instale e inicie o Cyberduck.
 2. A janela principal do aplicativo é aberta, na qual é possível criar uma conexão com o IBM COS. Clique em **Abrir conexão** para configurar uma conexão com o IBM COS.
 3. Uma janela pop-up é aberta. No menu suspenso na parte superior, selecione `S3 (HTTPS)`. Insira as informações nos campos a seguir e, em seguida, clique em Conectar:

    * `Server`: insira o terminal do IBM COS
        * *Assegure-se de que a região do terminal corresponda ao depósito desejado. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `ID da chave de acesso`
    * `Chave de acesso secreta`
    * `Add to Keychain`: salve a conexão com o keychain para permitir o uso em outros aplicativos *(opcional)*

 4. O Cyberduck leva você para a raiz da conta na qual os depósitos podem ser criados.
    * Clique com o botão direito na área de janela principal e selecione **Nova pasta** (*o aplicativo lida com muitos protocolos de transferência em que a Pasta é a construção de contêiner mais comum*).
    * Insira o nome do depósito e, em seguida, clique em Criar.
 5. Depois que o depósito for criado, clique duas vezes no depósito para visualizá-lo. Dentro do depósito, é possível executar várias funções, tais como:
    * Fazer upload de arquivos no depósito
    * Listar conteúdo do depósito
    * Fazer download de objetos do depósito
    * Sincronizar arquivos locais com um depósito
    * Sincronizar objetos com outro depósito
    * Criar um archive de um depósito
 6. Clique com o botão direito no depósito e selecione **Sincronizar**. Uma janela pop-up é aberta, na qual é possível navegar para a pasta que você deseja sincronizar com o depósito. Selecione a pasta e clique em Escolher.
 7. Depois de selecionar a pasta, uma nova janela pop-up é aberta. Aqui, um menu suspenso está disponível no qual você seleciona a operação de sincronização com o depósito. Três opções possíveis de sincronização estão disponíveis no menu:

    * `Download`: isso faz download de objetos mudados e ausentes do depósito.
    * `Upload`: isso faz upload de arquivos mudados e ausentes para o depósito.
    * `Mirror`: isso executa as operações de download e upload, assegurando que todos os arquivos e objetos novos e atualizados sejam sincronizados entre a pasta local e o depósito.

 8. Outra janela é aberta para mostrar solicitações de transferência ativas e históricas. Depois que a solicitação de sincronização for concluída, a janela principal executará uma operação de lista no depósito para refletir o conteúdo atualizado no depósito.

## Mountain Duck
{: #mountain-duck}

O Mountain Duck constrói o Cyberduck para permitir que você monte o Object Storage em nuvem como um disco no Localizador no Mac ou no Explorer no Windows. As versões de avaliação estão disponíveis, mas uma chave de registro é necessária para uso continuado.

A criação de um marcador no Mountain Duck é muito semelhante à criação de conexões no Cyberduck:

1. Faça download, instale e inicie o Mountain Duck
2. Criar um Novo marcador
3. No menu suspenso, selecione `S3 (HTTPS)` e insira as informações a seguir:
    * `Server`: insira o terminal do IBM COS 
        * *Assegure-se de que a região do terminal corresponda ao depósito desejado. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * `Username`: insira a Chave de acesso
    * Clique em **Conectar**
    * Você será solicitado a fornecer a Chave secreta, que será, então, salva no keychain.

Seus depósitos agora estarão disponíveis no Localizador ou no Explorer. É possível interagir com {{site.data.keyword.cos_short}} como qualquer outro sistema de arquivos montado.

## CLI
{: #cyberduck-cli}

O Cyberduck também fornece o `duck`, uma interface da linha de comandos (CLI) que é executada no shell no Linux, Mac OS X e Windows. As instruções de instalação estão disponíveis na [página da wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Installation){:new_window} do `duck`.

Para usar o `duck` com o {{site.data.keyword.cos_full}}, um perfil customizado precisa ser incluído no [Diretório de suporte a aplicativos](https://trac.cyberduck.io/wiki/help/en/howto/cli#Profiles){:new_window}. Informações detalhadas sobre os perfis de conexão `duck`, incluindo perfis de amostra e pré-configurados, estão disponíveis na [CLI help/how-to](https://trac.cyberduck.io/wiki/help/en/howto/profiles){: new_window}.

Abaixo está um perfil de exemplo para um terminal regional do COS:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
    <dict>
        <key>Protocol</key>
        <string>s3</string>
        <key>Vendor</key>
        <string>cos</string>
        <key>Scheme</key>
        <string>https</string>
	    <key>Default Hostname</key>
	    <string>s3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net</string>
        <key>Description</key>
        <string>IBM COS</string>
        <key>Default Port</key>
        <string>443</string>
        <key>Hostname Configurable</key>
        <true/>
        <key>Port Configurable</key>
        <true/>
        <key>Username Configurable</key>
        <true/>
    </dict>
</plist>
```

Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

A inclusão desse perfil no `duck` permite acessar o {{site.data.keyword.cos_short}} usando um comando semelhante ao abaixo:

```
duck --nokeychain --longlist cos://<bucket-name> --username <access-key> --password <secret-access-key>
```

*Valores da chave*
* `<bucket-name>` - nome do depósito do COS (*assegure-se de que as regiões de depósito e terminal estejam consistentes*)
* `<access-key>` - chave de acesso HMAC
* `<secret-access-key>` - chave secreta HMAC

```
Login successful…
---	May 31, 2018 1:48:16 AM		mynewfile1.txt
---	May 31, 2018 1:49:26 AM		mynewfile12.txt
---	Aug 10, 2018 9:49:08 AM		newbigfile.pdf
---	May 29, 2018 3:36:50 PM		newkptestfile.txt
```

Uma lista integral de opções de linha de comandos está disponível inserindo `duck --help` no shell ou visitando o [site da wiki](https://trac.cyberduck.io/wiki/help/en/howto/cli#Usage){:new_window}
