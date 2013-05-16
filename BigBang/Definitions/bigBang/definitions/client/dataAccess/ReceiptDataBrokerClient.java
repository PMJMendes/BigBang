package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Receipt;

public interface ReceiptDataBrokerClient extends DataBrokerClient<Receipt> {

	public void addReceipt(Receipt receipt);
	
	public void updateReceipt(Receipt receipt);
	
	public void removeReceipt(String id);
	
}
