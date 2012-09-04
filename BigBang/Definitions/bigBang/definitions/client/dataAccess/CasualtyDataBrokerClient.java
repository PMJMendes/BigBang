package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Casualty;

public interface CasualtyDataBrokerClient extends DataBrokerClient<Casualty> {

	public void addCasualty(Casualty casualty);
	
	public void updateCasualty(Casualty casualty);
	
	public void removeCasualty(String casualtyId);
	
}
