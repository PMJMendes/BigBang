package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.dataAccess.MediatorBroker;
import bigBang.definitions.client.dataAccess.MediatorDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Mediator;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.user.client.ui.StackPanel;

public class MediatorChildrenPanel extends View {

	protected MediatorBroker broker;
	protected MediatorDataBrokerClient brokerClient;
	
	protected Mediator mediator;
	
	protected ContactsList contactsList;
	protected DocumentsList documentsList;
	
	public MediatorChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		
		this.brokerClient = getBrokerClient();
		((MediatorBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.MEDIATOR)).registerClient(this.brokerClient);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	public void setOwner(Mediator owner) {
		this.mediator = owner;
		String agencyId = owner == null ? null : owner.id;
		
		boolean allow = owner != null && SessionGeneralSystem.getInstance() != null ? PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_MEDIATORS) && agencyId != null : false;
		this.contactsList.setOwner(agencyId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.MEDIATOR);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(agencyId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.MEDIATOR);
	}

	protected MediatorDataBrokerClient getBrokerClient(){
		return new MediatorDataBrokerClient() {
			
			protected int version;
			
			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				version = number;
			}
			
			@Override
			public int getDataVersion(String dataElementId) {
				return version;
			}
			
			@Override
			public void updateMediator(Mediator mediator) {
				return;
			}
			
			@Override
			public void setMediators(Mediator[] mediators) {
				return;
			}
			
			@Override
			public void removeMediator(String mediatorId) {
				if(mediatorId != null && mediator != null && mediatorId.equalsIgnoreCase(mediator.id)){
					setOwner(null);
				}
			}
			
			@Override
			public void addMediator(Mediator mediator) {
				return;
			}
		};
	}
	
}
