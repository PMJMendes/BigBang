package bigBang.module.casualtyModule.interfaces;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CasualtyServiceAsync
	extends SearchServiceAsync
{
	void getCasualty(String casualtyId, AsyncCallback<Casualty> callback);
	void editCasualty(Casualty casualty, AsyncCallback<Casualty> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void deleteCasualty(String casualtyId, String reason, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
}
