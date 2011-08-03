package bigBang.definitions.client.broker;

import bigBang.definitions.client.types.InsuranceAgency;
import bigBang.library.client.dataAccess.DataBrokerInterface;
import bigBang.library.client.response.ResponseHandler;

/**
 * The interface for a Insurance Agency DataBroker
 */
public interface InsuranceAgencyBroker extends DataBrokerInterface<InsuranceAgency> {

	/**
	 * Fetches all available insurance agencies
	 * @param handler The handler to be notified on response
	 */
	public void getInsuranceAgencies(ResponseHandler<InsuranceAgency[]> handler);
	
	/**
	 * Fetches the insurance agency for a given id
	 * @param agencyId The insurance agency id
	 * @param handler The handler to be notified on response
	 */
	public void getInsuranceAgency(String agencyId, ResponseHandler<InsuranceAgency> handler);

	/**
	 * Creates a new Insurance Agency
	 * @param agency The insurance agency to be created
	 * @param handler The handler to be notified on response
	 */
	public void addInsuranceAgency(InsuranceAgency agency, ResponseHandler<InsuranceAgency> handler);
	
	/**
	 * Updates the insurance agency for a given id
	 * @param agencyId The insurance agency
	 * @param handler The handler to be notified on response
	 */
	public void updateInsuranceAgency(InsuranceAgency agency, ResponseHandler<InsuranceAgency> handler);

	/**
	 * Removes the insurance agency for a given id
	 * @param agencyId The insurance agency id
	 * @param handler The handler to be notified on response
	 */
	public void removeInsuranceAgency(String agencyId, ResponseHandler<InsuranceAgency> handler);

}