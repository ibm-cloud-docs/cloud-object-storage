---

copyright:
  years: 2017, 2024
lastupdated: "2024-05-02"

keywords: troubleshooting, support, questions

subcollection: cloud-object-storage

---

{{site.data.keyword.attribute-definition-list}}
{:help: data-hd-content-type='help'}

# Support
{: #troubleshooting-cos}

If you have problems or questions when you use {{site.data.keyword.cos_full}}, you can get help starting right here.
{: shortdesc}

Whether by searching for information or by asking questions through a forum, you can find what you need. If you don't, you can also open a support ticket.

## Other support options
{: #troubleshooting-cos-other-options}

* If you have technical questions about {{site.data.keyword.cos_short}}, post your question on [Stack Overflow](https://stackoverflow.com/search?q=object-storage+ibm) and tag your question with `ibm` and `object-storage`.

## Next steps
{: #troubleshooting-cos-next-steps}

For more information about asking questions, see [Contacting support](https://cloud.ibm.com/docs/get-support?topic=get-support-using-avatar#asking-a-question).

See [Getting help](/docs/get-support?topic=get-support-using-avatar) for more details about using the forums.

For more information about opening an IBM support ticket, see how to [create a request](/docs/get-support?topic=get-support-open-case).

<single-service>

If you experience an issue or have questions when you deploy a Cloud Object Storage deployable architecture, you can use the following resources before you open a support case.

Review the FAQs.
IBM Cloud icon Check the status of the IBM Cloud platform and resources by going to the Status page.
GitHub icon Review the GitHub issues to see whether other users experienced the same problem.
Review the troubleshooting documentation to troubleshoot and resolve common issues.
If you still can't resolve the problem, you can open a support case. For more information, see Creating support cases. If you're looking to provide feedback, see Submitting feedback.

Providing support case details

To ensure that the support team can start investigating your case to provide a timely resolution, include details from the Schematics logs:

Find the Schematics log:

In the IBM Cloud console, go to Schematics > Workspaces > deployable architecture instance.
From the workspace Activity page, select the Schematics apply action that failed.
Click Jobs to see the detailed log output.
Provide errors from the Schematics log:

In the log file, find the last action that Schematics started before the error occurred. For example, in the following log output, Schematics tried to run a copy script in the instances_module module by using the Terraform null_resource.

2021/05/24 05:03:41 Terraform apply | module.instances_module.module.compute_remote_copy_rpms.null_resource.remote_copy[0]: Still creating... [5m0s elapsed]
2021/05/24 05:03:41 Terraform apply |
2021/05/24 05:03:42 Terraform apply |
2021/05/24 05:03:42 Terraform apply |
2021/05/24 05:03:42 Terraform apply | Error: timeout - last error: ssh: rejected: connect failed (Connection timed out)
Paste the errors into the case details.
Provide the architecture name, source URL, and version from the log:

In the log file, find the architecture information. In the following example, you see the Related Workspace, sourcerelease, and sourceurl:

2024/11/19 09:14:44 Related Workspace: name=deploy-arch-ibm-cos-9758, sourcerelease=(not specified), sourceurl=, folder=terraform-ibm-cos-8.14.5/solutions/instance
Copy the architecture information and paste it into the case details.
Routing your support case

To route your support case correctly to speed up resolution, select the applicable product when you open the case.

Routing when you can't deploy successfully

If you can't deploy your deployable architecture, open a support case with the most likely cause of the issue:

If you identified the service that you think is causing the error from the Schematics log, use the name of that service as the product name in the case.
If you can't identify the error from the Schematics log, use the name of the deployable architecture as it is listed in the IBM Cloud catalog.
Routing when you deployed successfully

If you successfully deployed, yet have an issue with a service in the deployable architecture, open a support case and use the name of that service.

</single-service>
