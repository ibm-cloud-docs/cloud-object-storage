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

# Utilisation de `rclone`
{: #rclone}

## Installation de `rclone`
{: #rclone-install}

L'outil `rclone` est utile pour assurer la synchronisation des répertoires et pour effectuer la migration des données entre les plateformes de stockage. Il s'agit d'un programme Go livré sous la forme d'un fichier binaire unique.

### Installation de Quickstart
{: #rclone-quick}

*  [Téléchargez](https://rclone.org/downloads/) le fichier binaire approprié.  
*  Extrayez le fichier binaire `rclone` ou `rclone.exe` de l'archive. 
*  Exécutez `rclone config` pour effectuer la configuration. 

### Installation à l'aide d'un script
{: #rclone-script}

Installez `rclone` sur des systèmes Linux/macOS/BSD :

```
curl https://rclone.org/install.sh | sudo bash
```

Des versions bêta sont également disponibles :

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

Le script d'installation commence par vérifier la version de `rclone` qui est installée et ignore la réception par téléchargement si la version en cours est déjà à jour.{:note}

### Installation sous Linux à partir d'un fichier binaire précompilé
{: #rclone-linux-binary}

Commencez par extraire et décompresser le fichier binaire :

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

Copiez ensuite le fichier binaire vers un emplacement pertinent :

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

Installez la documentation :

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

Exécutez `rclone config` pour effectuer la configuration : 

```
rclone config
```

### Installation sous macOS à partir d'un fichier binaire précompilé
{: #rclone-osx-binary}

Commencez par recevoir par téléchargement le package `rclone` :

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

Ensuite, extrayez le fichier reçu par téléchargement et accédez (à l'aide de la commande `cd`) au dossier dans lequel vous avez extrait le fichier : 

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

Déplacez `rclone` dans votre `$PATH` et entrez votre mot de passe lorsque vous y êtes invité :

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

La commande `mkdir` peut être exécutée sans danger, même si le répertoire existe.
{:tip}

Retirez les fichiers restants. 

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

Exécutez `rclone config` pour effectuer la configuration : 

```
rclone config
```

## Configuration de l'accès à IBM COS
{: #rclone-config}

1. Exécutez `rclone config` et sélectionnez `n` pour un nouveau conteneur d'objets distant. 

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. Entrez le nom de la configuration :
```
	name> <YOUR NAME>
```

3. Sélectionnez le stockage “s3”. 

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

  4. Sélectionnez IBM COS comme fournisseur de stockage S3. 

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

  1. Saisissez **False** pour entrer vos données d'identification. 

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

  6. Entrez la clé d'accès et le secret.

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. Spécifiez le noeud final pour IBM COS. Pour Public IBM COS, choisissez l'une des options fournies. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

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

  8. Spécifiez une contrainte d'emplacement IBM COS. La contrainte d'emplacement doit correspondre au noeud final. Pour plus d'informations sur les noeuds finaux, voir [Noeuds finaux et emplacements de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

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

  9. Spécifiez une liste de contrôle d'accès. Seules les valeurs `public-read` et `private` sont prises en charge.  

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. Passez en revue la configuration affichée et acceptez de sauvegarder le conteneur d'objets distant, puis quittez le programme. Le fichier de configuration doit se présenter comme suit : 

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

## Référence de commande
{: #rclone-reference}

### Création d'un compartiment
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### Création de la liste des compartiments disponibles
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### Création de la liste des éléments de contenu d'un compartiment
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### Copie d'un fichier d'un système local vers un conteneur d'objets distant
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### Copie d'un fichier d'un conteneur d'objets distant vers un conteneur d'objets local
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### Suppression d'un fichier sur un conteneur d'objets distant
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### Commandes de création de liste
{: #rclone-reference-listing}

Il existe plusieurs commandes de création de liste connexes :
* `ls`, permet de répertorier la taille et le chemin uniquement pour des objets. 
* `lsl`, permet de répertorier l'heure, la taille et le chemin d'une modification uniquement pour des objets. 
* `lsd`, permet de répertorier des répertoires uniquement. 
* `lsf`, permet de répertorier des objets et des répertoires dans un format facile à analyser. 
* `lsjson`, permet de répertorier des objets et des répertoires au format JSON. 

## `rclone sync`
{: #rclone-sync}

L'opération `sync` permet d'unifier la source et la destination et de modifier uniquement la destination. La synchronisation ne transfère pas les fichiers inchangés, testés par taille et heure de modification ou MD5SUM. La destination est mise à jour pour correspondre à la source, y compris en supprimant des fichiers si nécessaire.

Dans la mesure où cela peut entraîner une perte de données, effectuez d'abord des tests avec l'option `--dry-run` pour voir exactement ce qui doit être copié et supprimé.
{:important}

Notez que les fichiers de la destination ne seront pas supprimés si des erreurs se produisent à un moment quelconque.

Le _contenu_ du répertoire est synchronisé, et non pas le répertoire proprement dit. Lorsque `source:path` est un répertoire, c'est le contenu de `source:path` qui est copié et non le nom et le contenu du répertoire. Pour plus d'informations, voir les explications détaillées dans la rubrique sur la commande `copy`. 

Si l'objet `dest:path` n'existe pas, il est créé et le contenu `source:path` y est ajouté. 

```sh
rclone sync source:path dest:path [flags]
```

### Utilisation de `rclone` à partir de plusieurs endroits en même temps
{: #rclone-sync-multiple}

Vous pouvez utiliser `rclone` à partir de plusieurs emplacements en même temps si vous choisissez un autre sous-répertoire pour la sortie :

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

Si vous exécutez `sync` dans le même répertoire, vous devez utiliser `rclone copy`, sinon, les deux processus risquent de supprimer leurs fichiers mutuels :

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

Lorsque vous utilisez `sync`, `copy` ou `move`, les fichiers qui auraient été écrasés ou supprimés sont déplacés dans leur hiérarchie d'origine dans ce répertoire. 

Si `--suffix` est défini, le suffixe est ajouté aux fichiers déplacés. S'il existe un fichier avec le même chemin (après que le suffixe a été ajouté) dans le répertoire, il est écrasé. 

Le conteneur d'objets distant en cours d'utilisation doit prendre en charge le déplacement ou la copie côté serveur et vous devez utiliser le même conteneur d'objets distant que la destination de l'opération de synchronisation. Le répertoire de sauvegarde ne doit pas chevaucher le répertoire de destination. 

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

Effectuera une opération `sync` entre `/path/to/local` et `remote:current`, mais les fichiers qui auraient été mis à jour ou supprimés seront stockés dans `remote:old`.

Si vous exécutez `rclone` à partir d'un script, vous souhaiterez peut-être utiliser la date du jour comme nom de répertoire pour `--backup-dir` afin de stocker les anciens fichiers, ou vous souhaiterez peut-être transmettre le paramètre `--suffix` avec la date du jour. 

## Synchronisation quotidienne de `rclone`
{: #rclone-sync-daily}

La planification d'une sauvegarde est essentielle pour l'automatisation des sauvegardes. La façon dont vous effectuez cette opération dépend de votre plateforme. Windows peut utiliser Task Scheduler tandis que MacOS et Linux peuvent utiliser un fichier crontabs. 

### Synchronisation d'un répertoire
{: #rclone-sync-directory}

L'outil `Rclone` permet de synchroniser un répertoire local avec le conteneur d'objets distant en stockant tous les fichiers dans le répertoire local du conteneur. L'outil `Rclone` utilise la syntaxe `rclone sync source destination`, où `source` est le dossier local et `destination` est le conteneur dans votre IBM COS.

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

Vous avez peut-être déjà créé une destination, mais si tel n'est pas le cas, vous pouvez créer un nouveau compartiment en vous servant des étapes décrites ci-dessus.

### Planification d'un travail
{: #rclone-sync-schedule}

Avant de planifier un travail, vérifiez que vous avez effectué et achevé votre envoi par téléchargement initial. 

#### Windows
{: #rclone-sync-windows}

1. Créez un fichier texte nommé `backup.bat` quelque part sur votre ordinateur et collez-le dans la commande que vous avez utilisée dans la section relative à la [synchronisation d'un répertoire](#rclone-sync-directory).  Indiquez le chemin complet au fichier rclone.exe et n'oubliez pas de sauvegarder le fichier.

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. Utilisez `schtasks` pour planifier un travail. Cet utilitaire prend un certain nombre de paramètres. 
	* /RU – Utilisateur sous lequel le travail doit être exécuté. Ce paramètre est nécessaire si l'utilisateur que vous souhaitez utiliser est déconnecté. 
	* /RP – Mot de passe de l'utilisateur. 
	* /SC – La valeur DAILY est affectée à ce paramètre. 
	* /TN – Nom du travail. Nommez-le Backup. 
	* /TR – Chemin d'accès au fichier backup.bat que vous venez de créer. 
	* /ST – Heure de démarrage de la tâche. Il s'agit d'un format 24 heures. 01:05:00 correspond à 1h05. 13:05:00 correspond à 13h05. 

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac et Linux
{: #rclone-sync-nix}

1. Créez un fichier texte nommé `backup.sh` quelque part sur votre ordinateur et collez-le dans la commande que vous avez utilisée dans la section relative à la [synchronisation d'un répertoire](#rclone-sync-directory).  Le fichier doit se présenter comme illustré ci-après. Indiquez le chemin complet au fichier exécutable rclone et n'oubliez pas de sauvegarder le fichier.

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. Rendez le script exécutable à l'aide de `chmod`.

```sh
chmod +x backup.sh
```

3. Editez le fichier crontabs.

```sh
sudo crontab -e
```

4. Ajoutez une entrée au bas du fichier crontabs. Un fichier crontabs est simple, les cinq premières zones représentent, dans l'ordre, les minutes, les heures, les jours, les mois et les jours de la semaine. Utilisez * pour tout indiquer. Pour que le fichier `backup.sh` s'exécute tous les jours à 1h05, exécutez une commande qui ressemble à ceci :

```sh
5 1 * * * /full/path/to/backup.sh
```

5. Sauvegardez le fichier crontabs. Vous pouvez commencer !
