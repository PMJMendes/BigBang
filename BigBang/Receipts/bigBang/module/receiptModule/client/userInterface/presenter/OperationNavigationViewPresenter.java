package bigBang.module.receiptModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class OperationNavigationViewPresenter implements ViewPresenter {

	public static interface Display{
		HasWidgets getOperationViewContainer();
		void navigateInto();
		void navigateBackInto();
		Widget asWidget();
	}

	private Display view;

	public OperationNavigationViewPresenter(Display view){
		setView((UIObject) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this.view.asWidget());
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String operation = parameterHolder.getParameter("operation");
		operation = operation == null ? new String() : operation;

		if(operation.equalsIgnoreCase("somethingsomething")){ //TODO

		}else{
			//present("RECEIPT_SEARCH", parameterHolder.getParameters(new String[]{"id"}));
		}
	}

	private void initializeController(){
		new ViewPresenterController(view.getOperationViewContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				onParameters(historyItem);
			}
			
			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				section = section == null ? new String() : section;
				if(section.equalsIgnoreCase("receipt")){
					String operation = parameters.getParameter("operation");
					
					if(operation.equalsIgnoreCase("search")){
						present("RECEIPT_SEARCH", parameters);
					}else if(operation.equalsIgnoreCase("showhistory")){
						present("HISTORY", parameters);
					}else{
						goToDefault();
					}
				}
			}

			private void goToDefault(){
				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "receipt");
				item.setParameter("operation", "search");
				navigationManager.go(item);
			}
		};
	}

}
