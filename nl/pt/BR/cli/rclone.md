---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# Usando `rclone`
{: #rclone}

## Instalar o `rclone`
{: #rclone-install}

A ferramenta `rclone` é útil para manter os diretórios sincronizados e para migrar dados entre as plataformas de armazenamento. É um programa Go e vem como um único arquivo binário.

### Instalação da iniciação rápida
{: #rclone-quick}

*  [Faça download](https://rclone.org/downloads/) do binário relevante. 
*  Extraia o binário `rclone` ou `rclone.exe` do archive.
*  Execute `rclone config` para configurar.

### Instalação usando um script
{: #rclone-script}

Instale o `rclone` em sistemas Linux/macOS/BSD:

```
curl https://rclone.org/install.sh | sudo bash
```

As versões beta estão disponíveis também:

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

O script de instalação verifica a versão do `rclone` instalada primeiro e ignora o download se a versão atual já está atualizada.
{:note}

### Instalação do Linux de binário pré-compilado
{: #rclone-linux-binary}

Primeiro, busque e descompacte o binário:

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

Em seguida, copie o arquivo binário para um local sensível:

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

Instale a documentação:

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

Execute `rclone config` para configurar:

```
rclone config
```

### Instalação do macOS de binário pré-compilado
{: #rclone-osx-binary}

Primeiro, faça download do pacote `rclone`:

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

Em seguida, extraia o arquivo transferido por download e `cd` para a pasta extraída:

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

Mova `rclone` para seu `$PATH` e insira sua senha quando solicitado:

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

O comando `mkdir` é seguro para ser executado, mesmo se o diretório existe.
{:tip}

Remova os arquivos restantes.

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

Execute `rclone config` para configurar:

```
rclone config
```

## Configurar o acesso ao IBM COS
{: #rclone-config}

1. Execute `rclone config` e selecione `n` para um novo remoto.

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. Insira o nome para a configuração:
```
	name> <YOUR NAME>
```

3. Selecione o armazenamento “s3”.

```
	Choose a number from below, or type in your own value
		1 / Alias for a existing remote
		\ "alias"
		2 / Amazon Drive
		\ "amazon cloud drive"
		3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
		\ "s3"
		4 / Backblaze B2
		\ "b2"
	[snip]
		23 / http Connection
	  \ "http"
	Storage> 3
```

  4. Selecione IBM COS como o Provedor de armazenamento S3.

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  1. Insira **False** para inserir suas credenciais.

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. Insira a Chave de acesso e o Segredo.

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. Especifique o terminal para o IBM COS. Para o IBM COS público, escolha dentre as opções fornecidas. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```
Endpoint for IBM COS S3 API.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3.us.cloud-object-storage.appdomain.cloud"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. Especifique uma Restrição de local do IBM COS. A restrição de local deve corresponder ao terminal. Para obter mais informações sobre terminais, consulte [Terminais e locais de armazenamento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. Especifique uma ACL. Somente `public-read` e `private` são suportados. 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. Revise a configuração exibida e aceite para salvar o “remoto”, em seguida, encerre. O arquivo de configuração deve ser semelhante a este

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## Referência de Comandos
{: #rclone-reference}

### Criar um depósito
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### Listar depósitos disponíveis
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### Listar o conteúdo de um depósito
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### Copiar um arquivo de local para remoto
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### Copiar um arquivo de remoto para local
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### Excluir um arquivo no remoto
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### Comandos de lista
{: #rclone-reference-listing}

Há vários comandos de lista relacionados
* `ls` para listar o tamanho e o caminho somente de objetos
* `lsl` para listar o horário de modificação, o tamanho e o caminho somente de objetos
* `lsd` para listar somente diretórios
* `lsf` para listar objetos e diretórios em formato fácil de analisar
* `lsjson` para listar objetos e diretórios em formato JSON

## `rclone sync`
{: #rclone-sync}

A operação `sync` torna a origem e o destino idênticos e modifica somente o destino. A sincronização não transfere os arquivos inalterados, testando por tamanho e horário de modificação ou MD5SUM. O destino é atualizado para corresponder à origem, incluindo a exclusão de arquivos, se necessário.

Como isso pode causar perda de dados, teste primeiro com a sinalização `--dry-run` para ver exatamente o que seria copiado e excluído.
{:important}

Observe que os arquivos no destino não serão excluídos se houver algum erro em qualquer ponto.

O _conteúdo_ do diretório é sincronizado, não o próprio diretório. Quando `source:path` é um diretório, o conteúdo de `source:path` é copiado, não o nome de diretório e o conteúdo. Para obter mais informações, consulte a explicação ampliada no comando `copy`.

Se `dest:path` não existe, ele é criado e o conteúdo de `source:path` vai para lá.

```sh
rclone sync source:path dest:path [flags]
```

### Usando `rclone` de múltiplos locais ao mesmo tempo
{: #rclone-sync-multiple}

É possível usar `rclone` de múltiplos locais ao mesmo tempo se você escolhe um subdiretório diferente para a saída:

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

Se você executar `sync` para o mesmo diretório, será necessário usar `rclone copy`, caso contrário, os dois processos poderão excluir os outros arquivos um do outro:

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

Ao usar `sync`, `copy` ou `move`, quaisquer arquivos que tenham sido sobrescritos ou excluídos serão movidos em sua hierarquia original para esse diretório.

Se `--suffix` for configurado, os arquivos movidos terão o sufixo incluído neles. Se houver um arquivo com o mesmo caminho (depois que o sufixo tiver sido incluído) no diretório, ele será sobrescrito.

O remoto em uso deve suportar o movimento ou cópia do lado do servidor e deve-se usar o mesmo remoto que o destino da sincronização. O diretório de backup não deve sobrepor o diretório de destino.

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

Executará `sync` de `/path/to/local` para `remote:current`, mas, para quaisquer arquivos que tenham sido atualizados ou excluídos, eles serão armazenados em `remote:old`.

Se estiver executando `rclone` por meio de um script, você talvez deseje usar a data de hoje como o nome de diretório passado para `--backup-dir` para armazenar os arquivos antigos ou talvez deseje passar `--suffix` com a data de hoje.

## Sincronização diária de `rclone`
{: #rclone-sync-daily}

Planejar um backup é importante para automatizar backups. O modo como isso é feito depende de sua plataforma. O Windows pode usar o Planejador de Tarefas enquanto o MacOS e o Linux podem usar crontabs.

### Sincronizando um diretório
{: #rclone-sync-directory}

O `Rclone` sincroniza um diretório local com o contêiner remoto, armazenando todos os arquivos no diretório local no contêiner. O `Rclone` usa a sintaxe, `rclone sync source destination`, em que `source` é a pasta local e `destination` é o contêiner dentro de seu IBM COS.

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

Você pode já ter um destino que está criado, mas, se não tiver, será possível criar um novo depósito usando as etapas acima.

### Planejando uma tarefa
{: #rclone-sync-schedule}

Antes de planejar uma tarefa, certifique-se de que você tenha feito seu upload inicial e que ele tenha sido concluído.

#### Windows
{: #rclone-sync-windows}

1. Crie um arquivo de texto que é chamado `backup.bat` em algum lugar em seu computador e cole no comando que você usou na seção sobre [sincronizando um diretório](#rclone-sync-directory). Especifique o caminho completo para o rclone.exe e não se esqueça de salvar o arquivo.

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. Use `schtasks` para planejar uma tarefa. Esse utilitário toma vários parâmetros.
	* /RU - o usuário para executar a tarefa. Isso será necessário se o usuário que você deseja usar estiver com logout efetuado.
	* /RP - a senha para o usuário.
	* /SC - configure como DAILY
	* /TN – o nome da tarefa. Chame-o de backup
	* /TR - o caminho para o arquivo backup.bat que você acabou de criar.
	* /ST – o horário para iniciar a tarefa. Isso está no formato de horário de 24 horas. 01h05min00s é 1:05 AM. 13h05min00s seria 1:05 PM.

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac e Linux
{: #rclone-sync-nix}

1. Crie um arquivo de texto chamado `backup.sh` em algum lugar em seu computador e cole o comando que você usou na seção [sincronizando um Diretório](#rclone-sync-directory). Parece algo como a seguir. Especifique o caminho completo para o executável de rclone e não se esqueça de salvar o arquivo.

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. Torne o script executável com `chmod`.

```sh
chmod +x backup.sh
```

3. Edite crontabs.

```sh
sudo crontab -e
```

4. Inclua uma entrada na parte inferior do arquivo crontabs. Os crontabs são simples: os primeiros cinco campos representam, em ordem, minutos, horas, dias, meses e dias úteis. O uso de * denota tudo. Para fazer com que o `backup.sh` seja executado Diariamente à 1h05, use algo semelhante a este:

```sh
5 1 * * * /full/path/to/backup.sh
```

5. Salve os crontabs e você estará pronto para começar.
