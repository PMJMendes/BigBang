package bigBang.module.casualtyModule.client.userInterface;

import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.user.client.ui.Button;

public class NewSubCasualtyInsurerRequestSection extends FormViewSection {

	public Button newButton;
	
	public NewSubCasualtyInsurerRequestSection() {
		super("");
		newButton = new Button("Acrescentar Pedido de Segurador");
		addWidget(newButton);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setVisible(!readOnly);
	}

}