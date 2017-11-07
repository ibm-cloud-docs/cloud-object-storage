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

# Tutorial: Image Gallery

This tutorial introduces how a simple web application can be built on the {{site.data.keyword.cloud}}.  This application uses {{site.data.keyword.cos_full}} as the back-end storage for a Node.js application that allows a user to upload and view photos or other images.

## Getting started

Before getting started with writing any code, you must ensure that you
have the following items set up:

  - {{site.data.keyword.cloud_notm}} Platform
  - Node.js
  - Git

### Installing Node.js

The app uses Node.js as the server-side JavaScript engine to run the
JavaScript code. You must install node locally so that you can use the
node package manager (NPM). It is also helpful to have Node.js installed
so that you can test your code locally. Go to the
[Node.js](https://nodejs.org/en/download/releases/) releases web page
and download the Long Term Support (LTS) Version of Node.js, which
matches the latest version supported by the SDK for Node.js buildpack on
{{site.data.keyword.cloud_notm}} Platform. At the time of this writing the latest buildpack is Version
3.10, and it supports Node.js Version 6.9.4. You can find information
about the latest {{site.data.keyword.cloud_notm}} SDK for Node.js buildpack on the [SDK for
Nodejs latest
updates](https://console.ng.bluemix.net/docs/runtimes/nodejs/updates.html#latest_updates)
page. Run the Node.js installer to set up Node.js and NPM on your
system.

### Installing Git

Git is the most widely used source code versioning system in the
industry. We use Git later when we create a toolchain in {{site.data.keyword.cloud_notm}} Platform for
continuous delivery. If you do development regularly, you probably are
already familiar with Git. If you do not have a GitHub account, create a
free public personal account at the [Github](https://github.com/join)
website; otherwise, feel free to use any other account you might have.

Go to the [Github Desktop](https://desktop.github.com/) page to download
GitHub Desktop, and then run the installer. When the installer finishes,
you are prompted to log in to GitHub with your account.

In the Log in window (see figure below), enter the name and email you
want displayed publicly (assuming you have a public account) for any
commits to your repository.

![github_desktop_setup](https://cloud.githubusercontent.com/assets/19173079/24821330/a1c718e4-1bb3-11e7-8362-e3c6aa37bc7d.png)


You do not have to create any repositories yet. You might notice a
repository named Tutorial that is included with GitHub Desktop to help
familiarize you with the flow. Feel free to experiment with it.

## Creating the Web Gallery app on {{site.data.keyword.cloud_notm}} Platform

To create a Cloud Foundry app, log in to [{{site.data.keyword.cloud_notm}} Platform](https://console.ng.bluemix.net/docs/runtimes/nodejs/updates.html)
and click Create App (see figure below).

![bluemix_create_app](https://cloud.githubusercontent.com/assets/19173079/24821420/0d9b0af8-1bb4-11e7-80e3-cd1d91d19460.jpg)

Then, under Cloud Foundry Apps, select SDK for Node.js (see figure
below).

![cf_app_nodejs](https://cloud.githubusercontent.com/assets/19173079/24821453/52a651ac-1bb4-11e7-923e-e59f0b89dfec.jpg)


The following figure shows the app creation page where you give a name
to the app. Call it something descriptive, such as COS-WebGallery. The
Host name is populated automatically to reflect the App name field. The
Host name with the Domain name becomes the address that you use to view
the app on {{site.data.keyword.cloud_notm}} Platform. The Host name can be updated later if necessary, so
accept the default and click Create. {{site.data.keyword.cloud_notm}} Platform creates a starter app,
deploys and starts it for us.

![clickcreatenodeapp](https://cloud.githubusercontent.com/assets/19173079/24821507/97cf54ea-1bb4-11e7-928c-611546f6d980.jpg)

Now that the app is created and running, click View App from the app’s
Getting Started page to see it in a new browser window. It was created
with a basic Hello World starter app as a placeholder (see figure
below).

![initiahhelloworldapp](https://cloud.githubusercontent.com/assets/19173079/24821547/da5bc302-1bb4-11e7-84c7-d0143c40d5c3.jpg)

Notice back on the Getting Started page the prerequisites that you need
in for developing a Node.js app on {{site.data.keyword.cloud_notm}} Platform are listed. You already
created your {{site.data.keyword.cloud_notm}} Platform account, and installed Node.js. Download the [Cloud Foundry CLI](https://github.com/cloudfoundry/cli). Then run the installer. You
can use the tool to log in to {{site.data.keyword.cloud_notm}} Platform and interact directly with your
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
    in the following example.

```
git clone https://github.com/IBMRedbooks/IBMRedbooks-SG248385-Cloud-Object-Storage-as-a-Service.git
```

2.  Run the app locally. Open a CLI and change your working directory to
    the COS-WebGalleryStart directory. Notice the Node.js dependencies
    listed in the package.json file. Download them using the command
    shown in the following example.

```
npm install
```

Run the app using the command shown in the following example.

```
npm start
```

Open a browser and view your app on the address and port that is output
to the console, <http://localhost:3000>.

**Tip**: To restart the app locally, kill the node process (Ctrl+C) to
stop it, and use npm start again. Using nodemon to restart your app when
it detects a change saves you time. Install nodemon globally like this:
`npm install -g nodemon`. Then run it from the command line in your app
directory using: `nodemon`, to have nodemon start your app.

3.  Prepare the app for deployment. Update the application name property
    value in the `manifest.yml` file from COS-WebGallery, to the name you
    entered for your app on {{site.data.keyword.cloud_notm}} Platform. The COS-WebGallery manifest.yml
    looks like the following example. Also update the package.json file
    located in the app root directory for your app to reflect the name
    of your app, and your name as the author.


```
applications:

- name: COS-WebGalery

random-route: true

memory: 256M
```


4.  Deploy the app to {{site.data.keyword.cloud_notm}} Platform. To get the starter app with your changes
    to {{site.data.keyword.cloud_notm}} Platform, deploy it using the Cloud Foundry CLI:

a.  Set the API Endpoint for your region by using the api command (as
    shown in the following example). if you do not know your regional
    API endpoint URL see the Getting Started page.


```
 cf api &lt;API Endpoint&gt;
```

b.  Log in to {{site.data.keyword.cloud_notm}} Platform by using the login command. You can specify
    optional parameters if you want: your organization with option -o,
    and the space with option -s (as shown in the following example).


```
cf login
```

c.  Deploy the app to {{site.data.keyword.cloud_notm}} Platform with the push command (as shown in the
    following example).


```
cf push
```

The example below shows the commands we used to deploy the COS-WebGallery app.

```
cf api https://api.ng.bluemix.net
cf login -u myaccount@us.ibm.com -o “IBM Redbooks” -s scenarios
cf push
```

If successful, Cloud Foundry reports that the app was uploaded,
successfully deployed, and started. If you are logged in to the {{site.data.keyword.cloud_notm}} Platform
web console, you are notified there also of the status of your app (see
figure below).

![app_stage_notification](https://cloud.githubusercontent.com/assets/19173079/24821846/9f35e1a2-1bb6-11e7-9c58-45c545ef6494.jpg)

You can verify that the app was deployed by visiting the app URL
reported by Cloud Foundry with a browser, or from the {{site.data.keyword.cloud_notm}} Platform web
console by clicking View App button.

5.  Test the app. The visible change from the default app template that
    was deployed at creation to the starter app shown in the following
    proves that deploying the app to {{site.data.keyword.cloud_notm}} Platform was successful.

![verify_push](https://cloud.githubusercontent.com/assets/19173079/24821897/e7f82bca-1bb6-11e7-848c-29878a6fcc78.jpg)


### Creating a {{site.data.keyword.cloud_notm}} Platform toolchain

You are almost ready to start working on the Web Gallery app, but before
you start coding you must have a source repository for the code. It must
be accessible from both {{site.data.keyword.cloud_notm}} Platform and the local development environment.
We will want to both push changes to {{site.data.keyword.cloud_notm}} Platform, and pull down the changes
made in the {{site.data.keyword.cloud_notm}} Platform to our local development environment. To do
so, create a Delivery Pipeline for the app in {{site.data.keyword.cloud_notm}} Platform by completing the
following steps:

1.  After signing in to {{site.data.keyword.cloud_notm}} Platform, select the COS-WebGallery app, and
    from the app Overview window, scroll to Continuous delivery and
    click Enable (see figure below).

![continious_delivery_enable](https://cloud.githubusercontent.com/assets/19173079/24822095/3ffb0b70-1bb8-11e7-8d6d-190f58db2364.jpg)

2.  Set up the Toolchain Integrations. Scroll down to see the
    information that is shown in the following figure. The Continuous
    Delivery Toolchain template creates a GitHub repository, Delivery
    Pipeline, and integrates Eclipse Orion Web IDE to allow the editing
    of your app code in the cloud. Everything is populated with the
    necessary values to create the toolchain. Click Create.

![toolchain_integrations_setup](https://cloud.githubusercontent.com/assets/19173079/24822139/7f5c43ba-1bb8-11e7-8610-6441b7d1a963.jpg)


The {{site.data.keyword.cloud_notm}} Platform Toolchain is now set up (as shown in the figure below).

**Tip**: If you do not have an active GitHub session, you are prompted
to log in. Click Authorize Application to allow {{site.data.keyword.cloud_notm}} Platform to access your
GitHub account. If you have an active GitHub session but you have not
entered your password recently, you might be prompted to enter your
GitHub password to confirm. If {{site.data.keyword.cloud_notm}} Platform cannot access the GitHub repo,
the Build Stage of your Delivery Pipeline will be unable to use it as
input.

![created_toolchain](https://cloud.githubusercontent.com/assets/19173079/24822154/adf8fdb2-1bb8-11e7-8cae-7631f549c080.jpg)

3.  Click GitHub Tool to open the new repo created by your toolchain
    on GitHub.

4.  It is currently empty so you must add app code from your local
    development environment, but first you need to clone the empty repo.
    To do so, you have options as shown in the following figure.

![emptytoolchainrepo](https://cloud.githubusercontent.com/assets/19173079/24822196/f5efc100-1bb8-11e7-903f-c3562598f2b2.jpg)

This example uses the Quick setup option. Click Set up in Desktop. Allow
GitHub desktop to open the link, and select an empty directory as the
location for your new local repo. You now have a directory named the
same as your app with nothing except the .git directory inside. In this
example, it is named COS-WebGallery.

5.  Copy the files and directories from the starter app you modified in
    step 3. It looks like the following figure.

![localrepo_files](https://cloud.githubusercontent.com/assets/19173079/24822238/3aab33ba-1bb9-11e7-9476-78975d71e208.jpg)

6.  Return to GitHub Desktop and notice it detected what you added to
    the repo directory (see figure below). Type initial commit into the
    summary field, and click Commit to master.

![github_initialcommit](https://cloud.githubusercontent.com/assets/19173079/24822265/68a90e5e-1bb9-11e7-9bbe-d466ae5bb68d.jpg)

### Create a Git branch

Now, you need to create a branch for the local development environment
to use for your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline Build Stage:

1.  Click the branch icon; you are prompted to enter a name for the
    branch (see figure below). This example uses Local-dev as the name.

![new_git_branch](https://cloud.githubusercontent.com/assets/19173079/24822302/aba13902-1bb9-11e7-92af-be919c9a171e.jpg)

2.  After you create the branch, GitHub compares the local files on the
    Local-dev branch with the files in the repository on the master
    branch and reports No local changes. You can now click Publish to
    add the branch you created on your local repo to your GitHub repo
    (see figure below).

![publish_branch](https://cloud.githubusercontent.com/assets/19173079/24822322/de53cfa4-1bb9-11e7-8a2d-768acdaa4f95.jpg)

Now that the Local-dev branch is published to the GitHub repo in your
toolchain, the build stage of your {{site.data.keyword.cloud_notm}} Platform Delivery Pipeline will be
triggered followed by the deploy stage anytime you push a commit to it.
Deploying the app from the Cloud Foundry CLI will no longer be
necessary.

### Setting up {{site.data.keyword.cos_full_notm}} credentials

You need to setup {{site.data.keyword.cos_short}} credentials for the application, and a bucket
where it will store and retrieve images. After you have them, complete
the following steps:

1.  On the local development environment, place the credentials in the
    Windows path %USERPROFILE%\\.aws\\credentials (for Mac/Linux, they
    go into ~/.aws/credentials). Example 5-9 shows the contents of a
    credentials file.

```
\[default\]

aws\_access\_key\_id = {IAM-API-KEY}

aws\_secret\_access\_key = {RESOURCE-INSTANCE-ID}
```
2.  In {{site.data.keyword.cloud_notm}} Platform, set up the credentials as environment variables by
    logging in to {{site.data.keyword.cloud_notm}} Platform, and under Cloud Foundry Apps, select the
    app COS-WebGallery. From the app menu, click Runtime.

3.  In the Runtime window, click Environment variables at the top of the
    page and scroll to the User-defined section, which allows you to add
    the variables.

4.  Add two variables: API key and resoruce instance ID. These variables and their respective
    values are what the app uses to authenticate to the {{site.data.keyword.cos_short}} instance
    when running on {{site.data.keyword.cloud_notm}} Platform (see figure below). When you finish with the
    entries, click Save, and {{site.data.keyword.cloud_notm}} Platform restarts the app.

![bluemix_env_var](https://cloud.githubusercontent.com/assets/19173079/24822607/07019ace-1bbc-11e7-9d71-db6d53d3dc7a.jpg)

Head over to the {{site.data.keyword.cos_short}} portal for your service instance and add a bucket to contain your images. This scenario uses the bucket named web-images.


## Developing a simple {{site.data.keyword.cos_full_notm}} Web Gallery


Because this example uses an MVC architecture, adjusting the directory
structure to reflect architecture is necessary. The directory structure
has a views directory to contain the EJS view templates, a routes
directory to contain the express routes, and a controllers directory as
the place to put the controller logic. Place these items under a source
directory that is named src (see figure below).

![directorystructure](https://cloud.githubusercontent.com/assets/19173079/24822675/86a7084a-1bbc-11e7-9567-4fc6cea7a939.jpg)


**Tip**: The repo you cloned in earlier contains a directory named
COS-WebGalleryEnd. Viewing the source code in your preferred editor
might be helpful as you follow the next steps. This will be the version
of the COS-WebGallery app that is committed and deployed to {{site.data.keyword.cloud_notm}} Platform
later.

### Designing the app

These are the two main tasks that a user should be able to do with the
simple web app:

  - Upload images from a web browser to the {{site.data.keyword.cos_short}} bucket.
  - View the images in the {{site.data.keyword.cos_short}} bucket in a web browser.

The next steps focus on how to accomplish these two basic functions in a
simple fashion rather than building a fully developed production grade
app.

### Developing the app

Look at the main application file, which is app.js. This is the code
that we have told Node.js to process first when you start your app with
the npm start command (or nodemon). In the package.json file, inside the
scripts object, you see how "start" is defined (Example 5-10). This file
is what {{site.data.keyword.cloud_notm}} Platform uses to tell node to run app.js each time the app
starts. Also use it when testing the app locally.


```
...
"scripts": {
"start": "node app.js"
},
...
```

The following figure shows the beginnings for the application in app.js.
Lines 1 - 3 tell the node to load modules that are need to get started.
Line 4 creates the express app by using the express module. Line 25 gets
the Cloud Foundry environment object. Lines 28 - 32 tell the express app
to listen on the port that is assigned to the port property. We print a
message with the server URL to the console.

![app_1](https://cloud.githubusercontent.com/assets/19173079/24822730/1bb0fc0c-1bbd-11e7-9d61-95478757dcb8.jpg)

The next figure shows how to define a path and views. Line 7 tells the
express app to use the public directory to serve our static files, which
include any static images and style sheets we use. Lines 8 - 9 tells the
express app where to find the view templates for our views in the
src/views directory, and set our view engine to be EJS. Line 10 tells
express to use the body-parser middleware to expose incoming request
data to the app as JSON. In lines 12 - 16, the express app responds to
all incoming GET requests to our app URL by rendering the index.ejs view
template.

![app_2](https://cloud.githubusercontent.com/assets/19173079/24822753/488a863a-1bbd-11e7-9fe9-ee376e8fe2e4.jpg)


The following figure shows what the index view template when rendered
and sent to the browser.

![uploadimageview](https://cloud.githubusercontent.com/assets/19173079/24822932/f087e44e-1bbe-11e7-9349-93ff489eeb36.jpg)


In this example, our view templates share HTML code between the
&lt;head&gt;...&lt;/head&gt; tags, so we placed it into a separate
include template (see figure below). This template (head-inc.ejs)
contains a scriptlet for the page title on line 1. The title variable is
being set in app.js on line 12, and passed in as data for our view
template on line 15. Otherwise, we are simply using some CDN addresses
to pull in Bootstrap CSS, Bootstrap JavaScript, and JQuery. We use a
static styles.css file from our pubic/style sheets directory.

![view_head-inc](https://cloud.githubusercontent.com/assets/19173079/24822956/18ec3ec6-1bbf-11e7-9240-3f5a913c1320.jpg)


The body of the index view (see figure below), contains our bootstrap
styled navigation tabs, and our upload form in a basic bootstrap.
Consider these two notes:

-   We set our form method to POST and the form-data encoding type as
    multipart/form-data on line 24. For the form action, we send the
    data from our form to the app to the app route "/". Later we do
    additional work in our router logic to handle POST requests to
    that route.

-   We want to display feedback about the status of the attempted file
    upload to the user. This feedback is passed to our view in a
    variable named "status", and is displayed below the upload form on
    line 31.

![view_index-body](https://cloud.githubusercontent.com/assets/19173079/24822803/ae19fbd4-1bbd-11e7-8712-a720050cc3a6.jpg)

The following figure returns to app.js. Lines 18 - 19 sets up express
routes to handle additional requests that will be made to our app. The
code for these routers will be in two files under the ./src/routes
directory:

-   imageUploadRoutes.js: This file handles what happens when the user
    selects an image and clicks Upload.

-   galleryRoutes.js: This file handles requests when the user clicks
    the Gallery tab to request the imageGallery view.

![app_3](https://cloud.githubusercontent.com/assets/19173079/24822769/5ed0c49a-1bbd-11e7-9d48-39c68428314d.jpg)


#### Image upload

See imageUploadRoutes.js in the figure below. We must create an instance
of a new express router and name it imageUploadRouter in lines 1 - 2.
Then, on line 5, we create a function that returns imageUploadRouter,
and assign it to a variable called "router". We export the function in
"router"on line 28 to make it accessible to app.js. On line 7, we
require a file named galleryController.js. Because some logic is
dedicated to controlling how we upload our images, we put that logic in
this function and save it in our ./src/controllers directory.

Line 12 is where our imageUploadRouter is told to route requests for the
root app route ("/") when the HTTP POST method is used. Inside the post
function of our imageUploadRouter, we use middleware from the multer and
multer-s3 modules which is exposed by the galleryController as upload.
The middleware takes the data and file from our Upload form POST,
processes it, and runs a callback function. In the callback function on
line 13 - 22, we check that we get an HTTP status code of 200, and that
we had at least one file in our request object to upload. Based on those
conditions, we set the feedback in our status variable and render the
index view template with the new status.

![imguploadrouter](https://cloud.githubusercontent.com/assets/19173079/24822982/6d0aac40-1bbf-11e7-9bb5-7dd0a4fb52bc.jpg)

Look at how we set up the multer upload in the following figure. We
require modules aws-sdk, multer, and multer-s3. Lines 6 - 7 show how to
configure an S3 object that points to an {{site.data.keyword.cos_short}} server endpoint. We are
statically setting values such as the endpoint address, region, and
bucket for simplicity, but they could easily be referenced from an
environment variable or JSON configuration file.

![gallerycontroller](https://cloud.githubusercontent.com/assets/19173079/24822999/95634fda-1bbf-11e7-9414-c95b4e15eeb2.jpg)


We define upload used by imageUploadRouter on line 11 by creating a new
multer instance with a storage property on line 12. This property tells
multer where to send the file from our multipart/form-data. Since IBM
COS uses an implementation of the S3 API, we set storage to be an
s3-multer object. This s3-multer object contains an s3 property that we
have assigned to our s3 object from line 7, and a bucket property that
we have assigned the myBucket variable from line 8, which is assigned a
value of “web-images”. The s3-multer object now has all the data
necessary to connect and upload files to our {{site.data.keyword.cos_short}} bucket when it
receives data from the upload form. The name or key of the uploaded
object will be the original file name taken from the file object when it
is stored in our {{site.data.keyword.cos_short}} “web-images” bucket. For local testing, a
helpful task is to print the file object to the console, on line 17.

We perform a local test of the Upload form and the output from the
console log of the file in the following example.

```
{ fieldname: 'img-file',
originalname: 'Chrysanthemum.jpg',
encoding: '7bit',
mimetype: 'image/jpeg' }
```

The following figure shows that feedback from our callback saying it was
a successful upload.

![localtest1](https://cloud.githubusercontent.com/assets/19173079/24823021/cf3704cc-1bbf-11e7-8190-932e99cded91.jpg)

#### Image retrieval and display

The following figure refers to app.js. Line 19 creates galleryRouter,
and tells express to use it when the “/gallery” route is requested. Look
at the galleryRoutes.js file that is used to define galleryRouter.

![app_3](https://cloud.githubusercontent.com/assets/19173079/24822769/5ed0c49a-1bbd-11e7-9d48-39c68428314d.jpg)

1.  Defining which router to use for each path

The next figure shows galleryRoutes.js where we define the express
router assigned to galleryRouter on line 2. We follow the same pattern
that we did with imageUploadRouter and require galleryController on
lines 6 - 7, then set up our route on line 9. The main difference is we
are routing HTTP GET requests rather than POST, and sending all the
output in the response from getGalleryImages, which is exposed by the
galleryController on line 10.

![galleryroutes](https://cloud.githubusercontent.com/assets/19173079/24823054/08cceb0c-1bc0-11e7-9853-ece1cfd12bee.jpg)


Referring to galleryController.js (see figure below), we define the
getGalleryImages function we just saw on line 22. Using the same S3
object that we set up for our image upload function, we call a function
that named listObjectsV2 on line 26. This function returns data of the
objects in our bucket. To display images, we need an image URL for each
JPEG image in our web-images bucket to display in our view template. The
content on line 28 is an array map from the data object returned by
listObjectsV2 containing metadata about each object in our bucket. We
loop the content and search for any object key ending in .jpg, and
create a parameter to pass to the S3 getSignedUrl function. This
function returns a signed URL for any object when we pass it the
object’s bucket name and key. In the callback function we save each URL
in an array, and pass it res.render as imageUrls.

![gallerycontroller_getimages](https://cloud.githubusercontent.com/assets/19173079/24823067/28139b46-1bc0-11e7-8cc2-e202b51c4603.jpg)

1.  Retrieve the .jpg image URLs from {{site.data.keyword.cos_full_notm}}

The following figure shows the galleryView EJS template body. We get the
imageUrls array from the res.render() method and iterate over a pair of
nested &lt;div&gt;&lt;/div&gt; tags where the image URL will make a GET
request for the image when the /gallery route is requested.

![galleryview](https://cloud.githubusercontent.com/assets/19173079/24822878/6baf3a38-1bbe-11e7-8063-100e63480c02.jpg)


We test it locally from http://localhost:3000/gallery and see our image
in the following figure.

![localtest2](https://cloud.githubusercontent.com/assets/19173079/24822869/5310d658-1bbe-11e7-80fc-7a725314f7f5.jpg)


Committing to Git
-----------------

Now that the basic features of the app are working, we commit our code
to our local repo, and push it to GitHub. Back in GitHub Desktop, we
click Changes (see first figure below), type a summary of the changes in
the Summary field, and then click Commit to Local-dev. When we click
Sync, our commit is sent to the remote Local-dev branch that we
published to GitHub, and this action starts the Build Stage followed by
the Deploy Stage in our Delivery Pipeline

![commitupdates](https://cloud.githubusercontent.com/assets/19173079/24822835/0a6cdd66-1bbe-11e7-89ee-d57b8d64d4db.jpg)

![pipeline_triggled_aftersync](https://cloud.githubusercontent.com/assets/19173079/24822828/f29efe26-1bbd-11e7-8b9a-c472ea03ee2b.jpg)
