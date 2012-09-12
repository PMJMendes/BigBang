package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyObjectService;

public class InsuredObjectSearchBroker extends SearchDataBrokerImpl<InsuredObjectStub>
		implements SearchDataBroker<InsuredObjectStub> {

	public InsuredObjectSearchBroker(SearchServiceAsync service) {
		super(service);
	}
	
	public InsuredObjectSearchBroker(){
		this(PolicyObjectService.Util.getInstance());
	}

}
