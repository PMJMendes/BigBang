package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.CasualtyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.SubProcessesList;
import bigBang.library.client.userInterface.view.View;

public class CasualtyChildrenPanel extends View {

	protected Casualty casualty;
	protected CasualtyDataBrokerClient casualtyBrokerClient;

	public ContactsList contactsList;
	public DocumentsList documentsList;
	public SubCasualtyList subCasualtyList;
	public SubProcessesList subProcessesList;
	public HistoryList historyList;

	public CasualtyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		subCasualtyList = new SubCasualtyList();
		subProcessesList = new SubProcessesList();
		historyList = new HistoryList();

		wrapper.add(subCasualtyList, "Sub-Sinistros");
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(subProcessesList, "Sub-Processos");
		wrapper.add(historyList, "Histórico");
		
		this.casualtyBrokerClient = getCasualtyBrokerClient();
		((CasualtyDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CASUALTY)).registerClient(this.casualtyBrokerClient);
	}

	@Override
	protected void initializeView() {}

	public void setCasualty(Casualty casualty){
		this.casualty = casualty;
		String casualtyId = casualty == null ? null : casualty.id;
		
		
		boolean allow = casualty != null ? PermissionChecker.hasPermission(casualty, BigBangConstants.OperationIds.CasualtyProcess.UPDATE_CASUALTY) : false;
		this.contactsList.setOwner(casualtyId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.CASUALTY);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(casualtyId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.CASUALTY);
		this.documentsList.allowCreation(allow);
		this.subCasualtyList.setOwner(casualtyId);
		this.subProcessesList.setOwner(casualtyId);
		this.historyList.setOwner(casualtyId);
	}

	protected CasualtyDataBrokerClient getCasualtyBrokerClient(){
		return new CasualtyDataBrokerClient() {
			protected int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
					this.version = number;
				}
			}

			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CASUALTY)){
					return this.version;
				}
				return -1;
			}

			@Override
			public void updateCasualty(Casualty policy) {
				return;
			}

			@Override
			public void removeCasualty(String casualtyId) {
				if(casualty != null && casualty.id != null && casualtyId.equalsIgnoreCase(casualty.id)){
					setCasualty(null);
				}
			}

			@Override
			public void addCasualty(Casualty policy) {
				return;
			}

		};
	}

}
