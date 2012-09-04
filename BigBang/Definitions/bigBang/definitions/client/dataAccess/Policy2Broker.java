package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;

public interface Policy2Broker extends DataBrokerInterface<InsurancePolicy> {

	public boolean isTemp(String policyId);

	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);

	public void updatePolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);

	public void removePolicy(String policyId, ResponseHandler<String> handler);

	public void getClientPolicies(String clientid, ResponseHandler<Collection<InsurancePolicy>> policies);

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
			ResponseHandler<Collection<InsurancePolicy>> handler);

	void createManagerTransfer(String[] processIds, String newManagerId, ResponseHandler<Void> handler);

	void createExpense(Expense expense, ResponseHandler<Expense> handler);

	public void createCompanyInfoRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);
	
	public void createClientInfoRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);

}
