package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestObjectDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class QuoteRequestObjectViewPresenter implements ViewPresenter {

	public static interface Display{
		HasEditableValue<QuoteRequestObject> getQuoteRequestObjectForm();
		HasEditableValue<QuoteRequest> getQuoteRequestForm();

		//PERMISSIONS
		void setSaveModeEnabled(boolean enabled);
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);

		void registerActionHandler(ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		Widget asWidget();
	}

	public static enum Action{
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE
	}

	protected Display view;
	protected boolean bound = false;
	protected QuoteRequestObjectDataBroker broker;
	protected QuoteRequestBroker quoteRequestBroker;
	protected String ownerId;

	public QuoteRequestObjectViewPresenter(Display display){
		broker = (QuoteRequestObjectDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST_INSURED_OBJECT);
		quoteRequestBroker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		setView((UIObject) display);		
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
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.ownerId = parameterHolder.getParameter("quoterequestid");
		ownerId = ownerId == null ? new String() : ownerId;
		String objectId = parameterHolder.getParameter("objectid");
		objectId = objectId == null ? new String() : objectId;
		String type = parameterHolder.getParameter("type");

		if(ownerId.isEmpty()){
			clearView();
			onGetOwnerFailed();
		}else if(objectId.isEmpty()){
			clearView();
			onGetObjectFailed();
		}else if(objectId.equalsIgnoreCase("new")){
			showCreateObject(this.ownerId, type);
		}else{
			showObject(objectId);
		}
	}

	public void bind() {
		if(bound){return;}
		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					QuoteRequestObject value = view.getQuoteRequestObjectForm().getInfo();
					saveObject(value);
					break;
				case EDIT:
					onEdit();
					break;
				case CANCEL_EDIT:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	private void clearView(){
		view.clearAllowedPermissions();
		view.setSaveModeEnabled(false);
		view.getQuoteRequestForm().setValue(null);
		view.getQuoteRequestForm().setReadOnly(true);
		view.getQuoteRequestObjectForm().setValue(null);
		view.getQuoteRequestObjectForm().setReadOnly(true);
	}

	private void onEdit(){
		String objectId = view.getQuoteRequestObjectForm().getValue().id;
		broker.getQuoteRequestObject(objectId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(final QuoteRequestObject object) {
				quoteRequestBroker.getQuoteRequest(object.ownerId, new ResponseHandler<QuoteRequest>() {

					@Override
					public void onResponse(QuoteRequest request) {
						if(PermissionChecker.hasPermission(request, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST) || quoteRequestBroker.isTemp(request.id)){
							if(!quoteRequestBroker.isTemp(request.id)){
								quoteRequestBroker.openRequestResource(request.id, new ResponseHandler<QuoteRequest>() { //TODO

									@Override
									public void onResponse(
											QuoteRequest response) {
										broker.getQuoteRequestObject(object.id, new ResponseHandler<QuoteRequestObject>() {

											@Override
											public void onResponse(
													QuoteRequestObject response) {
												view.getQuoteRequestObjectForm().setValue(response);
												view.setSaveModeEnabled(true);
												view.getQuoteRequestObjectForm().setReadOnly(false);
												view.allowEdit(true);
												view.allowDelete(true);
											}

											@Override
											public void onError(
													Collection<ResponseError> errors) {
												onGetObjectFailed();
											}
										});
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onOpenResourceFailed();
									}
								});
							}else{
								broker.getQuoteRequestObject(object.id, new ResponseHandler<QuoteRequestObject>() {

									@Override
									public void onResponse(
											QuoteRequestObject response) {
										view.getQuoteRequestObjectForm().setValue(response);
										view.setSaveModeEnabled(true);
										view.getQuoteRequestObjectForm().setReadOnly(false);
										view.allowEdit(true);
										view.allowDelete(true);
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onGetObjectFailed();
									}
								});
							}
						}else {
							onUserLacksEditPermission();
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetObjectFailed();
			}
		});
	}

	private void onDelete(){
		final String objectId = view.getQuoteRequestObjectForm().getValue().id;
		broker.getQuoteRequestObject(objectId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(final QuoteRequestObject object) {
				quoteRequestBroker.getQuoteRequest(object.ownerId, new ResponseHandler<QuoteRequest>() {

					@Override
					public void onResponse(QuoteRequest request) {
						if(PermissionChecker.hasPermission(request, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST) || quoteRequestBroker.isTemp(request.id)){
							if(!quoteRequestBroker.isTemp(request.id)){
								quoteRequestBroker.openRequestResource(request.id, new ResponseHandler<QuoteRequest>() { //TODO

									@Override
									public void onResponse(
											QuoteRequest response) {
										broker.deleteQuoteRequestObject(objectId, new ResponseHandler<Void>() {

											@Override
											public void onResponse(Void response) {
												onDeleteSuccess();
											}

											@Override
											public void onError(
													Collection<ResponseError> errors) {
												onDeleteFailed();
											}
										});
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onOpenResourceFailed();
									}
								});
							}else{
								broker.deleteQuoteRequestObject(objectId, new ResponseHandler<Void>() {

									@Override
									public void onResponse(Void response) {
										onDeleteSuccess();
									}

									@Override
									public void onError(
											Collection<ResponseError> errors) {
										onDeleteFailed();
									}
								});
							}
						}else {
							onUserLacksEditPermission();
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetObjectFailed();
			}
		});
	}

	private void showObject(String objectId){
		broker.getQuoteRequestObject(objectId, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(final QuoteRequestObject object) {
				quoteRequestBroker.getQuoteRequest(object.ownerId, new ResponseHandler<QuoteRequest>() {

					@Override
					public void onResponse(QuoteRequest request) {
						view.getQuoteRequestObjectForm().setReadOnly(true);
						view.setSaveModeEnabled(false);
						boolean hasPermissions = PermissionChecker.hasPermission(request, BigBangConstants.OperationIds.QuoteRequestProcess.UPDATE_QUOTE_REQUEST) ||
								quoteRequestBroker.isTemp(request.id);
						view.allowEdit(hasPermissions);
						view.allowDelete(hasPermissions);
						view.getQuoteRequestForm().setValue(request);
						view.getQuoteRequestObjectForm().setValue(object);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetObjectFailed();
			}
		});
	}

	private void showCreateObject(final String ownerId, final String type) {
		quoteRequestBroker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(final QuoteRequest request) {
				boolean hasPermissions = true; //PermissionChecker.hasPermission(policy, BigBangConstants.OperationIds.QuoteRequestProcess.INCLUDE_INSURED_OBJECT);

				if(hasPermissions){
					if(quoteRequestBroker.isTemp(ownerId)){
						broker.createQuoteRequestObject(ownerId, type, new ResponseHandler<QuoteRequestObject>() {

							@Override
							public void onResponse(QuoteRequestObject object) {
								view.getQuoteRequestObjectForm().setReadOnly(false);
								view.setSaveModeEnabled(true);
								view.allowEdit(true);
								view.allowDelete(true);
								view.getQuoteRequestForm().setValue(request);
								view.getQuoteRequestObjectForm().setValue(object);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onCreateObjectFailed();
							}
						});
					}else{
						quoteRequestBroker.openRequestResource(ownerId, new ResponseHandler<QuoteRequest>() {

							@Override
							public void onResponse(QuoteRequest tempRequest) {
								broker.createQuoteRequestObject(tempRequest.id, type, new ResponseHandler<QuoteRequestObject>() {

									@Override
									public void onResponse(QuoteRequestObject object) {
										view.getQuoteRequestObjectForm().setReadOnly(false);
										view.setSaveModeEnabled(true);
										view.allowEdit(true);
										view.allowDelete(true);
										view.getQuoteRequestForm().setValue(request);
										view.getQuoteRequestObjectForm().setValue(object);
									}

									@Override
									public void onError(Collection<ResponseError> errors) {
										onCreateObjectFailed();
									}
								});
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onOpenResourceFailed();
							}
						});
					}
				}else{
					onUserLacksCreatePermission();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	protected void saveObject(QuoteRequestObject object) {
		broker.updateQuoteRequestObject(object, new ResponseHandler<QuoteRequestObject>() {

			@Override
			public void onResponse(QuoteRequestObject response) {
				onSaveObjectSucces();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveObjectFailed();
			}
		});
	}

	protected void onCancel(){
		if(this.quoteRequestBroker.isTemp(this.ownerId)) {
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.popFromStackParameter("display");
			item.removeParameter("objectid");
			item.removeParameter("type");
			NavigationHistoryManager.getInstance().go(item);
		}else{
			NavigationHistoryManager.Util.getInstance().reload();
		}
	}

	private void onSaveObjectSucces(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Unidade de Risco guardada no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		item.removeParameter("type");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Consulta de Mercado a que pertence a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		item.removeParameter("type");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		item.removeParameter("type");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCreateObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar a Unidade de Risco no espaço de trabalho"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		item.removeParameter("type");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSaveObjectFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a Unidade de Risco no espaço de trabalho"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksCreatePermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		item.removeParameter("type");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onOpenResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar Consulta de Mercado, assim como as respectivas unidades de risco"), TYPE.ALERT_NOTIFICATION));
	}

	private void onUserLacksEditPermission(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível editar Consulta de Mercado. Não tem as permissões necessárias"), TYPE.ALERT_NOTIFICATION));
	}

	private void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Unidade de Risco foi eliminada no espaço de trabalho"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("objectid");
		item.removeParameter("type");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onDeleteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a Unidade de Risco"), TYPE.ALERT_NOTIFICATION));
	}

}
