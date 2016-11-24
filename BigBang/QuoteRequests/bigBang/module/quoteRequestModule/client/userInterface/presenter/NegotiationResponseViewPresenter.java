package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Response;
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
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.interfaces.MailService;
import bigBang.library.interfaces.MailServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class NegotiationResponseViewPresenter implements ViewPresenter {

	private NegotiationBroker broker;
	private Display view;
	private boolean bound;
//	private int counter = 0;
	MailServiceAsync mailService;
	
	public enum Action{
		NEGOTIATION_RESPONSE,
		CANCEL
	}
	
	public interface Display{
		
		Widget asWidget();
		
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		
		HasEditableValue<Response> getForm();
		
		void clear();
		
	}
	
	public NegotiationResponseViewPresenter(Display view){
		this.broker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
		mailService = MailService.Util.getInstance();
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
	}

	private void bind() {
		if(bound){
			return;
		}
		
		view.registerActionHandler(new ActionInvokedEventHandler<NegotiationResponseViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:{
					onCancel();
					break;
				}
				case NEGOTIATION_RESPONSE:{
					onResponse();
					break;
				}
				}
				
			}
		});
		bound = true;
	}

	protected void onResponse(Response toSend) {
		broker.receiveResponse(toSend, new ResponseHandler<Negotiation>() {
		
			@Override
			public void onResponse(Negotiation response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Resposta recebida com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a resposta."), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.clear();
		Response response = new Response();
		response.negotiationId = parameterHolder.getParameter("negotiationid");
		view.getForm().setValue(response);
		
	}
	
	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onResponse() {
		final Response toSend = view.getForm().getInfo();
		onResponse(toSend);
		
//		counter = 0;
//		if(toSend.message.upgrades != null && toSend.message.upgrades.length > 0){
//			for(int i = 0; i<toSend.message.upgrades.length; i++){
//
//				exchangeService.getAttachment(toSend.message.emailId, toSend.message.upgrades[i].attachmentId, new BigBangAsyncCallback<Attachment>() {
//
//
//
//
//					public void onResponseSuccess(Attachment result) {
//
//						for(int k = 0; k<toSend.message.upgrades.length; k++){
//							if(toSend.message.upgrades[k].attachmentId.equals(result.id)){
//								toSend.message.upgrades[k].storageId = result.storageId;
//								break;
//							}
//						}
//
//						counter++;
//						if(counter == toSend.message.upgrades.length){
//							
//							onResponse(toSend);
//
//						}
//
//					};
//
//
//					public void onResponseFailure(Throwable caught) {
//						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a resposta ao pedido."), TYPE.ALERT_NOTIFICATION));
//						super.onResponseFailure(caught);
//
//					};
//				});
//
//			}
//
//
//		}
//
//		else{
//			onResponse(toSend);
//		}
	}

}
