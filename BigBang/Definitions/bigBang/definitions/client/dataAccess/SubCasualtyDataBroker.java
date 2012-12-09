package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;

public interface SubCasualtyDataBroker extends DataBrokerInterface<SubCasualty> {

	public void getSubCasualty(String id, ResponseHandler<SubCasualty> handler);
	
	public void updateSubCasualty(SubCasualty subCasualty, ResponseHandler<SubCasualty> handler);
	
	public void deleteSubCasualty(String subCasualtyId, String reason, ResponseHandler<Void> handler);

	public void getSubCasualties(String ownerId,
			ResponseHandler<Collection<SubCasualtyStub>> responseHandler);
	
	public SearchDataBroker<SubCasualtyStub> getSearchBroker();
	
	public void markForClosing(String subCasualtyId, String revisorId, ResponseHandler<Void> handler);
	
	public void closeSubCasualty(String subCasualtyId, ResponseHandler<Void> handler);
	
	public void rejectCloseSubCasualty(String subCasualtyId, String reason, ResponseHandler<Void> handler);

	public void markNotificationSent(String subCasualtyId,
			ResponseHandler<SubCasualty> responseHandler);
	
	void sendMessage(Conversation conversation, ResponseHandler<Conversation> handler);
	
	void receiveMessage(Conversation conversation, ResponseHandler<Conversation> handler);
}
