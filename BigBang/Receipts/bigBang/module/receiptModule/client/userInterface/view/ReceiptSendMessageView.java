package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;

public class ReceiptSendMessageView extends SendMessageView<Receipt>{

	public ReceiptSendMessageView() {
		super(new ReceiptForm());
	}

}
