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

# Zusätzliche Endpunktinformationen
{: #advanced-endpoints}

Die Ausfallsicherheit eines Buckets wird durch den Endpunkt definiert, mit dem er erstellt wurde. Bei _regionsübergreifender_ Ausfallsicherheit werden Ihre Daten über mehrere Ballungsräume gestreut, während die Daten bei _regionaler_ Ausfallsicherheit auf einen einzigen Ballungsraum verteilt werden. Bei der Verwendung eines _einzelnen Rechenzentrums_ für die Ausfallsicherheit werden Daten auf mehrere Appliances in einem einzigen Rechenzentrum verteilt. Regionale und regionsübergreifende Buckets können die Verfügbarkeit während eines Ausfalls des Standorts aufrechterhalten.

Für Datenverarbeitungsworkloads, die sich in der Nachbarschaft eines regionalen {{site.data.keyword.cos_short}}-Endpunkts befinden, wird eine geringere Latenzzeit bei besseren Leistungswerten verzeichnet. Bei Workloads, die sich nicht in einem einzelnen geografischen Bereich befinden, werden Verbindungen von einem regionsübergreifenden `geo`-Endpunkt an die nächsten regionalen Rechenzentren weitergeleitet.

Bei Verwendung eines regionsübergreifenden Endpunkts ist es möglich, den eingehenden Datenverkehr auf einen bestimmten Zugriffspunkt zu lenken und die Daten trotzdem gleichzeitig über alle drei Regionen zu verteilen. Wenn Anfragen an einen einzelnen Zugriffspunkt gesendet werden, gibt es keinen automatischen Failover, wenn die Verfügbarkeit dieser Region plötzlich nicht mehr gegeben ist. Anwendungen, die den Datenverkehr nicht an den `geo`-Endpunkt, sondern an einen Zugriffspunkt leiten, **müssen** intern eine entsprechende Failover-Logik implementieren, um die Vorteile des regionsübergreifenden Speichers hinsichtlich der Verfügbarkeit nutzen zu können.
{:tip}

Manche Workloads können von der Verwendung eines Endpunkts mit einem einzelnen Rechenzentrum profitieren. An einem einzigen Standort gespeicherte Daten werden zwar in einem einzigen Rechenzentrum gespeichert, sind aber trotzdem auf viele physische Speicher-Appliances verteilt. Das kann die Leistung für Datenverarbeitungsressourcen innerhalb derselben Website verbessern, kann die Verfügbarkeit bei einem Ausfall des Standorts aber nicht sicherstellen. Buckets mit einem einzelnen Rechenzentrum bieten im Falle einer Zerstörung des Standorts keine automatisierte Replikation bzw. kein automatisiertes Backup, sodass bei allen Anwendungen, die einen einzelnen Standort verwenden, die Disaster-Recovery im Design berücksichtigt werden sollte.

Alle Anfragen müssen bei der Verwendung von IAM SSL verwenden und der Service lehnt alle Anforderungen in Klartext ab.

Arten von Endpunkt:

{{site.data.keyword.cloud}}-Services sind mit einem aus drei Tiers bestehenden Netz verbunden, das eine Segmentierung von öffentlichem Datenverkehr, privatem Datenverkehr und Managementdatenverkehr bewirkt.

* **Private Endpunkte** sind für Anforderungen verfügbar, die von Kubernetes-Clustern, Bare-Metal-Servern, virtuellen Servern und anderen Cloudspeicherservices stammen. Private Endpunkte bieten eine bessere Leistung und verursachen keine Kosten für ausgehende oder eingehende Bandbreite, selbst wenn der Datenverkehr über mehrere Regionen oder Rechenzentren hinweg erfolgt. **Es ist am besten, nach Möglichkeit einen privaten Endpunkt zu verwenden.**
* **Öffentliche Endpunkte** können Anforderungen von überall akzeptieren und die Kosten werden nach ausgehender Bandbreite berechnet. Eingehende Bandbreite ist kostenlos. Öffentliche Endpunkte sollten für Zugriff verwendet werden, der nicht von einer {{site.data.keyword.cloud_notm}}-Cloud-Computing-Ressource stammt. 

Anforderungen müssen an den Endpunkt gesendet werden, der dem Standort eines bestimmten Buckets zugeordnet ist. Falls Sie nicht sicher sind, wo sich ein Bucket befindet, können Sie eine [Erweiterung der API zur Auflistung von Buckets](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations#compatibility-api-list-buckets-extended) verwenden, die die Standort- und Speicherklasseninformationen für alle Buckets in einer Serviceinstanz zurückgibt.

Ab Dezember 2018 wurden die Endpunkte aktualisiert. Herkömmliche Endpunkte funktionieren bis auf Weiteres weiterhin. Aktualisieren Sie Ihre Anwendungen so, dass sie die [neuen Endpunkte](https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints) verwenden.
{:note}
