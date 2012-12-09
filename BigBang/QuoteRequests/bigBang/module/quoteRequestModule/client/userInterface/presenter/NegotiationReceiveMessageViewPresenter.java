package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class NegotiationReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<Negotiation>{

	private NegotiationBroker negotiationBroker;

	public NegotiationReceiveMessageViewPresenter(Display<Negotiation> view) {
		super(view);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
	}

	@Override
	public void setParameters(final HasParameters parameterHolder) {
		
		ownerId = parameterHolder.getParameter("negotiationId");
		ownerTypeId = BigBangConstants.EntityIds.NEGOTIATION;
		
		negotiationBroker.getNegotiation(ownerId, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				view.getOwnerForm().setValue(response);	
				setParentParameters(parameterHolder);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar a negociação."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
		
	}

	protected void setParentParameters(HasParameters parameterHolder) {
		super.setParameters(parameterHolder);
		
	}

	@Override
	protected void showOwner(String ownerId) {
		negotiationBroker.getNegotiation(ownerId, new ResponseHandler<Negotiation>() {
			
			@Override
			public void onResponse(Negotiation response) {
				view.getOwnerForm().setValue(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}
	
	@Override
	protected void receive() {
		negotiationBroker.receiveMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onReceiveMessageSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onReceiveMessageFailed();
			}
		});		
	}

}
