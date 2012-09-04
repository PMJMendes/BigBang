package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.Policy2;
import bigBang.definitions.shared.Policy2Stub;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;

public interface Policy2Broker extends DataBrokerInterface<Policy2> {

	public boolean isTemp(String policyId);

	public void getPolicy(String policyId, ResponseHandler<Policy2> handler);

	public void updatePolicy(Policy2 policy, ResponseHandler<Policy2> handler);

	public void removePolicy(String policyId, ResponseHandler<String> handler);

	public void getClientPolicies(String clientid, ResponseHandler<Collection<Policy2>> policies);

	public void createReceipt(String policyId, Receipt receipt, ResponseHandler<Receipt> handler);

	public SearchDataBroker<Policy2Stub> getSearchBroker();

	void validatePolicy(String policyId, ResponseHandler<Void> handler);
	
	void executeDetailedCalculations(String policyId, ResponseHandler<Policy2> handler);
	
	void voidPolicy(PolicyVoiding voiding,	ResponseHandler<Policy2> responseHandler);
	
	void issueDebitNote(String policyId, DebitNote note, ResponseHandler<Void> handler);

	public void createNegotiation(Negotiation negotiation, ResponseHandler<Negotiation> handler);

	void transferToClient(String policyId, String newClientId,
			ResponseHandler<Policy2> handler);

	void getInsurancePoliciesWithNumber(String label,
			ResponseHandler<Collection<Policy2>> handler);

	void createManagerTransfer(String[] processIds, String newManagerId, ResponseHandler<Void> handler);

	void createExpense(Expense expense, ResponseHandler<Expense> handler);

	public void createCompanyInfoRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);
	
	public void createClientInfoRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);

}
