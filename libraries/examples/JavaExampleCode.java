// Required libraries
import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.SdkClientException;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.ibm.cloud.objectstorage.services.s3.transfer.TransferManager;
import com.ibm.cloud.objectstorage.services.s3.transfer.TransferManagerBuilder;
import com.ibm.cloud.objectstorage.services.s3.transfer.Upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class JavaExampleCode {
    private static AmazonS3 _cosClient;
    private static String api_key;
    private static String service_instance_id;
    private static String endpoint_url;
    private static String location;

    public static void main(String[] args) throws IOException
    {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();

        // Constants for IBM COS values
        SDKGlobalConfiguration.IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";
        api_key = "<api-key>"; // example: xxxd12V2QHXbjaM99G9tWyYDgF_0gYdlQ8aWALIQxXx4
        service_instance_id = "<resource-instance-id>"; // example: crn:v1:bluemix:public:cloud-object-storage:global:a/xx999cd94a0dda86fd8eff3191349999:9999b05b-x999-4917-xxxx-9d5b326a1111::
        endpoint_url = "<endpoint>"; // example: https://s3.us-south.cloud-object-storage.appdomain.cloud
        location = "<storage-class>"; // example: us-south-standard

        // Create client connection details
        _cosClient = createClient(api_key, service_instance_id, endpoint_url, location);

        // Setting string values
        String bucketName = "java.bucket" + UUID.randomUUID().toString().replace("-","");
        String itemName = UUID.randomUUID().toString().replace("-","") + "_java_file.txt";
        String fileText = "This is a test file from the Java code sample!!!";

        // create a new bucket
        createBucket(bucketName, _cosClient);

        // get the list of buckets
        listBuckets(_cosClient);

        // create a new text file & upload
        createTextFile(bucketName, itemName, fileText);

        // get the list of files from the new bucket
        listObjects(bucketName, _cosClient);

        // remove new file
        deleteItem(bucketName, itemName);

        // create & upload the large file using transfer manager & remove large file
        createLargeFile(bucketName);

        // remove the new bucket
        deleteBucket(bucketName);
    }

    private static void createLargeFile(String bucketName)  throws IOException {
        String fileName = "Sample"; //Setting the File Name

        try {
            File uploadFile = File.createTempFile(fileName,".tmp");
            uploadFile.deleteOnExit();
            fileName = uploadFile.getName();

            largeObjectUpload(bucketName, uploadFile);
        } catch (InterruptedException e) {
            System.out.println("object upload timed out");
        }

        deleteItem(bucketName, fileName); // remove new large file
    }

    // Create client connection
    public static AmazonS3 createClient(String api_key, String service_instance_id, String endpoint_url, String location)
    {
        AWSCredentials credentials;
        credentials = new BasicIBMOAuthCredentials(api_key, service_instance_id);

        ClientConfiguration clientConfig = new ClientConfiguration().withRequestTimeout(5000);
        clientConfig.setUseTcpKeepAlive(true);

        AmazonS3 cosClient = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint_url, location)).withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfig).build();
        return cosClient;
    }

    // Create a new bucket
    public static void createBucket(String bucketName, AmazonS3 cosClient)
    {
        cosClient.createBucket(bucketName);
        System.out.printf("Bucket: %s created!\n", bucketName);
    }

    // Retrieve the list of available buckets
    public static void listBuckets(AmazonS3 cosClient)
    {
        System.out.println("Listing buckets");
        final List<Bucket> bucketList = _cosClient.listBuckets();
        for (final Bucket bucket : bucketList) {
            System.out.println(bucket.getName());
        }
        System.out.println();
    }

    // Retrieve the list of contents for a bucket
    public static void listObjects(String bucketName, AmazonS3 cosClient)
    {
        System.out.println("Listing objects in bucket " + bucketName);
        ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
        }
        System.out.println();
    }

    // Create file and upload to new bucket
    public static void createTextFile(String bucketName, String itemName, String fileText) {
        System.out.printf("Creating new item: %s\n", itemName);

        InputStream newStream = new ByteArrayInputStream(fileText.getBytes(Charset.forName("UTF-8")));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileText.length());

        PutObjectRequest req = new PutObjectRequest(bucketName, itemName, newStream, metadata);
        _cosClient.putObject(req);

        System.out.printf("Item: %s created!\n", itemName);
    }

    // Delete item
    public static void deleteItem(String bucketName, String itemName) {
        System.out.printf("Deleting item: %s\n", itemName);
        _cosClient.deleteObject(bucketName, itemName);
        System.out.printf("Item: %s deleted!\n", itemName);
    }

    // Delete bucket
    public static void deleteBucket(String bucketName) {
        System.out.printf("Deleting bucket: %s\n", bucketName);
        _cosClient.deleteBucket(bucketName);
        System.out.printf("Bucket: %s deleted!\n", bucketName);
    }

    //  Upload large file to new bucket
    public static void largeObjectUpload(String bucketName, File uploadFile) throws IOException, InterruptedException {

        if (!uploadFile.isFile()) {
            System.out.printf("The file does not exist or is not accessible.\n");
            return;
        }

        System.out.println("Starting large file upload with TransferManager");

        //set the part size to 5 MB
        long partSize = 1024 * 1024 * 20;

        //set the threshold size to 5 MB
        long thresholdSize = 1024 * 1024 * 20;

        AmazonS3 s3client = createClient( api_key, service_instance_id, endpoint_url, location);

        TransferManager transferManager = TransferManagerBuilder.standard()
                .withS3Client(s3client)
                .withMinimumUploadPartSize(partSize)
                .withMultipartCopyThreshold(thresholdSize)
                .build();

        try {
            Upload lrgUpload = transferManager.upload(bucketName, uploadFile.getName(), uploadFile);
            lrgUpload.waitForCompletion();
            System.out.println("Large file upload complete!");
        } catch (SdkClientException e) {
            System.out.printf("Upload error: %s\n", e.getMessage());
        } finally {
            transferManager.shutdownNow();
        }
    }
}
