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

# A propos du stockage d'objets
{: #about-cos}

Le stockage d'objets est un concept de technologie de stockage moderne et une progression logique du stockage de fichiers et par blocs. Ce concept existe depuis la fin des années 1990, mais son acceptation par le marché et son succès ont été grandissants au cours des 10 dernières années. 

Le stockage d'objets a été inventé pour résoudre un certain nombre de problèmes :

*  La gestion de données à très grande échelle à l'aide de systèmes de fichiers et à blocs classiques était difficile car ces technologies entraînaient la formation d'îlots de données en raison des limites imposées à divers niveaux de la pile de logiciels et de matériel de gestion des données. 

*  La gestion d'espace de nom à grande échelle impliquait de gérer des bibliothèques complexes et volumineuses, requises pour accéder aux données. Les limites imposées aux structures imbriquées sur les grappes de stockage de fichiers et par blocs classiques ont également contribué à la formation d'îlots de données. 

*  Fournir la sécurité d'accès exigeait une combinaison de technologies, de schémas de sécurité complexes, et une implication humaine importante pour la gestion de ces domaines. 

Le stockage d'objets, également appelé stockage basé sur les objets (OBS), utilise une autre approche pour le stockage et le référencement des données. Les concepts de stockage d'objets incluent trois constructions : 

*  Données : données d'utilisateur et d'application qui nécessitent un stockage persistant. Il peut s'agir de texte, de format binaire, d'objet multimédia ou de tout autre contenu généré par la machine ou des humains. 

*  Métadonnées : il s'agit des données relatives aux données. Elles incluent certains attributs prédéfinis, tels que le temps d'envoi par téléchargement et la taille. Le stockage d'objets permet aux utilisateurs d'inclure des métadonnées personnalisées contenant des informations dans des paires clé-valeur. Ces informations contiennent généralement des informations pertinentes pour l'utilisateur ou l'application qui stocke les données et elles peuvent être modifiées à tout moment. La spécificité de la gestion des métadonnées dans les systèmes de stockage d'objets réside dans le fait que les métadonnées sont stockées avec l'objet. 

*  Clé : un identificateur de ressource unique est affecté à chaque objet d'un système OBS. Cette clé permet au système OBS de différencier les objets les uns des autres et elle est utilisée pour trouver les données sans avoir besoin de connaître l'unité physique, le tableau ou le site où résident les données. 

Cette approche permet au stockage d'objets de stocker des données dans une hiérarchie simple et plate, ce qui réduit le besoin en référentiels de métadonnées volumineux qui entravent les performances. 

L'accès aux données est réalisé à l'aide d'une interface REST sur le protocole HTTP, qui permet un accès n'importe où et n'importe quand, simplement en référençant la clé d'objet. 
