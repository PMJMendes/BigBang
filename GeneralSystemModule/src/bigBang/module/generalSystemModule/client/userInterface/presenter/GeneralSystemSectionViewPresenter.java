package bigBang.module.generalSystemModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class GeneralSystemSectionViewPresenter implements ViewPresenter {

	public interface Display {
		HasValue <Object> getOperationNavigationPanel();
		HasWidgets getOperationViewContainer();
//		void selectOperation(OperationViewPresenter p);
//		
//		void createOperationNavigationItem(OperationViewPresenter operationPresenter, boolean enabled);
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController controller;
	
	public GeneralSystemSectionViewPresenter(Display view) {
		this.setView((UIObject)view);
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
		initializeController();
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
	}
	
	private void bind() {
		this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {
			
			public void onValueChange(ValueChangeEvent<Object> event) {
				((ViewPresenter)event.getValue()).go(view.getOperationViewContainer());
			}
		});
	}

	private void initializeController(){
		this.controller = new ViewPresenterController(view.getOperationViewContainer()) {
			
			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("generalsystem")){
					String operation = parameters.getParameter("operation");
					operation = operation == null ? "" : operation;

					if(operation.equalsIgnoreCase("history")){
						present("HISTORY", parameters);
					}else if(operation.equalsIgnoreCase("user")){
						present("GENERAL_SYSTEM_USER_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("costcenter")){
						present("GENERAL_SYSTEM_COST_CENTER_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("clientgroup")){
						present("GENERAL_SYSTEM_CLIENT_GROUP_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("coverage")){
						present("GENERAL_SYSTEM_COVERAGE_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("insuranceagency")){
						present("GENERAL_SYSTEM_INSURANCE_AGENCY_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("mediator")){
						present("GENERAL_SYSTEM_MEDIATOR_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("tax")){
						present("GENERAL_SYSTEM_TAX_MANAGEMENT", parameters);
					}else if(operation.equalsIgnoreCase("user")){
						present("GENERAL_SYSTEM_USER_MANAGEMENT", parameters);
					}else{
						goToDefault();
					}
				}
			}
			
			private void goToDefault(){
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setParameter("operation", "history");
				navigationManager.go(item);
			}
			
			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}
	
}
