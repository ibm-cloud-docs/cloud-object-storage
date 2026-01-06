---

copyright:
  years: 2017, 2024
lastupdated: "2026-01-06"

keywords: data migration, object storage, cli, rclone

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
{:faq: data-hd-content-type='faq'}
{:support: data-reuse='support'}

# Using `rclone`
{: #rclone}

Getting the most out of {{site.data.keyword.cos_full}} when you have access to tools and solutions like `rclone` and the command-line interface (cli).
{: shortdesc}

## Install `rclone`
{: #rclone-install}

The `rclone` tool is useful for keeping directories synchronized and for migrating data between storage platforms. It's a Go program and comes as a single binary file.

### Quick start Installation
{: #rclone-quick}

* [Download](https://rclone.org/downloads/) the relevant binary.
* Extract the `rclone` or `rclone.exe` binary from the archive.
* Run `rclone config` to set up.

### Installation by using a script
{: #rclone-script}

Install `rclone` on Linux/macOS/BSD systems:

``` sh
curl https://rclone.org/install.sh | sudo bash
```

Beta versions are available as well:

``` sh
curl https://rclone.org/install.sh | sudo bash -s beta
```

The installation script checks the version of `rclone` installed first, and skips downloading if the current version is already up-to-date.
{: note}

### Linux installation from pre-compiled binary
{: #rclone-linux-binary}

1. Fetch and unpack the binary:

   ``` sh
   curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
   unzip rclone-current-linux-amd64.zip
   cd rclone-*-linux-amd64
   ```

1. Copy the binary file to a sensible location:

   ``` sh
   sudo cp rclone /usr/bin/
   sudo chown root:root /usr/bin/rclone
   sudo chmod 755 /usr/bin/rclone
   ```

1. Install the documentation:

   ``` sh
   sudo mkdir -p /usr/local/share/man/man1
   sudo cp rclone.1 /usr/local/share/man/man1/
   sudo mandb
   ```

1. Run `rclone config` to set up:

   ``` sh
   rclone config
   ```

### macOS installation from pre-compiled binary
{: #rclone-osx-binary}

1. Download the `rclone` package:

   ``` sh
   cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
   ```

1. Extract the downloaded file and `cd` to the extracted folder:

   ``` sh
   unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
   ```

1. Move `rclone` to your `$PATH` and enter your password when prompted:

   ``` sh
   sudo mkdir -p /usr/local/bin
   sudo mv rclone /usr/local/bin/
   ```

   The `mkdir` command is safe to run, even if the directory exists.
   {: tip}

1. Remove the leftover files.

   ``` sh
   cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
   ```

1. Run `rclone config` to set up:

   ``` sh
   rclone config
   ```

## Configure access to IBM COS
{: #rclone-config}

1. Run `rclone config` and select `n` for a new remote.

   ``` sh
   No remotes found - make a new one
      n) New remote
      s) Set configuration password
      q) Quit config
      n/s/q> n
   ```

1. Enter the name for the configuration:

   ``` sh
   name> <YOUR NAME>
   ```

1. Select “s3” storage.

   ``` txt
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

1. Select IBM COS as the S3 Storage Provider.

   ``` txt
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

1. Enter **False** to enter your credentials.

   ``` txt
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

1. Enter the Access Key and Secret.

   ``` txt
   AWS Access Key ID - leave blank for anonymous access or runtime credentials.
     access_key_id> <>
   AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
     secret_access_key> <>
   ```

1. Specify the endpoint for IBM COS. For Public IBM COS, choose from the provided options. For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

   ``` txt
   Endpoint for IBM COS S3 API.
   Choose a number from below, or type in your own value
      1 / US Cross Region Endpoint
        \ "s3.us.cloud-object-storage.appdomain.cloud"
      2 / US Cross Region Dallas Endpoint
        \ "s3-api.dal.us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
      3 / US Cross Region Washington DC Endpoint
        \ "s3-api.wdc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
      4 / US Cross Region San Jose Endpoint
        \ "s3-api.sjc-us-geo.objectstorage.s3.us-south.cloud-object-storage.appdomain.cloud.net"
      5 / US Cross Region Private Endpoint
        \ "s3-api.us-geo.objectstorage.service.networklayer.com"
   [snip]
      34 / Toronto Single Site Private Endpoint
        \ "s3.tor01.objectstorage.service.networklayer.com"
   endpoint>1
   ```

1. Specify an IBM COS Location Constraint. The location constraint must match the endpoint. For more information about endpoints, see [Endpoints and storage locations](/docs/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints).

   ``` txt
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

1. Specify an ACL. Only `public-read` and `private` are supported. 

   ``` txt
   Canned ACL used when creating buckets or storing objects in S3.
   Choose a number from below, or type in your own value
      1 "private"
      2 "public-read"
   acl>1
   ```

1. Review the displayed configuration and accept to save the “remote” then quit. The config file should look like this

   ``` txt
   [YOUR NAME]
   type = s3
   Provider = IBMCOS
   access_key_id = xxx
   secret_access_key = yyy
   endpoint = s3.us.cloud-object-storage.appdomain.cloud
   location_constraint = us-standard
   acl = private
   ```

## Command reference
{: #rclone-reference}

For the following list of commonly used commands, `REMOTE_NAME` is the name you entered for the configuration when you configured access to {{site.data.keyword.cos_full_notm}} and `BUCKET_NAME` is the name of a bucket.

### Create a bucket
{: #rclone-reference-create-bucket}

``` sh
rclone mkdir REMOTE_NAME:BUCKET_NAME
```

### List available buckets
{: #rclone-reference-list-buckets}

``` sh
rclone lsd REMOTE_NAME:
```

### List contents of a bucket
{: #rclone-reference-list-objects}

``` sh
rclone ls REMOTE_NAME:BUCKET_NAME
```

### Copy a file from local to remote
{: #rclone-reference-copy-local}

``` sh
rclone copy /Users/file.txt REMOTE_NAME:BUCKET_NAME
```

### Copy a file from remote to local
{: #rclone-reference-copy-remote}

```sh
rclone copy REMOTE_NAME:BUCKET_NAME/file.txt /Users/Documents/
```

### Delete a file on remote
{: #rclone-reference-delete-file}

``` sh
rclone delete REMOTE_NAME:BUCKET_NAME/file.txt
```

### List Commands
{: #rclone-reference-listing}

There are several related list commands:

* `ls` to list size and path of objects only
* `lsl` to list modification time, size, and path of objects only
* `lsd` to list directories only
* `lsf` to list objects and directories in easy to parse format
* `lsjson` to list objects and directories in JSON format

## `rclone sync`
{: #rclone-sync}

The `sync` operation makes the source and destination identical, and modifies the destination only. Syncing doesn’t transfer unchanged files, testing by size and modification time or MD5SUM. Destination is updated to match source, including deleting files if necessary.

Since this operation can cause data loss, test first with the `--dry-run` flag to see exactly what would be copied and deleted.
{: important}

Files in the destination aren't deleted if there are errors at any point.

The _contents_ of the directory are synced, not the directory itself. When `source:path` is a directory, it’s the contents of `source:path` that are copied, not the directory name and contents. For more information, see the extended explanation in the `copy` command.

If `dest:path` doesn’t exist, it is created and the `source:path` contents go there.

``` txt
rclone sync source:path dest:path [flags]
```

### Using `rclone` from multiple locations at the simultaneously
{: #rclone-sync-multiple}

You can use `rclone` from multiple places simultaneously if you choose different subdirectory for the output:

``` sh
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

If you `sync` to the same directory than you can use `rclone copy`, otherwise the two processes might delete each other's others files:

``` sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

When using `sync`, `copy` or `move` any files that would have been overwritten or deleted are moved in their original hierarchy into this directory.

If `--suffix` is set, then the moved files have the suffix added to them. If there is a file with the same path (after the suffix has been added) in the directory, it is overwritten.

The remote in use must support server-side move or copy and you must use the same remote as the destination of the sync. The backup directory must not overlap the destination directory.

``` txt
rclone sync /path/to/local remote:current --backup-dir remote:old
```

will `sync` `/path/to/local` to `remote:current`, but for any files that would have been updated or deleted will be stored in `remote:old`.

If running `rclone` from a script you might want to use today’s date as the directory name passed to `--backup-dir` to store the old files, or you might want to pass `--suffix` with today’s date.

## `rclone` daily sync
{: #rclone-sync-daily}

Scheduling a backup is important to automating backups. Depending on your platform depends on how you do this. Windows can use Task Scheduler while MacOS and Linux can use crontabs.

### Syncing a Directory
{: #rclone-sync-directory}

`Rclone` syncs a local directory with the remote container, storing all the files in the local directory in the container. `Rclone` uses the syntax, `rclone sync source destination`, where `source` is the local folder and `destination` is the container within your IBM COS.

```sh
rclone sync /path/to/my/backup/directory REMOTE_NAME:BUCKET_NAME
```

You might already have a destination that is created, but if you don't then you can create a new bucket by using the steps above.

### Scheduling a Job
{: #rclone-sync-schedule}

Before scheduling a job, make sure that you have done your initial upload and it has completed.

#### Windows
{: #rclone-sync-windows}

1. Create a text file that is called `backup.bat` somewhere on your computer and paste in the command you used in the section about [syncing a directory](#rclone-sync-directory).  Specify the full path to the `rclone.exe` and don’t forget to save the file.

   ``` txt
    C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" REMOTE_NAME:BUCKET_NAME
   ```

1. Use `schtasks` to schedule a job. This utility takes a number of parameters.

* /RU – the user to run the job as. This is needed if the user you want to use is logged out.
* /RP – the password for the user.
* /SC – set to DAILY
* /TN – the name of the job. Call it backup
* /TR – the path to the backup.bat file you created.
* /ST – the time to start the task. This is in the 24-hour time format. 01:05:00 is 1:05 AM. 13:05:00 would be 1:05 PM.

   ``` txt
   schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
   ```

#### Mac and Linux
{: #rclone-sync-nix}

1. Create a text file called `backup.sh` somewhere on your computer, and paste the command that you used in the section [syncing a Directory](#rclone-sync-directory). It looks something like the following. Specify the full path to the `rclone` executable and don’t forget to save the file.

   ``` txt
   #!/bin/sh
   /full/path/to/rclone sync /path/to/my/backup/directory REMOTE_NAME:BUCKET_NAME
   ```

1. Make the script executable with `chmod`.

   ```sh
   chmod +x backup.sh
   ```

1. Edit crontabs.

   ```sh
   sudo crontab -e
   ```

1. Add an entry to the bottom of the crontabs file. Crontabs are straight forward: the first five fields describe in order minutes, hours, days, months, and weekdays. Using * denotes all. To make the `backup.sh` run at Daily at 1:05 AM, use something that looks like this:

   ```sh
   5 1 * * * /full/path/to/backup.sh
   ```

1. Save the crontabs and you’re ready to go.
