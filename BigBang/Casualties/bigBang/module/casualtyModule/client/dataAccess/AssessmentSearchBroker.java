package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.AssessmentStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.casualtyModule.interfaces.AssessmentService;

public class AssessmentSearchBroker extends SearchDataBrokerImpl<AssessmentStub> implements SearchDataBroker<AssessmentStub>{

	public AssessmentSearchBroker() {
		this(AssessmentService.Util.getInstance());
	}

	public AssessmentSearchBroker(SearchServiceAsync service) {
		super(service);
	}
	
	
	
}
