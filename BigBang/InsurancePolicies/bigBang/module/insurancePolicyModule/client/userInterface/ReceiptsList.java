package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel;

public class ReceiptsList extends FilterableList<ReceiptStub> {

	public class Entry extends ReceiptSearchPanel.Entry {

		public Entry(ReceiptStub value) {
			super(value);
		}
		
	}
	
	protected ReceiptDataBroker broker;
	protected String ownerId;
	
	public ReceiptsList(){
		this.broker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		showFilterField(false);
	}
	
	public void setOwner(String ownerId, String ownerTypeId){
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else{
			broker.getReceiptsForOwner(ownerId, ownerTypeId, new ResponseHandler<Collection<ReceiptStub>>(){

				@Override
				public void onResponse(Collection<ReceiptStub> response) {
					clear();
					for(ReceiptStub receipt : response) {
						addEntry(receipt);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}
	
	public void addEntry(ReceiptStub receipt){
		this.add(new Entry(receipt));
	}
	
	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}
	
}
