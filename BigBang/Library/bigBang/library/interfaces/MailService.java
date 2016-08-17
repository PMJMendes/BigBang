package bigBang.library.interfaces;

import bigBang.definitions.shared.Document;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.MailItem;
import bigBang.library.shared.MailItemStub;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("MailService")
public interface MailService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static MailServiceAsync instance;
		public static MailServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(MailService.class);
			}
			return instance;
		}
	}

	MailItemStub[] getItems() throws SessionExpiredException, BigBangException;
	MailItemStub[] getItemsAll() throws SessionExpiredException, BigBangException;
	MailItemStub[] getFolder(MailItemStub current) throws SessionExpiredException, BigBangException;
	MailItem getItem(String folderId, String id) throws SessionExpiredException, BigBangException;
	Document getAttAsDoc(String emailId, String folderId, String attachmentId) throws SessionExpiredException, BigBangException;
}
