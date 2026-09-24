---

copyright:
  years: 2026
lastupdated: "2026-09-23"

keywords: faq, frequently asked questions, sdk, sdk v1, sdk v2, migration, go, java, node.js, end of support

subcollection: cloud-object-storage

content-type: faq

---

{{site.data.keyword.attribute-definition-list}}

# FAQ - SDK migration (v1 to v2)
{: #faq-sdk-migration}

Frequently asked questions about migrating from {{site.data.keyword.cos_full}} SDK v1 to v2 for Go, Java, and Node.js.
{: shortdesc}

The End-of-Support date for SDK v1 is **August 6, 2027** and applies to the Go, Java, and Node.js SDKs.
{: important}

## Why is a new SDK version being released?
{: #faq-sdk-why-v2}
{: faq}

SDK v1 was built on top of the AWS SDK v1 architecture for each language. While it served its purpose, it accumulated constraints that could not be fixed incrementally:

- **Everything ships as one package** — The entire SDK is one bundle and not modular, so your application includes all features, even the ones you don't use.
- **Older programming patterns** — The SDK relies on approaches like callbacks, mutable request objects, and session-based configuration, which are outdated compared to modern development practices.
- **Limited async support** — Concurrent workloads required workarounds that the v1 architecture was not designed for.

SDK v2 addresses these limitations and offers a modular, maintainable, and async-first design, while retaining all IBM COS capabilities. These benefits, combined with AWS having reached its v1 end-of-support and the general S3 ecosystem moving toward v2, make SDK v2 the natural path forward for IBM COS applications.

## What are the improvements in v2?
{: #faq-sdk-v2-improvements}
{: faq}

SDK v2 allows you to import only the parts of the SDK your application uses. Unused code is eliminated at build time, reducing bundle and JAR size significantly. Language-specific improvements include:

- **Node.js** — Native async/await, TypeScript built in, no more `.promise()` wrappers.
- **Java** — Immutable request builders, async client (`S3AsyncClient`).
- **Go** — Mandatory `context.Context` for cancellation and timeouts.

## When do I need to migrate?
{: #faq-sdk-migration-deadline}
{: faq}

The End-of-Support (EOS) date for SDK v1 is **August 6, 2027** for all three SDKs (Go, Java, and Node.js). The End-of-Support policy applies to SDK support, not service access.

## Is v2 a drop-in replacement for v1?
{: #faq-sdk-drop-in}
{: faq}

No. SDK v2 is not backward compatible with v1. Your application code must be updated. The language-specific migration guides contain complete before and after code examples for every changed area. For links to the migration guides, see [Where can I get help if I run into issues?](#faq-sdk-help).

## Will my stored data or buckets be affected?
{: #faq-sdk-data-impact}
{: faq}

No. Migration only touches your application code. Your IBM COS buckets, objects, access policies, retention configurations, and all stored data are completely unaffected. No data needs to be moved, copied, or recreated.

## What runtime or toolchain versions does v2 require?
{: #faq-sdk-runtime-requirements}
{: faq}

The following table lists the minimum runtime versions required for SDK v2.

| SDK | Minimum version required |
| --- | --- |
| Node.js | Node.js 20.x or later |
| Java | Java 8 or later |
| Go | Go 1.23 or later |
{: caption="Minimum runtime versions for SDK v2" caption-side="bottom"}

If your current environment does not meet these requirements, include a runtime upgrade as part of your migration plan.

## Can v1 and v2 coexist in the same application during migration?
{: #faq-sdk-coexistence}
{: faq}

Yes. SDK v1 and v2 have separate package names and module paths, so both can be declared as dependencies at the same time. This is the recommended approach — it allows you to migrate one component at a time rather than rewriting everything at once. After all components are migrated, you can remove v1 dependencies.

Do not share client instances, configuration objects, or credential providers between v1 and v2 code. They are architecturally incompatible and must remain fully isolated from each other.
{: important}

## Are IBM-specific features (WORM, Key Protect, Legal Hold) still available?
{: #faq-sdk-ibm-features}
{: faq}

Yes. All IBM COS-specific capabilities are fully supported in v2. The underlying service behavior is unchanged.

## Where can I get help if I run into issues?
{: #faq-sdk-help}
{: faq}

The following resources are available:

- **Language-specific migration guides** — Detailed before and after code examples for every changed area:
   - [IBM COS SDK for Go - Migration Guide](https://github.com/IBM/ibm-cos-sdk-go-v2/blob/main/MIGRATION_GUIDE_V2.md){: external}
   - [IBM COS SDK for Java - Migration Guide](https://github.com/IBM/ibm-cos-sdk-java-v2/blob/main/MIGRATION_GUIDE_V2.md){: external}
   - [IBM COS SDK for Node.js - Migration Guide](https://github.com/IBM/ibm-cos-sdk-js-v2/blob/main/MIGRATION_GUIDE_V2.md){: external}
- **SDK documentation** — [Getting started with the IBM COS SDKs](/docs/cloud-object-storage?topic=cloud-object-storage-sdk-gs)
- **SDK GitHub repositories** — Open an issue against the respective v2 repository for defects or questions not covered by the migration guide:
   - [IBM COS SDK for Go - Issues](https://github.com/IBM/ibm-cos-sdk-go-v2/issues){: external}
   - [IBM COS SDK for Java - Issues](https://github.com/IBM/ibm-cos-sdk-java-v2/issues){: external}
   - [IBM COS SDK for Node.js - Issues](https://github.com/IBM/ibm-cos-sdk-js-v2/issues){: external}
- **IBM Cloud Support** — [IBM Cloud support center](https://cloud.ibm.com/unifiedsupport/supportcenter){: external}

## How do I check which SDK version is installed in my project?
{: #faq-sdk-check-version}
{: faq}

The quickest check is to inspect your project's dependency file. Because v1 and v2 use entirely different package names, both can be present at the same time.

### Node.js
{: #faq-sdk-check-version-node}

**Method 1** — Check `package.json` for entries `ibm-cos-sdk` (v1) or `ibm-cos-sdk-v2` (v2).

**Method 2** — Run the following command to list the installed version:

```sh
npm list ibm-cos-sdk ibm-cos-sdk-v2
```
{: pre}

Example output:

```screen
$ npm list ibm-cos-sdk ibm-cos-sdk-v2
ibm-cos-sdk-js-v2@1.0.0 /Users/user/examples/ibm-cos-sdk-js-v2
├─┬ @ibm-cos/lib-storage@1.0.0
│ └── ibm-cos-sdk-v2@1.0.0
├── ibm-cos-sdk-v2@1.0.0
└── ibm-cos-sdk@1.15.2
```
{: screen}

### Go
{: #faq-sdk-check-version-go}

**Method 1** — Check `go.mod` for entries `github.com/IBM/ibm-cos-sdk-go` (v1) or `github.com/IBM/ibm-cos-sdk-go-v2` (v2).

**Method 2** — Run the following command to list the installed version:

```sh
go list -m all | grep ibm-cos-sdk-go
```
{: pre}

Example output:

```screen
$ go list -m all | grep ibm-cos-sdk-go
github.com/IBM/ibm-cos-sdk-go v1.9.0
github.com/IBM/ibm-cos-sdk-go-v2 v1.0.0
```
{: screen}

### Java (Maven)
{: #faq-sdk-check-version-maven}

**Method 1** — Check `<dependencies>` in `pom.xml` for the relevant group ID.

For v1, look for:

```xml
<dependency>
    <groupId>com.ibm.cos</groupId>
    <artifactId>ibm-cos-java-sdk</artifactId>
    <version>2.x.x</version>
</dependency>
```
{: .codeblock}

For v2, look for:

```xml
<dependency>
    <groupId>com.ibm.cos.v2</groupId>
    <artifactId>cos-java-sdk</artifactId>
    <version>1.x.x</version>
</dependency>
```
{: .codeblock}

**Method 2** — Run the following command to list the installed version:

```sh
mvn dependency:tree | grep -i ibm.cos
```
{: pre}

Example output:

```screen
[INFO] ------------------< com.example.app:ibm-cos-example >-------------------
[INFO] Building ibm-cos-example 1.0-SNAPSHOT
[INFO] --- dependency:3.7.0:tree (default-cli) @ ibm-cos-example ---
[INFO] com.example.app:ibm-cos-example:jar:1.0-SNAPSHOT
[INFO] +- com.ibm.cos:ibm-cos-java-sdk:jar:2.13.4:compile             # -> V1
[INFO] |  +- com.ibm.cos:ibm-cos-java-sdk-s3:jar:2.13.4:compile
[INFO] |  +- com.ibm.cos:ibm-cos-java-sdk-kms:jar:2.13.4:compile
[INFO] |  \- com.ibm.cos:ibm-cos-java-sdk-core:jar:2.13.4:compile
[INFO] +- com.ibm.cos.v2:cos-java-sdk:jar:1.0.0:compile               # -> V2
[INFO] |  +- com.ibm.cos.v2:s3:jar:1.0.0:compile
[INFO] |  |  +- com.ibm.cos.v2:aws-xml-protocol:jar:1.0.0:compile
[INFO] |  |  |  \- com.ibm.cos.v2:aws-query-protocol:jar:1.0.0:compile
```
{: screen}

### Java (Gradle)
{: #faq-sdk-check-version-gradle}

**Method 1** — Check `dependencies` in `build.gradle` for the relevant group ID.

For v1, look for:

```groovy
implementation("com.ibm.cos:ibm-cos-java-sdk:2.15.1")
```
{: .codeblock}

For v2, look for:

```groovy
implementation("com.ibm.cos.v2:cos-java-sdk:1.0.1")
```
{: .codeblock}

**Method 2** — Run the following command to list the installed version:

```sh
./gradlew dependencies | grep -i ibm.cos
```
{: pre}

Example output:

```screen
+--- com.ibm.cos:ibm-cos-java-sdk:2.15.1              # -> V1
|    +--- com.ibm.cos:ibm-cos-java-sdk-s3:2.15.1
|    |    +--- com.ibm.cos:ibm-cos-java-sdk-kms:2.15.1
|    |    |    \--- com.ibm.cos:ibm-cos-java-sdk-core:2.15.1
|    |    \--- com.ibm.cos:ibm-cos-java-sdk-core:2.15.1 (*)
|    +--- com.ibm.cos:ibm-cos-java-sdk-kms:2.15.1 (*)
|    \--- com.ibm.cos:ibm-cos-java-sdk-core:2.15.1 (*)
+--- com.ibm.cos.v2:cos-java-sdk:1.0.1                # -> V2
|    +--- com.ibm.cos.v2:s3:1.0.1
|    |    +--- com.ibm.cos.v2:aws-xml-protocol:1.0.1
|    |    |    +--- com.ibm.cos.v2:aws-query-protocol:1.0.1
|    |    |    |    +--- com.ibm.cos.v2:protocol-core:1.0.1
|    |    |    |    |    +--- com.ibm.cos.v2:sdk-core:1.0.1
```
{: screen}

## Is there a request header that reveals which SDK version is making requests?
{: #faq-sdk-user-agent}
{: faq}

Yes. Both SDK versions embed their identity and version number in the `User-Agent` and `x-amz-user-agent` HTTP headers that are sent with every request to IBM COS.

| SDK | v1 `User-Agent` contains | v2 `User-Agent` contains |
| --- | --- | --- |
| Node.js | `ibm-cos-sdk-js/<version>` | `ibm-cos-sdk-js-v2/<version>` |
| Go | `ibm-cos-sdk-go/<version>` | `ibm-cos-sdk-go-v2/<version>` |
| Java | `ibm-cos-java-sdk/<version>` | `ibm-cos-sdk-java-v2/<version>` |
{: caption="User-Agent header values by SDK version" caption-side="bottom"}

Look for the `User-Agent` header on outbound requests to your COS endpoint — the SDK version is clearly visible.

## How can I quickly audit code to identify which SDK version it is using?
{: #faq-sdk-code-audit}
{: faq}

The following table shows code patterns that distinguish SDK v1 from v2 without running the application.

| If you see this in the code | It is using |
| --- | --- |
| `require('ibm-cos-sdk')` | Node.js v1 |
| `.promise()` chained on an SDK call | Node.js v1 |
| `require('ibm-cos-sdk-v2')` | Node.js v2 |
| `new S3Client(config)` | Node.js v2 |
| `client.PutObject(...)` (context cannot be passed) | Go v1 |
| `client.PutObject(ctx, ...)` (context is required) | Go v2 |
| `import com.ibm.cloud.objectstorage` | Java v1 |
| `import com.ibm.cos.v2` | Java v2 |
| `AmazonS3ClientBuilder` / `AmazonS3` type | Java v1 |
| `S3Client.builder()...build()` | Java v2 |
{: caption="Code patterns for identifying SDK version" caption-side="bottom"}
