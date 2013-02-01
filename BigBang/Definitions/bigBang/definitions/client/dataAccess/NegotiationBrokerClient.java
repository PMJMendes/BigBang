package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Negotiation;

public interface NegotiationBrokerClient {

	public void updateNegotiation(Negotiation result);

	public void setDataVersionNumber(String negotiation, int currentDataVersion);

	public void removeNegotiation(String negotiationId);

}
