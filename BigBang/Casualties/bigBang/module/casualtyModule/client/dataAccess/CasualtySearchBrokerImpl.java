package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.casualtyModule.interfaces.CasualtyService;

public class CasualtySearchBrokerImpl extends
		SearchDataBrokerImpl<CasualtyStub> implements
		SearchDataBroker<CasualtyStub> {

	public CasualtySearchBrokerImpl(){
		this(CasualtyService.Util.getInstance());
	}
	
	public CasualtySearchBrokerImpl(SearchServiceAsync service) {
		super(service);
	}

}
