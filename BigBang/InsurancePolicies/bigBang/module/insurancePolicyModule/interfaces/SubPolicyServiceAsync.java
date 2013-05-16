package bigBang.module.insurancePolicyModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.PolicyVoiding;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.interfaces.DependentItemSubServiceAsync;
import bigBang.library.interfaces.ExactItemSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubPolicyServiceAsync
	extends SearchServiceAsync, ExactItemSubServiceAsync, DependentItemSubServiceAsync
{
	void getEmptySubPolicy(String policyId, AsyncCallback<SubPolicy> callback);
	void getSubPolicy(String subPolicyId, AsyncCallback<SubPolicy> callback);
	void editSubPolicy(SubPolicy subPolicy, AsyncCallback<SubPolicy> callback);
	void performCalculations(String subPolicyId, AsyncCallback<SubPolicy> callback);
	void validateSubPolicy(String subPolicyId, AsyncCallback<Void> callback);
	void includeObject(String subPolicyId, InsuredObject object, AsyncCallback<InsuredObject> callback);
	void includeObjectFromClient(String subPolicyId, AsyncCallback<InsuredObject> callback);
	void excludeObject(String subPolicyId, String objectId, AsyncCallback<Void> callback);
	void transferToPolicy(String subPolicyId, String newPolicyId, AsyncCallback<SubPolicy> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void createReceipt(String subPolicyId, Receipt receipt, AsyncCallback<Receipt> callback);
	void createExpense(Expense expense, AsyncCallback<Expense> callback);
	void voidSubPolicy(PolicyVoiding voiding, AsyncCallback<SubPolicy> callback);
	void deleteSubPolicy(String subPolicyId, String reason, AsyncCallback<Void> callback);
}
