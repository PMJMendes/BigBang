package bigBang.module.receiptModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Receipt;
import bigBang.library.interfaces.SearchServiceAsync;

public interface ReceiptServiceAsync extends SearchServiceAsync {

	void deleteReceipt(String receiptId, AsyncCallback<Void> callback);

	void editReceipt(Receipt receipt, AsyncCallback<Receipt> callback);

	void getReceipt(String receiptId, AsyncCallback<Receipt> callback);

}
