package bigBang.module.expenseModule.client.userInterface.presenter;

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

public class ExpenseSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED,
	}

	public static enum SectionOperation {
		OPERATIONS,
		MASS_MANAGER_TRANSFER, MASS_PARTICIPATE_TO_INSURER, MASS_NOTIFY_RESULTS_CLIENT
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

	public ExpenseSectionViewPresenter(View view) {
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
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
	}

	public void bind() {
		this.view.registerActionHandler(new ActionInvokedEventHandler<ExpenseSectionViewPresenter.Action>() {

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

		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<ExpenseSectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "casualty");
				item.setStackParameter("display");

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
					case MASS_PARTICIPATE_TO_INSURER:
						item.pushIntoStackParameter("display", "massparticipatetoinsurer");
						break;
					case MASS_NOTIFY_RESULTS_CLIENT:
						item.pushIntoStackParameter("display", "massnotifyresultsclient");
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
				if(section != null && section.equalsIgnoreCase("expense")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					//MASS OPERATIONS
					if(display.equalsIgnoreCase("massmanagertransfer")){
						view.selectOperation(SectionOperation.MASS_MANAGER_TRANSFER);
						present("MANAGER_TRANSFER", parameters);
					}else if(display.equalsIgnoreCase("massparticipatetoinsurer")){
						view.selectOperation(SectionOperation.MASS_PARTICIPATE_TO_INSURER);
						present("MASS_PARTICIPATE_TO_INSURER", parameters);
					}else if(display.equalsIgnoreCase("massnotifyresultsclient")){
						view.selectOperation(SectionOperation.MASS_NOTIFY_RESULTS_CLIENT);
						present("MASS_NOTIFY_RESULTS_CLIENT", parameters);
					}else{
						view.selectOperation(SectionOperation.OPERATIONS);
						present("EXPENSE_OPERATIONS", parameters);
					}
				}
				ExpenseSectionViewPresenter.this.overlayController.onParameters(parameters);
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
				}else if(show.equalsIgnoreCase("contactmanagement")){
					present("CONTACT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("documentmanagement")){
					present("DOCUMENT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("receiveresponse")){
					present("RECEIVE_RESPONSE", parameters);
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
