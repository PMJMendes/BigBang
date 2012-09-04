package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.module.insurancePolicyModule.interfaces.Policy2Service;
import bigBang.module.insurancePolicyModule.interfaces.Policy2ServiceAsync;

public class InsurancePolicySearchDataBroker extends
		SearchDataBrokerImpl<InsurancePolicyStub>  implements SearchDataBroker<InsurancePolicyStub>{

	public InsurancePolicySearchDataBroker() {
		this(Policy2Service.Util.getInstance());
	}
	
	public InsurancePolicySearchDataBroker(Policy2ServiceAsync service){
		super(service);
	}

}
