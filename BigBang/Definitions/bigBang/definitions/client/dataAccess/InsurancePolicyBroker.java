package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;

public interface InsurancePolicyBroker extends DataBrokerInterface<InsurancePolicy> {

	public boolean isTemp(String policyId);

	public void getPolicy(String policyId, ResponseHandler<InsurancePolicy> handler);

	public void openPolicyResource(String policyId, ResponseHandler<InsurancePolicy> handler);

	public void commitPolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);

	public void closePolicyResource(String policyId, ResponseHandler<Void> handler);

	public void openCoverageDetailsPage(String policyId, String insuredObjectId, String exerciseId, ResponseHandler<InsurancePolicy.TableSection> handler);

	public void saveCoverageDetailsPage(String policyId, String insuredObjectId, String exerciseId, InsurancePolicy.TableSection data, ResponseHandler<InsurancePolicy.TableSection> handler);

	public void updatePolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);

	public void initPolicy(InsurancePolicy policy, ResponseHandler<InsurancePolicy> handler);
	
	public void removePolicy(String policyId, ResponseHandler<String> handler);

	public void getClientPolicies(String clientid, ResponseHandler<Collection<InsurancePolicyStub>> policies);

	public void createReceipt(String policyId, Receipt receipt, ResponseHandler<Receipt> handler);

	public void remapItemId(String oldId, String newId, boolean inScratchPad);
	
	public SearchDataBroker<InsurancePolicyStub> getSearchBroker();

	public void discardTemp(String policyId);
	
	public void getPage(String policyId, String insuredObejctId, String exerciseId, ResponseHandler<TableSection> handler);

	void validatePolicy(String policyId, ResponseHandler<Void> handler);
	
	void executeDetailedCalculations(String policyId, ResponseHandler<InsurancePolicy> handler);
	
	void voidPolicy(PolicyVoiding voiding,	ResponseHandler<InsurancePolicy> responseHandler);
	
	void issueDebitNote(String policyId, DebitNote note, ResponseHandler<Void> handler);

	public String getEffectiveId(String ownerId);

	public String getFinalMapping(String ownerId);
	
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
