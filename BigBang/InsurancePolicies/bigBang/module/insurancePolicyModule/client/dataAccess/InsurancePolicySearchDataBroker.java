package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.Policy2Stub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;

public class InsurancePolicySearchDataBroker extends
		SearchDataBrokerImpl<Policy2Stub>  implements SearchDataBroker<Policy2Stub>{

	public InsurancePolicySearchDataBroker() {
		this(InsurancePolicyService.Util.getInstance());
	}
	
	public InsurancePolicySearchDataBroker(InsurancePolicyServiceAsync service){
		super(service);
	}

}
