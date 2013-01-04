package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class AssessmentChildrenPanel extends View{

	public SubProcessesList subProcessesList;
	public HistoryList historyList;

	public AssessmentChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();
		
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
	}

}
