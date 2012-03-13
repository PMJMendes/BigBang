package bigBang.module.receiptModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Receipt;
import bigBang.library.interfaces.SearchServiceAsync;

public interface ReceiptServiceAsync
	extends SearchServiceAsync
{
	void getReceipt(String receiptId, AsyncCallback<Receipt> callback);
	void editReceipt(Receipt receipt, AsyncCallback<Receipt> callback);
	void transferToPolicy(String receiptId, String newPolicyId, AsyncCallback<Receipt> callback);
	void deleteReceipt(String receiptId, AsyncCallback<Void> callback);
	void serialCreateReceipt(Receipt receipt, String docushareId, AsyncCallback<Receipt> callback);
}
