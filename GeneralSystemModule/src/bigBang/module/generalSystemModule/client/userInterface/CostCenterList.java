package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.shared.CostCenter;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class CostCenterList extends FilterableList<CostCenter> {

	public HasClickHandlers refreshButton;
	public HasClickHandlers newButton;
	
	public CostCenterList(){
		super();
		ListHeader header = new ListHeader();
		header.setText("Centros de Custo");
		header.showNewButton("Novo");
		header.showRefreshButton();
		refreshButton = header.getRefreshButton();
		newButton = header.getNewButton();
		setHeaderWidget(header);
		onSizeChanged();
	}

	@Override
	protected void onSizeChanged() {
		super.onSizeChanged();
		this.setFooterText(this.size() + " Centros de Custo");
	}
	
}
