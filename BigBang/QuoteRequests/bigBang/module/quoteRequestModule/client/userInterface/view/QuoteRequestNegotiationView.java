package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.module.quoteRequestModule.client.userInterface.form.QuoteRequestHeaderForm;

public class QuoteRequestNegotiationView extends NegotiationView<QuoteRequest>{

	public QuoteRequestNegotiationView() {
		super(new QuoteRequestHeaderForm());
		setParentHeaderTitle("Ficha da Consulta de Mercado");
	}

	@Override
	public void setParentHeaderTitle(String title) {
		
	}

}
