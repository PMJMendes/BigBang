package bigBang.library.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.userInterface.view.View;

public class InfoOrDocumentRequestChildrenPanel extends View {

	protected InfoOrDocumentRequest request;

	public HistoryList historyList;

	public InfoOrDocumentRequestChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		historyList = new HistoryList();
		wrapper.add(this.historyList, "Histórico");
	}

	@Override
	protected void initializeView() {}

	public void setOwner(InfoOrDocumentRequest owner){
		clear();
		this.request = owner;
		this.historyList.setOwner(owner == null ? null : owner.id);
	}

	public void clear(){
		this.historyList.clear();
	}

	public InfoOrDocumentRequest getOwner(){
		return this.request;
	}

}
