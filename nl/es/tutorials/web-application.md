---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-08"

keywords: tutorial, web application, photo galleries

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

# Guía de aprendizaje: Aplicación web de galería de imágenes
{: #web-application}

De principio a fin, la creación de una aplicación web cubre un montón de conceptos diferentes y es una buena forma de comenzar a conocer las prestaciones de {{site.data.keyword.cos_full}}. Esta guía de aprendizaje le mostrará cómo crear una galería de imágenes sencilla en {{site.data.keyword.cloud}} Platform y cómo reunir muchos conceptos y prácticas diferentes. La aplicación utilizará {{site.data.keyword.cos_full_notm}} como servidor de fondo para una aplicación
Node.js que permitirá al usuario cargar y ver archivos de imágenes JPEG.

## Antes de empezar
{: #wa-prereqs}

Como requisitos previos para crear una aplicación web, comenzaremos con lo siguiente:

  - Cuenta de {{site.data.keyword.cloud_notm}} Platform
  - Docker, como parte de {{site.data.keyword.cloud_notm}} Developer Tools
  - Node.js 
  - Git (tanto app de escritorio como CLI de interfaz de línea de mandatos)

### Instalación de Docker
{: #tutorial-wa-install-docker}

El paso de crear aplicaciones web con instancias de servidor tradicionales o incluso servidores virtuales a utilizar contenedores, como por ejemplo Docker, acelera el desarrollo y facilita las pruebas al tiempo que permite un despliegue automatizado. Un contenedor es una estructura ligera que no necesita sobrecarga adicional, como un sistema operativo; solo el código y la configuración para todo, desde dependencias hasta configuraciones.

Empezaremos por abrir una herramienta bien conocida para los desarrolladores experimentados, y un nuevo mejor amigo para aquellos que acaban de empezar: la línea de mandatos. Desde que se inventó la interfaz gráfica de usuario (GUI), la interfaz de línea de mandatos del sistema ha sido relegada a una segunda clase. Pero ahora es el momento de recuperarla (aunque la GUI no se va a ir por ahora, especialmente cuando necesitemos navegar por la web para descargar nuestro nuevo conjunto de herramientas de línea de mandatos). 

Abra el terminal, u otra interfaz de línea de mandatos adecuada para el sistema operativo, y cree un directorio con los mandatos apropiados para el shell concreto que esté utilizando. Cambie su propio directorio de referencia por el nuevo que acaba de crear. Cuando se cree, la aplicación tendrá su propio subdirectorio dentro de este, que contendrá el código de inicio y la configuración necesaria para ponerse en marcha.

Dejando la línea de mandatos y volviendo al navegador, siga las instrucciones para instalar las [herramientas del desarrollador de {{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) del enlace. 
Las herramientas del desarrollador ofrecen un enfoque ampliable y repetible para crear y desplegar aplicaciones de nube.

[Docker](https://www.docker.com) se instala como parte de Developer Tools, y lo necesitaremos, a pesar de funcionará mayormente como programa de fondo, dentro de las rutinas que generan la nueva app. Docker debe estar en ejecución para que los mandatos de compilación funcionen. Para continuar, cree una cuenta de Docker en línea en [Dockerhub](https://hub.docker.com), ejecute la app Docker y regístrese.

### Instalación de Node.js
{: #tutorial-wa-install-node}

La app que va a crear utilizará [Node.js](https://nodejs.org/) como motor del lado del servidor para ejecutar el código JavaScript para esta aplicación web. Para poder utilizar Node Package Manager (npm) incluido en Node para gestionar las dependencias de la app, debe instalar Node.js localmente. Además, el hecho de instalar Node.js localmente simplifica el proceso de prueba y agiliza el desarrollo. 

Antes de empezar, tenga en cuenta la posibilidad de utilizar un gestor de versiones, como Node Version Manager o `nvm`, para instalar Node, lo que reduce la complejidad de gestionar varias versiones de Node.js. En el momento de escribir esta documentación, para instalar o actualizar `nvm` en una máquina Mac o Linux, puede utilizar el script de instalación mediante cURL en la interfaz de CLI que acaba de abrir copiando y pegando uno de los mandatos de los dos primeros ejemplos en la línea de mandatos y pulsando Intro (tenga en cuenta que se supone que el shell es BASH, no uno alternativo):

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Ejemplo 1. Utilización de cURL para instalar Node Version Manager (nvm)" caption-side="bottom"}
`Ejemplo 1. Utilización de cURL para instalar Node Version Manager (nvm)`
   
...o Wget (solo se necesita uno, no ambos; utilice el que esté disponible en su sistema):

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Ejemplo 2. Utilización de Wget para instalar Node Version Manager (nvm)" caption-side="bottom"}
`Ejemplo 2. Utilización de Wget para instalar Node Version Manager (nvm)`

O bien, para Windows, puede utilizar [nvm para Windows](https://github.com/coreybutler/nvm-windows) con los instaladores y el código fuente del enlace.

Si no desea la complejidad añadida de dar soporte a varios releases de Node.js, visite el sitio web de [Node.js](https://nodejs.org/en/download/releases/) e instale la versión de soporte a largo plazo (LTS) de Node.js que coincida con la versión más reciente soportada por el paquete de compilación de SDK para Node.js que ahora se utiliza en {{site.data.keyword.cloud_notm}} Platform. En el momento de escribir esta documentación, el paquete de compilación más reciente es v3.26, y da soporte a la edición de la comunidad de Node.js v6.17.0+. 

Encontrará más información sobre el último SDK de {{site.data.keyword.cloud_notm}} para el paquete de compilación Node.js en la página [Actualizaciones más recientes de SDK para Nodejs](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates). 

Mediante `nvm` podría instalar la versión de Node que se ajuste a los requisitos copiando y pegando el mandato del Ejemplo 3 en su línea de mandatos.

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="Ejemplo 3. Utilización de `nvm` para instalar una versión específica de Node.js" caption-side="bottom"}
`Ejemplo 3. Utilización de nvm para instalar una versión específica de Node.js`

Sea cual sea el enfoque que utilice, una vez que haya seguido las instrucciones para instalar Node.js y npm (incluido con Node) en su sistema, según proceda para el sistema operativo y la estrategia que utilice, felicítese por haber realizado un buen trabajo.

### Instalación de Git
{: #tutorial-wa-install-git}

Es probable que ya esté familiarizado con Git, ya que es el sistema de mantenimiento de versiones de código fuente más utilizado entre los desarrolladores que crean aplicaciones para la web. 
Utilizaremos Git más tarde cuando creamos una cadena de herramientas de despliegue continuo (CD) en {{site.data.keyword.cloud_notm}} Platform para la entrega y el despliegue continuos. Si no tiene una cuenta de GitHub, cree una cuenta personal gratuita en el sitio web de [Github](https://github.com/join); de lo contrario, inicie una sesión con la cuenta que tenga.

Tenga en cuenta que hay instrucciones paso a paso importantes sobre cómo generar y cargar claves SSH en el [perfil de Github](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) para obtener acceso seguro a Github desde la línea de mandatos. Sin embargo, si lo hace ahora, irá adquiriendo práctica, ya que tendrá que repetir los pasos para la instancia de Github que ha utilizado para {{site.data.keyword.cloud_notm}} Platform, a la que accederemos más adelante. Aunque los pasos para utilizar claves de SSH pueden ser complicados, a base de práctica conseguirá trabajar con fluidez con SSH en la CLI.

Por ahora, vaya a la página de [Github Desktop](https://desktop.github.com/) para descargar GitHub Desktop y, a continuación, ejecute el instalador. Cuando finalice el instalador, se le solicitará que inicie una sesión en GitHub con su cuenta.

En la ventana de inicio de sesión (consulte la primera figura de esta guía de aprendizaje), especifique el nombre y el correo electrónico que desea mostrar públicamente (suponiendo que tenga una cuenta pública) para cualquier confirmación en su repositorio. Cuando haya enlazado la aplicación a su cuenta, se le solicitará que verifique la conexión de la aplicación a través de la cuenta de Github en línea.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)

Aún no tiene que crear ningún repositorio. Si observa un repositorio llamado Tutorial que se incluye con GitHub Desktop, experimente con el mismo tanto como quiera para familiarizarse con las operaciones. Acaba de completar la parte de requisito previo de esta guía de aprendizaje. ¿Está listo para crear una app?

## Creación de la app de inicio de Node.js mediante Developer Tools
{: #tutorial-create-skeleton}

Para empezar a desarrollar la aplicación localmente, comience iniciando una sesión en {{site.data.keyword.cloud_notm}} Platform directamente desde la línea de mandatos, tal como se muestra en el ejemplo 4. 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="Ejemplo 4. Mandato para iniciar una sesión en IBM Cloud Platform mediante CLI Developer Tools" caption-side="bottom"}
`Ejemplo 4. Mandato para iniciar una sesión en IBM Cloud Platform mediante CLI Developer Tools`

Puede especificar parámetros opcionales si lo desea: su organización con la opción -o y el espacio con la opción -s, o, si utiliza una cuenta federada: --sso. Cuando inicia la sesión, es posible que se le pida que elija una región; en este ejercicio seleccione `us-south` como región, ya que se utilizará la misma opción cuando se cree una cadena de herramientas de CD más adelante en esta guía de aprendizaje.  

A continuación, establezca el punto final (si no se ha establecido ya) con el mandato que se muestra en el Ejemplo 5. Otros puntos finales son posibles, y pueden resultar preferibles para uso de producción, pero, por ahora, use el código tal como se muestra, si es adecuado para su cuenta.

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="Ejemplo 5. Mandato para establecer el punto final de API para la cuenta." caption-side="bottom"}
`Ejemplo 5. Mandato para establecer el punto final de API para la cuenta`

Elija como destino Cloud Foundry (cf) de {{site.data.keyword.cloud_notm}} Platform con el código que se muestra en el Ejemplo 6, utilizando el mandato target y la opción --cf. La API `cf` está incluida en CLI Developer Tools.

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="Ejemplo 6. Establecimiento de opciones para utilizar la API de Cloud Foundry." caption-side="bottom"}
`Ejemplo 6. Establecimiento de opciones para utilizar la API de Cloud Foundry`

Y ahora ha llegado al objetivo de esta guía: la creación de una aplicación web comienza con el código que se muestra en el Ejemplo 7. El espacio `dev` es una opción predeterminada para su organización, pero tal vez desee crear otras para aislar diferentes asuntos; por ejemplo, puede mantener 'finance' separado de 'development'.

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="Ejemplo 7. Mandato para crear una app mediante IBM Cloud Developer Tools" caption-side="bottom"}
`Ejemplo 7. Mandato para crear una app mediante IBM Cloud Developer Tools`

Con ese mandato, se le harán una serie de preguntas. Puede retroceder en muchos puntos del proceso, pero, si cree que se ha perdido o que le falta algún paso, empiece de nuevo suprimiendo el directorio o creando otro para prueba y exploración. Además, cuando complete el proceso de creación de la aplicación localmente en la línea de mandatos, podrá ver los resultados en línea más adelante, en el portal en línea de {{site.data.keyword.cloud_notm}} donde ha creado la cuenta para gestionar los recursos que ha creado.

En el Ejemplo 8, observe la opción para crear una 'App web'; es la app que desea. Escriba '2' y pulse Intro.

```
                                        
--------------------------------------------------------------------------------
Seleccione un tipo de aplicación:
--------------------------------------------------------------------------------
 1. App en blanco
 2. Servicio de fondo / App web
 3. App móvil
--------------------------------------------------------------------------------
 0. Salir
--------------------------------------------------------------------------------
? Especifique el número de selección:> 2


```
{: caption="Ejemplo 8. Salida del mandato `ibmcloud dev create` en el que ha seleccionado la opción 2, correspondiente a App web" caption-side="bottom"}
`Ejemplo 8. Salida del mandato ibmcloud dev create en el que ha seleccionado la opción 2, correspondiente a App web`

Hay varias opciones en el Ejemplo 9, que dependen de lo que llamamos "buildpacks"; observe la opción para utilizar 'Node'. Escriba '4' y pulse Intro.

```

--------------------------------------------------------------------------------
Seleccione un lenguaje:
--------------------------------------------------------------------------------
 1. Go
 2. Java - MicroProfile / Java EE
 3. Java - Spring
 4. Node
 5. Python - Django
 6. Python - Flask
 7. Scala
 8. Swift
--------------------------------------------------------------------------------
 0. Volver a la selección anterior
--------------------------------------------------------------------------------
? Especifique el número de selección:> 4


```
{: caption="Ejemplo 9. Opciones de lenguaje de `ibmcloud dev create`, continuación." caption-side="bottom"}
`Ejemplo 9. Opciones de lenguaje de ibmcloud dev create, continuación`

Después de elegir el lenguaje de programación y/o la infraestructura, la siguiente selección que se muestra en el Ejemplo 10 tendrá tantas opciones que quizás tenga que desplazarse hasta el servicio que desee. Como puede ver en el ejemplo, queremos utilizar una app web Node.js simple con Express.js. Escriba '6' y pulse Intro.

```
? Seleccione un kit de inicio:

--------------------------------------------------------------------------------
APPSERVICE
--------------------------------------------------------------------------------
 1. MEAN Stack: MongoDb, Express.js, Angular, Node.js - Un proyecto de inicio
    para configurar una aplicación mongodb, express, angular y node
 2. MERN Stack: MongoDb, Express.js, React, Node.js - Un proyecto de inicio
    para configurar una aplicación mongodb, express, react y node
 3. Ejemplo de Node.js BFF con Express.js - Un iniciador para crear API de fondo y
    frontal en Node.js, mediante la infraestructura Express.js.
 4. App sin servidor de ejemplo de Node.js - Un iniciador que ofrece un conjunto de
    Cloud Functions y API para el proceso de fondo sin servidor que utiliza la base de datos
    Cloudant NoSQL.
 5. Node.js Microservice con Express.js - Un iniciador para crear un servicio de fondo de
    microservicio en Node.js, mediante la infraestructura Express.js.
 6. App web Node.js con Express.js - Un iniciador que ofrece una aplicación de servicio web
    básica en Node.js, mediante la infraestructura Express.js.
 7. App web Node.js con Express.js y React - Un iniciador que ofrece un potente servicio
    de fondo React proporcionado desde una aplicación Node.js,
    que incluye las herramientas clave de desarrollo web Gulp, SaaS y Webpack, mediante la
    infraestructura Express.js.

--------------------------------------------------------------------------------
FINANCE
--------------------------------------------------------------------------------
 8. Wealth Management Chatbot - Un chatbot que permite al usuario consultar el estado
    de sus inversiones y evaluar el impacto de distintos escenarios de mercado sobre     su conjunto de inversiones. Se puede ampliar fácilmente de diversas
    formas.

--------------------------------------------------------------------------------
WATSON
--------------------------------------------------------------------------------
 9. Watson Assistant Basic - Aplicación sencilla que muestra el servicio
    Watson Assistant en una interfaz de chat que simula tareas bancarias.
10. Watson Natural Language Understanding Basic - Colección de API
    que analizan texto para ayudarle a comprender sus conceptos, entidades,
    palabras clave y sentimientos y que permite crear un modelo personalizado ara algunas API a fin
    de crear resultados específicos adaptados a su dominio.
11. Watson News Intelligence - Este kit de inicio muestra cómo consultar nuevos conceptos
    para comprender lo que la gente opina o siente acerca de
    temas importantes.
12. Watson Speech to Text Basic - Ejemplo básico del servicio Speech to Text
    para convertir audio en diversos idiomas en texto.
13. Watson Text to Speech Basic - Ejemplo básico sobre cómo utilizar Text to
    Speech para convertir en streaming y sintetizar con baja latencia texto en audio.
14. Watson Visual Recognition Basic - Utilización de algoritmos de deep learning para analizar
    imágenes que pueden ofrecerle detalles del contenido visual.
--------------------------------------------------------------------------------
 0. Volver a la selección anterior
--------------------------------------------------------------------------------
? Especifique el número de selección:> 6

```
{: caption="Ejemplo 10. Opciones del esqueleto de la aplicación de `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 10. Opciones del esqueleto de la aplicación de ibmcloud dev create`

Ahora que ha elegido las opciones más sencillas, todavía tiene que elegir la opción más difícil para los desarrolladores de todo el mundo: asignar un nombre a la app. Siga el Ejemplo 11 y escriba 'webapplication'; luego pulse Intro.

```bash
? Especifique un nombre para la aplicación> webapplication
```
{: caption="Ejemplo 11. Llame a la aplicación 'webapplication' mediante `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 11. Llame a la aplicación 'webapplication' mediante ibmcloud dev create`

A continuación podrá añadir tantos servicios, como almacenes de datos o funciones de cálculo, como necesite o desee desde la consola web. Sin embargo, tal como se muestra en el Ejemplo 12, escriba 'n', que indica no, cuando se le pregunte si desea añadir servicios en este momento.

```
Se utiliza el grupo de recursos predeterminado (default) de la cuenta

? ¿Desea seleccionar un servicio para añadirlo a esta aplicación? [S/n]> n

```
{: caption="Ejemplo 12. Opción para añadir servicios cuando se utiliza `ibmcloud dev create`, continuación." caption-side="bottom"}
`Ejemplo 12. Opción para añadir servicios cuando se utiliza ibmcloud dev create, continuación`

Anteriormente, se han mencionado las ventajas de desarrollar con contenedores, en lugar de con servidores tradicionales o incluso servidores virtuales, en relación con Docker. Una forma de gestionar los contenedores es con software de orquestación, como Kubernetes, que se ha convertido en un estándar _de facto_ en el desarrollo. Pero, en esta guía de aprendizaje, dejaremos que el servicio Cloud Foundry gestione un único contenedor de Docker que contendrá el código, las bibliotecas y la configuración que necesita la app.

Tal como se muestra en el Ejemplo 13, escriba '1' y pulse Intro para utilizar 'IBM DevOps' para integrar CD en el ciclo de vida del proyecto.
 
```

--------------------------------------------------------------------------------
Seleccione entre las siguientes opciones de cadena de herramientas de DevOps y entorno de
tiempo de ejecución de destino:
 1. IBM DevOps, desplegar en buildpacks de Cloud Foundry
 2. IBM DevOps, desplegar en contenedores de Kubernetes
 3. Sin DevOps, con despliegue manual
--------------------------------------------------------------------------------
? Especifique el número de selección:> 1

```
{: caption="Ejemplo 13. Opciones de desarrollo de `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 13. Opciones de desarrollo de ibmcloud dev create`

Como se ha indicado anteriormente, seleccionaremos una región para nuestra cadena de herramientas de CD de despliegue automatizado, así que seleccione la misma opción que antes, '5', tal como se muestra en el Ejemplo 14.

```

--------------------------------------------------------------------------------
Seleccione una región para su cadena de herramientas entre las siguientes opciones:
--------------------------------------------------------------------------------
 1. eu-de (Frankfurt)
 2. eu-gb (Londres)
 3. jp-tok
 4. us-east (Washington DC)
 5. us-south (Dallas)
--------------------------------------------------------------------------------
 0. Volver a la selección anterior
--------------------------------------------------------------------------------
? Especifique el número de selección:> 5

```
{: caption="Ejemplo 14. Regiones disponibles como opciones en `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 14. Regiones disponibles como opciones en ibmcloud dev create`

En este punto, la generación de una nueva aplicación nos recordará que la cadena de herramientas utilizada para desplegar la app más adelante necesitará alguna configuración adicional, tal como se muestra en el Ejemplo 15. Como se ha mencionado anteriormente, se tendrá que cargar la clave pública a Github (en la instancia de la cadena de herramientas de CD en {{site.data.keyword.cloud_notm}} Platform) para distribuir la aplicación desplegada mediante Github. Encontrará instrucciones adicionales después de desplegar la aplicación y de iniciar una sesión en la cuenta de IBM Cloud GitLab en [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).

```

Nota: para conectar correctamente con la cadena de herramientas de DevOps, esta máquina se debe
configurar para acceso SSH a la cuenta de GitLab de IBM Cloud en
https://git.ng.bluemix.net/profile/keys para poder descargar el código
de aplicación.


```
{: caption="Ejemplo 15. Nota: claves de SSH mediante el mandato `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 15. Nota: claves SSH mediante ibmcloud dev create`

Las solicitudes siguientes confirmarán la aplicación y el nombre de la cadena de herramientas que ha definido anteriormente. En el Ejemplo 16 se muestra cómo puede modificar los nombres de host y de la cadena de herramientas, si lo desea. El nombre de host debe ser exclusivo para el dominio utilizado como punto final de servicio de la aplicación, pero, si no hay ningún conflicto, puede simplemente pulsar retorno cuando se le solicite confirmación.

```
La cadena de herramientas de DevOps para esta app será: webapplication
? Pulse [Retorno] para aceptarlo o escriba un nuevo valor>



El nombre de host para esta app será: webapplication
? Pulse [Retorno] para aceptarlo o escriba un nuevo valor>

La app webapplication se ha creado en IBM Cloud.

Cadena de herramientas de DevOps creada en
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="Ejemplo 16. Confirmación de los nombres de las propiedades en `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 16. Confirmación de los nombres de las propiedades en ibmcloud dev create`

Si copia y pega el enlace que se proporciona al final de la salida que ha recibido como resultado de utilizar el mandato `ibmcloud dev create`, podrá acceder a la cadena de herramientas del CD. Pero también puede acceder a la misma más adelante desde la consola, en el caso de que no haya capturado el enlace. 
Encontrará más información a continuación, a medida que el proceso continúe, como se muestra en el Ejemplo 17, donde las entradas de la aplicación se han creado en línea y se ha creado un directorio con el código de ejemplo. 

```
Cloning repository
https://git.ng.bluemix.net/Organization.Name/webapplication...
Cloning into 'webapplication'...
remote: Counting objects: 60, done.
remote: Compressing objects: 100% (54/54), done.
remote: Total 60 (delta 4), reused 0 (delta 0)
Receiving objects: 100% (60/60), 50.04 KiB | 1.52 MiB/s, done.
Resolving deltas: 100% (4/4), done.
OK

The app, webapplication, has been successfully saved into the
current directory.

```
{: caption="Ejemplo 17. Conformación de las acciones generadas por `ibmcloud dev create`." caption-side="bottom"}
`Ejemplo 17. Confirmación de acciones generadas por ibmcloud dev create`

La última sentencia del Ejemplo 17 significa que si ve el directorio actual, ahora debe estaría visible un nuevo subdirectorio `webapplication`. Dentro del directorio `webapplication` encontrará una versión inicial de la nueva aplicación Node.js. Sin embargo, si bien la receta puede estar presente, los propios ingredientes, aún contenidos en una imagen de Docker, se tienen que "cocer", es decir, compilar, mediante el mandato del Ejemplo 18. Como consecuencia de la instalación, Docker se debería estar ejecutando en la máquina local, pero, si lo tiene que reiniciar, hágalo. Si intenta compilar la nueva aplicación web sin que Docker se esté ejecutando, se producirá un error, aunque esta no es la única razón posible. Si hay algún problema, consulte los mensajes de error resultantes que pueden tener el enlace adecuado para ver los registros de resultados en el portal en línea para la cuenta de {{site.data.keyword.cloud_notm}} Platform.

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Ejemplo 18. Mandato build de {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Ejemplo 18. Mandato build de IBM Cloud Platform`

Además de compilar la app para distribuirla, la compilación de la app le permite ejecutar el mismo código localmente con el mandato `run` (después de copiar y pegar o de escribir el mandato del Ejemplo 19). Cuando haya terminado, copie y pegue el URL proporcionado en la barra de direcciones del navegador, normalmente <http://localhost:3000>.

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Ejemplo 19. Mandato de CLI de {{site.data.keyword.cloud_notm}} Platform para ejecutar la app" caption-side="bottom"}

Ahora que la app se ha creado y definido, visualice la aplicación para confirmar que funciona. Si ves la imagen de marcador de posición tal como se muestra en la Figura 2, ¡enhorabuena! Ha creado una nueva aplicación web Node.js y está lista para que se despliegue en la nube.

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="Figura 2. Nueva aplicación Node.js: ¡Enhorabuena!" caption-side="top"}

Despliegue la app en {{site.data.keyword.cloud_notm}} Platform con el mandato deploy (tal como se muestra en el Ejemplo 20).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Ejemplo 20. Mandato de CLI de {{site.data.keyword.cloud_notm}} Platform para cargar y desplegar la app" caption-side="bottom"}
`Ejemplo 20. Mandatos de CLI de IBM Cloud Platform para cargar y desplegar la app`

El URL se mostrará de nuevo como resultado de ejecutar el mandato `ibmcloud dev deploy` basado en el punto final regional y en el nombre de host que ha especificado anteriormente. Si hay algún problema, puede ver enlaces a los registros almacenados en el portal en {{site.data.keyword.cloud_notm}} Platform. Si no hay ningún problema, debería ver una pantalla idéntica en su navegador a la aplicación local que acaba de visitar. El siguiente paso es visitar la nueva aplicación web en la nube.

## Creación de la app Web Gallery mediante una aplicación de ejemplo
{: #tutorial-create-app}

Recordemos los requisitos previos que necesita para desarrollar una app Node.js en {{site.data.keyword.cloud_notm}} Platform. Ya ha creado la cuenta de {{site.data.keyword.cloud_notm}} Platform y ha instalado Developer Tools, que ha instalado
Docker. Luego ha instalado Node.js. El último elemento mostrado como requisito previo para esta guía de aprendizaje es Git, en el que nos vamos a centrar en este momento.  

Vamos a seguir los pasos específicos para trabajar con la galería de imágenes en Node.js. Por ahora, utilizaremos Github Desktop para este escenario, pero también podría utilizar el cliente de línea de mandatos Git para completar las mismas tareas. Para empezar, vamos a clonar una plantilla de inicio para la nueva aplicación web. 

Siga los siguientes pasos:

1.  Clone el repositorio que aparece en el Ejemplo 21. Descargue la plantilla correspondiente a la app en el
    entorno de desarrollo local mediante Git. En lugar de clonar la app de ejemplo
    desde {{site.data.keyword.cloud_notm}} Platform, utilice el mandato del Ejemplo 21 para colar la plantilla de inicio
    correspondiente a la app Web Gallery de {{site.data.keyword.cos_full_notm}}. Después de clonar el repositorio,
    encontrará la app de inicio en el directorio
    COS-WebGalleryStart. Abra una ventana CMD de Git y vaya al directorio en el que desea
    colar el repositorio Github. Utilice el mandato que se muestra en el primer ejemplo de esta guía de aprendizaje.

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="Ejemplo 21. Detalles del mandato clone de Git" caption-side="bottom"}
`Ejemplo 21. Detalles del mandato clone de Git`

2.  Ejecute la app localmente. Abra una aplicación de terminal proporcionando una CLI y cambie el directorio de trabajo por el directorio COS-WebGalleryStart. Tenga en cuenta las dependencias de Node.js mostradas en el archivo package.json. Descárguelas en el lugar correspondiente con el mandato que se muestra a continuación, en el Ejemplo 22.

```bash
npm install
```
{: codeblock}
{: caption="Ejemplo 22. Instalación de Node Package Manager (npm)" caption-side="bottom"}
`Ejemplo 22. Instalación de Node Package Manager (npm)`

3.  Ejecute la app utilizando el mandato que se muestra en el Ejemplo 23.

```bash
npm start
```
{: codeblock}
{: caption="Ejemplo 23. Detalles del inicio de la app con npm" caption-side="bottom"}
`Ejemplo 23. Detalles del inicio de la app con npm`

Abra un navegador y visualice la app en la dirección y el puerto que se encuentra en la salida de la consola, <http://localhost:3000>.

**Consejo**: para reiniciar la app localmente, cancele el proceso del nodo (Control+C) para detenerlo y utilice de nuevo `npm start`. Sin embargo, mientras desarrolla nuevas funciones, ahorrará tiempo si utiliza nodemon para reiniciar la app cuando detecte un cambio. Instale nodemon a nivel global del siguiente modo:
`npm install -g nodemon`. Luego ejecútelo desde la línea de mandatos en el directorio de la app con: `nodemon`, para que 'nodemon' inicie la app.

4.  Prepárese para preparar la app para el despliegue. Actualice el valor de la propiedad de nombre de aplicación en el archivo `manifest.yml` de COS-WebGallery por al nombre que ha especificado para la app en {{site.data.keyword.cloud_notm}} Platform y otra información tal como se muestra en el Ejemplo 24, si es necesario. La aplicación `manifest.yml` se parece al ejemplo siguiente. Además, puede personalizar el archivo `package.json` ubicado en el directorio raíz de la app con el nombre de la app y su nombre como autor.

```yaml
applications:
- path: .
  memory: 256M
  instances: 1
  domain: us-south.cf.appdomain.cloud
  name: webapplication
  host: webapplication
  disk_quota: 1024M
  random-route: true
```
{: codeblock}
{: caption="Ejemplo 24. Contenido de `manifest.yml`" caption-side="bottom"}
`Ejemplo 24. Contenido de manifest.yml`

**Consejo**: este es el punto en el que es posible que tenga que configurar las claves SSH para enviar por push código de forma interactiva a su origen remoto. Si define una 
    contraseña para la clave SSH, es necesario que especifique este código cada vez que envíe por push los cambios al origen remoto para el repositorio. 

5.  Elimine y sustituya el contenido de su directorio `webapplication` por el contenido del directorio que acaba de modificar, `COS-WebGalleryStart`.
    Utilizando sus conocimientos adquiridos sobre Git, añada los archivos que se han suprimido y añadido al repositorio con la CLI o con Github Desktop. A continuación, envíe por push los cambios al origen del repositorio. En el futuro será capaz de realizar cambios en la aplicación web basada en la nube simplemente enviando los campos por push a Git. La cadena de herramientas de CD reiniciará automáticamente el proceso del servidor después de clonar los cambios y de mandarlos al servidor. 


Básicamente, hemos vuelto a codificar nuestra aplicación, así que vamos a repetir el proceso de compilación. Pero esta vez utilizaremos el nuevo código de la galería de imágenes. 

###Despliegue de la app en {{site.data.keyword.cloud_notm}} Platform.### 

Para incorporar la app de inicio con los cambios
    en {{site.data.keyword.cloud_notm}} Platform, despliéguela con Developer Tools repitiendo los pasos que hemos llevado a cabo antes.

a.  Si aún no lo ha hecho, o si ha reiniciado o ha finalizado la sesión, inicie la sesión en {{site.data.keyword.cloud_notm}} Platform con el mandato login. Como recordatorio del Ejemplo 25, recuerde que puede especificar parámetros opcionales si lo desea: su organización con la opción -o y el espacio con la opción -s, o, si utiliza una cuenta federada: --sso. Recuerde elegir la misma región con la que has estado trabajando hasta el momento, si se la piden.

```bash
ibmcloud login
```
{: codeblock}
{: caption="Ejemplo 25. Mandato de CLI para iniciar sesión en {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Ejemplo 25. Mandato de CLI para iniciar sesión en IBM Cloud Platform`

b.  Configure el punto final de API para su región con el mandato api (tal como se muestra con marcadores opcionales en el Ejemplo 6). Si no sabe el URL del punto final de la API, consulte la página sobre Iniciación.

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="Ejemplo 26. Punto final de API de {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Ejemplo 26. Punto final de API de IBM Cloud Platform`

c.  Elija como destino Cloud Foundry de {{site.data.keyword.cloud_notm}} Platform con el código que se muestra en el Ejemplo 27, utilizando el mandato target y la opción --cf.


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="Ejemplo 27. CLI de {{site.data.keyword.cloud_notm}} Platform con Cloud Foundry como destino" caption-side="bottom"}
`Ejemplo 27. CLI de IBM Cloud Platform con Cloud Foundry como destino`

d.  Compile la app para la entrega de la aplicación con el mandato build (como en el Ejemplo 28).

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Ejemplo 28. Mandato build de {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Ejemplo 28. Mandato build de IBM Cloud Platform`

g.  Ahora vamos a probar la aplicación localmente. Además de compilar la app para distribuirla, la compilación de la app le permite ejecutar el mismo código localmente con el mandato run (después de escribir el mandato del Ejemplo 29).


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Ejemplo 29. Mandato de CLI de {{site.data.keyword.cloud_notm}} Platform para ejecutar la app" caption-side="bottom"}
`Ejemplo 29. Mandato de CLI de IBM Cloud Platform para ejecutar la app`

h.  Despliegue la app en {{site.data.keyword.cloud_notm}} Platform con el mandato deploy (tal como se muestra en el Ejemplo 30).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Ejemplo 30. Mandato de CLI de {{site.data.keyword.cloud_notm}} Platform para carga y despliegues" caption-side="bottom"}
`Ejemplo 30. Mandatos de CLI de IBM Cloud Platform para carga y despliegues`

El código del Ejemplo 31 muestra la secuencia de mandatos que se utiliza en este ejemplo para compilar, probar y desplegar la aplicación web inicial.

```bash
ibmcloud login --sso
ibmcloud api cloud.ibm.com
ibmcloud target --cf
ibmcloud dev enable
ibmcloud dev build
ibmcloud dev run
ibmcloud dev deploy
```
{: codeblock}
{: caption="Ejemplo 31. Lista de mandatos de CLI de {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Ejemplo 31. Lista de mandatos de CLI de IBM Cloud Platform`

Si todo funciona correctamente, {{site.data.keyword.cloud_notm}} Platform indica que la app se ha cargado, se ha desplegado correctamente y se ha iniciado. Si también ha iniciado una sesión en la consola web de {{site.data.keyword.cloud_notm}} Platform, se le notifica también el estado de la app. Pero, lo que es más importante, puede verificar que la app se ha desplegado visitando el URL de la app que muestra {{site.data.keyword.cloud_notm}} Platform con un navegador o desde la consola web pulsando el botón Ver app.

5.  Pruebe la app. El cambio visible entre la plantilla de app predeterminada que se desplegó durante la creación y la app de inicio mostrada demuestra que el despliegue de la app en {{site.data.keyword.cloud_notm}} Platform se ha realizado correctamente.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)

### Creación de una rama de Git
{: #tutorial-create-branch}

Ahora, tiene que crear una rama para el entorno de desarrollo local que se utilizará para la etapa de creación del conducto de entrega de {{site.data.keyword.cloud_notm}} Platform:

1.  Si utiliza Github Desktop, pulse el icono de la rama; se le solicitará que especifique el nombre de la rama (consulte la Figura 14). En este ejemplo el nombre es Local-dev.

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)

2.  Después de crear la rama, GitHub compara los archivos locales de la rama Local-dev con los archivos del repositorio de la rama maestra e informa de que no hay cambios locales. Ahora puede pulsar Publicar para añadir la rama que ha creado en el repositorio local al repositorio de GitHub (tal como se muestra en la Figura 5).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)

Ahora que la rama Local-dev se ha publicado en el repositorio GitHub en la cadena de herramientas, se activará la etapa de compilación del conducto de entrega de {{site.data.keyword.cloud_notm}} Platform, seguida de la etapa de despliegue en cuanto le envíe por push una confirmación.
Ya no será necesario desplegar la app desde la CLI, ya que el despliegue se ha integrado directamente en el flujo de trabajo.

### Configuración de las credenciales de almacenamiento de {{site.data.keyword.cos_full_notm}}
{: #tutorial-credentials}

Tiene que configurar las credenciales de {{site.data.keyword.cos_short}} para la aplicación web, así como un 'grupo' donde se almacenarán y se recuperarán las imágenes. La clave de API que cree necesitará las credenciales de HMAC de {{site.data.keyword.cos_short}}, definidas en las [credenciales de servicio](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials). 
Puede reconocer los términos `access_key_id` y `secret_access_key` si tiene una cuenta de AWS, y utilizar un archivo de credenciales que ya tenga las entradas `aws_access_key_id` y `aws_secret_access_key`. 

Cuando haya creado una clave de API, la haya descargado y la haya copiado en las credenciales de HMAC, siga los pasos siguientes:

1.  En el entorno de desarrollo local, coloque las credenciales en la vía de acceso de Windows `%USERPROFILE%\\.aws\\credentials` (para los usuarios de Mac/Linux, las credenciales deben ir en `~/.aws/credentials)`. En el Ejemplo 32 se muestra el contenido de un archivo de credenciales típico.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="Ejemplo 32. Credenciales definidas en el archivo `~/.aws/credentials'" caption-side="bottom"}
`Ejemplo 32. Credenciales definidas en el archivo ~/.aws/credentials`

2.  En la página web de la aplicación que ha creado mediante el mandato de CLI en {{site.data.keyword.cloud_notm}} Platform, defina las credenciales necesarias como variables de entorno por los métodos recomendados de desarrollo iniciando una sesión en {{site.data.keyword.cloud_notm}} Platform y bajo seleccionando "webapplication" en Apps de Cloud Foundry. En los separadores, pulse Tiempo de ejecución.

3.  En la ventana Tiempo de ejecución, pulse Variables de entorno en la parte superior de la página y desplácese hasta la sección Definidas por el usuario, donde puede añadir las variables.

4.  Añada dos variables: una con el valor de access_key_id, utilizando `AWS_ACCESS_KEY_ID` como nombre de clave, y otra con el valor de la clave de acceso secreta, llamada `AWS_SECRET_ACCESS_KEY`. 
    Estas variables y sus respectivos valores son lo que utiliza la app para autenticarse en la instancia de {{site.data.keyword.cos_short}} cuando se ejecuta en {{site.data.keyword.cloud_notm}} Platform (consulte la Figura 6). Cuando termine con las entradas, pulse Guardar y {{site.data.keyword.cloud_notm}} Platform reiniciará automáticamente la app.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)

A continuación, en el portal de {{site.data.keyword.cos_short}} correspondiente a su instancia de servicio, añada un grupo que contendrá las imágenes. En este ejemplo se utiliza el grupo llamado `web-images`.


## Personalización de la aplicación web Image Gallery de {{site.data.keyword.cos_full_notm}} de Node.js
{: #tutorial-develop}

Dado que en este ejemplo se utiliza una arquitectura MVC, el ajuste de la estructura de directorios dentro del proyecto para que refleje esta arquitectura resulta tanto práctico como recomendado. 
La estructura de directorios tiene un directorio views que contiene plantillas de vista de EJS, un directorio routes que contiene rutas express y un directorio controllers como lugar donde colocar la lógica del controlador. Coloque estos elementos bajo un directorio de origen padre llamado src
(consulte la Figura 7).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)

**Consejo**: el repositorio que ha clonado anteriormente contiene un directorio llamado
COS-WebGalleryEnd. Para seguir estos pasos le ayudará visualizar el código de origen de la aplicación completada en el editor que desee. Será la versión de la 'aplicación web' que se confirme y se despliegue en {{site.data.keyword.cloud_notm}} Platform
cuando termine esta guía de aprendizaje.

### Diseño de la app
{: #tutorial-develop-design}

Estas son las dos tareas principales que un usuario debe poder realizar con la aplicación web sencilla de galería de imágenes:

  - Cargar imágenes desde un navegador web en un grupo de {{site.data.keyword.cos_short}}.
  - Ver las imágenes del grupo de {{site.data.keyword.cos_short}} en un navegador web.

En los pasos siguientes se describe cómo realizar estas dos funciones de demostración; no se trata de crear una app completamente desarrollada lista para producción. Desplegar esta guía de aprendizaje y dejarla expuesta y en ejecución significa que cualquiera que encuentre la app puede realizar las mismas acciones: cargar archivos en el grupo de {{site.data.keyword.cos_full_notm}} y ver las imágenes JPEG que hay ahí en su navegador.

### Desarrollo de la app
{: #tutorial-develop-app}

En el archivo `package.json`, dentro del objeto
scripts, verá cómo se ha definido "start" (Ejemplo 33). Este archivo es lo que utiliza {{site.data.keyword.cloud_notm}} Platform para indicar al nodo que ejecute app.js cada vez que se inicie la app. Utilícelo también para probar la app localmente. Eche un vistazo al archivo principal de la aplicación, llamado app.js. Es el código que hemos pedido a Node.js que procese en primer lugar cuando se inicia la app con el mandato `npm start` (o nodemon). 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 33. Indicar a la app cómo arrancar el código personalizado" caption-side="bottom"}
`Ejemplo 33. Indicar a la app cómo arrancar el código personalizado`

Nuestro archivo app.js comienza con el código que se muestra en el Ejemplo 34.
Al principio, el código utiliza el nodo para cargar los módulos necesarios para comenzar.
La infraestructura Express crea la app como un singleton llamado simplemente `app`. 
El ejemplo termina (por ahora dejamos de lado la mayor parte del código) indicando a la app que escuche en el puerto asignado y una propiedad de entorno, o 3000 de forma predeterminada. 
Si se inicia correctamente, muestra un mensaje con el URL del servidor en la consola.

```javascript
var express = require('express');
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var app = express();
//...

// iniciar servidor en el puerto y host de enlace especificados
var port = process.env.PORT || 3000;
app.listen(port, function() {
    console.log("To view your app, open this link in your browser: http://localhost:" + port);
});
//...
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 34. La aplicación web tiene un comienzo sencillo pero potente" caption-side="bottom"}
`Ejemplo 34. La aplicación web tiene un comienzo sencillo pero potente`

Ahora veamos cómo en el Ejemplo 35 se muestra cómo definir una vía de acceso y vistas. La primera línea de código indica a la infraestructura Express que utilice el directorio público para distribuir nuestros archivos estáticos, que incluyen cualquier imagen estática y hoja de estilo que utilicemos. Las líneas siguientes indican a la app dónde encontrar las plantillas de nuestras vistas
en el directorio
src/views y cómo configurar el motor de vistas para que sea EJS. Además, la infraestructura utilizará el
middleware de análisis de contenido (body-parser) para exponer los datos de solicitud de entrada a la app como JSON. En las líneas finales del ejemplo, la app express responde a todas las solicitudes GET de entrada al URL de nuestra app mostrando la plantilla de vista index.ejs.

```javascript
//...
// distribuir archivos de ./public como archivos principales
app.use(express.static('public'));
app.set('views', './src/views');
app.set('view engine', 'ejs');
app.use(bodyParser.json());

var title = 'COS Image Gallery Web Application';
// Distribuir index.ejs
app.get('/', function (req, res) {
  res.render('index', {status: '', title: title});
});

//...
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 35. Ubicaciones de plantillas y vistas de la app web" caption-side="bottom"}
`Ejemplo 35. Ubicaciones de plantillas y vistas de la app web`

En la figura siguiente se muestra la plantilla de vista de índice cuando se representa y se envía al navegador. Si utiliza `nodemon`, es posible que haya notado que el navegador se ha renovado al guardar los cambios, y su app debería parecerse a la de la Figura 8.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)

En el Ejemplo 36, nuestras plantillas de vista comparten el código HTML comprendido entre las etiquetas
&lt;head&gt;...&lt;/head&gt;, por lo que lo hemos colocado en una plantilla include separada
(consulte el Ejemplo 16). Esta plantilla (head-inc.ejs) contiene un enlace de scriptlet (un enlace correspondiente a una variable JavaScript) para el título de la página en la línea 1. 
La variable `title` está establecida en `app.js` y se pasa como datos para la plantilla de vista en la línea que tiene debajo. De lo contrario, simplemente utilizaríamos algunas direcciones CDN para obtener en Bootstrap CSS, Bootstrap JavaScript y JQuery. Por último, añadimos un archivo styles.css estático personalizado de nuestro directorio de hojas de estilo/público.

```html
<title><%=title%></title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
      crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
        crossorigin="anonymous">
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous">
</script>

<link rel="stylesheet" href="stylesheets/style.css">

```
{: codeblock}
{: caption="Ejemplo 36. Elementos de HTML de head-inc.ejs" caption-side="bottom"}
`Ejemplo 36. Elementos de HTML de head-inc.ejs`

El cuerpo de la vista de índice contiene los separadores de navegación de estilo de programa de arranque (véase el Ejemplo 37) y el formulario de carga en un diseño básico proporcionado por los estilos CSS incluidos con el programa de arranque.

Vamos a estudiar estas dos especificaciones para nuestra app:

-   Definimos nuestro método de formulario en POST y el tipo de codificación de datos de formulario como datos de varias partes/formulario en la línea 24. Para la acción de formulario, enviamos datos de nuestro formulario a la app a la ruta de la app "/". Más adelante, realizamos un trabajo adicional en la lógica del direccionador para que maneje las solicitudes POST a dicha ruta.

-   Queremos mostrar al usuario comentarios sobre el estado del intento de cargar el archivo. Los comentarios se pasan a la vista en una variable llamada "status", y se muestran bajo el formulario de carga.

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
<ul class="nav nav-tabs">
    <li role="presentation" class="active"><a href="/">Home</a></li>
    <li role="presentation"><a href="/gallery">Gallery</a></li>
</ul>
<div class="container">
    <h2>Upload Image to IBM Cloud Object Storage</h2>
    <div class="row">
        <div class="col-md-12">
            <div class="container" style="margin-top: 20px;">
                <div class="row">

                    <div class="col-lg-8 col-md-8 well">

                        <p class="wellText">Upload your JPG image file here</p>

                        <form method="post" enctype="multipart/form-data" action="/">
                            <p><input class="wellText" type="file" size="100px" name="img-file" /></p>
                            <br/>
                            <p><input class="btn btn-danger" type="submit" value="Upload" /></p>
                        </form>

                        <br/>
                        <span class="notice"><%=status%></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

</html>
```
{: codeblock}
{: caption="Ejemplo 37. Elementos HTML de index.ejs" caption-side="bottom"}
`Ejemplo 37. Elementos HTML de index.ejs`

Volveremos un momento a `app.js` del Ejemplo 38. El ejemplo configura rutas Express para manejar peticiones adicionales que se harán en nuestra app. El código correspondiente a estos métodos de direccionamiento estarán en dos archivos bajo el directorio `./src/routes`
del proyecto:

-   imageUploadRoutes.js: este archivo gestiona lo que sucede cuando el usuario selecciona una imagen y pulsa Cargar.

-   galleryRoutes.js: este archivo gestiona las solicitudes cuando el usuario pulsa el separador
    Gallery para solicitar la vista imageGallery.

```javascript
//...
var imageUploadRoutes = require('./src/routes/imageUploadRoutes')(title);
var galleryRouter = require('./src/routes/galleryRoutes')(title);

app.use('/gallery', galleryRouter);
app.use('/', imageUploadRoutes);

//...
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 38. Ejemplos de direccionador Express de Node" caption-side="bottom"}
`Ejemplo 38. Ejemplos de direccionador Express de Node`

#### Carga de imágenes
{: #tutorial-develop-image-upload}

Consulte el código de 'imageUploadRoutes.js' del Ejemplo 39. Debemos crear una instancia del nuevo direccionador express, que en principio llamaremos `imageUploadRoutes`.
Luego crearemos una función que devuelva `imageUploadRoutes` y la asignaremos a una variable llamada `router`. Cuando terminemos, la función se debe exportar como un módulo para que sea accesible para la infraestructura y para el código principal de app.js. 
Para separar la lógica de direccionamiento de la lógica de carga se necesita un archivo de controlador llamado galleryController.js. Puesto que la lógica se dedica a procesar la solicitud de entrada y a proporcionar la respuesta apropiada, colocamos esa lógica en esa función y la guardamos en el directorio ./src/controllers.

La instancia del direccionador de la infraestructura Express es donde, por diseño, imageUploadRoutes direcciona las solicitudes para la ruta raíz de la app ("/") cuando se utiliza el método POST de HTTP. 
Dentro del método `post` de imageUploadRoutes, utilizamos el middleware de los módulos `multer` y `multer-s3` que galeryController expone como `upload`.
El middleware toma los datos y el archivo de nuestro formulario POST Upload, los procesa y ejecuta una función callback. En la función callback comprobamos que recibimos el código
de estado HTTP 200 y que tenemos al menos un archivo que cargar en nuestro objeto de solicitud. Con estas condiciones, definimos los comentarios en la variable `status` y mostramos la plantilla de vista de índice con el nuevo estado.

```javascript
var express = require('express');
var imageUploadRoutes = express.Router();
var status = '';

var router = function(title) {

    var galleryController =
        require('../controllers/galleryController')(title);

    imageUploadRoutes.route('/')
    	.post(
    		galleryController.upload.array('img-file', 1), function (req, res, next) {
                if (res.statusCode === 200 && req.files.length > 0) {
                    status = 'uploaded file successfully';
                }
                else {
                    status = 'upload failed';
                }
                res.render('index', {status: status, title: title});
            });

    return imageUploadRoutes;
};

module.exports = router;
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 39. Detalles del direccionador express de Node" caption-side="bottom"}
`Ejemplo 39. Detalles del direccionador express de Node`

En comparación, el código correspondiente a galleryRouter en el Ejemplo 40 es un modelo de simplicidad. Seguimos el mismo patrón que con imageUploadRouter y necesitamos galleryController en la primera línea de la función; luego configuramos la ruta. La principal diferencia es que direccionamos solicitudes HTTP GET en lugar de POST, y enviamos toda la salida en la respuesta de getGalleryImages, que galeryController expone en la última línea del ejemplo.

```javascript
var express = require('express');
var galleryRouter = express.Router();

var router = function(title) {

    var galleryController =
        require('../controllers/galleryController')(title);

    galleryRouter.route('/')
        .get(galleryController.getGalleryImages);

    return galleryRouter;
};
module.exports = router;

```
{: codeblock}
{: javascript}
{: caption="Ejemplo 40. Detalles del direccionador express de Node" caption-side="bottom"}
`Ejemplo 40. Detalles del direccionador express de Node`

Ahora nos centraremos en el controlador de la galería.

Observe cómo configuramos la carga `multer` en el Ejemplo 41 (que trunca parte del código que por ahora pasaremos por alto). Necesitamos los módulos `ibm-cos-sdk`, `multer` y `multer-s3`. El código muestra cómo configurar un objeto S3 que apunta a un punto final de servidor {{site.data.keyword.cos_short}}. Estamos definiendo de forma estática valores como la dirección de punto final, la región y el grupo para simplificar, pero se puede hacer referencia fácilmente a los mismos desde una variable de entorno o un archivo de configuración JSON.

Definimos `upload` tal como se utiliza en el Ejemplo 41 y se define en imageUploadRouter creando una nueva instancia `multer` con `storage` como única propiedad. Esta propiedad indica a `multer` dónde debe enviar el archivo procedente de nuestros datos de varias partes/formulario. Puesto que {{site.data.keyword.cloud_notm}}
Platform utiliza una implementación de la API S3, definimos el almacenamiento como un objeto `s3-multer`. Este objeto `s3-multer` contiene una propiedad `s3` que hemos asignado a nuestro objeto `s3` anteriormente, y una propiedad bucket a la que hemos asignado la variable `myBucket`, que tiene asignado
el valor “web-images”. Ahora el objeto `s3-multer` tiene todos los datos necesarios para conectar y cargar archivos en nuestro grupo de {{site.data.keyword.cos_short}} cuando recibe datos del formulario de carga. El nombre o la clave del objeto cargado será el nombre de archivo original tomado del objeto de archivo cuando se guarda en el grupo "web-images" de {{site.data.keyword.cos_short}}. 

**Consejo**: utilice una indicación de fecha y hora como parte del nombre de archivo para mantener la exclusividad del mismo. 

```javascript
var galleryController = function(title) {

    var aws = require('ibm-cos-sdk');
    var multer = require('multer');
    var multerS3 = require('multer-s3');
    
    var ep = new aws.Endpoint('s3.us-south.cloud-object-storage.appdomain.cloud');
    var s3 = new aws.S3({endpoint: ep, region: 'us-south-1'});
    var myBucket = 'web-images';

    var upload = multer({
        storage: multerS3({
            s3: s3,
            bucket: myBucket,
            acl: 'public-read',
            metadata: function (req, file, cb) {
                cb(null, {fieldName: file.fieldname});
            },
            key: function (req, file, cb) {
                console.log(file);
                cb(null, file.originalname);
            }
        })
    });
    
    var getGalleryImages = function (req, res) { ... };

    return {
        getGalleryImages: getGalleryImages,
        upload: upload
    };
};

module.exports = galleryController;
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 41. Detalles del controlador express de Node" caption-side="bottom"}
`Ejemplo 41. Detalles del controlador express de Node`

Para realizar pruebas locales, una tarea útil consiste en imprimir el objeto de archivo en la consola, `console.log(file)`. 
Realizamos una prueba local del formulario de carga y mostramos la salida del registro de la consola del archivo en el Ejemplo 42.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Ejemplo 42. Visualización de la consola del objeto de depuración" caption-side="bottom"}
`Ejemplo 42. Visualización de la consola del objeto de depuración`

Aunque no queremos alardear, la Figura 9 muestra el comentario en que se declara que, en la prueba, la
aplicación ha cargado el archivo correctamente.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)

#### Recuperación y visualización de imágenes
{: #tutorial-image-display}

Recuerde que, en app.js, la línea de código `app.use('/gallery', galleryRouter);` indica a la infraestructura express que utilice ese direccionador cuando se solicite la ruta "/gallery". 
Recuerde que este direccionador utiliza galeryController.js (vea el código del Ejemplo 43) y que definimos la función getGalleryImages, cuya firma hemos visto previamente. Utilizando el mismo objeto `s3` que configuramos para nuestra función de carga de imágenes, llamamos a la función denominada `listObjectsV2`. Esta función devuelve los datos de índice que definen cada uno de los objetos de nuestro grupo. Para visualizar imágenes dentro de HTML, necesitamos un URL de imagen para cada imagen JPEG de nuestro grupo `web-images` para que se visualicen en nuestra plantilla de vista. El final con el objeto de datos que devuelve `listObjectsV2` contiene metadatos sobre cada objeto del grupo. 

El código ejecuta un bucle sobre `bucketContents` en busca de cualquier clave de objeto que termine en ".jpg" y crea un parámetro que pasa a la función getSignedUrl de S3. Esta función devuelve un URL firmado para cualquier objeto cuando se proporciona el nombre y la clave del grupo del objeto. En la función callback, guardamos cada URL
en una matriz y la pasamos al método de respuesta del servidor HTTP `res.render`
como valor de una propiedad llamada `imageUrls`.

```javascript
//...
    
    var getGalleryImages = function (req, res) {
        var params = {Bucket: myBucket};
        var imageUrlList = [];
        
        s3.listObjectsV2(params, function (err, data) {    
            if (data) {
                var bucketContents = data.Contents;
                for (var i = 0; i < bucketContents.length; i++) {
                    if (bucketContents[i].Key.search(/.jpg/i) > -1) {
                        var urlParams = {Bucket: myBucket, Key: bucketContents[i].Key};
                        s3.getSignedUrl('getObject', urlParams, function (err, url) {
                            imageUrlList.push(url);
                        });
                    }
                }
            }
            res.render('galleryView', {
                title: title,
                imageUrls: imageUrlList
            });
        });
    };

//...
```
{: codeblock}
{: javascript}
{: caption="Ejemplo 43. Contenido parcial de galleryController.js" caption-side="bottom"}
`Ejemplo 43. Contenido parcial de galleryController.js`

El último ejemplo de código, número 44 de esta guía de aprendizaje, muestra el cuerpo de la plantilla galeryView con el código necesario para visualizar las imágenes. Se obtiene la matriz de imageUrls del método res.render() y se itera sobre un par de etiquetas &lt;div&gt;&lt;/div&gt; anidadas en las que el URL de la imagen hará una solicitud GET para la imagen cuando se solicite la ruta /gallery.

```html
<!DOCTYPE html>
<html>

<head>
    <%- include('head-inc'); %>
</head>

<body>
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="/">Home</a></li>
        <li role="presentation" class="active"><a href="/gallery">Gallery</a></li>
    </ul>
    <div class="container">
        <h2>IBM COS Image Gallery</h2>

        <div class="row">
            <% for (var i=0; i < imageUrls.length; i++) { %>
                <div class="col-md-4">
                    <div class="thumbnail">
                            <img src="<%=imageUrls[i]%>" alt="Lights" style="width:100%">
                    </div>
                </div>
            <% } %>
        </div>
    </div>
</body>

</html>
```
{: codeblock}
{: caption="Ejemplo 44. Scriptlets de bucle y de salida utilizados en la plantilla de la galería" caption-side="bottom"}
`Ejemplo 44. Scriptlets de bucle y de salida utilizados en la plantilla de la galería`

Lo probamos localmente desde http://localhost:3000/gallery y vemos nuestra imagen en la Figura 10.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)

### Confirmación en Git
{: #tutorial-develop-commit}

Ahora que las características básicas de la app funcionan, vamos a confirmar nuestro código en nuestro repositorio local, y luego lo enviamos por push a GitHub. Mediante GitHub Desktop, pulsamos Cambios (véase la
Figura 11), escribimos un resumen de los campos en el campo Resumen y pulsamos Confirmar en local-dev. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)

Cuando se pulsa Sincronizar, nuestra confirmación se envía a la rama
Local-dev que hemos publicado en
GitHub y esta acción inicia la etapa de compilación, seguida de la etapa de despliegue en el conducto de entrega, tal como se muestra en la última figura (número 12) de esta guía de aprendizaje. 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)

## Siguientes pasos
{: #nextsteps}

Enhorabuena. Hemos ejecutado de principio a fin el proceso de creación de una galería de imágenes de aplicación web mediante {{site.data.keyword.cloud_notm}} Platform. 
Cada uno de los conceptos que hemos cubierto en esta introducción básica se puede seguir explorando en [{{site.data.keyword.cloud_notm}} Plataform](https://cloud.ibm.com/). 

¡Buena suerte!
