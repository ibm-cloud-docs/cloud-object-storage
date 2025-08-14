---

copyright:
  years: 2017, 2024
lastupdated: "2024-06-05"

keywords: ip address, firewall, configuration, api

subcollection: cloud-object-storage


---

{{site.data.keyword.attribute-definition-list}}

# Restricting access by network context
{: #setting-a-firewall}

[Context-based restrictions](/docs/account?topic=account-context-restrictions-whatis&interface=ui) provide a way for administrators to limit access to resources. What if certain data must be accessed from trusted networks only? A properly configured policy restricts all access to data unless the request originates from an approved [network zone](/docs/account?topic=account-context-restrictions-whatis&interface=ui#network-zones-whatis) and endpoint type (public, private, or direct).
{: shortdesc}




## Using context-based restrictions
{: #setting-cbr}

A [context-based restriction](/docs/account?topic=account-context-restrictions-whatis&interface=ui) is comprised of a **rule** and one or more **contexts** (network zones and/or endpoint type). These restrictions do not replace IAM policies, but simply check that a request is coming from an allowed context, such as a range of IP addresses, VPCs, or service references.

A user must have the `Administrator` role on a service to create, update, or delete rules.  A user must have either the `Editor` or `Administrator` role to create, update, or delete network zones.

Context-based restrictions does not support applying context-based restrictions rules to specific objects or folders, only at the bucket level.

You can learn more about how context-based restrictions work in the [detailed documentation](/docs/account?topic=account-context-restrictions-create&interface=ui), or you can follow a [quick tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-cos-tutorial-cbr).

Audit log events generated will come from the context-based restrictions service, and not {{site.data.keyword.cos_short}}.
{: tip}

If no rules are applicable to a particular resource, access is determined by IAM policies and the presence of a legacy bucket firewall.
{: important}

Context-based restrictions are only applied at the bucket level and not to specific objects or folders.
{: important}

An account is limited in the [number of rules and network zones that can be supported](/docs/account?topic=account-known-issues#cbr-limits).

## Bucket firewalls versus context-based restrictions
{: #firewall-precursors}

Prior to the availability of context-based restrictions, {{site.data.keyword.cos_short}} itself would enforce access restrictions based on IP addresses. While this method is still supported, it is recommended to [use the newer context-based restrictions](/docs/account?topic=account-context-restrictions-create&interface=ui) instead of the legacy bucket firewall.
{: important}

Bucket firewalls and context-based restrictions operate independently of one another, which means it's possible to have a request permitted by one and denied by the other.

* Bucket creation requests **must** be permitted by any context-based restrictions.
* For all other bucket or object requests, both the context-based restrictions _and_ the bucket firewall must allow the request.

An IP address that is allowed by context-based restrictions can still be denied by the bucket firewall.
{: tip}

### About legacy bucket firewalls
{: #firewall-legacy-about}

There are some rules around setting a firewall:

* A user that sets or views a firewall must have the `Manager` role on the bucket.
* A user with the `Manager` role on the bucket can view and edit the list of allowed IP addresses from any IP address to prevent accidental lockouts.
* The {{site.data.keyword.cos_short}} Console can still access the bucket, provided the user's IP address is authorized.
* Other {{site.data.keyword.cloud_notm}} services **are not authorized** to bypass the firewall. This limitation means that other services that rely on IAM policies for bucket access (such as Aspera, SQL Query, Security Advisor, Watson Studio, Cloud Functions, and others) will be unable to do so.

When a firewall is set, the bucket is isolated from the rest of {{site.data.keyword.cloud_notm}}. Consider how this may impact applications and workflows that depend on other services directly accessing a bucket before enabling the firewall. This can be avoided by using [service references and context-based restrictions](/docs/account?topic=account-context-restrictions-whatis&interface=ui#service-attribute) instead.
{: important}

Access from a VPC environment can pass `allowed_network_type` checks, and VPC-zone underlay IP addresses can be added to the `allowed_ip` list. It is not possible to restrict access to an overlay IP for an individual VPC VSI or bare-metal server.
{: note}

First, make sure that you have an instance of {{site.data.keyword.cos_short}} and have provisioned at least one bucket. If not, follow the [getting started tutorial](/docs/cloud-object-storage?topic=cloud-object-storage-getting-started-cloud-object-storage) to obtain the prerequisites and become familiar with the console.

#### Set a list of authorized IP addresses using a legacy firewall
{: #firewall-console-enable}

1. Start by selecting **Storage** to view your resource list.
1. Next, select the service instance with your bucket from within the **Storage** menu. This takes you to the {{site.data.keyword.cos_short}} Console.
1. Choose the bucket that you want to limit access to authorized IP addresses.
1. Select **Access policies** from the navigation menu.
1. Select the **Authorized IPs** tab.
1. Click **Add IP addresses**, then choose **Add**.
1. Specify a list of IP addresses in [CIDR notation](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing){: external}, for example `192.168.0.0/16, fe80:021b::0/64`. Addresses can follow either IPv4 or IPv6 standards.
1. Click **Add**.
1. The firewall will not be enforced until the address is saved in the console. Click **Save all** to enforce the firewall.
1. Note that all objects in this bucket are only accessible from those IP addresses.

#### Remove any IP address restrictions using a legacy firewall
{: #firewalls-console-disable}

1. From the **Authorized IPs** tab, check the boxes next to any IP addresses or ranges to remove from the authorized list.
2. Select **Delete**, and then confirm the dialog box by clicking **Delete** again.
3. The updated list won't be enforced until the changes are saved in the console. Click **Save all** to enforce the new rules.
4. Now all objects in this bucket are only accessible from these IP addresses!

If there are no authorized IP addresses listed this means that normal IAM policies will apply to the bucket, with no restrictions on the user's IP address, unless there are context-based restrictions in place.
{: note}


#### Set a legacy firewall through an API
{: #firewall-api}

Firewalls are managed with the [COS Resource Configuration API](https://cloud.ibm.com/apidocs/cos/cos-configuration). This new REST API is used for configuring buckets.

Users with the `manager` role can view and edit the list of allowed IP addresses from any network in order to prevent accidental lockouts.
{: tip}
