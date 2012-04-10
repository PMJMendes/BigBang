package bigBang.module.casualtyModule.interfaces;

<<<<<<< .mine
import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.ManagerTransfer;
=======
import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Casualty;
>>>>>>> .r1374
import bigBang.library.interfaces.SearchServiceAsync;

public interface CasualtyServiceAsync
	extends SearchServiceAsync
{
	void getCasualty(String casualtyId, AsyncCallback<Casualty> callback);
	void editCasualty(Casualty casualty, AsyncCallback<Casualty> callback);
	void createManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void deleteCasualty(String casualtyId, String reason, AsyncCallback<Void> callback);
	void massCreateManagerTransfer(ManagerTransfer transfer, AsyncCallback<ManagerTransfer> callback);
	void getCasualty(String casualtyId, AsyncCallback<Casualty> callback);

	void deleteCasualty(String casualtyId, AsyncCallback<Void> callback);

	void updateCasualty(Casualty casualty, AsyncCallback<Casualty> callback);

}
