package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsuredObjectStubOLD;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;

public class InsuredObjectSearchBroker extends SearchDataBrokerImpl<InsuredObjectStubOLD>
		implements SearchDataBroker<InsuredObjectStubOLD> {

	public InsuredObjectSearchBroker(SearchServiceAsync service) {
		super(service);
	}

}
