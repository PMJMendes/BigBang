package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyService;

public class InsuranceSubPolicySearchDataBroker extends SearchDataBrokerImpl<SubPolicyStub> {

	public InsuranceSubPolicySearchDataBroker(){
		this(SubPolicyService.Util.getInstance());
	}
	
	public InsuranceSubPolicySearchDataBroker(SearchServiceAsync service) {
		super(service);
	}

}
