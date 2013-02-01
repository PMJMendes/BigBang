package bigBang.library.client.userInterface;

import bigBang.definitions.shared.Conversation;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.StackPanel;

public class ConversationChildrenPanel extends View {

	public HistoryList historyList;

	public ConversationChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		historyList = new HistoryList();

		wrapper.add(historyList, "Histórico");
	}

	@Override
	protected void initializeView() {}

	public void setOwner(Conversation request){
		String requestId = request == null ? null : request.id;
		this.historyList.setOwner(requestId);
	}
}
