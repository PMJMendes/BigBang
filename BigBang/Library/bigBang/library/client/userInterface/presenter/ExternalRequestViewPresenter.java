package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;
import bigBang.library.shared.Attachment;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ExternalRequestViewPresenter implements ViewPresenter{

	protected String ownerId;
	protected String ownerTypeId;
	private String externalRequestId;
	private ExternRequestServiceAsync service;
	private NegotiationBroker broker;
	private ExchangeServiceAsync exchangeService;
	private int counter;

	public static enum Action{
		CANCEL,
		CONFIRM
	}

	public static interface Display{
		Widget asWidget();
		HasValue<ProcessBase> getOwnerForm();
		HasEditableValue<ExternalInfoRequest> getForm();
		void setToolbarSaveMode(boolean b);
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

	}

	@Override
	public void setParameters(final HasParameters parameterHolder){

		externalRequestId = parameterHolder.getParameter("externalrequestid");


		if(externalRequestId == null){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o pedido de informação externo."), TYPE.ALERT_NOTIFICATION));
			view.getForm().setReadOnly(true);
		}
		else if(externalRequestId.equalsIgnoreCase("new")){
			ExternalInfoRequest externalRequest = new ExternalInfoRequest();
			view.getForm().setInfo(externalRequest);
			view.getForm().setReadOnly(false);
			view.setToolbarSaveMode(true);
		}
		else{
			service.getRequest(externalRequestId, new BigBangAsyncCallback<ExternalInfoRequest>() {
				@Override
				public void onResponseSuccess(ExternalInfoRequest result) {
					view.getForm().setValue(result);
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					super.onResponseFailure(caught);
				}

			});
		}
	}


	protected Display view;
	protected boolean bound = false;


	public ExternalRequestViewPresenter(Display view){
		setView((UIObject)view);
		service = ExternRequestService.Util.getInstance();
		broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		exchangeService = ExchangeService.Util.getInstance();
	}

	@Override
	public void setView(UIObject view){
		this.view = (Display)view;
	}
	@Override
	public void go(HasWidgets container){
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind(){
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){



			@Override
			public void onActionInvoked(
					ActionInvokedEvent<Action> action) {

				switch(action.getAction()){

				case CANCEL:{

					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.popFromStackParameter("display");
					navig.removeParameter("externalrequestid");	
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}
				case CONFIRM:{

					ExternalInfoRequest toSend = view.getForm().getInfo();
					saveExternalInformationRequest(toSend);
				}


				}


			}

		});

	}

	protected void saveExternalInformationRequest(final ExternalInfoRequest toSend) {
		toSend.parentDataObjectId = ownerId;
		toSend.parentDataTypeId = ownerTypeId;
		
		counter = 0;

		if(toSend.message.upgrades != null && toSend.message.upgrades.length > 0){
			for(int i = 0; i<toSend.message.upgrades.length; i++){

				exchangeService.getAttachment(toSend.message.emailId, toSend.message.upgrades[i].attachmentId, new BigBangAsyncCallback<Attachment>() {

					public void onResponseSuccess(Attachment result) {

						for(int k = 0; k<toSend.message.upgrades.length; k++){
							if(toSend.message.upgrades[k].attachmentId.equals(result.id)){
								toSend.message.upgrades[k].storageId = result.storageId;
								break;
							}
						}

						counter++;
						if(counter == toSend.message.upgrades.length){
							
							createExternalInfoRequest(toSend);

						}

					};


					public void onResponseFailure(Throwable caught) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a resposta ao pedido."), TYPE.ALERT_NOTIFICATION));
						super.onResponseFailure(caught);

					};
				});

			}


		}

		else{
			createExternalInfoRequest(toSend);
		}
	}

	protected void createExternalInfoRequest(ExternalInfoRequest toSend) {

		broker.createExternalInfoRequest(toSend, new ResponseHandler<ExternalInfoRequest>() {

			@Override
			public void onResponse(ExternalInfoRequest response) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Resposta ao pedido guardada com sucesso."), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
				navig.popFromStackParameter("display");
				navig.removeParameter("externalrequestid");	
				NavigationHistoryManager.getInstance().go(navig);
				counter = 0;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a resposta ao pedido."), TYPE.ALERT_NOTIFICATION));
				counter = 0;
			}
		});	
	}
}
