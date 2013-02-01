package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Negotiation; 
import bigBang.definitions.shared.Negotiation.Cancellation;
import bigBang.definitions.shared.Negotiation.Deletion;
import bigBang.definitions.shared.Negotiation.Grant;
import bigBang.definitions.shared.Negotiation.Response;

public interface NegotiationBroker extends DataBrokerInterface<Negotiation>{

	void getNegotiation(String negotiationId,
			ResponseHandler<Negotiation> handler);

	void updateNegotiation(Negotiation negotiation,
			ResponseHandler<Negotiation> handler);

	void removeNegotiation(Deletion deletion, ResponseHandler<String> handler);

	void cancelNegotiation(Cancellation cancellation,
			ResponseHandler<Negotiation> handler);

	void receiveResponse(Response response, ResponseHandler<Negotiation> handler);

	void grantNegotiation(Grant grant, ResponseHandler<Negotiation> handler);

	void sendMessage(Conversation info,
			ResponseHandler<Conversation> responseHandler);

	void receiveMessage(Conversation info,
			ResponseHandler<Conversation> responseHandler); 
	

}
