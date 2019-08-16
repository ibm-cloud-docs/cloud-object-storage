---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-21"

keywords: ip address, firewall, configuration, api

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

# Firewall einrichten
{: #setting-a-firewall}

IAM-Richtlinien bieten Administratoren eine Möglichkeit, den Zugriff auf einzelne Buckets einzuschränken. Dies kann z. B. für Daten der Fall sein, auf die nur über vertrauenswürdige Netze zugegriffen werden darf. Mit einer Bucket-Firewall können Sie den gesamten Zugriff auf Ihre Daten einschränken, es sei denn, die entsprechende Anforderung stammt von einer Quelle, die in der Liste zulässiger IP-Adressen enthalten ist.
{: shortdesc}

Für das Einrichten einer Firewall gelten die folgenden Regeln:

* Ein Benutzer, der eine Firewall einrichten oder anzeigen möchte, muss über die Rolle `Manager` für das betreffende Bucket verfügen. 
* Ein Benutzer mit der Rolle `Manager` für das Bucket kann die Liste der zulässigen IP-Adressen über jede IP-Adresse anzeigen und bearbeiten, um so unbeabsichtigte Aussperrungen zu verhindern.
* Über die {{site.data.keyword.cos_short}}-Konsole kann weiterhin auf das Bucket zugegriffen werden, sofern die IP-Adresse des Benutzers über die entsprechenden Berechtigungen verfügt.
* Andere {{site.data.keyword.cloud_notm}}-Services **sind nicht berechtigt**, die Firewall zu umgehen. Diese Einschränkung bedeutet, dass andere Services, die beim Bucketzugriff auf Basis von IAM-Richtlinien arbeiten (z. B. Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions usw.), die Firewall nicht umgehen können.

Nach Einrichtung einer Firewall wird das Bucket von den restlichen Komponenten von {{site.data.keyword.cloud_notm}} isoliert. Beachten Sie die Auswirkungen dieses Vorgangs auf Anwendungen und Workflows, die von anderen Services abhängig sind, die direkt auf ein Bucket zugreifen, bevor Sie die Firewall aktivieren.
{: important}

## Konsole zum Einrichten einer Firewall verwenden
{: #firewall-console}

Stellen Sie zunächst sicher, dass Sie über ein Bucket verfügen. Falls dies nicht der Fall ist, befolgen Sie die Anweisungen im [Lernprogramm 'Einführung'](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started), um sich mit der Konsole vertraut zu machen.

### Liste der berechtigten IP-Adressen festlegen
{: #firewall-console-enable}

1. Wählen Sie im [Konsolendashboard](https://cloud.ibm.com/) von {{site.data.keyword.cloud_notm}} die Option **Speicher** aus, um die Liste Ihrer Ressourcen anzuzeigen.
2. Wählen Sie als Nächstes im Menü **Speicher** die Serviceinstanz mit Ihrem Bucket aus. Daraufhin wird die {{site.data.keyword.cos_short}}-Konsole aufgerufen.
3. Wählen Sie das Bucket aus, für das Sie den Zugriff auf berechtigte IP-Adressen beschränken möchten. 
4. Wählen Sie im Navigationsmenü die Option **Zugriffsrichtlinien** aus.
5. Wählen Sie die Registerkarte **Berechtigte IPs** aus.
6. Klicken Sie auf **IP-Adressen hinzufügen** und wählen Sie dann **Hinzufügen** aus.
7. Fügen Sie eine Liste mit IP-Adressen in [CIDR-Notation](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing) hinzu. Beispiel: `192.168.0.0/16, fe80:021b::0/64`. Die Adressen können entweder auf Basis des IPv4- oder des IPv6-Standards angegeben werden. Klicken Sie auf **Hinzufügen**.
8. Die Firewall wird erst dann zwingend verwendet, wenn die Adresse in der Konsole gespeichert wird. Klicken Sie auf **Alle speichern**, um die Firewall zwingend zu verwenden.
9. Nun können Sie auf alle Objekte in diesem Bucket nur noch über diese IP-Adressen zugreifen!

### Beschränkungen der IP-Adressen entfernen
{: #firewalls-console-disable}

1. Markieren Sie auf der Registerkarte **Berechtigte IPs** die Kontrollkästchen neben den IP-Adressen oder IP-Adressbereichen, die aus der Liste der berechtigten IP-Adressen entfernt werden sollen.
2. Wählen Sie **Löschen** aus und bestätigen Sie anschließend das Dialogfenster, indem Sie erneut auf **Löschen** klicken.
3. Die aktualisierte Liste wird erst zwingend verwendet, wenn die Änderungen in der Konsole gespeichert werden. Klicken Sie auf **Alle speichern**, um die Verwendung der neuen Regeln zu erzwingen.
4. Nun können Sie auf alle Objekte in diesem Bucket nur noch über diese IP-Adressen zugreifen!

Enthält die Liste keine berechtigten IP-Adressen, dann bedeutet dies, dass für das Bucket die normalen IAM-Richtlinien ohne Beschränkungen in Bezug auf die IP-Adresse des Benutzers gelten.
{: note}


## Firewall über API einrichten
{: #firewall-api}

Firewalls werden über die [API für die COS-Ressourcenkonfiguration](https://cloud.ibm.com/apidocs/cos/cos-configuration) verwaltet. Diese neue REST-API wird zur Konfiguration von Buckets verwendet. 

Benutzer mit der Rolle `Manager` können die Liste der zulässigen IP-Adressen über jedes Netz anzeigen und bearbeiten, um so unbeabsichtigte Aussperrungen zu verhindern.
{: tip}
