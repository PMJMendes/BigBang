package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;

public class InsurancePolicyNegotiationConversationView extends ConversationView<Negotiation>{

	public InsurancePolicyNegotiationConversationView() {
		super(new NegotiationForm());
	}

}
