package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.CostCenter;

/**
 * The interface for a Cost Center DataBroker
 */
public interface CostCenterBroker extends DataBrokerInterface<CostCenter> {

	/**
	 * Fetches all available Cost Centers
	 * @param handler The handler to be notified on response
	 */
	public void getCostCenters(ResponseHandler<CostCenter[]> handler);
	
	/**
	 * Fetches the Cost Center for a given id
	 * @param costCenterId The Cost Center id
	 * @param handler The handler to be notified on response
	 */
	public void getCostCenter(String costCenterId, ResponseHandler<CostCenter> handler);

	/**
	 * Creates a new cost center
	 * @param costCenter The cost center to be created
	 * @param handler The handler to be notified on response
	 */
	public void addCostCenter(CostCenter costCenter, ResponseHandler<CostCenter> handler);
	
	/**
	 * Updates the Cost Center for a given id
	 * @param costCenter The Cost Center
	 * @param handler The handler to be notified on response
	 */
	public void updateCostCenter(CostCenter costCenter, ResponseHandler<CostCenter> handler);

	/**
	 * Removes the Cost Center for a given id
	 * @param costCenterId The Cost Center id
	 * @param handler The handler to be notified on response
	 */
	public void removeCostCenter(String costCenterId, ResponseHandler<CostCenter> handler);

}
