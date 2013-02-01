package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.shared.MedicalFile;
import bigBang.definitions.shared.MedicalFile.Appointment;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class AppointmentForm extends FormView<Appointment>{

	public Button removeButton;

	public TextBoxFormField label;
	public DatePickerFormField date;

	public AppointmentForm() {

		date = new DatePickerFormField("Data");
		label = new TextBoxFormField("Descrição");

		addSection("");
		currentSection.getHeader().setHeight("2px");

		addFormField(date, true);
		addFormField(label, true);

		removeButton = new Button("Remover");

		addWidget(removeButton);
		
		setValidator(new AppointmentFormValidator(this));

	}

	public HasClickHandlers getRemoveButton(){
		return removeButton;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if(removeButton != null){
			removeButton.setVisible(!readOnly);
		}
		super.setReadOnly(readOnly);
	}


	@Override
	public Appointment getInfo() {

		MedicalFile.Appointment app = value;

		app.date = date.getStringValue();
		app.label = label.getValue();

		return app;	
	}

	@Override
	public void setInfo(Appointment info) {

		date.setValue(info.date);
		label.setValue(info.label);

	}
}
