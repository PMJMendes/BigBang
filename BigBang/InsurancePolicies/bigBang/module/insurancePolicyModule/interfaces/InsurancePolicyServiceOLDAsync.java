package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicyOLD;
import bigBang.definitions.shared.InsurancePolicyOLD.TableSection;
import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Remap;
import bigBang.library.interfaces.DependentItemSubServiceAsync;
import bigBang.library.interfaces.ExactItemSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsurancePolicyServiceOLDAsync
	extends SearchServiceAsync, DependentItemSubServiceAsync, ExactItemSubServiceAsync
{
	void getPolicy(String policyId, AsyncCallback<InsurancePolicyOLD> callback);
	void getPage(String policyId, String insuredObjectId, String exerciseId, AsyncCallback<TableSection> callback);
	void openPolicyScratchPad(String policyId, AsyncCallback<Remap[]> callback);
	void initPolicyInPad(InsurancePolicyOLD policy, AsyncCallback<InsurancePolicyOLD> callback);
	void getPolicyInPad(String policyId, AsyncCallback<InsurancePolicyOLD> callback);
	void updateHeader(InsurancePolicyOLD policy, AsyncCallback<InsurancePolicyOLD> callback);
	void getPageForEdit(String policyId, String objectId, String exerciseId, AsyncCallback<TableSection> callback);
	void savePage(TableSection data, AsyncCallback<TableSection> callback);
	void getObjectInPad(String objectId, AsyncCallback<InsuredObjectOLD> callback);
	void createObjectInPad(String policyId, AsyncCallback<InsuredObjectOLD> callback);
	void createObjectFromClientInPad(String policyId, AsyncCallback<InsuredObjectOLD> callback);
	void updateObjectInPad(InsuredObjectOLD data, AsyncCallback<InsuredObjectOLD> callback);
	void deleteObjectInPad(String objectId, AsyncCallback<Void> callback);
	void getExerciseInPad(String exerciseId, AsyncCallback<Exercise> callback);
	void createFirstExercise(String policyId, AsyncCallback<Exercise> callback);
	void updateExerciseInPad(Exercise data, AsyncCallback<Exercise> callback);
	void deleteExerciseInPad(String exerciseId, AsyncCallback<Void> callback);
	void commitPad(String policyId, AsyncCallback<Remap[]> callback);
	void discardPad(String policyId, AsyncCallback<Remap[]> callback);
	void performCalculations(String policyId, AsyncCallback<InsurancePolicyOLD> callback);
	void validatePolicy(String policyId, AsyncCallback<Void> callback);
	void includeObject(String policyId, InsuredObjectOLD object, AsyncCallback<InsuredObjectOLD> callback);
	void includeObjectFromClient(String policyId, AsyncCallback<InsuredObjectOLD> callback);
	void excludeObject(String policyId, String objectId, AsyncCallback<Void> callback);
	void openNewExercise(String policyId, Exercise exercise, AsyncCallback<Exercise> callback);
	void transferToClient(String policyId, String newClientId, AsyncCallback<InsurancePolicyOLD> callback);
	void createDebitNote(String policyId, DebitNote note, AsyncCallback<Void> callback);
	void createInfoOrDocumentRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void createReceipt(String policyId, Receipt receipt, AsyncCallback<Receipt> callback);
	void createExpense(Expense expense, AsyncCallback<Expense> callback);
	void createNegotiation(Negotiation negotiation, AsyncCallback<Negotiation> callback);
	void voidPolicy(PolicyVoiding voiding, AsyncCallback<InsurancePolicyOLD> callback);
	void deletePolicy(String policyId, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
