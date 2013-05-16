package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.ConversationList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.StackPanel;

public class SubPolicyChildrenPanel extends View {

	protected SubPolicy subPolicy;

	public ContactsList contactsList;
	public DocumentsList documentsList;
	public ReceiptsList receiptList;
	public HistoryList historyList;
	public ConversationList conversationList;
	public SubProcessesList subProcessesList;
	public ExpensesList expensesList;

	public SubPolicyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		receiptList = new ReceiptsList();
		subProcessesList = new SubProcessesList();
		conversationList = new ConversationList();
		expensesList = new ExpensesList();
		historyList = new HistoryList();

		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(receiptList, "Recibos");
		wrapper.add(expensesList, "Despesas de Saúde");
		wrapper.add(conversationList, "Trocas de Mensagens");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");
	}

	@Override
	protected void initializeView() {}

	public void setSubPolicy(SubPolicy subPolicy){
		this.subPolicy = subPolicy;
		String subPolicyId = subPolicy == null ? null : subPolicy.id;

		boolean allow = subPolicy != null ? PermissionChecker.hasPermission(subPolicy, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EDIT_SUB_POLICY) : false;
		this.contactsList.setOwner(subPolicyId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(subPolicyId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.documentsList.allowCreation(allow);
		this.receiptList.setOwner(subPolicyId);
		this.expensesList.setOwner(subPolicyId);
		this.subProcessesList.setOwner(subPolicyId);
		this.conversationList.setOwner(subPolicyId);
		this.historyList.setOwner(subPolicyId);
	}

}
