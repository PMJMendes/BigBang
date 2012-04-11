package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;

public interface CasualtyDataBroker extends DataBrokerInterface<Casualty> {

	public void updateCasualty(Casualty casualty, ResponseHandler<Casualty> handler);
	
	public void deleteCasualty(String casualtyId, String reason, ResponseHandler<Void> handler);
	
	public void getCasualty(String casualtyId, ResponseHandler<Casualty> handler);

	public SearchDataBroker<CasualtyStub> getSearchBroker();
	
}
