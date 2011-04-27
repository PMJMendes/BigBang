package bigBang.module.generalSystemModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;

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
	
	private ToolButton newButton;
	
	public TaxList(){
		ListHeader header  = new ListHeader();
		header.setText("Impostos e Coeficientes");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
		setHeaderWidget(header);
	}
	
	public ToolButton getNewButton(){
		return this.newButton;
	}
	
}
