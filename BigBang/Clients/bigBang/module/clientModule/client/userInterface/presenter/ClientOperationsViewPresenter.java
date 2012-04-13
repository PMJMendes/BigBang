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
				String display = parameters.peekInStackParameter("display");
				display = display == null ? new String() : display;

				if(display.equalsIgnoreCase("search")){
					present("CLIENT_SEARCH", parameters);
				}else if(display.equalsIgnoreCase("createpolicy")){
					present("CLIENT_CREATE_INSURANCE_POLICY", parameters);
				}else if(display.equalsIgnoreCase("merge")){
					present("CLIENT_MERGE", parameters);
				}else if(display.equalsIgnoreCase("managertransfer")){
					present("SINGLE_MANAGER_TRANSFER", parameters);
				}else if(display.equalsIgnoreCase("viewmanagertransfer")){
					present("MANAGER_TRANSFER", parameters);
				}else if(display.equalsIgnoreCase("inforequest")){
					present("CLIENT_INFO_OR_DOCUMENT_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("viewinforequest")){
					present("VIEW_CLIENT_INFO_OR_DOCUMENT_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("documentmanagement")){
					present("DOCUMENT", parameters);
				}else if(display.equalsIgnoreCase("contactmanagement")){
					present("CONTACT", parameters);
				}else if(display.equalsIgnoreCase("clienthistory")){
					present("HISTORY", parameters);
				}else if(display.equalsIgnoreCase("createquoterequest")){
					present("CLIENT_CREATE_QUOTE_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("viewinsuredobject")){
					present("QUOTE_REQUEST_INSURED_OBJECT", parameters);
				}else if(display.equalsIgnoreCase("createcasualty")){
					present("CLIENT_CREATE_CASUALTY", parameters);
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
		};
	}

}
