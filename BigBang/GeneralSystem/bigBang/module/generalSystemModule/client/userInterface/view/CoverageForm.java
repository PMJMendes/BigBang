package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Coverage;
import bigBang.library.client.userInterface.CheckBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class CoverageForm extends FormView<Coverage> {

	private TextBoxFormField name;
	private CheckBoxFormField isMandatory;
	private CheckBoxFormField isHeader;
	
	private Coverage coverage;
	
	public CoverageForm(){
		addSection("Detalhes da cobertura");
		
		name = new TextBoxFormField("Nome");
		isMandatory = new CheckBoxFormField("Obrigatória");
		isHeader = new CheckBoxFormField("Cabeçalho");
		
		addFormField(name);
		addFormField(isMandatory);
		addFormField(isHeader);
		
	}
	
	@Override
	public Coverage getInfo() {
		if(this.coverage == null)
			this.coverage = new Coverage();
		coverage.name = name.getValue();
		coverage.isMandatory = isMandatory.getValue();
		coverage.isHeader = isHeader.getValue();
		return coverage;
	}

	@Override
	public void setInfo(Coverage info) {
		this.coverage = info != null ? (Coverage) info : new Coverage();
		this.name.setValue(info.name);
		this.isHeader.setValue(info.isHeader);
		this.isMandatory.setValue(info.isMandatory);
	}

	@Override
	public void clearInfo() {
		coverage = new Coverage();
		super.clearInfo();
	}
}
