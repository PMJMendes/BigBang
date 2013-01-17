package bigBang.module.generalSystemModule.client.userInterface.form;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.OtherEntity;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class OtherEntityForm extends FormView<OtherEntity>{

	protected TextBoxFormField name;
	protected ExpandableListBoxFormField type;
	protected TextAreaFormField notes;

	public OtherEntityForm() {
		name = new TextBoxFormField("Nome");
		type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.OTHER_ENTITIES_TYPE, "Tipo");
		notes = new TextAreaFormField("Notas");

		addSection("Informação Geral");

		addFormField(name);
		addFormField(type);
		addFormField(notes);

		setValidator(new OtherEntityFormValidator(this));
	}

	@Override
	public OtherEntity getInfo() {
		OtherEntity toReturn = value == null ? new OtherEntity() : value;

		toReturn.name = name.getValue();
		toReturn.type = type.getValue();
		toReturn.notes = notes.getValue();

		return toReturn;
	}

	@Override
	public void setInfo(OtherEntity info) {

		name.setValue(info.name);
		type.setValue(info.type);
		notes.setValue(info.notes);
		
	}

}
