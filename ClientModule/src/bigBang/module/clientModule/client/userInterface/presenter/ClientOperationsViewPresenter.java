package bigBang.module.clientModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ClientOperationsViewPresenter implements ViewPresenter {

	public static interface Display{
		HasWidgets getContainer();
		Widget asWidget();
	}
	
	private Display view;
	private ViewPresenterController controller;
	
	public ClientOperationsViewPresenter(Display view){
		setView((UIObject) view);
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
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}

			@Override
			public void onParameters(HasParameters parameters) {
				String operation = parameters.getParameter("operation");
				operation = operation == null ? new String() : operation;

				if(operation.equalsIgnoreCase("search")){
					present("CLIENT_SEARCH", parameters);
				}else if(operation.equalsIgnoreCase("createpolicy")){
					present("CLIENT_CREATE_INSURANCE_POLICY", parameters);
				}else if(operation.equalsIgnoreCase("merge")){
					present("CLIENT_MERGE", parameters);
				}else if(operation.equalsIgnoreCase("managertransfer")){
					present("SINGLE_MANAGER_TRANSFER", parameters);
				}else if(operation.equalsIgnoreCase("inforequest")){
					present("INFO_OR_DOCUMENT_REQUEST", parameters);
				}else if(operation.equalsIgnoreCase("clienthistory")){
					present("HISTORY", parameters);
				}else{
					goToDefault();
				}
			}

			private void goToDefault(){
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setParameter("operation", "search");
				navigationManager.go(item);
			}
		};
	}

}
