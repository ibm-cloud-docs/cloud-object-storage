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

# Tutoriel : Application Web Image Gallery
{: #web-application}

L'ensemble du processus de création d'une application Web couvre un grand nombre de concepts différents et constitue un excellent moyen de vous initier aux fonctions d'{{site.data.keyword.cos_full}}. Ce tutoriel vous montre comment créer une galerie d'images simple sur la plateforme {{site.data.keyword.cloud}} et comment rassembler de nombreux concepts et pratiques. Votre application utilisera {{site.data.keyword.cos_full_notm}} comme serveur de back end pour une application Node.js qui permet à un utilisateur d'envoyer par téléchargement et de visualiser des fichiers image JPEG.

## Avant de commencer
{: #wa-prereqs}

Les éléments prérequis pour la création d'une application Web sont les suivants :

  - Compte de la plateforme {{site.data.keyword.cloud_notm}}
  - Docker, qui fait partie des outils de développement {{site.data.keyword.cloud_notm}}
  - Node.js 
  - Git (application de bureau et interface de ligne de commande)

### Installation de Docker
{: #tutorial-wa-install-docker}

Passer de la création d'applications Web à l'aide d'instances de serveur traditionnelles ou d'instances virtuelles à l'utilisation de conteneurs, tels que Docker, a pour conséquence d'accélérer le développement et de faciliter les tests tout en prenant en charge le déploiement automatisé.
Un conteneur est une structure légère qui n'a pas besoin de temps système supplémentaire, comme un système d'exploitation, mais juste de votre code et de votre configuration pour tout ce qui va des dépendances aux paramètres. 

Commençons par ouvrir un outil que les développeurs expérimentés connaissent bien et qui va devenir le nouveau meilleur ami des novices, la ligne de commande. Depuis que l'interface graphique a été inventée, l'interface de ligne de commande de votre ordinateur a été reléguée au second plan. Mais aujourd'hui, il est temps de la faire revivre (attention, l'interface graphique ne va pas disparaître de si tôt, nous en aurons notamment besoin pour parcourir le Web afin de recevoir par téléchargement notre nouvel ensemble d'outils de ligne de commande). 

Ouvrez le terminal, ou toute autre interface de ligne de commande appropriée pour votre système d'exploitation, et créez un répertoire à l'aide des commandes adaptées à l'interpréteur de commandes que vous utilisez.
Remplacez votre propre répertoire de référence par celui que vous venez de créer.
Une fois créée, votre application comportera son propre sous-répertoire dans celui-ci, qui contiendra le code de démarrage et la configuration nécessaires pour commencer.


De retour dans le navigateur après avoir quitté la ligne de commande, suivez les instructions relatives à l'installation des [outils de développement de la plateforme {{site.data.keyword.cloud_notm}}](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) qui sont fournies via le lien.
Les outils de développement offrent une approche extensible et reproductible pour créer et déployer des applictions en cloud. 

[Docker](https://www.docker.com) est installé avec les outils de développement. Nous en aurons besoin, même si son travail se fera principalement en arrière-plan, dans les routines qui structurent votre nouvelle application.
Docker doit être en cours d'exécution pour les commandes de génération puissent fonctionner.
Créez un compte Docker en ligne sur [Dockerhub](https://hub.docker.com), exécutez l'application Docker et connectez-vous. 

### Installation de Node.js
{: #tutorial-wa-install-node}

L'application que vous allez créer utilise [Node.js](https://nodejs.org/) comme moteur côté serveur afin d'exécuter le code JavaScript dont elle aura besoin. Pour utiliser le gestionnaire npm (Node Package Manager) inclus dans Node, afin de gérer les dépendances de votre application, vous devez installer Node.js localement. De plus, lorsque Node.js est installé localement, les tests sont simplifiés, ce qui accélère le développement.
 

Avant de commencer, vous pouvez envisager d'utiliser un gestionnaire de version, tel que Node Version Manager, ou `nvm`, pour installer Node, ce qui réduit la complexité liée à la gestion de plusieurs versions de Node.js. Au moment où nous écrivons ces lignes, pour installer ou mettre à jour `nvm` sur un ordinateur Mac ou Linux, vous pouvez vous servir du script d'installation en utilisant cURL dans l'interface CLI que vous venez d'ouvrir et en copiant et collant l'une des commandes des deux premiers exemples sur votre ligne de commande et en appuyant sur Entrée (attention, pour cela, vous devez impérativement utiliser un interpréteur de commandes BASH) : 

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Exemple 1. Utilisation de cURL pour installer Node Version Manager (nvm)" caption-side="bottom"}
`Exemple 1. Utilisation de cURL pour installer Node Version Manager (nvm)`
   
... ou Wget (un seul est nécessaire ; utilisez la valeur disponible sur votre système) :

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Exemple 2. Utilisation de Wget pour installer Node Version Manager (nvm)" caption-side="bottom"}
`Exemple 2. Utilisation de Wget pour installer Node Version Manager (nvm)`

Ou, pour Windows, vous pouvez utiliser [nvm for Windows](https://github.com/coreybutler/nvm-windows) avec des programmes d'installation et du code source fournis via le lien.


Si vous ne voulez pas de la complexité supplémentaire inhérente à la prise en charge de plusieurs éditions de Node.js, consultez le site Web [Node.js](https://nodejs.org/en/download/releases/) et installez la version TLS (Long Term Support) de Node.js qui correspond à la dernière version prise en charge par le pack de construction SDK for Node.js désormais utilisé sur la plateforme {{site.data.keyword.cloud_notm}}. Au moment où nous écrivons ces lignes, le dernier pack de construction est au niveau de la version 3.26 et il prend en charge l'édition Community v6.17.0+ de Node.js. 

Vous trouverez des informations supplémentaires sur le dernier pack de construction SDK for Node.js d'{{site.data.keyword.cloud_notm}} sur la page [SDK for Nodejs latest updates](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates).  

En utilisant `nvm`, vous pouvez installer la version de Node correspondant aux exigences de copie et de collage de la commande de l'exemple 3 dans votre ligne de commande.


```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="Exemple 3. Utilisation de `nvm` pour installer une version spécifique de Node.js" caption-side="bottom"}
`Exemple 3. Utilisation de nvm pour installer une version spécifique de Node.js`

Quelle que soit l'approche que vous utilisez, une fois que vous avez suivi les instructions d'installation de Node.js et de npm (inclus avec Node) sur votre ordinateur, appropriées en fonction du système d'exploitation et de la stratégie que vous utilisez, vous pouvez vous féliciter d'avoir fait un excellent travail. 

### Installation de Git
{: #tutorial-wa-install-git}

Vous avez probablement déjà une bonne connaissance de Git, car c'est le système de gestion des versions de code source le plus utilisé parmi les développeurs qui créent des applications pour le Web. Nous utiliserons Git plus tard lorsque nous créerons une chaîne d'outils de déploiement continu (CD) dans la plateforme {{site.data.keyword.cloud_notm}} pour la distribution et le déploiement continus. Si vous n'avez pas de compte GitHub, créez un compte personnel public gratuit sur le site Web [Github](https://github.com/join). Sinon, n'hésitez pas à vous connecter avec tout autre compte que vous pourriez avoir.

Il existe des instructions détaillées importantes expliquant comment générer et envoyer par téléchargement des clés SSH sur votre [profil Github](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) afin de permettre un accès sécurisé à Github à partir de la ligne de commande. Cela dit, si vous exécutez maintenant ces instructions, tout ce que vous obtiendrez ce sont des pratiques recommandées, dans la mesure où vous devrez répéter les étapes pour l'instance de Github utilisée pour la plateforme {{site.data.keyword.cloud_notm}} , à laquelle nous accéderons ultérieurement. Bien que les étapes d'utilisation des clés SSH puissent s'avérer compliquées, avec de la pratique, vous en viendrez à maîtriser parfaitement l'utilisation de SSH sur l'interface de ligne de commande.

Pour le moment, accédez à la page [Github Desktop](https://desktop.github.com/) pour recevoir par téléchargement GitHub Desktop, puis exécutez le programme d'installation. Une fois le programme d'installation exécuté, vous êtes invité à vous connecter à GitHub avec votre compte.

Dans la fenêtre de connexion (voir la première illustration de ce tutoriel), entrez le nom et le courrier électronique qui doivent s'afficher publiquement (nous partons du principe que vous disposez d'un compte public) pour toute validation sur votre référentiel. Une fois que vous avez lié l'application à votre compte, il se peut que vous soyez invité à vérifier la connexion de l'application via votre compte Github en ligne.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

Vous n'avez pas besoin de créer de référentiels pour l'instant. Si vous remarquez un référentiel nommé Tutorial inclus avec GitHub Desktop, n'hésitez pas à l'expérimenter pour vous aider à vous familiariser avec les opérations. Vous venez de terminer la partie relative aux prérequis pour ce tutoriel. Etes-vous prêt à créer une application ?

## Création de l'application de démarrage de Node.js à l'aide des outils de développement
{: #tutorial-create-skeleton}

Pour commencer à développer votre application localement, connectez-vous à la plateforme {{site.data.keyword.cloud_notm}} directement à partir de la ligne de commande, comme illustré dans l'exemple 4. 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="Exemple 4. Commande de connexion à la plateforme IBM Cloud à l'aide des outils de développement CLI" caption-side="bottom"}
`Exemple 4. Commande de connexion à la plateforme IBM Cloud à l'aide des outils de développement CLI`

Vous pouvez spécifier des paramètres facultatifs si vous le souhaitez, l'option -o pour votre organisation, l'option -s pour l'espace, ou l'option --sso si vous utilisez un compte fédéré. Lorsque vous vous connectez, vous pouvez être invité à choisir une région. Pour les besoins de cet exercice, sélectionnez `us-south` comme région, car celle-ci sera utilisée lors de la création d'une chaîne d'outils CD, plus tard dans ce tutoriel.  

Ensuite, définissez le noeud final (s'il n'est pas déjà défini) à l'aide de la commande illustrée dans l'exemple 5. D'autres noeuds finaux sont possibles et peuvent être préférables pour une utilisation en production, mais pour l'instant, utilisez le code comme indiqué, si cela s'avère approprié pour votre compte.

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="Exemple 5. Commande de définition du noeud final d'API pour votre compte. " caption-side="bottom"}
`Exemple 5. Commande de définition du noeud final d'API pour votre compte`

Ciblez l'aspect Cloud Foundry (cf) de la plateforme {{site.data.keyword.cloud_notm}} à l'aide du code illustré dans l'exemple 6, en utilisant la commande cible et l'option --cf. L'API `cf` est imbriquée dans les outils de développement CLI.

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="Exemple 6. Définition des options d'utilisation de l'API Foundry Cloud." caption-side="bottom"}
`Exemple 6. Définition des options d'utilisation de l'API Foundry Cloud`

Et maintenant, le moment tant attendu est venu. La création d'une application Web commence avec le code illustré dans l'exemple 7. L'espace `dev` est une option par défaut pour votre organisation, mais vous souhaiterez peut-être en créer d'autres pour isoler les différents efforts, en conservant les parties 'financement' et 'développement' séparées. 

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="Exemple 7. Commande de création d'une application à l'aide d'IBM Cloud Developer Tools" caption-side="bottom"}
`Exemple 7. Commande de création d'une application à l'aide d'IBM Cloud Developer Tools`

Avec cette commande, le système vous posera toute une série de questions. Vous pouvez revenir à de nombreux stades du processus, mais si vous vous sentez perdu, ou si vous pensez que vous avez manqué des étapes, n'hésitez pas à tout recommencer en supprimant le répertoire ou en en créant un autre à des fins de test et d'exploration. De plus, lorsque vous aurez terminé le processus de création de votre application localement sur la ligne de commande, vous pourrez voir les résultats en ligne, dans votre portail en ligne {{site.data.keyword.cloud_notm}}, là où vous avez créé votre compte pour gérer les ressources que vous avez créées.

Dans l'exemple 8, notez l'option permettant de créer l'application Web que vous souhaitez. Tapez '2' et appuyez sur Entrée. 

```
                                        
--------------------------------------------------------------------------------
Sélectionnez un type d'application :
--------------------------------------------------------------------------------
 1. Application vide
 2. Service de back-end/Appli Web
 3. Application mobile
--------------------------------------------------------------------------------
 0. Quitter
--------------------------------------------------------------------------------
? Entrer le numéro de sélection : > 2


```
{: caption="Exemple 8. Résultat de la commande `ibmcloud dev create` pour laquelle vous sélectionnez l'option 2, Appli Web." caption-side="bottom"}
``Exemple 8. Résultat de la commande `ibmcloud dev create` pour laquelle vous sélectionnez l'option 2, Appli Web.``

Il existe un certain nombre d'options dans l'exemple 9 basées sur ce que l'on appelle les "packs de construction". Notez également l'option permettant d'utiliser 'Node'. Tapez '4' et appuyez sur Entrée. 

```

--------------------------------------------------------------------------------
Sélectionnez un langage :
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
 0. Revenir à la sélection précédente
--------------------------------------------------------------------------------
? Entrer le numéro de sélection : > 4


```
{: caption="Exemple 9. Options de langage lors de l'utilisation de la commande `ibmcloud dev create` (suite). " caption-side="bottom"}
`Exemple 9. Options de langage lors de l'utilisation de la commande 'ibmcloud dev create' (suite)`

Une fois que vous avez choisi le langage et/ou la structure de programmation, le nombre d'options possibles pour la sélection suivante présentée dans l'exemple 10 est tellement important que lors du défilement de ces options à l'écran, il se peut que le service que vous recherchez soit dépassé. Comme vous pouvez le voir dans l'exemple, nous souhaitons utiliser une simple application Web Node.js avec Express.js. Tapez '6' et appuyez sur Entrée. 

```
? Sélectionnez un kit de démarrage :

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
 0. Revenir à la sélection précédente
--------------------------------------------------------------------------------
? Entrer le numéro de sélection : > 6

```
{: caption="Exemple 10. Options d'application squelette lors de l'utilisation de la commande `ibmcloud dev create`." caption-side="bottom"}
`Exemple 10. Options d'application squelette lors de l'utilisation de la commande 'ibmcloud dev create'`

Maintenant que vous avez choisi les options les plus simples, l'option la plus difficile à définir pour tous les développeurs demeure : nommer votre application. Suivez le scénario illustré dans l'exemple 11, tapez 'webapplication' et appuyez sur Entrée. 

```bash
? Entrez un nom pour votre application > webapplication
```
{: caption="Exemple 11. Attribution d'un nom à votre application 'webapplication' à l'aide de la commande `ibmcloud dev create`." caption-side="bottom"}
`Exemple 11. Attribution d'un nom à votre application 'webapplication' à l'aide de la commande 'ibmcloud dev create'`

Vous pourrez par la suite ajouter autant de services, comme des magasins de données ou des fonctions de calcul, que nécessaire ou souhaité via la console Web. Cependant, pour l'instant et comme illustré dans l'exemple 12, tapez 'n' pour non lorsque le système vous demande si vous voulez ajouter des services. 

```
Utilisation de la valeur par défaut de groupe de ressources de votre compte

Voulez-vous sélectionner un service à ajouter à cette application ? [O/n]> n

```
{: caption="Exemple 12. Option d'ajout de services lors de l'utilisation de la commande `ibmcloud dev create` (suite). " caption-side="bottom"}
`Exemple 12. Option d'ajout de services lors de l'utilisation de la commande 'ibmcloud dev create' (suite)`

Auparavant, les avantages liés au développement à l'aide de conteneurs, au lieu de serveurs traditionnels ou même de serveurs virtuels, étaient mentionnés par rapport à Docker. Une façon de gérer les conteneurs consiste à utiliser un logiciel d'orchestration, comme Kubernetes, et cela est devenu une norme _de facto_ pour le développement. Cependant, pour ce tutoriel, nous pouvons laisser le service Cloud Foundry gérer un conteneur Docker unique qui contiendra le code, les bibliothèques et la configuration dont votre application a besoin.


Comme illustré dans l'exemple 13, tapez '1' et appuyez sur Entrée pour utiliser 'IBM DevOps' afin d'intégrer CD au cycle de vie de votre projet.

 
```

--------------------------------------------------------------------------------
Effectuez une sélection parmi les options suivantes de chaînes d'outils DevOps et d'environnements d'exécution cibles :

 1. IBM DevOps, déployer dans des packs de construction Cloud Foundry
 2. IBM DevOps, déployer dans des conteneurs Kubernetes
 3. Non DevOps, avec déploiement manuel
--------------------------------------------------------------------------------
? Entrer le numéro de sélection : > 1

```
{: caption="Exemple 13. Options de déploiement lors de l'utilisation de la commande `ibmcloud dev create`." caption-side="bottom"}
`Exemple 13. Options de déploiement lors de l'utilisation de la commande 'ibmcloud dev create'`

Comme mentionné précédemment, nous allons choisir une région pour notre chaîne d'outils CD de déploiement automatisé. Par conséquent, choisissez la même option que précédemment, '5', comme illustré dans l'exemple 14.


```

--------------------------------------------------------------------------------
Sélectionnez une région parmi les options suivantes pour votre chaîne d'outils :
--------------------------------------------------------------------------------
 1. Francfort (eu-de)
 2. Londres (eu-gb)
 3. jp-tok
 4. Washington DC (us-east)
 5. Dallas (us-south)
--------------------------------------------------------------------------------
 0. Revenir à la sélection précédente
--------------------------------------------------------------------------------
? Entrer le numéro de sélection  : > 5

```
{: caption="Exemple 14. Régions disponibles en tant qu'options dans la commande ` ibmcloud dev create`." caption-side="bottom"}
`Exemple 14. Régions disponibles en tant qu'options dans la commande 'ibmcloud dev create'`

A ce stade, la génération d'une nouvelle application nous rappellera que la chaîne d'outils utilisée pour déployer votre application ultérieurement aura besoin d'une configuration supplémentaire, comme le montre l'exemple 15. Comme mentionné précédemment, l'envoi par téléchargement de votre clé publique vers Github (au niveau de l'instance Chaîne d'outils CD sur la plateforme {{site.data.keyword.cloud_notm}}) sera nécessaire pour distribuer l'application déployée à l'aide de Github. D'autres instructions sont disponibles à l'adresse [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair) une fois que vous avez déployé votre application et que vous vous êtes connecté à votre compte IBM Cloud GitLab. 

```

Remarque : pour que la connexion à la chaîne d'outils DevOps réussisse, cette machine
doit être configurée pour un accès SSH à votre compte IBM Cloud GitLab à l'adresse
https://git.ng.bluemix.net/profile/keys afin de recevoir par téléchargement le code d'application.


```
{: caption="Exemple 15. Sortie de la commande `ibmcloud dev create` pour les clés SSH. " caption-side="bottom"}
`Exemple 15. Sortie de la commande 'ibmcloud dev create' pour les clés SSH`

D'autres invites s'afficheront pour vous permettre de confirmer le nom de chaîne d'outils et d'application que vous avez défini précédemment. L'exemple 16 montre comment modifier les noms d'hôte et de chaîne d'outils, si vous le souhaitez. Le nom d'hôte doit être unique pour le domaine utilisé comme noeud final de service de votre application, mais en l'absence de conflit de noms d'hôte, vous pouvez simplement appuyer sur le bouton Retour à l'invite de confirmation. 

```
La chaîne d'outils DevOps de cette application sera : webapplication
Appuyez sur [Retour] pour accepter, ou entrez une autre valeur maintenant >



Le nom d'hôte de cette application sera : webapplication
Appuyez sur [Retour] pour accepter, ou entrez une autre valeur maintenant >

L'application webapplication a été créée dans IBM Cloud.

Chaîne d'outils DevOps créée à l'adresse
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="Exemple 16. Confirmation des noms des propriétés dans la commande `ibmcloud dev create`." caption-side="bottom"}
`Exemple 16. Confirmation des noms des propriétés dans la commande 'ibmcloud dev create'`

Si vous copiez et collez le lien fourni à la fin de la sortie que vous avez reçue suite à l'utilisation de la commande `ibmcloud dev create`, vous pourrez accéder à votre chaîne d'outils CD. Mais, vous pouvez également accéder à votre chaîne d'outils CD ultérieurement à partir de la console, si vous n'avez pas capturé le lien.
D'autres informations sont affichées à mesure que le processus se poursuit, comme illustré dans l'exemple 17, où des entrées d'application ont été créées en ligne et un répertoire contenant l'exemple de code a été créé.
 

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

L'application webapplication a été correctement sauvegardée dans le répertoire en cours.

```
{: caption="Exemple 17. Confirmation des actions générées par la commande `ibmcloud dev create`." caption-side="bottom"}
`Exemple 17. Confirmation des actions générées par la commande 'ibmcloud dev create'`

Cette dernière instruction de l'exemple 17 signifie que si vous affichez votre répertoire en cours, un nouveau sous-répertoire `webapplication` doit désormais être visible. Le répertoire `webapplication` contiendra une structuration de votre nouvelle application Node.js. Cependant, même si la recette peut être présente, les ingrédients eux-mêmes, toujours encapsulés dans une image Docker, doivent être "bouillis", pardon, construits à l'aide de la commande illustrée dans l'exemple 18. Docker devrait être en cours d'exécution sur votre machine locale suite à l'installation, mais si vous devez le redémarrer, faites-le.
Toute tentative de construction de votre nouvelle application Web alors que Docker n'est pas en cours d'exécution échoue, mais les raisons de ce type d'échec sont multiples. Si des problèmes surviennent, recherchez dans les messages d'erreur générés l'éventuel lien approprié permettant de visualiser les journaux dans votre portail en ligne pour votre compte de la plateforme {{site.data.keyword.cloud_notm}}. 

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Exemple 18. Commande build de la plateforme {{site.data.keyword.cloud_notm}}" caption-side="bottom"}
`Exemple 18. Commande build de la plateforme IBM Cloud`

Générer une application a non seulement pour objectif de distribuer cette application, mais également d'exécuter le même code localement avec la commande `run` (après que vous avez copié et collé ou tapé la commande illustrée dans l'exemple 19). Une fois la commande terminée, copiez et collez l'URL fournie dans la barre d'adresse de votre navigateur, par exemple, <http://localhost:3000>.

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Exemple 19. Commande d'interface CLI de la plateforme {{site.data.keyword.cloud_notm}} pour exécuter votre application" caption-side="bottom"}

Maintenant que l'application a été créée et définie, affichez-la pour vérifier qu'elle fonctionne. Si vous voyez l'image de marque de réservation comme illustrée dans la figure 2, vous avez réussi ! Vous avez créé une nouvelle application Web Node.js et vous êtes prêt à la déployer sur le cloud.

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="Figure 2. Vous avez créé une nouvelle application Node.js. Félicitations !"}

Déployez l'application sur la plateforme {{site.data.keyword.cloud_notm}} à l'aide de la commande deploy (comme illustré dans l'exemple 20). 

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Exemple 20. Commande d'interface CLI de la plateforme {{site.data.keyword.cloud_notm}} pour envoyer par téléchargement et déployer votre application" caption-side="bottom"}
`Exemple 20. Commande d'interface CLI de la plateforme IBM Cloud pour envoyer par téléchargement et déployer votre application`

L'URL sera à nouveau affichée suite à l'exécution de la commande `ibmcloud dev deploy` en fonction du noeud final régional et du nom d'hôte que vous avez indiqués précédemment. Si des problèmes surviennent, il se peut que des liens vers les journaux stockés dans votre portail sur la plateforme {{site.data.keyword.cloud_notm}} s'affichent. En l'absence de problème, un écran identique à l'application locale que vous venez de visiter doit s'afficher dans votre navigateur.
Rendez-vous sur votre nouvelle application Web dans le cloud !

## Création de l'application Web Gallery à l'aide d'un exemple d'application
{: #tutorial-create-app}

Rappelons les éléments prérequis qui ont été nécessaires pour développer une application Node.js sur la plateforme {{site.data.keyword.cloud_notm}}. Vous avez déjà créé votre compte pour la plateforme {{site.data.keyword.cloud_notm}} et installé les outils de développement, et Docker a été installé lors de cette installation.
Ensuite, vous avez installé Node.js. Le dernier élément répertorié comme prérequis pour ce tutoriel était Git, que nous allons explorer maintenant.   

Nous allons commencer par énumérer les caractéristiques de l'utilisation de la galerie d'images dans Node.js. Nous allons utiliser Github Desktop pour ce scénario, mais vous pouvez également utiliser le client de ligne de commande Git pour effectuer les mêmes tâches. Pour commencer, nous allons cloner un modèle de module de démarrage pour votre nouvelle application Web.
 

Procédez comme suit :

1.  Clonez le référentiel indiqué dans l'exemple 21. Téléchargez le modèle pour votre application sur votre environnement de développement local à l'aide de Git. Au lieu de cloner l'exemple d'application à partir de la plateforme {{site.data.keyword.cloud_notm}}, utilisez la commande illustrée dans l'exemple 21 afin de cloner le modèle de module de démarrage pour l'application Web Gallery d'{{site.data.keyword.cos_full_notm}}. Une fois que vous avez cloné le référentiel, l'application de démarrage se trouve dans le répertoire COS-WebGalleryStart. Ouvrez une fenêtre de commande Git et accédez à un répertoire dans lequel vous souhaitez cloner le référentiel Github. Utilisez la commande illustrée dans le premier exemple de ce tutoriel. 

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="Exemple 21. Détails de la commande clone Git" caption-side="bottom"}
`Exemple 21. Détails de la commande clone Git`

2.  Exécutez l'application en local. Ouvrez une application terminal qui fournit une interface de ligne de commande et remplacez votre répertoire de travail par le répertoire COS-WebGalleryStart. Notez les dépendances Node.js répertoriées dans le fichier package.json. Téléchargez-les à l'aide de la commande illustrée dans l'exemple 22.


```bash
npm install
```
{: codeblock}
{: caption="Exemple 22. Installation de npm (Node Package Manager)" caption-side="bottom"}
`Exemple 22. Installation de npm (Node Package Manager)`

3.  Exécutez l'application à l'aide de la commande illustrée dans l'exemple 23.

```bash
npm start
```
{: codeblock}
{: caption="Exemple 23. Détails relatifs au démarrage de votre application à l'aide de npm" caption-side="bottom"}
`Exemple 23. Détails relatifs au démarrage de votre application à l'aide de npm`

Ouvrez un navigateur et affichez votre application sur l'adresse et le port affichés sur la console, <http://localhost:3000>.

**Astuce** : pour redémarrer l'application localement, arrêtez le processus de noeud (Ctrl+C) et utilisez à nouveau la commande `npm start`. Cependant, lorsque vous développez de nouvelles fonctions, l'utilisation de nodemon pour redémarrer votre application lorsqu'elle détecte un changement vous permet de gagner du temps. Installez nodemon de façon globale en exécutant la commande suivante : `npm install -g nodemon`. Exécutez-la ensuite à partir de la ligne de commande dans votre répertoire d'application en utilisant `nodemon` pour que celui-ci démarre votre application. 

4.  Tenez-vous prêt à préparer l'application en vue de son déploiement ! Mettez à jour la valeur de la propriété de nom d'application
    dans le fichier `manifest.yml` à partir de COS-WebGallery et remplacez-la par le nom que vous avez saisi pour votre
    application sur la plateforme {{site.data.keyword.cloud_notm}} et les autres informations illustrées dans l'exemple 24, si besoin.
    L'application `manifest.yml` se présente comme dans l'exemple illustré ci-après. De plus, vous pouvez personnaliser le fichier `package.json`
    situé dans le répertoire principal de votre application avec le nom de votre application et votre nom en tant qu'auteur.


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
{: caption="Exemple 24. Contenu du fichier `manifest.yml`" caption-side="bottom"}
`Exemple 24. Contenu du fichier manifest.yml`

**Astuce** : nous arrivons au stade où vous aurez peut-être besoin de configurer des clés SSH pour envoyer de façon interactive du code à votre origine éloignée. Si vous définissez une 
    phrase de passe pour votre clé SSH, vous devez entrer ce code à chaque fois que vous envoyez vos modifications vers l'origine éloignée pour votre référentiel.
 

5.  Retirez et remplacez le contenu de votre répertoire `webapplication` par le contenu du répertoire que vous venez de modifier, `COS-WebGalleryStart`.
    En faisant appel à vos compétences Git, ajoutez les fichiers qui ont été supprimés et ajoutés au référentiel à l'aide de l'interface CLI ou de Github Desktop. Ensuite, envoyez les  modifications vers l'origine du référentiel. A l'avenir, vous pourrez apporter des modifications à votre application Web basée sur le cloud simplement en envoyant ces modifications à Git. La chaîne d'outils CD redémarrera de façon automatique/magique le processus serveur après le clonage de vos modifications et leur stockage sur le serveur.
 


En résumé, nous avons recodé notre application, par conséquent, répétons le processus de génération. Mais cette fois-ci, nous allons utiliser le nouveau code Image Gallery.  

###Déploiement de l'application sur la plateforme {{site.data.keyword.cloud_notm}}### 

Pour obtenir l'application de démarrage avec vos modifications
    sur la plateforme {{site.data.keyword.cloud_notm}}, déployez-la à l'aide des outils de développement en répétant les mêmes étapes que celles effectuées précédemment.


a.  Si vous ne l'avez pas encore fait ou si vous avez redémarré ou si vous vous êtes déconnecté, connectez-vous à la plateforme {{site.data.keyword.cloud_notm}} à l'aide de la commande login.
Pour rappel, cette commande est illustrée dans l'exemple 25. Notez que vous pouvez spécifier des paramètres facultatifs si vous le souhaitez, l'option -o pour votre organisation, l'option -s pour l'espace, ou l'option --sso si vous utilisez un compte fédéré. N'oubliez pas de choisir la même région que celle utilisée jusqu'ici, si le système vous invite à en spécifier une. 

```bash
ibmcloud login
```
{: codeblock}
{: caption="Exemple 25. Commande CLI de connexion à la plateforme {{site.data.keyword.cloud_notm}}" caption-side="bottom"}
`Exemple 25. Commande CLI de connexion à la plateforme IBM Cloud`

b.  Définissez le noeud final d'API pour votre région en utilisant la commande d'API (comme illustré avec les marques de réservation facultatives dans l'exemple 6).
Si vous ne connaissez pas votre URL de noeud final d'API régionale, consultez la page d'initiation. 

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="Exemple 26. Noeud final d'API de la plateforme {{site.data.keyword.cloud_notm}}" caption-side="bottom"}
`Exemple 26. Noeud final d'API de la plateforme IBM Cloud`

c.  Ciblez l'aspect Cloud Foundry de la plateforme {{site.data.keyword.cloud_notm}} à l'aide du code illustré dans l'exemple 27, en utilisant la commande cible et l'option --cf. 


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="Exemple 27. Interface CLI de la plateforme {{site.data.keyword.cloud_notm}} utilisée pour cibler Cloud Foundry" caption-side="bottom"}
`Exemple 27. Interface CLI de la plateforme IBM Cloud utilisée pour cibler Cloud Foundry`

d.  Générez l'application pour distribution à l'aide de la commande build (comme illustré dans l'exemple 28).

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Exemple 28. Commande build de la plateforme {{site.data.keyword.cloud_notm}}" caption-side="bottom"}
`Exemple 28. Commande build de la plateforme IBM Cloud`

g.  Testez l'application en local. Générer une application a non seulement pour objectif de distribuer cette application, mais également d'exécuter le même code localement avec la commande run (après que vous avez tapé la commande illustrée dans l'exemple 29). 


```bash
ibmcloud dev run
```
{: codeblock}
{: caption="Exemple 29. Commande d'interface CLI de la plateforme {{site.data.keyword.cloud_notm}} pour exécuter votre application" caption-side="bottom"}
`Exemple 29. Commande d'interface CLI de la plateforme IBM Cloud pour exécuter votre application`

h.  Déployez l'application sur la plateforme {{site.data.keyword.cloud_notm}} à l'aide de la commande deploy (comme illustré dans l'exemple 30). 

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Exemple 30. Commande d'interface CLI de la plateforme {{site.data.keyword.cloud_notm}} pour envoyer par téléchargement et déployer votre application" caption-side="bottom"}
`Exemple 30. Commande d'interface CLI de la plateforme IBM Cloud pour envoyer par téléchargement et déployer votre application`

Le code présenté dans l'exemple 31 illustre la séquence de commandes utilisées pour générer, tester et déployer l'application Web initiale. 

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
{: caption="Exemple 31. Liste de commandes CLI de la plateforme {{site.data.keyword.cloud_notm}}" caption-side="bottom"}
`Exemple 31. Liste de commandes CLI de la plateforme IBM Cloud`

Si ces opérations aboutissent, la plateforme {{site.data.keyword.cloud_notm}} indique que l'application a été envoyée par téléchargement, déployée et démarrée.
Si vous êtes également connecté à la console Web de la plateforme {{site.data.keyword.cloud_notm}}, vous êtes informé de l'état de votre application. Mais, surtout, vous pouvez vérifier que l'application a été déployée en visitant l'URL de l'application renvoyée par la plateforme {{site.data.keyword.cloud_notm}} en utilisant un navigateur ou à partir de la console Web en cliquant sur le bouton Afficher l'application.

5.  Testez l'application. La modification visible par rapport au modèle d'application par défaut déployé lors de la création sur l'application de démarrage affichée ci-dessous montre que le déploiement de l'application sur la plateforme {{site.data.keyword.cloud_notm}} a abouti.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Création d'une branche Git
{: #tutorial-create-branch}

A présent, vous devez créez une branche pour l'environnement de développement local à utiliser pour votre étape de génération de pipeline de distribution de la plateforme {{site.data.keyword.cloud_notm}} :

1.  Si vous utilisez Github Desktop, cliquez sur l'icône de branche. Vous êtes invité à entrer un nom pour la branche (voir la figure 14). Cet exemple utilise Local-dev comme nom. 

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  Une fois que vous avez créé la branche, GitHub compare les fichiers locaux de la branche Local-dev avec les fichiers du référentiel sur la branche principale et ne signale aucune modification locale. Vous pouvez maintenant cliquer sur Publier pour ajouter la branche que vous avez créée sur votre référentiel local à votre référentiel GitHub (comme illustré dans la figure 5).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

Maintenant que la branche Local-dev est publiée sur le référentiel GitHub dans votre chaîne d'outils, l'étape de génération de votre pipeline de distribution de la plateforme {{site.data.keyword.cloud_notm}} sera déclenchée, suivie de l'étape de déploiement à chaque fois que vous enverrez une validation dans le référentiel.
Le déploiement de l'application à partir de l'interface de ligne de commande ne sera plus nécessaire car le déploiement a été intégré directement dans votre flux de travaux.

### Configuration de vos données d'identification {{site.data.keyword.cos_full_notm}}
{: #tutorial-credentials}

Vous devez configurer les données d'identification {{site.data.keyword.cos_short}} pour votre application Web, ainsi qu'un 'compartiment' qu'elle utilisera pour stocker et extraire des images.
La clé d'API que vous allez créer aura besoin des données d'identification HMAC {{site.data.keyword.cos_short}}, telles qu'elles sont définies par vos
[données d'identification de service](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials).
Vous reconnaîtrez peut-être les valeurs `access_key_id` et `secret_access_key` car vous possédez peut-être un compte AWS et vous utilisez peut-être un fichier de données d'identification contenant déjà les entrées `aws_access_key_id` et `aws_secret_access_key`.  

Une fois que vous avez créé une clé d'API et reçu par téléchargement puis copié les données d'identification HMAC, procédez comme suit : 

1.  Dans l'environnement de développement local, placez les données d'identification dans le chemin Windows `%USERPROFILE%\\.aws\\credentials` (pour les utilisateurs Mac/Linux, les données d'identification doivent figurer dans `~/.aws/credentials`). L'exemple 32 présente le contenu d'un fichier de données d'identification standard.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="Exemple 32. Données d'identification telles qu'elles sont définies dans votre fichier `~/.aws/credentials`" caption-side="bottom"}
`Exemple 32. Données d'identification telles qu'elles sont définies dans votre fichier ~/.aws/credentials`

2.  Sur la page Web de l'application que vous avez créée à l'aide de la commande CLI sur la plateforme {{site.data.keyword.cloud_notm}}, définissez vos données d'identification requises en tant que variables d'environnement conformément aux meilleures pratiques de développement en vous connectant à la plateforme {{site.data.keyword.cloud_notm}}, et sous Applications Cloud Foundry, en sélectionnant votre application, 'webapplication'. A partir des onglets, cliquez sur Exécution. 

3.  Dans la fenêtre Exécution, cliquez sur Variables d'environnement en haut de la page et faites défiler l'écran jusqu'à la section Défini par l'utilisateur, ce qui vous permet d'ajouter les variables.

4.  Ajoutez deux variables, une dotée de la valeur de votre élément access_key_id, en utilisant `AWS_ACCESS_KEY_ID` comme nom de la clé, et une autre dotée de la valeur de votre clé d'accès secrète, nommée `AWS_SECRET_ACCESS_KEY`. Ces variables et leurs valeurs respectives sont utilisées par l'application pour l'authentification auprès de l'instance {{site.data.keyword.cos_short}} lors de l'exécution sur la plateforme {{site.data.keyword.cloud_notm}} (voir la figure 6). Lorsque vous avez terminé de définir vos entrées, cliquez sur Sauvegarder. La plateforme {{site.data.keyword.cloud_notm}} redémarrera automatiquement l'application pour vous.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

Ensuite, sur le portail {{site.data.keyword.cos_short}} de votre instance de service, ajoutez un compartiment destiné à contenir vos images. Ce scénario utilise un compartiment nommé `web-images`.


## Personnalisation de l'application Web {{site.data.keyword.cos_full_notm}} Image Gallery pour Node.js
{: #tutorial-develop}

Etant donné que cet exemple utilise une architecture MVC, l'ajustement de la structure de répertoires dans votre projet pour refléter cette architecture est non seulement commode, mais également recommandé. Cette structure de répertoires comporte un répertoire views destiné à contenir les modèles de vue EJS, un répertoire routes destiné à contenir les routes express et un répertoire controllers dans lequel sera placée la logique de contrôleur.
Placez ces éléments sous un répertoire source parent nommé src (voir la figure 7).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**Astuce** : le référentiel que vous avez cloné précédemment contient un répertoire nommé COS-WebGalleryEnd. Afficher le code source de l'application terminée dans votre éditeur préféré peut s'avérer utile lorsque des étapes suivantes. Il s'agit de la version de votre application 'webapplication' qui sera validée et déployée sur la plateforme {{site.data.keyword.cloud_notm}} à la fin de ce tutoriel.


### Conception de l'application
{: #tutorial-develop-design}

Les deux principales tâches qu'un utilisateur doit pouvoir effectuer avec l'application Web Image Gallery simple sont les suivantes :

  - Envoyer par téléchargement des images à partir d'un navigateur Web dans le compartiment {{site.data.keyword.cos_short}}. 
  - Afficher les images du compartiment {{site.data.keyword.cos_short}} dans un navigateur Web. 

Les prochaines étapes se concentrent sur la façon d'effectuer ces deux fonctions de démonstration plutôt que de générer une application de production entièrement développée. Déployer ce tutoriel et le laisser exposé et actif signifie que quiconque localise l'application peut effectuer les mêmes actions : envoyer par téléchargement des fichiers au compartiment {{site.data.keyword.cos_full_notm}} et afficher des images JPEG qui figurent déjà dans ce compartiment à l'aide de son navigateur. 

### Développement de l'application
{: #tutorial-develop-app}

Dans le fichier `package.json`, au sein de l'objet scripts, vous pouvez voir de quelle manière le démarrage ("start") est défini (exemple 33). Ce fichier est ce dont se sert la plateforme {{site.data.keyword.cloud_notm}} pour indiquer à un noeud qu'il doit exécuter app.js à chaque fois que l'application démarre.
Utilisez-le également lorsque vous testez l'application localement. Jetez un coup d'oeil au principal fichier d'application, appelé app.js. Il s'agit du code que nous avons demandé à Node.js de traiter en premier lorsque vous démarrez votre application à l'aide de la commande `npm start` (ou nodemon). 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="Exemple 33. Indiquer à votre application comment amorcer votre code personnalisé" caption-side="bottom"}
`Exemple 33. Indiquer à votre application comment amorcer votre code personnalisé`

Notre fichier app.js débute par le code illustré dans l'exemple 34.
Au début, le code utilise le noeud pour charger les modules nécessaires au démarrage. L'infrastructure Express crée l'application en tant que singleton appelé simplement `app`.
L'exemple se termine (en ignorant une grande partie du code pour l'instant) en indiquant à l'application qu'elle doit écouter sur le port qui est affecté et en lui communiquant une propriété d'environnement, ou 3000 par défaut. Lorsque le démarrage aboutit, un message est imprimé avec l'URL de serveur vers la console.

```javascript
var express = require('express');
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var app = express();
//...

// start server on the specified port and binding host
var port = process.env.PORT || 3000;
app.listen(port, function() {
    console.log("To view your app, open this link in your browser: http://localhost:" + port);
});
//...
```
{: codeblock}
{: javascript}
{: caption="Exemple 34. Le démarrage de votre application Web est simple, mais puissant" caption-side="bottom"}
`Exemple 34. Le démarrage de votre application Web est simple, mais puissant`

Voyons comment l'exemple 35 montre comment définir un chemin et des vues. La première ligne de code indique à l'infrastructure Express qu'elle doit utiliser le répertoire public pour servir nos fichiers statiques, qui contiennent les images statiques et les feuilles de style que nous utilisons. Les lignes qui suivent indiquent à l'application où trouver les modèles pour nos vues dans le répertoire src/views, et définissent notre moteur de vue comme EJS. En outre, l'infrastructure utilisera le middleware body-parser pour exposer les données de demande entrantes sur l'application au format JSON. Dans les lignes qui ferment l'exemple, l'application express répond à toutes les demandes GET entrantes vers notre URL d'application en affichant le modèle de vue index.ejs.

```javascript
//...
// serve the files out of ./public as our main files
app.use(express.static('public'));
app.set('views', './src/views');
app.set('view engine', 'ejs');
app.use(bodyParser.json());

var title = 'COS Image Gallery Web Application';
// Serve index.ejs
app.get('/', function (req, res) {
  res.render('index', {status: '', title: title});
});

//...
```
{: codeblock}
{: javascript}
{: caption="Exemple 35. Vues d'application Web et emplacements de modèle" caption-side="bottom"}
`Exemple 35. Vues d'application Web et emplacements de modèle`

La figure suivante montre le modèle de vue d'index lorsqu'il est affiché et envoyé au navigateur. Si vous utilisez `nodemon`, vous aurez peut-être constaté que votre navigateur a été actualisé lorsque vous avez sauvegardé vos modifications et que votre application se présente comme illustré dans la figure 8.


![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

Dans l'exemple 36, nos modèles de vue partagent le code HTML placé entre les balises &lt;head&gt;...&lt;/head&gt;, par conséquent, nous l'avons placé dans un modèle d'inclusion distinct (voir l'exemple 16).
Ce modèle (head-inc.ejs) contient un scriptlet et une liaison pour une variable JavaScript pour le titre de la page sur la ligne 1.
La variable `title` est définie dans `app.js` et transmise en tant que données pour notre modèle de vue dans la ligne au-dessous. Sinon, nous utilisons simplement certaines adresses CDN pour extraire Bootstrap CSS, Bootstrap JavaScript et JQuery. Enfin, nous ajoutons un fichier styles.css statique provenant de notre répertoire public/style sheets. 

```html
<title><%=title%></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Latest compiled and minified CSS -->
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
{: caption="Exemple 36. Eléments HTML de head-inc.ejs" caption-side="bottom"}
`Exemple 36. Eléments HTML de head-inc.ejs`

Le corps de la vue d'index contient nos onglets de navigation avec le style d'amorçage (voir l'exemple 37) et notre formulaire d'envoi par téléchargement dans une présentation de base fournie par les styles CSS inclus avec l'amorçage. 

Tenez compte des deux spécifications suivantes pour notre application :

-   Nous indiquons POST pour notre méthode de formulaire et des données de formulaire/multiple pour le type de codage de données de formulaire sur la ligne 24. Pour l'action de formulaire, nous envoyons les données depuis notre formulaire vers l'application vers la route d'application "/". Ensuite, nous effectuons des tâches supplémentaires dans notre logique de routeur pour gérer des demandes POST vers cette route.


-   Nous voulons afficher des commentaires sur l'état de la tentative d'envoi par téléchargement de fichier vers l'utilisateur. Ces commentaires sont transmis à notre vue dans une variable nommée "status", et ils sont affiches sous le formulaire d'envoi par téléchargement. 

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
    <h2>Envoi par téléchargement d'image dans IBM Cloud Object Storage</h2>
    <div class="row">
        <div class="col-md-12">
            <div class="container" style="margin-top: 20px;">
                <div class="row">

                    <div class="col-lg-8 col-md-8 well">

                        <p class="wellText">Téléchargez ici votre fichier image JPG</p>

                        <form method="post" enctype="multipart/form-data" action="/">
                            <p><input class="wellText" type="file" size="100px" name="img-file" /></p>
                            <br/>
                            <p><input class="btn btn-danger" type="submit" value="Envoyer par téléchargement" /></p>
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
{: caption="Exemple 37. Eléments HTML de index.ejs" caption-side="bottom"}
`Exemple 37. Eléments HTML de index.ejs`

Prenons le temps de nous intéresser de nouveau à `app.js` dans l'exemple 38. L'exemple configure les routes Express pour traiter les demandes supplémentaires qui seront émises vers notre application. Le code pour ces méthodes de routage se trouvera dans deux fichiers sous le répertoire `./src/routes` dans votre projet :


-   imageUploadRoutes.js : ce fichier gère ce qu'il se produit lorsque l'utilisateur sélectionne une image et clique sur Envoyer par téléchargement. 

-   galleryRoutes.js : ce fichier gère les demandes générées lorsque l'utilisateur clique sur l'onglet Gallery pour demander
    la vue imageGallery. 

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
{: caption="Exemple 38. Exemples de routeur Node Express" caption-side="bottom"}
`Exemple 38. Exemples de routeur Node Express`

#### Envoi par téléchargement d'image
{: #tutorial-develop-image-upload}

Consultez le code de 'imageUploadRoutes.js' dans l'exemple 39. Nous devons créer une instance d'un
nouveau routeur Express et la nommer `imageUploadRoutes` au début.
Ensuite, nous créons une fonction qui renvoie `imageUploadRoutes`
et nous l'affectons à une variable nommée `router`. Une fois créée, la fonction doit
être exportée en tant que module afin de la rendre accessible à l'infrastructure et à notre code principal dans app.js.
Pour séparer notre logique de routage de la logique d'envoi par téléchargement, un fichier contrôleur nommé
galleryController.js est nécessaire. Cette logique étant utilisée exclusivement pour traiter la demande entrante et fournir la réponse
appropriée, nous la plaçons dans cette fonction et nous la sauvegardons dans le répertoire ./src/controllers. 

L'instance du routeur à partir de l'infrastructure Express est l'endroit où notre instance imageUploadRoutes
est conçue pour router des demandes pour la route d'application racine ("/") lorsque la méthode HTTP POST est utilisée.
Dans la méthode `post` de notre instance imageUploadRoutes, nous utilisons le middleware des modules `multer` et
`multer-s3` qui est exposé par galleryController dans `upload`.
Le middleware prend les données et le fichier de notre formulaire d'envoi par téléchargement POST, il les traite, puis il exécute
une fonction de rappel. Dans la fonction de rappel, nous nous assurons que le code d'état HTTP obtenu est 200 et qu'il
y a au moins un fichier dans notre objet de demande à envoyer par téléchargement.
A partir de ces conditions, nous définissons les commentaires dans notre variable `status` et affichons le modèle
de vue d'index avec le nouvel état.

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
{: caption="Exemple 39. Détails de routeur Node Express" caption-side="bottom"}
`Exemple 39. Détails de routeur Node Express`

En comparaison, le code de galleryRouter dans l'exemple 40 est un modèle de simplicité. Nous suivons le même schéma que
pour imageUploadRouter, nous plaçons galleryController sur la première ligne de la fonction, puis nous configurons notre route. La principale
différence réside dans le fait que nous acheminons les demandes HTTP GET au lieu des demandes POST et que nous envoyons l'ensemble de la sortie
dans la réponse de getGalleryImages, qui est exposée par galleryController sur la dernière ligne de l'exemple.

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
{: caption="Exemple 40. Détails de routeur Node Express" caption-side="bottom"}
`Exemple 40. Détails de routeur Node Express`

A présent, examinons avec attention le contrôleur pour la galerie. 

Notez la façon dont nous configurons l'envoi par téléchargement de module `multer` dans l'exemple 41. Une partie du code
est tronquée et nous pouvons l'ignorer pour l'instant. Nous avons besoin des modules `ibm-cos-sdk`, `multer`
et `multer-s3`. Le code montre comment configurer un objet S3 qui pointe vers un noeud final de serveur {{site.data.keyword.cos_short}}. Nous
définissons des valeurs de manière statique, par exemple, l'adresse de noeud final, la région et le compartiment dans un souci de simplification, mais elles pourraient
facilement être référencées à partir d'une variable d'environnement ou d'un fichier de configuration JSON.


Nous définissons `upload` tel qu'il est utilisé dans l'exemple 41 et défini dans l'instance imageUploadRouter en créant une nouvelle
instance `multer` avec `storage` comme seule propriété. Cette propriété indique à
`multer` où envoyer le fichier à partir de nos données de formulaire/multiple. Etant donné que la plateforme {{site.data.keyword.cloud_notm}}
utilise une implémentation de l'API S3, nous définissions le stockage pour qu'il soit un objet `s3-multer`. Cet objet `s3-multer` contient une propriété `s3` que nous avons affectée à notre objet `s3` précédemment, et une propriété bucket à laquelle nous avons affecté la
variable `myBucket`, à laquelle une variable “web-images” a été affectée. L'objet `s3-multer` comporte désormais toutes les données
nécessaires pour connecter et envoyer par téléchargement des fichiers à notre compartiment {{site.data.keyword.cos_short}} lorsqu'il reçoit des données du formulaire d'envoi par téléchargement. Le nom ou la clé de l'objet envoyé par téléchargement correspondra au nom de fichier d'origine issu de l'objet de fichier lorsqu'il est stocké dans notre
compartiment {{site.data.keyword.cos_short}} “web-images”.  

**Astuce** : indiquez une date et une heure dans les noms de fichier afin que ceux-ci soient uniques.  

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
{: caption="Exemple 41. Détails de contrôleur Node Express" caption-side="bottom"}
`Exemple 41. Détails de contrôleur Node Express`

Pour les tests locaux, une tâche utile consiste à imprimer l'objet de fichier sur la console, `console.log(file)`.
Nous effectuons un test local du formulaire d'envoi par téléchargement et affichons le résultat à partir du journal de la console du fichier dans l'exemple 42.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Exemple 42. Affichage sur la console de l'objet de débogage" caption-side="bottom"}
`Exemple 42. Affichage sur la console de l'objet de débogage`

La figure 9 illustre les commentaires en retour de notre rappel indiquant que l'application a réussi à envoyer par téléchargement le fichier lors des tests.


![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### Extraction et affichage d'image
{: #tutorial-image-display}

N'oubliez pas, dans le fichier app.js, la ligne de code `app.use('/gallery', galleryRouter);` indique à l'infrastructure
Express qu'elle doit utiliser ce routeur lorsque la route "/gallery" est demandée. Ce routeur utilise galleryController.js (voir le code dans l'exemple 43). Nous définissons la
fonction getGalleryImages dont nous avons déjà vu la signature précédemment. A l'aide de l'objet `s3`
que nous avons configuré pour notre fonction d'envoi par téléchargement d'image, nous appelons la fonction nommée `listObjectsV2`. Cette fonction renvoie les données d'index définissant chacun des objets contenus dans notre compartiment. Pour afficher les images dans HTML, nous avons besoin d'une URL d'image pour chaque image
JPEG contenue dans notre compartiment `web-images` à afficher dans notre modèle de vue. La jointure avec l'objet de données renvoyé par
`listObjectsV2` contient des métadonnées relatives à chaque objet contenu dans notre compartiment.
 

Le code fait une boucle dans `bucketContents` lorsqu'il recherche une clé d'objet se terminant par ".jpg," et il crée un
paramètre pour transmettre la fonction S3 getSignedUrl. Cette fonction renvoie une URL signée pour un objet lorsque nous fournissons le nom de
compartiment et la clé de l'objet.
Dans la fonction de rappel, nous sauvegardons chaque URL dans un tableau que nous transmettons à la méthode de réponse du serveur HTTP `res.render` comme valeur d'une propriété nommée `imageUrls`.

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
{: caption="Exemple 43. Contenu partiel de galleryController.js" caption-side="bottom"}
`Exemple 43. Contenu partiel de galleryController.js`

Le dernier exemple de code (44) de ce tutoriel illustre le corps du modèle galleryView avec le code
nécessaire pour afficher les images. Nous obtenons le tableau imageUrls à partir de la méthode res.render()
et effectuons une itération sur une paire de balises &lt;div&gt;&lt;/div&gt; imbriquées où l'URL d'image
créera une demande GET pour l'image lorsque la route /gallery sera demandée.


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
{: caption="Exemple 44. Scriptlets de boucle et de sortie utilisés dans le modèle gallery" caption-side="bottom"}
`Exemple 44. Scriptlets de boucle et de sortie utilisés dans le modèle gallery`

Nous effectuons les tests localement à partir de http://localhost:3000/gallery (voir l'image illustrée dans la figure 10).


![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### Validation Git
{: #tutorial-develop-commit}

Maintenant que les fonctionnalités de base de l'application fonctionnent, nous allons valider notre code sur
notre référentiel local, puis l'envoyer à GitHub. A l'aide de GitHub Desktop, nous cliquons sur Changes (voir la figure 11), nous
tapons un récapitulatif des modifications dans la zone Summary, puis nous cliquons sur Commit to Local-dev. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

Lorsque nous cliquons sur Sync, notre validation est envoyée à la branche Local-dev éloignée que nous avons publiée sur GitHub, mais cette action démarre la phase de génération suivie de la phase de déploiement dans votre pipeline de distribution, comme illustré dans la dernière figure, numéro 12, de ce tutoriel.
 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## Etapes suivantes
{: #nextsteps}

Félicitations ! Nous avons mené à bien toutes les étapes du processus de génération d'une application Web Image Gallery à l'aide de la plateforme {{site.data.keyword.cloud_notm}}.
Vous pouvez explorer davantage chacun des concepts abordés dans cette introduction de base en vous rendant sur la page de la plateforme [{{site.data.keyword.cloud_notm}}](https://cloud.ibm.com/). 

Bonne chance !
