package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.view.FormViewSection;

public class NewAppointmentItemSection extends FormViewSection{

	public Button newButton;
	
	public NewAppointmentItemSection(){
		super("");
		newButton = new Button("Acrescentar Consulta");
		addWidget(newButton);
	}
;	
	public NewAppointmentItemSection(String title) {
		super(title);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setVisible(!readOnly);
	}

	public HasClickHandlers getNewButton() {
		return newButton;
	};
}
