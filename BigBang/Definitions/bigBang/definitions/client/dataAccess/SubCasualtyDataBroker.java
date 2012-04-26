package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;

public interface SubCasualtyDataBroker extends DataBrokerInterface<SubCasualty> {

	public void getSubCasualty(String id, ResponseHandler<SubCasualty> handler);
	
	public void updateSubCasualty(SubCasualty subCasualty, ResponseHandler<SubCasualty> handler);
	
	public void deleteSubCasualty(String subCasualtyId, String reason, ResponseHandler<Void> handler);

	public void getSubCasualties(String ownerId,
			ResponseHandler<Collection<SubCasualtyStub>> responseHandler);
	
	public SearchDataBroker<SubCasualtyStub> getSearchBroker();

	public void createInfoOrDocumentRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);

	public void createExternalInfoRequest(ExternalInfoRequest toSend,
			ResponseHandler<ExternalInfoRequest> responseHandler);
	
}
