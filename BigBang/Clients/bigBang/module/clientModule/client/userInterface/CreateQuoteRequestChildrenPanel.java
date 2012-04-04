package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestInsuredObjectsList;

import com.google.gwt.user.client.ui.StackPanel;

public class CreateQuoteRequestChildrenPanel extends View {

	protected QuoteRequest owner;

	public QuoteRequestInsuredObjectsList insuredObjectsList;

	public CreateQuoteRequestChildrenPanel(){
		StackPanel wrapper = new StackPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		insuredObjectsList = new QuoteRequestInsuredObjectsList();

		wrapper.add(insuredObjectsList, "Unidades de Risco");
	}

	@Override
	protected void initializeView() {
		return;
	}

	public void setOwner(QuoteRequest owner){
		this.owner = owner;
		String ownerId = owner == null ? null : owner.id;

		insuredObjectsList.setOwner(ownerId);
	}

}
