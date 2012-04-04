package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequest.RequestSubLine;
import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.AsyncRequest;
import bigBang.library.client.event.AsyncRequestHandler;
import bigBang.library.client.event.FiresAsyncRequests;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CreateQuoteRequestViewPresenter implements ViewPresenter {

	public static enum Action {
		SAVE,
		CANCEL,
		CREATE_INSURED_OBJECT, CREATE_PERSON_INSURED_OBJECT, CREATE_COMPANY_INSURED_OBJECT, CREATE_EQUIPMENT_INSURED_OBJECT, CREATE_LOCATION_INSURED_OBJECT, CREATE_ANIMAL_INSURED_OBJECT
	}

	public interface Display {
		HasEditableValue<QuoteRequest> getForm();
		HasValue<Client> getOwnerForm();
		FiresAsyncRequests getFormAsFiresAsyncRequests();

		HasValueSelectables<QuoteRequestObjectStub> getObjectsList();

		void setSaveModeEnabled(boolean enabled);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected QuoteRequestBroker broker;
	protected ClientProcessBroker clientBroker;
	protected Display view;
	protected boolean bound = false;

	public CreateQuoteRequestViewPresenter(View view) {
		setView(view);
		broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		clientBroker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
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
		String ownerId = parameterHolder.getParameter("clientid");
		String requestId = parameterHolder.getParameter("quoterequestid");
		
		if(ownerId == null || ownerId.isEmpty()) {
			clearView();
			onGetQuoteRequestFailed();
		}else if(requestId != null && requestId.equalsIgnoreCase("new")) {
			openQuoteRequest();
		}else if(requestId != null && !requestId.isEmpty()){
			showCreateQuoteRequest(ownerId, requestId);
		}else{
			clearView();
			onGetQuoteRequestFailed();
		}
	}

	public void bind() {
		if(bound)
			return;

		view.registerActionHandler(new ActionInvokedEventHandler<CreateQuoteRequestViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				case CREATE_INSURED_OBJECT:
					break;
				case CREATE_PERSON_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PERSON);
					break;
				case CREATE_COMPANY_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_COMPANY);
					break;
				case CREATE_EQUIPMENT_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_EQUIPMENT);
					break;
				case CREATE_LOCATION_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_PLACE);
					break;
				case CREATE_ANIMAL_INSURED_OBJECT:
					onCreateInsuredObject(BigBangConstants.EntityIds.INSURED_OBJECT_TYPE_ANIMAL);
					break;
				default:
					break;
				}
			}
		});

		view.getFormAsFiresAsyncRequests().registerRequestHandler(new AsyncRequestHandler() {

			@Override
			public void onRequest(final AsyncRequest<Object> request) {
				String subLineId = request.getParameters().getParameter("subLineId");
				String operation = request.getParameters().getParameter("operation");

				if(operation.equalsIgnoreCase("getsublinedefinition")){
					broker.addSubLine(view.getForm().getValue().id, subLineId, new ResponseHandler<QuoteRequest.RequestSubLine>() {

						@Override
						public void onResponse(RequestSubLine response) {
							request.onResponse(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetSubLineDefinitionFailed();
							request.onError(null);
						}
					});
				}else if(operation.equalsIgnoreCase("deletesubline")){
					broker.deleteSubLine(subLineId, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							request.onResponse(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							request.onError(null);
						}
					});
				}
			}
		});

		SelectionChangedEventHandler selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				ValueSelectable<?> selected = (ValueSelectable<?>) event.getFirstSelected();
				if(selected != null) {				
					if(event.getSource() == view.getObjectsList()){
						QuoteRequestObject object = (QuoteRequestObject) selected.getValue();
						showInsuredObject(object.id);
					}
				}
			}
		};
		view.getObjectsList().addSelectionChangedEventHandler(selectionChangedHandler);

		bound = true;
	}

	protected void clearView(){
		view.getOwnerForm().setValue(null);
		view.getForm().setValue(null);
		view.setSaveModeEnabled(true);
	}

	protected void showCreateQuoteRequest(final String ownerId, String quoteRequestId) {
		if(!broker.isTemp(quoteRequestId)) {
			onGetQuoteRequestFailed();
		}else{
			broker.getQuoteRequest(quoteRequestId, new ResponseHandler<QuoteRequest>() {
				
				@Override
				public void onResponse(QuoteRequest response) {
					view.setSaveModeEnabled(true);
					view.getForm().setValue(response);
					showOwner(ownerId);
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetQuoteRequestFailed();
				}
			});
		}
	}

	protected void showOwner(String ownerId){
		clientBroker.getClient(ownerId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getOwnerForm().setValue(response);
				
				QuoteRequest value = view.getForm().getValue();
				value.clientName = response.name;
				value.clientNumber = response.clientNumber;
				value.clientId = response.id;
				
				view.getForm().setValue(value);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetQuoteRequestFailed();
			}
		});
	}
	
	protected void openQuoteRequest() {
		broker.openRequestResource(null, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("quoterequestid", response.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onOpenRequestResourceFailed();
			}
		});
	}

	protected void onSave() {
		broker.commitRequest(view.getForm().getInfo(), new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				onSaveSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveFailed();
			}
		});
	}

	protected void onCancel(){
		broker.closeRequestResource(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	protected void onCreateInsuredObject(String objectType){
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "viewinsuredobject");
		navItem.setParameter("objectid", "new");
		navItem.setParameter("type", objectType);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void showInsuredObject(String objectId) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "viewinsuredobject");
		navItem.setParameter("objectid", objectId);
		NavigationHistoryManager.getInstance().go(navItem);
	}

	protected void onGetQuoteRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Criar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("quoterequestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSaveFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Criar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSaveSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Consulta de Mercado foi Criada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("quoterequestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onOpenRequestResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Criar a Consulta de Mercado"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("quoterequestid");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onGetSubLineDefinitionFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível acrescentar a Modalidade à Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
	}

}
