package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.ManagerTransfer;
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
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.clientModule.shared.operation.ClientManagerTransferOperation;

public class ClientManagerTransferOperationViewPresenter implements
OperationViewPresenter {

	public static enum Action {
		TRANSFER
	}

	public interface Display {
		//Lists
		HasValueSelectables<?> getList();
		HasValueSelectables<?> getSelectedList();
		
		HasEditableValue<String> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);
		Collection<String> getSelectedClientIds();
		
		HasValue<Client> getClientForm();
		
		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setReadOnly(boolean readOnly);
		Widget asWidget();
		void showMessage(String string);
	}

	protected boolean bound = false;
	protected Display view;
	protected EventBus eventBus;
	protected ClientServiceAsync service;
	protected ClientManagerTransferOperation operation;
	protected ClientProcessBroker clientBroker;

	public ClientManagerTransferOperationViewPresenter(EventBus eventBus, Service service, View view){
		this.setService(service);
		this.setView(view);
		this.setEventBus(eventBus);
		this.clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
	}

	public void setService(Service service) {
		this.service = (ClientServiceAsync) service;
	}

	public void setEventBus(final EventBus eventBus) {
		this.eventBus = eventBus;
		if(this.eventBus == null)
			return;
		registerEventHandlers(eventBus);
	}

	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	//The compact version of the operation view
	public void goCompact(HasWidgets container){
		go(container);
		/*this.bind();
		container.clear();
		container.add((Widget)this.view.getSearchPreviewPanelContainer());*/
	}

	public void bind() {
		if(bound)
			return;
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<ClientManagerTransferOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case TRANSFER:
					onTransfer();
					break;
				}
			}
			
			public void onTransfer() {
				Collection<String> selectedIds = view.getSelectedClientIds();
				String[] clientIds = new String[selectedIds.size()];
				clientIds = selectedIds.toArray(clientIds); 
				String managerId = view.getForm().getValue();
				
				clientBroker.createManagerTransfer(clientIds, managerId, new ResponseHandler<ManagerTransfer>() {
					
					@Override
					public void onResponse(ManagerTransfer response) {
						view.showMessage("O gestor de cliente foi alterado com sucesso.");
						view.clear();
					}
					
					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		});
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(!event.getSelected().isEmpty()){
					view.getSelectedList().clearSelection();
					@SuppressWarnings("unchecked")
					ClientStub c = ((ValueSelectable<ClientStub>)event.getFirstSelected()).getValue();
					clientBroker.getClient(c.id, new ResponseHandler<Client>() {
						
						@Override
						public void onResponse(Client response) {
							view.getClientForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
				}
			}
		});
		view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(!event.getSelected().isEmpty()){
					view.getList().clearSelection();
					@SuppressWarnings("unchecked")
					ClientStub c = ((ValueSelectable<ClientStub>)event.getFirstSelected()).getValue();
					clientBroker.getClient(c.id, new ResponseHandler<Client>() {
						
						@Override
						public void onResponse(Client response) {
							view.getClientForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {}
					});
				}
			}
		});
	}

	public void setOperation(final ClientManagerTransferOperation operation) {
		this.operation = operation;
	}

	public void registerEventHandlers(final EventBus eventBus) {
		//TODO
	}

	public void setOperation(Operation o) {
		this.operation = (ClientManagerTransferOperation)o;
	}

	public Operation getOperation() {
		return this.operation;
	}

	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}

	protected void setReadOnly(boolean result) {
		// TODO Auto-generated method stub

	}

}
