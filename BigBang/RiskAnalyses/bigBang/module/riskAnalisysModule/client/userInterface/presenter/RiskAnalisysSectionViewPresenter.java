package bigBang.module.riskAnalisysModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class RiskAnalisysSectionViewPresenter implements ViewPresenter {

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
	
	public RiskAnalisysSectionViewPresenter(View view) {
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
	
	public void bind() {
		this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {
			
			public void onValueChange(ValueChangeEvent<Object> event) {
				((ViewPresenter)event.getValue()).go(view.getOperationViewContainer());
			}
		});
		
		//APPLICATION-WIDE EVENTS
	}
	
	private void initializeController(){
		this.controller = new ViewPresenterController(view.getOperationViewContainer()) {
			
			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("riskanalisys")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					//MASS OPERATIONS
					if(display.equalsIgnoreCase("history")){
						present("HISTORY", parameters, true);
					}else if(display.equalsIgnoreCase("massmanagertransfer")){
						present("MANAGER_TRANSFER", parameters);
					}else{
						present("RISK_ANALISYS_OPERATIONS", parameters);
					}
				}
			}
			
			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}
	
}
