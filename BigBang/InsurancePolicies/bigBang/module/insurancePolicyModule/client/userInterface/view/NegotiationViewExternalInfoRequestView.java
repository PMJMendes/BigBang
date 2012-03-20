package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.ViewExternalRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationForm;

public class NegotiationViewExternalInfoRequestView extends
		ViewExternalRequestView<Negotiation> {

	public NegotiationViewExternalInfoRequestView() {
		super(new NegotiationForm());
	}

}
