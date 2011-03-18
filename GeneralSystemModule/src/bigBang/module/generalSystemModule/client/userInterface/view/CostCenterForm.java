package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;

import com.google.gwt.user.client.ui.HasValue;

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
