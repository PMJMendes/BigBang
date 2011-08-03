package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.broker.CostCenterBroker;
import bigBang.definitions.client.brokerClient.CostCenterDataBrokerClient;
import bigBang.definitions.client.types.CostCenter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class CostCenterList extends FilterableList<CostCenter> implements CostCenterDataBrokerClient {

	protected CostCenterBroker costCenterBroker;
	protected int costCenterVersionNumber;
	protected ListHeader header;
	
	public CostCenterList(){
		super();
		((CostCenterBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.COST_CENTER)).registerClient(this);
		
		header = new ListHeader();
		header.setText("Centros de Custo");
		header.showNewButton("Novo");
		header.showRefreshButton();
		setHeaderWidget(header);
		onSizeChanged();
	}

	@Override
	protected void onSizeChanged() {
		super.onSizeChanged();
		this.setFooterText(this.size() + " Centros de Custo");
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equals(BigBangConstants.EntityIds.COST_CENTER))
			this.costCenterVersionNumber = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equals(BigBangConstants.EntityIds.COST_CENTER))
			return costCenterVersionNumber;
		throw new RuntimeException("The data element with id is not supported by the broker client : " + dataElementId);
	}

	@Override
	public void setCostCenters(CostCenter[] costCenters) {
		clear();
		for(int i = 0; i < costCenters.length; i++){
			addCostCenter(costCenters[i]);
		}
	}

	@Override
	public void addCostCenter(CostCenter costCenter) {
		add(new CostCenterListEntry(costCenter));
	}

	@Override
	public void updateCostCenter(CostCenter costCenter) {
		for(ValueSelectable<CostCenter> vs : this) {
			if(vs.getValue().id.equals(costCenter.id)){
				vs.setValue(costCenter);
				break;
			}
		}
	}

	@Override
	public void removeCostCenter(String costCenterId) {
		for(ValueSelectable<CostCenter> vs : this) {
			if(vs.getValue().id.equals(costCenterId)){
				remove(vs);
				break;
			}
		}
	}
	
	/**
	 * Gets a reference to the New button
	 * @return The refrence to the 'new' button
	 */
	public HasClickHandlers getNewButton(){
		return header.getNewButton();
	}
	
	/**
	 * Gets a refrence to the refresh button
	 * @return The reference to the 'refresh' button
	 */
	public HasClickHandlers getRefreshButton(){
		return header.getRefreshButton();
	}
	
}
