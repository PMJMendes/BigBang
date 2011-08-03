package bigBang.definitions.client.broker;

import bigBang.definitions.client.types.Mediator;
import bigBang.library.client.dataAccess.DataBrokerInterface;
import bigBang.library.client.response.ResponseHandler;

/**
 * The interface for a Mediator DataBroker
 */
public interface MediatorBroker extends DataBrokerInterface<Mediator> {

	/**
	 * Fetches all available Mediators
	 * @param handler The handler to be notified on response
	 */
	public void getMediators(ResponseHandler<Mediator[]> handler);
	
	/**
	 * Fetches the Mediator for a given id
	 * @param mediatorId The Mediator id
	 * @param handler The handler to be notified on response
	 */
	public void getMediator(String mediatorId, ResponseHandler<Mediator> handler);

	/**
	 * Creates a new Mediator
	 * @param mediator The mediator to be created
	 * @param handler The handler to be notified on response
	 */
	public void addMediator(Mediator mediator, ResponseHandler<Mediator> handler);
	
	/**
	 * Updates the Mediator for a given id
	 * @param mediator The Mediator
	 * @param handler The handler to be notified on response
	 */
	public void updateMediator(Mediator mediator, ResponseHandler<Mediator> handler);

	/**
	 * Removes the Mediator for a given id
	 * @param mediatorId The Mediator id
	 * @param handler The handler to be notified on response
	 */
	public void removeMediator(String mediatorId, ResponseHandler<Mediator> handler);

}