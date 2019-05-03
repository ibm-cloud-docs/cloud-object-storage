---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-05-03"

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

From beginning to end, a web application covers a lot of different concepts and is a great way to introduce
yourself to the features of {{site.data.keyword.cos_full}}. This tutorial will show you how to build
a simple image gallery on the {{site.data.keyword.cloud}} Platform, and how to bring together
many different concepts and practices. This application uses {{site.data.keyword.cos_full_notm}} as the 
back-end storage for a Node.js application that allows a user to upload and view JPEG image files.

## Before you begin
{: #gs-prereqs}

As prerequisites to building a web application, we will start with the following:

  - {{site.data.keyword.cloud_notm}} Platform account
  - Node.js
  - Git (both Desktop app and Command Line Interface&mdash;CLI)

### Installing Node.js
{: #tutorial-gs-install-node}

The app you will build uses Node.js as the server-side engine to run the
JavaScript code for this web application. In order to use the Node Package Manager (NPM), to manage 
your app's dependencies, you must install Node.js locally. Also, having Node.js installed locally 
simplifies testing, thus speeding up development. 

Go to the
[Node.js](https://nodejs.org/en/download/releases/) web site
and install the Long Term Support (LTS) Version of Node.js that
matches the latest version supported by the SDK for Node.js buildpack on
{{site.data.keyword.cloud_notm}} Platform. At the time of this writing, 
the latest buildpack is v3.26, and it supports Node.js community edition v6.17.x. 
You can find information about the latest {{site.data.keyword.cloud_notm}} 
SDK for Node.js buildpack on the [SDK for Nodejs latest updates](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html#latest_updates) page. 
Follow the instructions to install Node.js and NPM on your system, as appropriate 
to the operating system you are using.

### Installing Git
{: #tutorial-gs-install-git}

You are probably already familiar with Git, as it is the most widely used 
source code versioning system among developers building applications for the web. 
We will use Git later when we create a toolchain in the {{site.data.keyword.cloud_notm}} Platform for
continuous delivery. If you do not have a GitHub account, create a
free public personal account at the [Github](https://github.com/join)
website; otherwise, feel free to log in with any other account you might have.

Go to the [Github Desktop](https://desktop.github.com/) page to download
GitHub Desktop, and then run the installer. When the installer finishes,
you are prompted to log in to GitHub with your account.

In the Log in window (see the first figure in this tutorial), enter the name and email you
want displayed publicly (assuming you have a public account) for any
commits to your repository. Once you have linked the application to your account, you may be asked
to verify the connection through your Github account online.

![github_desktop_setup](http://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-001-github-login.png)
{: caption="Figure 1. Github Desktop Login window" caption-side="top"}

You do not have to create any repositories yet. If you notice a
repository named Tutorial included with GitHub Desktop, feel free to experiment with it to help 
familiarize you with the operations.

## Creating the Web Gallery app on {{site.data.keyword.cloud_notm}} Platform
{: #tutorial-create-app}

To create a Cloud Foundry app, log in to [{{site.data.keyword.cloud_notm}} Platform](https://cloud.ibm.com/docs/runtimes/nodejs/updates.html)
and click Create App (see the next figure).

![bluemix_create_app](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-002-create-app.jpg)
{: caption="Figure 2. Create App on {{site.data.keyword.cloud_notm}} Platform" caption-side="top"}

Then, under Cloud Foundry Apps, select SDK for Node.js (see Figure 3).

![cf_app_nodejs](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-003-cloud-foundry.jpg)
{: caption="Figure 3. Cloud Foundry Apps" caption-side="top"}

Figure 4 shows the app creation page where you provide a name
to identify the app. Call it something descriptive, such as COS-WebGallery. The App name will automatically appear in the host name field, or you can type your own. The host name, along with the the Domain you choose, becomes the internet address, or URL that you use to view
the app on {{site.data.keyword.cloud_notm}} Platform. Additional routes can be configured later, so
accept the defaults as given and click Create. {{site.data.keyword.cloud_notm}} Platform creates a starter app,
deploys and starts it for us.

![clickcreatenodeapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-004-platform-plans.jpg)
{: caption="Figure 4. {{site.data.keyword.cloud_notm}} Platform plans" caption-side="top"}

Now that the app is created and running, click View App from the app’s
Getting Started page to see it in a new browser window. It was created
with a basic Hello World starter app as a placeholder (see Figure 5).

![initiahhelloworldapp](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-005-hello-world.jpg)
{: caption="Figure 5. Hello World!" caption-side="top"}

Let's recall the prerequisites that you need
for developing a Node.js app on {{site.data.keyword.cloud_notm}} Platform as listed previously. You already
created your {{site.data.keyword.cloud_notm}} Platform account, and installed Node.js. Install the [Cloud Foundry CLI](https://github.com/cloudfoundry/cli) as instructed. You
can then use the tool to log in to {{site.data.keyword.cloud_notm}} Platform and interact directly with your
account from your local environment. This tool puts many powerful
commands at your disposal that you do not use in this scenario. More
information is at the [Cloud Foundry CLI commands index
page](https://github.com/cloudfoundry/cli).

The next item listed as a prerequisite is the Git command line client.
We will use Github Desktop in most this scenario, but you could also the
Git command line client to complete the same tasks. We will only use it
to clone a starter template for the app. If you do not have Git
installed, download it from [Git](https://git-scm.com/downloads) and run
the installer accepting the default options.

Follow these steps:

1.  Clone the repo. Download the template for your app on your local
    development environment using Git. Rather than cloning the sample
    app from {{site.data.keyword.cloud_notm}} Platform, use the command in Example 5-1 to clone the
    starter template for the {{site.data.keyword.cos_full_notm}} Web Gallery app. After cloning the
    repo you will find the starter app in the
    COS-WebGalleryStart directory. Open a Git CMD window and change to a
    directory where you want to clone Github repo. Use the command shown
    in the first example of this tutorial.

```
git clone https://github.com/IBMRedbooks/IBMRedbooks-SG248385-Cloud-Object-Storage-as-a-Service.git
```
{: caption="Example 1. Git clone command details" caption-side="bottom"}

2.  Run the app locally. Open a terminal application providing a CLI and change your working directory to
    the COS-WebGalleryStart directory. Please note the Node.js dependencies
    listed in the package.json file. Download them into place using the command
    shown next, in Example 2.

```
npm install
```
{: caption="Example 2. Node Package Manager (npm) install" caption-side="bottom"}

Run the app using the command shown in Example 3.

```
npm start
```
{: caption="Example 3. Details on starting your app with npm" caption-side="bottom"}

Open a browser and view your app on the address and port that is output
to the console, <http://localhost:3000>.

**Tip**: To restart the app locally, kill the node process (Ctrl+C) to
stop it, and use `npm start` again. Using nodemon to restart your app when
it detects a change saves you time. Install nodemon globally like this:
`npm install -g nodemon`. Then run it from the command line in your app
directory using: `nodemon`, to have nodemon start your app.

3.  Get ready to prepare the app for deployment! Update the application name property
    value in the `manifest.yml` file from COS-WebGallery, to the name you
    entered for your app on {{site.data.keyword.cloud_notm}} Platform. The COS-WebGallery `manifest.yml`
    looks like the following example. In addition, you can customize the package.json file
    located in the app root directory for your app with the name
    of your app and your name as the author.


```
applications:

- name: COS-WebGallery
  random-route: true
  memory: 256M

```
{: caption="Example 4. Contents of `manifest.yml`" caption-side="bottom"}


4.  Deploy the app to {{site.data.keyword.cloud_notm}} Platform. To get the starter app with your changes
    to {{site.data.keyword.cloud_notm}} Platform, deploy it using the Cloud Foundry CLI:

a.  Log in to {{site.data.keyword.cloud_notm}} Platform by using the login command. In addition to how it
is shown in Example 5, you can specify optional parameters if you want: your organization with option -o, and the space with option -s, or, if you are using a federated account: --sso.


```
ibmcloud login
```
{: caption="Example 5. CLI command for logging into the {{site.data.keyword.cloud_notm}} Platform" caption-side="bottom"}

b.  Set the API Endpoint for your region by using the api command (as
        shown with optional placeholders in Example 6). if you do not know your regional
        API endpoint URL, please see the Getting Started page.


```
ibmcloud api <cloud.ibm.com|api.ng.bluemix.net|other.endpoint.uri>
```
{: caption="Example 6. {{site.data.keyword.cloud_notm}} Platform API endpoint" caption-side="bottom"}

c.  Target the Cloud Foundry aspect of {{site.data.keyword.cloud_notm}} Platform using the code shown in 
Example 7, using the target command and the cf option.


```
ibmcloud target cf
```
{: caption="Example 7. {{site.data.keyword.cloud_notm}} Platform CLI targeting Cloud Foundry" caption-side="bottom"}

d.  Your directory will be home to a new application when you use the enable command. 
Using the `dev` space of your organization this way, allows you to silo your applications.
Answer the generated questions about your project after typing the command in Example 8.


```
ibmcloud dev enable
```
{: caption="Example 8. {{site.data.keyword.cloud_notm}} Platform CLI to define an app" caption-side="bottom"}

f.  The previous command created entries based on your answers to define an app in the {{site.data.keyword.cloud_notm}} Platform. 
Build the app for delivery that application with the build command (as in Example 9).


```
ibmcloud dev build
```
{: caption="Example 9. {{site.data.keyword.cloud_notm}} Platform build command" caption-side="bottom"}

g.  In addition to building the app for delivery, building the app allows you to run the same code locally with the run command (after you type the
    command from Example 10).


```
ibmcloud dev run 
```
{: caption="Example 10. {{site.data.keyword.cloud_notm}} Platform CLI command to run your app" caption-side="bottom"}

h.  Deploy the app to {{site.data.keyword.cloud_notm}} Platform with the deploy command 
(as shown in Example 11).

```
ibmcloud dev deploy
```
{: caption="Example 11. {{site.data.keyword.cloud_notm}} Platform CLI command to upload and deploys" caption-side="bottom"}

The code in Example 12 shows the commands we used in this example to initially build and deploy the COS-WebGallery app.

```
ibmcloud login --sso
ibmcloud api cloud.ibm.com
ibmcloud target cf
ibmcloud dev enable
ibmcloud dev build
ibmcloud dev run
ibmcloud dev deploy
```
{: caption="Example 12. {{site.data.keyword.cloud_notm}} Platform CLI command list" caption-side="bottom"}

If successful, {{site.data.keyword.cloud_notm}} Platform reports that the app was uploaded,
successfully deployed, and started. If you are also logged in to the {{site.data.keyword.cloud_notm}} Platform
web console, you are notified there also of the status of your app (see
figure).

![app_stage_notification](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-006-notifications.jpg)
{: caption="Figure 6. {{site.data.keyword.cloud_notm}} Platform notifications" caption-side="top"}

You can verify that the app was deployed by visiting the app URL
reported by {{site.data.keyword.cloud_notm}} Platform with a browser, or from the web
console by clicking View App button.

5.  Test the app. The visible change from the default app template that
    was deployed at creation to the starter app shown in the following
    proved that deploying the app to {{site.data.keyword.cloud_notm}} Platform was successful.

![verify_push](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-007-congrats.jpg)
{: caption="Figure 7. Results of viewing your deployed app (Congratulations!)" caption-side="top"}

### Creating a {{site.data.keyword.cloud_notm}} Platform toolchain
{: #tutorial-create-toolchain}

You are almost ready to start working on the code for the Image Gallery web application, but before
you start coding, you must have a repository for the source code. For it to work in this instance, it must
be accessible from both {{site.data.keyword.cloud_notm}} Platform and your local development environment.

With a repository in place, we will be able to pull changes from {{site.data.keyword.cloud_notm}} Platform 
to our local development environment, as well as push any changes we make locally to {{site.data.keyword.cloud_notm}} 
Platform. To automate the process, we'll create a Delivery Pipeline for our web application in {{site.data.keyword.cloud_notm}} 
Platform by completing the following steps:

1.  After signing in to {{site.data.keyword.cloud_notm}} Platform, select the COS-WebGallery app, and
    from the app Overview window, scroll to Continuous delivery and
    click Enable (see Figure 8).

![continuous_delivery_enable](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-008-continuous-delivery.jpg)
{: caption="Figure 8. Enable Continuous Delivery (CD)" caption-side="top"}

2.  Set up the Toolchain Integrations. Scroll down to see the
    information that is shown in Figure 9. The Continuous
    Delivery Toolchain template creates a GitHub repository, Delivery
    Pipeline, and integrates Eclipse Orion Web IDE to allow the editing
    of your app code in the cloud. Everything is populated with the
    necessary values to create the toolchain. Click Create.

![toolchain_integrations_setup](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-009-toolchain-setup.jpg)
{: caption="Figure 9. Toolchain Integrations Setup" caption-side="top"}

The {{site.data.keyword.cloud_notm}} Platform Toolchain is now set up (shown in Figure 10).

**Tip**: If you do not have an active GitHub session, you are prompted
to log in. Click Authorize Application to allow {{site.data.keyword.cloud_notm}} Platform to access your
GitHub account. If you have an active GitHub session but you have not
entered your password recently, you might be prompted to enter your
GitHub password to confirm. If {{site.data.keyword.cloud_notm}} Platform cannot access the GitHub repo,
the Build Stage of your Delivery Pipeline will be unable to use it as
input.

![created_toolchain](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-010-cd-toolchain.jpg)
{: caption="Figure 10. CD toolchain configured" caption-side="top"}

3.  Click GitHub Tool to open the new repo created by your toolchain
    on GitHub.

4.  It is currently empty so you must add app code from your local
    development environment, but first you need to clone the empty repo.
    To do so, you have options as shown in Figure 11.

![emptytoolchainrepo](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-011-github-next-steps.jpg)
{: caption="Figure 11. Github Repository Code Tab with next steps" caption-side="top"}

Here we'll use the Quick setup option. Click Set up in Desktop. Allow
GitHub desktop to open the link, and select an empty directory as the
location for your new local repo. You now have a directory named the
same as your app with nothing except the .git directory inside. In this
example, it is named COS-WebGallery.

5.  Copy the files and directories from the starter app you modified in
    step 3. Depending on your operating system, it may look like Figure 12.

![localrepo_files](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-012-local-files.jpg)
{: caption="Figure 12. How your local files might appear" caption-side="top"}

6.  Return to GitHub Desktop and note that it detected what you added to
    the repo directory (Figure 13). Type initial commit into the
    summary field, and click Commit to master.

![github_initialcommit](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-013-github-commit.jpg)
{: caption="Figure 13. Commit your changes in Github Desktop" caption-side="top"}

### Create a Git branch
{: #tutorial-create-branch}

Now, you need to create a branch for the local development environment
to use for your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline Build Stage:

1.  Click the branch icon; you are prompted to enter a name for the
    branch (see Figure 14). This example uses Local-dev as the name.

![new_git_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-014-dev-branch.jpg)
{: caption="Figure 14. Use Github Desktop to create a local dev branch" caption-side="top"}

2.  After you create the branch, GitHub compares the local files on the
    Local-dev branch with the files in the repository on the master
    branch and reports No local changes. You can now click Publish to
    add the branch you created on your local repo to your GitHub repo
    (as shown in Figure 15).

![publish_branch](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-015-git-push.jpg)
{: caption="Figure 15. Publish your git branch to your repo's remote origin" caption-side="top"}

Now that the Local-dev branch is published to the GitHub repo in your
toolchain, the build stage of your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline will be
triggered followed by the deploy stage anytime you push a commit to it.
Deploying the app from the Cloud Foundry CLI will no longer be
necessary, as the deployment has been integrated directly into your workflow.

### Setting up {{site.data.keyword.cos_full_notm}} your storage credentials
{: #tutorial-credentials}

You need to configure {{site.data.keyword.cos_short}} credentials for your web application, as well as a 'bucket'
where it will store and retrieve images. The API key you will create will need {{site.data.keyword.cos_short}} HMAC credentials, as defined by 
[Service Credentials](https://cloud.ibm.com/docs/services/cloud-object-storage/hmac?topic=cloud-object-storage-service-credentials). 
You may recognize the terms `access_key_id` and `secret_access_key` as you might have an AWS account, and use 
a credentials file that already has `aws_access_key_id` and `aws_secret_access_key` entries. 

After you have completed creating an API key, and copied the HMAC credentials, complete the following steps:

1.  On the local development environment, place the credentials in the
    Windows path `%USERPROFILE%\\.aws\\credentials` (for Mac/Linux users, the credentials should 
    go into `~/.aws/credentials)`. Example 12 shows the contents of a
    typical credentials file.

```
\[default\]

aws\_access\_key\_id = {access_key_id}

aws\_secret\_access\_key = {secret_access_key}
```
{: caption="Example 12. Credentials as they are defined in your `~/.aws/credentials` file" caption-side="bottom"}

2.  In the web page for the application you created using the CLI command on the {{site.data.keyword.cloud_notm}} Platform, 
    define your required credentials per as environment variables per development best practices by
    logging in to {{site.data.keyword.cloud_notm}} Platform, and under Cloud Foundry Apps, select the
    app COS-WebGallery. From the app menu, click Runtime.

3.  In the Runtime window, click Environment variables at the top of the
    page and scroll to the User-defined section, which allows you to add
    the variables.

4.  Add two variables: one with the value of your access_key_id, using `AWS_ACCESS_KEY_ID` as the name 
    of the key, and another with the value of your secret access key, named `AWS_SECRET_ACCESS_KEY`. 
    These variables and their respective values are what the app uses to authenticate to the 
    {{site.data.keyword.cos_short}} instance when running on {{site.data.keyword.cloud_notm}} 
    Platform (see Figure 16). When you finish with the
    entries, click Save, and {{site.data.keyword.cloud_notm}} Platform will automatically restart the app for you.

![bluemix_env_var](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-016-env-variables.jpg)
{: caption="Figure 16. Runtime Environment Variables defined for your app" caption-side="top"}

Next, over at the {{site.data.keyword.cos_short}} portal for your service instance, 
add a bucket to contain your images. This scenario uses the bucket named `web-images`.


## Customizing a Node.js {{site.data.keyword.cos_full_notm}} Image Gallery Web Application
{: #tutorial-develop}

Because this example uses an MVC architecture, adjusting the directory
structure within your project to reflect architecture is a convenience as well as a best practice. 
The directory structure has a views directory to contain the EJS view templates, a routes
directory to contain the express routes, and a controllers directory as
the place to put the controller logic. Place these items under a parent source
directory named src (see Figure 17).

![directorystructure](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-017-soure-code.jpg)
{: caption="Figure 17. Source code structure for your app" caption-side="top"}

**Tip**: The repo you cloned in earlier contains a directory named
COS-WebGalleryEnd. Viewing the source code in your preferred editor
might be helpful as you follow the next steps. This will be the version
of the COS-WebGallery app that is committed and deployed to {{site.data.keyword.cloud_notm}} Platform
later.

### Designing the app
{: #tutorial-develop-design}

These are the two main tasks that a user should be able to do with the
simple image gallery web application:

  - Upload images from a web browser to the {{site.data.keyword.cos_short}} bucket.
  - View the images in the {{site.data.keyword.cos_short}} bucket in a web browser.

The next steps focus on how to accomplish these two basic functions in a
simple fashion rather than building a fully developed production grade
app. Deploying this tutorial and leaving it exposed and running means that anyone who finds the app 
can perform the same actions: upload and view JPEG images in their browser.

### Developing the app
{: #tutorial-develop-app}

Take a look at the main application file, which is called app.js. This is the code
that we have told Node.js to process first when you start your app with
the `npm start` command (or nodemon). In the `package.json` file, inside the
scripts object, you see how "start" is defined (Example 13). This file
is what {{site.data.keyword.cloud_notm}} Platform uses to tell node to run app.js each time the app
starts. Also use it when testing the app locally.


```
...
"scripts": {
  "start": "node app.js"
},
...
```
{: caption="Example 13. Telling your app how to bootstrap your custom code" caption-side="bottom"}

Our app.js file begins with the code shown in Example 14.
At first, the code uses node to load modules that are needed to get started.
The Express framework creates the app as a singleton simply called `app`. 
The example ends (leaving out a majority of the code for now) telling the app
to listen on the port that is assigned and an environment property, or 3000 by default. 
When successfully launching at the start, it will print a message with the server URL to the console.

```
var express = require('express');
var cfenv = require('cfenv');
var bodyParser = require('body-parser');
var app = express();
...

// start server on the specified port and binding host
var port = process.env.PORT || 3000;
app.listen(port, function() {
    console.log("To view your app, open this link in your browser: http://localhost:" + port);
});
...
```
{: caption="Example 14. Your Web Application has a humble, but powerful, start" caption-side="bottom"}

Let's see how Example 15 shows how to define a path and views. The first line of code tells the
Express framework to use the public directory to serve our static files, which
include any static images and style sheets we use. The lines that follow tell the
app where to find the templates for our views in the
src/views directory, and set our view engine to be EJS. In addition, the framework will 
use the body-parser middleware to expose incoming request
data to the app as JSON. In the closing lines of the example, the express app responds to
all incoming GET requests to our app URL by rendering the index.ejs view
template.

```
...
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

...
```
{: caption="Example 15. Web app views and template locations" caption-side="bottom"}

The following figure shows what the index view template when rendered
and sent to the browser. If you are using `nodemon` you may have noticed 
that your browser refreshed when you saved your changes, and your app should look like Figure 18.

![uploadimageview](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-018-templates.jpg)
{: caption="Figure 18. Your updated web app using templates and views for displays" caption-side="top"}

In Example 16, our view templates share HTML code between the
&lt;head&gt;...&lt;/head&gt; tags, so we placed it into a separate
include template (see Example 16). This template (head-inc.ejs)
contains a scriptlet&mdash;a binding for a JavaScript variable&mdash;for the page title on line 1. 
The `title` variable is set in `app.js`, and passed in as data for our view
template in the line below that. Otherwise, we are simply using some CDN addresses
to pull in Bootstrap CSS, Bootstrap JavaScript, and JQuery. Finally, we add a custom 
static styles.css file from our pubic/style sheets directory.

```
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
{: caption="Example 16. HTML elements from head-inc.ejs" caption-side="bottom"}

The body of the index view contains our bootstrap
styled navigation tabs (see Example 17), and our upload form in a basic layout provided by 
the CSS styles included with bootstrap.

Consider these two specifications for our app:

-   We set our form method to POST and the form-data encoding type as
    multipart/form-data on line 24. For the form action, we send the
    data from our form to the app to the app route "/". Later we do
    additional work in our router logic to handle POST requests to
    that route.

-   We want to display feedback about the status of the attempted file
    upload to the user. This feedback is passed to our view in a
    variable named "status", and is displayed below the upload form.

```
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
{: caption="Example 17. HTML elements from index.ejs" caption-side="bottom"}

Let's take a moment to return to `app.js` in Example 18. The example sets up Express
routes to handle additional requests that will be made to our app. The
code for these routing methods will be in two files under the `./src/routes`
directory in your project:

-   imageUploadRoutes.js: This file handles what happens when the user
    selects an image and clicks Upload.

-   galleryRoutes.js: This file handles requests when the user clicks
    the Gallery tab to request the imageGallery view.

```
...
var imageUploadRoutes = require('./src/routes/imageUploadRoutes')(title);
var galleryRouter = require('./src/routes/galleryRoutes')(title);

app.use('/gallery', galleryRouter);
app.use('/', imageUploadRoutes);

...
```
{: caption="Example 18. Node Express router examples" caption-side="bottom"}

#### Image upload
{: #tutorial-develop-image-upload}

See the code from imageUploadRoutes.js in Example 19. We must create an instance
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

``` 
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
{: caption="Example 19. Node express router details" caption-side="bottom"}

In comparison, the code for the galleryRouter is a model of simplicity. We follow the same pattern
that we did with imageUploadRouter and require galleryController on
lines 6 - 7, then set up our route on line 9. The main difference is we
are routing HTTP GET requests rather than POST, and sending all the
output in the response from getGalleryImages, which is exposed by the
galleryController on line 10.

``` 
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
{: caption="Example 20. Node express router details" caption-side="bottom"}

We next turn our attention to the controller for the gallery.

Please note how we set up the `multer` upload in the Example 21, 
(which truncates some code we'll ignore for now). We
require modules `ibm-cos-sdk`, `multer`, and `multer-s3`. The code shows how to
configure an S3 object that points to an {{site.data.keyword.cos_short}} server endpoint. We are
statically setting values such as the endpoint address, region, and
bucket for simplicity, but they could easily be referenced from an
environment variable or JSON configuration file.

```
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
{: caption="Example 21. Node express controller details" caption-side="bottom"}

We define `upload` as used in the imageUploadRouter by creating a new
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
(TIP: use a timestamp as the filename for maintinaing filename uniqueness). 

For local testing, a
helpful task is to print the file object to the console, `console.log(file)`. 
We perform a local test of the Upload form and show the output from the
console log of the file in the Example 22.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```
{: caption="Example 22. Console display of debug object" caption-side="bottom"}

Figure 22 shows the feedback from our callback saying it was
a successful upload.

![localtest1](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-019-success.jpg)
{: caption="Figure 19. Success!" caption-side="top"}

#### Image retrieval and display
{: #tutorial-develop-image-display}

Remember, back in app.js, the line of code `app.use('/gallery', galleryRouter);` 
tells the express framework to use that router when the “/gallery” route is requested. 
That router, if you recall, uses galleryController.js (see the code in Example 23), we define the
getGalleryImages function, the signature of which we have seen previously. Using the same `s3`
object that we set up for our image upload function, we call the function named 
`listObjectsV2`. This function returns the index data defining each of the
objects in our bucket. To display images within HTML, we need an image URL for each
JPEG image in our `web-images` bucket to display in our view template. The
closure with the data object returned by `listObjectsV2` contains metadata 
about each object in our bucket. 

The code loops through the `bucketContents` and search for any object key ending in ".jpg," and
create a parameter to pass to the S3 getSignedUrl function. This
function returns a signed URL for any object when we pass it the
object’s bucket name and key. In the callback function we save each URL
in an array, and pass it to the HTTP server response method `res.render` 
as the value to a property named `imageUrls`.

```
...
    var imageUrlList = [];
    
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

...
```
{: caption="Example 23. Partial contents of galleryController.js" caption-side="bottom"}

The last code example in this tutorial shows the body for the galleryView template with the code 
needed to display the images. We get the imageUrls array from the res.render() 
method and iterate over a pair of nested &lt;div&gt;&lt;/div&gt; tags where 
the image URL will make a GET request for the image when the /gallery route 
is requested.

``` 
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
{: caption="Example 24. Loop and output scriptlets used in the gallery template" caption-side="bottom"}

We test it locally from http://localhost:3000/gallery and see our image
in Figure 20.

![localtest2](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-020-image-display.jpg)
{: caption="Figure 20. Images uploaded to the bucket are on display" caption-side="top"}

## Committing to Git
{: #tutorial-develop-commit}


Now that the basic features of the app are working, we will commit our code
to our local repo, and then push it to GitHub. Using GitHub Desktop, we
click Changes (see Figure 21), type a summary of the changes in
the Summary field, and then click Commit to Local-dev. 

![commitupdates](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-021-changes-in-git.jpg)
{: caption="Figure 21. Changes ready for commit in Git" caption-side="top"}

When we click
Sync, our commit is sent to the remote Local-dev branch that we
published to GitHub, and this action starts the Build Stage followed by
the Deploy Stage in our Delivery Pipeline, as exemplified in the last figure in this tutorial. 

![pipeline_triggled_aftersync](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/web-app-tutorial-022-final-pipeline.jpg)
{: caption="Figure 22. CD Delivery Pipeline" caption-side="top"}

Congratulations! We have gone from beginning to end along this
path to build a web application image gallery using the {{site.data.keyword.cloud_notm}} Platform. 
Each of the concepts we've covered in this basic introduction can be explored further. Good luck!
