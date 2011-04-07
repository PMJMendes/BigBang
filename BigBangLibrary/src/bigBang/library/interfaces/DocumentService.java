package bigBang.library.interfaces;

import bigBang.library.shared.Document;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DocumentService")
public interface DocumentService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static DocumentServiceAsync instance;
		public static DocumentServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(DocumentService.class);
			}
			return instance;
		}
	}
	
	public Document[] getDocuments(String entityTypeId, String entityId);
	
	public Document createDocument(String entityTypeId, String entityId, Document document);
	
	public Document saveDocument(Document document);
	
	public void deleteDocument(String id);
}
