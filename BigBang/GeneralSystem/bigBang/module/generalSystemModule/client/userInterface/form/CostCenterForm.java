package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.client.dataAccess.CostCenterDataBrokerClient;
import bigBang.definitions.shared.CostCenter;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CostCenterForm extends FormView<CostCenter> implements CostCenterDataBrokerClient {

	protected FormField<String> nameField;
	protected FormField<String> codeField;
	
	protected int dataVersionNumber;

	public CostCenterForm(){
		super();
		setHeight("100%");

		addSection("Informação Geral");
		nameField = new TextBoxFormField("Designação");
		codeField = new TextBoxFormField("Código");

		addFormField(nameField);
		addFormField(codeField);

		clearInfo();

		setReadOnly(true);
		setValidator(new CostCenterFormValidator(this));
	}

	@Override
	public CostCenter getInfo() {
		CostCenter info = value == null ? new CostCenter() : new CostCenter(value);
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

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		this.dataVersionNumber = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return dataVersionNumber;
	}

	@Override
	public void setCostCenters(CostCenter[] costCenters) {}

	@Override
	public void addCostCenter(CostCenter costCenter) {}

	@Override
	public void updateCostCenter(CostCenter costCenter) {
		if(this.value != null && this.value.id.equals(costCenter.id)) {
			this.setValue(costCenter);
		}
	}

	@Override
	public void removeCostCenter(String costCenterId) {
		if(this.value != null && this.value.id.equals(costCenterId)) {
			this.clearInfo();
			this.setValue(null);
		}
	}

}
