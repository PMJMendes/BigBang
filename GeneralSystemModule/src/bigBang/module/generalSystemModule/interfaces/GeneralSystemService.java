package bigBang.module.generalSystemModule.interfaces;

import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.generalSystemModule.shared.GeneralSystem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GeneralSystemService")
public interface GeneralSystemService
	extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static GeneralSystemServiceAsync instance;
		public static GeneralSystemServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(GeneralSystemService.class);
			}
			return instance;
		}
	}
	
	public String getGeneralSystemProcessId() throws SessionExpiredException, BigBangException;
	public String getGeneralSystemId() throws SessionExpiredException, BigBangException;
	public GeneralSystem getGeneralSystem() throws SessionExpiredException, BigBangException;
}
