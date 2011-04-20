package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.Tax;

public class TaxList extends FilterableList<Tax> {

	public static class Entry extends ListEntry <Tax> {
		
		private Label label;
		
		public Entry(Tax tax) {
			super(tax);
			setRightWidget(label);
			setInfo(tax);
		}
		
		public <I extends Object> void setInfo(I info) {
			if(label == null)
				label = new Label();
			Tax tax = (Tax) info;
			setTitle(tax.name);
			label.setText(tax.value + "TODO $");
		};
	}
	
	private HasClickHandlers newButton;
	
	public TaxList(){
		ListHeader header  = new ListHeader();
		header.setText("Impostos e Coeficientes");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
	}
	
	public HasClickHandlers getNewButton(){
		return this.newButton;
	}
	
}
