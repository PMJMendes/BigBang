package bigBang.library.interfaces;

import bigBang.definitions.shared.Document;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DocumentService")
public interface DocumentService
	extends RemoteService
{
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

	public Document[] getDocuments(String ownerId) throws SessionExpiredException, BigBangException;
	public Document getDocument(String docId) throws SessionExpiredException, BigBangException;
	public Document createDocument(Document document) throws SessionExpiredException, BigBangException;
	public Document saveDocument(Document document) throws SessionExpiredException, BigBangException;
	public void deleteDocument(String id) throws SessionExpiredException, BigBangException;
}
