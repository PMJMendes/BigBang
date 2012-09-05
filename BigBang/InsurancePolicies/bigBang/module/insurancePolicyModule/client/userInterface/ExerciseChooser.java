package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ExerciseChooser extends View implements HasValue<ExerciseData[]>{

	private ExpandableListBoxFormField exercises;
	private DatePickerFormField startDate;
	private DatePickerFormField endDate;
	private Button newButton;
	private ExerciseData[] value;

	public ExerciseChooser() {

		HorizontalPanel wrapper = new HorizontalPanel();
		initWidget(wrapper);
		exercises = new ExpandableListBoxFormField("Exercício");
		startDate = new DatePickerFormField("Data de início");
		endDate = new DatePickerFormField("Data de fim");
		newButton = new Button("Novo Exercício");
		
		exercises.setEditable(false);
		
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
		
		exercises.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(value != null){
					for(int i = 0; i<value.length; i++){
						if(event.getValue().equalsIgnoreCase(value[i].id)){
							startDate.setValue(value[i].startDate);
							endDate.setValue(value[i].endDate);
							break;
						}
					}
				}
			}
		});
	}


	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ExerciseData[]> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}


	@Override
	public ExerciseData[] getValue() {
		return value;
	}

	public String getCurrentId(){
		return exercises.getValue();
	}

	@Override
	public void setValue(ExerciseData[] value) {
		setValue(value, true);
	}


	@Override
	public void setValue(ExerciseData[] value, boolean fireEvents) {
		this.value = value;

		exercises.clearValues();
		
		for(int i = 0; i<value.length; i++){
			exercises.addItem(value[i].label, value[i].id);
		}
		exercises.removeItem(0);

		if(fireEvents){
			ValueChangeEvent.fire(this, value);
		}
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

}
