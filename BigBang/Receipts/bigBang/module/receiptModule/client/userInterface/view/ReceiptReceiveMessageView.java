package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;

public class ReceiptReceiveMessageView extends ReceiveMessageView<Receipt>{

	public ReceiptReceiveMessageView() {
		super(new ReceiptForm());
	}

}
