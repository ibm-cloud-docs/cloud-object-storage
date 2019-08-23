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

# Concessione dell'accesso pubblico
{: #iam-public-access}

A volte i dati sono destinati a essere condivisi. I bucket possono contenere set di dati aperti per ricerche accademiche e private o repository di immagini utilizzati dalle applicazioni web e dalle reti di distribuzione di contenuto. Rendi questi bucket accessibili utilizzando il gruppo **Accesso pubblico**.
{: shortdesc}

## Utilizzo della console per impostare l'accesso pubblico
{: #iam-public-access-console}

Innanzitutto, assicurati di avere un bucket. In caso negativo, attieniti all'[esercitazione introduttiva](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) per acquisire dimestichezza con la console.

### Abilita accesso pubblico
{: #public-access-console-enable}

1. Dal {{site.data.keyword.cloud_notm}} [dashboard della console](https://cloud.ibm.com/), seleziona **Archiviazione** per visualizzare il tuo elenco risorse.
2. Seleziona quindi l'istanza del servizio con il tuo bucket dall'interno del menu **Archiviazione**. Questa operazione ti porta alla console {site.data.keyword.cos_short}}.
3. Scegli il bucket che desideri sia accessibile pubblicamente. Tieni presente che questa politica rende _tutti gli oggetti in un bucket_ disponibili per il download per chiunque con l'URL appropriato.
4. Seleziona **Politiche di accesso** dal menu di navigazione.
5. Seleziona la scheda **Accesso pubblico**.
6. Fai clic su **Crea politica di accesso**. Dopo che hai letto l'avvertenza, scegli **Abilita**.
7. Ora tutti gli oggetti in questo bucket sono accessibili pubblicamente.

### Disabilita l'accesso pubblico
{: #public-access-console-disable}

1. Da qualsiasi punto della {{site.data.keyword.cloud_notm}}[console](https://cloud.ibm.com/), seleziona il menu **Gestisci** e **Accesso (IAM)**.
2. Seleziona **Gruppi di accesso** dal menu di navigazione.
3. Seleziona **Accesso pubblico** per visualizzare un elenco di tutte le politiche di accesso pubblico attualmente in uso.
4. Trova la politica che corrisponde al bucket che desideri impostare nuovamente su un controllo dell'accesso imposto.
5. Dall'elenco di azioni sull'estrema destra della voce di politica, scegli **Rimuovi**.
6. Conferma la finestra di dialogo e la politica viene ora rimossa dal bucket.

## Concessione dell'accesso pubblico su singoli oggetti
{: #public-access-object}

Per rendere un oggetto pubblicamente accessibile tramite l'API REST, un'intestazione `x-amz-acl: public-read` può essere inclusa nella richiesta. L'impostazione di questa intestazione tralascia qualsiasi controllo di [politica IAM](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-iam-overview) e consente richieste `HEAD` e `GET` non autenticate. Per ulteriori informazioni sugli endpoint, vedi [Endpoint e ubicazioni di archiviazione](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

Inoltre, le [credenziali HMAC](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-hmac-signature) rendono possibile la concessione dell'[accesso pubblico temporaneo che utilizza URL pre-firmati](/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-presign-url).

### Carica un oggetto pubblico
{: #public-access-object-upload}

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
```
{: codeblock}

### Consenti l'accesso pubblico a un oggetto esistente
{: #public-access-object-existing}

L'utilizzo del parametro di query `?acl` senza un payload e dell'intestazione `x-amz-acl: public-read` consente l'accesso pubblico all'oggetto senza che sia necessario sovrascrivere i dati.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}"
```
{: codeblock}

### Come rendere nuovamente privato un oggetto pubblico
{: #public-access-object-private}

L'utilizzo del parametro di query `?acl` senza un payload e di un'intestazione `x-amz-acl:` vuota revoca l'accesso pubblico all'oggetto senza che sia necessario sovrascrivere i dati.

```sh
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}?acl" \
     -H "Authorization: Bearer {token}" \
     -H "x-amz-acl:"
```
{: codeblock}

## Siti web statici
{: #public-access-static-website}

Anche se {{site.data.keyword.cos_full_notm}} non supporta l'hosting automatico di siti web statici, è possibile configurare manualmente un server web e utilizzarlo per fornire contenuto accessibile pubblicamente che è ospitato in un bucket. Per ulteriori informazioni, vedi [questa esercitazione](https://www.ibm.com/cloud/blog/static-websites-cloud-object-storage-cos).
