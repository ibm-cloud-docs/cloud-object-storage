---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: faq, questions

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

# Foire aux questions
{: #faq}

## Questions sur les API
{: #faq-api}

**Les noms de compartiment {{site.data.keyword.cos_full}} sont-ils sensibles à la casse ?**

Les noms de compartiment doivent être adressables par DNS et par conséquent, ils ne sont pas sensibles à la casse. 

**Quel nombre maximal de caractères un nom d'objet peut-il contenir ?**

1024

**Comment puis-je connaître la taille totale de mon compartiment à l'aide de l'API ?**

Il n'est pas possible d'extraire la taille d'un compartiment avec une seule demande. Vous devez répertorier le contenu d'un compartiment et additionner la taille de chaque objet.

**Puis-je faire migrer des données depuis AWS S3 vers {{site.data.keyword.cos_full_notm}} ?**

Oui, vous pouvez utiliser vos outils existants pour lire et écrire des données dans {{site.data.keyword.cos_full_notm}}. Vous devez configurer les données d'identification HMAC pour permettre à vos outils de s'authentifier. Les outils compatibles S3 ne sont pas tous pris en charge actuellement. Pour plus d'informations, voir [Utilisation des données d'identification HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac).


## Questions sur les offres
{: #faq-offering}

**Existe-t-il une limite de 100 compartiments par compte ? Que se passe-il si nous avons besoin de plus de compartiments ?**

Oui, la limite est actuellement fixée à 100 compartiments. Les préfixes sont généralement plus adaptés pour regrouper des objets dans un compartiment, sauf si les données doivent figurer dans une autre région ou classe de stockage. Par exemple, pour regrouper les dossiers de patients, vous utilisiez un préfixe par patient. Si cette solution ne peut pas être mise en oeuvre, contactez le support client.

**Si je veux stocker mes données à l'aide de la classe de stockage Coffre ou Coffre froid d'{{site.data.keyword.cos_full_notm}}, suis-je obligé de créer un autre compte ?**

Non, les classes de stockage, ainsi que les régions, sont définies au niveau du compartiment. Créez simplement un nouveau compartiment qui est défini pour la classe de stockage souhaitée.

**Lorsque je crée un compartiment à l'aide de l'API, comment puis-je définir la classe de stockage ?**

La classe de stockage (par exemple `us-flex`) est affectée à la variable de configuration `LocationConstraint` pour ce compartiment. Cela est dû à une différence clé entre la façon dont AWS S3 et {{site.data.keyword.cos_full_notm}} gèrent les classes de stockage. {{site.data.keyword.cos_short}} définit les classes de stockage au niveau du compartiment, tandis que AWS S3 affecte une classe de stockage à un objet individuel. Une liste de codes de mise à disposition valides pour `LocationConstraint` peut être référencée dans le [guide sur les classes de stockage](/docs/services/cloud-object-storage?topic=cloud-object-storage-classes).

**La classe de stockage d'un compartiment peut-elle être modifiée ?  Par exemple, si nous avons des données de production stockées à l'aide de la classe de stockage Standard, pouvons-nous facilement passer à la classe de stockage 'Coffre' à des fins de facturation si nous ne les utilisons pas souvent ?**

Aujourd'hui, la modification d'une classe de stockage nécessite de déplacer ou copier manuellement les données d'un compartiment vers un autre compartiment avec la classe de stockage souhaitée.


## Questions sur les performances
{: #faq-performance}

**La cohérence des données dans {{site.data.keyword.cos_short}} a-t-elle un impact sur les performances ?**

La cohérence avec n'importe quel système distribué entraîne des frais, mais le système de stockage dispersé d'{{site.data.keyword.cos_full_notm}} est beaucoup plus efficace et le temps système est inférieur à celui des systèmes fonctionnant avec plusieurs copies synchrones.

**Les performances sont-elles impactées si mon application doit manipuler des objets volumineux ?**

Pour optimiser les performances, il est possible d'envoyer et de recevoir par téléchargement les objets en plusieurs parties, en parallèle. 


## Questions sur le chiffrement
{: #faq-encryption}

**{{site.data.keyword.cos_short}} fournit-il le chiffrement des données au repos et dynamiques ?**

Oui. Les données au repos sont chiffrées coté fournisseur à l'aide d'un chiffrement AES (Advanced Encryption Standard) automatique de 256 bits et d'un hachage SHA-256 (Secure Hash Algorithm). Les données dynamiques sont sécurisées à l'aide d'un chiffrement de qualité opérateur intégré TLS/SSL (Transport Layer Security/Secure Sockets Layer) ou SNMPv3 avec AES.


**Quel est le temps système de chiffrement typique si un client souhaite chiffrer ses données ?**

Le chiffrement côté serveur est toujours en fonction pour les données client. Par rapport au hachage requis dans l'authentification S3 et au codage de l'effacement, le chiffrement ne représente pas une partie importante du coût de traitement de COS.

**{{site.data.keyword.cos_short}} chiffre-t-il toutes les données ?**

Oui, {{site.data.keyword.cos_short}} chiffre toutes les données. 

**Existe-t-il une conformité FIPS 140-2 pour {{site.data.keyword.cos_short}} par rapport aux algorithmes de chiffrement ?**

Oui, l'offre IBM COS Federal est approuvée pour les contrôles FedRAMP Moderate Security qui requièrent une configuration FIPS validée. L'offre IBM COS Federal est certifiée pour le niveau 1 de FIPS 140-2. Pour plus d'informations sur l'offre COS Federal, [contactez-nous](https://www.ibm.com/cloud/government) via notre site Federal. 

**Le chiffrement de clé client sera-t-il pris en charge ?**

Oui, le chiffrement de clé client est pris en charge à l'aide de SSE-C ou de Key Protect.

## Questions d'ordre général
{: #faq-general}

**Combien d'objets peuvent tenir dans un compartiment ?**

Il n'y a aucune limite au nombre d'objets pouvant tenir dans un compartiment. 

**Puis-je imbriquer des compartiments les uns dans les autres ?**

Non, les compartiments ne peuvent pas être imbriqués. Si un niveau d'organisation supérieur est requis dans un compartiment, l'utilisation des préfixes est prise en charge : `{endpoint}/{bucket-name}/{object-prefix}/{object-name}`. Notez que la clé de l'objet demeure la combinaison suivante : `{object-prefix}/{object-name}`.

**Quelle est la différence entre les demandes 'Classe A' et 'Classe B' ?**

Les demandes 'Classe A' sont des opérations qui sont liées à une modification ou à une création de liste. Cela inclut la création de compartiments, l'envoi par téléchargement ou la copie d'objets, la création ou la modification de configurations, la création de liste de compartiments et la création de liste d'éléments de contenu de compartiments. Les demandes 'Classe B' sont des opérations qui sont liées à l'extraction d'objets ou des métadonnées/configurations qui leur sont associées sur le système. La suppression de compartiments ou d'objets sur le système n'entraîne pas de frais.

**Comment structurer vos données à l'aide d'Object Storage de manière à faciliter leur recherche ? Sans structure de répertoire, il semble difficile de visualiser des milliers de fichiers d'un même niveau.**

Vous pouvez utiliser les métadonnées associées à chaque objet pour trouver les objets que vous recherchez. Le principal avantage d'Object Storage concerne les métadonnées associées à chaque objet. Chaque objet peut avoir jusqu'à 4 Mo de métadonnées dans {{site.data.keyword.cos_short}}. Lorsqu'elles sont déchargées sur une base de données, les métadonnées offrent d'excellentes capacités de recherche. Un grand nombre de paires (clé, valeur) peut être stocké dans 4 Mo. Vous pouvez également utiliser des préfixes pour faciliter vos recherches. Par exemple, si vous utilisez des compartiments pour séparer chaque donnée client, vous pouvez utiliser des préfixes dans les compartiments de l'organisation. Par exemple : /bucket1/folder/object, où 'folder/' est le préfixe. 

**Pouvez-vous confirmer qu'{{site.data.keyword.cos_short}} offre une cohérence immédiate par opposition à une cohérence éventuelle ? **

{{site.data.keyword.cos_short}} offre une cohérence immédiate pour les données et une cohérence éventuelle pour la comptabilité de l'utilisation. 


**{{site.data.keyword.cos_short}} peut-il partitionner automatiquement les données à l'instar de HDFS afin que je puisse lire les partitions en parallèle, par exemple, avec Spark ?**

{{site.data.keyword.cos_short}} prend en charge une opération GET par segments sur l'objet, par conséquent, une application peut effectuer une opération de type de lecture segmentée distribuée. La segmentation des données serait effectuée sur l'application à gérer. 
