package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.library.interfaces.DependentItemSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyValidationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsurancePolicyService")
public interface InsurancePolicyService extends SearchService, DependentItemSubService {

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

	public Remap[] openPolicyScratchPad(String policyId) throws SessionExpiredException, BigBangException;
	public InsurancePolicy initPolicyInPad(InsurancePolicy policy)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsurancePolicy getPolicyInPad(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsurancePolicy updateHeader(InsurancePolicy policy)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public InsurancePolicy.TableSection getPageForEdit(String policyId, String objectId, String exerciseId)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsurancePolicy.TableSection savePage(InsurancePolicy.TableSection data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public InsuredObject getObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsuredObject createObjectInPad(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsuredObject createObjectFromClientInPad(String policyId) throws SessionExpiredException, BigBangException;
	public InsuredObject updateObjectInPad(InsuredObject data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Exercise getExerciseInPad(String exerciseId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Exercise createFirstExercise(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Exercise updateExerciseInPad(Exercise data) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteExerciseInPad(String exerciseId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Remap[] commitPad(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Remap[] discardPad(String policyId) throws SessionExpiredException, BigBangException;

	public InsurancePolicy performCalculations(String policyId) throws SessionExpiredException, BigBangException,
			BigBangPolicyCalculationException;
	public void validatePolicy(String policyId) throws SessionExpiredException, BigBangException, BigBangPolicyValidationException;

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
