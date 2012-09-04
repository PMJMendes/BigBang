package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.client.dataAccess.InsurancePolicyDataBrokerClient;
import bigBang.definitions.shared.Policy2;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExercisesList;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectsList;

import com.google.gwt.user.client.ui.StackPanel;

public class NewInsurancePolicyChildrenPanel extends View {

	protected Policy2 insurancePolicy;
	protected InsurancePolicyDataBrokerClient policyBrokerClient;

	public InsuredObjectsList insuredObjectsList;
	public ExercisesList exercisesList;

	public NewInsurancePolicyChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		insuredObjectsList = new InsuredObjectsList();
		exercisesList = new ExercisesList();

		wrapper.add(insuredObjectsList, "Unidades de Risco");
		wrapper.add(exercisesList, "Exercícios");
	}

	@Override
	protected void initializeView() {}

	public void setPolicy(Policy2 policy){
		this.insurancePolicy = policy;
		String policyId = policy == null ? null : policy.id;
		this.insuredObjectsList.setOwner(policyId);
		this.exercisesList.setOwner(policyId);
	}

}
