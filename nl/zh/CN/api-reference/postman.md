---

copyright:
  years: 2017, 2018, 2019
lastupdated: "2019-03-19"

keywords: rest, s3, compatibility, api, postman, client, object storage

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

# 使用 `Postman`
{: #postman}

下面是针对 {{site.data.keyword.cos_full}} REST API 的基本 `Postman` 设置。在 API 参考中可以找到有关[存储区](/docs/services/cloud-object-storage/api-reference?topic=cloud-object-storage-compatibility-api-bucket-operations)或[对象](/docs/services/cloud-object-storage?topic=cloud-object-storage-object-operations)的其他详细信息。

使用 `Postman` 时，假定您在一定程度上熟悉对象存储器，并且了解[服务凭证](/docs/services/cloud-object-storage/iam?topic=cloud-object-storage-service-credentials)或[控制台](/docs/services/cloud-object-storage?topic=cloud-object-storage-getting-started)中的必要信息。如果遇到任何不熟悉的术语或变量，可以在[词汇表](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-terminology)中找到这些术语或变量。

个人可标识信息 (PII)：创建存储区和/或添加对象时，请确保未使用可以通过名称、位置或其他任何方式识别到任何用户（自然人）的任何信息。
{:tip}

## REST API 客户机概述
{: #postman-rest}

REST（具象状态传输）是一种体系结构样式，为计算机系统通过 Web 彼此交互提供了一种标准，交互通常使用标准的 HTTP URL 和动词（GET、PUT、POST 等），这在所有主要开发语言和平台都受支持。但是，与 REST API 进行交互并不像使用标准因特网浏览器那样简单。简单浏览器不允许对 URL 请求执行任何操作。这正是 REST API 客户机的用武之地。

REST API 客户机提供了一个基于 GUI 的简单应用程序，用于连接现有的 REST API 库。一款优秀的客户机允许用户快速将简单和复杂的 HTTP 请求组合在一起，借此轻松对 API 执行测试、开发和记录。Postman 就是这样一款出色的 REST API 客户机，它提供了完整的 API 开发环境，其中包含用于设计、模拟、调试、测试、记录、监视和发布 API 的内置工具。此外，Postman 还提供了一些非常有用的功能，例如“集合”和“工作空间”，能轻而易举地实现协作。 

## 先决条件
{: #postman-prereqs}
* IBM Cloud 帐户
* [已创建 Cloud Storage 资源](https://cloud.ibm.com/catalog/)（可以使用轻量/免费套餐）
* [已安装并配置 IBM Cloud CLI](https://cloud.ibm.com/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-ic-use-the-ibm-cli)
* [Cloud Storage 的服务实例标识](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials)
* [IAM (Identity and Access Management) 令牌](/docs/services/cloud-object-storage?topic=cloud-object-storage-service-credentials#service-credentials) 
* [COS 存储区的端点](/docs/services/cloud-object-storage/basics?topic=cloud-object-storage-endpoints)

### 创建存储区
{: #postman-create-bucket}
1.	启动 Postman。
2.	在“新建”选项卡中，从下拉列表中选择 `PUT`。
3.	在地址栏中输入端点，然后添加新存储区的名称。
a.	存储区名称必须在所有存储区中唯一，因此请选择比较具体的名称。
4.	在“类型”下拉列表中，选择“不记名令牌”。
5.	在“令牌”框中，添加 IAM 令牌。
6.	单击“预览请求”。
a.	您应该会看到确认消息，指出头已添加。
7.	单击“头”选项卡，在其中应该会看到 Authorization 的现有条目。
8.	添加新键。
a.	键：`ibm-service-instance-id`
b.	值：Cloud Storage 服务的资源实例标识。
9.	单击“发送”。
10.	您将收到状态 `200 正常`消息。

### 创建新的文本文件
{: #postman-create-text-file}

1.	通过单击加号 (+) 图标来创建新的选项卡。
2.	从列表中选择 `PUT`。
3.	在地址栏中，输入包含先前部分中存储区名称和文件名的端点地址。
4.	在“类型”列表中，选择“不记名令牌”。
5.	在“令牌”框中，添加 IAM 令牌。
6.	选择“主体”选项卡。
7.	选择原始选项，并确保选择“文本”。
8.	在提供的空白处输入文本。
9.	单击“发送”。
10.	您将收到状态 `200 正常`消息。

### 列出存储区的内容
{: #postman-list-objects}

1.	通过选择加号 (+) 图标来创建新的选项卡。
2.	验证是否选择了列表中的 `GET`。
3.	在地址栏中，输入包含先前部分中存储区名称的端点地址。
4.	在“类型”列表中，选择“不记名令牌”。
5.	在“令牌”框中，添加 IAM 令牌。
6.	单击“发送”。
7.	您将收到状态 `200 正常`消息。
8.	在“响应主体”部分中，会有一条 XML 消息，其中包含存储区中的文件列表。

## 使用样本集合
{: #postman-collection}

Postman 集合可供[下载 ![外部链接图标](../icons/launch-glyph.svg "外部链接图标")](https://s3.us.cloud-object-storage.appdomain.cloud/docs-resources/ibm_cos_postman.json){: new_window}，其中包含可配置的 {{site.data.keyword.cos_full}} API 请求样本。

### 将集合导入到 Postman
{: #postman-import-collection}

1. 在 Postman 中，单击右上角的“导入”按钮。
2. 使用以下任一方法导入集合文件：
    * 在“导入”窗口中，将集合文件拖放到标注为**将文件放在此处**的窗口中。
    * 单击“选择文件”按钮并浏览至相应文件夹，然后选择集合文件。
3. *IBM COS* 现在应该显示在“集合”窗口中。
4. 展开“集合”，您应该会看到二十 (20) 个样本请求。
5. 集合包含六 (6) 个变量，需要设置这些变量才能成功执行 API 请求。
    * 单击集合右侧的三个点图标以展开菜单，然后单击“编辑”。
6. 编辑变量，以便与 Cloud Storage 环境相匹配。
    * **bucket** - 输入要创建的新存储区的名称（存储区名称在 Cloud Storage 中必须唯一）。
    * **serviceid** - 输入 Cloud Storage 服务的 CRN。[此处](/docs/overview?topic=overview-crn)提供了获取 CRN 的指示信息。
    * **iamtoken** - 输入 Cloud Storage 服务的 OAUTH 令牌。[此处](/docs/services/key-protect?topic=key-protect-retrieve-access-token)提供了获取 OAUTH 令牌的指示信息。
    * **endpoint** - 输入 Cloud Storage 服务的区域端点。通过 [IBM Cloud 仪表板](https://cloud.ibm.com/resources/){:new_window}获取可用端点。
        * *确保所选端点与 Key Protect 服务相匹配，以保证样本正确运行*。
    * **rootkeycrn** - 在主 Key Protect 服务中创建的根密钥的 CRN。
        * CRN 应该类似于以下内容：<br/>`crn:v1:bluemix:public:kms:us-south:a/3d624cd74a0dea86ed8efe3101341742:90b6a1db-0fe1-4fe9-b91e-962c327df531:key:0bg3e33e-a866-50f2-b715-5cba2bc93234`
        * *确保选择的 Key Protect 服务与端点的区域相匹配*。
    * **bucketlocationvault** - 为*创建新存储区（其他存储类）*API 请求输入用于创建存储区的位置约束值。
        * 可接受的值包括：
            * us-south-vault
            * us-standard-flex
            * eu-cold
7. 单击“更新”。

### 运行样本
{: #postman-samples}
API 样本请求相当简单，很容易上手使用。这些样本设计为按顺序运行，并演示如何与 Cloud Storage 进行交互。还可以使用这些样本对 Cloud Storage 服务运行功能测试，以确保正常运行。

<table>
    <tr>
        <th>请求</th>
        <th>预期结果</th>
        <th>测试结果</th>
    </tr>
    <tr>
        <td>检索存储区列表</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
                <li>
                    在“主体”中，您应该会看到 Cloud Storage 中存储区的 XML 列表。
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期内容</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>创建新存储区</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>创建新的文本文件</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期头</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>创建新的二进制文件</td>
        <td>
            <ul>
                <li>
                    单击“主体”，然后单击“选择文件”以选择要上传的映像
                </li>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期头</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>检索存储区中的文件列表</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到在先前请求中创建的两个文件
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期头</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>检索存储区中的文件列表（按前缀过滤）</td>
        <td>
            <ul>
                <li>将 querystring 值更改为 prefix=&lt;some text&gt;</li>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到名称以指定前缀开头的文件
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期头</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>检索文本文件</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到在先前请求中输入的文本
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期主体内容</li>
                <li>响应包含预期头</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>检索二进制文件</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到在先前请求中选择的映像
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期头</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>检索失败分块上传的列表</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到存储区的所有失败分块上传
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期内容</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>检索失败分块上传的列表（按名称过滤）</td>
        <td>
            <ul>
                <li>将 querystring 值更改为 prefix=&lt;some text&gt;</li>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到存储区中名称以指定前缀开头的所有失败分块上传
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期内容</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>设置启用 CORS 的存储区</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>检索存储区 CORS 配置</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
                <li>
                    在响应的“主体”中，您应该会看到为存储区设置的 CORS 配置
                </li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
                <li>响应包含预期内容</li>
            </ul>
        </td>        
    </tr>
    <tr>
        <td>删除存储区 CORS 配置</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>删除文本文件</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>删除二进制文件</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>删除存储区</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>创建新存储区（其他存储类）</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>删除存储区（其他存储类）</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>创建新存储区 (Key Protect)</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
    <tr>
        <td>删除新存储区 (Key Protect)</td>
        <td>
            <ul>
                <li>状态码 200 正常</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>请求已成功</li>
            </ul>
        </td>                
    </tr>
</table>

## 使用 Postman 集合运行器
{: #postman-runner}

Postman 集合运行器提供了用于测试集合的用户界面，允许您一次运行集合中的所有请求。 

1. 单击 Postman 主窗口右上角的“运行器”按钮。
2. 在“运行器”窗口中，选择 IBM COS 集合，然后单击屏幕底部的蓝色大按钮**运行 IBM COS**。
3. “集合运行器”窗口在运行请求时会显示迭代。您将看到每个请求下显示了测试结果。
    * **运行摘要**显示请求的网格视图，并允许对结果进行过滤。
    * 还可以单击**导出结果**，以将结果保存到 JSON 文件。
