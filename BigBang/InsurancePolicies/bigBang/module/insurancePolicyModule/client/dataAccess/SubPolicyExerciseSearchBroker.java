package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.SubPolicyExerciseService;

public class SubPolicyExerciseSearchBroker extends SearchDataBrokerImpl<ExerciseStub>
		implements SearchDataBroker<ExerciseStub> {
	
	public SubPolicyExerciseSearchBroker(){
		this(SubPolicyExerciseService.Util.getInstance());
	}
	
	public SubPolicyExerciseSearchBroker(SearchServiceAsync service) {
		super(service);
	}

}
