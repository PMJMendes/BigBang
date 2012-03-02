package bigBang.module.riskAnalisysModule.interfaces;

import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("RiskAnalisysService")
public interface RiskAnalisysService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static RiskAnalisysServiceAsync instance;
		public static RiskAnalisysServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(RiskAnalisysService.class);
			}
			return instance;
		}
	}
}
