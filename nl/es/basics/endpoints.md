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

# Puntos finales y ubicaciones de almacenamiento
{: #endpoints}

Para enviar una solicitud de API REST o para configurar un cliente de almacenamiento es necesario establecer un punto final de destino o un URL. Cada ubicación de almacenamiento tiene su propio conjunto de URL.

La mayoría de los usuarios deben utilizar uno de los puntos finales siguientes para una ubicación de almacenamiento determinada. Los puntos finales privados se deben utilizar desde dentro de IBM Cloud y no incurren en cargos de transferencia de datos. Los puntos finales públicos se deben utilizar desde fuera de IBM Cloud e incurren en cargos de transferencia. Si es posible, se recomienda utilizar un punto final privado.

A partir de diciembre de 2018, hemos actualizado nuestros puntos finales. Los [puntos finales antiguos](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) seguirán funcionando hasta nuevo aviso. Actualice sus aplicaciones para que utilicen los nuevos puntos finales que se muestran aquí.
{:note}

## Puntos finales regionales
{: #endpoints-region}

Los grupos creados en un punto final regional distribuyen los datos entre tres centros de datos distribuidos en un área metropolitana. Cualquiera de estos centros de datos puede sufrir una interrupción o incluso puede ser destruido sin que ello afecte a la disponibilidad.

<table>
  <thead>
    <tr>
      <th>Región</th>
      <th>Tipo</th>
      <th>Punto final</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>EE. UU. sur</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>EE. UU. este</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>UE Reino Unido</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>UE Alemania</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>AP Australia</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>AP Japón</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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

## Puntos finales de varias regiones
{: #endpoints-geo}

Los grupos credos en un punto final de varias regiones distribuyen los datos entre tres regiones. Cualquiera de estas regiones puede sufrir una interrupción o incluso puede ser destruida sin que ello afecte a la disponibilidad. Las solicitudes se direccionan al centro de datos de la región más cercana mediante el direccionamiento BGP (Border Gateway Protocolo). En el caso de que se produzca una interrupción, las solicitudes se redireccionan automáticamente a una región activa. Los usuarios avanzados que deseen escribir su propia lógica de migración tras error pueden hacerlo enviando solicitudes a un [punto de acceso específico](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) y pasando por alto el direccionamiento de BGP.

<table>
  <thead>
    <tr>
      <th>Región</th>
      <th>Tipo</th>
      <th>Punto final</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>EE. UU. de varias regiones</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>UE de varias regiones</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>AP de varias regiones</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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



## Puntos finales de un solo centro de datos
{: #endpoints-zone}

Los centros de datos únicos no están coubicados con los servicios de IBM Cloud, como IAM o Key Protect, y no ofrecen resiliencia en el caso de que se produzca una interrupción del sitio o que resulte destruido. 

Si una anomalía en la red da como resultado que en una partición el centro de datos no puede acceder a una región de IBM Cloud de núcleo para acceder a IAM, la información de autenticación y autorización se lee desde una memoria caché que puede pasar a ser obsoleta. Esto puede hacer que no se impongan las políticas de IAM nuevas o modificadas durante un periodo de tiempo que puede alcanzar las 24 horas.
{:important}

<table>
  <thead>
    <tr>
      <th>Región</th>
      <th>Tipo</th>
      <th>Punto final</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Amsterdam, Países Bajos</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>Chennai, India</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
        <p>Público</p>
        <p>Privado</p>
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
      <td>Melbourne, Australia</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>Ciudad de México, México</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>Milán, Italia</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>Montreal, Canadá</td>
      <td>
        <p>Público</p>
        <p>Privado</p>
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
      <td>Oslo, Noruega</td>
      <td>Público</td>
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
      <td>San José, EE. UU.</td>
      <td>Público</td>
      <td>
        <p>
          <code class="highlighter-rouge">s3.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
        <p>
          <code class="highlighter-rouge">s3.private.sjc04.cloud-object-storage.appdomain.cloud</code>
        </p>
      </td>
    </tr> 
      <td>São Paulo, Brasil</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>Seúl, Corea del Sur</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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
      <td>Toronto, Canadá</td>
      <td>
        <p>Público</p>
        <p>Privado
        </p>
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

