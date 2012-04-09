package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.library.interfaces.SearchService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Casualtyservice")
public interface CasualtyService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static CasualtyServiceAsync instance;
		public static CasualtyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(CasualtyService.class);
			}
			return instance;
		}
	}
	
	public Casualty getCasualty(String casualtyId);
	
	public Casualty updateCasualty(Casualty casualty);
	
	public void deleteCasualty(String casualtyId);
}
