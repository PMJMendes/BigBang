package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.OperationInvokedEvent;
import bigBang.library.client.event.OperationInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.clientModule.shared.operation.ClientSearchOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ClientSearchOperationViewPresenter implements OperationViewPresenter, ClientProcessDataBrokerClient {

	public interface Display {
		HasSelectables<?> getClientSearchList();
		void setClient(Client client);
		
		void setReadOnly(boolean readonly);
		
		Widget asWidget();
		View getInstance();
	}

	protected Display view;
	private EventBus eventBus;
	private ClientSearchOperation operation;
	
	protected ClientProcessBroker clientBroker;
	protected int clientDataVersionNumber;
	
	public ClientSearchOperationViewPresenter(EventBus eventBus, ClientServiceAsync service, View view){
		this.setService((Service) service);
		this.setView(view);
		this.setEventBus(eventBus);
		this.clientBroker = (ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT);
	}

	public void setService(Service service) {
		//TODO CLEAN
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
		view.setReadOnly(true);
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
		this.view.getClientSearchList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()){
					ValueSelectable<?> vs = (ValueSelectable<?>) s;
					if(vs.getValue() instanceof ClientStub){
						ClientStub stub = (ClientStub) vs.getValue();
						showClientProcess(stub.id);
					}
				}
			}
		});
	}

	public void setOperation(final ClientSearchOperation operation) {
		this.operation = operation;
	}

	public void registerEventHandlers(final EventBus eventBus) {
		eventBus.addHandler(OperationInvokedEvent.TYPE,	new OperationInvokedEventHandler() {

			public void onOperationInvoked(OperationInvokedEvent event) {
				if(getOperation() == null || !event.getOperationId().equals(getOperation().getId()))
					return;
				if(event.goToScreen())
					GWT.log("GOTO SCREEN");
				else {
					View tempView  = view.getInstance();
					ClientSearchOperationViewPresenter presenter = new ClientSearchOperationViewPresenter(eventBus, null, tempView);
					event.getViewPresenterManager().managePresenter(getOperation().getId(), presenter);
					//presenter.setTarget(event.getTargetId());
				}
			}
		});
	}

	private void showClientProcess(final String processId){
		this.clientBroker.getClient(processId, new ResponseHandler<Client>() {
			
			@Override
			public void onResponse(Client response) {
				view.setClient(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				throw new RuntimeException("Could not get the client process with id=\"" + processId + "\"");
			}
		});
	}
	
	public void setOperation(Operation o) {
		this.operation = (ClientSearchOperation)o;
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

	private void setReadOnly(boolean result) {
		// TODO Auto-generated method stub
		this.view.setReadOnly(true);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.CLIENT)){
			this.clientDataVersionNumber = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.CLIENT))
			return this.clientDataVersionNumber;
		throw new RuntimeException(this.getClass().getName() + " does not support being a data broker client for data element with id=" + dataElementId);
	}

	@Override
	public void addClient(Client client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClient(Client client) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeClient(String clientId) {
		// TODO Auto-generated method stub
		
	}
}
