package bigBang.library.interfaces;

import bigBang.definitions.shared.ZipCode;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ZipCodeService")
public interface ZipCodeService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ZipCodeServiceAsync instance;
		public static ZipCodeServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ZipCodeService.class);
			}
			return instance;
		}
	}
	
	public ZipCode getZipCode(String code) throws SessionExpiredException, BigBangException;

}
