package bigBang.module.receiptModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptSectionViewPresenter implements ViewPresenter {

	public interface Display {
		HasWidgets getContainer();
		Widget asWidget();
	}

	private Display view;

	public ReceiptSectionViewPresenter(View view) {
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
		this.initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		return;
	}

	private void bind() {
//		this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {
//
//			public void onValueChange(ValueChangeEvent<Object> event) {
//				((ViewPresenter)event.getValue()).go(view.getOperationViewContainer());
//			}
//		});

		//APPLICATION-WIDE EVENTS
	}

	private void initializeController(){
		new ViewPresenterController(view.getContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				onParameters(historyItem);
			}
			
			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("receipt")){
					String operation = parameters.getParameter("operation");
					operation = operation == null ? "" : operation;

					//MASS OPERATIONS
					if(operation.equalsIgnoreCase("asddg")){
//						//SELECT IN TOOLBAR
//						present("RECEIPT_MASS_OPERATION", historyItem, true);
					}else{
						present("RECEIPT_SEARCH", parameters);
					}
				}
			}
		};
	}

}
