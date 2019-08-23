---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# Utilisation de `Postman`
{: #postman}

Voici une configuration `Postman` de base pour l'API REST {{site.data.keyword.cos_full}}. D'autres informations figurent dans la référence d'API pour des [compartiments](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) ou des [objets](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations). 

L'utilisation de `Postman` suppose de posséder des connaissances en matière de stockage d'objets et d'avoir récupéré les informations nécessaires à partir des [données d'identification de service](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) ou de la [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Si des termes ou des variables ne vous sont pas familiers, vous pouvez consulter leur définition dans le [glossaire](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology). 

Informations identifiant la personne. Lors de la création de compartiments et/ou de l'ajout d'objets, prenez soin de ne pas utiliser d'informations pouvant identifier un utilisateur (personne physique) en incluant un nom, un emplacement ou toute autre chose dans le nom du compartiment ou de l'objet. {:tip}

## Présentation du client API REST
{: #postman-rest}

REST (Representational State Transfer) est un style architectural qui fournit aux systèmes informatiques une norme leur permettant d'interagir les uns avec les autres sur le Web, en utilisant généralement des URL et des verbes (GET, PUT, POST, etc.) HTTP standard qui sont pris en charge par toutes les principaux langages et les principales plateformes de développement. Toutefois, interagir avec une API REST n'est pas aussi simple qu'utiliser un navigateur Internet standard. Les navigateurs simples ne permettent aucune manipulation de la demande d'URL. C'est là qu'intervient un client API REST.

Un client API REST fournit une simple application basée sur une interface graphique pour interagir avec une bibliothèque d'API REST existante. Un bon client facilite les tests, le développement et la documentation des API en permettant aux utilisateurs de rassembler rapidement des demandes HTTP simples et complexes. Postman est un excellent client API REST qui fournit un environnement de développement d'API complet incluant des outils intégrés pour la conception et la simulation, le débogage, les tests, la documentation, la surveillance et la publication des API. Il fournit également des fonctions utiles telles que les collections et les espaces de travail qui font de la collaboration un jeu d'enfant.  

## Prérequis
{: #postman-prereqs}
* Un compte IBM Cloud
* Une [ressource Cloud Storage créée](https://cloud.ibm.com/catalog/) (un plan lite/gratuit convient parfaitement)
* Une [interface CLI IBM Cloud installée et configurée](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* Un [ID d'instance de service pour votre service Cloud Storage](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* Un [jeton IAM (Identity and Access Management)](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* Un [noeud final pour votre compartiment COS](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### Création d'un compartiment
{: #postman-create-bucket}
1.	Lancez Postman. 
2.	Dans l'onglet New, sélectionnez `PUT` dans la liste déroulante. 
3.	Entrez le noeud final dans la barre d'adresse et ajoutez le nom de votre nouveau compartiment. 
a.	Les noms de compartiment doivent être uniques dans tous les compartiments, par conséquent, choisissez quelque chose de spécifique. 
4.	Dans la liste déroulante Type, sélectionnez Bearer Token. 
5.	Ajoutez le jeton IAM dans la zone Token. 
6.	Cliquez sur le bouton d'aperçu de la demande. 
a.	Un message confirmant que les en-têtes ont été ajoutés doit s'afficher. 
7.	Cliquez sur l'onglet Header où vous devez voir une entrée existante pour Authorization. 
8.	Ajoutez une nouvelle clé. 
a.	Clé : `ibm-service-instance-id`. 
b.	Valeur : ID d'instance de ressource pour votre service Cloud Storage. 
9.	Cliquez sur Send. 
10.	Vous recevrez un message d'état `200 OK`. 

### Création d'un nouveau fichier texte
{: #postman-create-text-file}

1.	Créez un nouvel onglet en cliquant sur l'icône Plus (+). 
2.	Sélectionnez `PUT` dans la liste. 
3.	Dans la barre d'adresse, entrez l'adresse du noeud final avec le nom de compartiment provenant de la section précédente et un nom de fichier. 
4.	Dans la liste Type, sélectionnez Bearer Token. 
5.	Ajoutez le jeton IAM dans la zone Token. 
6.	Sélectionnez l'onglet Body. 
7.	Sélectionnez l'option brute et assurez-vous que l'option Text est sélectionnée. 
8.	Entrez du texte dans l'espace fourni. 
9.	Cliquez sur Send. 
10.	Vous recevrez un message d'état `200 OK`. 

### Création de la liste des éléments de contenu d'un compartiment
{: #postman-list-objects}

1.	Créez un nouvel onglet en cliquant sur l'icône Plus (+). 
2.	Vérifiez que `GET` est sélectionné dans la liste. 
3.	Dans la barre d'adresse, entrez l'adresse du noeud final avec le nom de compartiment provenant de la section précédente. 
4.	Dans la liste Type, sélectionnez Bearer Token. 
5.	Ajoutez le jeton IAM dans la zone Token. 
6.	Cliquez sur Send. 
7.	Vous recevrez un message d'état `200 OK`. 
8.	Dans le corps de la section Response figure un message XML avec la liste des fichiers contenus dans votre compartiment. 

## Utilisation de l'exemple de collection
{: #postman-collection}

Une collection Postman est disponible pour [réception par téléchargement![Icône de lien externe](../icons/launch-glyph.svg "Icône de lien externe")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window} avec des exemples de demandes d'API {{site.data.keyword.cos_full}} configurables. 

### Importation de la collection dans Postman
{: #postman-import-collection}

1. Dans Postman, cliquez sur le bouton Import dans l'angle supérieur droit. 
2. Importez le fichier de collection en utilisant l'une des méthodes suivantes :
    * A partir de la fenêtre Import, faites glisser et déplacez le fichier de collection dans la fenêtre intitulée **Drop files here**. 
    * Cliquez sur le bouton Choose Files et accédez au dossier, puis sélectionnez le fichier de collection. 
3. *IBM COS* doit maintenant apparaître dans la fenêtre Collections. 
4. Développez la collection ; vingt (20) exemples de demande doivent apparaître. 
5. La collection contient six (6) variables qui devront être définies afin de pouvoir exécuter correctement les demandes d'API. 
    * Cliquez sur les trois points situés à droite de la collection pour développer le menu, puis cliquez sur Edit. 
6. Editez les variables pour qu'elles correspondant à votre environnement Cloud Storage. 
    * **bucket** - Entrez le nom du nouveau compartiment que vous souhaitez créer (les noms de compartiment doivent être uniques dans l'environnement Cloud Storage). 
    * **serviceid** - Entrez le CRN de votre service Cloud Storage. Vous trouverez les instructions d'obtention de votre CRN en cliquant [ici](/docs/overview?topic=overview-crn).
    * **iamtoken** - Entrez le jeton OAUTH pour votre service Cloud Storage. Bous trouverez les instructions d'obtention de votre jeton OAUTH en cliquant [ici](/docs/services/key-protect?topic=key-protect-retrieve-access-token). 
    * **endpoint** - Entrez le noeud final régional pour votre service Cloud Storage. Procurez-vous les noeuds finaux disponibles à partir du [tableau de bord IBM Cloud](https://cloud.ibm.com/resources/){:new_window}. 
        * *Vérifiez que le noeud final sélectionné correspond à votre service Key Protect de sorte que les exemples s'exécutent correctement.*
    * **rootkeycrn** - CRN de la clé racine créée dans votre principal service Key Protect. 
        * Le CRN doit se présenter comme suit :<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Vérifiez que le service Key Protect sélectionné correspond à la région du noeud final*. 
    * **bucketlocationvault** - Entrez la valeur de contrainte d'emplacement pour la création de compartiment pour la demande d'API *Création d'un compartiment avec une classe de stockage différente*. 
        * Les valeurs admises sont les suivantes :
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. Cliquez sur Update. 

### Exécution des exemples
{: #postman-samples}
Les exemples de demande d'API sont simples et plutôt faciles à utiliser. Conçus pour fonctionner dans un certain ordre, ils montrent comment interagir avec le service Cloud Storage. Ils peuvent également être utilisés pour exécuter un test fonctionnel sur votre service Cloud Storage afin de garantir un fonctionnement correct. 

<table>
    <tr>
        <th>Demande</th>
        <th>Résultat attendu</th>
        <th>Résultats de test</th>
    </tr>
    <tr>
        <td>Extraction d'une liste de compartiments</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
                <li>
                    Dans le corps, vous devez définir une liste XML recensant les compartiments inclus dans votre service Cloud Storage.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse comporte le contenu attendu. </li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Création d'un nouveau compartiment</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Création d'un nouveau fichier texte</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse contient l'en-tête attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Création d'un nouveau fichier binaire</td>
        <td>
            <ul>
                <li>
                    Cliquez sur Body, puis sur Choose File pour sélectionner une image à envoyer par téléchargement.
                </li>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse contient l'en-tête attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Extraction d'une liste de fichiers à partir d'un compartiment</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
                <li>
                    Les deux fichiers que vous avez créés lors des demandes précédentes doivent apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse contient l'en-tête attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Extraction d'une liste de fichiers à partir d'un compartiment (filtrage par préfixe)</td>
        <td>
            <ul>
                <li>Remplacez la valeur de chaîne de requête par prefix=&lt;some text&gt;. </li>
                <li>Code d'état : 200 OK</li>
                <li>
                    Les fichiers dont le nom commence par le préfixe spécifié doivent apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse contient l'en-tête attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Extraction d'un fichier texte</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
                <li>
                    Le texte que vous avez saisi lors de la demande précédente doit apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse comporte le contenu de texte attendu. </li>
                <li>La réponse contient l'en-tête attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Extraction d'un fichier binaire</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
                <li>
                    L'image que vous avez choisie lors de la demande précédente doit apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse contient l'en-tête attendu. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Extraction d'une liste d'envois par téléchargement en plusieurs parties ayant échoué</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
                <li>
                    Les envois par téléchargement en plusieurs parties ayant échoué pour le compartiment doivent apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse comporte le contenu attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Extraction d'une liste d'envois par téléchargement en plusieurs parties ayant échoué (filtrage par nom)</td>
        <td>
            <ul>
                <li>Remplacez la valeur de chaîne de requête par prefix=&lt;some text&gt;. </li>
                <li>Code d'état : 200 OK</li>
                <li>
                    Les envois par téléchargement en plusieurs parties ayant échoué pour le compartiment et dont le nom commence par le préfixe spécifié doivent apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse comporte le contenu attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Définition d'un compartiment activé pour CORS</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Extraction d'une configuration CORS pour un compartiment</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
                <li>
                    La configuration CORS définie pour le compartiment doit apparaître dans le corps de la réponse.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
                <li>La réponse comporte le contenu attendu. </li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Suppression d'une configuration CORS pour un compartiment</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suppression d'un fichier texte</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suppression d'un fichier binaire</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suppression d'un compartiment</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Création d'un nouveau compartiment avec une classe de stockage différente</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suppression d'un compartiment avec une classe de stockage différente</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Création d'un nouveau compartiment (Key Protect)</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Suppression d'un compartiment (Key Protect)</td>
        <td>
            <ul>
                <li>Code d'état : 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La demande a abouti. </li>
            </ul>
        </td>                
    </tr>
</table>

## Utilisation de Postman Collection Runner
{: #postman-runner}

Postman Collection Runner fournit une interface utilisateur afin de tester une collection et vous permet d'exécuter toutes les demandes en même temps dans une collection.  

1. Cliquez sur le bouton Runner dans l'angle supérieur droit de la fenêtre Postman principale. 
2. Dans la fenêtre Runner, sélectionnez la collection IBM COS et cliquez sur le gros bouton bleu intitulé **Run IBM COS** qui se trouve au bas de l'écran. 
3. La fenêtre Collection Runner affiche les itérations à mesure que les demandes sont exécutées. Les résultats de test apparaissent sous chacune des demandes. 
    * La fenêtre **Run Summary** affiche une vue de grille des demandes et permet de filtrer les résultats. 
    * Vous pouvez également cliquer sur **Export Results** afin de sauvegarder les résultats dans un fichier JSON. 
