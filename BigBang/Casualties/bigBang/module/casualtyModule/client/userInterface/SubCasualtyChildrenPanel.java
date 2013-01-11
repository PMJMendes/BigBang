package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.shared.SubCasualty;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.ConversationList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class SubCasualtyChildrenPanel extends View {

	protected SubCasualty subCasualty;
	protected SubCasualtyDataBrokerClient subCasualtyBrokerClient;

	public ContactsList contactsList;
	public DocumentsList documentsList;
	public SubProcessesList subProcessesList;
	public HistoryList historyList;
	public ConversationList conversationList;

	public SubCasualtyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		conversationList = new ConversationList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();

		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(conversationList, "Trocas de Mensagens");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");

		this.subCasualtyBrokerClient = getSubCasualtyBrokerClient();
		((SubCasualtyDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.SUB_CASUALTY)).registerClient(this.subCasualtyBrokerClient);
	}

	@Override
	protected void initializeView() {}

	public void setSubCasualty(SubCasualty subCasualty){
		this.subCasualty = subCasualty;
		String subCasualtyId = subCasualty == null ? null : subCasualty.id;


		boolean allow = subCasualty != null ? PermissionChecker.hasPermission(subCasualty, BigBangConstants.OperationIds.SubCasualtyProcess.UPDATE_SUB_CASUALTY) : false;
		this.contactsList.setOwner(subCasualtyId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.SUB_CASUALTY);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(subCasualtyId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.SUB_CASUALTY);
		this.documentsList.allowCreation(allow);
		this.conversationList.setOwner(subCasualtyId);
		this.subProcessesList.setOwner(subCasualtyId);
		this.historyList.setOwner(subCasualtyId);
	}

	protected SubCasualtyDataBrokerClient getSubCasualtyBrokerClient(){
		return new SubCasualtyDataBrokerClient() {
			protected int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					this.version = number;
				}
			}

			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
					return this.version;
				}
				return -1;
			}

			@Override
			public void updateSubCasualty(SubCasualty policy) {
				return;
			}

			@Override
			public void removeSubCasualty(String subCasualtyId) {
				if(subCasualty != null && subCasualty.id != null && subCasualtyId.equalsIgnoreCase(subCasualty.id)){
					setSubCasualty(null);
				}
			}

			@Override
			public void addSubCasualty(SubCasualty policy) {
				return;
			}

		};
	}

}
