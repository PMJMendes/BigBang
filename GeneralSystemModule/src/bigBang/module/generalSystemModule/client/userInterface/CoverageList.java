package bigBang.module.generalSystemModule.client.userInterface;

import java.util.Collection;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.dataAccess.CoverageBroker;
import bigBang.definitions.client.dataAccess.CoverageDataBrokerClient;
import bigBang.definitions.client.dataAccess.DataBroker;
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
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.module.generalSystemModule.client.userInterface.SubLineList.Entry;
import bigBang.module.generalSystemModule.client.userInterface.view.CoverageForm;
import bigBang.module.generalSystemModule.interfaces.CoveragesService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;
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

	private String parentSubLineId;
	private String parentLineId;
	private String coverageId;

	public CoverageList(){
		broker = (CoverageBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.COVERAGE);
		ListHeader header = new ListHeader();
		header.setText("Coberturas");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.clearInfo();
				form.setReadOnly(false);
				showForm(true);
			}
		});

		this.form = new CoverageForm();
		form.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Coverage coverage = form.getValue();
				if(coverage.id == null){
					createCoverage(coverage);
				}else{
					saveCoverage(coverage);
				}
			}
		});
		this.popup = new PopupPanel("Cobertura");
		Widget formContent = form.getNonScrollableContent();
		formContent.setHeight("80px");
		formContent.setWidth("650px");
		popup.add(formContent);
		popup.setWidth("650px");

		setHeaderWidget(header);

		editHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				for(ListEntry<Coverage> e : entries) {
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
				for(ListEntry<Coverage> e : entries) {
					if(event.getSource() == e){
						form.setValue(((Entry)e).getValue());
						showForm(true);
					}
				}
			}
		};

		setDoubleClickable(true);
		showFilterField(false);		
		setReadOnly(true);
	}

	public void setLineId(String parentLineId){
		this.parentLineId = parentLineId;
	}

	public void setCoverages(String parentLineId, String parentSubLineId, final String coverageId) {
		clear();
		this.parentLineId = parentLineId;
		this.parentSubLineId = parentSubLineId;

		broker.getCoverages(parentLineId, parentSubLineId, new ResponseHandler<Coverage[]>() {

			@Override
			public void onResponse(Coverage[] response) {

				if(coverageId != null){
					for(int i = 0; i<response.length; i++){  
						add(new Entry(response[i]));
						if(get(i).getValue().id.equalsIgnoreCase(coverageId)){
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

				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a lista de coberturas."), TYPE.ALERT_NOTIFICATION));

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

	private void createCoverage(Coverage coverage) {

		//TODO
	}

	private void saveCoverage(Coverage coverage){
		//TODO
		coverage.subLineId = this.parentSubLineId;
		CoveragesService.Util.getInstance().saveCoverage(coverage, new BigBangAsyncCallback<Coverage>() {

			@Override
			public void onResponseSuccess(Coverage result) {
				updateEntry(result);
				showForm(false);
			}
		});
	}

	private void updateEntry(Coverage coverage) {
		for(ListEntry<Coverage> e : entries){
			if(e.getValue().id.equals(coverage.id))
				e.setValue(coverage);
		}
	}

	public void setReadOnly(boolean readonly) {

		this.readonly = readonly;
		this.form.setReadOnly(readonly);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCoverage(String parentSubLineId, Coverage coverage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCoverage(String parentSubLineId, String coverageId) {
		// TODO Auto-generated method stub

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
}
