package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class ReopenCasualtyForm extends FormView<String>{
	
	protected ExpandableListBoxFormField reason;
	
	public ReopenCasualtyForm() {
		reason = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CASUALTY_SUB_CASUALTY_REOPEN, "Motivo");
		addSection("Reabertura de Sinistro");
		addFormField(reason);
		
		setValidator(new ReopenCasualtyFormValidator(this));
	}

	@Override
	public String getInfo() {
		return reason.getValue();
	}

	@Override
	public void setInfo(String info) {
		reason.setValue(info);
	}
}
