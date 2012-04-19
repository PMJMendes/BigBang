package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class SubCasualtyViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL,
		DELETE
	}

	public static interface Display {
		HasValue<Casualty> getParentForm();
		HasEditableValue<SubCasualty> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void setSaveModeEnabled(boolean enabled);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);

		Widget asWidget();
	}


	protected boolean bound = false;
	protected Display view;
	protected SubCasualtyDataBroker broker;

	public SubCasualtyViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
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
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		String subCasualtyId = parameterHolder.getParameter("subcasualtyid");
		String casualtyId = parameterHolder.getParameter("casualtyid");

		if(subCasualtyId != null && ! subCasualtyId.isEmpty()) {
			if(subCasualtyId.equalsIgnoreCase("new")) {
				showCreateSubCasualty(casualtyId);
			}else{
				showSubCasualty(subCasualtyId);
			}
		}else{
			onFailure();
		}
	}

	protected void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getParentForm().setValue(null);
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
	}

	protected void bind(){
		view.registerActionHandler(new ActionInvokedEventHandler<SubCasualtyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case EDIT:
					onEdit();
					break;
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				if(selected != null) {				
					if(event.getSource() == view.getSubProcessesList()) {
						BigBangProcess process = (BigBangProcess) selected.getValue();
						showSubProcess(process);
					}else if(event.getSource() == view.getHistoryList()) {
						HistoryItemStub historyItem = (HistoryItemStub) selected.getValue();
						showHistory(historyItem.id);
					}
				}
			}
		};

		view.getSubProcessesList().addSelectionChangedEventHandler(selectionChangedHandler);
		view.getHistoryList().addSelectionChangedEventHandler(selectionChangedHandler);
	}

	protected void showSubCasualty(String subCasualtyId){
		broker.getSubCasualty(subCasualtyId, new ResponseHandler<SubCasualty>() {

			@Override
			public void onResponse(final SubCasualty subCasualty) {
				CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
				casualtyBroker.getCasualty(subCasualty.casualtyId, new ResponseHandler<Casualty>() {

					@Override
					public void onResponse(Casualty casualty) {
						view.getParentForm().setValue(casualty);
						view.getForm().setValue(subCasualty);

						//TODO PERMISSIONS
						view.allowEdit(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.UPDATE_SUB_CASUALTY));
						view.allowDelete(PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.DELETE_SUB_CASUALTY));

						view.getForm().setReadOnly(true);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onFailure();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetSubCasualtyFailed();
			}
		});
	}

	protected void showCreateSubCasualty(String parentId){
		CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
		casualtyBroker.getCasualty(parentId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty casualty) {
				view.getParentForm().setValue(casualty);

				SubCasualty subCasualty = new SubCasualty();
				subCasualty.casualtyId = casualty.id;

				view.getForm().setValue(subCasualty);

				//TODO PERMISSIONS
				view.allowEdit(true);
				view.allowDelete(true);

				view.getForm().setReadOnly(false);
				view.setSaveModeEnabled(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onFailure();
			}
		});
	}

	protected void onEdit(){
		view.setSaveModeEnabled(true);
		view.getForm().setReadOnly(false);
	}

	protected void onSave() {
		SubCasualty subCasualty = view.getForm().getInfo();

		if(subCasualty.id == null) {
			CasualtyDataBroker casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
			casualtyBroker.createSubCasualty(subCasualty, new ResponseHandler<SubCasualty>(){

				@Override
				public void onResponse(SubCasualty response) {
					onCreateSubCasualtySuccess(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onCreateSubCasualtyFailed();
				}
			});
		}else{
			broker.updateSubCasualty(subCasualty, new ResponseHandler<SubCasualty>() {

				@Override
				public void onResponse(SubCasualty response) {
					onSaveSuccess();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onSaveFailed();
				}
			});
		}
	}

	protected void onCancel(){
		SubCasualty subCasualty = view.getForm().getValue();
		
		if(subCasualty.id == null) {
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.removeParameter("subcasualtyid");
			item.popFromStackParameter("display");
			NavigationHistoryManager.getInstance().go(item);
		}else{
			NavigationHistoryManager.getInstance().reload();
		}
	}

	protected void onDelete(){
		SubCasualty subCasualty = view.getForm().getValue();
		
		if(subCasualty.id == null) {
			onCancel();
		}else{
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.setParameter("show", "deletesubcasualty");
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	protected void showSubProcess(BigBangProcess process){
//		String type = process.dataTypeId;

		//TODO
//		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
//			showNegotiation(process.dataId);
//		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)) {
//			showInfoRequest(process.dataId);
//		}
	}

	protected void showHistory(String historyItemId){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", view.getForm().getValue().id);
		navItem.setParameter("historyItemId", historyItemId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Guardado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onCreateSubCasualtySuccess(SubCasualty subCasualty){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sub-Sinistro Criado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("subcasualtyid", subCasualty.id);
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onCreateSubCasualtyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Guardar o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onGetSubCasualtyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sub-Sinistro"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subcasualtyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
