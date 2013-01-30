package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Rectangle;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.interfaces.ExactItemSubServiceAsync;
import bigBang.library.interfaces.ImageSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReceiptServiceAsync
	extends SearchServiceAsync, ExactItemSubServiceAsync, ImageSubServiceAsync
{
	void getReceipt(String receiptId, AsyncCallback<Receipt> callback);
	void editReceipt(Receipt receipt, AsyncCallback<Receipt> callback);
	void editAndValidateReceipt(Receipt receipt, Rectangle rect, AsyncCallback<Receipt> callback);
	void receiveImage(String receiptId, DocuShareHandle source, AsyncCallback<Receipt> callback);
	void transferToPolicy(String receiptId, String newPolicyId, AsyncCallback<Receipt> callback);
	void validateReceipt(String receiptId, AsyncCallback<Receipt> callback);
	void setForReturn(Receipt.ReturnMessage message, AsyncCallback<Receipt> callback);
	void createPaymentNotice(String receiptId, AsyncCallback<Receipt> callback);
	void createSecondPaymentNotice(String receiptId, AsyncCallback<Receipt> callback);
	void cancelPaymentNotice(String receiptId, AsyncCallback<Receipt> callback);
	void markPayed(Receipt.PaymentInfo info, AsyncCallback<Receipt> callback);
	void getRelevantDebitNotes(String receiptId, AsyncCallback<DebitNote[]> callback);
	void associateWithDebitNote(String receiptId, String debitNoteId, AsyncCallback<Receipt> callback);
	void markNotPayed(String receiptId, AsyncCallback<Receipt> callback);
	void voidInternal(Receipt.ReturnMessage message, AsyncCallback<Receipt> callback);
	void setDASNotNecessary(String receiptId, AsyncCallback<Receipt> callback);
	void createDASRequest(DASRequest request, AsyncCallback<Receipt> callback);
	void createSignatureRequest(SignatureRequest request, AsyncCallback<Receipt> callback);
	void sendPayment(String receiptId, AsyncCallback<Receipt> callback);
	void createInternalReceipt(String receiptId, AsyncCallback<Receipt> callback);
	void sendReceipt(String receiptId, AsyncCallback<Receipt> callback);
	void returnPayment(String receiptId, AsyncCallback<Receipt> callback);
	void insurerAccouting(String receiptId, AsyncCallback<Receipt> callback);
	void mediatorAccounting(String receiptId, AsyncCallback<Receipt> callback);
	void returnToInsurer(String receiptId, AsyncCallback<Receipt> callback);
	void sendMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void receiveMessage(Conversation conversation, AsyncCallback<Conversation> callback);
	void deleteReceipt(String receiptId, AsyncCallback<Void> callback);
	void serialCreateReceipt(Receipt receipt, DocuShareHandle source, AsyncCallback<Receipt> callback);
	void massCreatePaymentNotice(String[] receiptIds, AsyncCallback<Void> callback);
	void massCreateSecondPaymentNotice(String[] receiptIds, AsyncCallback<Void> callback);
	void massCreateSignatureRequest(String[] receiptIds, int replylimit, AsyncCallback<Void> callback);
	void massSendPayment(String[] receiptIds, AsyncCallback<Void> callback);
	void massCreateInternalReceipt(String[] receiptIds, AsyncCallback<Void> callback);
	void massSendReceipt(String[] receiptIds, AsyncCallback<Void> callback);
	void massInsurerAccounting(String[] receiptIds, InsurerAccountingExtra[] extraInfo, AsyncCallback<Void> callback);
	void massMediatorAccounting(String[] receiptIds, AsyncCallback<Void> callback);
	void massReturnToInsurer(String[] receiptIds, AsyncCallback<Void> callback);
}
