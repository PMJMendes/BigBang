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
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter.Action;
import bigBang.module.generalSystemModule.client.userInterface.view.LineForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
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

public class LineList extends FilterableList<Line> implements CoverageDataBrokerClient{

	public static class Entry extends NavigationListEntry<Line> {
		protected Image editImage;

		public Entry(Line line){
			super(line);
			this.textLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			this.textLabel.getElement().getStyle().setFontSize(14, Unit.PX);

			this.titleLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			setLeftWidget(editImage);
			setHeight("40px");
		}

		public <I extends Object> void setInfo(I info) {
			Line line = (Line) info;
			setTitle(line.categoryName);
			setText(line.name);
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

	private LineForm form;
	private PopupPanel popup;
	private VerticalPanel popupWrapper;
	private BigBangOperationsToolBar toolbar;
	private MenuItem delete;

	private boolean readonly;
	private ClickHandler editHandler;
	private DoubleClickHandler doubleClickHandler;
	private CoverageBroker broker;
	private String lineId;

	ActionInvokedEventHandler<Action> handler;

	protected String clickedLine;

	public LineList(){

		broker = (CoverageBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		broker.registerClient(this);
		ListHeader header = new ListHeader();
		header.setText("Ramos");
		header.showNewButton("Novo");
		this.showFilterField(false);
		this.newButton = header.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.NEW_LINE);
			}
		});

		this.form = new LineForm();
		this.popup = new PopupPanel();

		Widget formContent = form.getNonScrollableContent();
		formContent.setHeight("120px");
		formContent.setWidth("650px");
		toolbar = new BigBangOperationsToolBar(){

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT_LINE);
			}

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE_LINE);
			}

			@Override
			public void onCancelRequest() {

				fireAction(Action.CANCEL_EDIT_LINE);

			}

		};

		toolbar.hideAll();
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				fireAction(Action.DELETE_LINE);
			}
		});
		toolbar.addItem(SUB_MENU.ADMIN, delete);
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);

		popupWrapper = new VerticalPanel();

		popupWrapper.add(toolbar);
		popupWrapper.add(formContent);
		popup.add(popupWrapper);
		popup.setWidth("650px");

		header.showRefreshButton();
		header.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireAction(Action.REFRESH);
			}
		});

		setHeaderWidget(header);

		editHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<Line> e: entries){
					if(event.getSource() == ((Entry)e).editImage){
						clickedLine = ((Entry)e).getValue().id;
						fireAction(Action.DOUBLE_CLICK_LINE);
						break;
					}
				}
				


			}
		};
		doubleClickHandler = new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				clickedLine = ((Entry)LineList.this.getSelected().toArray()[0]).getValue().id;
				fireAction(Action.DOUBLE_CLICK_LINE);
			}
		};

		setDoubleClickable(true);
		setReadOnly(true);
	}

	public String getClickedLine(){
		return clickedLine;
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

	private void updateEntry(Line line) {
		for(ListEntry<Line> e : entries){
			if(e.getValue().id.equals(line.id))
				e.setValue(line);
		}
	}

	public void setReadOnly(boolean readonly) {

		this.readonly = readonly;
		this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<Line> e : entries){
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
		if(this.lineId != null){
			for(int i = 0; i<lines.length; i++){
				add(new Entry(lines[i]));
				if(this.lineId.equalsIgnoreCase(lines[i].id)){
					get(i).setSelected(true);

				}
			}
		}
		else{
			for(int i = 0; i<lines.length; i++){
				add(new Entry(lines[i]));
			}
		}
	}

	@Override
	public void addLine(Line line) {

		this.add(new Entry(line));
		return;


	}

	@Override
	public void updateLine(Line line) {

		updateEntry(line);

	}

	@Override
	public void removeLine(String lineId) {

		for(int i = 0; i<this.size(); i++){
			if(this.get(i).getValue().id.equalsIgnoreCase(lineId)){
				this.remove(i);
				return;
			}
		}

	}

	@Override
	public void setSubLines(SubLine[] subLines) {
		return;

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
	public void setTaxes( Tax[] taxes) {
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

	public void setId(String lineId) {
		this.lineId = lineId;

	}

	public void registerActionHandler(ActionInvokedEventHandler<Action> handler){
		this.handler = handler;		
	}

	public FormView<Line> getForm(){
		return this.form;
	}

	public void closePopup() {
		popup.hidePopup();
	}

	public void setLineSelected(String lineId2) {

		for(int i = 0; i<this.size(); i++){
			if(lineId2.equalsIgnoreCase(this.get(i).getValue().id)){
				get(i).setSelected(true);
			}else{
				get(i).setSelected(false, false);
			}
		}
	}

	public BigBangOperationsToolBar getToolbar(){
		return toolbar;
	}
}
