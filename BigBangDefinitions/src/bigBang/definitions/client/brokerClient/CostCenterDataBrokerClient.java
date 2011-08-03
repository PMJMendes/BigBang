package bigBang.definitions.client.brokerClient;

import bigBang.definitions.client.types.CostCenter;
import bigBang.library.client.dataAccess.DataBrokerClient;

public interface CostCenterDataBrokerClient extends
DataBrokerClient<CostCenter> {

	/**
	 * Sends all the existing cost centers to the broker client
	 * @param groups The array with the groups
	 */
	public void setCostCenters(CostCenter[] costCenters);

	/**
	 * Adds a cost center to the BrokerClient cache
	 * @param costCenter The cost center to add
	 */
	public void addCostCenter(CostCenter costCenter);

	/**
	 * Updates the current cost center info in the BrokerClient
	 * @param costCenter The cost center information
	 */
	public void updateCostCenter(CostCenter costCenter);

	/**
	 * Removes a cost center from the BrokerClient cache
	 * @param costCenterId The cost center id
	 */
	public void removeCostCenter(String costCenterId);

}
