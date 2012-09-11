package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyObjectService;

public class SubPolicyInsuredObjectSearchBroker extends SearchDataBrokerImpl<InsuredObjectStub>
		implements SearchDataBroker<InsuredObjectStub> {

	public SubPolicyInsuredObjectSearchBroker(){
		this(SubPolicyObjectService.Util.getInstance());
	}
	
	public SubPolicyInsuredObjectSearchBroker(SearchServiceAsync service) {
		super(service);
	}

}
