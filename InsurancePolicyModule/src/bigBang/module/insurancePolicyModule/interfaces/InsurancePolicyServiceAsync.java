package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsurancePolicyServiceAsync
	extends SearchServiceAsync
{
	void getPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);
	void getPage(String policyId, String insuredObjectId, String exerciseId, AsyncCallback<TableSection> callback);
	void initializeNewPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
	void openForEdit(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
	void updateHeader(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
	void getPageForEdit(String scratchPadId, String tempObjectId, String tempExerciseId, AsyncCallback<TableSection> callback);
	void savePage(InsurancePolicy.TableSection data, AsyncCallback<TableSection> callback);
	void getPadItemsFilter(String listId, String scratchPadId, AsyncCallback<TipifiedListItem[]> callback);
	void getObjectInPad(String tempObjectId, AsyncCallback<InsuredObject> callback);
	void createObjectInPad(String scratchPadId, AsyncCallback<InsuredObject> callback);
	void createObjectFromClientInPad(String scratchPadId, AsyncCallback<InsuredObject> callback);
	void updateObjectInPad(InsuredObject data, AsyncCallback<InsuredObject> callback);
	void deleteObjectInPad(String tempObjectId, AsyncCallback<Void> callback);
	void getExerciseInPad(String tempExerciseId, AsyncCallback<Exercise> callback);
	void createFirstExercise(String scratchPadId, AsyncCallback<Exercise> callback);
	void updateExerciseInPad(Exercise data, AsyncCallback<Exercise> callback);
	void deleteExerciseInPad(String tempExerciseId, AsyncCallback<Void> callback);
	void commitPolicy(String scratchPadId, AsyncCallback<InsurancePolicy> callback);
	void discardPolicy(String scratchPadId, AsyncCallback<InsurancePolicy> callback);
	void includeObject(String policyId, InsuredObject object, AsyncCallback<InsuredObject> callback);
	void includeObjectFromClient(String policyId, AsyncCallback<InsuredObject> callback);
	void editObject(String policyId, InsuredObject object, AsyncCallback<InsuredObject> callback);
	void excludeObject(String policyId, String objectId, AsyncCallback<Void> callback);
	void openNewExercise(String policyId, Exercise exercise, AsyncCallback<Exercise> callback);
	void editExercise(String policyId, Exercise exercise, AsyncCallback<Exercise> callback);
	void editPolicy(InsurancePolicy policy, AsyncCallback<InsurancePolicy> callback);
	void voidPolicy(String policyId, AsyncCallback<InsurancePolicy> callback);
	void createInfoOrDocumentRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void createReceipt(String policyId, Receipt receipt, AsyncCallback<Receipt> callback);
	void deletePolicy(String policyId, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
