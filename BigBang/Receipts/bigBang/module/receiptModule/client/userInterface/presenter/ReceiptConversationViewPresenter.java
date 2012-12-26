package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

public class ReceiptConversationViewPresenter extends ConversationViewPresenter<Receipt> {

	protected ReceiptDataBroker broker;
	protected Receipt receipt;
	
	public ReceiptConversationViewPresenter(Display<Receipt> view) {
		super(view);
		broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
	}

	@Override
	protected void fillOwner(String ownerId, final ResponseHandler<Receipt> handler) {
		broker.getReceipt(ownerId, new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				receipt = response;
				setContacts();
				handler.onResponse(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	protected void setContacts() {
		view.addContact("Recibo (" + receipt.number + ")",receipt.id ,BigBangConstants.EntityIds.RECEIPT);
		view.addContact("Apólice " + (BigBangConstants.EntityIds.INSURANCE_SUB_POLICY.equalsIgnoreCase(receipt.ownerTypeId) ? "Adesão " : "") + "(" + receipt.policyNumber + ")", receipt.policyId, receipt.ownerTypeId);
		view.addContact("Cliente (" + receipt.clientName + ")", receipt.clientId, BigBangConstants.EntityIds.CLIENT);
		view.addContact("Seguradora (" + receipt.insurerName + ")", receipt.insurerId, BigBangConstants.EntityIds.INSURANCE_AGENCY);
		view.addContact("Mediador (" + receipt.inheritMediatorName + ")", receipt.inheritMediatorId, BigBangConstants.EntityIds.MEDIATOR);

	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		String receiptId = parameterHolder.getParameter("receiptid");
		parameterHolder.setParameter("ownerid", receiptId);
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.RECEIPT);
		super.setParameters(parameterHolder);
	}


}
