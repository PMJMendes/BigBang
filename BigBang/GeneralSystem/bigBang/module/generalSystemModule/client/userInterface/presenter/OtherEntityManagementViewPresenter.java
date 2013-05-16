package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.OtherEntityBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.OtherEntity;
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
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class OtherEntityManagementViewPresenter implements ViewPresenter {

	public static enum Action{

		NEW,
		REFRESH,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE
	}

	public interface Display{
		HasValueSelectables<OtherEntity> getList();
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();

		HasEditableValue<OtherEntity> getForm();

		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);

		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
		void prepareNewOtherEntity(OtherEntity newEntity);
		void removeFromList(ValueSelectable<OtherEntity> selected);
	}

	protected boolean bound = false;
	protected Display view;
	protected OtherEntityBroker broker;
	private boolean inCreation;
	private boolean hasPermissions;

	public OtherEntityManagementViewPresenter(Display view) {
		broker = ((OtherEntityBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.OTHER_ENTITY));
		setView((UIObject)view);
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

	private void setup() {
		onRefresh();
	}

	protected void onGetListFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de entidades"), TYPE.ALERT_NOTIFICATION));

	}

	private void bind() {
		if(bound) return;

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {

				@SuppressWarnings("unchecked")
				ValueSelectable<OtherEntity> selected = (ValueSelectable<OtherEntity>) event.getFirstSelected();

				OtherEntity selectedValue = selected == null? null : selected.getValue();
				String otherEntityId = selectedValue == null? "" : selectedValue.id;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();

				if(otherEntityId.isEmpty()){
					item.removeParameter("otherentityid");
				}else{
					item.setParameter("otherentityid", otherEntityId);
				}

				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<OtherEntityManagementViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.setParameter("otherentityid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					deleteOtherEntityId(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(view.getForm().validate()){
						OtherEntity entity = view.getForm().getInfo();
						if(entity.id.equalsIgnoreCase("new")){
							createOtherEntity(entity);
						}
						else{
							saveOtherEntity(entity);
						}
					}else{
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
					}
					break;
				case CANCEL_EDIT:
					if(inCreation){
						deleteOtherEntity(view.getForm().getValue());
					}
					else{
						NavigationHistoryManager.getInstance().reload();
					}
					break;
				case REFRESH:
					onRefresh();
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
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.OTHER_ENTITY);
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
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.OTHER_ENTITY);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		bound = true;
	}

	protected void onRefresh() {

		broker.requireDataRefresh();
		broker.getOtherEntities(new ResponseHandler<OtherEntity[]>() {

			@Override
			public void onResponse(OtherEntity[] response) {
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetListFailed();
			}
		});
	}

	protected void deleteOtherEntity(OtherEntity value) {
		if(value.id.equalsIgnoreCase("new")){
			clearNewOtherEntity();
		}else{
			broker.removeOtherEntity(value.id, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("otherentityid");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Entidade eliminada com sucesso."), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onDeleteFailed();
				}
			});
		}
	}



	protected void onDeleteFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar a entidade"), TYPE.ALERT_NOTIFICATION));
	}

	protected void saveOtherEntity(OtherEntity entity) {
		broker.saveOtherEntity(entity, new ResponseHandler<OtherEntity>() {

			@Override
			public void onResponse(OtherEntity response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("otherentityid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Entidade guardada com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveOtherEntityFailed();
			}
		});
	}

	protected void onSaveOtherEntityFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível gravar a entidade"), TYPE.ALERT_NOTIFICATION));

	}

	protected void createOtherEntity(OtherEntity entity) {
		broker.createOtherEntity(entity, new ResponseHandler<OtherEntity>() {

			@Override
			public void onResponse(OtherEntity response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("otherentityid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				clearNewOtherEntity();
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Entidade criada com sucesso."), TYPE.TRAY_NOTIFICATION));				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateOtherEntityFailed();
			}
		});
	}

	protected void onCreateOtherEntityFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar a entidade"), TYPE.ALERT_NOTIFICATION));

	}

	protected void deleteOtherEntityId(OtherEntity value) {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar a entidade"), TYPE.ALERT_NOTIFICATION));

	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String otherEntityId = parameterHolder.getParameter("otherentityid");
		otherEntityId = otherEntityId == null ? new String() : otherEntityId;

		if(inCreation){
			clearNewOtherEntity();
		}

		hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.MANAGE_OTHER_ENTITIES);

		view.allowCreate(hasPermissions);
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);

		if(otherEntityId.isEmpty()){
			clearView();
		}else if(otherEntityId.equalsIgnoreCase("new")){
			setupNewOtherEntity();
		}
		else{
			showOtherEntity(otherEntityId);
		}

	}

	private void showOtherEntity(String otherEntityId) {
		for(ValueSelectable<OtherEntity> entry : view.getList().getAll()){
			OtherEntity entity = entry.getValue();
			if(entity.id.equalsIgnoreCase(otherEntityId) && !entry.isSelected()){
				entry.setSelected(true, true);
				break;
			}
		}

		broker.getOtherEntity(otherEntityId, new ResponseHandler<OtherEntity>() {

			@Override
			public void onResponse(OtherEntity response) {

				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOtherEntityFailed();
			}
		});
	}

	protected void onGetOtherEntityFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a entidade"), TYPE.ALERT_NOTIFICATION));

	}

	private void setupNewOtherEntity() {

		if(hasPermissions){
			inCreation = true;
			OtherEntity newEntity = new OtherEntity();
			newEntity.id = "new";
			newEntity.name = "Nova Entidade";
			
			view.getList().clearSelection();
			view.prepareNewOtherEntity(newEntity);
			view.getForm().setValue(newEntity);
			view.setSaveModeEnabled(true);
			view.getForm().setReadOnly(false);
		}
		else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearView() {
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	private void clearNewOtherEntity() {
		if(inCreation){
			for(ValueSelectable<OtherEntity> selected : view.getList().getAll()){
				OtherEntity entity = selected.getValue();
				if(entity == null || entity.id.equalsIgnoreCase("new")){
					view.removeFromList(selected);
					break;
				}
			}
			inCreation = false;
			clearView();
		}
	}

}
