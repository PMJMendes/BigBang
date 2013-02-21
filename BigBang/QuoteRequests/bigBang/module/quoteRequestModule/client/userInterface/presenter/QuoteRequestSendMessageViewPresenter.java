package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;

public class QuoteRequestSendMessageViewPresenter extends SendMessageViewPresenter<QuoteRequest> {

	private QuoteRequestBroker broker;
	protected QuoteRequest quote;

	public QuoteRequestSendMessageViewPresenter(
			bigBang.library.client.userInterface.presenter.SendMessageViewPresenter.Display<QuoteRequest> view) {
		super(view);
		broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.QUOTE_REQUEST);
		super.setParameters(parameterHolder);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<QuoteRequest> responseHandler) {
		broker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				quote = response;
				setContacts();
				responseHandler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetContactsFailed();
			}
		});		
	}

	protected void setContacts() {
		view.addContact("Consulta (" + quote.processNumber +")", quote.id, BigBangConstants.EntityIds.QUOTE_REQUEST);
		view.addContact("Cliente (" + quote.clientName + ")", quote.clientId, BigBangConstants.EntityIds.CLIENT);
		view.addContact("Mediador (" + quote.inheritMediatorName + ")", quote.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
	}

	@Override
	protected void send() {

		broker.sendMessage(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				onSendRequestSuccess();
				navigateBack();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendRequestFailed();
			}
		});	
	}

}
