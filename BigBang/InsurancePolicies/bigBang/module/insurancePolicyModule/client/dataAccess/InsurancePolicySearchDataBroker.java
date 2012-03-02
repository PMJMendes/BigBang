package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;

public class InsurancePolicySearchDataBroker extends
		SearchDataBrokerImpl<InsurancePolicyStub>  implements SearchDataBroker<InsurancePolicyStub>{

	public InsurancePolicySearchDataBroker() {
		this(InsurancePolicyService.Util.getInstance());
	}
	
	public InsurancePolicySearchDataBroker(InsurancePolicyServiceAsync service){
		super(service);
	}

}
