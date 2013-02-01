package bigBang.module.quoteRequestModule.client.userInterface.view;


import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.ReceiveMessageView;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;

public class NegotiationReceiveMessageView extends ReceiveMessageView<Negotiation>{

	public NegotiationReceiveMessageView() {
		super(new NegotiationForm());
		}

	@Override
	protected void initializeView() {
		return;
	}
	
}
