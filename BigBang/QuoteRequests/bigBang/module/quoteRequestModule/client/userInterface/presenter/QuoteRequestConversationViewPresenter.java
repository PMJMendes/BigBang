package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class QuoteRequestConversationViewPresenter extends ConversationViewPresenter<QuoteRequest>{

	private QuoteRequestBroker broker;
	protected QuoteRequest quote;
	
	public QuoteRequestConversationViewPresenter(
			bigBang.library.client.userInterface.presenter.ConversationViewPresenter.Display<QuoteRequest> view) {
		super(view);
		this.broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
	}

	@Override
	protected void fillOwner(String ownerId,
			final ResponseHandler<QuoteRequest> handler) {
		broker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {
			
			@Override
			public void onResponse(QuoteRequest response) {
				quote = response;
				setContacts();
				handler.onResponse(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	protected void setContacts() {
		view.addContact("Consulta (" + quote.processNumber +")", quote.id, BigBangConstants.EntityIds.QUOTE_REQUEST);
		view.addContact("Cliente (" + quote.clientName + ")", quote.clientId, BigBangConstants.EntityIds.CLIENT);
		view.addContact("Mediador (" + quote.inheritMediatorName + ")", quote.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		String quoteRequestId = parameterHolder.getParameter("quoterequestid");
		parameterHolder.setParameter("ownerid", quoteRequestId);
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.QUOTE_REQUEST);
		super.setParameters(parameterHolder);
	}

}
