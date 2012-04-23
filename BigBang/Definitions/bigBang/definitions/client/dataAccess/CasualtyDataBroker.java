package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.SubCasualty;

public interface CasualtyDataBroker extends DataBrokerInterface<Casualty> {

	public void updateCasualty(Casualty casualty, ResponseHandler<Casualty> handler);
	
	public void deleteCasualty(String casualtyId, String reason, ResponseHandler<Void> handler);
	
	public void getCasualty(String casualtyId, ResponseHandler<Casualty> handler);

	public void close(String casualtyId, ResponseHandler<Void> handler);

	public SearchDataBroker<CasualtyStub> getSearchBroker();

	public void getCasualtiesForClient(String ownerId,
			ResponseHandler<Collection<CasualtyStub>> responseHandler);

	public void createSubCasualty(SubCasualty subCasualty,
			ResponseHandler<SubCasualty> responseHandler);
	
	public void createManagerTransfer(String[] dataObjectIds, String newManagerId, ResponseHandler<ManagerTransfer> handler);
	
}
