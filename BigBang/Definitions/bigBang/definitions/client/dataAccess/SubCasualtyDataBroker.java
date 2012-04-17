package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SubCasualty;

public interface SubCasualtyDataBroker extends DataBrokerInterface<SubCasualty> {

	public void getSubCasualty(String id, ResponseHandler<SubCasualty> handler);
	
	public void updateSubCasualty(SubCasualty subCasualty, ResponseHandler<SubCasualty> handler);
	
	public void deleteSubCasualty(String subCasualtyId, String reason, ResponseHandler<Void> handler);
	
}
