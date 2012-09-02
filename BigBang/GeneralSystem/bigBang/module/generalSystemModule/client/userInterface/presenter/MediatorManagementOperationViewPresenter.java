package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.MediatorBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Mediator;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
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


public class MediatorManagementOperationViewPresenter implements ViewPresenter {

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
		void removeFromList(ValueSelectable<Mediator> selectable);
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();

		//Form
		HasEditableValue<Mediator> getForm();
		boolean isFormValid();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewMediator(Mediator mediator);

		//PERMISSIONS
		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	private Display view;
	protected MediatorBroker mediatorBroker;
	private boolean bound = false;

	public MediatorManagementOperationViewPresenter(View view){
		this.mediatorBroker = (MediatorBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.MEDIATOR);
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
		setup();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String mediatorId = parameterHolder.getParameter("mediatorid");
		mediatorId = mediatorId == null ? new String() : mediatorId;

		if(inMediatorCreation()){
			clearNewMediator();
		}

		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageMediators);
		view.allowCreate(hasPermissions);
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);

		if(mediatorId.isEmpty()){
			clearView();
		}else if(mediatorId.equalsIgnoreCase("new")){
			setupNewMediator();
		}else{
			showMediator(mediatorId);
		}
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
				String mediatorId = selectedValue == null ? null : selectedValue.id;
				mediatorId = mediatorId == null ? new String() : mediatorId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(mediatorId.isEmpty()){
					item.removeParameter("mediatorid");
				}else{
					item.setParameter("mediatorid", mediatorId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<MediatorManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.setParameter("mediatorid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					deleteMediator(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(view.getForm().validate()) {
						Mediator info = view.getForm().getInfo();
						view.getForm().setReadOnly(true);
						if(info.id.equalsIgnoreCase("new"))
							createMediator(info);
						else
							saveMediator(info);
					}
					break;
				case CANCEL_EDIT:
					if(inMediatorCreation()){
						deleteMediator(view.getForm().getValue());
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

		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Contact> selected = (ValueSelectable<Contact>) event.getFirstSelected();
				Contact item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "contactmanagement");
					navItem.setParameter("ownerid", view.getForm().getValue().id);
					navItem.setParameter("contactid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Document> selected = (ValueSelectable<Document>) event.getFirstSelected();
				Document item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "documentmanagement");
					navItem.setParameter("ownerid", item.ownerId);
					navItem.setParameter("documentid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		bound = true;
	}

	private void setup(){
		this.mediatorBroker.requireDataRefresh();
		this.mediatorBroker.getMediators(new ResponseHandler<Mediator[]>() {

			@Override
			public void onResponse(Mediator[] response) {
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetMediatorListFailed();
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

	private void setupNewMediator(){
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageMediators);
		if(hasPermissions){
			Mediator mediator = new Mediator();
			mediator.id = "new";
			mediator.name = "Novo Mediador";
			view.getList().clearSelection();
			view.prepareNewMediator(mediator);
			view.getForm().setValue(mediator);

			view.allowDelete(hasPermissions);
			view.allowEdit(hasPermissions);
			view.setSaveModeEnabled(hasPermissions);
			view.getForm().setReadOnly(!hasPermissions);
		}else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearNewMediator(){
		if(inMediatorCreation()){
			for(ValueSelectable<Mediator> selected : view.getList().getAll()){
				Mediator mediator = selected.getValue();
				if(mediator == null || mediator.id.equalsIgnoreCase("new")){
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


	private boolean inMediatorCreation(){
		for(ValueSelectable<Mediator> selected : view.getList().getAll()){
			Mediator mediator = selected.getValue();
			if(mediator == null || mediator.id.equalsIgnoreCase("new")){
				return true;
			}
		}
		return false;
	}

	private void showMediator(String mediatorId){
		//Selects the mediator in list
		for(ValueSelectable<Mediator> entry : view.getList().getAll()){
			Mediator listMediator = entry.getValue();
			if(listMediator.id.equalsIgnoreCase(mediatorId) && !entry.isSelected()){
				entry.setSelected(true, true);
			}
		}
		//Gets the user to show
		this.mediatorBroker.getMediator(mediatorId, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				view.clearAllowedPermissions();

				boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageMediators);
				view.allowEdit(hasPermissions);
				view.allowDelete(hasPermissions);

				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetMediatorFailed();
			}
		});
	}

	public void createMediator(Mediator c) {
		c.id = null;
		this.mediatorBroker.addMediator(c, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("mediatorid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mediador criado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateMediatorFailed();
			}
		});
	}

	public void saveMediator(Mediator c) {
		this.mediatorBroker.updateMediator(c, new ResponseHandler<Mediator>() {

			@Override
			public void onResponse(Mediator response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("mediatorid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mediador guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveMediatorFailed();
			}
		});
	}

	public void deleteMediator(final Mediator c) {
		if(c.id.equalsIgnoreCase("new")){
			clearNewMediator();
		}else{
			this.mediatorBroker.removeMediator(c.id, new ResponseHandler<Mediator>() {

				@Override
				public void onResponse(Mediator response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("mediatorid");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mediador eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onDeleteMediatorFailed();
				}
			});
		}
	}	

	private void onRefresh(){
		mediatorBroker.requireDataRefresh();
		mediatorBroker.getMediators(new ResponseHandler<Mediator[]>() {

			@Override
			public void onResponse(Mediator[] response) {
				return;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}

	private void onGetMediatorFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter o mediador seleccionado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("mediatorid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetMediatorListFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de mediadores"), TYPE.ALERT_NOTIFICATION));
	}

	private void onCreateMediatorFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar o mediador"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onSaveMediatorFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações ao mediador"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onDeleteMediatorFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar o mediador"), TYPE.ALERT_NOTIFICATION));
	}

}
