package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestHeaderForm;

public class QuoteRequestConversationView extends ConversationView<QuoteRequest>{

	public QuoteRequestConversationView() {
		super(new QuoteRequestHeaderForm());
	}
	
}
