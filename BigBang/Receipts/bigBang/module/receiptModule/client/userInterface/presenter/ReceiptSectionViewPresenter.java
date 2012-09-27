package bigBang.module.receiptModule.client.userInterface.presenter;

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

public class ReceiptSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED,
	}

	public static enum SectionOperation {
		OPERATIONS,
		SERIAL_RECEIPT_CREATION,
		SERIAL_RECEIPT_MARK_FOR_PAYMENT,
		MASS_SEND_RECEIPT_TO_CLIENT,
		MASS_INSURER_ACCOUNTING,
		MASS_AGENT_ACCOUNTING,
		MASS_SIGNATURE_REQUEST,
		REPORT,
		MASS_RETURN_TO_INSURER,
		MASS_CREATE_PAYMENT_NOTICE,
		GENERAL_TASKS
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
		
		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<ReceiptSectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setStackParameter("display");
				item.setParameter("section", "receipt");

				if(action.getAction() == null) {
					item.pushIntoStackParameter("display", "search");
				}else{
					switch(action.getAction()) {
					case OPERATIONS:
						item.pushIntoStackParameter("display", "search");
						break;
					case MASS_AGENT_ACCOUNTING:
						item.pushIntoStackParameter("display", "massagentaccounting");
						break;
					case MASS_CREATE_PAYMENT_NOTICE:
						item.pushIntoStackParameter("display", "masscreatepaymentnotice");
						break;
					case MASS_INSURER_ACCOUNTING:
						item.pushIntoStackParameter("display", "massinsureraccounting");
						break;
					case MASS_SIGNATURE_REQUEST:
						item.pushIntoStackParameter("display", "masssignaturerequest");
						break;
					case MASS_SEND_RECEIPT_TO_CLIENT:
						item.pushIntoStackParameter("display", "masssendreceipt");
						break;
					case SERIAL_RECEIPT_CREATION:
						item.pushIntoStackParameter("display", "serialreceiptcreation");
						break;
					case SERIAL_RECEIPT_MARK_FOR_PAYMENT:
						item.pushIntoStackParameter("display", "serialmarkforpayment");
						break;
					case MASS_RETURN_TO_INSURER:
						item.pushIntoStackParameter("display", "massreceiptreturn");
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
				if(section != null && section.equalsIgnoreCase("receipt")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					if(display.equalsIgnoreCase("search")){
						view.selectOperation(SectionOperation.OPERATIONS);
						present("RECEIPT_OPERATIONS", parameters, true);
					}else if(display.equalsIgnoreCase("serialreceiptcreation")){
						view.selectOperation(SectionOperation.SERIAL_RECEIPT_CREATION);
						present("SERIAL_RECEIPT_CREATION", parameters);
					}else if(display.equalsIgnoreCase("masscreatepaymentnotice")){
						view.selectOperation(SectionOperation.MASS_CREATE_PAYMENT_NOTICE);
						present("MASS_CREATE_PAYMENT_NOTICE", parameters);
					}else if(display.equalsIgnoreCase("serialmarkforpayment")){
						present("SERIAL_RECEIPT_MARK_FOR_PAYMENT", parameters);
						view.selectOperation(SectionOperation.SERIAL_RECEIPT_MARK_FOR_PAYMENT);
					}else if(display.equalsIgnoreCase("masssignaturerequest")){
						present("MASS_SIGNATURE_REQUEST", parameters);
						view.selectOperation(SectionOperation.MASS_SIGNATURE_REQUEST);
					}else if(display.equalsIgnoreCase("masssendreceipt")){
						view.selectOperation(SectionOperation.MASS_SEND_RECEIPT_TO_CLIENT);
						present("MASS_SEND_RECEIPT_TO_CLIENT", parameters);
					}else if(display.equalsIgnoreCase("massinsureraccounting")){
						view.selectOperation(SectionOperation.MASS_INSURER_ACCOUNTING);
						present("MASS_INSURER_ACCOUNTING", parameters);
					}else if(display.equalsIgnoreCase("massagentaccounting")){
						view.selectOperation(SectionOperation.MASS_AGENT_ACCOUNTING);
						present("MASS_AGENT_ACCOUNTING", parameters);
					}else if(display.equalsIgnoreCase("massreceiptreturn")){
						view.selectOperation(SectionOperation.MASS_RETURN_TO_INSURER);
						present("MASS_RECEIPT_RETURN_TO_AGENCY", parameters);
					}else if(display.equalsIgnoreCase("report")){
						view.selectOperation(SectionOperation.REPORT);
						parameters.setParameter("processtypeid", BigBangConstants.EntityIds.RECEIPT);
						present("REPORTS", parameters);
					}else if(display.equalsIgnoreCase("generaltasks")){
						view.selectOperation(SectionOperation.GENERAL_TASKS);
						present("RECEIPT_GENERAL_TASKS", parameters);
					}
					else{
						present("RECEIPT_OPERATIONS", parameters, true);
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
				}else if(show.equalsIgnoreCase("cancelsignaturerequest")){
					present("CANCEL_SIGNATURE_REQUEST", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("createsignaturerequest")){
					present("CREATE_SIGNATURE_REQUEST", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("contactmanagement")){
					present("CONTACT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("documentmanagement")){
					present("DOCUMENT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("canceldasrequest")){
					present("CANCEL_DAS_REQUEST", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("createdasrequest")){
					present("CREATE_DAS_REQUEST", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("repeatdasrequest")){
					present("REPEAT_DAS_REQUEST", parameters);
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
