package bigBang.module.mainModule.client.userInterface;

import com.google.gwt.user.client.ui.HasWidgets;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

public class MainViewController extends ViewPresenterController {

	public MainViewController(HasWidgets container) {
		super(container);
	}

	@Override
	protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
		String section = historyItem.getParameter("section");

		if(section == null || section.isEmpty()){
			goToDefault();
			return;
		}

		if(section.equalsIgnoreCase("tasks")){
			present("TASKS_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("generalsystem")){
			present("GENERAL_SYSTEM_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("client")){
			present("CLIENT_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("insurancepolicy")){
			present("INSURANCE_POLICY_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("receipt")){
			present("RECEIPT_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("casualty")){
			present("CASUALTY_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("quoterequest")){
			present("QUOTE_REQUEST_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("complaint")){
			present("COMPLAINT_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("expense")){
			present("EXPENSE_SECTION", historyItem);
		}else if(section.equalsIgnoreCase("riskanalisys")){
			present("RISK_ANALISYS_SECTION", historyItem);
		}else{
			goToDefault();
		}
		//TODO IMPORTANT FJVC
	}

	@Override
	public void onParameters(HasParameters parameters) {
		return;
	}

	private void goToDefault(){
		NavigationHistoryItem item = navigationManager.getCurrentState();
		item.setParameter("section", "tasks");
		NavigationHistoryManager.getInstance().go(item);
	}

}
