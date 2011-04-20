package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.Coverage;

public class CoverageForm extends FormView<Coverage> {

	private TextBoxFormField name;
	private Button saveButton;
	
	private Coverage coverage;
	
	public CoverageForm(){
		addSection("Informação Geral");
		
		name = new TextBoxFormField("Nome");
		
		addFormField(name);
		
		saveButton = new Button("Guardar");
		addButton(saveButton);
	}
	
	@Override
	public Coverage getInfo() {
		if(this.coverage == null)
			this.coverage = new Coverage();
		coverage.name = name.getValue();
		return coverage;
	}

	@Override
	public void setInfo(Coverage info) {
		this.coverage = info != null ? (Coverage) info : new Coverage();
		this.name.setValue(info.name);
	}

	@Override
	public void clearInfo() {
		coverage = new Coverage();
		super.clearInfo();
	}

	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
}
