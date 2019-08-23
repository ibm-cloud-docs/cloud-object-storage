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

# Utilizzo di `Postman`
{: #postman}

Di seguito troverai la configurazione di base di `Postman` per l'API REST {{site.data.keyword.cos_full}}. Puoi trovare ulteriori dettagli nel riferimento API per i [bucket](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations) o gli [oggetti](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations).

L'utilizzo di `Postman` presuppone una certa familiarità con l'archiviazione oggetti e le informazioni necessarie provenienti da una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) o dalla [console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started). Se ci sono termini o variabili che non ti sono familiari, puoi trovarli nel [glossario](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology).

Informazioni d'identificazione personale (PII): quando crei i bucket e/o aggiungi gli oggetti, assicurati di non utilizzare informazioni che possono identificare un utente (persona fisica) per nome, ubicazione o qualsiasi altro mezzo.
{:tip}

## Panoramica del cliente API REST
{: #postman-rest}

REST (REpresentational State Transfer) è uno stile di architettura che fornisce uno standard per i sistemi di computer per interagire
tra loro sul web, utilizzando, di norma, i verbi e gli URL HTTP standard (GET, PUT, POST, ecc.) supportati da tutti i maggiori linguaggi e piattaforme di sviluppo. Tuttavia, l'interazione con un'API REST non è così semplice come utilizzare un browser internet standard. I browser semplici non consentono manipolazioni della richiesta URL. È in questo contesto che entra in gioco un'API REST.

Un client API REST fornisce un'applicazione basata sulla GUI semplice per interfacciarsi con una libreria API REST esistente. Un buon client rende semplice eseguire test, sviluppare e documentare le API consentendo agli utenti di mettere insieme richieste HTTP sia semplici che complesse. Postman è un eccellente client API REST che fornisce un ambiente di sviluppo API completo che include strumenti integrati per progettare e simulare, eseguire il debug, eseguire il test, documentare, monitorare e pubblicare le API. Fornisce anche funzioni utili come le raccolte e gli spazi di lavoro per rendere la collaborazione un gioco da ragazzi.  

## Prerequisiti
{: #postman-prereqs}
* Account IBM Cloud
* [Risorsa di archiviazione Cloud creata](https://cloud.ibm.com/catalog/) (il piano lite/gratuito funziona benissimo)
* [CLI IBM Cloud installata e configurata](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [ID istanza del servizio per la tua archiviazione Cloud](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [Token IAM (Identity and Access Management)](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [Endpoint per il tuo bucket COS](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### Crea un bucket
{: #postman-create-bucket}
1.	Avvia Postman
2.	Nella scheda New, seleziona l'elenco a discesa `PUT`. 
3.	Immetti l'endpoint nella barra degli indirizzi e aggiungi il nome del tuo nuovo bucket.
a.	I nomi bucket devono essere univoci in tutti i bucket, quindi scegli qualcosa di specifico.
4.	Nell'elenco a discesa Type, seleziona Bearer Token.
5.	Aggiungi il token IAM nella casella Token.
6.	Fai clic su Preview Request.
a.	Dovresti vedere un messaggio di conferma indicante che le intestazioni sono state aggiunte. 
7.	Fai clic sulla scheda Header in cui dovresti vedere una voce esistente per Authorization.
8.	Aggiungi una nuova chiave. 
a.	Chiave: `ibm-service-instance-id`
b.	Valore: ID istanza della risorsa per il tuo servizio di archiviazione cloud. 
9.	Fai clic su Send.
10.	Riceverai un messaggio di stato `200 OK`.

### Crea un nuovo file di testo
{: #postman-create-text-file}

1.	Crea una nuova scheda facendo clic sull'icona Più (+).
2.	Seleziona `PUT` dall'elenco.
3.	Nella barra degli indirizzi, immetti l'indirizzo dell'endpoint con il nome bucket proveniente dalla sezione precedente e un nome file. 
4.	Nell'elenco Type, seleziona Bearer Token.
5.	Aggiungi il token IAM nella casella Token.
6.	Seleziona la scheda Body.
7.	Seleziona un'opzione non elaborata e assicurati che sia selezionato Text.
8.	Immetti il testo nello spazio fornito. 
9.	Fai clic su Send.
10.	Riceverai un messaggio di stato `200 OK`.

### Elenca il contenuto di un bucket
{: #postman-list-objects}

1.	Crea una nuova scheda selezionando l'icona Più (+).
2.	Verifica che `GET` sia selezionato nell'elenco.
3.	Nella barra degli indirizzi, immetti l'indirizzo dell'endpoint con il nome bucket proveniente dalla sezione precedente. 
4.	Nell'elenco Type, seleziona Bearer Token.
5.	Aggiungi il token IAM nella casella Token.
6.	Fai clic su Send.
7.	Riceverai un messaggio di stato `200 OK`.
8.	Nella sezione Body of the Response è presente un messaggio XML con l'elenco dei file nel tuo bucket.

## Utilizzo della raccolta di esempio
{: #postman-collection}

Una raccolta Postman è disponibile per il [download ![Icona link esterno](../icons/launch-glyph.svg "Icona link esterno")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window} con esempi di richiesta API {{site.data.keyword.cos_full}} configurabili. 

### Importa la raccolta in Postman
{: #postman-import-collection}

1. In Postman, fai clic sul pulsante Import nell'angolo in alto a destra
2. Importa il file della raccolta utilizzando uno di questi metodi: 
    * Dalla finestra Import, trascina e rilascia il file della raccolta nella finestra etichettata **Drop files here**
    * Fai clic sul pulsante Choose Files e passa alla cartella e seleziona il file della raccolta
3. Ora *IBM COS* dovrebbe apparire nella finestra Collections
4. Espandi la raccolta e dovresti vedere venti (20) richieste di esempio
5. La raccolta contiene sei (6) variabili che devono essere configurate per poter eseguire correttamente le richieste API
    * Fai clic sui tre puntini alla destra della raccolta per espandere il menu e fai clic su Edit
6. Modifica le variabili in modo che soddisfino il tuo ambiente di archiviazione Cloud
    * **bucket** - Immetti il nome per il nuovo bucket che vuoi creare (i nomi bucket devono essere univoci nell'archiviazione Cloud).
    * **serviceid** - Immetti il CRN del tuo servizio di archiviazione Cloud. Le istruzioni per ottenere il tuo CRN sono disponibili [qui](/docs/overview?topic=overview-crn).
    * **iamtoken** - Immetti il token OAUTH per il tuo servizio di archiviazione Cloud. Le istruzioni per ottenere il tuo token OAUTH sono disponibili [qui](/docs/services/key-protect?topic=key-protect-retrieve-access-token).
    * **endpoint** - Immetti l'endpoint regionale per il tuo servizio di archiviazione Cloud. Ottieni gli endpoint disponibili dal [dashboard di IBM Cloud](https://cloud.ibm.com/resources/){:new_window}
        * *Assicurati che il tuo endpoint selezionato corrisponda al tuo servizio Key Protect per assicurarti che gli esempi vengano eseguiti correttamente*
    * **rootkeycrn** - Il CRN della chiave root creata nel tuo servizio Key Protect principale.
        * Il CRN dovrebbe somigliare a quanto segue:<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *Assicurati che il servizio Key Protect selezionato corrisponda alla regione dell'endpoint*
    * **bucketlocationvault** - Immetti il valore del vincolo di ubicazione per la creazione bucket per la richiesta API *Crea un nuovo bucket (classe di archiviazione diversa)*.
        * I valori accettabili includono:
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. Fai clic su Update

### Esecuzione degli esempi
{: #postman-samples}
Le richieste API di esempio sono piuttosto semplici e facili da utilizzare. Sono progettate per essere eseguite in ordine e per dimostrare come interagire con l'archiviazione Cloud. Possono anche essere utilizzate per eseguire un test funzionale sul tuo servizio di archiviazione Cloud per garantire il corretto funzionamento. 

<table>
    <tr>
        <th>Richiesta</th>
        <th>Risultato previsto</th>
        <th>Risultati del test</th>
    </tr>
    <tr>
        <td>Richiama l'elenco di bucket</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo dovresti vedere un elenco XML di bucket nella tua archiviazione cloud.
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include il contenuto previsto</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Crea un nuovo bucket</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>Crea un nuovo file di testo</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include l'intestazione prevista</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Crea un nuovo file binario</td>
        <td>
            <ul>
                <li>
                    Fai clic su Body e fai clic su Choose File per selezionare un'immagine da caricare
                </li>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include l'intestazione prevista</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Richiama l'elenco di file dal bucket</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere i due file che hai creato nelle richieste precedenti
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include l'intestazione prevista</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Richiama l'elenco di file dal bucket (filtro in base al prefisso)</td>
        <td>
            <ul>
                <li>Modifica il valore querystring in prefix=&lt;some text&gt;</li>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere i file con i nomi che iniziano con il prefisso specificato
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include l'intestazione prevista</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Richiama il file di testo</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere il testo che hai immesso nella richiesta precedente
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include il contenuto del corpo previsto</li>
                <li>La risposta include l'intestazione prevista</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Richiama il file binario</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere l'immagine che hai scelto nella richiesta precedente
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include l'intestazione prevista</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Richiama l'elenco di caricamenti in più parti non riusciti</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere i caricamenti in più parti non riusciti per il bucket
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include il contenuto previsto</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Richiama l'elenco dei caricamenti in più parti non riusciti (filtro in base al nome)</td>
        <td>
            <ul>
                <li>Modifica il valore querystring in prefix=&lt;some text&gt;</li>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere i caricamenti in più parti non riusciti per il bucket con i nomi che iniziano con il prefisso specificato
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include il contenuto previsto</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Configura il bucket abilitato a CORS</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Richiama la configurazione CORS del bucket</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
                <li>
                    Nel corpo della risposta dovresti vedere la configurazione CORS impostata per il bucket
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
                <li>La risposta include il contenuto previsto</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>Elimina la configurazione CORS del bucket</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Elimina il file di testo</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Elimina il file binario</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Elimina il bucket</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Crea un nuovo bucket (classe di archiviazione diversa)</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Elimina il bucket (classe di archiviazione diversa)</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Crea un nuovo bucket (Key Protect)</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>Elimina il bucket (Key Protect)</td>
        <td>
            <ul>
                <li>Codice stato 200 OK</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>La richiesta è riuscita</li>
            </ul>
        </td>                
    </tr>
</table>

## Utilizzo di Postman Collection Runner
{: #postman-runner}

Postman Collection Runner fornisce un'interfaccia utente per eseguire il test di una raccolta e ti consente di eseguire tutte le richieste presenti in una raccolta contemporaneamente.  

1. Fai clic sul pulsante Runner nell'angolo in alto a destra sulla finestra Postman principale.
2. Nella finestra Runner, seleziona la raccolta IBM COS e fai clic sul grande pulsante blu **Run IBM COS** nella parte inferiore dello schermo.
3. La finestra Collection Runner mostrerà le iterazioni mentre le richieste vengono eseguite. Vedrai comparire i risultati del test sotto ciascuna delle richieste. 
    * **Run Summary** mostra una vista a griglia delle richieste e consente il filtraggio dei risultati. 
    * Puoi anche fare clic su **Export Results** che salverà i risultati in un file JSON.
