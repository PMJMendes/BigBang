package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InsurancePolicySectionViewPresenter implements ViewPresenter{

	public interface Display {
		HasWidgets getContainer();
		//		HasWidgets getOperationViewContainer();
		//		void selectOperation(OperationViewPresenter p);
		//		
		//		void createOperationNavigationItem(OperationViewPresenter operationPresenter, boolean enabled);
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController controller;

	public InsurancePolicySectionViewPresenter(View view) {
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
	}

	public void bind() {
		//		this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {
		//			
		//			public void onValueChange(ValueChangeEvent<Object> event) {
		//				((ViewPresenter)event.getValue()).go(view.getOperationViewContainer());
		//			}
		//		});

		//APPLICATION-WIDE EVENTS
	}

	private void initializeController(){
		this.controller = new ViewPresenterController(view.getContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}

			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("insurancepolicy")){
					String operation = parameters.getParameter("operation");
					operation = operation == null ? "" : operation;

					//MASS OPERATIONS
					if(operation.equalsIgnoreCase("history")){
						present("HISTORY", parameters);
					}else {
						present("INSURANCE_POLICY_OPERATIONS", parameters);
					}
				}
			}
		};
	}

}
