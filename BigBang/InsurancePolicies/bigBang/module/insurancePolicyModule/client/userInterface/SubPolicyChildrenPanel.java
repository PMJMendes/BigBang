package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
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

	public SubPolicyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		insuredObjectsList = new SubPolicyInsuredObjectsList();
		exercisesList = new SubPolicyExercisesList();
		receiptList = new ReceiptsList();
		historyList = new HistoryList();

		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(insuredObjectsList, "Unidades de Risco");
		wrapper.add(exercisesList, "Exercícios");
		wrapper.add(receiptList, "Recibos");
		wrapper.add(historyList, "Histórico");
	}

	@Override
	protected void initializeView() {}

	public void setSubPolicy(SubPolicy subPolicy){
		this.subPolicy = subPolicy;
		String subPolicyId = subPolicy == null ? null : subPolicy.id;
		this.contactsList.setOwner(subPolicyId);
		this.documentsList.setOwner(subPolicyId);
		this.insuredObjectsList.setOwner(subPolicyId);
		this.exercisesList.setOwner(subPolicyId);
		this.receiptList.setOwner(subPolicyId);
		this.historyList.setOwner(subPolicyId);
	}

}
