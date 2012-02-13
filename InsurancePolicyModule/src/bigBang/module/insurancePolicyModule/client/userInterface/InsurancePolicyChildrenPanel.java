package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.ui.StackPanel;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.ContactsList;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.HistoryList;
import bigBang.library.client.userInterface.view.View;

public class InsurancePolicyChildrenPanel extends View {

	protected InsurancePolicy insurancePolicy;
	protected InsurancePolicyDataBrokerClient policyBrokerClient;

	public ContactsList contactsList;
	public DocumentsList documentsList;
	public InsuredObjectsList insuredObjectsList;
	public ExercisesList exercisesList;
	public HistoryList historyList;

	public InsurancePolicyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		contactsList = new ContactsList();
		documentsList = new DocumentsList();
		insuredObjectsList = new InsuredObjectsList();
		exercisesList = new ExercisesList();
		historyList = new HistoryList();

		wrapper.add(contactsList, "Contactos");
		wrapper.add(documentsList, "Documentos");
		wrapper.add(insuredObjectsList, "Unidades de Risco");
		wrapper.add(exercisesList, "Exercícios");
		wrapper.add(historyList, "Histórico");

		this.policyBrokerClient = getPolicyBrokerClient();
		((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY)).registerClient(this.policyBrokerClient);
	}

	@Override
	protected void initializeView() {}

	public void setPolicy(InsurancePolicy policy){
		this.insurancePolicy = policy;
		String policyId = policy == null ? null : policy.id;
		this.contactsList.setOwner(policyId);
		this.documentsList.setOwner(policyId);
		this.insuredObjectsList.setOwner(policyId);
		this.exercisesList.setOwner(policyId);
		this.historyList.setOwner(policyId);
	}

	protected InsurancePolicyDataBrokerClient getPolicyBrokerClient(){
		return new InsurancePolicyDataBrokerClient() {
			protected int version;

			@Override
			public void setDataVersionNumber(String dataElementId, int number) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					this.version = number;
				}
			}

			@Override
			public int getDataVersion(String dataElementId) {
				if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.INSURANCE_POLICY)){
					return this.version;
				}
				return -1;
			}

			@Override
			public void updateInsurancePolicy(InsurancePolicy policy) {
				return;
			}

			@Override
			public void removeInsurancePolicy(String policyId) {
				if(insurancePolicy != null && insurancePolicy.id != null && policyId.equalsIgnoreCase(insurancePolicy.id)){
					setPolicy(null);
				}
			}

			@Override
			public void addInsurancePolicy(InsurancePolicy policy) {
				return;
			}

			@Override
			public void remapItemId(String oldId, String newId) {
				return;
			}
		};
	}

}
