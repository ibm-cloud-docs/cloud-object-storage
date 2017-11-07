---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}

# Users and service IDs

## Inviting users and assigning access
{: #iamuserinv}

You can invite users across {{site.data.keyword.cloud}} services, applications, and {{site.data.keyword.cloud_notm}} infrastructure from a single location. To invite users and manage outstanding invitations, you must be either an account owner, an organization manager, or you must have infrastructure permissions to add users. You can invite users, cancel invitations, and resend a pending invitation to an invited user. You can invite a single user or, if you are providing the same access for all members in a group of users, you can invite multiple users at once.

### Inviting users

To invite users or manage user invitations in your account, complete the following steps:

1. From the menu bar, click **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Users**. The Users window displays a list of users with their email addresses and current status in the currently selected account.
2. Click **Invite users**.
3. Specify the email address or IBMid of the user. If you are providing multiple users the same access, enter multiple email addresses or IBMids by separating the user ID entries with commas, spaces, or line breaks.
4. Add one or more of the access options that you manage. You must assign at least one access option and configure the settings for the user in each access option that you assign. For any additional access options that you don't add and configure, the default value of *no access* is assigned. You might see one or all of the following access options, depending on the options that you are authorized to manage: **Identity and access enabled services**, **Cloud Foundry access**, **Infrastructure access**. See [Assigning user access](/docs/iam/assignaccess.html) for more information.

If you determine that a user does not need access, you can cancel an invitation for any users that are shown in a **Processing** or **Pending** state in the **Status** column. If an invited user did not receive an invitation, you can resend the invitation to any user in a **Pending** state.

If you want to invite users using the CLI, see the [`bluemix iam account-user-invite`](https://console.stage1.bluemix.net/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_iam_account_user_invite) command.

### Assigning user access
{: #assignaccess}

You assign access for users as you invite them, assigning Cloud Foundry roles, policies, and infrastructure permissions. Depending on the access options that you are authorized to manage, you can invite and provide access for users across the account, organization, space, and service instances. The following sections describe the three types of access that can be assigned to an invited user.

#### Identity and access enabled services

You can assign a single service policy when you invite a new user. Once the user has accepted the invitation, you can add additional service policies. See [Identity and access-enabled service access policies](/docs/iam/iamusermanage.html#iammanidaccser) for details on editing policies to add additional roles, adding additional service policies, or removing a policy for a user.

**Note**: Depending on which service you select when assigning the policy, you might not see all of fields described in the following procedure.

1. From the **Invite users** screen, expand the **Identity and Access enabled services** section.
2. Select a service.
3. Select **All current regions** or a specific region, if your are prompted.
**Note**: Not all services require a region selection.
4. Select **All current service instances** or select a specific service instance.
5. Depending on the service that you selected, you might see the following fields. If you do not enter values for these fields, the policy is assigned at the service instance level instead of the bucket level.
    * **Resource type**: Enter **bucket**.
    * **Resource**: Enter the name of your bucket.
6. Select a role to define the scope of access for the policy.
7. Optional: Select **Add role** to specify an additional role for the policy.

See [Identity and access management policies and roles](/docs/iam/users_roles.html#iamusermanpol) for more specific information about the roles when setting service policies.

#### Cloud Foundry access

When you invite new users, you can choose to add the user to an organization in the account. If you add the user to an organization, you can assign the user an organization role. Then, you choose to give the invited user access to any or all of the spaces in the selected organization with an assigned space role.

1. From the **Invite users** screen, expand the **Cloud Foundry access** section.
2. Select an organization to add the user to.
3. Select an organization role to define the level of access to the selected organization.
4. Optional: Select **Add role** to specify an additional role.
5. Select **All current regions** or a specific region.
6. Select **All current spaces** or a specific space.
7. Select a space role to define the level of access to the selected spaces.  The minimum required is `auditor`.
8. Optional: Select **Add role** to specify an additional role.

See [Cloud Foundry roles](/docs/iam/users_roles.html#cfroles) for more specific information about these roles.

**Note**: You can add a Cloud Foundry role using the [`bluemix iam account-user-invite`](/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_iam_account_user_invite) {{site.data.keyword.cloud_notm}} CLI command, but the UI must be used to assign other access or permissions.

## Managing user API keys
{: #userapikey}

A federated or non-federated user can create an API key to use on the CLI or as part of automation to log in as your user identity. You can use the {{site.data.keyword.cloud_notm}} Platform UI or the {{site.data.keyword.cloud_notm}} CLI to manage your API keys by listing your keys, creating keys, updating keys, or deleting keys. To manage the {{site.data.keyword.cloud_notm}} API keys associated with your user identity, go to **Manage** &gt; **Security** &gt; **{{site.data.keyword.cloud_notm}} Platform API keys** to see a list of your API Keys with descriptions and dates. Then, you can create, edit or delete API keys from this page. And, for a full list of available CLI commands, see [`bluemix iam api-keys`](/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_iam).

As a [federated user](/docs/admin/adminpublic.html#federatedid), you can use an API key to login by using the `BLUEMIX_API_KEY` environment variable. For more information about using an API key for logging in, see the documentation for the [{{site.data.keyword.cloud_notm}} CLI `bluemix login` command](/docs/cli/reference/bluemix_cli/bx_cli.html#bluemix_login) and the [cf CLI `cf login` command](/docs/cli/reference/cfcommands/index.html#cf_login).

### Creating an API key

As an {{site.data.keyword.cloud_notm}} Platform user you might want to use an API key when you enable a program or script without distributing your password to the script. A benefit of using an API key can be that a user or organization can create several API Keys for different programs and the API keys can be deleted independently if compromised without interfering with other API keys or even the user.

To create an API key for your user identity in the UI:

1. Go to **Manage** &gt; **Security** &gt; **{{site.data.keyword.cloud_notm}} API keys**.
2. Click **Create API key**.
3. Enter a name and description for your API key.
4. Click **Create API key**.
5. Then, click **Show** to display the API key to copy and save it for later, or click **Download API key**.

**Note**: For security reasons, the API key is only available to be copied or downloaded at the time of creation. If the API key is lost, you must create a new API key.

To create an API key by using the CLI:

1. Enter `bluemix iam api-key-create NAME [-d DESCRIPTION] [-f, --file FILE]` in your command prompt, and specify a name, description, and file for saving your key. For example:

```
bluemix iam api-key-create MyKey -d "this is my API key" -f key_file
```
{:codeblock}

After an API key is created by using the CLI, there are a few ways that you can use the key with the {{site.data.keyword.cloud_notm}} CLI:

* Enter it with the `bx login` command

```
 bx login --apikey <your api key>
```
{:codeblock}

* Create an API key file to use with the `bx login` command:

 ```
 bx login --apkey @apikeyfile
 ```
 {:codeblock}

 The `apikeyfile` is created by using the `—file` option on the `bx iam api-key-create` command.
* In your command prompt, you can set the environment variable by entering `BLUEMIX_API_KEY=<your api key>` and then entering `bx login`.
* Or, if you want to avoid the {{site.data.keyword.cloud_notm}} CLI and just log in to the cf CLI by using your API key, enter:

 ```
 cf login -u apikey -p <yourapikey>
 ```
 {:codeblock}

  In this option, you use the user name of `apikey` and the password is your `apikey`. Now, you can use `apikey` in other tools like Eclipse or other places looking for `cf login` that accepts only user name and password.

### Updating an API key

If you want to change the name or the description of an API key, complete the following steps in the UI or CLI.

To edit an API key:

1. Go to **Manage** &gt; **Security** &gt; **{{site.data.keyword.cloud_notm}} API keys**.
2. From the **Actions** menu of an API key that is listed in the table, click **Edit the name & description**
3. Update the information for your API key.
4. Click **Update API key**.

To edit an API key by using the CLI:

1. Enter `bluemix iam api-key-update NAME [-n NAME] [-d DESCRIPTION]` in your command prompt, specifying the old name, new name, and new description for the key. For example:

```
bx iam api-key-update MyCurrentName -n MyNewName -d "the new description of my key"
```
{:codeblock}

### Deleting an API key

If you are using a key rotation strategy, you might want to delete an older key and replace it with a new key.

To delete an API key:

1. Go to **Manage** &gt; **Security** &gt; **{{site.data.keyword.cloud_notm}} API keys**.
2. From the **Actions** menu of an API key that is listed in the table, click to **Delete**.
3. Then, confirm the deletion by clicking **Delete key**.

To delete an API key by using the CLI:
1. Enter `bluemix iam api-key-delete NAME` in your command prompt, specifying the name of your key that should be deleted.


## Creating and managing service IDs
{: #serviceids}

A service ID identifies a service or application similar to how a user ID identifies a user. A service ID that you create can be used to enable an application outside of the {{site.data.keyword.cloud_notm}} Platform to access your {{site.data.keyword.cloud_notm}} Platform services. You can assign specific access policies to the service ID that restrict permissions for using specific services, or even combine permissions for accessing different services. Since service IDs are not tied to a specific user, if a user happens to leave an organization and is deleted from the account, the service ID remains ensuring that your application or service stays up and running.

When you create a service ID, you create a unique name and description that is easy for you to identify and work with in the UI. Once you have created your service ID, you can create API keys specific to each service ID that your application can use to authenticate with your {{site.data.keyword.cloud_notm}} Platform services. To ensure that your application has the appropriate access for authenticating with your {{site.data.keyword.cloud_notm}} Platform services, you use service policies assigned to each service ID that you create.

The access policies associated with a service ID enable specific actions that can be taken when that service ID is used to access a specific service. A single service ID can have multiple policies assigned that define the level of access allowed when accessing multiple Identity and access-enabled services, and even different instances of a single service. For example, you have two services with two service instances each. For example, you might assign the `Viewer` role for all available instances of one service and assign the `Editor` role for only one instance of a second service. This way you can customize access to multiple services, but use a single API key for authentication to all.


### Creating a service ID

To create a service ID, go to **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**. Follow the process to create a name and description for your service ID. Then, use the **Actions** menu to manage your service ID. You can start by assigning a policy and creating API keys. For more information about working with API keys, see [API keys for service IDs](apikeys.html#api-keys-for-service-ids).

###  Updating a service ID

You can update a service ID by changing the name and description at any time. You can also delete and create new API keys as needed or update the assigned access policies. However, any changes you make to an existing service ID, such as changing the assigned policies or deleting an API key that is currently being used, might cause service interruptions to applications using that service ID.

## Managing service ID access policies
{: #serviceidpolicy}

When you create a service ID, you can assign service policies for that service ID to control the level of access that is allowed when it is used to authenticate with your services. You can update these assigned access policies at any time by changing an existing policy, deleting a policy, or assigning a new one.

**Note**: If you delete or edit an existing policy for a service ID currently being used, it might cause service interupption.

### Assigning new policies

To assign a new policy:

1. From the menu bar, click **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**.
2. Select the service ID from the table that you want to assign a service policy for.
3. Click **Assign policies**.
4. Select the service from the list that you want to define permissions for.
5. Optional: Click **Specify optional service context**, if you want to refine the region and service instance for the policy.
6. Select the role that you want to assign.
7. Optional: Select **Add role** if you want to add additional roles for the policy.

### Editing existing policies

To edit an existing policy:

1. From the menu bar, click **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**.
2. Select the service ID from the table that you want to edit a service policy for.
3. Identify the row of the policy that you want to edit, and select **Edit policy** from the **Actions** menu.
4. Make your changes, and then save the policy.

### Deleting existing policies

To delete an existing policy:

1. From the menu bar, click **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**.
2. Select the service ID from the table that you want to delete a service policy for.
3. Identify the row of the policy that you want to delete, and select **Remove policy** from the **Actions** menu.
4. Make your changes, and then save the policy.

## Managing service ID API keys
{: #serviceidapikeys}

Service IDs are created to enable access to your {{site.data.keyword.cloud_notm}} services by applications hosted both inside and outside of {{site.data.keyword.cloud_notm}} Platform. API keys are used by an application to authenticate as a particular service ID and be granted the access associated with that service ID.

Once you create a service ID, you can start creating API keys and assigning service policies. Each policy specifies the level of access that is allowed when the API key is used to authenticate with your services. For more information about creating a service ID and assigning policies, see [Creating and managing service IDs](serviceids.html).

Each API key that is associated with a service ID inherits the policy that has been assigned to the service ID. For example, if you want one application to be able to simply view resources within a service, then you need to use an API key associated with a service ID that has a policy assigned with the `Viewer` role. And, if you want another application to be able to have full access within a service, then you need to use an API key associated with a second service ID that has a policy assigned with the `Administrator` role.

### Creating an API key for a service ID

Create an API key to associate with a service ID.

1. Go to **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**.
2. If you don't have a service ID created already, create the service ID.
3. From the **Actions** menu, go to the **Manage service ID** option.
4. Click **Create** in the API keys section.
5. Add a name and description to easily identify the API key.
6. Click **Create**.
7. Save your API key by copying or downloading it to secure location.

**Note**: For security reasons, the API key is only available to be copied or downloaded at the time of creation. If the API key is lost, you must create a new API key.

### Updating an API key for a service ID

You can update an API key by editing the name or description used to identify the key in the UI.

1. Go to **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**.
2. If you don't have a service ID created already, create the service ID.
3. From the **Actions** menu, go to the **Manage service ID** option.
4. From the **Actions** menu in the API keys section, go to the **Edit name & description** option.


### Deleting an API key for a service ID

You can delete an API key that is associated with a service ID. However, deleting an API key that is currently in use by an application will remove the ability for that application to authenticate with your services.

1. Go to **Manage** &gt; **Security** &gt; **Identity & Access** &gt; **Service IDs**.
2. If you don't have a service ID created already, create the service ID.
3. From the **Actions** menu, go to the **Manage service ID** option.
4. From the **Actions** menu in the API keys section, go to the **Delete key** option.
