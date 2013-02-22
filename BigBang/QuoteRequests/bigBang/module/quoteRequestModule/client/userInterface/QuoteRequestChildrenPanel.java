package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.ConversationList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.StackPanel;

public class QuoteRequestChildrenPanel extends View {

	protected QuoteRequest owner;
	
	public ContactsList contactsList;
	public DocumentsList documentsList;
	public SubProcessesList subProcessesList;
	public HistoryList historyList;
	public ConversationList conversationList;
	
	public QuoteRequestChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();
		conversationList = new ConversationList();
		
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(conversationList, "Trocas de Mensagens");
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
		
		boolean allow = owner != null ? PermissionChecker.hasPermission(owner, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST) : false;
		this.contactsList.setOwner(ownerId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.QUOTE_REQUEST);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(ownerId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.QUOTE_REQUEST);
		this.documentsList.allowCreation(allow);
		
		conversationList.setOwner(ownerId);
		contactsList.setOwner(ownerId);
		documentsList.setOwner(ownerId);
		subProcessesList.setOwner(ownerId);
		historyList.setOwner(ownerId);
	}
	
}
