package bigBang.module.quoteRequestModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.QuoteRequestObjectStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestObjectService;

public class QuoteRequestObjectSearchBroker extends
		SearchDataBrokerImpl<QuoteRequestObjectStub> implements
		SearchDataBroker<QuoteRequestObjectStub> {

	public QuoteRequestObjectSearchBroker(){
		this(QuoteRequestObjectService.Util.getInstance());
	}
	
	public QuoteRequestObjectSearchBroker(SearchServiceAsync service) {
		super(service);
	}

}
