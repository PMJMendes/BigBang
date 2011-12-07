package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.shared.Permission;
import bigBang.module.receiptModule.interfaces.ReceiptServiceAsync;
import bigBang.module.receiptModule.shared.operation.ReceiptSearchOperation;
import bigBang.module.receiptModule.shared.ModuleConstants;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ReceiptSearchOperationViewPresenter implements
OperationViewPresenter, ReceiptDataBrokerClient {

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

		Widget asWidget();
		View getInstance();
	}

	protected ReceiptServiceAsync service;
	protected EventBus eventBus;
	protected Display view;
	protected boolean bound;
	protected ReceiptSearchOperation operation;
	protected ReceiptProcessDataBroker receiptBroker;
	protected int version;

	public ReceiptSearchOperationViewPresenter(EventBus evenBus, Service service, View view) {
		setEventBus(eventBus);
		setService(service);
		setView(view);
		this.receiptBroker = (ReceiptProcessDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
	}

	@Override
	public void setService(Service service) {
		this.service = (ReceiptServiceAsync) service;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		if(bound)
			return;
		bound = true;
		view.lockForm(true);
		view.clearAllowedPermissions();
		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selected = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				final ReceiptStub selectedValue = selected == null ? null : selected.getValue();
				view.setSaveModeEnabled(false);
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.scrollFormToTop();
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						BigBangPermissionManager.Util.getInstance().getProcessPermissions(selectedValue.processId, new ResponseHandler<Permission[]> () {

							@Override
							public void onResponse(Permission[] response) {
								view.scrollFormToTop();
								view.clearAllowedPermissions();
								for(int i = 0; i < response.length; i++) {
									Permission p = response[i];
									if(p.instanceId == null){continue;}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CHANGE_RECEIPT_DATA)){
										view.allowUpdate(true);
									}else if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.DELETE_RECEIPT)){
										view.allowDelete(true);
									}
								}
								receiptBroker.getReceipt(selectedValue.id, new ResponseHandler<Receipt>() {

									@Override
									public void onResponse(Receipt value) {
										view.getForm().setValue(value);
										view.lockForm(value == null);
									}

									@Override
									public void onError(Collection<ResponseError> errors) {}
								});
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								// TODO Auto-generated method stub
							}

						});
					}
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
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		this.operation = (ReceiptSearchOperation) o;
	}

	@Override
	public Operation getOperation() {
		return this.operation;
	}

	@Override
	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {
		this.operation.setPermission(hasPermissionForOperation);
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
			public void onError(Collection<ResponseError> errors) {}
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
			public void onError(Collection<ResponseError> errors) {}
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
