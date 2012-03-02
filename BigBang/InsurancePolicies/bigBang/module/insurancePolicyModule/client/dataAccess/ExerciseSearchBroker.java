package bigBang.module.insurancePolicyModule.client.dataAccess;

import bigBang.definitions.shared.ExerciseStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.insurancePolicyModule.interfaces.PolicyExerciseService;

public class ExerciseSearchBroker extends SearchDataBrokerImpl<ExerciseStub> {

	public ExerciseSearchBroker(){
		this(PolicyExerciseService.Util.getInstance());
	}

	public ExerciseSearchBroker(SearchServiceAsync service) {
		super(service);
	}

}
