package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.ViewInfoOrDocumentRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestForm;

public class ViewQuoteRequestInfoOrDocumentRequestView extends ViewInfoOrDocumentRequestView<QuoteRequest> {

	public ViewQuoteRequestInfoOrDocumentRequestView() {
		super(new QuoteRequestForm());
	}

}
