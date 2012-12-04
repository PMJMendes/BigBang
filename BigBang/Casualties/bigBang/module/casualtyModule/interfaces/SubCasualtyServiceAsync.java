package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubCasualtyServiceAsync
	extends SearchServiceAsync
{
	void getSubCasualty(String subCasualtyId, AsyncCallback<SubCasualty> callback);
	void editSubCasualty(SubCasualty subCasualty, AsyncCallback<SubCasualty> callback);
	void sendNotification(String subCasualtyId, AsyncCallback<SubCasualty> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void markForClosing(String subCasualtyId, String revisorId, AsyncCallback<SubCasualty> callback);
	void closeProcess(String subCasualtyId, AsyncCallback<SubCasualty> callback);
	void rejectClosing(String subCasualtyId, String reason, AsyncCallback<SubCasualty> callback);
	void deleteSubCasualty(String subCasualtyId, String reason, AsyncCallback<Void> callback);
}
