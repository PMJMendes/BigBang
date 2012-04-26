package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SubCasualtyService")
public interface SubCasualtyService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SubCasualtyServiceAsync instance;
		public static SubCasualtyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SubCasualtyService.class);
			}
			return instance;
		}
	}

	public SubCasualty getSubCasualty(String subCasualtyId) throws SessionExpiredException, BigBangException;

	public SubCasualty editSubCasualty(SubCasualty subCasualty) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request) throws SessionExpiredException, BigBangException;

	public SubCasualty markForClosing(String subCasualtyId, String revisorId) throws SessionExpiredException, BigBangException;
	public SubCasualty closeProcess(String subCasualtyId) throws SessionExpiredException, BigBangException;
	public SubCasualty rejectClosing(String subCasualtyId, String reason) throws SessionExpiredException, BigBangException;

	public void deleteSubCasualty(String subCasualtyId, String reason) throws SessionExpiredException, BigBangException;
}
