package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicyOLD;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.library.interfaces.DependentItemSubService;
import bigBang.library.interfaces.ExactItemSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsurancePolicyServiceOLD")
public interface InsurancePolicyServiceOLD
	extends SearchService, DependentItemSubService, ExactItemSubService
{

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static InsurancePolicyServiceOLDAsync instance;
		public static InsurancePolicyServiceOLDAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(InsurancePolicyServiceOLD.class);
			}
			return instance;
		}
	}

	public InsurancePolicyOLD getPolicy(String policyId) throws SessionExpiredException, BigBangException;
	public InsurancePolicyOLD.TableSection getPage(String policyId, String insuredObjectId, String exerciseId)
			throws SessionExpiredException, BigBangException;

	public Remap[] openPolicyScratchPad(String policyId) throws SessionExpiredException, BigBangException;
	public InsurancePolicyOLD initPolicyInPad(InsurancePolicyOLD policy)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsurancePolicyOLD getPolicyInPad(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsurancePolicyOLD updateHeader(InsurancePolicyOLD policy)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public InsurancePolicyOLD.TableSection getPageForEdit(String policyId, String objectId, String exerciseId)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsurancePolicyOLD.TableSection savePage(InsurancePolicyOLD.TableSection data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public InsuredObjectOLD getObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsuredObjectOLD createObjectInPad(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsuredObjectOLD createObjectFromClientInPad(String policyId) throws SessionExpiredException, BigBangException;
	public InsuredObjectOLD updateObjectInPad(InsuredObjectOLD data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Exercise getExerciseInPad(String exerciseId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Exercise createFirstExercise(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Exercise updateExerciseInPad(Exercise data) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteExerciseInPad(String exerciseId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Remap[] commitPad(String policyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Remap[] discardPad(String policyId) throws SessionExpiredException, BigBangException;

	public InsurancePolicyOLD performCalculations(String policyId) throws SessionExpiredException, BigBangException,
			BigBangPolicyCalculationException;
	public void validatePolicy(String policyId) throws SessionExpiredException, BigBangException, BigBangPolicyValidationException;

	public InsuredObjectOLD includeObject(String policyId, InsuredObjectOLD object) throws SessionExpiredException, BigBangException;
	public InsuredObjectOLD includeObjectFromClient(String policyId) throws SessionExpiredException, BigBangException;
	public void excludeObject(String policyId, String objectId) throws SessionExpiredException, BigBangException;

	public Exercise openNewExercise(String policyId, Exercise exercise) throws SessionExpiredException, BigBangException;

	public InsurancePolicyOLD transferToClient(String policyId, String newClientId) throws SessionExpiredException, BigBangException;

	public void createDebitNote(String policyId, DebitNote note) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Receipt createReceipt(String policyId, Receipt receipt) throws SessionExpiredException, BigBangException;
	public Expense createExpense(Expense expense) throws SessionExpiredException, BigBangException;
	public Negotiation createNegotiation(Negotiation negotiation) throws SessionExpiredException, BigBangException;

	public InsurancePolicyOLD voidPolicy(PolicyVoiding voiding) throws SessionExpiredException, BigBangException;

	public void deletePolicy(String policyId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	//public RiskAnalisys createRiskAnalisys(String policyId, RiskAnalisys riskAnalisys) throws SessionExpiredException, BigBangException;
	//public 
}
