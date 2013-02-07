package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestHeaderForm;

public class QuoteRequestSendMessageView extends SendMessageView<QuoteRequest>{

	public QuoteRequestSendMessageView() {
		super(new QuoteRequestHeaderForm());

	}
}
