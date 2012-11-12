package bigBang.module.mainModule.client.userInterface;

import com.google.gwt.user.client.ui.HasWidgets;

import Jewel.Web.client.Jewel_Web;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.module.mainModule.client.userInterface.presenter.MainScreenViewPresenter;

public class MainViewController extends ViewPresenterController {

	private MainScreenViewPresenter mainViewPresenter;
	
	public MainViewController(HasWidgets container) {
		super(container);
	}

	@Override
	protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
		initializeMainViewPresenter();

		String section = historyItem.getParameter("section");
		
		if(section == null || section.isEmpty()){
			goToDefault();
			return;
		}

		if(section.equalsIgnoreCase("personal")){
			mainViewPresenter.setParameters(historyItem);
			present("TASKS_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("generalsystem")){
			mainViewPresenter.setParameters(historyItem);
			present("GENERAL_SYSTEM_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("client")){
			mainViewPresenter.setParameters(historyItem);
			present("CLIENT_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("insurancepolicy")){
			mainViewPresenter.setParameters(historyItem);
			present("INSURANCE_POLICY_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("receipt")){
			mainViewPresenter.setParameters(historyItem);
			present("RECEIPT_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("casualty")){
			mainViewPresenter.setParameters(historyItem);
			present("CASUALTY_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("quoterequest")){
			mainViewPresenter.setParameters(historyItem);
			present("QUOTE_REQUEST_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("complaint")){
			mainViewPresenter.setParameters(historyItem);
			present("COMPLAINT_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("expense")){
			mainViewPresenter.setParameters(historyItem);
			present("EXPENSE_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("riskanalisys")){
			mainViewPresenter.setParameters(historyItem);
			present("RISK_ANALISYS_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("tests")){
			mainViewPresenter.setParameters(historyItem);
			present("TESTS_SECTION", historyItem, true);
		}else if(section.equalsIgnoreCase("backoffice")){
			new Jewel_Web().onModuleLoad();
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
		item.setParameter("section", "personal");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void initializeMainViewPresenter(){
		if(this.mainViewPresenter == null){
			mainViewPresenter = (MainScreenViewPresenter) ViewPresenterFactory.getInstance().getViewPresenter("MAIN_SCREEN");
			mainViewPresenter.go(container);
			setContainer(mainViewPresenter.getContainer());
		}
	}

}

