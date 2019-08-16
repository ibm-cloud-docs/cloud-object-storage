---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-08"

keywords: tutorial, web application, photo galleries

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

# Lernprogramm: Webanwendung für Bildergalerie
{: #web-application}

Die Erstellung einer Webanwendung umfasst vom Anfang bis zum Ende eine Vielzahl an unterschiedlichen Konzepten und ist eine hervorragende Möglichkeit, um sich mit den Funktionen von {{site.data.keyword.cos_full}} vertraut zu machen. In diesem Lernprogramm wird beschrieben, wie eine einfache Bildergalerie auf der {{site.data.keyword.cloud}}-Plattform erstellt und viele unterschiedliche Konzepte und Verfahren miteinander verknüpft werden. Von der Anwendung wird {{site.data.keyword.cos_full_notm}} als Back-End-Server für eine Node.js-Anwendung verwendet, die es dem Benutzer ermöglicht, JPEG-Bilddateien hochzuladen und anzuzeigen.

## Vorbereitende Schritte
{: #wa-prereqs}

Zum Erstellen einer Webanwendung beginnen wir mit den folgenden Voraussetzungen:

  - Konto der {{site.data.keyword.cloud_notm}}-Plattform
  - Docker als Bestandteil von {{site.data.keyword.cloud_notm}} Developer Tools
  - Node.js 
  - Git (sowohl Desktop-App als auch Befehlszeilenschnittstelle &mdash; CLI)

### Docker installieren
{: #tutorial-wa-install-docker}

Durch den Wechsel bei der Erstellung von Webanwendungen mit traditionellen Serverinstanzen oder sogar virtuellen Servern zur Verwendung von Containern wie Docker wird die Entwicklung beschleunigt und das Testen vereinfacht, während die automatisierte Bereitstellung unterstützt wird. Ein Container weist eine schlanke Struktur auf, für die kein zusätzlicher Aufwand wie bei einem Betriebssystem erforderlich ist; benötigt werden nur der Code und die Konfiguration für alle Angaben von den Abhängigkeiten bis zu den Einstellungen.

Beginnen Sie mit dem Öffnen eines Tools, mit dem erfahrene Entwickler vertraut sind, und das der neue beste Freund aller sein wird, die gerade anfangen: Der Befehlszeile. Seit die grafische Benutzerschnittstelle (GUI) erfunden wurde, wurde die Befehlszeilenschnittstelle des Computers zu einer Schnittstelle zweiter Klasse degradiert. Jetzt ist der Zeitpunkt gekommen, ihr wieder mehr Beachtung zu schenken (auch wenn die GUI so schnell nicht verschwinden wird &mdash; besonders bei der Suche im Web zum Herunterladen des neuen Toolsets für die Befehlszeile). 

Öffnen Sie das Terminal oder eine andere geeignete Befehlszeilenschnittstelle für Ihr Betriebssystem und erstellen Sie ein Verzeichnis mit den Befehlen, die für die jeweilige Shell geeignet sind, die Sie verwenden. Wechseln Sie in das neue Verzeichnis, das Sie soeben erstellt haben. Nach der Erstellung verfügt Ihre Anwendung über ein eigenes Unterverzeichnis in diesem Verzeichnis, in dem der Startcode und die Konfiguration enthalten sind, die für die Betriebsbereitschaft erforderlich sind.

Verlassen Sie die Befehlszeile, kehren Sie zum Browser zurück und gehen Sie gemäß den Anweisungen zum Installieren von [{{site.data.keyword.cloud_notm}} Platform Developer Tools](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) unter dem nebenstehenden Link vor. Developer Tools bietet eine erweiterbare und wiederholbare Methode zum Erstellen und Bereitstellen von Cloudanwendungen.

[Docker](https://www.docker.com) wird als Bestandteil von Developer Tools installiert; Docker wird hauptsächlich im Hintergrund in Routinen ausgeführt, von denen das Gerüst einer neuen App erstellt wird. Docker muss ausgeführt werden, damit die Erstellungsbefehle funktionieren. Fahren Sie mit der Erstellung eines Docker-Kontos online unter [Dockerhub](https://hub.docker.com) fort, führen Sie Docker aus und melden Sie sich an.

### Node.js installieren
{: #tutorial-wa-install-node}

Von der App, die Sie erstellen, wird [Node.js](https://nodejs.org/) als serverseitige Engine zum Ausführen des JavaScript-Codes für diese Webanwendung verwendet. Damit Sie den im Lieferumfang von Node enthaltenen Node Package Manager (npm) zum Verwalten der Appabhängigkeiten verwenden können, müssen Sie Node.js lokal installieren. Wenn Node.js lokal installiert ist, verläuft auch das Testen schneller und somit auch die Entwicklung. 

Bevor Sie beginnen, sollten Sie in Betracht ziehen, einen Versionsmanager wie Node Version Manager oder `nvm` zum Installieren von Node zu verwenden, um die Komplexität der Verwaltung unterschiedlicher Versionen von Node.js zu reduzieren. Gegenwärtig können Sie zum Installieren oder Aktualisieren von `nvm` auf einer Mac OS- oder Linux-Maschine das Installationsscript mithilfe von cURL in der Befehlszeilenschnittstelle verwenden, die Sie gerade geöffnet haben; kopieren Sie hierzu einen der Befehle in den beiden ersten Beispielen in die Befehlszeile und drücken Sie die Eingabetaste (beachten Sie hierbei, dass vorausgesetzt wird, dass Sie eine Bash-Shell und keine andere Shell verwenden):

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Beispiel 1. cURL zum Installieren von Node Version Manager (nvm) verwenden" caption-side="bottom"}
`Beispiel 1. cURL zum Installieren von Node Version Manager (nvm) verwenden`
   
...oder Wget (nur ein Befehl ist erforderlich, nicht beide; verwenden Sie den Befehl, der auf Ihrem System verfügbar ist):

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Beispiel 2. Wget zum Installieren von Node Version Manager (nvm) verwenden" caption-side="bottom"}
`Beispiel 2. Wget zum Installieren von Node Version Manager (nvm) verwenden`

Unter Windows können Sie alternativ [nvm for Windows](https://github.com/coreybutler/nvm-windows) mit den Installationsprogrammen und dem Quellcode unter dem angegebenen Link verwenden.

Wenn Sie die zusätzliche Komplexität vermeiden möchten, die sich aus der Unterstützung mehrerer Releases von Node.js ergibt, besuchen Sie die Website [Node.js](https://nodejs.org/en/download/releases/) und installieren Sie die LTS-Version (LTS = Long Term Support) von Node.js, die mit der neuesten Version übereinstimmt, die vom SDK für Node.js-Buildpack unterstützt wird, das jetzt auf der {{site.data.keyword.cloud_notm}}-Plattform verwendet wird. Das neueste Buildpack ist im Moment Version 3.26; von ihm wird Node.js Community Edition Version 6.17.0+ unterstützt. 

Weitere Informationen zum neuesten {{site.data.keyword.cloud_notm}}-SDK für das Node.js-Buildpack finden Sie auf der Seite [Neueste Aktualisierungen für das SDK für Node.js](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates). 

Mit `nvm` können Sie die Version von Node installieren, die den Anforderungen zum Kopieren und Einfügen des Befehls aus Beispiel 3 in die Befehlszeile entspricht.

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="Beispiel 3. 'nvm' zum Installieren einer bestimmten Version von Node.js verwenden" caption-side="bottom"}
`Beispiel 3. 'nvm' zum Installieren einer bestimmten Version von Node.js verwenden`

Sobald Sie die Anweisungen zum Installieren von Node.js und npm (wird beides mit Node bereitgestellt) auf Ihrem Computer abhängig von Ihrem Betriebssystem und der jeweiligen Strategie ausgeführt haben, haben Sie die ersten Schritte erfolgreich abgeschlossen.

### Git installieren
{: #tutorial-wa-install-git}

Sie sind wahrscheinlich bereits mit Git vertraut, da es das am weitesten verbreitete System für die Versionskontrolle von Quellcode unter Entwicklern ist, die Anwendungen für das Web erstellen. Git wird später verwendet, wenn eine Toolchain für fortlaufende Bereitstellung (Continuous Deployment, CD) in der {{site.data.keyword.cloud_notm}}-Plattform für fortlaufende Bereitstellung und Entwicklung erstellt wird. Wenn Sie nicht über ein GitHub-Konto verfügen, erstellen Sie auf der Website [GitHub](https://github.com/join) ein kostenloses öffentliches Privatkonto; andernfalls können Sie sich an einem anderen Konto anmelden, über das Sie möglicherweise verfügen.

Beachten Sie die schrittweisen Anweisungen zum Generieren und Hochladen von SSH-Schlüsseln in das [GitHub-Profil](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) zum Schutz des Zugriffs auf GitHub von der Befehlszeile aus. Wenn Sie diese Schritte nun ausführen, dient dies allerdings nur dazu, dass Sie eine gewisse Routine bekommen, denn Sie müssen die Schritte für die für die {{site.data.keyword.cloud_notm}}-Plattform verwendete GitHub-Instanz wiederholen, auf die zu einem späteren Zeitpunkt zugegriffen wird. Die Schritte zur Verwendung von SSH-Schlüsseln können kompliziert sein, mit etwas Übung ist der Umgang mit SSH in der Befehlszeilenschnittstelle aber kein Problem mehr.

Rufen Sie jetzt die Seite [GitHub Desktop](https://desktop.github.com/) auf, um GitHub Desktop herunterzuladen, und führen Sie anschließend das Installationsprogramm aus. Wenn das Installationsprogramm beendet wird, werden Sie aufgefordert, sich mit Ihrem Konto an GitHub anzumelden.

Geben Sie im Anmeldefenster (siehe erste Abbildung in diesem Lernprogramm) den Namen und die E-Mail ein, die öffentlich für alle Commits für Ihr Repository angezeigt werden sollen (sofern Sie über ein öffentliches Konto verfügen). Wenn Sie die Anwendung mit Ihrem Programm verknüpft haben, werden Sie möglicherweise aufgefordert, die Anwendungsverbindung über das GitHub-Konto online zu überprüfen.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

Bisher müssen Sie keine Repositorys erstellen. Wenn Sie ein Repository mit dem Namen "Tutorial" entdecken, das mit GitHub Desktop bereitgestellt wird, können Sie mit diesem Repository experimentieren, um sich mit den Operationen vertraut zu machen. Jetzt haben Sie den Abschnitt mit den Voraussetzungen in diesem Lernprogramm abgeschlossen. Sind Sie bereit, eine App zu erstellen?

## Starter-App 'Node.js' mit Developer Tools erstellen
{: #tutorial-create-skeleton}

Beginnen Sie zum lokalen Starten einer Anwendung mit dem Anmelden an der {{site.data.keyword.cloud_notm}}-Plattform direkt in der Befehlszeile wie in Beispiel 4 beschrieben. 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="Beispiel 4. Befehl zum Anmelden an der IBM Cloud-Plattform mit Developer Tools in der Befehlszeilenschnittstelle" caption-side="bottom"}
`Beispiel 4. Befehl zum Anmelden an der IBM Cloud-Plattform mit Developer Tools in der Befehlszeilenschnittstelle`

Bei Bedarf können Sie optionale Parameter angeben: Ihre Organisation mit '-o', den Bereich mit '-s' oder bei Verwendung eines föderierten Kontos '--sso'. Es kann vorkommen, dass Sie beim Anmelden aufgefordert werden, eine Region auszuwählen; wählen Sie für diese Übung `us-south` als Region aus, da dieselbe Option später in diesem Lernprogramm beim Erstellen einer CD-Toolchain verwendet wird.  

Legen Sie als nächsten Schritt mit dem in Beispiel 5 aufgeführten Befehl den Endpunkt fest (falls noch keiner festgelegt wurde); weitere Endpunkte sind möglich und können für die Produktion auch günstiger sein, verwenden Sie vorerst aber den dargestellten Code, sofern dieser für Ihr Konto geeignet ist.

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="Beispiel 5. Befehl zum Festlegen des API-Endpunkts für das Konto" caption-side="bottom"}
`Beispiel 5. Befehl zum Festlegen des API-Endpunkts für das Konto`

Wählen Sie den Aspekt 'Cloud Foundry' (cf) der {{site.data.keyword.cloud_notm}}-Plattform unter Verwendung des in Beispiel 6 beschriebenen Codes mit dem Befehl 'target' und der Option '--cf' als Ziel fest. Die API `cf` ist in Developer Tools in die Befehlszeilenschnittstelle eingebettet.

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="Beispiel 6. Optionen für Verwendung der Cloud Foundry-API festlegen" caption-side="bottom"}
`Beispiel 6. Optionen für Verwendung der Cloud Foundry-API festlegen`

Jetzt ist der Moment gekommen, auf den Sie hingearbeitet haben: Die Erstellung der Webanwendung beginnt mit dem Code in Beispiel 7. Der Bereich `dev` ist eine Standardoption für die Organisation, es kann aber sinnvoll sein, weitere zum Abgrenzen unterschiedlicher Aufgabenbereiche zu erstellen, zum Beispiel um die Finanzabteilung von der Entwicklungsabteilung zu trennen.

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="Beispiel 7. Befehl zum Erstellen einer App mithilfe von IBM Cloud Developer Tools" caption-side="bottom"}
`Beispiel 7. Befehl zum Erstellen einer App mithilfe von IBM Cloud Developer Tools`

Bei der Ausführung dieses Befehls werden Ihnen einige Fragen gestellt. Sie können an vielen Punkten des Prozesses zurück gehen, wenn Sie jedoch den Eindruck haben, dass etwas fehlt oder Sie Schritte übersprungen haben, sollten Sie nicht zögern, erneut zu starten; löschen Sie hierzu das Verzeichnis oder erstellen Sie ein weiteres zum Testen und Erkunden der Vorgehensweisen. Auch wenn Sie den Prozess zur lokalen Erstellung der Anwendung in der Befehlszeile ausführen, können Sie die Ergebnisse später online im {{site.data.keyword.cloud_notm}}-Onlineportal anzeigen, in dem Sie Ihr Konto erstellt haben, um die erstellten Ressourcen zu verwalten.

Beachten Sie in Beispiel 8 die Option zum Erstellen einer 'Web-App' &mdash; das ist die App, die Sie erstellen möchten. Geben Sie '2' ein und drücken Sie die Eingabetaste.

```
                                        
--------------------------------------------------------------------------------
Select an application type:
--------------------------------------------------------------------------------
 1. Blank App
 2. Backend Service / Web App
 3. Mobile App
--------------------------------------------------------------------------------
 0. Exit
--------------------------------------------------------------------------------
? Enter selection number:> 2


```
{: caption="Beispiel 8. Ausgabe des Befehls 'ibmcloud dev create', für den Sie Option 2 für eine Web-App ausgewählt haben" caption-side="bottom"}
`Beispiel 8. Ausgabe des Befehls 'ibmcloud dev create', für den Sie Option 2 für eine Web-App ausgewählt haben`

In Beispiel 9 gibt es eine Reihe an Optionen, die auf den sogenannten "Buildpacks" basieren; beachten Sie die Option für die Verwendung von 'Node'. Geben Sie '4' ein und drücken Sie die Eingabetaste.

```

--------------------------------------------------------------------------------
Select a language:
--------------------------------------------------------------------------------
 1. Go
 2. Java - MicroProfile / Java EE
 3. Java - Spring
 4. Node
 5. Python - Django
 6. Python - Flask
 7. Scala
 8. Swift
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 4


```
{: caption="Beispiel 9. Fortsetzung der Sprachenoptionen von 'ibmcloud dev create'" caption-side="bottom"}
`Beispiel 9. Fortsetzung der Sprachenoptionen von 'ibmcloud dev create'`

Nach der Auswahl der Programmiersprache und/oder des Frameworks stehen für die nächste Auswahl in Beispiel 10 so viele Optionen zur Verfügung, dass Sie möglicherweise zu dem von Ihnen gewünschten Service blättern müssen. Wie im Beispiel beschrieben soll eine einfache Node.js-Web-App mit Express.js ausgewählt werden. Geben Sie '6' ein und drücken Sie die Eingabetaste.

```
? Select a Starter Kit:

--------------------------------------------------------------------------------
APPSERVICE
--------------------------------------------------------------------------------
 1. MEAN Stack: MongoDb, Express.js, Angular, Node.js - A starter
    project for setting up a mongodb, express, angular and node application
 2. MERN Stack: MongoDb, Express.js, React, Node.js - A starter
    project for setting up a mongodb, express, react and node application
 3. Node.js BFF Example with Express.js - A starter for building
    backend-for-frontend APIs in Node.js, using the Express.js framework.
 4. Node.js Example Serverless App - A starter providing a set of
    Cloud Functions and API for a serverless backend that uses Cloudant NoSQL
    database.
 5. Node.js Microservice with Express.js - A starter for building a
    microservice backend in Node.js, using the Express.js framework.
 6. Node.js Web App with Express.js - A starter that provides a basic
    web serving application in Node.js, using the Express.js framework.
 7. Node.js Web App with Express.js and React - A starter that
    provides a rich React frontend delivered from a Node.js application,
    including key web development tools Gulp, SaaS, and Webpack, using the
    Express.js framework.

--------------------------------------------------------------------------------
FINANCE
--------------------------------------------------------------------------------
 8. Wealth Management Chatbot - A chatbot that allows the user to
    query the status of their investments and evaluate the impact of different
    market scenarios on their investment portfolio. It can easily be extended
    in several ways.

--------------------------------------------------------------------------------
WATSON
--------------------------------------------------------------------------------
 9. Watson Assistant Basic - Simple application that demonstrates the
    Watson Assistant service in a chat interface simulating banking tasks.
10. Watson Natural Language Understanding Basic - Collection of APIs
    that can analyze text to help you understand its concepts, entities,
    keywords, sentiment, and can create a custom model for some APIs to get
    specific results that are tailored to your domain.
11. Watson News Intelligence - This starter kit demonstrates how to
    query news content to understand what people are saying or feeling about
    important topics.
12. Watson Speech to Text Basic - Basic sample of Speech to Text
    service to convert speech in multiple languages into text.
13. Watson Text to Speech Basic - Basic sample of how to use Text to
    Speech for streaming, low latency, synthesis of audio from text.
14. Watson Visual Recognition Basic - Use deep learning algorithms to
    analyze images that can give you insights into your visual content.
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 6

```
{: caption="Beispiel 10. Anwendungsoptionen für Gerüst von 'ibmcloud dev create'" caption-side="bottom"}
`Beispiel 10. Anwendungsoptionen für Gerüst von 'ibmcloud dev create'`

Sie haben jetzt zwar die einfacheren Optionen ausgewählt, die schwierigste Entscheidung für alle Entwickler steht aber noch aus: Die Benennung der App. Gehen wie in Beispiel 11 beschrieben vor, geben Sie 'webapplication' ein und drücken Sie die Eingabetaste.

```bash
? Enter a name for your application> webapplication
```
{: caption="Beispiel 11. Name 'webapplication' für Anwendung mit 'ibmcloud dev create' festlegen" caption-side="bottom"}
`Beispiel 11. Name 'webapplication' für Anwendung mit 'ibmcloud dev create' festlegen`

Später können Sie in der Webkonsole so viele Services hinzufügen (zum Beispiel Datenspeicher oder Rechenfunktionen), wie Sie benötigen oder wünschen. Geben Sie jedoch wie in Beispiel 12 beschrieben 'n' für Nein ein, wenn Sie gefragt werden, ob Sie zu diesem Zeitpunkt Services hinzufügen möchten.

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="Beispiel 12. Fortsetzung für das Hinzufügen von Services mit 'ibmcloud dev create'" caption-side="bottom"}
`Beispiel 12. Fortsetzung für das Hinzufügen von Services mit 'ibmcloud dev create'`

Früher wurden die Vorteile der Entwicklung mit Containern anstatt mit herkömmlichen Servern oder sogar virtuellen Servern im Zusammenhang mit Docker erwähnt. Eine Möglichkeit zur Verwaltung von Containern ist eine Orchestrierungssoftware wie Kubernetes, die in der Entwicklung zu einem _De-facto-Standard_ geworden ist. In diesem Lernprogramm wird dagegen ein einzelner Docker-Container mit Code, Bibliotheken und Konfiguration für die App vom Cloud Foundry-Service verwaltet.

Geben Sie wie in Beispiel 13 angegeben '1' ein und drücken Sie die Eingabetaste, um 'IBM DevOps' für die Integration von Continuous Delivery (CD) in den Projektlebenszyklus zu verwenden.
 
```

--------------------------------------------------------------------------------
Select from the following DevOps toolchain and target runtime environment
options:
 1. IBM DevOps, deploy to Cloud Foundry buildpacks
 2. IBM DevOps, deploy to Kubernetes containers
 3. No DevOps, with manual deployment
--------------------------------------------------------------------------------
? Enter selection number:> 1

```
{: caption="Beispiel 13. Bereitstellungsoptionen von 'ibmcloud dev create'" caption-side="bottom"}
`Beispiel 13. Bereitstellungsoptionen von 'ibmcloud dev create'`

Wie bereits erwähnt, wird eine Region für die CD-Toolchain zur automatisierten Bereitstellung ausgewählt; wählen Sie also wie in Beispiel 14 angegeben wieder Option '5' aus.

```

--------------------------------------------------------------------------------
Select a region for your toolchain from the following options:
--------------------------------------------------------------------------------
 1. eu-de (Frankfurt)
 2. eu-gb (London)
 3. jp-tok
 4. us-east (Washington DC)
 5. us-south (Dallas)
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 5

```
{: caption="Beispiel 14. Regionen, die als Optionen in 'ibmcloud dev create' verfügbar sind" caption-side="bottom"}
`Beispiel 14. Regionen, die als Optionen in 'ibmcloud dev create' verfügbar sind`

An diesem Punkt muss bei der Generierung einer neuen Anwendung berücksichtigt werden, dass für die Toolchain, die später zum Bereitstellen der App verwendet wird, wie in Beispiel 15 beschrieben, eine weitere Konfiguration erforderlich ist. Wie bereits erwähnt ist das Hochladen des öffentlichen Schlüssels zu GitHub (zur CD-Toolchain-Instanz auf der {{site.data.keyword.cloud_notm}}-Plattform) erforderlich, damit die bereitgestellte Anwendung mithilfe von GitHub bereitgestellt werden kann. Weitere Anweisungen finden Sie nach dem Bereitstellen Ihrer Anwendung und dem Anmelden am IBM Cloud-GitLab-Konto unter [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).

```

Note: For successful connection to the DevOps toolchain, this machine
must be configured for SSH access to your IBM Cloud GitLab account at
https://git.ng.bluemix.net/profile/keys in order to download the
application code.


```
{: caption="Beispiel 15. Hinweis: SSH-Schlüssel vom Befehl 'ibmcloud dev create'" caption-side="bottom"}
`Beispiel 15. Hinweis: SSH-Schlüssel vom Befehl 'ibmcloud dev create'`

Im Verlauf weiterer Eingabeaufforderungen werden Sie zum Bestätigen des Anwendungsnamens und des Toolchainnamens aufgefordert, die Sie vorher definiert haben. In Beispiel 16 wird beschrieben, wie der Hostname und der Toolchainname bei Bedarf geändert werden können. Der Hostname muss in der Domäne eindeutig sein, die als Serviceendpunkt der Anwendung verwendet wird; falls keine Konflikte auftreten, können Sie einfach die Eingabetaste drücken, wenn Sie zur Bestätigung aufgefordert werden.

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="Beispiel 16. Namen für Eigenschaften in 'ibmcloud dev create' bestätigen" caption-side="bottom"}
`Beispiel 16. Namen für Eigenschaften in 'ibmcloud dev create' bestätigen`

Wenn Sie den Link, den Sie am Ende der Ausgabe, die Sie nach dem Ausführen des Befehls `ibmcloud dev create` als Ergebnis erhalten, kopieren und einfügen, können Sie auf die CD-Toolchain zugreifen. Sie können darauf aber auch später über die Konsole zugreifen, wenn Sie den Link nicht erfasst haben. Wenn der Prozess fortgesetzt wird, folgen wie in Beispiel 17 angegeben weitere Informationen, in dem Anwendungseinträge online erstellt wurden und ein Verzeichnis mit Beispielcode erstellt wurde. 

```
Cloning repository
https://git.ng.bluemix.net/Organization.Name/webapplication...
Cloning into 'webapplication'...
remote: Counting objects: 60, done.
remote: Compressing objects: 100% (54/54), done.
remote: Total 60 (delta 4), reused 0 (delta 0)
Receiving objects: 100% (60/60), 50.04 KiB | 1.52 MiB/s, done.
Resolving deltas: 100% (4/4), done.
OK

The app, webapplication, has been successfully saved into the
current directory.

```
{: caption="Beispiel 17. Bestätigung der durch 'ibmcloud dev create' generierten Aktionen" caption-side="bottom"}
`Beispiel 17. Bestätigung der durch 'ibmcloud dev create' generierten Aktionen`

Diese letzte Anweisung aus Beispiel 17 bedeutet, dass beim Anzeigen des aktuellen Verzeichnisses jetzt das neue Unterverzeichnis `webapplication` angezeigt wird. Im Verzeichnis `webapplication` befindet sich ein Gerüst der neuen Node.js-Anwendung. Die Anleitung ist zwar weitgehend vorhanden, die Bestandteile, die noch in einem Docker-Image enthalten sind, müssen jedoch erst noch erstellt werden; hierfür wird der Befehl in Beispiel 18 ausgeführt. Docker sollte aufgrund der Installation bereits auf der lokalen Maschine aktiv sein; falls dies nicht der Fall sein sollte, starten Sie Docker jetzt. Jeder Versuch, die neue Webanwendung zu erstellen, ohne dass Docker ausgeführt wird, schlägt fehl; dies ist jedoch nicht der einzige mögliche Grund für ein Fehlschlagen. Falls ein Problem auftritt, überprüfen Sie die entsprechenden Fehlernachrichten, in denen ein Link zum Anzeigen der Ergebnisprotokolle im Onlineportal für Ihr {{site.data.keyword.cloud_notm}}-Plattform-Konto enthalten sein kann.

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Beispiel 18. Befehl 'build' für {{site.data.keyword.cloud_notm}}-Plattform" caption-side="bottom"}
`Beispiel 18. Befehl 'build' für IBM Cloud-Plattform`

Zusätzlich zum Erstellen der App zur Bereitstellung ermöglicht das Erstellen der App auch das Ausführen desselben Codes lokal mit dem Befehl `run` (nach dem Kopieren und Einfügen oder Eingeben des Befehls aus Beispiel 19). Wenn Sie fertig sind, kopieren Sie die bereitgestellte URL und fügen Sie sie in die Adressleiste des Browsers ein, in der Regel <http://localhost:3000>.

```bash
ibmcloud dev run
```
{: codeblock}
{: caption="Beispiel 19. CLI-Befehl der {{site.data.keyword.cloud_notm}}-Plattform zum Ausführen der App" caption-side="bottom"}

Da die App jetzt erstellt und definiert ist, überprüfen Sie die Anwendung, um sicherzustellen, dass sie funktioniert. Wenn das in Abbildung 2 dargestellte Platzhalterbild angezeigt wird, waren Sie erfolgreich. Sie haben eine neue Node.js-Webanwendung erstellt und sind bereit, sie in der Cloud bereitzustellen.

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="Abbildung 2. Neue Node.js-Anwendung erfolgreich erstellt" caption-side="top"}

Stellen Sie die App auf der {{site.data.keyword.cloud_notm}}-Plattform mit dem Befehl 'deploy' (wie in Beispiel 20 angegeben) bereit.

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Beispiel 20. CLI-Befehl der {{site.data.keyword.cloud_notm}}-Plattform zum Hochladen und Bereitstellen der App" caption-side="bottom"}
`Beispiel 20. CLI-Befehl der IBM Cloud-Plattform zum Hochladen und Bereitstellen der App`

Die URL wird erneut als Ergebnis der Ausführung des Befehls `ibmcloud dev deploy` abhängig von den vorherigen Angaben für den regionalen Endpunkt und Hostnamen angezeigt. Wenn Probleme auftreten, werden möglicherweise Links zu den Protokollen angezeigt, die im Portal auf der {{site.data.keyword.cloud_notm}}-Plattform gespeichert sind. Wenn keine Probleme auftreten, sollte die Anzeige im Browser mit der Anzeige in der lokalen Anwendung identisch sein, die Sie gerade besucht haben. Besuchen Sie jetzt Ihre neue Webanwendung in der Cloud.

## Web Gallery-App mit Beispielanwendung erstellen
{: #tutorial-create-app}

Auch hierfür müssen die Voraussetzungen berücksichtigt werden, die für die Entwicklung einer Node.js-App auf der {{site.data.keyword.cloud_notm}}-Plattform erfüllt sein mussten. Sie haben bereits Ihr Konto für die {{site.data.keyword.cloud_notm}}-Plattform erstellt und auch schon Developer Tools installiert (Installation von Docker). Anschließend haben Sie Node.js installiert. Die letzte Voraussetzung auf der Liste für dieses Lernprogramm war Git; dieses Tool wird jetzt ausführlich besprochen.  

Zunächst werden die speziellen Angaben für die Arbeit der Bildergalerie in Node.js besprochen. Für dieses Szenario wird jetzt zwar GitHub Desktop verwendet, Sie können zum Ausführen derselben Tasks aber auch den Git-Befehlszeilenclient verwenden. Als Einstieg wird eine Startervorlage für die neue Webanwendung geklont. 

Führen Sie die folgenden Schritte aus:

1.  Klonen Sie das in Beispiel 21 aufgeführte Repository. Laden Sie die Vorlage für die App in Ihre lokale Entwicklungsumgebung mit Git herunter. Klonen Sie nicht die Beispielapp auf der {{site.data.keyword.cloud_notm}}-Plattform, sondern verwenden Sie den Befehl in Beispiel 21, um die Startervorlage für die {{site.data.keyword.cos_full_notm}} Web Gallery-App zu klonen. Nach dem Klonen des Repositorys finden Sie die Starter-App im Verzeichnis 'COS-WebGalleryStart'. Öffnen Sie ein Git-Befehlsfenster und wechseln Sie in das Verzeichnis, in dem Sie das GitHub-Repository klonen möchten. Verwenden Sie den Befehl, der im ersten Beispiel dieses Lernprogramms aufgeführt wurde.

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="Beispiel 21. Details zum Git-Befehl 'clone'" caption-side="bottom"}
`Beispiel 21. Details zum Git-Befehl 'clone'`

2.  Führen Sie die App lokal aus. Öffnen Sie eine Terminalanwendung, von der eine Befehlszeilenschnittstelle bereitgestellt wird, und ändern Sie Ihr Arbeitsverzeichnis in das Verzeichnis 'COS-WebGalleryStart'. Beachten Sie hierbei die Node.js-Abhängigkeiten, die in der Datei 'package.json' aufgelistet werden. Laden Sie sie mit dem nächsten Befehl in Beispiel 22 an die Position herunter.

```bash
npm install
```
{: codeblock}
{: caption="Beispiel 22. Installation von Node Package Manager (npm)" caption-side="bottom"}
`Beispiel 22. Installation von Node Package Manager (npm)`

3.  Führen Sie die App mit dem in Beispiel 23 angegebenen Befehl aus.

```bash
npm start
```
{: codeblock}
{: caption="Beispiel 23. Details zum Starten der App mit npm" caption-side="bottom"}
`Beispiel 23. Details zum Starten der App mit npm`

Öffnen Sie einen Browser und zeigen Sie Ihre App an der Adresse und dem Port an, die in der Konsole ausgegeben werden (<http://localhost:3000>).

**Tipp**: Wenn Sie die App lokal erneut starten möchten, beenden Sie den Knotenprozess (Strg + C), um die App zu stoppen, und verwenden Sie `npm start`, um sie erneut zu starten. Während Sie neue Funktionen entwickeln, können Sie jedoch mit 'nodemon' zum erneuten Starten der App Zeit sparen, falls eine Änderung entdeckt wird. Installieren Sie 'nodemon' global mit dem folgenden Befehl: `npm install -g nodemon`. Starten Sie die Ausführung anschließend in der Befehlszeile im Appverzeichnis mit `nodemon`, damit die App von 'nodemon' gestartet wird.

4.  Bereiten Sie jetzt die Bereitstellung der App vor. Ändern Sie den Eigenschaftswert des Anwendungsnamens in der Datei `manifest.yml` von 'COS-WebGallery' in den Namen, den Sie für die App auf der {{site.data.keyword.cloud_notm}}-Plattform eingegeben haben; verwenden Sie bei Bedarf auch die in Beispiel 24 aufgeführten Informationen. Die Anwendung `manifest.yml` stimmt mit der im folgenden Beispiel überein. Darüber hinaus können Sie die Datei `package.json`, die sich im Stammverzeichnis der App befindet, für die App unter Verwendung des App-Namens und Ihres Namens als Autor anpassen.

```yaml
applications:
- path: .
  memory: 256M
  instances: 1
  domain: us-south.cf.appdomain.cloud
  name: webapplication
  host: webapplication
  disk_quota: 1024M
  random-route: true
```
{: codeblock}
{: caption="Beispiel 24. Inhalt von 'manifest.yml'" caption-side="bottom"}
`Beispiel 24. Inhalt von 'manifest.yml'`

**Tipp**: Jetzt ist der Punkt gekommen, an dem Sie möglicherweise SSH-Schlüssel einrichten müssen, um Code interaktiv mit einer Push-Operation zum fernen Ursprung zu übertragen. Wenn Sie eine Kennphrase für den SSH-Schlüssel festgelegt haben, müssen Sie diesen Code jedes Mal zum Übertragen der Änderungen mit einer Push-Operation an den fernen Ursprung für das Repository übertragen. 

5.  Entfernen Sie den Inhalt des Verzeichnisses `webapplication` und ersetzen Sie ihn durch den Inhalt des Verzeichnisses `COS-WebGalleryStart`, das Sie soeben geändert haben. Fügen Sie unter Verwendung Ihrer optimierten Git-Kenntnissen entweder in der Befehlszeilenschnittstelle oder in GitHub Desktop die Dateien hinzu, die gelöscht und zum Repository hinzugefügt wurden. Überträgen Sie die Änderungen anschließend mit einer Push-Operation an das ursprüngliche Repository. In Zukunft können Sie Änderungen an einer cloudbasierten Webanwendung einfach durch das Übertragen der Änderungen mit einer Push-Operation vornehmen. Der Serverprozess wird von der CD-Toolchain automatisch erneut gestartet, nachdem die Änderungen geklont und auf dem Server verdeckt gespeichert wurden. 


Da die Anwendung dadurch erneut codiert wurde, muss der Erstellungsprozess wiederholt werden. Aber dieses Mal wird der neue Code für die Bildergalerie verwendet. 

###App auf der {{site.data.keyword.cloud_notm}}-Plattform bereitstellen### 

Zum Abrufen der Starter-App mit den Änderungen 
    auf der {{site.data.keyword.cloud_notm}}-Plattform stellen Sie diese mithilfe von Developer Tools bereit; wiederholen Sie hierzu die Schritte, die Sie weiter oben ausgeführt haben.

a.  Falls Sie sich noch nicht angemeldet haben oder wenn Sie einen Neustart ausgeführt haben oder wenn Sie sich abgemeldet haben, melden Sie sich an der {{site.data.keyword.cloud_notm}}-Plattform mit dem Anmeldebefehl an. Er wurde in Beispiel 25 angegeben und kann bei Bedarf mit optionalen Parametern verwendet werden; für eine Organisation kann die Option '-o', für einen Bereich die Option '-s' und bei Verwendung eines föderierten Kontos die Option '--sso' angegeben werden. Bedenken Sie, dass Sie dieselbe Region auswählen müssen, mit der Sie bisher gearbeitet haben, wenn Sie dazu aufgefordert werden.

```bash
ibmcloud login
```
{: codeblock}
{: caption="Beispiel 25. CLI-Befehl zum Anmelden an der {{site.data.keyword.cloud_notm}}-Plattform" caption-side="bottom"}
`Beispiel 25. CLI-Befehl zum Anmelden an der IBM Cloud-Plattform`

b.  Legen Sie den API-Endpunkt für Ihre Region mithilfe des API-Befehls fest (wie in Beispiel 6 mit optionalen Platzhaltern dargestellt). Wenn Sie die URL Ihres regionalen API-Endpunkts nicht kennen, finden Sie Informationen hierzu auf der Einführungsseite.

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="Beispiel 26. API-Endpunkt der {{site.data.keyword.cloud_notm}}-Plattform" caption-side="bottom"}
`Beispiel 26. API-Endpunkt der IBM Cloud-Plattform`

c.  Wählen Sie den Aspekt 'Cloud Foundry' der {{site.data.keyword.cloud_notm}}-Plattform unter Verwendung des in Beispiel 27 beschriebenen Codes mit dem Befehl 'target' und der Option '--cf' als Ziel fest. 


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="Beispiel 27. Cloud Foundry in Befehlszeilenschnittstelle der {{site.data.keyword.cloud_notm}}-Plattform als Ziel auswählen" caption-side="bottom"}
`Beispiel 27. Cloud Foundry in Befehlszeilenschnittstelle der IBM Cloud-Plattform als Ziel auswählen`

d.  Erstellen Sie die App für die Bereitstellung dieser Anwendung mit dem Befehl 'build' (wie in Beispiel 28 dargestellt).

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Beispiel 28. Befehl 'build' für {{site.data.keyword.cloud_notm}}-Plattform" caption-side="bottom"}
`Beispiel 28. Befehl 'build' für IBM Cloud-Plattform`

g.  Die Anwendung wird zunächst lokal getestet. Zusätzlich zum Erstellen der App zur Bereitstellung ermöglicht das Erstellen der App auch das Ausführen desselben Codes lokal mit dem Befehl 'run' (nach dem Eingeben des Befehls aus Beispiel 29). 


```bash
ibmcloud dev run
```
{: codeblock}
{: caption="Beispiel 29. CLI-Befehl der {{site.data.keyword.cloud_notm}}-Plattform zum Ausführen der App" caption-side="bottom"}
`Beispiel 29. CLI-Befehl der IBM Cloud-Plattform zum Ausführen der App`

h.  Stellen Sie die App auf der {{site.data.keyword.cloud_notm}}-Plattform mit dem Befehl 'deploy' (wie in Beispiel 30 angegeben) bereit.

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Beispiel 30. CLI-Befehl der {{site.data.keyword.cloud_notm}}-Plattform zum Hochladen und Bereitstellen" caption-side="bottom"}
`Beispiel 30. CLI-Befehl der IBM Cloud-Plattform zum Hochladen und Bereitstellen`

Der Code in Beispiel 31 gibt die Reihenfolge der Befehle an, die in diesem Beispiel verwendet werden, um die ursprüngliche Webanwendung zu erstellen, zu testen und bereitzustellen.

```bash
ibmcloud login --sso
ibmcloud api cloud.ibm.com
ibmcloud target --cf
ibmcloud dev enable
ibmcloud dev build
ibmcloud dev run
ibmcloud dev deploy
```
{: codeblock}
{: caption="Beispiel 31. Liste der CLI-Befehle der {{site.data.keyword.cloud_notm}}-Plattform" caption-side="bottom"}
`Beispiel 31. Liste der CLI-Befehle der IBM Cloud-Plattform`

Bei erfolgreicher Ausführung wird von der {{site.data.keyword.cloud_notm}}-Plattform gemeldet, dass die App hochgeladen, erfolgreich bereitgestellt und gestartet wurde. Wenn Sie sich auch an der Webkonsole der {{site.data.keyword.cloud_notm}}-Plattform angemeldet haben, werden Sie auch in der Webkonsole über den Status der App benachrichtigt. Am wichtigsten ist jedoch, dass Sie die Bereitstellung der App durch das Besuchen der von der {{site.data.keyword.cloud_notm}}-Plattform gemeldeten App-URL in einem Browser oder durch Klicken auf die App-Schaltfläche in der Webkonsole überprüfen können.

5.  Testen Sie die App. Die sichtbare Änderung der Standardappvorlage, die bei der Erstellung der Starter-App bereitgestellt wurde, ist der Nachweis dafür, dass die Bereitstellung der App auf der {{site.data.keyword.cloud_notm}}-Plattform erfolgreich war.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Git-Branch erstellen
{: #tutorial-create-branch}

Jetzt müssen Sie einen Zweig (Branch) für die lokale Entwicklungsumgebung erstellen, die Sie für die Build-Stage der Delivery Pipeline für die {{site.data.keyword.cloud_notm}}-Plattform verwenden.

1.  Wenn Sie GitHub Desktop verwenden, klicken Sie auf das Symbol für den Zweig (Branch); Sie werden aufgefordert, einen Namen für den Zweig einzugeben (siehe Abbildung 14). In diesem Beispiel wird 'Local-dev' als Name verwendet.

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  Nach der Erstellung des Zweigs werden von GitHub die lokalen Dateien im Zweig 'Local-dev' mit den Dateien im Repository im Masterzweig verglichen und es werden keine lokalen Änderungen gemeldet. Sie können auf 'Veröffentlichen' klicken, um den im lokalen Repository erstellten Zweig zum GitHub-Repository hinzuzufügen (wie in Abbildung 5 dargestellt).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

Da der Zweig 'Local-dev' jetzt im GitHub-Repository in der Toolchain veröffentlicht ist, wird die Build-Stage der Delivery Pipeline für die {{site.data.keyword.cloud_notm}}-Plattform gestartet; danach folgt eine Bereitstellungsstage, sobald Sie ein Commit mit einer Push-Operation übertragen. Die Bereitstellung der App über die Befehlszeilenschnittstelle ist nicht mehr erforderlich, da die Bereitstellung direkt in den Workflow integriert wurde.

### Speicherberechtigungsnachweise für {{site.data.keyword.cos_full_notm}} konfigurieren
{: #tutorial-credentials}

Sie müssen die {{site.data.keyword.cos_short}}-Berechtigungsnachweise für die Webanwendung konfigurieren und ein Bucket einrichten, in dem die Bilder gespeichert und abgerufen werden. Für den von Ihnen erstellten API-Schlüssel sind {{site.data.keyword.cos_short}}-HMAC-Berechtigungsnachweise erforderlich, die unter Verwendung der [Serviceberechtigungsnachweise](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials) definiert wurden. Möglicherweise bemerken Sie die Begriffe `access_key_id` und `secret_access_key`, falls Sie über ein AWS-Konto verfügen, und verwenden eine Berechtigungsnachweisdatei, die bereits die Einträge `aws_access_key_id` und `aws_secret_access_key` enthält. 

Wenn Sie einen API-Schlüssel erstellt und heruntergeladen, und anschließend die HMAC-Berechtigungsnachweise kopiert haben, führen Sie die folgenden Schritte aus:

1.  Versetzen Sie die Berechtigungsnachweise in der lokalen Entwicklungsumgebung in den Windows-Pfad `%USERPROFILE%\\.aws\\credentials` (Benutzer von Mac OS oder Linux versetzen die Berechtigungsnachweise in `~/.aws/credentials)`. In Beispiel 32 wird der Inhalt einer typischen Berechtigungsnachweisdatei dargestellt.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="Beispiel 32. In der Datei '~/.aws/credentials' definierte Berechtigungsnachweise" caption-side="bottom"}
`Beispiel 32. In der Datei '~/.aws/credentials' definierte Berechtigungsnachweise`

2.  Definieren Sie auf der Webseite für die Anwendung, die Sie mit dem CLI-Befehl auf der {{site.data.keyword.cloud_notm}}-Plattform erstellt haben, die erforderlichen Berechtigungsnachweise als Umgebungsvariablen gemäß den bewährten Verfahren für die Entwicklung; melden Sie sich hierzu an der {{site.data.keyword.cloud_notm}}-Plattform an und wählen Sie unter den Cloud Foundry-Apps Ihre App 'webapplication' aus. Klicken Sie in den Registerkarten auf 'Laufzeit'.

3.  Klicken Sie im Fenster 'Laufzeit' oben auf der Seite auf 'Umgebungsvariablen' und blättern Sie zum Abschnitt 'Benutzerdefiniert', in dem Sie Variablen hinzufügen können.

4.  Fügen Sie zwei Variablen hinzu: Eine Variable mit dem Wert der Zugriffsschlüssel-ID und `AWS_ACCESS_KEY_ID` als Name des Schlüssels, und eine weitere Variable mit dem Wert des geheimen Zugriffsschlüssels und dem Namen `AWS_SECRET_ACCESS_KEY`. Diese Variablen und ihren jeweiligen Werten werden von der App zum Authentifizieren an der {{site.data.keyword.cos_short}}-Instanz verwendet, wenn sie auf der {{site.data.keyword.cloud_notm}}-Plattform ausgeführt wird (siehe Abbildung 6). Wenn Sie die Einträge fertiggestellt haben, klicken Sie auf 'Speichern'; die App wird automatisch erneut von der {{site.data.keyword.cloud_notm}}-Plattform gestartet.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

Fügen Sie als nächsten Schritt im {{site.data.keyword.cos_short}}-Portal für die Serviceinstanz ein Bucket hinzu, in dem die Bilder enthalten sind. Im folgenden Szenario wird das Bucket `web-images` verwendet.


## Node.js für Webanwendung für {{site.data.keyword.cos_full_notm}}-Bildergalerie anpassen
{: #tutorial-develop}

Da in diesem Beispiel eine MVC-Architektur verwendet wird, ist das Anpassen der Verzeichnisstruktur im Projekt zum Abbilden dieser Architektur eine Erleichterung und auch ein bewährtes Verfahren. Die Verzeichnisstruktur weist das Verzeichnis 'views' für die EJS-Ansichtsvorlagen, das Verzeichnis 'routes' für die Express-Routen und das Verzeichnis 'controllers' für die Controllerlogik auf. Platzieren Sie diese Elemente unter einem übergeordneten Quellenverzeichnis mit dem Namen 'src' (siehe Abbildung 7).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**Tipp**: Das zuvor geklonte Repository enthält ein Verzeichnis mit dem Namen 'COS-WebGalleryEnd'. Das Anzeigen des Quellcodes der ausgeführten Anwendung in Ihrem bevorzugten Editor kann während der Ausführung der nächsten Schritte hilfreich sein. Hierbei handelt es sich um die Version von 'webapplication', die festgeschrieben und auf der {{site.data.keyword.cloud_notm}} Plattform bereitgestellt wird, wenn Sie dieses Lernprogramm ausführen.

### App entwerfen
{: #tutorial-develop-design}

In einer einfachen Webanwendung für eine Bildergalerie muss ein Benutzer zwei Hauptaufgaben ausführen können:

  - Bilder von einem Web-Browser in das {{site.data.keyword.cos_short}}-Bucket hochladen
  - Bilder im {{site.data.keyword.cos_short}}-Bucket in einem Web-Browser anzeigen

In den nächsten Schritten liegt der Schwerpunkt darauf, diese beiden Demonstrationsfunktionen auszuführen und nicht darauf, eine vollständig entwickelte einsatzfähige App zu erstellen. Wenn dieses Lernprogramm bereitgestellt wird und zugänglich und aktiv bleibt, bedeutet dies, dass alle, die die App finden, dieselben Aktionen ausführen können: Dateien in das {{site.data.keyword.cos_full_notm}}-Bucket hochladen und bereits im Bucket befindliche JPEG-Bilder anzeigen.

### App entwickeln
{: #tutorial-develop-app}

In der Datei `package.json` wird innerhalb des Scripts-Objekts angezeigt, wie 'start' definiert ist (Beispiel 33). Mithilfe dieser Datei wird Node von der {{site.data.keyword.cloud_notm}}-Plattform bei jedem Start der App signalisiert, dass 'app.js' ausgeführt werden soll. Verwenden Sie sie auch, wenn Sie die App lokal testen. Machen Sie sich mit der Hauptanwendungsdatei mit dem Namen 'app.js' vertraut. Hierbei handelt es sich um den Code, der von Node.js zuerst ausgeführt werden soll, wenn Sie die App mit dem Befehl `npm start` (oder nodemon) starten. 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="Beispiel 33. Vorgehensweise zum Booten des angepassten Codes für App festlegen" caption-side="bottom"}
`Beispiel 33. Vorgehensweise zum Booten des angepassten Codes für App festlegen`

Die Datei 'app.js' beginnt mit dem in Beispiel 34 angegebenen Code. Zunächst wird Node vom Code zum Laden der Module verwendet, die zum Starten erforderlich sind. Vom Express-Framework wird die App als ein Singleton mit dem Namen `app` erstellt. Das Beispiel endet damit (der größte Teil des Codes wird zum derzeitigen Zeitpunkt weggelassen), dass die App angewiesen wird, an dem Port, der als Umgebungseigenschaft zugewiesen ist - oder standardmäßig 3000 - empfangsbereit zu sein. Bei einem erfolgreichen Start am Anfang wird in der Konsole eine Nachricht mit der Server-URL ausgegeben.

```javascript
var express = require('express');
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var app = express();
//...

// Server für angegebenen Port starten und Host binden
var port = process.env.PORT || 3000;
app.listen(port, function() {
    console.log("To view your app, open this link in your browser: http://localhost:" + port);
});
//...
```
{: codeblock}
{: javascript}
{: caption="Beispiel 34. Der Start der Webanwendung ist einfach, aber leistungsfähig" caption-side="bottom"}
`Beispiel 34. Der Start der Webanwendung ist einfach, aber leistungsfähig`

In Beispiel 35 wird erläutert, wie ein Pfad und Sichten definiert werden. In der ersten Zeile des Codes wird das Express-Framework angewiesen, das öffentliche Verzeichnis zum Ausführen der statischen Dateien zu verwenden, zu denen alle verwendeten statischen Bilder und Style-Sheets gehören. In den folgenden Zeilen wird die App darüber informiert, an welchen Positionen sich die Vorlagen für die Sichten im Verzeichnis 'src/views' befinden und EJS wird als Engine zum Anzeigen festgelegt. Außerdem wird vom Framework die Middleware für Hauptteilparser dazu verwendet, der App eingehende Anforderungsdaten als JSON verfügbar zu machen. In den Schlusszeilen des Beispiels wird von der Express-App auf alle an der App-URL eingehenden GET-Anforderungen mit der Darstellung der Ansichtsvorlage 'index.ejs' geantwortet.

```javascript
//...
// Dateien aus ./public als Hauptdateien verarbeiten
app.use(express.static('public'));
app.set('views', './src/views');
app.set('view engine', 'ejs');
app.use(bodyParser.json());

var title = 'COS Image Gallery Web Application';
// index.ejs verarbeiten
app.get('/', function (req, res) {
  res.render('index', {status: '', title: title});
});

//...
```
{: codeblock}
{: javascript}
{: caption="Beispiel 35. Ansichts- und Vorlagenspeicherpositionen der Web-App" caption-side="bottom"}
`Beispiel 35. Ansichts- und Vorlagenspeicherpositionen der Web-App`

In der folgenden Abbildung wird die Indexansichtsvorlage dargestellt, wenn sie wiedergegeben und an den Browser gesendet wird. Wenn Sie `nodemon` verwenden, haben Sie möglicherweise festgestellt, dass der Browser aktualisiert wurde, als Sie die Änderungen gespeichert haben; die App sollte somit wie in Abbildung 8 dargestellt werden.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

Da der HTML-Code in Beispiel 36 von den Tags &lt;head&gt;...&lt;/head&gt; gemeinsam genutzt wird, wurde er in die separate Vorlage 'include' versetzt (siehe Beispiel 16). Diese Schablone (head-inc.ejs) enthält ein Scriptlet &mdash; ein Binding für eine JavaScript-Variable &mdash; für den Seitentitel in Zeile 1. Die Variable `title` wird in `app.js` festgelegt und für die Ansichtsvorlage in der Zeile darunter übergeben. Andernfalls werden einfach einige CDN-Adressen zum Durchführen einer Pull-Operation für Bootstrap CSS, Bootstrap JavaScript und jQuery verwendet. Zum Schluss wird eine angepasste statische styles.css-Datei aus dem Verzeichnis 'pubic/style sheets' hinzugefügt.

```html
<title><%=title%></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Neueste kompilierte und minimiert CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
      crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
        crossorigin="anonymous">
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous">
</script>

<link rel="stylesheet" href="stylesheets/style.css">

```
{: codeblock}
{: caption="Beispiel 36. HTML-Elemente aus 'head-inc.ejs'" caption-side="bottom"}
`Beispiel 36. HTML-Elemente aus 'head-inc.ejs'`

Der Hauptteil der Indexansicht enthält die Navigationsregisterkarten im Bootstrap-Stil (siehe Beispiel 37) und das Uploadformular im einfachen Layout, das von den CSS-Stilen bereitgestellt wird, die in Bootstrap enthalten sind.

Berücksichtigen Sie die beiden folgenden Spezifikationen für die App:

-   Als Formularmethode wird POST und als Codierungstyp für die Formulardaten werden mehrteilige Formulardaten in Zeile 24 verwendet. Die Aktion für das Formular besteht darin, die Daten aus dem Formular zur App zur App-Route "/" zu senden. Später wird zum Router noch die Logik zum Verarbeiten von POST-Anforderungen für diese Route hinzugefügt.

-   Dem Benutzer soll ein Feedback zum Status eines versuchten Dateiuploads angezeigt werden. Dieses Feedback wird an die Ansicht in einer Variablen mit dem Namen 'status' übergeben und unterhalb des Hochladeformulars angezeigt.

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="/">Home</a></li>
    <li role="presentation"><a href="/gallery">Gallery</a></li>
</ul>
<div class="container">
    <h2>Upload Image to IBM Cloud Object Storage</h2>
    <div class="row">
        <div class="col-md-12">
            <div class="container" style="margin-top: 20px;">
                <div class="row">

                    <div class="col-lg-8 col-md-8 well">

                        <p class="wellText">Upload your JPG image file here</p>

                        <form method="post" enctype="multipart/form-data" action="/">
                            <p><input class="wellText" type="file" size="100px" name="img-file" /></p>
                            <br/>
                            <p><input class="btn btn-danger" type="submit" value="Upload" /></p>
                        </form>

                        <br/>
                        <span class="notice"><%=status%></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>
```
{: codeblock}
{: caption="Beispiel 37. HTML-Elemente von 'index.ejs'" caption-side="bottom"}
`Beispiel 37. HTML-Elemente von 'index.ejs'`

Jetzt ist ein Rückblick auf `app.js` in Beispiel 38 erforderlich. Im Beispiel werden Express-Routen zur Verarbeitung zusätzlicher Anforderungen konfiguriert, die an die App gerichtet werden. Der Code für diese Routing-Methoden befindet sich in zwei Dateien im Verzeichnis `./src/routes` in Ihrem Projekt:

-   imageUploadRoutes.js: Von dieser Datei wird verarbeitet, was geschieht, wenn der Benutzer ein Bild auswählt und auf 'Hochladen' klickt.

-   galleryRoutes.js: Von dieser Datei werden die Anforderungen verarbeitet, wenn der Benutzer auf die Registerkarte für die Galerie klickt, um die Ansicht für die Bildergalerie (imageGallery) anzufordern.

```javascript
//...
var imageUploadRoutes = require('./src/routes/imageUploadRoutes')(title);
var galleryRouter = require('./src/routes/galleryRoutes')(title);

app.use('/gallery', galleryRouter);
app.use('/', imageUploadRoutes);

//...
```
{: codeblock}
{: javascript}
{: caption="Beispiel 38. Beispiele für Node-Express-Router" caption-side="bottom"}
`Beispiel 38. Beispiele für Node-Express-Router`

#### Bildupload
{: #tutorial-develop-image-upload}

Machen Sie sich mit dem Code von 'imageUploadRoutes.js' in Beispiel 39 vertraut. Zunächst muss eine Instanz eines neuen Express-Routers erstellt werden und der Name `imageUploadRoutes` muss für sie festgelegt werden. Später wird eine Funktion erstellt, von der `imageUploadRoutes` zurückgegeben und einer Variablen mit der Bezeichnung `router` zugeordnet wird. Wenn dies abgeschlossen ist, muss die Funktion als Modul exportiert werden, um sie im Framework und für den Code in 'app.js' zugänglich zu machen. Zum Trennen der Routing-Logik von der Upload-Logik ist eine Controllerdatei erforderlich, die den Namen 'galleryController.js' aufweist. Da diese Logik für die Verarbeitung eingehender Anforderungen und das Bereitstellen der entsprechenden Antworten vorgesehen ist, wird die Logik in diese Funktion integriert und im Verzeichnis './src/controllers' gespeichert.

Die Instanz des Routers aus dem Express-Framework ist die Position, an die von 'imageUploadRoutes' Anforderungen für die App-Stammroute ("/") weitergeleitet werden sollen, wenn die HTTP-Methode POST verwendet wird. In der Methode `post` von 'imageUploadRoutes' wird Middleware aus den Modulen `multer` und `multer-s3` verwendet, was von 'galleryController' als `upload` zugänglich gemacht wird. Daten und Dateien werden von der Middleware vom POST-Uploadformular übernommen, verarbeitet und danach wird eine Callback-Funktion ausgeführt. Mit der Callback-Funktion wird überprüft, ob der HTTP-Statuscode 200 lautet und ob mindestens eine Datei im Anforderungsobjekt enthalten war. Aus diesen Bedingungen ergibt sich das Feedback für die Variable `status` und die Darstellung der Indexansichtsvorlage mit dem neuen Status.

```javascript
var express = require('express');
var imageUploadRoutes = express.Router();
var status = '';

var router = function(title) {

    var galleryController =
        require('../controllers/galleryController')(title);

    imageUploadRoutes.route('/')
    	.post(
    		galleryController.upload.array('img-file', 1), function (req, res, next) {
                if (res.statusCode === 200 && req.files.length > 0) {
                    status = 'uploaded file successfully';
                }
                else {
                    status = 'upload failed';
                }
                res.render('index', {status: status, title: title});
            });

    return imageUploadRoutes;
};

module.exports = router;
```
{: codeblock}
{: javascript}
{: caption="Beispiel 39. Details des Node-Express-Routers" caption-side="bottom"}
`Beispiel 39. Details des Node-Express-Routers`

Im Vergleich dazu besticht der Code für 'galleryRouter' in Beispiel 40 durch seine Einfachheit. Das Muster ist mit dem für 'imageUploadRouter' identisch und 'galleryController' muss in der ersten Zeile der Funktion enthalten sein, danach wird die Route festgelegt. Der Hauptunterschied besteht darin, dass HTTP-Abrufanforderungen des Typs GET anstatt POST weitergeleitet werden; somit wird die gesamte Ausgabe in der Antwort von 'getGalleryImages' gesendet, die von 'galleryController' in der letzten Zeile des Beispiels zugänglich gemacht wird.

```javascript
var express = require('express');
var galleryRouter = express.Router();

var router = function(title) {

    var galleryController =
        require('../controllers/galleryController')(title);

    galleryRouter.route('/')
        .get(galleryController.getGalleryImages);

    return galleryRouter;
};
module.exports = router;

```
{: codeblock}
{: javascript}
{: caption="Beispiel 40. Details des Node-Express-Routers" caption-side="bottom"}
`Beispiel 40. Details des Node-Express-Routers`

Im nächsten Schritt steht der Controller für die Galerie im Mittelpunkt.

Beachten Sie, wie Upload `multer` in Beispiel 41 konfiguriert wird (hierbei wird Code abgeschnitten, der hier ignoriert wird). Erforderlich sind die Module `ibm-cos-sdk`, `multer` und `multer-s3`. Aus dem Code geht hervor, wie ein S3-Objekt konfiguriert werden muss, von dem auf einen {{site.data.keyword.cos_short}}-Serverendpunkt verwiesen wird. Der Einfachheit halber werden Werte für Endpunktadresse, Region und Bucket statisch festgelegt, auf sie kann aber auch problemlos von einer Umgebungsvariablen oder JSON-Konfigurationsdatei verwiesen werden.

`upload` wird wie in Beispiel 41 verwendet definiert und in 'imageUploadRouter' durch Erstellen einer neuen `multer`-Instanz mit `storage` als eine Eigenschaft definiert. Mit dieser Eigenschaft wird `multer` angewiesen, an welche Position die Datei mit den mehrteiligen Formulardaten gesendet werden soll. Da von der {{site.data.keyword.cloud_notm}}-Plattform eine Implementierung der S3-API verwendet wird, wird als Speicher ein `s3-multer`-Objekt festgelegt. Dieses Objekt `s3-multer` enthält die Eigenschaft `s3`, die vorher dem Objekt `s3` zugeordnet wurde, und ein Bucketmerkmal, dem die Variable `myBucket` mit dem Wert 'web-images' zugeordnet ist. Das Objekt `s3-multer` verfügt nun über alle Daten, die zum Herstellen einer Verbindung und zum Hochladen von Dateien in das {{site.data.keyword.cos_short}}-Bucket erforderlich sind, wenn es Daten aus dem Uploadformular empfängt. Der Name oder Schlüssel des hochgeladenen Objekts ist der ursprüngliche Dateiname, der aus dem Dateiobjekt übernommen wird, wenn er im {{site.data.keyword.cos_short}}-Bucket 'web-images' gespeichert wird. 

**Tipp**: Verwenden Sie eine Zeitmarke als Teil des Dateinamens, um die Eindeutigkeit des Dateinamens zu erhalten. 

```javascript
var galleryController = function(title) {

    var aws = require('ibm-cos-sdk');
    var multer = require('multer');
    var multerS3 = require('multer-s3');

    var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
    var s3 = new aws.S3({endpoint: ep, region: 'us-south-1'});
    var myBucket = 'web-images';

    var upload = multer({
        storage: multerS3({
            s3: s3,
            bucket: myBucket,
            acl: 'public-read',
            metadata: function (req, file, cb) {
                cb(null, {fieldName: file.fieldname});
            },
            key: function (req, file, cb) {
                console.log(file);
                cb(null, file.originalname);
            }
        })
    });
    
    var getGalleryImages = function (req, res) { ... };

    return {
        getGalleryImages: getGalleryImages,
        upload: upload
    };
};

module.exports = galleryController;
```
{: codeblock}
{: javascript}
{: caption="Beispiel 41. Details des Node-Express-Controllers" caption-side="bottom"}
`Beispiel 41. Details des Node-Express-Controllers`

Eine hilfreiche Task für lokale Tests ist das Ausgeben des Dateiobjekts in der Konsole in `console.log(file)`. In Beispiel 42 wird ein lokaler Test des Uploadformulars ausgeführt und die Ausgabe des Konsolenprotokolls der Datei angezeigt.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Beispiel 42. Konsolenanzeige des Debugobjekts" caption-side="bottom"}
`Beispiel 42. Konsolenanzeige des Debugobjekts`

In Abbildung 9 wird das Feedback des Callbacks dargestellt, aus dem hervorgeht, das von der Anwendung während des Tests eine Datei erfolgreich hochgeladen wurde.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### Abrufen und Anzeigen eines Bilds
{: #tutorial-image-display}

In der Datei 'app.js' wurde das Express-Framework mit der Codezeile `app.use('/gallery', galleryRouter);` angewiesen, den Router zu verwenden, wenn die Route '/gallery' angefordert wird. Von diesem Router wird 'galleryController.js' verwendet (siehe Code in Beispiel 43), die Funktion 'getGalleryImages' wird definiert, die zugehörige Signatur wurde bereits besprochen. Da dasselbe Objekt `s3` verwendet wird, das für die Funktion zum Hochladen der Bilder konfiguriert wurde, wird jetzt für die Funktion der Name `listObjectsV2` festgelegt. Von dieser Funktion werden Indexdaten zurückgegeben, von denen jedes einzelne Objekt im Bucket definiert wird. Zum Anzeigen von Bildern in HTML ist eine Bild-URL für jedes einzelne JPEG-Bild im Bucket `web-images` erforderlich, das in der Ansichtsschablone angezeigt werden soll. Der Abschluss mit dem Datenobjekt, das von `listObjectsV2` zurückgegeben wird, enthält Metadaten zu jedem einzelnen Objekt im Bucket. 

Vom Code wird `bucketContents` in Schleifen durchlaufen und dabei nach einem Objektschlüssel gesucht, der mit '.jpg' endet; es wird ein Parameter erstellt, der an die S3-Funktion 'getSignedUrl' übergeben wird. Von dieser Funktion wird eine signierte URL für jedes Objekt zurückgegeben, wenn der Bucketname und -schlüssel des Objekts angegeben wird. In einer Callback-Funktion wird jede URL in einem Array gespeichert und an die HTTP-Serverantwortmethode `res.render` als der Wert für eine Eigenschaft mit dem Namen `imageUrls` übergeben.

```javascript
//...
    
    var getGalleryImages = function (req, res) {
        var params = {Bucket: myBucket};
        var imageUrlList = [];

        s3.listObjectsV2(params, function (err, data) {
            if (data) {
                var bucketContents = data.Contents;
                for (var i = 0; i < bucketContents.length; i++) {
                    if (bucketContents[i].Key.search(/.jpg/i) > -1) {
                        var urlParams = {Bucket: myBucket, Key: bucketContents[i].Key};
                        s3.getSignedUrl('getObject', urlParams, function (err, url) {
                            imageUrlList.push(url);
                        });
                    }
                }
            }
            res.render('galleryView', {
                title: title,
                imageUrls: imageUrlList
            });
        });
    };

//...
```
{: codeblock}
{: javascript}
{: caption="Beispiel 43. Teilinhalt von 'galleryController.js'" caption-side="bottom"}
`Beispiel 43. Teilinhalt von 'galleryController.js'`

In Beispiel 44, dem letzten Codebeispiel dieses Lernprogramms, wird der Hauptteil für die Schablone 'galleryView' mit dem Code angezeigt, der zum Anzeigen der Bilder erforderlich ist. Das mit der Methode 'res.render()' abgerufene Array 'imageUrls' wird mit einem Paar verschachtelter Tags des Typs &lt;div&gt;&lt;/div&gt; iteriert; hierbei wird von der Bild-URL eine GET-Anforderung für das Bild erstellt, wenn die Route '/gallery' angefordert wird.

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="/">Home</a></li>
        <li role="presentation" class="active"><a href="/gallery">Gallery</a></li>
    </ul>
    <div class="container">
        <h2>IBM COS Image Gallery</h2>

        <div class="row">
            <% for (var i=0; i < imageUrls.length; i++) { %>
                <div class="col-md-4">
                    <div class="thumbnail">
                            <img src="<%=imageUrls[i]%>" alt="Lights" style="width:100%">
                    </div>
                </div>
            <% } %>
        </div>
    </div>
</body>

</html>
```
{: codeblock}
{: caption="Beispiel 44. In Galerievorlage verwendete Scriptlets wiederholt abspielen und ausgeben" caption-side="bottom"}
`Beispiel 44. In Galerievorlage verwendete Scriptlets wiederholt abspielen und ausgeben`

Der Test wird lokal unter 'http://localhost:3000/gallery' durchgeführt und das Bild wird in Abbildung 10 dargestellt.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### In Git festschreiben
{: #tutorial-develop-commit}

Da die grundlegenden Funktionen der App jetzt funktionieren, wird der Code zunächst im lokalen Repository festgeschrieben und anschließend mit einer Push-Operation zu Git übertragen. Klicken Sie hierzu in GitHub Desktop auf 'Änderungen' (siehe Abbildung 11), geben Sie eine Zusammenfassung der Änderungen in das entsprechende Feld ein und klicken Sie auf die Schaltfläche zum Festschreiben in 'Local-dev'. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

Wenn Sie auf 'Synchronisieren' klicken, wird die Festschreibung an den fernen Zweig (Branch) 'Local-dev' gesendet, der in GitHub veröffentlicht wurde; durch diese Aktion wird die Build-Stage gestartet, auf die die Bereitstellungsstage in der Delivery Pipeline folgt (wie bereits in Abbildung 12 dieses Lernpgrogramms erläutert). 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## Nächste Schritte
{: #nextsteps}

Glückwunsch! Sie haben vom Anfang bis zum Ende dieses Lernprogramms mithilfe der {{site.data.keyword.cloud_notm}}-Plattform eine Webanwendung für eine Bildergalerie erstellt. Jedes in dieser grundlegenden Einführung besprochene Konzept können Sie unter [{{site.data.keyword.cloud_notm}}-Plattform](https://cloud.ibm.com/) näher kennenlernen. 

Viel Glück!
