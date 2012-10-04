package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.shared.Coverage;
import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CoverageForm extends FormView<Coverage> {

	protected TextBoxFormField name;
	protected CheckBoxFormField isMandatory;
	protected CheckBoxFormField isHeader;
	protected NumericTextBoxFormField order;
	
	private Coverage coverage;
	
	public CoverageForm(){
		addSection("Detalhes da cobertura");
		
		name = new TextBoxFormField("Nome");
		isMandatory = new CheckBoxFormField("Obrigatória");
		isHeader = new CheckBoxFormField("Cabeçalho");
		order = new NumericTextBoxFormField("Índice", false);
		
		addFormField(name, false);
		addFormFieldGroup(new FormField[]{
				isMandatory,
				isHeader
		}, true);
		addFormFieldGroup(new FormField[]{
				order
		}, true);
		setValidator(new CoverageFormValidator(this));
	}
	
	@Override
	public Coverage getInfo() {
		if(this.coverage == null)
			this.coverage = new Coverage();
		coverage.name = name.getValue();
		coverage.isMandatory = isMandatory.getValue();
		coverage.isHeader = isHeader.getValue();
		
		Double orderValue = order.getValue();
		coverage.order = orderValue == null ? null : orderValue.intValue();
		return coverage;
	}

	@Override
	public void setInfo(Coverage info) {
		this.coverage = info != null ? (Coverage) info : new Coverage();
		this.name.setValue(info.name);
		this.isHeader.setValue(info.isHeader);
		this.isMandatory.setValue(info.isMandatory);
		this.order.setValue(info.order == null ? null : info.order.doubleValue());
	}

	@Override
	public void clearInfo() {
		coverage = new Coverage();
		super.clearInfo();
	}
}
