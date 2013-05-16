package bigBang.module.generalSystemModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.dataAccess.CoverageDataBrokerClient;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.module.generalSystemModule.client.userInterface.presenter.TaxManagementOperationViewPresenter.Action;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Image;

public class TaxList extends FilterableList<Tax> implements CoverageDataBrokerClient {

	public static class Entry extends ListEntry <Tax> {
		protected Image editImage;

		public Entry(Tax tax) {
			super(tax);
			setLeftWidget(editImage);
		}

		public <I extends Object> void setInfo(I info) {
			Tax tax = (Tax) info;
			setTitle(tax.name);
		};
		
		public void setEditable(boolean editable) {
			editImage.setVisible(editable);
		}

		@Override
		public void setSelected(boolean selected, boolean fireEvents) {
			if(editImage == null)
				editImage = new Image();
			Resources r = GWT.create(Resources.class);
			super.setSelected(selected, fireEvents);
			editImage.setResource(selected ? r.listEditIconSmallWhite() : r.listEditIconSmallBlack());
		}
	}

	private ToolButton newButton;
	CoverageBroker broker;
	private DoubleClickHandler doubleClickHandler;
	private String parentLineId;
	private String parentSubLineId;
	private String coverageId;
	private String taxId;
	private ActionInvokedEventHandler<Action> handler;
	private boolean readonly;
	private String clickedTax;
	private ClickHandler editHandler;

	public TaxList(){
		
		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		broker.registerClient(this);
		ListHeader header  = new ListHeader();
		this.showFilterField(false);
		header.setText("Campos da cobertura");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
		

		this.editHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<Tax> e: entries){
					if(event.getSource() == ((Entry)e).editImage){
						clickedTax = ((Entry)e).getValue().id;
						fireAction(Action.DOUBLE_CLICK_TAX);
						break;
					}
				}
				
			}
		};
		this.doubleClickHandler = new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				clickedTax = ((Entry)getSelected().toArray()[0]).getValue().id;
				fireAction(Action.DOUBLE_CLICK_TAX);
			}
		};
		
		
		setHeaderWidget(header);
		
		setReadOnly(true);
		
		
		
		this.newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.NEW_TAX);
			}
		});


	}
	public boolean add(Entry e) {
		e.setEditable(!readonly);
		e.editImage.addClickHandler(this.editHandler);
		e.setDoubleClickable(true);
		e.addHandler(doubleClickHandler, DoubleClickEvent.getType());
		return super.add(e);
	}

	public void setReadOnly(boolean readonly) {
		this.readonly = readonly;
		this.newButton.setEnabled(!readonly);
	}
	
	public ToolButton getNewButton(){
		return this.newButton;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {

		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}

	@Override
	public void setLines(Line[] lines) {
		if(this.parentLineId != null){
			for(int i = 0; i<lines.length; i++){
				if(lines[i].id.equalsIgnoreCase(parentLineId)){
					setSubLines(lines[i].subLines);
					break;
				}
			}
		}
	}

	@Override
	public void addLine(Line line) {
		return;
	}

	@Override
	public void updateLine(Line line) {
		return;		
	}

	@Override
	public void removeLine(String lineId) {
		return;
	}

	@Override
	public void setSubLines(SubLine[] subLines) {
		if(this.parentSubLineId != null){
			for(int i = 0; i<subLines.length; i++){
				if(this.parentSubLineId.equalsIgnoreCase(subLines[i].id)){
					setCoverages(subLines[i].coverages);
					break;
				}
			}
		}
	}

	@Override
	public void addSubLine(String parentLineId, SubLine subLine) {
		return;

	}

	@Override
	public void updateSubLine(String parentLineId, SubLine subLine) {
		return;
	}

	@Override
	public void removeSubLine(String parentLineId, String subLineId) {
		return;
	}

	@Override
	public void setCoverages(Coverage[] coverages) {
		if(this.coverageId != null){
			for(int i = 0; i<coverages.length; i++){
				if(this.coverageId.equalsIgnoreCase(coverages[i].id)){
					setTaxes(coverages[i].taxes);
					break;
				}
			}
		}
	}

	@Override
	public void addCoverage(String parentSubLineId, Coverage coverage) {
		return;
	}

	@Override
	public void updateCoverage(String parentSubLineId, Coverage coverage) {
		return;
	}

	@Override
	public void removeCoverage(String parentSubLineId, String coverageId) {
		return;
	}

	@Override
	public void setTaxes(Tax[] taxes) {
		clear(); 
		if(this.taxId != null){
			for(int i = 0; i<taxes.length; i++){
				add(new Entry(taxes[i]));
				if(this.taxId.equalsIgnoreCase(taxes[i].id)){
					get(i).setSelected(true, false);
				}
			}
		}
		else{
			for(int i = 0; i<taxes.length; i++){
				add(new Entry(taxes[i]));
			}
		}
	}

	@Override
	public void addTax(String parentCoverageId, Tax tax) {
		if(this.coverageId.equalsIgnoreCase(parentCoverageId)){
			this.add(new Entry(tax));
		}
	}

	@Override
	public void updateTax(String parentCoverageId, Tax tax) {
		if(this.coverageId.equalsIgnoreCase(parentCoverageId)){
			updateEntry(tax);
		}
	}

	@Override
	public void removeTax(String parentCoverageId, String taxId) {
		if(this.coverageId.equalsIgnoreCase(parentCoverageId)){
			for(int i =0; i<size(); i++){
				if(this.get(i).getValue().id.equalsIgnoreCase(taxId)){
					this.remove(i);
					return;
				}
			}
		}

	}

	private void updateEntry(Tax tax) {
		for(ListEntry<Tax> e : entries){
			if(e.getValue().id.equals(tax.id))
				e.setValue(tax);
		}
	}

	public void setIds(String lineId, String subLineId, String coverageId2,
			String taxId2) {

		this.parentLineId = lineId;
		this.parentSubLineId = subLineId;
		this.coverageId = coverageId2;
		this.taxId = taxId2;

	}
	
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler2){
		this.handler = handler2;		
	}
	
	protected void fireAction(Action action){
		if(this.handler != null) {
			handler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	public java.util.List<ListEntry<Tax>> getEntries() {
		return entries;
	}


	public String getClickedTax() {
		return clickedTax;
	}
	public void setLineId(String lineId) {
		this.parentLineId = lineId;
		
	}
	public void setSubLineId(String subLineId) {
		this.parentSubLineId = subLineId;
		
	}
	public void setCoverageId(String coverageId2) {
		this.coverageId = coverageId2;
	}


}
