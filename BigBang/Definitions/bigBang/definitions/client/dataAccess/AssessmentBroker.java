package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;

public interface AssessmentBroker extends DataBrokerInterface<Assessment>{

	public void getAssessment(String id, ResponseHandler<Assessment> handler);

	public void editAssessment(Assessment assessment, ResponseHandler<Assessment> handler);
	
	public void sendMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	public void receiveMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	public void closeProcess(String id, ResponseHandler<Assessment> handler);
	
}
