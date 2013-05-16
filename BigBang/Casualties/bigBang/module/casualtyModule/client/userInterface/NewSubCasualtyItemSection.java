package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.view.FormViewSection;

public class NewSubCasualtyItemSection extends FormViewSection {

	public Button newButton;
	
	public NewSubCasualtyItemSection() {
		super("");
		newButton = new Button("Acrescentar Detalhe");
		addWidget(newButton);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setVisible(!readOnly);
	}

}
