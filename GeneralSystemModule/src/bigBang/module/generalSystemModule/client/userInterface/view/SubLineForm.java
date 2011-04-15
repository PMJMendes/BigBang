package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.SubLine;

public class SubLineForm extends FormView<SubLine> {

	private TextBoxFormField name;
	
	private SubLine subLine;
	
	public SubLineForm(){
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome");
		
		
	}
	
	@Override
	public SubLine getInfo() {
		subLine.name = name.getValue();
		return subLine;
	}

	@Override
	public void setInfo(SubLine info) {
		this.subLine = info != null ? (SubLine) info : new SubLine();
		this.name.setValue(info.name);
	}

}
