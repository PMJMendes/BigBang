package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ReceiveMessageViewPresenter;

public class QuoteRequestReceiveMessageViewPresenter extends ReceiveMessageViewPresenter<QuoteRequest> {

	private QuoteRequestBroker broker;
	
	public QuoteRequestReceiveMessageViewPresenter(Display<QuoteRequest> view) {
		super(view);
		broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
	}

	@Override
	protected void receive() {
		broker.receiveMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {
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

	@Override
	protected void showOwner(String ownerId) {
		broker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {
			
			@Override
			public void onResponse(QuoteRequest response) {
				view.getOwnerForm().setValue(response);				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();								
			}
		});
	}

}
