package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED,
	}

	public static enum SectionOperation {
		OPERATIONS,
	}

	public static interface Display {
		HasWidgets getOperationViewContainer();
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		void selectOperation(SectionOperation operation);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void registerOperationSelectionHandler(ActionInvokedEventHandler<SectionOperation> handler);
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController overlayController;
	private ViewPresenterController controller;

	public QuoteRequestSectionViewPresenter(View view) {
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
		initializeController();
	}

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
		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<QuoteRequestSectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "quoterequest");
				item.setStackParameter("display");

				if(action.getAction() == null) {
					item.pushIntoStackParameter("display", "search");
				}else{
					switch(action.getAction()) {
					case OPERATIONS:
						item.pushIntoStackParameter("display", "search");
						break;
					}
				}

				NavigationHistoryManager.getInstance().go(item);
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
				if(section != null && section.equalsIgnoreCase("quoterequest")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					if(display.equalsIgnoreCase("TODO")){
						//present("HISTORY", parameters);
					}else {
						view.selectOperation(SectionOperation.OPERATIONS);
						present("QUOTE_REQUEST_OPERATIONS", parameters);
					}
				}
				QuoteRequestSectionViewPresenter.this.overlayController.onParameters(parameters);
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
				}else if(show.equalsIgnoreCase("closerequest")) {
					present("QUOTE_REQUEST_CLOSE", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("deleterequest")){
					present("QUOTE_REQUEST_DELETE", parameters);
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
