package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterFormValidator;
import bigBang.module.generalSystemModule.shared.CostCenter;

import com.google.gwt.user.client.ui.HasValue;

public class CostCenterForm extends FormView {

	private FormField<String> nameField;
	private FormField<String> codeField;

	private CostCenter info;

	public CostCenterForm(){
		super();
		setHeight("100%");

		addSection("Informação Geral");
		nameField = new TextBoxFormField("Designação", new CostCenterFormValidator.NameValidator());
		codeField = new TextBoxFormField("Código", new CostCenterFormValidator.CodeValidator());

		addFormField(nameField);
		addFormField(codeField);

		clearInfo();
		
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

	@Override
	public Object getInfo() {
		info.name = this.nameField.getValue();
		info.code = this.codeField.getValue();
		return info;
	}

	@Override
	public void setInfo(Object info) {
		this.info = (CostCenter) info;
		this.nameField.setValue(this.info.name);
		this.codeField.setValue(this.info.code);
	}

	@Override
	public void clearInfo() {
		this.info = new CostCenter();
		this.nameField.setValue("");
		this.codeField.setValue("");
	}

}
