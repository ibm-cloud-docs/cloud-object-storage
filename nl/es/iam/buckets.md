---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: access control, iam, basics, buckets

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

# Permisos sobre grupos
{: #iam-bucket-permissions}

Asigne roles de acceso para los usuarios y los ID de servicio sobre los grupos, mediante la interfaz de usuario o la CLI para crear políticas.

| Rol de acceso | Acciones de ejemplo                                             |
|:------------|-------------------------------------------------------------|
| Gestor     | Hacer públicos los objetos, crear y destruir grupos y objetos |
| Escritor      | Crear y destruir grupos y objetos                      |
| Lector      | Listar y descargar objetos                                   |
| Lector de contenido      | Descargar objetos                                   |

## Concesión de acceso a un usuario
{: #iam-user-access}

Si el usuario tiene que poder utilizar la consola, **también** es necesario otorgarle un rol de acceso mínimo de plataforma de `Visor` sobre la propia instancia, además del rol de acceso al servicio (por ejemplo, `Lector`). Esto le permitirá ver todos los grupos y obtener una lista de los objetos contenidos en los mismos. A continuación, seleccione **Permisos de grupo** en el menú de navegación de la izquierda, seleccione el usuario y seleccione el nivel de acceso (`Gestor` o `Escritor`) que necesite.

Si el usuario va a interactuar con los datos mediante la API y no requiere acceso a la consola, _y_ es miembro de la cuenta, puede otorgar acceso a un solo grupo sin acceso a la instancia padre.

## Aplicación de políticas
{: #iam-policy-enforcement}

Las políticas de IAM se aplican en orden jerárquico, desde el nivel más alto de acceso al más restringido. Los conflictos se resuelven según la política más permisiva. Por ejemplo, si un usuario tiene el rol de acceso al servicio de `Escritor` y de `Lector` sobre un grupo, se pasa por alto la política que otorga el rol de `Lector`.

Esto también se aplica a las políticas de instancia de servicio y de nivel de grupo.

- Si un usuario tiene una política que otorga el rol de `Escritor` sobre una instancia de servicio y el rol de `Lector` sobre un solo grupo, se pasa por alto la política de nivel de grupo.
- Si un usuario tiene una política que otorga el rol de `Lector` sobre una instancia de servicio y el rol de `Escritor` sobre un solo grupo, se aplican ambas políticas y el rol de `Escritor` prevalece para el grupo individual.

Si es necesario restringir el acceso a un solo grupo (o conjunto de grupos), asegúrese de que el ID de usuario o de servicio no tenga ninguna política de nivel de instancia mediante la consola o la CLI.

### Utilización de la UI
{: #iam-policy-enforcement-console}

Para crear una nueva política de nivel de grupo: 

  1. Vaya a la consola de **Acceso IAM** desde el menú **Gestionar**.
  2. Seleccione **Usuarios** en el menú de navegación de la izquierda.
  3. Seleccione un usuario.
  4. Seleccione el separador **Políticas de acceso** para ver las políticas existentes del usuario, asignar una nueva política o editar una política existente.
  5. Pulse **Asignar acceso** para crear una nueva política.
  6. Seleccione **Asignar acceso a recursos**.
  7. En primer lugar, seleccione **Cloud Object Storage** en el menú de servicios.
  8. Luego seleccione la instancia de servicio adecuada. Especifique `grupo` en el campo **Tipo de recurso** y el nombre del grupo en el campo **ID de recurso**.
  9. Seleccione el rol de acceso al servicio que desee.
  10.  Pulse **Asignar**

Tenga en cuenta que si deja los campos **Tipo de recurso** o **Recurso** en blanco, se creará una política de nivel de instancia.
{:tip}

### Utilización de la CLI
{: #iam-policy-enforcement-cli}

Desde un terminal, ejecute el mandato siguiente:

```bash
ibmcloud iam user-policy-create <user-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Para obtener una lista de las políticas existentes:

```bash
ibmcloud iam user-policies <user-name>
```
{:codeblock}

Para editar una política existente:

```bash
ibmcloud iam user-policy-update <user-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

## Concesión de acceso a un ID de servicio
{: #iam-service-id}
Si tiene que otorgar acceso a una grupo para una aplicación u otra entidad no humana, utilice un ID de servicio. El ID de servicio se puede crear específicamente para este propósito, o bien puede ser un ID de servicio existente que se esté utilizando.

### Utilización de la UI
{: #iam-service-id-console}

  1. Vaya a la consola de **Acceso (IAM)** desde el menú **Gestionar**.
  2. Seleccione **ID de servicio** en el menú de navegación de la izquierda.
  3. Seleccione un ID de servicio para ver las políticas existentes y asigne una política nueva o edite una política existente.
  3. Seleccione la instancia de servicio, el ID de servicio y el rol deseado.
  4. Especifique `grupo` en el campo **Tipo de recurso** y el nombre del grupo en el campo **Recurso**.
  5. Pulse **Enviar**

  Tenga en cuenta que si deja los campos **Tipo de recurso** o **Recurso** en blanco, se creará una política de nivel de instancia.
  {:tip}

### Utilización de la CLI
{: #iam-service-id-cli}

Desde un terminal, ejecute el mandato siguiente:

```bash
ibmcloud iam service-policy-create <service-id-name> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}

Para obtener una lista de las políticas existentes:

```bash
ibmcloud iam service-policies <service-id-name>
```
{:codeblock}

Para editar una política existente:

```bash
ibmcloud iam service-policy-update <service-id-name> <policy-id> \
      --roles <role> \
      --service-name cloud-object-storage \
      --service-instance <resource-instance-id>
      --region global \
      --resource-type bucket \
      --resource <bucket-name>
```
{:codeblock}
