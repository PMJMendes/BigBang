package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.client.types.SubLine;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubLineForm extends FormView<SubLine> {

	private TextBoxFormField name;
	private Button saveButton;
	
	private SubLine subLine;
	
	public SubLineForm(){
		addSection("Informação Geral");
		
		name = new TextBoxFormField("Nome");
		
		addFormField(name);
		
		saveButton = new Button("Guardar");
		addButton(saveButton);
	}
	
	@Override
	public SubLine getInfo() {
		if(this.subLine == null)
			this.subLine = new SubLine();
		subLine.name = name.getValue();
		return subLine;
	}

	@Override
	public void setInfo(SubLine info) {
		this.subLine = info != null ? (SubLine) info : new SubLine();
		this.name.setValue(info.name);
	}

	@Override
	public void clearInfo() {
		subLine = new SubLine();
		super.clearInfo();
	}

	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
}
