package bigBang.module.clientModule.client.userInterface.presenter;

import bigBang.library.client.EventBus;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.OperationInvokedEvent;
import bigBang.library.client.event.OperationInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.SearchService;
import bigBang.library.interfaces.Service;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroup;
import bigBang.module.clientModule.shared.operation.ClientSearchOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ClientSearchOperationViewPresenter implements OperationViewPresenter {

	public interface Display {
		HasSelectables<?> getClientSearchList();
		void setClient(Client client);
		
		Widget asWidget();
		View getInstance();
	}

	protected Display view;
	private EventBus eventBus;
	@SuppressWarnings("unused")
	private SearchService service;
	private ClientSearchOperation operation;
	
	public ClientSearchOperationViewPresenter(EventBus eventBus, SearchService service, View view){
		this.setService((Service) service);
		this.setView(view);
		this.setEventBus(eventBus);
	}

	public void setService(Service service) {
		this.service = (SearchService) service;
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
		this.view.getClientSearchList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				for(Selectable s : event.getSelected()){
					ValueSelectable<?> vs = (ValueSelectable<?>) s;
					if(vs.getValue() instanceof Client){
						GWT.log("Client selected");
					}else if(vs.getValue() instanceof ClientGroup) {
						GWT.log("Group selected");
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

	@SuppressWarnings("unused")
	private void fetchClientProcess(String processId){
		Client process = new Client();
		this.view.setClient(process);
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
		
	}
}
