package bigBang.module.receiptModule.client.userInterface.presenter;

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

public class ReceiptSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED
	}

	public interface Display {
		HasValue <Object> getOperationNavigationPanel();
		HasWidgets getOperationViewContainer();
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController controller;
	private ViewPresenterController overlayController;

	public ReceiptSectionViewPresenter(View view) {
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
		this.initializeController();
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

	private void bind() {
		this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {

			public void onValueChange(ValueChangeEvent<Object> event) {
				((ViewPresenter)event.getValue()).go(view.getOperationViewContainer());
			}
		});
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

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
		this.controller = new ViewPresenterController(view.getOperationViewContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}

			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("receipt")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					if(display.equalsIgnoreCase("history")){
						present("HISTORY", parameters);
					}else if(display.equalsIgnoreCase("serialreceiptcreation")){
						present("SERIAL_RECEIPT_CREATION", parameters);
					}
					else{
						present("RECEIPT_OPERATIONS", parameters);
					}
				}
				ReceiptSectionViewPresenter.this.overlayController.onParameters(parameters);
			}
		};
		this.overlayController = new ViewPresenterController(view.getOverlayViewContainer()){

			@Override
			public void onParameters(HasParameters parameters) {
				String show = parameters.getParameter("show");
				show = show == null ? new String() : show;

				if(show.isEmpty()){
					view.showOverlayViewContainer(false);

					//OVERLAY VIEWS
				}else if(show.equalsIgnoreCase("receipttransfertopolicy")){
					present("RECEIPT_INSURANCE_POLICY_TRANSFER", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("associatewithdebitnote")){
					present("RECEIPT_ASSOCIATE_DEBIT_NOTE", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("returnreceipt")){
					present("RECEIPT_RETURN", parameters);
					view.showOverlayViewContainer(true);
				}

			}

			@Override
			protected void onNavigationHistoryEvent(
					NavigationHistoryItem historyItem) {
				return;
			}

		};
	}

}
