package bigBang.module.casualtyModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Casualty;
import bigBang.library.interfaces.SearchServiceAsync;

public interface CasualtyServiceAsync extends SearchServiceAsync{

	void getCasualty(String casualtyId, AsyncCallback<Casualty> callback);

	void deleteCasualty(String casualtyId, AsyncCallback<Void> callback);

	void updateCasualty(Casualty casualty, AsyncCallback<Casualty> callback);

}
