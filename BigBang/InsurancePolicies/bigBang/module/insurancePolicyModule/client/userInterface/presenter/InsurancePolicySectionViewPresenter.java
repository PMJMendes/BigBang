package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

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

public class InsurancePolicySectionViewPresenter implements ViewPresenter{

	public static enum Action {
		ON_OVERLAY_CLOSED,
	}

	public static enum SectionOperation {
		OPERATIONS,
		MASS_MANAGER_TRANSFER,
		REPORT, GENERAL_TASKS
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

	public InsurancePolicySectionViewPresenter(View view) {
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

		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<InsurancePolicySectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "insurancepolicy");
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
					case REPORT:
						item.pushIntoStackParameter("display", "report");
						break;
					case GENERAL_TASKS:
						item.pushIntoStackParameter("display", "generaltasks");
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
				if(section != null && section.equalsIgnoreCase("insurancepolicy")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					if(display.equalsIgnoreCase("massmanagertransfer")){
						view.selectOperation(SectionOperation.MASS_MANAGER_TRANSFER);
						present("INSURANCE_POLICY_CREATE_MASS_MANAGER_TRANSFER", parameters);
					}else if(display.equalsIgnoreCase("report")){
						view.selectOperation(SectionOperation.REPORT);
						parameters.setParameter("processtypeid", BigBangConstants.EntityIds.INSURANCE_POLICY);
						present("REPORTS", parameters);
					}else if(display.equalsIgnoreCase("generaltasks")){
						view.selectOperation(SectionOperation.GENERAL_TASKS);
						present("INSURANCE_POLICY_GENERAL_TASKS", parameters);
					}else {
						view.selectOperation(SectionOperation.OPERATIONS);
						present("INSURANCE_POLICY_OPERATIONS", parameters, true);
					}
				}
				InsurancePolicySectionViewPresenter.this.overlayController.onParameters(parameters);
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
				}else if(show.equalsIgnoreCase("voidpolicy")){
					present("INSURANCE_POLICY_VOID", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("issuecreditnote")){
					present("INSURANCE_POLICY_CREATE_DEBIT_NOTE", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("contactmanagement")){
					present("CONTACT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("documentmanagement")){
					present("DOCUMENT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("deletesubpolicy")){
					present("INSURANCE_POLICY_SUB_POLICY_DELETE", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("deletenegotiation")){
					present("INSURANCE_POLICY_NEGOTIATION_DELETE", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("cancelnegotiation")){
					present("NEGOTIATION_CANCEL", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("transfertoclient")){
					present("INSURANCE_POLICY_TRANSFER_TO_CLIENT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("voidsubpolicy")){
					present("INSURANCE_POLICY_SUB_POLICY_VOID", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("subpolicytransfertopolicy")){
					present("INSURANCE_POLICY_SUB_POLICY_TRANSFER_TO_POLICY", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("grantnegotiation")){
					present("NEGOTIATION_GRANT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("responsenegotiation")){
					present("INSURANCE_POLICY_NEGOTIATION_RESPONSE", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("replyexternalrequest")){
					present("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_REPLY", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("continueexternalrequest")){
					present("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_CONTINUATION", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("closeexternalrequest")){
					present("EXTERNAL_INFO_OR_DOCUMENT_REQUEST_CLOSING", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("transfermanager")){
					present("INSURANCE_POLICY_MANAGER_TRANSFER", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("replyinforequest")){
					present("INFO_OR_DOCUMENT_REQUEST_REPLY", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("createsubpolicyreceipts")){
					present("INSURANCE_POLICY_CREATE_SUB_POLICY_RECEIPT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("conversationclose")){
					present("CONVERSATION_CLOSE", parameters);
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
