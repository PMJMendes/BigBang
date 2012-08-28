package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubCasualtyServiceAsync
	extends SearchServiceAsync
{
	void getSubCasualty(String subCasualtyId, AsyncCallback<SubCasualty> callback);
	void editSubCasualty(SubCasualty subCasualty, AsyncCallback<SubCasualty> callback);
	void sendNotification(String subCasualtyId, AsyncCallback<SubCasualty> callback);
	void createInfoRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createExternalRequest(ExternalInfoRequest request, AsyncCallback<ExternalInfoRequest> callback);
	void markForClosing(String subCasualtyId, String revisorId, AsyncCallback<SubCasualty> callback);
	void closeProcess(String subCasualtyId, AsyncCallback<SubCasualty> callback);
	void rejectClosing(String subCasualtyId, String reason, AsyncCallback<SubCasualty> callback);
	void deleteSubCasualty(String subCasualtyId, String reason, AsyncCallback<Void> callback);
}
