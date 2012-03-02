package bigBang.library.interfaces;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("BigBangProcessService")
public interface BigBangProcessService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static BigBangProcessServiceAsync instance;
		public static BigBangProcessServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(BigBangProcessService.class);
			}
			return instance;
		}
	}

	public BigBangProcess getProcess(String dataObjectId) throws SessionExpiredException, BigBangException;
	public BigBangProcess[] getSubProcesses(String dataObjectId) throws SessionExpiredException, BigBangException;
}
