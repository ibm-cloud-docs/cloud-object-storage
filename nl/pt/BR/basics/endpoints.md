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

# Terminais e locais de armazenamento
{: #endpoints}

O envio de uma solicitação de API de REST ou a configuração de um cliente de armazenamento requer a configuração de um terminal de destino ou URL. Cada local de armazenamento tem seu próprio conjunto de URLs.

A maioria dos usuários deve usar um dos terminais a seguir para um determinado local de armazenamento. Os terminais privados devem ser usados de dentro da nuvem IBM e não incorrem em encargos de transferência de dados. Os terminais públicos devem ser usados de fora da nuvem IBM e incorrem em encargos de transferência. Se possível, é recomendado usar um terminal privado.

A partir de dezembro de 2018, nós atualizamos nossos terminais. Os [terminais anteriores](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) continuarão a funcionar até novo aviso. Atualize seus aplicativos para usar os novos terminais listados aqui.
{:note}

## Terminais regionais
{: #endpoints-region}

Os depósitos criados em um terminal regional distribuem dados em três data centers difundidos em uma área metropolitana. Qualquer um desses data centers pode sofrer uma indisponibilidade ou até mesmo a destruição sem afetar a disponibilidade.

<table>
  <thead>
    <tr>
      <th>Região</th>
      <th>Tipo</th>
      <th>Ponto de Extremidade</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Sul dos EUA</td>
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
      <td>Leste dos EUA</td>
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
      <td>Reino Unido UE</td>
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
      <td>Alemanha UE</td>
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
      <td>Austrália AP</td>
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
      <td>Japão AP</td>
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

## Terminais de região cruzada
{: #endpoints-geo}

Os depósitos criados em um terminal de região cruzada distribuem dados em três regiões. Qualquer uma dessas regiões pode sofrer uma indisponibilidade ou até mesmo a destruição sem afetar a disponibilidade. As solicitações são roteadas para o data center da região mais próxima usando o roteamento do Protocolo de Roteamento de Borda (BGP). No caso de uma indisponibilidade, as solicitações são roteadas novamente de forma automática para uma região ativa. Os usuários avançados que desejam escrever sua própria lógica de failover podem fazer isso enviando solicitações para um [ponto de acesso específico](/docs/services/cloud-object-storage/info?topic=cloud-object-storage-advanced-endpoints) e efetuando bypass do roteamento do BGP.

<table>
  <thead>
    <tr>
      <th>Região</th>
      <th>Tipo</th>
      <th>Ponto de Extremidade</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Região cruzada dos EUA</td>
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
      <td>Região cruzada da UE</td>
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
      <td>Região cruzada da AP</td>
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



## Terminais de Data center único
{: #endpoints-zone}

Os data centers únicos não são localizados concomitantemente com os serviços do IBM Cloud, como o IAM ou o Key Protect, e não oferecem nenhuma resiliência no caso de uma indisponibilidade ou destruição de site. 

Se uma falha de rede resultar em uma partição na qual o data center não consegue atingir uma região principal do IBM Cloud para acessar o IAM, as informações de autenticação e autorização serão lidas de um cache que pode se tornar antigo. Isso pode resultar em uma falta de cumprimento de políticas do IAM novas ou alteradas por até 24 horas.
{:important}

<table>
  <thead>
    <tr>
      <th>Região</th>
      <th>Tipo</th>
      <th>Ponto de Extremidade</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Amsterdã, Holanda</td>
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
      <td>Chennai, Índia</td>
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
      <td>Melbourne, Austrália</td>
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
      <td>Cidade do México, México</td>
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
      <td>Milão, Itália</td>
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
      <td>San Jose, EUA</td>
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
      <td>Seul, Coreia do Sul</td>
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

