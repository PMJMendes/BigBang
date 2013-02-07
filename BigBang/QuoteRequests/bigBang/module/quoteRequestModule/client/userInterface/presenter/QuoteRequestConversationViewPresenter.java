package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class QuoteRequestConversationViewPresenter extends ConversationViewPresenter<QuoteRequest>{

	public QuoteRequestConversationViewPresenter(
			bigBang.library.client.userInterface.presenter.ConversationViewPresenter.Display<QuoteRequest> view) {
		super(view);
		//TODO
	}

	@Override
	protected void fillOwner(String ownerId,
			ResponseHandler<QuoteRequest> handler) {
		// TODO Auto-generated method stub
		
	}

}
