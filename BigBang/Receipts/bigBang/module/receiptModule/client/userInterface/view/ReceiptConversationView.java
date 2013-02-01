package bigBang.module.receiptModule.client.userInterface.view;

import bigBang.definitions.shared.Receipt;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.receiptModule.client.userInterface.form.ReceiptForm;

public class ReceiptConversationView extends ConversationView<Receipt>{

	public ReceiptConversationView() {
		super(new ReceiptForm());
	}

}
