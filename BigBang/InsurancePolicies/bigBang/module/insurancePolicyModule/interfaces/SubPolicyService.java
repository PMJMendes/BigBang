package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
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

@RemoteServiceRelativePath("SubPolicyService")
public interface SubPolicyService
	extends SearchService, ExactItemSubService, DependentItemSubService
{

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SubPolicyServiceAsync instance;
		public static SubPolicyServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SubPolicyService.class);
			}
			return instance;
		}
	}

	public SubPolicy getEmptySubPolicy(String policyId) throws SessionExpiredException, BigBangException;

	public SubPolicy getSubPolicy(String subPolicyId) throws SessionExpiredException, BigBangException;

	public SubPolicy editSubPolicy(SubPolicy subPolicy) throws  SessionExpiredException, BigBangException;

	public SubPolicy performCalculations(String subPolicyId) throws SessionExpiredException, BigBangException,
			BigBangPolicyCalculationException;
	public void validateSubPolicy(String subPolicyId) throws SessionExpiredException, BigBangException, BigBangPolicyValidationException;

	public InsuredObject includeObject(String subPolicyId, InsuredObject object) throws SessionExpiredException, BigBangException;
	public InsuredObject includeObjectFromClient(String subPolicyId) throws SessionExpiredException, BigBangException;
	public void excludeObject(String subPolicyId, String objectId) throws SessionExpiredException, BigBangException;

	public SubPolicy transferToPolicy(String subPolicyId, String newPolicyId) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;

	public Receipt createReceipt(String policyId, Receipt receipt) throws SessionExpiredException, BigBangException;
	public Expense createExpense(Expense expense) throws SessionExpiredException, BigBangException;

	public SubPolicy voidSubPolicy(PolicyVoiding voiding) throws SessionExpiredException, BigBangException;

	public void deleteSubPolicy(String policyId, String reason) throws SessionExpiredException, BigBangException;
}
