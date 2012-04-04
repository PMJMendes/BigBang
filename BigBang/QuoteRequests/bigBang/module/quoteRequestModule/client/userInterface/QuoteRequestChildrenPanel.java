package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class QuoteRequestChildrenPanel extends View {

	protected QuoteRequest owner;
	
	public ContactsList contactsList;
	public DocumentsList documentsList;
	public QuoteRequestInsuredObjectsList insuredObjectsList;
	public SubProcessesList subProcessesList;
	public HistoryList historyList;
	
	public QuoteRequestChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		insuredObjectsList = new QuoteRequestInsuredObjectsList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();
		
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(insuredObjectsList, "Unidades de Risco");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	public void setOwner(QuoteRequest owner){
		this.owner = owner;
		String ownerId = owner == null ? null : owner.id;
		
		contactsList.setOwner(ownerId);
		documentsList.setOwner(ownerId);
		insuredObjectsList.setOwner(ownerId);
		subProcessesList.setOwner(ownerId);
		historyList.setOwner(ownerId);
	}
	
}
