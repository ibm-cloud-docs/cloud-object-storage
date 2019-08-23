---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: gui, desktop, crossftp

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


# Transferencia de archivos mediante CrossFTP
{: #crossftp}

[CrossFTP](http://www.crossftp.com/){:new_window} es un cliente FTP con todas las prestaciones que da soporte a soluciones de almacenamiento en la nube compatibles con S3, incluido {{site.data.keyword.cos_full}}. CrossFTP da soporte a Mac OS X, Microsoft Windows, Linux y se ofrece en las versiones gratuita, Pro, y Enterprise con prestaciones como:

* Interfaz con separadores
* Cifrado de contraseña
* Búsqueda
* Transferencia por lotes
* Cifrado (*versiones Pro/Enterprise*)
* Sincronización (*versiones Pro/Enterprise*)
* Planificador (*versiones Pro/Enterprise*)
* Interfaz de línea de mandatos (*versiones Pro/Enterprise*)

## Conexión con IBM Cloud Object Storage
{: #crossftp-connect}

1. Descargue, instale e inicie CrossFTP.
2. En el panel de la derecha, cree un sitio nuevo pulsando el icono más (+) para abrir Site Manager.
3. En el separador *General*, especifique lo siguiente:
    * Establezca **Protocolo** en `S3/HTTPS`
    * Establezca **Etiqueta** en el nombre descriptivo que elija
    * Establezca **Host** en el punto final de {{site.data.keyword.cos_short}} (es decir, `s3.us.cloud-object-storage.appdomain.cloud`)
        * *Asegúrese de que la región de punto final coincida con el grupo de destino previsto. Para obtener más información sobre puntos finales, consulte [Puntos finales y ubicaciones de almacenamiento](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).*
    * Deje el **Puerto** `443`
    * Establezca **Clave de acceso** y **Secreto** en las credenciales de HMAC con los derechos de acceso adecuados para el grupo de destino
4. En el separador *S3*
    * Asegúrese de que `Utilizar DevPay` no esté marcado
    * Pulse **Conjunto de API ...** y asegúrese de que `Dev Pay` y `CloudFront Distribution` no están marcados
5. ***Solo para Mac OS X***
    * Pulse *Seguridad > Protocolos TLS/SSL...* en la barra de menús
    * Seleccione la opción `Personalizar los protocolos habilitados`
    * Añada `TLSv1.2` al recuadro **Habilitado**
    * Pulse **Aceptar**
6. ***Solo para Linux***
    * Pulse *Seguridad > Valores de cifrado...* en la barra de menús
    * Seleccione la opción `Personalizar las suites de cifrado habilitadas`
    * Añada `TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA` al recuadro **Habilitado**
    * Pulse **Aceptar**
7. Pulse **Aplicar** y luego **Cerrar**
8. Debería haber una nueva entrada bajo *Sitios* con la *Etiqueta* especificada en el paso 3
9. Efectúe una doble pulsación en la nueva entrada para conectar con el punto final

Desde aquí, la ventana muestra una lista de los grupos disponibles y puede examinar los archivos disponibles y transferirlos a y desde los discos locales.
