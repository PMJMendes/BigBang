package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicyOperationsViewPresenter implements ViewPresenter {

	public static interface Display{
		HasWidgets getContainer();
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController controller;

	public InsurancePolicyOperationsViewPresenter(Display view){
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
					present("INSURANCE_POLICY_SEARCH", parameters);
				}else if(display.equalsIgnoreCase("history")){
					present("history", parameters);
				}else if(display.equalsIgnoreCase("viewmanagertransfer")){
					present("MANAGER_TRANSFER", parameters);
				}else if(display.equalsIgnoreCase("createreceipt")){
					present("INSURANCE_POLICY_CREATE_RECEIPT", parameters);
				}else if(display.equalsIgnoreCase("viewinsuredobject")){
					present("INSURANCE_POLICY_INSURED_OBJECT", parameters);
				}else if(display.equalsIgnoreCase("viewexercise")){
					present("INSURANCE_POLICY_EXERCISE", parameters);
				}else if(display.equalsIgnoreCase("subpolicy")){
					present("INSURANCE_POLICY_SUB_POLICY", parameters);
				}else if(display.equalsIgnoreCase("negotiation")){
					present("INSURANCE_POLICY_NEGOTIATION", parameters);
				}else if(display.equalsIgnoreCase("viewsubpolicyexercise")){
					present("INSURANCE_POLICY_SUB_POLICY_EXERCISE", parameters);
				}else if(display.equalsIgnoreCase("viewsubpolicyobject")){
					present("INSURANCE_POLICY_SUB_POLICY_INSURED_OBJECT", parameters);
				}else if(display.equalsIgnoreCase("externalrequest")){
					present("INSURANCE_POLICY_NEGOTIATION_EXTERNAL_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("subpolicyinforequest")){
					present("INSURANCE_POLICY_SUB_POLICY_INFO_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("subpolicytransfertopolicy")){
					present("INSURANCE_POLICY_SUB_POLICY_TRANSFER_TO_POLICY", parameters);
				}else if(display.equalsIgnoreCase("subpolicycreatereceipt")){
					present("INSURANCE_POLICY_SUB_POLICY_CREATE_RECEIPT", parameters);
				}else if(display.equalsIgnoreCase("viewnegotiationexternalrequest")){
					present("INSURANCE_POLICY_NEGOTIATION_VIEW_EXTERNAL_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("createexpense")){
					present("INSURANCE_POLICY_CREATE_EXPENSE", parameters);
				}else if(display.equalsIgnoreCase("createexpensesubpolicy")){
					present("INSURANCE_POLICY_CREATE_EXPENSE_SUB_POLICY", parameters);
				}else if(display.equalsIgnoreCase("companyinforequest")){
					present("INSURANCE_POLICY_CREATE_COMPANY_INFO_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("clientinforequest")){
					present("INSURANCE_POLICY_CREATE_CLIENT_INFO_REQUEST", parameters);
				}else if(display.equalsIgnoreCase("viewinforequest")){
					present("VIEW_INSURANCE_POLICY_INFO_REQUEST", parameters);
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
