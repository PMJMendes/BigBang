package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptSearchOperationViewPresenter implements ViewPresenter, ReceiptDataBrokerClient, HasOperationPermissions {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE,
	}

	public interface Display {
		//List
		HasValueSelectables<?> getList();
		void selectReceipt(Receipt receipt);

		//Form
		HasEditableValue<Receipt> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);
		void scrollFormToTop();

		void allowUpdate(boolean allow);
		void allowDelete(boolean allow);

		void clearAllowedPermissions();
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);
		void setReadOnly(boolean readOnly);

		void showErrorMessage(String message);

		Widget asWidget();
	}

	protected Display view;
	protected boolean bound;
	protected ReceiptProcessDataBroker receiptBroker;
	protected int version;
	protected ViewPresenterController controller;

	public ReceiptSearchOperationViewPresenter(View view) {
		setView(view);
		this.receiptBroker = (ReceiptProcessDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		controller.onParameters(parameterHolder);
	}

	@Override
	public void setPermittedOperations(String[] operationIds) {
		// TODO Auto-generated method stub

	}

	private void bind() {
		if(bound)
			return;

		view.lockForm(true);
		view.clearAllowedPermissions();
		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selected = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				ReceiptStub selectedValue = selected == null ? null : selected.getValue();
				view.setSaveModeEnabled(false);

				if(selectedValue == null || selectedValue.id == null){
					NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
					navigationItem.removeParameter("receiptid");
					NavigationHistoryManager.getInstance().go(navigationItem);
				}else{
					NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
					navigationItem.setParameter("receiptid", selectedValue.id);
					NavigationHistoryManager.getInstance().go(navigationItem);
				}
			}
		});
		this.view.registerActionInvokedHandler(new ActionInvokedEventHandler<ReceiptSearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case CANCEL:
					view.getForm().revert();
					view.getForm().setReadOnly(true);
					view.setSaveModeEnabled(false);
					break;
				case DELETE:
					deleteReceipt();
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					Receipt info = view.getForm().getInfo();
					saveReceipt(info);
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	private void initializeController(){
		this.controller = new ViewPresenterController() {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
			
			@Override
			public void onParameters(HasParameters parameters) {
				String display = parameters.peekInStackParameter("display");
				display = display == null ? "" : display;

				//SEARCH
				{
					String receiptId = parameters.getParameter("receiptid");
					receiptId = receiptId == null ? "" : receiptId;
					if(!receiptId.isEmpty()){
						showReceiptWithId(receiptId);
					}else{
						clearView();
					}
				}
			}

		};
	}

	private void clearView(){
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getList().clearSelection();
		view.clear();
	}

	private void showReceiptWithId(String id){
		//		BigBangPermissionManager.Util.getInstance().getProcessPermissions(selectedValue.processId, new ResponseHandler<Permission[]> () {
		//
		//			@Override
		//			public void onResponse(Permission[] response) {
		//				view.scrollFormToTop();
		//				view.clearAllowedPermissions();
		//				for(int i = 0; i < response.length; i++) {
		//					Permission p = response[i];
		//					if(p.instanceId == null){continue;}
		//					if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CHANGE_RECEIPT_DATA)){
		//						view.allowUpdate(true);
		//					}else if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.DELETE_RECEIPT)){
		//						view.allowDelete(true);
		//					}
		//				}
		receiptBroker.getReceipt(id, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt value) {
				view.getForm().setValue(value);
				view.lockForm(value == null);
				//TODO select in list
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				view.showErrorMessage("Não foi possível obter o recibo pedido.");
			}
		});
		//			}
		//
		//			@Override
		//			public void onError(Collection<ResponseError> errors) {
		//				// TODO Auto-generated method stub
		//			}
		//
		//		});
	}

	protected void saveReceipt(Receipt receipt){
		this.receiptBroker.updateReceipt(receipt, new ResponseHandler<Receipt>() {

			@Override
			public void onResponse(Receipt response) {
				view.selectReceipt(response);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				view.showErrorMessage("De momento não foi possível guardar o recibo.");
			}
		});
	}

	protected void deleteReceipt(){
		Receipt receipt = view.getForm().getValue();
		if(receipt == null || receipt.id == null){
			return;
		}
		String id = receipt.id;
		this.receiptBroker.removeReceipt(id, new ResponseHandler<String>() {

			@Override
			public void onResponse(String response) {
				view.getList().clearSelection();
				view.getForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				view.showErrorMessage("Não é possível eliminar o recibo.");
			}
		});
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		this.version = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return this.version;
	}

	@Override
	public void addReceipt(Receipt receipt) {
		return;
	}

	@Override
	public void updateReceipt(Receipt receipt) {
		return;
	}

	@Override
	public void removeReceipt(String id) {
		return;
	}

}
