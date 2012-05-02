package bigBang.module.clientModule.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
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

public class ClientSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED,
	}

	public static enum SectionOperation {
		OPERATIONS,
		MASS_MANAGER_TRANSFER,
		REPORT
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

		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<ClientSectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setStackParameter("display");
				item.setParameter("section", "client");

				if(action.getAction() == null) {
					item.pushIntoStackParameter("display", "search");
				}else{
					switch(action.getAction()) {
					case OPERATIONS:
						item.pushIntoStackParameter("display", "search");
						break;
					case MASS_MANAGER_TRANSFER:
						item.pushIntoStackParameter("display", "massmanagertransfer");
						break;
					case REPORT:
						item.pushIntoStackParameter("display", "report");
						break;
					}
				}

				NavigationHistoryManager.getInstance().go(item);
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
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					//MASS OPERATIONS
					if(display.equalsIgnoreCase("massmanagertransfer")){
						view.selectOperation(SectionOperation.MASS_MANAGER_TRANSFER);
						present("CLIENT_MASS_MANAGER_TRANSFER", parameters);
					}else if(display.equalsIgnoreCase("report")){
						view.selectOperation(SectionOperation.REPORT);
						parameters.setParameter("processtypeid", BigBangConstants.EntityIds.CLIENT);
						present("REPORTS", parameters);
					}else {
						view.selectOperation(SectionOperation.OPERATIONS);
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
				}else if(show.equalsIgnoreCase("cancelinforequest")){
					present("INFO_OR_DOCUMENT_REQUEST_CANCELLATION", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("replyinforequest")){
					present("INFO_OR_DOCUMENT_REQUEST_REPLY", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("repeatinforequest")){
					present("INFO_OR_DOCUMENT_REQUEST_REPEAT", parameters);
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
