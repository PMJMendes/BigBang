package bigBang.module.expenseModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ExpenseOperationsViewPresenter implements ViewPresenter {

	public static interface Display{
		HasWidgets getContainer();
		Widget asWidget();
	}
	
	private Display view;
	private ViewPresenterController controller;
	
	public ExpenseOperationsViewPresenter(Display view){
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
				String operation = parameters.getParameter("operation");
				operation = operation == null ? new String() : operation;

				if(operation.equalsIgnoreCase("search")){
					present("EXPENSE_SEARCH", parameters);
				}else{
					goToDefault();
				}
			}

			private void goToDefault(){
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setParameter("operation", "search");
				navigationManager.go(item);
			}
			
			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

}
