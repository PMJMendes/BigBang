package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.view.FormViewSection;

public class NewExceptionSection extends FormViewSection{

	public Button newButton;

	public NewExceptionSection() {
		super("");
		newButton = new Button("Nova Excepção");
		addWidget(newButton);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setVisible(!readOnly);
	}


}
