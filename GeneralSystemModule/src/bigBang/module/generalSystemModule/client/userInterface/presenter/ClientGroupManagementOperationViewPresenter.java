package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.ClientGroupBroker;
import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.response.ResponseError;
import bigBang.library.client.response.ResponseHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.operation.ClientGroupManagementOperation;

public class ClientGroupManagementOperationViewPresenter implements
		OperationViewPresenter {

	public static enum Action{
		NEW,
		REFRESH,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE
	}

	public interface Display {
		//List
		HasValueSelectables<ClientGroup> getList();

		//Form
		HasEditableValue<ClientGroup> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewClientGroup();
		void removeNewClientGroupPreparation();
		void setSaveModeEnabled(boolean enabled);

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected EventBus eventBus;
	protected ClientGroupManagementOperation operation;
	protected ClientGroupBroker clientGroupBroker;
	
	public ClientGroupManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		this.setService(service);
		this.setView(view);
		this.setEventBus(eventBus);
		
		this.clientGroupBroker = ((ClientGroupBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT_GROUP));
	}

	public void setService(Service service) {}

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
		bind();
		bound = true;

		view.clear();
		view.getList().setMultipleSelectability(false);
		view.getForm().setReadOnly(true);
		fetchClientGroupList();
			
		container.clear();
		container.add(this.view.asWidget());
	}
	
	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientGroup> selected = (ValueSelectable<ClientGroup>) event.getFirstSelected();
				ClientGroup selectedValue = selected == null ? null : selected.getValue();
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewClientGroupPreparation();
						clientGroupBroker.getClientGroup(selectedValue.id, new ResponseHandler<ClientGroup>() {

							@Override
							public void onResponse(ClientGroup value) {
								view.getForm().setValue(value);
								view.lockForm(value == null);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {}
						});
					}
					
				}
			}
		});
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<ClientGroupManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					view.prepareNewClientGroup();
					for(Selectable s : view.getList().getSelected()) {
						@SuppressWarnings("unchecked")
						ValueSelectable<ClientGroup> vs = (ValueSelectable<ClientGroup>) s;
						ClientGroup value = vs.getValue();
						view.getForm().setValue(value);
						view.getForm().setReadOnly(false);
						view.setSaveModeEnabled(true);
						break;
					}
					break;
				case REFRESH:
					fetchClientGroupList();
					break;
				case DELETE:
					if(view.getForm().getValue().id == null)
						view.removeNewClientGroupPreparation();
					else
						deleteClientGroup(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					ClientGroup info = view.getForm().getInfo();
					if(info.id == null)
						createClientGroup(info);
					else
						saveClientGroup(info);
					break;
				case CANCEL_EDIT:
					if(view.getForm().getInfo().id == null){
						view.removeNewClientGroupPreparation();
					}else{
						view.getForm().revert();
						view.getForm().setReadOnly(true);
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void fetchClientGroupList() {
		//Refreshes The mediators data (Info automatically propagated to the broker clients)
		this.clientGroupBroker.requireDataRefresh();
		this.clientGroupBroker.getClientGroups(new ResponseHandler<ClientGroup[]>() {

			@Override
			public void onResponse(ClientGroup[] response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}
	
	public void createClientGroup(ClientGroup c) {
		this.clientGroupBroker.addClientGroup(c, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void saveClientGroup(ClientGroup c) {
		this.clientGroupBroker.updateClientGroup(c, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void deleteClientGroup(final ClientGroup c) {
		this.clientGroupBroker.removeClientGroup(c.id, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				view.getForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}
	
	//The compact version of the operation view
	public void goCompact(HasWidgets container){
		go(container);
		/*this.bind();
		container.clear();
		container.add((Widget)this.view.getSearchPreviewPanelContainer());*/
	}

	public void setOperation(final ClientGroupManagementOperation operation) {
		this.operation = operation;
	}

	public void registerEventHandlers(final EventBus eventBus) {
		//TODO
	}

	public void setOperation(Operation o) {
		this.operation = (ClientGroupManagementOperation)o;
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
