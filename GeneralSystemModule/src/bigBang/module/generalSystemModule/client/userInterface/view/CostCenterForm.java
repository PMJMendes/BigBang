package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.formValidator.CostCenterFormValidator;

import com.google.gwt.user.client.ui.HasValue;

public class CostCenterForm extends FormView<CostCenter> {

	private FormField<String> nameField;
	private FormField<String> codeField;

	public CostCenterForm(){
		super();
		setHeight("100%");

		addSection("Informação Geral");
		nameField = new TextBoxFormField("Designação", new CostCenterFormValidator.NameValidator());
		codeField = new TextBoxFormField("Código", new CostCenterFormValidator.CodeValidator());

		addFormField(nameField);
		addFormField(codeField);

		clearInfo();

		setReadOnly(true);
	}

	public HasValue<String> getNameField() {
		return nameField;
	}

	public HasValue<String> getCodeField() {
		return codeField;
	}

	@Override
	public CostCenter getInfo() {
		CostCenter info = new CostCenter();
		info.name = this.nameField.getValue();
		info.code = this.codeField.getValue();
		return info;
	}

	@Override
	public void setInfo(CostCenter info) {
		if(info == null){
			clearInfo();
			return;
		}
		this.nameField.setValue(info.name == null ? "" : info.name);
		this.codeField.setValue(info.code == null ? "" : info.code);
	}

	@Override
	public void clearInfo() {
		setValue(new CostCenter());
		this.nameField.setValue("");
		this.codeField.setValue("");
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
