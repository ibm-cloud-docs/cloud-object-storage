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

# Utilisation de l'option Transfert haut débit Aspera
{: #aspera}

L'option Transfert haut débit Aspera surmonte les limitations des transferts FTP et HTTP traditionnels et améliore les performances de transfert de données dans la plupart des conditions, en particulier sur les réseaux avec des temps d'attente élevés et soumis à des pertes de paquets. Au lieu d'utiliser la méthode `PUT` HTTP standard, l'option Transfert haut débit Aspera télécharge l'objet à l'aide du [protocole FASP](https://asperasoft.com/technology/transport/fasp/). L'utilisation de l'option Transfert haut débit Aspera pour les envois par téléchargement et les réceptions par téléchargement offre les avantages suivants :

- Vitesses de transfert plus rapides
- Transfert d'envois par téléchargement d'objet volumineux de plus de 200 Mo dans la console et de 1 Go à l'aide d'un SDK ou d'une bibliothèque
- Envois par téléchargement de dossiers entiers contenant tout type de données, y compris des fichiers multimédia, des images de disque et d'autres données structurées ou non structurées
- Personnalisation des vitesses de transfert et des préférences par défaut 
- Possibilité de visualiser, de mettre en pause/reprendre ou d'annuler des transferts indépendamment les uns des autres 

L'option Transfert haut débit Aspera est disponible dans la {{site.data.keyword.cloud_notm}} [console](#aspera-console) et peut également être utilisée à l'aide d'un programme via un [SDK](#aspera-sdk).  

L'option Transfert haut débit Aspera est disponible dans certaines régions uniquement. Pour plus d'informations, voir [Services intégrés](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability).
{:tip}

## Utilisation de la console
{: #aspera-console}

Lorsque vous créez un compartiment dans une [région prise en charge](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-service-availability), vous avez la possibilité de sélectionner l'option Transfert haut débit Aspera pour envoyer par téléchargement des fichiers ou des dossiers. Dès lors que vous tentez d'envoyer par téléchargement un objet, vous êtes invité à installer le client Aspera Connect. 

### Installation d'Aspera Connect
{: #aspera-install}

1. Sélectionnez **Installer Aspera Connect**. 
2. Suivez les instructions d'installation appropriées à votre système d'exploitation et de votre navigateur. 
3. Reprenez l'envoi par téléchargement de fichier ou de dossier. 

Le plug-in Aspera Connect peut également être installé directement à partir du [site Web d'Aspera](https://downloads.asperasoft.com/connect2/). Pour obtenir de l'aide lors du traitement des incidents liés au plug-in Aspera Connect, [voir la documentation](https://downloads.asperasoft.com/en/documentation/8). 

Une fois le plug-in installé, vous avez la possibilité de définir le transfert haut débit Aspera comme option par défaut pour tous les envois par téléchargement vers le compartiment cible qui utilisent le même navigateur. Sélectionnez **Mémoriser mes préférences de navigateur**. Des options sont également disponibles sur la page de configuration de compartiment sous **Options de transfert**. Ces options vous permettent de choisir la valeur Standard ou Haut débit comme transport par défaut pour les envois par téléchargement et les réceptions par téléchargement. 

La console IBM Cloud Object Storage basée sur le Web n'est pas couramment employée pour utiliser {{site.data.keyword.cos_short}}. L'option de transfert Standard limite la taille des objets à 200 Mo et le nom de fichier et la clé sont identiques. La prise en charge des objets plus volumineux et des performances améliorées (en fonction des facteurs de réseau) est fournie par l'option Transfert haut débit Aspera. 

### Etat de transfert
{: #aspera-console-transfer-status}

**Actif :** lorsqu'un transfert est initié, l'état de transfert est Actif. Lorsque le transfert est actif, vous pouvez le mettre en pause, le reprendre ou l'annuler.  

**Terminé :** à la fin de votre transfert, des informations sur ce dernier et sur tous les transferts réalisés dans cette session s'affichent sur l'onglet Terminé. Vous pouvez effacer ces informations. Seules les informations relatives aux transferts terminés dans la session en cours sont affichées. 

**Préférences : ** vous pouvez définir la valeur Haut débit par défaut pour les envois par téléchargement et/ou les réceptions par téléchargement. 

Les réceptions par téléchargement à l'aide de l'option Transfert haut débit Aspera entraînent des frais de sortie. Pour plus d'informations, voir la [page de tarification](https://www.ibm.com/cloud/object-storage).
{:tip}

**Préférences avancées :** vous pouvez définir une bande passante pour les envois par téléchargement et les réceptions par téléchargement. 

----

## Utilisation de bibliothèques et de SDK
{: #aspera-sdk}

Le SDK de l'option Transfert haut débit Aspera vous permet de lancer un transfert haut débit dans vos applications personnalisées lorsque vous utilisez Java ou Python. 

### Quand utiliser l'option Transfert haut débit Aspera
{: #aspera-guidance}

Le protocole FASP utilisé par l'option Transfert haut débit Aspera n'est pas adapté à tous les transferts de données vers et depuis COS. Plus précisément, tout transfert faisant appel à l'option Transfert haut débit Aspera devrait :

1. Toujours utiliser des sessions multiples. Au moins deux sessions parallèles utiliseront de manière optimale les capacités de transfert haut débit Aspera. Voir les conseils spécifiques pour [Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera) et [Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera). 
2. L'option Transfert haut débit Aspera est idéale pour les fichiers plus volumineux, et tous les fichiers ou répertoires qui contiennent une quantité totale de données inférieure à 1 Go doivent plutôt transférer l'objet en plusieurs parties à l'aide des classes TransferManager standard. L'option Transfert haut débit Aspera requiert un temps jusqu'au premier octet (TTFB) plus élevé par rapport aux transferts HTTP classiques. L'instanciation de nombreux objets TransferManager Aspera pour gérer les transferts de fichiers individuels plus petits peut entraîner des performances médiocres par rapport aux demandes HTTP de base, par conséquent, il est préférable d'instancier un seul client pour envoyer par téléchargement un répertoire de fichiers plus petits à la place. 
3. L'option Transfert haut débit Aspera a été conçue en partie pour améliorer les performances dans des environnements réseau comportant de grandes quantités de perte de paquets, ce qui rend le protocole performant sur de grandes distances et sur des réseaux publics étendus. L'option Transfert haut débit Aspera ne doit pas être utilisée pour les transferts au sein d'une région ou d'un centre de données. 

Le SDK de l'option Transfert haut débit Aspera est close source, il s'agit donc d'une dépendance facultative pour le SDK COS (qui utilise une licence Apache). {:tip}

#### Conditionnement COS/option Transfert haut débit Aspera
{: #aspera-packaging}

L'image ci-après illustre de façon détaillée l'interaction entre le SDK COS et la bibliothèque de transfert haut débit Aspera pour fournir des fonctionnalités. 

<img alt="SDK COS/option Transfert haut débit Aspera." src="https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/aspera-packaging.png" height="200px" />
{: caption="Figure 1 : SDK COS/fonction Transfert haut débit Aspera. " caption-side="bottom"} 

### Plateformes prises en charge
{: #aspera-sdk-platforms}

| Système d'exploitation | Version   | Architecture | Version Java testée  | Version Python testée  |
|------------------------|-----------|--------------|--------------|----------------|
| Ubuntu                 | 18.04 LTS | 64 bits      | 6 et plus | 2.7, 3.6       |
| Mac OS X               | 10.13     | 64 bits      | 6 et plus | 2.7, 3.6       |
| Microsoft&reg; Windows | 10        | 64 bits      | 6 et plus | 2.7, 3.6       |

Chaque session de transfert haut débit Aspera génère un processus `ascp` individuel qui s'exécute sur la machine client pour effectuer le transfert. Assurez-vous que votre environnement de calcul puisse permettre l'exécution de ce processus. {:tip}

**Limitations supplémentaires**

* Les fichiers binaires 32 bits ne sont pas pris en charge. 
* Le support Windows requiert Windows 10. 
* Le support Linux est limité à Ubuntu (testé sur la version 18.04 LTS). 
* Les clients Aspera Transfer Manager doivent être créés à l'aide de clés d'API IAM et non de données d'identification HMAC. 

### Obtention du SDK à l'aide de Java
{: #aspera-sdk-java} 
{: java}

Le moyen le plus simple d'utiliser le SDK Java pour {{site.data.keyword.cos_full_notm}} et l'option Transfert haut débit Aspera est d'utiliser Maven pour gérer les dépendances. Si vous ne connaissez pas bien Maven, vous pouvez devenir opérationnel en vous reportant au guide intitulé [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html){:new_window}.
{: java}

Maven utilise un fichier nommé `pom.xml` pour spécifier les bibliothèques (et leurs versions) nécessaires pour un projet Java. Voici un exemple de fichier `pom.xml` permettant d'utiliser le SDK Java pour {{site.data.keyword.cos_full_notm}} et l'option Transfert haut débit Aspera.
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

Des exemples illustrant le lancement de transferts haut débit Aspera avec Java sont disponibles dans la section [Utilisation de l'option Transfert haut débit Aspera](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples-aspera).
{: java}

### Obtention du SDK à l'aide de Python
{: #aspera-sdk-python} 
{: python}

Les SDK Python pour {{site.data.keyword.cos_full_notm}} et l'option Transfert haut débit Aspera sont disponibles à partir du référentiel de logiciels Python Package Index (PyPI).
{: python}

```
pip install cos-aspera
```
{: codeblock}
{: python}

Des exemples illustrant le lancement de transferts Aspera avec Python sont disponibles dans la section [Utilisation de l'option Transfert haut débit Aspera](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#python-examples-aspera).
{: python}
