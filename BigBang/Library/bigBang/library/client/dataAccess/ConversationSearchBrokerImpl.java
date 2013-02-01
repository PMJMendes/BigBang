package bigBang.library.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.ConversationStub;
import bigBang.library.interfaces.ConversationService;
import bigBang.library.interfaces.SearchServiceAsync;

public class ConversationSearchBrokerImpl extends SearchDataBrokerImpl<ConversationStub> implements
		SearchDataBroker<ConversationStub> {

	public ConversationSearchBrokerImpl(SearchServiceAsync service) {
		super(service);
	}

	public ConversationSearchBrokerImpl(){
		this(ConversationService.Util.getInstance());
	}
}
