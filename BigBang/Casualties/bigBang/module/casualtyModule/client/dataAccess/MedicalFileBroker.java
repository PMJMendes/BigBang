package bigBang.module.casualtyModule.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.MedicalFile;

public interface MedicalFileBroker {
	
	public void getMedicalFile(String id, ResponseHandler<MedicalFile> handler);
	
	public void editMedicalFile(MedicalFile file, ResponseHandler<MedicalFile> handler);
	
	public void sendMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	public void receiveMessage(Conversation conversation, ResponseHandler<Conversation> handler);	
	
	public void closeProcess(String id, ResponseHandler<MedicalFile> handler);
}
