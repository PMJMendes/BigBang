package bigBang.module.quoteRequestModule.client.userInterface.view;


import bigBang.library.client.userInterface.view.ExternalRequestView;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.module.quoteRequestModule.client.userInterface.NegotiationForm;

public class NegotiationExternalRequestView<T> extends ExternalRequestView<T>{

	@SuppressWarnings("unchecked")
	public NegotiationExternalRequestView() {
		super((FormView<T>) new NegotiationForm());
		
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
