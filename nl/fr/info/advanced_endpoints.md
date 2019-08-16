---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: endpoints, legacy, access points, manual failover

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

# Informations supplémentaires sur les noeuds finaux
{: #advanced-endpoints}

La résilience d'un compartiment est définie par le noeud final utilisé pour le créer. La résilience _inter-régionale_ propagera vos données dans plusieurs zones métropolitaines, tandis que la résilience _régionale_ propagera les données dans une seule zone métropolitaine. La résilience de _centre de données unique_ répartit les données sur plusieurs dispositifs au sein d'un même centre de données. Les compartiments régionaux et inter-régionaux peuvent assurer la disponibilité lorsqu'un site est indisponible. 

Les charges de travail de calcul colocalisées avec un noeud final {{site.data.keyword.cos_short}} régional auront des temps d'attente réduits et de meilleures performances. Pour les charges de travail non concentrées dans une zone géographique unique, un noeud final `géographique` inter-régional achemine les connexions vers les centres de données régionaux les plus proches.

Lorsqu'un noeud final inter-régional est utilisé, il est possible de diriger le trafic entrant vers un point d'accès spécifique tout en distribuant des données dans les trois régions. Lorsque des demandes sont envoyées à un point d'accès individuel, aucun basculement automatisé ne s'effectue si cette région devient indisponible. Les applications qui dirigent le trafic vers un point d'accès au lieu du noeud final `géographique` **doivent** implémenter en interne la logique de basculement appropriée pour bénéficier des avantages de disponibilité du stockage inter-régional. {:tip}

Certaines charges de travail peuvent tirer parti de l'utilisation d'un noeud final de centre de données unique. Les données stockées dans un seul site sont toujours distribuées sur de nombreux dispositifs de stockage physique, mais elles sont contenues dans un seul centre de données. Cela peut permettre d'améliorer les performances des ressources de calcul au sein du même site, mais pas d'assurer la disponibilité si le site devient indisponible. Les compartiments de centres de données uniques ne fournissent pas de réplication ou de sauvegarde automatisée si le site est détruit, par conséquent, toutes les applications utilisant un site unique doivent être conçue avec une fonction de reprise après incident. 

Toutes les demandes doivent utiliser SSL lors de l'utilisation d'IAM. Le service rejette toute demande de texte en clair.

Types de noeud final :

Les services {{site.data.keyword.cloud}} sont connectés à un réseau à trois niveaux, segmentant le trafic public, privé et de gestion. 

* Les **noeuds finaux privés** sont disponibles pour les demandes provenant des clusters Kubernetes, des serveurs bare metal, des serveurs virtuels et d'autres services de stockage de cloud. Les noeuds finaux privés offrent de bien meilleures performances et les bandes passantes sortantes ou entrantes n'entraînent pas de frais même si le trafic s'effectue entre les régions et entre les centres de données. **Il est recommandé d'utiliser un noeud final privé, dans la mesure du possible. **
* Les **noeuds finaux publics** peuvent accepter des demandes de n'importe où et les frais sont imputés à la bande passante sortante. La bande passante entrante est gratuite. Les noeuds finaux publics doivent être utilisés pour les accès autres que ceux des ressources de cloud computing {{site.data.keyword.cloud_notm}}.  

Les demandes doivent être envoyées au noeud final associé à un emplacement de compartiment défini. Si vous n'êtes pas sûr de l'emplacement d'un compartiment, il existe une [extension de l'API de liste de compartiments](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) qui renvoie les informations relatives à l'emplacement et à la classe de stockage pour tous les compartiments d'une instance de service. 

Nous avons mis à jour nos noeuds finaux en décembre 2018. Les noeuds finaux existants continueront de fonctionner jusqu'à nouvel ordre. Mettez à jour vos applications pour qu'elles utilisent les [nouveaux noeuds finaux](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints). {:note}
