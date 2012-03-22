package bigBang.module.quoteRequestModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;

public class QuoteRequestSearchDataBroker extends SearchDataBrokerImpl<QuoteRequestStub>
		implements SearchDataBroker<QuoteRequestStub> {

	public QuoteRequestSearchDataBroker() {
		this(QuoteRequestService.Util.getInstance());
	}
	
	public QuoteRequestSearchDataBroker(SearchServiceAsync service) {
		super(service);
	}
}
