package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

//import bigBang.library.client.userInterface.ConversationList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class HistoryAndConversationAndSubProcessChildrenPanel extends View{

	//public ConversationList conversationList;
	public SubProcessesList subProcessesList;
	public HistoryList historyList;

	public HistoryAndConversationAndSubProcessChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();
	//	conversationList = new ConversationList();
		
		//wrapper.add(conversationList, "Trocas de Mensagens");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");

		
	}

	@Override
	protected void initializeView() {
		return;		
	}
	
	public void setOwner(String requestId){
		this.subProcessesList.setOwner(requestId);
		this.historyList.setOwner(requestId);
	//	conversationList.setOwner(requestId);
	}

}
