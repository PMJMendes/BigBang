package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.shared.FormField;
import bigBang.library.shared.userInterface.TextBoxFormField;
import bigBang.library.shared.userInterface.view.FormView;
import bigBang.library.shared.userInterface.view.FormViewSection;
import bigBang.module.generalSystemModule.client.userInterface.UserList;

public class CostCenterForm extends FormView {

	private FormField<String> nameField;
	private FormField<String> codeField;
	
	public CostCenterForm(){
		super();
		setHeight("100%");

		addSection("Informação Geral");
		nameField = new TextBoxFormField("Designação");
		codeField = new TextBoxFormField("Código");
		
		addFormField(nameField);
		addFormField(codeField);
		
		FormViewSection section = new FormViewSection("Membros");
		panel.add(section.getHeader());
		
		setReadOnly(true);
	}

	public HasValue<String> getNameField() {
		return nameField;
	}
	
	public HasValue<String> getCodeField() {
		return codeField;
	}
	
}
