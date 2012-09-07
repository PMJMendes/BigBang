package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;

public interface InsurancePolicyBroker extends DataBrokerInterface<InsurancePolicy> {

	//GET & SET
	
	public void persistPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);
	
	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);

	public void updatePolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void discardEditData();
	
	public void removePolicy(String policyId, ResponseHandler<String> handler);

	public void getInsuredObject(String policyId, String objectId, ResponseHandler<InsuredObject> handler);

	public InsuredObject addInsuredObject(String policyId);

	public void removeInsuredObject(String policyId, String objectId);

	public void updateInsuredObject(String policyId, InsuredObject object);
	
	public ExerciseData addExercise(String policyId, ExerciseData exercise);
	
	public ExerciseData updateExercise(String policyId, ExerciseData exercise);
	
	public FieldContainer getFieldsContainerForPolicy(String policyId, String ExerciseId);
	
	public FieldContainer getFieldsContainerForInsuredObject(String objectId, String exerciseId);


	// OTHER OPS

	public void getClientPolicies(String clientid, ResponseHandler<Collection<InsurancePolicyStub>> policies);

	public void createReceipt(String policyId, Receipt receipt, ResponseHandler<Receipt> handler);

	public SearchDataBroker<InsurancePolicyStub> getSearchBroker();

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

	public void createCompanyInfoRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);
	
	public void createClientInfoRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);

}
