package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientGroupBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ClientGroup;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ClientGroupManagementOperationViewPresenter implements ViewPresenter {

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
		void removeFromList(ValueSelectable<ClientGroup> selectable);

		//Form
		HasEditableValue<ClientGroup> getForm();
		boolean isFormValid();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewClientGroup(ClientGroup clientGroup);

		//PERMISSIONS
		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected ClientGroupBroker clientGroupBroker;
	
	public ClientGroupManagementOperationViewPresenter(View view){
		this.clientGroupBroker = ((ClientGroupBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT_GROUP));
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
		setup();
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		String groupId = parameterHolder.getParameter("groupid");
		groupId = groupId == null ? new String() : groupId;

		if(inClientGroupCreation()){
			clearNewClientGroup();
		}
		
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageClientGroups);
		view.allowCreate(hasPermissions);
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);

		if(groupId.isEmpty()){
			clearView();
		}else if(groupId.equalsIgnoreCase("new")){
			setupNewClientGroup();
		}else{
			showClientGroup(groupId);
		}
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
				String groupId = selectedValue == null ? null : selectedValue.id;
				groupId = groupId == null ? new String() : groupId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(groupId.isEmpty()){
					item.removeParameter("groupid");
				}else{
					item.setParameter("groupid", groupId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<ClientGroupManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.setParameter("groupid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					deleteClientGroup(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					ClientGroup info = view.getForm().getInfo();
					view.getForm().setReadOnly(true);
					if(info.id.equalsIgnoreCase("new")){
						createClientGroup(info);
					}else{
						saveClientGroup(info);
					}
					break;
				case CANCEL_EDIT:
					if(inClientGroupCreation()){
						deleteClientGroup(view.getForm().getValue());
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
		this.clientGroupBroker.requireDataRefresh();
		this.clientGroupBroker.getClientGroups(new ResponseHandler<ClientGroup[]>() {

			@Override
			public void onResponse(ClientGroup[] response) {
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientGroupListFailed();
			}
		});
	}

	private void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	private void setupNewClientGroup(){
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageClientGroups);
		if(hasPermissions){
			ClientGroup group = new ClientGroup();
			group.id = "new";
			group.name = "Novo Grupo de Clientes";
			view.getList().clearSelection();
			view.prepareNewClientGroup(group);
			view.getForm().setValue(group);
	
			view.allowDelete(hasPermissions);
			view.allowEdit(hasPermissions);
			view.setSaveModeEnabled(hasPermissions);
			view.getForm().setReadOnly(!hasPermissions);
		}else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearNewClientGroup(){
		if(inClientGroupCreation()){
			for(ValueSelectable<ClientGroup> selected : view.getList().getAll()){
				ClientGroup group = selected.getValue();
				if(group == null || group.id.equalsIgnoreCase("new")){
					view.removeFromList(selected);
					break;
				}
			}
			view.clearAllowedPermissions();
			view.getForm().setValue(null);
			view.getForm().setReadOnly(true);
			view.getList().clearSelection();
		}
	}

	private boolean inClientGroupCreation(){
		for(ValueSelectable<ClientGroup> selected : view.getList().getAll()){
			ClientGroup group = selected.getValue();
			if(group == null || group.id.equalsIgnoreCase("new")){
				return true;
			}
		}
		return false;
	}

	private void showClientGroup(String groupId){
		//Selects the client group in list
		for(ValueSelectable<ClientGroup> entry : view.getList().getAll()){
			ClientGroup listClientGroup = entry.getValue();
			if(listClientGroup.id.equalsIgnoreCase(groupId) && !entry.isSelected()){
				entry.setSelected(true, true);
			}
		}
		//Gets the client group to show
		this.clientGroupBroker.getClientGroup(groupId, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				view.clearAllowedPermissions();

				boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageClientGroups);
				view.allowEdit(hasPermissions);
				view.allowDelete(hasPermissions);

				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientGroupFailed();
			}
		});
	}

	public void createClientGroup(ClientGroup c) {
		c.id = null;
		this.clientGroupBroker.addClientGroup(c, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("groupid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Grupo de Clientes criado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateClientGroupFailed();
			}
		});
	}

	public void saveClientGroup(ClientGroup c) {
		this.clientGroupBroker.updateClientGroup(c, new ResponseHandler<ClientGroup>() {

			@Override
			public void onResponse(ClientGroup response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("groupid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Grupo de Clientes guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveClientGroupFailed();
			}
		});
	}

	public void deleteClientGroup(final ClientGroup c) {
		if(c.id.equalsIgnoreCase("new")){
			clearNewClientGroup();
		}else{
			this.clientGroupBroker.removeClientGroup(c.id, new ResponseHandler<ClientGroup>() {

				@Override
				public void onResponse(ClientGroup response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("groupid");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Grupo de Clientes eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onDeleteClientGroupFailed();
				}
			});
		}
	}	

	private void onRefresh(){
		clientGroupBroker.requireDataRefresh();
		NavigationHistoryManager.getInstance().reload();
	}
	
	private void onGetClientGroupFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter o Grupo de Clientes seleccionado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("groupid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetClientGroupListFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de Grupos de Utilizadores"), TYPE.ALERT_NOTIFICATION));
	}

	private void onCreateClientGroupFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar o Grupo de Clientes"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onSaveClientGroupFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações ao Grupo de Clientes"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onDeleteClientGroupFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar o Grupo de Clientes"), TYPE.ALERT_NOTIFICATION));
	}

}