package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.ViewExternalRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationForm;

public class NegotiationExternalInfoRequestView extends
		ViewExternalRequestView<Negotiation> {

	public NegotiationExternalInfoRequestView() {
		super(new NegotiationForm());
	}

}
