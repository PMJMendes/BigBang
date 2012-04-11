package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("CasualtyService")
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

	public Casualty getCasualty(String casualtyId) throws SessionExpiredException, BigBangException;

	public Casualty editCasualty(Casualty casualty) throws SessionExpiredException, BigBangException;

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Casualty closeProcess(String casualtyId) throws SessionExpiredException, BigBangException;

	public void deleteCasualty(String casualtyId, String reason) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;
}
