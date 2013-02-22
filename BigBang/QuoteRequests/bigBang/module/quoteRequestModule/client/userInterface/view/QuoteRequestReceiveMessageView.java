package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestHeaderForm;

public class QuoteRequestReceiveMessageView extends ReceiveMessageView<QuoteRequest>{

	public QuoteRequestReceiveMessageView() {
		super(new QuoteRequestHeaderForm());
	}

}
