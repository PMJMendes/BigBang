package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ClientManagerTransferOperationViewPresenter implements ViewPresenter {

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
		Collection<ClientStub> getSelectedClientStubs();

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
	protected ClientProcessBroker clientBroker;

	public ClientManagerTransferOperationViewPresenter(View view){
		this.setView(view);
		this.clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
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
				Collection<ClientStub> selectedClients = view.getSelectedClientStubs();
				String[] clientIds = new String[selectedClients.size()];
				String[] clientProcessIds = new String[selectedClients.size()];
				
				int i = 0;
				for(ClientStub s : selectedClients) {
					clientIds[i] = s.id;
					clientProcessIds[i] = s.processId;
					i++;
				}
				String managerId = view.getForm().getValue();
				
//				clientBroker.createManagerTransfer(clientProcessIds,clientIds, managerId, new ResponseHandler<ManagerTransfer>() {
//					
//					@Override
//					public void onResponse(ManagerTransfer response) {
//						view.showMessage("Foi criado um novo processo de Alteração de Gestor de Cliente.");
//						view.clear();
//					}
//					
//					@Override
//					public void onError(Collection<ResponseError> errors) {
//						view.showMessage("Não foi possível criar um novo processo de Alteração de Gestor de Cliente. Por favor tente de novo mais tarde.");
//					}
//				});
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
		
		//APPLICATION-WIDE EVENTS
	}

}
