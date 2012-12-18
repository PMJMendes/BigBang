package bigBang.module.casualtyModule.client.userInterface.form;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;

public class SubCasualtyReopenForm extends FormView<String[]>{

	protected ExpandableListBoxFormField subCasualties;
	protected ExpandableListBoxFormField reason;


	public SubCasualtyReopenForm() {
		reason = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CASUALTY_SUB_CASUALTY_REOPEN, "Motivo");
		subCasualties = new ExpandableListBoxFormField("Sub-Sinistros");
		subCasualties.allowEdition(false);
		addSection("Reabertura de Sub-Sinistro");

		addFormField(subCasualties);
		addFormField(reason);

		setValidator(new SubCasualtyReopenFormValidator(this));
	}

	@Override
	public String[] getInfo() {
		return new String[]{subCasualties.getValue(), reason.getValue()};
	}

	@Override
	public void setInfo(String[] info) {
		subCasualties.setValue(info[0]);
		reason.setValue(info[1]);
	}

	public void addItem(TipifiedListItem item) {
		subCasualties.addItem(item);
	}

	public void clearItems() {
		subCasualties.clearValues();
	}

}
