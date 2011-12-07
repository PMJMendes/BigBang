package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsurancePolicyService")
public interface InsurancePolicyService extends SearchService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static InsurancePolicyServiceAsync instance;
		public static InsurancePolicyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(InsurancePolicyService.class);
			}
			return instance;
		}
	}
	
	public InsurancePolicy getPolicy(String policyId) throws SessionExpiredException, BigBangException;
	public InsurancePolicy.TableSection getPage(String policyId, String insuredObjectId, String exerciseId)
			throws SessionExpiredException, BigBangException;

	public InsurancePolicy initializeNewPolicy(InsurancePolicy policy) throws SessionExpiredException, BigBangException;
	public InsurancePolicy openForEdit(InsurancePolicy policy) throws SessionExpiredException, BigBangException;

	public InsurancePolicy updateHeader(InsurancePolicy policy) throws SessionExpiredException, BigBangException;

	public InsurancePolicy.TableSection getPageForEdit(String scratchPadId, String tempObjectId, String tempExerciseId)
			throws SessionExpiredException, BigBangException;
	public InsurancePolicy.TableSection savePage(InsurancePolicy.TableSection data)
			throws SessionExpiredException, BigBangException;

	public TipifiedListItem[] getPadItemsFilter(String listId, String scratchPadId) throws SessionExpiredException, BigBangException;

	public InsuredObject getObjectInPad(String tempObjectId) throws SessionExpiredException, BigBangException;
	public InsuredObject createObjectInPad(String scratchPadId) throws SessionExpiredException, BigBangException;
	public InsuredObject createObjectFromClientInPad(String scratchPadId) throws SessionExpiredException, BigBangException;
	public InsuredObject updateObjectInPad(InsuredObject data) throws SessionExpiredException, BigBangException;
	public void deleteObjectInPad(String tempObjectId) throws SessionExpiredException, BigBangException;

	public Exercise getExerciseInPad(String tempExerciseId) throws SessionExpiredException, BigBangException;
	public Exercise createFirstExercise(String scratchPadId) throws SessionExpiredException, BigBangException;
	public Exercise updateExerciseInPad(Exercise data) throws SessionExpiredException, BigBangException;
	public void deleteExerciseInPad(String tempExerciseId) throws SessionExpiredException, BigBangException;

	public InsurancePolicy commitPolicy(String scratchPadId) throws SessionExpiredException, BigBangException;
	public InsurancePolicy discardPolicy(String scratchPadId) throws SessionExpiredException, BigBangException;

	public InsuredObject includeObject(String policyId, InsuredObject object) throws SessionExpiredException, BigBangException;
	public InsuredObject includeObjectFromClient(String policyId) throws SessionExpiredException, BigBangException;
	public void excludeObject(String policyId, String objectId) throws SessionExpiredException, BigBangException;

	public Exercise openNewExercise(String policyId, Exercise exercise) throws SessionExpiredException, BigBangException;
	public Exercise editExercise(String policyId, Exercise exercise) throws SessionExpiredException, BigBangException;

	public InsurancePolicy voidPolicy(String policyId) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Receipt createReceipt(String policyId, Receipt receipt) throws SessionExpiredException, BigBangException;

	public void deletePolicy(String policyId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	
	
	//public RiskAnalisys createRiskAnalisys(String policyId, RiskAnalisys riskAnalisys) throws SessionExpiredException, BigBangException;
	//public 
	
}
