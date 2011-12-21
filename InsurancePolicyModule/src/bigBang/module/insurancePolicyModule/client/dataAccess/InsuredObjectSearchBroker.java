package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;

public class InsuredObjectSearchBroker extends SearchDataBrokerImpl<InsuredObjectStub>
		implements SearchDataBroker<InsuredObjectStub> {

	public InsuredObjectSearchBroker(SearchServiceAsync service) {
		super(service);
	}

}
