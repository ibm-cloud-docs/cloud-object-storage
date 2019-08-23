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

# Configuración de un cortafuegos
{: #setting-a-firewall}

Las políticas IAM proporcionan un modo de que los administradores limiten el acceso a grupos individuales. ¿Qué ocurre si solo se debe acceder a determinados datos desde redes de confianza? Un cortafuegos de grupo restringe todo el acceso a los datos a menos que la solicitud proceda de una lista de direcciones IP permitidas.
{: shortdesc}

Hay algunas reglas relacionadas con la configuración de un cortafuegos:

* Un usuario que configura o que visualiza un cortafuegos debe tener el rol de `Gestor` en el grupo. 
* Un usuario con el rol de `Gestor` sobre el grupo puede ver y editar la lista de direcciones IP permitidas desde cualquier dirección IP para evitar bloqueos accidentales.
* La consola de {{site.data.keyword.cos_short}} puede seguir accediendo al grupo, siempre y cuando la dirección IP del usuario tenga autorización.
* Otros servicios de {{site.data.keyword.cloud_notm}} **no tienen autorización** para eludir el cortafuegos. Esta limitación significa que otros servicios que se basan en políticas de IAM para el acceso al grupo (como Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions y otros) no podrán hacerlo.

Cuando se establece un cortafuegos, el grupo queda aislado del resto de {{site.data.keyword.cloud_notm}}. Tenga en cuenta la forma en que esto puede afectar a las aplicaciones y a los flujos de trabajo que dependen de otros servicios que acceden directamente a un grupo antes de habilitar el cortafuegos.
{: important}

## Utilización de la consola para establecer un cortafuegos
{: #firewall-console}

En primer lugar, asegúrese de que tiene un grupo. Si no es así, siga la [guía de aprendizaje de iniciación](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started) para familiarizarse con la consola.

### Establecimiento de una lista de direcciones IP autorizadas
{: #firewall-console-enable}

1. En el {{site.data.keyword.cloud_notm}}[panel de control de la consola](https://cloud.ibm.com/), seleccione **Almacenamiento** para ver la lista de recursos.
2. A continuación, seleccione la instancia de servicio con el grupo desde el menú **Almacenamiento**. Esto le lleva a la consola de {{site.data.keyword.cos_short}}.
3. Elija el grupo al que desea limitar el acceso a las direcciones IP autorizadas. 
4. Seleccione **Políticas de acceso** en el menú de navegación.
5. Seleccione el separador **IP autorizadas**.
6. Pulse **Añadir direcciones IP** y luego seleccione **Añadir**.
7. Añada una lista de direcciones IP en [notación CIDR](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing), por ejemplo `192.168.0.0/16, fe80:021b::0/64`. Las direcciones pueden seguir los estándares IPv4 o IPv6. Pulse
**Añadir**.
8. El cortafuegos no se aplicará hasta que se guarde la dirección en la consola. Pulse **Guardar todo** para aplicar el cortafuegos.
9. Ahora solo se podrá acceder a todos los objetos de este grupo desde estas direcciones IP.

### Eliminación de cualquier restricción de dirección IP
{: #firewalls-console-disable}

1. En el separador **IP autorizadas**, marque los recuadros que hay junto a las direcciones IP o rangos que se deben eliminar de la lista autorizada.
2. Seleccione **Suprimir** y luego confirme el recuadro de diálogo pulsando de nuevo **Suprimir**.
3. La lista actualizada no se aplicará hasta que los cambios se guarden en la consola. Pulse **Guardar todo** para aplicar las nuevas reglas.
4. Ahora solo se podrá acceder a todos los objetos de este grupo desde estas direcciones IP.

Si la lista no contiene ninguna dirección IP autorizada, significa que se aplicarán las políticas IAM normales al grupo, sin restricciones en cuanto a la dirección IP del usuario. 
{: note}


## Establecimiento de un cortafuegos mediante una API
{: #firewall-api}

Los cortafuegos se gestionan con la [API de configuración de recursos de COS](https://cloud.ibm.com/apidocs/cos/cos-configuration). Esta nueva API REST se utiliza para configurar grupos. 

Los usuarios con el rol de `Gestor` pueden ver y editar la lista de direcciones IP permitidas desde cualquier red con el fin de evitar bloqueos accidentales.
{: tip}
