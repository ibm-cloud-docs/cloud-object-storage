---

copyright:
  years: 2017, 2020
lastupdated: "2020-09-04"

keywords: web application, node, gallery, tutorial

subcollection: cloud-object-storage

---
{:new_window: target="_blank"}
{:external: target="_blank" .external}
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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Web application tutorial
{: #web-application}

This tutorial shows you how to build a simple image gallery using {{site.data.keyword.cos_full}}, bringing together many different concepts and practices key to web development.
{: shortdesc}

 From beginning to end, building a web application covers a lot of different concepts and is a great way to introduce yourself to the features of {{site.data.keyword.cos_full_notm}}. Your application uses {{site.data.keyword.cos_full_notm}} for storage in a Node.js application that allows a user to upload and view JPEG image files.

## Before you begin
{: #wa-prereqs}

We need to make sure that we have our prerequisites:

- {{site.data.keyword.cloud_notm}} Platform account
- Docker, as part of the {{site.data.keyword.cloud_notm}} Developer Tools
- Node.js 
- Git (both desktop and command line)

Let's start by opening a tool familiar to experienced developers, and a new best friend to those just getting started: the command line. For many, the graphic user interface (GUI) relegated your computer's command-line interface to second-class status. But now, it's time to bring it back (although the GUI isn't going away anytime soon, especially when we need to browse the web to download instructions for the command-line toolset). 

Open a shell and create a directory. Change your own reference directory to the new one you created. When created, your application has its own subdirectory with the starter code and configuration that is needed to get up and running.

Leave the command line and return to your browser so you can follow the instructions to install the [{{site.data.keyword.cloud_notm}} Platform developer tools](/docs/cli?topic=cli-install-devtools-manually) at the link. The Developer Tools offer an extensible and repeatable approach to building and deploying cloud applications. 

### Installing Docker
{: #tutorial-wa-install-docker}

Using containers, like Docker, speeds up development and eases testing and supports automated deployment. A container is a lightweight structure that doesn't need an operating system, just your code and configuration for everything from dependencies to settings.

[Docker](https://www.docker.com){: external} is installed as part of the Developer Tools, and we need it. Its work
takes place mostly in the background within routines that scaffold your new app. Docker must be running for the build 
commands to work. Go ahead and create a Docker account online at [Docker hub](https://hub.docker.com){: external}, run the Docker app, and sign in.

### Installing Node.js
{: #tutorial-wa-install-node}

The app that you build uses [Node.JS](https://nodejs.org/){: external} as the server-side engine to run the JavaScript code for this web application. To use the Node Package Manager (`npm`) to manage  your app's dependencies, you must install Node locally. Also, a local installation of Node simplifies testing, speeding up development. 

Before you start, you might consider a version manager, like Node Version Manager, or `nvm`, to install Node. A version manager reduces the complexity of managing different versions of Node.js.

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{: pre}

...or `wget` (just one is necessary, but not both; use whichever is available on your system):

```bash
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{: pre}

Or, for Windows, you can use [nvm for Windows](https://github.com/coreybutler/nvm-windows){: external} with installers and source code at the link.

Using `nvm`, install Node.

```bash
nvm install v6.17.1
```
{: pre}

Whichever approach you use after you install Node.js and `npm` (included with Node) 
on your computer, congratulate yourself on a job well started!

### Installing Git
{: #tutorial-wa-install-git}

You're probably already familiar with Git, as it's the most widely used 
source code versioning system. 
We use Git later when we create a Continuous Deployment (CD) Toolchain in the {{site.data.keyword.cloud_notm}} Platform for
continuous delivery and deployment. If you don't have a GitHub account, create a
free public personal account at the [GitHub](https://github.com/join) website; otherwise, feel free to log in with any other account you might have.

We need to generate and upload SSH keys to your 
[GitHub profile](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) for secure access to GitHub from the command line. However, doing that now provides good practice, as you repeat the steps
for the instance of GitHub used for the {{site.data.keyword.cloud_notm}} Platform later.

For now, download the [GitHub Desktop](https://desktop.github.com/) and run the installer. When the installer finishes, log in to GitHub with your account.

Enter a name and email (this is displayed publicly) for any
commits to your repository. Once the application is linked to your account, you might be asked
to verify the application connection through your GitHub account online.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png){: caption="Figure 1. GitHub Desktop Login window"}

## Creating the Node.js starter app
{: #tutorial-create-skeleton}

To start developing your application locally, begin by logging in to the {{site.data.keyword.cloud_notm}} Platform directly from the command line, as shown in the example. You can specify optional parameters, such as your organization with option `-o` and the space with option `-s`. If you're using a federated account use `--sso`. 

```bash
ibmcloud login
```
{: pre} 

After logging in, when you are asked if you want to install any extensions, you may see an announcement regarding the Cloud Foundry plugin. Type the command as shown in order to download and install the CLI extension used in this tutorial. 

```bash
ibmcloud cf install
```
{: pre}

When you log in you might be asked to choose a region. For this exercise, select `us-south` as the region, as that same option is used to build a CD Toolchain later in this tutorial.  

Next, set the endpoint (if it isn't set already). Other endpoints are possible, and 
might be preferable for production use. For now, use the code as shown, if appropriate for your account.

```bash
ibmcloud api cloud.ibm.com
```
{: pre}

Target the Cloud Foundry (`cf`) aspect of {{site.data.keyword.cloud_notm}} Platform by using the `target` command and the `--cf` option. The `cf` API is no longer embedded within the CLI Developer Tools and will have to be downloaded separately.

```bash
ibmcloud target --cf
```
{: pre}

And now, time to create a web application. The `dev` space is a default option for your organization, but you might prefer to create others for isolating different efforts. For example, keeping 'finance' separate from 'development'.

```bash
ibmcloud dev create
```
{: pre}

With that command, you're asked a series of questions. You can go back at many points in the process, so if you feel lost you can start over by deleting the existing directory and creating a new directory. Even when you create your application on the command line, you'll still see the results in your {{site.data.keyword.cloud_notm}} console.

Note the option for creating a 'Web App'. That's the one you want.

```
===============================================================================
Select an application type:

 1. Backend Service / Web App
 2. Mobile App
-------------------------
 0. Exit

===============================================================================
? Enter selection number:> 1
```
{: screen}

A number of options are provided, but we want 'Node'. Type '4' and press enter.

```
===============================================================================
Select a language:

 1. Go
 2. Java - MicroProfile / Java EE
 3. Java - Spring
 4. Node
 5. Python - Django
 6. Python - Flask
 7. Swift
-------------------------
 0. Return to the previous selection

===============================================================================
? Enter selection number:> 4
```
{: screen}

After you make your selection for the programming language and framework, the next selection will have so many options, it might scroll past your wanted service. As you can see in the example, we wish to use a simple Node.js Web App with Express.js. Type '3' and press enter.

```
===============================================================================
Select a Starter Kit:

APPSERVICE
-------------------------------------------------------------------------------

 1. Node-RED - A starter to run the Node-RED open-source project on 
    IBM Cloud.

 2. Node.js + Cloudant - A web application with Node.js and Cloudant

 3. Node.js Express App - Start building your next Node.js Express 
    app on IBM Cloud.


WATSON
-------------------------------------------------------------------------------

 4. Natural Language Understanding Node.js App - Use Watson Natural 
    Language Understanding to analyze text to help you understand its 
    concepts, entities, keywords, sentiment, and more.

 5. Speech to Text Node.js App - React app using the Watson Speech to 
    Text service to transform voice audio into written text.

 6. Text to Speech Node.js App - React app using the Watson Text to 
    Speech service to transform text into audio.

 7. Visual Recognition Node.js App - React app using the Watson 
    Visual Recognition service to analyze images for scenes, objects, text, 
    and other subjects.

-------------------------
 0. Return to the previous selection

===============================================================================
? Enter selection number:> 3
```
{: screen}

The hardest option for developers everywhere is still required: naming your app. Follow the example and type `webapplication`, then press enter.

```
? Enter a name for your application> webapplication
```
{: screen}

Later, you can add as many services, like data stores or compute functions, as needed or wanted through the web console. However, type 'n' for no when asked if you want to add services now. Also, if you haven't already set a resource group, you may be prompted at this time. You may skip this by typing 'n' at this prompt.

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: screen}

One way to manage a containerized application is with orchestration software, like Kubernetes, which is a _de facto_ standard in development. But for this tutorial, we can let the Cloud Foundry service manage a single Docker container that holds the code, libraries, and configuration needed by your app.

Type '1' and press enter to use 'IBM DevOps' for integrating CD within your project lifecycle.
 
```
===============================================================================
Select from the following DevOps toolchain and target runtime environment 
options:

 1. IBM DevOps, deploy to Knative-based Kubernetes containers
 2. IBM DevOps, deploy to Helm-based Kubernetes containers
 3. IBM DevOps, deploy to Cloud Foundry buildpacks
 4. No DevOps, with manual deployment

===============================================================================
? Enter selection number:> 3
```
{: screen}

We must choose a region for our automated deployment CD toolchain. So, select the same option as earlier, 
'5'.

```
--------------------------------------------------------------------------------
Select a region for your toolchain from the following options:
--------------------------------------------------------------------------------
 1. eu-de (Frankfurt)
 2. eu-gb (London)
 3. jp-tok
 4. us-east (Washington DC)
 5. us-south (Dallas)
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 5
```
{: screen}

Generating a new application reminds us that the toolchain used
to deploy your app needs some additional configuration. As mentioned earlier,
uploading your public key to GitHub (at the CD Toolchain instance on the {{site.data.keyword.cloud_notm}} 
Platform), is required to deliver the deployed application by using GitHub. More instructions can be found after you deploy
your application and log in to your IBM Cloud GitLab account at [readme file#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).

```
Note: For successful connection to the DevOps toolchain, this machine 
must be configured for SSH access to your IBM Cloud GitLab account at 
https://git.cloud.ibm.com/profile/keys in order to download the 
application code.
```
{: screen}

Further prompts confirm the application and toolchain name that you defined earlier. The example shows how you can alter the 
host and toolchain names, if you want. The hostname must be unique for the service endpoint of your application, but barring a conflict, 
you can simply press return when asked for confirmation.

```
The DevOps toolchain for this app will be: webapplication
? Press [Enter] to accept this, or enter a new value now>


The hostname for this app will be: webapplication
? Press [Enter] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: screen}

If you copy and paste the link that is returned by the `ibmcloud dev create` command, you can also access your CD Toolchain. You can access that from the console later, in case you missed capturing the link. 
Further information follows, as the process continues where application entries are created 
online, and a directory with the sample code is created. 

```
Cloning repository 
https://git.cloud.ibm.com/Organization.Name/webapplication...
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
{: screen}

That last statement means that if you view your current directory, a new subdirectory `webapplication` is now visible. This directory holds a scaffold of your new Node.js application. However, while the recipe might be present, the ingredients themselves are still wrapped up in a Docker image and must be combined. Docker is running on your local machine as a consequence of installation,
but if you need to restart it do so. If you build your new web application without Docker running it fails, but that's not the only possible error. If you run into trouble, check the resulting error messages, which might have the 
appropriate link to view result logs in your online portal for your {{site.data.keyword.cloud_notm}} Platform account.

```bash
ibmcloud dev build
```
{: pre}

Now that the app is built, you can run the code locally with the `run` command. When finished, copy and paste the provided URL into your
browser's address bar, typically, `http://localhost:3000`.

```bash
ibmcloud dev run 
```
{: pre}

Now that the app is created and defined, view your application to confirm it works. If you see the placeholder image as shown in Figure 2, well done! You've created a new Node.js web application and are ready to deploy it to the cloud.

![initial node app](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png){: caption="Figure 2. New Node.js Application!"}

Deploy the app to {{site.data.keyword.cloud_notm}} Platform with the `deploy` command (as shown in the example).

```bash
ibmcloud dev deploy
```
{: pre}

The URL again is displayed by `ibmcloud dev deploy` based on the regional endpoint
and the hostname you specified earlier. You can see links to the logs that are stored in your portal
at the {{site.data.keyword.cloud_notm}} Platform. Go ahead and visit your new web application in the cloud!

## Creating the Web Gallery app
{: #tutorial-create-app}

Let's recall the prerequisites that you needed for developing a Node.js app on {{site.data.keyword.cloud_notm}} Platform. You 
already created your {{site.data.keyword.cloud_notm}} Platform account as well as installed the Developer Tools, which 
installed Docker. Then, you installed Node.js. The last item listed as a prerequisite for this tutorial was Git, which we dive into now.  

We're going to start the specifics of working on the image gallery in Node.js. For now, we use GitHub Desktop for this scenario, but you might also use the Git command-line client to complete the same tasks. To get started, let's clone a starter template for your new web application. 

Follow these steps:

1.  Download the sample here: [download ![External link icon](/docs-content/v1/content/icons/launch-glyph.svg)](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/image-gallery-tutorial.zip){: external}. Download the template for your app to your local
    development environment using your browser. Rather than cloning the sample
    app from {{site.data.keyword.cloud_notm}} Platform, use the command in the example to obtain the
    starter template for the {{site.data.keyword.cos_full_notm}} Web Gallery app. After cloning the
    repo you will find the starter app in the
    COS-WebGalleryStart directory. Open a Git CMD window and change to a
    directory where you want to clone Github repo. Once there, use the command shown
    in the first example of this tutorial to start adding your new files.

```bash
curl https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/image-gallery-tutorial.zip -o image-gallery-tutorial.zip
```
{: pre}


1.  Run the app locally. Open your terminal and change your working directory to the `COS-WebGalleryStart directory`. Note the Node.js dependencies that are listed in the package.json file. Download them into place by using the command shown next.

```bash
npm install
```
{: pre}

1.  Run the app by using the command shown.

```bash
npm start
```
{: pre}

Open a browser and view your app on the address and port that is output
to the console, `http://localhost:3000`.

To restart the app locally, kill the node process (Ctrl+C) to
stop it, and use `npm start` again. Using `nodemon` instead restarts the app when
it detects a change, and saves you time. Install `nodemon` globally like this:
`npm install -g nodemon`. Run it from the command line in your app
directory by using: `nodemon`, to start your app.
{:tip}

1.  Get ready to prepare the app for deployment! Update the application name property
    value in the `manifest.yml` file from COS-WebGallery, to the name you
    entered for your app on {{site.data.keyword.cloud_notm}} Platform and the other information as shown in the example, 
    if necessary. The application `manifest.yml` looks like the following example. You can customize the `package.json` file that is located in the app root directory for your app with the name
    of your app and your name as the author.

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
{: screen}

Now is the point where you might need to set up SSH keys to interactively push code to your remote origin. If you set a 
passphrase for your SSH key, you're required to enter this code each time you push your changes to the remote origin for 
your repository. 
{: tip}

1.  Remove and replace the contents of your `webapplication` directory with the contents of the directory you modified, `COS-WebGalleryStart`.
    Using your finely tuned Git skills, add the files that were deleted and added to the repository with either the CLI or 
    GitHub Desktop. Then, push the changes to the repository origin. In the future, you can make changes to your 
    cloud-based web application just by pushing changes to Git. The CD toolchain will auto-magically restart the server process
    after cloning your changes and stashing them on the server. 


In essence, we've recoded our application, so let's repeat the build process. But this time we use the new Image Gallery code. 

### Deploy the app to {{site.data.keyword.cloud_notm}} Platform.
{: #wb-app-deploy} 

To get the starter app with your changes
    to {{site.data.keyword.cloud_notm}} Platform, deploy it using the Developer Tools by repeating the same steps that we performed 
    earlier.

a. If you haven't already, or if you restarted or logged out, log in to {{site.data.keyword.cloud_notm}} Platform by using the `login` command. 

```bash
ibmcloud login
```
{: pre}

b. Set the API Endpoint for your region by using the `api` command.

```bash
ibmcloud api cloud.ibm.com
```
{: pre}

c. Target the Cloud Foundry aspect of {{site.data.keyword.cloud_notm}} Platform by using the `target` command and the `--cf` option.


```bash
ibmcloud target --cf
```
{: pre}

d. Build the app for delivery that application with the build command (as in the example).

```bash
ibmcloud dev build
```
{: pre}

g. Let's go ahead and test the application locally. This allows you to run the same code locally with the `run` command.


```bash
ibmcloud dev run 
```
{: pre}

h.  Deploy the app to {{site.data.keyword.cloud_notm}} Platform with the `deploy` command.

```bash
ibmcloud dev deploy
```
{: pre}

The code shows the sequence of commands that are used in this example to build, test, and deploy the initial web application.

```bash
ibmcloud login --sso
ibmcloud api cloud.ibm.com
ibmcloud target --cf
ibmcloud dev enable
ibmcloud dev build
ibmcloud dev run
ibmcloud dev deploy
```
{: pre}

When the process finished, {{site.data.keyword.cloud_notm}} Platform will report that the app was uploaded,
successfully deployed, and started. If you're also logged in to the {{site.data.keyword.cloud_notm}} Platform
web console, you're notified there also of the status of your app. But, most importantly, you can verify that the app 
was deployed by visiting the app URL reported by {{site.data.keyword.cloud_notm}} Platform with a browser, or from the web
console by clicking View App button.

Test the app. The visible change from the default app template that
    was deployed at creation to the starter app shown in the following
    proved that deploying the app to {{site.data.keyword.cloud_notm}} Platform was successful.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg){: caption="Figure 3. Results of viewing your deployed app."}

### Create a Git branch
{: #tutorial-create-branch}

Now, you need to create a branch for the local development environment
to use for your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline Build Stage:

1.  If using GitHub Desktop, click the branch icon; you're prompted to enter a name for the
    branch . This example uses `local-dev` as the name.

   ![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg){: caption="Figure 4. Use GitHub Desktop to create a local dev branch"}

1.  After you create the branch, GitHub compares the local files on the
    Local-dev branch with the files in the repository on the default
    branch and reports No local changes. You can now click Publish to
    add the branch you created on your local repo to your GitHub repo
    (as shown in Figure 5).

   ![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg){: caption="Figure 5. Publish your git branch to your repo's remote origin"}

Now that the Local-dev branch is published to the GitHub repo in your
toolchain, the build stage of your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline will be
triggered followed by the deployment stage anytime you push a commit to it.
Deploying the app from the CLI is not necessary, as the deployment has been integrated directly into your workflow.

### Setting up {{site.data.keyword.cos_full_notm}} your storage credentials
{: #tutorial-credentials}

You need to configure {{site.data.keyword.cos_short}} credentials for your web application, as well as a 'bucket'
where it will store and retrieve images. The API key that you will create will need {{site.data.keyword.cos_short}} HMAC credentials, as defined by your 
[Service Credentials](https://cloud.ibm.com/docs/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials). 
You might recognize the terms `access_key_id` and `secret_access_key` as you might have an AWS account, and use 
a credentials file that already has `aws_access_key_id` and `aws_secret_access_key` entries. 

After you have completed creating an API key, downloaded, and then copied the HMAC credentials, complete the following steps:

1.  On the local development environment, place the credentials in the
    Windows path `%USERPROFILE%\\.aws\\credentials` (for Mac/Linux users, the credentials should 
    go into `~/.aws/credentials)`. The example shows the contents of a
    typical credentials file.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}

  1.  In the web page for the application you created by using the CLI command on the {{site.data.keyword.cloud_notm}} Platform, 
    define your required credentials as environment variables per development best practices by
    logging in to {{site.data.keyword.cloud_notm}} Platform, and under Cloud Foundry Apps, select your
    app, `webapplication`. From the tabs, click **Runtime**.

  2.  In the Runtime window, click Environment variables at the beginning of the
    page and scroll to the User-defined section, which allows you to add
    the variables.

  3.  Add two variables: one with the value of your access_key_id, using `AWS_ACCESS_KEY_ID` as the name 
    of the key, and another with the value of your secret access key, named `AWS_SECRET_ACCESS_KEY`. 
    These variables and their respective values are what the app uses to authenticate to the 
    {{site.data.keyword.cos_short}} instance when running on {{site.data.keyword.cloud_notm}} 
    Platform (see Figure 6). When you finish with the
    entries, click Save, and {{site.data.keyword.cloud_notm}} Platform will automatically restart the app for you.

![ibm_cloud_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg){: caption="Figure 6. Runtime Environment Variables defined for your app"}

Next, over at the {{site.data.keyword.cos_short}} Portal for your service instance, 
add a bucket to contain your images. This scenario uses the bucket that is named `web-images`.


## Customizing a Node.js {{site.data.keyword.cos_full_notm}} Image Gallery web Application
{: #tutorial-develop}

Because this example uses an MVC architecture, adjusting the directory
structure within your project to reflect this architecture is a convenience as well as a best practice. 
The directory structure has a views directory to contain the EJS view templates, a routes
directory to contain the express routes, and a `controllers` directory as
the place to put the controller logic. Place these items under a parent source
directory named src (see Figure 7).

![Directory structure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg){: caption="Figure 7. Source code structure for your app"}

**Tip**: The repo that you cloned earlier contain a directory that is named
`COS-WebGalleryEnd`. Viewing the source code of the completed application in your preferred editor
might be helpful as you follow the next steps. This is the version
of your `webapplication` that is committed and deployed to {{site.data.keyword.cloud_notm}} Platform
when you complete this tutorial.

### Designing the app
{: #tutorial-develop-design}

These are the two main tasks that a user should be able to do with the
simple image gallery web application:

  - Upload images from a web browser to the {{site.data.keyword.cos_short}} bucket.
  - View the images in the {{site.data.keyword.cos_short}} bucket in a web browser.

The next steps focus on how to accomplish these two demonstration functions rather than building a fully developed, production-grade
app. Deploying this tutorial and leaving it exposed and running means that anyone who finds the app 
can perform the same actions: upload files to your {{site.data.keyword.cos_full_notm}} bucket and view any JPEG images already there in their browser.

### Developing the app
{: #tutorial-develop-app}

In the `package.json` file, inside the
scripts object, you see how "start" is defined. This file
is what {{site.data.keyword.cloud_notm}} Platform uses to tell node to run app.js each time the app
starts. Also, use it when testing the app locally. Look at the main application file, which is called `app.js`. This is the code that we have told Node.js to process first when you start your app with the `npm start` command (or `nodemon`). 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}

Our `app.js` file uses node to load modules that are needed to get started.
The Express framework creates the app as a singleton simply called `app`. 
The example ends (leaving out most of the code for now) telling the app
to listen on the port that is assigned and an environment property, or 3000 by default. 
When successfully starting at the start, it prints a message with the server URL to the console.

```javascript
var express = require('express');
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var app = express();
//...

// start server on the specified port and binding host
var port = process.env.PORT || 3000;
app.listen(port, function() {
    console.log("To view your app, open this link in your browser: http://localhost:" + port);
});
//...
```
{: codeblock}
{: javascript}

Let's see how to define a path and views. The first line of code tells the
Express framework to use the public directory to serve our static files, which
include any static images and stylesheets we use. The lines that follow tell the
app where to find the templates for our views in the
`src/views` directory, and set our view engine to be EJS. In addition, the framework uses the body-parser middleware to expose incoming request
data to the app as JSON. In the closing lines of the example, the express app responds to
all incoming GET requests to our app URL by rendering the `index.ejs` view
template.

```javascript
//...
// serve the files out of ./public as our main files
app.use(express.static('public'));
app.set('views', './src/views');
app.set('view engine', 'ejs');
app.use(bodyParser.json());

var title = 'COS Image Gallery Web Application';
// Serve index.ejs
app.get('/', function (req, res) {
  res.render('index', {status: '', title: title});
});

//...
```
{: codeblock}
{: javascript}

The following figure shows what the index view template when rendered
and sent to the browser. If you are using ,`nodemon` you might have noticed 
that your browser refreshed when you saved your changes.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg){: caption="Figure 8. Your updated web app by using templates and views for displays"}

Our view templates share HTML code between the
`<head>...</head>`; tags, so we placed it into a separate
include template. This template (`head-inc.ejs`)
contains a scriptlet (a binding for a JavaScript variable) for the page title on line 1. 
The `title` variable is set in `app.js`, and passed in as data for our view
template in the line below that. Otherwise, we are simply using some CDN addresses
to pull in Bootstrap CSS, Bootstrap JavaScript, and JQuery. Finally, we add a custom 
static `styles.css` file from our `pubic/stylesheets` directory.

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

The body of the index view contains our bootstrap
styled navigation tabs, and our upload form in a basic layout that is provided by 
the CSS styles included with bootstrap.

Consider these two specifications for our app:

-   We set our form method to `POST` and the form-data encoding type as
    multipart/form-data on line 24. For the form action, we send the
    data from our form to the app to the app route "/". Later, we do
    extra work in our router logic to handle `POST` requests to
    that route.

-   We want to display feedback about the status of the attempted file
    upload to the user. This feedback is passed to our view in a
    variable named "status", and is displayed after the upload form.

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

Let's take a moment to return to `app.js`. The example sets up Express
routes to handle extra requests that are made to our app. The
code for these routing methods are in two files under the `./src/routes`
directory in your project:

- `imageUploadRoutes.js`: This file handles what happens when the user
    selects an image and clicks Upload.

- `galleryRoutes.js`: This file handles requests when the user clicks
    the Gallery tab to request the `imageGallery` view.

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

#### Image upload
{: #tutorial-develop-image-upload}

See the code from `imageUploadRoutes.js`. We must create an instance
of a new express router and name it `imageUploadRoutes` at the start.
Later, we create a function that returns `imageUploadRoutes`,
and assign it to a variable called `router`. When completed, the function must be 
exported as a module to make it accessible to the framework and our main code in `app.js`. 
Separating our routing logic from the upload logic requires a controller file named 
`galleryController.js`. Because that logic is dedicated to processing the incoming request and 
providing the appropriate response, we put that logic in that function and save it in 
the `./src/controllers` directory.

The instance of the Router from the Express framework is where our `imageUploadRoutes` 
is designed to route requests for the root app route ("/") when the HTTP `POST` method is used. 
Inside the `post` method of our `imageUploadRoutes`, we use middleware from the `multer` and
`multer-s3` modules that is exposed by the `galleryController` as `upload`.
The middleware takes the data and file from our upload form `POST`,
processes it, and runs a callback function. In the callback function 
we check that we get an HTTP status code of `200`, and that
we had at least one file in our request object to upload. Based on those
conditions, we set the feedback in our `status` variable and render the
index view template with the new status.

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

In comparison, the code for the `galleryRouter` is a model of simplicity. We follow the same pattern
that we did with `imageUploadRouter` and require `galleryController` on the first line of the function, then set up our route. The main difference is we
are routing HTTP `GET` requests rather than `POST`, and sending all the
output in the response from `getGalleryImages`, which is exposed by the
`galleryController` on the last line of the example.

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

We next turn our attention to the controller for the gallery.

Note how we set up the `multer` upload, which truncates some code we ignore for now. We
require modules `ibm-cos-sdk`, `multer`, and `multer-s3`. The code shows how to
configure an S3 object that points to an {{site.data.keyword.cos_short}} server endpoint. We are
statically setting values such as the endpoint address, region, and
bucket for simplicity, but they might easily be referenced from an
environment variable or JSON configuration file.

We define `upload` in the `imageUploadRouter` by creating a new
`multer` instance with `storage` as its only property. This property tells the
`multer` where to send the file from our `multipart/form-data`. Since the {{site.data.keyword.cloud_notm}} Platform uses an implementation of the S3 API, we set storage to be an `s3-multer` object. This `s3-multer` object contains an `s3` property that is assigned to our `s3` object. There is also a `bucket` property that is assigned to the `myBucket` variable, which in turn is assigned a value of `web-images`. The `s3-multer` object now has all the data
necessary to upload files to our {{site.data.keyword.cos_short}} bucket when it
receives data from the upload form. The name (or key) of the uploaded
object is the original file name. 

Use a time stamp as part of the file name for maintaining file name uniqueness. 
{:tip}

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
    
    var getGalleryImages = function (req, res) { /* ... shown below ... */ };

    return {
        getGalleryImages: getGalleryImages,
        upload: upload
    };
};

module.exports = galleryController;
```
{: codeblock}
{: javascript}

For local testing, a
helpful task is to print the file object to the console, `console.log(file)`. 
We perform a local test of the upload form and show the output from the
console log of the file.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```

While bragging is unseemly, the feedback from our callback 
declares the application has "uploaded file successfully" when tested.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg){: caption="Figure 9. Success!"}

#### Image retrieval and display
{: #tutorial-image-display}

Remember, back in `app.js`, the line of code `app.use('/gallery', galleryRouter);` 
tells the express framework to use that router when the `/gallery` route is requested. 
That router, if you recall, uses `galleryController.js` , we define the
`getGalleryImages` function, the signature of which we have seen previously. Using the same `s3`
object that we set up for our image upload function, we call the function that is named 
`listObjectsV2`. This function returns the index data defining each of the
objects in our bucket. To display images within HTML, we need an image URL for each
JPEG image in our `web-images` bucket to display in our view template. The
closure with the data object returned by `listObjectsV2` contains metadata 
about each object in our bucket. 

The code loops through the `bucketContents` searching for any object key ending in ".jpg," and
create a parameter to pass to the S3 `getSignedUrl` function. This
function returns a signed URL for any object when we provide the
object’s bucket name and key. In the callback function, we save each URL
in an array, and pass it to the HTTP Server response method `res.render` 
as the value to a property named `imageUrls`.

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

The last code example shows the body of the `galleryView` template with the code that is needed to display our images. We get the `imageUrls` array from the `res.render()` 
method and iterate over a pair of nested `<div>...</div>` tags. Each sends a `GET` request for the image when the `/gallery` route is requested.

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

We test it locally from `http://localhost:3000/gallery`
 and see our image.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg){: caption="Figure 10. Images uploaded to the bucket are on display"}

### Committing to Git
{: #tutorial-develop-commit}

Now that the basic features of the app are working, we commit our code
to our local repo, and then push it to GitHub. Using GitHub Desktop, we
click Changes (see Figure 11), type a summary of the changes in
the Summary field, and then click Commit to Local-dev. 

![Commit updates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg){: caption="Figure 11. Changes ready for commit in Git"}

When we click **sync**, our commit is sent to the remote `local-dev` branch. This action starts the Build and Deploy Stages in our Delivery Pipeline. 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg){: caption="Figure 12. CD Delivery Pipeline"}

## Next Steps
{: #webapp-next-steps}

We went from beginning to end and built a basic web application image gallery by using the {{site.data.keyword.cloud_notm}} Platform. 
Each of the concepts we've covered in this basic introduction can be explored further at the 
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/). 

Good luck!
