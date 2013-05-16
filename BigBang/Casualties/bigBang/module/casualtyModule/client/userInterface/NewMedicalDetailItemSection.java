package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.view.FormViewSection;

public class NewMedicalDetailItemSection extends FormViewSection {

	public Button newButton;
	
	public NewMedicalDetailItemSection() {
		super("");
		newButton = new Button("Acrescentar Detalhe");
		addWidget(newButton);
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
