---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: administration, billing, platform

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

# Facturation
{: #billing}

Des informations sur la tarification sont disponibles à l'adresse [{{site.data.keyword.cloud}}](https://www.ibm.com/cloud/object-storage#s3api){:new_window}.

## Factures
{: #billing-invoices}

Pour consulter vos factures de compte, accédez à **Gérer** > **Facturation et utilisation** dans le menu de navigation. 

Chaque compte reçoit une seule facture. Si vous avez besoin de factures différentes pour différents ensembles de conteneurs, vous devez créer plusieurs comptes. 

## Tarification {{site.data.keyword.cos_full_notm}}
{: #billing-pricing}

Les coûts de stockage pour {{site.data.keyword.cos_full}} sont déterminés par le volume total de données stockées, la quantité de bande passante sortante publique utilisée et le nombre total de demandes opérationnelles traitées par le système. 

Les offres d'infrastructure sont connectées à un réseau à trois niveaux, segmentant le trafic public, privé et de gestion. Le transfert de données entre les services d'infrastructure sur le réseau privé est gratuit. Les offres d'infrastructure (telles que les serveurs bare metal, les serveurs virtuels et le stockage en cloud) se connectent aux autres applications et services du catalogue de la plateforme {{site.data.keyword.cloud_notm}} (par exemple, les services Watson et les environnements d'exécution Cloud Foundry) sur le réseau public, par conséquent, le transfert de données entre ces deux types d'offre est comptabilisé et facturé conformément aux tarifs de bande passante de réseau public standard.
{: tip}

## Classes de demande
{: #billing-request-classes}

Les demandes 'Classe A' sont liées à une modification ou à une création de liste. Cette catégorie comprend la création de compartiments, l'envoi par téléchargement ou la copie d'objets, la création ou la modification de configurations, la création de liste de compartiments et la création de liste d'éléments de contenu de compartiments. 

Les demandes 'Classe B' sont liées à l'extraction d'objets ou des métadonnées/configurations qui leur sont associées sur le système. 

La suppression de compartiments ou d'objets sur le système n'entraîne pas de frais.

| Classe | Demandes | Exemples |
|--- |--- |--- |
| Classe A | Demandes PUT, COPY et POST et GET utilisées pour créer des listes de compartiments et d'objets |Création de compartiments, téléchargement ou copie d'objets, création de liste de compartiments, création de liste d'éléments de contenu de compartiments, définition de listes ACL et définition de configurations CORS |
| Classe B | Demandes GET (création de liste exclue), HEAD et OPTIONS| Extraction d'objets et de métadonnées |

## Transferts Aspera
{: #billing-aspera}

L'option [Transfert haut débit Aspera](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-aspera) entraîne des frais de sortie supplémentaires. Pour plus d'informations, voir la [page de tarification](https://www.ibm.com/cloud/object-storage#s3api).


## Classes de stockage
{: #billing-storage-classes}

Les données stockées n'ont pas toutes besoin d'être fréquemment consultées, et parfois certaines données archivées sont rarement, voire jamais consultées. Pour les charges de travail moins actives, vous pouvez créer des compartiments dans une classe de stockage différente. Le stockage d'objets dans ces compartiments entraîne des frais selon une planification différente de celle du stockage standard.

Il existe quatre classes :

*  **Standard** : classe utilisée pour les charges de travail actives, sans frais pour les données extraites (en dehors du coût de la demande opérationnelle proprement dite). 
*  **Coffre** : classe utilisée pour les charges de travail tièdes pour lesquelles les données sont consultées moins d'une fois par mois. Des frais d'extraction supplémentaires ($/Go) s'appliquent à chaque fois que les données sont lues. Le service comprend un seuil minimal pour la taille d'objet et la durée de stockage adaptés à l'usage prévu, le stockage de données moins actives tièdes. 
*  **Coffre froid** : classe utilisée pour les charges de travail froides pour lesquelles les données sont consultées tous les 90 jours tout au plus. Des frais d'extraction supplémentaires supérieurs ($/Go) s'appliquent à chaque fois que les données sont lues. Le service comprend un seuil minimal plus élevé pour la taille d'objet et la durée de stockage adaptés à l'usage prévu, le stockage de données inactives froides. 
*  **Flex** : classe utilisée pour les charges de travail dynamiques pour lesquelles les modèles d'accès sont plus difficiles à prévoir. En fonction de l'utilisation, si les coûts et les frais d'extraction dépassent une valeur plafonnée, les frais d'extraction sont abandonnés et de nouveaux frais de capacité s'appliquent à la place. Si les données ne sont pas fréquemment consultées, ce stockage est plus économique que le stockage standard, et si les modèles d'utilisation d'accès deviennent brusquement plus actifs, ce stockage est plus économique que le stockage Coffre ou Coffre froid. La classe Flex ne nécessite pas de seuil minimal pour la taille d'objet ou la période de stockage. 

Pour plus d'informations sur la tarification, voir le [tableau de tarification à l'adresse ibm.com](https://www.ibm.com/cloud/object-storage#s3api).

Pour plus d'informations sur la création de compartiments avec différentes classes de stockage, voir la rubrique [Référence d'API](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-storage-class).
