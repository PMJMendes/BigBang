package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.User;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class UserManagementOperationViewPresenter implements ViewPresenter {

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
		void removeFromList(ValueSelectable<User> selectable);

		//Form
		HasEditableValue<User> getForm();
		void showFormPassword(boolean show);
		boolean isFormValid();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewUser(User user);

		//PERMISSIONS
		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
		void addUserToList(ListEntry<User> response);
	}

	private Display view;
	private UserBroker userBroker;
	private boolean bound = false;

	public UserManagementOperationViewPresenter(View view){
		this.userBroker = (UserBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.USER);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
		if(view.getList().getAll().size() == 0){
			setup();
		}
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		String userId = parameterHolder.getParameter("userid");
		userId = userId == null ? new String() : userId;

		if(inUserCreation()){
			clearNewUser();
		}

		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageUsers);
		view.allowCreate(hasPermissions);
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);

		if(userId.isEmpty()){
			clearView();
		}else if(userId.equalsIgnoreCase("new")){
			setupNewUser();
		}else{
			showUser(userId);
		}
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
				String userId = selectedValue == null ? null : selectedValue.id;
				userId = userId == null ? new String() : userId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(userId.isEmpty()){
					item.removeParameter("userid");
				}else{
					item.setParameter("userid", userId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<UserManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.setParameter("userid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					deleteUser(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(view.getForm().validate()) {
						User info = view.getForm().getInfo();
						view.getForm().setReadOnly(true);
						if(info.id.equalsIgnoreCase("new"))
							createUser(info);
						else
							saveUser(info);
					}else{
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
					}
					break;
				case CANCEL_EDIT:
					if(inUserCreation()){
						deleteUser(view.getForm().getValue());
					}else{
						NavigationHistoryManager.getInstance().reload();
					}
					break;
				case REFRESH:
					onRefresh();
					break;
				default:
					break;
				}
			}
		});

		bound = true;
	}

	private void setup(){
		this.userBroker.requireDataRefresh();
		this.userBroker.getUsers(new ResponseHandler<User[]>() {

			@Override
			public void onResponse(User[] response) {
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetUserListFailed();
			}
		});
	}


	private void onGetUserListFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de utilizadores"), TYPE.ALERT_NOTIFICATION));
	}


	private void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	private void setupNewUser(){
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageUsers);
		if(hasPermissions){
			User user = new User();
			user.id = "new";
			user.name = "Novo Utilizador";
			view.getList().clearSelection();
			view.prepareNewUser(user);
			view.getForm().setValue(user);
			view.showFormPassword(true);

			view.allowDelete(hasPermissions);
			view.allowEdit(hasPermissions);
			view.setSaveModeEnabled(hasPermissions);
			view.getForm().setReadOnly(!hasPermissions);
		}else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearNewUser(){
		if(inUserCreation()){
			for(ValueSelectable<User> selected : view.getList().getAll()){
				User user = selected.getValue();
				if(user == null || user.id.equalsIgnoreCase("new")){
					view.removeFromList(selected);
					break;
				}
			}
			view.clearAllowedPermissions();
			view.getForm().setValue(null);
			view.getForm().setReadOnly(true);
			view.showFormPassword(false);
			view.getList().clearSelection();
		}
	}

	private boolean inUserCreation(){
		for(ValueSelectable<User> selected : view.getList().getAll()){
			User user = selected.getValue();
			if(user == null || user.id.equalsIgnoreCase("new")){
				return true;
			}
		}
		return false;
	}

	private void showUser(String userId){
		//Selects the user in list
		for(ValueSelectable<User> entry : view.getList().getAll()){
			User listUser = entry.getValue();
			if(listUser.id.equalsIgnoreCase(userId) && !entry.isSelected()){
				entry.setSelected(true, true);
			}
		}
		//Gets the user to show
		this.userBroker.getUser(userId, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				view.clearAllowedPermissions();

				boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageUsers);
				view.allowEdit(hasPermissions);
				view.allowDelete(hasPermissions);

				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				ensureListedAndSelected(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetUserFailed();
			}
		});
	}

	protected void ensureListedAndSelected(User response) {
		for(ValueSelectable<User> stub : view.getList().getAll()){
			if(stub.getValue().id.equals(response.id)){
				stub.setSelected(true, false);
			}
			else{
				stub.setSelected(false,false);
			}
		}
	}

	public void createUser(User c) {
		c.id = null;
		this.userBroker.addUser(c, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("userid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Utilizador criado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateUserFailed();
			}
		});
	}

	public void saveUser(User c) {
		this.userBroker.updateUser(c, new ResponseHandler<User>() {

			@Override
			public void onResponse(User response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("userid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Utilizador guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveUserFailed();
			}
		});
	}

	public void deleteUser(final User c) {
		if(c.id.equalsIgnoreCase("new")){
			clearNewUser();
		}else{
			this.userBroker.removeUser(c.id, new ResponseHandler<User>() {

				@Override
				public void onResponse(User response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("userid");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Utilizador eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onDeleteUserFailed();
				}
			});
		}
	}	

	protected void onRefresh(){
		userBroker.requireDataRefresh();
		userBroker.getUsers(new ResponseHandler<User[]>() {

			@Override
			public void onResponse(User[] response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	private void onGetUserFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter o utilizador seleccionado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("userid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateUserFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar o utilizador"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onSaveUserFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações ao utilizador"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onDeleteUserFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar o utilizador"), TYPE.ALERT_NOTIFICATION));
	}

}
