package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.interfaces.TransferManagerService;
import bigBang.library.interfaces.TransferManagerServiceAsync;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ManagerTransferViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		ACCEPT,
		CANCEL
	}

	public interface Display{
		HasValue<ManagerTransfer> getForm();
		void setObjectType(String type);

		HasValueSelectables<Object> getList();
		void addToList(ListEntry<Object> selectable);
		void clearList();

		//PERMISSIONS
		void allowAccept(boolean allow);
		void allowCancel(boolean allow); 
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private ManagerTransfer transfer;
	private Display view;
	private TransferManagerServiceAsync managerTransferService;
	private boolean bound = false;

	public ManagerTransferViewPresenter(Display view){
		this.managerTransferService = TransferManagerService.Util.getInstance();
		setView((UIObject) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String id = parameterHolder.getParameter("id");
		id = id == null ? new String() : id;
		String transferId = parameterHolder.getParameter("transferid");
		transferId = transferId == null ? new String() : transferId;

		clearView();
		
		if(!transferId.isEmpty()){
			id = transferId;
		}
		
		if(id.isEmpty()){
			onGetManagerTransferFailed();
		}else{
			showTransfer(id);
		}
	}

	public void bind(){
		if(bound){
			return;
		}
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler(){

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Object> selected = (ValueSelectable<Object>) event.getFirstSelected();
				Object selectedObject = selected == null ? null : selected.getValue();
				if(selectedObject != null) {
					navigateTo(selectedObject);
				}
			}

		});

		view.registerActionHandler(new ActionInvokedEventHandler<ManagerTransferViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case ACCEPT:
					acceptTransfer();
					break;
				case CANCEL:
					cancelTransfer();
					break;
				}
			}
		});
		bound = true;
	}

	private void clearView() {
		view.clearList();
		view.getForm().setValue(null);
		view.allowAccept(false);
		view.allowCancel(false);
	}

	private void showTransfer(String transferId) {
		ManagerTransferViewPresenter.this.managerTransferService.getTransfer(transferId, new BigBangAsyncCallback<ManagerTransfer>() {

			@Override
			public void onResponseSuccess(ManagerTransfer result) {
				setManagerTransfer(result);
			}
		});
	}

	private void setManagerTransfer(ManagerTransfer transfer){
		this.transfer = transfer;
		
		view.allowAccept(PermissionChecker.hasPermission(transfer, BigBangConstants.OperationIds.ManagerTransfer.ACCEPT_MANAGER_TRANSFER));
		view.allowCancel(PermissionChecker.hasPermission(transfer, BigBangConstants.OperationIds.ManagerTransfer.CANCEL_MANAGER_TRANSFER));
		
		view.clearList();
		view.getForm().setValue(this.transfer);
		fillList();
	}

	private void fillList(){
		if(transfer.objectTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
			view.setObjectType("Clientes");
			for(int i = 0; i<transfer.objectStubs.length; i++){
				ClientStub client = (ClientStub)transfer.objectStubs[i];
				ListEntry<Object> temp = new ListEntry<Object>(client);
				temp.setHeight("40px");
				temp.setTitle("#" + client.clientNumber);
				temp.setText(client.name);
				view.addToList(temp);
			}

		}else if(transfer.objectTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){

			view.setObjectType("Apólices");
			for(int i = 0; i<transfer.objectStubs.length; i++){
				InsurancePolicyStub policy = (InsurancePolicyStub)transfer.objectStubs[i];
				ListEntry<Object> temp = new ListEntry<Object>(policy);
				temp.setHeight("40px");
				temp.setTitle("#" + policy.number);
				temp.setText(policy.categoryName+" / " +policy.lineName + " / "+policy.subLineName);
				view.addToList(temp);
			}
		}else if(transfer.objectTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)){

			view.setObjectType("Consultas de Mercado");
			for(int i = 0; i<transfer.objectStubs.length; i++){
				QuoteRequestStub request = (QuoteRequestStub)transfer.objectStubs[i];
				ListEntry<Object> temp = new ListEntry<Object>(request);
				temp.setHeight("40px");
				temp.setTitle("#" + request.processNumber);
				view.addToList(temp);
			}

		}else if(transfer.objectTypeId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){

			view.setObjectType("Sinistros");
			for(int i = 0; i<transfer.objectStubs.length; i++){
				CasualtyStub casualty = (CasualtyStub)transfer.objectStubs[i];
				ListEntry<Object> temp = new ListEntry<Object>(casualty);
				temp.setHeight("40px");
				temp.setTitle("#" + casualty.processNumber);
				view.addToList(temp);
			}

		}
	}
	
	private void acceptTransfer(){
		managerTransferService.acceptTransfer(this.transfer.id, new BigBangAsyncCallback<ManagerTransfer>() {

			@Override
			public void onResponseSuccess(ManagerTransfer result) {
				onAcceptTransferSuccess();
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				onAcceptTransferFailed();
				super.onResponseFailure(caught);
			}
		});
	}
	
	private void cancelTransfer(){
		managerTransferService.cancelTransfer(this.transfer.id, new BigBangAsyncCallback<ManagerTransfer>() {

			@Override
			public void onResponseSuccess(ManagerTransfer result) {
				onCancelTransferSuccess();
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				onCancelTransferFailed();
				super.onResponseFailure(caught);
			}
		});
	}

	private void navigateTo(Object object){
		if(object instanceof ClientStub){
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "client");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("clientid", ((ClientStub) object).id);
			NavigationHistoryManager.getInstance().go(item);
			
		}else if(object instanceof InsurancePolicyStub) {
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "insurancepolicy");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("policyid", ((InsurancePolicyStub) object).id);
			NavigationHistoryManager.getInstance().go(item);
			
		}else if(object instanceof QuoteRequestStub) {
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "quoterequest");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("quoterequestid", ((QuoteRequestStub) object).id);
			NavigationHistoryManager.getInstance().go(item);
			
		}else if(object instanceof CasualtyStub) {
			NavigationHistoryItem item = new NavigationHistoryItem();
			item.setParameter("section", "casualty");
			item.setStackParameter("display");
			item.pushIntoStackParameter("display", "search");
			item.setParameter("casualtyid", ((CasualtyStub) object).id);
			NavigationHistoryManager.getInstance().go(item);
		}
	}
	
	private void onGetManagerTransferFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar a transferência de gestor"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onAcceptTransferSuccess(){
		EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ManagerTransfer.ACCEPT_MANAGER_TRANSFER, this.transfer.id));
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A transferência foi aceite"), TYPE.TRAY_NOTIFICATION));
	}
	
	private void onAcceptTransferFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível aceitar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onCancelTransferSuccess(){
		EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ManagerTransfer.CANCEL_MANAGER_TRANSFER, this.transfer.id));
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Transferência de Gestor foi cancelada"), TYPE.TRAY_NOTIFICATION));
	}
	
	private void onCancelTransferFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível cancelar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}

	@Override
	public void setPermittedOperations(String[] operationIds) {
		return;
	}
	
}


