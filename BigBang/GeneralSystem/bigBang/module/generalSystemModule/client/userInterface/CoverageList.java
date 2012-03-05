package bigBang.module.generalSystemModule.client.userInterface;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.dataAccess.CoverageDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CoverageList extends FilterableList<Coverage> implements CoverageDataBrokerClient{

	public static class Entry extends ListEntry<Coverage> {
		protected Image editImage;

		public Entry(Coverage coverage){
			super(coverage);
			setLeftWidget(editImage);
		}

		public <I extends Object> void setInfo(I info) {
			Coverage coverage = (Coverage) info;
			setTitle(coverage.name);
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

	CoverageBroker broker;
	private ToolButton newButton;
	private CoverageForm form;
	private PopupPanel popup;
	private boolean readonly;
	private ClickHandler editHandler;
	private DoubleClickHandler doubleClickHandler;

	private BigBangOperationsToolBar toolbar;

	private String parentSubLineId;
	private String parentLineId;
	private String coverageId;
	private ActionInvokedEventHandler<Action> handler;
	private MenuItem delete;
	private String clickedCoverage;

	public CoverageList(){

		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		broker.registerClient(this);
		ListHeader header = new ListHeader();
		header.setText("Coberturas");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.NEW_COVERAGE);
			}
		});

		this.form = new CoverageForm();
		this.popup = new PopupPanel("");
		Widget formContent = form.getNonScrollableContent();
		this.showFilterField(false);
		formContent.setHeight("80px");
		formContent.setWidth("650px");

		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT_COVERAGE);

			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE_COVERAGE);

			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL_EDIT_COVERAGE);

			}

		};

		toolbar.hideAll();



		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				fireAction(Action.DELETE_COVERAGE);

			}
		});

		toolbar.addItem(SUB_MENU.ADMIN, delete);
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);

		VerticalPanel popupWrapper = new VerticalPanel();
		popupWrapper.add(toolbar);
		popupWrapper.add(formContent);

		popup.add(popupWrapper);
		popup.setWidth("650px");

		setHeaderWidget(header);

		editHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<Coverage> e: entries){
					if(event.getSource() == ((Entry)e).editImage){
						clickedCoverage = ((Entry)e).getValue().id;
						fireAction(Action.DOUBLE_CLICK_COVERAGE);
						break;
					}
				}
				
			}
		};
		doubleClickHandler = new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				clickedCoverage = ((Entry)CoverageList.this.getSelected().toArray()[0]).getValue().id;
				fireAction(Action.DOUBLE_CLICK_COVERAGE);
			}
		};

		setDoubleClickable(true);

		setReadOnly(true);
	}

	public String getClickedCoverage(){
		return clickedCoverage;
	}
	
	protected void fireAction(Action action){
		if(this.handler != null) {
			handler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	public void setLineId(String parentLineId){
		this.parentLineId = parentLineId;
	}

	public boolean add(Entry e) {
		e.setEditable(!readonly);
		e.editImage.addClickHandler(this.editHandler);
		e.setDoubleClickable(true);
		e.addHandler(doubleClickHandler, DoubleClickEvent.getType());
		return super.add(e);
	}

	public HasClickHandlers getNewButton(){
		return this.newButton;
	}

	public void showForm(boolean show) {
		if(!show){
			popup.hidePopup();
			return;
		}
		popup.center();
	}

	private void updateEntry(Coverage coverage) {
		for(ListEntry<Coverage> e : entries){
			if(e.getValue().id.equals(coverage.id))
				e.setValue(coverage);
		}
	}

	public void setReadOnly(boolean readonly) {

		this.readonly = readonly;
		//this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<Coverage> e : entries){
			((Entry) e).setEditable(!readonly);
		}
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
		if(parentLineId != null){
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
		clear();
		if(this.coverageId != null){
			for(int i = 0; i<coverages.length; i++){
				add(new Entry(coverages[i]));
				if(this.coverageId.equalsIgnoreCase(coverages[i].id)){
					get(i).setSelected(true, false);
				}
			}
		}
		else{
			for(int i = 0; i<coverages.length; i++){
				add(new Entry(coverages[i]));
			}
		}

	}

	@Override
	public void addCoverage(String parentSubLineId, Coverage coverage) {
		if(this.parentSubLineId.equalsIgnoreCase(parentSubLineId)){
			this.add(new Entry(coverage));
		}

	}

	@Override
	public void updateCoverage(String parentSubLineId, Coverage coverage) {
		if(this.parentSubLineId.equalsIgnoreCase(parentSubLineId)){
			updateEntry(coverage);
		}

	}

	@Override
	public void removeCoverage(String parentSubLineId, String coverageId) {
		if(this.parentSubLineId.equalsIgnoreCase(parentSubLineId)){
			for(int i = 0; i<this.size(); i++){
				if(this.get(i).getValue().id.equalsIgnoreCase(coverageId)){
					this.remove(i);
					return;
				}
			}
		}
	}

	@Override
	public void setTaxes(Tax[] taxes) {
		return;
	}

	@Override
	public void addTax(String parentCoverageId, Tax tax) {
		return;
	}

	@Override
	public void updateTax(String parentCoverageId, Tax tax) {
		return;
	}

	@Override
	public void removeTax(String parentCoverageId, String taxId) {
		return;		
	}

	public void setSubLineId(String subLineId) {
		this.parentSubLineId = subLineId;
	}

	public void setId(String coverageId) {
		this.coverageId = coverageId;
	}

	public void registerActionHandler(
			ActionInvokedEventHandler<bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action> handler) {
		this.handler = handler;

	}

	public CoverageForm getForm() {
		return form;
	}

	public BigBangOperationsToolBar getToolBar() {
		return toolbar;
	}

	public void closePopup() {
		popup.hidePopup();
		
	}
}
