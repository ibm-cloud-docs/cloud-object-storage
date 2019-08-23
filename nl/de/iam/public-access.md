---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: public, cdn, anonymous, files

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Öffentlichen Zugriff erlauben
{: #iam-public-access}

Manchmal sind Daten für die gemeinsame Nutzung bestimmt. Buckets können offene Datensätze für die akademische und private Recherche oder Image-Repositorys enthalten, die von Webanwendungen und Content Delivery Networks genutzt werden. Diese Buckets können Sie mithilfe der Gruppe **Öffentlicher Zugriff** zugänglich machen.
{: shortdesc}

## Konsole zum Festlegen des öffentlichen Zugriffs verwenden
{: #iam-public-access-console}

Stellen Sie zuerst sicher, dass Sie über ein Bucket verfügen. Sollte das nicht der Fall sein, gehen Sie anhand der Schritte im [Lernprogramm zur Einführung](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) vor, um sich mit der Konsole vertraut zu machen.

### Öffentlichen Zugriff aktivieren
{: #public-access-console-enable}

1. Wählen Sie im Dashboard der {{site.data.keyword.cloud_notm}} [-Konsole](https://cloud.ibm.com/) die Option **Speicher** aus, um Ihre Ressourcenliste anzuzeigen.
2. Wählen Sie als Nächstes im Menü **Speicher** die Serviceinstanz mit Ihrem Bucket aus. Dadurch gelangen Sie zur {site.data.keyword.cos_short}-Konsole.
3. Wählen Sie das Bucket aus, das Sie öffentlich zugänglich machen möchten. Berücksichtigen Sie dabei, dass durch diese Richtlinie _alle Objekte in einem Bucket_ für jeden beliebigen Benutzer mit der entsprechenden URL zum Herunterladen verfügbar werden.
4. Wählen Sie im Navigationsmenü die Option **Zugriffsrichtlinien** aus.
5. Wählen Sie die Registerkarte **Öffentlicher Zugriff** aus.
6. Klicken Sie auf **Zugriffsrichtlinie erstellen**. Lesen Sie die Warnung und wählen Sie anschließend **aktivieren** aus.
7. Nun sind alle Objekte in diesem Bereich öffentlich zugänglich!

### Öffentlichen Zugriff inaktivieren
{: #public-access-console-disable}

1. Wählen Sie von einer beliebigen Position in der {{site.data.keyword.cloud_notm}} [-Konsole](https://cloud.ibm.com/) das Menü **Verwalten** aus und wählen Sie **Zugriff (IAM)** aus.
2. Wählen Sie im Navigationsmenü die Option **Zugriffsgruppen** aus.
3. Wählen Sie **Öffentlicher Zugriff** aus, um eine Liste aller derzeit verwendeten Richtlinien für öffentlichen Zugriff abzurufen.
4. Suchen Sie die Richtlinie, die dem Bucket entspricht, den Sie erneut der durchgesetzten Zugriffssteuerung unterstellen möchten.
5. Wählen Sie in der Liste der Aktionen ganz rechts neben dem Richtlinieneintrag **Entfernen** aus.
6. Bestätigen Sie das Dialogfeld. Die Richtlinie wird jetzt aus dem Bucket entfernt.

## Öffentlichen Zugriff auf einzelne Objekte erlauben
{: #public-access-object}

Wenn Sie ein Objekt über die REST-API über öffentlichen Zugriff zugänglich machen möchten, können Sie einen Header vom Typ `x-amz-acl: public-read` in die Anforderung einbinden. Die Festlegung dieses Headers bewirkt die Umgehung aller [IAM-Richtlinien](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview)überprüfungen und lässt nicht authentifizierte `HEAD`- und `GET`-Anforderungen zu. Weitere Informationen zu Endpunkten enthält [Endpunkte und Speicherpositionen](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Außerdem ist es durch [HMAC-Berechtigungsnachweise](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) möglich, [temporär öffentlichen Zugriff zuzulassen, der vorab signierte URLs zulässt](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url).

### Öffentliches Objekt hochladen
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpunkt}/{bucketname}/{objektname}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{objektinhalt}"
```
{: codeblock}

### Öffentlichen Zugriff auf ein vorhandenes Objekt zulassen
{: #public-access-object-existing}

Durch die Verwendung des Abfrageparameters `?acl` ohne Nutzdaten und den Header `x-amz-acl: public-read` wird der öffentliche Zugriff auf das Objekt zugelassen, ohne dass hierzu die Daten überschrieben werden müssen.

```sh
curl -X "PUT" "https://{endpunkt}/{bucketname}/{objektname}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### Öffentliches Objekt erneut als privat definieren
{: #public-access-object-private}

Durch die Verwendung des Abfrageparameters `?acl` ohne Nutzdaten und den leeren Header `x-amz-acl:` wird der öffentliche Zugriff auf das Objekt widerrufen, ohne dass hierzu die Daten überschrieben werden müssen.

```sh
curl -X "PUT" "https://{endpunkt}/{bucketname}/{objektname}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## Statische Websites
{: #public-access-static-website}

Auch wenn {{site.data.keyword.cos_full_notm}} das automatische Hosting statischer Websites nicht unterstützt, ist es möglich, einen Webserver manuell zu konfigurieren und ihn für öffentlich zugängliche Inhalte, die in einem Bucket gehostet werden, zu verwenden. Weitere Informationen finden Sie in [diesem Lernprogramm](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).
