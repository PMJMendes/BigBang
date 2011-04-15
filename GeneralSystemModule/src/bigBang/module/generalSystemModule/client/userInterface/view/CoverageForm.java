package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;

import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.Coverage;

public class CoverageForm extends FormView<Coverage> {

	private Button editCostCenterButton;
	private Button saveCostCenterButton;
	private Button deleteCostCenterButton;


	private TextBoxFormField name;

	private Coverage coverage;

	public CoverageForm(){
		addSection("Informação Geral");
		name = new TextBoxFormField("Nome");
		
		this.editCostCenterButton = new Button("Editar");	
		this.saveCostCenterButton = new Button("Guardar");
		this.deleteCostCenterButton = new Button("Apagar");
	}

	@Override
	public Coverage getInfo() {
		coverage.name = name.getValue();
		return coverage;
	}

	@Override
	public void setInfo(Coverage info) {
		this.coverage = info != null ? (Coverage) info : new Coverage();
		this.name.setValue(info.name);
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
