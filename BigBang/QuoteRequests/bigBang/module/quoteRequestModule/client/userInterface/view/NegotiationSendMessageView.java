package bigBang.module.quoteRequestModule.client.userInterface.view;

import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.SendMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;

public class NegotiationSendMessageView extends SendMessageView<Negotiation>{

	public NegotiationSendMessageView() {
		super(new NegotiationForm());
	}

}
