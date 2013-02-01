package bigBang.module.receiptModule.client.userInterface.form;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.clientModule.shared.ModuleConstants;

public class VoidDebitNoteForm extends FormView<Receipt.ReturnMessage>{
	
	protected ExpandableListBoxFormField motive;
	
	public VoidDebitNoteForm() {
		addSection("Anulação de Nota de Débito");
		motive = new ExpandableListBoxFormField(ModuleConstants.ListIDs.RECEIPT_RETURN_MOTIVES, "Motivo da anulação");
		addFormField(motive);
		
		setValidator(new VoidDebitNoteFormValidator(this));	}
	
	@Override
	public ReturnMessage getInfo() {
		ReturnMessage ret = value;
		ret.motiveId = motive.getValue();
		return ret;
	}

	@Override
	public void setInfo(ReturnMessage info) {
		return;
	}

}
