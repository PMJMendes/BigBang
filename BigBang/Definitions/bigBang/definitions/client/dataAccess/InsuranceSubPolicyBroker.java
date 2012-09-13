package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;

public interface InsuranceSubPolicyBroker extends
		DataBrokerInterface<SubPolicy> {

	//GET & SET
	
	public void getSubPolicy(String subPolicyId, ResponseHandler<SubPolicy> handler);

	public void getEmptySubPolicy(String policyId, ResponseHandler<SubPolicy> handler);

	public SubPolicy getSubPolicyHeader(String subPolicyId);

	public SubPolicy updateSubPolicyHeader(SubPolicy subPolicy);

	public void persistSubPolicy(String subPolicyId, ResponseHandler<SubPolicy> handler);

	public SubPolicy discardEditData(String subPolicyId);

	public void removeSubPolicy(String subPolicyId, String reason, ResponseHandler<String> handler);

	public void getInsuredObject(String subPolicyId, String objectId, ResponseHandler<InsuredObject> handler);

	public InsuredObject createInsuredObject(String subPolicyId);

	public InsuredObject updateInsuredObject(String subPolicyId, InsuredObject object);

	public void removeInsuredObject(String subPolicyId, String objectId);

	public FieldContainer getContextForSubPolicy(String subPolicyId, String exerciseId);
	
	public void saveContextForSubPolicy(String policyId, String exerciseId, FieldContainer contents);

	public FieldContainer getContextForInsuredObject(String subPolicyId, String objectId, String exerciseId);

	public void saveContextForInsuredObject(String subPolicyId, String objectId, String exerciseId, FieldContainer contents);


	// OTHER OPS
	
	public SearchDataBroker<SubPolicyStub> getSearchBroker();

	public void getSubPoliciesForPolicy(String ownerId,
			ResponseHandler<Collection<SubPolicyStub>> responseHandler);

	void createReceipt(String subPolicyId, Receipt receipt, ResponseHandler<Receipt> handler);
	void createReceipt(Receipt receipt, ResponseHandler<Receipt> handler);
	
	void validateSubPolicy(String subPolicyId, ResponseHandler<Void> handler);
	
	void includeInsuredObject(String subPolicyId, InsuredObject object, ResponseHandler<InsuredObject> handler);

	void includeObjectFromClient(String subPolicyId, ResponseHandler<InsuredObject> handler);
	
	void excludeObject(String subPolicyId, String objectId, ResponseHandler<Void> handler);
	
	void transferToInsurancePolicy(String subPolicyId, String newPolicyId, ResponseHandler<SubPolicy> handler);
	
	void createInfoOrDocumentRequest(InfoOrDocumentRequest request, ResponseHandler<InfoOrDocumentRequest> handler);

	void executeDetailedCalculations(String subPolicyId, ResponseHandler<SubPolicy> handler);
	
	void voidSubPolicy(PolicyVoiding voiding, ResponseHandler<SubPolicy> responseHandler);

	void createExpense(Expense expense, ResponseHandler<Expense> handler);
}
