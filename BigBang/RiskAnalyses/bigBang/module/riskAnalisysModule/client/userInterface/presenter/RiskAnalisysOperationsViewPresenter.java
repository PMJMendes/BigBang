package bigBang.module.riskAnalisysModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class RiskAnalisysOperationsViewPresenter implements ViewPresenter {

	public static interface Display {
		HasWidgets getContainer();
		Widget asWidget();
	}
	
	private Display view;
	private ViewPresenterController controller;
	
	public RiskAnalisysOperationsViewPresenter(Display view) {
		setView((UIObject)view);
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
	}
	
	private void initializeController(){
		this.controller = new ViewPresenterController(view.getContainer()) {
			
			@Override
			public void onParameters(HasParameters parameters) {
				String display = parameters.peekInStackParameter("display");
				display = display == null ? new String() : display;

				if(display.equalsIgnoreCase("search")){
					present("RISK_ANALISYS_SEARCH", parameters);
				}else if(display.equalsIgnoreCase("riskanalisyshistory")){
					present("HISTORY", parameters);
				}else{
					goToDefault();
				}
			}

			private void goToDefault(){
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "search");
				navigationManager.go(item);
			}
			
			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

}
