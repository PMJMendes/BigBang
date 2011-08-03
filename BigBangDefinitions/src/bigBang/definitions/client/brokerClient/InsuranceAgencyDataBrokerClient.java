package bigBang.definitions.client.brokerClient;

import bigBang.definitions.client.types.InsuranceAgency;
import bigBang.library.client.dataAccess.DataBrokerClient;

public interface InsuranceAgencyDataBrokerClient extends
DataBrokerClient<InsuranceAgency> {

	/**
	 * Sends all the existing insurance agencies to the broker client
	 * @param groups The array with the groups
	 */
	public void setInsuranceAgencies(InsuranceAgency[] insuranceAgencys);

	/**
	 * Adds a insurance agency to the BrokerClient cache
	 * @param insuranceAgency The insurance agency to add
	 */
	public void addInsuranceAgency(InsuranceAgency insuranceAgency);

	/**
	 * Updates the current insurance agency info in the BrokerClient
	 * @param insuranceAgency The insurance agency information
	 */
	public void updateInsuranceAgency(InsuranceAgency insuranceAgency);

	/**
	 * Removes an insurance agency from the BrokerClient cache
	 * @param insuranceAgencyId The insurance agency id
	 */
	public void removeInsuranceAgency(String insuranceAgencyId);

}
