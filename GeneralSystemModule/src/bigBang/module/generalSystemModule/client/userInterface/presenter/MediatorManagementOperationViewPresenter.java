package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.MediatorBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Mediator;
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
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.operation.MediatorManagementOperation;


public class MediatorManagementOperationViewPresenter implements
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
		HasValueSelectables<Mediator> getList();

		//Form
		HasEditableValue<Mediator> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewMediator();
		void removeNewMediatorPreparation();
		void setSaveModeEnabled(boolean enabled);

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private Display view;
	@SuppressWarnings("unused")
	private EventBus eventBus;

	private MediatorManagementOperation operation;
	protected MediatorBroker mediatorBroker;

	private boolean bound = false;

	public MediatorManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);

		this.mediatorBroker = (MediatorBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.MEDIATOR);
	}

	public void setService(Service service) {}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;

		view.clear();
		view.getList().setMultipleSelectability(false);
		view.getForm().setReadOnly(true);
		fetchMediatorList();
			
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
				ValueSelectable<Mediator> selected = (ValueSelectable<Mediator>) event.getFirstSelected();
				Mediator selectedValue = selected == null ? null : selected.getValue();
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewMediatorPreparation();
						mediatorBroker.getMediator(selectedValue.id, new ResponseHandler<Mediator>() {

							@Override
							public void onResponse(Mediator value) {
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
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<MediatorManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					view.prepareNewMediator();
					for(Selectable s : view.getList().getSelected()) {
						@SuppressWarnings("unchecked")
						ValueSelectable<Mediator> vs = (ValueSelectable<Mediator>) s;
						Mediator value = vs.getValue();
						view.getForm().setValue(value);
						view.getForm().setReadOnly(false);
						break;
					}
					break;
				case REFRESH:
					fetchMediatorList();
					break;
				case DELETE:
					if(view.getForm().getValue().id == null)
						view.removeNewMediatorPreparation();
					else
						deleteMediator(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					Mediator info = view.getForm().getInfo();
					if(info.id == null)
						createMediator(info);
					else
						saveMediator(info);
					break;
				case CANCEL_EDIT:
					if(view.getForm().getInfo().id == null){
						view.removeNewMediatorPreparation();
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
	
	private void fetchMediatorList() {
		//Refreshes The mediators data (Info automatically propagated to the broker clients)
		this.mediatorBroker.requireDataRefresh();
		this.mediatorBroker.getMediators(new ResponseHandler<Mediator[]>() {

			@Override
			public void onResponse(Mediator[] response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	} 

	public void createMediator(Mediator c) {
		this.mediatorBroker.addMediator(c, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void saveMediator(Mediator c) {
		this.mediatorBroker.updateMediator(c, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void deleteMediator(final Mediator c) {
		this.mediatorBroker.removeMediator(c.id, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				view.getForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}	

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public void setOperation(Operation o) {
		this.operation = (MediatorManagementOperation) o;
	}

	public Operation getOperation() {
		return operation;
	}

	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

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
		view.setReadOnly(result);
	}

}
