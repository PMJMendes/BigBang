package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.TotalLossFile;
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

	public SubCasualty sendNotification(String subCasualtyId) throws SessionExpiredException, BigBangException;

	public Assessment createAssessment(Assessment assessment) throws SessionExpiredException, BigBangException;
	public MedicalFile createMedicalFile(MedicalFile file) throws SessionExpiredException, BigBangException;
	public TotalLossFile createTotalLoss(TotalLossFile file) throws SessionExpiredException, BigBangException;
	public Receipt createReceipt(String subCasualtyId, Receipt receipt) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public SubCasualty markForClosing(String subCasualtyId, String revisorId) throws SessionExpiredException, BigBangException;
	public SubCasualty closeProcess(String subCasualtyId) throws SessionExpiredException, BigBangException;
	public SubCasualty rejectClosing(String subCasualtyId, String reason) throws SessionExpiredException, BigBangException;

	public void deleteSubCasualty(String subCasualtyId, String reason) throws SessionExpiredException, BigBangException;
}
