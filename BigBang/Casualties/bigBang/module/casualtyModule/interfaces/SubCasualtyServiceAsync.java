package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.SubCasualty;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubCasualtyServiceAsync
	extends SearchServiceAsync
{

	void closeProcess(String subCasualtyId, AsyncCallback<SubCasualty> callback);

	void deleteSubCasualty(String subCasualtyId, String reason,
			AsyncCallback<Void> callback);

	void editSubCasualty(SubCasualty subCasualty,
			AsyncCallback<SubCasualty> callback);

	void getSubCasualty(String subCasualtyId,
			AsyncCallback<SubCasualty> callback);
}
