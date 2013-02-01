package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.casualtyModule.interfaces.SubCasualtyService;

public class SubCasualtySearchDataBroker extends
		SearchDataBrokerImpl<SubCasualtyStub> implements
		SearchDataBroker<SubCasualtyStub> {

	public SubCasualtySearchDataBroker(){
		this(SubCasualtyService.Util.getInstance());
	}
	
	public SubCasualtySearchDataBroker(SearchServiceAsync service) {
		super(service);
	}

}
