package bigBang.module.quoteRequestModule.client.userInterface.view;


import bigBang.definitions.shared.Negotiation;
import bigBang.library.client.userInterface.view.ExternalRequestView;
import bigBang.module.quoteRequestModule.client.userInterface.form.NegotiationForm;

public class NegotiationExternalRequestView extends ExternalRequestView<Negotiation>{

	public NegotiationExternalRequestView() {
		super(new NegotiationForm());
		
		setParentHeaderTitle("Ficha da Negociação");
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public void setParentHeaderTitle(String title){
		ownerHeader.setText(title);
	}

}
