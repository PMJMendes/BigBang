package bigBang.library.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBrokerClient;
import bigBang.definitions.shared.Assessment;

public interface AssessmentBrokerClient extends DataBrokerClient<Assessment>{
	
	void updateAssessment(Assessment response);

}
