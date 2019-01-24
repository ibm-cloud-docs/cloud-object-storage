---

copyright:
  years: 2017
lastupdated: "17-10-2018"

---
{:new_window: target="_blank"}
{:shortdesc: .shortdesc}
{:codeblock: .codeblock}
{:pre: .pre}
{:screen: .screen}
{:tip: .tip}
{:important: .important}
{:note: .note}

# Using `rclone`

## Install `rclone`

`rclone` is a Go program and comes as a single binary file.  It is useful for keeping directories synchronized and for migrating data between storage platforms.

### Quickstart Installation
*  Download the relevant binary - https://rclone.org/downloads. 
*  Extract the `rclone` or `rclone.exe` binary from the archive.
*  Run `rclone config` to setup.

### Script Installation

To install rclone on Linux/macOS/BSD systems, run:

```
curl https://rclone.org/install.sh | sudo bash
```

For beta installation, run:

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

This script checks the version of rclone installed first and won’t re-download if not needed.
{:note}

### Linux installation from precompiled binary

Fetch and unpack:

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

Copy binary file:

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

Install manpage:

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

Run `rclone config` to setup:

```
rclone config
```

### macOS installation from precompiled binary

Download the latest version of rclone:

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

Unzip the download and cd to the extracted folder:

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

Move rclone to your $PATH. You will be prompted for your password.

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

The `mkdir` command is safe to run, even if the directory already exists.
{:tip}

Remove the leftover files.

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

Run `rclone config` to setup:

```
rclone config
```

## Configure access to IBM COS

  1. Run `rclone config` and select `n` for a new remote.

```
No remotes found - make a new one
	n) New remote
	s) Set configuration password
	q) Quit config
	n/s/q> n
```

  2. Enter the name for the configuration
```
name> <YOUR NAME>
```

  3. Select “s3” storage.

```
Choose a number from below, or type in your own value
	1 / Alias for a existing remote
	\ "alias"
	2 / Amazon Drive
	\ "amazon cloud drive"
	3 / Amazon S3 Complaint Storage Providers (Dreamhost, Ceph, Minio, IBM COS)
	\ "s3"
	4 / Backblaze B2
	\ "b2"
[snip]
	23 / http Connection
  \ "http"
Storage> 3
```

  4. Select IBM COS as the S3 Storage Provider.

```
Choose the S3 provider.
Enter a string value. Press Enter for the default ("")
Choose a number from below, or type in your own value
	 1 / Amazon Web Services (AWS) S3
	   \ "AWS"
	 2 / Ceph Object Storage
	 \ "Ceph"
   3 / Digital Ocean Spaces
   \ "Digital Ocean"
	 4 / Dreamhost DreamObjects
   \ "Dreamhost"
   5 / IBM COS S3
	 \ "IBMCOS"
	 [snip]
	 Provider>5
```

  5. Enter False to allow you to enter your credentials.

```
Get AWS credentials from the runtime (environment variables or EC2/ECS meta data if no env vars). 
Only applies if access_key_id and secret_access_key is blank.
Enter a boolean value (true or false). Please Enter for the default ("false").
Choose a number from below, or type in your own value
	 1 / Enter AWS credentials in the next step
   \ "false"
   2 / Get AWS credentials from the environment (env vars or IAM)
   \ "true"
   env_auth>false
```

  6. Enter the Access Key and Secret.

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. Specify the endpoint for IBM COS. For Public IBM COS, choose from the option below. For On Premise IBM COS, enter an endpoint address.

```
Endpoint for IBM COS S3 API.
	Specify if using an IBM COS On Premise.
	Choose a number from below, or type in your own value
	 1 / US Cross Region Endpoint
	   \ "s3-api.us-geo.objectstorage.softlayer.net"
	 2 / US Cross Region Dallas Endpoint
	   \ "s3-api.dal.us-geo.objectstorage.softlayer.net"
	 3 / US Cross Region Washington DC Endpoint
	   \ "s3-api.wdc-us-geo.objectstorage.softlayer.net"
	 4 / US Cross Region San Jose Endpoint
	   \ "s3-api.sjc-us-geo.objectstorage.softlayer.net"
	 5 / US Cross Region Private Endpoint
	   \ "s3-api.us-geo.objectstorage.service.networklayer.com"
[snip]
	34 / Toronto Single Site Private Endpoint
	   \ "s3.tor01.objectstorage.service.networklayer.com"
	endpoint>1
```

  8. Specify a IBM COS Location Constraint. The location constraint must match endpoint when using IBM Cloud Public. For on-prem COS, do not make a selection from this list, hit enter

```
 1 / US Cross Region Standard
	   \ "us-standard"
	 2 / US Cross Region Vault
	   \ "us-vault"
	 3 / US Cross Region Cold
	   \ "us-cold"
	 4 / US Cross Region Flex
	   \ "us-flex"
	 5 / US East Region Standard
	   \ "us-east-standard"
[snip]
	32 / Toronto Flex
	   \ "tor01-flex"
location_constraint>1
```

  9. Specify a canned ACL. IBM Cloud (Storage) supports “public-read” and “private”. IBM Cloud(Infra) supports all the canned ACLs. On-Premise COS supports all the canned ACLs.

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 / / Owner gets FULL_CONTROL. No one else has access rights (default). This acl is available on IBM Cloud (Infra), IBM Cloud (Storage), On-Premise COS
	   \ "private"
	 2 / Owner gets FULL_CONTROL. The AllUsers group gets READ access. This acl is available on IBM Cloud (Infra), IBM Cloud (Storage), On-Premise IBM COS
	   \ "public-read"
	 3 / Owner gets FULL_CONTROL. The AllUsers group gets READ and WRITE access. This acl is available on IBM Cloud (Infra), On-Premise IBM COS
	   \ "public-read-write"
	 4 / Owner gets FULL_CONTROL. The AuthenticatedUsers group gets READ access. Not supported on Buckets. This acl is available on IBM Cloud (Infra) and On-Premise IBM COS
	   \ "authenticated-read"
acl>1
```

  10. Review the displayed configuration and accept to save the “remote” then quit. The config file should look like this

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3-api.us-geo.objectstorage.softlayer.net
	location_constraint = us-standard
	acl = private
```

## Commands

### Create a bucket.

```
rclone mkdir RemoteName:newbucket
```

### List available buckets.

```
rclone lsd RemoteName:
```

### List contents of a bucket.

```
rclone ls RemoteName:newbucket
```

### Copy a file from local to remote.

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### Copy a file from remote to local.

```
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### Delete a file on remote.

```
rclone delete RemoteName:newbucket/file.txt
```

### List Commands.

There are several related list commands
* `ls` to list size and path of objects only
* `lsl` to list modification time, size and path of objects only
* `lsd` to list directories only
* `lsf` to list objects and directories in easy to parse format
* `lsjson` to list objects and directories in JSON format

## `rclone` sync

Make source and dest identical, modifying destination only.

### Synopsis

Sync the source to the destination, changing the destination only. Doesn’t transfer unchanged files, testing by size and modification time or MD5SUM. Destination is updated to match source, including deleting files if necessary.

Since this can cause data loss, test first with the `--dry-run` flag to see exactly what would be copied and deleted.
{:important}

Note that files in the destination won’t be deleted if there were any errors at any point.

It is always the contents of the directory that is synced, not the directory so when source:path is a directory, it’s the contents of source:path that are copied, not the directory name and contents. See extended explanation in the `copy` command above if unsure.

If dest:path doesn’t exist, it is created and the source:path contents go there.

```
rclone sync source:path dest:path [flags]
```

### Using rclone from multiple locations at the same time

You can use rclone from multiple places at the same time if you choose different subdirectory for the output, eg

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

If you sync to the same directory then you should use rclone copy otherwise the two rclones may delete each others files, eg

```
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### --backup-dir=DIR

When using `sync`, `copy` or `move` any files which would have been overwritten or deleted are moved in their original hierarchy into this directory.

If `--suffix` is set, then the moved files will have the suffix added to them. If there is a file with the same path (after the suffix has been added) in DIR, then it will be overwritten.

The remote in use must support server side move or copy and you must use the same remote as the destination of the sync. The backup directory must not overlap the destination directory.

For example

```
rclone sync /path/to/local remote:current --backup-dir remote:old
```

will `sync` `/path/to/local` to `remote:current`, but for any files which would have been updated or deleted will be stored in `remote:old`.

If running `rclone` from a script you might want to use today’s date as the directory name passed to `--backup-dir` to store the old files, or you might want to pass `--suffix` with today’s date.

## `rclone` daily sync

Scheduling a backup is important to automating backups. Depending on your platform will depend on how you do this. Windows can use Task Scheduler while Mac OS and Linux can use crontabs.

### Syncing a Directory

`Rclone` will sync a local directory with the remote container, storing all the files in the local directory in the container. `Rclone` uses the syntax, `rclone sync source destination`, where `source` is the local folder and `destination` is the container within your IBM COS.

For example

```
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

You may already have a destination created, but if you don't then you can create a new bucket using the steps above.

### Scheduling a Job

Before scheduling a job, make sure you have done your initial upload and it has completed.

#### Windows

1. Create a text file called `backup.bat` somewhere on your computer and paste in the command you used in the section [Syncing a Directory](#syncing-a-directory). It will look something like the following. Specify the full path to the rclone.exe and don’t forget to save the file.

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. Use `schtasks` to schedule a job. This utility takes a number of parameters.
	* /RU – the user to run the job as. This is needed if the the user you want to use is logged out.
	* /RP – the password for the user.
	* /SC – set to DAILY
	* /TN – the name of the job. Call it Backup
	* /TR – the path to the backup.bat file you just created.
	* /ST – the time to start the task. This is in the 24 hour time format. 01:05:00 is 1:05 AM. 13:05:00 would be 1:05 PM.

```
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac and Linux

1. Create a text file called `backup.sh` somewhere on your computer, and paste the command you used in the section [Syncing a Directory](#syncing-a-directory). It will look something like the following. Specify the full path to the rclone executable and don’t forget to save the file.

```
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. Make the script executable with `chmod`.

```
chmod +x backup.sh
```

3. Edit crontabs.

```
sudo crontab -e
```

4. Add an entry to the bottom of the crontabs file. Crontabs are straight forward: the first 5 fields represent in order minutes, hours, days, months, and weekdays. Using * will denote all. To make the `backup.sh` run at Daily at 1:05 AM, use something that looks like this:

```
5 1 * * * /full/path/to/backup.sh
```

5. Save the crontabs and you’re ready to go.