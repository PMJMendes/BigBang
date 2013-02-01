package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DebitNoteBatch;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.SubPolicy;

public interface InsurancePolicyBroker extends DataBrokerInterface<InsurancePolicy> {

	//GET & SET

	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);

	public void getEmptyPolicy(String subLineId, String clientId, ResponseHandler<InsurancePolicy> handler);

	public InsurancePolicy getPolicyHeader(String policyId);

	public InsurancePolicy updatePolicyHeader(InsurancePolicy policy);
	
	public void updateCoverages(StructuredFieldContainer.Coverage[] coverages);

	public void persistPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);

	public InsurancePolicy discardEditData(String policyId);

	public void removePolicy(String policyId, ResponseHandler<String> handler);

	public ComplexFieldContainer.ExerciseData createExercise(String policyId);

	public ComplexFieldContainer.ExerciseData updateExercise(String policyId, ComplexFieldContainer.ExerciseData exercise);

	public InsuredObjectStub[] getAlteredObjects(String policyId);

	public void getInsuredObject(String policyId, String objectId, ResponseHandler<InsuredObject> handler);

	public InsuredObject createInsuredObject(String policyId);

	public InsuredObject updateInsuredObject(String policyId, InsuredObject object);

	public InsuredObjectStub removeInsuredObject(String policyId, String objectId);

	public FieldContainer getContextForPolicy(String policyId, String exerciseId);
	
	public void saveContextForPolicy(String policyId, String exerciseId, FieldContainer contents);

	public FieldContainer getContextForInsuredObject(String policyId, String objectId, String exerciseId);

	public void saveContextForInsuredObject(String policyId, String objectId, String exerciseId, FieldContainer contents);


	// OTHER OPS

	public SearchDataBroker<InsurancePolicyStub> getSearchBroker();

	public void getClientPolicies(String clientid, ResponseHandler<Collection<InsurancePolicyStub>> policies);

	public void createSubPolicy(SubPolicy subPolicy, ResponseHandler<SubPolicy> handler);

	public void createReceipt(String policyId, Receipt receipt, ResponseHandler<Receipt> handler);

	void validatePolicy(String policyId, ResponseHandler<Void> handler);

	void executeDetailedCalculations(String policyId, ResponseHandler<InsurancePolicy> handler);

	void voidPolicy(PolicyVoiding voiding,	ResponseHandler<InsurancePolicy> responseHandler);

	void issueDebitNote(String policyId, DebitNote note, ResponseHandler<Void> handler);

	public void createNegotiation(Negotiation negotiation, ResponseHandler<Negotiation> handler);

	void transferToClient(String policyId, String newClientId,
			ResponseHandler<InsurancePolicy> handler);

	void getInsurancePoliciesWithNumber(String label,
			ResponseHandler<Collection<InsurancePolicyStub>> handler);

	void createManagerTransfer(String[] processIds, String newManagerId, ResponseHandler<Void> handler);

	void createExpense(Expense expense, ResponseHandler<Expense> handler);

	void getDeadClientPolicies(String clientid,
			ResponseHandler<Collection<InsurancePolicyStub>> policies);

	void createSubPolicyReceipts(DebitNoteBatch debitNote,
			ResponseHandler<Void> responseHandler);

	public void sendMessage(Conversation info,
			ResponseHandler<Conversation> responseHandler);

	public void receiveMessage(Conversation info,
			ResponseHandler<Conversation> responseHandler);

}
