package bigBang.module.casualtyModule.client.userInterface;

import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.user.client.ui.Button;

public class NewSubCasualtyFramingEntitySection extends FormViewSection {

	public Button newButton;
	
	public NewSubCasualtyFramingEntitySection() {
		super("");
		newButton = new Button("Acrescentar interveniente");
		addWidget(newButton);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setVisible(!readOnly);
	}

}
