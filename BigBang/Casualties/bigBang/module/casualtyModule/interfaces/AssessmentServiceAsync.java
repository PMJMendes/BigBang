package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Assessment;
import bigBang.definitions.shared.Conversation;
import bigBang.library.interfaces.Service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AssessmentServiceAsync
	extends Service
{
	void getAssessment(String id, AsyncCallback<Assessment> callback);
	void editAssessment(Assessment assessment, AsyncCallback<Assessment> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void closeProcess(String id, AsyncCallback<Assessment> callback);
}
