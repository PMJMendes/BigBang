package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.ConversationView;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;

public class NegotiationConversationView extends ConversationView<Negotiation>{

	public NegotiationConversationView() {
		super(new NegotiationForm());
	}

}
