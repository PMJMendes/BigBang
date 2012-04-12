package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.client.dataAccess.ReceiptDataBrokerClient;
import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class ReceiptChildrenPanel extends View{

	protected Receipt receipt;
	protected ReceiptDataBrokerClient receiptBrokerClient;
	
	public ContactsList contactsList;
	public DocumentsList documentsList;
	public SubProcessesList subProcessesList;
	public HistoryList historyList;
	
	public ReceiptChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();
		
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");
		
		receiptBrokerClient = getReceiptBrokerClient();
		((ReceiptProcessDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT)).registerClient(receiptBrokerClient);
	}
	
	private ReceiptDataBrokerClient getReceiptBrokerClient() {
		return new ReceiptDataBrokerClient() {
			protected int version;
			
			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
					this.version = number;
				}
				
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.RECEIPT)){
					return this.version;
				}
				return -1;
			}
			
			@Override
			public void updateReceipt(Receipt receipt) {
				return;
			}
			
			@Override
			public void removeReceipt(String id) {
				if(receipt != null && receipt.id != null && id.equalsIgnoreCase(receipt.id)){
					setReceipt(null);
				}
				
			}
			
			@Override
			public void addReceipt(Receipt receipt) {
				return;
			}
		};
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
		String receiptId = receipt == null ? null: receipt.id;
		
		boolean allow = receipt != null ? PermissionChecker.hasPermission(receipt, BigBangConstants.OperationIds.ReceiptProcess.UPDATE_RECEIPT) : false;
		this.contactsList.setOwner(receiptId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.RECEIPT);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(receiptId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.RECEIPT);
		this.documentsList.allowCreation(allow);
		subProcessesList.setOwner(receiptId);
		historyList.setOwner(receiptId);
	}

	@Override
	protected void initializeView() {
		return;
	}



}
