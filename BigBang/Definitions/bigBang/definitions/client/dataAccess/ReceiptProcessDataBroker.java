package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.definitions.shared.ReceiptStub;

public interface ReceiptProcessDataBroker extends DataBrokerInterface<Receipt> {

	public void getReceipt(String id, ResponseHandler<Receipt> handler);
	
	public void updateReceipt(Receipt receipt, ResponseHandler<Receipt> handler);
	
	public void removeReceipt(String id, ResponseHandler<String> handler);
	
	public void getReceiptsForOwner(String ownerId, ResponseHandler<Collection<ReceiptStub>> handler);
	
	public SearchDataBroker<ReceiptStub> getSearchBroker();

	void transferToInsurancePolicy(String receiptId, String newPolicyId,
			ResponseHandler<Receipt> handler);

	void associateWithDebitNote(String receiptId, String debitNoteId,
			ResponseHandler<Receipt> handler);

	void getRelevantDebitNotes(String receiptId,
			ResponseHandler<DebitNote[]> handler);

	void setForReturn(ReturnMessage message, ResponseHandler<Receipt> handler);

	void validateReceipt(String receiptId, ResponseHandler<Receipt> handler);

	void getReceiptsWithNumber(String label,
			ResponseHandler<Collection<ReceiptStub>> handler);

	void serialCreateReceipt(Receipt receipt, DocuShareHandle source,
			ResponseHandler<Receipt> handler);
	
	void receiveImage(String receiptId, DocuShareHandle source, ResponseHandler<Receipt> handler);

	void massCreatePaymentNotice(String[] receiptIds,
			ResponseHandler<Void> handler);

	void createPaymentNotice(String receiptId, ResponseHandler<Receipt> handler);

	void sendPayment(String receiptId, ResponseHandler<Receipt> handler);
	
	void returnToInsurer(String receiptId, ResponseHandler<Receipt> handler);
	
	void massReturnToInsurer(String[] receiptIds, ResponseHandler<Void> handler);
	
}
