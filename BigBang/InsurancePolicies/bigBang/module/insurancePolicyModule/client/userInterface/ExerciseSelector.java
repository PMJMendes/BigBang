package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ExerciseSelector extends View implements HasValue<ExerciseData>{

	private ListBoxFormField exercises;
	private DatePickerFormField startDate;
	private DatePickerFormField endDate;
	private Button newButton;
	private ExerciseData value;

	public ExerciseSelector() {

		HorizontalPanel wrapper = new HorizontalPanel();
		initWidget(wrapper);
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
		newButton.getElement().getStyle().setMarginRight(5, Unit.PX);

		wrapper.setStyleName("bigBangExerciseChooser");

	}

	public HasValue<String> getExerciseSelector(){
		return exercises;
	}


	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ExerciseData> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}


	@Override
	public ExerciseData getValue() {
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
	public void setValue(ExerciseData value) {
		setValue(value, true);
	}


	@Override
	public void setValue(ExerciseData value, boolean fireEvents) {
		if(value == null){
			return;
		}
		this.value = value;

		exercises.setValue(value.id);
		startDate.setValue(value.startDate);
		endDate.setValue(value.endDate);

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
	}

	public void setAvailableExercises(ExerciseData[] availableExs){

		exercises.clearValues();

		for(int i = 0; i<availableExs.length; i++){
			exercises.addItem(availableExs[i].label, availableExs[i].id);
		}
		
		exercises.removeItem(0);

	}

	public void addAvailableExercise(ExerciseData newExercise){
		exercises.addItem(newExercise.label, newExercise.id);
	}


	@Override
	protected void initializeView() {
		return;		
	}


	public void setReadOnly(boolean readOnly) {
		startDate.setReadOnly(readOnly);
		endDate.setReadOnly(readOnly);
		newButton.setVisible(!readOnly);
	}

	public void allowCreateExercise(boolean allow) {
		newButton.setEnabled(allow);
	}

	public HasClickHandlers getNewButton() {
		return newButton;
	}

}
