package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.shared.SubLine;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class SubLineForm extends FormView<SubLine> {

	protected TextBoxFormField name;
	protected ExpandableListBoxFormField type, periodType;
	protected NumericTextBoxFormField percentage;
	protected RadioButtonFormField isLife;
	protected TextBoxFormField description;
	protected SubLine subLine;
	
	
	public SubLineForm(){
		addSection("Detalhes da modalidade");
		name = new TextBoxFormField("Nome");
		type = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ObjectTypes, "Tipo de unidade de risco");
		type.allowEdition(false);
		periodType = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ExercisePeriod, "Tipo de período");
		periodType.allowEdition(false);
		percentage = new NumericTextBoxFormField("Angariação", false);
		percentage.setUnitsLabel("%");
		isLife = new RadioButtonFormField("Vida");
		isLife.addOption("1", "Sim");
		isLife.addOption("0", "Não");
		description = new TextBoxFormField("Descrição");
		addFormField(name, false);
		addFormField(description, false);
		addFormField(type, true);
		addFormField(periodType, true);
		addFormField(percentage, true);
		addFormField(isLife, true);
		setValidator(new SubLineFormValidator(this));
	}
	
	@Override
	public SubLine getInfo() {
		if(this.subLine == null)
			this.subLine = new SubLine();
		subLine.name = name.getValue();
		subLine.objectTypeId = type.getValue();
		subLine.exercisePeriodId = periodType.getValue();
		subLine.description = description.getValue();
		subLine.commissionPercent = percentage.getValue();
		String isLifeValue = isLife.getValue();
		subLine.isLife = isLifeValue == null ? null : isLifeValue.equalsIgnoreCase("1") ? true : false;
		return subLine;
	}

	@Override
	public void setInfo(SubLine info) {
		this.subLine = info != null ? (SubLine) info : new SubLine();
		this.name.setValue(info.name);
		this.type.setValue(info.objectTypeId);
		this.periodType.setValue(info.exercisePeriodId);
		this.description.setValue(info.description);
		this.isLife.setValue(info.isLife == null ? null : info.isLife ? "1" : "0");
		this.percentage.setValue(info.commissionPercent);
	}

	@Override
	public void clearInfo() {
		subLine = new SubLine();
		super.clearInfo();
	}

}
