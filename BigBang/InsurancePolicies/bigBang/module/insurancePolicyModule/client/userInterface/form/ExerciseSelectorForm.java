package bigBang.module.insurancePolicyModule.client.userInterface.form;

import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ExerciseSelectorForm extends FormView<ExerciseData>{

	protected ListBoxFormField exercises;
	protected DatePickerFormField startDate;
	protected DatePickerFormField endDate;
	private Button newButton;
	private ExerciseData value;

	public ExerciseSelectorForm() {
		FormViewSection section = new FormViewSection("");
		section.showHeader(false);
		addSection(section);

		HorizontalPanel wrapper = new HorizontalPanel();
		exercises = new ListBoxFormField("Exercício");
		startDate = new DatePickerFormField("Data de início");
		endDate = new DatePickerFormField("Data de fim");
		newButton = new Button("Abrir Próximo Exercício");

		wrapper.add(exercises);
		wrapper.add(startDate);
		wrapper.add(endDate);
		wrapper.setCellHorizontalAlignment(exercises, HasHorizontalAlignment.ALIGN_LEFT);
		wrapper.setCellHorizontalAlignment(startDate, HasHorizontalAlignment.ALIGN_LEFT);
		wrapper.setCellHorizontalAlignment(endDate, HasHorizontalAlignment.ALIGN_LEFT);

		wrapper.add(newButton);
		wrapper.setCellHorizontalAlignment(newButton, HasHorizontalAlignment.ALIGN_RIGHT);		
		wrapper.setCellVerticalAlignment(newButton, HasVerticalAlignment.ALIGN_MIDDLE);
		wrapper.setCellWidth(newButton, "100%");
		wrapper.setSize("100%", "100%");
		newButton.getElement().getStyle().setMarginRight(25, Unit.PX);

		registerFormField(exercises);
		registerFormField(startDate);
		registerFormField(endDate);

		addWidget(wrapper);
		
		setValidator(new ExerciseSelectorFormValidator(this));
	}

	public HasValue<String> getExerciseSelector(){
		return exercises;
	}


	@Override
	public ExerciseData getInfo() {
		ExerciseData newValue = value;

		if(value != null){
			newValue.startDate = startDate.getStringValue();
			newValue.endDate = endDate.getStringValue();
		}

		return newValue;
	}

	public String getCurrentId(){
		return exercises.getValue();
	}

	@Override
	public void setInfo(ExerciseData info) {
		this.value = info;
		if(info == null){
			clearInfo();
			return;
		}

		exercises.setValue(info.id, false);
		startDate.setValue(info.startDate);
		endDate.setValue(info.endDate);
	}

	public void setAvailableExercises(ExerciseData[] availableExs){
		if(availableExs == null || availableExs.length == 0){
			this.setVisible(false);
			clearInfo();
			return;
		}
		exercises.clearValues();
		for(int i = 0; i<availableExs.length; i++){
			exercises.addItem(availableExs[i].label, availableExs[i].id);
		}
		exercises.removeItem(0);
		this.setVisible(true);
	}

	public void addAvailableExercise(ExerciseData newExercise){
		exercises.addItem(newExercise.label, newExercise.id);
	}

	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(newButton != null) {
			newButton.setVisible(!readOnly);
		}
		if(exercises != null) {
			exercises.setReadOnly(false);
		}
	}

	public void allowCreateExercise(boolean allow) {
		newButton.setEnabled(allow);
	}

	public HasClickHandlers getNewButton() {
		return newButton;
	}

}
