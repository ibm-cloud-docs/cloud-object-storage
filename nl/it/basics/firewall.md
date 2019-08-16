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

# Configurazione di un firewall
{: #setting-a-firewall}

Le politiche IAM forniscono agli amministratori un modo per limitare l'accesso ai singoli bucket. Cosa succede se si deve accedere a determinati dati unicamente dalle reti attendibili? Un firewall bucket limita tutto l'accesso ai dati a meno che la richiesta non sia originata da un elenco di indirizzi IP consentiti.
{: shortdesc}

Ci sono alcune regole per la configurazione di un firewall:

* Un utente che configura o visualizza un firewall deve disporre del ruolo di gestore (`Manager`) per il bucket. 
* Un utente con il ruolo di gestore (`Manager`) per il bucket può visualizzare e modificare l'elenco degli indirizzi IP consentiti da qualsiasi indirizzo IP per impedire blocchi accidentali. 
* La console di {{site.data.keyword.cos_short}} può ancora accedere al bucket, se l'indirizzo IP dell'utente è autorizzato.
* Altri servizi {{site.data.keyword.cloud_notm}} **non sono autorizzati** a ignorare il firewall. Questa limitazione significa che gli altri servizi che si basano sulle politiche IAM per l'accesso bucket (ad esempio Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions e altri) non saranno in grado di farlo. 

Quando un firewall è configurato, il bucket viene isolato dal resto di {{site.data.keyword.cloud_notm}}. Valuta in che modo ciò può influire sulle applicazioni e sui flussi di lavoro che dipendono da altri servizi che accedono direttamente a un bucket prima di abilitare il firewall.
{: important}

## Utilizzo della console per configurare un firewall
{: #firewall-console}

Innanzitutto, assicurati di avere un bucket. In caso negativo, segui l'[esercitazione introduttiva](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) per acquisire familiarità con la console.

### Configura un elenco di indirizzi IP autorizzati
{: #firewall-console-enable}

1. Dal [dashboard della console](https://cloud.ibm.com/) di {{site.data.keyword.cloud_notm}}, seleziona **Archiviazione** per visualizzare il tuo elenco di risorse. 
2. Poi, seleziona l'istanza del servizio con il tuo bucket dal menu **Archiviazione**. Questa opzione ti porta alla console di {{site.data.keyword.cos_short}}.
3. Scegli il bucket a cui desideri limitare l'accesso degli indirizzi IP autorizzati.  
4. Seleziona **Politiche di accesso** dal menu di navigazione.
5. Seleziona la scheda **IP autorizzati**.
6. Fai clic su **Aggiungi indirizzi IP**, quindi scegli **Aggiungi**.
7. Aggiungi un elenco di indirizzi IP nella [notazione CIDR](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing), ad esempio `192.168.0.0/16, fe80:021b::0/64`. Gli indirizzi possono seguire gli standard IPv4 o IPv6. Fai clic su **Aggiungi**.
8. Il firewall non verrà applicato fino a quando l'indirizzo non viene salvato nella console. Fai clic su **Salva tutto** per applicare il firewall.
9. Ora tutti gli oggetti presenti in questo bucket sono accessibili solo da questi indirizzi IP!

### Rimuovi le limitazioni dell'indirizzo IP
{: #firewalls-console-disable}

1. Dalla scheda **IP autorizzati**, seleziona le caselle accanto agli intervalli o agli indirizzi IP per rimuoverli dall'elenco autorizzato. 
2. Seleziona **Elimina** e poi conferma la finestra di dialogo facendo di nuovo clic su **Elimina**.
3. L'elenco aggiornato non verrà applicato fino a quando le modifiche non vengono salvate nella console. Fai clic su **Salva tutto** per applicare le nuove regole.
4. Ora tutti gli oggetti presenti in questo bucket sono accessibili solo da questi indirizzi IP!

Se non ci sono indirizzi IP autorizzati elencati, significa che al bucket verranno applicate le normali politiche IAM, senza limitazioni per l'indirizzo IP dell'utente.
{: note}


## Configura un firewall tramite un'API
{: #firewall-api}

I firewall vengono gestiti con l'[API di configurazione delle risorse COS](https://cloud.ibm.com/apidocs/cos/cos-configuration). Questa nuova API REST viene utilizzata per configurare i bucket.  

Gli utenti con il ruolo di gestore (`manager`) possono visualizzare e modificare l'elenco degli indirizzi IP consentiti da qualsiasi rete per impedire blocchi accidentali.
{: tip}
