package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;

public interface ReceiptProcessDataBroker extends DataBrokerInterface<Receipt> {

	public void getReceipt(String id, ResponseHandler<Receipt> handler);
	
	public void updateReceipt(Receipt receipt, ResponseHandler<Receipt> handler);
	
	public void removeReceipt(String id, ResponseHandler<String> handler);
	
	public void getReceiptsForOwner(String ownerId, ResponseHandler<Collection<ReceiptStub>> handler);
	
	public SearchDataBroker<ReceiptStub> getSearchBroker();
	
}
