package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.dataAccess.InsuranceAgencyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.user.client.ui.StackPanel;

public class InsuranceAgencyChildrenPanel extends View {

	protected InsuranceAgencyBroker broker;
	protected InsuranceAgencyDataBrokerClient brokerClient;
	
	protected InsuranceAgency agency;
	
	public ContactsList contactsList;
	public DocumentsList documentsList;
	
	public InsuranceAgencyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		
		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		
		this.brokerClient = getBrokerClient();
		((InsuranceAgencyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_AGENCY)).registerClient(this.brokerClient);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	public void setOwner(InsuranceAgency owner) {
		this.agency = owner;
		String agencyId = owner == null ? null : owner.id;
		
		boolean allow = owner != null && SessionGeneralSystem.getInstance() != null ? PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), BigBangConstants.OperationIds.GeneralSystemProcess.MANAGE_COMPANIES) && agencyId != null : false;
		this.contactsList.setOwner(agencyId);
		this.contactsList.setOwnerType(BigBangConstants.EntityIds.INSURANCE_AGENCY);
		this.contactsList.allowCreation(allow);
		this.documentsList.setOwner(agencyId);	
		this.documentsList.setOwnerType(BigBangConstants.EntityIds.INSURANCE_AGENCY);
		this.documentsList.allowCreation(allow);

	}

	protected InsuranceAgencyDataBrokerClient getBrokerClient(){
		return new InsuranceAgencyDataBrokerClient() {
			
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
			public void updateInsuranceAgency(InsuranceAgency insuranceAgency) {
				return;
			}
			
			@Override
			public void setInsuranceAgencies(InsuranceAgency[] insuranceAgencys) {
				return;
			}
			
			@Override
			public void removeInsuranceAgency(String insuranceAgencyId) {
				if(insuranceAgencyId != null && agency != null && insuranceAgencyId.equalsIgnoreCase(agency.id)){
					setOwner(null);
				}
			}
			
			@Override
			public void addInsuranceAgency(InsuranceAgency insuranceAgency) {
				return;
			}
		};
	}
	
}
