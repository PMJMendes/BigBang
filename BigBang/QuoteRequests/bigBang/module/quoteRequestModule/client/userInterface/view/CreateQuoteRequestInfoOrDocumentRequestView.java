package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestForm;

public class CreateQuoteRequestInfoOrDocumentRequestView extends SendMessageView<QuoteRequest> {

	public CreateQuoteRequestInfoOrDocumentRequestView(){
		super(new QuoteRequestForm());
	}

}
