package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.dataAccess.CoverageDataBrokerClient;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.view.SubLineForm;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;
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
			setNavigatable(subLine.coverages != null && subLine.coverages.length > 0);
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
	private PopupPanel popup;
	private boolean readonly;
	private ClickHandler editHandler;
	private DoubleClickHandler doubleClickHandler; 

	private String parentLineId;
	private String subLineId;

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
				form.clearInfo();
				form.setReadOnly(false);
				showForm(true);
			}
		});

		this.form = new SubLineForm();
		form.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SubLine subLine = form.getValue();
				if(subLine.id == null){
					createSubLine(subLine);
				}else{
					saveSubLine(subLine);
				}
			}
		});
		this.popup = new PopupPanel("Modalidade");
		Widget formContent = form.getNonScrollableContent();
		formContent.setHeight("80px");
		formContent.setWidth("650px");
		popup.add(formContent);
		popup.setWidth("650px");

		setHeaderWidget(header);

		editHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<SubLine> e : entries) {
					if(event.getSource() == ((Entry)e).editImage){
						form.setValue(((Entry)e).getValue());
						showForm(true);
					}
				}
			}
		};
		doubleClickHandler = new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				for(ListEntry<SubLine> e : entries) {
					if(event.getSource() == e){
						form.setValue(((Entry)e).getValue());
						showForm(true);
					}
				}
			}
		};

		setDoubleClickable(true);

		setReadOnly(true);
	}

	public void setSubLines(String parentLineId, final String subLineId) {

		broker.getSubLines(parentLineId, new ResponseHandler<SubLine[]>() {

			@Override
			public void onResponse(SubLine[] response) {
				if(subLineId != null){
					for(int i = 0; i<response.length; i++){
						add(new Entry(response[i]));
						if(response[i].id.equalsIgnoreCase(subLineId)){
							get(i).setSelected(true, false);
						}
					}
				}
				else{
					for(int i = 0; i<response.length; i++){
						add(new Entry(response[i]));
					}

				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de modalidades."), TYPE.ALERT_NOTIFICATION));

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

	public HasClickHandlers getNewButton(){
		return this.newButton;
	}

	private void showForm(boolean show) {
		if(!show){
			popup.hidePopup();
			return;
		}
		popup.center();
	}

	private void createSubLine(SubLine subLine) {
		//TODO
		subLine.lineId = this.parentLineId;
	}

	private void saveSubLine(SubLine subLine){
		//TODO
		subLine.lineId = this.parentLineId;
	}

	private void updateEntry(SubLine subLine) {
		for(ListEntry<SubLine> e : entries){
			if(e.getValue().id.equals(subLine.id))
				e.setValue(subLine);
		}
	}

	public void setReadOnly(boolean readonly) {

		this.readonly = readonly;
		this.form.setReadOnly(readonly);
		this.newButton.setEnabled(!readonly);
		for(ListEntry<SubLine> e : entries){
			((Entry) e).setEditable(!readonly);
		}
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLines(Line[] lines) {
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
		if(this.subLineId != null){
			for(int i = 0; i<subLines.length; i++){
				add(new Entry(subLines[i]));
				if(this.subLineId.equalsIgnoreCase(subLines[i].id)){
					get(i).setSelected(true, false);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSubLine(String parentLineId, SubLine subLine) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSubLine(String parentLineId, String subLineId) {
		// TODO Auto-generated method stub

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
}
