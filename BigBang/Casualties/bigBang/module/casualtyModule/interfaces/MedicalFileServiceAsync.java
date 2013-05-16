package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MedicalFileServiceAsync
	extends SearchServiceAsync
{
	void getMedicalFile(String id, AsyncCallback<MedicalFile> callback);
	void editMedicalFile(MedicalFile medicalFile, AsyncCallback<MedicalFile> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void closeProcess(String id, AsyncCallback<MedicalFile> callback);
}
