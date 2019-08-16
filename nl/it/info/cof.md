---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: cloud foundry, compute, stateless

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

# Utilizzo di Cloud Object Storage con le applicazioni Cloud Foundry
{: #cloud-foundry}

{{site.data.keyword.cos_full}} può essere accoppiato ad applicazioni {{site.data.keyword.cfee_full}} per fornire un contenuto altamente disponibile utilizzando le regioni e gli endpoint.

## Cloud Foundry Enterprise Environment
{: #cloud-foundry-ee}
{{site.data.keyword.cfee_full}} è una piattaforma per ospitare applicazioni e servizi nel cloud. Su richiesta, puoi istanziare più piattaforme isolate di livello aziendale che vengono eseguite all'interno del tuo account e possono essere distribuite su hardware condiviso o dedicato. La piattaforma semplifica il ridimensionamento delle applicazioni man mano che il consumo aumenta, semplificando il runtime e l'infrastruttura in modo da consentirti di concentrarti sullo sviluppo.

Una corretta implementazione di una piattaforma Cloud Foundry richiede [una pianificazione e una progettazione adeguate](/docs/cloud-foundry?topic=cloud-foundry-bpimplementation#bpimplementation) per le risorse necessarie e i requisiti aziendali. Trova ulteriori informazioni [introduttive](/docs/cloud-foundry?topic=cloud-foundry-about#creating) con Cloud Foundry Enterprise Environment e un'[esercitazione](/docs/cloud-foundry?topic=cloud-foundry-getting-started#getting-started) introduttiva.

### Regioni
{: #cloud-foundry-regions}
Gli [endpoint regionali](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) sono una parte importante dell'ambiente IBM Cloud. Puoi creare applicazioni e istanze del servizio in regioni differenti con la stessa infrastruttura IBM Cloud per la gestione delle applicazioni e la stessa vista dei dettagli dell'utilizzo per la fatturazione. Scegliendo una regione IBM Cloud geograficamente vicina a te o ai tuo i clienti, puoi ridurre la latenza dei dati nelle tue applicazioni e ridurre al minimo i costi. Le regioni possono anche essere selezionate per far fronte a eventuali problemi di sicurezza o requisiti normativi. 

Con {{site.data.keyword.cos_full}}, puoi scegliere di diffondere i dati in un singolo data center, un'intera regione o anche una combinazione di regioni [selezionando l'endpoint](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints#select-regions-and-endpoints) dove la tua applicazione invia le richieste API.

### Connessioni e alias di risorse
{: #cloud-foundry-aliases}

Un alias è una connessione tra il tuo servizio gestito all'interno di un gruppo di risorse e un'applicazione all'interno di un'organizzazione o di uno spazio. Gli alias sono come collegamenti simbolici che contengono riferimenti a risorse remote. Consente l'interoperabilità e il riutilizzo di un'istanza nell'ambito della piattaforma. Nella console {{site.data.keyword.cloud_notm}}, la connessione (alias) è rappresentata come un'istanza del servizio. Puoi creare un'istanza di un servizio in un gruppo di risorse e quindi riutilizzarla da qualsiasi regione disponibile creando un alias in un'organizzazione o uno spazio in tali regioni.

## Memorizzazione di credenziali come variabili VCAP 
{: #cloud-foundry-vcap}

Le credenziali {{site.data.keyword.cos_short}} possono essere memorizzate nella variabile di ambiente VCAP_SERVICES, che può essere analizzata per l'utilizzo quando si accede al servizio {{site.data.keyword.cos_short}}. Le credenziali includono le informazioni come sono presentate nel seguente esempio:

```json
{
    "cloud-object-storage": [
        {
            "credentials": {
                "apikey": "abcDEFg_lpQtE23laVRPAbmmBIqKIPmyN4EyJnAnYU9S-",
                "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
                "iam_apikey_description": "Auto generated apikey during resource-key operation for Instance - crn:v1:bluemix:public:cloud-object-storage:global:a/123456cabcddda99gd8eff3191340732:7766d05c-b182-2425-4d7e-0e5c123b4567::",
                "iam_apikey_name": "auto-generated-apikey-cf4999ce-be10-4712-b489-9876e57a1234",
                "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
                "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::a/ad123ab94a1cca96fd8efe3191340999::serviceid:ServiceId-41e36abc-7171-4545-8b34-983330d55f4d",
                "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/1d524cd94a0dda86fd8eff3191340732:8888c05a-b144-4816-9d7f-1d2b333a1444::"
            },
            "syslog_drain_url": null,
            "volume_mounts": [],
            "label": "cloud-object-storage",
            "provider": null,
            "plan": "Lite",
            "name": "mycos",
            "tags": [
                "Lite",
                "storage",
                "ibm_release",
                "ibm_created",
                "rc_compatible",
                "ibmcloud-alias"
            ]
        }
    ]
}
```

La variabile di ambiente VCAP_SERVICES può quindi essere analizzata all'interno della tua applicazione per accedere al tuo contenuto {{site.data.keyword.cos_short}}. Di seguito è riportato un esempio di integrazione della variabile di ambiente con l'SDK COS utilizzando Node.js.

```javascript
const appEnv = cfenv.getAppEnv();
const cosService = 'cloud-object-storage';

// init the cos sdk
var cosCreds = appEnv.services[cosService][0].credentials;
var AWS = require('ibm-cos-sdk');
var config = {
    endpoint: 's3.us-south.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net',
    apiKeyId: cosCreds.apikey,
    ibmAuthEndpoint: 'https://iam.cloud.ibm.com/identity/token',
    serviceInstanceId: cosCreds.resource_instance_id,
};

var cos = new AWS.S3(config);
```

Per ulteriori informazioni su come utilizzare l'SDK per accedere a {{site.data.keyword.cos_short}} con degli esempi di codice visita:

* [Utilizzo di Java](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#using-java)
* [Utilizzo di Python](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-python#using-python)
* [Utilizzo di Node.js](/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-node#using-node-js)

## Creazione di bind di servizio 
{: #cloud-foundry-bindings}

### Dashboard
{: #cloud-foundry-bindings-console}

Il modo più semplice per creare un bind di servizio è utilizzando il [dashboard {{site.data.keyword.cloud}}](https://cloud.ibm.com/resources). 

1. Accedi al [dashboard](https://cloud.ibm.com/resources)
2. Fai clic sull'applicazione Cloud Foundry
3. Fai clic su Connessioni nel menu a sinistra
4. Fai clic su **Crea connessione** sulla destra
5. Dalla pagina *Connetti servizio compatibile esistente*, passa il puntatore del mouse sul tuo servizio {{site.data.keyword.cos_short}} e fai clic su **Connetti**.
6. Dalla schermata a comparsa *Connetti servizio abilitato a IAM*, seleziona il ruolo di accesso, lascia Genera automaticamente per l'ID servizio e fai clic su **Connetti**
7. L'applicazione Cloud Foundry deve essere ripreparata al fine di utilizzare il nuovo bind di servizio. Fai clic su **Riprepara** per avviare il processo.
8. Una volta completata la ripreparazione, il tuo servizio Cloud Object Storage è disponibile per la tua applicazione.

La variabile di ambiente VCAP_SERVICES delle applicazioni viene aggiornata automaticamente con le informazioni sul servizio. Per visualizzare la nuova variabile:

1. Fai clic su *Runtime* nel menu a destra
2. Fai clic su *Variabili di ambiente*
3. Verifica che il tuo servizio COS sia ora elencato

### Strumenti client IBM (CLI)
{: #cloud-foundry-bindings-cli}

1. Accedi alla CLI IBM Cloud
```
 ibmcloud login --apikey <your api key>
```

2. Indica come destinazione il tuo ambiente Cloud Foundry
```
 ibmcloud target --cf
```

3. Crea un alias di servizio per il tuo {{site.data.keyword.cos_short}}
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Crea un bind di servizio tra il tuo alias {{site.data.keyword.cos_short}} e la tua applicazione Cloud Foundry e fornisci un ruolo per il tuo bind. I ruoli validi sono:<br/><ul><li>Writer (Scrittore)</li><li>Reader (Lettore)</li><li>Manager (Gestore)</li><li>Administrator (Amministratore)</li><li>Operator (Operatore)</li><li>Viewer (Visualizzatore)</li><li>Editor</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role>
```

### Strumenti client IBM (CLI) con credenziali HMAC
{: #cloud-foundry-hmac}

HMAC (hash-based message authentication code) è un meccanismo per calcolare un codice di autenticazione del messaggio creato che utilizza una coppia di chiavi di accesso e segreta. Questa tecnica può essere utilizzata per verificare l'integrità e l'autenticità di un messaggio. Ulteriori informazioni sull'utilizzo delle [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac#using-hmac-credentials) sono disponibili nella documentazione di {{site.data.keyword.cos_short}}.

1. Accedi alla CLI IBM Cloud
```
 ibmcloud login --apikey <your api key>
```

2. Indica come destinazione il tuo ambiente Cloud Foundry
```
 ibmcloud target --cf
```

3. Crea un alias di servizio per il tuo {{site.data.keyword.cos_short}}
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```

4. Crea un bind di servizio tra il tuo alias {{site.data.keyword.cos_short}} e la tua applicazione Cloud Foundry e fornisci un ruolo per il tuo bind. <br/><br/>* **Nota:** un parametro supplementare* (`{"HMAC":true}`) *è necessario per creare le credenziali del servizio con HMAC abilitato.*<br/><br/>I ruoli validi sono:<br/><ul><li>Writer (Scrittore)</li><li>Reader (Lettore)</li><li>Manager (Gestore)</li><li>Administrator (Amministratore)</li><li>Operator (Operatore)</li><li>Viewer (Visualizzatore)</li><li>Editor</li></ul>
```
ibmcloud resource service-binding-create <service alias> <cf app name> <role> -p '{"HMAC":true}'
```

### Esecuzione del bind a {{site.data.keyword.containershort_notm}}
{: #cloud-foundry-k8s}

La creazione di un bind di servizio a {{site.data.keyword.containershort}} richiede una procedura leggermente differente. 

*Per questa sezione, dovrai anche installare [jq - un leggero processore JSON di riga di comando](https://stedolan.github.io/jq/){:new_window}.*

Hai bisogno delle seguenti informazioni e di sostituire i valori chiave nei comandi di seguito:

* `<service alias>` - nuovo nome alias per il servizio COS
* `<cos instance name>` - nome della tua istanza COS esistente
* `<service credential name>` - nuovo nome per la tua chiave/credenziale del servizio
* `<role>` - ruolo da collegare alla tua chiave del servizio (vedi sopra per i ruoli validi; `Writer` è quello specificato con maggiore frequenza)
* `<cluster name>` - nome del tuo servizio cluster Kubernetes esistente
* `<secret binding name>` - questo valore viene generato quando COS è associato al servizio cluster


1. Crea un alias di servizio per la tua istanza COS<br/><br/>* **Nota:** l'istanza COS può avere solo un singolo alias di servizio*
```
ibmcloud resource service-alias-create <service alias> --instance-name <cos instance name>
```
 
1. Crea una nuova chiave di servizio con le autorizzazioni all'alias di servizio COS
```
ibmcloud resource service-key-create <service credential name> <role> --alias-name <service alias> --parameters '{"HMAC":true}’
```

3. Esegui il bind del servizio cluster a COS
```
ibmcloud cs cluster-service-bind --cluster <cluster name> --namespace default --service <service alias>
```

4. Verifica che l'alias di servizio COS sia associato al cluster
```
ibmcloud cs cluster-services --cluster <cluster name>
```
L'output sarà simile al seguente:
```
OK
Service   Instance GUID                          Key             Namespace
sv-cos    91e0XXXX-9982-4XXd-be60-ee328xxxacxx   cos-hmac        default
```

5. Richiama l'elenco di segreti nel tuo cluster e trova il segreto per il tuo servizio COS. Di norma sarà `binding-` più il `<service alias>` che hai specificato nel passo 1 (ossia `binding-sv-cos`). Utilizza questo valore come `<secret binding name>` nel passo 6.
```
kubectl get secrets
```
l'output dovrebbe essere simile a questo:
```
NAME                                   TYPE                                  DATA      AGE
binding-sv-cos                         Opaque                                1         18d
bluemix-default-secret                 kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-international   kubernetes.io/dockerconfigjson        1         20d
bluemix-default-secret-regional        kubernetes.io/dockerconfigjson        1         20d
default-token-8hncf                    kubernetes.io/service-account-token   3         20d
```

6. Verifica che le credenziali HMAC COS siano disponibili nei tuoi segreti del cluster
```
kubectl get secret <secret binding name> -o json | jq .data.binding | sed -e 's/^"//' -e 's/"$//' | base64 -D | jq .cos_hmac_keys
```
l'output dovrebbe essere simile a questo:
```json
{
    "access_key_id": "9XX0adb9948c41eebb577bdce6709760",
    "secret_access_key": "bXXX5d8df62748a46ea798be7eaf8efeb6b27cdfc40a3cf2"
}
```
