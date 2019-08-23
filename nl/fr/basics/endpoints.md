---

copyright:
  years: 2017
lastupdated: "2018-05-25"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}

# Noeuds finaux et emplacements de stockage
{: #endpoints}

L'envoi d'une demande d'API REST ou la configuration d'un client de stockage nécessite de définir un noeud final cible ou une URL. Chaque emplacement de stockage possède son propre ensemble d'adresses URL.

La plupart des utilisateurs doivent utiliser l'un des noeuds finaux décrits ci-après pour un emplacement de stockage donné. Les noeuds finaux privés doivent être utilisés à partir d'IBM Cloud et cela n'entraîne pas de frais liés au transfert de données. Les noeuds finaux publics doivent être utilisés en dehors d'IBM Cloud et cela entraîne des frais liés au transfert de données. Il est recommandé d'utiliser un noeud final privé, dans la mesure du possible. 

Nous avons mis à jour nos noeuds finaux en décembre 2018. Les [noeuds finaux existants](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) continueront de fonctionner jusqu'à nouvel ordre. Mettez à jour vos applications pour qu'elles utilisent les nouveaux noeuds finaux répertoriés ici.{:note}

## Noeuds finaux régionaux
{: #endpoints-region}

Des compartiments créés sur un noeud final régional distribuent des données dans trois centres de données répartis dans une région métropolitaine. L'un de ces centres de données peut être indisponible ou même détruit sans que cela ne vienne mettre en péril la disponibilité des données.

<table>
  <thead>
    <tr>
      <th>Région</th>
      <th>Type</th>
      <th>Noeud final</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Sud des Etats-Unis</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us-south.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us-south.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Est des Etats-Unis</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us-east.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us-east.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Union européenne - Royaume-Uni</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-gb.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu-gb.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Union européenne - Allemagne</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu-de.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu-de.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Asie-Pacifique - Australie</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.au-syd.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.au-syd.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Asie-Pacifique - Japon</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.jp-tok.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.jp-tok.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

## Noeuds finaux inter-régionaux
{: #endpoints-geo}

Les compartiments créés au niveau d'un noeud final inter-régional distribuent des données dans trois régions. L'une de ces régions peut être indisponible ou même détruite sans que cela ne vienne mettre en péril la disponibilité des données. Les demandes sont acheminées vers le centre de données de la région la plus proche à l'aide du routage BGP (Border Gateway Protocol). En cas d'indisponibilité, les demandes sont automatiquement réacheminées vers une région active. Les utilisateurs expérimentés qui souhaitent écrire leur propre logique de basculement peuvent le faire en envoyant des demandes à un [point d'accès spécifique](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) et en ignorant le routage BGP.

<table>
  <thead>
    <tr>
      <th>Région</th>
      <th>Type</th>
      <th>Noeud final</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Inter-régional pour les Etats-Unis</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.us.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.us.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Inter-régional pour l'Union européenne</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.eu.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Inter-régional pour l'Asie-Pacifique</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ap.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}



## Noeuds finaux de centre de données unique
{: #endpoints-zone}

Les centres de données uniques ne sont pas colocalisés avec les services IBM Cloud, tels que IAM ou Key Protect, et n'offrent pas de résilience en cas d'indisponibilité ou de destruction d'un site.  

Si en raison d'une panne de réseau, le centre de données d'une partition ne peut pas atteindre une région IBM Cloud centrale pour accéder à IAM, les informations d'authentification et d'autorisation sont lues à partir d'une cache qui peut devenir périmé. Cela peut entraîner une absence d'application des règles IAM nouvelles ou modifiées pendant une période pouvant aller jusqu'à 24 heures.{:important}

<table>
  <thead>
    <tr>
      <th>Région</th>
      <th>Type</th>
      <th>Noeud final</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Amsterdam, Pays-Bas</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.ams03.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.ams03.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Chennai, Inde</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.che01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.che01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Hong Kong</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.hkg02.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.hkg02.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Melbourne, Australie</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mel01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mel01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Mexico, Mexique</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mex01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mex01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Milan, Italie</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mil01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mil01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Montréal, Canada</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.mon01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.mon01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Oslo, Norvège</td>
      <td>Public</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.osl01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.osl01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>San José, Etats-Unis</td>
      <td>Public</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>São Paulo, Brésil</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sao01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sao01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Séoul, Corée du Sud</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.seo01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.seo01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
    <tr>
      <td>Toronto, Canada</td>
      <td>
        <p>Public</p>
        <p>Privé</p>
      </td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.tor01.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.tor01.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr>
  </tbody>
</table>
{:.endpointtable}

