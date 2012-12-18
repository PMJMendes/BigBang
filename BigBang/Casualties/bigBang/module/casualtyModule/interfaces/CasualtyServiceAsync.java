package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CasualtyServiceAsync
	extends SearchServiceAsync
{
	void getCasualty(String casualtyId, AsyncCallback<Casualty> callback);
	void editCasualty(Casualty casualty, AsyncCallback<Casualty> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void createSubCasualty(SubCasualty subCasualty, AsyncCallback<SubCasualty> callback);
	void closeProcess(String casualtyId, AsyncCallback<Casualty> callback);
	void reopenProcess(String casualtyId, String motiveId, AsyncCallback<Casualty> callback);
	void deleteCasualty(String casualtyId, String reason, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
