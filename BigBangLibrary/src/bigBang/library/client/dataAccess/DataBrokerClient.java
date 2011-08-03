package bigBang.library.client.dataAccess;

/**
 * An interface to be implemented by a DataBroker client.
 *
 * @see DataBroker
 * @author Francisco Cabrita @ Premium Minds Lda.
 */
public interface DataBrokerClient<T> {

	/**
	 * Sets the curent data version number held by the client.
	 * @param dataElementId The id of the data element accessed by this client's broker
	 * @param number The new version number;
	 */
	public void setDataVersionNumber(String dataElementId, int number);

	/**
	 * Gets the current data version held by the client
	 * @param dataElementId The id of the data element accessed by this client's broker 
	 * @return The data version being held by this client for the given data element
	 */
	public int getDataVersion(String dataElementId);

}
