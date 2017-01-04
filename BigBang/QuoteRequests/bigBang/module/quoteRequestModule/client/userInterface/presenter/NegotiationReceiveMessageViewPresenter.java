package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.NegotiationBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class NegotiationReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<Negotiation>{

	private NegotiationBroker negotiationBroker;

	public NegotiationReceiveMessageViewPresenter(Display<Negotiation> view) {
		super(view);
		negotiationBroker = (NegotiationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.NEGOTIATION);
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
				String error = "";
				for (ResponseError tmp : errors) {
					error = error + tmp.description;
				}
				onReceiveMessageFailed(error);;
			}
		});		
	}

}
