---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: authorization, aws, hmac, signature

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

# Utilizzo delle credenziali HMAC
{: #hmac}

L'API {{site.data.keyword.cos_full}} è un'API basata su REST per la lettura e la scrittura di oggetti. Utilizza {{site.data.keyword.iamlong}} per l'autenticazione/autorizzazione e supporta un sottoinsieme dell'APi S3 per una facile migrazione delle applicazioni a {{site.data.keyword.cloud_notm}}.

Oltre all'autenticazione basata sui token IAM, è anche possibile [eseguire l'autenticazione utilizzando una firma](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) creata da una coppia di chiavi di accesso e segreta. Ciò è funzionalmente identico alle chiavi AWS Signature Versione 4 e le chiavi HMAC fornite da IBM COS dovrebbero funzionare con la maggior parte delle librerie e degli strumenti compatibili con S3.

Gli utenti possono creare un insieme di credenziali HMAC durante la creazione di una [credenziale del servizio](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials) fornendo il parametro di configurazione `{"HMAC":true}` durante la creazione della credenziale. Ecco un esempio che mostra come utilizzare la CLI {{site.data.keyword.cos_full}} per creare una chiave di servizio con le credenziali HMAC utilizzando il ruolo **Writer** (altri ruoli potrebbero essere disponibili per il tuo account e potrebbero essere più adatti per le tue esigenze). 

```
ibmcloud resource service-key-create <key-name-without-spaces> Writer --instance-name "<instance name--use quotes if your instance name has spaces>" --parameters '{"HMAC":true}'
```
{:codeblock: .codeblock}

{: caption="Esempio 1. Utilizzo di cURL per creare credenziali HMAC. Nota l'uso sia di virgolette singole che doppie." caption-side="bottom"}

Se vuoi archiviare i risultati della chiave appena creata dal comando nell'Esempio 1, puoi accodare ` > file.skey` alla fine dell'esempio. Per gli scopi di questa serie di istruzioni, devi solo trovare l'intestazione `cos_hmac_keys` con le chiavi secondarie, `access_key_id`, e `secret_access_key`, i due campi di cui hai bisogno, come mostrato nell'Esempio 2.

```
    cos_hmac_keys:
        access_key_id:      7exampledonotusea6440da12685eee02
        secret_access_key:  8not8ed850cddbece407exampledonotuse43r2d2586
```

{: caption="Esempio 2. Chiavi importanti quando si generano le credenziali HMAC." caption-side="bottom"}

Di particolare interesse è la capacità di impostare variabili di ambiente (le istruzioni a essere relative sono specifiche per il sistema operativo coinvolto). Nel caso, poniamo, dell'Esempio 3, uno script `.bash_profile` contiene `COS_HMAC_ACCESS_KEY_ID` e `COS_HMAC_SECRET_ACCESS_KEY` che sono esportate all'avvio di una shell e utilizzate nello sviluppo.

```
export COS_HMAC_ACCESS_KEY_ID="7exampledonotusea6440da12685eee02"
export COS_HMAC_SECRET_ACCESS_KEY="8not8ed850cddbece407exampledonotuse43r2d2586"

```
{:codeblock: .codeblock}

{: caption="Esempio 3. Utilizzo delle credenziali HMAC come variabili di ambiente." caption-side="bottom"}

Una volta creata la credenziale del servizio, la chiave HMAC viene inclusa nel campo `cos_hmac_keys`. Queste chiavi HMAC sono quindi associate ad un [ID servizio](/docs/iam?topic=iam-serviceids#serviceids) e possono essere utilizzate per accedere a qualsiasi risorsa od operazione consentita dal ruolo dell'ID servizio. 

Nota: quando utilizzi le credenziali HMAC per creare le firme da utilizzare con le chiamate [API REST](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api) dirette, sono necessarie delle intestazioni aggiuntive:
1. Tutte le richieste devono avere un'intestazione `x-amz-date` con la data in formato `%Y%m%dT%H%M%SZ`.
2. Qualsiasi richiesta che abbia un payload (caricamenti di oggetti, eliminazione di più oggetti ecc.) deve fornire un'intestazione `x-amz-content-sha256` con un hash SHA256 del contenuto del payload.
3. Gli ACL (diversi da `public-read`) non sono supportati.

Non tutti gli strumenti compatibili con S3 sono attualmente supportati. Alcuni strumenti provano a impostare ACL diversi da `public-read` alla creazione del bucket. La creazione di bucket tramite questi strumenti non riuscirà. Se una richiesta `PUT bucket` non riesce con un errore di ACL non supportato, utilizza prima la [Console](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) per creare il bucket e configura quindi lo strumento per leggere e scrivere oggetti in tale bucket.Gli strumenti che impostano gli ACL sulle scritture di oggetti non sono attualmente supportati.
{:tip}
