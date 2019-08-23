---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: about, basics

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

# Acerca del almacenamiento de objetos
{: #about-cos}

El almacenamiento de objetos es un concepto moderno de tecnología de almacenamiento y una progresión lógica del almacenamiento en bloque y de archivos. El almacenamiento de objetos ha estado presente desde finales de la década de 1990, pero ha ganado aceptación en el mercado en los últimos 10 años.

El almacenamiento de objetos se inventó para solucionar una serie de problemas:

*  La gestión de datos a escalas muy grandes mediante los sistemas en bloque y de archivos convencionales era difícil porque estas tecnologías conducen a islas de datos debido a las limitaciones en los distintos niveles del hardware de gestión de datos y la pila de software.

*  La gestión del espacio de nombres a escala ha dado como resultado el mantenimiento de jerarquías grandes y complejas, necesarias para acceder a los datos. Las limitaciones en las estructuras anidadas en las matrices tradicionales de almacenamiento en bloque y de archivos contribuyeron aún más a la formación de islas de datos.

*  La prestación de servicios de seguridad de acceso requería una combinación de tecnologías, complejos sistemas de seguridad y una importante participación humana para gestionar estas áreas.

El almacenamiento de objetos, también conocido como almacenamiento basado en objetos (OBS), utiliza un enfoque diferente para almacenar y hacer referencia a los datos. Los conceptos de almacenamiento de datos de objetos incluyen las tres construcciones siguientes:

*  Datos: son los datos de usuario y de aplicación que requieren almacenamiento persistente. Puede ser texto, formatos binarios, multimedia, o cualquier otro contenido generado por una máquina o por personas.

*  Metadatos: son los datos sobre los datos. Incluye algunos atributos predefinidos como, por ejemplo, tiempo de carga y tamaño. El almacenamiento de objetos permite a los usuarios incluir metadatos personalizados que contienen cualquier información en pares de clave y valor. Esta información suele contener información referente al usuario o a la aplicación que almacena los datos y se puede modificar en cualquier momento. Un aspecto exclusivo del manejo de metadatos en sistemas de almacenamiento de objetos es que los metadatos se almacenan con el objeto.

*  Clave: se asigna un identificador de recurso exclusivo a cada objeto en un sistema OBS. Esta clave permite al sistema de almacenamiento de objetos diferenciar objetos entre sí y se utiliza para buscar los datos sin necesidad de conocer la unidad física exacta, la matriz o el sitio donde están los datos.

Este enfoque permite que el almacenamiento de objetos almacene datos en una jerarquía simple y plana, lo que alivia la necesidad de disponer de repositorios de metadatos de gran tamaño que afectan negativamente al rendimiento.

El acceso a los datos se consigue utilizando una interfaz REST a través del protocolo HTTP, que permite el acceso en cualquier lugar y en cualquier momento simplemente haciendo referencia a la clave de objeto.
