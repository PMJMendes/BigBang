package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.BigBangPolicyValidationException;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.interfaces.DependentItemSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.CorruptedPadException;
import bigBang.library.shared.SessionExpiredException;
import bigBang.module.insurancePolicyModule.shared.BigBangPolicyCalculationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SubPolicyService")
public interface SubPolicyService
	extends SearchService, DependentItemSubService
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

	public SubPolicy getSubPolicy(String subPolicyId) throws SessionExpiredException, BigBangException;
	public SubPolicy.TableSection getPage(String subPolicyId, String objectId, String exerciseId)
			throws SessionExpiredException, BigBangException;

	public Remap[] openSubPolicyScratchPad(String subPolicyId) throws SessionExpiredException, BigBangException;
	public SubPolicy initSubPolicyInPad(SubPolicy subPolicy)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public SubPolicy getSubPolicyInPad(String subPolicyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public SubPolicy updateHeader(SubPolicy subPolicy)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public SubPolicy.TableSection getPageForEdit(String subPolicyId, String objectId, String exerciseId)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public SubPolicy.TableSection savePage(SubPolicy.TableSection data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;

	public InsuredObjectOLD getObjectInPad(String objectId)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsuredObjectOLD createObjectInPad(String subPolicyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public InsuredObjectOLD createObjectFromClientInPad(String subPolicyId) throws SessionExpiredException, BigBangException;
	public InsuredObjectOLD updateObjectInPad(InsuredObjectOLD data)
			throws SessionExpiredException, BigBangException, CorruptedPadException;
	public void deleteObjectInPad(String objectId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Exercise getExerciseInPad(String exerciseId) throws SessionExpiredException, BigBangException, CorruptedPadException;

	public Remap[] commitPad(String subPolicyId) throws SessionExpiredException, BigBangException, CorruptedPadException;
	public Remap[] discardPad(String subPolicyId) throws SessionExpiredException, BigBangException;

	public SubPolicy performCalculations(String subPolicyId) throws SessionExpiredException, BigBangException,
			BigBangPolicyCalculationException;
	public void validateSubPolicy(String subPolicyId) throws SessionExpiredException, BigBangException, BigBangPolicyValidationException;

	public InsuredObjectOLD includeObject(String subPolicyId, InsuredObjectOLD object) throws SessionExpiredException, BigBangException;
	public InsuredObjectOLD includeObjectFromClient(String subPolicyId) throws SessionExpiredException, BigBangException;
	public void excludeObject(String subPolicyId, String objectId) throws SessionExpiredException, BigBangException;

	public SubPolicy transferToPolicy(String subPolicyId, String newPolicyId) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoOrDocumentRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;

	public Receipt createReceipt(String policyId, Receipt receipt) throws SessionExpiredException, BigBangException;
	public Expense createExpense(Expense expense) throws SessionExpiredException, BigBangException;

	public SubPolicy voidSubPolicy(PolicyVoiding voiding) throws SessionExpiredException, BigBangException;

	public void deleteSubPolicy(String policyId, String reason) throws SessionExpiredException, BigBangException;
}
