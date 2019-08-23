---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: data migration, object storage, cli, rclone

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

# 使用 `rclone`
{: #rclone}

## 安装 `rclone`
{: #rclone-install}

`rclone` 工具对于保持目录同步以及在存储平台之间迁移数据非常有用。这是一个 Go 程序，作为单个二进制文件提供。

### 快速入门安装
{: #rclone-quick}

*  [下载](https://rclone.org/downloads/)相关二进制文件。 
*  从归档中抽取 `rclone` 或 `rclone.exe` 二进制文件。
*  运行 `rclone config` 以进行设置。

### 使用脚本进行安装
{: #rclone-script}

在 Linux/macOS/BSD 系统上安装 `rclone`：

```
curl https://rclone.org/install.sh | sudo bash
```

还有 Beta 版本可用：

```
curl https://rclone.org/install.sh | sudo bash -s beta
```

安装脚本会首先检查安装的 `rclone` 版本，如果当前版本已经是最新版本，那么将跳过下载。
{:note}

### 通过预编译的二进制文件在 Linux 上进行安装
{: #rclone-linux-binary}

首先，访存二进制文件并对其解包：

```
curl -O https://downloads.rclone.org/rclone-current-linux-amd64.zip
unzip rclone-current-linux-amd64.zip
cd rclone-*-linux-amd64
```

接下来，将二进制文件复制到合理的位置：

```
sudo cp rclone /usr/bin/
sudo chown root:root /usr/bin/rclone
sudo chmod 755 /usr/bin/rclone
```

安装文档：

```
sudo mkdir -p /usr/local/share/man/man1
sudo cp rclone.1 /usr/local/share/man/man1/
sudo mandb
```

运行 `rclone config` 以进行设置：

```
rclone config
```

### 通过预编译的二进制文件在 macOS 上进行安装
{: #rclone-osx-binary}

首先，下载 `rclone` 包：

```
cd && curl -O https://downloads.rclone.org/rclone-current-osx-amd64.zip
```

接下来，对下载的文件解压缩，然后通过 `cd` 命令转至解压缩的文件夹：

```
unzip -a rclone-current-osx-amd64.zip && cd rclone-*-osx-amd64
```

将 `rclone` 移至 `$PATH`，并在提示时输入密码：

```
sudo mkdir -p /usr/local/bin
sudo mv rclone /usr/local/bin/
```

运行 `mkdir` 命令是安全的，即使该目录存在也是如此。
{:tip}

除去剩余文件。

```
cd .. && rm -rf rclone-*-osx-amd64 rclone-current-osx-amd64.zip
```

运行 `rclone config` 以进行设置：

```
rclone config
```

## 配置对 IBM COS 的访问权
{: #rclone-config}

1. 运行 `rclone config`，并选择 `n`，这表示新建远程。

```
	No remotes found - make a new one
		n) New remote
		s) Set configuration password
		q) Quit config
		n/s/q> n
```

2. 输入配置的名称：
```
	name> <YOUR NAME>
```

3. 选择“s3”存储器。

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

  4. 选择 IBM COS 作为 S3 存储器提供者。

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

  1. 输入 **False** 以输入凭证。

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

  6. 输入访问密钥和私钥。

```
AWS Access Key ID - leave blank for anonymous access or runtime credentials.
	access_key_id> <>
AWS Secret Access Key (password) - leave blank for anonymous access or runtime credentials.
	secret_access_key> <>
```

  7. 指定 IBM COS 的端点。对于公共 IBM COS，请从提供的选项中进行选择。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

```
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

  8. 指定 IBM COS 位置约束。位置约束必须与端点相匹配。有关端点的更多信息，请参阅[端点和存储位置](/docs/services/cloud-object-storage?topic=cloud-object-storage-endpoints#endpoints)。

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

  9. 指定 ACL。仅支持 `public-read` 和 `private`。 

```
Canned ACL used when creating buckets and/or storing objects in S3.
Choose a number from below, or type in your own value
   1 "private"
   2 "public-read"
acl>1
```

  10. 复查显示的配置并接受以保存“远程”，然后退出。配置文件应该如下所示：

```
  [YOUR NAME]
	type = s3
	Provider = IBMCOS
	access_key_id = xxx
	secret_access_key = yyy
	endpoint = s3.us.cloud-object-storage.appdomain.cloud
	location_constraint = us-standard
	acl = private
```

## 命令参考
{: #rclone-reference}

### 创建存储区
{: #rclone-reference-create-bucket}

```
rclone mkdir RemoteName:newbucket
```

### 列出可用存储区
{: #rclone-reference-list-buckets}

```
rclone lsd RemoteName:
```

### 列出存储区的内容
{: #rclone-reference-list-objects}

```
rclone ls RemoteName:newbucket
```

### 将文件从本地复制到远程
{: #rclone-reference-copy-local}

```
rclone copy /Users/file.txt RemoteName:newbucket
```

### 将文件从远程复制到本地
{: #rclone-reference-copy-remote}

```sh
rclone copy RemoteName:newbucket/file.txt /Users/Documents/
```

### 删除远程上的文件
{: #rclone-reference-delete-file}

```
rclone delete RemoteName:newbucket/file.txt
```

### 列表命令
{: #rclone-reference-listing}

有多个相关的列表命令
* `ls` 仅列出对象的大小和路径
* `lsl` 仅列出对象的修改时间、大小和路径
* `lsd` 仅列出目录
* `lsf` 以易于解析的格式列出对象和目录
* `lsjson` 以 JSON 格式列出对象和目录

## `rclone sync`
{: #rclone-sync}

`sync` 操作使源和目标完全相同，并且仅修改目标。同步不会传输未更改的文件，并且会按大小和修改时间或 MD5SUM 进行测试。目标会更新为与源相匹配，包括根据需要删除文件。

由于这可能导致数据丢失，因此请首先使用 `--dry-run` 标志进行测试，以准确了解将复制和删除的内容。
{:important}

请注意，如果在此过程中任何时候发生任何错误，那么不会删除目标中的文件。

同步的是目录的_内容_，而不是目录本身。`source:path` 是目录时，将复制的是 `source:path` 的内容，而不是目录名称和内容。有关更多信息，请参阅 `copy` 命令中的扩展说明。

如果 `dest:path` 不存在，那么会创建该路径，并会将 `source:path` 内容复制到其中。

```sh
rclone sync source:path dest:path [flags]
```

### 同时从多个位置使用 `rclone`
{: #rclone-sync-multiple}

如果为输出选择了不同的子目录，那么可以同时从多个位置使用 `rclone`：

```
Server A> rclone sync /tmp/whatever remote:ServerA
Server B> rclone sync /tmp/whatever remote:ServerB
```

如果运行 `sync` 以同步到相同的目录，那么应该使用 `rclone copy`，否则这两个进程可能会删除彼此的其他文件：

```sh
Server A> rclone copy /tmp/whatever remote:Backup
Server B> rclone copy /tmp/whatever remote:Backup
```

### `--backup-dir=DIR`
{: #rclone-sync-backup}

使用 `sync`、`copy` 或 `move` 时，已覆盖或已删除的任何文件都会在其原始层次结构中移至此目录。

如果设置了 `--suffix`，那么会向已移动的文件添加后缀。如果在目录中存在具有相同路径（添加后缀之后）的文件，那么会覆盖该文件。

使用的远程必须支持服务器端移动或复制，并且必须使用与同步目标相同的远程。备份目录不能与目标目录重叠。

```sh
rclone sync /path/to/local remote:current --backup-dir remote:old
```

这会通过 `sync` 操作将 `/path/to/local` 同步到 `remote:current`，但已更新或已删除的任何文件都将存储在 `remote:old` 中。

如果通过脚本运行 `rclone`，那么您可能希望将今天的日期用作目录名称以传递到 `--backup-dir` 来存储旧文件，或者您可能希望传递使用今天日期的 `--suffix`。

## `rclone` 每日同步
{: #rclone-sync-daily}

安排备份对于自动备份非常重要。如何安排备份取决于您的平台。Windows 可以使用任务调度程序，而 MacOS 和 Linux 可以使用 crontab。

### 同步目录
{: #rclone-sync-directory}

`Rclone` 用于将本地目录与远程容器进行同步，从而将本地目录中的所有文件存储到该容器中。`Rclone` 使用的语法为 `rclone sync source destination`，其中 `source` 是本地文件夹，`destination` 是 IBM COS 中的容器。

```sh
rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

您可能已经有创建的目标，但如果没有，那么可以使用上面的步骤来创建新的存储区。

### 安排作业
{: #rclone-sync-schedule}

安排作业之前，请确保已执行初始上传并且此上传已完成。

#### Windows
{: #rclone-sync-windows}

1. 在计算机上的某个位置创建名为 `backup.bat` 的文本文件，然后将有关[同步目录](#rclone-sync-directory)的部分中使用的命令粘贴到该文件中。指定 rclone.exe 的完整路径，并请务必保存该文件。

```
 C:\full\path\to\rclone.exe sync "C:\path\to\my\backup\directory" RemoteName:newbucket
```

2. 使用 `schtasks` 来安排作业。此实用程序采用多个参数。
	* /RU - 要以其身份运行作业的用户。如果您要使用的用户已注销，那么此值是必需的。
	* /RP - 用户的密码。
	* /SC - 设置为 DAILY。
	* /TN - 作业的名称。请将其命名为 backup。
	* /TR - 刚才创建的 backup.bat 文件的路径。
	* /ST - 启动任务的时间。此时间为 24 小时制。01:05:00 表示凌晨 1:05。13:05:00 表示下午 1:05。

```sh
schtasks /Create /RU username /RP "password" /SC DAILY /TN Backup /TR C:\path\to\backup.bat /ST 01:05:00
```

#### Mac 和 Linux
{: #rclone-sync-nix}

1. 在计算机上的某个位置创建名为 `backup.sh` 的文本文件，然后将[同步目录](#rclone-sync-directory)部分中使用的命令粘贴到该文件中。粘贴的命令类似于以下内容。指定 rclone 可执行文件的完整路径，并请务必保存该文件。

```sh
#!/bin/sh
/full/path/to/rclone sync /path/to/my/backup/directory RemoteName:newbucket
```

2. 使用 `chmod` 使该脚本可执行。

```sh
chmod +x backup.sh
```

3. 编辑 crontab。

```sh
sudo crontab -e
```

4. 向 crontab 文件的底部添加一个条目。crontab 非常简单：前五个字段按顺序依次表示分钟、小时、天、月和星期几。使用 * 表示全部。要使 `backup.sh` 在每天凌晨 1:05 运行，请使用类似于以下内容的命令：

```sh
5 1 * * * /full/path/to/backup.sh
```

5. 保存 crontab，现在您已准备好继续执行后续操作。
