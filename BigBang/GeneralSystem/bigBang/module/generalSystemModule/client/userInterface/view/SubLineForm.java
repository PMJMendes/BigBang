package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.SubLine;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class SubLineForm extends FormView<SubLine> {

	private TextBoxFormField name;
	private ExpandableListBoxFormField type, periodType;
	private SubLine subLine;
	
	
	public SubLineForm(){
		addSection("Detalhes da modalidade");
		name = new TextBoxFormField("Nome");
		type = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ObjectTypes, "Tipo de unidade de risco");
		type.setEditable(false);
		periodType = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ExercisePeriod, "Tipo de período");
		periodType.setEditable(false);
		addFormField(name);
		addFormField(type);
		addFormField(periodType);
		
	}
	
	@Override
	public SubLine getInfo() {
		if(this.subLine == null)
			this.subLine = new SubLine();
		subLine.name = name.getValue();
		subLine.objectTypeId = type.getValue();
		subLine.exercisePeriodId = periodType.getValue();
		return subLine;
	}

	@Override
	public void setInfo(SubLine info) {
		this.subLine = info != null ? (SubLine) info : new SubLine();
		this.name.setValue(info.name);
		this.type.setValue(info.objectTypeId);
		this.periodType.setValue(info.exercisePeriodId);
	}

	@Override
	public void clearInfo() {
		subLine = new SubLine();
		super.clearInfo();
	}

}
