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
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.form.SubLineForm;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action;

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

public class SubLineList extends FilterableList<SubLine> implements CoverageDataBrokerClient{

	public static class Entry extends NavigationListEntry<SubLine> {
		protected Image editImage;

		public Entry(SubLine subLine){
			super(subLine);
			setLeftWidget(editImage);
		}

		public <I extends Object> void setInfo(I info) {
			SubLine subLine = (SubLine) info;
			setTitle(subLine.name);
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
	private CoverageBroker broker;
	private ToolButton newButton;
	private SubLineForm form;

	private String clickedSubline;

	private BigBangOperationsToolBar toolbar;

	private PopupPanel popup;
	private boolean readonly;
	private ClickHandler editHandler;
	private DoubleClickHandler doubleClickHandler; 

	private String parentLineId;
	private String subLineId;
	private ActionInvokedEventHandler<Action> handler;
	private MenuItem delete;

	public SubLineList(){

		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		broker.registerClient(this);
		ListHeader header = new ListHeader();
		header.setText("Modalidades");
		header.showNewButton("Novo");
		this.showFilterField(false);
		this.newButton = header.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.NEW_SUB_LINE);
			}
		});

		this.form = new SubLineForm();
		this.popup = new PopupPanel("");

		Widget formContent = form.getNonScrollableContent();
		formContent.setHeight("80px");
		formContent.setWidth("650px");

		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT_SUB_LINE);

			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE_SUB_LINE);

			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL_EDIT_SUB_LINE);

			}

		};

		toolbar.hideAll();
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				fireAction(Action.DELETE_SUB_LINE);

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
				for(ListEntry<SubLine> e: entries){
					if(event.getSource() == ((Entry)e).editImage){
						clickedSubline = ((Entry)e).getValue().id;
						fireAction(Action.DOUBLE_CLICK_SUB_LINE);
						break;
					}
				}
				
			}
		};
		doubleClickHandler = new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				clickedSubline = ((Entry)SubLineList.this.getSelected().toArray()[0]).getValue().id;
				fireAction(Action.DOUBLE_CLICK_SUB_LINE);
			}
		};

		setDoubleClickable(true);

		setReadOnly(true);
	}

	public String getClickedSubLine(){
		return clickedSubline;
	}

	protected void fireAction(Action action){
		if(this.handler != null) {
			handler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
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

	private void updateEntry(SubLine subLine) {
		for(ListEntry<SubLine> e : entries){
			if(e.getValue().id.equals(subLine.id))
				e.setValue(subLine);
		}
	}

	public void setReadOnly(boolean readonly) {

		this.readonly = readonly;
		this.newButton.setEnabled(!readonly);
		for(ListEntry<SubLine> e : entries){
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
		clear();
		for(int i = 0; i<lines.length; i++){
			if(lines[i].id.equalsIgnoreCase(parentLineId)){
				setSubLines(lines[i].subLines);
				break;
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
		clear();
		if(this.subLineId != null){
			for(int i = 0; i<subLines.length; i++){
				add(new Entry(subLines[i]));
				if(this.subLineId.equalsIgnoreCase(subLines[i].id)){
					get(i).setSelected(true);
				}
			}
		}
		else{
			for(int i = 0; i<subLines.length; i++){
				add(new Entry(subLines[i]));
			}
		}

	}

	@Override
	public void addSubLine(String parentLineId, SubLine subLine) {
		if(this.parentLineId.equalsIgnoreCase(parentLineId)){
			this.add(new Entry(subLine));
		}

	}

	@Override
	public void updateSubLine(String parentLineId, SubLine subLine) {
		if(this.parentLineId.equalsIgnoreCase(parentLineId)){
			updateEntry(subLine);
		}
	}

	@Override
	public void removeSubLine(String parentLineId, String subLineId) {
		if(this.parentLineId.equalsIgnoreCase(parentLineId)){
			for(int i = 0; i<this.size(); i++){
				if(this.get(i).getValue().id.equalsIgnoreCase(subLineId)){
					this.remove(i);
					return;
				}
			}
		}

	}

	@Override
	public void setCoverages(Coverage[] coverages) {
		return;
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

	public void setLineId(String lineId) {
		this.parentLineId = lineId;		
	}

	public void setId(String subLineId) {
		this.subLineId = subLineId;
	}

	public void registerActionHandler(
			ActionInvokedEventHandler<bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action> handler) {
		this.handler = handler;

	}

	public void setSublineSelected(String subLineId2) {
		for(int i = 0; i<this.size(); i++){
			if(subLineId2.equalsIgnoreCase(this.get(i).getValue().id)){
				get(i).setSelected(true);
			}else{
				get(i).setSelected(false, false);
			}
		}

	}

	public SubLineForm getForm(){
		return form;
	}

	public BigBangOperationsToolBar getToolBar(){
		return toolbar;
	}

	public void closePopup() {
		popup.hidePopup();
	}
}
