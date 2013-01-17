package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.OtherEntityBroker;
import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

public class OtherEntityChildrenPanel extends View implements OtherEntityBrokerClient{

	protected OtherEntityBroker broker;
	protected OtherEntity entity;
	protected int version;

	public ContactsList contactsList;
	public DocumentsList documentsList;

	public OtherEntityChildrenPanel() {
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);

		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();

		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");


	}

	@Override
	protected void initializeView() {
		return;
	}


	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}


	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}


	@Override
	public void setOtherEntities(OtherEntity[] entities) {
		return;
	}


	@Override
	public void addOtherEntity(OtherEntity entity) {
		return;
	}


	@Override
	public void updateOtherEntity(OtherEntity entity) {
		return;
	}


	@Override
	public void removeOtherEntity(String entityId) {
		if(entityId != null && entity != null && entityId.equalsIgnoreCase(entityId)){
			setOwner(null);
		}
	}

	public void setOwner(OtherEntity entity) {
		this.entity = entity;
		
		String entityId = entity == null ? null : entity.id;
		
		boolean allow = entity != null && SessionGeneralSystem.getInstance() != null ? PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.MANAGE_OTHER_ENTITIES) && entityId != null : false;
			
		contactsList.setOwner(entityId);
		contactsList.setOwnerType(BigBangConstants.EntityIds.OTHER_ENTITY);
		contactsList.allowCreation(allow);
		documentsList.setOwner(entityId);
		documentsList.setOwnerType(BigBangConstants.EntityIds.OTHER_ENTITY);
		documentsList.allowCreation(allow);
	}




}
