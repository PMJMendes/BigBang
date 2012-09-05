package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("PolicyObjectServiceOLD")
public interface PolicyObjectServiceOLD
	extends SearchService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static PolicyObjectServiceOLDAsync instance;
		public static PolicyObjectServiceOLDAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(PolicyObjectServiceOLD.class);
			}
			return instance;
		}
	}

	public InsuredObjectOLD getObject(String objectId) throws SessionExpiredException, BigBangException;
}
