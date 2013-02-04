package bigBang.module.receiptModule.interfaces;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.InsurerAccountingExtra;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Rectangle;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.interfaces.ExactItemSubService;
import bigBang.library.interfaces.ImageSubService;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ReceiptService")
public interface ReceiptService
	extends SearchService, ExactItemSubService, ImageSubService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ReceiptServiceAsync instance;
		public static ReceiptServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ReceiptService.class);
			}
			return instance;
		}
	}
	
	public Receipt getReceipt(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt editReceipt(Receipt receipt) throws SessionExpiredException, BigBangException;
	public Receipt receiveImage(Receipt receipt, DocuShareHandle source) throws SessionExpiredException, BigBangException;
	public Receipt transferToPolicy(String receiptId, String newPolicyId) throws SessionExpiredException, BigBangException;

	public Receipt editAndValidateReceipt(Receipt receipt, Rectangle rect) throws SessionExpiredException, BigBangException;
	public Receipt validateReceipt(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt setForReturn(Receipt.ReturnMessage message) throws SessionExpiredException, BigBangException;

	public Receipt createPaymentNotice(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt createSecondPaymentNotice(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt cancelPaymentNotice(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt markPayed(Receipt.PaymentInfo info) throws SessionExpiredException, BigBangException;
	public DebitNote[] getRelevantDebitNotes(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt associateWithDebitNote(String receiptId, String debitNoteId) throws SessionExpiredException, BigBangException;
	public Receipt markNotPayed(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt voidInternal(Receipt.ReturnMessage message) throws SessionExpiredException, BigBangException;

	public Receipt setDASNotNecessary(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt createDASRequest(DASRequest request) throws SessionExpiredException, BigBangException;

	public Receipt createSignatureRequest(SignatureRequest request) throws SessionExpiredException, BigBangException;
	public Receipt sendPayment(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt createInternalReceipt(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt sendReceipt(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt returnPayment(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt insurerAccouting(String receiptId) throws SessionExpiredException, BigBangException;
	public Receipt mediatorAccounting(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt returnToInsurer(String receiptId) throws SessionExpiredException, BigBangException;

	public Conversation sendMessage(Conversation conversation) throws SessionExpiredException, BigBangException;
	public Conversation receiveMessage(Conversation conversation) throws SessionExpiredException, BigBangException;

	public void deleteReceipt(String receiptId) throws SessionExpiredException, BigBangException;

	public Receipt serialCreateReceipt(Receipt receipt, DocuShareHandle source) throws SessionExpiredException, BigBangException;
	public void massCreatePaymentNotice(String[] receiptIds) throws SessionExpiredException, BigBangException;
	public void massCreateSecondPaymentNotice(String[] receiptIds) throws SessionExpiredException, BigBangException;
	public void massCreateSignatureRequest(String[] receiptIds, int replylimit) throws SessionExpiredException, BigBangException;
	public void massSendPayment(String[] receiptIds) throws SessionExpiredException, BigBangException;
	public void massCreateInternalReceipt(String[] receiptIds) throws SessionExpiredException, BigBangException;
	public void massSendReceipt(String[] receiptIds) throws SessionExpiredException, BigBangException;
	public void massInsurerAccounting(String[] receiptIds, InsurerAccountingExtra[] extraInfo) throws SessionExpiredException, BigBangException;
	public void massMediatorAccounting(String[] receiptIds) throws SessionExpiredException, BigBangException;
	public void massReturnToInsurer(String[] receiptIds) throws SessionExpiredException, BigBangException;
}
