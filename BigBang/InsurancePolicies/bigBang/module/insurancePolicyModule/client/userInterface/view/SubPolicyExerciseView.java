package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseForm;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyExerciseViewPresenter;

import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubPolicyExerciseView extends View implements SubPolicyExerciseViewPresenter.Display {

	private ExerciseForm exerciseForm;
	private SubPolicyForm subPolicyForm;
	
	public SubPolicyExerciseView(){
		
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		exerciseForm = new ExerciseForm();
		subPolicyForm = new SubPolicyForm();
		
		VerticalPanel insurancePolicyFormWrapper = new VerticalPanel();
		insurancePolicyFormWrapper.setSize("100%", "100%");
				
		ListHeader insurancePolicyFormHeader = new ListHeader("Apólice Adesão");
		insurancePolicyFormHeader.setHeight("30px");
		insurancePolicyFormWrapper.add(insurancePolicyFormHeader);
		
		subPolicyForm.setSize("100%", "100%");
		subPolicyForm.setReadOnly(true);
		insurancePolicyFormWrapper.add(subPolicyForm);
		insurancePolicyFormWrapper.setCellHeight(subPolicyForm, "100%");
		mainWrapper.addWest(insurancePolicyFormWrapper, 600);
		
		VerticalPanel exerciseFormWrapper = new VerticalPanel();
		exerciseFormWrapper.setSize("100%", "100%");
		
		ListHeader exerciseHeader = new ListHeader("Exercício");
		exerciseHeader.setHeight("30px");
		exerciseFormWrapper.add(exerciseHeader);

		exerciseForm.setReadOnly(true);
		exerciseForm.setSize("100%", "100%");
		exerciseFormWrapper.add(exerciseForm);
		exerciseFormWrapper.setCellHeight(exerciseForm, "100%");
		mainWrapper.add(exerciseFormWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public ExerciseForm getExerciseForm() {
		return exerciseForm;
	}

	@Override
	public HasEditableValue<SubPolicy> getSubPolicyForm() {
		return this.subPolicyForm;
	}

}
