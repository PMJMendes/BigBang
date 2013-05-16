package bigBang.library.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bbfile")
public interface FileService
	extends RemoteService, DependentItemSubService
{
	public static final String GET_PREFIX = "bbfile?fileref=";
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static FileServiceAsync instance;
		public static FileServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(FileService.class);
			}
			return instance;
		}
	}

	void process(String formatId, String storageId) throws SessionExpiredException, BigBangException;

	void Discard(String pstrID) throws SessionExpiredException, BigBangException;
	void Discard(String[] parrIDs) throws SessionExpiredException, BigBangException;
}
