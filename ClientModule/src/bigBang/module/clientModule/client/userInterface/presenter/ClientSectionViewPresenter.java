package bigBang.module.clientModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ClientSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED
	}
	
	public static interface Display {
		HasValue <Object> getOperationNavigationPanel();
		HasWidgets getOperationViewContainer();
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		
		//void selectOperation(OperationViewPresenter p);

		//void createOperationNavigationItem(OperationViewPresenter operationPresenter, boolean enabled);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController controller;
	private ViewPresenterController overlayController;

	public ClientSectionViewPresenter(View view) {
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
		initializeController();
	}

	@Override
	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
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
		this.view.registerActionHandler(new ActionInvokedEventHandler<ClientSectionViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case ON_OVERLAY_CLOSED:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
					break;
				}
			}
		});
	}

	private void initializeController(){
		this.controller = new ViewPresenterController(this.view.getOperationViewContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
			
			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("client")){
					String operation = parameters.getParameter("operation");
					operation = operation == null ? "" : operation;
					
					//MASS OPERATIONS
					if(operation.equalsIgnoreCase("history")){
						present("HISTORY", parameters);
					}else if(operation.equalsIgnoreCase("massmanagertransfer")){
						present("MANAGER_TRANSFER", parameters);
					}else{
						present("CLIENT_OPERATIONS", parameters);
					}
				}
				ClientSectionViewPresenter.this.overlayController.onParameters(parameters);
			}
		};
		this.overlayController = new ViewPresenterController(view.getOverlayViewContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				String show = parameters.getParameter("show");
				show = show == null ? new String() : show;

				if(show.isEmpty()){
					view.showOverlayViewContainer(false);
					
				//OVERLAY VIEWS
				}else if(show.equalsIgnoreCase("managertransfer")){
					present("CLIENT_SINGLE_MANAGER_TRANSFER", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("contactmanagement")){
					present("CONTACT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("documentmanagement")){
					present("DOCUMENT", parameters);
					view.showOverlayViewContainer(true);
				}
			}
			
			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

}
