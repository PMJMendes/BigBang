package bigBang.library.client.userInterface;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.StackPanel;

public class ExternalInfoRequestChildrenPanel extends View {

	protected ExternalInfoRequest externalRequest;

	public HistoryList historyList;

	public ExternalInfoRequestChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		historyList = new HistoryList();

		wrapper.add(historyList, "Histórico");
	}

	@Override
	protected void initializeView() {}

	public void setOwner(ExternalInfoRequest request){
		this.externalRequest = request;
		String requestId = request == null ? null : request.id;
		this.historyList.setOwner(requestId);
	}
}
