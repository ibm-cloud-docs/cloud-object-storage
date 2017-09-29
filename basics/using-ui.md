---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Upload an object

The user interface portal provides a high level view of a storage account.  It is possible to create buckets and upload objects using the portal, but typically most interaction with the object store is done through the API by a client application.

## Using buckets

1. Choose **Dashboard** from the left navigation menu.
2. Cloud Object Storage instances appear under "Global Services" in the dashboard.
3. Select an instance and then navigate to **Buckets and objects**.
4. A new bucket can be added by clicking on the **Create bucket** button.
5. The new bucket name can be entered and the new bucket added, by clicking the **Add** button. Bucket names must be DNS-compliant. Names must be between 3 and 63 characters long, must be made of lowercase letters, numbers, and dashes, must be globally unique, and cannot appear to be an IP address. A common approach to ensuring uniqueness is to append a UUID or other distinctive suffix to bucket names.
6. A bucket can be deleted, by clicking the red **Delete** button to the extreme right of the bucket name. This opens a confirmation window.
7. **Note: only empty buckets can be deleted**.  Attempting to delete a non-empty bucket will result in an error.
7. Click the **Delete** button to remove the bucket from the account.


## Using objects

1. The objects present within a bucket can be viewed, by clicking the bucket name on the buckets list.
2. The header is updated to include the following info:
  * Name of the account currently being viewed
  * The current region being viewed
  * The name of the bucket whose objects are currently displayed
  * The type of the bucket whose objects are currently displayed
3. Above the grid on the left is a link with the name of the bucket currently viewed. Click the link to show the list of buckets again.
4. The grid shows the list of objects in the bucket.
5. A new object can be added, by clicking the **+** button at the right of the first row.
6. The file can be selected from the file system, by clicking the **select** button and the new file uploaded, by clicking the **Add** button.  When using the portal to add an object, file size is limited to 200 megabytes. (All objects are limited to 10TB when using the API, including multipart uploads which are capped at 10k parts of no larger than 5GB each.)
7. An object can be deleted, by clicking on the red **-** button to the extreme right of the object name. This opens a confirmation window.
8. Click the **Delete** button to remove the object from the bucket.
