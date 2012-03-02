package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Mediator;

public interface MediatorDataBrokerClient extends
DataBrokerClient<Mediator> {

	/**
	 * Sends all the existing mediators to the broker client
	 * @param mediators The array with the mediators
	 */
	public void setMediators(Mediator[] mediators);

	/**
	 * Adds a mediator to the BrokerClient cache
	 * @param mediator The mediator to add
	 */
	public void addMediator(Mediator mediator);

	/**
	 * Updates the current mediator info in the BrokerClient
	 * @param mediator The mediator information
	 */
	public void updateMediator(Mediator mediator);

	/**
	 * Removes a mediator from the BrokerClient cache
	 * @param mediatorId The mediator id
	 */
	public void removeMediator(String mediatorId);

}