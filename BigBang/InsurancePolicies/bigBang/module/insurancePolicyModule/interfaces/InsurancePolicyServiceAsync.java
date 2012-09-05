package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.library.interfaces.ExactItemSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsurancePolicyServiceAsync
	extends SearchServiceAsync, ExactItemSubServiceAsync
{
	void getEmptyPolicy(String subLineId, String clientId, AsyncCallback<InsurancePolicy> callback);
	void getPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);
	void getEmptyObject(String policyId, AsyncCallback<InsuredObject> callback);
	void getPolicyObject(String objectId, AsyncCallback<InsuredObject> callback);
	void editPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
	void performCalculations(String policyId, AsyncCallback<InsurancePolicy> callback);
	void validatePolicy(String policyId, AsyncCallback<Void> callback);
	void includeObject(String policyId, InsuredObjectOLD object, AsyncCallback<InsuredObjectOLD> callback);
	void includeObjectFromClient(String policyId, AsyncCallback<InsuredObjectOLD> callback);
	void excludeObject(String policyId, String objectId, AsyncCallback<Void> callback);
	void openNewExercise(String policyId, Exercise exercise, AsyncCallback<Exercise> callback);
	void transferToClient(String policyId, String newClientId, AsyncCallback<InsurancePolicy> callback);
	void createDebitNote(String policyId, DebitNote note, AsyncCallback<Void> callback);
	void createInfoOrDocumentRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void createReceipt(String policyId, Receipt receipt, AsyncCallback<Receipt> callback);
	void createExpense(Expense expense, AsyncCallback<Expense> callback);
	void createNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void voidPolicy(PolicyVoiding voiding, AsyncCallback<InsurancePolicy> callback);
	void deletePolicy(String policyId, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
