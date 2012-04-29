package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.StackPanel;

public class SubPolicyChildrenPanel extends View {

	protected SubPolicy subPolicy;

	public ContactsList contactsList;
	public DocumentsList documentsList;
	public SubPolicyInsuredObjectsList insuredObjectsList;
	public SubPolicyExercisesList exercisesList;
	public ReceiptsList receiptList;
	public HistoryList historyList;
	public SubProcessesList subProcessesList;

	public SubPolicyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		insuredObjectsList = new SubPolicyInsuredObjectsList();
		exercisesList = new SubPolicyExercisesList();
		receiptList = new ReceiptsList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();

		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(insuredObjectsList, "Unidades de Risco");
		wrapper.add(exercisesList, "Exercícios");
		wrapper.add(receiptList, "Recibos");
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
		this.insuredObjectsList.setOwner(subPolicyId);
		this.exercisesList.setOwner(subPolicyId);
		this.receiptList.setOwner(subPolicyId);
		this.subProcessesList.setOwner(subPolicyId);
		this.historyList.setOwner(subPolicyId);
	}

}
