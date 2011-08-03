package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.UserBroker;
import bigBang.definitions.client.types.User;
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
import bigBang.module.generalSystemModule.shared.operation.UserManagementOperation;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class UserManagementOperationViewPresenter implements
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
		HasValueSelectables<User> getList();

		//Form
		HasEditableValue<User> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewUser();
		void removeNewUserPreparation();
		void setSaveModeEnabled(boolean enabled);

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private Display view;
	@SuppressWarnings("unused")
	private EventBus eventBus;

	private UserManagementOperation operation;
	protected UserBroker userBroker;

	private boolean bound = false;

	public UserManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);

		this.userBroker = (UserBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER);
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
		fetchUserList();
			
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
				ValueSelectable<User> selected = (ValueSelectable<User>) event.getFirstSelected();
				User selectedValue = selected == null ? null : selected.getValue();
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewUserPreparation();
						userBroker.getUser(selectedValue.id, new ResponseHandler<User>() {

							@Override
							public void onResponse(User value) {
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
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<UserManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					view.prepareNewUser();
					for(Selectable s : view.getList().getSelected()) {
						@SuppressWarnings("unchecked")
						ValueSelectable<User> vs = (ValueSelectable<User>) s;
						User value = vs.getValue();
						view.getForm().setValue(value);
						view.getForm().setReadOnly(false);
						break;
					}
					break;
				case REFRESH:
					fetchUserList();
					break;
				case DELETE:
					if(view.getForm().getValue().id == null)
						view.removeNewUserPreparation();
					else
						deleteUser(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					User info = view.getForm().getInfo();
					if(info.id == null)
						createUser(info);
					else
						saveUser(info);
					break;
				case CANCEL_EDIT:
					if(view.getForm().getInfo().id == null){
						view.removeNewUserPreparation();
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
	
	private void fetchUserList() {
		//Refreshes The users data (Info automatically propagated to the broker clients)
		this.userBroker.requireDataRefresh();
		this.userBroker.getUsers(new ResponseHandler<User[]>() {

			@Override
			public void onResponse(User[] response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	} 

	public void createUser(User c) {
		this.userBroker.addUser(c, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void saveUser(User c) {
		this.userBroker.updateUser(c, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void deleteUser(final User c) {
		this.userBroker.removeUser(c.id, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
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
		this.operation = (UserManagementOperation) o;
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
