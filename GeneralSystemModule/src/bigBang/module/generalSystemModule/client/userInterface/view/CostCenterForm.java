package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.formValidator.CostCenterFormValidator;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

public class CostCenterForm extends FormView<CostCenter> {

	private FormField<String> nameField;
	private FormField<String> codeField;
	
	private Button editCostCenterButton;
	private Button saveCostCenterButton;
	private Button deleteCostCenterButton;

	public CostCenterForm(){
		super();
		setHeight("100%");

		addSection("Informação Geral");
		nameField = new TextBoxFormField("Designação", new CostCenterFormValidator.NameValidator());
		codeField = new TextBoxFormField("Código", new CostCenterFormValidator.CodeValidator());

		addFormField(nameField);
		addFormField(codeField);

		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
		
		this.addButton(editCostCenterButton);
		this.addButton(saveCostCenterButton);
		this.addButton(deleteCostCenterButton);

		clearInfo();

		setReadOnly(true);
	}

	@Override
	public CostCenter getInfo() {
		CostCenter info = value == null ? new CostCenter() : value;
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
		if(info.name == null)
			this.nameField.clear();
		else
			this.nameField.setValue(info.name);
		if(info.code == null)
			codeField.clear();
		else
			this.codeField.setValue(info.code);
	}

	public HasClickHandlers getSaveButton() {
		return saveCostCenterButton;
	}
	
	public HasClickHandlers getEditButton() {
		return editCostCenterButton;
	}
	
	public HasClickHandlers getDeleteButton() {
		return deleteCostCenterButton;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		saveCostCenterButton.setVisible(!readOnly);
		editCostCenterButton.setVisible(readOnly);
	}

}
