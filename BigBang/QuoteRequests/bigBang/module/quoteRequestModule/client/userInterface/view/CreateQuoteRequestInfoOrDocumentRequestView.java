package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.InfoOrDocumentRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestForm;

public class CreateQuoteRequestInfoOrDocumentRequestView extends InfoOrDocumentRequestView<QuoteRequest> {

	public CreateQuoteRequestInfoOrDocumentRequestView(){
		super(new QuoteRequestForm());
	}

}
