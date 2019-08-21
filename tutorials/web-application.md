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

# Tutorial: Image Gallery Web Application
{: #web-application}

From beginning to end, building a web application covers a lot of different concepts and is a great way to introduce
yourself to the features of {{site.data.keyword.cos_full}}.
{:shortdesc: .shortdesc}

This tutorial will show you how to build
a simple image gallery on the {{site.data.keyword.cloud}} Platform, and how to bring together
many different concepts and practices. Your application will use {{site.data.keyword.cos_full_notm}} as the 
back-end server for a Node.js application that allows a user to upload and view JPEG image files.

## Before you begin
{: #wa-prereqs}

As prerequisites to building a web application, we will start with the following:

  - {{site.data.keyword.cloud_notm}} Platform account
  - Docker, as part of the {{site.data.keyword.cloud_notm}} Developer Tools
  - Node.js 
  - Git (both Desktop app and Command Line Interface&mdash;CLI)

### Installing Docker
{: #tutorial-wa-install-docker}

Transitioning from building web applications with traditional server instances or even virtual 
servers to using containers, like Docker, speeds up development and eases testing while supporting 
automated deployment. A container is a lightweight structure that doesn't need additional overhead, like an operating 
system, just your code and configuration for everything from dependencies to settings.

Let's start by opening a tool familiar to experienced developers, and a new best friend to those just getting
started: the command line. Ever since the graphic user interface (GUI) was invented, your computer's 
command line interface has been relegated to second-class status. But now, it's time to bring it back (although the GUI
isn't going away any time soon&mdash;especially when we need to browse the web to download our new command line toolset). 

Go ahead and open the Terminal, or other appropriate Command Line Interface for your operating system, and create a 
directory using the commands appropriate to the particular shell you are using. Change your own reference directory to 
the new one you just created. When created, your application will have its own subdirectory within that one, containing 
the starter code and configuration needed to get up and running.

Leaving the command line and returning to the browser, follow the instructions to install the [{{site.data.keyword.cloud_notm}} Platform developer tools](https://cloud.ibm.com/docs/cli?topic=cloud-cli-ibmcloud-cli) at the link. 
The Developer Tools offer a extensible and repeatable approach to building and deploying cloud applications.

[Docker](https://www.docker.com) is installed as part of the Developer Tools, and we will need it, even though its work
will take place mostly in the background, within routines that scaffold your new app. Docker must be running for the build 
commands to work. Go ahead and create a Docker account online at [Dockerhub](https://hub.docker.com), run the Docker app, and sign in.

### Installing Node.js
{: #tutorial-wa-install-node}

The app you will build uses [Node.js](https://nodejs.org/) as the server-side engine to run the
JavaScript code for this web application. In order to use Node's included Node Package Manager (npm), to manage 
your app's dependencies, you must install Node.js locally. Also, having Node.js installed locally 
simplifies testing, thus speeding up development. 

Before you start, you may consider using a version
manager, like Node Version Manager, or `nvm`, to install Node, reducing the complexity of managing multiple versions of Node.js. As of this writing, to install or update `nvm` on a Mac or Linux machine, you can use the install script using cURL in 
the CLI interface you just opened by copying and pasting one of the commands in the first two examples to your command line, 
and pressing enter (note, this assumes that your shell is BASH, and not an alternative):

```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Example 1. Using cURL to install Node Version Manager (nvm)" caption-side="bottom"}
`Example 1. Using cURL to install Node Version Manager (nvm)`
   
...or Wget (just one is necessary, but not both; use whichever is available on your system):

```
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
```
{:codeblock: .codeblock}
{: caption="Example 2. Using Wget to install Node Version Manager (nvm)" caption-side="bottom"}
`Example 2. Using Wget to install Node Version Manager (nvm)`

Or, for Windows, you can use [nvm for Windows](https://github.com/coreybutler/nvm-windows) with installers
and source code at the link.

If you don't want the added complexity of supporting multiple releases of Node.js, please visit the
[Node.js](https://nodejs.org/en/download/releases/) web site
and install the Long Term Support (LTS) Version of Node.js that
matches the latest version supported by the SDK for Node.js buildpack now used on the
{{site.data.keyword.cloud_notm}} Platform. At the time of this writing, 
the latest buildpack is v3.26, and it supports Node.js community edition v6.17.0+. 

You can find additional information about the latest {{site.data.keyword.cloud_notm}} 
SDK for Node.js buildpack on the [SDK for Nodejs latest updates](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates) page. 

Using `nvm` you could install the version of Node that matches the requirements copying and pasting the command from Example 3 to your
command line.

```bash
nvm install v6.17.1
```
{:codeblock: .codeblock}
{: caption="Example 3. Using `nvm` to install a specific version of Node.js" caption-side="bottom"}
`Example 3. Using nvm to install a specific version of Node.js`

Whichever approach you use, once you have followed the instructions to install Node.js and npm (included with Node) 
on your computer, as appropriate to the operating system and strategy you are using, congratulate yourself on a job well 
started!

### Installing Git
{: #tutorial-wa-install-git}

You are probably already familiar with Git, as it is the most widely used 
source code versioning system among developers building applications for the web. 
We will use Git later when we create a Continuous Deployment (CD) Toolchain in the {{site.data.keyword.cloud_notm}} Platform for
continuous delivery and deployment. If you do not have a GitHub account, create a
free public personal account at the [Github](https://github.com/join)
website; otherwise, feel free to log in with any other account you might have.

Please note, there are important, step-by-step, instructions on how to generate and upload SSH keys to your 
[Github profile](https://help.github.com/en/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent) for secure access to Github from the command line. However, if
you do that now, you will only be getting good practice, as you will have to repeat the steps
for the instance of Github used for the {{site.data.keyword.cloud_notm}} Platform, which we will access later. Although 
the steps for using SSH keys can be complicated, with practice, you, too, can be fluent with SSH on the CLI.

For now, go to the [Github Desktop](https://desktop.github.com/) page to download
GitHub Desktop, and then run the installer. When the installer finishes,
you are prompted to log in to GitHub with your account.

In the Log in window (see the first figure in this tutorial), enter the name and email you
want displayed publicly (assuming you have a public account) for any
commits to your repository. Once you have linked the application to your account, you may be asked
to verify the application connection through your Github account online.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png){: caption="Figure 1. Github Desktop Login window" caption-side="top"}

You do not have to create any repositories yet. If you notice a repository named Tutorial included with GitHub Desktop, 
feel free to experiment with it to help familiarize you with the operations. You have just completed the prerequisite 
portion of this tutorial. Are you ready to build an app?

## Creating the Node.js starter app using the Developer Tools
{: #tutorial-create-skeleton}

To start developing your application locally, begin by logging in to the {{site.data.keyword.cloud_notm}} Platform directly
from the command line, as shown in Example 4. 

```bash
ibmcloud login
```
{:codeblock: .codeblock}
{: caption="Example 4. Command to login to the IBM Cloud Platfirm using CLI Developer Tools" caption-side="bottom"}
`Example 4. Command to login to the IBM Cloud Platfirm using CLI Developer Tools`

You may specify optional parameters if you want: your organization with option -o, and the space with option -s, or, 
if you are using a federated account: --sso. When you login you may be asked to 
choose a region, for the purposes of this exercise select `us-south` as the region, as that same option will be used when building a CD Toolchain, later 
on in this tutorial.  

Next, set the endpoint (if it is not set already) using the command shown in Example 5. Other endpoints are possible, and 
may be preferable for production use, but for now, use the code as shown, if appropriate for your account.

```bash
ibmcloud api cloud.ibm.com
```
{:codeblock: .codeblock}
{: caption="Example 5. Command to set the API endpoint for your account." caption-side="bottom"}
`Example 5. Command to set the API endpoint for your account`

Target the Cloud Foundry (cf) aspect of {{site.data.keyword.cloud_notm}} Platform using the code shown in 
Example 6, using the target command and the --cf option. The `cf` API is embedded within the CLI Developer Tools.

```bash
ibmcloud target --cf
```
{:codeblock: .codeblock}
{: caption="Example 6. Setting your options for using the Cloud Foundry API." caption-side="bottom"}
`Example 6. Setting your options for using the Cloud Foundry API`

And now, the moment you've been working towards: creating a web application starts with the code shown in Example 7. The `dev` space
is a default option for your organization, but you may wish to create others for isolating different efforts, keeping 'finance'
separate from 'development', for example.

```bash
ibmcloud dev create
```
{:codeblock: .codeblock}
{: caption="Example 7. Command to create an app using IBM Cloud Developer Tools" caption-side="bottom"}
`Example 7. Command to create an app using IBM Cloud Developer Tools`

With that command, you will be asked a series of questions. You can go back at many points in the process, but if you feel
you have become lost, or missed steps, please feel free to start over by deleting the directory, or creating another for
your testing and exploration. Also, when you complete the process creating your application locally on the command line, you will be able to see the results
online later, in your {{site.data.keyword.cloud_notm}} online portal where you created your account to manage the 
resources you've created.

In Example 8, note the option for creating a 'Web App'&mdash;that's the one you want. Type '2' and press enter.

```
                                        
--------------------------------------------------------------------------------
Select an application type:
--------------------------------------------------------------------------------
 1. Blank App
 2. Backend Service / Web App
 3. Mobile App
--------------------------------------------------------------------------------
 0. Exit
--------------------------------------------------------------------------------
? Enter selection number:> 2


```
{: caption="Example 8. Output from the command `ibmcloud dev create` where you select option #2, for a Web App" caption-side="bottom"}
`Example 8. Output from the command ibmcloud dev create where you select option #2, for a Web App`

There are a number of options in Example 9 based on what are called "buildpacks," and please note the option for using 'Node'. Type '4' and press enter.

```

--------------------------------------------------------------------------------
Select a language:
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
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 4


```
{: caption="Example 9. Language options from `ibmcloud dev create` continued." caption-side="bottom"}
`Example 9. Language options from ibmcloud dev create continued`

After you have made your selection for the programming language and/or framework, the next selection shown in Example 10
will have so many options, it may scroll past your desired service. As you can see in the example, we
wish to use a simple Node.js Web App with Express.js. Type '6' and press enter.

```
? Select a Starter Kit:

--------------------------------------------------------------------------------
APPSERVICE
--------------------------------------------------------------------------------
 1. MEAN Stack: MongoDb, Express.js, Angular, Node.js - A starter 
    project for setting up a mongodb, express, angular and node application
 2. MERN Stack: MongoDb, Express.js, React, Node.js - A starter 
    project for setting up a mongodb, express, react and node application
 3. Node.js BFF Example with Express.js - A starter for building 
    backend-for-frontend APIs in Node.js, using the Express.js framework.
 4. Node.js Example Serverless App - A starter providing a set of 
    Cloud Functions and API for a serverless backend that uses Cloudant NoSQL 
    database.
 5. Node.js Microservice with Express.js - A starter for building a 
    microservice backend in Node.js, using the Express.js framework.
 6. Node.js Web App with Express.js - A starter that provides a basic 
    web serving application in Node.js, using the Express.js framework.
 7. Node.js Web App with Express.js and React - A starter that 
    provides a rich React frontend delivered from a Node.js application, 
    including key web development tools Gulp, SaaS, and Webpack, using the 
    Express.js framework.

--------------------------------------------------------------------------------
FINANCE
--------------------------------------------------------------------------------
 8. Wealth Management Chatbot - A chatbot that allows the user to 
    query the status of their investments and evaluate the impact of different 
    market scenarios on their investment portfolio. It can easily be extended 
    in several ways.

--------------------------------------------------------------------------------
WATSON
--------------------------------------------------------------------------------
 9. Watson Assistant Basic - Simple application that demonstrates the 
    Watson Assistant service in a chat interface simulating banking tasks.
10. Watson Natural Language Understanding Basic - Collection of APIs 
    that can analyze text to help you understand its concepts, entities, 
    keywords, sentiment, and can create a custom model for some APIs to get 
    specific results that are tailored to your domain.
11. Watson News Intelligence - This starter kit demonstrates how to 
    query news content to understand what people are saying or feeling about 
    important topics.
12. Watson Speech to Text Basic - Basic sample of Speech to Text 
    service to convert speech in multiple languages into text.
13. Watson Text to Speech Basic - Basic sample of how to use Text to 
    Speech for streaming, low latency, synthesis of audio from text.
14. Watson Visual Recognition Basic - Use deep learning algorithms to 
    analyze images that can give you insights into your visual content.
--------------------------------------------------------------------------------
 0. Return to the previous selection
--------------------------------------------------------------------------------
? Enter selection number:> 6

```
{: caption="Example 10. Skeleton application options from `ibmcloud dev create`." caption-side="bottom"}
`Example 10. Skeleton application options from ibmcloud dev create`

Now that you have chosen the more straightforward options, the hardest option for developers everywhere is still required: naming your app. Please 
follow the example shown in Example 11 and type 'webapplication', then press enter.

```bash
? Enter a name for your application> webapplication
```
{: caption="Example 11. Name your application 'webapplication' using `ibmcloud dev create`." caption-side="bottom"}
`Example 11. Name your application 'webapplication' using ibmcloud dev create`

Later, you may add as many services, like datastores or compute functions, as needed or desired through the web console. However, as shown in Example 12, type 'n' for no when asked if you want to add services at this time.

```
Using the resource group Default (default) of your account

? Do you want to select a service to add to this application? [Y/n]> n

```
{: caption="Example 12. Option to add services when using `ibmcloud dev create` continued." caption-side="bottom"}
`Example 12. Option to add services when using ibmcloud dev create continued`

Earlier, the advantages of developing with containers, instead of traditional server iron, or even virtual servers, 
was mentioned regarding Docker. One way to manage containers is with orchestration software, like Kubernetes, which has 
become a _de facto_ standard in development. But for this tutorial, we can let the Cloud Foundry service manage a single 
Docker container that will contain the code, libraries, and configuration needed by your app.

As shown in Example 13, type '1' and press enter to use 'IBM DevOps' for the purpose of integrating CD within your project
lifecycle.
 
```

--------------------------------------------------------------------------------
Select from the following DevOps toolchain and target runtime environment 
options:
 1. IBM DevOps, deploy to Cloud Foundry buildpacks
 2. IBM DevOps, deploy to Kubernetes containers
 3. No DevOps, with manual deployment
--------------------------------------------------------------------------------
? Enter selection number:> 1

```
{: caption="Example 13. Deployment options from `ibmcloud dev create`." caption-side="bottom"}
`Example 13. Deployment options from ibmcloud dev create`

As noted earlier, we will choose a region for our automated deployment CD toolchain, so select the same option as earlier, 
'5' as shown in Example 14.

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
{: caption="Example 14. Regions available as options in `ibmcloud dev create`." caption-side="bottom"}
`Example 14. Regions available as options in ibmcloud dev create`

At this point, generating a new application will remind us that the toolchain used
to deploy your app later on will need some additional configuration, as shown in Example 15. As mentioned earlier,
uploading your public key to Github (at the CD Toolchain instance on the {{site.data.keyword.cloud_notm}} 
Platform), will be required to deliver the deployed application using Github. Additional instructions can be found after you deploy
your application and log in to the your IBM Cloud GitLab account at [README#generating-a-new-ssh-key-pair](https://us-south.git.cloud.ibm.com/help/ssh/README#generating-a-new-ssh-key-pair).

```

Note: For successful connection to the DevOps toolchain, this machine 
must be configured for SSH access to your IBM Cloud GitLab account at 
https://git.ng.bluemix.net/profile/keys in order to download the 
application code.


```
{: caption="Example 15. Note given re: SSH keys by the `ibmcloud dev create` command." caption-side="bottom"}
`Example 15. Note given re: SSH keys by the ibmcloud dev create`

Further prompts will confirm the application and toolchain name you defined earlier. Example 16 shows how you can alter the 
host and toolchain names, if you wish. The hostname has to be unique for the domain used as the service endpoint of your application, but if there is no conflict, 
you may simply press return when asked for confirmation.

```
The DevOps toolchain for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>



The hostname for this app will be: webapplication
? Press [Return] to accept this, or enter a new value now>

The app webapplication has been created in IBM Cloud.

DevOps toolchain created at                           
https://cloud.ibm.com/devops/toolchains/6ffb568a-e48f-4e27-aed0-00ca931dde66?env_id=ibm:yp:us-south

```
{: caption="Example 16. Confirming names for properties in `ibmcloud dev create`." caption-side="bottom"}
`Example 16. Confirming names for properties in ibmcloud dev create`

Should you copy and paste that link given at the end of the output you received as a result of using the `ibmcloud dev create` command, you will 
be able to access your CD Toolchain. But, you can also access that from the console later, in case you missed capturing the link. 
Further information follows, as the process continues, as shown in Example 17, where application entries have been created 
online, and a directory with the sample code has been created. 

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
{: caption="Example 17. Confirmation of actions generated by `ibmcloud dev create`." caption-side="bottom"}
`Example 17. Confirmation of actions generated by ibmcloud dev create`

That last statement from Example 17 means that if you view your current directory, a new subdirectory `webapplication` should
now be visible. Inside the `webapplication` directory you will find a scaffold of your new Node.js application. However, while 
the recipe may be present, the ingredients themselves, still wrapped up in a Docker image, have to be "boiled"&mdash;sorry, 
built&mdash;using the command in Example 18. Docker should be running on your local machine as a consequence of installation,
but if you need to restart it, please do so. Any attempt to build your new web application without Docker running will 
fail, but that's not the only possible reason. If there is any issue, check the resulting error messages which may have the 
appropriate link to view result logs in your online portal for your {{site.data.keyword.cloud_notm}} Platform account.

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Example 18. {{site.data.keyword.cloud_notm}} Platform build command" caption-side="bottom"}
`Example 18. IBM Cloud Platform build command`

In addition to building the app for delivery, building the app allows you to run the same code locally with the `run` command 
(after you copy and paste or type the command from Example 19). When finished, copy and paste the provided URL into your
browser's address bar, typically, <http://localhost:3000>.

```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Example 19. {{site.data.keyword.cloud_notm}} Platform CLI command to run your app" caption-side="bottom"}

Now that the app is created and defined, view your application to confirm it works. If you see the placeholder image as 
shown in Figure 2, congratulations! You have created a new Node.js web application and are ready to deploy it to the cloud.

![initialnodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-splash-graphic.png)
{: caption="Figure 2. New Node.js Application: Congratulations!" caption-side="top"}

Deploy the app to {{site.data.keyword.cloud_notm}} Platform with the deploy command (as shown in Example 20).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Example 20. {{site.data.keyword.cloud_notm}} Platform CLI command to upload and deploy your app" caption-side="bottom"}
`Example 20. IBM Cloud Platform CLI command to upload and deploy your app`

The URL again will be displayed as a result of running the command `ibmcloud dev deploy` based upon the regional endpoint
and the hostname you specified earlier. If there are any issues, you may see links to the logs that are stored in your portal
at the {{site.data.keyword.cloud_notm}} Platform. If there aren't any issues, you should see an identical display in your browser 
to the local application you just visited. Go ahead and visit your new web application in the cloud!

## Creating the Web Gallery app using a sample application
{: #tutorial-create-app}

Let's recall the prerequisites you needed for developing a Node.js app on {{site.data.keyword.cloud_notm}} Platform. You 
already created your {{site.data.keyword.cloud_notm}} Platform account as well as installed the Developer Tools, which 
installed Docker. Then, you installed Node.js. The last item listed as a prerequisite for this tutorial was Git, which we will dive into now.  

We're going to start the specifics of working on the image gallery in Node.js. For now, we will use Github Desktop for 
this scenario, but you could also use the Git command line client to complete the same tasks. To get started, let's clone a starter 
template for your new web application. 

Follow these steps:

1.  Clone the repo listed in Example 21. Download the template for your app on your local
    development environment using Git. Rather than cloning the sample
    app from {{site.data.keyword.cloud_notm}} Platform, use the command in Example 21 to clone the
    starter template for the {{site.data.keyword.cos_full_notm}} Web Gallery app. After cloning the
    repo you will find the starter app in the
    COS-WebGalleryStart directory. Open a Git CMD window and change to a
    directory where you want to clone Github repo. Use the command shown
    in the first example of this tutorial.

```bash
git clone https://git.ng.bluemix.net/Chris.Pitchford/temp-image-gallery-tutorial ./temp-web-application
```
{: codeblock}
{: caption="Example 21. Git clone command details" caption-side="bottom"}
`Example 21. Git clone command details`

2.  Run the app locally. Open a terminal application providing a CLI and change your working directory to
    the COS-WebGalleryStart directory. Please note the Node.js dependencies
    listed in the package.json file. Download them into place using the command
    shown next, in Example 22.

```bash
npm install
```
{: codeblock}
{: caption="Example 22. Node Package Manager (npm) install" caption-side="bottom"}
`Example 22. Node Package Manager (npm) install`

3.  Run the app using the command shown in Example 23.

```bash
npm start
```
{: codeblock}
{: caption="Example 23. Details on starting your app with npm" caption-side="bottom"}
`Example 23. Details on starting your app with npm`

Open a browser and view your app on the address and port that is output
to the console, <http://localhost:3000>.

**Tip**: To restart the app locally, kill the node process (Ctrl+C) to
stop it, and use `npm start` again. However, while you develop new features, using nodemon to restart your app when
it detects a change saves you time. Install nodemon globally like this:
`npm install -g nodemon`. Then run it from the command line in your app
directory using: `nodemon`, to have 'nodemon' start your app.

4.  Get ready to prepare the app for deployment! Update the application name property
    value in the `manifest.yml` file from COS-WebGallery, to the name you
    entered for your app on {{site.data.keyword.cloud_notm}} Platform and the other information as shown in Example 24, 
    if necessary. The application `manifest.yml` looks like the following example. In addition, you can customize the `package.json` file
    located in the app root directory for your app with the name
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
{: codeblock}
{: caption="Example 24. Contents of `manifest.yml`" caption-side="bottom"}
`Example 24. Contents of manifest.yml`

**Tip**: Now is the point where you might need to set up SSH keys to interactively push code to your remote origin. If you set a 
    passphrase for your SSH key, you are required to enter this code each time you push your changes to the remote origin for 
    your repository. 

5.  Remove and replace the contents of your `webapplication` directory with the contents of the directory you just modified, `COS-WebGalleryStart`.
    Using your finely tuned Git skills, add the files that were deleted and added to the repository with either the CLI or 
    Github Desktop. Then, push the changes to the repository origin. In the future, you will be able to make changes to your 
    cloud-based web application just by pushing changes to Git. The CD toolchain will auto-magically restart the server process
    after cloning your changes and stashing them on the server. 


In essence, we've re-coded our application, so let's repeat the build process. But this time we'll use the new Image Gallery code. 

###Deploy the app to {{site.data.keyword.cloud_notm}} Platform.### 

To get the starter app with your changes
    to {{site.data.keyword.cloud_notm}} Platform, deploy it using the Developer Tools by repeating the same steps we performed 
    earlier.

a.  If you haven't already, or if you've restarted or logged out, log in to {{site.data.keyword.cloud_notm}} Platform 
by using the login command. As a reminder it is shown in Example 25, and note you can specify optional parameters if you 
want: your organization with option -o, and the space with option -s, or, if you are using a federated account: --sso. Remember 
to choose the same region you've been working with to this point, if you're asked.

```bash
ibmcloud login
```
{: codeblock}
{: caption="Example 25. CLI command for logging into the {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}
`Example 25. CLI command for logging into the IBM Cloud Platform`

b.  Set the API Endpoint for your region by using the api command (as
        shown with optional placeholders in Example 6). if you do not know your regional
        API endpoint URL, please see the Getting Started page.

```bash
ibmcloud api cloud.ibm.com
```
{: codeblock}
{: caption="Example 26. {{site.data.keyword.cloud_notm}} Platform API endpoint" caption-side="bottom"}
`Example 26. IBM Cloud Platform API endpoint`

c.  Target the Cloud Foundry aspect of {{site.data.keyword.cloud_notm}} Platform using the code shown in 
Example 27, using the target command and the --cf option.


```bash
ibmcloud target --cf
```
{: codeblock}
{: caption="Example 27. {{site.data.keyword.cloud_notm}} Platform CLI targeting Cloud Foundry" caption-side="bottom"}
`Example 27. IBM Cloud Platform CLI targeting Cloud Foundry`

d.  Build the app for delivery that application with the build command (as in Example 28).

```bash
ibmcloud dev build
```
{: codeblock}
{: caption="Example 28. {{site.data.keyword.cloud_notm}} Platform build command" caption-side="bottom"}
`Example 28. IBM Cloud Platform build command`

g.  Let's go ahead and test the application locally. In addition to building the app for delivery, building the app allows you to run the same code locally with the run command (after you type the
    command from Example 29).


```bash
ibmcloud dev run 
```
{: codeblock}
{: caption="Example 29. {{site.data.keyword.cloud_notm}} Platform CLI command to run your app" caption-side="bottom"}
`Example 29. IBM Cloud Platform CLI command to run your app`

h.  Deploy the app to {{site.data.keyword.cloud_notm}} Platform with the deploy command 
(as shown in Example 30).

```bash
ibmcloud dev deploy
```
{: codeblock}
{: caption="Example 30. {{site.data.keyword.cloud_notm}} Platform CLI command to upload and deploys" caption-side="bottom"}
`Example 30. IBM Cloud Platform CLI command to upload and deploys`

The code in Example 31 shows the sequence of commands used in this example to build, test, and deploy the initial web application.

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
{: caption="Example 31. {{site.data.keyword.cloud_notm}} Platform CLI command list" caption-side="bottom"}
`Example 31. IBM Cloud Platform CLI command list`

If successful, {{site.data.keyword.cloud_notm}} Platform reports that the app was uploaded,
successfully deployed, and started. If you are also logged in to the {{site.data.keyword.cloud_notm}} Platform
web console, you are notified there also of the status of your app. But, most importantly, you can verify that the app 
was deployed by visiting the app URL reported by {{site.data.keyword.cloud_notm}} Platform with a browser, or from the web
console by clicking View App button.

5.  Test the app. The visible change from the default app template that
    was deployed at creation to the starter app shown in the following
    proved that deploying the app to {{site.data.keyword.cloud_notm}} Platform was successful.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg){: caption="Figure 3. Results of viewing your deployed app. Congratulations!" caption-side="top"}

### Create a Git branch
{: #tutorial-create-branch}

Now, you need to create a branch for the local development environment
to use for your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline Build Stage:

1.  If using Github Desktop, click the branch icon; you are prompted to enter a name for the
    branch (see Figure 14). This example uses Local-dev as the name.

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg){: caption="Figure 4. Use Github Desktop to create a local dev branch" caption-side="top"}

2.  After you create the branch, GitHub compares the local files on the
    Local-dev branch with the files in the repository on the master
    branch and reports No local changes. You can now click Publish to
    add the branch you created on your local repo to your GitHub repo
    (as shown in Figure 5).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg){: caption="Figure 5. Publish your git branch to your repo's remote origin" caption-side="top"}

Now that the Local-dev branch is published to the GitHub repo in your
toolchain, the build stage of your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline will be
triggered followed by the deploy stage anytime you push a commit to it.
Deploying the app from the CLI will no longer be necessary, as the deployment has been integrated directly into your workflow.

### Setting up {{site.data.keyword.cos_full_notm}} your storage credentials
{: #tutorial-credentials}

You need to configure {{site.data.keyword.cos_short}} credentials for your web application, as well as a 'bucket'
where it will store and retrieve images. The API key you will create will need {{site.data.keyword.cos_short}} HMAC credentials, as defined by your 
[Service Credentials](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials). 
You may recognize the terms `access_key_id` and `secret_access_key` as you might have an AWS account, and use 
a credentials file that already has `aws_access_key_id` and `aws_secret_access_key` entries. 

After you have completed creating an API key, downloaded, and then copied the HMAC credentials, complete the following steps:

1.  On the local development environment, place the credentials in the
    Windows path `%USERPROFILE%\\.aws\\credentials` (for Mac/Linux users, the credentials should 
    go into `~/.aws/credentials)`. Example 32 shows the contents of a
    typical credentials file.

```bash
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: codeblock}
{: caption="Example 32. Credentials as they are defined in your `~/.aws/credentials` file" caption-side="bottom"}
`Example 32. Credentials as they are defined in your ~/.aws/credentials file`

2.  In the web page for the application you created using the CLI command on the {{site.data.keyword.cloud_notm}} Platform, 
    define your required credentials as environment variables per development best practices by
    logging in to {{site.data.keyword.cloud_notm}} Platform, and under Cloud Foundry Apps, select your
    app, 'webapplication.' From the tabs, click Runtime.

3.  In the Runtime window, click Environment variables at the top of the
    page and scroll to the User-defined section, which allows you to add
    the variables.

4.  Add two variables: one with the value of your access_key_id, using `AWS_ACCESS_KEY_ID` as the name 
    of the key, and another with the value of your secret access key, named `AWS_SECRET_ACCESS_KEY`. 
    These variables and their respective values are what the app uses to authenticate to the 
    {{site.data.keyword.cos_short}} instance when running on {{site.data.keyword.cloud_notm}} 
    Platform (see Figure 6). When you finish with the
    entries, click Save, and {{site.data.keyword.cloud_notm}} Platform will automatically restart the app for you.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg){: caption="Figure 6. Runtime Environment Variables defined for your app" caption-side="top"}

Next, over at the {{site.data.keyword.cos_short}} Portal for your service instance, 
add a bucket to contain your images. This scenario uses the bucket named `web-images`.


## Customizing a Node.js {{site.data.keyword.cos_full_notm}} Image Gallery Web Application
{: #tutorial-develop}

Because this example uses an MVC architecture, adjusting the directory
structure within your project to reflect this architecture is a convenience as well as a best practice. 
The directory structure has a views directory to contain the EJS view templates, a routes
directory to contain the express routes, and a controllers directory as
the place to put the controller logic. Place these items under a parent source
directory named src (see Figure 7).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg){: caption="Figure 7. Source code structure for your app" caption-side="top"}

**Tip**: The repo you cloned earlier contains a directory named
COS-WebGalleryEnd. Viewing the source code of the completed application in your preferred editor
might be helpful as you follow the next steps. This will be the version
of your 'webapplication' that is committed and deployed to {{site.data.keyword.cloud_notm}} Platform
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
scripts object, you see how "start" is defined (Example 33). This file
is what {{site.data.keyword.cloud_notm}} Platform uses to tell node to run app.js each time the app
starts. Also use it when testing the app locally. Take a look at the main application file, which is called app.js. This is the code that we have told Node.js to process first when you start your app with the `npm start` command (or nodemon). 


```json
{
    "scripts": {
      "start": "node app.js"
    }
}
```
{: codeblock}
{: javascript}
{: caption="Example 33. Telling your app how to bootstrap your custom code" caption-side="bottom"}
`Example 33. Telling your app how to bootstrap your custom code`

Our app.js file begins with the code shown in Example 34.
At first, the code uses node to load modules that are needed to get started.
The Express framework creates the app as a singleton simply called `app`. 
The example ends (leaving out a majority of the code for now) telling the app
to listen on the port that is assigned and an environment property, or 3000 by default. 
When successfully launching at the start, it will print a message with the server URL to the console.

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
{: caption="Example 34. Your Web Application has a humble, but powerful, start" caption-side="bottom"}
`Example 34. Your Web Application has a humble, but powerful, start`

Let's see how Example 35 shows how to define a path and views. The first line of code tells the
Express framework to use the public directory to serve our static files, which
include any static images and style sheets we use. The lines that follow tell the
app where to find the templates for our views in the
src/views directory, and set our view engine to be EJS. In addition, the framework will 
use the body-parser middleware to expose incoming request
data to the app as JSON. In the closing lines of the example, the express app responds to
all incoming GET requests to our app URL by rendering the index.ejs view
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
{: caption="Example 35. Web app views and template locations" caption-side="bottom"}
`Example 35. Web app views and template locations`

The following figure shows what the index view template when rendered
and sent to the browser. If you are using `nodemon` you may have noticed 
that your browser refreshed when you saved your changes, and your app should look like Figure 8.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg){: caption="Figure 8. Your updated web app using templates and views for displays" caption-side="top"}

In Example 36, our view templates share HTML code between the
&lt;head&gt;...&lt;/head&gt; tags, so we placed it into a separate
include template (see Example 16). This template (head-inc.ejs)
contains a scriptlet&mdash;a binding for a JavaScript variable&mdash;for the page title on line 1. 
The `title` variable is set in `app.js`, and passed in as data for our view
template in the line below that. Otherwise, we are simply using some CDN addresses
to pull in Bootstrap CSS, Bootstrap JavaScript, and JQuery. Finally, we add a custom 
static styles.css file from our pubic/style sheets directory.

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
{: caption="Example 36. HTML elements from head-inc.ejs" caption-side="bottom"}
`Example 36. HTML elements from head-inc.ejs`

The body of the index view contains our bootstrap
styled navigation tabs (see Example 37), and our upload form in a basic layout provided by 
the CSS styles included with bootstrap.

Consider these two specifications for our app:

-   We set our form method to POST and the form-data encoding type as
    multipart/form-data on line 24. For the form action, we send the
    data from our form to the app to the app route "/". Later, we do
    additional work in our router logic to handle POST requests to
    that route.

-   We want to display feedback about the status of the attempted file
    upload to the user. This feedback is passed to our view in a
    variable named "status", and is displayed below the upload form.

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
{: caption="Example 37. HTML elements from index.ejs" caption-side="bottom"}
`Example 37. HTML elements from index.ejs`

Let's take a moment to return to `app.js` in Example 38. The example sets up Express
routes to handle additional requests that will be made to our app. The
code for these routing methods will be in two files under the `./src/routes`
directory in your project:

-   imageUploadRoutes.js: This file handles what happens when the user
    selects an image and clicks Upload.

-   galleryRoutes.js: This file handles requests when the user clicks
    the Gallery tab to request the imageGallery view.

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
{: caption="Example 38. Node Express router examples" caption-side="bottom"}
`Example 38. Node Express router examples`

#### Image upload
{: #tutorial-develop-image-upload}

See the code from 'imageUploadRoutes.js' in Example 39. We must create an instance
of a new express router and name it `imageUploadRoutes` at the start.
Later, we create a function that returns `imageUploadRoutes`,
and assign it to a variable called `router`. When completed, the function must be 
exported as a module to make it accessible to the framework and our main code in app.js. 
Separating our routing logic from the upload logic requires a controller file named 
galleryController.js. Because that logic is dedicated to processing the incoming request and 
providing the appropriate response, we put that logic in that function and save it in 
the ./src/controllers directory.

The instance of the Router from the Express framework is where our imageUploadRoutes 
is designed to route requests for the root app route ("/") when the HTTP POST method is used. 
Inside the `post` method of our imageUploadRoutes, we use middleware from the `multer` and
`multer-s3` modules which is exposed by the galleryController as `upload`.
The middleware takes the data and file from our Upload form POST,
processes it, and runs a callback function. In the callback function 
we check that we get an HTTP status code of 200, and that
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
{: caption="Example 39. Node express router details" caption-side="bottom"}
`Example 39. Node express router details`

In comparison, the code for the galleryRouter in Example 40 is a model of simplicity. We follow the same pattern
that we did with imageUploadRouter and require galleryController on the first line of the function, then set up our route. The main difference is we
are routing HTTP GET requests rather than POST, and sending all the
output in the response from getGalleryImages, which is exposed by the
galleryController on the last line of the example.

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
{: caption="Example 40. Node express router details" caption-side="bottom"}
`Example 40. Node express router details`

We next turn our attention to the controller for the gallery.

Please note how we set up the `multer` upload in the Example 41, 
(which truncates some code we'll ignore for now). We
require modules `ibm-cos-sdk`, `multer`, and `multer-s3`. The code shows how to
configure an S3 object that points to an {{site.data.keyword.cos_short}} server endpoint. We are
statically setting values such as the endpoint address, region, and
bucket for simplicity, but they could easily be referenced from an
environment variable or JSON configuration file.

We define `upload` as used in Example 41 and defined in the imageUploadRouter by creating a new
`multer` instance with `storage` as its only property. This property tells
`multer` where to send the file from our multipart/form-data. Since the {{site.data.keyword.cloud_notm}} 
Platform uses an implementation of the S3 API, we set storage to be an
`s3-multer` object. This `s3-multer` object contains an `s3` property that we
have assigned to our `s3` object earlier, and a bucket property that
we have assigned the `myBucket` variable, which is assigned a
value of “web-images”. The `s3-multer` object now has all the data
necessary to connect and upload files to our {{site.data.keyword.cos_short}} bucket when it
receives data from the upload form. The name or key of the uploaded
object will be the original file name taken from the file object when it
is stored in our {{site.data.keyword.cos_short}} “web-images” bucket 

**Tip**: use a timestamp as part of the filename for maintaining filename uniqueness. 

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
{: caption="Example 41. Node express controller details" caption-side="bottom"}
`Example 41. Node express controller details`

For local testing, a
helpful task is to print the file object to the console, `console.log(file)`. 
We perform a local test of the Upload form and show the output from the
console log of the file in the Example 42.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Example 42. Console display of debug object" caption-side="bottom"}
`Example 42. Console display of debug object`

While bragging is unseemly, Figure 9 shows the feedback from our callback 
declaring that the application has indeed: "uploaded file successfully" when tested.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg){: caption="Figure 9. Success!" caption-side="top"}

#### Image retrieval and display
{: #tutorial-image-display}

Remember, back in app.js, the line of code `app.use('/gallery', galleryRouter);` 
tells the express framework to use that router when the “/gallery” route is requested. 
That router, if you recall, uses galleryController.js (see the code in Example 43), we define the
getGalleryImages function, the signature of which we have seen previously. Using the same `s3`
object that we set up for our image upload function, we call the function named 
`listObjectsV2`. This function returns the index data defining each of the
objects in our bucket. To display images within HTML, we need an image URL for each
JPEG image in our `web-images` bucket to display in our view template. The
closure with the data object returned by `listObjectsV2` contains metadata 
about each object in our bucket. 

The code loops through the `bucketContents` searcing for any object key ending in ".jpg," and
create a parameter to pass to the S3 getSignedUrl function. This
function returns a signed URL for any object when we provide the
object’s bucket name and key. In the callback function we save each URL
in an array, and pass it to the HTTP server response method `res.render` 
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
{: caption="Example 43. Partial contents of galleryController.js" caption-side="bottom"}
`Example 43. Partial contents of galleryController.js`

The last code example, number 44 in this tutorial, shows the body for the galleryView template with the code 
needed to display the images. We get the imageUrls array from the res.render() 
method and iterate over a pair of nested &lt;div&gt;&lt;/div&gt; tags where 
the image URL will make a GET request for the image when the /gallery route 
is requested.

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
{: caption="Example 44. Loop and output scriptlets used in the gallery template" caption-side="bottom"}
`Example 44. Loop and output scriptlets used in the gallery template`

We test it locally from http://localhost:3000/gallery and see our image
in Figure 10.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg){: caption="Figure 10. Images uploaded to the bucket are on display" caption-side="top"}

### Committing to Git
{: #tutorial-develop-commit}

Now that the basic features of the app are working, we will commit our code
to our local repo, and then push it to GitHub. Using GitHub Desktop, we
click Changes (see Figure 11), type a summary of the changes in
the Summary field, and then click Commit to Local-dev. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg){: caption="Figure 11. Changes ready for commit in Git" caption-side="top"}

When we click
Sync, our commit is sent to the remote Local-dev branch that we
published to GitHub, and this action starts the Build Stage followed by
the Deploy Stage in our Delivery Pipeline, as exemplified in the last figure, number 12, in this tutorial. 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg){: caption="Figure 12. CD Delivery Pipeline" caption-side="top"}

## Next Steps
{: #nextsteps}

Congratulations! We have gone from beginning to end along this
path to build a web application image gallery using the {{site.data.keyword.cloud_notm}} Platform. 
Each of the concepts we've covered in this basic introduction can be explored further at the 
[{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/). 

Good luck!
