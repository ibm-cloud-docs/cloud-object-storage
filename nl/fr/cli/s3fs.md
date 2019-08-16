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

# Montage d'un compartiment à l'aide de `s3fs`
{: #s3fs}

Les applications qui s'attendent à lire et à écrire des données dans un système de fichiers de type NFS peuvent utiliser `s3fs`, qui peut monter un compartiment en tant que répertoire tout en conservant le format d'objet natif pour les fichiers. Cela vous permet d'interagir avec votre stockage en cloud à l'aide de commandes shell familières, comme `ls` pour créer des listes ou `cp` pour copier des fichiers, ainsi que de fournir des accès aux applications existantes qui dépendent de la lecture et de l'écriture à partir de fichiers locaux. Pour une présentation plus détaillée, [consultez le fichier README officiel du projet](https://github.com/s3fs-fuse/s3fs-fuse).

## Prérequis
{: #s3fs-prereqs}

* Un compte IBM Cloud et une instance d'{{site.data.keyword.cos_full}}
* Un environnement Linux ou OSX
* Des données d'identification (une [clé d'API IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) ou des [données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac))

## Installation
{: #s3fs-install}

Sous OSX, utilisez [Homebrew](https://brew.sh/) :

```sh
brew cask install osxfuse
brew install s3fs
```
{:codeblock}

Sous Debian ou Ubuntu : 

```sh
sudo apt-get install automake autotools-dev fuse g++ git libcurl4-openssl-dev libfuse-dev libssl-dev libxml2-dev make pkg-config
```
{:codeblock}

La documentation `s3fs` officiel suggère d'utiliser `libcurl4-gnutls-dev` au lieu de `libcurl4-openssl-dev`. Les deux fonctionnent, mais la version OpenSSL est susceptible de fournir de meilleures performances.
{:tip}

Vous pouvez également générer `s3fs` à partir d'une source. Commencez par cloner le référentiel Github :

```sh
git clone https://github.com/s3fs-fuse/s3fs-fuse.git 
```
{:codeblock}

Générez ensuite `s3fs` :

```sh
cd s3fs-fuse
./autogen.sh
./configure
make

```
{:codeblock}

Puis, installez le fichier binaire :

```sh
sudo make install
```
{:codeblock}

## Configuration
{: #s3fs-config}

Stockez vos données d'identification dans un fichier contenant `<access_key>:<secret_key>` ou `:<api_key>`. Ce fichier doit avoir un accès limité, par conséquent, exécutez ce qui suit : 

```sh
chmod 0600 <credentials_file> 
```
{:codeblock}

A présent, montez un compartiment à l'aide de la commande suivante :

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file>
```
{:codeblock}

Si le fichier de données d'identification ne comporte qu'une clé d'API (pas de données d'identification HMAC), vous devez également ajouter l'indicateur `ibm_iam_auth` :

```sh
s3fs <bucket> <mountpoint> -o url=http{s}://<endpoint> –o passwd_file=<credentials_file> -o ibm_iam_auth
```
{:codeblock}

L'élément `<bucket>` est un compartiment existant et l'élément `<mountpoint>` est le répertoire local dans lequel vous souhaitez monter le compartiment. L'élément `<endpoint>` doit correspondre à l'[emplacement du compartiment](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints). L'élément `credentials_file` est le fichier créé avec la clé d'API ou les données d'identification HMAC. 

A présent, la commande `ls <mountpoint>` va créer la liste des objets contenus dans ce compartiment comme s'il s'agissait de fichiers locaux (ou dans le cas de préfixes d'objet, comme s'il s'agissait de répertoires imbriqués). 

## Optimisation des performances
{: #s3fs-performance}

Les performances n'égaleront jamais celles d'un véritable système de fichiers local, mais il est possible d'utiliser des options plus avancées pour augmenter la capacité de traitement.  

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

1. L'option `cipher_suites=AESGCM` n'est pertinente que lorsqu'un noeud final HTTPS est utilisé. Par défaut, les connexions sécurisées à IBM COS utilisent la suite de chiffrement `AES256-SHA`. L'utilisation d'une suite `AESGCM` à la place réduit considérablement la surcharge de l'unité centrale sur votre machine client, provoquée par les fonctions de chiffrement TLS, tout en offrant le même niveau de sécurité cryptographique.
2. L'option `kernel_cache` active le cache de la mémoire tampon du noyau sur votre point de montage `s3fs`. Cela signifie que les objets ne seront lus qu'une seule fois par `s3fs`, car la lecture répétitive du même fichier peut être effectuée à partir du cache de la mémoire tampon du noyau. Le cache de la mémoire tampon du noyau n'utilisera que de la mémoire disponible qui n'est pas utilisée par d'autres processus. Cette option n'est pas recommandée si vous vous attendez à ce que les objets de compartiment soient écrasés à partir d'un autre processus ou d'une autre machine pendant le montage du compartiment, et que votre cas d'utilisation requiert un accès direct au contenu le plus à jour. 
3. L'option `max_background=1000` améliore les performances des opérations de lecture de fichiers simultanées par `s3fs`. Par défaut, FUSE prend en charge les demandes de lecture pouvant atteindre 128 ko. Dans le cas d'une demande de lecture de plus de 128 ko, le noyau fractionne la demande volumineuse en plusieurs sous-demandes plus petites et laisse à s3fs le soin de les traiter de manière asynchrone. L'option `max_background` définit le nombre maximal global de demandes asynchrones simultanées de ce type. Par défaut, cette option a pour valeur 12, mais si une valeur élevée arbitraire (1000) lui est affectée, les demandes de lecture ne peuvent pas être bloquées, même en cas de lecture simultanée d'un nombre important de fichiers. 
4. L'option `max_stat_cache_size=100000` réduit le nombre de demandes HTTP `HEAD` redondantes envoyées par `s3fs` ainsi que le temps nécessaire pour répertorier un répertoire ou extraire des attributs de fichier. Typiquement, l'utilisation d'un système de fichiers génère des accès fréquents aux métadonnées d'un fichier via un appel `stat()` qui effectue un mappage à `HEAD` sur le système de stockage d'objets. Par défaut, `s3fs` met en cache les attributs (métadonnées) de 1 000 objets au maximum. Chaque entrée mise en cache prend jusqu'à 0,5 ko de mémoire. Idéalement, vous souhaitez que le cache puisse contenir les métadonnées de tous les objets de votre compartiment. Toutefois, vous souhaiterez peut-être prendre en compte les implications de cette mise en cache sur l'utilisation de la mémoire. La valeur `100000` ne prendra pas plus que 0,5 Ko * 100000 = 50 Mo.
5. L'option `multipart_size=52` permet de définir la taille maximale des demandes et des réponses envoyées et reçues au/depuis le serveur COS, en Mo. Par défaut, `s3fs` affecte la valeur 10 Mo à cette option. Le fait d'augmenter cette valeur augmente également la capacité de traitement (Mo/s) par connexion HTTP. D'autre part, le temps d'attente pour le premier octet servi à partir du fichier augmentera en conséquence. Par conséquent, si votre cas d'utilisation lit uniquement une petite quantité de données de chaque fichier, vous ne souhaiterez probablement pas augmenter cette valeur. En outre, pour les objets volumineux (par exemple, de plus de 50 Mo), la capacité de traitement augmente si cette valeur est suffisamment basse pour permettre l'extraction simultanée du fichier à l'aide de plusieurs demandes. Je trouve que la valeur optimale pour cette option est d'environ 50 Mo. Les meilleures pratiques de COS suggèrent d'utiliser des demandes qui sont des multiples de 4 Mo, par conséquent, il est recommandé d'affecter la valeur 52 (Mo) à cette option. 
6. L'option `parallel_count=30` définit le nombre maximal de demandes envoyées simultanément à COS pour chaque opération de lecture/écriture de fichier. Par défaut, cette option a pour valeur 5. Pour les objets très volumineux, vous pouvez obtenir une plus grande capacité de traitement en augmentant cette valeur. Comme pour l'option précédente, indiquez une valeur basse si vous lisez uniquement une petite quantité de données de chaque fichier. 
7. Avec l'option `multireq_max=30`, lorsqu'une liste de contenu est créée pour un répertoire, une demande de métadonnées d'objet (`HEAD`) est envoyée pour chaque objet de la liste (sauf si les métadonnées sont trouvées dans le cache). Cette option limite le nombre de demandes simultanées de ce type envoyées à COS pour une seule opération de création de liste de contenu de répertoire. Par défaut, cette option a pour valeur 20. Notez que cette valeur doit être supérieure ou égale à celle de l'option `parallel_count` ci-dessus. 
8. L'option `dbglevel=warn` affecte la valeur `warn` au niveau de débogage au lieu de la valeur par défaut (`crit`) pour journaliser les messages dans /var/log/syslog.

## Limitations
{: #s3fs-limitations}

Il convient de rappeler que s3fs peut ne pas convenir à toutes les applications, car les services d'Object Storage ont un temps d'attente élevé pour le premier octet et n'ont pas d'accès en écriture aléatoire. Les charges de travail qui ne lisent que des fichiers volumineux, comme les charges de travail d'apprentissage en profondeur, peuvent atteindre une bonne capacité de traitement avec `s3fs`. 
