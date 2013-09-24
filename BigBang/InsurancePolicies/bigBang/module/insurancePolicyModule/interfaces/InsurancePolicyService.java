package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DebitNoteBatch;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.interfaces.DependentItemSubService;
import bigBang.library.interfaces.ExactItemSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InsurancePolicyService")
public interface InsurancePolicyService
	extends SearchService, ExactItemSubService, DependentItemSubService
{
	public static class Util
	{
		private static InsurancePolicyServiceAsync instance;
		public static InsurancePolicyServiceAsync getInstance()
		{
			if (instance == null)
				instance = GWT.create(InsurancePolicyService.class);

			return instance;
		}
	}

	public InsurancePolicy getEmptyPolicy(String subLineId, String clientId) throws SessionExpiredException, BigBangException;

	public InsurancePolicy getPolicy(String policyId) throws SessionExpiredException, BigBangException;

	public InsurancePolicy editPolicy(InsurancePolicy policy) throws  SessionExpiredException, BigBangException, BigBangPolicyValidationException;

	public InsurancePolicy performCalculations(String policyId)
			throws SessionExpiredException, BigBangException, BigBangPolicyCalculationException;
	public void validatePolicy(String policyId) throws SessionExpiredException, BigBangException, BigBangPolicyValidationException;

	public InsuredObject includeObject(String policyId, InsuredObject object) throws SessionExpiredException, BigBangException;
	public InsuredObject includeObjectFromClient(String policyId) throws SessionExpiredException, BigBangException;
	public void excludeObject(String policyId, String objectId) throws SessionExpiredException, BigBangException;

	public InsurancePolicy openNewExercise(String policyId, Exercise exercise) throws SessionExpiredException, BigBangException;

	public InsurancePolicy transferToClient(String policyId, String newClientId) throws SessionExpiredException, BigBangException;

	public void createDebitNote(String policyId, DebitNote note) throws SessionExpiredException, BigBangException;

	public ManagerTransfer createManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public SubPolicy createSubPolicy(SubPolicy subPolicy) throws SessionExpiredException, BigBangException;
	public Receipt createReceipt(String policyId, Receipt receipt) throws SessionExpiredException, BigBangException;
	public Expense createExpense(Expense expense) throws SessionExpiredException, BigBangException;
	public Negotiation createNegotiation(Negotiation negotiation) throws SessionExpiredException, BigBangException;

	public void createSubPolicyReceipts(DebitNoteBatch batch) throws SessionExpiredException, BigBangException;

	public InsurancePolicy voidPolicy(PolicyVoiding voiding) throws SessionExpiredException, BigBangException;
	public InsurancePolicy reactivatePolicy(String policyId) throws SessionExpiredException, BigBangException;

	public void deletePolicy(String policyId) throws SessionExpiredException, BigBangException;

	public ManagerTransfer massCreateManagerTransfer(ManagerTransfer transfer) throws SessionExpiredException, BigBangException;

	//public RiskAnalisys createRiskAnalisys(String policyId, RiskAnalisys riskAnalisys) throws SessionExpiredException, BigBangException;
	//public 
}
