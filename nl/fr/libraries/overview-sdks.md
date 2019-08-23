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

# A propos des SDK IBM COS
{: #sdk-about}

IBM COS fournit des SDK pour Java, Python, NodeJS et Go. Ces SDK sont basés sur les SDK d'API AWS S3 officiels, mais ont été modifiés pour utiliser des fonctions IBM Cloud, notamment IAM, Key Protect, Immutable Object Storage, etc. 

| Fonction                    | Java                                              | Python                                            | NodeJS                                            | GO                                                | Interface CLI                                           |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
|Prise en charge de clé d'API IAM| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|
|Envois par téléchargement en plusieurs parties gérés| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|
|Réceptions par téléchargement en plusieurs parties gérées| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|Création d'une liste de compartiments étendue|                                                   |                                                   |                                                   |                                                   |                                                   |
|Création d'une liste d'objets version 2| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|                                                   |                                                   |
| Key Protect                 | ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|
| SSE-C                       | ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|Règles d'archivage| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|Règles de conservation| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|                                                   |                                                   |
|Transfert haut débit Aspera| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)| ![Icône de marque de contrôle](../../icons/checkmark-icon.svg)|                                                   |                                                   |                                                   |

## Prise en charge de clé d'API IAM
{: #sdk-about-iam}
Permet de créer des clients avec une clé d'API au lieu d'une paire de clés d'accès/secrète. La gestion des jetons est gérée automatiquement et ceux-ci sont automatiquement régénérés lors des opérations de longue durée. 
## Envois par téléchargement en plusieurs parties gérés
A l'aide d'une classe `TransferManager`, le SDK peut gérer toute la logique nécessaire à l'envoi par téléchargement d'objets en plusieurs parties. 
## Réceptions par téléchargement en plusieurs parties gérées
A l'aide d'une classe `TransferManager`, le SDK peut gérer toute la logique nécessaire à la réception par téléchargement d'objets en plusieurs parties. 
## Création d'une liste de compartiments étendue
Il s'agit d'une extension de l'API S3 qui renvoie une liste de compartiments avec des codes de mise à disposition (combinaison de l'emplacement et de la classe de stockage du compartiment, renvoyée sous la forme `LocationConstraint`) pour les compartiments lors de la création de la liste.  Cette fonction est utile pour trouver un compartiment, car les compartiments d'une instance de service sont tous répertoriés quel que soit le noeud final utilisé. 
## Création d'une liste d'objets version 2
Cette fonction permet de définir une portée plus puissante pour la création de liste d'objets. 
## Key Protect
Key Protect est un service IBM Cloud qui gère les clés de chiffrement. Cette fonction est utilisée comme paramètre facultatif lors de la création d'un compartiment. 
## SSE-C                      
## Règles d'archivage              
## Règles de conservation         
## Transfert haut débit Aspera 
