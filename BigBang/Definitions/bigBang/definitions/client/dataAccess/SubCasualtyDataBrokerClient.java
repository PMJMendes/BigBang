package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.SubCasualty;

public interface SubCasualtyDataBrokerClient extends
		DataBrokerClient<SubCasualty> {

	public void addSubCasualty(SubCasualty subCasualty);
	
	public void updateSubCasualty(SubCasualty subCasualty);
	
	public void removeSubCasualty(String id);
	
}
