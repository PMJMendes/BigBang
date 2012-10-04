package bigBang.module.receiptModule.client.userInterface.form;
import bigBang.definitions.shared.Receipt.ReturnMessage;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.clientModule.shared.ModuleConstants;

public class ReceiptReturnForm extends FormView<ReturnMessage>{

	ExpandableListBoxFormField motive;
	String id;
	
	public ReceiptReturnForm(){
		
		motive = new ExpandableListBoxFormField(ModuleConstants.ListIDs.RECEIPT_RETURN_MOTIVES, "Motivo");
		addSection("Motivo");
		addFormField(motive);
		
	}
	
	@Override
	public ReturnMessage getInfo() {
		
		ReturnMessage newMessage = getValue();
		newMessage.motiveId = motive.getValue();		
		newMessage.receiptId = id;
		return newMessage;
		
	}

	@Override
	public void setInfo(ReturnMessage info) {
		clear();
		motive.setValue(info.motiveId);
		id = info.receiptId;
	}

	private void clear() {
		
		motive.setValue("");
		
	}

}
