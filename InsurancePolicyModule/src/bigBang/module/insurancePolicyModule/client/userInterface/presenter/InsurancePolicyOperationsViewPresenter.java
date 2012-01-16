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
				String operation = parameters.getParameter("operation");
				operation = operation == null ? new String() : operation;

				if(operation.equalsIgnoreCase("search")){
					present("INSURANCE_POLICY_SEARCH", parameters);
				}else if(operation.equalsIgnoreCase("createreceipt")){
					present("INSURANCE_POLICY_CREATE_RECEIPT", parameters);
				}else if(operation.equalsIgnoreCase("viewinsuredobject")){
					present("INSURANCE_POLICY_INSURED_OBJECT", parameters);
				}else if(operation.equalsIgnoreCase("viewexercise")){
					present("INSURANCE_POLICY_EXERCISE", parameters);
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
