package com.premiumminds.BigBang.Jewel.SysObjects.StorageConnect ;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.StorageObject;

public class StorageMethods {

	/** Global instance of the JSON factory. */
	static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**
	 * Fetches the metadata for the given bucket.
	 * 
	 * @param bucketName
	 *            the name of the bucket to get metadata about.
	 * @return a Bucket containing the bucket's metadata.
	 */
	public static Bucket getBucket(String bucketName) throws IOException,
			GeneralSecurityException {
		Storage client = StorageFactory.getService();

		Storage.Buckets.Get bucketRequest = client.buckets().get(bucketName);

		// Fetch the full set of the bucket's properties (e.g. include the ACLs
		// in the response)
		bucketRequest.setProjection("full");
		return bucketRequest.execute();
	}

	/**
	 * Fetch a list of the objects within the given bucket.
	 * 
	 * @param bucketName
	 *            the name of the bucket to list.
	 * @return a list of the contents of the specified bucket.
	 */
	public static List<StorageObject> listBucket(String bucketName)
			throws IOException, GeneralSecurityException {

		Storage client = StorageFactory.getService();
		Storage.Objects.List listRequest = client.objects().list(bucketName);

		List<StorageObject> results = new ArrayList<StorageObject>();
		Objects objects;

		// Iterate through each page of results, and add them to our results
		// list.
		do {
			objects = listRequest.execute();
			// Add the items in this page of results to the list we'll return.
			if (objects.getItems() != null && objects.getItems().size() > 0) {
				results.addAll(objects.getItems());
			}

			// Get the next page, in the next iteration of this loop.
			listRequest.setPageToken(objects.getNextPageToken());
		} while (null != objects.getNextPageToken());

		return results;
	}
	
	/**
	 * Fetch a list of the objects within the given bucket.
	 * 
	 * @param bucketName
	 *            the name of the bucket to list.
	 * @return a list of the contents of the specified bucket.
	 */
	public static StorageObject getObject(String bucketName, String objectName)
			throws IOException, GeneralSecurityException {

		Storage client = StorageFactory.getService();
		Storage.Objects.Get getRequest = client.objects().get(bucketName, objectName);

		return getRequest.execute();
	}
	
	/**
	 * Fetch a list of the objects within the given bucket.
	 * 
	 * @param bucketName
	 *            the name of the bucket to list.
	 * @return a list of the contents of the specified bucket.
	 */
	public static ByteArrayOutputStream downloadObject(String bucketName, String objectName)
			throws IOException, GeneralSecurityException {

		Storage client = StorageFactory.getService();
		Storage.Objects.Get getRequest = client.objects().get(bucketName, objectName);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		getRequest.getMediaHttpDownloader().setDirectDownloadEnabled(false);
		getRequest.executeMediaAndDownloadTo(out);

		return out;
	}

	/**
	 * Uploads data to an object in a bucket.
	 * 
	 * @param name
	 *            the name of the destination object.
	 * @param contentType
	 *            the MIME type of the data.
	 * @param file
	 *            the file to upload.
	 * @param bucketName
	 *            the name of the bucket to create the object in.
	 */
	public static void uploadFile(String name, String contentType, File file,
			String bucketName) throws IOException, GeneralSecurityException {
		InputStreamContent contentStream = new InputStreamContent(contentType,
				new FileInputStream(file));
		// Setting the length improves upload performance
		contentStream.setLength(file.length());
		StorageObject objectMetadata = new StorageObject()
		// Set the destination object name
				.setName(name)
				// Set the access control list to publicly read-only
				.setAcl(Arrays.asList(new ObjectAccessControl().setEntity(
						"allUsers").setRole("READER")));

		// Do the insert
		Storage client = StorageFactory.getService();
		Storage.Objects.Insert insertRequest = client.objects().insert(
				bucketName, objectMetadata, contentStream);

		insertRequest.execute();
	}

	/**
	 * Deletes an object in a bucket.
	 * 
	 * @param path
	 *            the path to the object to delete.
	 * @param bucketName
	 *            the bucket the object is contained in.
	 */
	public static void deleteObject(String path, String bucketName)
			throws IOException, GeneralSecurityException {
		Storage client = StorageFactory.getService();
		client.objects().delete(bucketName, path).execute();
	}
}
