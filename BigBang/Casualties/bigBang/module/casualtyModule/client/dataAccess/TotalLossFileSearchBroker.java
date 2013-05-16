package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.TotalLossFileStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.casualtyModule.interfaces.TotalLossService;

public class TotalLossFileSearchBroker extends SearchDataBrokerImpl<TotalLossFileStub> implements SearchDataBroker<TotalLossFileStub> {

	public TotalLossFileSearchBroker(SearchServiceAsync service) {
		super(service);
	}
	
	public TotalLossFileSearchBroker(){
		this(TotalLossService.Util.getInstance());
	}

}
