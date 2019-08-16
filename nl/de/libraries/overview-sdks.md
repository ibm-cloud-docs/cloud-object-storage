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

# Informationen zu IBM COS-SDKs
{: #sdk-about}

Von IBM COS werden SDKs für Java, Python, Node.js und Go bereitgestellt. Diese SDKs basieren auf den offiziellen AWS S3-API-SDKs, wurden jedoch so geändert, dass von ihnen IBM Cloud-Funktionen wie IAM, Key Protect, unveränderlicher Objektspeicher, etc verwendet werden kann.

| Funktion                    | Java                                              | Python                                            | Node.js                                           | GO                                                | Befehlszeilenschnittstelle                        |
|-----------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| Unterstützung des IAM-API-Schlüssels | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |
| Verwaltete mehrteilige Uploads | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |
| Verwaltete mehrteilige Downloads | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Erweiterte Bucketliste      |                                                   |                                                   |                                                   |                                                   |                                                   |
| Objektliste der Version 2   | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Key Protect                 | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |
| SSE-C                       | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Archivierungsregeln         | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aufbewahrungsrichtlinien    | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |                                                   |                                                   |
| Aspera-Hochgeschwindigkeitsübertragung | ![Hakensymbol](../../icons/checkmark-icon.svg) | ![Hakensymbol](../../icons/checkmark-icon.svg) |                                                   |                                                   |                                                   |

## Unterstützung des IAM-API-Schlüssels
{: #sdk-about-iam}
Ermöglicht das Erstellen von Clients mit einem API-Schlüssel anstatt einem Paar aus Zugriffsschlüsseln und geheimen Schlüsseln. Die Token werden automatisch verwaltet und bei Operationen mit langer Laufzeit automatisch aktualisiert.
## Verwaltete mehrteilige Uploads
Unter Verwendung der Klasse `TransferManager` wird vom SDK die gesamte erforderliche Logik zum Hochladen von Objekten in mehreren Teilen verarbeitet.
## Verwaltete mehrteilige Downloads
Unter Verwendung der Klasse `TransferManager` wird vom SDK die gesamte erforderliche Logik zum Herunterladen von Objekten in mehreren Teilen verarbeitet.
## Erweiterte Bucketliste
Dies ist eine Erweiterung der S3-API, von der eine Liste der Buckets mit Bereitstellungscodes (eine Kombination aus Position und Speicherklasse des Buckets, die als `LocationConstraint` zurückgegeben wird) für Buckets beim Auflisten zurückgegeben wird. Dies ist bei der Suche nach einem Bucket nützlich, da alle Buckets in einer Serviceinstanz unabhängig vom verwendeten Endpunkt aufgelistet werden.
## Objektliste der Version 2
Die Liste der Version 2 ermöglicht ein leistungsfähigeres Scoping der Objektliste.
## Key Protect
Key Protect ist ein IBM Cloud-Service, von dem Verschlüsselungsschlüssel verwaltet werden; dieser Service ist ein optionaler Parameter während der Erstellung von Buckets.
## SSE-C                      
## Archivierungsregeln              
## Aufbewahrungsrichtlinien         
## Aspera-Hochgeschwindigkeitsübertragung 
